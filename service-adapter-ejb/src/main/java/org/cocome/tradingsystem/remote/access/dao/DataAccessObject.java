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

package org.cocome.tradingsystem.remote.access.dao;

import de.kit.ipd.java.utils.framework.table.Table;

import org.cocome.tradingsystem.remote.access.Notification;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Used as interface for data retrieval and manipulation through the rest interface
 *
 * @param <E> the entity type to wrap
 * @author Rudolf Biczok
 */
public interface DataAccessObject<E> {

    String getEntityTypeName();

    Notification createEntities(Table<String> table);

    Notification updateEntities(Table<String> table);

    Notification deleteEntities(Table<String> table);

    Table<String> toTable(final List<E> list);

    default <T> T querySingleInstance(final TypedQuery<T> query) {
        assert query != null;
        T result;
        try {
            result = query.getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
        return result;
    }
}
