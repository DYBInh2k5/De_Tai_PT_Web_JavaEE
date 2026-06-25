# Văn Bản Thuyết Trình — Đề Tài 07

## Slide 1: Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate

Kính chào quý thầy cô và các bạn!

Tôi là **Võ Duy Bình**, nhóm trưởng của nhóm thực hiện bài tập lớn này. Cùng với các thành viên **Nguyễn Vũ Minh Huy** và **Trần Bá Lợi**, chúng tôi đã hoàn thành đề tài: *"Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học"*.

Bài tập này được thực hiện dưới sự hướng dẫn của **thầy Phan Hồng Trung**, trong học kỳ 2 năm học 2024-2025, tại **Trường Đại Học Hoa Sen, Khoa Công Nghệ**.

---

## Slide 2: Mục Tiêu Đề Tài

Nhóm đã đặt ra **bốn mục tiêu chính** để đảm bảo bài tập lớn vừa có chiều sâu lý thuyết, vừa có giá trị thực hành:

**Thứ nhất:** Hiểu rõ quan hệ thực thể — nắm vững quan hệ One-to-Many và Many-to-One trong JPA/Hibernate, biết cách chúng hoạt động trong thực tế.

**Thứ hai:** Thành thạo các annotation — sử dụng chính xác các annotation như `@OneToMany`, `@ManyToOne`, `@JoinColumn`, `mappedBy`, và `cascade` để ánh xạ quan hệ.

**Thứ ba:** Xây dựng ứng dụng hoàn chỉnh — phát triển ứng dụng quản lý lớp học và sinh viên với Spring Boot kết nối SQL Server.

**Thứ tư:** Thực hiện CRUD đầy đủ — hoàn thành các chức năng Create, Read, Update, Delete với giao diện Bootstrap responsive.

---

## Slide 3: Giới Thiệu & Lý Do Chọn Đề Tài

Trong phát triển ứng dụng Web hiện đại, quản lý dữ liệu có quan hệ là yêu cầu cốt lõi. Tuy nhiên, lập trình viên Java thường gặp khó khăn khi "dịch" mô hình quan hệ của cơ sở dữ liệu sang mô hình đối tượng của Java — đây chính là bài toán **Object-Relational Impedance Mismatch**.

**Tại sao cần học JPA/Hibernate?**

- Thứ nhất, JPA/Hibernate giải quyết sự không tương thích giữa mô hình quan hệ (SQL) và mô hình đối tượng (Java).
- Thứ hai, nó cho phép lập trình viên làm việc với dữ liệu hoàn toàn bằng ngôn ngữ hướng đối tượng, mà không cần viết SQL thủ công.
- Thứ ba, nó giảm boilerplate code, tăng năng suất phát triển.

**Ứng dụng thực tế:**
- Quản lý nhân sự: Phòng ban ↔ Nhân viên
- Hệ thống bán hàng: Khách hàng ↔ Đơn hàng
- Quản lý giáo dục: **Lớp học ↔ Sinh viên** ← Đề tài này

---

## Slide 4: Công Nghệ Sử Dụng

Ứng dụng của chúng tôi được xây dựng trên nền tảng công nghệ hiện đại:

| Công nghệ | Vai trò |
|-----------|---------|
| Spring Boot 3.2.5 | Backend framework chính |
| Spring Data JPA | ORM để tương tác với database |
| Hibernate | JPA implementation |
| SQL Server 2019 | Cơ sở dữ liệu quan hệ |
| Thymeleaf | Template engine để tạo View |
| Bootstrap 5 | Giao diện Web responsive |
| Lombok | Giảm boilerplate Java code |

---

## Slide 5: Tổng Quan Quan Hệ One-to-Many / Many-to-One

Mô hình quan hệ giữa hai thực thể **Classroom** (Lớp học) và **Student** (Sinh viên) là nền tảng của toàn bộ ứng dụng.

**Phía "Một" — Classroom:**
Một lớp học có thể chứa nhiều sinh viên. Annotation `@OneToMany` được đặt ở đây với thuộc tính `mappedBy`. Lớp này có khóa chính `id`, các trường `classCode` và `className`, và một danh sách `List<Student>`.

**Phía "Nhiều" — Student:**
Mỗi sinh viên chỉ thuộc về một lớp học. Annotation `@ManyToOne` được đặt ở đây cùng `@JoinColumn`. Lớp này có khóa chính `id`, các trường `studentCode`, `fullName`, `email`, và khóa ngoại `class_id` trỏ đến `Classes(id)`.

**Owning Side vs Inverse Side:**
- **Owning Side:** Student (chứa `@JoinColumn`) — bên chứa khóa ngoại, quản lý quan hệ trong database
- **Inverse Side:** Classroom (chứa `mappedBy`) — bên chỉ đọc dữ liệu, không quản lý FK

---

## Slide 6: Thiết Kế Cơ Sở Dữ Liệu

Hệ thống sử dụng hai bảng quan hệ với ràng buộc toàn vẹn tham chiếu.

**Bảng Classes:**

| Cột | Kiểu | Ràng buộc |
|-----|------|-----------|
| id | BIGINT | Khóa chính, tự tăng |
| class_code | NVARCHAR(50) | Mã lớp, duy nhất, không null |
| class_name | NVARCHAR(255) | Tên lớp, không null |

**Bảng Students:**

| Cột | Kiểu | Ràng buộc |
|-----|------|-----------|
| id | BIGINT | Khóa chính, tự tăng |
| student_code | NVARCHAR(50) | Mã sinh viên, duy nhất, không null |
| full_name | NVARCHAR(255) | Họ tên, không null |
| email | NVARCHAR(255) | Email, không null |
| class_id | BIGINT | Khóa ngoại trỏ đến `Classes(id)`, `ON DELETE SET NULL` |

Khi một lớp bị xóa, các sinh viên của lớp đó sẽ có `class_id` được set thành **NULL**, thay vì bị xóa theo.

---

## Slide 7: Kiến Trúc Chương Trình (MVC)

Ứng dụng tuân thủ mô hình **MVC (Model-View-Controller)** với sự phân tầng rõ ràng:

**Tầng 1 — Giao diện Người Dùng:**
Trình duyệt tương tác với HTML/Bootstrap được tạo bởi Thymeleaf.

**Tầng 2 — Controller & Service:**
`ClassroomController` và `StudentController` xử lý HTTP requests, gọi các Service để xử lý logic nghiệp vụ.

**Tầng 3 — Repository & Persistence:**
Hibernate/JPA tương tác với SQL Server (ClassroomDB) để lưu trữ và truy xuất dữ liệu.

Mỗi tầng có trách nhiệm riêng biệt: Controller xử lý HTTP, Service chứa logic nghiệp vụ, Repository truy xuất dữ liệu, và View hiển thị giao diện người dùng.

**Luồng request MVC + Thymeleaf — ví dụ xem danh sách lớp học:**

1. Trình duyệt gõ `http://localhost:8080/classes`
2. `ClassroomController.findAll()` nhận request HTTP GET
3. Controller gọi `ClassroomService.getAllClassrooms()` — tầng Service xử lý nghiệp vụ
4. Service gọi `ClassroomRepository.findAll()` — Repository tạo truy vấn SQL qua JPA
5. Hibernate sinh câu lệnh: `SELECT * FROM Classes` và trả về `List<Classroom>`
6. Controller đưa dữ liệu vào **Model**: `model.addAttribute("classrooms", list)`
7. Controller trả về tên View: `"class-list"` — Thymeleaf tìm file `class-list.html`
8. Thymeleaf đọc `class-list.html`, dùng `th:each="c : ${classrooms}"` để lặp danh sách và `th:text="${c.className}"` để hiển thị tên lớp
9. Trình duyệt nhận HTML hoàn chỉnh và render giao diện

---

## Slide 8: Mapping JPA — Annotation Chi Tiết

Đây là phần quan trọng nhất — cách ánh xạ quan hệ bằng annotation.

**Phía "Một" — Classroom.java:**
```java
@OneToMany(mappedBy = "classroom",
            cascade = {PERSIST, MERGE},
            fetch = LAZY)
private List<Student> students;
```

**Phía "Nhiều" — Student.java:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "class_id")
private Classroom classroom;
```

**Giải thích chi tiết:**

- `mappedBy = "classroom"` — Chỉ định Student là owning side. Hibernate không tạo thêm cột FK ở bảng Classes.
- `@JoinColumn(name = "class_id")` — Ánh xạ cột khóa ngoại `class_id` trong bảng Students trỏ đến `Classes(id)`.
- `cascade = {PERSIST, MERGE}` — Không dùng REMOVE vì database dùng ON DELETE SET NULL. Nếu dùng REMOVE, sẽ xung đột với hành vi xóa của database.
- `fetch = LAZY` — Tải dữ liệu khi cần thiết, tối ưu hiệu suất.

---

## Slide 9: Demo Các Chức Năng Ứng Dụng

Ứng dụng của chúng tôi cung cấp **5 chức năng chính:**

1. **CRUD Lớp Học:**
   Người dùng có thể thêm, sửa, xóa và xem danh sách lớp học.

2. **CRUD Sinh Viên:**
   Người dùng có thể thêm, sửa, xóa và xem danh sách sinh viên.

3. **Gán Sinh Viên Vào Lớp:**
   Khi thêm hoặc sửa sinh viên, người dùng chọn lớp từ dropdown để liên kết đúng lớp học.

4. **Lọc Sinh Viên Theo Lớp:**
   Dropdown filter hiển thị sinh viên của lớp được chọn, giúp dễ dàng quản lý.

5. **Xem Số Lượng Sinh Viên:**
   Badge hiển thị số lượng sinh viên của mỗi lớp, cung cấp cái nhìn tổng quan nhanh.

---

## Slide 10: Kết Quả Đạt Được & Bài Học Kinh Nghiệm

**✅ Kết Quả Đạt Được:**
- Hoàn thành đủ 5 chức năng bắt buộc
- Hiểu và vận dụng được `@OneToMany` / `@ManyToOne`
- Ứng dụng chạy ổn định, có giao diện Bootstrap responsive
- CRUD hoàn chỉnh kết nối SQL Server thực tế
- Nắm vững khái niệm owning side, inverse side, mappedBy, cascade

**⚠️ Khó Khăn / Hạn Chế:**
- Cấu hình SQL Server Authentication và TCP/IP lần đầu gặp khó khăn
- Hiểu đúng owning side và cách Hibernate cập nhật khóa ngoại
- Chưa có validation nâng cao (chỉ dùng HTML required)
- Chưa có phân trang khi dữ liệu lớn
- Chưa có tìm kiếm nâng cao

Nhóm đã hoàn thành mục tiêu đề ra và sẵn sàng tiếp thu ý kiến đóng góp từ thầy/cô và các bạn!

---

## Slide 11: Kết Luận & Hướng Phát Triển

**Kết Luận:**
- Đề tài giúp hiểu rõ cách JPA/Hibernate ánh xạ quan hệ giữa các bảng sang mô hình đối tượng Java
- Nắm vững quan hệ One-to-Many và Many-to-One trong thực tế quản lý lớp học
- Áp dụng được các annotation `@OneToMany`, `@ManyToOne`, `@JoinColumn`, `mappedBy`, `cascade`
- Xây dựng được ứng dụng Web hoàn chỉnh với Spring Boot + SQL Server + Bootstrap

**Hướng Phát Triển:**
- Thêm Spring Validation (`@Valid`) kiểm tra dữ liệu đầu vào
- Thêm phân trang với Spring Data Pageable
- Thêm tìm kiếm sinh viên theo tên
- Thêm Spring Security đăng nhập / phân quyền

---

## Slide 12: Cảm Ơn & Câu Hỏi

Cảm ơn quý thầy cô và các bạn đã lắng nghe!

Rất mong nhận được ý kiến đóng góp và câu hỏi từ các bạn.

---

*Đây là văn bản thuyết trình hoàn chỉnh cho tất cả 12 slide. Bạn có thể điều chỉnh tốc độ, nhấn mạnh, hoặc thêm ví dụ cụ thể khi thuyết trình trực tiếp. Chúc bạn thuyết trình thành công!*
