package org.sakaiproject.lessonbuildertool.model;

import java.io.Serializable;
/**
 * Interface de actividad tiene actividad
 * Representa a la relacion entre una actividad de mayor nivel y una de menor nivel
 * @author ASHYI
 * @see ActividadTieneActividadImpl
 */

public interface ActividadTieneActividad extends Serializable{	
	
	/**
	 * @return Orden de la actividad "hija"
	 */
	public int getOrden();
	/**
	 * @param orden Orden de la actividad "hija"
	 */
	public void setOrden(int orden);
	/**
	 * @return id Actividad "padre"
	 */
	public Actividad getIdActividad();
	/**
	 * @param idActividad id Actividad "padre"
	 */
	public void setIdActividad(Actividad idActividad);
	/**
	 * @return id Actividad "hija"
	 */
	public Actividad getIdActividadSiguienteNivel();
	/**
	 * @param id_Actividad_Dependiente id Actividad "hija"
	 */
	public void setIdActividadSiguienteNivel(Actividad id_Actividad_Dependiente);
	/**
	 * @return Estilo grafico de la actividad "padre"
	 */
	public String getEstiloActividad();
	
	/**
	 * @param estiloActividad Estilo grafico de la actividad "padre"
	 */
	public void setEstiloActividad(String estiloActividad);
	/**
	 * @return Estilo grafico de la actividad "hija"
	 */
	public String getEstiloActividadSiguienteNivel();
	/**
	 * @param estiloActividadSiguienteNivel Estilo grafico de la actividad "hija"
	 */
	public void setEstiloActividadSiguienteNivel(String estiloActividadSiguienteNivel);
	
}