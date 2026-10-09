## MODIFIED Requirements

### Requirement: Extract expense data from receipt image
The system SHALL accept a receipt image, extract relevant purchase data (such as store name, date, total amount, and items) using either an AI-based or OCR-based extraction mechanism depending on the active configuration, and store this information as an expense record. The system MUST provide an environment-level configuration to toggle between AI and OCR implementations.

#### Scenario: AI extraction is enabled and valid receipt is processed
- **WHEN** the extraction configuration is set to AI and the user uploads a valid receipt image via the API
- **THEN** the system successfully extracts the purchase details using the AI engine and saves a new expense record

#### Scenario: OCR extraction is enabled and valid receipt is processed
- **WHEN** the extraction configuration is set to OCR and the user uploads a valid receipt image via the API
- **THEN** the system successfully extracts the purchase details using the OCR engine and saves a new expense record
