IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Subjects]') AND type = N'U')
BEGIN
    CREATE TABLE Subjects (
        id           BIGINT IDENTITY(1,1) PRIMARY KEY,
        subject_code NVARCHAR(50)  NOT NULL UNIQUE,
        subject_name NVARCHAR(255) NOT NULL,
        credits      INT NOT NULL DEFAULT 3
    );
END;

IF NOT EXISTS (SELECT 1 FROM Subjects WHERE subject_code = 'JAVA01')
BEGIN
    INSERT INTO Subjects (subject_code, subject_name, credits) VALUES
        ('JAVA01', N'Lập trình Java', 3),
        ('WEB01',  N'Phát triển Web', 3),
        ('DB01',   N'Cơ sở dữ liệu', 3);
END;
