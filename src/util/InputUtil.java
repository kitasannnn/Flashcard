package util;

import java.util.Scanner;

public final class InputUtil {
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputUtil() {
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    public static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Không được để trống.");
        }
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }
}
