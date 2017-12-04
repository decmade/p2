package com.cloudgames.entities.interfaces;

import java.util.Map;

/**
 * << decorator >>
 * 
 * interface for bean object wrapper classes
 * 
 * @author john.w.brown.jr@gmail.com
 *
 * @param <T>
 */
public interface EntityWrapperInterface<T>
{
	/**
	 * set the object to decorate 
	 * 
	 * @param T subject
	 * 
	 */
	public void setSubject(T subject);
	
	/**
	 * retrieve the decorated object
	 * 
	 * @return T
	 */
	public T getSubject();
	
	/**
	 * return true of the wrapper has a subject
	 * set, fasle if now
	 * 
	 * @return boolean
	 */
	public boolean hasSubject();
	
	/**
	 * returns as a Map<String, Object> that contains
	 * the key->value pairs of the object.
	 * 
	 * this is fed to the Jackson library for creating
	 * JSON data
	 * 
	 * this allows you to specify what data is encoded
	 * in the JSON string and how when you convert an
	 * object's properties to JSON
	 * 
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getPropertyMap();
}
