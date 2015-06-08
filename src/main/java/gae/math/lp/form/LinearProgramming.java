package gae.math.lp.form;

import java.util.List;

public class LinearProgramming {
    private String varcount;
    private Function function;
    private List<Constraint> constraints;  
    private double[] solutions;
    
    public String getVarcount() {
        return varcount;
    }
    public void setVarcount(String varcount) {
        this.varcount = varcount;
    }
    public Function getFunction() {
        return function;
    }
    public void setFunction(Function function) {
        this.function = function;
    }
    public List<Constraint> getConstraints() {
        return constraints;
    }
    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }
    public double[] getSolutions() {
        return solutions;
    }
    public void setSolutions(double[] solutions) {
        this.solutions = solutions;
    }
}
