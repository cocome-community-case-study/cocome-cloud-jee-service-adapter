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

/**
 *
 * @author unknown
 *
 */
@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Message", propOrder = { "header", "body" })
public class Message {

	private static final String MESSAGE_ENTRY_RESULT = "result";

	// **********************************************************************
	// * FIELDS *
	// **********************************************************************

	@XmlElementWrapper(name = "Header")
	@XmlElement(name = "HeaderEntry")
	private List<MessageEntry> header = new ArrayList<>();

	@XmlElementWrapper(name = "Body")
	@XmlElement(name = "BodyEntry")
	private List<MessageEntry> body = new ArrayList<>();

	// **********************************************************************
	// * CONSTRUCTORS *
	// **********************************************************************

	/** Empty constructor. */
	public Message() {}

	/**
	 * Add an header entry and set the index ot the header size.
	 * This is an elaborate construct to increment the index.
	 *
	 * @param name
	 *            entry name
	 * @param text
	 *            entry content
	 */
	public void appendHeader(final String name, final String text) {
		final MessageEntry entry = new MessageEntry();
		entry.setName(name);
		entry.setIndex(this.header.size());
		entry.setContent(text);
		this.header.add(entry);
	}

	/**
	 * Add an body entry and set the index ot the header size.
	 * This is an elaborate construct to increment the index.
	 *
	 * @param name
	 *            entry name
	 * @param text
	 *            entry content
	 */
	public void appendBody(final String name, final String text) {
		final MessageEntry entry = new MessageEntry();
		entry.setName(name);
		entry.setIndex(this.body.size());
		entry.setContent(text);
		this.body.add(entry);
	}

	/**
	 * Get result part of message.
	 *
	 * @return result message as string
	 */
	@XmlTransient
	public final String getResultBodyContent() {
		for (final MessageEntry nextEntry : this.body) {
			if (nextEntry.getName().equalsIgnoreCase(MESSAGE_ENTRY_RESULT))
				return nextEntry.getContent();
		}
		return null;
	}

}
