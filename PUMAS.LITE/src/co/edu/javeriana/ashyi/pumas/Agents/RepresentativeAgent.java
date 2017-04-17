package co.edu.javeriana.ashyi.pumas.Agents;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.Data;

/**
 * @author ASHYI
 * Agente representante: agente que interpreta al Usuario Ejecutor dentro de PUMAS-Lite 
 * y que maneja su perfil (conformado por los datos personales y sus respectivos cambios). 
 */
public class RepresentativeAgent extends Agent{

	/**
	 * Constructor base
	 * @param id nombre
	 * @param datos datos
	 * @param acciones lista de acciones
	 */
	public RepresentativeAgent(String id, Data datos, List<Class> acciones) {
		super(id, datos, acciones);
	}
 
}