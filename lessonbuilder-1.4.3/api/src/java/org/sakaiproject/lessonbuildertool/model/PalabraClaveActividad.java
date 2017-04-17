package org.sakaiproject.lessonbuildertool.model;

/**
 * Interface de una palabra clave que esta relacionada a una actividad
 * Representa a una palabra clave que hace parte de una actividad macro en ASHYI
 * @author ASHYI
 * @see PalabraClaveActividadImpl
 */

public interface PalabraClaveActividad {
	
	/**
	 * @return PalabraClave a relacionar
	 */
	public PalabraClave getIdPalabraClave();

	/**
	 * @param idPalabraClave PalabraClave a relacionar
	 */
	public void setIdPalabraClave(PalabraClave idPalabraClave);

	/**
	 * @return Actividad asociada
	 */
	public Actividad getIdActividad();

	/**
	 * @param idActividad Actividad asociada
	 */
	public void setIdActividad(Actividad idActividad);

}
