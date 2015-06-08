package gae.spring.mvc.form.annotation.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Id;

@Entity
public class Student implements java.io.Serializable {
    private static final long serialVersionUID = -7399807703803718622L;
    private Long id;
    private String name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
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
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    @Override
    public String toString() {
        return "[id=" + id + ", name=" + name + "]";
    }
}
