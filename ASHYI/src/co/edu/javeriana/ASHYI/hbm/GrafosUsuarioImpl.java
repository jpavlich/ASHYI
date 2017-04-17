package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;
import java.util.Date;

import co.edu.javeriana.ASHYI.model.Grafo;
import co.edu.javeriana.ASHYI.model.GrafosUsuario;
import co.edu.javeriana.ASHYI.model.Usuario;

/**
 * @author ASHYI
 * @see GrafosUsuario
 */
public class GrafosUsuarioImpl implements GrafosUsuario, Serializable{

	private Grafo idGrafo;
	private Usuario idUsuario;
	private int idActividad;
	private boolean activo;
	private Date fecha;
	private String origen;
	
	/**
	 * Constructor base	 
	 */
	public GrafosUsuarioImpl() {
	}
	
	/**
	 * @param grafo
	 * @param user
	 * @param idActividad2
	 * @param fechaActual
	 * @param es el grafo activo
	 * @param origen
	 */
	public GrafosUsuarioImpl(Grafo idGrafo, Usuario idUsuario, int idActividad,Date fecha,boolean activo, String origen) {
		super();
		this.idGrafo = idGrafo;
		this.idUsuario = idUsuario;
		this.idActividad = idActividad;
		this.activo = activo;
		this.fecha = fecha;
		this.origen = origen;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#getIdGrafo()
	 */
	public Grafo getIdGrafo() {
		return idGrafo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#setIdGrafo(co.edu.javeriana.ASHYI.model.Grafo)
	 */
	public void setIdGrafo(Grafo idGrafo) {
		this.idGrafo = idGrafo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#getIdUsuario()
	 */
	public Usuario getIdUsuario() {
		return idUsuario;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#setIdUsuario(co.edu.javeriana.ASHYI.model.Usuario)
	 */
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#getIdActividad()
	 */
	public int getIdActividad() {
		return idActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#setIdActividad(int)
	 */
	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#isActivo()
	 */
	public boolean isActivo() {
		return activo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#setActivo(boolean)
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#getFecha()
	 */
	public Date getFecha() {
		return fecha;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#setFecha(java.util.Date)
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#getOrigen()
	 */
	public String getOrigen() {
		return origen;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.GrafosUsuario#setOrigen(java.lang.String)
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
}
