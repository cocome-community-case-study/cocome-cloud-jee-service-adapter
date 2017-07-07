package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a class of production unity utilizing a specific set of {@link ProductionUnitOperation}
 * @author Rudolf Biczok
 */
@Entity
public class ProductionUnitClass implements Serializable, QueryableById {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private Collection<ProductionUnitOperation> operations;

    /**
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            Identifier value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The name of the product
     */
    @Basic
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return all available operations of this production unit type
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<ProductionUnitOperation> getOperations() {
        return operations;
    }

    /**
     * @param operations all available operations of this production unit type
     */
    public void setOperations(Collection<ProductionUnitOperation> operations) {
        this.operations = operations;
    }
}
