package org.sakaiproject.lessonbuildertool.model;

import java.util.List;

/**
 * Interface de actividad
 * Representa a una actividad en ASHYI
 * Puede ser: compuesta macro, compuesta recursiva o atomica
 * @author ASHYI
 * @see ActividadImpl
 */

public interface Actividad {
	
		
	/**
	 * @return id de la actividad
	 */
	public Integer getIdActividad() ;

	/**
	 * @param idActividad id de la actividad
	 */
	public void setIdActividad(Integer idActividad);

	/**
	 * @return Nombre de actividad
	 */
	public String getNombre();

	/**
	 * @param nombre Nombre de actividad
	 */
	public void setNombre(String nombre);

	/**
	 * @return nivel de recursividad de la actividad
	 */
	public Integer getNivel_recursividad();

	/**
	 * @param nivel_recursividad nivel de recursividad de la actividad
	 */
	public void setNivel_recursividad(Integer nivel_recursividad);

	/**
	 * @return recursos asociados a la actividad
	 */
	public List<RecursosActividad> getRecursos();

	/**
	 * @param  recursos recursos asociados a la actividad
	 */
	public void setRecursos(List<RecursosActividad> recursos);

//	public String getObjetivo();
//
//	public void setObjetivo(String objetivo);

	/**
	 * @return  descripcion de la actividad
	 */
	public String getDescripcion();

	/**
	 * @param descripcion descripcion de la actividad
	 */
	public void setDescripcion(String descripcion);	
	
	/**
	 * @return caracteristicas asociadas a la actividad
	 */
	public List<CaracteristicaActividad> getCaracteristicas();

	/**
	 * @param caracteristicas caracteristicas asociadas a la actividad
	 */
	public void setCaracteristicas(List<CaracteristicaActividad> caracteristicas);
	
	/**
	 * @param dependencias dependencias asociadas a la actividad
	 */
	public void setDependencias(List<DependenciaActividad> dependencias);
	
	/**
	 * @return dependencias asociadas a la actividad
	 */
	public List<DependenciaActividad> getDependencias();
	
	/**
	 * @return actividades asociadas a una actividad de mayor nivel  de recursividad
	 */
	public List<ActividadTieneActividad> getActividades();
	
	/**
	 * @param actividades actividades asociadas a una actividad de mayor nivel  de recursividad 
	 */
	public void setActividades(List<ActividadTieneActividad> actividades);	

	/**
	 * @return objetivos asociados a la actividad
	 */
	public List<ObjetivosActividad> getObjetivo();

	/**
	 * @param objetivo objetivos asociados a la actividad
	 */
	public void setObjetivo(List<ObjetivosActividad> objetivo);
	
	/**
	 * @return tipo de la actividad
	 */
	public Tipo getIdTipo();

	/**
	 * @param idTipo tipo de la actividad
	 */
	public void setIdTipo(Tipo idTipo);
	
	/**
	 * @return item de la actividad (Actividad Macro - Actividad Recursiva - Actividad Atomica)
	 */
	public Item getIdItem();

	/**
	 * @param idItem item de la actividad (Actividad Macro - Actividad Recursiva - Actividad Atomica)
	 */
	public void setIdItem(Item idItem);
	
//	public boolean isEs_inicial();
//
//	public void setEs_inicial(boolean es_inicial);
//	
//	public boolean isEs_final();
//
//	public void setEs_final(boolean es_final);
	
	/**
	 * @return dedicacion de la actividad
	 */
	public Integer getDedicacion();

	/**
	 * @param dedicacion dedicacion de la actividad
	 */
	public void setDedicacion(Integer dedicacion);
	
	/**
	 * @return Nivel de la actividad (i.e. "Universitario")
	 */
	public String getNivel();

	/**
	 * @param nivel Nivel de la actividad (i.e. "Universitario")
	 */
	public void setNivel(String nivel);
	
	/**
	 * @return si la actividad es de refuerzo
	 */
	public boolean isEs_refuerzo();

	/**
	 * @param es_refuerzo si la actividad es de refuerzo
	 */
	public void setEs_refuerzo(boolean es_refuerzo);
	
}