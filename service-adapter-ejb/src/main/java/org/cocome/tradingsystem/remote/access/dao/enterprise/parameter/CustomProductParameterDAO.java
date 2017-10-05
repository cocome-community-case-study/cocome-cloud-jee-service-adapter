package org.cocome.tradingsystem.remote.access.dao.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.CustomProductParameter;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;
import org.cocome.tradingsystem.remote.access.dao.AbstractInheritanceTreeDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * DAO for {@link CustomProductParameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class CustomProductParameterDAO extends AbstractInheritanceTreeDAO<CustomProductParameter> {

    @Inject
    private BooleanCustomProductParameterDAO booleanCustomProductParameterDAO;

    @Inject
    private NorminalCustomProductParameterDAO norminalCustomProductParameterDAO;

    @Override
    protected List<AbstractDAO<? extends CustomProductParameter>> getSubClasseDAOs() {
        return Arrays.asList(booleanCustomProductParameterDAO,
                norminalCustomProductParameterDAO);
    }

    @Override
    protected Class<CustomProductParameter> getEntityType() {
        return CustomProductParameter.class;
    }
}
