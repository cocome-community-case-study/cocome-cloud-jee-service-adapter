package org.cocome.tradingsystem.remote.access;

import org.junit.Assert;

import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DatabaseAccessTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(DatabaseAccess.class.getPackage())
                .addAsResource("META-INF/persistence_arquillian.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private DatabaseAccess databaseAccess;

    @Test
    public void shouldFindAllGamesUsingJpqlQuery() throws Exception {
        Assert.assertNotNull(databaseAccess);
    }

}