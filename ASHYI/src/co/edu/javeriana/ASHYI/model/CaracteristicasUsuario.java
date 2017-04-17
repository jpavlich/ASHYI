package co.edu.javeriana.ASHYI.model;

/**
 * Interface de Caracteristicas de un usuario
 * Representa a una caracteristica que un usuario posee en ASHYI
 * @author ASHYI
 * @see CaracteristicasUsuarioImpl
 */

public interface CaracteristicasUsuario {
	
//	/**
//	 * @return
//	 */
//	public int getIdCaracteristicaUsuario();
//
//	/**
//	 * @param idCaracteristicaUsuario
//	 */
//	public void setIdCaracteristicaUsuario(int idCaracteristicaUsuario);

	/**
	 * @return Usuario asociado
	 */
	public Usuario getIdUsuario();

	/**
	 * @param idUsuario Usuario asociado
	 */
	public void setIdUsuario(Usuario idUsuario);

	/**
	 * @return Nivel de la caracteristica para el usuario
	 */
	public int getNivel();

	/**
	 * @param nivel Nivel de la caracteristica para el usuario
	 */
	public void setNivel(int nivel);

	/**
	 * @return Caracteristica asociada
	 */
	public Caracteristica getIdCaracteristica();

	/**
	 * @param idCaracteristica Caracteristica asociada
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica);

}
