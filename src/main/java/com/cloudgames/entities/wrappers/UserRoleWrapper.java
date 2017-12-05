package com.cloudgames.entities.wrappers;

import java.util.HashMap;
import java.util.Map;

import com.cloudgames.entities.interfaces.UserRoleInterface;

/**
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public class UserRoleWrapper extends AbstractEntityWrapper<UserRoleInterface> implements UserRoleInterface {

	@Override
	public String getDescription() {
		if ( this.hasSubject() ) {
			return this.subject.getDescription();
		} else {
			return "";
		}
	}

	@Override
	public void setDescription(String description) {
		if ( this.hasSubject() ) {
			this.subject.setDescription(description);
		}
		
	}

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
	public Map<String, Object> getPropertyMap() {
		Map<String, Object> props = new HashMap<>();
		
		props.put("id", this.getId() );
		props.put("description", this.getDescription() );
		
		return props;
	}

}
