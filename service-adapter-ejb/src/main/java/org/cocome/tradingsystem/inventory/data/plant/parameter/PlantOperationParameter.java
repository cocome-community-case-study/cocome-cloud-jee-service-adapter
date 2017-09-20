package org.cocome.tradingsystem.inventory.data.plant.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Abstract class of {@link IParameter} for {@link PlantOperation}
 *
 * @author Rudolf Biczok
 */
@Entity
public class PlantOperationParameter implements IParameter {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private String category;
    private PlantOperation plantOperation;

    /**
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The parameter name
     */
    @Override
    @Basic
    public String getName() {
        return name;
    }

    /**
     * @param name The parameter name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The the parameter category
     */
    @Basic
    public String getCategory() {
        return category;
    }

    /**
     * @param category The parameter category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the corresponding plant operation
     */
    @NotNull
    @ManyToOne
    public PlantOperation getPlantOperation() {
        return plantOperation;
    }

    /**
     * @param plantOperation the corresponding plant operation
     */
    public void setPlantOperation(PlantOperation plantOperation) {
        this.plantOperation = plantOperation;
    }
}
