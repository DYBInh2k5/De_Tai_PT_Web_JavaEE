# Dự án Quản lý Lớp học và Sinh viên (JPA One-to-Many & Many-to-One)

Dự án này là bản demo tìm hiểu về quan hệ One-to-Many và Many-to-One trong JPA/Hibernate sử dụng Spring Boot và SQL Server.

### 📚 Tài liệu hướng dẫn chi tiết:
1.  **[HUONG_DAN_CHI_TIET.md](file:///d:/Downloads/ttjavaee/HUONG_DAN_CHI_TIET.md)**: Tổng quan kỹ thuật và kiến thức JPA.
2.  **[HUONG_DAN_BAO_CAO.md](file:///d:/Downloads/ttjavaee/HUONG_DAN_BAO_CAO.md)**: Cấu trúc và nội dung chi tiết cho file Báo cáo (.docx/.pdf).
3.  **[HUONG_DAN_SLIDE.md](file:///d:/Downloads/ttjavaee/HUONG_DAN_SLIDE.md)**: Gợi ý nội dung cho từng Slide thuyết trình (.pptx).
4.  **[HUONG_DAN_NOP_BAI.md](file:///d:/Downloads/ttjavaee/HUONG_DAN_NOP_BAI.md)**: Checklist các file cần nộp và cách đóng gói bài làm.

## Công nghệ sử dụng
- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- Thymeleaf (Giao diện)
- Bootstrap 5 (CSS)
- SQL Server (Cơ sở dữ liệu)
- Lombok

## Cấu trúc quan hệ
- **Classroom (Một)**: Một lớp có thể có nhiều sinh viên (`@OneToMany`).
- **Student (Nhiều)**: Nhiều sinh viên thuộc về một lớp (`@ManyToOne`).

## Hướng dẫn cài đặt và chạy
1. **Cơ sở dữ liệu**:
   - Mở SQL Server Management Studio (SSMS).
   - Chạy file `init-db.sql` để tạo database `ClassroomDB` và dữ liệu mẫu.
   
2. **Cấu hình**:
   - Mở file `src/main/resources/application.properties`.
   - Cập nhật `spring.datasource.password` theo mật khẩu tài khoản `sa` của bạn.

3. **Chạy ứng dụng**:
   - Sử dụng IDE (IntelliJ IDEA, Eclipse, VS Code) để chạy class `ClassroomManagementApplication`.
   - Hoặc dùng terminal: `mvn spring-boot:run`.

4. **Truy cập**:
   - Mở trình duyệt và truy cập: `http://localhost:8080/`.

## Các chức năng chính
- Xem danh sách lớp học và số lượng sinh viên mỗi lớp.
- Thêm, sửa, xóa lớp học.
- Xem danh sách sinh viên theo từng lớp.
- Quản lý sinh viên (CRUD).
- Lọc sinh viên theo lớp.
- Gán lớp cho sinh viên khi thêm mới hoặc chỉnh sửa.
