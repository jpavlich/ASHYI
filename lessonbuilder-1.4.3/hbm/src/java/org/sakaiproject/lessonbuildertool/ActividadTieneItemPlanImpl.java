package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.ActividadTieneItemPlanImpl;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneActividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneItemPlan;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;


public class ActividadTieneItemPlanImpl implements ActividadTieneItemPlan, Serializable{
	
	private Actividad idActividad;
	private ItemPlan idItemPlan;
	private String estiloItemPlan;
	
	/**
	 * Constructor base
	 */
	public ActividadTieneItemPlanImpl() {
	}
	
	/**
	 * @param idActividad
	 * @param idItemPlan
	 * @param estiloItemPlan
	 */
	public ActividadTieneItemPlanImpl(Actividad idActividad,
			ItemPlan idItemPlan, String estiloItemPlan) {
		super();
		this.idActividad = idActividad;
		this.idItemPlan = idItemPlan;
		this.estiloItemPlan = estiloItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ActividadTieneItemPlan#getIdActividad()
	 */
	@Override
	public Actividad getIdActividad() {
		return this.idActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ActividadTieneItemPlan#setIdActividad(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	@Override
	public void setIdActividad(Actividad idActividad) {
		this.idActividad=idActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ActividadTieneItemPlan#getIdItemPlan()
	 */
	@Override
	public ItemPlan getIdItemPlan() {
		return this.idItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ActividadTieneItemPlan#setIdItemPlan(org.sakaiproject.lessonbuildertool.model.ItemPlan)
	 */
	@Override
	public void setIdItemPlan(ItemPlan id_ItemPlan) {
		this.idItemPlan=idItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ActividadTieneItemPlan#getEstiloItemPlan()
	 */
	@Override
	public String getEstiloItemPlan() {
		return this.estiloItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ActividadTieneItemPlan#setEstiloItemPlan(java.lang.String)
	 */
	@Override
	public void setEstiloItemPlan(String estiloItemPlan) {
		this.estiloItemPlan=estiloItemPlan;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		Integer hsCode=0;
		hsCode= this.idActividad.hashCode();
		hsCode= 17 * hsCode + this.getIdActividad().getIdActividad();
		hsCode= 31* hsCode + this.getIdItemPlan().getIdItemPlan();
		return hsCode;
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object ati) {
    	ActividadTieneItemPlanImpl dep=(ActividadTieneItemPlanImpl)ati;
        if (dep == null)
            return false;
        if (dep.getIdActividad().getIdActividad().equals(this.idActividad.getIdActividad())  && 
        	dep.getIdItemPlan().getIdItemPlan().equals(this.getIdItemPlan().getIdItemPlan()))
            return true;
        return false;
    }
}