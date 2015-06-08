package gae.spring.mvc.form.annotation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import gae.spring.mvc.form.annotation.form.StudentInputForm;
import gae.spring.mvc.form.annotation.form.StudentOutputForm;
import gae.spring.mvc.form.annotation.model.Phone;
import gae.spring.mvc.form.annotation.model.Student;
import gae.spring.mvc.form.annotation.model.StudentPhone;
import gae.spring.mvc.form.annotation.service.PhoneManager;
import gae.spring.mvc.form.annotation.service.StudentManager;
import gae.spring.mvc.form.annotation.service.StudentPhoneManager;
import gae.spring.mvc.form.annotation.service.TinyMCEManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FormAnnotationController {
    private static final Logger logger = Logger.getLogger(FormAnnotationController.class.getName());
    @Autowired
    PhoneManager phoneManager;
    @Autowired
    StudentManager studentManager;
    @Autowired
    StudentPhoneManager studentPhoneManager;
    @Autowired
    TinyMCEManager tinyMCEManager;
    
    
    
    @RequestMapping(value = "/input2.do", method = RequestMethod.GET)
    public ModelAndView anyMethodName_3() {
         ModelAndView mav = new ModelAndView();
         mav.setViewName("input2");
         mav.addObject("phone", new Phone());
         return mav;
    }
    
    @RequestMapping(value = "/output2.do", method = RequestMethod.POST)
    public ModelAndView anyMethodName_4(Phone phone) {
         ModelAndView mav = new ModelAndView();
         mav.setViewName("output2");
         mav.addObject("phone", phoneManager.save(phone));
         return mav;
    }
    
    //===============================================================================    
    
    
    @ModelAttribute("phones")
    public List<Phone> getPhones() {
        List<Phone> phones = phoneManager.getAll();
        return phones;
    }
    
    @RequestMapping(value = "/input3.do", method = RequestMethod.GET)
    public ModelAndView anyMethodName_5() {
         ModelAndView mav = new ModelAndView();
         mav.setViewName("input3");
         mav.addObject("studentInputForm", new StudentInputForm());
         return mav;
    }
    
    @RequestMapping(value = "/output3.do", method = RequestMethod.POST)
    public void anyMethodName_6(@ModelAttribute StudentInputForm studentInputForm, Model model) {
        Student student = new Student(studentInputForm.getStudentName());
        student = studentManager.save(student);
        Long studentId = student.getId();
        List<Long> phoneIds = studentInputForm.getPhoneIds();
        for (Long phoneId : phoneIds) {
            studentPhoneManager.save(new StudentPhone(studentId, phoneId));
        }
        model.addAttribute("studentName", studentInputForm.getStudentName());
        model.addAttribute("phoneId", studentInputForm.getPhoneId());
        model.addAttribute("phoneIds", studentInputForm.getPhoneIds());
        model.addAttribute(studentInputForm);
    }
    
    //===============================================================================    
    
    @ModelAttribute("students")
    public List<Student> getStudents() {
        List<Student> students = studentManager.getAll();
        return students;
    }
    
    @RequestMapping(value = "/input4.do", method = RequestMethod.GET)
    public ModelAndView anyMethodName_7() {
         ModelAndView mav = new ModelAndView();
         mav.setViewName("input4");
         mav.addObject("studentOutputForm", new StudentOutputForm());
         return mav;
    }
    
    @RequestMapping(value = "/output4.do", method = RequestMethod.POST)
    public void anyMethodName_8(@ModelAttribute StudentOutputForm studentOutputForm, Model model) {
        Long studentId = studentOutputForm.getStudentId();
        List<StudentPhone> sps = studentPhoneManager.getByStudentId(studentId);
       
        Student student = studentManager.find(studentId);
        List<Phone> phones = new ArrayList<Phone>();
        for (StudentPhone sp : sps) {
            phones.add(phoneManager.find(sp.getPhoneId()));
        }

        studentOutputForm.setStudent(student);
        studentOutputForm.setPhones(phones);
        
        model.addAttribute("student", studentOutputForm.getStudent());
        model.addAttribute("phones", studentOutputForm.getPhones());
        model.addAttribute(studentOutputForm);
    }
}

