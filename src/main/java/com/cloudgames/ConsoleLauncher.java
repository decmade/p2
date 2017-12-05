package com.cloudgames;

import org.hibernate.Session;

import com.cloudgames.dbal.HibernateSessionUtil;
import com.cloudgames.entities.User;

public class ConsoleLauncher {
	public static void main(String[] args) {
		Session session = HibernateSessionUtil.getSession();
		
		User user = new User();
		user.setLastName("Brown");
		user.setFirstName("John");
		
		session.persist(user);
	}
}
