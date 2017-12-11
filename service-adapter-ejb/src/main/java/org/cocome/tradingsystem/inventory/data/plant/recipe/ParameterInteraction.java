package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.plant.parameter.Parameter;

import javax.persistence.*;

/**
 * Used to connect parameters from {@link CustomProduct} and {@link PlantOperation}.
 * Other subsystems are supposed to copy the customer's parameter values to the plant
 * operation based on this mapping
 *
 * @author Rudolf Biczok
 */
@Entity
public class ParameterInteraction implements InteractionEntity<Parameter> {
    private static final long serialVersionUID = 1L;

    private long id;
    private Recipe recipe;
    private Parameter from;
    private Parameter to;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @ManyToOne
    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    @OneToOne(fetch = FetchType.EAGER)
    public Parameter getFrom() {
        return from;
    }

    @Override
    public void setFrom(Parameter from) {
        this.from = from;
    }

    @Override
    @OneToOne(fetch = FetchType.EAGER)
    public Parameter getTo() {
        return to;
    }

    @Override
    public void setTo(Parameter to) {
        this.to = to;
    }

    @Transient
    @Override
    public Class<Parameter> getInteractionType() {
        return Parameter.class;
    }
}
