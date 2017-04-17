package co.edu.javeriana.ashiy.model;

import java.util.List;

public class Actividad {
        private String nombre; 
        private String descripcion;
        private List<Actividad> actividades;
        private boolean estado;
        
                
        public Actividad(String nombre, String descripcion,List<Actividad> tareas, boolean estado) {
                this.nombre = nombre;
                this.descripcion = descripcion;
                this.actividades = tareas;
                this.estado = estado;
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
        public List<Actividad> getActividades() {
                return actividades;
        }
        public void setActividades(List<Actividad> unidades) {
                this.actividades = unidades;
        }
        public boolean isEstado() {
                return estado;
        }
        public void setEstado(boolean estado) {
                this.estado = estado;
        }
        
        
}