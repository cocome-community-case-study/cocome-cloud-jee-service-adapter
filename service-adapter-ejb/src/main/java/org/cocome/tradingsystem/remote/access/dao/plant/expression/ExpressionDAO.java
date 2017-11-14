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
    private ConditionalExpressionDAO conditionalExpressionDAO;

    @Inject
    private ProductionUnitOperationDAO productionUnitOperationDAO;

    @Override
    protected List<AbstractDAO<? extends Expression>> getSubClasseDAOs() {
        return Arrays.asList(conditionalExpressionDAO, productionUnitOperationDAO);
    }

    @Override
    protected Class<Expression> getEntityType() {
        return Expression.class;
    }
}
