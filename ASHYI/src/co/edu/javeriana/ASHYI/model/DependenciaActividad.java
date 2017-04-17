package co.edu.javeriana.ASHYI.model;
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
	 * @return Id de Conexion
	 */
	public int getIdConexion();
	/**
	 * @param ID Id Conexion
	 */
	public void setIdConexion(int ID);
	/**
	 * @return Actividad asociada
	 */
	public Actividad getIdActividad();
	/**
	 * @param idActividad Actividad asociada
	 */
	public void setIdActividad(Actividad idActividad);
	/** 
	 * @return Actividad_Dependiente
	 */
	public Actividad getId_Actividad_Dependiente();
	/**
	 * @param id_Actividad_Dependiente Actividad_Dependiente
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