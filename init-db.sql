-- ============================================================
-- Script khởi tạo Database cho ứng dụng Quản lý Lớp học
-- Chạy script này trong SQL Server Management Studio (SSMS)
-- hoặc Azure Data Studio trước khi khởi động ứng dụng
-- ============================================================

-- 1. Tạo Database nếu chưa tồn tại
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ClassroomDB')
BEGIN
    CREATE DATABASE ClassroomDB;
    PRINT 'Database ClassroomDB đã được tạo.';
END
ELSE
BEGIN
    PRINT 'Database ClassroomDB đã tồn tại.';
END
GO

USE ClassroomDB;
GO

-- 2. Tạo bảng Classes (Lớp học)
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Classes]') AND type = N'U')
BEGIN
    CREATE TABLE Classes (
        id         BIGINT IDENTITY(1,1) PRIMARY KEY,
        class_code NVARCHAR(50)  NOT NULL UNIQUE,
        class_name NVARCHAR(255) NOT NULL
    );
    PRINT 'Bảng Classes đã được tạo.';
END
GO

-- 3. Tạo bảng Students (Sinh viên)
-- class_id cho phép NULL để sinh viên có thể chưa được gán lớp
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Students]') AND type = N'U')
BEGIN
    CREATE TABLE Students (
        id           BIGINT IDENTITY(1,1) PRIMARY KEY,
        student_code NVARCHAR(50)  NOT NULL UNIQUE,
        full_name    NVARCHAR(255) NOT NULL,
        email        NVARCHAR(255) NOT NULL,
        class_id     BIGINT NULL,
        CONSTRAINT FK_Student_Class
            FOREIGN KEY (class_id) REFERENCES Classes(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE
    );
    PRINT 'Bảng Students đã được tạo.';
END
GO

-- 4. Chèn dữ liệu mẫu cho bảng Classes
IF NOT EXISTS (SELECT 1 FROM Classes WHERE class_code = 'CNTT01')
BEGIN
    INSERT INTO Classes (class_code, class_name) VALUES
        ('CNTT01', N'Công nghệ thông tin 01'),
        ('CNTT02', N'Công nghệ thông tin 02'),
        ('KTPM01', N'Kỹ thuật phần mềm 01');
    PRINT 'Đã chèn dữ liệu mẫu vào bảng Classes.';
END
GO

-- 5. Chèn dữ liệu mẫu cho bảng Students
IF NOT EXISTS (SELECT 1 FROM Students WHERE student_code = 'SV001')
BEGIN
    DECLARE @class1 BIGINT = (SELECT id FROM Classes WHERE class_code = 'CNTT01');
    DECLARE @class2 BIGINT = (SELECT id FROM Classes WHERE class_code = 'CNTT02');
    DECLARE @class3 BIGINT = (SELECT id FROM Classes WHERE class_code = 'KTPM01');

    INSERT INTO Students (student_code, full_name, email, class_id) VALUES
        ('SV001', N'Nguyễn Văn A',  'a.nguyen@example.com', @class1),
        ('SV002', N'Trần Thị B',    'b.tran@example.com',   @class1),
        ('SV003', N'Lê Văn C',      'c.le@example.com',     @class2),
        ('SV004', N'Phạm Thị D',    'd.pham@example.com',   @class3),
        ('SV005', N'Hoàng Văn E',   'e.hoang@example.com',  @class1),
        ('SV006', N'Đặng Thị F',    'f.dang@example.com',   @class2);
    PRINT 'Đã chèn dữ liệu mẫu vào bảng Students.';
END
GO

-- 6. Kiểm tra dữ liệu
SELECT 'Classes' AS [Bảng], COUNT(*) AS [Số bản ghi] FROM Classes
UNION ALL
SELECT 'Students', COUNT(*) FROM Students;
GO

PRINT '=== Khởi tạo database hoàn tất! ===';
GO
