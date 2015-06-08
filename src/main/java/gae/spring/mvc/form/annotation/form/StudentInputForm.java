package gae.spring.mvc.form.annotation.form;

import java.util.List;

public class StudentInputForm {
    private String studentName;
    private Long phoneId;
    private List<Long> phoneIds;

    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public Long getPhoneId() {
        return phoneId;
    }
    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }
    public List<Long> getPhoneIds() {
        return phoneIds;
    }
    public void setPhoneIds(List<Long> phoneIds) {
        this.phoneIds = phoneIds;
    }
}