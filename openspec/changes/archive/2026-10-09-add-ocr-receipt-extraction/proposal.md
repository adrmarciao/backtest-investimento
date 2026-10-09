## Why

We currently rely solely on the Gemini AI API for extracting data from receipts, which introduces an external dependency and latency. By creating an alternative Optical Character Recognition (OCR) implementation, we can avoid relying solely on the AI feature and offer a faster, independent option.

## What Changes

- Create an alternative OCR-based implementation for extracting text from receipt images.
- Add an environment variable configuration in `.env` to allow toggling between the AI and OCR approaches.
- Update the system to use the configured implementation based on the toggle.

## Capabilities

### New Capabilities

*(None)*

### Modified Capabilities

- `expense-tracking`: Introduce an alternative OCR text extraction mechanism and an environment variable to toggle between AI-based and OCR-based implementations.

## Impact

- `expense-tracking` module
- `.env` configuration
- Existing AI-based extraction flow remains intact but becomes configurable.
