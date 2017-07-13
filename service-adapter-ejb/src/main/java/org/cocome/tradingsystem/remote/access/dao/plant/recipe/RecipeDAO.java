package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link Recipe}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class RecipeDAO extends AbstractDAO<Recipe> {

    private static final String ID_COL = Recipe.class.getSimpleName() + "Id";
    private static final String OP_ID_COL = PlantOperation.class.getSimpleName() + "Id";
    private static final String EP_IN_ID_COL = EntryPointInteraction.class.getSimpleName() + "InputId";
    private static final String EP_OUT_ID_COL = EntryPointInteraction.class.getSimpleName() + "OutputId";
    private static final String PARAM_ID_COL = ParameterInteraction.class.getSimpleName() + "Id";

    @Override
    public Class<Recipe> getEntityType() {
        return Recipe.class;
    }

    @Override
    public Table<String> toTable(final List<Recipe> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, OP_ID_COL, EP_IN_ID_COL, EP_OUT_ID_COL, PARAM_ID_COL);

        //Every element in the collections gets one exclusive table row
        int row = 0;
        for (final Recipe entity : list) {
            for (final PlantOperation pi : entity.getOperations()) {
                table.set(row, 0, String.valueOf(entity.getId()));
                table.set(row, 1, String.valueOf(pi.getId()));
                table.set(row, 2, "");
                table.set(row, 3, "");
                table.set(row, 4, "");
                row++;
            }
            for (final EntryPointInteraction ei : entity.getInputInteractions()) {
                table.set(row, 0, String.valueOf(entity.getId()));
                table.set(row, 1, "");
                table.set(row, 2, String.valueOf(ei.getId()));
                table.set(row, 3, "");
                table.set(row, 4, "");
                row++;
            }
            for (final EntryPointInteraction ei : entity.getOutputInteractions()) {
                table.set(row, 0, String.valueOf(entity.getId()));
                table.set(row, 1, "");
                table.set(row, 2, "");
                table.set(row, 3, String.valueOf(ei.getId()));
                table.set(row, 4, "");
                row++;
            }
            for (final ParameterInteraction pi : entity.getParameterInteractions()) {
                table.set(row, 0, String.valueOf(entity.getId()));
                table.set(row, 1, "");
                table.set(row, 2, "");
                table.set(row, 3, "");
                table.set(row, 4, String.valueOf(pi.getId()));
                row++;
            }
        }
        return table;
    }

    @Override
    public List<Recipe> fromTable(final EntityManager em,
                                  final Table<String> table,
                                  final Notification notification,
                                  final String sourceOperation) {
        final Map<String, Recipe> map = new HashMap<>();
        for (int i = 0; i < table.size(); i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colOpId = table.getColumnByName(i, OP_ID_COL);
            final Column<String> colEpInId = table.getColumnByName(i, EP_IN_ID_COL);
            final Column<String> colEpOutId = table.getColumnByName(i, EP_OUT_ID_COL);
            final Column<String> colParamId = table.getColumnByName(i, PARAM_ID_COL);

            Recipe recipe = map.get(colId.getValue());
            if (recipe == null) {
                recipe = getOrCreateReferencedEntity(
                        Recipe.class,
                        Long.parseLong(colId.getValue()),
                        em);
                recipe.setOperations(new ArrayList<>());
                recipe.setInputInteractions(new ArrayList<>());
                recipe.setOutputInteractions(new ArrayList<>());
                recipe.setParameterInteractions(new ArrayList<>());
                map.put(colId.getValue(), recipe);
            }
            try {
                if (!colOpId.getValue().isEmpty()) {
                    recipe.getOperations().add(getReferencedEntity(
                            PlantOperation.class,
                            Long.valueOf(colOpId.getValue()),
                            em));
                }
                if (!colEpInId.getValue().isEmpty()) {
                    recipe.getInputInteractions().add(getReferencedEntity(
                            EntryPointInteraction.class,
                            Long.valueOf(colEpInId.getValue()),
                            em));
                }
                if (!colEpOutId.getValue().isEmpty()) {
                    recipe.getOutputInteractions().add(getReferencedEntity(
                            EntryPointInteraction.class,
                            Long.valueOf(colEpOutId.getValue()),
                            em));
                }
                if (!colParamId.getValue().isEmpty()) {
                    recipe.getParameterInteractions().add(getReferencedEntity(
                            ParameterInteraction.class,
                            Long.valueOf(colParamId.getValue()),
                            em));
                }
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
            }

        }
        return new ArrayList<>(map.values());
    }
}
