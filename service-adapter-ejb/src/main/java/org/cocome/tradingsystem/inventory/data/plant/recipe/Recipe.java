/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
@Entity
public class Recipe implements Serializable, QueryableById {
    private static final long serialVersionUID = 1L;

    private long id;

    private CustomProduct customProduct;

    // Represent the vertices of the recipe graph
    private Collection<PlantOperation> operations;

    // Represent the edges of the recipe graph
    private Collection<ParameterInteraction> parameterInteractions;
    private Collection<EntryPointInteraction> entryPointInteractions;

    /**
     * @return A unique identifier of this Plant.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id a unique identifier of this Plant
     */
    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the custom product for which this recipe is used for
     */
    @NotNull
    @OneToOne
    public CustomProduct getCustomProduct() {
        return customProduct;
    }

    /**
     * @param customProduct the custom product for which this recipe is used for
     */
    public void setCustomProduct(CustomProduct customProduct) {
        this.customProduct = customProduct;
    }

    /**
     * @return the plant operation that is supposed to be executed within this interaction step
     */
    @OneToMany
    public Collection<PlantOperation> getOperations() {
        return operations;
    }

    /**
     * @param operations the plant operation that is supposed to be executed within this interaction step
     */
    public void setOperations(Collection<PlantOperation> operations) {
        this.operations = operations;
    }

    /**
     * @return the list of incoming interactions
     */
    @OneToMany
    public Collection<EntryPointInteraction> getEntryPointInteractions() {
        return entryPointInteractions;
    }

    /**
     * @param inputInteractions the list of incoming interactions
     */
    public void setEntryPointInteractions(Collection<EntryPointInteraction> inputInteractions) {
        this.entryPointInteractions = inputInteractions;
    }

    /**
     * @return the list of parameter bindings
     */
    @OneToMany
    public Collection<ParameterInteraction> getParameterInteractions() {
        return parameterInteractions;
    }

    /**
     * @param parameterInteractions the list of parameter bindings
     */
    public void setParameterInteractions(Collection<ParameterInteraction> parameterInteractions) {
        this.parameterInteractions = parameterInteractions;
    }
}
