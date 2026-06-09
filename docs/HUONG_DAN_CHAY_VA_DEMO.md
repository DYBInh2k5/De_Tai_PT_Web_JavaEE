# HƯỚNG DẪN CHẠY DỰ ÁN VÀ DEMO CHI TIẾT
## Ứng dụng Quản lý Lớp học & Sinh viên — Spring Boot + SQL Server

---

## MỤC LỤC

1. [Yêu cầu môi trường](#1-yeu-cau)
2. [Bước 1 — Chuẩn bị SQL Server](#2-buoc-1)
3. [Bước 2 — Mở dự án trong IntelliJ IDEA](#3-buoc-2)
4. [Bước 3 — Cấu hình kết nối database](#4-buoc-3)
5. [Bước 4 — Chạy ứng dụng](#5-buoc-4)
6. [Demo — Quản lý Lớp học](#6-demo-lop-hoc)
7. [Demo — Quản lý Sinh viên](#7-demo-sinh-vien)
8. [Xử lý lỗi thường gặp](#8-xu-ly-loi)

---

## 1. Yêu cầu môi trường

| Phần mềm | Phiên bản |
|---|---|
| **JDK** | 17+ |
| **IntelliJ IDEA** | 2022+ (Community hoặc Ultimate) |
| **SQL Server** | 2019+ (Developer Edition — miễn phí) |
| **SSMS** | 19+ (SQL Server Management Studio) |

> Không cần cài Maven hay Tomcat riêng — IntelliJ và Spring Boot đã có sẵn.

---

## 2. Bước 1 — Chuẩn bị SQL Server

### 2.1. Kiểm tra SQL Server đang chạy

`Win + R` → `services.msc` → tìm **SQL Server (MSSQLSERVER)** → Status = **Running**.

Nếu chưa chạy: chuột phải → **Start**.

### 2.2. Bật SQL Server Authentication (nếu chưa)

1. SSMS → chuột phải tên server → **Properties** → tab **Security**
2. Chọn **SQL Server and Windows Authentication mode** → OK
3. Chuột phải tên server → **Restart**

### 2.3. Đặt mật khẩu tài khoản sa

```sql
ALTER LOGIN sa ENABLE;
ALTER LOGIN sa WITH PASSWORD = 'YourPassword123';
```

### 2.4. Bật TCP/IP port 1433

1. Mở **SQL Server Configuration Manager**
2. **SQL Server Network Configuration** → **Protocols for MSSQLSERVER**
3. Chuột phải **TCP/IP** → **Enable**
4. Double-click **TCP/IP** → tab **IP Addresses** → **IPAll** → TCP Port = `1433`
5. OK → Restart SQL Server

### 2.5. Tạo database và bảng

Mở SSMS → **New Query** → mở file `init-db.sql` → **Execute (F5)**

Kết quả thành công:
```
Database ClassroomDB da duoc tao.
Bang Classes da duoc tao.
Bang Students da duoc tao.
Da chen du lieu mau.
=== Khoi tao database hoan tat! ===
```

> Nếu bỏ qua bước này: chỉ cần tạo database rỗng `CREATE DATABASE ClassroomDB;`
> Hibernate sẽ tự tạo bảng khi ứng dụng khởi động.

---

## 3. Bước 2 — Mở dự án trong IntelliJ IDEA

### 3.1. Mở đúng folder

```
File → Open → chọn folder "De_Tai_PT_Web_JavaEE" (folder chứa pom.xml) → OK
```

Khi hỏi **"Trust this project?"** → chọn **Trust Project**.

### 3.2. Chờ Maven tải dependencies

Quan sát thanh loading **góc dưới phải** IntelliJ — chờ đến khi hết loading (1–5 phút lần đầu).

### 3.3. Kiểm tra JDK 17

```
File → Project Structure → Project → SDK → chọn JDK 17
```

Nếu chưa có: `SDKs → + → Download JDK → Version 17 → Download`

### 3.4. Nếu code hiển thị màu đỏ

View → Tool Windows → **Maven** → nhấn nút **Reload All Maven Projects** (biểu tượng 2 mũi tên tròn).

---

## 4. Bước 3 — Cấu hình kết nối database

Mở file: `src/main/resources/application.properties`

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

### 5.1. Cách 1: Chạy bằng Maven Wrapper (khuyên dùng)

Mở **Command Prompt** hoặc **PowerShell** tại thư mục dự án (`De_Tai_PT_Web_JavaEE`):

```bash
mvnw spring-boot:run
```

> Lần đầu chạy sẽ hơi chậm vì Maven tải dependencies. Kiên nhẫn chờ.

### 5.2. Cách 2: Chạy bằng Maven (đã cài Maven toàn cục)

```bash
mvn spring-boot:run
```

### 5.3. Cách 3: Chạy bằng IntelliJ IDEA

1. **File → Open** → chọn thư mục `De_Tai_PT_Web_JavaEE` → **OK**
2. Chờ IntelliJ tải dependencies (góc dưới phải)
3. Mở file: `src/main/java/com/ttjavaee/classroom/ClassroomManagementApplication.java`
4. Chuột phải → **Run 'ClassroomManagementApplication'**
5. Hoặc click nút ▶ xanh cạnh `public static void main`

### 5.4. Kiểm tra log thành công

Dù chạy cách nào, khi console hiện:
```
Hibernate: create table Classes (...)
Hibernate: create table Students (...)
Tomcat started on port 8080 (http)
Started ClassroomManagementApplication in X.XXX seconds
```
→ **Ứng dụng đã sẵn sàng!**

### 5.5. Truy cập

Mở trình duyệt → **http://localhost:8080**

Trang tự chuyển về **http://localhost:8080/classes**

---

## CÁCH TẮT ỨNG DỤNG

Khi dùng xong, cần tắt server để giải phóng cổng 8080.

| Phương pháp | Cách làm |
|-------------|----------|
| **Nếu chạy bằng CMD** | Nhấn `Ctrl + C` → gõ `Y` → Enter |
| **Nếu chạy bằng IntelliJ** | Nhấn ■ **Stop** (nút ô vuông đỏ) trong tab Run |
| **Task Manager** | Mở Task Manager → Processes → chọn `java` → End Task |
| **Lệnh PowerShell/CMD** | `taskkill /F /IM java.exe` (tắt **mọi** Java trên máy) |

### Kiểm tra cổng 8080 đã được giải phóng chưa

Mở CMD chạy:
```bash
netstat -ano | findstr :8080
```
- **Không có kết quả** → cổng trống, có thể chạy lại.
- **Có kết quả** → còn process đang giữ cổng, cần tắt thủ công.

> ⚠️ **Lưu ý quan trọng:** Nếu không tắt server trước khi chạy lại, bạn sẽ gặp lỗi:
> ```
> Web server failed to start. Port 8080 was already in use.
> >
> Cách xử lý: tắt server cũ (theo hướng dẫn trên) rồi chạy lại.
> ```
> 
> 💡 **Mẹo:** Nếu chỉ muốn tạm dừng xem code mà không tắt server, bạn có thể để nguyên cửa sổ CMD đó. Server vẫn chạy ngầm, trình duyệt vẫn truy cập được. Chỉ khi nào không dùng nữa mới cần tắt.

---

## 6. Demo — Quản lý Lớp học

**URL:** `http://localhost:8080/classes`

### 6.1. Xem danh sách lớp

Bảng gồm: ID, Mã lớp, Tên lớp, **Số SV** (badge xanh — minh họa @OneToMany), Hành động.

Dữ liệu mẫu sau khi chạy `init-db.sql`:
```
1 | CNTT01 | Cong nghe thong tin 01 | 3
2 | CNTT02 | Cong nghe thong tin 02 | 2
3 | KTPM01 | Ky thuat phan mem 01   | 1
```

### 6.2. Thêm lớp mới

1. Nhấn **"+ Thêm Lớp mới"**
2. Nhập Mã lớp: `HTTT01`, Tên lớp: `He thong thong tin 01`
3. Nhấn **Lưu** → quay lại danh sách, thấy lớp mới

Validation: Để trống Mã lớp → trình duyệt báo required.

### 6.3. Sửa lớp

Nhấn **Sửa** → form tự điền dữ liệu cũ → sửa → Lưu.

### 6.4. Xóa lớp — minh họa ON DELETE SET NULL

1. Nhấn **Xóa** lớp KTPM01 → xác nhận
2. Lớp bị xóa. Sinh viên SV004 (Pham Thi D) **không bị xóa**
3. Vào danh sách sinh viên → SV004 hiển thị badge xám **"Chua gan lop"**

> Đây là minh họa ràng buộc ON DELETE SET NULL trong SQL Server.

### 6.5. Xem sinh viên theo lớp

Nhấn **"Xem SV"** ở lớp CNTT01 → trang `/classes/1/students`

Hiển thị: tiêu đề tên lớp + bảng 3 sinh viên (SV001, SV002, SV005).

> Đây là minh họa điều hướng qua quan hệ @OneToMany.

---

## 7. Demo — Quản lý Sinh viên

**URL:** `http://localhost:8080/students`

### 7.1. Xem danh sách sinh viên

Bảng gồm: ID, Mã SV, Họ tên, Email, **Lớp học** (badge xanh — minh họa @ManyToOne), Hành động.

Dữ liệu mẫu:
```
SV001 | Nguyen Van A  | CNTT01
SV002 | Tran Thi B    | CNTT01
SV003 | Le Van C      | CNTT02
SV004 | Pham Thi D    | Chua gan lop  (sau khi xoa KTPM01)
SV005 | Hoang Van E   | CNTT01
SV006 | Dang Thi F    | CNTT02
```

### 7.2. Lọc sinh viên theo lớp

Dropdown **"Lọc theo lớp"** → chọn `Cong nghe thong tin 01` → nhấn **Lọc**

Kết quả: chỉ hiển thị SV001, SV002, SV005.

Nhấn **"Xóa lọc"** → hiển thị lại tất cả.

### 7.3. Thêm sinh viên — gán vào lớp

1. Nhấn **"+ Thêm Sinh viên mới"**
2. Nhập:
   - Mã SV: `SV007`
   - Họ tên: `Vu Thi G`
   - Email: `g.vu@example.com`
   - Lớp học: chọn `CNTT01 - Cong nghe thong tin 01`
3. Nhấn **Lưu**

> Dropdown chọn lớp minh họa việc gán quan hệ @ManyToOne khi thêm sinh viên.

### 7.4. Sửa sinh viên — chuyển lớp

1. Nhấn **Sửa** sinh viên SV003
2. Dropdown Lớp học đang chọn sẵn CNTT02
3. Đổi sang `CNTT01 - Cong nghe thong tin 01` → Lưu
4. Danh sách: SV003 giờ hiển thị lớp CNTT01

> Hibernate tự UPDATE cột class_id — đây là cơ chế @ManyToOne + @JoinColumn.

### 7.5. Xóa sinh viên

Nhấn **Xóa** → xác nhận → sinh viên bị xóa khỏi hệ thống.

---

## 8. Xử lý lỗi thường gặp

### Lỗi: Cannot connect to SQL Server

Fix:
- Kiểm tra SQL Server đang chạy (services.msc)
- Bật TCP/IP port 1433
- Kiểm tra lại password trong application.properties

---

### Lỗi: Database 'ClassroomDB' does not exist

Fix: Chạy trong SSMS:
```sql
CREATE DATABASE ClassroomDB;
```

---

### Lỗi: Port 8080 already in use

Fix 1: Tắt process đang dùng port:
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Fix 2: Đổi port trong application.properties:
```properties
server.port=8081
```

---

### Lỗi: Code đỏ, Cannot resolve symbol

Fix: View → Tool Windows → Maven → **Reload All Maven Projects**

---

## Tóm tắt URL

| Chức năng | URL |
|---|---|
| Danh sách lớp | http://localhost:8080/classes |
| Thêm lớp | http://localhost:8080/classes/add |
| SV theo lớp | http://localhost:8080/classes/{id}/students |
| Danh sách sinh viên | http://localhost:8080/students |
| Lọc SV theo lớp | http://localhost:8080/students?classId={id} |
| Thêm sinh viên | http://localhost:8080/students/add |