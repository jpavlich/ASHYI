package org.sakaiproject.lessonbuildertool.model;

import java.util.List;

/**
 * Interface de un Objetivo
 * Representa a un objetivo en ASHYI
 * @author ASHYI
 * @see ObjetivoImpl
 */

public interface Objetivo {
	
	/**
	 * @return id del objetivo
	 */
	public Integer getIdObjetivo();
	/**
	 * @param idObjetivo id del objetivo
	 */
	public void setIdObjetivo(Integer idObjetivo);
	/**
	 * @return nombre del objetivo (palabras clave concatenadas)
	 */
	public String getNombre();
	/**
	 * @param nombre nombre del objetivo (palabras clave concatenadas)
	 */
	public void setNombre(String nombre);
	/**
	 * @return palabras clave que conforman en objetivo
	 */
	public List<PalabraClave> getPalabras();
	/**
	 * @param palabras palabras clave que conforman en objetivo
	 */
	public void setPalabras(List<PalabraClave> palabras);

}
