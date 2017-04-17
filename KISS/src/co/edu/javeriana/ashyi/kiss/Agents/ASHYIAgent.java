package co.edu.javeriana.ashyi.kiss.Agents;

import java.util.List;

import co.edu.javeriana.ashyi.kiss.Data.Data;


/**
 * @author ASHYI
 * Agente ashyi, agente que desarrolla actividades en la interface grafica
 * Creado para usuarios ejecutor y planificador
 * @see Agent
 */
public class ASHYIAgent extends Agent{

	/**
	 * Constructor base
	 * @param id nombre
	 * @param datos datos
	 * @param acciones lista de acciones
	 */
	public ASHYIAgent(String id, Data datos, List<Class> acciones) {
		super(id, datos, acciones);
	}	
}