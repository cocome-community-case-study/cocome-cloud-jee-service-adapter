package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ProductionUnitOperation}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductionUnitOperationDAO extends AbstractDAO<ProductionUnitOperation> {

    private static final String ID_COL = ProductionUnitOperation.class.getSimpleName() + "Id";
    private static final String NAME_COL = ProductionUnitOperation.class.getSimpleName() + "Name";
    private static final String OID_COL = ProductionUnitOperation.class.getSimpleName() + "OID";
    private static final String EXP_TIME_COL = ProductionUnitOperation.class.getSimpleName() + "ExpectedTime";
    private static final String PUC_COL = ProductionUnitClass.class.getSimpleName() + "Id";

    @Override
    public Class<ProductionUnitOperation> getEntityType() {
        return ProductionUnitOperation.class;
    }

    @Override
    public Table<String> toTable(final List<ProductionUnitOperation> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, NAME_COL, OID_COL, EXP_TIME_COL, PUC_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getName());
            table.set(i, 2, list.get(i).getOperationId());
            table.set(i, 3, String.valueOf(list.get(i).getExecutionDurationInMillis()));
            table.set(i, 4, String.valueOf(list.get(i).getProductionUnitClass().getId()));
        }
        return table;
    }

    @Override
    public List<ProductionUnitOperation> fromTable(final EntityManager em,
                                                   final Table<String> table,
                                                   final Notification notification,
                                                   final String sourceOperation) {
        final int len = table.size();
        final List<ProductionUnitOperation> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colOId = table.getColumnByName(i, OID_COL);
            final Column<String> colExecTime = table.getColumnByName(i, EXP_TIME_COL);
            final Column<String> colPUC = table.getColumnByName(i, PUC_COL);

            final ProductionUnitClass puc;
            try {
                puc = getReferencedEntity(
                        ProductionUnitClass.class,
                        Long.valueOf(colPUC.getValue()),
                        em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            final ProductionUnitOperation op = getOrCreateReferencedEntity(ProductionUnitOperation.class, colId, em);
            op.setProductionUnitClass(puc);
            op.setName(colName.getValue());
            op.setOperationId(colOId.getValue());
            op.setExecutionDurationInMillis(Long.valueOf(colExecTime.getValue()));
            list.add(op);
        }
        return list;
    }
}
