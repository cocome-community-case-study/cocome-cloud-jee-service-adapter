package org.cocome.tradingsystem.inventory.data.plant.recipe;

import javax.persistence.*;

/**
 * @author Rudolf Biczok
 */
@Entity
public class EntryPointInteraction implements InteractionEntity<EntryPoint, EntryPoint> {
    private static final long serialVersionUID = 1L;

    private long id;

    private EntryPoint from;
    private EntryPoint to;

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

    @Override
    @OneToOne(fetch = FetchType.EAGER)
    public EntryPoint getFrom() {
        return from;
    }

    @Override
    public void setFrom(EntryPoint from) {
        this.from = from;
    }

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
    public Class<EntryPoint> getFromClass() {
        return EntryPoint.class;
    }

    @Transient
    @Override
    public Class<EntryPoint> getToClass() {
        return EntryPoint.class;
    }
}
