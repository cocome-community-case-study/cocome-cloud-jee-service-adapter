package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
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
    private static final String PROD_CLASS_ID_COL = ProductionUnitClass.class.getSimpleName() + "Id";
    private static final String PROD_CLASS_NAME_COL = ProductionUnitClass.class.getSimpleName() + "Name";

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
                PROD_CLASS_ID_COL,
                PROD_CLASS_NAME_COL);

        int row = 0;
        for (final ProductionUnit entity : entities) {
            table.set(row, 0, String.valueOf(entity.getId()));
            table.set(row, 1, entity.getLocation());
            table.set(row, 2, entity.getInterfaceUrl());
            table.set(row, 3, String.valueOf(entity.getProductionUnitClass().getId()));
            table.set(row, 4, entity.getProductionUnitClass().getName());
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
            final Column<String> colPUCId = table.getColumnByName(i, PROD_CLASS_ID_COL);
            final Column<String> colPUCName = table.getColumnByName(i, PROD_CLASS_NAME_COL);

            final ProductionUnitClass puc;
            try {
                puc = getReferencedEntity(
                        ProductionUnitClass.class,
                        Long.valueOf(colPUCId.getValue()),
                        em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.SUCCESS,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }
            puc.setId(Long.parseLong(colPUCId.getValue()));
            puc.setName(colPUCName.getValue());

            final ProductionUnit pu = getOrCreateReferencedEntity(ProductionUnit.class,
                    Long.parseLong(colPUId.getValue()),
                    em);
            pu.setLocation(colPULocation.getValue());
            pu.setInterfaceUrl(colPUInterfaceURL.getValue());
            pu.setProductionUnitClass(puc);

            entities.add(pu);
        }

        return entities;
    }
}
