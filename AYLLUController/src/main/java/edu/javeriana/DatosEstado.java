package edu.javeriana;

import java.io.Serializable;

public class DatosEstado implements Serializable{

	private int numero;
	private String mensaje;
	
	
	public DatosEstado(int numero, String mensaje) {
		super();
		this.numero = numero;
		this.mensaje = mensaje;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
}
