## 1. Infrastructure and Configuration

- [x] 1.1 Add `tess4j` dependency to `expense-tracking/pom.xml` and verify `mvn clean install` succeeds.
- [x] 1.2 Update the `Dockerfile` to install `tesseract-ocr` and `tesseract-ocr-por` packages and verify `docker-compose build` succeeds.
- [x] 1.3 Add `expense.extraction.engine` property to `application.properties` (mapped from `.env`) and verify the application context loads correctly.

## 2. Adapter Refactoring

- [x] 2.1 Add `@ConditionalOnProperty(name = "expense.extraction.engine", havingValue = "ai", matchIfMissing = true)` to `GenerativeAIReceiptExtractionAdapter` and verify application starts up properly with AI active.
- [x] 2.2 Create a new `OcrReceiptExtractionAdapter` class implementing `ReceiptExtractionPort` with `@ConditionalOnProperty(name = "expense.extraction.engine", havingValue = "ocr")`.

## 3. OCR Implementation

- [x] 3.1 Implement base text extraction using `ITesseract.doOCR` in `OcrReceiptExtractionAdapter` and verify it extracts raw text from a sample receipt image.
- [x] 3.2 Implement regex or heuristic logic to parse the raw text for common fields (Store name, Date, Total Amount) and verify parsing works correctly in a unit test.
- [x] 3.3 Add unit tests verifying that both AI and OCR beans are loaded conditionally depending on the environment property.
