package co.edu.javeriana.ASHYI.model;

/**
 * Interface de Caracteristica
 * Representa a una caracteristica en ASHYI
 * En ASHYI-EDU i.e. estilo de aprendizaje, habilidad, personalidad...
 * @author ASHYI
 * @see CaracteristicaImpl
 */

public interface Caracteristica {	
	
	/**
	 * @return Item (i.e. Estilo Aprendizaje - Habilidad - Competencia - Personalidad - Contexto - Situacion de aprendizaje)
	 */
	public Item getIdItem();
	/**
	 * @param idItem Item (i.e. Estilo Aprendizaje - Habilidad - Competencia - Personalidad - Contexto - Situacion de aprendizaje)
	 */
	public void setIdItem(Item idItem);
	/**
	 * @return idCaracteristica id de la caracteristica
	 */
	public int getIdCaracteristica();
	/**
	 * @param idCaracteristica id de la caracteristica
	 */
	public void setIdCaracteristica(int idCaracteristica);
	/**
	 * @return nombre de la caracteristica
	 */
	public String getNombre();
	/**
	 * @param nombre de la caracteristica
	 */
	public void setNombre(String nombre);		
	
}