package co.edu.javeriana.ASHYI.hbm;

import java.util.List;

import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.ActividadTieneActividad;
import co.edu.javeriana.ASHYI.model.CaracteristicaActividad;
import co.edu.javeriana.ASHYI.model.DependenciaActividad;
import co.edu.javeriana.ASHYI.model.Item;
import co.edu.javeriana.ASHYI.model.ObjetivosActividad;
import co.edu.javeriana.ASHYI.model.RecursosActividad;
import co.edu.javeriana.ASHYI.model.Tipo;

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
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getDedicacion()
	 */
	public Integer getDedicacion() {
		return dedicacion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setDedicacion(java.lang.Integer)
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
	 * @param objetivo de la actividad a asociar
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
	 * @param actividad de nivel de recursividad mas bajo a asociar (actividad hija)
	 */
	public void addActividades(ActividadTieneActividad actividad)
	{
		getActividades().add(actividad);
	}

	/**
	 * @param caracterisitca a asociar a la actividad
	 */
	public void addCaracterisitcas(CaracteristicaActividad caracterisitca)
	{
		getCaracteristicas().add(caracterisitca);
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setDependencias(java.util.List)
	 */
	public void setDependencias(List<DependenciaActividad> dependencias) {
		this.dependencias = dependencias;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getCaracteristicas()
	 */
	public List<CaracteristicaActividad> getCaracteristicas() {
		return caracteristicas;
	}


	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setCaracteristicas(java.util.List)
	 */
	public void setCaracteristicas(List<CaracteristicaActividad> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getIdActividad()
	 */
	public Integer getIdActividad() {
		return idActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setIdActividad(java.lang.Integer)
	 */
	public void setIdActividad(Integer idActividad) {
		this.idActividad = idActividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getIdTipo()
	 */
	public Tipo getIdTipo() {
		return idTipo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setIdTipo(co.edu.javeriana.ASHYI.model.Tipo)
	 */
	public void setIdTipo(Tipo idTipo) {
		this.idTipo = idTipo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getNombre()
	 */
	public String getNombre() {
		return nombre;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setNombre(java.lang.String)
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getNivel_recursividad()
	 */
	public Integer getNivel_recursividad() {
		return nivel_recursividad;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setNivel_recursividad(java.lang.Integer)
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
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getDescripcion()
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setDescripcion(java.lang.String)
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getDependencias()
	 */
	public List<DependenciaActividad> getDependencias() {
		return dependencias;
	}
	
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getActividades()
	 */
	public List<ActividadTieneActividad> getActividades() {
		return actividades;
	}


	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setActividades(java.util.List)
	 */
	public void setActividades(List<ActividadTieneActividad> actividades) {
		this.actividades = actividades;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getObjetivo()
	 */
	public List<ObjetivosActividad> getObjetivo() {
		return objetivo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setObjetivo(java.util.List)
	 */
	public void setObjetivo(List<ObjetivosActividad> objetivo) {
		this.objetivo = objetivo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getIdItem()
	 */
	public Item getIdItem() {
		return idItem;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setIdItem(co.edu.javeriana.ASHYI.model.Item)
	 */
	public void setIdItem(Item idItem) {
		this.idItem = idItem;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getRecursos()
	 */
	public List<RecursosActividad> getRecursos() {
		return recursos;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setRecursos(java.util.List)
	 */
	public void setRecursos(List<RecursosActividad> recursos) {
		this.recursos = recursos;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#getNivel()
	 */
	public String getNivel() {
		return nivel;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setNivel(java.lang.String)
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#isEs_refuerzo()
	 */
	public boolean isEs_refuerzo() {
		return es_refuerzo;
	}

	/* (non-Javadoc)
	 * @see co.edu.javeriana.ASHYI.model.Actividad#setEs_refuerzo(boolean)
	 */
	public void setEs_refuerzo(boolean es_refuerzo) {
		this.es_refuerzo = es_refuerzo;
	}
	
}