# Quản lý Lớp học & Sinh viên
### Đề tài 07: One-to-Many và Many-to-One trong JPA/Hibernate

---

## �️ Công nghệ sử dụng

| Công nghệ | Phiên bản |
|-----------|-----------|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Spring Data JPA / Hibernate | (theo Spring Boot) |
| SQL Server | 2019+ |
| Thymeleaf | (theo Spring Boot) |
| Bootstrap | 5.3.0 |
| Lombok | (theo Spring Boot) |
| Maven | 3.6+ |

---

## ⚙️ Yêu cầu môi trường

- **JDK 17** trở lên
- **SQL Server** đang chạy trên `localhost:1433`
- **IntelliJ IDEA** (Ultimate hoặc Community với plugin Spring)
- **Maven** (đã tích hợp trong IntelliJ)

---

## 🚀 Hướng dẫn chạy chương trình

### Bước 1: Tạo Database

1. Mở **SQL Server Management Studio (SSMS)** hoặc **Azure Data Studio**
2. Kết nối vào SQL Server local
3. Mở file `init-db.sql` và chạy toàn bộ script
4. Kiểm tra database `ClassroomDB` đã được tạo với dữ liệu mẫu

### Bước 2: Cấu hình kết nối Database

Mở file `src/main/resources/application.properties` và sửa thông tin kết nối:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=<MẬT_KHẨU_SQL_SERVER_CỦA_BẠN>
```

> ⚠️ Thay `<MẬT_KHẨU_SQL_SERVER_CỦA_BẠN>` bằng mật khẩu thực tế của tài khoản `sa`

### Bước 3: Chạy ứng dụng trong IntelliJ IDEA

**Cách 1 - Qua IntelliJ:**
1. Mở project trong IntelliJ IDEA
2. Chờ Maven tải dependencies (lần đầu có thể mất vài phút)
3. Tìm file `ClassroomManagementApplication.java`
4. Click nút ▶️ **Run** hoặc nhấn `Shift + F10`

**Cách 2 - Qua Maven:**
```bash
cd De_Tai_PT_Web_JavaEE
mvn spring-boot:run
```

### Bước 4: Truy cập ứng dụng

Mở trình duyệt và vào: **http://localhost:8080**

---

## 📋 Chức năng

| Chức năng | URL |
|-----------|-----|
| Trang chủ (redirect) | `GET /` |
| Danh sách lớp học | `GET /classes` |
| Thêm lớp học | `GET /classes/add` |
| Sửa lớp học | `GET /classes/edit/{id}` |
| Xóa lớp học | `GET /classes/delete/{id}` |
| Xem sinh viên theo lớp | `GET /classes/{id}/students` |
| Danh sách sinh viên | `GET /students` |
| Lọc sinh viên theo lớp | `GET /students?classId={id}` |
| Thêm sinh viên | `GET /students/add` |
| Sửa sinh viên | `GET /students/edit/{id}` |
| Xóa sinh viên | `GET /students/delete/{id}` |

---

## 🗂️ Cấu trúc Project

```
src/main/java/com/ttjavaee/classroom/
├── ClassroomManagementApplication.java   ← Main class
├── controller/
│   ├── HomeController.java               ← Redirect / → /classes
│   ├── ClassroomController.java          ← CRUD lớp học
│   └── StudentController.java            ← CRUD sinh viên
├── entity/
│   ├── Classroom.java                    ← Entity lớp học (@OneToMany)
│   └── Student.java                      ← Entity sinh viên (@ManyToOne)
├── repository/
│   ├── ClassroomRepository.java          ← JPA Repository lớp học
│   └── StudentRepository.java            ← JPA Repository sinh viên
└── service/
    ├── ClassroomService.java             ← Business logic lớp học
    └── StudentService.java               ← Business logic sinh viên

src/main/resources/
├── application.properties               ← Cấu hình DB, JPA, server
└── templates/
    ├── layout.html                       ← Navbar fragment dùng chung
    ├── class-list.html                   ← Danh sách lớp học
    ├── class-form.html                   ← Form thêm/sửa lớp
    ├── class-students.html               ← Sinh viên theo lớp
    ├── student-list.html                 ← Danh sách sinh viên
    └── student-form.html                 ← Form thêm/sửa sinh viên
```

---

## 🔗 Quan hệ JPA

```
Classroom (1) ←──────────── (N) Student
   @OneToMany                    @ManyToOne
   mappedBy="classroom"          @JoinColumn(name="class_id")
   cascade=ALL
```

---

## ❗ Lưu ý

- `spring.jpa.hibernate.ddl-auto=update` sẽ tự tạo/cập nhật bảng theo Entity
- Nếu đã chạy `init-db.sql`, Hibernate sẽ không tạo lại bảng
- Khi xóa lớp học, `class_id` của sinh viên trong lớp đó sẽ được set `NULL` (do `ON DELETE SET NULL`)
