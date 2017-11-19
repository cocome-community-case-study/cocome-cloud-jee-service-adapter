package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
@Entity
public class PlantOperation extends RecipeOperation {
    private static final long serialVersionUID = 1L;

    private Plant plant;

    private List<Expression> expressions;

    /**
     * @return the plant that owns this production unit
     */
    @NotNull
    @ManyToOne
    public Plant getPlant() {
        return plant;
    }

    /**
     * @param plant the plant that owns this production unit
     */
    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    /**
     * @return the expressions executed within this operation
     */
    //Make sure the execution order of the specified operations is maintained by the DB
    @OrderColumn(name = "EXEC_ORDER")
    //Needed to allow multiple uses of the same operation in the same const expression
    @JoinTable(
            name = "PLANTOPERATION_EXPRESSION",
            joinColumns = {
                    @JoinColumn(name = "EXEC_ORDER"),
                    @JoinColumn(name = "PLANTOPERATION_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "EXPRESSION_ID"),
            })
    @OneToMany
    public List<Expression> getExpressions() {
        return expressions;
    }

    /**
     * @param expressions the expressions executed within this operation
     */
    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }
}
