package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Grafo;

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
		
		idItemPlan_Inicial = -1;
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
	 * @see co.edu.javeriana.ASHYI.model.Grafo#getIdGrafo()
	 */
	public int getIdGrafo() {
		return idGrafo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Grafo#setIdGrafo(int)
	 */
	public void setIdGrafo(int idGrafo) {
		this.idGrafo = idGrafo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Grafo#getIdItemPlan_Inicial()
	 */
	public int getIdItemPlan_Inicial() {
		return idItemPlan_Inicial;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Grafo#setIdItemPlan_Inicial(int)
	 */
	public void setIdItemPlan_Inicial(int idItemPlan_Inicial) {
		this.idItemPlan_Inicial = idItemPlan_Inicial;
	}
	
}
