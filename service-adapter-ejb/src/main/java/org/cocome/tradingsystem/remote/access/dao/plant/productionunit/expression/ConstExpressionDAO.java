package org.cocome.tradingsystem.remote.access.dao.plant.productionunit.expression;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConstExpression;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link ConstExpression}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ConstExpressionDAO extends AbstractDAO<ConstExpression> {

    private static final String ID_COL = ConstExpression.class.getSimpleName() + "Id";
    private static final String OP_ID_COL = ProductionUnitOperation.class.getSimpleName() + "Id";
    private static final String OP_OID_COL = ProductionUnitOperation.class.getSimpleName() + "OperationId";

    @Override
    protected Class<ConstExpression> getEntityType() {
        return ConstExpression.class;
    }

    @Override
    protected void syncEntity(EntityManager em, ConstExpression entity) {
    }

    @Override
    public Table<String> toTable(final List<ConstExpression> entities) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, OP_ID_COL, OP_OID_COL);
        int row = 0;
        for (final ConstExpression entity : entities) {
            for (final ProductionUnitOperation option : entity.getOperations()) {
                table.set(row, 0, String.valueOf(entity.getId()));
                table.set(row, 1, String.valueOf(option.getId()));
                table.set(row, 2, option.getOperationId());
                row++;
            }
        }
        return table;
    }

    @Override
    public List<ConstExpression> fromTable(final Table<String> table) {
        final Map<String, ConstExpression> map = new HashMap<>();
        final int len = table.size();

        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colOpId = table.getColumnByName(i, OP_ID_COL);
            final Column<String> colOpOId = table.getColumnByName(i, OP_OID_COL);

            ConstExpression expr = map.get(colId.getValue());
            if (expr == null) {
                expr = new ConstExpression();
                expr.setId(Long.parseLong(colId.getValue()));
                expr.setOperations(new ArrayList<>());
                map.put(colId.getValue(), expr);
            }

            final ProductionUnitOperation operation = new ProductionUnitOperation();
            operation.setId(Long.parseLong(colOpId.getValue()));
            operation.setOperationId(colOpOId.getValue());
            expr.getOperations().add(operation);
        }

        return new ArrayList<>(map.values());
    }
}
