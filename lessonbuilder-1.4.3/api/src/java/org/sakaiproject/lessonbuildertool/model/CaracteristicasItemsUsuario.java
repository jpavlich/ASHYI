package org.sakaiproject.lessonbuildertool.model;

import java.util.Date;

/**
 * Interface de Caracteristicas de un item de usuario
 * Representa a una caracteristica que un item usuario posee en ASHYI
 * @author ASHYI
 * @see CaracteristicasItemsUsuarioImpl
 */

public interface CaracteristicasItemsUsuario {

	/**
	 * @return Caracteristica asociada
	 */
	public Caracteristica getIdCaracteristica();

	/**
	 * @param idCaracteristica Caracteristica asociada
	 */
	public void setIdCaracteristica(Caracteristica idCaracteristica);

	/**
	 * @return Item plan asociado
	 */
	public ItemPlan getIdItemPlan();

	/**
	 * @param idItemPlan Item plan asociado
	 */
	public void setIdItemPlan(ItemPlan idItemPlan);

	/**
	 * @return Usuario asociado
	 */
	public Usuario getIdUsuario();

	/**
	 * @param idUsuario Usuario asociado
	 */
	public void setIdUsuario(Usuario idUsuario);

	/**
	 * @return estado de la caracteristica (adquirida o no)
	 */
	public boolean isEstado();

	/**
	 * @param estado Estado de la caracteristica (adquirida o no)
	 */
	public void setEstado(boolean estado);
	
	public Date getFecha();

	public void setFecha(Date fecha);
}
