package cocome.cloud.sa.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "MessageEntry")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageEntry", propOrder = { "index","content"})
public class MessageEntry {
	
	// **********************************************************************
	// * FIELDS																*
	// **********************************************************************
	
	@XmlAttribute(name="index")
	private int index;
	
	@XmlAttribute(name="name")
	private String name;
	
	@XmlElement(name="Content")
	private String content;
	
	// **********************************************************************
	// * CONSTRUCTORS														*
	// **********************************************************************
	
	public MessageEntry() {}
	
	// **********************************************************************
	// * PUBLIC																*
	// **********************************************************************

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
