package co.edu.javeriana.ashyi.Graph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ashyi.Graph.Methods.MetodosFuncionDistancia;


/**
 * @author co.edu.javeriana.ashyi
 *
 * @param <T> Tipo de dato de los v&eacutertices.
 * 
 * Clase gen&eacuterica que calcula los caminos m&aacutes cortos en un grafo dirigido ac&iacuteclico con peso positivo en los arcos a partir de un v&eacutertice inicial.
 * Usa el algoritmo de Ordenaci&oacuten topol&oacutegica.
 * El tipo de dato de los v&eacutertices es gen&eacuterico y el peso de las aristas es de tipo Double.
 */
public class ShortestPaths <T> {

	/**
	 * El grafo para calcular los caminos m&aacutes cortos.
	 */
	private Graph graph;
	/**
	 * El v&eacutertice inicial.
	 */
	private T source;
	/**
	 * Almacena la distancia m&iacutenima a cada v&eacutertice desde el v&eacutertice inicial.
	 */
	private HashMap<T, Double> distanceTo;
	/**
	 * Almacena desde d&oacutende se llega a cada v&eacutertice a partir del v&eacutertice inicial.
	 */
	private HashMap<T, GraphEdge<T>> pathTo;
	
	/**
	 * Crea un objeto de tipo ShortestPaths.
	 */
	public ShortestPaths() {
		super();
	}

	/**
	 * Crea un objeto de tipo ShortestPaths.
	 * @param graph el grafo para calcular los caminos m&aacutes cortos.
	 * @param source el v&eacutertice inicial.
	 */
	public ShortestPaths(Graph graph, T source) {
		super();
		this.graph = graph;
		this.source=source;
		this.distanceTo=new HashMap<T, Double>();
		this.pathTo=new HashMap<T, GraphEdge<T>>();
		this.initialize();
		this.computeShortestPaths();
	}

	/**
	 * Inicializa el HashMap distanceTo con distancias infinitas a todos los v&eacutertices menos al inicial, 
	 * que queda en cero; esto se hace para que el algoritmo que calcula los caminos m&aacutes cortos se pueda ejecutar.
	 */
	private void initialize() {
		Set<T> vertices=graph.getVertices();
		Iterator<T> it=vertices.iterator();
		while(it.hasNext())
		{
			distanceTo.put(it.next(),Double.POSITIVE_INFINITY);
		}
		distanceTo.put(source, 0.0);
	}

	/**
	 * Ejecuta el algoritmo que calcula los caminos m&aacutes cortos se pueda ejecutar.
	 */
	private void computeShortestPaths() {
		TopologicalOrder<T> top=new TopologicalOrder<>(graph);
		Deque<T> topologicalOrder=top.getPostorder();

		Iterator<T> it=topologicalOrder.iterator();
		while(it.hasNext()){
			HashSet<GraphEdge> adj=(HashSet<GraphEdge>) graph.adj(it.next());
			Iterator<GraphEdge> it2=adj.iterator();
			while(it2.hasNext())
			{
				GraphEdge auxEdge=it2.next();
				this.relax(auxEdge);
			}
		}
	}

	/**
	 * Retorna el grafo.
	 * @return graph el grafo.
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Asigna el grafo.
	 * @param graph el grafo.
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Retorna el v&eacutertice inicial.
	 * @return source el v&eacutertice inicial.
	 */
	public T getSource() {
		return source;
	}

	/**
	 * Asigna el v&eacutertice inicial.
	 * @param source el v&eacutertice inicial.
	 */
	public void setSource(T source) {
		this.source = source;
	}

	/**
	 * Indica si el v&eacutertice inicial tiene camino hacia un v&eacutertice dado.
	 * @param destination el v&eacutertice a revisar.
	 * @return true si hay camino.
	 * @return false si no hay camino.
	 */
	public boolean hasPathTo(T destination)
	{
		return this.pathTo.containsKey(destination);
	}

	/**
	 * Retorna la distancia entre el v&eacutertice inicial y un v&eacutertice dado.
	 * @param destination el v&eacutertice a revisar.
	 * @return la distancia entre los dos v&eacutertices.
	 */
	public double distanceTo (T destination)
	{
		if(this.distanceTo.containsKey(destination))
			return this.distanceTo.get(destination);
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * Retorna el camino entre el v&eacutertice inicial y un v&eacutertice dado.
	 * @param list lista con los arcos que componen el camino.
	 */
	public LinkedList<GraphEdge<T>> pathTo(T destination)
	{
		LinkedList<GraphEdge<T>> list=new LinkedList<>();
		T actual=destination;
		while(pathTo.get(actual)!=null)
		{
			list.add(pathTo.get(actual));
		}
		return list;
	}

	/**
	 * Actualiza un arco con la distancia y el camino hacia un v&eacutertice a partir del v&eacutertice inicial 
	 * si es menor a la anterior.
	 * @param e el v&eacutertice a actualizar.
	 */
	private void relax(GraphEdge<T> e) {
		T v=e.getOrigin();
		T w=e.getDestination();
		if (distanceTo.get(w) > distanceTo.get(v) + e.getWeight()) {
			distanceTo.put(w, distanceTo.get(v) + e.getWeight());
			pathTo.put(w, e);
		}
	}

	/**
	 * Retorna el HashMap de distancias entre el v&eacutertice inicial y los dem&aacutes.
	 * @return distanceTo el HashMap de distancias.
	 */
	public HashMap<T, Double> getDistanceTo() {
		return distanceTo;
	}

	/**
	 * Asigna el HashMap de distancias entre el v&eacutertice inicial y los dem&aacutes.
	 * @param distanceTo el HashMap de distancias.
	 */
	public void setDistanceTo(HashMap<T, Double> distanceTo) {
		this.distanceTo = distanceTo;
	}

	/**
	 * Retorna el HashMap que almacena desde d&oacutende se llega a cada v&eacutertice a partir del v&eacutertice inicial.
	 * @return pathTo el HashMap de caminos.
	 */
	public HashMap<T, GraphEdge<T>> getPathTo() {
		return pathTo;
	}

	/**
	 * Asigna el HashMap que almacena desde d&oacutende se llega a cada v&eacutertice a partir del v&eacutertice inicial.
	 * @param pathTo el HashMap de caminos.
	 */
	public void setPathTo(HashMap<T, GraphEdge<T>> pathTo) {
		this.pathTo = pathTo;
	}
	
	/**
	 * metodo recursivo que recorre el grafo general de actividades y encuentra el camino mas corto para un usuario
	 * @param grafo grafo general de actividades para buscar
	 * @param idNodoActual id del item en donde va la busqueda
	 * @param mapaItems Mapa completo de Items 
	 * @param usuario arreglo que contiene la infromacion del perfil del usuario ejecutor
	 * @param mapa Lista de caracteristicas pre condicion del sistema
	 * @param mapaPost Lista de caracteristicas post condicion del sistema
	 * @param caracteristicas Lista completa de caracteristicas del sistema
	 * @param distanciaRecorrida doble, distancia recorrida hasta el momento en el grafo
	 * @param camino Lista de enteros que conforman el camino
	 * @param caminoMasCorto Camino mas corto para este grafo y este usuario ejecutor
	 */	
	public void CaminoMasCorto(Graph grafo, int idNodoActual, Map<Integer, ItemPlan> mapaItems, double usuario[],
			Map<Integer, List<Caracteristica>> mapa, Map<Integer, List<Caracteristica>> mapaPost,
			List<List<Caracteristica>> caracteristicas, double distanciaRecorrida, List<Integer> camino,
			Camino caminoMasCorto) {
		MetodosFuncionDistancia m = new MetodosFuncionDistancia();
		
		//Camino caminoMasCorto = (Camino) caminoC;
		
		int opcionD = 2;// ecuclediana

		List<Integer> adyacentes = grafo.adjList(idNodoActual);

		if (adyacentes.isEmpty()) {
			//if (distanciaRecorrida <= ((Camino) caminoMasCorto).getDistanciaMasCorta() && caminoMasCorto.getCaminoMasCorto().size() < camino.size()) {
			if (distanciaRecorrida <= ((Camino) caminoMasCorto).getDistanciaMasCorta() ) {
				List<Integer> activRemediales = new ArrayList<Integer>();
				//eliminar actividades de refuerzo repetidas
				for(int pos = 0; pos < camino.size(); pos++)
				{
					ItemPlan ipT = mapaItems.get(camino.get(pos));
					if(ipT.getIdActividad().isEs_refuerzo())
					{
						if(activRemediales.contains(ipT.getIdActividad().getIdActividad()))
						{
							camino.remove(camino.get(pos));
							pos--;							
						}
						else
						{
							activRemediales.add(ipT.getIdActividad().getIdActividad());
						}
					}
				}
				
				caminoMasCorto.setDistanciaMasCorta(distanciaRecorrida);
				caminoMasCorto.getCaminoMasCorto().clear();
				caminoMasCorto.getCaminoMasCorto().addAll(camino);
				return;
			}
		}

		ItemPlan itemActual = mapaItems.get(idNodoActual);

		for (Integer idAdyacente : adyacentes) {
			if (itemActual.isEstaActivo()) {
				camino.add(idAdyacente);

				ItemPlan itemAdyacente = mapaItems.get(idAdyacente);

				double actividad[] = construirActividad(itemActual, caracteristicas, mapaPost);
				if (itemActual.getOrden() != 1)
					usuario = modificarUsuario(usuario, mapaPost.get(idNodoActual), caracteristicas);
				double distancia = 0;
				// cosntruir actividad con item destino

				boolean llamar = true;
				
				if (!itemAdyacente.getIdActividad().isEs_refuerzo()) {// una actividad de refuerzo no genera trabajo para el estudiante
					actividad = construirActividad(itemAdyacente, caracteristicas, mapa);
					distancia = (double) m.calculateDistance(usuario, actividad);
					if(itemActual.getIdActividad().isEs_refuerzo())
					{						
						if((distancia*100)/1.5 > 70)// si genera un trabajo mayor al 70%, es mejor hacer una de refuerzo primero
							distancia *= 0.8;
					}
				}
				//System.out.println("!!!!!! items: " + idNodoActual + " -> " + idAdyacente + ": " + distancia);
				if(llamar)
				{
					distanciaRecorrida += distancia;
					CaminoMasCorto(grafo, idAdyacente, mapaItems, usuario, mapa, mapaPost, caracteristicas,
							distanciaRecorrida, camino, caminoMasCorto);
					camino.remove(idAdyacente);
					distanciaRecorrida -= distancia;
				}
			}
		}
	}
	
	/**
	 * Con base en los datos de la actividad, construir un arreglo con los valores del mismo 
	 * @param item ItemPlan asociado
	 * @param caracteristicas lista de caracteristicas de la actividad
	 * @param mapa Lista de caracteristicas completas del sistema
	 * @return arreglo de las caracteristicas de la actividad
	 */
	private double[] construirActividad(ItemPlan item, List<List<Caracteristica>> caracteristicas,
			Map<Integer, List<Caracteristica>> mapa) {
		// Consulta consAHYI = new Consulta();
		double[] actividad = new double[55];

		// 1 pre condiciones y 2 consulta tipo
		// List<Caracteristica> caracteristicasActividad =
		// getCaracteristicasAc(item, consAHYI,1,2);
		List<Caracteristica> caracteristicasActividad = mapa.get(item.getIdItemPlan());

		int pos = 0;
		// construir el vector de actividad segun el orden de las
		// caracteristicas
		for (int i = 0; i < caracteristicas.size(); i++) {
			for (Caracteristica cG : caracteristicas.get(i)) {
				int encuentra = 0;
				for (Caracteristica cA : caracteristicasActividad) {
					if (cG.getIdCaracteristica() == cA.getIdCaracteristica()) {
						actividad[pos] = 1;
						encuentra++;
						break;
					}
				}
				if (encuentra == 0)
					actividad[pos] = 0;
				pos++;
			}
		}
		return actividad;
	}
	
	/**
	 * Segun las caracteristicas dadas (alncanzadas / no alcanzadas), modificar el arreglo del usuario ejecutor
	 * @param usuario arreglo de las caracteristicas del usuario ejecutor
	 * @param caracteristicasAc Caracteristicas post de una actividad
	 * @param caracteristicas Lista completa de caracteristicas del sistema
	 * @return arreglo modificado de las caracteristicas del usuario ejecutor
	 */
	private double[] modificarUsuario(double[] usuario, List<Caracteristica> caracteristicasAc,
			List<List<Caracteristica>> caracteristicas) {
		// consultar las caracteristicas del sistema
		// Consulta consAHYI = new Consulta();
		// List<List<Caracteristica>> caracteristicas =
		// consAHYI.getCaracteristicas();

		int pos = 20;// desde la posici�n de las hab y comp
		// organizar el vector de usuario seg�n el orden de las
		// caracteristicas de las post condiciones de la actividad
		// 2 --> desde las habilidades
		for (int i = 2; i < caracteristicas.size(); i++) {
			for (Caracteristica cG : caracteristicas.get(i)) {
				for (int j = 0; j < caracteristicasAc.size(); j++) {
					Caracteristica cAc = caracteristicasAc.get(j);
					// si es la post condicion de la actividad
					if (cAc.getIdCaracteristica() == cG.getIdCaracteristica()) 
					{
						usuario[pos] = 1;
						break;
					}
				}
				pos++;
			}
		}

		return usuario;
	}
}
