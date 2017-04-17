package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneActividad;

/**
 * @author ASHYI
 * @see ActividadTieneActividad
 */
public class ActividadTieneActividadImpl implements ActividadTieneActividad, Serializable{
	
	private int orden;
//	private int idActividad;
//	private int idActividadSiguienteNivel;
	private Actividad idActividad;
	private Actividad idActividadSiguienteNivel;
	private String estiloActividad;
	private String estiloActividadSiguienteNivel;
	
	/**
	 * Cosntructor base
	 */
	public ActividadTieneActividadImpl() {
	}
	
	/**
	 * @param idActividad
	 * @param idActividadSiguienteNivel
	 */
	public ActividadTieneActividadImpl(Actividad idActividad, Actividad idActividadSiguienteNivel) {
		super();
		this.idActividad = idActividad;
		this.idActividadSiguienteNivel = idActividadSiguienteNivel;
	}
	
	/**
	 * @param orden
	 * @param idActividad
	 * @param idActividadSiguienteNivel
	 * @param estiloActividad
	 * @param estiloActividadSiguienteNivel
	 */
	public ActividadTieneActividadImpl(int orden, Actividad idActividad,
			Actividad idActividadSiguienteNivel, String estiloActividad, String estiloActividadSiguienteNivel) {
		super();
		this.orden = orden;
		this.idActividad = idActividad;
		this.idActividadSiguienteNivel = idActividadSiguienteNivel;
		this.estiloActividad=estiloActividad;
		this.estiloActividadSiguienteNivel=estiloActividadSiguienteNivel;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#getOrden()
	 */
	public int getOrden() {
		return orden;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#setOrden(int)
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#getIdActividad()
	 */
	public Actividad getIdActividad() {
		return idActividad;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#setIdActividad(co.edu.javeriana.ASHYI.model.Actividad)
	 */
	public void setIdActividad(Actividad idActividad) {
		this.idActividad = idActividad;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#getIdActividadSiguienteNivel()
	 */
	public Actividad getIdActividadSiguienteNivel() {
		return idActividadSiguienteNivel;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#setIdActividadSiguienteNivel(co.edu.javeriana.ASHYI.model.Actividad)
	 */
	public void setIdActividadSiguienteNivel(Actividad id_Actividad_Dependiente) {
		idActividadSiguienteNivel = id_Actividad_Dependiente;
	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#getEstiloActividad()
	 */
	public String getEstiloActividad() {
		return estiloActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#setEstiloActividad(java.lang.String)
	 */
	public void setEstiloActividad(String estiloActividad) {
		this.estiloActividad = estiloActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#getEstiloActividadSiguienteNivel()
	 */
	public String getEstiloActividadSiguienteNivel() {
		return estiloActividadSiguienteNivel;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.ActividadTieneActividad#setEstiloActividadSiguienteNivel(java.lang.String)
	 */
	public void setEstiloActividadSiguienteNivel(String estiloActividadSiguienteNivel) {
		this.estiloActividadSiguienteNivel = estiloActividadSiguienteNivel;
	}
		
}