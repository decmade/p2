package com.cloudgames.services.interfaces;

import com.cloudgames.entities.interfaces.UserInterface;

public interface UserServiceInterface extends ServiceInterface<UserInterface>{

	UserInterface fetchByIdentity(String identity);

}
