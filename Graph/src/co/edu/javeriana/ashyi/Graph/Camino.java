package co.edu.javeriana.ashyi.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ASHYI
 * Camino a buscar en el grafo de actividades
 */
public class Camino {
	/**
	 * distancia mas corta del punto inicial al final
	 */
	double distanciaMasCorta = Double.POSITIVE_INFINITY;
	/**
	 * lista de id de items que conforman el camimo m[as corto del punto inicial al final
	 */
	List<Integer> caminoMasCorto = new ArrayList<>();

	/**
	 * @return distancia mas corta del punto inicial al final
	 */
	public double getDistanciaMasCorta() {
		return distanciaMasCorta;
	}

	/**
	 * @param distanciaMasCorta distancia mas corta del punto inicial al final
	 */
	public void setDistanciaMasCorta(double distanciaMasCorta) {
		this.distanciaMasCorta = distanciaMasCorta;
	}

	/**
	 * @return lista de id de items que conforman el camimo m[as corto del punto inicial al final
	 */
	public List<Integer> getCaminoMasCorto() {
		return caminoMasCorto;
	}

	/**
	 * @param caminoMasCorto lista de id de items que conforman el camimo m[as corto del punto inicial al final
	 */
	public void setCaminoMasCorto(List<Integer> caminoMasCorto) {
		this.caminoMasCorto = caminoMasCorto;
	}
}