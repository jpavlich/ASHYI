package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Item;

/**
 * @author ASHYI
 * @see Item
 */
public class ItemImpl implements Item, Serializable {	
	
	private int idItem;
	private String nombre;
	private String test_definicion; 
	
	/**
	 * Constructor base
	 */
	public ItemImpl() {}
	
	/**
	 * @param idItem
	 * @param nombre
	 * @param test
	 */
	public ItemImpl( int idItem, String nombre, String test) {
		super();
		this.idItem = idItem;
		this.nombre = nombre;
		this.test_definicion = test;
	}
	
	/**
	 * @param nombre
	 * @param test_definicion
	 */
	public ItemImpl(String nombre, String test_definicion) {
		super();
		this.nombre = nombre;
		this.test_definicion = test_definicion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Item#getIdItem()
	 */
	public int getIdItem() {
		return idItem;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Item#setIdItem(int)
	 */
	public void setIdItem(int idItem) {
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
	 * @see co.edu.javeriana.ASHYI.model.Item#getNombre()
	 */
	public String getNombre() {
		return nombre;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Item#setNombre(java.lang.String)
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Item#getTest_definicion()
	 */
	public String getTest_definicion() {
		return test_definicion;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Item#setTest_definicion(java.lang.String)
	 */
	public void setTest_definicion(String test_definicion) {
		this.test_definicion = test_definicion;
	}
	
}