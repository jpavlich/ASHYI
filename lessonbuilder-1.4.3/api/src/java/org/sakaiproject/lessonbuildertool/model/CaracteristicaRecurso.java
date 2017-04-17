package org.sakaiproject.lessonbuildertool.model;

/**
 * Interface de Caracteristicas de un recurso
 * Representa a una caracteristica que un recurso posee en ASHYI
 * @author ASHYI
 * @see CaracteristicaRecursoImpl
 */

public interface CaracteristicaRecurso{
	
	/**
	 * @return Recurso asociado
	 */
	public Recurso getIdRecurso();

	/**
	 * @param idRecurso Recurso asociado
	 */
	public void setIdRecurso(Recurso idRecurso);

	/**
	 * @return Caracteristica asociada
	 */
	public Caracteristica getIdCaracteristica();

	/**
	 * @param idCaracteristica Caracteristica asociada
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica);
	
}