package co.edu.javeriana.ASHYI.model;

/**
 * Interface de Caracteristicas de un recurso
 * Representa a una caracteristica que un recurso posee en ASHYI
 * @author ASHYI
 * @see CaracteristicaRecursoImpl
 */

public interface CaracteristicaCaracteristicas{
	
	/**
	 * @return Caracteristica asociada
	 */
	public Caracteristica getIdCaracteristica();

	/**
	 * @param idRecurso Caracteristica asociada
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica);

	/**
	 * @return Caracteristica asociada en la relacion
	 */
	public Caracteristica getIdCaracteristicaRelacion();

	/**
	 * @param idCaracteristica Caracteristica asociada en la relacion
	 */
	public void setIdCaracteristicaRelacion(Caracteristica idCaracteristicaRelacion);
	
}