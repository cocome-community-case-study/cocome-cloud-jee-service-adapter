package org.cocome.tradingsystem.remote.access.dao;

import de.kit.ipd.java.utils.framework.table.Row;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.framework.table.TableHeader;
import org.cocome.tradingsystem.inventory.data.enterprise.QueryableById;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Abstract class for entity access with
 *
 * @param <E> the entity type to wrap
 * @author Rudolf Biczok
 */
public abstract class AbstractInheritanceTreeDAO<E extends QueryableById> extends AbstractDAO<E> {

    @Override
    public Table<String> toTable(final List<E> list) {
        final Map<Class<? extends E>,
                AbstractDAO<? extends E>> daoMap =
                this.getSubClasseDAOs()
                        .stream()
                        .collect(Collectors.toMap(AbstractDAO::getEntityType, Function.identity()));

        final Map<Class<?>, List<E>> groupedList =
                list.stream().collect(Collectors.groupingBy(Object::getClass));

        checkForUnknownClasses(daoMap, groupedList);

        final Map<Class<?>, Table<String>> tableMap = new HashMap<>();
        final Map<Class<?>, Integer> beginMap = new HashMap<>();
        final Map<Class<?>, Integer> endMap = new HashMap<>();

        final int numberOfMetaCols = 3;

        final List<String> colNames = new ArrayList<>();
        final String classTypeCol = this.getEntityType().getSimpleName() + "Type";
        colNames.add(classTypeCol);
        final String beginCol = this.getEntityType().getSimpleName() + "BeginCol";
        colNames.add(beginCol);
        final String endCol = this.getEntityType().getSimpleName() + "EndCol";
        colNames.add(endCol);

        int lastBeginOffset = numberOfMetaCols;

        for (final Map.Entry<Class<?>, List<E>> entry : groupedList.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getName()))
                .collect(Collectors.toList())) {
            @SuppressWarnings("unchecked") final AbstractDAO<E> dao
                    = (AbstractDAO<E>) daoMap.get(entry.getKey());
            final Table<String> table = dao.toTable(entry.getValue());
            tableMap.put(entry.getKey(), table);

            beginMap.put(entry.getKey(), lastBeginOffset);
            endMap.put(entry.getKey(), lastBeginOffset + table.getHeader().size() - 1);

            colNames.addAll(table.getHeader().stream().map(TableHeader::getName).collect(Collectors.toList()));

            lastBeginOffset += table.getHeader().size();
        }

        final Table<String> joinTable = new Table<>();

        joinTable.addHeader(colNames.toArray(new String[colNames.size()]));

        final Map<Class<?>, Integer> perSubClassEntityCount = tableMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> 0));

        for (int i = 0; i < list.size(); ++i) {
            final E entity = list.get(i);

            final Table<String> table = tableMap.get(entity.getClass());

            joinTable.set(i, 0, entity.getClass().getName());
            joinTable.set(i, 1, String.valueOf(beginMap.get(entity.getClass())));
            joinTable.set(i, 2, String.valueOf(endMap.get(entity.getClass())));

            for (int k = numberOfMetaCols; k < colNames.size(); k++) {
                final Row<String> row = table.getRow(perSubClassEntityCount.get(entity.getClass()));
                if (k >= beginMap.get(entity.getClass()) && k <= endMap.get(entity.getClass())) {
                    final String value = row.getColumns().get(k - beginMap.get(entity.getClass())).getValue();
                    joinTable.set(i, k, value);
                } else {
                    joinTable.set(i, k, "null");
                }
            }
            perSubClassEntityCount.put(entity.getClass(), perSubClassEntityCount.get(entity.getClass()) + 1);
        }

        return joinTable;
    }


    private void checkForUnknownClasses(Map<Class<? extends E>, AbstractDAO<? extends E>> daoMap,
                                        Map<Class<?>, List<E>> groupedList) {
        final List<Class<?>> unknownClasses = groupedList.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .filter(c -> !daoMap.containsKey(c))
                .collect(Collectors.toList());

        if (!unknownClasses.isEmpty()) {
            throw new IllegalArgumentException("Unknown subclasses: " + unknownClasses + " of " + this.getEntityType());
        }
    }

    @Override
    public List<E> fromTable(final EntityManager em,
                             final Table<String> table,
                             final Notification notification,
                             final String sourceOperation) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    protected abstract List<AbstractDAO<? extends E>> getSubClasseDAOs();
}
