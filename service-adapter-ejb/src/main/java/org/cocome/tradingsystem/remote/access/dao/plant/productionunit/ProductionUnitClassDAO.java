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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link ProductionUnitClass}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductionUnitClassDAO extends AbstractDAO<ProductionUnitClass> {

    private static final String ID_COL = ProductionUnitClass.class.getSimpleName() + "Id";
    private static final String NAME_COL = ProductionUnitClass.class.getSimpleName() + "Name";
    private static final String OPT_ID_COL = ProductionUnitOperation.class.getSimpleName() + "Id";
    private static final String OPT_OID_COL = ProductionUnitOperation.class.getSimpleName() + "OperationId";

    @Override
    public Class<ProductionUnitClass> getEntityType() {
        return ProductionUnitClass.class;
    }

    @Override
    public Table<String> toTable(final List<ProductionUnitClass> entities) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, NAME_COL, OPT_ID_COL, OPT_OID_COL);
        int row = 0;
        for (final ProductionUnitClass entity : entities) {
            for (final ProductionUnitOperation operation : entity.getOperations()) {
                table.set(row, 0, String.valueOf(entity.getId()));
                table.set(row, 1, entity.getName());
                table.set(row, 2, String.valueOf(operation.getId()));
                table.set(row, 3, operation.getOperationId());
                row++;
            }
        }
        return table;
    }

    @Override
    public List<ProductionUnitClass> fromTable(final EntityManager em,
                                               final Table<String> table,
                                               final Notification notification,
                                               final String sourceOperation) {
        final Map<String, ProductionUnitClass> map = new HashMap<>();
        final int len = table.size();

        for (int i = 0; i < len; i++) {
            final Column<String> colParamId = table.getColumnByName(i, ID_COL);
            final Column<String> colParamName = table.getColumnByName(i, NAME_COL);
            final Column<String> colOptId = table.getColumnByName(i, OPT_ID_COL);
            final Column<String> colOptName = table.getColumnByName(i, OPT_OID_COL);

            ProductionUnitClass unitClass = map.get(colParamId.getValue());
            if (unitClass == null) {
                unitClass = getOrCreateReferencedEntity(ProductionUnitClass.class,
                        Long.parseLong(colParamId.getValue()),
                        em);
                unitClass.setName(colParamName.getValue());
                unitClass.setOperations(new ArrayList<>());
                map.put(colParamId.getValue(), unitClass);
            }

            final ProductionUnitOperation option = new ProductionUnitOperation();
            option.setId(Long.parseLong(colOptId.getValue()));
            option.setOperationId(colOptName.getValue());
            unitClass.getOperations().add(option);
        }

        return new ArrayList<>(map.values());
    }
}
