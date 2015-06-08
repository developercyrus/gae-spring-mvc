package gae.math.lp.controller;

import gae.math.lp.form.LinearProgramming;
import gae.math.lp.service.SimplexSolverManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {
    @Autowired
    SimplexSolverManager simplexSolverManager;
    
    @RequestMapping(value = "/lpin", method = RequestMethod.GET)
    public ModelAndView anyMethodName_1() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gae/math/lp/lpin");
        //mav.setViewName("lpin");
        return mav;
    }
    
    
    @RequestMapping(value = "/lpout", method = RequestMethod.POST)
    @ResponseBody
    public LinearProgramming anyMethodName_2(@RequestBody LinearProgramming lp) {
        lp.setSolutions(simplexSolverManager.solve(lp));
        return lp;
    }
    
    /*
    @RequestMapping(value = "/lpout", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView anyMethodName_2(@RequestBody LinearProgramming lp) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gae/math/lp/lpout");
        lp.setSolutions(simplexSolverManager.solve(lp));
        mav.addObject("lp", lp);
        return mav;
    }
    */
}
