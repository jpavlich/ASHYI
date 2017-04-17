package org.sakaiproject.lessonbuildertool.model;

import java.util.Date;

/**
 * Interface de una respuesta asociada a in item de usuario
 * Representa a una respuesta que un usuario realiza para dar desarrollo a una actividad atomica en ASHYI
 * @author ASHYI
 * @see RespuestaItemsUsuarioImpl
 */

public interface RespuestaItemsUsuario {

	/**
	 * @return id de la respuesta
	 */
	public Integer getIdRespuesta();

	/**
	 * @param idRespuesta id de la respuesta
	 */
	public void setIdRespuesta(Integer idRespuesta);

	/**
	 * @return Usuario quien realiza la respuesta
	 */
	public Usuario getIdUsuario();

	/**
	 * @param idUsuario Usuario quien realiza la respuesta
	 */
	public void setIdUsuario(Usuario idUsuario);
	
	/**
	 * @return ItemPlan asociado a la respuesta
	 */
	public ItemPlan getIdItemPlan();

	/**
	 * @param idItemPlan ItemPlan asociado a la respuesta
	 */
	public void setIdItemPlan(ItemPlan idItemPlan);	

	/**
	 * @return comentarios del usuario ejecutor a la respuesta
	 */
	public String getRetroalimentacion_usuario();

	/**
	 * @param retroalimentacion_usuario comentarios del usuario ejecutor a la respuesta
	 */
	public void setRetroalimentacion_usuario(String retroalimentacion_usuario);

	/**
	 * @return feedback del usuario planeador de la respuesta
	 */
	public String getRetroalimentacion_profesor();

	/**
	 * @param retroalimentacion_profesor feedback de la respuesta
	 */
	public void setRetroalimentacion_profesor(String retroalimentacion_profesor);	
	
	/**
	 * @return nombre del archivo (si aplica)
	 */
	public String getArchivo();
	
	/**
	 * @param nombreArchivo nombre del archivo(si aplica)
	 */
	public void setArchivo(String nombreArchivo);
	
	/**
	 * @return fecha de realizacion de la respuesta
	 */
	public Date getFecha();

	/**
	 * @param fecha fecha de realizacion de la respuesta
	 */
	public void setFecha(Date fecha);
	
}
