package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicaRecurso;
import co.edu.javeriana.ASHYI.model.Recurso;

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
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaRecurso#getIdRecurso()
	 */
	public Recurso getIdRecurso() {
		return idRecurso;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaRecurso#setIdRecurso(co.edu.javeriana.ASHYI.model.Recurso)
	 */
	public void setIdRecurso(Recurso idRecurso) {
		this.idRecurso = idRecurso;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaRecurso#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaRecurso#setIdCaracteristica(co.edu.javeriana.ASHYI.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}	
}