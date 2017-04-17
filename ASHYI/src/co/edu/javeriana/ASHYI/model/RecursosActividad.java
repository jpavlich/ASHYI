package co.edu.javeriana.ASHYI.model;

/**
 * Interface de un recurso asociado a una actividad
 * Representa a un recurso que una actividad posee en ASHYI
 * @author ASHYI
 * @see RecursosActividadImpl
 */

public interface RecursosActividad {
	
	/** 
	 * @return idRecursosActividad
	 */
	public int getIdRecursosActividad();
	/**
	 * @param idRecursosActividad
	 */
	public void setIdRecursosActividad(int idRecursosActividad);
	/**
	 * @return Actividad asociada
	 */
	public Actividad getIdActividad();
	/**
	 * @param idActividad Actividad asociada
	 */
	public void setIdActividad(Actividad idActividad);
	/**
	 * @return Recurso asociado
	 */
	public Recurso getIdRecurso();
	/**
	 * @param idRecurso Recurso asociado 
	 */
	public void setIdRecurso(Recurso idRecurso);

	
//	public int getOrden();
//
//	public void setOrden(int orden);

}
