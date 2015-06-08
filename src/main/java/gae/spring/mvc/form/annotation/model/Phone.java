package gae.spring.mvc.form.annotation.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Phone implements java.io.Serializable {
    private static final long serialVersionUID = 6971178241135584112L;
    private Long id;
    private Integer phoneNo;

    public Phone() {
    }

    public Phone(Integer phoneNo) {
        this.phoneNo = phoneNo;
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
    public Integer getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(Integer phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String toString() {
        return "[id=" + id + ", phoneNo=" + phoneNo + "]";
    }
}
