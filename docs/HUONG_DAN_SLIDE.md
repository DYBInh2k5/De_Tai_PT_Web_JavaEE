# HƯỚNG DẪN LÀM SLIDE THUYẾT TRÌNH (.pptx)
## 12 slide — Phong cách: Vừa chiếu slide vừa chạy demo vừa chỉ code

> ⚠️ **Cách trình bày mới:** Không chỉ đọc slide. Mỗi slide là một trạm — vừa nói, vừa chuyển qua cửa sổ code / SSMS / trình duyệt để minh họa trực tiếp.
>
> **Cần mở sẵn 3 cửa sổ:** Slide (fullscreen), IntelliJ (code + project tree), Trình duyệt + SSMS

---

## ✅ SLIDE ĐÃ HOÀN THÀNH

> **Link slide Gamma:** https://gamma.app/docs/Tim-hieu-quan-he-One-to-Many-va-Many-to-One-trong-JPAHibernate-qu-0xgbhtwrqgd0ais

Slide đã được làm xong trên Gamma. Nội dung bên dưới dùng để đối chiếu hoặc làm lại bằng PowerPoint.

---

## Slide 1 — Trang bìa (30s)

**WINDOW:** SLIDE

- **Tên đề tài:** Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học
- Tên trường / khoa
- Danh sách thành viên nhóm
- Giảng viên hướng dẫn
- Học kỳ / Năm học

---

## Slide 2 — Mục tiêu đề tài (30s)

**WINDOW:** SLIDE

- Hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate
- Nắm vững các annotation: @OneToMany, @ManyToOne, @JoinColumn, mappedBy, cascade
- Xây dựng ứng dụng quản lý lớp học và sinh viên với Spring Boot + SQL Server
- Thực hiện đầy đủ CRUD có kết nối database và giao diện Bootstrap

---

## Slide 3 — Công nghệ sử dụng (15s)

**WINDOW:** SLIDE

| Công nghệ | Vai trò |
|---|---|
| Spring Boot 3.2.5 | Backend framework |
| Spring Data JPA | ORM / tương tác DB |
| Hibernate | JPA implementation |
| SQL Server 2019 | Cơ sở dữ liệu |
| Thymeleaf | Template engine (View) |
| Bootstrap 5 | Giao diện Web |
| Lombok | Giảm boilerplate Java |

---

## Slide 4 — Tổng quan quan hệ One-to-Many / Many-to-One (90s)

**WINDOW:** SLIDE

Vẽ sơ đồ:

```
[Classes]  1 ──────── N  [Students]
  id (PK)                  id (PK)
  classCode                studentCode
  className                fullName
                           email
                           class_id (FK)
```

- **One-to-Many:** 1 lớp có nhiều sinh viên → @OneToMany ở Classroom
- **Many-to-One:** nhiều sinh viên thuộc 1 lớp → @ManyToOne ở Student
- **Owning side:** Student (có @JoinColumn — bên chứa khóa ngoại)
- **Inverse side:** Classroom (có mappedBy)

**WINDOW:** INTELLIJ

Mở `entity/Classroom.java` — chỉ dòng 55: `@OneToMany(mappedBy = "classroom")`
Mở `entity/Student.java` — chỉ dòng 59-61: `@ManyToOne` + `@JoinColumn(name = "class_id")`

**WINDOW:** SLIDE (quay lại)

---

## Slide 5 — Thiết kế cơ sở dữ liệu (60s)

**WINDOW:** SLIDE

**Bảng Classes:** id (PK), class_code (UNIQUE), class_name
**Bảng Students:** id (PK), student_code (UNIQUE), full_name, email, class_id (FK → ON DELETE SET NULL)

**WINDOW:** SSMS

Chạy `SELECT * FROM Classes`
Chạy `SELECT * FROM Students`

**WINDOW:** SLIDE (quay lại)

---

## Slide 6 — Kiến trúc chương trình (MVC) (60s)

**WINDOW:** SLIDE

```
Trình duyệt → Controller → Service → Repository (JPA) → SQL Server
```

Thymeleaf Templates → View (HTML + Bootstrap)

**Luồng request MVC + Thymeleaf (bắt buộc):**

```
1. GET /classes
2. ClassroomController.findAll()
3. → ClassroomService.getAllClassrooms()
4. → ClassroomRepository.findAll()
5. ← List<Classroom>
6. model.addAttribute("classrooms", list) → "class-list"
7. class-list.html: th:each, th:text
```
> ⚠️ **Bắt buộc theo thông báo mục 3.**

**WINDOW:** INTELLIJ

Mở `controller/ClassroomController.java:38-44` — luồng request
Mở `templates/class-list.html:44-54` — th:each + badge students.size()

**WINDOW:** SLIDE (quay lại)

---

## Slide 7 — Mapping JPA (Trọng tâm) (60s)

**WINDOW:** SLIDE

**Classroom.java (phía "Một"):**
```java
@OneToMany(mappedBy = "classroom",
           cascade = {PERSIST, MERGE},
           fetch = LAZY)
private List<Student> students;
```

**Student.java (phía "Nhiều"):**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "class_id")
private Classroom classroom;
```

**WINDOW:** INTELLIJ

Quay lại 2 file entity — giải thích cascade (PERSIST, MERGE, không REMOVE), fetch LAZY

**WINDOW:** SLIDE (quay lại)

---

## Slide 8 — Demo các chức năng (3 phút)

> ⚠️ **Chạy LIVE — không dùng ảnh chụp.** Vừa chạy vừa chỉ code.

**WINDOW:** TRÌNH DUYỆT — `http://localhost:8080/classes`

1. **DS lớp** — thấy badge số SV
   **WINDOW:** INTELLIJ → `class-list.html:53` (badge)
   **WINDOW:** TRÌNH DUYỆT

2. **Thêm lớp** — CNTT02 → Lưu
   **WINDOW:** SSMS → `SELECT * FROM Classes`
   **WINDOW:** TRÌNH DUYỆT

3. **DS sinh viên** — `/students`, cột tên lớp
   **WINDOW:** INTELLIJ → chỉ `student.classroom.className`

4. **Thêm SV** — `/students/add`, dropdown chọn lớp
   **WINDOW:** INTELLIJ → `student-form.html:67-73` (dropdown)

5. **Lọc SV** — `/students?classId=1`
   **WINDOW:** INTELLIJ → `StudentRepository.java:24` (`findByClassroomId`)
   **WINDOW:** SSMS → `SELECT * FROM Students WHERE class_id = 1`

---

## Slide 9 — Kết quả & Khó khăn (60s)

**WINDOW:** SLIDE

**Kết quả:** Hoàn thành 5 chức năng, hiểu @OneToMany/@ManyToOne, ứng dụng chạy ổn định.

**Khó khăn:** Cấu hình SQL Server + TCP/IP, hiểu owning side.
**Hạn chế:** Thiếu validation nâng cao, chưa phân trang.

---

## Slide 10 — Kết luận & Hướng phát triển (30s)

**WINDOW:** SLIDE

Nắm vững JPA/Hibernate mapping. Hướng phát triển: @Valid, Pageable, @Query, Spring Security.

---

## Cảm ơn & Câu hỏi (15s)

**WINDOW:** SLIDE

*(Slide cuối: lời cảm ơn và mời đặt câu hỏi)*
