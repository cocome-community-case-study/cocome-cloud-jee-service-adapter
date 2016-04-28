package org.cocome.tradingsystem.usermanager.credentials;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlTransient;

import org.cocome.tradingsystem.usermanager.LoginUser;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;

@Entity
@DiscriminatorColumn(name="CREDENTIAL_TYPE")
public abstract class AbstractCredential implements ICredential, Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	private long id;
	
	@Enumerated(EnumType.STRING)
	@Override
	public CredentialType getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setType(CredentialType type) {
		// type is fixed depending on subclass
	}

	@Override
	public boolean isMatching(ICredential credential) {
		// TODO Auto-generated method stub
		return false;
	}

	@Basic
	@Override
	public String getCredentialString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCredentialString(String credential) {
		// TODO Auto-generated method stub

	}

	@Override
	public char[] resetCredential(LoginUser user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
