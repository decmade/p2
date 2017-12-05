package com.cloudgames.entities;


import javax.persistence.*;

import com.cloudgames.entities.interfaces.UserRoleInterface;

@Entity
@Table(name="user_role")
public class UserRole extends AbstractDefinitionEntity implements UserRoleInterface {
	
}
