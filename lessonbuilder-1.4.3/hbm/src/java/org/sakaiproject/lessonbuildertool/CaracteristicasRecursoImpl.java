package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Recurso;
import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso;

/**
 * @author ASHYI
 * @see CaracteristicasRecurso
 */
public class CaracteristicasRecursoImpl implements CaracteristicaRecurso, Serializable{
	
	private Recurso idRecurso;
	private Caracteristica idCaracteristica;
	
	/**
	 * Constructor base
	 */
	public CaracteristicasRecursoImpl()
	{}

	/**
	 * @param idRecurso
	 * @param idCaracteristica
	 */
	public CaracteristicasRecursoImpl(Recurso idRecurso, Caracteristica idCaracteristica) {
		super();
		this.idRecurso = idRecurso;
		this.idCaracteristica = idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso#getIdRecurso()
	 */
	public Recurso getIdRecurso() {
		return idRecurso;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso#setIdRecurso(org.sakaiproject.lessonbuildertool.model.Recurso)
	 */
	public void setIdRecurso(Recurso idRecurso) {
		this.idRecurso = idRecurso;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso#setIdCaracteristica(org.sakaiproject.lessonbuildertool.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}	
}