-- Flyway V1: schema (chạy trên database ClassroomDB)
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Classes]') AND type = N'U')
BEGIN
    CREATE TABLE Classes (
        id         BIGINT IDENTITY(1,1) PRIMARY KEY,
        class_code NVARCHAR(50)  NOT NULL UNIQUE,
        class_name NVARCHAR(255) NOT NULL
    );
END;

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
END;
