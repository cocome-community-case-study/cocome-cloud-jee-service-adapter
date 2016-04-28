package org.cocome.tradingsystem.usermanager.credentials;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.cocome.tradingsystem.usermanager.LoginUser;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;

@Entity
@XmlRootElement(name="Password")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Password", propOrder = { "password"})
public class PlainPassword extends AbstractCredential implements Serializable {
	private static final long serialVersionUID = -5536507566362794379L;

	//	private final static Logger LOG = Logger.getLogger(PlainPassword.class);
	@XmlTransient
	private static final CredentialType TYPE = CredentialType.PASSWORD;
	
	@XmlElement(name="password")
	private String password;

	
//	@PostConstruct
//	public void initPassword() {
//		resetCredential(null);
//	}

	@Override
	public boolean isMatching(ICredential credentials) {
		if (credentials == null) return false;
//		LOG.debug("Comparing " + password.toString() + " with " 
//				+ credentials.getCredentialString());
		
		boolean result = password.equals(credentials.getCredentialString());
//		LOG.debug("Result was " + result);
		return result;
	}

	@Basic
	@Override
	public String getCredentialString() {
		return password;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="CREDENTIAL_TYPE")
	@Override
	public CredentialType getType() {
		return TYPE;
	}

	public void setType(CredentialType type) {
		// type is fixed
	}
	
	@Override
	public void setCredentialString(String credential) {
		password = credential;
	}

	@Override
	public char[] resetCredential(LoginUser user) {
		password = new String(RandomPasswordGenerator.generatePassword());
		return password.toCharArray();
	}
}
