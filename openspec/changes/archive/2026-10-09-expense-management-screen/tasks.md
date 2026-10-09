## 1. Backend Implementation

- [x] 1.1 Add an `updateExpense` method in `ExpenseService` and expose it via a `PUT` endpoint in the controller, then write a test verifying it updates the record.
- [x] 1.2 Add a `deleteExpense` method in `ExpenseService` and expose it via a `DELETE` endpoint in the controller, then write a test verifying it removes the record.

## 2. Frontend API Client

- [x] 2.1 Update the frontend API client (or service) to include methods for calling the new backend `PUT` and `DELETE` expense endpoints, and verify they trigger network requests properly.

## 3. Frontend UI Components

- [x] 3.1 Create an `ExpenseList` component that fetches and displays the list of recorded expenses, and verify it renders the data returned by the API.
- [x] 3.2 Create an `ExpenseEditModal` (or inline form) component that populates with the selected expense's data, submits the update via the API client, and triggers a list refresh, then verify it correctly saves changes.
- [x] 3.3 Add delete functionality with a confirmation dialog to the list items, and verify it successfully calls the API delete endpoint and removes the item from the local list view.
- [x] 3.4 Integrate the `ExpenseList` into the "Controle de Gastos" tab/screen, ensuring proper layout and routing.
