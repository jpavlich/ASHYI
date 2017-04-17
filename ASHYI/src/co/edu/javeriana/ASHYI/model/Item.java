package co.edu.javeriana.ASHYI.model;

/**
 * Interface de un Item
 * Representa a una item en ASHYI i.e. Actividad macro, activida recursiva, actividad atomica, 
 * Estilo Aprendizaje, Habilidad, Competencia, Personalidad, Contexto, Situacion de aprendizaje
 * @author ASHYI
 * @see ItemImpl
 */

public interface Item {
	
	/**
	 * @return id del item
	 */
	public int getIdItem();
	/**
	 * @param idItem id del item
	 */
	public void setIdItem(int idItem);
	/**
	 * @return Nombre del item
	 */
	public String getNombre();
	/**
	 * @param nombre Nombre del item
	 */
	public void setNombre(String nombre);
	/**
	 * @return test por el que se define el item
	 */
	public String getTest_definicion();
	/**
	 * @param test_definicion test por el que se define el item
	 */
	public void setTest_definicion(String test_definicion);
	
}