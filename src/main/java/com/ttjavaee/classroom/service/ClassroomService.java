package com.ttjavaee.classroom.service;

import com.ttjavaee.classroom.entity.Classroom;
import com.ttjavaee.classroom.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id).orElse(null);
    }

    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }
}
