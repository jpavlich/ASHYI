package co.edu.javeriana.ASHYI.hbm;

import java.io.Serializable;

import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicasTipo;
import co.edu.javeriana.ASHYI.model.Tipo;

/**
 * @author ASHYI
 * @see CaracteristicasTipo
 */
public class CaracteristicasTipoImpl implements CaracteristicasTipo, Serializable{
	
	private int idCaracteristicaTipo;
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

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasTipo#getIdCaracteristicaTipo()
	 */
	public int getIdCaracteristicaTipo() {
		return idCaracteristicaTipo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasTipo#setIdCaracteristicaTipo(int)
	 */
	public void setIdCaracteristicaTipo(int idCaracteristicaTipo) {
		this.idCaracteristicaTipo = idCaracteristicaTipo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasTipo#getIdTipo()
	 */
	public Tipo getIdTipo() {
		return idTipo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasTipo#setIdTipo(co.edu.javeriana.ASHYI.model.Tipo)
	 */
	public void setIdTipo(Tipo idTipo) {
		this.idTipo = idTipo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasTipo#getLinea()
	 */
	public int getLinea() {
		return linea;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasTipo#setLinea(int)
	 */
	public void setLinea(int linea) {
		this.linea = linea;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasTipo#getIdCaracteristica()
	 */
	public Caracteristica getIdCaracteristica() {
		return idCaracteristica;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.CaracteristicasTipo#setIdCaracteristica(co.edu.javeriana.ASHYI.model.Caracteristica)
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}

}