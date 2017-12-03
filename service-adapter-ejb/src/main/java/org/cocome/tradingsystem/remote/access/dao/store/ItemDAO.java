package org.cocome.tradingsystem.remote.access.dao.store;

import org.cocome.tradingsystem.inventory.data.store.Item;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;
import org.cocome.tradingsystem.remote.access.dao.AbstractInheritanceTreeDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * DAO for {@link Item}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ItemDAO extends AbstractInheritanceTreeDAO<Item> {

    @Inject
    private StockItemDAO stockItemDAO;

    @Inject
    private OnDemandItemDAO onDemandItemDAO;

    @Override
    protected List<AbstractDAO<? extends Item>> getSubClasseDAOs() {
        return Arrays.asList(stockItemDAO, onDemandItemDAO);
    }

    @Override
    protected Class<Item> getEntityType() {
        return Item.class;
    }
}
