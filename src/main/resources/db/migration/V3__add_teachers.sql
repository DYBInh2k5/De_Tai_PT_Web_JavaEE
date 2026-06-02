IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Teachers]') AND type = N'U')
BEGIN
    CREATE TABLE Teachers (
        id            BIGINT IDENTITY(1,1) PRIMARY KEY,
        teacher_code  NVARCHAR(50)  NOT NULL UNIQUE,
        full_name     NVARCHAR(255) NOT NULL,
        email         NVARCHAR(255) NOT NULL
    );
END;

IF NOT EXISTS (SELECT 1 FROM Teachers WHERE teacher_code = 'GV001')
BEGIN
    INSERT INTO Teachers (teacher_code, full_name, email) VALUES
        ('GV001', N'Nguyễn Văn Hùng', 'hung.nguyen@school.edu'),
        ('GV002', N'Trần Thị Lan',   'lan.tran@school.edu');
END;
