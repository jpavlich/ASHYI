package org.sakaiproject.lessonbuildertool.model;

/**
 * Interface de actividad de usuario
 * @author ASHYI
 * @see ActividadesUsuarioImpl
 */

public interface ActividadesUsuario {

	public int getIdActividadUsuario();

	public void setIdActividadUsuario(int idActividadUsuario);

	public Actividad getIdActividad();

	public void setIdActividad(Actividad idActividad);

	public Usuario getIdUsuario();

	public void setIdUsuario(Usuario idUsuario);

	public boolean isRealizada();

	public void setRealizada(boolean realizada);

	public double getNota();

	public void setNota(double nota);

	public int getCalificacion_usuario();

	public void setCalificacion_usuario(int calificacion_usuario);

	public String getRetroalimentacion_usuario();

	public void setRetroalimentacion_usuario(String retroalimentacion_usuario);

	public String getRetroalimentacion_profesor();

	public void setRetroalimentacion_profesor(String retroalimentacion_profesor);
}
