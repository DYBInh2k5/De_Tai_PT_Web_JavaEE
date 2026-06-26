# Văn Bản Thuyết Trình — Vừa Chiếu Slide vừa Demo Live

---

## Slide 1: Trang bìa

**Nói:**
Xin chào thầy cô và các bạn. Nhóm em xin trình bày đề tài: "Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học".

Nhóm gồm: Võ Duy Bình, Nguyễn Vũ Minh Huy, Trần Bá Lợi — dưới sự hướng dẫn của thầy Phan Hồng Trung.

---

## Slide 2: Mục tiêu

**Nói:**
Bốn mục tiêu chính: hiểu quan hệ One-to-Many và Many-to-One, thành thạo annotation, xây dựng ứng dụng Spring Boot, hoàn thành CRUD với Bootstrap.

**Thao tác:** Chuyển slide nhanh, không dừng lâu.

---

## Slide 3: Giới thiệu & Lý do

**Nói:**
Vấn đề cốt lõi là Object-Relational Impedance Mismatch — sự khác biệt giữa bảng SQL và object Java. JPA/Hibernate giải quyết điều này bằng cách ánh xạ bảng thành Entity. Đây cũng là công nghệ chính của đề tài.

---

## Slide 4: Công nghệ

**Nói:**
Spring Boot, JPA/Hibernate, SQL Server, Thymeleaf, Bootstrap, Lombok.

**Thao tác:** Lướt qua bảng công nghệ, chuyển nhanh.

---

## Slide 5: Tổng quan quan hệ One-to-Many / Many-to-One

**Nói:**
Đây là nội dung trọng tâm. Quan hệ giữa Classroom và Student — một lớp có nhiều sinh viên, mỗi sinh viên thuộc một lớp.

**Hành động — MỞ CODE:**
- Mở `entity/Classroom.java` — chỉ vào dòng 55: `@OneToMany(mappedBy = "classroom")`
- Mở `entity/Student.java` — chỉ vào dòng 59-61: `@ManyToOne` + `@JoinColumn(name = "class_id")`

**Nói tiếp:**
Student là owning side — giữ khóa ngoại `class_id`. Classroom là inverse side — chỉ phản chiếu qua `mappedBy`.

---

## Slide 6: Thiết kế CSDL

**Nói:**
Hai bảng: Classes (id, class_code, class_name) và Students (id, student_code, full_name, email, class_id FK). ON DELETE SET NULL — xóa lớp thì class_id của SV được set NULL, không mất dữ liệu SV.

**Hành động — MỞ SSMS:**
- `SELECT * FROM Classes`
- `SELECT * FROM Students`

---

## Slide 7: Kiến trúc MVC

**Nói:**
Ứng dụng tuân thủ MVC 3 tầng: Controller → Service → Repository → DB. Thymeleaf làm View.

**Hành động — MỞ CODE để chạy luồng:**
- Mở `controller/ClassroomController.java` — chỉ dòng 38-44: `GET /classes` → `model.addAttribute("classes", ...)` → `return "class-list"`
- Mở `templates/class-list.html` — chỉ dòng 44: `th:each="classroom : ${classes}"` và dòng 53: badge `classroom.students.size()`

**Nói:**
Đây là luồng từ URL `/classes` → Controller đưa list vào Model → Thymeleaf render HTML qua `th:each` và `th:text`.

---

## Slide 8: Mapping JPA

**Nói:**
Hai annotation quyết định quan hệ: `@OneToMany(mappedBy)` ở Classroom và `@ManyToOne` + `@JoinColumn` ở Student.

**Hành động — QUAY LẠI CODE:**
- Vẫn là 2 file entity ở Slide 5 — giải thích thêm cascade và fetch
- `cascade = {PERSIST, MERGE}`: lưu Classroom thì Student tự đồng bộ
- Không dùng REMOVE vì DB đã có `ON DELETE SET NULL`
- `fetch = LAZY`: chỉ tải danh sách SV khi gọi `.getStudents()`

---

## Slide 9: Demo — BẮT ĐẦU CHẠY

**Tổng quan:** 5 chức năng chính. Mở trình duyệt, mở SSMS song song.

### Demo 1: Danh sách lớp
- Vào `http://localhost:8080/classes`
- **Nói:** Thấy 2 lớp, mỗi lớp có badge số SV (dùng `classroom.students.size()`)

### Demo 2: Thêm lớp mới
- `/classes/add` → nhập mã CNTT02, tên "Công nghệ thông tin 02" → Lưu
- **Nói:** Quay lại danh sách thấy lớp mới. Đây là POST `/classes/save` → `ClassroomController.saveClass()`

### Demo 3: Danh sách sinh viên
- `/students` — cột "Lớp học" hiển thị tên lớp (không phải ID)
- **Hành động — MỞ CODE:** `templates/student-list.html` — chỉ `student.classroom.className`

### Demo 4: Thêm SV + chọn lớp
- `/students/add` — nhập thông tin, chọn lớp từ dropdown → Lưu
- **Hành động — MỞ CODE:** `templates/student-form.html` dòng 67-73 — dropdown `th:each="c : ${classes}"` và `th:selected`

### Demo 5: Lọc SV theo lớp
- `/students?classId=1` — chỉ hiển thị SV của lớp 1
- **Hành động — MỞ CODE:** `repository/StudentRepository.java` dòng 24 — `findByClassroomId()` — Spring Data JPA tự sinh `WHERE class_id = ?`

### Chứng minh SQL Server
- **Chuyển sang SSMS:** `SELECT * FROM Students WHERE class_id = 1` — dữ liệu khớp

---

## Slide 10: Kết quả & Khó khăn

**Nói:**
Đã hoàn thành 5 chức năng. Khó khăn khi cấu hình SQL Server Authentication và hiểu owning side. Hạn chế: thiếu validation, phân trang, tìm kiếm.

---

## Slide 11: Kết luận & Hướng phát triển

**Nói:**
Nắm vững @OneToMany, @ManyToOne, @JoinColumn, mappedBy, cascade. Hướng phát triển: thêm @Valid, Pageable, tìm kiếm, Spring Security.

---

## Slide 12: Cảm ơn

**Nói:**
Cảm ơn thầy cô và các bạn đã lắng nghe. Rất mong nhận được câu hỏi góp ý.
