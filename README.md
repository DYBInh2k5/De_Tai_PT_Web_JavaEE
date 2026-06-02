# Quản lý Lớp học & Sinh viên
### Đề tài 07: One-to-Many và Many-to-One trong JPA/Hibernate

**Phiên bản:** V1.4 (Phase 0–5 — xem `docs/KE_HOACH_PHAT_TRIEN.md`)

---

## Công nghệ sử dụng

| Công nghệ | Phiên bản |
|-----------|-----------|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Spring Data JPA / Hibernate | (theo Spring Boot) |
| Bean Validation | (theo Spring Boot) |
| SQL Server | 2019+ |
| Thymeleaf | (theo Spring Boot) |
| Bootstrap | 5.3.0 |
| Lombok | (theo Spring Boot) |
| Maven Wrapper | 3.9.6 (`mvnw`) |
| Flyway | (theo Spring Boot) |
| SpringDoc OpenAPI | 2.5.0 |
| Docker | Dockerfile + Compose |

---

## Yêu cầu môi trường

- **JDK 17** trở lên (`java -version` phải hiện 17.x)
- **SQL Server** đang chạy (`localhost:1433` hoặc `SQLEXPRESS`)
- **IntelliJ IDEA** (khuyên dùng) hoặc VS Code + Extension Pack for Java
- **Không bắt buộc cài Maven** toàn cục — dùng `mvnw` / `mvnw.cmd` trong project

---

## Hướng dẫn chạy chương trình

### Bước 1: Tạo Database

1. Mở **SSMS** hoặc **Azure Data Studio**
2. Chạy toàn bộ file `init-db.sql`
3. Kiểm tra database `ClassroomDB` và dữ liệu mẫu

### Bước 2: Cấu hình mật khẩu SQL Server

**Cách A — Biến môi trường (khuyên dùng):**

```powershell
$env:DB_PASSWORD = "MatKhauSaCuaBan"
```

**Cách B — Sửa file dev:**

Mở `src/main/resources/application-dev.properties` và đổi dòng `spring.datasource.password`.

**Cách C — File local (không commit):**

1. Sao chép `application.properties.example` → `src/main/resources/application-local.properties`
2. Điền mật khẩu; thêm vào `application.properties`: `spring.profiles.active=dev,local`

> SQL Server Express: xem comment trong `application-dev.properties`.

### Bước 3: Chạy ứng dụng

**Windows (PowerShell):**

```powershell
cd De_Tai_PT_Web_JavaEE
$env:JAVA_HOME = "C:\jdk-17.0.12"   # đường dẫn JDK 17 của bạn
.\mvnw.cmd spring-boot:run
```

**IntelliJ IDEA:**

1. **File → Open** → chọn folder `De_Tai_PT_Web_JavaEE`
2. **Project SDK** = JDK 17
3. Run `ClassroomManagementApplication.java`

### Bước 4: Truy cập

**http://localhost:8080**

---

## Chức năng

| Chức năng | URL / Method |
|-----------|----------------|
| Tổng quan (Dashboard) | `GET /dashboard` |
| Trang chủ | `GET /` → redirect `/dashboard` |
| Danh sách lớp | `GET /classes` |
| Thêm / sửa lớp | `GET /classes/add`, `/edit/{id}` · `POST /classes/save` |
| Xóa lớp | `POST /classes/delete/{id}` |
| SV theo lớp | `GET /classes/{id}/students` |
| Danh sách SV | `GET /students` |
| Lọc SV theo lớp | `GET /students?classId={id}` |
| Tìm kiếm SV | `GET /students?keyword={text}` |
| Phân trang | `GET /classes?page=0&size=5`, `/students?page=0&size=5` |
| Thêm / sửa SV | `GET /students/add`, `/edit/{id}` · `POST /students/save` |
| Xóa SV | `POST /students/delete/{id}` |

### Tính năng V1.1

- **Validation** form: mã/tên/email bắt buộc, định dạng email
- **Thông báo** thành công / lỗi (flash message) sau thao tác
- **Xóa bằng POST** (an toàn hơn GET)
- **Xử lý trùng mã** (mã lớp / mã SV) qua `GlobalExceptionHandler`
- **Profile** `dev` + mật khẩu qua `DB_PASSWORD`

### Tính năng V1.2 (Phase 2)

- **Phân trang** danh sách lớp và sinh viên
- **Tìm kiếm** sinh viên theo mã hoặc họ tên
- **Flyway** quản lý schema (`src/main/resources/db/migration/`)
- **Tối ưu query** đếm SV/lớp bằng `SIZE()` (tránh N+1)

### Tính năng V1.3 (Phase 3)

- **Giảng viên** — CRUD, gán vào lớp (`@ManyToOne`)
- **Môn học** — CRUD
- **Điểm số** — nhập điểm SV theo môn (0–10)
- **Dashboard** — thống kê, điểm TB, top lớp

### Tính năng V1.4 (Phase 4–5)

- **REST API** — `/api/v1/...` (JSON)
- **Swagger UI** — http://localhost:8080/swagger-ui.html
- **Docker** — `docker compose up --build`
- **Health check** — `/actuator/health`

---

## REST API (tóm tắt)

| Method | Endpoint | Mô tả |
|--------|----------|--------|
| GET | `/api/v1/dashboard/stats` | Thống kê tổng quan |
| GET/POST/PUT/DELETE | `/api/v1/classes` | CRUD lớp |
| GET/POST/PUT/DELETE | `/api/v1/students` | CRUD SV (+ `?keyword`, `classId`) |
| GET/POST/PUT/DELETE | `/api/v1/teachers` | CRUD GV |
| GET/POST/PUT/DELETE | `/api/v1/subjects` | CRUD môn |
| GET/POST/PUT/DELETE | `/api/v1/grades` | CRUD điểm |

Tài liệu tương tác: **Swagger UI** (`/swagger-ui.html`).

---

## Chạy bằng Docker

```powershell
cd De_Tai_PT_Web_JavaEE
# Mật khẩu SA phải đủ mạnh (chữ hoa, số, ký tự đặc biệt)
$env:DB_PASSWORD = "YourPassword123!"
docker compose up --build
```

- Web: http://localhost:8080  
- Swagger: http://localhost:8080/swagger-ui.html  
- Health: http://localhost:8080/actuator/health  

---

## Cấu trúc Project

```
De_Tai_PT_Web_JavaEE/
├── mvnw, mvnw.cmd              ← Maven Wrapper
├── application.properties.example
├── init-db.sql
├── pom.xml
├── docs/                       ← Báo cáo, hướng dẫn, kế hoạch PT
└── src/main/
    ├── java/com/ttjavaee/classroom/
    │   ├── ClassroomManagementApplication.java
    │   ├── controller/
    │   ├── entity/
    │   ├── api/                         ← REST controllers
    │   ├── api/dto/                     ← API request/response
    │   ├── config/OpenApiConfig.java
    │   ├── dto/ClassroomSummaryDto.java
    │   ├── exception/GlobalExceptionHandler.java
    │   ├── repository/
    │   └── service/
    └── resources/
        ├── application.properties
        ├── application-dev.properties
        ├── db/migration/          ← Flyway V1, V2
        └── templates/
```

---

## Quan hệ JPA

```
Classroom (1) ←──────────── (N) Student
   @OneToMany                    @ManyToOne
   mappedBy="classroom"          @JoinColumn(name="class_id")
   cascade=PERSIST,MERGE         (owning side)
```

Khi xóa lớp: SQL Server `ON DELETE SET NULL` → sinh viên không bị xóa.

---

## Tài liệu trong `docs/`

| File | Nội dung |
|------|----------|
| `KE_HOACH_PHAT_TRIEN.md` | Kế hoạch phát triển & tiến độ |
| `BAO_CAO_CHI_TIET.md` | Nội dung báo cáo đầy đủ |
| `HUONG_DAN_CHI_TIET.md` | Hướng dẫn đề tài |
| `HUONG_DAN_KET_NOI_SQLSERVER.md` | Cài SQL Server |
| `HUONG_DAN_NOP_BAI.md` | Checklist nộp bài |
| `HUONG_DAN_SLIDE.md` | Gợi ý slide |
| `HUONG_DAN_BAO_CAO.md` | Gợi ý viết báo cáo |

---

## Lưu ý

- Không commit mật khẩu thật; dùng `DB_PASSWORD` hoặc `application-local.properties`
- Xóa thư mục `target/` trước khi nén bài nộp
- Schema do **Flyway** quản lý (`ddl-auto=validate`); vẫn có thể chạy `init-db.sql` lần đầu
