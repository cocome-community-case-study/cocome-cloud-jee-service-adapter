package org.cocome.tradingsystem.remote.access.dao.enterprise.parameter;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.NorminalCustomProductParameter;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * DAO for {@link NorminalCustomProductParameter}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class NorminalCustomProductParameterDAO extends AbstractDAO<NorminalCustomProductParameter> {
    private static final String CUSTOM_PRODUCT_ID_COL = CustomProduct.class.getSimpleName() + "Id";
    private static final String ID_COL = NorminalCustomProductParameter.class.getSimpleName() + "Id";
    private static final String NAME_COL = NorminalCustomProductParameter.class.getSimpleName() + "Name";
    private static final String CATEGORY_COL = NorminalCustomProductParameter.class.getSimpleName() + "Category";
    private static final String OPTONS_COL = NorminalCustomProductParameter.class.getSimpleName() + "Options";

    @Override
    public Class<NorminalCustomProductParameter> getEntityType() {
        return NorminalCustomProductParameter.class;
    }

    @Override
    public Table<String> toTable(final List<NorminalCustomProductParameter> list) {
        final Table<String> table = new Table<>();
        table.addHeader(CUSTOM_PRODUCT_ID_COL, ID_COL, NAME_COL, CATEGORY_COL, OPTONS_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getProduct().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, list.get(i).getName());
            table.set(i, 3, list.get(i).getCategory());
            table.set(i, 4, joinStringValues(list.get(i).getOptions()));
        }
        return table;
    }

    @Override
    public List<NorminalCustomProductParameter> fromTable(final EntityManager em,
                                                          final Table<String> table,
                                                          final Notification notification,
                                                          final String sourceOperation) {
        final int len = table.size();
        final List<NorminalCustomProductParameter> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colProductId = table.getColumnByName(i, CUSTOM_PRODUCT_ID_COL);
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colCategory = table.getColumnByName(i, CATEGORY_COL);
            final Column<String> colOptions = table.getColumnByName(i, OPTONS_COL);

            final CustomProduct prod;
            try {
                prod = getReferencedEntity(
                        CustomProduct.class,
                        Long.valueOf(colProductId.getValue()),
                        em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            final NorminalCustomProductParameter param = getOrCreateReferencedEntity(
                    NorminalCustomProductParameter.class, colId, em);
            param.setProduct(prod);
            param.setName(colName.getValue());
            param.setCategory(colCategory.getValue());
            param.setOptions(new HashSet<>(Arrays.asList(colOptions.getValue().split(SET_DELIMITER))));
            list.add(param);
        }
        return list;
    }
}
