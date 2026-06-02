# HƯỚNG DẪN CHẠY DỰ ÁN VÀ DEMO CHI TIẾT
## Ứng dụng Quản lý Lớp học — Spring Boot + SQL Server + JPA/Hibernate

---

## MỤC LỤC

1. [Yêu cầu môi trường](#1-yeu-cau-moi-truong)
2. [Bước 1 — Chuẩn bị SQL Server](#2-buoc-1--chuan-bi-sql-server)
3. [Bước 2 — Mở dự án trong IntelliJ IDEA](#3-buoc-2--mo-du-an-trong-intellij-idea)
4. [Bước 3 — Cấu hình kết nối database](#4-buoc-3--cau-hinh-ket-noi-database)
5. [Bước 4 — Chạy ứng dụng](#5-buoc-4--chay-ung-dung)
6. [Demo — Dashboard](#6-demo--dashboard-tong-quan)
7. [Demo — Lớp học](#7-demo--quan-ly-lop-hoc)
8. [Demo — Sinh viên](#8-demo--quan-ly-sinh-vien)
9. [Demo — Giảng viên](#9-demo--quan-ly-giang-vien)
10. [Demo — Môn học](#10-demo--quan-ly-mon-hoc)
11. [Demo — Điểm số](#11-demo--quan-ly-diem-so)
12. [Demo — Swagger API](#12-demo--swagger-api)
13. [Xử lý lỗi thường gặp](#13-xu-ly-loi-thuong-gap)
14. [Tóm tắt URL](#14-tom-tat-toan-bo-url)
---

## 1. Yêu cầu môi trường

| Phần mềm | Phiên bản | Ghi chú |
|---|---|---|
| **JDK** | 17+ | Bắt buộc |
| **IntelliJ IDEA** | 2022+ | Community hoặc Ultimate đều được |
| **SQL Server** | 2019+ | Developer Edition miễn phí |
| **SSMS** | 19+ | SQL Server Management Studio |

> Không cần cài Maven, Tomcat riêng — Spring Boot và IntelliJ đã có sẵn.

---

## 2. Bước 1 — Chuẩn bị SQL Server

### 2.1. Đảm bảo SQL Server đang chạy

Nhấn `Win + R` → gõ `services.msc` → tìm **SQL Server (MSSQLSERVER)** → Status phải là **Running**.

Nếu chưa chạy: chuột phải → **Start**.

### 2.2. Bật SQL Server Authentication (nếu chưa bật)

1. Mở SSMS → kết nối vào server
2. Chuột phải tên server → **Properties** → tab **Security**
3. Chọn **SQL Server and Windows Authentication mode** → OK
4. Chuột phải tên server → **Restart**

### 2.3. Đặt mật khẩu cho tài khoản sa

Trong SSMS mở New Query, chạy:
```sql
ALTER LOGIN sa ENABLE;
ALTER LOGIN sa WITH PASSWORD = 'YourPassword123';
```

### 2.4. Bật TCP/IP port 1433

1. Mở **SQL Server Configuration Manager**
2. **SQL Server Network Configuration** → **Protocols for MSSQLSERVER**
3. Chuột phải **TCP/IP** → **Enable**
4. Double-click **TCP/IP** → tab **IP Addresses** → cuộn xuống **IPAll** → TCP Port = **1433**
5. OK → Restart SQL Server

### 2.5. Tạo database ClassroomDB

Trong SSMS, mở New Query và chạy:
```sql
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ClassroomDB')
    CREATE DATABASE ClassroomDB;
```

> Chỉ cần tạo database RỖNG.
> Flyway sẽ tự động tạo toàn bộ bảng và dữ liệu mẫu khi ứng dụng khởi động lần đầu.

---

## 3. Bước 2 — Mở dự án trong IntelliJ IDEA

### 3.1. Mở đúng folder

```
File → Open → chọn folder "De_Tai_PT_Web_JavaEE" (folder chứa pom.xml) → OK
```

Khi IntelliJ hỏi "Trust this project?" → chọn **Trust Project**.

> Phải mở đúng folder chứa pom.xml, không mở folder cha bên ngoài.

### 3.2. Chờ Maven tải dependencies

IntelliJ tự động đọc `pom.xml` và tải thư viện. Quan sát thanh loading ở **góc dưới bên phải**.

Lần đầu mất 2–5 phút tùy mạng. **Chờ đến khi loading biến mất mới tiếp tục.**

### 3.3. Kiểm tra JDK 17

```
File → Project Structure → Project → SDK → chọn JDK 17
```

Nếu chưa có:
```
File → Project Structure → SDKs → + → Download JDK → Version 17 → Download
```

### 3.4. Nếu thấy code đỏ (Cannot resolve symbol)

View → Tool Windows → **Maven** → nhấn nút **Reload All Maven Projects** (2 mũi tên tròn).

---

## 4. Bước 3 — Cấu hình kết nối database

Mở file: `src/main/resources/application-dev.properties`

Sửa dòng password:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourPassword123    <-- ĐỔI THÀNH MẬT KHẨU CỦA BẠN
```

Nếu dùng SQL Server Express:
```properties
spring.datasource.url=jdbc:sqlserver://localhost\SQLEXPRESS;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
```

---

## 5. Bước 4 — Chạy ứng dụng

### 5.1. Tìm file main trong Project panel

```
src/main/java/com/ttjavaee/classroom/ClassroomManagementApplication.java
```

### 5.2. Chạy

Chuột phải file → **Run 'ClassroomManagementApplication'**

Hoặc mở file → click nút tam giác xanh ▶ cạnh `public static void main`

### 5.3. Kiểm tra log khởi động thành công

Trong tab **Run** ở dưới IntelliJ, phải thấy:

```
Flyway Community Edition by Redgate
Successfully validated 5 migrations
Migrating schema [dbo] to version 1 - create schema
Migrating schema [dbo] to version 2 - seed data
Migrating schema [dbo] to version 3 - add teachers
Migrating schema [dbo] to version 4 - add subjects
Migrating schema [dbo] to version 5 - add grades and class teacher
Successfully applied 5 migrations

Tomcat started on port(s): 8080 (http)
Started ClassroomManagementApplication in X.XXX seconds
```

Thấy dòng **Started ClassroomManagementApplication** = ứng dụng chạy thành công.

### 5.4. Truy cập

Mở trình duyệt, vào: **http://localhost:8080**

Trang tự động chuyển về **http://localhost:8080/dashboard**

---

## 6. Demo — Dashboard (Tổng quan)

**URL:** `http://localhost:8080/dashboard`

### Các ô thống kê

| Ô | Nội dung | Màu |
|---|---|---|
| Lớp học | Tổng số lớp | Xanh dương |
| Sinh viên | Tổng số sinh viên | Xanh lá |
| Giảng viên | Tổng số giảng viên | Xanh nhạt |
| Môn học | Tổng số môn học | Vàng |
| Bản ghi điểm | Tổng số điểm đã nhập | Xám |
| Điểm TB | Điểm trung bình toàn hệ thống / 10 | Đen |

**Sau khi Flyway chạy xong, các ô sẽ hiển thị:**
- Lớp học: 3
- Sinh viên: 6
- Giảng viên: 2
- Môn học: 3
- Bản ghi điểm: 3
- Điểm TB: ~8.42

### Bảng Top lớp đông sinh viên nhất

```
CNTT01 | Công nghệ thông tin 01 | Nguyễn Văn Hùng | 3 SV
CNTT02 | Công nghệ thông tin 02 | Nguyễn Văn Hùng | 2 SV
KTPM01 | Kỹ thuật phần mềm 01  | Trần Thị Lan     | 1 SV
```

### Nút điều hướng nhanh

- **Quản lý lớp** → /classes
- **Quản lý sinh viên** → /students
- **Quản lý điểm** → /grades
- **Swagger API** → /swagger-ui.html (mở tab mới)
---

## 7. Demo — Quản lý Lớp học

**URL:** `http://localhost:8080/classes`

### 7.1. Xem danh sách lớp

Bảng gồm các cột:

| Cột | Mô tả |
|---|---|
| ID | Khóa chính tự tăng |
| Mã lớp | Ví dụ: CNTT01 |
| Tên lớp | Ví dụ: Công nghệ thông tin 01 |
| Giảng viên | Tên GV phụ trách (badge xám). "Chưa gán" nếu chưa có |
| Số SV | Badge xanh — số sinh viên trong lớp (minh họa @OneToMany) |
| Hành động | Xem SV / Sửa / Xóa |

Dòng thông tin phân trang: `Hiển thị 3/3 lớp (trang 1/1)`

**Dữ liệu mẫu hiển thị:**
```
1 | CNTT01 | Công nghệ thông tin 01 | Nguyễn Văn Hùng | 3
2 | CNTT02 | Công nghệ thông tin 02 | Nguyễn Văn Hùng | 2
3 | KTPM01 | Kỹ thuật phần mềm 01  | Trần Thị Lan     | 1
```

### 7.2. Thêm lớp mới

1. Nhấn **"+ Thêm Lớp mới"**
2. Nhập:
   - Mã lớp: `HTTT01`
   - Tên lớp: `Hệ thống thông tin 01`
   - Giảng viên: chọn từ dropdown (tùy chọn)
3. Nhấn **Lưu**
4. Flash message xanh: *"Đã thêm lớp học thành công."*
5. Quay lại danh sách → thấy lớp HTTT01 mới

**Demo validation:** Xóa trắng Mã lớp → nhấn Lưu → ô Mã lớp viền đỏ + thông báo *"Mã lớp không được để trống"*.

### 7.3. Sửa lớp

1. Nhấn **Sửa** ở hàng CNTT01
2. Form tự điền sẵn: Mã lớp = CNTT01, Tên lớp = Công nghệ thông tin 01, GV = Nguyễn Văn Hùng
3. Sửa tên lớp thành `Công nghệ thông tin 01 - Khóa 2025`
4. Nhấn **Lưu** → flash message: *"Đã cập nhật lớp học thành công."*

### 7.4. Xóa lớp — minh họa ON DELETE SET NULL

1. Nhấn **Xóa** ở hàng KTPM01
2. Hộp thoại xác nhận: *"Bạn có chắc chắn muốn xóa lớp này? Sinh viên trong lớp sẽ được gỡ gán khỏi lớp."*
3. Nhấn OK
4. Flash message: *"Đã xóa lớp học. Sinh viên trong lớp được gỡ gán (class_id = NULL)."*
5. Vào danh sách sinh viên → SV004 (Phạm Thị D) giờ hiển thị badge xám **"Chưa gán lớp"**

> **Điểm quan trọng:** SV004 KHÔNG bị xóa — chỉ bị gỡ gán. Đây là hành vi của ON DELETE SET NULL được cấu hình trong Flyway migration.

### 7.5. Xem sinh viên theo lớp

1. Nhấn **"Xem SV"** ở hàng CNTT01
2. Chuyển sang trang `/classes/1/students`
3. Tiêu đề: *"Sinh viên lớp: Công nghệ thông tin 01"*
4. Bảng hiển thị 3 sinh viên: SV001 (Nguyễn Văn A), SV002 (Trần Thị B), SV005 (Hoàng Văn E)

> **Đây là minh họa điều hướng qua quan hệ @OneToMany** — từ Classroom truy cập List<Student>.

---

## 8. Demo — Quản lý Sinh viên

**URL:** `http://localhost:8080/students`

### 8.1. Xem danh sách sinh viên

Bảng gồm: ID, Mã SV, Họ tên, Email, **Lớp học** (badge màu — minh họa @ManyToOne), Hành động.

**Dữ liệu mẫu:**
```
SV001 | Nguyễn Văn A | a.nguyen@example.com | CNTT01 (xanh)
SV002 | Trần Thị B   | b.tran@example.com   | CNTT01 (xanh)
SV003 | Lê Văn C     | c.le@example.com     | CNTT02 (xanh)
SV004 | Phạm Thị D   | d.pham@example.com   | Chưa gán lớp (xám)  ← sau khi xóa KTPM01
SV005 | Hoàng Văn E  | e.hoang@example.com  | CNTT01 (xanh)
SV006 | Đặng Thị F   | f.dang@example.com   | CNTT02 (xanh)
```

### 8.2. Lọc và tìm kiếm

**Lọc theo lớp:**
- Chọn `Công nghệ thông tin 01` → nhấn Áp dụng → chỉ hiển thị SV001, SV002, SV005

**Tìm kiếm:**
- Nhập `Trần` vào ô tìm kiếm → nhấn Áp dụng → chỉ hiển thị SV002
- Nhập `SV00` → tìm theo mã SV → hiển thị tất cả bắt đầu bằng SV00

**Kết hợp:**
- Chọn lớp CNTT01 + nhập `Nguyễn` → chỉ hiển thị SV001

**Số dòng/trang:** Chọn 5, 10, hoặc 20 → nhấn Áp dụng.

**Xóa lọc:** Nhấn **"Xóa lọc"** → hiển thị lại tất cả sinh viên.

### 8.3. Thêm sinh viên mới

1. Nhấn **"+ Thêm Sinh viên mới"**
2. Nhập:
   - Mã sinh viên: `SV007`
   - Họ tên: `Vũ Thị G`
   - Email: `g.vu@example.com`
   - Lớp học: chọn `CNTT01 - Công nghệ thông tin 01`
3. Nhấn **Lưu** → flash message: *"Đã thêm sinh viên thành công."*

**Demo validation:**
- Để trống Họ tên → *"Họ tên không được để trống"*
- Nhập email sai: `abc@` → *"Email không hợp lệ"*

### 8.4. Sửa sinh viên — chuyển lớp (minh họa @ManyToOne)

1. Nhấn **Sửa** ở SV003 (Lê Văn C — đang ở CNTT02)
2. Form hiện dropdown Lớp học đang chọn sẵn CNTT02
3. Đổi lớp sang `CNTT01 - Công nghệ thông tin 01`
4. Nhấn **Lưu** → flash message: *"Đã cập nhật sinh viên thành công."*
5. Danh sách: SV003 giờ hiển thị badge `Công nghệ thông tin 01`

> Hibernate tự động UPDATE cột class_id trong bảng Students — đây là cơ chế của @ManyToOne + @JoinColumn.

### 8.5. Xóa sinh viên — CASCADE với Grades

1. Nhấn **Xóa** ở SV001 (đang có 2 bản ghi điểm)
2. Xác nhận → xóa thành công
3. Vào trang **Điểm số** → 2 bản ghi điểm của SV001 cũng biến mất

> ON DELETE CASCADE trên bảng Grades — xóa Student thì Grades liên quan cũng xóa theo.

---

## 9. Demo — Quản lý Giảng viên

**URL:** `http://localhost:8080/teachers`

### 9.1. Xem danh sách

```
GV001 | Nguyễn Văn Hùng | hung.nguyen@school.edu
GV002 | Trần Thị Lan     | lan.tran@school.edu
```

### 9.2. Thêm giảng viên

1. Nhấn **"+ Thêm GV"**
2. Nhập:
   - Mã GV: `GV003`
   - Họ tên: `Lê Văn Minh`
   - Email: `minh.le@school.edu`
3. Nhấn **Lưu** → flash message thành công

### 9.3. Gán giảng viên cho lớp

1. Vào **Quản lý Lớp học** → Sửa lớp CNTT02
2. Dropdown Giảng viên: chọn `GV003 - Lê Văn Minh`
3. Lưu → danh sách lớp cập nhật tên GV

### 9.4. Xóa giảng viên

Xóa GV001 → lớp CNTT01, CNTT02 vẫn tồn tại nhưng cột Giảng viên hiển thị badge **"Chưa gán"** (teacher_id = NULL).

---

## 10. Demo — Quản lý Môn học

**URL:** `http://localhost:8080/subjects`

### 10.1. Xem danh sách

```
JAVA01 | Lập trình Java  | 3 tín chỉ
WEB01  | Phát triển Web  | 3 tín chỉ
DB01   | Cơ sở dữ liệu   | 3 tín chỉ
```

### 10.2. Thêm môn học

1. Nhấn **"+ Thêm môn học"**
2. Nhập:
   - Mã môn: `NET01`
   - Tên môn: `Lập trình .NET`
   - Số tín chỉ: `3` (mặc định điền sẵn là 3)
3. Lưu → flash message thành công

**Demo validation số tín chỉ:**
- Nhập `0` → *"Số tín chỉ tối thiểu là 1"*
- Nhập `11` → *"Số tín chỉ tối đa là 10"*

---

## 11. Demo — Quản lý Điểm số

**URL:** `http://localhost:8080/grades`

### 11.1. Xem danh sách điểm

Dòng tóm tắt: *"Điểm trung bình hệ thống: 8.42 / 10"*

Bảng hiển thị: Sinh viên (Mã + Tên), Môn học, Điểm (badge xanh), Ngày thi, Hành động.

**Dữ liệu mẫu:**
```
SV001 - Nguyễn Văn A | Lập trình Java | 8.50 | 2025-12-15
SV001 - Nguyễn Văn A | Phát triển Web | 7.75 | 2025-12-20
SV002 - Trần Thị B   | Lập trình Java | 9.00 | 2025-12-15
```

### 11.2. Lọc điểm theo sinh viên

Chọn `SV001 - Nguyễn Văn A` → nhấn Lọc → chỉ hiển thị 2 điểm của SV001.

### 11.3. Nhập điểm mới

1. Nhấn **"+ Nhập điểm"**
2. Chọn:
   - Sinh viên: `SV003 - Lê Văn C`
   - Môn học: `WEB01 - Phát triển Web`
   - Điểm: `8.00`
   - Ngày thi: `2025-12-22` (tùy chọn)
3. Lưu → flash message: *"Đã thêm điểm thành công."*
4. Điểm TB cập nhật

**Demo validation:**
- Nhập điểm `-1` → *"Điểm tối thiểu là 0"*
- Nhập điểm `10.5` → *"Điểm tối đa là 10"*
- Thử nhập lại điểm SV003 môn WEB01 → lỗi database UNIQUE constraint

### 11.4. Sửa điểm

1. Nhấn **Sửa** ở bản ghi bất kỳ
2. Form tự điền sẵn SV, môn, điểm cũ
3. Sửa điểm → Lưu → flash message: *"Đã cập nhật điểm thành công."*

---

## 12. Demo — Swagger API

**URL:** `http://localhost:8080/swagger-ui.html`

Giao diện Swagger UI liệt kê toàn bộ REST API.

### Các nhóm API

| Nhóm | Base URL | Các endpoint |
|---|---|---|
| Dashboard API | `/api/v1/dashboard` | GET /stats |
| Classroom API | `/api/v1/classrooms` | GET, POST, PUT /{id}, DELETE /{id} |
| Student API | `/api/v1/students` | GET, POST, PUT /{id}, DELETE /{id} |
| Teacher API | `/api/v1/teachers` | GET, POST, PUT /{id}, DELETE /{id} |
| Subject API | `/api/v1/subjects` | GET, POST, PUT /{id}, DELETE /{id} |
| Grade API | `/api/v1/grades` | GET, POST, PUT /{id}, DELETE /{id} |

### Demo gọi API — Lấy danh sách lớp

1. Click **Classroom API** → click `GET /api/v1/classrooms`
2. Nhấn **Try it out** → **Execute**
3. Response JSON (200 OK):
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "classCode": "CNTT01",
      "className": "Công nghệ thông tin 01",
      "teacherId": 1,
      "teacherName": "Nguyễn Văn Hùng"
    }
  ]
}
```

### Demo gọi API — Tạo lớp học mới

1. Click `POST /api/v1/classrooms` → **Try it out**
2. Sửa request body:
```json
{
  "classCode": "AI01",
  "className": "Trí tuệ nhân tạo 01"
}
```
3. Execute → 201 Created → lớp mới xuất hiện trên web UI

### Demo gọi API — Health Check

Truy cập: `http://localhost:8080/actuator/health`
```json
{ "status": "UP" }
```

---

## 13. Xử lý lỗi thường gặp

### Lỗi: Cannot connect to SQL Server

Nguyên nhân: SQL Server chưa chạy hoặc TCP/IP chưa bật.

Fix:
- Kiểm tra services.msc → SQL Server → Running
- Bật TCP/IP port 1433 (xem Bước 1.4)
- Kiểm tra lại password trong application-dev.properties

---

### Lỗi: Database ClassroomDB does not exist

Fix: Chạy trong SSMS:
```sql
CREATE DATABASE ClassroomDB;
```

---

### Lỗi: Flyway checksum mismatch

Nguyên nhân: File migration bị sửa sau khi đã chạy.

Fix: Trong SSMS:
```sql
USE ClassroomDB;
DELETE FROM flyway_schema_history;
```
Sau đó restart ứng dụng.

---

### Lỗi: Port 8080 already in use

Fix 1 — Tìm và tắt process:
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Fix 2 — Đổi port trong application.properties:
```properties
server.port=8081
```
Truy cập: http://localhost:8081

---

### Lỗi: Code đỏ, Cannot resolve symbol

Fix: View → Tool Windows → Maven → nhấn nút Reload All Maven Projects (2 mũi tên vòng tròn).

---

### Lỗi: Whitelabel Error Page

Đảm bảo truy cập đúng URL trong bảng tóm tắt bên dưới.

---

## 14. Tóm tắt toàn bộ URL

| Tính năng | URL |
|---|---|
| Trang chủ (auto redirect) | http://localhost:8080/ |
| Dashboard tổng quan | http://localhost:8080/dashboard |
| Danh sách lớp học | http://localhost:8080/classes |
| Thêm lớp học | http://localhost:8080/classes/add |
| Sinh viên theo lớp | http://localhost:8080/classes/{id}/students |
| Danh sách sinh viên | http://localhost:8080/students |
| Tìm/lọc sinh viên | http://localhost:8080/students?classId=1&keyword=Nguyen |
| Thêm sinh viên | http://localhost:8080/students/add |
| Danh sách giảng viên | http://localhost:8080/teachers |
| Danh sách môn học | http://localhost:8080/subjects |
| Danh sách điểm | http://localhost:8080/grades |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Health Check | http://localhost:8080/actuator/health |
| API Docs JSON | http://localhost:8080/api/v1/api-docs |