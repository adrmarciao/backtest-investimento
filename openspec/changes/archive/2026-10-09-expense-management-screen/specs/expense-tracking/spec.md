## ADDED Requirements

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
