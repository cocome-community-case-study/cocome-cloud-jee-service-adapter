package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.plant.Plant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents a class of production unity utilizing a specific set of {@link ProductionUnitOperation}
 *
 * @author Rudolf Biczok
 */
@Entity
public class ProductionUnitClass implements Serializable, QueryableById {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private Plant plant;

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
     * @param id Identifier value.
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
     * @param name The name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The plant this unit class belongs to
     */
    @ManyToOne
    @NotNull
    public Plant getPlant() {
        return this.plant;
    }

    /**
     * @param enterprise The plant this unit class belongs
     */
    public void setPlant(final Plant enterprise) {
        this.plant = enterprise;
    }
}
