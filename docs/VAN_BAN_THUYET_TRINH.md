# Văn Bản Thuyết Trình — 8 slide (10 phút)

## Slide 1: Giới Thiệu Đề Tài & Mục Tiêu (30 giây)

Kính chào quý thầy cô và các bạn!

Tôi là **Võ Duy Bình**, nhóm trưởng nhóm đề tài 07. Thành viên gồm **Nguyễn Vũ Minh Huy** và **Trần Bá Lợi**, dưới sự hướng dẫn của **thầy Phan Hồng Trung**, học kỳ 2 năm học 2024-2025, Trường Đại Học Hoa Sen.

Đề tài của chúng tôi: **Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học.**

Bài toán demo: Xây dựng ứng dụng Web quản lý lớp học và sinh viên với Spring Boot, JPA/Hibernate, SQL Server và Bootstrap.

Bốn mục tiêu chính:
1. Hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate
2. Thành thạo các annotation @OneToMany, @ManyToOne, @JoinColumn, mappedBy, cascade
3. Xây dựng ứng dụng quản lý lớp học với Spring Boot và SQL Server
4. Hoàn thành CRUD với giao diện Bootstrap

---

## Slide 2: Công Nghệ Chính — JPA/Hibernate (1.5 phút)

**Vấn đề:** Khi lập trình Java kết nối database, chúng ta gặp sự không tương thích giữa mô hình quan hệ (bảng SQL) và mô hình đối tượng (Java class). Đây gọi là **Object-Relational Impedance Mismatch**. Ví dụ: bảng Students có class_id là khóa ngoại, nhưng trong Java chúng ta muốn có `Student.getClassroom()` trả về đối tượng Classroom thay vì một con số.

**JPA/Hibernate giải quyết thế nào?** JPA là specification, Hibernate là implementation. Nó ánh xạ bảng thành Java Entity class qua annotation. Lập trình viên làm việc với object, không cần viết SQL thủ công.

**Khác biệt so với cách làm thông thường:**
- Thông thường: Viết JDBC, tạo Connection, PreparedStatement, ResultSet, tự map từng cột
- Với JPA: Chỉ cần `@Entity`, `@OneToMany`, `@ManyToOne`, gọi `repository.findAll()`

**Vị trí trong hệ thống:** Tầng Persistence — Service gọi Repository, Repository dùng Hibernate để truy vấn SQL Server.

**Công nghệ bổ trợ:** Spring Boot (Backend), Thymeleaf (View), Bootstrap (UI), Lombok (giảm boilerplate).

---

## Slide 3: Phân Tích Chức Năng & Thiết Kế Hệ Thống (1.5 phút)

### Chức năng chính
Ứng dụng có 5 chức năng: CRUD lớp học, CRUD sinh viên, gán sinh viên vào lớp, lọc sinh viên theo lớp, xem số lượng sinh viên từng lớp.

### Kiến trúc MVC
Ứng dụng tuân thủ mô hình MVC:

- **Controller:** `ClassroomController`, `StudentController` — nhận HTTP request, gọi Service
- **Service:** `ClassroomService`, `StudentService` — xử lý logic nghiệp vụ
- **Repository:** `ClassroomRepository`, `StudentRepository` — truy vấn database qua JPA
- **Entity:** `Classroom`, `Student` — ánh xạ bảng Classes, Students
- **View:** Thymeleaf templates — hiển thị HTML + Bootstrap

### Thiết kế cơ sở dữ liệu

**Bảng Classes:** id (PK, IDENTITY), class_code (NVARCHAR, UNIQUE), class_name (NVARCHAR)

**Bảng Students:** id (PK, IDENTITY), student_code (NVARCHAR, UNIQUE), full_name (NVARCHAR), email (NVARCHAR), class_id (BIGINT, FK → Classes.id, ON DELETE SET NULL)

Khi xóa một lớp, class_id của sinh viên được set NULL thay vì xóa sinh viên — giữ lại dữ liệu, tránh mất thông tin.

---

## Slide 4: Cài Đặt Kỹ Thuật Quan Trọng (1.5 phút)

### Luồng MVC + Thymeleaf (bắt buộc)
Lấy chức năng **xem danh sách lớp học** làm ví dụ:

1. Trình duyệt gõ `http://localhost:8080/classes`
2. `ClassroomController.findAll()` nhận request
3. Controller gọi `ClassroomService.getAllClassrooms()`
4. Service gọi `ClassroomRepository.findAll()` — Hibernate sinh SQL: `SELECT * FROM Classes`
5. Repository trả về `List<Classroom>`
6. Controller đưa vào Model: `model.addAttribute("classrooms", list)`
7. Controller trả về tên view `"class-list"`
8. Thymeleaf đọc `class-list.html`, dùng `th:each="c : ${classrooms}"` để lặp, `th:text="${c.className}"` để hiển thị tên lớp
9. Trình duyệt nhận HTML hoàn chỉnh

### Entity Mapping (2 annotation chính)
```java
// Classroom.java
@OneToMany(mappedBy = "classroom", cascade = {PERSIST, MERGE})
private List<Student> students;

// Student.java
@ManyToOne
@JoinColumn(name = "class_id")
private Classroom classroom;
```

**Student** là owning side (có `@JoinColumn` — quản lý khóa ngoại). **Classroom** là inverse side (có `mappedBy` — chỉ phản chiếu). Cascade PERSIST + MERGE giúp đồng bộ dữ liệu khi lưu hoặc cập nhật.

### Cấu hình database
```
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB
spring.jpa.hibernate.ddl-auto=update
```

---

## Slide 5: Demo Chương Trình (3 phút)

Chạy live ứng dụng tại `http://localhost:8080`:

1. **Danh sách lớp học** — thấy 2 lớp, mỗi lớp có badge số SV
2. **Thêm lớp mới** — nhập mã + tên, lưu, quay lại danh sách thấy lớp mới
3. **Danh sách sinh viên** — tên lớp hiển thị trong cột Lớp học
4. **Thêm sinh viên** — chọn lớp từ dropdown, lưu, kiểm tra
5. **Lọc sinh viên theo lớp** — chọn lớp từ dropdown, chỉ hiển thị SV lớp đó

Chứng minh SQL Server: Mở SSMS, chạy `SELECT * FROM Students WHERE class_id = 1` thấy dữ liệu khớp.

---

## Slide 6: Kết Quả & Khó Khăn (2 phút)

**Đã hoàn thành:**
- CRUD lớp học, CRUD sinh viên, gán SV vào lớp, lọc, đếm số SV
- Ứng dụng chạy ổn định, giao diện Bootstrap responsive
- Kết nối SQL Server thực tế qua Hibernate/JPA

**Khó khăn:**
- Cấu hình SQL Server Authentication (mixed mode, bật TCP/IP)
- Hiểu owning side và cách Hibernate cập nhật khóa ngoại

**Hạn chế và hướng khắc phục:**
- Chưa có validation nâng cao → thêm `@Valid` + `BindingResult`
- Chưa có phân trang → thêm `Pageable`
- Chưa có tìm kiếm → thêm `@Query` + form search

---

## Slide 7: Kết Luận & Hướng Phát Triển

**Kết luận:** Đề tài giúp hiểu rõ cách JPA/Hibernate ánh xạ quan hệ One-to-Many và Many-to-One giữa bảng Classes và Students sang Java Entity. Nắm vững các annotation `@OneToMany`, `@ManyToOne`, `@JoinColumn`, `mappedBy`, `cascade`. Xây dựng được ứng dụng Spring Boot hoàn chỉnh kết nối SQL Server.

**Hướng phát triển:**
1. Spring Validation (`@Valid`) kiểm tra dữ liệu đầu vào
2. Phân trang với Spring Data Pageable
3. Tìm kiếm sinh viên theo tên
4. Spring Security đăng nhập / phân quyền

---

## Slide 8: Cảm Ơn & Câu Hỏi

Cảm ơn quý thầy cô và các bạn đã lắng nghe!

Rất mong nhận được ý kiến đóng góp và câu hỏi từ các bạn.

---

*Tổng thời lượng: 10 phút (30s + 1.5p + 1.5p + 1.5p + 3p + 2p).*
