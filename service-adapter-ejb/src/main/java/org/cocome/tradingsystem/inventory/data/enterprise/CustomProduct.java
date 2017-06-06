package org.cocome.tradingsystem.inventory.data.enterprise;

import javax.persistence.*;
import java.io.Serializable;


/**
 * This class represents a dynamic product in the database
 *
 * @author Rudolf Biczok
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class CustomProduct implements Serializable {
    private long id;

    private long barcode;

    /**
     * Gets identifier value
     *
     * @return The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * Sets identifier.
     *
     * @param id Identifier value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The barcode of the product
     */
    @Basic
    public long getBarcode() {
        return barcode;
    }

    /**
     * @param barcode The barcode of the product
     */
    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

}
