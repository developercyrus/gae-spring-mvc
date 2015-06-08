package gae.spring.mvc.form.annotation.service;

import gae.spring.mvc.form.annotation.model.StudentPhone;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StudentPhoneManager {
    @PersistenceContext
    private EntityManager manager;
    
    @Transactional(readOnly = true)
    public List<StudentPhone> getByStudentId(Long studentId) {
        Query query = manager.createQuery("select sp from StudentPhone sp where studentId = " + studentId, StudentPhone.class);
        return query.getResultList();
    }
    
    @Transactional(readOnly = true)
    public void save(StudentPhone sp) {
        manager.persist(sp);
        manager.flush();
    }
    
    
}
