package org.cocome.tradingsystem.remote.access.dao.enterprise;

import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;
import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.remote.access.Notification;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link ProductSupplier}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class ProductSupplierDAO implements DataAccessObject<ProductSupplier> {

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "productsupplier";
    }

    @Override
    public Notification createEntities(List<ProductSupplier> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final ProductSupplier nextProSupp : entities) {
            if (this.queryProductSupplier(em, nextProSupp) == null) {
                em.persist(nextProSupp);
                notification.addNotification(
                        "createProductSupplier", Notification.SUCCESS,
                        "Creation ProductSupplier:" + nextProSupp);
            } else {
                notification.addNotification(
                        "createProductSupplier", Notification.FAILED,
                        "ProductSupplier already available:" + nextProSupp);
            }
        }
        return notification;
    }

    @Override
    public Notification updateEntities(List<ProductSupplier> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();

        for (final ProductSupplier nextSupplier : entities) {
            final ProductSupplier _supplier = em.find(ProductSupplier.class, nextSupplier.getId());
            if (_supplier == null) {
                notification.addNotification(
                        "updateProductSupplier", Notification.FAILED,
                        "ProductSupplier not available:" + nextSupplier);
                continue;
            }
            _supplier.setName(nextSupplier.getName());
            em.merge(_supplier);
            notification.addNotification(
                    "updateProductSupplier", Notification.SUCCESS,
                    "Update ProductSupplier:" + nextSupplier);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(List<ProductSupplier> list) {
        final Table<String> table = new Table<>();
        table.addHeader("ProductSupplierId", "ProductSupplierName");
        int row = 0;
        for (final ProductSupplier nextProductSupplier : list) {
            table.addColumn(row, 0, String.valueOf(nextProductSupplier.getId()), true);
            table.addColumn(row, 1, nextProductSupplier.getName(), true);
            row++;
        }
        return table;
    }

    @Override
    public List<ProductSupplier> fromTable(Table<String> table) {
        final List<ProductSupplier> list = new ArrayList<>();
        final int len = table.size();
        Column<String> colName;
        Column<String> colId;
        for (int i = 0; i < len; i++) {
            colName = table.getColumnByName(i, "ProductSupplierName");
            colId = table.getColumnByName(i, "ProductSupplierId");
            if (colName == null) {
                // TODO message error!
                System.out.println("Name column was null for row=" + i);
                continue;
            }

            final ProductSupplier p = new ProductSupplier();
            if (colId != null) {
                p.setId(Long.parseLong(colId.getValue()));
            }
            p.setName(colName.getValue());
            list.add(p);
        }
        return list;
    }

    ProductSupplier queryProductSupplier(final EntityManager em, final ProductSupplier ps) {
        return querySingleInstance(em.createQuery(
                "SELECT p FROM ProductSupplier p WHERE p.name = :pName",
                ProductSupplier.class).setParameter("pName", ps.getName()));
    }
}
