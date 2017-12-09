package com.cloudgames.services.interfaces;

import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.AccountInterface;

public interface AccountServiceInterface extends ServiceInterface<AccountInterface>{

	AccountInterface fetchByOwner(User u);

}