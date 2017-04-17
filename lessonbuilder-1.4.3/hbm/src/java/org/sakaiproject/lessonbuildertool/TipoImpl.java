package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo;
import org.sakaiproject.lessonbuildertool.model.Tipo;

/**
 * @author ASHYI
 * @see Tipo
 */

public class TipoImpl implements Tipo, Serializable {	
	
	private Integer idTipo;
	private String nombre;
	
	private List<CaracteristicasTipo> caracteristicas;
	
	/**
	 * Constructor base
	 */
	public TipoImpl() {}
	
	/**
	 * @param idTip
	 * @param nombre
	 */
	public TipoImpl( int idTip, String nombre) {
		super();
		this.idTipo = idTip;
		this.nombre = nombre;
		this.caracteristicas = new ArrayList<CaracteristicasTipo>();
	}
	
	/**
	 * @param nombre
	 */
	public TipoImpl(String nombre) {
		super();
		this.nombre = nombre;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Tipo#getIdTipo()
	 */
	public Integer getIdTipo() {
		return idTipo;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Tipo#setIdTipo(java.lang.Integer)
	 */
	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Tipo#getNombre()
	 */
	public String getNombre() {
		return nombre;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Tipo#setNombre(java.lang.String)
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return
	 */
	public List<CaracteristicasTipo> getCaracteristicas() {
		return caracteristicas;
	}

	/**
	 * @param caracteristicas
	 */
	public void setCaracteristicas(List<CaracteristicasTipo> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	
	/**
	 * @param caracteristica
	 */
	public void addCaracteristica(CaracteristicasTipo caracteristica)
	{
		this.caracteristicas.add(caracteristica);
	}
}