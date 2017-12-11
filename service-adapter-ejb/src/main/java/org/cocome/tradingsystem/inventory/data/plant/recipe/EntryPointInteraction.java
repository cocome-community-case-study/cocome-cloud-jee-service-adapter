package org.cocome.tradingsystem.inventory.data.plant.recipe;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Rudolf Biczok
 */
@Entity
public class EntryPointInteraction implements InteractionEntity<EntryPoint> {
    private static final long serialVersionUID = 1L;

    private long id;

    private EntryPoint from;
    private EntryPoint to;
    private Recipe recipe;

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

    @NotNull
    @ManyToOne
    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @NotNull
    @Override
    @OneToOne(fetch = FetchType.EAGER)
    public EntryPoint getFrom() {
        return from;
    }

    @Override
    public void setFrom(EntryPoint from) {
        this.from = from;
    }

    @NotNull
    @Override
    @OneToOne(fetch = FetchType.EAGER)
    public EntryPoint getTo() {
        return to;
    }

    @Override
    public void setTo(EntryPoint to) {
        this.to = to;
    }

    @Transient
    @Override
    public Class<EntryPoint> getInteractionType() {
        return EntryPoint.class;
    }

}
