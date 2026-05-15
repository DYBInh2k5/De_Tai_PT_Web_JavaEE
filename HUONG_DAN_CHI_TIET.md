# BÁO CÁO VÀ HƯỚNG DẪN CHI TIẾT ĐỀ TÀI 07

**Đề tài:** Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học.

---

## 1. Giới thiệu đề tài
Đề tài tập trung vào việc áp dụng công nghệ JPA/Hibernate để giải quyết bài toán quản lý quan hệ giữa các bảng trong cơ sở dữ liệu quan hệ (RDBMS). Cụ thể là mối quan hệ một-nhiều (One-to-Many) giữa Lớp học và Sinh viên.

### Mục tiêu:
- Hiểu cách cấu hình và sử dụng `@OneToMany` và `@ManyToOne`.
- Xây dựng ứng dụng Web hoàn chỉnh với Spring Boot, SQL Server và Bootstrap.
- Thực hiện đầy đủ các thao tác CRUD (Create, Read, Update, Delete) trên dữ liệu có quan hệ.

---

## 2. Cơ sở lý thuyết

### 2.1. Quan hệ One-to-Many & Many-to-One
- **One-to-Many (Một - Nhiều):** Một thực thể ở phía "Một" có thể liên kết với nhiều thực thể ở phía "Nhiều". 
    - Ví dụ: Một lớp học (`Classroom`) có thể chứa nhiều sinh viên (`Student`).
    - Trong JPA: Sử dụng annotation `@OneToMany`.
- **Many-to-One (Nhiều - Một):** Nhiều thực thể ở phía "Nhiều" cùng liên kết với một thực thể duy nhất ở phía "Một".
    - Ví dụ: Nhiều sinh viên cùng thuộc về một lớp học.
    - Trong JPA: Sử dụng annotation `@ManyToOne`.

### 2.2. Các Annotation quan trọng
- `@JoinColumn`: Xác định cột khóa ngoại trong bảng (thường đặt ở phía "Nhiều").
- `mappedBy`: Xác định tên thuộc tính ở phía đối diện sở hữu quan hệ (đặt ở phía "Một").
- `cascade`: Định nghĩa các thao tác (như xóa, lưu) sẽ được lan truyền từ thực thể cha sang thực thể con.

---

## 3. Thiết kế hệ thống

### 3.1. Sơ đồ Cơ sở dữ liệu (SQL Server)
- **Bảng Classes**: Lưu thông tin lớp học.
    - `id` (BIGINT, PK): Khóa chính tự tăng.
    - `class_code` (NVARCHAR): Mã lớp (Duy nhất).
    - `class_name` (NVARCHAR): Tên lớp.
- **Bảng Students**: Lưu thông tin sinh viên.
    - `id` (BIGINT, PK): Khóa chính tự tăng.
    - `student_code` (NVARCHAR): Mã sinh viên (Duy nhất).
    - `full_name` (NVARCHAR): Họ và tên.
    - `email` (NVARCHAR): Địa chỉ email.
    - `class_id` (BIGINT, FK): Khóa ngoại tham chiếu tới `Classes(id)`.

### 3.2. Kiến trúc chương trình
Ứng dụng tuân thủ mô hình MVC (Model-View-Controller):
- **Entity**: Ánh xạ bảng DB thành đối tượng Java.
- **Repository**: Giao tiếp với DB thông qua Spring Data JPA.
- **Service**: Xử lý logic nghiệp vụ.
- **Controller**: Tiếp nhận request từ trình duyệt và trả về View.
- **View**: Giao diện HTML sử dụng Thymeleaf.

---

## 4. Cài đặt chi tiết

### 4.1. Entity Mapping (Trọng tâm)
Trong file `Classroom.java`:
```java
@OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
private List<Student> students;
```

Trong file `Student.java`:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "class_id")
private Classroom classroom;
```

### 4.2. Các chức năng chính
1. **Quản lý Lớp học**:
   - Hiển thị danh sách lớp kèm số lượng sinh viên hiện có (sử dụng `class.students.size()`).
   - Thêm mới, chỉnh sửa và xóa lớp học.
2. **Quản lý Sinh viên**:
   - Hiển thị danh sách toàn bộ sinh viên hoặc lọc theo lớp.
   - Thêm mới sinh viên: Có dropdown list để chọn lớp học từ dữ liệu DB.
   - Chỉnh sửa thông tin và chuyển lớp cho sinh viên.
3. **Xem chi tiết**: Từ danh sách lớp học, có thể nhấn "Xem SV" để xem toàn bộ sinh viên thuộc lớp đó.

---

## 5. Hướng dẫn sử dụng & Cài đặt

### Bước 1: Khởi tạo Database
- Mở SQL Server Management Studio.
- Chạy toàn bộ script trong file `init-db.sql` đi kèm.

### Bước 2: Cấu hình ứng dụng
- Mở file `src/main/resources/application.properties`.
- Chỉnh sửa mật khẩu SQL Server của bạn tại dòng:
  `spring.datasource.password=Mật_Khẩu_Của_Bạn`

### Bước 3: Chạy ứng dụng
- Chạy class `ClassroomManagementApplication.java` từ IDE.
- Truy cập trình duyệt: `http://localhost:8080`.

---

## 6. Kết quả đạt được
- Ứng dụng chạy ổn định trên môi trường Web.
- Thể hiện rõ ràng cách ánh xạ quan hệ One-to-Many giữa Lớp và Sinh viên.
- Giao diện thân thiện, dễ sử dụng nhờ Bootstrap.
- Xử lý tốt các ràng buộc khóa ngoại (ví dụ: Khi xóa lớp, có thể cấu hình xóa sinh viên hoặc set NULL).

---
*Tài liệu này được biên soạn để phục vụ việc báo cáo và hướng dẫn triển khai dự án.*
