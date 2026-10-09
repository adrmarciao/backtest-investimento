## Why

The user wants to track expenses automatically by capturing data from purchase receipts. Currently, there is no way to automatically ingest and categorize receipt data. Building this now will provide a foundation for an automated expense control system, starting with a robust backend and a temporary mobile-web bridge until a native app is developed.

## What Changes

- Create a new Java Spring Boot microservice dedicated to expense tracking.
- Implement an AI-based receipt extraction engine (Generative AI Vision) using a Hexagonal Architecture (ports & adapters) to allow swapping with a local OCR later.
- Set up a MongoDB database for storing extracted receipt data (due to its flexible document structure).
- Add a new "Controle de Gastos" tab in the existing Frontend Web application.
- Implement a temporary QR Code flow on the Web Frontend that opens a simple mobile-friendly web page where the user can take a photo of the receipt and submit it to the backend via HTTP POST.

## Capabilities

### New Capabilities
- `expense-tracking`: Core expense tracking system, handling receipt image ingestion, OCR/AI data extraction, and expense storage.

### Modified Capabilities
- 

## Impact

- Adds a new independent Spring Boot microservice to the project infrastructure.
- Introduces MongoDB as a new data store dependency for this service.
- Requires integration with external Generative AI APIs (e.g., Google Gemini or OpenAI).
- Expands the frontend UI with a new menu and a dedicated mobile-friendly upload page.
