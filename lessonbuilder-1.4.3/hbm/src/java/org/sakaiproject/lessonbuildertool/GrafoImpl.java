package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Grafo;

/**
 * @author ASHYI
 * @see Grafo
 */

public class GrafoImpl implements Grafo, Serializable{

	private int idGrafo;
	private int idItemPlan_Inicial;
	
	/**
	 * Cosntructor base
	 */
	public GrafoImpl() {
	}

	/**
	 * @param idGrafo
	 * @param idItemPlan_Inicial
	 */
	public GrafoImpl(int idGrafo, int idItemPlan_Inicial) {
		super();
		this.idGrafo = idGrafo;
		this.idItemPlan_Inicial = idItemPlan_Inicial;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Grafo#getIdGrafo()
	 */
	public int getIdGrafo() {
		return idGrafo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Grafo#setIdGrafo(int)
	 */
	public void setIdGrafo(int idGrafo) {
		this.idGrafo = idGrafo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Grafo#getIdItemPlan_Inicial()
	 */
	public int getIdItemPlan_Inicial() {
		return idItemPlan_Inicial;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Grafo#setIdItemPlan_Inicial(int)
	 */
	public void setIdItemPlan_Inicial(int idItemPlan_Inicial) {
		this.idItemPlan_Inicial = idItemPlan_Inicial;
	}
	
}
