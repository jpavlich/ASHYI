package co.edu.javeriana.ashiy.model;

import java.util.ArrayList;
import java.util.List;

public class SubTema {
        private String nombre; 
        private String descripcion;
        private List<unidadDidactica> unidades; 
        
        
        public SubTema(String nomST, String descST, List<unidadDidactica> unidades) 
        {               
                this.nombre = nomST;
                this.descripcion = descST;
                this.unidades = unidades;
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
        public List<unidadDidactica> getUnidades() {
                return unidades;
        }
        public void setUnidades(List<unidadDidactica> unidades) {
                this.unidades = unidades;
        }
        
        
}