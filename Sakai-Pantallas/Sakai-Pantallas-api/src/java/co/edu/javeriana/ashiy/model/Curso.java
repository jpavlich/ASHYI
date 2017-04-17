package co.edu.javeriana.ashiy.model;

import java.util.ArrayList;
import java.util.List;


//Crearlo obteniendo la informaciÃ³n dada por Sakai?
public class Curso {
	private String nombre; 
	private String descripcion;
	private List<Tema> temas;
	
	
	public Curso(String nombre, String descripcion, List<Tema> temas) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.temas = temas;
		
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<Tema> getTemas() {
		return temas;
	}
	public void setTemas(List<Tema> temas) {
		this.temas = temas;
	}
	
	
}
