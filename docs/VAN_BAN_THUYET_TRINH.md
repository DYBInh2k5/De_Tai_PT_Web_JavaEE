# Văn Bản Thuyết Trình — Vừa chiếu slide, vừa chạy demo, vừa chỉ code

> **Tổng thời gian:** 10 phút (7 phút slide + 3 phút demo lồng ghép)
> **Cần mở sẵn:** Trình duyệt (http://localhost:8080), IntelliJ (project), SSMS (ClassroomDB)

---

## Slide 1 — Trang bìa (30s)

**WINDOW: SLIDE**

Xin chào thầy cô và các bạn. Nhóm em xin trình bày đề tài: "Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học".

Nhóm gồm: Võ Duy Bình, Nguyễn Vũ Minh Huy, Trần Bá Lợi — GVHD thầy Phan Hồng Trung, Học kỳ 2 năm học 2024-2025.

---

## Slide 2 — Mục tiêu (30s)

**WINDOW: SLIDE**

Bốn mục tiêu: hiểu quan hệ One-to-Many/Many-to-One, thành thạo annotation, xây dựng ứng dụng Spring Boot + SQL Server, CRUD + Bootstrap.

---

## Slide 3 — Giới thiệu & Lý do (30s)

**WINDOW: SLIDE**

Vấn đề: Object-Relational Impedance Mismatch — bảng SQL khác object Java. JPA/Hibernate ánh xạ bảng → Entity, giải quyết sự không tương thích này.

---

## Slide 4 — Công nghệ (15s)

**WINDOW: SLIDE**

Spring Boot, JPA/Hibernate, SQL Server, Thymeleaf, Bootstrap, Lombok.

---

## Slide 5 — Tổng quan quan hệ (1.5 phút)

**WINDOW: SLIDE**

Quan hệ giữa Classroom và Student — một lớp có nhiều SV, mỗi SV thuộc một lớp.
Student là owning side (giữ FK `class_id`). Classroom là inverse side (`mappedBy`).

**WINDOW: INTELLIJ**

Mở `src/main/java/com/ttjavaee/classroom/entity/Classroom.java`

> Chỉ dòng 55: `@OneToMany(mappedBy = "classroom")` — đây là phía "Một", mappedBy cho biết bên kia là chủ quan hệ.

Mở `src/main/java/com/ttjavaee/classroom/entity/Student.java`

> Chỉ dòng 59-61: `@ManyToOne` + `@JoinColumn(name = "class_id")` — đây là phía "Nhiều", `@JoinColumn` chỉ định cột FK trong DB.
> Student là owning side vì nó chứa khóa ngoại.

**WINDOW: SLIDE** (quay lại)

---

## Slide 6 — Thiết kế CSDL (1 phút)

**WINDOW: SLIDE**

Bảng Classes: id (PK), class_code (UNIQUE), class_name.
Bảng Students: id (PK), student_code (UNIQUE), full_name, email, class_id (FK → Classes.id).
ON DELETE SET NULL — xóa lớp thì class_id SV thành NULL, giữ lại SV.

**WINDOW: SSMS**

Chạy: `SELECT * FROM Classes` → thấy dữ liệu
Chạy: `SELECT * FROM Students` → thấy class_id liên kết

**WINDOW: SLIDE** (quay lại)

---

## Slide 7 — Kiến trúc MVC (1 phút)

**WINDOW: SLIDE**

MVC 3 tầng: Controller → Service → Repository → DB. Thymeleaf làm View.

**WINDOW: INTELLIJ**

Mở `controller/ClassroomController.java`

> Chỉ dòng 38-44: `listClasses()` — nhận GET `/classes`, gọi Service, đưa `model.addAttribute("classes", list)`, trả về `"class-list"`.

Mở `templates/class-list.html`

> Chỉ dòng 44: `th:each="classroom : ${classes}"` — Thymeleaf lặp danh sách từ Model.
> Chỉ dòng 53: `<span th:text="${classroom.students.size()}">` — hiển thị số SV, thể hiện quan hệ @OneToMany.

**Nói:**
Luồng: URL → Controller → Model → View → HTML. Đây là MVC + Thymeleaf.

**WINDOW: SLIDE** (quay lại)

---

## Slide 8 — Mapping JPA (1 phút)

**WINDOW: SLIDE**

Hai annotation chính: `@OneToMany(mappedBy)` và `@ManyToOne` + `@JoinColumn`.

**WINDOW: INTELLIJ**

Quay lại `Classroom.java` và `Student.java`

> `cascade = {PERSIST, MERGE}`: lưu/merge Classroom thì Student tự đồng bộ.
> Không dùng REMOVE vì DB có `ON DELETE SET NULL` — nếu dùng sẽ xung đột.
> `fetch = LAZY`: chỉ tải danh sách SV khi cần (`getStudents()`).

**WINDOW: SLIDE** (quay lại)

---

## Slide 9 — Demo: Chạy 5 chức năng (3 phút)

> **WINDOW: TRÌNH DUYỆT** là chính. Mở code khi cần minh họa.

### 9.1. Danh sách lớp

Vào `http://localhost:8080/classes`

> Nói: Thấy danh sách lớp, badge xanh hiển thị số SV — dùng `.students.size()` bên Java.

**WINDOW: INTELLIJ** → `class-list.html` dòng 53 (badge)

**WINDOW: TRÌNH DUYỆT** → quay lại

### 9.2. Thêm lớp mới

Click "Thêm Lớp mới" → nhập CNTT02, Công nghệ thông tin 02 → Lưu

> Nói: POST `/classes/save` → `ClassroomController.saveClass()` → `Service.saveClassroom()` → `Repository.save()` → Hibernate sinh `INSERT INTO Classes`

**WINDOW: SSMS** → `SELECT * FROM Classes` thấy dòng mới

**WINDOW: TRÌNH DUYỆT** → quay lại

### 9.3. Danh sách SV

Vào `/students` — thấy cột "Lớp học" hiển thị tên lớp (không phải ID)

> Nói: Đây là nhờ `student.classroom.className` trong Thymeleaf — đi qua quan hệ @ManyToOne.

### 9.4. Thêm SV + chọn lớp

Click "Thêm Sinh viên" → nhập thông tin → chọn lớp từ dropdown → Lưu

**WINDOW: INTELLIJ** → `student-form.html` dòng 67-73

> Chỉ dropdown: `th:each="c : ${classes}"` lặp danh sách lớp, `th:selected` tự chọn lớp hiện tại, `name="classroomId"` gửi ID lớp lên Controller.

**WINDOW: SSMS** → `SELECT * FROM Students` thấy SV mới có class_id

**WINDOW: TRÌNH DUYỆT** → quay lại

### 9.5. Lọc SV theo lớp

Chọn lớp từ dropdown filter → chỉ hiển thị SV của lớp đó (URL: `/students?classId=1`)

**WINDOW: INTELLIJ** → `repository/StudentRepository.java` dòng 24

> Chỉ `findByClassroomId(Long)` — Spring Data JPA tự sinh `SELECT * FROM Students WHERE class_id = ?`. Không cần viết SQL.

**WINDOW: SSMS** → `SELECT * FROM Students WHERE class_id = 1` — dữ liệu khớp hoàn toàn.

### 9.6. Xóa lớp (chứng minh ON DELETE SET NULL)

Vào `/classes`, xóa lớp CNTT02 vừa tạo.

**WINDOW: SSMS** → `SELECT * FROM Students WHERE class_id = (SELECT id FROM Classes WHERE class_code = 'CNTT02')` — hoặc kiểm tra SV trước đó có class_id thành NULL.

> Nói: Đây là ON DELETE SET NULL — SV không bị xóa, chỉ mất liên kết lớp.

**WINDOW: TRÌNH DUYỆT** → `/classes` thấy lớp đã biến mất

---

## Slide 10 — Kết quả & Khó khăn (1 phút)

**WINDOW: SLIDE**

**Đã làm được:**
- CRUD lớp + SV, gán SV vào lớp, lọc, đếm số SV
- Ứng dụng Spring Boot chạy ổn định kết nối SQL Server
- Hiểu @OneToMany, @ManyToOne, @JoinColumn, mappedBy, cascade

**Khó khăn:**
- Cấu hình SQL Server Authentication + bật TCP/IP
- Hiểu owning side và cách Hibernate cập nhật FK

**Hạn chế + hướng khắc phục:**
- Chưa validation nâng cao → thêm `@Valid`
- Chưa phân trang → thêm `Pageable`
- Chưa tìm kiếm → thêm `@Query`

---

## Slide 11 — Kết luận & Hướng phát triển (30s)

**WINDOW: SLIDE**

Nắm vững JPA/Hibernate mapping. Hướng phát triển: Spring Validation, phân trang, tìm kiếm, Spring Security.

---

## Slide 12 — Cảm ơn (15s)

**WINDOW: SLIDE**

Cảm ơn thầy cô và các bạn. Rất mong nhận được góp ý và câu hỏi!

---

> **Gợi ý:** Cử 1 thành viên điều khiển slide, 1 thành viên nói + demo. Mở sẵn 3 cửa sổ: slide (fullscreen), IntelliJ (thu nhỏ 1/2 màn hình), trình duyệt + SSMS (nửa còn lại).
