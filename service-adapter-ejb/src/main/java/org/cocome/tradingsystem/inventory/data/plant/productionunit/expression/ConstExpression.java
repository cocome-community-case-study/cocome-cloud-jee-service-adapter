package org.cocome.tradingsystem.inventory.data.plant.productionunit.expression;

import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * Plant-local expression that represents a constant list of operations
 * @author Rudolf Bicozok
 */
@Entity
public class ConstExpression extends Expression {
    private static final long serialVersionUID = 1L;

    private Collection<ProductionUnitOperation> operations;

    /**
     * @return the list of operations that are supposed to be executed
     */
    @OneToMany(cascade = CascadeType.ALL)
    public Collection<ProductionUnitOperation> getOperations() {
        return operations;
    }

    /**
     * @param operations the list of operations that are supposed to be executed
     */
    public void setOperations(Collection<ProductionUnitOperation> operations) {
        this.operations = operations;
    }
}
