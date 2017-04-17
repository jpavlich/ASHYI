package edu.javeriana;

import java.io.Serializable;

import BESA.Kernel.Agent.StateBESA;


public class EstadoAgente extends StateBESA implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatosEstado datos;

	public EstadoAgente(DatosEstado datos) {
		super();
		this.datos = datos;
	}

	public DatosEstado getDatos() {
		return datos;
	}

	public void setDatos(DatosEstado datos) {
		this.datos = datos;
	} 
	
	

}
