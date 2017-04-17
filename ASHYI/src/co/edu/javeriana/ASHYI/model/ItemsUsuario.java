package co.edu.javeriana.ASHYI.model;

/**
 * Interface de un Item de Usuario
 * Representa a un item plan que debe ejecutar un usuario ejecutor en ASHYI
 * @author ASHYI
 * @see ItemsUsuarioImpl
 */

public interface ItemsUsuario {
	
	/**
	 * @return Usuario asociado
	 */
	public Usuario getIdUsuario();

	/**
	 * @param idUsuario Usuario asociado
	 */
	public void setIdUsuario(Usuario idUsuario);

	/**
	 * @return si ha sido realizada o no
	 */
	public boolean isRealizada();

	/**
	 * @param realizada si ha sido realizada o no
	 */
	public void setRealizada(boolean realizada);

	/**
	 * @return nota que se obtiene de la realizacion y evaluacion de una actividad atomica (si aplica)
	 */
	public double getNota();

	/**
	 * @param nota nota que se obtiene de la realizacion y evaluacion de una actividad atomica (si aplica)
	 */
	public void setNota(double nota);

	/**
	 * @return calificacion que le da el usuario a la actividad
	 */
	public double getCalificacion_usuario();

	/**
	 * @param calificacion_usuario calificacion que le da el usuario a la actividad
	 */
	public void setCalificacion_usuario(double calificacion_usuario);	
	
	/**
	 * @return estado: activo o no para el usuario en particular
	 */
	public boolean isEstaActivo();

	/**
	 * @param estado activo o no para el usuario en particular
	 */
	public void setEstaActivo(boolean estado);
	
	/**
	 * @return Item Plan asociado
	 */
	public ItemPlan getIdItemPlan();

	/**
	 * @param idItemPlan Item Plan asociado
	 */
	public void setIdItemPlan(ItemPlan idItemPlan);
}
