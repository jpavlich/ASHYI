package org.sakaiproject.lessonbuildertool.model;
import java.io.Serializable;

/**
 * Interface de Dependencia de una Actividad
 * @author ASHYI
 * @see DependenciaActividadImpl
 */

public interface DependenciaActividad extends Serializable{	
	
	/**
	 * @return orden
	 */
	public int getOrden();
	/**
	 * @param orden
	 */
	public void setOrden(int orden);
	/**
	 * @return tipoConexion
	 */
	public int getTipoConexion();
	/**
	 * @param tipoConexion
	 */
	public void setTipoConexion(int tipoConexion);
	/**
	 * @return IdConexion
	 */
	public int getIdConexion();
	/**
	 * @param IdConexion
	 */
	public void setIdConexion(int ID);
	/**
	 * @return Actividad
	 */
	public Actividad getIdActividad();
	/**
	 * @param Actividad
	 */
	public void setIdActividad(Actividad idActividad);
	/** 
	 * @return Actividad_Dependiente
	 */
	public Actividad getId_Actividad_Dependiente();
	/**
	 * @param Actividad_Dependiente
	 */
	public void setId_Actividad_Dependiente(Actividad id_Actividad_Dependiente);
	/**
	 * @return
	 */
	@Override
	public int hashCode();
	/**
	 * @param obj
	 * @return
	 */
	@Override
    public boolean equals(Object obj);
}