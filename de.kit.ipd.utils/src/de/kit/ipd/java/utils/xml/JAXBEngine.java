package de.kit.ipd.java.utils.xml;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;

/**
 * A JAXB-Framework layer to persist and unpersist xml based on schema
 * 
 * @author alessandrogiusa@gmail.com
 * 
 */
public class JAXBEngine {

	/** if debug, all System.out.println will be invoked */
	boolean debug = false;

	/**
	 * Turn the debug on
	 */
	public void debugON() {
		this.debug = true;
	}

	/**
	 * Turn the debug off
	 */
	public void debugOFF() {
		this.debug = false;
	}

	/**
	 * Resolve the {@link SchemaOutputResolver}
	 * 
	 * @author alessandro.giusa@valeo.com, SBX1322
	 * 
	 */
	private class SchemaFileResolver extends SchemaOutputResolver {

		private String schemaName;
		private String directory;

		/**
		 * Create an instance of a {@link SchemaFileResolver}
		 * 
		 * @param directory
		 *            parent path of schema name
		 * @param schemaName
		 *            only the filename of the schema which should be created
		 *            with the extension .xsd
		 */
		public SchemaFileResolver(String directory, String schemaName) {
			super();
			if (!schemaName.contains(".")) {
				this.schemaName += ".xsd";
			} else {
				this.schemaName = schemaName;
			}

			if (directory == null || directory.isEmpty()) {
				this.directory = ".";
			} else {
				this.directory = directory;
			}
		}

		@Override
		public Result createOutput(String namespaceUri, String suggestedFileName)
				throws IOException {
			File file = new File(this.directory, this.schemaName);
			StreamResult result = new StreamResult(file);
			result.setSystemId(file.toURI().toURL().toString());
			if (debug)
				System.out.println(file.toURI().toURL().toString());
			return result;
		}

		/**
		 * Get the schema file
		 * 
		 * @return
		 */
		public File schemaFile() {
			return new File(this.directory, this.schemaName);
		}
	}

	/**
	 * This implementation of {@link SchemaOutputResolver} is only for virtual
	 * working, where the schema is not needed afterwards
	 * 
	 * @author alessandro.giusa@valeo.com, SBX1322
	 * 
	 */
	public class SchemaStringBufferResolver extends SchemaOutputResolver {

		private StringWriter buffer;

		@Override
		public Result createOutput(String namespaceUri, String suggestedFileName)
				throws IOException {
			buffer = new StringWriter();
			StreamResult result = new StreamResult(buffer);
			result.setSystemId("");
			return result;
		}

		public StringWriter getBuffer() {
			return buffer;
		}
	}

	/**
	 * Get new instance of {@link JAXBEngine}. Client should take care of the
	 * instace
	 * 
	 * @return
	 */
	public static JAXBEngine getInstance() {
		return new JAXBEngine();
	}

	/** instance of this class, Singleton */
	private static JAXBEngine INSTANCE;

	/**
	 * Get an instance of the JAXBEngine ->Singleton. Each call of this method,
	 * will bring the same object
	 * 
	 * @return
	 */
	public static JAXBEngine getSingleTon() {
		if (INSTANCE == null) {
			INSTANCE = new JAXBEngine();
		}
		return INSTANCE;
	}

	/**
	 * Kill the internal instance
	 */
	public static void kill() {
		INSTANCE = null;
	}

	private JAXBEngine() {
	}

	/**
	 * Create a {@link SchemaFileResolver#SchemaResolver(String, String)}
	 * 
	 * @param namespaceUri
	 * @param suggestedFileName
	 * @return
	 */
	private SchemaFileResolver getSchemaRessolver(String namespaceUri,
			String suggestedFileName) {
		SchemaFileResolver schemaRes = new SchemaFileResolver(namespaceUri,
				suggestedFileName);
		return schemaRes;
	}

	/**
	 * Write the obj to xml by using the given schema
	 * 
	 * @param obj
	 *            object which should be persist to xml
	 * @param clazz
	 *            class of the obj
	 * @param fileSchema
	 *            whole path to the schema which should be used
	 * @param outputFilename
	 *            output file name
	 * @return true if the write process was okay
	 */
	public boolean write(Object obj, File fileSchema, String outputFilename,
			Class... classes) {
		try {
			this.checkFiles(fileSchema);
			JAXBContext context = JAXBContext.newInstance(classes);
			Marshaller ms = context.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			if (debug)
				System.out.println(fileSchema.getAbsolutePath());
			Schema schema = sf.newSchema(fileSchema);
			ms.setSchema(schema);
			ms.marshal(obj, new FileOutputStream(outputFilename, false));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Write the given classes into the provided node. A schema file will be
	 * generated temperately in the location of eclipse platform with the name
	 * tempschema.xsd
	 * 
	 * @param obj
	 * @param node
	 * @param classes
	 * @return true, if the write process was successfully
	 */
	public boolean write(Object obj, File fileSchema, Node node,
			Class... classes) {
		try {
			this.checkFiles(fileSchema);
			JAXBContext context = JAXBContext.newInstance(classes);
			Marshaller ms = context.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(fileSchema);
			ms.setSchema(schema);
			ms.marshal(obj, node);
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Write the given classes into the provided node. A schema file will be
	 * generated temperately in the location of eclipse platform with the name
	 * tempschema.xsd
	 * 
	 * @param obj
	 * @param node
	 * @param classes
	 * @return true, if the write process was successfully
	 */
	public boolean write(Object obj, Node node, Class... classes) {
		try {
			JAXBContext context = JAXBContext.newInstance(classes);
			Marshaller ms = context.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			ms.setSchema(this.createSchema(context));
			ms.marshal(obj, node);
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Get the given object based on the classes back as {@link XML}
	 * 
	 * @param obj
	 * @param classes
	 * @return
	 */
	public XML write(Object obj, Class... classes) {
		try {
			JAXBContext context = null;
			if (classes == null || classes.length == 0) {
				context = JAXBContext.newInstance(obj.getClass());
			} else {
				context = JAXBContext.newInstance(classes);
			}

			Marshaller ms = context.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			ms.setSchema(this.createSchema(context));
			XML xml = XML.newInstance();
			ms.marshal(obj, xml.getDocument());
			return xml;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write the object to the console
	 * 
	 * @param obj
	 * @param node
	 * @param classes
	 * @return true, if the write process was successfully
	 */
	public boolean write(Object obj, OutputStream out, Class... classes) {
		try {
			JAXBContext context = JAXBContext.newInstance(classes);
			Marshaller ms = context.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			ms.setSchema(this.createSchema(context));
			ms.marshal(obj, out);
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Write the object to the console
	 * 
	 * @param obj
	 * @param node
	 * @param classes
	 * @return true, if the write process was successfully
	 */
	public boolean write(Object obj, Writer writer, Class... classes) {
		try {
			JAXBContext context = JAXBContext.newInstance(classes);
			Marshaller ms = context.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			ms.setSchema(this.createSchema(context));
			ms.marshal(obj, writer);
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Read the xml passed as string
	 * 
	 * @param xml
	 *            string with xml
	 * @param classes
	 * @return
	 */
	public Object read(String xml, Class... classes) {
		if (xml != null && !xml.isEmpty()) {
			try {
				JAXBContext context = JAXBContext.newInstance(classes);
				Unmarshaller unm = context.createUnmarshaller();
				unm.setSchema(this.createSchema(context));
				return unm.unmarshal(new StringReader(xml));
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Read the xml passed as string
	 * 
	 * @param xml
	 *            string with xml
	 * @param classes
	 * @return
	 */
	public Object read(File xml, Class... classes) {
		if (xml != null && xml.exists()) {
			try {
				JAXBContext context = JAXBContext.newInstance(classes);
				Unmarshaller unm = context.createUnmarshaller();
				unm.setSchema(this.createSchema(context));
				return unm.unmarshal(xml);
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Read xml to object of type which is passed in by using the passed schema
	 * 
	 * @param filexml
	 *            xml which should be get into object
	 * @param fileSchema
	 *            schema of filexml
	 * @param type
	 *            type of the object which should be created
	 * @return the main object based on xml and schema
	 */
	public Object read(File filexml, File fileSchema, Class... classes) {
		try {
			this.checkFiles(filexml, fileSchema);
			JAXBContext context = JAXBContext.newInstance(classes);
			Unmarshaller unm = context.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			if (debug)
				System.out.println(fileSchema.getAbsolutePath());
			Schema schema = sf.newSchema(fileSchema);
			unm.setSchema(schema);
			Object obj = unm.unmarshal(filexml);
			return obj;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Check whether the files are available or not
	 * 
	 * @param files
	 * @throws FileNotFoundException
	 */
	private void checkFiles(File... files) throws FileNotFoundException,
			NullPointerException {
		if (files != null && files.length > 0) {
			File[] myFiles = files.clone();
			for (File itrFile : myFiles) {
				if (!itrFile.exists()) {
					throw new FileNotFoundException("The file:"
							+ itrFile.getAbsolutePath() + " is not available?");
				}
			}
		} else {
			throw new NullPointerException("the given files are null or empty");
		}
	}

	/**
	 * Create a schema file based on clazz passed in. The schema consider all
	 * complex types in the class. The whole hierarchy
	 * 
	 * @param parentPath
	 *            parent path of schema. if "" or null, the current place of
	 *            thread is taken
	 * @param schemaName
	 *            name for the schema file
	 * @param clazz
	 *            the class which from what the schema should be generated.
	 *            Normally the top-level class
	 * @return the file of the schema, this can be used as input for other
	 *         methods of this class
	 */
	public File createSchema(String parentPath, String schemaName,
			Class... classes) {
		try {
			JAXBContext context = JAXBContext.newInstance(classes);
			SchemaFileResolver sr = getSchemaRessolver(parentPath, schemaName);
			context.generateSchema(sr);
			return sr.schemaFile();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create a {@link Schema} file in a virtual {@link StringWriter} /
	 * {@link StringReader}
	 * 
	 * @param context
	 * @return
	 */
	public Schema createSchema(JAXBContext context) {
		try {
			SchemaStringBufferResolver sr = new SchemaStringBufferResolver();
			context.generateSchema(sr);
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			StringWriter writer = sr.getBuffer();
			StringReader reader = new StringReader(writer.getBuffer()
					.toString());
			StreamSource source = new StreamSource(reader);
			Schema schema = sf.newSchema(source);
			return schema;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generate java-files based on the passed schema into
	 * 
	 * @param packagename
	 * @param destdir
	 * @param fileschema
	 * @return
	 */
	public boolean generateClasses(String packagename, File destdir,
			File fileschema) {
		if (destdir.exists() && fileschema.exists()) {
			if (debug)
				System.out.println("creating files in directory "
						+ destdir.getAbsoluteFile() + " whith schema "
						+ fileschema.getAbsolutePath());
			try {
				String command = "xjc -p " + packagename + " -d "
						+ destdir.getAbsolutePath() + " "
						+ fileschema.getAbsolutePath();
				if (debug)
					System.out.println("com: " + command);
				Runtime.getRuntime().exec(command);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		if (debug)
			System.out.println("directory " + destdir.getAbsoluteFile()
					+ " OR schema " + fileschema.getAbsolutePath()
					+ " doesn't exist!");
		return false;
	}
}
