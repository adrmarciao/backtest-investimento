## Context

Currently, the backend `ExpenseService` has some API endpoints or repositories but may need `PUT/PATCH` and `DELETE` endpoints for managing the expense entries in the MongoDB collection. The frontend does not currently have a dedicated view to list, edit, or delete expenses with interactive forms.

## Goals / Non-Goals

**Goals:**
- Provide a responsive and user-friendly interface to list all recorded expenses.
- Implement RESTful endpoints in the backend to update and delete expense records.
- Wire the frontend to communicate with these new backend endpoints.

**Non-Goals:**
- Do not implement complex pagination or filtering in this initial iteration; simply list the expenses.
- Do not change the existing receipt extraction process.

## Decisions

- **Decision 1: Use `PUT` vs `PATCH` for updates:**
  - **Rationale:** We will use `PUT` to update the whole expense record at once, which maps well to a simple edit form on the frontend where all fields (date, store name, total amount) are present and submitted together.
  - **Alternatives considered:** Using `PATCH` for partial updates, but it adds complexity and the current data model is simple enough to handle full replacements of editable fields.
- **Decision 2: Frontend Data State Management:**
  - **Rationale:** Use local state or a simple fetching hook (like SWR or React Query, or equivalent standard mechanisms in the project) to fetch the list and invalidate/refetch it when an edit or delete occurs.
  - **Alternatives considered:** Complex global state management (like Redux), which is overkill for this simple list.

## Risks / Trade-offs

- **Risk:** Deleting an expense cannot be undone.
  - **Mitigation:** The frontend will show a confirmation dialog before completing a delete action to prevent accidental data loss.
