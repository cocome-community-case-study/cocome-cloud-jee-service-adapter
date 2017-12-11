package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import org.cocome.tradingsystem.inventory.data.plant.parameter.Parameter;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;
import org.cocome.tradingsystem.remote.access.dao.AbstractInheritanceTreeDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * DAO for {@link Parameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ParameterDAO extends AbstractInheritanceTreeDAO<Parameter> {

    @Inject
    private BooleanParameterDAO booleanParameterDAO;

    @Inject
    private NominalParameterDAO nominalParameterDAO;

    @Override
    protected List<AbstractDAO<? extends Parameter>> getSubClasseDAOs() {
        return Arrays.asList(booleanParameterDAO,
                nominalParameterDAO);
    }

    @Override
    protected Class<Parameter> getEntityType() {
        return Parameter.class;
    }
}
