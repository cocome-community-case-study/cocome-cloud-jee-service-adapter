package org.cocome.tradingsystem.inventory.data.plant.recipe;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Rudolf Biczok
 */
@Entity
public class StepInteraction implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private EntryPoint from;
    private EntryPoint to;

    /**
     * @return the database id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id
     *            the database id
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the entry point of the previous {@link PlantOperation}
     */
    @OneToOne(fetch = FetchType.EAGER)
    public EntryPoint getFrom() {
        return from;
    }

    /**
     * @param from the entry point of the previous {@link PlantOperation}
     */
    public void setFrom(EntryPoint from) {
        this.from = from;
    }

    /**
     * @return the next entry point to the next {@link PlantOperation}
     */
    @OneToOne(fetch = FetchType.EAGER)
    public EntryPoint getTo() {
        return to;
    }

    /**
     * @param to the next entry point to the next {@link PlantOperation}
     */
    public void setTo(EntryPoint to) {
        this.to = to;
    }
}
