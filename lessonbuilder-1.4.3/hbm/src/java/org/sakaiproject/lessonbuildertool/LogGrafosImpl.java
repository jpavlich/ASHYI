package org.sakaiproject.lessonbuildertool;

import java.util.Date;

import org.sakaiproject.lessonbuildertool.model.LogGrafos;

public class LogGrafosImpl implements LogGrafos{

	
	int idUsuario;
    int idItemPlan;
    int idGrafoActivo;
    int idGrafoAnterior;
    int idInstructor;
    int idTipoEvento;
    double nota;
    Date fecha;
    int consecutivo;
    
    public LogGrafosImpl() {
		
	}
    
    
	public LogGrafosImpl( int idUsuario, int idItemPlan,
			int idGrafoActivo, int idGrafoAnterior, int idInstructor,
			int idTipoEvento, double nota, Date fecha) {
		super();

		this.idUsuario = idUsuario;
		this.idItemPlan = idItemPlan;
		this.idGrafoActivo = idGrafoActivo;
		this.idGrafoAnterior = idGrafoAnterior;
		this.idInstructor = idInstructor;
		this.idTipoEvento = idTipoEvento;
		this.nota = nota;
		this.fecha = fecha;
	}
	
	
	
	public int getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public int getIdItemPlan() {
		return idItemPlan;
	}
	public void setIdItemPlan(int idItemPlan) {
		this.idItemPlan = idItemPlan;
	}
	public int getIdGrafoActivo() {
		return idGrafoActivo;
	}
	public void setIdGrafoActivo(int idGrafoActivo) {
		this.idGrafoActivo = idGrafoActivo;
	}
	public int getIdGrafoAnterior() {
		return idGrafoAnterior;
	}
	public void setIdGrafoAnterior(int idGrafoAnterior) {
		this.idGrafoAnterior = idGrafoAnterior;
	}
	public int getIdInstructor() {
		return idInstructor;
	}
	public void setIdInstructor(int idInstructor) {
		this.idInstructor = idInstructor;
	}
	public int getIdTipoEvento() {
		return idTipoEvento;
	}
	public void setIdTipoEvento(int idTipoEvento) {
		this.idTipoEvento = idTipoEvento;
	}
	public double getNota() {
		return nota;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
