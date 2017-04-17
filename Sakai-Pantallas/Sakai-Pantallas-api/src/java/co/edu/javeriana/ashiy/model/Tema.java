package co.edu.javeriana.ashiy.model;

import java.util.ArrayList;
import java.util.List;

public class Tema {
	private String nombre; 
	private String descripcion;
	private List<SubTema> contenido;
	
	
	
	public Tema(String nombre, String descripcion, List<SubTema> subtemas) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.contenido = subtemas;
		
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
	public List<SubTema> getContenido() {
		return contenido;
	}
	public void setContenido(List<SubTema> contenido) {
		this.contenido = contenido;
	}
	
	
}
