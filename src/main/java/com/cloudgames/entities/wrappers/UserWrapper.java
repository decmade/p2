package com.cloudgames.entities.wrappers;

import java.util.HashMap;
import java.util.Map;

import com.cloudgames.entities.UserStatus;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.io.Encryption;

/**
 * @author john.w.brown.jr@gmail.com
 *
 */
public class UserWrapper extends AbstractEntityWrapper<UserInterface> implements UserInterface {
	
	@Override
	public int getId() {
		if ( this.hasSubject() ) {
			return this.subject.getId();
		} else {
			return 0;
		}
	}

	@Override
	public void setId(int id) {
		if ( this.hasSubject() ) {
			this.subject.setId(id);
		}
		
	}

	@Override
	public String getIdentity() {
		if ( this.hasSubject() ) {
			return this.subject.getIdentity();
		} else {
			return "";
		}
	}

	@Override
	public void setIdentity(String identity) {
		if ( this.hasSubject() ) {
			this.subject.setIdentity(identity);
		}
		
	}

	@Override
	public String getCredential() {
		if ( this.hasSubject() ) {
			return this.subject.getCredential();
		} else {
			return "";
		}
	}

	@Override
	public void setCredential(String credential) {
		if ( this.hasSubject() ) {
			this.subject.setCredential(credential);
		}
		
	}

	@Override
	public String getSecret() {
		if ( this.hasSubject() ) {
			return this.subject.getSecret();
		} else {
			return "";
		}
	}

	@Override
	public void setSecret(String secret) {
		if ( this.hasSubject() ) {
			this.subject.setSecret(secret);
		}
		
	}

	@Override
	public String getLastName() {
		if ( this.hasSubject() ) {
			return this.subject.getLastName();
		} else {
			return "";
		}
	}

	@Override
	public void setLastName(String lastName) {
		if ( this.hasSubject() ) {
			this.subject.setLastName(lastName);
		}
	}

	@Override
	public String getFirstName() {
		if ( this.hasSubject() ) {
			return this.subject.getFirstName();
		} else {
			return "";
		}
	}

	@Override
	public void setFirstName(String firstName) {
		if ( this.hasSubject() ) {
			this.subject.setFirstName(firstName);
		}
		
	}

	@Override
	public UserStatus getStatus() {
		if ( this.hasSubject() ) {
			return this.subject.getStatus();
		} else {
			return null;
		}
	}

	@Override
	public void setStatus(UserStatus status) {
		if ( this.hasSubject() ) {
			this.subject.setStatus(status);
		}
		
	}

	@Override
	public Map<String, Object> getPropertyMap() {
		Map<String, Object> props = new HashMap<>();
		
		
		props.put("id", this.getId() );
		props.put("identity", this.getIdentity() );
		props.put("lastName", this.getLastName() );
		props.put("firstName", this.getFirstName() );
		props.put("status", this.getStatusPropertyMap() );
		
		return props;
	}
	
	/**
	 * returns true if the credential value passed
	 * matches the one for the user injected
	 * 
	 * @param String credential
	 * 
	 * @return boolean
	 */
	public boolean checkCredential(String credential) {
		if ( this.hasSubject() ) {
			String password = this.getCredential();
			String secret = this.getSecret();
			
			String hash = Encryption.encrypt(credential, secret);
			
			return password.equals( hash );
		} else {
			return false;
		}
	}
	
	private Map<String, Object> getStatusPropertyMap()
	{
		UserStatusWrapper wrapper = new UserStatusWrapper();
		
		wrapper.setSubject( this.getStatus() );
		
		return wrapper.getPropertyMap();
	}

	
}
