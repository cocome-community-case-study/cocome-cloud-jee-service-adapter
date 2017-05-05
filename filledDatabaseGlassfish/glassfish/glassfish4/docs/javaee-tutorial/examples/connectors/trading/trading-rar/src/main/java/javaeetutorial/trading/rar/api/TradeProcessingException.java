/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package javaeetutorial.trading.rar.api;

/* Indicates that the trade order could not be processed */
public class TradeProcessingException extends Exception {

    public TradeProcessingException(String msg) {
        super(msg);
    }
}
