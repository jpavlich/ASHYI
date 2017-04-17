package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;

import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadesUsuario;
import org.sakaiproject.lessonbuildertool.model.Usuario;


public class ActividadesUsuarioImpl implements ActividadesUsuario, Serializable{
	
	private int idActividadUsuario;
	private Actividad idActividad;
	private Usuario idUsuario;
	private boolean realizada;
	private double nota;
	private int calificacion_usuario;
	private String retroalimentacion_usuario;
	private String retroalimentacion_profesor;
	
	public ActividadesUsuarioImpl()
	{}

	public ActividadesUsuarioImpl(int idActividadUsuario,
			Actividad idActividad, Usuario idUsuario, boolean realizada,
			double nota, int calificacion_usuario,
			String retroalimentacion_usuario, String retroalimentacion_profesor) {
		super();
		this.idActividadUsuario = idActividadUsuario;
		this.idActividad = idActividad;
		this.idUsuario = idUsuario;
		this.realizada = realizada;
		this.nota = nota;
		this.calificacion_usuario = calificacion_usuario;
		this.retroalimentacion_usuario = retroalimentacion_usuario;
		this.retroalimentacion_profesor = retroalimentacion_profesor;
	}

	public ActividadesUsuarioImpl(Actividad idActividad, Usuario idUsuario,
			boolean realizada, double nota, int calificacion_usuario,
			String retroalimentacion_usuario, String retroalimentacion_profesor) {
		super();
		this.idActividad = idActividad;
		this.idUsuario = idUsuario;
		this.realizada = realizada;
		this.nota = nota;
		this.calificacion_usuario = calificacion_usuario;
		this.retroalimentacion_usuario = retroalimentacion_usuario;
		this.retroalimentacion_profesor = retroalimentacion_profesor;
	}

	public int getIdActividadUsuario() {
		return idActividadUsuario;
	}

	public void setIdActividadUsuario(int idActividadUsuario) {
		this.idActividadUsuario = idActividadUsuario;
	}

	public Actividad getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(Actividad idActividad) {
		this.idActividad = idActividad;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public boolean isRealizada() {
		return realizada;
	}

	public void setRealizada(boolean realizada) {
		this.realizada = realizada;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}

	public int getCalificacion_usuario() {
		return calificacion_usuario;
	}

	public void setCalificacion_usuario(int calificacion_usuario) {
		this.calificacion_usuario = calificacion_usuario;
	}

	public String getRetroalimentacion_usuario() {
		return retroalimentacion_usuario;
	}

	public void setRetroalimentacion_usuario(String retroalimentacion_usuario) {
		this.retroalimentacion_usuario = retroalimentacion_usuario;
	}

	public String getRetroalimentacion_profesor() {
		return retroalimentacion_profesor;
	}

	public void setRetroalimentacion_profesor(String retroalimentacion_profesor) {
		this.retroalimentacion_profesor = retroalimentacion_profesor;
	}
	
}