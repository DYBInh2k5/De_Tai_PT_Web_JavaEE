# HƯỚNG DẪN LÀM SLIDE THUYẾT TRÌNH (.pptx)
## 8 slide — theo cấu trúc thông báo mới (7-9 slide, 10 phút)

---

## Slide 1 — Giới thiệu đề tài & Mục tiêu (30 giây)

**Gộp trang bìa + mục tiêu thành 1 slide duy nhất.**

- **Tên đề tài:** Tìm hiểu quan hệ One-to-Many và Many-to-One trong JPA/Hibernate qua ứng dụng quản lý lớp học
- **Bài toán demo:** Quản lý lớp học và sinh viên (CRUD, gán SV vào lớp, lọc, đếm)
- **Thành viên:** 1. Võ Duy Bình, 2. Nguyễn Vũ Minh Huy, 3. Trần Bá Lợi
- **GVHD:** Phan Hồng Trung — Học kỳ 2, Năm học 2024-2025
- **Mục tiêu chính:** Hiểu quan hệ One-to-Many/Many-to-One trong JPA/Hibernate, thành thạo annotation, xây dựng ứng dụng Spring Boot + SQL Server + Bootstrap

> ⚠️ **Không dành quá nhiều thời gian cho giới thiệu nhóm. Chỉ 30 giây.**

---

## Slide 2 — Công nghệ chính: JPA/Hibernate (1.5 phút)

**Trọng tâm: JPA/Hibernate giải quyết vấn đề gì? Khác gì cách làm thông thường?**

Nội dung cần có:
1. **Vấn đề:** Object-Relational Impedance Mismatch — SQL quan hệ vs Java hướng đối tượng
2. **JPA/Hibernate là gì:** ORM framework ánh xạ bảng → Java object
3. **Khác biệt:** Thay vì viết JDBC/SQL thủ công, lập trình viên làm việc qua entity + annotation
4. **Vị trí trong hệ thống:** Tầng Persistence — nằm giữa Service Layer và Database
5. **Công nghệ bổ trợ:** Spring Boot (Backend), Thymeleaf (View), Bootstrap (UI), Lombok (code gọn)

> ⚠️ **Không đọc định nghĩa chung chung. Gắn ngay với demo quản lý lớp học.**

---

## Slide 3 — Phân tích chức năng & Thiết kế hệ thống (1.5 phút)

### Chức năng chính (5 cái)
1. CRUD lớp học
2. CRUD sinh viên
3. Gán sinh viên vào lớp
4. Lọc sinh viên theo lớp
5. Xem số lượng SV từng lớp

### Kiến trúc MVC
> ⚠️ Dán ảnh sơ đồ MVC (đã vẽ ở báo cáo 3.4).

```
Trình duyệt → Controller → Service → Repository → SQL Server
                                    ↕
                              Entity (JPA)
                                    ↕
                              View (Thymeleaf)
```

### Thiết kế CSDL
> ⚠️ Dán ảnh ERD (đã vẽ ở báo cáo 3.3.1).

**Classes:** id (PK), class_code, class_name
**Students:** id (PK), student_code, full_name, email, class_id (FK → Classes.id, ON DELETE SET NULL)

---

## Slide 4 — Cài đặt kỹ thuật quan trọng (1.5 phút)

### 4.1. Luồng request MVC + Thymeleaf (bắt buộc)

Vẽ luồng cho chức năng **xem danh sách lớp học**:

```
1. Trình duyệt gõ http://localhost:8080/classes
2. ClassroomController.findAll()
3. Gọi ClassroomService.getAllClassrooms()
4. Gọi ClassroomRepository.findAll()
5. Repository trả List<Classroom>
6. Controller đưa vào Model: model.addAttribute("classrooms", list)
7. Trả về "class-list"
8. Thymeleaf đọc class-list.html, dùng th:each="c : ${classrooms}"
   và th:text="${c.className}" để hiển thị
9. Trình duyệt nhận HTML hoàn chỉnh
```

### 4.2. Entity + Annotation mapping (2-3 dòng code tiêu biểu)

```java
// Classroom.java — phía "Một"
@OneToMany(mappedBy = "classroom", cascade = {PERSIST, MERGE})
private List<Student> students;

// Student.java — phía "Nhiều"
@ManyToOne
@JoinColumn(name = "class_id")
private Classroom classroom;
```

**Giải thích nhanh:**
- Student là **owning side** (có `@JoinColumn` — quản lý FK)
- Classroom là **inverse side** (có `mappedBy` — chỉ phản chiếu)
- `cascade = {PERSIST, MERGE}`: lưu/merge Classroom thì Student tự động đồng bộ
- `fetch = LAZY`: tải danh sách SV khi cần (mặc định)

### 4.3. Cấu hình database

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ClassroomDB
spring.jpa.hibernate.ddl-auto=update
```

> ⚠️ **Không chiếu code dài. Chỉ 2-3 dòng tiêu biểu + giải thích vai trò.**

---

## Slide 5 — Demo chương trình (3 phút)

> 🚨 **Demo chạy live, không dùng ảnh chụp.**

Chạy tuần tự 5 chức năng trên `http://localhost:8080`:

| Thứ tự | Chức năng | URL | Xác nhận |
|--------|-----------|-----|----------|
| 1 | Danh sách lớp + badge số SV | `/classes` | Có cột Số SV |
| 2 | Thêm lớp mới | `/classes/add` | Lớp mới xuất hiện |
| 3 | Danh sách SV + tên lớp | `/students` | Tên lớp hiển thị |
| 4 | Thêm SV, chọn lớp từ dropdown | `/students/add` | SV thuộc đúng lớp |
| 5 | Lọc SV theo lớp | `/students?classId=1` | Chỉ SV của lớp đó |

**Chứng minh SQL Server:** Mở SSMS → `SELECT * FROM Students WHERE class_id = 1` → dữ liệu khớp.

---

## Slide 6 — Kết quả đạt được & Khó khăn (2 phút)

**Đã hoàn thành:**
- CRUD lớp học + sinh viên
- Gán SV vào lớp qua dropdown
- Lọc SV theo lớp
- Badge đếm số SV
- Kết nối SQL Server thực tế

**Khó khăn:**
- Cấu hình SQL Server Authentication (TCP/IP, mixed mode)
- Hiểu owning side và cách Hibernate cập nhật FK khi lưu

**Hạn chế (đã biết + hướng khắc phục):**
| Hạn chế | Hướng khắc phục |
|---------|-----------------|
| Chưa có validation nâng cao (chỉ HTML required) | Thêm `@Valid` + `BindingResult` |
| Chưa có phân trang | Thêm `Pageable` + `Page` |
| Chưa có tìm kiếm | Thêm `@Query` + form search |

---

## Slide 7 — Kết luận & Hướng phát triển

**Kết luận:**
- Hiểu và áp dụng được `@OneToMany`, `@ManyToOne`, `@JoinColumn`, `mappedBy`, `cascade`
- Xây dựng ứng dụng Spring Boot hoàn chỉnh kết nối SQL Server, giao diện Bootstrap

**Hướng phát triển:**
1. Spring Validation (`@Valid`) kiểm tra dữ liệu đầu vào
2. Phân trang với Spring Data Pageable
3. Tìm kiếm sinh viên theo tên
4. Spring Security đăng nhập / phân quyền

---

## Slide 8 — Cảm ơn & Câu hỏi

Cảm ơn quý thầy cô và các bạn đã lắng nghe!

Rất mong nhận được ý kiến đóng góp và câu hỏi.

---

## Tổng kết thời lượng

| Slide | Nội dung | Thời gian |
|-------|----------|-----------|
| 1 | Giới thiệu + Mục tiêu | 30s |
| 2 | Công nghệ chính (JPA/Hibernate) | 1.5p |
| 3 | Phân tích + Thiết kế | 1.5p |
| 4 | Cài đặt kỹ thuật (MVC + code) | 1.5p |
| 5 | Demo live | 3p |
| 6 | Kết quả + Khó khăn | 2p |
| 7 | Kết luận + Hướng PT | *(nằm trong 2p)* |
| 8 | Cảm ơn & Câu hỏi | — |
| **Tổng** | | **10 phút** |
