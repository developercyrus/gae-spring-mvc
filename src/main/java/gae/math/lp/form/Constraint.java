package gae.math.lp.form;

public class Constraint {
    private String[] variables;
    private String relationship;
    private String constant;
    
    public String[] getVariables() {
        return variables;
    }
    public void setVariables(String[] variables) {
        this.variables = variables;
    }
    public String getRelationship() {
        return relationship;
    }
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
    public String getConstant() {
        return constant;
    }
    public void setConstant(String constant) {
        this.constant = constant;
    }
}
