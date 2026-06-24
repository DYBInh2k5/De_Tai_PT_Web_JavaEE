    # HƯỚNG DẪN LÀM SLIDE THUYẾT TRÌNH (.pptx)
## 11 slide (10 nội dung + Cảm ơn) — khoảng 8–12 slide theo yêu cầu đề bài

---

## ✅ SLIDE ĐÃ HOÀN THÀNH

> **Link slide Gamma:** https://gamma.app/docs/Tim-hieu-quan-he-One-to-Many-va-Many-to-One-trong-JPAHibernate-qu-0xgbhtwrqgd0ais

Slide đã được làm xong trên Gamma. Nội dung tham khảo bên dưới dùng để đối chiếu hoặc làm lại bằng PowerPoint nếu cần nộp file `.pptx`.

---

## Slide 1 — Trang bìa

- **Tên đề tài:** Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học
- Tên trường / khoa
- Danh sách thành viên nhóm
- Giảng viên hướng dẫn
- Học kỳ / Năm học

---

## Slide 2 — Mục tiêu đề tài

- Hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate
- Nắm vững các annotation: @OneToMany, @ManyToOne, @JoinColumn, mappedBy, cascade
- Xây dựng ứng dụng quản lý lớp học và sinh viên với Spring Boot + SQL Server
- Thực hiện đầy đủ CRUD có kết nối database và giao diện Bootstrap

---

## Slide 3 — Công nghệ sử dụng

Dạng bảng hoặc icon:

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

## Slide 4 — Tổng quan quan hệ One-to-Many / Many-to-One

> ⚠️ Vẽ lại bằng Draw.io với 2 khối Classes ── Students, dùng mũi tên 1→N. Ghi rõ annotation tương ứng. Export PNG dán vào slide.

Vẽ sơ đồ đơn giản:

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

---

## Slide 5 — Thiết kế cơ sở dữ liệu

> ⚠️ Dán ảnh ERD đã vẽ ở báo cáo (3.3.1) vào slide này. Bảng mô tả bên dưới để tham khảo thêm.

Dán ERD hoặc bảng mô tả:

**Bảng Classes:**
| Cột | Kiểu | Ràng buộc |
|---|---|---|
| id | BIGINT | PK, IDENTITY |
| class_code | NVARCHAR(50) | NOT NULL, UNIQUE |
| class_name | NVARCHAR(255) | NOT NULL |

**Bảng Students:**
| Cột | Kiểu | Ràng buộc |
|---|---|---|
| id | BIGINT | PK, IDENTITY |
| student_code | NVARCHAR(50) | NOT NULL, UNIQUE |
| full_name | NVARCHAR(255) | NOT NULL |
| email | NVARCHAR(255) | NOT NULL |
| class_id | BIGINT | FK → Classes(id), ON DELETE SET NULL |

---

## Slide 6 — Kiến trúc chương trình (MVC)

> ⚠️ Dán ảnh sơ đồ MVC đã vẽ ở báo cáo (3.4) vào slide này. Sơ đồ text bên dưới để tham khảo.

Vẽ sơ đồ tầng:

```
Trình duyệt
    ↕ HTTP
Controller (ClassroomController, StudentController)
    ↕
Service (ClassroomService, StudentService)
    ↕
Repository (ClassroomRepository, StudentRepository)
    ↕ Hibernate/JPA
SQL Server (ClassroomDB)
```

Thymeleaf Templates → View (HTML + Bootstrap)

---

## Slide 7 — Mapping JPA (Trọng tâm)

> ⚠️ Dán ảnh UML Class Diagram (2 class Classroom + Student kèm annotation) đã vẽ theo hướng dẫn ở báo cáo (4.4) vào slide này.

Hiển thị 2 đoạn code song song:

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

Giải thích ngắn:
- `mappedBy = "classroom"` → Student là owning side
- `@JoinColumn(name = "class_id")` → cột FK trong bảng Students
- `cascade = {PERSIST, MERGE}` → không dùng REMOVE vì DB dùng ON DELETE SET NULL

---

## Slide 8 — Demo các chức năng

Dán ảnh chụp màn hình (dùng `Win + Shift + S` để chụp từng phần):

1. **Danh sách lớp học** — `http://localhost:8080/classes` — có cột Số SV (badge xanh)
2. **Form thêm sinh viên** — `http://localhost:8080/students/add` — dropdown chọn lớp
3. **Danh sách sinh viên** — `http://localhost:8080/students` — cột Lớp học hiển thị tên lớp
4. **Lọc sinh viên theo lớp** — `http://localhost:8080/students?classId=1` — dropdown filter
5. **Sinh viên theo lớp** — `http://localhost:8080/classes/1/students` — danh sách SV trong 1 lớp
6. **Form thêm lớp** — `http://localhost:8080/classes/add` — form nhập mã và tên lớp

---

## Slide 9 — Kết quả & Khó khăn

**Kết quả đạt được:**
- Hoàn thành đủ 5 chức năng bắt buộc
- Hiểu và vận dụng được @OneToMany / @ManyToOne
- Ứng dụng chạy ổn định, có giao diện Bootstrap

**Khó khăn / Hạn chế:**
- Cấu hình SQL Server Authentication và TCP/IP lần đầu
- Hiểu đúng owning side và cách Hibernate cập nhật khóa ngoại
- Chưa có validation nâng cao (chỉ dùng HTML required)
- Chưa có phân trang khi dữ liệu lớn

---

## Slide 10 — Kết luận & Hướng phát triển

**Kết luận:**
Đề tài giúp hiểu rõ cách JPA/Hibernate ánh xạ quan hệ giữa các bảng sang mô hình đối tượng Java, đặc biệt là quan hệ One-to-Many và Many-to-One trong thực tế quản lý lớp học.

**Hướng phát triển:**
- Thêm Spring Validation (@Valid) kiểm tra dữ liệu đầu vào
- Thêm phân trang với Spring Data Pageable
- Thêm tìm kiếm sinh viên theo tên
- Thêm Spring Security đăng nhập / phân quyền

---

## Cảm ơn & Câu hỏi

*(Slide cuối: lời cảm ơn và mời đặt câu hỏi)*