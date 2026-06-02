-- Flyway V2: dữ liệu mẫu (chỉ chèn nếu chưa có)
IF NOT EXISTS (SELECT 1 FROM Classes WHERE class_code = 'CNTT01')
BEGIN
    INSERT INTO Classes (class_code, class_name) VALUES
        ('CNTT01', N'Công nghệ thông tin 01'),
        ('CNTT02', N'Công nghệ thông tin 02'),
        ('KTPM01', N'Kỹ thuật phần mềm 01');
END;

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
END;
