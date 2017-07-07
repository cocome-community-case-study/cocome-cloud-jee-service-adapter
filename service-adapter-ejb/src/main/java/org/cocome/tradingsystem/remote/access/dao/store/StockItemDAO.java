package org.cocome.tradingsystem.remote.access.dao.store;

import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.enterprise.ProductDAO;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link StockItem}
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class StockItemDAO implements DataAccessObject<StockItem> {

    @EJB
    private ProductDAO productDAO;

    @EJB
    private StoreDAO storeDAO;

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "stockitem";
    }

    @Override
    public Notification createEntities(List<StockItem> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        Store _store;
        Product _product;

        for (final StockItem nextStockItem : entities) {
            // query product
            _product = productDAO.queryProduct(em, nextStockItem.getProduct());
            if (_product == null) {
                notification.addNotification(
                        "createStockItem", Notification.FAILED,
                        "Product not available:" + nextStockItem.getProduct());
                continue;
            }
            // query store
            _store = storeDAO.queryStoreById(em, nextStockItem.getStore());
            if (_store == null) {
                notification.addNotification(
                        "createStockItem", Notification.FAILED,
                        "Store not available:" + nextStockItem.getStore());
                continue;
            }
            // ensure store can save items
            if (_store.getStockItems() == null) {
                _store.setStockItems(new ArrayList<>());
            }
            // update object with actual database objects
            nextStockItem.setStore(_store);
            nextStockItem.setProduct(_product);
            // persist
            _store.getStockItems().add(nextStockItem);
            em.persist(nextStockItem);
            em.merge(_store.getEnterprise());
            em.merge(_store);
            em.merge(_product);
            notification.addNotification(
                    "createStockItem", Notification.SUCCESS,
                    "Creation StockItem:" + nextStockItem);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification updateEntities(List<StockItem> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        StockItem _stockItem = null;
        Store _store;

        for (final StockItem nextStockItem : entities) {
            // query store
            _store = storeDAO.queryStoreById(em, nextStockItem.getStore());
            if (_store == null) {
                notification.addNotification(
                        "updateStockItems", Notification.FAILED,
                        "Store not available:" + nextStockItem.getStore());
                continue;
            }

            // query stock item
            for (final StockItem _storeNextStockItem : _store.getStockItems()) {
                if (_storeNextStockItem.getProduct().getBarcode()
                        == nextStockItem.getProduct().getBarcode()) {
                    _stockItem = _storeNextStockItem;
                    break;
                }
            }

            // update stock item
            if (_stockItem == null) {
                notification.addNotification(
                        "updateStockItems", Notification.FAILED,
                        "StockItem not available:" + nextStockItem);
                continue;
            }

            _stockItem.setAmount(nextStockItem.getAmount());
            _stockItem.setIncomingAmount(nextStockItem.getIncomingAmount());
            _stockItem.setMaxStock(nextStockItem.getMaxStock());
            _stockItem.setMinStock(nextStockItem.getMinStock());
            _stockItem.setSalesPrice(nextStockItem.getSalesPrice());

            em.merge(_stockItem);
            notification.addNotification(
                    "updateStockItems", Notification.SUCCESS,
                    "Update StockItem:" + _stockItem);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(List<StockItem> list) {
        final Table<String> table = new Table<>();
        table.addHeader("EnterpriseName",
                "StoreName", "StoreLocation", "ProductBarcode",
                "StockItemId", "StockItemMinStock", "StockItemMaxStock",
                "StockItemIncomingAmount", "StockItemAmount",
                "StockItemSalesPrice");
        int row = 0;
        for (final StockItem nextStockItem : list) {
            table.set(row, 0, nextStockItem.getStore().getEnterprise().getName());
            table.set(row, 1, nextStockItem.getStore().getName());
            table.set(row, 2, nextStockItem.getStore().getLocation());
            table.set(row, 3, String.valueOf(nextStockItem.getProduct().getBarcode()));
            table.set(row, 4, String.valueOf(nextStockItem.getId()));
            table.set(row, 5, String.valueOf(nextStockItem.getMinStock()));
            table.set(row, 6, String.valueOf(nextStockItem.getMaxStock()));
            table.set(row, 7, String.valueOf(nextStockItem.getIncomingAmount()));
            table.set(row, 8, String.valueOf(nextStockItem.getAmount()));
            table.set(row, 9, String.valueOf(nextStockItem.getSalesPrice()));
            row++;
        }
        return table;
    }

    @Override
    public List<StockItem> fromTable(Table<String> table) {
        final List<StockItem> list = new ArrayList<>();
        final int len = table.size();
        Column<String> colStoreId;
        Column<String> colProductBarcode;
        Column<String> colStockItemMinStock;
        Column<String> colStockItemMaxStock;
        Column<String> colStockItemIncomingAmount;
        Column<String> colStockItemAmount;
        Column<String> colStockItemSalesPrice;
        for (int i = 0; i < len; i++) {
            colStoreId = table.getColumnByName(i, "StoreId");
            colProductBarcode = table.getColumnByName(i, "ProductBarcode");
            colStockItemMinStock = table.getColumnByName(i, "StockItemMinStock");
            colStockItemMaxStock = table.getColumnByName(i, "StockItemMaxStock");
            colStockItemIncomingAmount = table.getColumnByName(i, "StockItemIncomingAmount");
            colStockItemAmount = table.getColumnByName(i, "StockItemAmount");
            colStockItemSalesPrice = table.getColumnByName(i, "StockItemSalesPrice");

            final Store store = new Store();

            if (colStoreId != null) {
                store.setId(Long.parseLong(colStoreId.getValue()));
            } else {
                store.setId(-1L);
            }

            final Product product = new Product();
            product.setBarcode(Long.parseLong(colProductBarcode.getValue()));

            final StockItem stockItem = new StockItem();
            stockItem.setStore(store);
            stockItem.setProduct(product);
            stockItem.setIncomingAmount(Long.parseLong(colStockItemIncomingAmount.getValue()));
            stockItem.setMaxStock(Long.parseLong(colStockItemMaxStock.getValue()));
            stockItem.setMinStock(Long.parseLong(colStockItemMinStock.getValue()));
            stockItem.setAmount(Long.parseLong(colStockItemAmount.getValue()));
            stockItem.setSalesPrice(Double.parseDouble(colStockItemSalesPrice.getValue()));

            list.add(stockItem);
        }
        return list;
    }
}
