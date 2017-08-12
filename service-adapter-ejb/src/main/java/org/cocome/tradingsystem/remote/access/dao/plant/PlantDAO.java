package org.cocome.tradingsystem.remote.access.dao.plant;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Plant}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PlantDAO extends AbstractDAO<Plant> {

    private static final String
            ENTERPRISE_ID_COL = TradingEnterprise.class.getSimpleName() + "Id";
    private static final String ID_COL = Plant.class.getSimpleName() + "Id";
    private static final String NAME_COL = Plant.class.getSimpleName() + "Name";
    private static final String LOCATION_COL = Plant.class.getSimpleName() + "Location";

    @Override
    public Class<Plant> getEntityType() {
        return Plant.class;
    }

    @Override
    public Table<String> toTable(final List<Plant> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ENTERPRISE_ID_COL, ID_COL, NAME_COL, LOCATION_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getEnterprise().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, list.get(i).getName());
            table.set(i, 3, list.get(i).getLocation());
        }
        return table;
    }

    @Override
    public List<Plant> fromTable(final EntityManager em,
                                 final Table<String> table,
                                 final Notification notification,
                                 final String sourceOperation) {
        final int len = table.size();
        final List<Plant> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colEnterpriseId = table.getColumnByName(i, ENTERPRISE_ID_COL);
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colName = table.getColumnByName(i, NAME_COL);
            final Column<String> colLocation = table.getColumnByName(i, LOCATION_COL);

            final TradingEnterprise t;
            try {
                t = getReferencedEntity(
                        TradingEnterprise.class,
                        Long.valueOf(colEnterpriseId.getValue()),
                        em);
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            final Plant plant = getOrCreateReferencedEntity(Plant.class, colId, em);
            plant.setEnterprise(t);
            plant.setLocation(colLocation.getValue());
            plant.setName(colName.getValue());
            list.add(plant);
        }
        return list;
    }
}
