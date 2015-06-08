package gae.security.http.ettercap.controller;

import gae.security.http.ettercap.model.User;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Component
@Controller
public class HttpEttercapController {
    private static final Logger logger = Logger.getLogger(HttpEttercapController.class.getName());
    
    @RequestMapping(value = "/ettercapindex.do", method = RequestMethod.GET)
    public ModelAndView anyMethodName_1()  {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gae/security/http/ettercap/ettercapindex");
        mav.addObject("user", new User());
        return mav;
    }
    
    @RequestMapping(value = "/ettercaplogin.do", method = RequestMethod.POST)
    public ModelAndView anyMethodName_2(User user) {
         ModelAndView mav = new ModelAndView();
         if (user.getUsername().equals("test") && user.getPassword().equals("test")) {
             mav.setViewName("gae/security/http/ettercap/ettercaploginsuccess");
         }
         else {
             mav.setViewName("gae/security/http/ettercap/ettercaploginfail");
         }       
         return mav;
    }
}
