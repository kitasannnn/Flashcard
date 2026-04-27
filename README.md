# 📚 Flashcard Learning App (DSA Queue Project)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Status-In--Progress-orange?style=for-the-badge)

Ứng dụng học từ vựng thông minh qua Terminal, sử dụng cấu trúc dữ liệu **Queue (Hàng đợi)** và thuật toán **Lặp lại ngắt quãng (Spaced Repetition System)**. Đây là đồ án môn Cấu trúc dữ liệu và Giải thuật (DSA).

## 🚀 Tính năng nổi bật
- **Spaced Repetition (SRS):** Tự động điều chỉnh tần suất xuất hiện của từ vựng dựa trên kết quả học tập (level & failCount).
- **Robust CSV Parser:** Xử lý dữ liệu bền vững, hỗ trợ các chuỗi từ vựng phức tạp chứa dấu phẩy hoặc ngoặc kép.
- **CLI UX/UI:** Giao diện Terminal trực quan với mã màu ANSI và thanh tiến trình (Progress Bar).
- **Statistics Dashboard:** Thống kê tỷ lệ chính xác và danh sách "Top 5 từ khó nhất".

## 📅 Lộ trình phát triển (Project Roadmap)

### Tuần 1: Nghiên cứu & Thiết kế
- Phân tích cấu trúc dữ liệu Queue (LinkedList) với cơ chế FIFO làm luồng học tập cốt lõi.
- Thiết kế kiến trúc Layered Architecture: `model`, `service`, `ui`, và `util`.

### Tuần 2: Setup hạ tầng & File I/O
- Cấu hình môi trường Java và hệ thống quản lý mã nguồn Git.
- Hoàn thiện module xử lý File CSV, hỗ trợ tương thích ngược dữ liệu 3 cột và 5 cột.

### Tuần 3: Core Logic (Queue & Model)
- Triển khai class `Flashcard` và lõi `FlashcardManager`.
- Hoàn thiện các thao tác Queue: `offer`, `poll`, và thuật toán xoay vòng thẻ khi trả lời sai.

### Tuần 4: Giao diện Console & UX
- Xây dựng hệ thống Menu tương tác 5 chức năng.
- Tích hợp màu sắc hiển thị và Progress Bar để theo dõi tiến độ học tập.

### Tuần 5: Thuật toán SRS
- Tích hợp logic Spaced Repetition: Tăng level khi đúng, reset level và đẩy về cuối hàng đợi khi sai.