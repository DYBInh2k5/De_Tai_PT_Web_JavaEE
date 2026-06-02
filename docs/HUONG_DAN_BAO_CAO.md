# HƯỚNG DẪN VIẾT BÁO CÁO (FILE .DOCX / .PDF)

Báo cáo của bạn cần tuân thủ cấu trúc chuyên nghiệp. Dưới đây là gợi ý nội dung chi tiết cho từng chương.

---

## 1. Trang bìa
- Tên Trường / Khoa.
- Tên môn học: Phát triển ứng dụng Web (hoặc tên tương đương).
- **Tên đề tài:** Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học.
- Danh sách thành viên nhóm.
- Giảng viên hướng dẫn.

## 2. Giới thiệu đề tài
- **Lý do chọn đề tài:** Sự quan trọng của việc quản lý dữ liệu quan hệ trong các ứng dụng thực tế.
- **Mục tiêu:** Xây dựng ứng dụng demo hoàn chỉnh sử dụng Spring Boot và JPA.

## 3. Cơ sở lý thuyết
- **Tổng quan JPA/Hibernate:** Định nghĩa, vai trò của ORM.
- **Quan hệ One-to-Many:** Giải thích cách 1 dòng ở bảng này liên kết nhiều dòng ở bảng kia.
- **Quan hệ Many-to-One:** Góc nhìn ngược lại từ phía thực thể con.
- **Các Annotation:** Giải thích `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@OneToMany`, `@ManyToOne`, `@JoinColumn`.

## 4. Phân tích và thiết kế hệ thống
- **Chức năng:** Liệt kê các chức năng CRUD lớp học, CRUD sinh viên, lọc sinh viên theo lớp.
- **Sơ đồ luồng (Flowchart):** Mô tả cách người dùng thêm một sinh viên mới và gán vào lớp.
- **Thiết kế Database:** 
    - Bảng `Classes` (id, class_code, class_name).
    - Bảng `Students` (id, student_code, full_name, email, class_id).

## 5. Cài đặt chương trình
- **Cấu trúc Project:** Chụp ảnh cây thư mục dự án trong IntelliJ.
- **Cấu hình Database:** Mô tả file `application.properties`.
- **Mô tả Code:**
    - Chụp code Entity [Classroom.java](file:///d:/Downloads/ttjavaee/src/main/java/com/ttjavaee/classroom/entity/Classroom.java) và [Student.java](file:///d:/Downloads/ttjavaee/src/main/java/com/ttjavaee/classroom/entity/Student.java).
    - Giải thích vai trò của Repository, Service và Controller.

## 6. Kết quả demo
- Chụp ảnh màn hình giao diện:
    - Danh sách lớp học.
    - Form thêm/sửa lớp học.
    - Danh sách sinh viên (có lọc).
    - Form thêm sinh viên (có dropdown chọn lớp).

## 7. Đánh giá và kết luận
- **Kết quả:** Đã hoàn thành các chức năng bắt buộc; V1.1 bổ sung validation và UX cơ bản.
- **Hạn chế:** Chưa đăng nhập, chưa REST API (Phase 3–5).
- **Đã làm V1.2:** Phân trang, tìm kiếm, Flyway, N+1 — mô tả trong báo cáo mục 6.3.
- **Hướng phát triển:** REST API, giảng viên/môn học/điểm (Phase 3).

## 8. Tài liệu tham khảo
- Spring Boot Documentation.
- Hibernate User Guide.
- Baeldung (JPA/Hibernate tutorials).
