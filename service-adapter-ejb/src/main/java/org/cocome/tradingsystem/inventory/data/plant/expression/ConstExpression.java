package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;

import javax.persistence.*;
import java.util.List;

/**
 * Plant-local expression that represents a constant list of operations
 *
 * @author Rudolf Bicozok
 */
@Entity
public class ConstExpression extends Expression {
    private static final long serialVersionUID = 1L;

    private List<ProductionUnitOperation> operations;

    /**
     * @return the list of operations that are supposed to be executed
     */
    //Make sure the execution order of the specified operations is maintained by the DB
    @OrderColumn(name = "EXEC_ORDER")
    //Needed to allow multiple uses of the same operation in the same const expression
    @JoinTable(
            name = "EXPRESSION_PRODUCTIONUNITOPERATION",
            joinColumns = {
                    @JoinColumn(name = "EXEC_ORDER"),
                    @JoinColumn(name = "CONSTEXPRESSION_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "OPERATIONS_ID"),
            })
    @ManyToMany(cascade = CascadeType.ALL)
    public List<ProductionUnitOperation> getOperations() {
        return operations;
    }

    /**
     * @param operations the list of operations that are supposed to be executed
     */
    public void setOperations(List<ProductionUnitOperation> operations) {
        this.operations = operations;
    }
}
