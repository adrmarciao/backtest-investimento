## Purpose

This capability allows users to automatically track their expenses by extracting and storing structured data directly from purchase receipt images.

## ADDED Requirements

### Requirement: Extract expense data from receipt image
The system SHALL accept a receipt image, use AI/OCR to extract relevant purchase data (such as store name, date, total amount, and items), and store this information as an expense record.

#### Scenario: Valid receipt image is processed
- **WHEN** the user uploads a valid receipt image via the API
- **THEN** the system successfully extracts the purchase details and saves a new expense record in the database

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
