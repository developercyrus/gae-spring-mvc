package gae.spring.mvc.form.annotation.service;

import gae.spring.mvc.form.annotation.model.Phone;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PhoneManager {
    @PersistenceContext
    private EntityManager manager;
    
    @Transactional(readOnly = true)
    public Phone save(Phone phone) {
        manager.persist(phone);
        manager.flush();
        return phone;
    }
    
    @Transactional(readOnly = true)
    public Phone find(Long id) {
        Phone phone = manager.find(Phone.class, id);
        return phone;
    }
        
    @Transactional(readOnly = true)
    public List<Phone> getAll() {
        Query query = manager.createQuery("select p from Phone p", Phone.class);
        return query.getResultList();
    }
}
