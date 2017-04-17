package org.sakaiproject.lessonbuildertool.model;

import java.util.Date;

public interface LogCaracteristicas {

	public int getIdUsuario();

	public void setIdUsuario(int idUsuario);

	public int getIdItemPlan() ;

	public void setIdItemPlan(int idItemPlan) ;

	public int getIdTipoEvento();

	public void setIdTipoEvento(int idTipoEvento);

	public int getIdCaracteristica();

	public void setIdCaracteristica(int idCaracteristica);

	public Date getFechaCalificacion();

	public void setFechaCalificacion(Date fechaCalificacion);

	public Date getFechaEnvio();

	public void setFechaEnvio(Date fechaEnvio);

	public int getCaracteristicasActuales();

	public void setCaracteristicasActuales(int caracteristicasActuales);

	public int getConsecutivo();

	public void setConsecutivo(int consecutivo);
}
