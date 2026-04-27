# Session Log - Flashcard Queue Project

## 1) Project Setup & Structure

### Created folders
- src/model
- src/service
- src/ui
- src/util
- data
- .vscode

### Created core files
- src/model/Flashcard.java
- src/service/FlashcardManager.java
- src/ui/Main.java
- src/util/AppConfig.java
- src/util/InputUtil.java
- src/util/ColorUtil.java
- data/vocab.csv
- .vscode/settings.json

### Workspace configuration
- Added java.project.sourcePaths = ["src"] in .vscode/settings.json to fix package/source-root mismatch in VS Code.

## 2) Flashcard Queue Logic Implemented

### Base model
- Flashcard initially had:
  - id
  - word
  - meaning

### Queue manager (LinkedList-based)
- FlashcardManager uses Queue<Flashcard> backed by LinkedList.
- Implemented queue operations:
  - addFlashcard(...): enqueue at tail
  - peekFlashcard(): view head
  - pollFlashcard(): dequeue head
  - removeFrontFlashcard(): remove head (boolean)
  - moveFrontToBack(): rotate head to tail (boolean)

### Interactive CLI menu in Main
- Upgraded from a static demo to loop-based interactive menu.
- Current menu options:
  1. Học từ (SRS)
  2. Thêm từ
  3. Xem danh sách
  4. Thoát
  5. Xem thống kê

### Input validation
- Added InputUtil with safe input methods:
  - readInt(prompt): handles NumberFormatException and retries
  - readNonEmptyString(prompt): disallows empty input
  - readString(prompt): raw trimmed string input

## 3) SRS (Spaced Repetition) Upgrade

### Flashcard model extension
- Added fields:
  - level (int)
  - failCount (int)
- Added constructor with full SRS fields and safe normalization:
  - level minimum = 1
  - failCount minimum = 0

### SRS result processing
- Added processFrontCardResult(isCorrect) in FlashcardManager:
  - If correct:
    - level = level + 1
  - If wrong:
    - level = 1
    - failCount = failCount + 1
  - Card is pushed back to tail to continue cyclic review.

## 4) CLI UX Enhancements

### ANSI color utility
- Added ColorUtil with constants:
  - RED
  - GREEN
  - YELLOW
  - CYAN
  - RESET

### Colorized CLI behavior
- Yellow: menu items
- Green: correct answer feedback
- Red: wrong answer feedback
- Cyan: informational messages (study state, save/exit, etc.)

### Progress bar in study flow
- Added simple progress bar rendering in Main:
  - Format similar to: [====>-----] current/max
  - Driven by card level (clamped against max threshold)

## 5) Statistics Dashboard (Menu 5)

Implemented in Main + FlashcardManager:
- Total words in storage: manager.size()
- Average accuracy (%): manager.getAverageAccuracyPercent()
- Top 5 hardest words: manager.getTopHardestCards(5)
  - Sorted by failCount descending
  - Display includes word, failCount, level

## 6) CSV File Handling (Read/Write) - Full Detail

### Required file location
- CSV data path is centralized in AppConfig:
  - data/vocab.csv

### Write logic
- Method name aligned with requirement:
  - saveToCSV()
- Backward compatibility:
  - saveToFile() now delegates to saveToCSV()
- Header upgraded to:
  - id,word,meaning,level,failCount
- Persists all card fields including SRS metrics.

### Read logic
- loadFromFile() supports both formats:
  - Legacy 3-column rows: id,word,meaning
  - New 5-column rows: id,word,meaning,level,failCount

### Robust CSV parsing improvement
- Replaced simple split(",") with a custom parser that correctly handles:
  - Quoted fields
  - Commas inside quoted text
  - Escaped quotes using doubled quotes
- Added unescape logic for quoted text.
- This prevents corrupted parsing when meanings contain commas or quotes.

### Error handling
- File read/write uses try-catch and reports IOException messages.
- Numeric parse for level/failCount uses safe fallback defaults.

## 7) Vietnamese Text Normalization

- Updated user-facing CLI strings to Vietnamese with full diacritics across:
  - Main
  - InputUtil
  - FlashcardManager messages
- Keeps the app output clearer and more professional for Vietnamese users.

## 8) Validation & Stability Checks

- Repeatedly checked diagnostics for src after major edits.
- Result at end of session: no source errors reported by workspace diagnostics.

## 9) Current Project State (End of Session)

The project is now a complete Java SE Flashcard Queue application with:
- Clean package structure
- Interactive CLI menu
- Queue-based study cycle
- SRS progression logic (level/failCount)
- Colorized UX + progress bar
- Statistics dashboard
- Backward-compatible and robust CSV persistence
- Vietnamese UI text with proper accents
