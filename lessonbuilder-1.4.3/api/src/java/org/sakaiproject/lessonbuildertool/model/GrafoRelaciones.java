package org.sakaiproject.lessonbuildertool.model;

/**
 * Interface de las Relaciones de un Grafo
 * Representa a una relacion que compone un grafo en ASHYI
 * @author ASHYI
 * @see GrafoRelacionesImpl
 */

public interface GrafoRelaciones {
	
	/**
	 * @return Grafo que contiene las relaciones
	 */
	public Grafo getIdGrafo();
	/**
	 * @param idGrafo Grafo que contiene las relaciones
	 */
	public void setIdGrafo(Grafo idGrafo);
	/**
	 * @return id de la Relacion
	 */
	public int getIdRelacionGrafo();
	/**
	 * @param idRelacionGrafo id de la Relacion
	 */
	public void setIdRelacionGrafo(int idRelacionGrafo);
	/**
	 * @return id del ItemPlan que esta en el destino de la relacion
	 */
	public int getIdItemPlan_Destino();
	/**
	 * @param idItemPlan_Destino id del ItemPlan que esta en el destino de la relacion
	 */
	public void setIdItemPlan_Destino(int idItemPlan_Destino);
	/**
	 * @return id del ItemPlan que esta en el origen de la relacion
	 */
	public int getIdItemPlan_Origen();
	/**
	 * @param idItemPlan_Origen id del ItemPlan que esta en el origen de la relacion
	 */
	public void setIdItemPlan_Origen(int idItemPlan_Origen);
	/**
	 * @return orden de la relacion en el grafo
	 */
	public int getOrden();
	/**
	 * @param orden orden de la relacion en el grafo
	 */
	public void setOrden(int orden);
}
