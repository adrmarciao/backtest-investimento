## Why

Currently, the system allows users to capture and record expenses from receipts, and has a requirement to list them. However, users need full control over their expense data to correct any mistakes made during extraction or to remove invalid entries. Creating an interactive expense management screen to list, edit, and delete recorded expenses will provide this necessary control and improve the overall usability of the expense tracking feature.

## What Changes

- Add an interactive screen to list all recorded expenses.
- Add functionality to edit the details of an existing expense (e.g., date, total amount, store name).
- Add functionality to delete an expense record.
- Enhance the backend API to support updating and deleting expenses.

## Capabilities

### New Capabilities

- `expense-tracking/expense-management-screen`: Provides the interactive user interface to list, edit, and delete expenses.

### Modified Capabilities

- `expense-tracking`: Enhance the expense management requirements to include the ability to edit and delete existing expense records, in addition to listing them.

## Impact

- **Frontend**: New UI components for the expense list, edit form, and delete confirmation dialog in the `frontend` application.
- **Backend API**: The `expense-tracking` service needs new endpoints for updating (`PUT` / `PATCH`) and deleting (`DELETE`) expenses.
- **Database**: Ensure the repository handles update and delete operations gracefully.
