-- ============================================================
-- Script khoi tao Database: Quan ly Lop hoc & Sinh vien
-- De tai 07: One-to-Many va Many-to-One trong JPA/Hibernate
--
-- CAC BUOC CHAY:
--   1. Mo SQL Server Management Studio (SSMS)
--   2. File -> Open -> mo file nay
--   3. Execute (F5)
--
-- FILE NAY SE:
--   - Tao database ClassroomDB (neu chua co)
--   - Tao bang Classes va Students
--   - Tao rang buoc khoa ngoai FK_Student_Class
--   - Chen du lieu mau (3 lop, 6 sinh vien)
-- ============================================================

-- ============================================================
-- 1. TAO DATABASE
-- Kiem tra neu database chua ton tai thi tao moi
-- ============================================================
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

-- Chuyen sang su dung database ClassroomDB
USE ClassroomDB;
GO

-- ============================================================
-- 2. TAO BANG CLASSES (Lop hoc)
--
-- id:         BIGINT, tu dong tang (IDENTITY), khoa chinh
-- class_code: NVARCHAR(50), khong duoc NULL, duy nhat (UNIQUE)
--              VD: CNTT01, KTPM01
-- class_name: NVARCHAR(255), khong duoc NULL
--              VD: Cong nghe thong tin 01
-- ============================================================
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

-- ============================================================
-- 3. TAO BANG STUDENTS (Sinh vien)
--
-- id:           BIGINT, tu dong tang (IDENTITY), khoa chinh
-- student_code: NVARCHAR(50), khong duoc NULL, duy nhat
--                VD: SV001, SV002
-- full_name:    NVARCHAR(255), khong duoc NULL
-- email:        NVARCHAR(255), khong duoc NULL
-- class_id:     BIGINT, duoc phep NULL
--                -> NULL co nghia sinh vien chua duoc xep lop
--
-- KHOA NGOAI (Foreign Key):
--   FK_Student_Class: class_id tham chieu den Classes.id
--
--   ON DELETE SET NULL:
--     Khi xoa mot lop hoc, tat ca sinh vien trong lop do
--     se bi SET class_id = NULL (khong bi xoa theo)
--     -> DAM BAO TOAN VEN DU LIEU
--
--   ON UPDATE CASCADE:
--     Khi sua id cua lop, tu dong cap nhat class_id
--     trong bang Students (it dung nhung day du)
-- ============================================================
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

-- ============================================================
-- 4. CHEN DU LIEU MAU - LOP HOC
-- 3 lop: CNTT01, CNTT02, KTPM01
-- Chi chen neu chua co du lieu (tranh loi khi chay lai)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM Classes WHERE class_code = 'CNTT01')
BEGIN
    INSERT INTO Classes (class_code, class_name) VALUES
        ('CNTT01', N'Cong nghe thong tin 01'),
        ('CNTT02', N'Cong nghe thong tin 02'),
        ('KTPM01', N'Ky thuat phan mem 01');
    PRINT 'Da chen du lieu mau vao Classes.';
END
GO

-- ============================================================
-- 5. CHEN DU LIEU MAU - SINH VIEN
-- 6 sinh vien, phan bo vao 3 lop:
--   CNTT01: SV001 (Nguyen Van A), SV002 (Tran Thi B), SV005 (Hoang Van E)
--   CNTT02: SV003 (Le Van C), SV006 (Dang Thi F)
--   KTPM01: SV004 (Pham Thi D)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM Students WHERE student_code = 'SV001')
BEGIN
    -- Lay ID cua cac lop tu bang Classes
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

-- ============================================================
-- 6. KIEM TRA KET QUA
-- Xem so luong ban ghi trong moi bang
-- ============================================================
SELECT 'Classes' AS [Bang], COUNT(*) AS [So ban ghi] FROM Classes
UNION ALL
SELECT 'Students', COUNT(*) FROM Students;
GO

PRINT '=== Khoi tao database hoan tat! ===';
GO