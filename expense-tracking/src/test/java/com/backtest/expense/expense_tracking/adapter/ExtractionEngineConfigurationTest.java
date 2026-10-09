package com.backtest.expense.expense_tracking.adapter;

import com.backtest.expense.expense_tracking.port.ReceiptExtractionPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractionEngineConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(
                    GenerativeAIReceiptExtractionAdapter.class,
                    OcrReceiptExtractionAdapter.class
            )
            // Mocking dependencies needed by GenerativeAIReceiptExtractionAdapter
            .withBean("apiKey", String.class, () -> "fake-key")
            .withBean("model", String.class, () -> "gemini-test")
            .withBean("thinkingBudget", Integer.class, () -> 0)
            .withBean(org.springframework.web.reactive.function.client.WebClient.Builder.class, () -> org.springframework.web.reactive.function.client.WebClient.builder())
            .withBean(com.fasterxml.jackson.databind.ObjectMapper.class, () -> new com.fasterxml.jackson.databind.ObjectMapper());

    @Test
    public void testAiEngineLoadedByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ReceiptExtractionPort.class);
            assertThat(context).getBean(ReceiptExtractionPort.class)
                    .isInstanceOf(GenerativeAIReceiptExtractionAdapter.class);
        });
    }

    @Test
    public void testAiEngineLoadedWhenPropertySetToAi() {
        contextRunner.withPropertyValues("expense.extraction.engine=ai").run(context -> {
            assertThat(context).hasSingleBean(ReceiptExtractionPort.class);
            assertThat(context).getBean(ReceiptExtractionPort.class)
                    .isInstanceOf(GenerativeAIReceiptExtractionAdapter.class);
        });
    }

    @Test
    public void testOcrEngineLoadedWhenPropertySetToOcr() {
        contextRunner.withPropertyValues("expense.extraction.engine=ocr").run(context -> {
            assertThat(context).hasSingleBean(ReceiptExtractionPort.class);
            assertThat(context).getBean(ReceiptExtractionPort.class)
                    .isInstanceOf(OcrReceiptExtractionAdapter.class);
        });
    }
}
