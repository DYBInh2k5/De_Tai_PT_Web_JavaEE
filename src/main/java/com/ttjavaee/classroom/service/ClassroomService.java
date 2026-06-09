package com.ttjavaee.classroom.service;

import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// ============================================================
// Service: TANG NGHIEP VU (Business Logic Layer)
// Day la lop trung gian giua Controller (giao dien) va Repository (CSDL)
//
// @Service: danh dau day la Bean Service de Spring quan ly
// ============================================================
@Service
public class ClassroomService {

    // ============================================================
    // @Autowired: Spring tu dong "tien" (inject) Repository vao day
    // Khong can new, Spring tu tao va quan ly doi tuong
    // ============================================================
    @Autowired
    private ClassroomRepository classroomRepository;

    // Lay danh sach TAT CA cac lop hoc
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    // Tim 1 lop hoc theo ID (tra ve null neu khong tim thay)
    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id).orElse(null);
    }

    // Luu lop hoc (THEM moi hoac CAP NHAT)
    // - Neu classroom.id == null: them moi (INSERT)
    // - Neu classroom.id != null: cap nhat (UPDATE)
    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    // Xoa lop hoc theo ID
    // Khi xoa: DB co rang buoc ON DELETE SET NULL
    // -> sinh vien trong lop se bi go lop (class_id = NULL), khong bi xoa
    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }
}
