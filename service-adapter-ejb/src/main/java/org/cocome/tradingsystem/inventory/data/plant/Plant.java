package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This class represents a plant that provides access to a collection of {@link PlantOperation}.
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
    @NotNull
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
}
