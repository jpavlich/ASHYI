package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Grafo;
import co.edu.javeriana.ASHYI.model.GrafoRelaciones;

/**
 * @author ASHYI
 * @see GrafoRelaciones
 */
public class GrafoRelacionesImpl implements GrafoRelaciones, Serializable{
	
	private Grafo idGrafo;
	private int idRelacionGrafo;
	private int idItemPlan_Destino;
	private int idItemPlan_Origen;
	private int orden;
		
	/**
	 * Constructor base
	 */
	public GrafoRelacionesImpl() {
	}
	
	/**
	 * @param Grafo que contiene las relaciones
	 * @param idRelacionGrafo
	 * @param idItemPlan_Destino
	 * @param idItemPlan_Origen
	 * @param orden de la relacion en el grafo
	 */
	public GrafoRelacionesImpl(Grafo idGrafo, int idRelacionGrafo,
			int idItemPlan_Destino, int idItemPlan_Origen, int orden) {
		super();
		this.idGrafo = idGrafo;
		this.idRelacionGrafo = idRelacionGrafo;
		this.idItemPlan_Destino = idItemPlan_Destino;
		this.idItemPlan_Origen = idItemPlan_Origen;
		this.orden=orden;
	}
	
	/**
	 * @param Grafo que contiene las relaciones
	 * @param idRelacionGrafo
	 * @param idItemPlan_Destino
	 * @param idItemPlan_Origen
	 */
	public GrafoRelacionesImpl(Grafo idGrafo, int idRelacionGrafo,
			int idItemPlan_Destino, int idItemPlan_Origen) {
		super();
		this.idGrafo = idGrafo;
		this.idRelacionGrafo = idRelacionGrafo;
		this.idItemPlan_Destino = idItemPlan_Destino;
		this.idItemPlan_Origen = idItemPlan_Origen;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#getIdGrafo()
	 */
	public Grafo getIdGrafo() {
		return idGrafo;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#setIdGrafo(co.edu.javeriana.ASHYI.model.Grafo)
	 */
	public void setIdGrafo(Grafo idGrafo) {
		this.idGrafo = idGrafo;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#getIdRelacionGrafo()
	 */
	public int getIdRelacionGrafo() {
		return idRelacionGrafo;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#setIdRelacionGrafo(int)
	 */
	public void setIdRelacionGrafo(int idRelacionGrafo) {
		this.idRelacionGrafo = idRelacionGrafo;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#getIdItemPlan_Destino()
	 */
	public int getIdItemPlan_Destino() {
		return idItemPlan_Destino;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#setIdItemPlan_Destino(int)
	 */
	public void setIdItemPlan_Destino(int idItemPlan_Destino) {
		this.idItemPlan_Destino = idItemPlan_Destino;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#getIdItemPlan_Origen()
	 */
	public int getIdItemPlan_Origen() {
		return idItemPlan_Origen;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#setIdItemPlan_Origen(int)
	 */
	public void setIdItemPlan_Origen(int idItemPlan_Origen) {
		this.idItemPlan_Origen = idItemPlan_Origen;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#getOrden()
	 */
	public int getOrden() {
		return this.orden;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafoRelaciones#setOrden(int)
	 */
	public void setOrden(int orden) {
		this.orden=orden;
	}
}
