package org.cocome.tradingsystem.inventory.data.plant.productionunit.expression;


import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Represents an conditional expression.
 * @author Rudolf Biczok
 */
@Entity
public class IfElseExpression extends Expression {
    private static final long serialVersionUID = 1L;

    private ProductionParameter<PlantOperation> parameter;
    private String parameterValue;
    private Expression onTrueExpression;
    private Expression onFalseExpression;

    /**
     * @return the parameter to be tested
     */
    @OneToOne(cascade = CascadeType.ALL)
    public ProductionParameter<PlantOperation> getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to be tested
     */
    public void setParameter(ProductionParameter<PlantOperation> parameter) {
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
    @OneToOne(cascade = CascadeType.ALL)
    public Expression getOnTrueExpression() {
        return onTrueExpression;
    }

    /**
     * @param onTrueExpression the expression that is supposed to be executed if the condition holds
     */
    public void setOnTrueExpression(Expression onTrueExpression) {
        this.onTrueExpression = onTrueExpression;
    }

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    @OneToOne(cascade = CascadeType.ALL)
    public Expression getOnFalseExpression() {
        return onFalseExpression;
    }

    /**
     * @param onFalseExpression the expression that is supposed to be executed if the condition
     *                          does not hold
     */
    public void setOnFalseExpression(Expression onFalseExpression) {
        this.onFalseExpression = onFalseExpression;
    }
}
