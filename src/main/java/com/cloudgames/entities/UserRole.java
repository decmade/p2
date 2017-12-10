package com.cloudgames.entities;


import javax.persistence.*;

import com.cloudgames.entities.interfaces.UserRoleInterface;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@Table(name="user_role")
@JsonAutoDetect
public class UserRole extends AbstractDefinitionEntity implements UserRoleInterface {
	
}
