package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.DependenciaActividad;

/**
 * @author ASHYI
 * @see DependenciaActividad
 */

public class DependenciaActividadImpl implements DependenciaActividad, Serializable{
	
	private Integer orden;
	private Actividad idActividad;
	private Actividad Id_Actividad_Dependiente;
	private Integer tipoConexion;
	private Integer idConexion;
	
	/**
	 * Constructor base
	 */
	public DependenciaActividadImpl() {
	}
	
	/**
	 * @param orden
	 * @param idActividad
	 * @param id_Actividad_Dependiente
	 * @param tipoConexion
	 */
	public DependenciaActividadImpl(int orden, Actividad idActividad,
			Actividad id_Actividad_Dependiente, int tipoConexion) {
		super();
		this.orden = orden;
		this.idActividad = idActividad;
		Id_Actividad_Dependiente = id_Actividad_Dependiente;
		this.tipoConexion=tipoConexion;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#getOrden()
	 */
	public int getOrden() {
		return orden;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#setOrden(int)
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#getIdActividad()
	 */
	public Actividad getIdActividad() {
		return idActividad;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#setIdActividad(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void setIdActividad(Actividad idActividad) {
		this.idActividad = idActividad;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#getId_Actividad_Dependiente()
	 */
	public Actividad getId_Actividad_Dependiente() {
		return Id_Actividad_Dependiente;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#setId_Actividad_Dependiente(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void setId_Actividad_Dependiente(Actividad id_Actividad_Dependiente) {
		Id_Actividad_Dependiente = id_Actividad_Dependiente;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#getTipoConexion()
	 */
	public int getTipoConexion() {
		return tipoConexion;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#setTipoConexion(int)
	 */
	public void setTipoConexion(int tipoConexion) {
		this.tipoConexion=tipoConexion;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#getIdConexion()
	 */
	public int getIdConexion() {
		return this.idConexion;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.DependenciaActividad#setIdConexion(int)
	 */
	public void setIdConexion(int ID) {
		this.idConexion=ID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
        /*return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(this.idActividad).
            append(this.Id_Actividad_Dependiente).
            append(this.idConexion).
            append(this.tipoConexion).
            toHashCode();*/
		int hsCode;
		hsCode= this.idActividad.hashCode();
		hsCode= 17* hsCode +this.Id_Actividad_Dependiente.hashCode();
		hsCode= 31* hsCode + this.getIdConexion();
		return hsCode;
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj) {
    	DependenciaActividad dep=(DependenciaActividad)obj;
        if (dep == null)
            return false;
        if (dep.getIdActividad() == this.idActividad && dep.getId_Actividad_Dependiente() == this.Id_Actividad_Dependiente)
            return true;
        return false;
    }
}