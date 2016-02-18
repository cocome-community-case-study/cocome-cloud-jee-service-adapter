package de.kit.ipd.java.utils.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.w3c.dom.Node;

import de.kit.ipd.java.utils.xml.XML.XMLOption;
import de.kit.ipd.java.utils.xml.XML.XMLVisitor;

public class TestingXML extends XMLVisitor{
	
	public static void main(String[] args) throws FileNotFoundException {
		TestingXML test = new TestingXML();
		XML xml = XML.valueOf(new FileInputStream("testing/testing.xml"));
		xml.walkXML(XMLOption.DEPTH_FIRST_SEARCH,"/", test);
	}

	
	@Override
	protected XMLOption visitNode(Node node) {
		System.out.println(node.getNodeName());
		return XMLOption.CONTINUE;
	}
	
//	@Override
//	protected XMLOption visitNode(String content) {
//		System.out.println(content);
//		return XMLOption.CONTINUE;
//	}
}
