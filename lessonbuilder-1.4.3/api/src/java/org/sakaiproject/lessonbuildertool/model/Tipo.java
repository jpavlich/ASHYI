package org.sakaiproject.lessonbuildertool.model;

/**
 * Interface de tipo de actividad
 * Representa a un tipo de una actividad en ASHYI
 * @author ASHYI
 * @see TipoImpl
 */

public interface Tipo {
	
	/**
	 * @return id del tipo
	 */
	public Integer getIdTipo();
	/**
	 * @param idTipo id del tipo
	 */
	public void setIdTipo(Integer idTipo);
	/**
	 * @return nombre del tipo
	 */
	public String getNombre();
	/**
	 * @param nombre nombre del tipo
	 */
	public void setNombre(String nombre);

}
