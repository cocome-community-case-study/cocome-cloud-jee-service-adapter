package org.cocome.tradingsystem.remote.access.dao.plant.parameter;

import org.cocome.tradingsystem.inventory.data.plant.parameter.PlantOperationParameter;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;
import org.cocome.tradingsystem.remote.access.dao.AbstractInheritanceTreeDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.parameter.BooleanPlantOperationParameterDAO;
import org.cocome.tradingsystem.remote.access.dao.plant.parameter.NorminalPlantOperationParameterDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * DAO for {@link PlantOperationParameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PlantOperationParameterDAO extends AbstractInheritanceTreeDAO<PlantOperationParameter> {

    @Inject
    private BooleanPlantOperationParameterDAO booleanPlantOperationParameterDAO;

    @Inject
    private NorminalPlantOperationParameterDAO norminalPlantOperationParameterDAO;

    @Override
    protected List<AbstractDAO<? extends PlantOperationParameter>> getSubClasseDAOs() {
        return Arrays.asList(booleanPlantOperationParameterDAO,
                norminalPlantOperationParameterDAO);
    }

    @Override
    protected Class<PlantOperationParameter> getEntityType() {
        return PlantOperationParameter.class;
    }
}
