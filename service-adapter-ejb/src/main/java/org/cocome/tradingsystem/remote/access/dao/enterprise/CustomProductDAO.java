package org.cocome.tradingsystem.remote.access.dao.enterprise;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link CustomProduct}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class CustomProductDAO extends AbstractDAO<CustomProduct> {

    private static final String ID_COL = CustomProduct.class.getSimpleName() + "Id";
    private static final String BARCODE_COL = CustomProduct.class.getSimpleName() + "Barcode";
    private static final String NAME_COL = CustomProduct.class.getSimpleName() + "Location";
    private static final String PURCHASE_PRICE_COL = CustomProduct.class.getSimpleName() + "PurchasePrice";

    @Override
    public Class<CustomProduct> getEntityType() {
        return CustomProduct.class;
    }

    @Override
    public Table<String> toTable(final List<CustomProduct> entities) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL,
                BARCODE_COL,
                NAME_COL,
                PURCHASE_PRICE_COL);

        int row = 0;
        for (final CustomProduct entity : entities) {
            table.set(row, 0, String.valueOf(entity.getId()));
            table.set(row, 1, String.valueOf(entity.getBarcode()));
            table.set(row, 2, entity.getName());
            table.set(row, 3, String.valueOf(entity.getPurchasePrice()));
            row++;
        }
        return table;
    }

    @Override
    public List<CustomProduct> fromTable(final EntityManager em,
                                         final Table<String> table,
                                         final Notification notification,
                                         final String sourceOperation) {
        final int len = table.size();
        final List<CustomProduct> entities = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colBarcode = table.getColumnByName(i, BARCODE_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colPurchasePrice = table.getColumnByName(i, PURCHASE_PRICE_COL);

            final CustomProduct product = getOrCreateReferencedEntity(CustomProduct.class, colId, em);

            product.setBarcode(Long.valueOf(colBarcode.getValue()));
            product.setName(colName.getValue());
            product.setPurchasePrice(Double.valueOf(colPurchasePrice.getValue()));

            entities.add(product);
        }

        return entities;
    }
}
