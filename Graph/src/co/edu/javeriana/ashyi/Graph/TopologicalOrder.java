package co.edu.javeriana.ashyi.Graph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * @author co.edu.javeriana.ashyi
 *
 * @param <T> Tipo de dato de los v&eacutertices.
 * 
 * Clase gen&eacuterica que calcula el orden topol&oacutegico de un grafo dirigido y ac&iacuteclico. 
 * El tipo de dato de los v&eacutertices es gen&eacuterico.
 */
public class TopologicalOrder<T> {

	/*** 
	 * El grafo para calcular el orden topol&oacutegico.
	 */
	private Graph graph;
	/*** 
	 * HashSet que almacena los v&eacutertices visitados durante la ejecuci&oacuten del algoritmo.
	 */
	private HashSet<T> marked;
	/*** 
	 * HashSet que almacena posorden del grafo.
	 */
	private Deque<T> postorder;

	/*** 
	 * Stack que almacena al rev&eacutes el posorden del grafo, 
	 * el cual equivale al orden topol&oacutegico del grafo.
	 */
	private Stack<T> reversedPostOrder;

	/*** 
	 * Crea un objeto de tipo TopologicalOrder.
	 * @param graph el grafo para calcular el orden topol&oacutegico.
	 */
	public TopologicalOrder(Graph graph) {
		System.out.println();
		this.graph = graph;
		marked = new HashSet<>();
		postorder = new LinkedList<>();
		reversedPostOrder=new Stack<>();
		depthFirstPostOrder();
	}

	/*** 
	 * Visita los vértices del grafo por b&uacutesqueda en profundidad.
	 */
	private void depthFirstPostOrder() {
		Set<T> vertices = graph.getVertices();
		Iterator<T> it=vertices.iterator();
		while(it.hasNext())
		{
			T vertex=it.next();
			if(!marked.contains(vertex))
				dfs(vertex);
		}
	}

	/*** 
	 * Visita los vértices del grafo por b&uacutesqueda en profundidad y almacena el posorden y el posorden al rev&eacutes.
	 */
	private void dfs(T v) {
		marked.add(v);
		Set<GraphEdge> adj=graph.adj(v);
		Iterator<GraphEdge> it=adj.iterator();
		while(it.hasNext()) {
			T val=(T) it.next().getDestination();
			if (!marked.contains(val)) {
				dfs(val);
			}
		}

		postorder.push(v);
		reversedPostOrder.add(v);	
	}

	/*** 
	 * Retorna el orden topol&oacutegico del grafo.
	 */
	public Stack<T> getTopologicalSort() {
		return reversedPostOrder;
	}

	/*** 
	 * Retorna el posorden del grafo.
	 */
	public Deque<T> getPostorder() {
		return postorder;
	}
}
