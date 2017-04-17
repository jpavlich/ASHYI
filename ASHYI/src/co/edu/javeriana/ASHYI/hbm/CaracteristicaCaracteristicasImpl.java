package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicaCaracteristicas;

/**
 * @author ASHYI
 * @see CaracteristicasRecurso
 */
public class CaracteristicaCaracteristicasImpl implements CaracteristicaCaracteristicas, Serializable{
	
	private Caracteristica idCaracteristica;
	private Caracteristica idCaracteristicaRelacion;
	
	/**
	 * Constructor base
	 */
	public CaracteristicaCaracteristicasImpl()
	{}
		

	/**
	 * @param idCaracteristica
	 * @param idCaracteristicaRelacion
	 */
	public CaracteristicaCaracteristicasImpl(Caracteristica idCaracteristica,
			Caracteristica idCaracteristicaRelacion) {
		super();
		this.idCaracteristica = idCaracteristica;
		this.idCaracteristicaRelacion = idCaracteristicaRelacion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaCaracteristicas#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaCaracteristicas#setIdCaracteristica(co.edu.javeriana.ASHYI.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaCaracteristicas#getIdCaracteristicaRelacion()
	 */
	public Caracteristica getIdCaracteristicaRelacion() {
		return idCaracteristicaRelacion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaCaracteristicas#setIdCaracteristicaRelacion(co.edu.javeriana.ASHYI.model.Caracteristica)
	 */
	public void setIdCaracteristicaRelacion(Caracteristica idCaracteristicaRelacion) {
		this.idCaracteristicaRelacion = idCaracteristicaRelacion;
	}

	
}