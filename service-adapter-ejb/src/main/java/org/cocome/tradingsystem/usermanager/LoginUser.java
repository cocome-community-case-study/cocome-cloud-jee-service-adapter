package org.cocome.tradingsystem.usermanager;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;

import org.cocome.tradingsystem.usermanager.credentials.AbstractCredential;
import org.cocome.tradingsystem.usermanager.datatypes.CredentialType;
import org.cocome.tradingsystem.usermanager.datatypes.Role;

@Entity
public class LoginUser implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String username;
	private Map<CredentialType, AbstractCredential> credentials = new LinkedHashMap<>((int) (CredentialType.SIZE / 0.75));
	private Set<Role> roles = new LinkedHashSet<>((int) (Role.SIZE / 0.75));
	
	@Basic
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setCredentials(Map<CredentialType, AbstractCredential> credentials) {
		this.credentials = credentials;
	}

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@MapKeyEnumerated(EnumType.STRING)
	public Map<CredentialType, AbstractCredential> getCredentials() {
		return credentials;
	}
	
	@ElementCollection(fetch=FetchType.EAGER)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		if (roles != null) {
			this.roles = roles;
		}
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "[User: " + id + ":" + username + "]";
	}
}
