package org.cocome.tradingsystem.remote.access.dao.plant.expression;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConstExpression;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ConstExpression}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ConstExpressionDAO extends AbstractDAO<ConstExpression> {

    private static final String PLANT_OP_ID_COL = PlantOperation.class.getSimpleName() + "Id";
    private static final String ID_COL = ConstExpression.class.getSimpleName() + "Id";
    private static final String OPTS_COL = ConstExpression.class.getSimpleName() + "Operations";

    @Override
    public Class<ConstExpression> getEntityType() {
        return ConstExpression.class;
    }

    @Override
    public Table<String> toTable(final List<ConstExpression> list) {
        final Table<String> table = new Table<>();
        table.addHeader(PLANT_OP_ID_COL, ID_COL, OPTS_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getPlantOperation().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, joinValues(list.get(i).getOperations()));
        }
        return table;
    }

    @Override
    public List<ConstExpression> fromTable(final EntityManager em,
                                           final Table<String> table,
                                           final Notification notification,
                                           final String sourceOperation) {
        final int len = table.size();
        final List<ConstExpression> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colPlantOptId = table.getColumnByName(i, PLANT_OP_ID_COL);
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colOpts = table.getColumnByName(i, OPTS_COL);

            final ConstExpression param = getOrCreateReferencedEntity(
                    ConstExpression.class, colId, em);

            try {
                param.setPlantOperation(getReferencedEntity(
                        PlantOperation.class,
                        colPlantOptId,
                        em));
                param.setOperations(getReferencedEntities(
                        ProductionUnitOperation.class,
                        colOpts,
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(param);
        }
        return list;
    }
}
