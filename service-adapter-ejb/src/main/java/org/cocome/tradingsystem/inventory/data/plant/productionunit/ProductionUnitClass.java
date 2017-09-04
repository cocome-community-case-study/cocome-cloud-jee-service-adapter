package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents a class of production unity utilizing a specific set of {@link ProductionUnitOperation}
 * @author Rudolf Biczok
 */
@Entity
public class ProductionUnitClass implements Serializable, QueryableById {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private TradingEnterprise enterprise;

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
     * @return The enterprise which the Plant belongs to
     */
    @ManyToOne(cascade = CascadeType.ALL)
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
