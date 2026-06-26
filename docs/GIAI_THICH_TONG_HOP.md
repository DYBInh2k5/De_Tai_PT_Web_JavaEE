# Giải Thích Tổng Hợp — Đề Tài 07
## Dùng cho thuyết trình và trả lời câu hỏi (15 phút)

### Phong cách trình bày

Vừa chiếu slide, vừa chạy demo live, vừa mở code. Mỗi slide có 1-2 lần **WINDOW: INTELLIJ / SSMS / TRÌNH DUYỆT** để chuyển cửa sổ. Chi tiết trong `VAN_BAN_THUYET_TRINH.md`.

---

## 1. Công Nghệ Chính: JPA/Hibernate

### JPA/Hibernate là gì?
- **JPA (Java Persistence API):** Là specification (đặc tả) do Oracle định nghĩa, quy định cách ánh xạ các đối tượng Java vào bảng trong cơ sở dữ liệu quan hệ.
- **Hibernate:** Là implementation (triển khai) phổ biến nhất của JPA. Nó chính là thư viện chạy thật sự.

### Dùng để giải quyết vấn đề gì?
Giải quyết **Object-Relational Impedance Mismatch** — sự không tương thích giữa:
- Mô hình quan hệ: bảng, cột, khóa ngoại (SQL)
- Mô hình đối tượng: class, field, object reference (Java)

Ví dụ: Trong SQL, bảng Students có cột `class_id` là số. Trong Java, chúng ta muốn `student.getClassroom()` trả về đối tượng `Classroom` chứ không phải một con số.

### Nằm ở tầng nào?
Tầng **Persistence** — nằm giữa Service Layer (xử lý nghiệp vụ) và Database (SQL Server).

```
Controller → Service → Repository (JPA/Hibernate) → SQL Server
```

### Khác gì so với cách làm thông thường?
| Cách thông thường (JDBC) | Với JPA/Hibernate |
|---|---|
| Viết SQL thủ công | Hibernate tự sinh SQL |
| Tự map ResultSet vào object | Annotation @Entity + @Column |
| Quản lý Connection thủ công | EntityManager tự động |
| Nhiều boilerplate code | Code ngắn gọn hơn |

---

## 2. Luồng Request MVC + Thymeleaf (BẮT BUỘC)

Ví dụ chức năng **xem danh sách lớp học** (`GET /classes`):

```
Bước 1:  Trình duyệt gõ http://localhost:8080/classes
Bước 2:  ClassroomController.findAll()           ← nhận HTTP request
Bước 3:  Gọi ClassroomService.getAllClassrooms() ← Service xử lý nghiệp vụ
Bước 4:  Gọi ClassroomRepository.findAll()       ← JPA/Hibernate sinh SQL
Bước 5:  Hibernate tạo: SELECT * FROM Classes
Bước 6:  Repository trả về List<Classroom>       ← kết quả từ database
Bước 7:  Controller đưa vào Model: model.addAttribute("classrooms", list)
Bước 8:  Controller trả về "class-list"          ← tên view (file HTML)
Bước 9:  Thymeleaf đọc class-list.html:
         - th:each="c : ${classrooms}"           ← lặp danh sách
         - th:text="${c.className}"              ← hiển thị tên lớp
Bước 10: Trình duyệt nhận HTML hoàn chỉnh
```

**Cách truyền dữ liệu sang Thymeleaf:**
- Dùng `Model.addAttribute("key", value)` trong Controller
- Trong HTML: `${key}` để đọc giá trị, `th:each` để lặp, `th:text` để hiển thị

---

## 3. Repository — Service — Controller: Trách Nhiệm Từng Lớp

| Tầng | Trách nhiệm | Ví dụ |
|---|---|---|
| **Controller** | Nhận HTTP request, gọi Service, trả về View | `ClassroomController.findAll()` |
| **Service** | Xử lý logic nghiệp vụ, gọi Repository | `ClassroomService.getAllClassrooms()` |
| **Repository** | Truy xuất database qua JPA | `ClassroomRepository.findAll()` |

**Nguyên tắc:** Controller KHÔNG được xử lý nghiệp vụ hoặc truy vấn database trực tiếp. Phải qua Service → Repository.

---

## 4. Entity Mapping — Annotation Chi Tiết

### Classroom.java (phía "Một")
```java
@Entity
@Table(name = "Classes")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String classCode;

    @Column(nullable = false)
    private String className;

    @OneToMany(mappedBy = "classroom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Student> students;
}
```

### Student.java (phía "Nhiều")
```java
@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String studentCode;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Classroom classroom;
}
```

### Giải thích từng annotation

| Annotation | Vai trò |
|---|---|
| `@Entity` | Đánh dấu class là entity (bảng trong DB) |
| `@Table(name)` | Chỉ định tên bảng trong database |
| `@Id` | Khóa chính |
| `@GeneratedValue(IDENTITY)` | Tự tăng — SQL Server tự sinh id |
| `@Column(nullable, unique)` | Ràng buộc cột |
| `@OneToMany(mappedBy)` | Phía "Một" — một lớp có nhiều SV |
| `@ManyToOne` | Phía "Nhiều" — nhiều SV thuộc một lớp |
| `@JoinColumn(name)` | Khóa ngoại — cột class_id trong bảng Students |
| `cascade = {PERSIST, MERGE}` | Khi lưu/sửa Classroom, tự động đồng bộ Student |
| `fetch = LAZY` | Chỉ tải danh sách SV khi thực sự cần |

### Owning Side vs Inverse Side
- **Owning Side:** Student (có `@JoinColumn`) — bên chứa khóa ngoại thực tế trong DB
- **Inverse Side:** Classroom (có `mappedBy`) — bên chỉ phản chiếu, không quản lý FK

### Tại sao cascade KHÔNG dùng REMOVE?
Vì database dùng `ON DELETE SET NULL` — khi xóa lớp, `class_id` của SV được set NULL thay vì xóa SV. Nếu code thêm `CascadeType.REMOVE`, Hibernate sẽ cố xóa SV trước, xung đột với ràng buộc DB.

---

## 5. Thiết Kế Cơ Sở Dữ Liệu

### Bảng Classes
| Cột | Kiểu | Ràng buộc |
|---|---|---|
| id | BIGINT | PK, IDENTITY (tự tăng) |
| class_code | NVARCHAR(50) | NOT NULL, UNIQUE |
| class_name | NVARCHAR(255) | NOT NULL |

### Bảng Students
| Cột | Kiểu | Ràng buộc |
|---|---|---|
| id | BIGINT | PK, IDENTITY |
| student_code | NVARCHAR(50) | NOT NULL, UNIQUE |
| full_name | NVARCHAR(255) | NOT NULL |
| email | NVARCHAR(255) | NOT NULL |
| class_id | BIGINT | FK → Classes(id), ON DELETE SET NULL |

---

## 6. Lombok — So Sánh Trước và Sau

### Trước khi dùng Lombok (code dài)
```java
public class Classroom {
    private Long id;
    private String classCode;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }
    // ... thêm constructor, toString, equals...
}
```

### Sau khi dùng Lombok (code gọn)
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Classroom {
    @Id @GeneratedValue(...)
    private Long id;
    private String classCode;
}
```

### Annotation Lombok đã sử dụng
| Annotation | Tác dụng |
|---|---|
| `@Data` | Sinh getter, setter, toString, equals, hashCode |
| `@NoArgsConstructor` | Sinh constructor không tham số (JPA cần) |
| `@AllArgsConstructor` | Sinh constructor có đầy đủ tham số |
| `@Builder` | Sinh builder pattern (dùng trong test) |

---

## 7. Cấu Hình Database Quan Trọng

File: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB
spring.datasource.username=sa
spring.datasource.password=123456
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServer2016Dialect
```

- `ddl-auto=update`: Hibernate tự động tạo/sửa bảng theo entity (không cần chạy script SQL thủ công)
- `show-sql=true`: In câu SQL ra console để debug
- `dialect`: Dialect riêng cho SQL Server 2016+

---

## 8. Demo — Các Tình Huống Cần Chạy (Slide+Demo+Code)

> **Phong cách:** Vừa nói, vừa chiếu slide, vừa chạy demo live, vừa chỉ code. Mỗi bước có WINDOW để biết chuyển sang cửa sổ nào.

**Cần mở sẵn:**
- Slide (fullscreen)
- IntelliJ (project tree + file code)
- Trình duyệt → `http://localhost:8080`
- SSMS → query `ClassroomDB`

| Thứ tự | Chức năng | Demo + Code |
|---|---|---|
| 1 | Danh sách lớp | **WINDOW: TRÌNH DUYỆT** → `/classes` — badge số SV<br>**WINDOW: INTELLIJ** → `class-list.html:53` (`.students.size()`) |
| 2 | Thêm lớp | **WINDOW: TRÌNH DUYỆT** → `/classes/add` → nhập CNTT02 → Lưu<br>**WINDOW: SSMS** → `SELECT * FROM Classes` |
| 3 | DS sinh viên | **WINDOW: TRÌNH DUYỆT** → `/students` — cột "Lớp học" hiển thị tên lớp<br>**WINDOW: INTELLIJ** → chỉ `student.classroom.className` |
| 4 | Thêm SV + chọn lớp | **WINDOW: TRÌNH DUYỆT** → `/students/add` — dropdown chọn lớp<br>**WINDOW: INTELLIJ** → `student-form.html:67-73` (dropdown `th:each` + `th:selected`) |
| 5 | Lọc theo lớp | **WINDOW: TRÌNH DUYỆT** → `/students?classId=1`<br>**WINDOW: INTELLIJ** → `StudentRepository.java:24` (`findByClassroomId`)<br>**WINDOW: SSMS** → `SELECT * FROM Students WHERE class_id = 1` |
| 6 | Xóa lớp | **WINDOW: TRÌNH DUYỆT** → xóa 1 lớp<br>**WINDOW: SSMS** → kiểm tra `class_id` SV bị SET NULL |

---

## 9. Câu Hỏi Thường Gặp Khi Trả Lời

### "Giải thích công nghệ chính?"
JPA/Hibernate — ORM ánh xạ bảng SQL thành Java Entity, giải quyết sự khác biệt giữa mô hình quan hệ và hướng đối tượng.

### "Giải thích luồng xử lý từ giao diện đến database?"
Trả lời theo 10 bước ở mục 2 — bắt đầu từ URL `/classes` đến khi render HTML.

### "Chỉ ra phần code quan trọng nhất?"
2 annotation: `@OneToMany(mappedBy)` ở Classroom và `@ManyToOne` + `@JoinColumn` ở Student. Đây là lõi của quan hệ.

### "Hạn chế và hướng cải thiện?"
3 hạn chế chính: thiếu validation (`@Valid`), thiếu phân trang (`Pageable`), thiếu tìm kiếm (`@Query`). Mỗi cái nêu rõ cách khắc phục.

### "Nếu demo lỗi?"
Nguyên nhân thường gặp: SQL Server chưa bật TCP/IP, sai mật khẩu, port 8080 bị chiếm. Cách khắc phục: kiểm tra SQL Server Configuration Manager, `netstat -ano | findstr :8080` để kill process.

---

## 11. Các File Code Quan Trọng — Vị Trí và Vai Trò

Dưới đây là danh sách các file code quan trọng nhất, vị trí và dòng code cần nhớ để trả lời câu hỏi:

### Entity (JPA Mapping — Quan trọng nhất)

| # | File | Vai trò | Dòng quan trọng |
|---|---|---|---|
| 1 | `entity/Classroom.java:55` | `@OneToMany(mappedBy = "classroom")` — phía "Một" | `mappedBy` chỉ định Student là owning side |
| 2 | `entity/Student.java:59-61` | `@ManyToOne` + `@JoinColumn(name = "class_id")` — phía "Nhiều" | Cột FK `class_id` trong bảng Students |

### Repository (Truy xuất DB)

| # | File | Vai trò | Dòng quan trọng |
|---|---|---|---|
| 3 | `repository/ClassroomRepository.java` | CRUD lớp học kế thừa từ `JpaRepository` | Không có method tùy chỉnh |
| 4 | `repository/StudentRepository.java:24` | `findByClassroomId(Long)` — Spring Data JPA tự sinh `WHERE class_id = ?` | Method query tự động, không cần `@Query` |

### Service (Logic nghiệp vụ)

| # | File | Vai trò |
|---|---|---|
| 5 | `service/ClassroomService.java` | Gọi `ClassroomRepository` — CRUD lớp |
| 6 | `service/StudentService.java` | Gọi `StudentRepository` — CRUD SV + lọc theo lớp |

### Controller (MVC — Luồng request)

| # | File | Vai trò | Dòng quan trọng |
|---|---|---|---|
| 7 | `controller/ClassroomController.java:38-44` | `GET /classes` — `model.addAttribute("classes", ...)` → `return "class-list"` | MVC flow mẫu: Model → View |
| 8 | `controller/StudentController.java` | `GET /students?classId=...` — lọc SV + `model.addAttribute("classes", ...)` | Gửi danh sách lớp vào dropdown |

### View (Thymeleaf)

| # | File | Vai trò | Dòng quan trọng |
|---|---|---|---|
| 9 | `templates/class-list.html:44-54` | `th:each="classroom : ${classes}"` + `th:text` + badge `classroom.students.size()` | Lặp danh sách, hiển thị quan hệ OneToMany |
| 10 | `templates/student-form.html:67-73` | Dropdown chọn lớp: `th:each="c : ${classes}"` + `th:selected` | Gán SV vào lớp qua form |

### Config

| # | File | Vai trò |
|---|---|---|
| 11 | `application.properties` | Kết nối SQL Server, `ddl-auto=update`, `show-sql=true` |
| 12 | `pom.xml` | Dependencies: spring-boot-starter-data-jpa, thymeleaf, lombok, sqlserver driver |

---

## 10. Những Điều KHÔNG Nên Làm

- Không đọc lại toàn bộ lý thuyết trong báo cáo
- Không trình bày định nghĩa chung chung (ORM là gì, JPA là gì) — phải gắn với demo
- Không chiếu code quá dài — chỉ 2-3 dòng tiêu biểu
- Không demo bằng ảnh chụp — phải chạy live
- Không dành quá nhiều thời gian giới thiệu nhóm — tối đa 30 giây
