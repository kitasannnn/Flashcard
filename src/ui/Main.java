package ui;

import java.util.List;
import model.Flashcard;
import service.FlashcardManager;
import util.ColorUtil;
import util.InputUtil;

public class Main {
    public static void main(String[] args) {
        FlashcardManager manager = new FlashcardManager();
        manager.loadFromFile();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputUtil.readInt("Chọn chức năng (1-5): ");

            switch (choice) {
                case 1 -> studyFlashcard(manager);
                case 2 -> addFlashcard(manager);
                case 3 -> manager.printAll();
                case 4 -> {
                    manager.saveToCSV();
                    System.out.println(ColorUtil.CYAN + "Đã lưu dữ liệu. Tạm biệt!" + ColorUtil.RESET);
                    running = false;
                }
                case 5 -> showStatistics(manager);
                default -> System.out.println(ColorUtil.RED + "Lựa chọn không hợp lệ. Vui lòng chọn 1-5." + ColorUtil.RESET);
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n" + ColorUtil.YELLOW + "===== FLASHCARD QUEUE MENU =====" + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + "1. Học từ (SRS)" + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + "2. Thêm từ" + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + "3. Xem danh sách" + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + "4. Thoát" + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + "5. Xem thống kê" + ColorUtil.RESET);
    }

    private static void studyFlashcard(FlashcardManager manager) {
        Flashcard current = manager.peekFlashcard();
        if (current == null) {
            System.out.println(ColorUtil.CYAN + "Queue đang rỗng, chưa có từ để học." + ColorUtil.RESET);
            return;
        }

        System.out.println("\n" + ColorUtil.CYAN + "Đang ôn tập..." + ColorUtil.RESET);
        System.out.println("Từ: " + current.getWord());
        System.out.println("Cấp độ hiện tại: " + current.getLevel());
        System.out.println("Tiến độ: " + buildProgressBar(current.getLevel(), 10));

        String answer = InputUtil.readNonEmptyString("Nhập nghĩa của từ: ");
        boolean isCorrect = answer.equalsIgnoreCase(current.getMeaning().trim());

        manager.processFrontCardResult(isCorrect);
        manager.saveToCSV();

        if (isCorrect) {
            System.out.println(ColorUtil.GREEN + "Chính xác! Level +1." + ColorUtil.RESET);
        } else {
            System.out.println(ColorUtil.RED + "Sai rồi! Level reset về 1 và tăng failCount." + ColorUtil.RESET);
            System.out.println(ColorUtil.RED + "Đáp án đúng: " + current.getMeaning() + ColorUtil.RESET);
        }
    }

    private static void addFlashcard(FlashcardManager manager) {
        String id = InputUtil.readNonEmptyString("Nhập id: ");
        String word = InputUtil.readNonEmptyString("Nhập từ vựng: ");
        String meaning = InputUtil.readNonEmptyString("Nhập nghĩa: ");

        manager.addFlashcard(new Flashcard(id, word, meaning));
        manager.saveToCSV();
        System.out.println(ColorUtil.CYAN + "Đã thêm flashcard vào cuối Queue." + ColorUtil.RESET);
    }

    private static void showStatistics(FlashcardManager manager) {
        int total = manager.size();
        double averageAccuracy = manager.getAverageAccuracyPercent();
        List<Flashcard> hardest = manager.getTopHardestCards(5);

        System.out.println("\n" + ColorUtil.CYAN + "===== THỐNG KÊ =====" + ColorUtil.RESET);
        System.out.println("Tổng số từ trong kho: " + total);
        System.out.printf("Tỷ lệ chính xác trung bình: %.2f%%%n", averageAccuracy);
        System.out.println("Top 5 từ khó nhất (theo failCount):");

        if (hardest.isEmpty()) {
            System.out.println("(Danh sách rỗng)");
            return;
        }

        int rank = 1;
        for (Flashcard card : hardest) {
            System.out.println(
                rank + ". " +
                card.getWord() +
                " | fail=" + card.getFailCount() +
                " | level=" + card.getLevel()
            );
            rank++;
        }
    }

    private static String buildProgressBar(int value, int max) {
        int safeMax = Math.max(max, 1);
        int clamped = Math.max(0, Math.min(value, safeMax));
        int filled = (int) Math.round((clamped * 1.0 / safeMax) * 10);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < filled) {
                bar.append("=");
            } else if (i == filled && filled < 10) {
                bar.append(">");
            } else {
                bar.append("-");
            }
        }
        bar.append("] ").append(clamped).append("/").append(safeMax);
        return bar.toString();
    }
}
