package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Represents a product customization parameter for {@link CustomProduct}.
 * @author Rudolf Biczok
 */
@Entity
public class CustomProductParameter implements IParameter {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private String category;
    private CustomProduct product;

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
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The parameter name
     */
    @Override
    @Basic
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @Basic
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @NotNull
    @ManyToOne
    public CustomProduct getProduct() {
        return product;
    }

    public void setProduct(CustomProduct product) {
        this.product = product;
    }
}