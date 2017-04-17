package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Item;

/**
 * @author ASHYI
 * @see Item
 */
public class ItemImpl implements Item, Serializable {	
	
	private Integer idItem;
	private String nombre;
	private String test_definicion; 
	
	/**
	 * Constructor base
	 */
	public ItemImpl() {}
	
	/**
	 * @param idItem
	 * @param nombre
	 * @param test por el que se define el item
	 */
	public ItemImpl( Integer idItem, String nombre, String test) {
		super();
		this.idItem = idItem;
		this.nombre = nombre;
		this.test_definicion = test;
	}
	
	/**
	 * @param nombre
	 * @param test por el que se define el item
	 */
	public ItemImpl(String nombre, String test_definicion) {
		super();
		this.nombre = nombre;
		this.test_definicion = test_definicion;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Item#getIdItem()
	 */
	public Integer getIdItem() {
		return idItem;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Item#setIdItem(java.lang.Integer)
	 */
	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	/**
	 * @param nombre
	 */
	public ItemImpl(String nombre) {
		super();
		this.nombre = nombre;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Item#getNombre()
	 */
	public String getNombre() {
		return nombre;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Item#setNombre(java.lang.String)
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Item#getTest_definicion()
	 */
	public String getTest_definicion() {
		return test_definicion;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Item#setTest_definicion(java.lang.String)
	 */
	public void setTest_definicion(String test_definicion) {
		this.test_definicion = test_definicion;
	}

	@Override
	public void setIdItem(int idItem) {
		// TODO Auto-generated method stub
		
	}
	
}