package gae.math.lp.service;

import gae.math.lp.form.Constraint;
import gae.math.lp.form.Function;
import gae.math.lp.form.LinearProgramming;
import gae.polyu.library.controller.PolyULibraryController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.springframework.stereotype.Service;

@Service
public class SimplexSolverManager {
    private static final Logger logger = Logger.getLogger(SimplexSolverManager.class.getName());
    private Function function;
    private Constraint contraint;
    
    public double[] solve(LinearProgramming lp) {
        int varCount = Integer.parseInt(lp.getVarcount());
        double[] solutions = new double[varCount];
        
        Function function = lp.getFunction();
        List<Constraint> constraints = lp.getConstraints();

        LinearObjectiveFunction f = new LinearObjectiveFunction(this.parseToDouble(function.getVariables()), Double.parseDouble(function.getConstant()));
        Collection<LinearConstraint> c = new ArrayList<LinearConstraint>();
        
        for (Constraint constraint : constraints) {
            c.add(new LinearConstraint(this.parseToDouble(constraint.getVariables()), this.parseToRelationship(constraint.getRelationship()), Double.parseDouble(constraint.getConstant())));
            varCount = constraint.getVariables().length;
        }
            
        RealPointValuePair solution = null;
        try {
            solution = new SimplexSolver().optimize(f, c, this.parseToGoalType(function.getGoal()), false);
            for (int i=0; i<varCount; i++) {
                solutions[i] = solution.getPoint()[i];
                logger.info("solutions[" + i + "]=" + solutions[i]);
            }
        } catch (OptimizationException e) {
            e.printStackTrace();
        }

        /*
        double min = solution.getValue();
        */
        return solutions;
    }
    
    private double[] parseToDouble(String[] variables) {
        double[] returned = new double[variables.length];
        for(int i = 0; i < variables.length; i++){
            returned[i] = Double.parseDouble(variables[i]);
        }
        return returned;
    }
    
    private Relationship parseToRelationship(String s) {
        if (s.equals("EQ")) {
            return Relationship.EQ;
        }
        else if (s.equals("LEQ")) {
            return Relationship.LEQ;
        }
        else if (s.equals("GEQ")) {
            return Relationship.GEQ;
        }
        return null;
    }
    
    private GoalType parseToGoalType(String s) {
        if (s.equals("MINIMIZE")) {
            return GoalType.MINIMIZE;
        }
        else if (s.equals("MAXIMIZE")) {
            return GoalType.MAXIMIZE;
        }
        return null;
    }
}
