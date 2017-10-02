package org.cocome.tradingsystem.inventory.data.enterprise;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This class represents a customizable product in the database
 *
 * @author Rudolf Biczok
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class CustomProduct extends AbstractProduct {

    private static final long serialVersionUID = -2577328715744776645L;
}
