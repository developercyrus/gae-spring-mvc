package gae.spring.mvc.form.annotation.service;

import gae.spring.mvc.form.annotation.model.TinyMCE;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TinyMCEManager {
    @PersistenceContext
    private EntityManager manager;
    
    @Transactional(readOnly = true)
    public TinyMCE save(TinyMCE mce) {
        manager.persist(mce);
        manager.flush();
        return mce;
    }
}
