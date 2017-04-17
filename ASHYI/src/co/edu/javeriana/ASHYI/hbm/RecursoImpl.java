package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ASHYI.model.CaracteristicaRecurso;
import co.edu.javeriana.ASHYI.model.Recurso;

/**
 * @author ASHYI
 * @see Recurso
 */

public class RecursoImpl implements Recurso, Serializable{
	
	private int idRecurso;
	private String nombre;	
	private String idItemSakai;
	private String naturaleza;
	private String tipo;
	private String formato;
	private String nivel;
	private String tipo_acceso;
	
	private List<CaracteristicaRecurso> caracteristicas;
	
	/**
	 * Constructor base
	 */
	public RecursoImpl() {
	}
	
	/**
	 * @param idRecurso
	 * @param nombre del recurso
	 * @param idItemSakai id del item relacioando en sakai
	 * @param naturaleza: fisico o logico
	 * @param tipo: Internet, Infraestructura, text/html, pdf, forum, etc
	 * @param formato: docx, html, pdf, etc
	 * @param nivel: Universitario, secundario, etc
	 * @param tipo_acceso: Libre, Pago
	 */
	public RecursoImpl(int idRecurso, String nombre, String idItemSakai,
			String naturaleza, String tipo, String formato, String nivel,
			String tipo_acceso) {
		super();
		this.idRecurso = idRecurso;
		this.nombre = nombre;
		this.idItemSakai = idItemSakai;
		this.naturaleza = naturaleza;
		this.tipo = tipo;
		this.formato = formato;
		this.nivel = nivel;
		this.tipo_acceso = tipo_acceso;
		this.caracteristicas = new ArrayList<CaracteristicaRecurso>();
	}

	/**
	 * @param nombre del recurso
	 * @param idItemSakai id del item relacioando en sakai
	 * @param naturaleza: fisico o logico
	 * @param tipo: Internet, Infraestructura, text/html, pdf, forum, etc
	 * @param formato: docx, html, pdf, etc
	 * @param nivel: Universitario, secundario, etc
	 * @param tipo_acceso: Libre, Pago
	 */
	public RecursoImpl(String nombre, String idItemSakai,
			String naturaleza, String tipo, String formato, String nivel,
			String tipo_acceso) {
		super();
		this.nombre = nombre;
		this.idItemSakai = idItemSakai;
		this.naturaleza = naturaleza;
		this.tipo = tipo;
		this.formato = formato;
		this.nivel = nivel;
		this.tipo_acceso = tipo_acceso;
		this.caracteristicas = new ArrayList<CaracteristicaRecurso>();
	}
	
	/**
	 * @param nombre del recurso	 
	 * @param naturaleza: fisico o logico
	 * @param idItemSakai id del item relacioando en sakai
	 */
	public RecursoImpl(String nombre, String naturaleza, String idItemSakai) {
		super();
		this.nombre = nombre;
		this.idItemSakai = idItemSakai;
		this.naturaleza = naturaleza;
		this.caracteristicas = new ArrayList<CaracteristicaRecurso>();
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getIdRecurso()
	 */
	public int getIdRecurso() {
		return idRecurso;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setIdRecurso(int)
	 */
	public void setIdRecurso(int idRecurso) {
		this.idRecurso = idRecurso;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getNombre()
	 */
	public String getNombre() {
		return nombre;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setNombre(java.lang.String)
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getIdItemSakai()
	 */
	public String getIdItemSakai() {
		return idItemSakai;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setIdItemSakai(java.lang.String)
	 */
	public void setIdItemSakai(String idItemSakai) {
		this.idItemSakai = idItemSakai;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getNaturaleza()
	 */
	public String getNaturaleza() {
		return naturaleza;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setNaturaleza(java.lang.String)
	 */
	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getTipo()
	 */
	public String getTipo() {
		return tipo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setTipo(java.lang.String)
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getFormato()
	 */
	public String getFormato() {
		return formato;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setFormato(java.lang.String)
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getNivel()
	 */
	public String getNivel() {
		return nivel;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setNivel(java.lang.String)
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getTipo_acceso()
	 */
	public String getTipo_acceso() {
		return tipo_acceso;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setTipo_acceso(java.lang.String)
	 */
	public void setTipo_acceso(String tipo_acceso) {
		this.tipo_acceso = tipo_acceso;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#getCaracteristicas()
	 */
	public List<CaracteristicaRecurso> getCaracteristicas() {
		return caracteristicas;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Recurso#setCaracteristicas(java.util.List)
	 */
	public void setCaracteristicas(List<CaracteristicaRecurso> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}	

}