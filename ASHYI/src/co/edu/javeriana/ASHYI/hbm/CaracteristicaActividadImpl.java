package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicaActividad;

/**
 * @author ASHYI
 * @see CaracteristicaActividad
 *
 */
public class CaracteristicaActividadImpl implements CaracteristicaActividad, Serializable{
	
	private int idCaracteristicaActividad;
	private Actividad idActividad;
	private Caracteristica idCaracteristica;
	private boolean precondicion;
	private boolean postcondicion;
	private int prioridad;
	//private int orden;
	
	/**
	 * Constructor general
	 */
	public CaracteristicaActividadImpl()
	{}
	
	/**
	 * @param actividad
	 * @param caracteristica
	 * @param si es precondicion
	 * @param si es postcondicion
	 * @param prioridad
	 */
	public CaracteristicaActividadImpl(Actividad actividad,
			Caracteristica caracteristica, boolean precondicion,
			boolean postcondicion, int prioridad) {
		super();
		this.idActividad = actividad;
		this.idCaracteristica = caracteristica;
		this.precondicion = precondicion;
		this.postcondicion = postcondicion;
		this.prioridad = prioridad;
	}

	/**
	 * @param idCaracteristicaActividad
	 * @param Actividad
	 * @param Caracteristica
	 * @param si es precondicion
	 * @param si es postcondicion
	 * @param prioridad
	 */
	public CaracteristicaActividadImpl(int idCaracteristicaActividad,
			Actividad idActividad, Caracteristica idCaracteristica,
			boolean precondicion, boolean postcondicion,			
			int prioridad) {
		super();
		this.idCaracteristicaActividad = idCaracteristicaActividad;
		this.idActividad = idActividad;
		this.idCaracteristica = idCaracteristica;
		this.precondicion = precondicion;
		this.postcondicion = postcondicion;
		this.prioridad = prioridad;
	}

//	public int getOrden() {
//		return orden;
//	}
//
//	public void setOrden(int orden) {
//		this.orden = orden;
//	}
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#getIdCaracteristicaActividad()
	 */
	public int getIdCaracteristicaActividad() {
		return idCaracteristicaActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#setIdCaracteristicaActividad(int)
	 */
	public void setIdCaracteristicaActividad(int idCaracteristicaActividad) {
		this.idCaracteristicaActividad = idCaracteristicaActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#getIdActividad()
	 */
	public Actividad getIdActividad() {
		return idActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#setIdActividad(co.edu.javeriana.ASHYI.model.Actividad)
	 */
	public void setIdActividad(Actividad idActividad) {
		this.idActividad = idActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#setIdCaracteristica(co.edu.javeriana.ASHYI.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#isPrecondicion()
	 */
	public boolean isPrecondicion() {
		return precondicion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#setPrecondicion(boolean)
	 */
	public void setPrecondicion(boolean precondicion) {
		this.precondicion = precondicion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#isPostcondicion()
	 */
	public boolean isPostcondicion() {
		return postcondicion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#setPostcondicion(boolean)
	 */
	public void setPostcondicion(boolean postcondicion) {
		this.postcondicion = postcondicion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#getPrioridad()
	 */
	public int getPrioridad() {
		return prioridad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicaActividad#setPrioridad(int)
	 */
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}	
	
}