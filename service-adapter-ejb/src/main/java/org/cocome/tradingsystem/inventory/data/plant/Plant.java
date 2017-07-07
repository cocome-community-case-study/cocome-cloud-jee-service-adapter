package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.RecipeStep;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * This class represents a plant that provides access to a collection of {@link RecipeStep}.
 *
 * @author Rudolf Biczok
 */
@Entity
public class Plant implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String location;
    private TradingEnterprise enterprise;
    private Collection<PlantOperation> operations;
    private Collection<ProductionUnit> productionUnits;

    /**
     * @return A unique identifier of this Plant.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id
     *            a unique identifier of this Plant
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Returns the name of the Plant.
     *
     * @return Plant name.
     */
    @Basic
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *            the name of the Plant
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the location of the Plant.
     *
     * @return Plant location.
     */
    @Basic
    public String getLocation() {
        return this.location;
    }

    /**
     * Sets the location of the Plant.
     *
     * @param location
     *            Plant location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * @return The enterprise which the Plant belongs to
     */
    @ManyToOne
    public TradingEnterprise getEnterprise() {
        return this.enterprise;
    }

    /**
     * @param enterprise
     *            The enterprise which the Plant belongs to
     */
    public void setEnterprise(final TradingEnterprise enterprise) {
        this.enterprise = enterprise;
    }

    /**
     * @return all available operations this plant can offer
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<PlantOperation> getOperations() {
        return operations;
    }

    /**
     * @param operations all available operations this plant can offer
     */
    public void setOperations(Collection<PlantOperation> operations) {
        this.operations = operations;
    }

    /**
     * @return the production units available in this plant
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<ProductionUnit> getProductionUnits() {
        return productionUnits;
    }

    /**
     * @param productionUnits the production units available in this plant
     */
    public void setProductionUnits(Collection<ProductionUnit> productionUnits) {
        this.productionUnits = productionUnits;
    }
}
