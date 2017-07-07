package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
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
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public ProductionUnitClass getProductionUnitClass() {
        return productionUnitClass;
    }

    /**
     * @param productionUnitClass the production unit class
     */
    public void setProductionUnitClass(ProductionUnitClass productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }
}
