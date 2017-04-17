package co.edu.javeriana.ASHYI.model;

/**
 * Interface de Caracteristicas de un tipode actividad
 * Representa a una caracteristica que un tipo de actividad posee en ASHYI
 * @author ASHYI
 * @see CaracteristicasTipoImpl
 */

public interface CaracteristicasTipo {

	/**
	 * @return
	 */
	public int getIdCaracteristicaTipo();

	/**
	 * @param idCaracteristicaTipo
	 */
	public void setIdCaracteristicaTipo(int idCaracteristicaTipo);

	/**
	 * @return Tipo asociado
	 */
	public Tipo getIdTipo();

	/**
	 * @param idTipo Tipo asociado
	 */
	public void setIdTipo(Tipo idTipo);

	/**
	 * @return
	 */
	public int getLinea();
	
	/**
	 * @param linea 
	 */
	public void setLinea(int linea);

	/**
	 * @return Caracteristica asociada
	 */
	public Caracteristica getIdCaracteristica();

	/**
	 * @param idCaracteristica Caracteristica asociada
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica);
}
