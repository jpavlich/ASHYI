package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.lessonbuildertool.model.Objetivo;
import org.sakaiproject.lessonbuildertool.model.PalabraClave;

/**
 * @author ASHYI
 * @see Objetivo
 */

public class ObjetivoImpl implements Objetivo, Serializable {
	
	private Integer idObjetivo;
	private String nombre;
	private List<PalabraClave> palabras;
	
	/**
	 * Constructor base
	 */
	public ObjetivoImpl() {
	}
	
	/**
	 * @param idObjetivo
	 * @param nombre (palabras clave concatenadas)
	 * @param palabras clave que conforman el objetivo
	 */
	public ObjetivoImpl(Integer idObjetivo, String nombre, List<PalabraClave> palabras) {
		super();
		this.idObjetivo = idObjetivo;
		this.nombre = nombre;
		this.palabras = palabras;
	}


	/**
	 * @param idObjetivo
	 * @param nombre (palabras clave concatenadas)
	 */
	public ObjetivoImpl(Integer idObjetivo, String nombre) {
		super();
		this.idObjetivo = idObjetivo;
		this.nombre = nombre;
		this.palabras = new ArrayList<PalabraClave>();
	}
	
	/**
	 * @param nombre (palabras clave concatenadas)
	 */
	public ObjetivoImpl(String nombre) {
		super();
		this.nombre = nombre;
		this.palabras = new ArrayList<PalabraClave>();
	}
	
	/**
	 * @param palabra clave a aniadir al objetivo
	 */
	public void addPalabras(PalabraClave palabra)
	{
		getPalabras().add(palabra);
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Objetivo#getIdObjetivo()
	 */
	public Integer getIdObjetivo() {
		return idObjetivo;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Objetivo#setIdObjetivo(java.lang.Integer)
	 */
	public void setIdObjetivo(Integer idObjetivo) {
		this.idObjetivo = idObjetivo;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Objetivo#getNombre()
	 */
	public String getNombre() {
		return nombre;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Objetivo#setNombre(java.lang.String)
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Objetivo#getPalabras()
	 */
	public List<PalabraClave> getPalabras() {
		return palabras;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Objetivo#setPalabras(java.util.List)
	 */
	public void setPalabras(List<PalabraClave> palabras) {
		this.palabras = palabras;
	}
}
