package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;

import javax.persistence.*;
import java.util.Collection;

/**
 * Represents a conditional expression.
 * @author Rudolf Biczok
 */
@Entity
public class ConditionalExpression extends Expression {
    private static final long serialVersionUID = 1L;

    private PlantOperationParameter parameter;
    private String parameterValue;
    private Collection<Expression> onTrueExpressions;
    private Collection<Expression> onFalseExpressions;

    /**
     * @return the parameter to be tested
     */
    @OneToOne
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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<Expression> getOnTrueExpressions() {
        return onTrueExpressions;
    }

    /**
     * @param onTrueExpressions the expression that is supposed to be executed if the condition holds
     */
    public void setOnTrueExpressions(Collection<Expression> onTrueExpressions) {
        this.onTrueExpressions = onTrueExpressions;
    }

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<Expression> getOnFalseExpressions() {
        return onFalseExpressions;
    }

    /**
     * @param onFalseExpressions the expression that is supposed to be executed if the condition
     *                          does not hold
     */
    public void setOnFalseExpressions(Collection<Expression> onFalseExpressions) {
        this.onFalseExpressions = onFalseExpressions;
    }
}
