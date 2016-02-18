package de.kit.ipd.java.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XML {
	
	
	
	/*************************************************************************
	 * LOCAL CLASS
	 ************************************************************************/
	
	public class XMLNotification extends Throwable {
		private static final long serialVersionUID = 1L;
		
		public static final String SUCCESSFULLY_FINISHED = "exception.successfully.finished";
		
		public XMLNotification(String string) {
			super(string);
		}
	}
	
	
	public enum XMLOption{
		CONTINUE,
		BREAK,
		SKIP,
		BREADTH_FIRST_SEARCH,
		DEPTH_FIRST_SEARCH;
	}
	
	
	/**
	 * 
	 * @author AlessandroGiusa@gmail.com
	 *
	 */
	public static abstract class XMLVisitor{
		
		protected XMLOption visitNode(Node node){
			return XMLOption.CONTINUE;
		}
		
		protected XMLOption visitNode(String content){
			return XMLOption.CONTINUE;
		}
		
		protected XMLOption visitAttr(Node node){
			return XMLOption.CONTINUE;
		}
		
		protected XMLOption visitAttr(String content){
			return XMLOption.CONTINUE;
		}
		
	}
	
	
	/**************************************************************************
	 * STATIC
	 *************************************************************************/
	public final static XML valueOf(InputStream input) {
		try {
			if(input!=null && input.available()!=0){
				DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
				df.setNamespaceAware(true);
				DocumentBuilder docBuilder = df.newDocumentBuilder();
				return new XML(docBuilder.parse(input));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public final static XML valueOf(String xmlexpression) {
		try {
			StringReader reader = new StringReader(xmlexpression);
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			df.setNamespaceAware(true);
			DocumentBuilder docBuilder = df.newDocumentBuilder();
			return new XML(docBuilder.parse(new InputSource(reader)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static XML valueOf(NodeList list, String rootName) {
		XML temp = XML.valueOf("<"+rootName+"/>");
		Node root = temp.getRootNode();
		for (int i = 0; i < list.getLength(); i++) {
			temp.attachNode(list.item(i),root);
		}
		return temp;
	}
	
	public static XML newInstance(){
		try {
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			df.setNamespaceAware(true);
			DocumentBuilder docBuilder = df.newDocumentBuilder();
			return new XML(docBuilder.newDocument());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Transform the given XML object using the given XSL transformation XML
	 * object.
	 * 
	 * @param from
	 * @param xsl
	 * @return String with the transformation
	 */
	public static String transform(XML from, XML xsl){
		try {
			Document document = from.getDocument();
			StringReader xslReader = new StringReader(xsl.toString());
			
			DOMSource src = new DOMSource(document);
			StringWriter output = new StringWriter();
			StreamResult result = new StreamResult(output);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer(new StreamSource(xslReader));
			t.transform(src, result);
			return output.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**************************************************************************
	 * FIELDS
	 *************************************************************************/
	
	private Document doc;
	
	/**************************************************************************
	 * CONSTRUCTOR
	 *************************************************************************/
	
	private XML() {}
	
	private XML(Document document){
		this.doc = document;
	}
	
	/**************************************************************************
	 * PUBLIC
	 *************************************************************************/
	
	public final Document getDocument(){
		return this.doc;
	}
	
	public final Object search(String xpathexpression, Node node,
			QName xpathconstants) {
		try {
			final XPathFactory xpathfac = XPathFactory.newInstance();
			final XPath path = xpathfac.newXPath();
			return path.evaluate(xpathexpression, node,
					xpathconstants);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public final XML filter(String xpathExpression, String rootNewXML){
		if(xpathExpression==null || xpathExpression.isEmpty()){
			throw new IllegalArgumentException("param xpathExpression is either" +
					" null or empty");
		}
		final NodeList list = (NodeList) 
				search(xpathExpression, doc, XPathConstants.NODESET);
		return XML.valueOf(list, rootNewXML);
	}
	
	public final Node getNode(String xpathExpression){
		if(xpathExpression==null || xpathExpression.isEmpty()){
			throw new IllegalArgumentException("param xpathExpression is either" +
					" null or empty");
		}
		return (Node) search(xpathExpression, doc, XPathConstants.NODE);
	}
	
	
	public final void write(OutputStream out) {
		try {
			final TransformerFactory tf = TransformerFactory.newInstance();
			final Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
			final DOMSource src = new DOMSource(doc);
			final StreamResult result = new StreamResult(out);
			transformer.transform(src, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Walk through the XML-file using visitor-pattern approach
	 * 
	 * @param start
	 *            use XPath to get the starting point of the walk through. The
	 *            starting point should be a node not a collection of node
	 * @param visitors
	 */
	public final void walkXML(XMLOption strategy, String start, XMLVisitor...visitors){
		Node starting = getNode(start);
		if (starting.getNodeType() == Node.DOCUMENT_NODE) {
			starting = starting.getFirstChild().getFirstChild();
		}
		if (starting != null) {
			switch (strategy) {
			case BREADTH_FIRST_SEARCH:
				walkRecursivlyBreadthFirstSearch(new ArrayList<Node>(),starting, visitors);
				break;
			case DEPTH_FIRST_SEARCH:
				try {
					walkRecursivlyDepthFirstSearch(starting, starting, visitors);
				} catch (XMLNotification e) {
					if(!e.getMessage().equals(XMLNotification.SUCCESSFULLY_FINISHED)){
						e.printStackTrace();
					}
				}
				break;
			default:
				throw new IllegalArgumentException("passed invalide strategy");
			}
			
		} else {
			// TODO waring, that first node could not be found
		}
	}
	
	@Override
	public String toString() {
		String output = null;
		if (doc == null)
			return "";
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(
					writer));
			output = writer.getBuffer().toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return output;
	}
		
	/**************************************************************************
	 * PRIVATE
	 *************************************************************************/
	
	private final Node getRootNode(){
		return getNode("/*"); //$NON-NLS-1$
	}
	
	/**
	 * Attach the Node n1 to the document
	 * @param n1
	 * @param document
	 * @throws DOMException
	 */
	private final void attachNode(Node n1,Node n2){
		try {
			Node myNode = n1.cloneNode(true);
			myNode = doc.adoptNode(myNode);
			if (myNode != null) {
				n2.appendChild(myNode);
			}
		} catch (DOMException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Walk through the document starting from the given start {@link Node}.
	 * Default strategy is a broad search algorithm.
	 * 
	 * @param start
	 * @param visitors
	 */
	private void walkRecursivlyBreadthFirstSearch(List<Node>visitedNodes, Node node,XMLVisitor...visitors){
		if(node!=null) {
			XMLOption option = XMLOption.CONTINUE;
			for (XMLVisitor visitor : visitors) {
				try {
					switch (node.getNodeType()) {
					case Node.ATTRIBUTE_NODE:
						option = visitor.visitAttr(node);
						if(option!=XMLOption.BREAK && !node.getTextContent().isEmpty()){
							option = visitor.visitAttr(node.getTextContent());
						}
						visitedNodes.add(node);
						break;
					case Node.ELEMENT_NODE:
						option = visitor.visitNode(node);
						if(option!=XMLOption.BREAK && !node.getTextContent().isEmpty()){
							option = visitor.visitNode(node.getTextContent());
						}
						visitedNodes.add(node);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				switch (option) {
				case CONTINUE:
					// do nothing
					break;
				case BREAK:
					//end with the recursive walk through
					return;
				case SKIP:
					//TODO no functionality yet
					break;
				}
			}
			walkRecursivlyBreadthFirstSearch(visitedNodes,node.getNextSibling(), visitors);
		}
		//here there is no sibling any more
		if(visitedNodes.size() > 0){
			int len = visitedNodes.size()-1;
			for (int i = 0; i <= len; i++) {
				if(visitedNodes.get(i).hasChildNodes()){
					Node n = visitedNodes.get(i).getFirstChild();
					visitedNodes.remove(0);
					walkRecursivlyBreadthFirstSearch(visitedNodes, n, visitors);
					break;
				}
			}
		}
	}
	
	private void walkRecursivlyDepthFirstSearch(Node start, Node node,XMLVisitor...visitors) throws XMLNotification{
		if(node!=null) {
			XMLOption option = XMLOption.CONTINUE;
			for (XMLVisitor visitor : visitors) {
				try {
					switch (node.getNodeType()) {
					case Node.ATTRIBUTE_NODE:
						option = visitor.visitAttr(node);
						if(option!=XMLOption.BREAK && !node.getTextContent().isEmpty()){
							option = visitor.visitAttr(node.getTextContent());
						}
						break;
					case Node.ELEMENT_NODE:
						option = visitor.visitNode(node);
						if(option!=XMLOption.BREAK && !node.getTextContent().isEmpty()){
							option = visitor.visitNode(node.getTextContent());
						}
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				switch (option) {
				case CONTINUE:
					// do nothing
					break;
				case BREAK:
					//end with the recursive walk through
					return;
				case SKIP:
					//TODO no functionality yet
					break;
				}
			}
			walkRecursivlyDepthFirstSearch(node, node.getFirstChild(), visitors);
		}
		//here there is no sibling any more
		Node nextSibling = null;
		while((nextSibling = start.getNextSibling())==null){
			start = start.getParentNode();
			if(start.getNodeType()==Node.DOCUMENT_NODE){
				throw new XMLNotification(XMLNotification.SUCCESSFULLY_FINISHED);
			}
		}
		if(nextSibling!=null){
			walkRecursivlyDepthFirstSearch(start, nextSibling, visitors);
		}
	}
	
}
