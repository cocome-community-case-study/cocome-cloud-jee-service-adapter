package org.cocome.tradingsystem.remote.access.dao.plant.productionunit.expression;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConstExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * DAO for {@link Expression}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ExpressionDAO extends AbstractDAO<Expression> {

    private static final String ID_COL = Expression.class.getSimpleName() + "Id";
    private static final String TYPE_COL = Expression.class.getSimpleName() + "Type";
    private static final String PARAM_ID_COL = ProductionParameter.class.getSimpleName() + "Id";
    private static final String PARAM_NAME_COL = ProductionParameter.class.getSimpleName() + "Name";
    private static final String PARAM_VALUE_COL = ConditionalExpression.class.getSimpleName() + "Value";
    private static final String ON_TRUE_COL = ConditionalExpression.class.getSimpleName() + "OnTrueExpressionOf";
    private static final String ON_FALSE_COL = ConditionalExpression.class.getSimpleName() + "OnFalseExpressionOf";
    private static final String OP_ID_COL = ProductionUnitOperation.class.getSimpleName() + "Id";
    private static final String OP_OID_COL = ProductionUnitOperation.class.getSimpleName() + "OperationId";

    @Override
    protected Class<Expression> getEntityType() {
        return Expression.class;
    }

    @Override
    public Table<String> toTable(final List<Expression> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL,
                TYPE_COL,
                PARAM_ID_COL,
                PARAM_NAME_COL,
                PARAM_VALUE_COL,
                ON_TRUE_COL,
                ON_FALSE_COL,
                OP_ID_COL,
                OP_OID_COL
        );
        writeEntityToTable(table, null, null, list, 0);
        return table;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Expression> fromTable(final EntityManager em,
                                      final Table<String> table,
                                      final Notification notification,
                                      final String sourceOperation) {
        final Map<String, Expression> map = new HashMap<>();
        for (int i = 0; i < table.size(); i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colType = table.getColumnByName(i, TYPE_COL);
            final Column<String> colParamId = table.getColumnByName(i, PARAM_ID_COL);
            final Column<String> colParamValue = table.getColumnByName(i, PARAM_VALUE_COL);
            final Column<String> colTrueExp = table.getColumnByName(i, ON_TRUE_COL);
            final Column<String> colFalseExp = table.getColumnByName(i, ON_FALSE_COL);
            final Column<String> colOptId = table.getColumnByName(i, OP_ID_COL);
            final Column<String> colOptOId = table.getColumnByName(i, OP_OID_COL);

            if (colType.getValue().equals(ConstExpression.class.getName())) {
                ConstExpression exp = (ConstExpression) map.get(colId.getValue());
                if (exp == null) {
                    exp = getOrCreateReferencedEntity(ConstExpression.class,
                            Long.parseLong(colId.getValue()),
                            em);
                    exp.setOperations(new ArrayList<>());

                    if (colTrueExp.getValue() != null) {
                        final ConditionalExpression c = (ConditionalExpression) map.get(colTrueExp.getValue());
                        assert c != null;
                        c.getOnTrueExpressions().add(exp);
                    }
                    if (colFalseExp.getValue() != null) {
                        final ConditionalExpression c = (ConditionalExpression) map.get(colFalseExp.getValue());
                        assert c != null;
                        c.getOnFalseExpressions().add(exp);
                    }
                    map.put(colId.getValue(), exp);
                }
                final ProductionUnitOperation option = getOrCreateReferencedEntity(
                        ProductionUnitOperation.class,
                        Long.parseLong(colOptId.getValue()),
                        em);
                option.setOperationId(colOptOId.getValue());
                exp.getOperations().add(option);

                map.put(colId.getValue(), exp);
            } else if (colType.getValue().equals(ConditionalExpression.class.getName())) {
                final ProductionParameter<PlantOperation> param;
                try {
                    param = getReferencedEntity(
                            ProductionParameter.class,
                            Long.valueOf(colParamId.getValue()),
                            em);
                } catch (final EntityNotFoundException e) {
                    notification.addNotification(
                            sourceOperation,
                            Notification.FAILED,
                            String.format("%s not available: %s", getEntityType().getSimpleName(),
                                    e.getMessage()));
                    continue;
                }

                ConditionalExpression exp = (ConditionalExpression) map.get(colId.getValue());
                if (exp == null) {
                    exp = getOrCreateReferencedEntity(
                            ConditionalExpression.class,
                            Long.parseLong(colId.getValue()),
                            em);
                    exp.setOnTrueExpressions(new ArrayList<>());
                    exp.setOnFalseExpressions(new ArrayList<>());

                    if (colTrueExp.getValue() != null) {
                        final ConditionalExpression c = (ConditionalExpression) map.get(colTrueExp.getValue());
                        assert c != null;
                        c.getOnTrueExpressions().add(exp);
                    }
                    if (colFalseExp.getValue() != null) {
                        final ConditionalExpression c = (ConditionalExpression) map.get(colFalseExp.getValue());
                        assert c != null;
                        c.getOnFalseExpressions().add(exp);
                    }
                    map.put(colId.getValue(), exp);
                }
                exp.setParameterValue(colParamValue.getValue());
                exp.setParameter(param);
            } else {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("Unknown parameter type: %s", colType.getValue()));
            }
        }
        return new ArrayList<>(map.values());
    }

    private int writeEntityToTable(final Table<String> table,
                                   final Long onTrueReference,
                                   final Long onFalseReference,
                                   final Collection<Expression> entities,
                                   final int row) {
        int newCount = row;
        for (final Expression entity : entities) {
            if (entity.getClass() == ConstExpression.class) {
                final ConstExpression exp = (ConstExpression) entity;
                for (final ProductionUnitOperation option : exp.getOperations()) {
                    table.set(row, 0, String.valueOf(exp.getId()));
                    table.set(row, 1, exp.getClass().getName());
                    table.set(row, 2, "");
                    table.set(row, 3, "");
                    table.set(row, 4, "");
                    table.set(row, 5, stringValueOrNull(onTrueReference));
                    table.set(row, 6, stringValueOrNull(onFalseReference));
                    table.set(row, 7, String.valueOf(option.getId()));
                    table.set(row, 8, option.getOperationId());
                    newCount++;
                }
            } else if (entity.getClass() == ConditionalExpression.class) {
                final ConditionalExpression exp = (ConditionalExpression) entity;
                table.set(row, 0, String.valueOf(exp.getId()));
                table.set(row, 1, exp.getClass().getName());
                table.set(row, 2, String.valueOf(exp.getParameter().getId()));
                table.set(row, 3, exp.getParameter().getName());
                table.set(row, 4, exp.getParameterValue());
                table.set(row, 5, stringValueOrNull(onTrueReference));
                table.set(row, 6, stringValueOrNull(onFalseReference));
                table.set(row, 7, "");
                table.set(row, 8, "");
                newCount++;
                //recursively traverse the nested expressions
                newCount += writeEntityToTable(table,
                        exp.getId(),
                        null,
                        exp.getOnTrueExpressions(), newCount);
                newCount += writeEntityToTable(table,
                        null,
                        exp.getId(),
                        exp.getOnFalseExpressions(),
                        newCount);
            } else {
                throw new UnsupportedOperationException("Unknown class: " + entity.getClass());
            }
        }
        return newCount;
    }

    private String stringValueOrNull(final Long onTrueReference) {
        return onTrueReference != null ? String.valueOf(onTrueReference) : "";
    }
}
