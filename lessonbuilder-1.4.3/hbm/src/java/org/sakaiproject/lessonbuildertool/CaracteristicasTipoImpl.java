package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo;
import org.sakaiproject.lessonbuildertool.model.Tipo;

/**
 * @author ASHYI
 * @see CaracteristicasTipo
 */

public class CaracteristicasTipoImpl implements CaracteristicasTipo, Serializable{
	
	//private int idCaracteristicaTipo;
	private Tipo idTipo;
	private Caracteristica idCaracteristica;
	private int linea;
	
	/**
	 * Constructor base
	 */
	public CaracteristicasTipoImpl()
	{}
	
	/**
	 * @param tipo
	 * @param caracteristica
	 * @param linea
	 */
	public CaracteristicasTipoImpl(Tipo tipo,
			Caracteristica caracteristica, int linea) {
		super();
		this.idTipo = tipo;
		this.idCaracteristica = caracteristica;
		this.linea = linea;
	}

//	public int getIdCaracteristicaTipo() {
//		return idCaracteristicaTipo;
//	}
//
//	public void setIdCaracteristicaTipo(int idCaracteristicaTipo) {
//		this.idCaracteristicaTipo = idCaracteristicaTipo;
//	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo#getIdTipo()
	 */
	public Tipo getIdTipo() {
		return idTipo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo#setIdTipo(org.sakaiproject.lessonbuildertool.model.Tipo)
	 */
	public void setIdTipo(Tipo idTipo) {
		this.idTipo = idTipo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo#getLinea()
	 */
	public int getLinea() {
		return linea;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo#setLinea(int)
	 */
	public void setLinea(int linea) {
		this.linea = linea;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo#setIdCaracteristica(org.sakaiproject.lessonbuildertool.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}

}