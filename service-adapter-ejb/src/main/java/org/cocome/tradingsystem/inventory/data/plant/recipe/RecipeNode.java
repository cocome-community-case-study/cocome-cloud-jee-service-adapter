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

import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
@Entity
public class RecipeNode implements QueryableById, Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private Recipe recipe;
    private RecipeOperation operation;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }


    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe from) {
        this.recipe = from;
    }

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    public RecipeOperation getOperation() {
        return operation;
    }

    public void setOperation(RecipeOperation to) {
        this.operation = to;
    }
}
