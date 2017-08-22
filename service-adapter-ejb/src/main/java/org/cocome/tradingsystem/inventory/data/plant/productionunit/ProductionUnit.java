package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.plant.Plant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This class represents a Product in the database
 *
 * @author Rudolf Biczok
 */
@Entity
public class ProductionUnit implements Serializable, QueryableById {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String location;
    private String interfaceUrl;

    private Plant plant;
    private ProductionUnitClass productionUnitClass;

    /**
     * Gets identifier value
     *
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * Sets identifier.
     *
     * @param id
     *            Identifier value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Production unit location.
     */
    @Basic
    public String getLocation() {
        return this.location;
    }

    /**
     * @param location
     *            Production unit location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * @return The URL location used to communicate with the device
     */
    @Basic
    public String getInterfaceUrl() {
        return this.interfaceUrl;
    }

    /**
     * @param interfaceUrl
     *            The URL location used to communicate with the device
     */
    public void setInterfaceUrl(final String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    /**
     * @return the production unit class
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotNull
    public ProductionUnitClass getProductionUnitClass() {
        return productionUnitClass;
    }

    /**
     * @param productionUnitClass the production unit class
     */
    public void setProductionUnitClass(ProductionUnitClass productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }

    /**
     * @return the plant that owns this production unit
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    public Plant getPlant() {
        return plant;
    }

    /**
     * @param plant the plant that owns this production unit
     */
    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
