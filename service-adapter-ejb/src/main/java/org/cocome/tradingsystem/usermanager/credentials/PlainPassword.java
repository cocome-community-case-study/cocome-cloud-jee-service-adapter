package org.cocome.tradingsystem.usermanager.credentials;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;

@Entity
public class PlainPassword extends AbstractCredential implements Serializable {
	private static final long serialVersionUID = -5536507566362794379L;

	private static final CredentialType TYPE = CredentialType.PASSWORD;

	private String password;

	@Override
	public boolean isMatching(ICredential credentials) {
		return credentials != null && password.equals(credentials.getCredentialString());
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

	}
