# TỔNG QUAN DỰ ÁN
## Quản lý Lớp học & Sinh viên — Đề tài 07
### One-to-Many và Many-to-One trong JPA/Hibernate

---

## 1. THÔNG TIN DỰ ÁN

| Thông tin | Chi tiết |
|---|---|
| Tên đề tài | Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate |
| Môn học | Phát triển ứng dụng Web Java EE |
| Framework | Spring Boot 3.2.5 |
| Database | SQL Server 2019+ |
| Java | JDK 17 |
| Port | http://localhost:8080 |
| GitHub | https://github.com/DYBInh2k5/De_Tai_PT_Web_JavaEE |
| **Slide thuyết trình** | **https://gamma.app/docs/Tim-hieu-quan-he-One-to-Many-va-Many-to-One-trong-JPAHibernate-qu-0xgbhtwrqgd0ais** |

---

## 2. CẤU TRÚC PROJECT

```
De_Tai_PT_Web_JavaEE/
├── pom.xml                          ← Maven dependencies
├── init-db.sql                      ← Script tạo DB + dữ liệu mẫu
├── README.md                        ← Hướng dẫn chạy nhanh
│
├── src/main/java/com/ttjavaee/classroom/
│   ├── ClassroomManagementApplication.java   ← @SpringBootApplication (main)
│   │
│   ├── entity/
│   │   ├── Classroom.java           ← @Entity, @OneToMany (phía "Một")
│   │   └── Student.java             ← @Entity, @ManyToOne, @JoinColumn (phía "Nhiều")
│   │
│   ├── repository/
│   │   ├── ClassroomRepository.java ← extends JpaRepository<Classroom, Long>
│   │   └── StudentRepository.java   ← + findByClassroomId(Long)
│   │
│   ├── service/
│   │   ├── ClassroomService.java    ← getAllClassrooms, save, delete, getById
│   │   └── StudentService.java      ← getAllStudents, getByClassroom, save, delete
│   │
│   └── controller/
│       ├── HomeController.java      ← GET / → redirect:/classes
│       ├── ClassroomController.java ← CRUD lớp + xem SV theo lớp
│       └── StudentController.java   ← CRUD sinh viên + lọc theo lớp
│
├── src/main/resources/
│   ├── application.properties       ← Cấu hình DB, JPA, Thymeleaf
│   └── templates/
│       ├── layout.html              ← Fragment: navbar + scripts
│       ├── class-list.html          ← Danh sách lớp + số SV
│       ├── class-form.html          ← Form thêm/sửa lớp
│       ├── class-students.html      ← Sinh viên theo lớp
│       ├── student-list.html        ← Danh sách SV + lọc theo lớp
│       └── student-form.html        ← Form thêm/sửa SV + chọn lớp
│
└── docs/
    ├── BAO_CAO_CHI_TIET.md          ← Nội dung báo cáo đầy đủ (copy vào Word)
    ├── HUONG_DAN_BAO_CAO.md         ← Hướng dẫn làm Word
    ├── HUONG_DAN_SLIDE.md           ← Hướng dẫn làm PowerPoint
    ├── HUONG_DAN_CHAY_VA_DEMO.md    ← Hướng dẫn chạy + demo từng chức năng
    ├── HUONG_DAN_KET_NOI_SQLSERVER.md ← Setup SQL Server
    ├── VAN_BAN_THUYET_TRINH.md      ← Script thuyết trình 11 slide
    └── HUONG_DAN_NOP_BAI.md         ← Checklist nộp bài
```

---

## 3. CẤU HÌNH DATABASE

File: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=<MẬT KHẨU CỦA BẠN>
spring.jpa.hibernate.ddl-auto=update   ← Hibernate tự tạo bảng
```

---

## 4. QUAN HỆ JPA — TRỌNG TÂM ĐỀ TÀI

### Classroom.java — phía "Một" (One)
```java
@Entity
@Table(name = "Classes")
public class Classroom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String classCode;   // → cột class_code
    private String className;   // → cột class_name

    @OneToMany(mappedBy = "classroom",
               cascade = {CascadeType.PERSIST, CascadeType.MERGE},
               fetch = FetchType.LAZY)
    private List<Student> students;
}
```

### Student.java — phía "Nhiều" (Many) — Owning Side
```java
@Entity
@Table(name = "Students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentCode;
    private String fullName;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")   // ← cột khóa ngoại trong bảng Students
    private Classroom classroom;
}
```

### Sơ đồ quan hệ
```
[Classes]              [Students]
   id ←─────────────── class_id (FK)
   class_code           student_code
   class_name           full_name
                        email

Classroom (1) ──────── (N) Student
  @OneToMany              @ManyToOne
  mappedBy="classroom"    @JoinColumn(name="class_id")
  (inverse side)          (owning side — chứa FK)
```

### Giải thích các annotation
| Annotation | Đặt ở | Ý nghĩa |
|---|---|---|
| `@OneToMany` | Classroom | 1 lớp có nhiều sinh viên |
| `@ManyToOne` | Student | Nhiều SV thuộc 1 lớp |
| `@JoinColumn(name="class_id")` | Student | Tên cột FK trong bảng Students |
| `mappedBy = "classroom"` | Classroom | Student là owning side |
| `cascade = {PERSIST, MERGE}` | Classroom | Không dùng REMOVE (xung đột ON DELETE SET NULL) |
| `fetch = LAZY` | Cả hai | Chỉ load dữ liệu liên quan khi cần |

---

## 5. DATABASE SQL SERVER

### Bảng Classes
```sql
CREATE TABLE Classes (
    id         BIGINT IDENTITY(1,1) PRIMARY KEY,
    class_code NVARCHAR(50)  NOT NULL UNIQUE,
    class_name NVARCHAR(255) NOT NULL
);
```

### Bảng Students
```sql
CREATE TABLE Students (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    student_code NVARCHAR(50)  NOT NULL UNIQUE,
    full_name    NVARCHAR(255) NOT NULL,
    email        NVARCHAR(255) NOT NULL,
    class_id     BIGINT NULL,
    CONSTRAINT FK_Student_Class
        FOREIGN KEY (class_id) REFERENCES Classes(id)
        ON DELETE SET NULL    ← Xóa lớp → SV không bị xóa, class_id = NULL
        ON UPDATE CASCADE
);
```

### Dữ liệu mẫu sau khi chạy init-db.sql
```
Classes:
  CNTT01 | Cong nghe thong tin 01
  CNTT02 | Cong nghe thong tin 02
  KTPM01 | Ky thuat phan mem 01

Students:
  SV001 | Nguyen Van A  | CNTT01
  SV002 | Tran Thi B    | CNTT01
  SV003 | Le Van C      | CNTT02
  SV004 | Pham Thi D    | KTPM01
  SV005 | Hoang Van E   | CNTT01
  SV006 | Dang Thi F    | CNTT02
```

---

## 6. CHỨC NĂNG VÀ URL

### Quản lý Lớp học
| Chức năng | Method | URL | Template |
|---|---|---|---|
| Danh sách lớp + số SV | GET | /classes | class-list.html |
| Form thêm lớp | GET | /classes/add | class-form.html |
| Lưu lớp (thêm/sửa) | POST | /classes/save | — redirect |
| Form sửa lớp | GET | /classes/edit/{id} | class-form.html |
| Xóa lớp | POST | /classes/delete/{id} | — redirect |
| SV theo lớp | GET | /classes/{id}/students | class-students.html |

### Quản lý Sinh viên
| Chức năng | Method | URL | Template |
|---|---|---|---|
| Danh sách SV (tất cả) | GET | /students | student-list.html |
| Lọc SV theo lớp | GET | /students?classId={id} | student-list.html |
| Form thêm SV | GET | /students/add | student-form.html |
| Lưu SV (thêm/sửa) | POST | /students/save | — redirect |
| Form sửa SV | GET | /students/edit/{id} | student-form.html |
| Xóa SV | POST | /students/delete/{id} | — redirect |

---

## 7. LUỒNG XỬ LÝ QUAN TRỌNG

### Thêm sinh viên + gán lớp
```
Browser: POST /students/save
         body: studentCode, fullName, email, classroomId=2
           │
           ▼
StudentController.saveStudent()
  classroomId = 2 → classroomService.getClassroomById(2) → Classroom object
  student.setClassroom(classroom)   ← gán quan hệ @ManyToOne
           │
           ▼
StudentService.saveStudent(student)
           │
           ▼
StudentRepository.save(student)
  Hibernate: INSERT INTO Students (..., class_id) VALUES (..., 2)
           │
           ▼
Browser: redirect /students
```

### Xóa lớp — hành vi ON DELETE SET NULL
```
Browser: POST /classes/delete/3
           │
           ▼
ClassroomService.deleteClassroom(3)
  → ClassroomRepository.deleteById(3)
  → Hibernate: DELETE FROM Classes WHERE id = 3
           │
           ▼
SQL Server trigger FK_Student_Class ON DELETE SET NULL:
  UPDATE Students SET class_id = NULL WHERE class_id = 3
  (SV004 vẫn tồn tại, chỉ mất liên kết với lớp)
           │
           ▼
Browser: redirect /classes
```

---

## 8. DEPENDENCIES (pom.xml)

| Dependency | Vai trò |
|---|---|
| spring-boot-starter-data-jpa | Spring Data JPA + Hibernate |
| spring-boot-starter-web | Spring MVC, Tomcat nhúng |
| spring-boot-starter-thymeleaf | Template engine |
| mssql-jdbc | JDBC driver cho SQL Server |
| lombok | @Getter, @Setter, @NoArgsConstructor tự động |

---

## 9. CHECKLIST ĐỐI CHIẾU ĐỀ BÀI

### Chức năng bắt buộc (Mục 4)
- [x] CRUD lớp học — ClassroomController (5 endpoints)
- [x] CRUD sinh viên — StudentController (5 endpoints)
- [x] Gán sinh viên vào lớp — student-form.html dropdown + saveStudent()
- [x] Lọc sinh viên theo lớp — GET /students?classId={id}
- [x] Xem số lượng SV từng lớp — class-list.html: classroom.students.size()

### Nội dung kỹ thuật (Mục 3)
- [x] @OneToMany — Classroom.java
- [x] @ManyToOne — Student.java
- [x] @JoinColumn — Student.java
- [x] mappedBy — Classroom.java
- [x] cascade — Classroom.java (PERSIST + MERGE)
- [x] Bảng Classes + Students — init-db.sql
- [x] Khóa ngoại FK — init-db.sql
- [x] Dữ liệu mẫu — init-db.sql
- [x] ON DELETE SET NULL — init-db.sql

### Sản phẩm phải nộp (Mục 7)
- [x] Source code — De_Tai_PT_Web_JavaEE/
- [x] File SQL — init-db.sql
- [x] README — README.md
- [ ] Báo cáo .docx — tự làm từ docs/BAO_CAO_CHI_TIET.md
- [ ] Báo cáo .pdf — export từ Word
- [ ] Slide .pptx — tự làm từ docs/HUONG_DAN_SLIDE.md

---

## 10. HƯỚNG DẪN CHẠY NHANH

```
1. SSMS: chạy init-db.sql
2. Sửa application.properties: spring.datasource.password=<mật khẩu>
3. IntelliJ: File → Open → De_Tai_PT_Web_JavaEE (folder chứa pom.xml)
4. Chờ Maven load xong
5. Run ClassroomManagementApplication.java
6. Trình duyệt: http://localhost:8080
```