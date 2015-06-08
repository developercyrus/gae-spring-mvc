package gae.spring.mvc.form.annotation.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StudentPhone implements java.io.Serializable {
    private static final long serialVersionUID = -633313359733627328L;
    private Long id;
    private Long studentId;
    private Long phoneId;

    public StudentPhone() {
    }

    public StudentPhone(Long studentId, Long phoneId) {
        this.studentId = studentId;
        this.phoneId = phoneId;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Basic
    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    @Basic
    public Long getPhoneId() {
        return phoneId;
    }
    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }    
}
