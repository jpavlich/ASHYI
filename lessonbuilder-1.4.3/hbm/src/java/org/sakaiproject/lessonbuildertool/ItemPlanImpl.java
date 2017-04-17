package org.sakaiproject.lessonbuildertool;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.Recurso;

/**
 * @author ASHYI
 * @see ItemPlan
 */

public class ItemPlanImpl implements ItemPlan, Serializable {	
	
	private Integer idItemPlan;
	private Actividad idActividad;
	private Actividad idUnidadDidacticaPadre;
	private Recurso idRecurso;
	private boolean estaActivo;
	private Integer orden;
	private float tiempo_duracion;
	private Date fecha_inicial;
	private Date fecha_final;

	/**
	 * Constructor base
	 */
	public ItemPlanImpl() {
		this.idActividad = null;
		this.estaActivo = true;
		this.orden = 0;
	}
	
	/**
	 * @param idItemPlan
	 * @param uniad didactica
	 * @param Actividad
	 * @param UnidadDidacticaPadre
	 * @param Recurso
	 * @param estaActivo o no
	 * @param orden
	 * @param tiempo_duracion
	 * @param fecha_inicial
	 * @param fecha_final
	 */
	public ItemPlanImpl(Integer idItemPlan, Actividad ud, Actividad idActividad,
			Actividad idUnidadDidacticaPadre, Recurso idRecurso,
			boolean estaActivo, Integer orden, float tiempo_duracion,
			Date fecha_inicial, Date fecha_final) {
		super();
		this.idUnidadDidacticaPadre=ud;
		this.idItemPlan = idItemPlan;
		this.idActividad = idActividad;
		this.idUnidadDidacticaPadre = idUnidadDidacticaPadre;
		this.idRecurso = idRecurso;
		this.estaActivo = estaActivo;
		this.orden = orden;
		this.tiempo_duracion = tiempo_duracion;
		this.fecha_inicial = fecha_inicial;
		this.fecha_final = fecha_final;
	}

	/**
	 * @param unidad didactica
	 * @param Actividad
	 * @param Recurso
	 * @param estaActivo o no
	 * @param orden
	 */
	public ItemPlanImpl(Actividad ud, Actividad idActividad, Recurso idRecurso, boolean estaActivo, Integer orden) {
		super();
		this.idUnidadDidacticaPadre=ud;
		this.idActividad = idActividad;
		this.estaActivo = estaActivo;
		this.idRecurso = idRecurso;
		this.orden=orden;
	}
	
	/**
	 * @param unidad didactica
	 * @param Actividad
	 * @param estaActivo o no
	 * @param orden
	 */
	public ItemPlanImpl(Actividad ud, Actividad idActividad, boolean estaActivo, Integer orden) {
		super();
		this.idUnidadDidacticaPadre=ud;
		this.idActividad = idActividad;
		this.estaActivo = estaActivo;
		this.orden=orden;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#getIdRecurso()
	 */
	public Recurso getIdRecurso() {
		return idRecurso;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setIdRecurso(org.sakaiproject.lessonbuildertool.model.Recurso)
	 */
	public void setIdRecurso(Recurso idRecurso) {
		this.idRecurso = idRecurso;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#getIdActividad()
	 */
	public Actividad getIdActividad() {
		return idActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setIdActividad(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void setIdActividad(Actividad idActividad) {
		this.idActividad = idActividad;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#isEstaActivo()
	 */
	public boolean isEstaActivo() {
		return estaActivo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setEstaActivo(boolean)
	 */
	public void setEstaActivo(boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#getIdItemPlan()
	 */
	public Integer getIdItemPlan() {
		return idItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setIdItemPlan(java.lang.Integer)
	 */
	public void setIdItemPlan(Integer idItemPlan) {
		this.idItemPlan = idItemPlan;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#getOrden()
	 */
	public Integer getOrden() {
		return orden;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setOrden(java.lang.Integer)
	 */
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#getTiempo_duracion()
	 */
	public float getTiempo_duracion() {
		return tiempo_duracion;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setTiempo_duracion(float)
	 */
	public void setTiempo_duracion(float tiempo_duracion) {
		this.tiempo_duracion = tiempo_duracion;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#getFecha_inicial()
	 */
	public Date getFecha_inicial() {
		return fecha_inicial;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setFecha_inicial(java.util.Date)
	 */
	public void setFecha_inicial(Date fecha_inicial) {
		this.fecha_inicial = fecha_inicial;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#getFecha_final()
	 */
	public Date getFecha_final() {
		return fecha_final;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setFecha_final(java.util.Date)
	 */
	public void setFecha_final(Date fecha_final) {
		this.fecha_final = fecha_final;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setTiempo_duracion()
	 */
	public void setTiempo_duracion()
	{
		if( this.fecha_final != null && this.fecha_inicial != null)
		{
//			SimpleDateFormat formatter;
//			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//			Date date = formatter.parse(dI.substring(0, 16));
//			//this.fecha_inicial =  date;//AAAA-MM-DDTHH:MM
//			//this.fecha_inicial =  new SimpleDateFormat("yyyy-MM-dd").parse(dI);//AAAA-MM-DDTHH:MM
//			date = formatter.parse(dF.substring(0, 16));
//			this.fecha_final = date;
			
			long fechaInicialMs = this.fecha_inicial.getTime();
			long fechaFinalMs = this.fecha_final.getTime();
			long diferencia = fechaFinalMs - fechaInicialMs;
			float dias = (float) Math.floor(diferencia / (1000 * 60 * 60));		
			
			this.tiempo_duracion = dias;			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setFechas(java.lang.String, java.lang.String)
	 */
	public void setFechas(String fechaInicialObjetivoUD, String fechaFinalObjetivoUD)
	{
		try {
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
			Date date1 = formatter.parse(fechaInicialObjetivoUD.substring(0, fechaInicialObjetivoUD.length()));
			this.fecha_inicial = date1;
			
			//System.out.println("fechasss Inicial: "+date1 +" s: "+date1.toString());
			Date date2 = formatter.parse(fechaFinalObjetivoUD.substring(0, fechaFinalObjetivoUD.length()));
			this.fecha_final = date2;
			
			//System.out.println("fechasss final: "+date2 +" s: "+date2.toString());
			long fechaInicialMs = date1.getTime();
			long fechaFinalMs = date2.getTime();
			long diferencia = fechaFinalMs - fechaInicialMs;
			float horas = (float) Math.floor(diferencia / (1000 * 60 * 60));		
			
			this.tiempo_duracion = horas;	
			//System.out.println("tiempo "+tiempo_duracion);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#getIdUnidadDidacticaPadre()
	 */
	public Actividad getIdUnidadDidacticaPadre() {
		return idUnidadDidacticaPadre;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.ItemPlan#setIdUnidadDidacticaPadre(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void setIdUnidadDidacticaPadre(Actividad _idUnidadDidacticaPadre) {
		this.idUnidadDidacticaPadre = _idUnidadDidacticaPadre;
	}
}