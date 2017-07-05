package cocome.cloud.sa.serviceprovider.impl.dao.plant;


import org.cocome.tradingsystem.remote.access.DatabaseAccess;
import org.cocome.tradingsystem.remote.access.DatabaseAccessBean;

import javax.ejb.EJB;

import org.glassfish.hk2.classmodel.reflect.util.JarArchive;
import org.jboss.arquillian.protocol.servlet.arq514hack.descriptors.api.application.ApplicationDescriptor;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Objects;

@RunWith(Arquillian.class)
public class PlantDAOTest {

    @Deployment
    public static Archive<?> createDeployment() {
        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.cocome:service-adapter-ejb")
                .withoutTransitivity()
                .as(File.class);

        Assert.assertNotNull(libs);
        Assert.assertTrue(libs.length == 1);

        final JavaArchive ejb = ShrinkWrap.createFromZipFile(JavaArchive.class, libs[0]);

        final WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war")
                //.addClass(DatabaseAccess.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        final ApplicationDescriptor applicationDescriptor = Descriptors.create(ApplicationDescriptor.class)
                .displayName("test-app").version("6")
                .ejbModule(ejb.getName())
                .webModule(testWar.getName(), "/test")
                .libraryDirectory("lib");

        // Embedding war package which contains the test class is needed
        // So that Arquillian can invoke test class through its servlet test runner
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class)
                .setApplicationXML(new StringAsset(applicationDescriptor.exportAsString()))
                .addAsModule(ejb)
                .addAsModule(testWar);
        return ear;
    }

    @EJB(mappedName = "ejb/remote/DatabaseAccess")
    private DatabaseAccess databaseAccess;

    @Test
    public void shouldFindAllGamesUsingJpqlQuery() throws Exception {
        Assert.assertNotNull(databaseAccess);
    }

}
