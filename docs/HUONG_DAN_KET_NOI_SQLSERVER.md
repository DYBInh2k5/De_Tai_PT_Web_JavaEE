# HƯỚNG DẪN KẾT NỐI SQL SERVER VỚI INTELLIJ IDEA

> Hướng dẫn này dành cho dự án **Quản lý Lớp học & Sinh viên** sử dụng Spring Boot + SQL Server.

---

## MỤC LỤC

1. [Yêu cầu cài đặt](#1-yêu-cầu-cài-đặt)
2. [Bước 1 — Cài đặt và khởi động SQL Server](#2-bước-1--cài-đặt-và-khởi-động-sql-server)
3. [Bước 2 — Tạo Database bằng script SQL](#3-bước-2--tạo-database-bằng-script-sql)
4. [Bước 3 — Kết nối SQL Server trong IntelliJ IDEA](#4-bước-3--kết-nối-sql-server-trong-intellij-idea)
5. [Bước 4 — Cấu hình application.properties](#5-bước-4--cấu-hình-applicationproperties)
6. [Bước 5 — Chạy ứng dụng và kiểm tra](#6-bước-5--chạy-ứng-dụng-và-kiểm-tra)
7. [Xử lý lỗi thường gặp](#7-xử-lý-lỗi-thường-gặp)

---

## 1. Yêu cầu cài đặt

Trước khi bắt đầu, đảm bảo máy đã cài đủ:

| Phần mềm | Phiên bản tối thiểu | Link tải |
|---|---|---|
| **JDK** | 17+ | https://www.oracle.com/java/technologies/downloads/ |
| **IntelliJ IDEA** | 2022+ (Community hoặc Ultimate) | https://www.jetbrains.com/idea/download/ |
| **SQL Server** | 2019+ (Developer Edition — miễn phí) | https://www.microsoft.com/en-us/sql-server/sql-server-downloads |
| **SQL Server Management Studio (SSMS)** | 19+ | https://learn.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms |
| **Maven** | 3.6+ (đã tích hợp trong IntelliJ) | — |

> **Lưu ý:** SQL Server **Developer Edition** miễn phí và đầy đủ tính năng, dùng cho mục đích học tập/phát triển.

---

## 2. Bước 1 — Cài đặt và khởi động SQL Server

### 2.1. Kiểm tra SQL Server đã chạy chưa

Mở **Services** (nhấn `Win + R` → gõ `services.msc`) và tìm:
- `SQL Server (MSSQLSERVER)` — phải ở trạng thái **Running**

Hoặc kiểm tra bằng PowerShell:
```powershell
Get-Service -Name "MSSQLSERVER"
# Kết quả mong muốn: Status = Running
```

Nếu chưa chạy, click chuột phải → **Start**.

### 2.2. Bật SQL Server Authentication

Mặc định SQL Server chỉ cho phép Windows Authentication. Cần bật thêm **SQL Server Authentication** để dùng tài khoản `sa`:

1. Mở **SSMS** → kết nối vào server
2. Chuột phải vào tên server → **Properties**
3. Chọn tab **Security**
4. Chọn **SQL Server and Windows Authentication mode**
5. Nhấn **OK**
6. **Restart SQL Server** (chuột phải vào server → Restart)

### 2.3. Đặt mật khẩu cho tài khoản `sa`

Trong SSMS:
```sql
-- Bật tài khoản sa và đặt mật khẩu
ALTER LOGIN sa ENABLE;
ALTER LOGIN sa WITH PASSWORD = 'YourPassword123';
GO
```

> ⚠️ Mật khẩu SQL Server phải đủ mạnh: ít nhất 8 ký tự, có chữ hoa, chữ thường, số.

### 2.4. Bật TCP/IP cho SQL Server

1. Mở **SQL Server Configuration Manager**
   - Tìm trong Start Menu hoặc chạy: `SQLServerManager16.msc`
2. Vào **SQL Server Network Configuration** → **Protocols for MSSQLSERVER**
3. Chuột phải **TCP/IP** → **Enable**
4. Double-click **TCP/IP** → tab **IP Addresses**
5. Kéo xuống **IPAll** → đặt **TCP Port = 1433**
6. Nhấn **OK** → Restart SQL Server

---

## 3. Bước 2 — Tạo Database bằng script SQL

### 3.1. Mở SSMS và kết nối

1. Mở **SQL Server Management Studio**
2. Điền thông tin kết nối:
   - **Server name:** `localhost` hoặc `localhost\SQLEXPRESS` (nếu dùng Express)
   - **Authentication:** SQL Server Authentication
   - **Login:** `sa`
   - **Password:** *(mật khẩu bạn đã đặt)*
3. Nhấn **Connect**

### 3.2. Chạy script khởi tạo

1. Trong SSMS, nhấn **New Query** (hoặc `Ctrl + N`)
2. Mở file `init-db.sql` trong project (dùng File → Open hoặc kéo thả vào SSMS)
3. Nhấn **Execute** (hoặc `F5`)
4. Kiểm tra kết quả ở tab **Messages**:
   ```
   Database ClassroomDB đã được tạo.
   Bảng Classes đã được tạo.
   Bảng Students đã được tạo.
   Đã chèn dữ liệu mẫu vào bảng Classes.
   Đã chèn dữ liệu mẫu vào bảng Students.
   === Khởi tạo database hoàn tất! ===
   ```

### 3.3. Kiểm tra dữ liệu

```sql
USE ClassroomDB;
SELECT * FROM Classes;
SELECT * FROM Students;
```

---

## 4. Bước 3 — Kết nối SQL Server trong IntelliJ IDEA

> Tính năng **Database Tool** có đầy đủ trong **IntelliJ IDEA Ultimate**.  
> Với **Community Edition**, bạn bỏ qua bước này — ứng dụng vẫn chạy bình thường qua `application.properties`.

### 4.1. Mở Database Tool Window

- Menu **View** → **Tool Windows** → **Database**
- Hoặc nhấn biểu tượng **Database** ở thanh bên phải

### 4.2. Thêm kết nối mới

1. Nhấn dấu **+** (Add) → **Data Source** → **Microsoft SQL Server**

2. Điền thông tin:

   | Trường | Giá trị |
   |---|---|
   | **Host** | `localhost` |
   | **Port** | `1433` |
   | **Authentication** | User & Password |
   | **User** | `sa` |
   | **Password** | *(mật khẩu của bạn)* |
   | **Database** | `ClassroomDB` |

3. Nhấn **Download missing driver files** (lần đầu IntelliJ tự tải JDBC driver)

4. Nhấn **Test Connection**
   - ✅ **Successful** → kết nối thành công
   - ❌ Lỗi → xem phần [Xử lý lỗi](#7-xử-lý-lỗi-thường-gặp)

5. Nhấn **OK** để lưu

### 4.3. Duyệt dữ liệu trong IntelliJ

Sau khi kết nối thành công, trong **Database** panel:
```
localhost@ClassroomDB
  └── ClassroomDB
        └── dbo
              ├── Tables
              │     ├── Classes      ← double-click để xem dữ liệu
              │     └── Students
              └── ...
```

Double-click vào bảng để xem/sửa dữ liệu trực tiếp trong IntelliJ.

---

## 5. Bước 4 — Cấu hình application.properties

Mở file `src/main/resources/application.properties` và cập nhật:

```properties
# ============================================
# KẾT NỐI SQL SERVER — SỬA THÔNG TIN NÀY
# ============================================

# Nếu dùng SQL Server mặc định (instance MSSQLSERVER):
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true

# Nếu dùng SQL Server Express (instance SQLEXPRESS):
# spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true

spring.datasource.username=sa
spring.datasource.password=YourPassword123        ← ĐỔI THÀNH MẬT KHẨU CỦA BẠN
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# ============================================
# JPA / HIBERNATE
# ============================================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect

# ============================================
# THYMELEAF
# ============================================
spring.thymeleaf.cache=false
spring.thymeleaf.encoding=UTF-8

# ============================================
# SERVER
# ============================================
server.port=8080
```

### Giải thích các tham số trong URL kết nối

| Tham số | Ý nghĩa |
|---|---|
| `localhost:1433` | Host và port của SQL Server |
| `databaseName=ClassroomDB` | Tên database cần kết nối |
| `encrypt=true` | Mã hóa kết nối |
| `trustServerCertificate=true` | Bỏ qua kiểm tra SSL certificate (dùng cho local dev) |

---

## 6. Bước 5 — Chạy ứng dụng và kiểm tra

### 6.1. Mở project trong IntelliJ

1. **File** → **Open** → chọn folder `De_Tai_PT_Web_JavaEE` (folder chứa `pom.xml`)
2. Chọn **Open as Project**
3. Chờ IntelliJ load Maven dependencies (thanh tiến trình ở góc dưới phải)

### 6.2. Chạy ứng dụng

1. Tìm file `ClassroomManagementApplication.java`:
   ```
   src/main/java/com/ttjavaee/classroom/ClassroomManagementApplication.java
   ```
2. Click nút ▶️ **Run** bên cạnh `main()` hoặc nhấn `Shift + F10`

### 6.3. Kiểm tra log khởi động

Trong **Run** console, tìm dòng:
```
Started ClassroomManagementApplication in X.XXX seconds
Tomcat started on port(s): 8080 (http)
```

Nếu thấy 2 dòng này → ứng dụng đã chạy thành công.

### 6.4. Truy cập ứng dụng

Mở trình duyệt → **http://localhost:8080**

---

## 7. Xử lý lỗi thường gặp

### ❌ Lỗi: `Connection refused` hoặc `Cannot connect to localhost:1433`

**Nguyên nhân:** SQL Server chưa chạy hoặc TCP/IP chưa bật.

**Cách fix:**
1. Kiểm tra SQL Server đang chạy (xem Bước 1.1)
2. Bật TCP/IP và đặt port 1433 (xem Bước 1.4)
3. Restart SQL Server sau khi thay đổi

---

### ❌ Lỗi: `Login failed for user 'sa'`

**Nguyên nhân:** Sai mật khẩu hoặc SQL Server Authentication chưa bật.

**Cách fix:**
1. Bật SQL Server Authentication (xem Bước 1.2)
2. Kiểm tra lại mật khẩu trong `application.properties`
3. Reset mật khẩu `sa` trong SSMS (xem Bước 1.3)

---

### ❌ Lỗi: `Database 'ClassroomDB' does not exist`

**Nguyên nhân:** Chưa chạy script `init-db.sql`.

**Cách fix:**
Chạy file `init-db.sql` trong SSMS (xem Bước 2).

---

### ❌ Lỗi: `SSL connection` hoặc certificate error

**Nguyên nhân:** Thiếu tham số SSL trong connection URL.

**Cách fix:**
Đảm bảo URL có đủ 2 tham số:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
```

---

### ❌ Lỗi: `Port 8080 already in use`

**Nguyên nhân:** Có ứng dụng khác đang dùng port 8080.

**Cách fix — Cách 1:** Đổi port trong `application.properties`:
```properties
server.port=8081
```
Sau đó truy cập `http://localhost:8081`

**Cách fix — Cách 2:** Tìm và tắt process đang dùng port 8080:
```powershell
# Tìm process dùng port 8080
netstat -ano | findstr :8080

# Tắt process (thay XXXX bằng PID tìm được)
taskkill /PID XXXX /F
```

---

### ❌ Lỗi: `Named instance` — không kết nối được SQL Server Express

Nếu bạn cài **SQL Server Express**, tên instance mặc định là `SQLEXPRESS`. Sửa URL:
```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
```
> Lưu ý dùng `\\` (double backslash) trong file `.properties`.

---

## Tóm tắt nhanh (Checklist)

```
□ SQL Server đang chạy (Services → MSSQLSERVER → Running)
□ SQL Server Authentication đã bật
□ Tài khoản sa đã được bật và có mật khẩu
□ TCP/IP đã bật, port 1433
□ Đã chạy init-db.sql → database ClassroomDB tồn tại
□ application.properties đã điền đúng password
□ IntelliJ đã load xong Maven dependencies
□ Chạy ClassroomManagementApplication.java → thấy "Started on port 8080"
□ Truy cập http://localhost:8080 → thấy giao diện
```