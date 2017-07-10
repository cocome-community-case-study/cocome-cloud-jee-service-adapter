package org.cocome.tradingsystem.remote.access.dao.plant.productionunit.expression;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.ReflectionUtil;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected Class<Expression> getEntityType() {
        return Expression.class;
    }

    @Override
    public Table<String> toTable(final List<Expression> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, TYPE_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, list.get(i).getClass().getName());
        }
        return table;
    }

    @Override
    public List<Expression> fromTable(final EntityManager em,
                                      final Table<String> table,
                                      final Notification notification,
                                      final String sourceOperation) {
        final int len = table.size();
        final List<Expression> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colType = table.getColumnByName(i, TYPE_COL);

            final Expression param = ReflectionUtil.createInstance(
                    Expression.class,
                    colType.getValue());
            param.setId(Long.parseLong(colId.getValue()));
            list.add(param);
        }
        return list;
    }
}
