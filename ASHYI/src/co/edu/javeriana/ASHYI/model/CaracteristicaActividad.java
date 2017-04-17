package co.edu.javeriana.ASHYI.model;

/**
 * Interface de Caracteristicas de una actividad
 * Representa a una caracteristica que una actividad posee en ASHYI
 * @author ASHYI
 * @see CaracteristicaActividadImpl
 */

public interface CaracteristicaActividad{
	
	/**
	 * @return 
	 */
	public int getIdCaracteristicaActividad();
	/**
	 * @param idCaracteristicaActividad
	 */
	public void setIdCaracteristicaActividad(int idCaracteristicaActividad);
	/**
	 * @return Actividad asociada
	 */
	public Actividad getIdActividad();
	/**
	 * @param idActividad Actividad asociada
	 */
	public void setIdActividad(Actividad idActividad);
	/**
	 * @return Caracteristica asociada
	 */
	public Caracteristica getIdCaracteristica();
	/**
	 * @param idCaracteristica Caracteristica asociada
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica);
	/**
	 * @return si es precondicion
	 */
	public boolean isPrecondicion();
	/**
	 * @param precondicion si es precondicion
	 */
	public void setPrecondicion(boolean precondicion);
	/**
	 * @return si es postcondicion
	 */
	public boolean isPostcondicion() ;
	/**
	 * @param postcondicion si es postcondicion
	 */
	public void setPostcondicion(boolean postcondicion);	
	/**
	 * @return prioridad de la caracteristica
	 */
	public int getPrioridad();
	/**
	 * @param prioridad prioridad de la caracteristica
	 */
	public void setPrioridad(int prioridad);	
//	public int getOrden();
//	public void setOrden(int orden);
	
}