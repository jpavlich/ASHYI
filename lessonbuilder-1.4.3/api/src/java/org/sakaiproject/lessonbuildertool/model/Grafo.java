package org.sakaiproject.lessonbuildertool.model;

/**
 * Interface de Grafo
 * Representa a un grafo a ejecutar o un grafo general en ASHYI
 * @author ASHYI
 * @see GrafoImpl
 */

public interface Grafo {

	/**
	 * @return id del Grafo
	 */
	public int getIdGrafo();

	/**
	 * @param idGrafo id del Grafo
	 */
	public void setIdGrafo(int idGrafo);

	/**
	 * @return ItemPlan Inicial del grafo
	 */
	public int getIdItemPlan_Inicial();

	/**
	 * @param ItemPlan_Inicial ItemPlan Inicial del grafo
	 */
	public void setIdItemPlan_Inicial(int idItemPlan_Inicial);

}
