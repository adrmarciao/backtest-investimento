package com.backtest.expense.expense_tracking.adapter;

import com.backtest.expense.expense_tracking.model.Expense;
import com.backtest.expense.expense_tracking.port.ReceiptExtractionPort;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ConditionalOnProperty(name = "expense.extraction.engine", havingValue = "ocr")
public class OcrReceiptExtractionAdapter implements ReceiptExtractionPort {

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(?i)(?:total|valor).*?(?:R\\$)?\\s*(\\d+[.,]\\d{2})");

    @Override
    public Expense extractExpenseFromReceipt(byte[] imageBytes, String contentType) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                throw new RuntimeException("Could not read image from bytes");
            }

            ITesseract tesseract = new Tesseract();
            // Assuming tesseract is installed in standard location or language data is available
            tesseract.setLanguage("por+eng"); // Try to load Portuguese and English
            tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata"); // Common datapath for Ubuntu
            
            // In a real scenario we'd do image preprocessing here (grayscale, thresholding)
            String extractedText = tesseract.doOCR(image);

            return parseTextToExpense(extractedText);

        } catch (IOException | TesseractException e) {
            throw new RuntimeException("Failed to extract text from receipt using OCR", e);
        }
    }

    protected Expense parseTextToExpense(String text) {
        Expense expense = new Expense();
        expense.setItems(new ArrayList<>());
        
        if (text == null || text.isBlank()) {
            return expense;
        }

        // Try to find a date
        Matcher dateMatcher = DATE_PATTERN.matcher(text);
        if (dateMatcher.find()) {
            try {
                String dateStr = dateMatcher.group(0);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                expense.setDate(LocalDate.parse(dateStr, formatter));
            } catch (DateTimeParseException ignored) {
            }
        } else {
            expense.setDate(LocalDate.now()); // Fallback
        }

        // Try to find a total amount
        Matcher amountMatcher = AMOUNT_PATTERN.matcher(text);
        if (amountMatcher.find()) {
            String amountStr = amountMatcher.group(1).replace(",", ".");
            try {
                expense.setTotalAmount(new BigDecimal(amountStr));
            } catch (NumberFormatException ignored) {
            }
        }

        // Use the first non-empty line as the store name as a heuristic
        String[] lines = text.split("\\R");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.length() > 3) {
                expense.setStoreName(trimmed);
                break;
            }
        }
        
        if (expense.getStoreName() == null) {
            expense.setStoreName("Unknown Store (OCR)");
        }

        return expense;
    }
}
