package co.edu.javeriana.ashiy.model;

import java.util.List;

public class unidadDidactica {
	
	private String nombre;
	private List<Actividad> actividades;

	public unidadDidactica(List<Actividad> actividades, String nombre) {
		super();
		this.actividades = actividades;
		this.nombre = nombre;
	}

	public List<Actividad> getActividades() {
		return actividades;
	}

	public void setActividades(List<Actividad> actividades) {
		this.actividades = actividades;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
