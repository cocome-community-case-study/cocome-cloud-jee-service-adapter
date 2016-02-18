package cocome.cloud.sa.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Message", propOrder = { "header","body"})
public class Message {
	
	public static final String MESSAGE_ENTRY_RESULT = "result";
	
	// **********************************************************************
	// * FIELDS																*
	// **********************************************************************
	
	@XmlElementWrapper(name="Header")
	@XmlElement(name="HeaderEntry")
	private List<MessageEntry> header = new ArrayList<>();
	
	@XmlElementWrapper(name="Body")
	@XmlElement(name="BodyEntry")
	private List<MessageEntry> body = new ArrayList<>();
	
	// **********************************************************************
	// * CONSTRUCTORS														*
	// **********************************************************************
	
	public Message() {}
	
	
	// **********************************************************************
	// * PUBLIC																*
	// **********************************************************************
	
	public List<MessageEntry> getHeader() {
		return this.header;
	}
	
	public void setHeader(List<MessageEntry> header) {
		this.header = header;
	}
	
	public List<MessageEntry> getBody() {
		return this.body;
	}
	
	public void setBody(List<MessageEntry> body) {
		this.body = body;
	}
	
	public void appendHeader(String name, String text){
		MessageEntry entry = new MessageEntry();
		entry.setName(name);
		entry.setIndex(this.header.size());
		entry.setContent(text);
		this.header.add(entry);
	}
	
	public void appendBody(String name, String text){
		MessageEntry entry = new MessageEntry();
		entry.setName(name);
		entry.setIndex(this.body.size());
		entry.setContent(text);
		this.body.add(entry);
	}
	
	@XmlTransient
	public final String getResultBodyContent() {
		for(final MessageEntry nextEntry:this.body) {
			if(nextEntry.getName().equalsIgnoreCase(MESSAGE_ENTRY_RESULT)){
				return nextEntry.getContent();
			}
		}
		return null;
	}

}
