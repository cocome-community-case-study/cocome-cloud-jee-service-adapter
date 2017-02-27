# CoCoME Cloud JEE Service-Adapter

This repository contains the service-adapter implementing the evolution
scenario adding a service-adapter.

## Prerequisites

- Running Glassfish 4.x
- A database supported by JPA (e.g., PostgreSQL)

## Compilation

Before compilation you must copy the `settings.xml.template` located in
the project root directory file to `settings.xml` and adjust the
properties defined in the XML file.

The properties correspond with values of your running Glassfish
instance. In a default Glassfish installation the domain is called
`domain1` if you did not provide a different name. However, we recommend
to create your own domain, e.g., `cocome` or `service-adapter`. 

To build the service-adapter with Maven type in the project root
directory:

`mvn -s settings.xml compile`

If the compilation is successful you can proceed and type

`mvn -s settings.xml package`

to produce deployable packages. This last step can be omitted when you
choose to directly deploy your build with Maven. However, it is helpful
to make sure packaging works before trying to deploy. Therefore, we
recommend to test packaging before deploying.

## Deployment

Before you can deploy the service-adapter make sure you have added a
data (re)source called `jdbc/CoCoMEDB`. This can be done on command line
or via the Glassfish UI. You need:
- A JDBC connection pool (please consult the [Glassfish documentation]
(https://glassfish.java.net/docs/4.0/administration-guide.pdf))
- A JDBC resource named `jdbc/CoCoMEDB` using the connection pool you
  defined
  
 - JDBC connection pool (default jdcb-pool (DerbyPool) is automatically 
    created when 'asadmin start-database' was executed for the first time.
 - JDBC ressource:  'asadmin create-jdbc-resource --connectionpoolid DerbyPool 
     --host localhost --port 8248  jdbc/CoCoMEDB'
   Notice that port 8248 is the admin port of the adapter-domain. So when
   you did not use the same port you have to change it!

- Make sure you refer to the same domain for `service adapter` in this setting file and in the setting file of jee-plattform-migration.

You can directly deploy them via the Cargo Maven plug-in with

`mvn -s settings.xml install`

Alternatively, you can deploy the application via `asadmin` or the
Glassfish administration interface.

## Undeployment

Undeployment can be performed via the Glassfish administration
interface the `asadmin` command line tool or with

`mvn -s settings.xml post-clean`

## Development

Before you start to contribute to the service-adapter make sure you
adhere the coding style of the service-adapter and setup your IDE
accordingly. For Eclipse, we provide configuration files in the
root directory.
- `eclipse-cleanup.xml` 
- `eclipse-formatter.xml`
- `eclipse.importorder`
You can use them globally in preferences or configure them in each
project. 

Furthermore, we provide a `checkstyle.xml` configuration, which can
be used with Maven and in your IDE.

`mvn -s settings.xml checkstyle:check`

This will produce also errors in existing code. We are aware of that
and will fix in over time.

For your Eclipse setup you may use the Kieker Eclipse setup page as
reference (https://kieker.uni-kiel.de/trac/wiki/KiekerCodingConventionSetup)

## Troubleshooting

In case the deployment fails, this might be due to a wide range of
problems. You may consult the server logs and your settings file.

- Make sure admin user name and password are correct and that the ports
  are set properly. Depending on your installation they might differ
  from 4848 (admin) and 8080 (web).
- In case you deployed before, redeployment will fail until you undeploy
  first. This can be done with
  `mvn -s settings.xml post-clean install`
  which undeploys and then tries to deploy the application again
- If undeployment does not work. Try undeploying with the Glassfish
  administration interface or `asadmin`. In seldom cases Glassfish does
  not delete all necessary files for undeployment. You may repair this
  as follows:
  - stop your Glassfish server
  - go to `${GLASSFISH_HOME}/glassfish/domains/${YOUR_DOMAIN}` directory
  - delete all directories which correspond with your deployment
  - You must also check the `__internal` directory
  - Find the `domain.xml` or `config.xml` and remove references to the
    deployment.








