-- 1. Tạo Database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ClassroomDB')
BEGIN
    CREATE DATABASE ClassroomDB;
END
GO

USE ClassroomDB;
GO

-- 2. Tạo bảng Classes
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Classes]') AND type in (N'U'))
BEGIN
    CREATE TABLE Classes (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        class_code NVARCHAR(50) NOT NULL UNIQUE,
        class_name NVARCHAR(255) NOT NULL
    );
END
GO

-- 3. Tạo bảng Students
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Students]') AND type in (N'U'))
BEGIN
    CREATE TABLE Students (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        student_code NVARCHAR(50) NOT NULL UNIQUE,
        full_name NVARCHAR(255) NOT NULL,
        email NVARCHAR(255) NOT NULL,
        class_id BIGINT,
        CONSTRAINT FK_Student_Class FOREIGN KEY (class_id) REFERENCES Classes(id) ON DELETE SET NULL
    );
END
GO

-- 4. Chèn dữ liệu mẫu
-- Chèn Lớp học
INSERT INTO Classes (class_code, class_name) VALUES ('CNTT01', 'Công nghệ thông tin 01');
INSERT INTO Classes (class_code, class_name) VALUES ('CNTT02', 'Công nghệ thông tin 02');
INSERT INTO Classes (class_code, class_name) VALUES ('KTPM01', 'Kỹ thuật phần mềm 01');

-- Chèn Sinh viên (Giả sử ID của các lớp lần lượt là 1, 2, 3)
INSERT INTO Students (student_code, full_name, email, class_id) VALUES ('SV001', 'Nguyễn Văn A', 'a.nguyen@example.com', 1);
INSERT INTO Students (student_code, full_name, email, class_id) VALUES ('SV002', 'Trần Thị B', 'b.tran@example.com', 1);
INSERT INTO Students (student_code, full_name, email, class_id) VALUES ('SV003', 'Lê Văn C', 'c.le@example.com', 2);
INSERT INTO Students (student_code, full_name, email, class_id) VALUES ('SV004', 'Phạm Thị D', 'd.pham@example.com', 3);
GO
