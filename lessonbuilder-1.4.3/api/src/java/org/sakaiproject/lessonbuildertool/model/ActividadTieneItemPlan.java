package org.sakaiproject.lessonbuildertool.model;

import java.io.Serializable;

public interface ActividadTieneItemPlan extends Serializable{	
	
	/**
	 * @return
	 */
	public Actividad getIdActividad();
	/**
	 * @param idActividad
	 */
	public void setIdActividad(Actividad idActividad);
	/**
	 * @return
	 */
	public ItemPlan getIdItemPlan();
	/**
	 * @param id_ItemPlan
	 */
	public void setIdItemPlan(ItemPlan id_ItemPlan);
	/**
	 * @return
	 */
	public String getEstiloItemPlan();
	/**
	 * @param estiloItemPlan
	 */
	public void setEstiloItemPlan(String estiloItemPlan);
	
}