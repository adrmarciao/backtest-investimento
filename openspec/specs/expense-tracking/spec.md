## Purpose

This capability allows users to automatically track their expenses by extracting and storing structured data directly from purchase receipt images.

## Requirements

### Requirement: Extract expense data from receipt image
The system SHALL accept a receipt image, extract relevant purchase data (such as store name, date, total amount, and items) using either an AI-based or OCR-based extraction mechanism depending on the active configuration, and store this information as an expense record. The system MUST provide an environment-level configuration to toggle between AI and OCR implementations.

#### Scenario: AI extraction is enabled and valid receipt is processed
- **WHEN** the extraction configuration is set to AI and the user uploads a valid receipt image via the API
- **THEN** the system successfully extracts the purchase details using the AI engine and saves a new expense record

#### Scenario: OCR extraction is enabled and valid receipt is processed
- **WHEN** the extraction configuration is set to OCR and the user uploads a valid receipt image via the API
- **THEN** the system successfully extracts the purchase details using the OCR engine and saves a new expense record

### Requirement: Generate mobile upload QR code
The system SHALL provide a mechanism to generate a QR code containing a URL to a mobile-friendly page where users can upload receipt images.

#### Scenario: User requests a mobile upload session
- **WHEN** the user initiates a new expense flow in the web application
- **THEN** the system generates and displays a QR code that opens the upload interface on a mobile device

### Requirement: List recorded expenses
The system SHALL provide a view to list all successfully processed and stored expense records.

#### Scenario: User views their expenses
- **WHEN** the user navigates to the "Controle de Gastos" tab
- **THEN** the system retrieves and displays a list of all recorded expenses

### Requirement: Update an expense record
The system SHALL allow updating the details of an existing expense record (e.g., date, total amount, store name).

#### Scenario: Expense is updated successfully
- **WHEN** a valid update request is received for an existing expense
- **THEN** the system updates the corresponding record and persists the changes

### Requirement: Delete an expense record
The system SHALL allow the deletion of an existing expense record.

#### Scenario: Expense is deleted successfully
- **WHEN** a valid delete request is received for an existing expense
- **THEN** the system permanently removes the corresponding record
