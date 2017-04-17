package org.sakaiproject.lessonbuildertool.model;

/**
 * Interface de Objetivos de una actividad
 * Representa a un objetivo que cumple una actividad en ASHYI
 * @author ASHYI
 * @see ObjetivosActividadImpl
 */

public interface ObjetivosActividad {
	
	/**
	 * @return Actividad asociada
	 */
	public Actividad getIdActividad();
	/**
	 * @param idActividad Actividad asociada
	 */
	public void setIdActividad(Actividad idActividad);
	/**
	 * @return Objetivo asociado
	 */
	public Objetivo getIdObjetivo();
	/**
	 * @param idObjetivo Objetivo asociado
	 */
	public void setIdObjetivo(Objetivo idObjetivo);
	/**
	 * @return 1 si es propia de la actividad, 2 si es de una actividad de mayor nivel y la actividad lo cumple
	 */
	public Integer getTipo();
	/**
	 * @param tipo 1 si es propia de la actividad, 2 si es de una actividad de mayor nivel y la actividad lo cumple
	 */
	public void setTipo(Integer tipo);

}
