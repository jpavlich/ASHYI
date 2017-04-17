package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.Item;

/**
 * @author ASHYI
 * @see Caracteristica
 */

public class CaracteristicaImpl implements Caracteristica, Serializable {
	
	private int idCaracteristica;
	private Item idItem;
	private String nombre;
	
	public CaracteristicaImpl() {}
	
	public CaracteristicaImpl(int idCaracteristica, Item Item, String nombre) {
		super();
		this.idCaracteristica = idCaracteristica;
		this.idItem = Item;
		this.nombre = nombre;
	}	
	
	public CaracteristicaImpl(Item Item, String nombre) {
		super();
		this.idItem = Item;
		this.nombre = nombre;
	}
	
	public int getIdCaracteristica() {
		return idCaracteristica;
	}
	public void setIdCaracteristica(int idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Item getIdItem() {
		return idItem;
	}

	public void setIdItem(Item idItem) {
		this.idItem = idItem;
	}
	
}