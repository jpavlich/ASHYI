package co.edu.javeriana.ashyi.kiss.Actions;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.GrafoRelaciones;
import co.edu.javeriana.ASHYI.model.GrafosUsuario;
import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.MainAgentData;

public class MainPassCalculateDistanceG<T> extends MessagePassingAction<T>{
	
	public MainPassCalculateDistanceG(Message mensaje) {
		super(mensaje);
	}

	public T execute() {
		// enviar a ejecutor para ruta
		Message mensaje = (Message)this.getMensaje();
		sendToDistance(mensaje);
		return null;
	}
	
	/**
	 * Desarrollo del evento "Calcular distancia" de un usuario ejecutor
	 * Enviar evento al agente ejecutor o representante segun el caso de los datos del evento 
	 * @param datosLlegan datos del evento
	 */
	private void sendToDistance(Message datosLlegan) {

		System.out.println("Enviando datos de distancia para ejecutor: "
				+ datosLlegan.getReceiverAgent());
		String alias = "";
		if (datosLlegan.getReceiverAgent().contains("RepresentativeAgent"))
			alias = datosLlegan.getReceiverAgent().replace(
					"RepresentativeAgent", "Executor");
		else
			alias = datosLlegan.getReceiverAgent();
		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());
		
		MainAgentData datos = (MainAgentData) myAgent.getDatos();

		// ejecutor
		Agent handler = this.getAgent().getAdmLocal().getAgent(alias);

		List<Object> datosEnviar = new ArrayList<Object>();

		List datosL = (List) datosLlegan.getMessage();

		datosEnviar.add((List) datosL.get(0));
		
//		System.out.println("Grafo a enviar: "+datos.getGrafoMapa((int) datosL.get(1)));
//		System.out.println("Grafo a enviar: "+datos.getGrafoMapa((int) datosL.get(1)).getNumVertices());
		
		if(datos.getGrafoMapa((int) datosL.get(1)).getNumVertices() == 0)
		{
			Consulta c = new Consulta();
			int idUsuario = c.getIdUsuarioInstructor((Integer) datosL.get(1));
			List<GrafosUsuario> lista=c.getGrafoUsuario(idUsuario, (Integer) datosL.get(1));
			if(lista != null)
				if(!lista.isEmpty())
				{
					System.out.println("El grafo ya existe");
					//crear grafo a partir de bd
					datos.setGrafoMapa(cargarGrafoExistente(lista.get(0).getIdGrafo().getIdGrafo(), c), (Integer) datosL.get(1));
					System.out.println("Grafo graph del profesor cargado de la base de datos");				
				}
		}
		datosEnviar.add(datos.getGrafoMapa((int) datosL.get(1)));
		datosEnviar.add(datos.getIdItemsMapa((int) datosL.get(1)));	
		datosEnviar.add((int) datosL.get(1));
		datosEnviar.add((int) datosL.get(2));// tipoRuta

		Message data = new Message(datosEnviar, datosLlegan.getReceiverAgent(), this.getAgent().getID());
		
		ExecutorCalculateDistanceG evento = new ExecutorCalculateDistanceG(data);
		handler.executeAction(evento);

	}
	
	/**
	 * Carga de grafo de una actividad recursiva de un agente planificar
	 * @param idGrafo id del grafo a buscar
	 * @param c Puente de consulta
	 * @return grafo consultado
	 */
	private Graph<Integer> cargarGrafoExistente(int idGrafo, Consulta c) {
		List<GrafoRelaciones> relaciones=c.getGrafoRelaciones(idGrafo);
		Graph<Integer> grafo=new Graph<Integer>();
		System.out.println("En cargar grafo existente numero de relaciones: "+relaciones.size());
		for (int i = 0; i < relaciones.size(); i++) {
			GrafoRelaciones rel=relaciones.get(i);
			grafo.addEdge(rel.getIdItemPlan_Origen(), rel.getIdItemPlan_Destino(), 1.0, rel.getOrden());
			System.out.println("Edge: "+rel.getIdItemPlan_Origen()+" y "+rel.getIdItemPlan_Destino());
		}
		return grafo;
	}

}
