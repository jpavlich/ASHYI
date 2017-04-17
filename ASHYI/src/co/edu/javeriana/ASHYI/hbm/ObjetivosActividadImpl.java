package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.Objetivo;
import co.edu.javeriana.ASHYI.model.ObjetivosActividad;

/**
 * @author ASHYI
 * @see Objetivo
 */

public class ObjetivosActividadImpl implements ObjetivosActividad, Serializable{
	
	//private Integer idObjetivoActividad;
	private Integer tipo;
	private Actividad idActividad;
	private Objetivo idObjetivo;
	
	/**
	 * Constructor base
	 */
	public ObjetivosActividadImpl() {
	}	
	
	/**
	 * @param tipo 1 si es propia de la actividad, 2 si es de una actividad de mayor nivel y la actividad lo cumple
	 * @param Actividad asociada
	 * @param Objetivo asociado
	 */
	public ObjetivosActividadImpl(Integer tipo, Actividad idActividad, Objetivo idObjetivo) {
		super();
		this.tipo = tipo;
		this.idActividad = idActividad;
		this.idObjetivo = idObjetivo;
	}

	/**
	 * @param Actividad
	 * @param obActividad
	 */
	public ObjetivosActividadImpl(Actividad ac, Objetivo obActividad) {
		this.idActividad = ac;
		this.idObjetivo = obActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ObjetivosActividad#getTipo()
	 */
	public Integer getTipo() {
		return tipo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ObjetivosActividad#setTipo(java.lang.Integer)
	 */
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

//	public Integer getOrden() {
//		return orden;
//	}
//	public void setOrden(Integer orden) {
//		this.orden = orden;
//	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ObjetivosActividad#getIdActividad()
	 */
	public Actividad getIdActividad() {
		return idActividad;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ObjetivosActividad#setIdActividad(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void setIdActividad(Actividad idActividad) {
		this.idActividad = idActividad;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ObjetivosActividad#getIdObjetivo()
	 */
	public Objetivo getIdObjetivo() {
		return idObjetivo;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ObjetivosActividad#setIdObjetivo(org.sakaiproject.lessonbuildertool.model.Objetivo)
	 */
	public void setIdObjetivo(Objetivo idObjetivo) {
		this.idObjetivo = idObjetivo;
	}

	/**
	 * @param objeto a comparar
	 * @return
	 */
	public Integer compareTo(Object o) {
		ObjetivosActividadImpl ob=(ObjetivosActividadImpl) o;
		if(this.tipo<ob.tipo)
			return -1;
		else if(this.tipo>ob.tipo)
			return 1;
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		Integer hsCode;
		hsCode= this.idActividad.hashCode();
		hsCode= 17* hsCode + this.getIdActividad().getIdActividad();
		hsCode= 31* hsCode + this.getIdObjetivo().getIdObjetivo();
		return hsCode;
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj) {
    	ObjetivosActividad dep=(ObjetivosActividad)obj;
        if (dep == null)
            return false;
        if (dep.getIdActividad().getIdActividad().equals(this.idActividad.getIdActividad())  && 
        	dep.getIdObjetivo().getIdObjetivo().equals(this.idObjetivo.getIdObjetivo()))
            return true;
        return false;
    }
}