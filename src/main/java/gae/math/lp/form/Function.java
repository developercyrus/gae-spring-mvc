package gae.math.lp.form;

public class Function {
    private String goal;
    private String constant;
    private String[] variables;
    
    public String getGoal() {
        return goal;
    }
    public void setGoal(String goal) {
        this.goal = goal;
    }
    public String getConstant() {
        return constant;
    }
    public void setConstant(String constant) {
        this.constant = constant;
    }
    public String[] getVariables() {
        return variables;
    }
    public void setVariables(String[] variables) {
        this.variables = variables;
    }
}
