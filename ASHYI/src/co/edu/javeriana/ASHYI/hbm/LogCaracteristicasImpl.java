package co.edu.javeriana.ASHYI.hbm;

import java.util.Date;

import co.edu.javeriana.ASHYI.model.LogCaracteristicas;

public class LogCaracteristicasImpl implements LogCaracteristicas{

	int idUsuario;
	int idItemPlan;
	int idTipoEvento;
	int idCaracteristica;
	Date fechaCalificacion;
	Date fechaEnvio;
	int caracteristicasActuales;
	int consecutivo;
	
	
	
	public LogCaracteristicasImpl() {
		super();
	}



	public LogCaracteristicasImpl(int idUsuario, int idItemPlan,
			int idTipoEvento, int idCaracteristica, Date fechaCalificacion,
			Date fechaEnvio, int caracteristicasActuales) {
		super();
		this.idUsuario = idUsuario;
		this.idItemPlan = idItemPlan;
		this.idTipoEvento = idTipoEvento;
		this.idCaracteristica = idCaracteristica;
		this.fechaCalificacion = fechaCalificacion;
		this.fechaEnvio = fechaEnvio;
		this.caracteristicasActuales = caracteristicasActuales;
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



	public int getIdTipoEvento() {
		return idTipoEvento;
	}



	public void setIdTipoEvento(int idTipoEvento) {
		this.idTipoEvento = idTipoEvento;
	}



	public int getIdCaracteristica() {
		return idCaracteristica;
	}



	public void setIdCaracteristica(int idCaracteristica) {
		this.idCaracteristica = idCaracteristica;
	}



	public Date getFechaCalificacion() {
		return fechaCalificacion;
	}



	public void setFechaCalificacion(Date fechaCalificacion) {
		this.fechaCalificacion = fechaCalificacion;
	}



	public Date getFechaEnvio() {
		return fechaEnvio;
	}



	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}



	public int getCaracteristicasActuales() {
		return caracteristicasActuales;
	}



	public void setCaracteristicasActuales(int caracteristicasActuales) {
		this.caracteristicasActuales = caracteristicasActuales;
	}



	public int getConsecutivo() {
		return consecutivo;
	}



	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	
	
}
