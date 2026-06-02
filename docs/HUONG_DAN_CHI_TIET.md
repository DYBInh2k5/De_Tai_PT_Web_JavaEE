# BÁO CÁO VÀ HƯỚNG DẪN CHI TIẾT ĐỀ TÀI 07

**Đề tài:** Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học.

> **Cập nhật V1.4:** REST API + Swagger + Docker. Chi tiết: `KE_HOACH_PHAT_TRIEN.md`.

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

### 3.1. Phân biệt Mô hình Quan hệ vs Mô hình Đối tượng
- **Mô hình Quan hệ (Relational Model - SQL Server):**
    - Dữ liệu lưu trong bảng (tables), hàng (rows) và cột (columns).
    - Kết nối thông qua Khóa ngoại (Foreign Keys).
    - Không hỗ trợ kế thừa, đa hình.
- **Mô hình Đối tượng (Object Model - Java/JPA):**
    - Dữ liệu lưu trong Đối tượng (Objects), Thuộc tính (Attributes).
    - Kết nối thông qua Tham chiếu đối tượng (Object References).
    - Hỗ trợ đầy đủ tính kế thừa, bao đóng, đa hình.
- **ORM (JPA/Hibernate):** Là kỹ thuật ánh xạ giữa hai mô hình này, giúp lập trình viên thao tác với DB bằng ngôn ngữ hướng đối tượng.

### 3.2. Sơ đồ Cơ sở dữ liệu (SQL Server)
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

## 5. Hướng dẫn sử dụng & Cài đặt (Trên IntelliJ IDEA)

### Bước 1: Khởi tạo Database
- Mở SQL Server Management Studio.
- Chạy toàn bộ script trong file `init-db.sql` đi kèm.

### Bước 2: Mở dự án trong IntelliJ IDEA
- Chọn **File > Open**, trỏ đến thư mục `ttjavaee`.
- Chọn file `pom.xml` và chọn **Open as Project**.
- Đợi IntelliJ tải xong các thư viện Maven (góc dưới bên phải).

### Bước 3: Cấu hình ứng dụng
- Mở `src/main/resources/application-dev.properties` hoặc đặt biến môi trường:
  ```powershell
  $env:DB_PASSWORD = "MatKhauSaCuaBan"
  ```
- Xem thêm `application.properties.example` ở thư mục gốc project.

### Bước 4: Chạy ứng dụng
- **IntelliJ:** Run `ClassroomManagementApplication.java` (JDK 17).
- **Dòng lệnh:** `.\mvnw.cmd spring-boot:run` (Windows) trong folder `De_Tai_PT_Web_JavaEE`.
- Truy cập: `http://localhost:8080`.

### Bước 5: Kiểm tra tính năng
- **V1.1:** Form trống → validation; mã trùng → lỗi; xóa POST + flash message.
- **V1.2:** Tìm kiếm SV, phân trang, Flyway.
- **V1.3:** Dashboard, GV, môn, điểm.
- **V1.4:** Swagger `/swagger-ui.html`; thử `GET /api/v1/students`; Docker `docker compose up`.

---

## 6. Kết quả đạt được
- Ứng dụng chạy ổn định trên môi trường Web.
- Thể hiện rõ ràng cách ánh xạ quan hệ One-to-Many giữa Lớp và Sinh viên.
- Giao diện thân thiện, dễ sử dụng nhờ Bootstrap.
- Xử lý tốt các ràng buộc khóa ngoại (khi xóa lớp → `class_id` sinh viên = NULL).
- **V1.1:** Validation, flash message, POST delete, xử lý mã trùng.
- **V1.2:** Phân trang, tìm kiếm SV, Flyway, tối ưu truy vấn.
- **V1.3:** Mở rộng domain GV / môn / điểm; quan hệ `@ManyToOne` Lớp–GV; dashboard thống kê.

---
*Tài liệu này được biên soạn để phục vụ việc báo cáo và hướng dẫn triển khai dự án.*
