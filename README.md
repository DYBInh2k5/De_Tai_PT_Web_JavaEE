# 🏫 Quản lý Lớp học & Sinh viên — JPA One-to-Many / Many-to-One

## 1. GIỚI THIỆU DỰ ÁN

Đề tài: **Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học** (Đề tài 07)

Dự án là một ứng dụng Web Java/Spring Boot đơn giản dùng để **quản lý lớp học và sinh viên**, minh họa cách ánh xạ quan hệ **One-to-Many** (một-nhiều) và **Many-to-One** (nhiều-một) trong JPA/Hibernate.

---

## 2. CÔNG NGHỆ SỬ DỤNG

| Công nghệ | Mục đích |
|-----------|----------|
| **Java 17** | Ngôn ngữ lập trình |
| **Spring Boot 3.2.5** | Framework xây dựng ứng dụng Web |
| **Spring Data JPA / Hibernate** | ORM — ánh xạ đối tượng Java sang bảng CSDL |
| **SQL Server** | Cơ sở dữ liệu quan hệ |
| **Thymeleaf** | Template engine — viết HTML nhúng dữ liệu Java |
| **Bootstrap 5.3** | Thư viện CSS/JS làm giao diện đẹp, responsive |
| **Lombok** | Giảm code boilerplate (getter, setter, constructor) |
| **Maven** | Build tool, quản lý dependencies |

---

## 3. YÊU CẦU HỆ THỐNG

- **JDK 17+**
- **SQL Server 2019+** (đã cài đặt và có tài khoản `sa`)
- **Maven 3.6+** (hoặc dùng Maven Wrapper có sẵn)
- **IDE**: IntelliJ IDEA / VS Code / Eclipse (khuyến nghị IntelliJ)

---

## 4. CẤU TRÚC THƯ MỤC DỰ ÁN

```
classroom-management/
│
├── pom.xml                              # Cấu hình Maven (dependencies, build)
├── init-db.sql                          # Script SQL tạo database + dữ liệu mẫu
├── README.md                            # File hướng dẫn (chính là file này)
│
├── src/main/java/com/ttjavaee/classroom/
│   ├── ClassroomManagementApplication.java   # Lớp chính khởi động Spring Boot
│   │
│   ├── entity/
│   │   ├── Classroom.java                    # Entity Lớp học (One - bên một)
│   │   └── Student.java                      # Entity Sinh viên (Many - bên nhiều)
│   │
│   ├── repository/
│   │   ├── ClassroomRepository.java          # Repository cho Classroom
│   │   └── StudentRepository.java            # Repository cho Student
│   │
│   ├── service/
│   │   ├── ClassroomService.java             # Business logic cho Lớp học
│   │   └── StudentService.java               # Business logic cho Sinh viên
│   │
│   └── controller/
│       ├── HomeController.java               # Điều hướng trang chủ
│       ├── ClassroomController.java          # Xử lý request về Lớp học
│       └── StudentController.java            # Xử lý request về Sinh viên
│
├── src/main/resources/
│   ├── application.properties                # Cấu hình datasource, JPA, server
│   │
│   └── templates/                            # Giao diện HTML (Thymeleaf)
│       ├── layout.html                       # Layout chung (navbar + scripts)
│       ├── class-list.html                   # Danh sách lớp học
│       ├── class-form.html                   # Form thêm/sửa lớp học
│       ├── class-students.html               # Sinh viên theo lớp
│       ├── student-list.html                 # Danh sách sinh viên (có lọc)
│       └── student-form.html                 # Form thêm/sửa sinh viên
│
└── docs/                                     # Tài liệu báo cáo (hỗ trợ làm .docx)
```

---

## 5. CHỨC NĂNG CỦA TỪNG FILE CODE

### 5.1. Entity Layer (Lớp thực thể — ánh xạ bảng CSDL)

#### 📄 `Classroom.java`
- **Vai trò**: Đại diện cho bảng `Classes` trong SQL Server.
- **Mỗi lớp** có: `id` (khóa chính), `classCode` (mã lớp, unique), `className` (tên lớp).
- **Quan hệ**: `@OneToMany` — một Classroom có nhiều Student.
  - `mappedBy = "classroom"`: bên Student giữ khóa ngoại.
  - `cascade = {PERSIST, MERGE}`: khi lưu/sửa Classroom, các Student trong danh sách cũng được lưu/sửa theo.
  - `fetch = LAZY`: danh sách Student chỉ được load từ DB khi thực sự gọi `.getStudents()`.
- **Lombok**: `@Getter` `@Setter` `@NoArgsConstructor` `@AllArgsConstructor` tự sinh getter/setter/constructor.

#### 📄 `Student.java`
- **Vai trò**: Đại diện cho bảng `Students` trong SQL Server.
- **Mỗi sinh viên** có: `id`, `studentCode` (mã SV, unique), `fullName`, `email`.
- **Quan hệ**: `@ManyToOne` — nhiều Student thuộc về một Classroom.
  - `@JoinColumn(name = "class_id")`: cột `class_id` trong bảng Students là **khóa ngoại** tham chiếu đến `Classes.id`.
  - Đây là **owning side** (bên chủ quan hệ) — bên giữ khóa ngoại.

---

### 5.2. Repository Layer (Lớp truy cập dữ liệu)

#### 📄 `ClassroomRepository.java`
- Kế thừa `JpaRepository<Classroom, Long>`.
- **Không cần viết method**: Spring Data JPA tự động cung cấp `findAll()`, `findById()`, `save()`, `deleteById()`, `count()`.

#### 📄 `StudentRepository.java`
- Kế thừa `JpaRepository<Student, Long>`.
- **Method tự định nghĩa**: `findByClassroomId(Long classroomId)` — Spring Data JPA tự động phân tích tên method và sinh câu SQL: `SELECT * FROM Students WHERE class_id = ?`.

---

### 5.3. Service Layer (Lớp nghiệp vụ)

#### 📄 `ClassroomService.java`
- **Các method**:
  - `getAllClassrooms()` → lấy tất cả lớp.
  - `getClassroomById(id)` → tìm lớp theo id (trả null nếu không có).
  - `saveClassroom(classroom)` → thêm mới (id=null) hoặc cập nhật (id≠null).
  - `deleteClassroom(id)` → xóa lớp. **Lưu ý**: DB có `ON DELETE SET NULL` nên sinh viên trong lớp bị gỡ lớp chứ không bị xóa.

#### 📄 `StudentService.java`
- **Các method**:
  - `getAllStudents()` → lấy tất cả sinh viên.
  - `getStudentsByClassroom(classroomId)` → lọc sinh viên theo lớp.
  - `getStudentById(id)` → tìm sinh viên theo id.
  - `saveStudent(student)` → thêm/sửa sinh viên.
  - `deleteStudent(id)` → xóa sinh viên.

---

### 5.4. Controller Layer (Lớp điều khiển)

#### 📄 `HomeController.java`
- `GET /` → redirect (chuyển hướng) đến `/classes`.

#### 📄 `ClassroomController.java`
- `GET /classes` → hiển thị danh sách lớp + số lượng sinh viên từng lớp.
- `GET /classes/add` → hiển thị form thêm lớp mới.
- `POST /classes/save` → lưu lớp (thêm mới hoặc cập nhật).
- `GET /classes/edit/{id}` → hiển thị form sửa lớp (dữ liệu đã điền sẵn).
- `POST /classes/delete/{id}` → xóa lớp.
- `GET /classes/{id}/students` → hiển thị sinh viên theo lớp.

#### 📄 `StudentController.java`
- `GET /students` → hiển thị danh sách sinh viên.
  - Có tham số `?classId=...` để lọc theo lớp.
- `GET /students/add` → hiển thị form thêm sinh viên (có dropdown chọn lớp).
- `POST /students/save` → lưu sinh viên (nhận `classroomId` riêng từ form).
- `GET /students/edit/{id}` → hiển thị form sửa sinh viên.
- `POST /students/delete/{id}` → xóa sinh viên.

---

### 5.5. Cấu hình & Giao diện

#### 📄 `application.properties`
- Cấu hình kết nối SQL Server (`localhost:1433`, database `ClassroomDB`).
- `spring.jpa.hibernate.ddl-auto=update`: Hibernate tự động tạo/cập nhật bảng khi chạy.
- `spring.jpa.show-sql=true`: in câu SQL ra console để debug.

#### 📄 `pom.xml`
- Khai báo 6 dependencies chính: `spring-boot-starter-data-jpa`, `spring-boot-starter-thymeleaf`, `spring-boot-starter-web`, `mssql-jdbc`, `lombok`, `spring-boot-starter-test`.

#### 📄 Các file HTML trong `templates/`
- **`layout.html`**: định nghĩa navbar chung và script Bootstrap — các trang khác tái sử dụng qua `th:replace="~{layout :: ...}"`.
- **`class-list.html`**: bảng danh sách lớp, cột "Số SV" dùng `classroom.students.size()` — minh họa duyệt quan hệ `@OneToMany` từ Classroom → Student.
- **`class-form.html`**: form thêm/sửa lớp, tự động đổi tiêu đề "Thêm" ↔ "Sửa" dựa trên `classroom.id == null`.
- **`class-students.html`**: danh sách sinh viên của một lớp cụ thể.
- **`student-list.html`**: danh sách sinh viên + bộ lọc dropdown theo lớp. Cột "Lớp học" dùng `student.classroom.className` — minh họa duyệt quan hệ `@ManyToOne` từ Student → Classroom.
- **`student-form.html`**: form thêm/sửa sinh viên + dropdown chọn lớp (`name="classroomId"`).

---

## 6. QUAN HỆ JPA ONE-TO-MANY VÀ MANY-TO-ONE

### 6.1. Mô hình quan hệ trong CSDL

```
┌──────────────────┐          ┌──────────────────────┐
│     Classes      │          │      Students         │
├──────────────────┤          ├──────────────────────┤
│ id (PK)          │◄─────────│ class_id (FK)        │
│ class_code       │    1   * │ student_code          │
│ class_name       │          │ full_name             │
└──────────────────┘          │ email                 │
                              └──────────────────────┘
```

- **Classes** (1) ────< **Students** (nhiều): Một lớp có nhiều sinh viên.
- Khóa ngoại `class_id` trong bảng `Students` tham chiếu đến khóa chính `id` của bảng `Classes`.

### 6.2. Ánh xạ trong JPA

| Bên | Annotation | Giải thích |
|-----|-----------|------------|
| `Classroom.java` | `@OneToMany(mappedBy = "classroom")` | Một Classroom có danh sách Student. `mappedBy` nói JPA: bên Student có field "classroom" là chủ quan hệ. |
| `Student.java` | `@ManyToOne` | Nhiều Student thuộc về một Classroom. |
| `Student.java` | `@JoinColumn(name = "class_id")` | Chỉ rõ cột khóa ngoại trong bảng Students là `class_id`. |

### 6.3. Luồng hoạt động

1. **Tạo lớp mới**: Form nhập `classCode`, `className` → POST `/classes/save` → ClassroomService → ClassroomRepository.save() → INSERT INTO Classes.
2. **Tạo sinh viên và gán lớp**: Form nhập thông tin SV + chọn lớp từ dropdown → POST `/students/save` → StudentController tìm Classroom theo `classroomId`, gán `student.setClassroom(classroom)` → StudentRepository.save() → INSERT INTO Students với `class_id` tương ứng.
3. **Xem danh sách sinh viên theo lớp**: GET `/classes/{id}/students` → StudentRepository.findByClassroomId(id) → `SELECT * FROM Students WHERE class_id = ?`.
4. **Xem số lượng sinh viên của lớp**: Trong class-list.html gọi `${classroom.students.size()}` → Hibernate tự động load danh sách Student của Classroom đó (LAZY loading) và đếm số lượng.
5. **Xóa lớp**: POST `/classes/delete/{id}` → DELETE FROM Classes WHERE id = ? → DB tự động SET NULL `class_id` của các sinh viên trong lớp (nhờ `ON DELETE SET NULL`).

---

## 7. HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY

### Bước 1: Tạo Database

1. Mở **SQL Server Management Studio (SSMS)**.
2. Đăng nhập với tài khoản `sa`.
3. File → Open → Mở file `init-db.sql`.
4. Execute (F5) để chạy script.
   - Script sẽ tạo database `ClassroomDB` (nếu chưa có).
   - Tạo bảng `Classes` và `Students`.
   - Tạo ràng buộc khóa ngoại `FK_Student_Class`.
   - Chèn dữ liệu mẫu (3 lớp, 6 sinh viên).

### Bước 2: Cấu hình kết nối

Mở file `src/main/resources/application.properties`, kiểm tra và sửa:

```properties
spring.datasource.password=1    # ← Sửa mật khẩu SQL Server của bạn vào đây
```

### Bước 3: Chạy ứng dụng

**Cách 1 — Maven Wrapper (không cần cài Maven):**
```bash
mvnw spring-boot:run
```

**Cách 2 — Maven:**
```bash
mvn spring-boot:run
```

**Cách 3 — IDE:**
- Mở project trong IntelliJ IDEA.
- Chạy file `ClassroomManagementApplication.java` (Run 'main()').

### Bước 4: Truy cập

Mở trình duyệt: [http://localhost:8080](http://localhost:8080)

---

## 8. CÁC CHỨC NĂNG DEMO

| STT | Chức năng | URL | Mô tả |
|-----|-----------|-----|-------|
| 1 | Danh sách lớp học | `GET /classes` | Bảng hiển thị tất cả lớp + số sinh viên từng lớp |
| 2 | Thêm lớp học | `GET /classes/add` | Form nhập mã lớp, tên lớp |
| 3 | Sửa lớp học | `GET /classes/edit/{id}` | Form điền sẵn dữ liệu cũ |
| 4 | Xóa lớp học | `POST /classes/delete/{id}` | Xóa lớp, SV bị gỡ lớp (SET NULL) |
| 5 | Sinh viên theo lớp | `GET /classes/{id}/students` | Danh sách SV thuộc một lớp |
| 6 | Danh sách sinh viên | `GET /students` | Tất cả SV + dropdown lọc theo lớp |
| 7 | Lọc sinh viên theo lớp | `GET /students?classId=X` | Chỉ hiển thị SV thuộc lớp X |
| 8 | Thêm sinh viên | `GET /students/add` | Form nhập thông tin + chọn lớp |
| 9 | Sửa sinh viên | `GET /students/edit/{id}` | Form điền sẵn + chọn lớp |
| 10 | Xóa sinh viên | `POST /students/delete/{id}` | Xóa sinh viên khỏi DB |

---

## 9. CẤU TRÚC MVC (Model-View-Controller)

```
┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────────┐     ┌──────────┐
│  Browser │────▶│Controller│────▶│ Service  │────▶│  Repository  │────▶│    DB    │
│ (Client) │     │  (Java)  │     │  (Java)  │     │  (JPA/Spring)│     │ SQLServer│
└──────────┘     └──────────┘     └──────────┘     └──────────────┘     └──────────┘
     ▲                │                                                        
     │                ▼                                                        
     │          ┌──────────┐                                                   
     └──────────│   View   │                                                   
                │ (HTML +  │                                                   
                │Thymeleaf)│                                                   
                └──────────┘
```

**Controller**: Nhận request từ trình duyệt, gọi Service, đưa dữ liệu vào Model, trả về tên template.

**Service**: Xử lý nghiệp vụ, gọi Repository.

**Repository**: Giao tiếp với DB, thực thi câu lệnh SQL.

**View (Template)**: Thymeleaf render HTML với dữ liệu từ Model.

---

## 10. GIẢI THÍCH CÁC KHÁI NIỆM JPA QUAN TRỌNG

### mappedBy
- Đặt ở bên `@OneToMany` (Classroom).
- `mappedBy = "classroom"`: nói với JPA rằng bên Student có field "classroom" là **chủ sở hữu quan hệ**.
- Bên chủ sở hữu (Student) là bên có khóa ngoại và chịu trách nhiệm cập nhật khóa ngoại.

### cascade
- **CascadeType.PERSIST**: Khi lưu Classroom, nếu có Student mới trong danh sách, tự động lưu Student luôn.
- **CascadeType.MERGE**: Khi cập nhật Classroom, tự động cập nhật Student.
- **KHÔNG dùng REMOVE**: Vì DB đã có `ON DELETE SET NULL`.

### fetch = LAZY
- Khi load Classroom, danh sách Student **chưa được load ngay** từ DB.
- Chỉ load khi thực sự gọi `classroom.getStudents()`.
- Tiết kiệm tài nguyên, tránh N+1 query không mong muốn.

### @JoinColumn
- Đặt ở bên `@ManyToOne` (Student) — bên có khóa ngoại.
- `name = "class_id"`: tên cột khóa ngoại trong bảng Students.

---

## 11. XỬ LÝ KHI XÓA LỚP CÓ SINH VIÊN

CSDL được thiết kế với ràng buộc `ON DELETE SET NULL`:

```sql
CONSTRAINT FK_Student_Class
    FOREIGN KEY (class_id) REFERENCES Classes(id)
    ON DELETE SET NULL
```

Khi xóa một lớp (DELETE FROM Classes WHERE id = X), SQL Server tự động:
- Set `class_id = NULL` cho tất cả sinh viên thuộc lớp đó.
- Sinh viên **không bị xóa**, chỉ bị gỡ khỏi lớp.

Đây là lựa chọn hợp lý vì sinh viên vẫn tồn tại trong hệ thống, có thể được gán lại vào lớp khác sau này.
