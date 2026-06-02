# HƯỚNG DẪN VIẾT BÁO CÁO

Báo cáo cần đủ 8 chương theo cấu trúc đề bài yêu cầu.
Nội dung chi tiết đã có sẵn trong file `BAO_CAO_CHI_TIET.md`.

---

## Cấu trúc báo cáo

### 1. Trang bìa
- Tên trường / khoa
- Tên môn học: Phát triển ứng dụng Web Java EE
- Tên đề tài: Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học
- Danh sách thành viên nhóm (STT, Họ tên, MSSV)
- Giảng viên hướng dẫn
- Học kỳ / Năm học

### 2. Giới thiệu đề tài
- Lý do chọn đề tài: vấn đề Object-Relational Impedance Mismatch trong Java
- Mục tiêu: xây dựng ứng dụng demo minh họa quan hệ @OneToMany / @ManyToOne

### 3. Cơ sở lý thuyết
- ORM là gì, JPA là gì, Hibernate là gì
- Quan hệ One-to-Many và Many-to-One
- Khóa chính, khóa ngoại
- Giải thích các annotation: @Entity, @Table, @OneToMany, @ManyToOne, @JoinColumn
- mappedBy và cascade ở mức cơ bản

### 4. Phân tích và thiết kế hệ thống
- Mô tả chức năng: CRUD lớp, CRUD sinh viên, gán SV vào lớp, lọc theo lớp, đếm SV
- Sơ đồ luồng: thêm sinh viên, xóa lớp
- Thiết kế database: ERD, mô tả bảng Classes và Students
- Kiến trúc MVC (Controller → Service → Repository → Entity → DB)

### 5. Cài đặt chương trình
- Cấu trúc project (cây thư mục)
- Cấu hình application.properties
- Mô tả Entity: Classroom.java (@OneToMany), Student.java (@ManyToOne)
- Mô tả Repository, Service, Controller
- Mô tả các template HTML chính

### 6. Kết quả demo
- **Chụp ảnh màn hình** từng chức năng và dán vào đây:
  - Danh sách lớp học (có cột Số SV)
  - Form thêm/sửa lớp
  - Danh sách sinh viên (có cột Lớp học)
  - Form thêm sinh viên (dropdown chọn lớp)
  - Lọc sinh viên theo lớp
  - Xem sinh viên theo lớp
- Mô tả cách chạy chương trình (tham khảo README.md)

### 7. Đánh giá và kết luận
- Kết quả đạt được (bảng đối chiếu yêu cầu)
- Hạn chế: chưa có validation nâng cao, chưa phân trang
- Hướng phát triển: thêm tìm kiếm, thêm phân trang, thêm Spring Security

### 8. Tài liệu tham khảo
1. Spring Boot Documentation — https://docs.spring.io/spring-boot/
2. Spring Data JPA Reference — https://docs.spring.io/spring-data/jpa/
3. Hibernate ORM Documentation — https://hibernate.org/orm/documentation/
4. Baeldung — Hibernate One-to-Many — https://www.baeldung.com/hibernate-one-to-many
5. Baeldung — JPA Many-to-One — https://www.baeldung.com/jpa-many-to-one
6. Thymeleaf Documentation — https://www.thymeleaf.org/documentation.html
7. Bootstrap 5 — https://getbootstrap.com/docs/5.3/

---

## Lưu ý khi làm báo cáo Word

- Font chữ: Times New Roman 13pt
- Căn lề: trên 3cm, dưới 2.5cm, trái 3.5cm, phải 2cm
- Đánh số trang từ trang nội dung
- Code trong báo cáo dùng font Courier New hoặc Consolas
- **Bắt buộc có ảnh chụp màn hình** ở Chương 6

## File báo cáo đã có sẵn

Nội dung chi tiết đầy đủ: `docs/BAO_CAO_CHI_TIET.md`
Copy vào Word, thêm ảnh chụp màn hình, định dạng là xong.