package co.edu.javeriana.ashyi.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author co.edu.javeriana.ashyi
 *
 * @param <T> Tipo de dato de los v&eacutertices.
 * 
 * Clase gen&eacuterica que representa un grafo ac&iacuteclico, dirigido con peso en las aristas y sin valores negativos. 
 * El tipo de dato de los v&eacutertices es gen&eacuterico y el peso de las aristas es de tipo Double.
 */
public class Graph<T> {

	/**
	* Mapa en el que se guardan los arcos entre cada v&eacutertice con sus vecinos.
	*/
	private Map<T, LinkedHashSet<GraphEdge>> graph;
	/**
	* N&uacutemero de arcos.
	*/
	private volatile int numEdges;
	/**
	* N&uacutemero de v&eacutertices.
	*/
	private volatile int numVertices;
	/**
	* Atributo auxiliar que permite conocer el orden m&aacutes grande de v&eacutertices del grafo.
	* En ASHYI los v&eacutertices del grafo est&aacuten organizados por varios niveles, el mayor nivel es el mayor orden.
	*/
	private volatile int ordenMayor;

	
	/**
	* Construye un grafo nuevo.
	*/
	public Graph() {
		graph = Collections.synchronizedMap(new LinkedHashMap<T, LinkedHashSet<GraphEdge>>());
		ordenMayor=0;
	}

	/**
	* Retorna el mapa de arcos entre v&eacutertices.
	* @return graph el mapa de arcos. 
	*/
	public Map<T, LinkedHashSet<GraphEdge>> getGraph() {
		return graph;
	}

	/**
	* Asigna un mapa de arcos al grafo.
	* @param graph el mapa de arcos.
	*/
	public void setGraph(Map<T, LinkedHashSet<GraphEdge>> graph) {
		this.graph = graph;
	}

	/**
	* Retorna el n&uacutemero de arcos del grafo.
	* @return numEdges el n&uacutemero de arcos.
	*/
	public int getNumEdges() {
		return numEdges;
	}
	
	/**
	* Asigna el n&uacutemero de arcos del grafo.
	* @param numEdges el n&uacutemero de arcos.
	*/
	public void setNumEdges(int numEdges) {
		this.numEdges = numEdges;
	}

	/**
	* Retorna el n&uacutemero de v&eacutertices del grafo.
	* @return numVertices el n&uacutemero de v&eacutertices.
	*/
	public int getNumVertices() {
		return numVertices;
	}

	/**
	* Asigna el n&uacutemero de v&eacutertices del grafo.
	* @param numVertices el n&uacutemero de v&eacutertices.
	*/
	public void setNumVertices(int numVertices) {
		this.numVertices = numVertices;
	}

	/**
	* Retorna un set con los v&eacutertices del grafo.
	* @return graph.keySet() set que contiene los v&eacutertices.
	*/
	public Set<T> getVertices() {
		return graph.keySet();
	}

	/**
	* Retorna el orden mayor del grafo.
	* @return ordenMayor el orden mayor.
	*/
	public int getOrdenMayor() {
		return ordenMayor;
	}

	/**
	* Asigna el orden mayor del grafo.
	* @param ordenMayor el orden mayor.
	*/
	public void setOrdenMayor(int ordenMayor) {
		this.ordenMayor = ordenMayor;
	}

	/**
	* Retorna los arcos del grafo.
	* @return edges los arcos.
	*/
	public LinkedHashSet<GraphEdge> getEdges() {
		LinkedHashSet<GraphEdge> edges=new LinkedHashSet<GraphEdge>();
		Iterator<LinkedHashSet<GraphEdge>> it=graph.values().iterator();
		while(it.hasNext())
		{
			Set<GraphEdge> graphEdgeSet=it.next();
			Iterator <GraphEdge> it2=graphEdgeSet.iterator();
			while(it2.hasNext())
			{
				GraphEdge edge=it2.next();
				if(!edges.contains(edge))
				{
					edges.add(edge);
				}
			}
		}
		return edges;
	}

	/**
	* Retorna la lista de v&eacutertices adyacentes a un v&eacutertice dado.
	* @param v el v&eacutertice dado.
	* @return adjList la lista de v&eacutertices adyacentes a v.
	*/
	public ArrayList<T> adjList(T v) {
		ArrayList<T> adjList = new ArrayList<T>();
		if (graph.containsKey(v)) {
			Iterator<GraphEdge> it = graph.get(v).iterator();
			while (it.hasNext()) {
				adjList.add((T) it.next().getDestination());
			}
		}
		return adjList;
	}

	/**
	* Retorna el set de v&eacutertices adyacentes a un v&eacutertice dado.
	* @param v el v&eacutertice dado.
	* @return adjV el set de v&eacutertices adyacentes a v.
	*/
	public LinkedHashSet<GraphEdge> adj(T v) {
		LinkedHashSet<GraphEdge> adjV=new LinkedHashSet<>();
		if(graph.get(v)!=null)
			adjV=graph.get(v);
		return adjV;
	}

	/**
	* A&ntildeade un arco con orden al grafo.
	* @param origin el v&eacutertice de origen.
	* @param destination el v&eacutertice de destino.
	* @param weight el peso del arco.
	* @param order el orden del arco.
	*/
	public void addEdge(T origin, T destination, double weight, int order) {
		GraphEdge newNode = new GraphEdge<T>(origin, destination, weight, order);
		if (!this.graph.containsKey(origin)) {
			this.graph.put(origin, new LinkedHashSet<GraphEdge>());
			this.numVertices++;
		}
		if (!this.graph.containsKey(destination)) {
			this.graph.put(destination, new LinkedHashSet<GraphEdge>());
			this.numVertices++;
		}

		if(order>this.ordenMayor)
			this.ordenMayor=order;
		
		if(!this.graph.get(origin).contains(newNode))
		{
			this.graph.get(origin).add(newNode);
			this.numEdges++;
		}
	}

	/**
	* A&ntildeade un arco al grafo.
	* @param origin el v&eacutertice de origen.
	* @param destination el v&eacutertice de destino.
	* @param weight el peso del arco.
	*/
	public void addEdge(T origin, T destination, double weight) {
		GraphEdge newNode = new GraphEdge<T>(origin, destination, weight);
		if (!this.graph.containsKey(origin)) {
			this.graph.put(origin, new LinkedHashSet<GraphEdge>());
			this.numVertices++;
		}
		if (!this.graph.containsKey(destination)) {
			this.graph.put(destination, new LinkedHashSet<GraphEdge>());
			this.numVertices++;
		}

		if(!this.graph.get(origin).contains(newNode))
		{
			this.graph.get(origin).add(newNode);
			this.numEdges++;
		}
	}

	/**
	* A&ntildeade un v&eacutertice al grafo si no existe a&uacuten.
	* @param v el nuevo v&eacutertice.
	*/
	public void addVertex(T v) {
		if (this.graph.get(v) == null) {
			this.graph.put(v, new LinkedHashSet<GraphEdge>());
			this.numEdges++;
		}
	}
	
	public Object createGarph()
	{
		return new Object();
		
	}

}
