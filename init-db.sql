-- ============================================================
-- Script khoi tao Database: Quan ly Lop hoc & Sinh vien
-- De tai 07: One-to-Many va Many-to-One trong JPA/Hibernate
--
-- HUONG DAN: Chay file nay trong SSMS truoc khi khoi dong ung dung
-- 1. Mo SSMS -> New Query -> mo file nay -> Execute (F5)
-- ============================================================

-- 1. Tao Database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ClassroomDB')
BEGIN
    CREATE DATABASE ClassroomDB;
    PRINT 'Database ClassroomDB da duoc tao.';
END
ELSE
BEGIN
    PRINT 'Database ClassroomDB da ton tai.';
END
GO

USE ClassroomDB;
GO

-- 2. Tao bang Classes (Lop hoc)
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Classes]') AND type = N'U')
BEGIN
    CREATE TABLE Classes (
        id         BIGINT IDENTITY(1,1) PRIMARY KEY,
        class_code NVARCHAR(50)  NOT NULL UNIQUE,
        class_name NVARCHAR(255) NOT NULL
    );
    PRINT 'Bang Classes da duoc tao.';
END
GO

-- 3. Tao bang Students (Sinh vien)
-- class_id cho phep NULL: sinh vien co the chua duoc gan lop
-- ON DELETE SET NULL: khi xoa lop, sinh vien bi go gan (khong bi xoa)
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
    PRINT 'Bang Students da duoc tao.';
END
GO

-- 4. Du lieu mau - Lop hoc
IF NOT EXISTS (SELECT 1 FROM Classes WHERE class_code = 'CNTT01')
BEGIN
    INSERT INTO Classes (class_code, class_name) VALUES
        ('CNTT01', N'Cong nghe thong tin 01'),
        ('CNTT02', N'Cong nghe thong tin 02'),
        ('KTPM01', N'Ky thuat phan mem 01');
    PRINT 'Da chen du lieu mau vao Classes.';
END
GO

-- 5. Du lieu mau - Sinh vien
IF NOT EXISTS (SELECT 1 FROM Students WHERE student_code = 'SV001')
BEGIN
    DECLARE @class1 BIGINT = (SELECT id FROM Classes WHERE class_code = 'CNTT01');
    DECLARE @class2 BIGINT = (SELECT id FROM Classes WHERE class_code = 'CNTT02');
    DECLARE @class3 BIGINT = (SELECT id FROM Classes WHERE class_code = 'KTPM01');

    INSERT INTO Students (student_code, full_name, email, class_id) VALUES
        ('SV001', N'Nguyen Van A', 'a.nguyen@example.com', @class1),
        ('SV002', N'Tran Thi B',  'b.tran@example.com',   @class1),
        ('SV003', N'Le Van C',    'c.le@example.com',      @class2),
        ('SV004', N'Pham Thi D',  'd.pham@example.com',   @class3),
        ('SV005', N'Hoang Van E', 'e.hoang@example.com',  @class1),
        ('SV006', N'Dang Thi F',  'f.dang@example.com',   @class2);
    PRINT 'Da chen du lieu mau vao Students.';
END
GO

-- 6. Kiem tra ket qua
SELECT 'Classes' AS [Bang], COUNT(*) AS [So ban ghi] FROM Classes
UNION ALL
SELECT 'Students', COUNT(*) FROM Students;
GO

PRINT '=== Khoi tao database hoan tat! ===';
GO