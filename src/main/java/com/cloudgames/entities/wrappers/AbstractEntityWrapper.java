package com.cloudgames.entities.wrappers;

import java.util.Map;

import com.cloudgames.entities.interfaces.EntityWrapperInterface;

/**
 * template for WrapperInterface objects
 * 
 * @author john.w.brown.jr@gmail.com
 *
 * @param <T>
 */
abstract public class AbstractEntityWrapper<T> implements EntityWrapperInterface<T>
{
	protected T subject;
		
	public AbstractEntityWrapper()
	{
		this.subject = null;
	}
	
	@Override
	abstract public Map<String, Object> getPropertyMap();
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void setSubject(T subject) 
	{
		/*
		 * prevent infinite reference loop of setting subject if wrapper
		 * decorates another wrapper of the same type
		 */
		if ( EntityWrapperInterface.class.isInstance(subject) ) {
			this.subject = ((EntityWrapperInterface<T>) subject).getSubject();
		} else {
			this.subject = subject;
		}
	}

	@Override
	public T getSubject() 
	{
		return this.subject;
	}

	@Override
	public boolean hasSubject() 
	{
		return ( this.subject != null );
	}	

}
