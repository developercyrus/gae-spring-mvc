package gae.spring.mvc.form.annotation.service;

import gae.spring.mvc.form.annotation.model.Student;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StudentManager {
    @PersistenceContext
    private EntityManager manager;
    
    @Transactional(readOnly = true)
    public Student save(Student student) {
        manager.persist(student);
        manager.flush();
        return student;
    }
    
    @Transactional(readOnly = true)
    public Student find(Long id) {
        Student student = manager.find(Student.class, id);
        return student;
    }
    
    @Transactional(readOnly = true)
    public List<Student> getAll() {
        Query query = manager.createQuery("select s from Student s", Student.class);
        return query.getResultList();
    }
}
