package org.cocome.tradingsystem.remote.access.dao.store;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.time.TimeUtils;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.LegacyDataAccessObject;
import org.cocome.tradingsystem.remote.access.dao.enterprise.ProductDAO;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link ProductOrder}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductOrderDAO implements LegacyDataAccessObject<ProductOrder> {

    @EJB
    private ProductDAO productDAO;

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "productorder";
    }

    @Override
    public Notification createEntities(List<ProductOrder> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        Store _store;
        Product _product;
        ProductOrder _productOrder;
        for (final ProductOrder nextOrder : entities) {

            _store = em.find(Store.class, nextOrder.getStore().getId());
            if (_store == null) {
                notification.addNotification(
                        "createProductOrder", Notification.FAILED,
                        "Store not available:" + nextOrder.getStore());
                continue;
            }

            _productOrder = new ProductOrder();
            _productOrder.setOrderEntries(new ArrayList<>());
            _productOrder.setStore(_store);
            _productOrder.setDeliveryDate(nextOrder.getDeliveryDate());
            _productOrder.setOrderingDate(nextOrder.getOrderingDate());
            // set the store of database

            // search for the right product
            for (final OrderEntry nextOrderEntry : nextOrder.getOrderEntries()) {
                // query product
                _product = productDAO.queryProduct(em, nextOrderEntry.getProduct());
                if (_product == null) {
                    System.out.println(
                            "Product missing:" + nextOrderEntry.getProduct());
                    notification.addNotification(
                            "createProductOrder", Notification.FAILED,
                            "Product not available:" + nextOrderEntry.getProduct());
                    continue;
                }
                final OrderEntry _orderEntery = new OrderEntry();
                _orderEntery.setProduct(_product);
                _orderEntery.setAmount(nextOrderEntry.getAmount());
                _orderEntery.setOrder(_productOrder);
                _productOrder.getOrderEntries().add(_orderEntery);
                // set the product of database
            }
            // persist order
            em.persist(_productOrder);
            notification.addNotification(
                    "createProductOrder", Notification.SUCCESS,
                    String.format("%s[id=%d]", ProductOrder.class.getName(), _productOrder.getId()));
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Notification deleteEntities(final List<ProductOrder> entities) {
        final Notification notification = new Notification();
        if (entities != null) {
            final EntityManager em = this.emf.createEntityManager();

            for (final ProductOrder entity : entities) {
                final ProductOrder managedEntity = this.queryProductOrder(em, entity);
                if (managedEntity == null) {
                    notification.addNotification(
                            "createEntities", Notification.FAILED,
                            "Entity not available:" + entity);
                    return notification;
                }

                em.remove(managedEntity);
                notification.addNotification(
                        "deleteEntities", Notification.SUCCESS,
                        "Entity deleted:" + entity);
            }
            em.flush();
            em.close();
            return notification;
        }
        throw new IllegalAccessError("[deleteEntity]argument is null");
    }

    @Override
    public Notification updateEntities(List<ProductOrder> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        ProductOrder _order;
        for (final ProductOrder nextOrder : entities) {
            _order = em.find(ProductOrder.class, nextOrder.getId());
            if (_order == null) {
                notification.addNotification(
                        "updateProductOrder", Notification.FAILED,
                        "ProductOrder not available:" + nextOrder);
                continue;
            }

            // update
            if (nextOrder.getDeliveryDate() != null) {
                _order.setDeliveryDate(nextOrder.getDeliveryDate());
            }

            if (nextOrder.getOrderingDate() != null) {
                _order.setOrderingDate(nextOrder.getOrderingDate());
            }

            em.merge(_order);
            notification.addNotification(
                    "updateProductOrder", Notification.SUCCESS,
                    "Update ProductOrder:" + nextOrder);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(List<ProductOrder> list) {
        final Table<String> table = new Table<>();
        table.addHeader("ProductOrderId", "EnterpriseName",
                "StoreName", "StoreLocation", "ProductBarcode",
                "OrderDeliveryDate", "OrderOrderingDate",
                "OrderAmount");
        int row = 0;
        for (final ProductOrder nextOrder : list) {
            for (final OrderEntry nextOrderEntry : nextOrder.getOrderEntries()) {
                table.addColumn(row, 0, String.valueOf(nextOrder.getId()), true);
                table.addColumn(row, 1, nextOrder.getStore().getEnterprise().getName(), true);
                table.addColumn(row, 2, nextOrder.getStore().getName(), true);
                table.addColumn(row, 3, nextOrder.getStore().getLocation(), true);
                table.addColumn(row, 4, String.valueOf(nextOrderEntry.getProduct().getBarcode()), true);
                table.addColumn(row, 5, TimeUtils.convertToStringDate(nextOrder.getDeliveryDate()), true);
                table.addColumn(row, 6, TimeUtils.convertToStringDate(nextOrder.getOrderingDate()), true);
                table.addColumn(row, 7, String.valueOf(nextOrderEntry.getAmount()), true);
                row++;
            }
        }
        return table;
    }

    @Override
    public List<ProductOrder> fromTable(Table<String> table) {
        final Map<String, ProductOrder> map = new HashMap<>();

        final int len = table.size();
        Column<String> colProductOrderId;
        Column<String> colStoreId;
        Column<String> colProductBarcode;
        Column<String> colOrderDeliveryDate;
        Column<String> colOrderOrderingDate;
        Column<String> colOrderAmount;

        for (int i = 0; i < len; i++) {
            colProductOrderId = table.getColumnByName(i, "ProductOrderId");
            colStoreId = table.getColumnByName(i, "StoreId");
            colProductBarcode = table.getColumnByName(i, "ProductBarcode");
            colOrderDeliveryDate = table.getColumnByName(i, "OrderDeliveryDate");
            colOrderOrderingDate = table.getColumnByName(i, "OrderOrderingDate");
            colOrderAmount = table.getColumnByName(i, "OrderAmount");

            ProductOrder productOrder = map.get(colProductOrderId.getValue());
            if (productOrder == null) {
                productOrder = new ProductOrder();
                productOrder.setId(Long.parseLong(colProductOrderId.getValue()));
                productOrder.setOrderEntries(new ArrayList<>());

                final Store store = new Store();
                store.setId(Long.parseLong(colStoreId.getValue()));
                productOrder.setStore(store);

                productOrder.setDeliveryDate(TimeUtils.convertToDateObject(
                        colOrderDeliveryDate.getValue()));
                productOrder.setOrderingDate(TimeUtils.convertToDateObject(
                        colOrderOrderingDate.getValue()));

                map.put(colProductOrderId.getValue(), productOrder);
            }

            final Product product = new Product();
            product.setBarcode(Long.parseLong(colProductBarcode.getValue()));

            final OrderEntry orderentry = new OrderEntry();
            orderentry.setOrder(productOrder);

            orderentry.setAmount(Long.parseLong(colOrderAmount.getValue()));
            orderentry.setProduct(product);
            productOrder.getOrderEntries().add(orderentry);
        }

        final List<ProductOrder> list = new ArrayList<>(map.values());
        return list;
    }

    private ProductOrder queryProductOrder(final EntityManager em, final ProductOrder productOrder) {
        return querySingleInstance(em.createQuery(
                "SELECT s FROM ProductOrder s WHERE s.id = :id",
                ProductOrder.class).setParameter("id", productOrder.getId()));
    }
}
