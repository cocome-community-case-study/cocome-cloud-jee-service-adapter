package org.cocome.tradingsystem.remote.access.dao.enterprise;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.LegacyDataAccessObject;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Product}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductDAO implements LegacyDataAccessObject<Product> {

    @EJB
    private ProductSupplierDAO productSupplierDAO;

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "product";
    }

    @Override
    public Notification createEntities(List<Product> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final Product nextProduct : entities) {
            if (this.queryProduct(em, nextProduct) == null) {
                em.persist(nextProduct);
                notification.addNotification(
                        "createProducts", Notification.SUCCESS,
                        String.format("%s[id=%d]", Product.class.getName(), nextProduct.getId()));
            } else {
                notification.addNotification(
                        "createProducts", Notification.FAILED,
                        "Product already available:" + nextProduct);
            }
        }
        return notification;
    }

    @Override
    public Notification deleteEntities(final List<Product> entities) {
        final Notification notification = new Notification();
        if (entities != null) {
            final EntityManager em = this.emf.createEntityManager();

            for (final Product entity : entities) {
                final Product managedEntity = this.queryProduct(em, entity);
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
    public Notification updateEntities(List<Product> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        for (final Product nextProduct : entities) {
            // query product
            final Product _pro = this.queryProduct(em, nextProduct);
            if (_pro != null) {
                // update purchase price
                double pprice = _pro.getPurchasePrice();
                _pro.setPurchasePrice(nextProduct.getPurchasePrice());
                notification.addNotification(
                        "updateProduct", Notification.SUCCESS,
                        "Update PurchasePrice:From" + pprice + "->TO:" + nextProduct.getPurchasePrice());
                // update product supplier
                if (nextProduct.getSupplier() != null) {
                    ProductSupplier _proSupp = productSupplierDAO.queryProductSupplier(em, nextProduct.getSupplier());
                    if (_proSupp != null) {
                        _pro.setSupplier(_proSupp);
                        notification.addNotification(
                                "updateProduct", Notification.SUCCESS,
                                "Update ProductSupplier:" + _proSupp);
                    } else {
                        notification.addNotification(
                                "updateProduct", Notification.FAILED,
                                "New ProductSupplier");
                    }
                } else {
                    notification.addNotification(
                            "updateProduct", Notification.WARNING,
                            "ProductSupplier not provided for:" + nextProduct);
                }
                // refresh database object
                em.merge(_pro);
            } else {
                notification.addNotification(
                        "updateProducts", Notification.FAILED,
                        "Product not available:" + nextProduct);
            }
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(List<Product> list) {
        final Table<String> table = new Table<>();
        table.addHeader("ProductBarcode", "ProductName", "ProductPurchasePrice");
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getBarcode()));
            table.set(i, 1, list.get(i).getName());
            table.set(i, 2, String.valueOf(list.get(i).getPurchasePrice()));
            // TODO supplier should also be added!
        }
        return table;
    }

    @Override
    public List<Product> fromTable(Table<String> table) {
        final List<Product> list = new ArrayList<>();
        final int len = table.size();
        Column<String> colBarcode;
        Column<String> colName;
        Column<String> colPurchasePrice;
        for (int i = 0; i < len; i++) {
            colBarcode = table.getColumnByName(i, "ProductBarcode");
            colName = table.getColumnByName(i, "ProductName");
            colPurchasePrice = table.getColumnByName(i, "ProductPurchasePrice");

            if (colBarcode == null) {
                System.out.println("Barcode column was null for row=" + i);
                continue;
            }
            final Product p = new Product();
            p.setBarcode(Long.parseLong(colBarcode.getValue()));
            if (colName != null) {
                p.setName(colName.getValue());
            }
            if (colPurchasePrice != null) {
                p.setPurchasePrice(Double.parseDouble(colPurchasePrice.getValue()));
            }
            list.add(p);
        }
        return list;
    }

    public Product queryProduct(final EntityManager em, final Product product) {
        return querySingleInstance(em.createQuery(
                "SELECT p FROM Product p WHERE p.barcode = :pBarCode",
                Product.class).setParameter("pBarCode", product.getBarcode()));
    }
}
