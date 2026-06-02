IF NOT EXISTS (
    SELECT 1 FROM sys.columns
    WHERE object_id = OBJECT_ID(N'[dbo].[Classes]') AND name = 'teacher_id'
)
BEGIN
    ALTER TABLE Classes ADD teacher_id BIGINT NULL;
    ALTER TABLE Classes ADD CONSTRAINT FK_Class_Teacher
        FOREIGN KEY (teacher_id) REFERENCES Teachers(id)
        ON DELETE SET NULL ON UPDATE CASCADE;
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Grades]') AND type = N'U')
BEGIN
    CREATE TABLE Grades (
        id         BIGINT IDENTITY(1,1) PRIMARY KEY,
        student_id BIGINT NOT NULL,
        subject_id BIGINT NOT NULL,
        score      DECIMAL(4, 2) NOT NULL,
        exam_date  DATE NULL,
        CONSTRAINT FK_Grade_Student FOREIGN KEY (student_id) REFERENCES Students(id) ON DELETE CASCADE,
        CONSTRAINT FK_Grade_Subject FOREIGN KEY (subject_id) REFERENCES Subjects(id) ON DELETE CASCADE,
        CONSTRAINT UQ_Grade_Student_Subject UNIQUE (student_id, subject_id)
    );
END;

-- Gán giảng viên mẫu cho lớp
IF EXISTS (SELECT 1 FROM Teachers WHERE teacher_code = 'GV001')
BEGIN
    DECLARE @gv1 BIGINT = (SELECT id FROM Teachers WHERE teacher_code = 'GV001');
    DECLARE @gv2 BIGINT = (SELECT id FROM Teachers WHERE teacher_code = 'GV002');
    UPDATE Classes SET teacher_id = @gv1 WHERE class_code IN ('CNTT01', 'CNTT02') AND teacher_id IS NULL;
    UPDATE Classes SET teacher_id = @gv2 WHERE class_code = 'KTPM01' AND teacher_id IS NULL;
END;

-- Điểm mẫu
IF NOT EXISTS (SELECT 1 FROM Grades)
BEGIN
    DECLARE @subJava BIGINT = (SELECT id FROM Subjects WHERE subject_code = 'JAVA01');
    DECLARE @subWeb  BIGINT = (SELECT id FROM Subjects WHERE subject_code = 'WEB01');
    DECLARE @sv1 BIGINT = (SELECT id FROM Students WHERE student_code = 'SV001');
    DECLARE @sv2 BIGINT = (SELECT id FROM Students WHERE student_code = 'SV002');

    IF @subJava IS NOT NULL AND @sv1 IS NOT NULL
        INSERT INTO Grades (student_id, subject_id, score, exam_date) VALUES (@sv1, @subJava, 8.50, '2025-12-15');
    IF @subWeb IS NOT NULL AND @sv1 IS NOT NULL
        INSERT INTO Grades (student_id, subject_id, score, exam_date) VALUES (@sv1, @subWeb, 7.75, '2025-12-20');
    IF @subJava IS NOT NULL AND @sv2 IS NOT NULL
        INSERT INTO Grades (student_id, subject_id, score, exam_date) VALUES (@sv2, @subJava, 9.00, '2025-12-15');
END;
