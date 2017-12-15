package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.recipe.Recipe;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Recipe}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class RecipeDAO extends AbstractDAO<Recipe> {

    private static final String ID_COL = Recipe.class.getSimpleName() + "Id";
    private static final String CUSTOM_PRODUCT_BARCODE_COL = CustomProduct.class.getSimpleName() + "Barcode";
    private static final String NAME_COL = Recipe.class.getSimpleName() + "Name";
    private static final String ENTERPRISE_ID_COL = TradingEnterprise.class.getSimpleName() + "Id";

    @Override
    public Class<Recipe> getEntityType() {
        return Recipe.class;
    }

    @Override
    public Table<String> toTable(final List<Recipe> list) {
        final Table<String> table = new Table<>();
        table.addHeader(
                ID_COL, CUSTOM_PRODUCT_BARCODE_COL, NAME_COL, ENTERPRISE_ID_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getCustomProduct().getBarcode()));
            table.set(i, 2, list.get(i).getName());
            table.set(i, 3, String.valueOf(list.get(i).getEnterprise().getId()));
        }
        return table;
    }

    @Override
    public List<Recipe> fromTable(final EntityManager em,
                                  final Table<String> table,
                                  final Notification notification,
                                  final String sourceOperation) {
        final int len = table.size();
        final List<Recipe> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colCustomProductBarcode = table.getColumnByName(i, CUSTOM_PRODUCT_BARCODE_COL);
            final Column<String> colNameId = table.getColumnByName(i, NAME_COL);
            final Column<String> colEnterpriseId = table.getColumnByName(i, ENTERPRISE_ID_COL);

            final Recipe recipe = getOrCreateReferencedEntity(Recipe.class, colId, em);
            recipe.setName(colNameId.getValue());
            try {
                recipe.setCustomProduct(queryProductByBarcode(em,
                        Long.valueOf(colCustomProductBarcode.getValue())));
                recipe.setEnterprise(getReferencedEntity(
                        TradingEnterprise.class,
                        colEnterpriseId,
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(recipe);
        }
        return list;
    }

    public CustomProduct queryProductByBarcode(final EntityManager em, final long barcode) {
        return querySingleInstance(em.createQuery(
                "SELECT p FROM CustomProduct p WHERE p.barcode = :barcode",
                CustomProduct.class).setParameter("barcode", barcode));
    }
}
