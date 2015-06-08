package gae.spring.mvc.form.annotation.form;

import java.util.List;

import gae.spring.mvc.form.annotation.model.Phone;
import gae.spring.mvc.form.annotation.model.Student;

public class StudentOutputForm {
    private Long studentId;
    private Student student;
    private List<Phone> phones;

    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    public List<Phone> getPhones() {
        return phones;
    }
    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}