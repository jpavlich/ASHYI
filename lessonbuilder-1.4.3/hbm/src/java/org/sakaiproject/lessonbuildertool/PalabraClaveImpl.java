package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.PalabraClave;

/**
 * @author ASHYI
 * @see PalabraClave
 */

public class PalabraClaveImpl implements PalabraClave, Serializable {
	
	private int idPalabraClave;
	private String palabra;
	private int tipo;//1 verbo 2 complemento
	
	/**
	 * Constructor base
	 */
	public PalabraClaveImpl() {
	}
	
	/**
	 * @param idPalabraClave
	 * @param palabra clave a agregar
	 * @param tipo: 1 si es verbo, 2 si es complemento
	 */
	public PalabraClaveImpl(int idPalabraClave, String palabra, int tipo) {
		super();
		this.idPalabraClave = idPalabraClave;
		this.palabra = palabra;
		this.tipo = tipo;
	}


	/**
	 * @param palabra clave a agregar
	 * @param tipo: 1 si es verbo, 2 si es complemento
	 */
	public PalabraClaveImpl(String palabra, int tipo) {
		super();
		this.palabra = palabra;
		this.tipo = tipo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClave#getIdPalabraClave()
	 */
	public int getIdPalabraClave() {
		return idPalabraClave;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClave#setIdPalabraClave(int)
	 */
	public void setIdPalabraClave(int idPalabraClave) {
		this.idPalabraClave = idPalabraClave;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClave#getPalabra()
	 */
	public String getPalabra() {
		return palabra;
	}
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClave#setPalabra(java.lang.String)
	 */
	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClave#getTipo()
	 */
	public int getTipo() {
		return tipo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.PalabraClave#setTipo(int)
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	

}
