package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnit;
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
 * DAO for {@link ProductionUnit}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductionUnitDAO extends AbstractDAO<ProductionUnit> {

    private static final String ID_COL = ProductionUnit.class.getSimpleName() + "Id";
    private static final String LOCATION_COL = ProductionUnit.class.getSimpleName() + "Location";
    private static final String INTERFACE_URL_COL = ProductionUnit.class.getSimpleName() + "InterfaceURL";
    private static final String DOUBLE_FLAG_COL = ProductionUnit.class.getSimpleName() + "Double";
    private static final String PLANT_ID_COL = Plant.class.getSimpleName() + "Id";
    private static final String PROD_CLASS_ID_COL = ProductionUnitClass.class.getSimpleName() + "Id";

    @Override
    public Class<ProductionUnit> getEntityType() {
        return ProductionUnit.class;
    }

    @Override
    public Table<String> toTable(final List<ProductionUnit> entities) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL,
                LOCATION_COL,
                INTERFACE_URL_COL,
                DOUBLE_FLAG_COL,
                PLANT_ID_COL,
                PROD_CLASS_ID_COL);

        int row = 0;
        for (final ProductionUnit entity : entities) {
            table.set(row, 0, String.valueOf(entity.getId()));
            table.set(row, 1, entity.getLocation());
            table.set(row, 2, entity.getInterfaceUrl());
            table.set(row, 3, String.valueOf(entity.isDouble()));
            table.set(row, 4, String.valueOf(entity.getPlant().getId()));
            table.set(row, 5, String.valueOf(entity.getProductionUnitClass().getId()));
            row++;
        }
        return table;
    }

    @Override
    public List<ProductionUnit> fromTable(final EntityManager em,
                                          final Table<String> table,
                                          final Notification notification,
                                          final String sourceOperation) {
        final int len = table.size();
        final List<ProductionUnit> entities = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            final Column<String> colPUId = table.getColumnByName(i, ID_COL);
            final Column<String> colPULocation = table.getColumnByName(i, LOCATION_COL);
            final Column<String> colPUInterfaceURL = table.getColumnByName(i, INTERFACE_URL_COL);
            final Column<String> colDoubleFlag = table.getColumnByName(i, DOUBLE_FLAG_COL);
            final Column<String> colPlantId = table.getColumnByName(i, PLANT_ID_COL);
            final Column<String> colPUCId = table.getColumnByName(i, PROD_CLASS_ID_COL);
            final Plant plant;
            final ProductionUnitClass puc;
            try {
                plant = getReferencedEntity(
                        Plant.class,
                        colPlantId,
                        em);
                puc = getReferencedEntity(
                        ProductionUnitClass.class,
                        colPUCId,
                        em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }
            final ProductionUnit pu = getOrCreateReferencedEntity(ProductionUnit.class, colPUId, em);
            pu.setLocation(colPULocation.getValue());
            pu.setInterfaceUrl(colPUInterfaceURL.getValue());
            pu.setDouble(Boolean.valueOf(colDoubleFlag.getValue()));
            pu.setPlant(plant);
            pu.setProductionUnitClass(puc);

            entities.add(pu);
        }

        return entities;
    }
}
