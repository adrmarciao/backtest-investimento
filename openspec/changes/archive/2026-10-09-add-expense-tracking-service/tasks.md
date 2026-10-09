## 1. Setup Backend Module
- [x] 1.1 Create new `expense-tracking` Spring Boot module/package and verify application context starts without errors
- [x] 1.2 Add MongoDB dependencies and configure application properties, verify connection on startup
- [x] 1.3 Add AI API key to `.env` file and configure application properties to read it securely
- [x] 1.4 Update `docker-compose.yml` to include the new `expense-tracking` microservice and the MongoDB database

## 2. Core Backend Implementation
- [x] 2.1 Define `Expense` MongoDB entity and repository, write an integration test verifying save/load
- [x] 2.2 Create the `ReceiptExtractionPort` interface and domain logic to orchestrate extraction and saving, verify with a mock adapter unit test
- [x] 2.3 Implement `GenerativeAIReceiptExtractionAdapter` using chosen AI SDK (Gemini/OpenAI) and write a unit test with mock API responses

## 3. Backend API Endpoints
- [x] 3.1 Implement REST endpoint to receive receipt image and token, verify with Postman or curl returning HTTP 200
- [x] 3.2 Implement REST endpoint to list recorded expenses, verify output matches test data inserted in MongoDB

## 4. Web Frontend Updates
- [x] 4.1 Add "Controle de Gastos" menu and tab displaying the list of expenses, verify UI renders data from the new endpoint
- [x] 4.2 Implement QR Code generation for the mobile upload page URL, verify QR code correctly embeds the secure URL
- [x] 4.3 Create the mobile-friendly web page for uploading images, verify the `<input type="file">` opens the camera/file picker on a mobile browser simulator
- [x] 4.4 Integrate the mobile upload page with the backend API, verify an image uploaded from the browser successfully creates an expense record in the backend
