package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.Item;

/**
 * @author ASHYI
 * @see Caracteristica
 */
public class CaracteristicaImpl implements Caracteristica, Serializable {
	
	private int idCaracteristica;
	private Item idItem;
	private String nombre;
	
	/**
	 *  Constructor base
	 */
	public CaracteristicaImpl() {}
	
	/**
	 * @param idCaracteristica
	 * @param Item (i.e. Estilo Aprendizaje - Habilidad - Competencia - Personalidad - Contexto - Situacion de aprendizaje)
	 * @param nombre de la caracteristica
	 */
	public CaracteristicaImpl(int idCaracteristica, Item Item, String nombre) {
		super();
		this.idCaracteristica = idCaracteristica;
		this.idItem = Item;
		this.nombre = nombre;
	}	
	
	/**
	 * @param Item (i.e. Estilo Aprendizaje - Habilidad - Competencia - Personalidad - Contexto - Situacion de aprendizaje)
	 * @param nombre de la caracteristica
	 */
	public CaracteristicaImpl(Item Item, String nombre) {
		super();
		this.idItem = Item;
		this.nombre = nombre;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Caracteristica#getIdCaracteristica()
	 */
	public int getIdCaracteristica() {
		return idCaracteristica;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Caracteristica#setIdCaracteristica(int)
	 */
	public void setIdCaracteristica(int idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Caracteristica#getNombre()
	 */
	public String getNombre() {
		return nombre;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Caracteristica#setNombre(java.lang.String)
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Caracteristica#getIdItem()
	 */
	public Item getIdItem() {
		return idItem;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Caracteristica#setIdItem(co.edu.javeriana.ASHYI.model.Item)
	 */
	public void setIdItem(Item idItem) {
		this.idItem = idItem;
	}
	
}