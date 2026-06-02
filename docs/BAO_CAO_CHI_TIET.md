# TRANG BÌA

---

**TRƯỜNG ĐẠI HỌC / KHOA CÔNG NGHỆ THÔNG TIN**
*(Điền tên trường và khoa của bạn)*

---

**MÔN HỌC:** Phát triển ứng dụng Web Java EE

---

# BÁO CÁO ĐỀ TÀI

## Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học

**Đề tài số:** 07

---

**DANH SÁCH THÀNH VIÊN NHÓM:**

| STT | Họ và tên | MSSV | Vai trò |
|-----|-----------|------|---------|
| 1   | *(Họ tên thành viên 1)* | *(MSSV)* | Nhóm trưởng |
| 2   | *(Họ tên thành viên 2)* | *(MSSV)* | Thành viên |
| 3   | *(Họ tên thành viên 3)* | *(MSSV)* | Thành viên |

---

**GIẢNG VIÊN HƯỚNG DẪN:** *(Tên giảng viên)*

**HỌC KỲ / NĂM HỌC:** *(VD: Học kỳ 2 - Năm học 2024-2025)*

**NGÀY NỘP:** *(Ngày/Tháng/Năm)*

---
# BÁO CÁO ĐỀ TÀI 07
## Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học

---

# CHƯƠNG 1: GIỚI THIỆU ĐỀ TÀI

## 1.1. Lý do chọn đề tài

Trong phát triển ứng dụng Web hiện đại, việc quản lý dữ liệu có quan hệ là một yêu cầu cốt lõi. Hầu hết các hệ thống thực tế — từ quản lý nhân sự, bán hàng đến giáo dục — đều có các bảng dữ liệu liên kết với nhau thông qua khóa ngoại.

Tuy nhiên, lập trình viên Java thường gặp khó khăn khi phải "dịch" mô hình quan hệ của cơ sở dữ liệu sang mô hình đối tượng của Java. Đây chính là bài toán **Object-Relational Impedance Mismatch** — sự không tương thích giữa hai mô hình.

JPA/Hibernate ra đời để giải quyết vấn đề này, cho phép lập trình viên làm việc với dữ liệu quan hệ hoàn toàn bằng ngôn ngữ hướng đối tượng. Nhóm chọn đề tài này để hiểu sâu cơ chế ánh xạ quan hệ, đặc biệt là **One-to-Many** và **Many-to-One** — hai quan hệ phổ biến nhất trong thực tế.

## 1.2. Mục tiêu đề tài

- Hiểu và giải thích được quan hệ One-to-Many và Many-to-One trong JPA/Hibernate.
- Nắm vững các annotation: `@OneToMany`, `@ManyToOne`, `@JoinColumn`, `mappedBy`, `cascade`.
- Xây dựng ứng dụng Web quản lý lớp học và sinh viên sử dụng Spring Boot + SQL Server.
- Thực hiện đầy đủ CRUD cho cả hai thực thể có quan hệ với nhau.
- Hiển thị dữ liệu quan hệ trên giao diện Web bằng Thymeleaf và Bootstrap.

---

# CHƯƠNG 2: CƠ SỞ LÝ THUYẾT

## 2.1. Tổng quan về ORM và JPA/Hibernate

### 2.1.1. ORM là gì?

**ORM (Object-Relational Mapping)** là kỹ thuật ánh xạ giữa mô hình đối tượng (Object Model) trong ngôn ngữ lập trình hướng đối tượng và mô hình quan hệ (Relational Model) trong cơ sở dữ liệu.

| Mô hình Quan hệ (SQL) | Mô hình Đối tượng (Java) |
|---|---|
| Bảng (Table) | Lớp (Class) |
| Hàng (Row) | Đối tượng (Object) |
| Cột (Column) | Thuộc tính (Field/Property) |
| Khóa ngoại (Foreign Key) | Tham chiếu đối tượng (Object Reference) |
| JOIN giữa các bảng | Điều hướng qua thuộc tính |

**Vấn đề không tương thích (Impedance Mismatch):**
- SQL không hỗ trợ kế thừa, đa hình — Java thì có.
- SQL kết nối bảng qua khóa ngoại — Java kết nối đối tượng qua tham chiếu.
- SQL trả về tập hợp dữ liệu phẳng — Java làm việc với đồ thị đối tượng.

ORM giải quyết sự không tương thích này bằng cách tự động chuyển đổi giữa hai mô hình.

### 2.1.2. JPA là gì?

**JPA (Java Persistence API)** là đặc tả (specification) chuẩn của Java EE/Jakarta EE định nghĩa cách ánh xạ đối tượng Java với cơ sở dữ liệu quan hệ. JPA chỉ là **interface/specification**, không phải implementation.

### 2.1.3. Hibernate là gì?

**Hibernate** là implementation phổ biến nhất của JPA. Khi dùng Spring Boot với `spring-boot-starter-data-jpa`, Hibernate được sử dụng mặc định làm JPA provider.

```
Ứng dụng Java
    ↓ (dùng)
JPA API (jakarta.persistence)
    ↓ (implement bởi)
Hibernate
    ↓ (kết nối)
SQL Server
```

## 2.2. Quan hệ trong cơ sở dữ liệu

### 2.2.1. Khóa chính và khóa ngoại

**Khóa chính (Primary Key - PK):**
- Là cột xác định duy nhất mỗi hàng trong bảng.
- Không được NULL, không được trùng lặp.
- Ví dụ: `id` trong bảng `Classes` và `Students`.

**Khóa ngoại (Foreign Key - FK):**
- Là cột trong bảng con tham chiếu đến khóa chính của bảng cha.
- Đảm bảo tính toàn vẹn tham chiếu (Referential Integrity).
- Ví dụ: `class_id` trong bảng `Students` tham chiếu đến `id` trong bảng `Classes`.

### 2.2.2. Quan hệ One-to-Many (Một - Nhiều)

Quan hệ One-to-Many xảy ra khi **một bản ghi** ở bảng A có thể liên kết với **nhiều bản ghi** ở bảng B, nhưng mỗi bản ghi ở bảng B chỉ liên kết với **một bản ghi** ở bảng A.

**Ví dụ thực tế:**
- Một lớp học có nhiều sinh viên.
- Một khách hàng có nhiều đơn hàng.
- Một tác giả có nhiều bài viết.
- Một phòng ban có nhiều nhân viên.

**Biểu diễn trong SQL:**
```sql
-- Bảng cha (phía "Một")
CREATE TABLE Classes (
    id         BIGINT PRIMARY KEY,
    class_code NVARCHAR(50),
    class_name NVARCHAR(255)
);

-- Bảng con (phía "Nhiều") - chứa khóa ngoại
CREATE TABLE Students (
    id           BIGINT PRIMARY KEY,
    student_code NVARCHAR(50),
    full_name    NVARCHAR(255),
    email        NVARCHAR(255),
    class_id     BIGINT REFERENCES Classes(id)  -- Khóa ngoại
);
```

### 2.2.3. Quan hệ Many-to-One (Nhiều - Một)

Many-to-One là **góc nhìn ngược lại** của One-to-Many, nhìn từ phía bảng con:
- Nhiều sinh viên cùng thuộc về một lớp học.

Trong JPA, cùng một quan hệ vật lý trong database nhưng được biểu diễn bằng hai annotation khác nhau tùy theo góc nhìn:
- Từ `Classroom` nhìn sang `Student` → `@OneToMany`
- Từ `Student` nhìn sang `Classroom` → `@ManyToOne`

## 2.3. Mapping quan hệ trong JPA

### 2.3.1. Annotation @Entity và @Table

```java
@Entity                        // Đánh dấu class này là JPA Entity
@Table(name = "Classes")       // Chỉ định tên bảng trong DB
public class Classroom { ... }
```

### 2.3.2. Annotation @Id và @GeneratedValue

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự tăng (IDENTITY trong SQL Server)
private Long id;
```

| Strategy | Mô tả |
|---|---|
| `IDENTITY` | Dùng cột AUTO_INCREMENT/IDENTITY của DB |
| `SEQUENCE` | Dùng sequence của DB (Oracle, PostgreSQL) |
| `TABLE` | Dùng bảng phụ để quản lý ID |
| `AUTO` | JPA tự chọn chiến lược phù hợp |

### 2.3.3. Annotation @OneToMany

Đặt ở phía **"Một"** (Classroom), trỏ đến danh sách phía **"Nhiều"** (Student):

```java
@OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Student> students;
```

| Thuộc tính | Giá trị | Ý nghĩa |
|---|---|---|
| `mappedBy` | `"classroom"` | Tên thuộc tính trong Student sở hữu quan hệ |
| `cascade` | `CascadeType.ALL` | Mọi thao tác trên Classroom lan truyền sang Student |
| `fetch` | `FetchType.LAZY` | Chỉ load danh sách Student khi thực sự cần |

**Các giá trị CascadeType:**

| Giá trị | Ý nghĩa |
|---|---|
| `PERSIST` | Khi lưu Classroom, tự động lưu Student liên quan |
| `MERGE` | Khi cập nhật Classroom, tự động cập nhật Student |
| `REMOVE` | Khi xóa Classroom, tự động xóa Student |
| `REFRESH` | Khi refresh Classroom, tự động refresh Student |
| `ALL` | Áp dụng tất cả các cascade trên |

**FetchType:**

| Giá trị | Ý nghĩa | Mặc định cho |
|---|---|---|
| `LAZY` | Load dữ liệu liên quan khi truy cập lần đầu | `@OneToMany`, `@ManyToMany` |
| `EAGER` | Load dữ liệu liên quan ngay khi load entity cha | `@ManyToOne`, `@OneToOne` |

### 2.3.4. Annotation @ManyToOne và @JoinColumn

Đặt ở phía **"Nhiều"** (Student), trỏ về phía **"Một"** (Classroom):

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "class_id")   // Tên cột khóa ngoại trong bảng Students
private Classroom classroom;
```

`@JoinColumn` chỉ định:
- `name`: Tên cột khóa ngoại trong bảng hiện tại (`Students.class_id`)
- `referencedColumnName`: Tên cột được tham chiếu ở bảng cha (mặc định là PK)
- `nullable`: Có cho phép NULL không (mặc định `true`)

### 2.3.5. Khái niệm "Owning Side" và "mappedBy"

Trong quan hệ hai chiều (bidirectional), JPA cần biết **bên nào sở hữu quan hệ** (owning side) — tức là bên nào chịu trách nhiệm cập nhật khóa ngoại trong DB.

- **Owning side**: Bên có `@JoinColumn` → là `Student` (vì bảng `Students` chứa cột `class_id`)
- **Inverse side**: Bên có `mappedBy` → là `Classroom`

`mappedBy = "classroom"` có nghĩa: *"Quan hệ này được quản lý bởi thuộc tính `classroom` trong class `Student`"*

> **Lưu ý quan trọng:** Nếu chỉ set `classroom.getStudents().add(student)` mà không set `student.setClassroom(classroom)`, Hibernate sẽ **không** cập nhật khóa ngoại vào DB vì `Student` mới là owning side.

---

# CHƯƠNG 3: PHÂN TÍCH VÀ THIẾT KẾ HỆ THỐNG

## 3.1. Mô tả chức năng

**Quản lý Lớp học:**

| Chức năng | Mô tả |
|---|---|
| Xem danh sách lớp | Hiển thị tất cả lớp học kèm số lượng sinh viên |
| Thêm lớp mới | Nhập mã lớp và tên lớp |
| Sửa thông tin lớp | Cập nhật mã lớp hoặc tên lớp |
| Xóa lớp | Xóa lớp (sinh viên trong lớp sẽ được set class_id = NULL) |
| Xem sinh viên theo lớp | Xem danh sách sinh viên thuộc một lớp cụ thể |

**Quản lý Sinh viên:**

| Chức năng | Mô tả |
|---|---|
| Xem danh sách sinh viên | Hiển thị tất cả sinh viên kèm tên lớp |
| Lọc theo lớp | Lọc danh sách sinh viên theo lớp học |
| Thêm sinh viên | Nhập thông tin và chọn lớp từ dropdown |
| Sửa thông tin sinh viên | Cập nhật thông tin hoặc chuyển lớp |
| Xóa sinh viên | Xóa sinh viên khỏi hệ thống |

## 3.2. Sơ đồ luồng xử lý

**Luồng thêm sinh viên mới:**
```
Người dùng
    │
    ▼
[Truy cập /students/add]
    │
    ▼
StudentController.showAddForm()
    │  Lấy danh sách lớp từ DB
    ▼
ClassroomService.getAllClassrooms()
    │
    ▼
[Hiển thị student-form.html với dropdown chọn lớp]
    │
    │ Người dùng điền form và Submit
    ▼
[POST /students/save]
    │
    ▼
StudentController.saveStudent()
    │  Nhận classroomId từ form
    │  Lookup Classroom từ DB
    │  Gán classroom vào student
    ▼
StudentService.saveStudent()
    │
    ▼
StudentRepository.save()
    │  Hibernate: INSERT INTO Students (class_id = ?)
    ▼
[Redirect về /students]
```

**Luồng xóa lớp học:**
```
Người dùng click "Xóa"
    │
    ▼
[GET /classes/delete/{id}]
    │
    ▼
ClassroomController.deleteClass()
    │
    ▼
ClassroomService.deleteClassroom()
    │
    ▼
ClassroomRepository.deleteById()
    │  SQL Server: ON DELETE SET NULL
    │  → class_id của sinh viên trong lớp = NULL
    ▼
[Redirect về /classes]
```

## 3.3. Thiết kế cơ sở dữ liệu

### 3.3.1. Sơ đồ ERD

```
┌─────────────────────┐          ┌──────────────────────────┐
│       Classes        │          │         Students          │
├─────────────────────┤          ├──────────────────────────┤
│ id (PK, BIGINT)     │◄────────┤ id (PK, BIGINT)           │
│ class_code (UNIQUE) │  1    N  │ student_code (UNIQUE)     │
│ class_name          │          │ full_name                 │
└─────────────────────┘          │ email                     │
                                  │ class_id (FK → Classes.id)│
                                  └──────────────────────────┘
```

### 3.3.2. Mô tả chi tiết bảng

**Bảng Classes:**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| id | BIGINT | PK, IDENTITY(1,1) | Khóa chính tự tăng |
| class_code | NVARCHAR(50) | NOT NULL, UNIQUE | Mã lớp (duy nhất) |
| class_name | NVARCHAR(255) | NOT NULL | Tên lớp học |

**Bảng Students:**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| id | BIGINT | PK, IDENTITY(1,1) | Khóa chính tự tăng |
| student_code | NVARCHAR(50) | NOT NULL, UNIQUE | Mã sinh viên (duy nhất) |
| full_name | NVARCHAR(255) | NOT NULL | Họ và tên |
| email | NVARCHAR(255) | NOT NULL | Địa chỉ email |
| class_id | BIGINT | FK, NULL | Khóa ngoại → Classes(id) |

### 3.3.3. Ràng buộc khóa ngoại và hành vi khi xóa

```sql
CONSTRAINT FK_Student_Class
    FOREIGN KEY (class_id) REFERENCES Classes(id)
    ON DELETE SET NULL    -- Khi xóa lớp, class_id của SV = NULL
    ON UPDATE CASCADE     -- Khi cập nhật id lớp, tự cập nhật class_id
```

**Kiểm tra hành vi khi xóa lớp có sinh viên:**
```sql
-- Trước khi xóa lớp CNTT01 (id=1)
SELECT * FROM Students WHERE class_id = 1;
-- Kết quả: SV001, SV002, SV005 có class_id = 1

DELETE FROM Classes WHERE id = 1;

-- Sau khi xóa
SELECT * FROM Students WHERE student_code IN ('SV001','SV002','SV005');
-- Kết quả: class_id của 3 sinh viên này = NULL (không bị xóa theo)
```

## 3.4. Kiến trúc chương trình (MVC + Layered Architecture)

```
┌─────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                 │
│         Thymeleaf Templates (HTML + Bootstrap)       │
│  class-list  class-form  student-list  student-form  │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP Request/Response
┌──────────────────────▼──────────────────────────────┐
│                   CONTROLLER LAYER                   │
│         ClassroomController  StudentController       │
└──────────────────────┬──────────────────────────────┘
                       │ Method calls
┌──────────────────────▼──────────────────────────────┐
│                    SERVICE LAYER                     │
│          ClassroomService  StudentService            │
└──────────────────────┬──────────────────────────────┘
                       │ Repository calls
┌──────────────────────▼──────────────────────────────┐
│                  REPOSITORY LAYER                    │
│       ClassroomRepository  StudentRepository         │
│              (Spring Data JPA)                       │
└──────────────────────┬──────────────────────────────┘
                       │ SQL (Hibernate)
┌──────────────────────▼──────────────────────────────┐
│                   DATABASE LAYER                     │
│              SQL Server - ClassroomDB                │
│              Tables: Classes, Students               │
└─────────────────────────────────────────────────────┘
```

---

# CHƯƠNG 4: CÀI ĐẶT CHƯƠNG TRÌNH

## 4.1. Cấu trúc project

```
De_Tai_PT_Web_JavaEE/
├── pom.xml                                    ← Cấu hình Maven, dependencies
├── init-db.sql                                ← Script tạo DB và dữ liệu mẫu
├── README.md                                  ← Hướng dẫn chạy chương trình
└── src/
    └── main/
        ├── java/com/ttjavaee/classroom/
        │   ├── ClassroomManagementApplication.java   ← Main class (@SpringBootApplication)
        │   ├── controller/
        │   │   ├── HomeController.java               ← Redirect / → /classes
        │   │   ├── ClassroomController.java          ← CRUD lớp học
        │   │   └── StudentController.java            ← CRUD sinh viên
        │   ├── entity/
        │   │   ├── Classroom.java                    ← Entity @OneToMany
        │   │   └── Student.java                      ← Entity @ManyToOne
        │   ├── repository/
        │   │   ├── ClassroomRepository.java          ← JPA Repository
        │   │   └── StudentRepository.java            ← JPA Repository + custom query
        │   └── service/
        │       ├── ClassroomService.java             ← Business logic
        │       └── StudentService.java               ← Business logic
        └── resources/
            ├── application.properties               ← Cấu hình DB, JPA, server
            └── templates/
                ├── layout.html                      ← Navbar fragment dùng chung
                ├── class-list.html                  ← Danh sách lớp học
                ├── class-form.html                  ← Form thêm/sửa lớp
                ├── class-students.html              ← Sinh viên theo lớp
                ├── student-list.html                ← Danh sách sinh viên
                └── student-form.html                ← Form thêm/sửa sinh viên
```

## 4.2. Cấu hình dependencies (pom.xml)

```xml
<dependencies>
    <!-- Spring Data JPA: tích hợp Hibernate -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Thymeleaf: template engine cho View -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    <!-- Spring MVC: xử lý HTTP request -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- SQL Server JDBC Driver -->
    <dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok: tự sinh getter/setter/constructor -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## 4.3. Cấu hình kết nối database (application.properties)

```properties
# Kết nối SQL Server
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourPassword123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect

# Thymeleaf
spring.thymeleaf.cache=false
spring.thymeleaf.encoding=UTF-8

# Server
server.port=8080
```

**Giải thích `ddl-auto`:**

| Giá trị | Hành vi |
|---|---|
| `none` | Không làm gì với schema |
| `validate` | Kiểm tra schema có khớp Entity không |
| `update` | Cập nhật schema nếu cần (dùng khi dev) |
| `create` | Xóa và tạo lại schema mỗi lần khởi động |
| `create-drop` | Tạo khi start, xóa khi stop |

## 4.4. Mô tả Entity (Trọng tâm đề tài)

### 4.4.1. Entity Classroom.java

```java
@Entity
@Table(name = "Classes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String classCode;       // Ánh xạ → cột class_code

    @Column(nullable = false)
    private String className;       // Ánh xạ → cột class_name

    // Quan hệ One-to-Many: 1 lớp có nhiều sinh viên
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students;
}
```

**Giải thích chi tiết:**
- `@Table(name = "Classes")`: Ánh xạ class `Classroom` với bảng `Classes` trong DB.
- `classCode` → Hibernate tự chuyển camelCase thành `class_code` (snake_case) trong SQL.
- `@OneToMany(mappedBy = "classroom")`: Khai báo quan hệ một-nhiều. `mappedBy = "classroom"` chỉ ra rằng thuộc tính `classroom` trong class `Student` là owning side — Student chịu trách nhiệm cập nhật khóa ngoại.
- `cascade = CascadeType.ALL`: Mọi thao tác trên Classroom đều lan truyền sang Student.
- `fetch = FetchType.LAZY`: Danh sách sinh viên chỉ được load khi gọi `getStudents()`, tránh load dữ liệu không cần thiết.

### 4.4.2. Entity Student.java

```java
@Entity
@Table(name = "Students")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String studentCode;     // Ánh xạ → cột student_code

    @Column(nullable = false)
    private String fullName;        // Ánh xạ → cột full_name

    @Column(nullable = false)
    private String email;

    // Quan hệ Many-to-One: nhiều sinh viên thuộc 1 lớp
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")  // Cột khóa ngoại trong bảng Students
    private Classroom classroom;
}
```

**Giải thích chi tiết:**
- `@ManyToOne`: Khai báo quan hệ nhiều-một từ Student về Classroom.
- `@JoinColumn(name = "class_id")`: Chỉ định cột `class_id` trong bảng `Students` là khóa ngoại tham chiếu đến `Classes.id`.
- `fetch = FetchType.LAZY`: Thông tin lớp học chỉ được load khi cần, tránh N+1 query problem.

**Tóm tắt quan hệ hai chiều:**
```
Classroom                              Student
─────────                              ───────
@OneToMany                             @ManyToOne
mappedBy = "classroom"  ←──────────── @JoinColumn(name = "class_id")
List<Student> students                 Classroom classroom
         (inverse side)                       (owning side)
```

## 4.5. Mô tả Repository

### ClassroomRepository.java

```java
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    // JpaRepository cung cấp sẵn các method:
    // findAll()        → SELECT * FROM Classes
    // findById(id)     → SELECT * FROM Classes WHERE id = ?
    // save(entity)     → INSERT hoặc UPDATE
    // deleteById(id)   → DELETE FROM Classes WHERE id = ?
    // count()          → SELECT COUNT(*) FROM Classes
}
```

### StudentRepository.java

```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Custom query method: Spring Data JPA tự sinh SQL dựa trên tên method
    // → SELECT * FROM Students WHERE class_id = ?
    List<Student> findByClassroomId(Long classroomId);
}
```

**Spring Data JPA Query Methods — quy tắc đặt tên:**

| Method name | SQL tương ứng |
|---|---|
| `findByClassroomId(Long id)` | `WHERE class_id = ?` |
| `findByFullNameContaining(String name)` | `WHERE full_name LIKE '%?%'` |
| `findByEmailEndingWith(String domain)` | `WHERE email LIKE '%?'` |
| `findByClassroomIdOrderByFullNameAsc(Long id)` | `WHERE class_id = ? ORDER BY full_name ASC` |
| `countByClassroomId(Long id)` | `SELECT COUNT(*) WHERE class_id = ?` |

## 4.6. Mô tả Service

### ClassroomService.java

```java
@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id).orElse(null);
    }

    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }
}
```

### StudentService.java

```java
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Lọc sinh viên theo lớp — dùng custom query method
    public List<Student> getStudentsByClassroom(Long classroomId) {
        return studentRepository.findByClassroomId(classroomId);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
```

**Vai trò của Service Layer:**
- Tách biệt logic nghiệp vụ khỏi Controller.
- Controller không gọi Repository trực tiếp — chỉ gọi qua Service.
- Dễ dàng thêm validation, transaction, logging mà không ảnh hưởng Controller.

## 4.7. Mô tả Controller

### ClassroomController.java

```java
@Controller
@RequestMapping("/classes")
public class ClassroomController {

    @Autowired private ClassroomService classroomService;
    @Autowired private StudentService studentService;

    @GetMapping                          // GET /classes
    public String listClasses(Model model) {
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "class-list";
    }

    @GetMapping("/add")                  // GET /classes/add
    public String showAddForm(Model model) {
        model.addAttribute("classroom", new Classroom());
        return "class-form";
    }

    @PostMapping("/save")                // POST /classes/save
    public String saveClass(@ModelAttribute("classroom") Classroom classroom) {
        classroomService.saveClassroom(classroom);
        return "redirect:/classes";
    }

    @GetMapping("/edit/{id}")            // GET /classes/edit/{id}
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("classroom", classroomService.getClassroomById(id));
        return "class-form";
    }

    @PostMapping("/delete/{id}")         // POST /classes/delete/{id}
    public String deleteClass(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        classroomService.deleteClassroom(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa lớp học thành công.");
        return "redirect:/classes";
    }

    @GetMapping("/{id}/students")        // GET /classes/{id}/students
    public String viewStudents(@PathVariable Long id, Model model) {
        model.addAttribute("classroom", classroomService.getClassroomById(id));
        model.addAttribute("students", studentService.getStudentsByClassroom(id));
        return "class-students";
    }
}
```

### StudentController.java

```java
@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired private StudentService studentService;
    @Autowired private ClassroomService classroomService;

    @GetMapping                          // GET /students hoặc /students?classId=1
    public String listStudents(
            @RequestParam(value = "classId", required = false) Long classId,
            Model model) {
        if (classId != null) {
            model.addAttribute("students", studentService.getStudentsByClassroom(classId));
            model.addAttribute("selectedClassId", classId);
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-list";
    }

    @GetMapping("/add")                  // GET /students/add
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    @PostMapping("/save")                // POST /students/save
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult,
            @RequestParam(value = "classroomId", required = false) Long classroomId,
            RedirectAttributes redirectAttributes) {
        // Gán quan hệ Many-to-One: lookup Classroom từ DB rồi set vào Student
        Classroom classroom = classroomService.getClassroomById(classroomId);
        student.setClassroom(classroom);
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")            // GET /students/edit/{id}
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("classes", classroomService.getAllClassrooms());
        return "student-form";
    }

    @GetMapping("/delete/{id}")          // GET /students/delete/{id}
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
```

**Lý do tách `classroomId` ra khỏi `@ModelAttribute`:**

Thymeleaf không thể tự động bind `<select name="classroomId">` (gửi giá trị `Long`) vào thuộc tính `classroom` (kiểu `Classroom`) trong Student. Cần nhận `classroomId` riêng rồi tự lookup object từ DB và gán thủ công.

## 4.8. Mô tả View (Thymeleaf Templates)

### layout.html — Fragment dùng chung

```html
<!-- Định nghĩa fragment navbar — các trang khác nhúng vào -->
<nav th:fragment="navbar" class="navbar navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" th:href="@{/}">🏫 Quản lý Trường học</a>
    <ul class="navbar-nav">
        <li><a th:href="@{/classes}">📚 Lớp học</a></li>
        <li><a th:href="@{/students}">👨‍🎓 Sinh viên</a></li>
    </ul>
</nav>

<!-- Định nghĩa fragment scripts -->
<div th:fragment="scripts">
    <script src="bootstrap.bundle.min.js"></script>
</div>
```

Các trang khác nhúng fragment bằng:
```html
<nav th:replace="~{layout :: navbar}"></nav>
...
<div th:replace="~{layout :: scripts}"></div>
```

### class-list.html — Hiển thị số sinh viên qua @OneToMany

```html
<tr th:each="classroom : ${classes}">
    <td th:text="${classroom.classCode}"></td>
    <td th:text="${classroom.className}"></td>
    <!-- Truy cập quan hệ @OneToMany để đếm số sinh viên -->
    <td>
        <span class="badge bg-info"
              th:text="${classroom.students != null ? classroom.students.size() : 0}">
        </span>
    </td>
    <td>
        <a th:href="@{/classes/{id}/students(id=${classroom.id})}" class="btn btn-sm btn-info">Xem SV</a>
        <a th:href="@{/classes/edit/{id}(id=${classroom.id})}" class="btn btn-sm btn-warning">Sửa</a>
        <a th:href="@{/classes/delete/{id}(id=${classroom.id})}" class="btn btn-sm btn-danger"
           onclick="return confirm('Xóa lớp này?')">Xóa</a>
    </td>
</tr>
```

### student-form.html — Dropdown chọn lớp

```html
<!-- Dropdown hiển thị danh sách lớp, selected theo lớp hiện tại của sinh viên -->
<select name="classroomId" class="form-select" required>
    <option value="">-- Chọn lớp học --</option>
    <option th:each="c : ${classes}"
            th:value="${c.id}"
            th:text="${c.classCode + ' - ' + c.className}"
            th:selected="${student.classroom != null and student.classroom.id == c.id}">
    </option>
</select>
```

### student-list.html — Hiển thị tên lớp qua @ManyToOne

```html
<!-- Hiển thị tên lớp trong bảng — truy cập quan hệ @ManyToOne -->
<td>
    <span th:if="${student.classroom != null}"
          class="badge bg-primary"
          th:text="${student.classroom.className}"></span>
    <span th:if="${student.classroom == null}"
          class="badge bg-secondary">Chưa gán lớp</span>
</td>
```

---

# CHƯƠNG 5: KẾT QUẢ DEMO

## 5.1. Danh sách các màn hình

| Màn hình | URL | Mô tả |
|---|---|---|
| Danh sách lớp học | `/classes` | Bảng lớp kèm số SV, nút Xem SV / Sửa / Xóa |
| Form thêm lớp | `/classes/add` | Form 2 trường: Mã lớp, Tên lớp |
| Form sửa lớp | `/classes/edit/{id}` | Cùng form, tự điền dữ liệu hiện tại |
| Sinh viên theo lớp | `/classes/{id}/students` | Danh sách SV của một lớp cụ thể |
| Danh sách sinh viên | `/students` | Bảng SV kèm tên lớp, bộ lọc theo lớp |
| Lọc SV theo lớp | `/students?classId={id}` | Chỉ hiển thị SV của lớp được chọn |
| Form thêm sinh viên | `/students/add` | Form 4 trường + dropdown chọn lớp |
| Form sửa sinh viên | `/students/edit/{id}` | Cùng form, tự điền dữ liệu + chọn đúng lớp |

## 5.2. Cách chạy chương trình

**Bước 1:** Mở SQL Server Management Studio, chạy toàn bộ file `init-db.sql`

**Bước 2:** Mở file `src/main/resources/application.properties`, sửa:
```properties
spring.datasource.password=<mật_khẩu_SQL_Server_của_bạn>
```

**Bước 3:** Mở IntelliJ IDEA → `File → Open` → chọn folder `De_Tai_PT_Web_JavaEE` (folder chứa `pom.xml`)

**Bước 4:** Chờ Maven tải dependencies (góc dưới phải IntelliJ)

**Bước 5:** Tìm `ClassroomManagementApplication.java` → Click nút ▶️ Run (hoặc `Shift + F10`)

**Bước 6:** Mở trình duyệt → truy cập `http://localhost:8080`

---

# CHƯƠNG 6: ĐÁNH GIÁ VÀ KẾT LUẬN

## 6.1. Kết quả đạt được

| Yêu cầu đề tài | Trạng thái |
|---|---|
| CRUD lớp học | ✅ Hoàn thành |
| CRUD sinh viên | ✅ Hoàn thành |
| Gán sinh viên vào lớp | ✅ Hoàn thành |
| Lọc sinh viên theo lớp | ✅ Hoàn thành |
| Xem số lượng sinh viên của từng lớp | ✅ Hoàn thành |
| Kết nối SQL Server | ✅ Hoàn thành |
| Giao diện Bootstrap responsive | ✅ Hoàn thành |
| Mapping @OneToMany và @ManyToOne | ✅ Hoàn thành |
| Sử dụng @JoinColumn, mappedBy, cascade | ✅ Hoàn thành |

**Về mặt lý thuyết**, nhóm đã hiểu và trình bày được:
- Sự khác biệt giữa mô hình quan hệ (SQL) và mô hình đối tượng (Java).
- Cơ chế hoạt động của ORM/JPA/Hibernate.
- Ý nghĩa và cách sử dụng `@OneToMany`, `@ManyToOne`, `@JoinColumn`, `mappedBy`, `cascade`, `fetch`.
- Khái niệm owning side và inverse side trong quan hệ hai chiều.
- Cách Spring Data JPA tự sinh SQL từ tên method.

## 6.2. Cải tiến đã triển khai (V1.1 — Phase 0 & 1)

| Cải tiến | Mô tả |
|----------|--------|
| Maven Wrapper | `mvnw` / `mvnw.cmd` — chạy không cần cài Maven toàn cục |
| Profile `dev` | Tách cấu hình DB; mật khẩu qua `DB_PASSWORD` |
| Bean Validation | `@NotBlank`, `@Email`, `@Size` trên Entity; `@Valid` trên Controller |
| Flash message | Thông báo thành công / lỗi sau thêm, sửa, xóa |
| POST delete | `POST /classes/delete/{id}`, `POST /students/delete/{id}` |
| GlobalExceptionHandler | Bắt `DataIntegrityViolationException` (mã trùng) |
| `@Transactional` | Service layer |

## 6.3. Cải tiến V1.2 (Phase 2)

| Cải tiến | Kỹ thuật |
|----------|----------|
| Phân trang | Spring Data `Pageable`, `PageRequest` |
| Tìm kiếm SV | `@Query` + `keyword` (mã, họ tên) |
| Tránh N+1 (lớp) | `ClassroomSummaryDto` + `SIZE(c.students)` |
| Tránh N+1 (SV) | `@EntityGraph(attributePaths = "classroom")` |
| Flyway | `db/migration/V1`, `V2`; `ddl-auto=validate` |

## 6.4. Hạn chế còn lại

- **Chưa có bảo mật**: Không đăng nhập / phân quyền.
- **Chưa có REST API**: Chỉ giao diện Thymeleaf.
- **Sắp xếp cột**: Chưa click header để sort động trên UI.

## 6.5. Cải tiến V1.3 (Phase 3)

| Module | Quan hệ JPA | URL |
|--------|-------------|-----|
| Giảng viên | `@OneToMany` → Classroom | `/teachers` |
| Lớp ↔ GV | `@ManyToOne` Teacher | Form lớp học |
| Môn học | Độc lập | `/subjects` |
| Điểm | `@ManyToOne` Student + Subject | `/grades` |
| Dashboard | Aggregation query | `/dashboard` |

Flyway: `V3__add_teachers.sql`, `V4__add_subjects.sql`, `V5__add_grades_and_class_teacher.sql`

## 6.6. Cải tiến V1.4 (Phase 4–5)

| Cải tiến | Mô tả |
|----------|--------|
| REST API | `/api/v1` JSON, DTO, `ApiResponse` wrapper |
| Swagger | SpringDoc OpenAPI 3 |
| Docker | `docker-compose` (SQL Server + app) |
| CI | GitHub Actions build + test |
| Actuator | Health endpoint |

## 6.7. Hướng phát triển tiếp

- **Spring Security** — đăng nhập, phân quyền admin/user
- **Frontend SPA** — React/Vue gọi API
- **Testcontainers** — integration test với SQL Server

---

# TÀI LIỆU THAM KHẢO

1. Spring Boot Official Documentation — https://docs.spring.io/spring-boot/docs/current/reference/html/
2. Spring Data JPA Reference — https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
3. Hibernate ORM Documentation — https://hibernate.org/orm/documentation/
4. Baeldung — Spring Data JPA Tutorial — https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa
5. Baeldung — Hibernate One-to-Many — https://www.baeldung.com/hibernate-one-to-many
6. Baeldung — JPA Many-to-One — https://www.baeldung.com/jpa-many-to-one
7. Thymeleaf Documentation — https://www.thymeleaf.org/documentation.html
8. Bootstrap 5 Documentation — https://getbootstrap.com/docs/5.3/
9. Microsoft SQL Server JDBC Driver — https://learn.microsoft.com/en-us/sql/connect/jdbc/

