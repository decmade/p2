package com.cloudgames.dbal;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateSessionUtil {
	
	public static Session getSession() {
	
		return getFactory().openSession();
	}
	
	private static Configuration getConfiguration() {
		Configuration config = new Configuration();
		return config.configure();
	}
	
	private static ServiceRegistry getRegistry() {
		Configuration config = getConfiguration();
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
		
		registryBuilder.applySettings( config.getProperties() );
		
		return registryBuilder.build();
	}
	
	private static SessionFactory getFactory() {
		ServiceRegistry registry = getRegistry();
		Configuration config = getConfiguration();
		
		return config.buildSessionFactory( registry );
	}
	
	
	

}
