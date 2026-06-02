# HƯỚNG DẪN NỘP BÀI (CHECKLIST)

Để đảm bảo điểm cao, bạn cần nộp đầy đủ các thành phần sau theo yêu cầu của đề tài:

---

## 1. Danh sách file cần nộp
1.  **Báo cáo:** `Bao_Cao_Nhom_XX.docx` và `Bao_Cao_Nhom_XX.pdf`.
2.  **Slide:** `Thuyet_Trinh_Nhom_XX.pptx`.
3.  **Mã nguồn:** Thư mục project (nén thành file `.zip` hoặc `.rar`).
4.  **Database:** File `init-db.sql`.
5.  **Hướng dẫn:** File `README.md`, `docs/KE_HOACH_PHAT_TRIEN.md` (kế hoạch & tiến độ).

## 2. Lưu ý trước khi nén bài
- **Xóa thư mục `target/`**: Trước khi nén mã nguồn, hãy xóa thư mục `target` để file nén nhẹ hơn.
- **Giữ `mvnw`, `mvnw.cmd`, `.mvn/`**: Người chấm có thể chạy không cần cài Maven.
- **Không nén mật khẩu thật**: Dùng `application-dev.properties` mặc định hoặc `application.properties.example`; hướng dẫn set `DB_PASSWORD` trong README.
- **Kiểm tra SQL**: Chạy thử `init-db.sql` trên máy sạch.
- **Ghi rõ JDK 17** trong README và báo cáo mục cài đặt.

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
