# Kế hoạch phát triển — Đề tài 07

**Dự án:** Quản lý Lớp học & Sinh viên (Spring Boot + JPA + SQL Server)  
**Cập nhật lần cuối:** 2026-05-29 · **Phiên bản:** V1.4 (Phase 4–5)

---

## Tiến độ tổng quan

| Phase | Mô tả | Trạng thái |
|-------|--------|------------|
| **0** | Maven Wrapper, profile dev, README | ✅ Hoàn thành |
| **1** | Validation, flash message, POST delete, ExceptionHandler | ✅ Hoàn thành |
| **2** | Phân trang, tìm kiếm, tối ưu N+1, Flyway | ✅ Hoàn thành |
| **3** | Teacher, Subject, Grade, Dashboard | ✅ Hoàn thành |
| **4** | REST API + Swagger | ✅ Hoàn thành |
| **5** | Docker, Actuator, CI/CD | ✅ Hoàn thành (Security hoãn) |

---

## Phase 0 — Đã hoàn thành

| Task | Chi tiết |
|------|----------|
| Maven Wrapper | `mvnw`, `mvnw.cmd`, `.mvn/wrapper/` |
| Profile `dev` | `application-dev.properties` |
| Mẫu cấu hình | `application.properties.example` |
| `.gitignore` | Bỏ ignore `mvnw`; thêm `application-local.properties` |
| README | Hướng dẫn JDK 17, `mvnw`, `DB_PASSWORD` |

---

## Phase 1 — Đã hoàn thành

| Task | File / thành phần |
|------|-------------------|
| Bean Validation | `pom.xml`, `Classroom.java`, `Student.java` |
| `@Valid` + `BindingResult` | `ClassroomController`, `StudentController` |
| Flash messages | `RedirectAttributes`, `layout.html` fragment `alerts` |
| POST delete | `POST /classes/delete/{id}`, `POST /students/delete/{id}` |
| Xử lý trùng mã | `GlobalExceptionHandler` |
| `@Transactional` | `ClassroomService`, `StudentService` |
| Form hiển thị lỗi | `class-form.html`, `student-form.html` |

---

## Phase 2 — Đã hoàn thành

| Task | Chi tiết |
|------|----------|
| Phân trang | `Pageable` — `/classes`, `/students` (mặc định 5 dòng/trang) |
| Tìm kiếm SV | Theo mã hoặc họ tên + lọc lớp (`keyword`, `classId`) |
| Tối ưu N+1 | `ClassroomSummaryDto` + `SIZE(c.students)`; `@EntityGraph` trên Student |
| Flyway | `V1__create_schema.sql`, `V2__seed_data.sql`; `ddl-auto=validate` |
| UI | Fragment phân trang Thymeleaf, chọn số dòng/trang |

**Ghi chú:** Vẫn có thể chạy `init-db.sql` thủ công; Flyway `baseline-on-migrate` tương thích DB đã tồn tại.

---

## Phase 3 — Đã hoàn thành

| Task | Chi tiết |
|------|----------|
| Flyway V3–V5 | `Teachers`, `Subjects`, `Grades`, `Classes.teacher_id` |
| Giảng viên | CRUD `/teachers` |
| Môn học | CRUD `/subjects` |
| Điểm | CRUD `/grades` — SV + Môn + điểm (0–10), unique (student, subject) |
| Lớp ↔ GV | `@ManyToOne` Teacher trên Classroom, dropdown form lớp |
| Dashboard | `/dashboard` — thống kê, điểm TB, top lớp đông SV |
| Trang chủ | `/` → redirect `/dashboard` |

---

## Phase 4 — Đã hoàn thành

| Task | Chi tiết |
|------|----------|
| REST API | `/api/v1/*` — classes, students, teachers, subjects, grades, dashboard |
| DTO | Request/Response tách khỏi Entity |
| ApiResponse | `{ success, message, data }` |
| ApiExceptionHandler | 404, 400, 409 JSON |
| SpringDoc | Swagger UI: `/swagger-ui.html` |

## Phase 5 — Đã hoàn thành (một phần)

| Task | Chi tiết |
|------|----------|
| Docker | `Dockerfile`, `docker-compose.yml` |
| Profile `docker` | `application-docker.properties` |
| Actuator | `/actuator/health` |
| GitHub Actions | `.github/workflows/maven.yml` |
| **Chưa làm** | Spring Security (đăng nhập) — có thể bổ sung sau |

---

## Ghi chú cho nhóm

- Mỗi phase xong → cập nhật bảng tiến độ trên đầu file này
- Mỗi phase xong → bổ sung slide & báo cáo (mục cải tiến)
- Trước khi nộp bài: chạy checklist trong `HUONG_DAN_NOP_BAI.md`
