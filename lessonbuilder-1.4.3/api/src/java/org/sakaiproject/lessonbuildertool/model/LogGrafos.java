package org.sakaiproject.lessonbuildertool.model;

import java.util.Date;

public interface LogGrafos {

	
	public int getConsecutivo() ;
	public void setConsecutivo(int consecutivo);
	public int getIdUsuario() ;
	public void setIdUsuario(int idUsuario);
	public int getIdItemPlan();
	public void setIdItemPlan(int idItemPlan);
	public int getIdGrafoActivo();
	public void setIdGrafoActivo(int idGrafoActivo);
	public int getIdGrafoAnterior();
	public void setIdGrafoAnterior(int idGrafoAnterior);
	public int getIdInstructor();
	public void setIdInstructor(int idInstructor);
	public int getIdTipoEvento();
	public void setIdTipoEvento(int idTipoEvento);
	public double getNota();
	public void setNota(double nota);
	public Date getFecha();
	public void setFecha(Date fecha);
}
