# HƯỚNG DẪN KẾT NỐI SQL SERVER VỚI INTELLIJ IDEA

---

## Yêu cầu

- SQL Server 2019+ (Developer Edition — miễn phí)
- SQL Server Management Studio (SSMS) 19+
- IntelliJ IDEA 2022+

---

## Bước 1 — Cài đặt SQL Server

Tải SQL Server Developer Edition (miễn phí):
https://www.microsoft.com/en-us/sql-server/sql-server-downloads

Tải SSMS:
https://learn.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms

---

## Bước 2 — Bật SQL Server Authentication

1. Mở SSMS → kết nối bằng Windows Authentication
2. Chuột phải tên server → **Properties** → tab **Security**
3. Chọn **SQL Server and Windows Authentication mode** → OK
4. Chuột phải tên server → **Restart**

---

## Bước 3 — Kích hoạt tài khoản sa

Trong SSMS → New Query:
```sql
ALTER LOGIN sa ENABLE;
ALTER LOGIN sa WITH PASSWORD = 'YourPassword123';
GO
```

---

## Bước 4 — Bật TCP/IP port 1433

1. Tìm **SQL Server Configuration Manager** trong Start Menu
2. **SQL Server Network Configuration** → **Protocols for MSSQLSERVER**
3. Chuột phải **TCP/IP** → **Enable**
4. Double-click **TCP/IP** → tab **IP Addresses** → **IPAll** → TCP Port = `1433`
5. OK → Restart SQL Server (services.msc → SQL Server → Restart)

---

## Bước 5 — Tạo database ClassroomDB

```sql
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ClassroomDB')
    CREATE DATABASE ClassroomDB;
GO
```

---

## Bước 6 — Cấu hình application.properties

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourPassword123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

**Nếu dùng SQL Server Express:**
```properties
spring.datasource.url=jdbc:sqlserver://localhost\SQLEXPRESS;databaseName=ClassroomDB;encrypt=true;trustServerCertificate=true
```

---

## Bước 7 — Kết nối Database trong IntelliJ IDEA (tùy chọn, chỉ Ultimate)

1. View → Tool Windows → **Database**
2. Nhấn **+** → **Data Source** → **Microsoft SQL Server**
3. Điền:
   - Host: `localhost`, Port: `1433`
   - User: `sa`, Password: *(mật khẩu)*
   - Database: `ClassroomDB`
4. **Download missing driver files** → **Test Connection** → OK

---

## Xử lý lỗi thường gặp

### Connection refused / Cannot connect

- Kiểm tra SQL Server đang chạy: `services.msc`
- Bật TCP/IP và đặt port 1433 (Bước 4)
- Kiểm tra password trong `application.properties`

### Login failed for user 'sa'

- Bật SQL Server Authentication (Bước 2)
- Kích hoạt tài khoản sa (Bước 3)

### SSL / Certificate error

Đảm bảo URL có đủ 2 tham số:
```
encrypt=true;trustServerCertificate=true
```

---

## Kiểm tra nhanh

```
[ ] SQL Server đang chạy (services.msc → Running)
[ ] SQL Server Authentication đã bật
[ ] Tài khoản sa đã bật và có mật khẩu
[ ] TCP/IP port 1433 đã bật
[ ] Database ClassroomDB đã tồn tại
[ ] Password đúng trong application.properties
[ ] Ứng dụng khởi động thấy: Tomcat started on port 8080
```