## Context

Currently, the `ReceiptExtractionPort` is exclusively implemented by `GenerativeAIReceiptExtractionAdapter`, which relies on the Gemini AI API. We need an alternative implementation using OCR to avoid complete dependency on external AI services, and a toggle to switch between these implementations via environment variables.

## Goals / Non-Goals

**Goals:**
- Provide an OCR-based implementation of `ReceiptExtractionPort`.
- Allow toggling between AI and OCR implementations via an environment variable in `.env` (mapped to `application.properties`).

**Non-Goals:**
- Perfecting the OCR text parsing to match AI capabilities (OCR might be less intelligent at structuring data, we just need a functional baseline).
- Removing the AI implementation entirely.

## Decisions

**1. OCR Library: Tess4J (Tesseract)**
- **Rationale**: Tesseract is the most mature open-source OCR engine. `tess4j` provides a straightforward Java wrapper for it. It runs locally, fulfilling the goal of removing external dependencies.
- **Alternatives Considered**: Using a cloud OCR API (e.g., Google Cloud Vision, AWS Textract). Rejected because it still introduces an external dependency, defeating the purpose of a standalone fallback.

**2. Toggle Mechanism: Spring Boot `@ConditionalOnProperty`**
- **Rationale**: The project uses Spring Boot. We can annotate the `GenerativeAIReceiptExtractionAdapter` and the new `OcrReceiptExtractionAdapter` with `@ConditionalOnProperty(name = "expense.extraction.engine", havingValue = "ai")` (and `"ocr"` respectively). This allows Spring to instantiate only the active bean based on `application.properties` which reads from `.env`.
- **Alternatives Considered**: A Factory pattern that instantiates the engine at runtime based on the property. Rejected because Spring's DI and conditional beans handle this more elegantly and cleanly at startup.

## Risks / Trade-offs

- **[Risk]** OCR extraction accuracy is generally lower than LLMs for unstructured receipts. Data parsing (regex/heuristics) will be required to extract specific fields like total amount and date from raw OCR text.
  - **Mitigation**: Implement robust regex patterns for common receipt formats (e.g., date formats, currency formats) in the OCR adapter, but accept that some manual correction by the user might be more frequently needed when using OCR.
- **[Risk]** Tesseract requires native binaries (tesseract engine and language data files) installed on the host/container.
  - **Mitigation**: Update the `Dockerfile` to install `tesseract-ocr` and `tesseract-ocr-por` (Portuguese language pack, assuming local context) packages.
