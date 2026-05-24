# HƯỚNG DẪN LÀM SLIDE THUYẾT TRÌNH (FILE .PPTX)

Slide nên súc tích, nhiều hình ảnh minh họa và ít chữ. Dưới đây là cấu trúc 10 slide đề nghị:

---

### Slide 1: Tiêu đề
- Tên đề tài: Tìm hiểu quan hệ One-to-Many & Many-to-One trong JPA.
- Thành viên nhóm và Giảng viên hướng dẫn.

### Slide 2: Mục tiêu đề tài
- Hiểu và áp dụng quan hệ thực thể trong JPA.
- Xây dựng ứng dụng quản lý Lớp học - Sinh viên.

### Slide 3: Công nghệ sử dụng
- Backend: Spring Boot, Spring Data JPA.
- Database: SQL Server.
- Frontend: Thymeleaf, Bootstrap 5.

### Slide 4: Tổng quan Quan hệ One-to-Many
- Khái niệm: 1 Lớp -> n Sinh viên.
- Mapping trong JPA: `@OneToMany(mappedBy = "...")`.

### Slide 5: Tổng quan Quan hệ Many-to-One
- Khái niệm: n Sinh viên -> 1 Lớp.
- Mapping trong JPA: `@ManyToOne`, `@JoinColumn`.

### Slide 6: Thiết kế Cơ sở dữ liệu
- Chụp ảnh sơ đồ quan hệ (ERD) hoặc bảng trong SQL Server.
- Nhấn mạnh khóa ngoại `class_id`.

### Slide 7: Kiến trúc chương trình
- Mô hình MVC (Model - View - Controller).
- Luồng dữ liệu: Browser -> Controller -> Service -> Repository -> Database.

### Slide 8: Các chức năng Demo
- CRUD Lớp học.
- CRUD Sinh viên.
- Thống kê số lượng sinh viên theo lớp.

### Slide 9: Kết quả đạt được & Khó khăn
- Đã nắm vững cách mapping quan hệ.
- Khó khăn: Cấu hình kết nối SQL Server, xử lý lỗi vòng lặp vô tận (nếu có).

### Slide 10: Kết luận & Cảm ơn
- Tóm tắt lại ý nghĩa đề tài.
- Lời cảm ơn và mời đặt câu hỏi.
