package org.cocome.tradingsystem.remote.access.dao.plant.expression;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;
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
 * DAO for {@link ConditionalExpression}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ConditionalExpressionDAO extends AbstractDAO<ConditionalExpression> {

    private static final String PARAMETER_ID_COL = PlantOperationParameter.class.getSimpleName() + "Id";
    private static final String ID_COL = ConditionalExpression.class.getSimpleName() + "Id";
    private static final String PARAM_VALUE_COL = ConditionalExpression.class.getSimpleName() + "ParameterValue";
    private static final String TRUE_EXPRS_COL = ConditionalExpression.class.getSimpleName() + "OnTrueExpressions";
    private static final String FALSE_EXPRS_COL = ConditionalExpression.class.getSimpleName() + "OnFalseExpressions";

    @Override
    public Class<ConditionalExpression> getEntityType() {
        return ConditionalExpression.class;
    }

    @Override
    public Table<String> toTable(final List<ConditionalExpression> list) {
        final Table<String> table = new Table<>();
        table.addHeader(
                PARAMETER_ID_COL,
                ID_COL,
                PARAM_VALUE_COL,
                TRUE_EXPRS_COL,
                FALSE_EXPRS_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getParameter().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, String.valueOf(list.get(i).getParameterValue()));
            table.set(i, 3, joinValues(list.get(i).getOnTrueExpressions()));
            table.set(i, 4, joinValues(list.get(i).getOnFalseExpressions()));
        }
        return table;
    }

    @Override
    public List<ConditionalExpression> fromTable(final EntityManager em,
                                                 final Table<String> table,
                                                 final Notification notification,
                                                 final String sourceOperation) {
        final int len = table.size();
        final List<ConditionalExpression> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colParamId = table.getColumnByName(i, PARAMETER_ID_COL);
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colParamValue = table.getColumnByName(i, PARAM_VALUE_COL);
            final Column<String> colOnTrue = table.getColumnByName(i, TRUE_EXPRS_COL);
            final Column<String> colOnFalse = table.getColumnByName(i, FALSE_EXPRS_COL);

            final ConditionalExpression param = getOrCreateReferencedEntity(
                    ConditionalExpression.class, colId, em);

            try {
                param.setParameter(getReferencedEntity(
                        PlantOperationParameter.class,
                        colParamId,
                        em));
                param.setOnTrueExpressions(getReferencedEntities(
                        Expression.class,
                        colOnTrue,
                        em));
                param.setOnFalseExpressions(getReferencedEntities(
                        Expression.class,
                        colOnFalse,
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }
            param.setParameterValue(colParamValue.getValue());

            list.add(param);
        }
        return list;
    }
}
