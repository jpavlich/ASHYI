package co.edu.javeriana.ASHYI.model;

/**
 * Interface de una palabra clave que conforma un objetivo
 * Representa a una palabra clave en ASHYI
 * @author ASHYI
 * @see PalabraClaveImpl
 */

public interface PalabraClave {
	
	/**
	 * @return id de la palabra clave
	 */
	public int getIdPalabraClave();
	/**
	 * @param idPalabraClave id de la palabra clave
	 */
	public void setIdPalabraClave(int idPalabraClave);
	/**
	 * @return palabra clave a agregar
	 */
	public String getPalabra();
	/**
	 * @param palabra palabra clave a agregar
	 */
	public void setPalabra(String palabra);
	/**
	 * @return tipo: 1 si es verbo, 2 si es complemento
	 */
	public int getTipo();
	/**
	 * @param tipo 1 si es verbo, 2 si es complemento
	 */
	public void setTipo(int tipo);
}
