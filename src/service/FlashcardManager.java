package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import model.Flashcard;
import util.AppConfig;

public class FlashcardManager {
    private final Queue<Flashcard> queue;

    public FlashcardManager() {
        this.queue = new LinkedList<>();
    }

    public void addFlashcard(Flashcard flashcard) {
        if (flashcard != null) {
            queue.offer(flashcard);
        }
    }

    public Flashcard pollFlashcard() {
        return queue.poll();
    }

    public Flashcard peekFlashcard() {
        return queue.peek();
    }

    public boolean processFrontCardResult(boolean isCorrect) {
        Flashcard front = queue.poll();
        if (front == null) {
            return false;
        }

        if (isCorrect) {
            front.setLevel(front.getLevel() + 1);
        } else {
            front.setLevel(1);
            front.setFailCount(front.getFailCount() + 1);
        }

        queue.offer(front);
        return true;
    }

    public boolean removeFrontFlashcard() {
        return queue.poll() != null;
    }

    public boolean moveFrontToBack() {
        Flashcard front = queue.poll();
        if (front == null) {
            return false;
        }
        queue.offer(front);
        return true;
    }

    public int size() {
        return queue.size();
    }

    public List<Flashcard> getAllFlashcards() {
        return new ArrayList<>(queue);
    }

    public double getAverageAccuracyPercent() {
        if (queue.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Flashcard card : queue) {
            int success = Math.max(card.getLevel() - 1, 0);
            int fail = Math.max(card.getFailCount(), 0);
            int total = success + fail;
            double accuracy = total == 0 ? 100.0 : (success * 100.0 / total);
            sum += accuracy;
        }
        return sum / queue.size();
    }

    public List<Flashcard> getTopHardestCards(int limit) {
        List<Flashcard> list = getAllFlashcards();
        list.sort(Comparator.comparingInt(Flashcard::getFailCount).reversed());
        int safeLimit = Math.max(limit, 0);
        if (safeLimit >= list.size()) {
            return list;
        }
        return new ArrayList<>(list.subList(0, safeLimit));
    }

    public void clear() {
        queue.clear();
    }

    public void loadFromFile() {
        queue.clear();
        File file = new File(AppConfig.DATA_FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.toLowerCase().startsWith("id,word,meaning")) {
                    continue;
                }

                List<String> parts = parseCsvLine(line);
                if (parts.size() >= 3) {
                    String id = unescapeCsv(parts.get(0).trim());
                    String word = unescapeCsv(parts.get(1).trim());
                    String meaning = unescapeCsv(parts.get(2).trim());

                    int level = 1;
                    int failCount = 0;
                    if (parts.size() >= 4) {
                        level = parseSafeInt(unescapeCsv(parts.get(3).trim()), 1);
                    }
                    if (parts.size() >= 5) {
                        failCount = parseSafeInt(unescapeCsv(parts.get(4).trim()), 0);
                    }

                    queue.offer(new Flashcard(id, word, meaning, level, failCount));
                }
            }
        } catch (IOException e) {
            System.out.println("Không thể đọc file CSV: " + e.getMessage());
        }
    }

    public void saveToCSV() {
        File file = new File(AppConfig.DATA_FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,word,meaning,level,failCount");
            writer.newLine();
            for (Flashcard flashcard : queue) {
                writer.write(
                    escapeCsv(flashcard.getId()) + "," +
                    escapeCsv(flashcard.getWord()) + "," +
                    escapeCsv(flashcard.getMeaning()) + "," +
                    flashcard.getLevel() + "," +
                    flashcard.getFailCount()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Không thể ghi file CSV: " + e.getMessage());
        }
    }

    public void saveToFile() {
        saveToCSV();
    }

    public void printAll() {
        if (queue.isEmpty()) {
            System.out.println("Danh sách flashcard rỗng.");
            return;
        }

        for (Flashcard flashcard : queue) {
            System.out.println(
                flashcard.getId() + " | " +
                flashcard.getWord() + " | " +
                flashcard.getMeaning() + " | " +
                "level=" + flashcard.getLevel() + " | " +
                "fail=" + flashcard.getFailCount()
            );
        }
    }

    private int parseSafeInt(String raw, int defaultValue) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.replace("\"", "\"\"");
        if (normalized.contains(",") || normalized.contains("\"") || normalized.contains("\n")) {
            return "\"" + normalized + "\"";
        }
        return normalized;
    }

    private String unescapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed.replace("\"\"", "\"");
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        fields.add(current.toString());
        return fields;
    }
}
