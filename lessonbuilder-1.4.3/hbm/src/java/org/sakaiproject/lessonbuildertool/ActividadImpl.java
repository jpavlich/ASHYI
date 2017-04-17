package org.sakaiproject.lessonbuildertool;

import java.util.List;

import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneActividad;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaActividad;
import org.sakaiproject.lessonbuildertool.model.DependenciaActividad;
import org.sakaiproject.lessonbuildertool.model.Item;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.RecursosActividad;
import org.sakaiproject.lessonbuildertool.model.Tipo;

/**
 * @author ASHYI
 * @see Actividad
 */

public class ActividadImpl implements Actividad {
	
	private List<CaracteristicaActividad> caracteristicas;
	private List<ObjetivosActividad> objetivo;
	private List<RecursosActividad> recursos;
	
	private Integer idActividad;
	private Item idItem;
	private Tipo idTipo;
	private String nombre;
	private Integer nivel_recursividad;
	//private Recurso idRecurso;	
	private String descripcion;
//	private boolean es_inicial;
//	private boolean es_final;
	private boolean es_refuerzo;
	private Integer dedicacion;
	private String nivel;
			
	private List<DependenciaActividad> dependencias;
	private List<ActividadTieneActividad> actividades;
	
	/**
	 * Constructor base
	 */
	public ActividadImpl() {}
		
//	public ActividadImpl(Tipo tipo, Item idItem, String name, String description, String dI, String dF, Integer nR, Integer dedicacion, String nivel)
//	{
//		try {	
//			SimpleDateFormat formatter;
//			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//			Date date = formatter.parse(dI.substring(0, 16));
//			//this.fecha_inicial =  date;//AAAA-MM-DDTHH:MM
//			//this.fecha_inicial =  new SimpleDateFormat("yyyy-MM-dd").parse(dI);//AAAA-MM-DDTHH:MM
//			date = formatter.parse(dF.substring(0, 16));
//			this.fecha_final = date;
//			
//			long fechaInicialMs = this.fecha_inicial.getTime();
//			long fechaFinalMs = this.fecha_final.getTime();
//			long diferencia = fechaFinalMs - fechaInicialMs;
//			float dias = (float) Math.floor(diferencia / (1000 * 60 * 60));		
//			
//			this.tiempo_duracion = dias;			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			this.idItem = idItem;
//			this.idTipo = tipo;
//			this.nombre = name;
//			this.nivel_recursividad = nR;
//			//this.objetivo = goal;
//			this.descripcion = description;		
//			this.dedicacion = dedicacion;
//	}
	
	/**
	 * @param tipo (i.e. exposicion - tarea )
	 * 	@see Tipo
	 * @param idItem
	 * 	@see Item
	 * @param name
	 * @param descripcion
	 * @param nivel de recursividad (1 - 2 - 3)
	 * @param dedicacion (en horas)
	 * @param nivel (nivel de actividad i.e. Universitario)
	 */
	public ActividadImpl(Tipo tipo, Item idItem, String name, String description, Integer nR, Integer dedicacion, String nivel)
	{		
			this.idItem = idItem;
			this.idTipo = tipo;
			this.nombre = name;
			this.nivel_recursividad = nR;
			//this.objetivo = goal;
			this.descripcion = description;		
			this.dedicacion = dedicacion;
			this.nivel = nivel;
	}
	
//	public ActividadImpl(
//			Integer idActividad, Item item, Tipo idTipo, String nombre, Integer nivel_recursividad, String objetivo, String descripcion,
//			Integer tiempo_duracion, Date fecha_inicial, Date fecha_final, Integer dedicacion) {
//		super();
//		this.caracteristicas = new ArrayList<CaracteristicaActividad>();
//		this.idActividad = idActividad;
//		this.idItem = item;
//		this.idTipo = idTipo;
//		this.nombre = nombre;
//		this.nivel_recursividad = nivel_recursividad;
//		//this.objetivo = objetivo;
//		this.descripcion = descripcion;
//		this.tiempo_duracion = tiempo_duracion;
//		this.fecha_inicial = fecha_inicial;
//		this.fecha_final = fecha_final;
//		this.dedicacion = dedicacion;
//		this.dependencias = new ArrayList<DependenciaActividad>();
//		this.actividades = new ArrayList<ActividadTieneActividad>();
//		this.objetivo = new ArrayList<ObjetivosActividad>();
//		this.recursos = new ArrayList<RecursosActividad>();
//	}
	
	/**
	 * @param idItem
	 * 	@see Item
	 * @param nombre
	 * @param nivel de recursividad (1 - 2 - 3)
	 * @param dedicacion (en horas)
	 * @param nivel (nivel de actividad i.e. Universitario)
	 */
	public ActividadImpl(Item item,
			String nombre, Integer nivel_recursividad, Integer dedicacion, String nivel) {
		super();
		this.idItem = item;
		this.nombre = nombre;
		this.nivel_recursividad = nivel_recursividad;
		this.dedicacion = dedicacion;
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getDedicacion()
	 */
	public Integer getDedicacion() {
		return dedicacion;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setDedicacion(java.lang.Integer)
	 */
	public void setDedicacion(Integer dedicacion) {
		this.dedicacion = dedicacion;
	}

//	public boolean isEs_inicial() {
//		return es_inicial;
//	}
//
//	public void setEs_inicial(boolean es_inicial) {
//		this.es_inicial = es_inicial;
//	}
//
//	public boolean isEs_final() {
//		return es_final;
//	}
//
//	public void setEs_final(boolean es_final) {
//		this.es_final = es_final;
//	}

	/**
	 * @param obj
	 */
	public void addObjetivo(ObjetivosActividad obj)
	{
		getObjetivo().add(obj);
	}
	
	/**
	 * @param actividad
	 */
	public void addDependencia(DependenciaActividad actividad)
	{
		getDependencias().add(actividad);
	}
	
	/**
	 * @param actividad
	 */
	public void addActividades(ActividadTieneActividad actividad)
	{
		getActividades().add(actividad);
	}

	/**
	 * @param caracterisitca
	 */
	public void addCaracterisitcas(CaracteristicaActividad caracterisitca)
	{
		getCaracteristicas().add(caracterisitca);
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setDependencias(java.util.List)
	 */
	public void setDependencias(List<DependenciaActividad> dependencias) {
		this.dependencias = dependencias;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getCaracteristicas()
	 */
	public List<CaracteristicaActividad> getCaracteristicas() {
		return caracteristicas;
	}


	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setCaracteristicas(java.util.List)
	 */
	public void setCaracteristicas(List<CaracteristicaActividad> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getIdActividad()
	 */
	public Integer getIdActividad() {
		return idActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setIdActividad(java.lang.Integer)
	 */
	public void setIdActividad(Integer idActividad) {
		this.idActividad = idActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getIdTipo()
	 */
	public Tipo getIdTipo() {
		return idTipo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setIdTipo(org.sakaiproject.lessonbuildertool.model.Tipo)
	 */
	public void setIdTipo(Tipo idTipo) {
		this.idTipo = idTipo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getNombre()
	 */
	public String getNombre() {
		return nombre;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setNombre(java.lang.String)
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getNivel_recursividad()
	 */
	public Integer getNivel_recursividad() {
		return nivel_recursividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setNivel_recursividad(java.lang.Integer)
	 */
	public void setNivel_recursividad(Integer nivel_recursividad) {
		this.nivel_recursividad = nivel_recursividad;
	}

//	public String getObjetivo() {
//		return objetivo;
//	}
//
//	public void setObjetivo(String objetivo) {
//		this.objetivo = objetivo;
//	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getDescripcion()
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setDescripcion(java.lang.String)
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getDependencias()
	 */
	public List<DependenciaActividad> getDependencias() {
		return dependencias;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getActividades()
	 */
	public List<ActividadTieneActividad> getActividades() {
		return actividades;
	}


	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setActividades(java.util.List)
	 */
	public void setActividades(List<ActividadTieneActividad> actividades) {
		this.actividades = actividades;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getObjetivo()
	 */
	public List<ObjetivosActividad> getObjetivo() {
		return objetivo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setObjetivo(java.util.List)
	 */
	public void setObjetivo(List<ObjetivosActividad> objetivo) {
		this.objetivo = objetivo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getIdItem()
	 */
	public Item getIdItem() {
		return idItem;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setIdItem(org.sakaiproject.lessonbuildertool.model.Item)
	 */
	public void setIdItem(Item idItem) {
		this.idItem = idItem;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getRecursos()
	 */
	public List<RecursosActividad> getRecursos() {
		return recursos;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setRecursos(java.util.List)
	 */
	public void setRecursos(List<RecursosActividad> recursos) {
		this.recursos = recursos;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#getNivel()
	 */
	public String getNivel() {
		return nivel;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setNivel(java.lang.String)
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#isEs_refuerzo()
	 */
	public boolean isEs_refuerzo() {
		return es_refuerzo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.Actividad#setEs_refuerzo(boolean)
	 */
	public void setEs_refuerzo(boolean es_refuerzo) {
		this.es_refuerzo = es_refuerzo;
	}
	
}