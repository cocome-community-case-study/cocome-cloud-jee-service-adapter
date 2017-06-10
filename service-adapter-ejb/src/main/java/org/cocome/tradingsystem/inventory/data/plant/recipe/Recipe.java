package org.cocome.tradingsystem.inventory.data.plant.recipe;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the top-level recipe for producing a custom product.
 * @author Rudolf Biczok
 */
@Entity
public class Recipe implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    private RecipeStep initialStep;

    /**
     * @return A unique identifier of this Plant.
     */
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

    @OneToOne
    public RecipeStep getInitialStep() {
        return initialStep;
    }

    public void setInitialStep(RecipeStep initialStep) {
        this.initialStep = initialStep;
    }

}
