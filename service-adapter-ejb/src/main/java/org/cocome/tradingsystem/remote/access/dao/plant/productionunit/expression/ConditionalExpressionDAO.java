package org.cocome.tradingsystem.remote.access.dao.plant.productionunit.expression;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.ProductionParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.remote.access.ReflectionUtil;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

    private static final String ID_COL = ConditionalExpression.class.getSimpleName() + "Id";
    private static final String PARAM_ID_COL = ProductionParameter.class.getSimpleName() + "Id";
    private static final String PARAM_NAME_COL = ProductionParameter.class.getSimpleName() + "Name";
    private static final String PARAM_TYPE_COL = ProductionParameter.class.getSimpleName() + "Type";
    private static final String PARAM_VALUE_COL = ProductionParameter.class.getSimpleName() + "Value";
    private static final String ON_TRUE_COL = ConditionalExpression.class.getSimpleName() + "OnTrueId";
    private static final String ON_TRUE_TYPE_COL = ConditionalExpression.class.getSimpleName() + "OnTrueType";
    private static final String ON_FALSE_COL = ConditionalExpression.class.getSimpleName() + "OnFalse";
    private static final String ON_FALSE_TYPE_COL = ConditionalExpression.class.getSimpleName() + "OnFalseType";

    @Override
    protected Class<ConditionalExpression> getEntityType() {
        return ConditionalExpression.class;
    }

    @Override
    protected void syncEntity(EntityManager em, ConditionalExpression entity) {
        entity.setParameter(
                getReferencedEntity(
                        entity.getParameter(),
                        em));
        entity.setOnFalseExpression(
                getReferencedEntity(
                        entity.getOnFalseExpression(),
                        em));
        entity.setOnTrueExpression(
                getReferencedEntity(
                        entity.getOnTrueExpression(),
                        em));
    }

    @Override
    public Table<String> toTable(final List<ConditionalExpression> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL,
                PARAM_ID_COL,
                PARAM_NAME_COL,
                PARAM_TYPE_COL,
                PARAM_VALUE_COL,
                ON_TRUE_COL,
                ON_TRUE_TYPE_COL,
                ON_FALSE_COL,
                ON_FALSE_TYPE_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getParameter().getId()));
            table.set(i, 2, list.get(i).getParameter().getName());
            table.set(i, 3, list.get(i).getParameter().getClass().getName());
            table.set(i, 4, list.get(i).getParameterValue());
            table.set(i, 5, String.valueOf(list.get(i).getOnTrueExpression().getId()));
            table.set(i, 6, list.get(i).getOnTrueExpression().getClass().getName());
            table.set(i, 7, String.valueOf(list.get(i).getOnFalseExpression().getId()));
            table.set(i, 8, list.get(i).getOnFalseExpression().getClass().getName());
        }
        return table;
    }

    @Override
    public List<ConditionalExpression> fromTable(final Table<String> table) {
        final int len = table.size();
        final List<ConditionalExpression> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colParamId = table.getColumnByName(i, PARAM_ID_COL);
            final Column<String> colParamName = table.getColumnByName(i, PARAM_NAME_COL);
            final Column<String> colParamType = table.getColumnByName(i, PARAM_TYPE_COL);
            final Column<String> colParamValue = table.getColumnByName(i, PARAM_VALUE_COL);
            final Column<String> colOnTrueExpId = table.getColumnByName(i, ON_TRUE_COL);
            final Column<String> colOnTrueExpType = table.getColumnByName(i, ON_TRUE_TYPE_COL);
            final Column<String> colOnFalseExpId = table.getColumnByName(i, ON_FALSE_COL);
            final Column<String> colOnFalseExpType = table.getColumnByName(i, ON_FALSE_TYPE_COL);

            @SuppressWarnings("unchecked")
            final ProductionParameter<PlantOperation> param = ReflectionUtil.createInstance(
                    ProductionParameter.class,
                    colParamType.getValue());
            param.setId(Long.valueOf(colParamId.getValue()));
            param.setName(colParamName.getValue());

            final Expression onTrue = ReflectionUtil.createInstance(
                    Expression.class,
                    colOnTrueExpType.getValue());
            onTrue.setId(Long.valueOf(colOnTrueExpId.getValue()));

            final Expression onFalse = ReflectionUtil.createInstance(
                    Expression.class,
                    colOnFalseExpType.getValue());
            onFalse.setId(Long.valueOf(colOnFalseExpId.getValue()));

            final ConditionalExpression condition = new ConditionalExpression();
            condition.setId(Long.parseLong(colId.getValue()));
            condition.setParameterValue(colParamValue.getValue());
            condition.setParameter(param);
            condition.setOnTrueExpression(onTrue);
            condition.setOnFalseExpression(onFalse);

            list.add(condition);
        }
        return list;
    }
}
