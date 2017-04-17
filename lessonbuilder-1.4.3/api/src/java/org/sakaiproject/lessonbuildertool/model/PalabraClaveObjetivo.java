package org.sakaiproject.lessonbuildertool.model;

/**
 * Interface de una palabra clave que esta relacionada a un objetivo
 * Representa a una palabra clave que conforma un objetivo en ASHYI
 * @author ASHYI
 * @see PalabraClaveObjetivoImpl
 */

public interface PalabraClaveObjetivo {
	
	/**
	 * @return Objetivo asociado
	 */
	public Objetivo getIdObjetivo();

	/**
	 * @param idObjetivo Objetivo asociado
	 */
	public void setIdObjetivo(Objetivo idObjetivo);

	/**
	 * @return PalabraClave a relacionar
	 */
	public PalabraClave getIdPalabraClave();

	/**
	 * @param idPalabraClave PalabraClave a relacionar
	 */
	public void setIdPalabraClave(PalabraClave idPalabraClave);

}
