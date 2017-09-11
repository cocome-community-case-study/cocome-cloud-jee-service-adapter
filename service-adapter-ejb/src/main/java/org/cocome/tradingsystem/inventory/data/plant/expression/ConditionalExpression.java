package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a conditional expression.
 * @author Rudolf Biczok
 */
@Entity
public class ConditionalExpression extends Expression {
    private static final long serialVersionUID = 1L;

    private PlantOperationParameter parameter;
    private String parameterValue;
    private List<Expression> onTrueExpressions;
    private List<Expression> onFalseExpressions;

    /**
     * @return the parameter to be tested
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    public PlantOperationParameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to be tested
     */
    public void setParameter(PlantOperationParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the expected parameter value
     */
    public String getParameterValue() {
        return parameterValue;
    }

    /**
     * @param parameterValue the expected parameter value
     */
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    //Make sure the execution order of the specified operations is maintained by the DB
    @OrderColumn(name = "EXEC_ORDER")
    //Needed to allow multiple uses of the same operation in the same const expression
    @JoinTable(
            name = "EXPRESSION_ONTRUEEXPRESSION",
            joinColumns = {
                    @JoinColumn(name = "EXEC_ORDER"),
                    @JoinColumn(name = "ONTRUEEXPRESSION_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "CONDITIONALEXPRESSION_ID"),
            })
    @OneToMany(cascade = CascadeType.ALL)
    public List<Expression> getOnTrueExpressions() {
        return onTrueExpressions;
    }

    /**
     * @param onTrueExpressions the expression that is supposed to be executed if the condition holds
     */
    public void setOnTrueExpressions(List<Expression> onTrueExpressions) {
        this.onTrueExpressions = onTrueExpressions;
    }

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    //Make sure the execution order of the specified operations is maintained by the DB
    @OrderColumn(name = "EXEC_ORDER")
    //Needed to allow multiple uses of the same operation in the same const expression
    @JoinTable(
            name = "EXPRESSION_ONFALSEEXPRESSION",
            joinColumns = {
                    @JoinColumn(name = "EXEC_ORDER"),
                    @JoinColumn(name = "ONFALSEEXPRESSION_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "CONDITIONALEXPRESSION_ID"),
            })
    @OneToMany(cascade = CascadeType.ALL)
    public List<Expression> getOnFalseExpressions() {
        return onFalseExpressions;
    }

    /**
     * @param onFalseExpressions the expression that is supposed to be executed if the condition
     *                          does not hold
     */
    public void setOnFalseExpressions(List<Expression> onFalseExpressions) {
        this.onFalseExpressions = onFalseExpressions;
    }
}
