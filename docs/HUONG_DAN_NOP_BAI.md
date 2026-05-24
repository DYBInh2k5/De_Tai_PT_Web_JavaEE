# HƯỚNG DẪN NỘP BÀI (CHECKLIST)

Để đảm bảo điểm cao, bạn cần nộp đầy đủ các thành phần sau theo yêu cầu của đề tài:

---

## 1. Danh sách file cần nộp
1.  **Báo cáo:** `Bao_Cao_Nhom_XX.docx` và `Bao_Cao_Nhom_XX.pdf`.
2.  **Slide:** `Thuyet_Trinh_Nhom_XX.pptx`.
3.  **Mã nguồn:** Thư mục project (nén thành file `.zip` hoặc `.rar`).
4.  **Database:** File `init-db.sql`.
5.  **Hướng dẫn:** File `README.md` (đã có sẵn trong project).

## 2. Lưu ý trước khi nén bài
- **Xóa thư mục `target/`**: Trước khi nén mã nguồn, hãy xóa thư mục `target` để file nén nhẹ hơn (IntelliJ sẽ tự tạo lại khi chạy).
- **Kiểm tra SQL**: Chạy thử file `.sql` trên một máy khác để đảm bảo không lỗi.
- **Cấu hình DB**: Đảm bảo trong báo cáo có nhắc người chấm bài thay đổi password SQL Server trong `application.properties`.

## 3. Cấu trúc thư mục nộp bài đề nghị
```text
Nhom_XX_De_Tai_07/
├── 01_Bao_Cao/
│   ├── Bao_Cao.docx
│   └── Bao_Cao.pdf
├── 02_Slide/
│   └── Thuyet_Trinh.pptx
├── 03_Source_Code/
│   ├── (Toàn bộ thư mục dự án ttjavaee)
│   └── init-db.sql
└── README.txt (Hướng dẫn nhanh)
```

## 4. Cách thức nộp
- Nén cả thư mục lớn `Nhom_XX_De_Tai_07` thành file `.zip`.
- Đặt tên file theo đúng quy định của giảng viên.
