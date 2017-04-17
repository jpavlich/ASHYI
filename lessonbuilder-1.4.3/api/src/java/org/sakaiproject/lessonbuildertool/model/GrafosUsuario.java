package org.sakaiproject.lessonbuildertool.model;

import java.util.Date;

/**
 * Interface de los grafos de un usuario
 * Representa a un grafo que un usuario debe ejecutar/organizar en ASHYI
 * @author ASHYI
 * @see GrafosUsuarioImpl
 */

public interface GrafosUsuario {

	/**
	 * @return Grafo asociado
	 */
	public Grafo getIdGrafo();

	/**
	 * @param idGrafo Grafo asociado
	 */
	public void setIdGrafo(Grafo idGrafo);

	/**
	 * @return Usuario asociado
	 */
	public Usuario getIdUsuario();

	/**
	 * @param idUsuario Usuario asociado
	 */
	public void setIdUsuario(Usuario idUsuario);

	/**
	 * @return id de la actividad asociada
	 */
	public int getIdActividad();

	/**
	 * @param idActividad id de la actividad asociada
	 */
	public void setIdActividad(int idActividad);
	
	/**
	 * @return el grafo esta activo o no
	 */
	public boolean isActivo();

	/**
	 * @param activo el grafo esta activo o no
	 */
	public void setActivo(boolean activo);

	/**
	 * @return fecha de creacion del grafo
	 */
	public Date getFecha();

	/**
	 * @param fecha fecha de creacion del grafos
	 */
	public void setFecha(Date fecha);
	
	/**
	 * origen del grafo (calificacion, original, contexto)
	 * @return
	 */
	public String getOrigen();

	/**
	 * @param origen origen del grafo (calificacion, original, contexto)
	 */
	public void setOrigen(String origen);

}
