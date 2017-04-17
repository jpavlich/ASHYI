package org.sakaiproject.lessonbuildertool.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sakaiproject.lessonbuildertool.model.Caracteristica;

public class ActividadEntity {
	
	private List<Caracteristica> caracteristicas;
	
	private int idActividad;
	private int idTipo;
	private String nombre;
	private int nivel_recursividad;
	private int recurso;	
	private String objetivo;
	private String descripcion;
	private int tiempo_duracion;
	private Date fecha_inicial;
	private Date fecha_final;
	
	private List<ActividadEntity> dependencias;
	private List<ActividadEntity> actividades;
	
	public ActividadEntity(List<Caracteristica> caracteristicasA,
			int idActividad, int idTipo, String nombre, int nivel_recursividad,
			int recurso, String objetivo, String descripcion,
			int tiempo_duracion, Date fecha_inicial, Date fecha_final) {
		super();
		this.caracteristicas = caracteristicasA;
		this.idActividad = idActividad;
		this.idTipo = idTipo;
		this.nombre = nombre;
		this.nivel_recursividad = nivel_recursividad;
		this.recurso = recurso;
		this.objetivo = objetivo;
		this.descripcion = descripcion;
		this.tiempo_duracion = tiempo_duracion;
		this.fecha_inicial = fecha_inicial;
		this.fecha_final = fecha_final;
		this.dependencias = new ArrayList<ActividadEntity>();
		this.actividades = new ArrayList<ActividadEntity>();
	}

	
	public List<Caracteristica> getCaracteristicas() {
		return caracteristicas;
	}


	public void setCaracteristicas(List<Caracteristica> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}


	public List<ActividadEntity> getActividades() {
		return actividades;
	}


	public void setActividades(List<ActividadEntity> actividades) {
		this.actividades = actividades;
	}
	
	public int getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	public int getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(int idTipo) {
		this.idTipo = idTipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNivel_recursividad() {
		return nivel_recursividad;
	}

	public void setNivel_recursividad(int nivel_recursividad) {
		this.nivel_recursividad = nivel_recursividad;
	}

	public int getRecurso() {
		return recurso;
	}

	public void setRecurso(int recurso) {
		this.recurso = recurso;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getTiempo_duracion() {
		return tiempo_duracion;
	}

	public void setTiempo_duracion(int tiempo_duracion) {
		this.tiempo_duracion = tiempo_duracion;
	}

	public Date getFecha_inicial() {
		return fecha_inicial;
	}

	public void setFecha_inicial(Date fecha_inicial) {
		this.fecha_inicial = fecha_inicial;
	}

	public Date getFecha_final() {
		return fecha_final;
	}

	public void setFecha_final(Date fecha_final) {
		this.fecha_final = fecha_final;
	}

	public List<ActividadEntity> getDependencias() {
		return dependencias;
	}

	public void setDependencias(List<ActividadEntity> dependencias) {
		this.dependencias = dependencias;
	}
	
	
}