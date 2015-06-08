package gae.spring.mvc.form.annotation.controller;

import java.util.logging.Logger;

import gae.spring.mvc.form.annotation.model.TinyMCE;
import gae.spring.mvc.form.annotation.service.TinyMCEManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TinyMCEController {
    private static final Logger logger = Logger.getLogger(TinyMCEController.class.getName());
    @Autowired
    TinyMCEManager tinyMCEManager;
    

    @RequestMapping(value = "/tinyMCEinput", method = RequestMethod.GET)
    public ModelAndView anyMethodName_9() {
         ModelAndView mav = new ModelAndView();
         mav.setViewName("tinyMCEinput");
         mav.addObject("tinyMCE", new TinyMCE());
         return mav;
    }
    
    @RequestMapping(value = "/tinyMCEoutput", method = RequestMethod.POST)
    public ModelAndView anyMethodName_10(TinyMCE tinyMCE) {
         ModelAndView mav = new ModelAndView();
         mav.setViewName("tinyMCEoutput");
         mav.addObject("tinyMCE", tinyMCEManager.save(tinyMCE));
         return mav;
    }
}