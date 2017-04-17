package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.Recurso;
import co.edu.javeriana.ASHYI.model.RecursosActividad;

/**
 * @author ASHYI
 * @see RecursosActividad
 */

public class RecursosActividadImpl implements RecursosActividad, Serializable{
	
	private int idRecursosActividad;
	//private int orden;
	private Actividad idActividad;
	private Recurso idRecurso;
	
	/**
	 * Constructor base
	 */
	public RecursosActividadImpl() {
	}	
	
	/**
	 * @param idRecursosActividad
	 * @param Actividad asociada
	 * @param Recurso asociado
	 */
	public RecursosActividadImpl(int idRecursosActividad,
			Actividad idActividad, Recurso idRecurso) {
		super();
		this.idRecursosActividad = idRecursosActividad;
		this.idActividad = idActividad;
		this.idRecurso = idRecurso;
	}	
	
	/**
	 * @param Actividad asociada
	 * @param Recurso asociado
	 */
	public RecursosActividadImpl( Actividad idActividad,
			Recurso idRecurso) {
		super();
		this.idActividad = idActividad;
		this.idRecurso = idRecurso;
	}	
	

	
//	public int getOrden() {
//		return orden;
//	}
//
//	public void setOrden(int orden) {
//		this.orden = orden;
//	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RecursosActividad#getIdRecursosActividad()
	 */
	public int getIdRecursosActividad() {
		return idRecursosActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RecursosActividad#setIdRecursosActividad(int)
	 */
	public void setIdRecursosActividad(int idRecursosActividad) {
		this.idRecursosActividad = idRecursosActividad;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RecursosActividad#getIdActividad()
	 */
	public Actividad getIdActividad() {
		return idActividad;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RecursosActividad#setIdActividad(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void setIdActividad(Actividad idActividad) {
		this.idActividad = idActividad;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RecursosActividad#getIdRecurso()
	 */
	public Recurso getIdRecurso() {
		return idRecurso;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.RecursosActividad#setIdRecurso(org.sakaiproject.lessonbuildertool.model.Recurso)
	 */
	public void setIdRecurso(Recurso idRecurso) {
		this.idRecurso = idRecurso;
	}
}