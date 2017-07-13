package org.cocome.tradingsystem.inventory.data.plant.recipe;

import javax.persistence.*;

/**
 * @author Rudolf Biczok
 */
@Entity
public class EntryPointInteraction extends InteractionEntity<EntryPoint, EntryPoint> {
    private static final long serialVersionUID = 1L;

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
