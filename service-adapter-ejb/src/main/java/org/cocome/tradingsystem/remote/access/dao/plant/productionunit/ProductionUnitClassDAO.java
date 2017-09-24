package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ProductionUnitClass}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductionUnitClassDAO extends AbstractDAO<ProductionUnitClass> {

    private static final String PLANT_ID_COL = Plant.class.getSimpleName() + "Id";
    private static final String ID_COL = ProductionUnitClass.class.getSimpleName() + "Id";
    private static final String NAME_COL = ProductionUnitClass.class.getSimpleName() + "Name";

    @Override
    public Class<ProductionUnitClass> getEntityType() {
        return ProductionUnitClass.class;
    }

    @Override
    public Table<String> toTable(final List<ProductionUnitClass> list) {
        final Table<String> table = new Table<>();
        table.addHeader(PLANT_ID_COL, ID_COL, NAME_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getPlant().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, list.get(i).getName());
        }
        return table;
    }

    @Override
    public List<ProductionUnitClass> fromTable(final EntityManager em,
                                               final Table<String> table,
                                               final Notification notification,
                                               final String sourceOperation) {
        final int len = table.size();
        final List<ProductionUnitClass> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colPlantId = table.getColumnByName(i, PLANT_ID_COL);
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);

            final Plant plant;
            try {
                plant = getReferencedEntity(
                        Plant.class,
                        Long.valueOf(colPlantId.getValue()),
                        em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            final ProductionUnitClass puc = getOrCreateReferencedEntity(ProductionUnitClass.class, colId, em);
            puc.setPlant(plant);
            puc.setName(colName.getValue());
            list.add(puc);
        }
        return list;
    }
}
