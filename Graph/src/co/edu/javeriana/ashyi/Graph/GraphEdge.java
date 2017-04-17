package co.edu.javeriana.ashyi.Graph;

/**
 * @author co.edu.javeriana.ashyi
 *
 * @param <T> Tipo de dato de los v&eacutertices.
 * 
 * Clase gen&eacuterica que representa un arco con peso y sin valores negativos de un grafo. 
 * El tipo de dato de los v&eacutertices es gen&eacuterico y el peso de las aristas es de tipo Double.
 */
public class GraphEdge<T> implements Comparable<T>{
	
	/**
	* V&eacutertice de origen.
	*/
	private volatile T origin;
	/**
	* V&eacutertice de destino.
	*/
	private volatile T destination;
	/**
	* Peso del arco.
	*/
	private volatile double weight;
	/**
	* Orden del arco.
	*/
	private volatile int order;

	/**
	* Crea un arco con orden.
	* @param origin el v&eacutertice de origen.
	* @param destination el v&eacutertice de destino.
	* @param weight el peso del arco.
	* @param order el orden del arco.
	*/
	public GraphEdge(T origin, T destination, double weight, int order) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.weight = weight;
		this.order = order;
	}

	/**
	* Crea un arco.
	* @param origin el v&eacutertice de origen.
	* @param destination el v&eacutertice de destino.
	* @param weight el peso del arco.
	*/
	public GraphEdge(T origin, T destination, double weight) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.weight = weight;
	}

	/**
	* Retorna el v&eacutertice de origen.
	* @return origin el v&eacutertice de origen.
	*/
	public T getOrigin() {
		return origin;
	}

	/**
	* Asigna el v&eacutertice de origen.
	* @param origin el v&eacutertice de origen.
	*/
	public void setOrigin(T origin) {
		this.origin = origin;
	}

	/**
	* Retorna el v&eacutertice de destino.
	* @return destination el v&eacutertice de destino.
	*/
	public T getDestination() {
		return destination;
	}

	/**
	* Asigna el v&eacutertice de destino.
	* @param destination el v&eacutertice de destino.
	*/
	public void setDestination(T destination) {
		this.destination = destination;
	}

	/**
	* Retorna el peso.
	* @return weight el peso.
	*/
	public double getWeight() {
		return weight;
	}

	/**
	* Asigna el peso.
	* @param weight el peso.
	*/
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	* Retorna el orden.
	* @return order el orden.
	*/
	public int getOrder() {
		return order;
	}

	/**
	* Asigna el orden.
	* @param order el orden.
	*/
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	* Retorna el String de impresi&oacuten del arco.
	* @return ts la impresi&oacuten.
	*/
	@Override
	public String toString() {
		String ts="" + origin + "->" + destination + ": " + weight;
		return ts;
	}

	/**
	* Compara el arco con otro arco.
	* @param obj el arco a comparar.
	* @return true si los dos arcos son iguales.
 	* @return false si los dos arcos son diferentes.
	*/
	@Override
	public boolean equals(Object obj) {
		GraphEdge<T> edge=(GraphEdge<T>) obj;
		return (this.origin.equals(edge.origin)&&this.destination.equals(edge.destination));
	}

	/**
	* Compara el arco con otro arco.
	* @param o el arco a comparar.
	* @return 0 si los dos arcos son iguales.
 	* @return 1 si los dos arcos son diferentes.
	*/
	@Override
	public int compareTo(T o) {
		GraphEdge<T> edge=(GraphEdge<T>) o;
		if(this.origin==edge.origin&&this.destination==edge.destination)
			return 0;
		else
			return 1;
	}
	
	
}
