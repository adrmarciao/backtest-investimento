## Context

See proposal.md for motivation - we are introducing an expense tracking system based on receipt image processing. The primary constraints are the need to start with a cloud-based Generative AI for OCR/extraction while preserving the ability to swap to a local OCR later, and the lack of a native mobile app necessitating a web-based mobile upload bridge.

## Goals / Non-Goals

**Goals:**
- Implement the expense tracking backend as a standalone Spring Boot microservice.
- Use Hexagonal Architecture (Ports and Adapters) for the extraction engine to decouple the business logic from the specific OCR/AI implementation.
- Use MongoDB to store the extracted expense documents.
- Provide a secure, temporary QR-code-to-web-upload flow to bridge the web frontend and mobile device cameras.

**Non-Goals:**
- Developing the native Android application (deferred to a future phase).
- Implementing traditional OCR (Tesseract) immediately (we will start with Generative AI Vision APIs).

## Decisions

### 1. Hexagonal Architecture for Receipt Extraction
- **Rationale**: The user intends to use a Generative AI API (e.g., Gemini or OpenAI) for initial rapid development but wants the option to migrate to a local OCR solution later. We will define a `ReceiptExtractionPort` interface in the core domain, and implement a `GenerativeAIReceiptExtractionAdapter`.
- **Alternatives Considered**: Direct tight coupling to an AI SDK. Rejected because it violates the future migration requirement.

### 2. Database: MongoDB
- **Rationale**: Receipts contain hierarchical data (store, metadata, list of items with varying attributes). A NoSQL document database like MongoDB is perfectly suited for storing this flexible, JSON-like structure without complex relational mapping.
- **Alternatives Considered**: PostgreSQL (current stack default). Rejected because mapping varying receipt item structures into relational tables adds unnecessary complexity.

### 3. QR Code Mobile Upload Bridge
- **Rationale**: To allow users to use their phone cameras without a native app, the web frontend will generate a QR code containing a URL to a simple HTML page hosted by the backend. This page will use `<input type="file" accept="image/*" capture="environment">` to open the native camera and upload the image via HTTP POST.
- **Alternatives Considered**: Forcing the user to transfer photos to their PC manually. Rejected as it creates excessive friction.

## Risks / Trade-offs

- **[Risk] Generative AI Hallucination or Parsing Failures**
  - **Mitigation**: Use structured output modes (JSON schema enforcement) available in modern AI APIs and validate the extracted data structure before persisting it.
- **[Risk] Unauthorized access to the mobile upload endpoint**
  - **Mitigation**: The URL generated in the QR code should include a short-lived, single-use signed token to prevent abuse of the upload endpoint.
- **[Risk] Leaking API keys in source code**
  - **Mitigation**: The AI API key will be securely stored in a local `.env` file and read via application properties.
