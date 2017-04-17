package co.edu.javeriana.ASHYI.model;

import java.util.Date;

/**
 * Interface de un Item Plan
 * Representa a un Item Plan en ASHYI
 * A diferencia de la actividad, el item plan tiene asociado datos con el uso de la actividad
 * @author ASHYI
 * @see ItemPlanImpl
 */

public interface ItemPlan {
	/**
	 * @return Actividad relacionada
	 */
	public Actividad getIdActividad();
	/**
	 * @param idActividad Actividad relacionada
	 */
	public void setIdActividad(Actividad idActividad);
	/**
	 * @return esta Activo o no
	 */
	public boolean isEstaActivo();
	/**
	 * @param estaActivo esta Activo o no
	 */
	public void setEstaActivo(boolean estaActivo);
	/**
	 * @return Recurso asociado
	 */
	public Recurso getIdRecurso();
	/**
	 * @param idRecurso Recurso asociado
	 */
	public void setIdRecurso(Recurso idRecurso);
	/**
	 * @return orden que representa el item plan dentro del grafo
	 */
	public Integer getOrden();
	/**
	 * @param orden orden que representa el item plan dentro del grafo
	 */
	public void setOrden(Integer orden);
	/**
	 * @return id de Item Plan
	 */
	public Integer getIdItemPlan();
	/**
	 * @param idItemPlan id de Item Plan
	 */
	public void setIdItemPlan(Integer idItemPlan);
	/**
	 * @return tiempo de duracion del item plan
	 */
	public float getTiempo_duracion();
	/**
	 * @param tiempo_duracion tiempo de duracion del item plan
	 */
	public void setTiempo_duracion(float tiempo_duracion);
	/**
	 * @return fecha inicial
	 */
	public Date getFecha_inicial();
	/**
	 * @param fecha_inicial fecha inicial
	 */
	public void setFecha_inicial(Date fecha_inicial);
	/**
	 * @return fecha de vencimiento del item plan
	 */
	public Date getFecha_final();
	/**
	 * @param fecha_final fecha de vencimiento del item plan
	 */
	public void setFecha_final(Date fecha_final);
	/**
	 * Calcular el tiempo de duracion del item plan, 
	 * dependiendo del las fechas de inicio y final 
	 */
	public void setTiempo_duracion();
	/**
	 * @param fechaInicialObjetivoUD fecha inicial
	 * @param fechaFinalObjetivoUD fecha final
	 */
	public void setFechas(String fechaInicialObjetivoUD, String fechaFinalObjetivoUD);
	/**
	 * @return Actividad recursiva relacionada
	 */
	public Actividad getIdUnidadDidacticaPadre();
	/**
	 * @param idActividad Actividad recursiva relacionada
	 */
	public void setIdUnidadDidacticaPadre(Actividad idActividad);
}
