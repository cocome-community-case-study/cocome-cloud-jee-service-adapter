package org.cocome.tradingsystem.remote.access.dao.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;
import org.cocome.tradingsystem.remote.access.dao.AbstractInheritanceTreeDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.productionunit.ProductionUnitOperationDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * DAO for {@link Expression}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ExpressionDAO extends AbstractInheritanceTreeDAO<Expression> {

    @Inject
    private ProductionUnitOperationDAO productionUnitOperationDAO;

    @Inject
    private ConditionalExpressionDAO conditionalExpressionDAO;

    @Override
    protected List<AbstractDAO<? extends Expression>> getSubClasseDAOs() {
        return Arrays.asList(productionUnitOperationDAO, conditionalExpressionDAO);
    }

    @Override
    protected Class<Expression> getEntityType() {
        return Expression.class;
    }
}
