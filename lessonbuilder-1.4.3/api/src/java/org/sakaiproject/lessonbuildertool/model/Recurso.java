package org.sakaiproject.lessonbuildertool.model;

import java.util.List;

/**
 * Interface de un recurso
 * Representa a un recurso en ASHYI
 * @author ASHYI
 * @see RecursoImpl
 */

public interface Recurso {
	
	/**
	 * @return id del recurso
	 */
	public int getIdRecurso();

	/**
	 * @param idRecurso id del recurso
	 */
	public void setIdRecurso(int idRecurso);

	/**
	 * @return nombre del recurso
	 */
	public String getNombre();

	/**
	 * @param nombre nombre del recurso
	 */
	public void setNombre(String nombre);

	/**
	 * @return id del item relacioando en sakai
	 */
	public String getIdItemSakai();

	/**
	 * @param idItemSakai id del item relacioando en sakai
	 */
	public void setIdItemSakai(String idItemSakai);

	/**
	 * @return naturaleza: fisico o logico
	 */
	public String getNaturaleza();

	/**
	 * @param naturaleza fisico o logico
	 */
	public void setNaturaleza(String naturaleza);

	/**
	 * @return tipo: Internet, Infraestructura, text/html, pdf, forum, etc
	 */
	public String getTipo();

	/**
	 * @param tipo Internet, Infraestructura, text/html, pdf, forum, etc
	 */
	public void setTipo(String tipo);

	/**
	 * @return formato: docx, html, pdf, etc
	 */
	public String getFormato();

	/**
	 * @param formato docx, html, pdf, etc
	 */
	public void setFormato(String formato);

	/**
	 * @return nivel: Universitario, secundario, etc
	 */
	public String getNivel();

	/**
	 * @param nivel Universitario, secundario, etc
	 */
	public void setNivel(String nivel);

	/**
	 * @return tipo_acceso: Libre, Pago
	 */
	public String getTipo_acceso();

	/**
	 * @param tipo_acceso Libre, Pago
	 */
	public void setTipo_acceso(String tipo_acceso);

	/**
	 * @return caracteristicas del recurso
	 */
	public List<CaracteristicaRecurso> getCaracteristicas();

	/**
	 * @param caracteristicas caracteristicas del recurso
	 */
	public void setCaracteristicas(List<CaracteristicaRecurso> caracteristicas);
}
