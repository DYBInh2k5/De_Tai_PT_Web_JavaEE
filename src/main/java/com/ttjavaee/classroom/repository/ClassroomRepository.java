package com.ttjavaee.classroom.repository;

import com.ttjavaee.classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ============================================================
// Repository: LOP TRUNG GIAN giua Service va CSDL
//
// JpaRepository<Classroom, Long>:
//   - Classroom: kieu Entity quan ly
//   - Long: kieu cua khoa chinh (id)
//
// KHONG can viet bat ky phuong thuc nao!
// Spring Data JPA tu dong cung cap:
//   - findAll()      -> SELECT * FROM Classes
//   - findById(id)   -> SELECT * FROM Classes WHERE id = ?
//   - save(entity)   -> INSERT neu id=null, UPDATE neu id da ton tai
//   - deleteById(id) -> DELETE FROM Classes WHERE id = ?
//   - count()        -> SELECT COUNT(*) FROM Classes
// ============================================================
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
