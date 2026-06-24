# HƯỚNG DẪN NỘP BÀI (CHECKLIST)

---

## Danh sách file cần nộp

| STT | File | Trạng thái |
|---|---|---|
| 1 | `Bao_Cao_Nhom_XX.docx` | Tự làm từ `docs/BAO_CAO_CHI_TIET.md` |
| 2 | `Bao_Cao_Nhom_XX.pdf` | Export từ Word |
| 3 | `Thuyet_Trinh_Nhom_XX.pptx` | Tự làm theo `docs/HUONG_DAN_SLIDE.md` + script mẫu `docs/VAN_BAN_THUYET_TRINH.md` |
| 4 | Source code (folder zip) | Toàn bộ project |
| 5 | `init-db.sql` | Có sẵn trong project root |
| 6 | `README.md` | Có sẵn trong project root |

---

## Cấu trúc thư mục nộp bài

```
Nhom_XX_De_Tai_07/
├── 01_Bao_Cao/
│   ├── Bao_Cao.docx
│   └── Bao_Cao.pdf
├── 02_Slide/
│   └── Thuyet_Trinh.pptx
├── 03_Source_Code/
│   └── De_Tai_PT_Web_JavaEE/   ← toàn bộ project (đã xóa folder target/)
└── README.md
```

---

## Lưu ý trước khi nén

- **Xóa folder `target/`** trong project trước khi nén (giảm dung lượng file zip)
  - Trong IntelliJ: Build → Clean Project
  - Hoặc xóa thủ công folder `De_Tai_PT_Web_JavaEE/target/`
- Kiểm tra file `init-db.sql` chạy được trên SSMS
- Đảm bảo báo cáo Word có **ảnh chụp màn hình** các chức năng

---

## Nén file

Nén folder `Nhom_XX_De_Tai_07` thành file `.zip`, đặt tên theo quy định của giảng viên.