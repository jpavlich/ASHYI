package Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.Graph.GraphEdge;
import co.edu.javeriana.ashyi.Graph.ShortestPaths;

/**
 * @author co.edu.javeriana.ashyi
 * 
 * Clase Main que prueba el algoritmo de ShortestPaths con un grafo de n&uacutemeros enteros.
 */
public class TestMain {

	/**
	 * Grafo para probar.
	 */
	static Graph<Integer> graph;
	/**
	 * ShortestPaths para ejecutar el algoritmo.
	 */
	static ShortestPaths<Integer> sp;

	/**
	 * M&eacutetodo main donde se crea el grafo.
	 * @param args arreglo con un String que indica la ruta al archivo de texto para crear el grafo de prueba.
	 */
	public static void main(String[] args) {
		graph = new Graph();
		String file = args[0];
		try {
			Scanner in = new Scanner(new FileReader(file));
			while (in.hasNext()) {
				int n1 = in.nextInt();
				int n2 = in.nextInt();
				double d1 = in.nextDouble();
				graph.addEdge(n1,
						n2, 
						d1);
			}
			System.out.println("grafo: "+graph.getGraph().toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sp=new ShortestPaths<Integer>(graph, 8);
	}
	
	/**
	 * M&eacutetodo donde se crea el grafo.
	 * @param fileName String que indica la ruta al archivo de texto para crear el grafo de prueba.
	 */
	public static Graph<Integer> crearGrafo(String fileName) {
		graph = new Graph();
		String file = fileName;
		try {
			Scanner in = new Scanner(new FileReader(file));
			while (in.hasNext()) {
				graph.addEdge(in.nextInt(), in.nextInt(), in.nextDouble());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//sp=new ShortestPaths<Integer>(graph, 8);
		return graph;
	}
	
	/**
	 * M&eacutetodo donde se genera el camino mas corto
	 * @param graph grafo a quien se le calcula el camino mas corto.
	 */
	public static ShortestPaths<Integer> crearCamino(Graph<Integer> graph) {
		
		sp=new ShortestPaths<Integer>(graph, 8);
		return sp;
	}

	/**
	 * M&eacutetodo que retorna el camino desde el v&eacutertice inicial a los dem&aacutes.
	 * @param i v&eacutertice inicial para calcular los caminos m&aacutes cortos.
	 * @return String de un objeto JSON que muestra los caminos desde el v&eacutertice i a los dem&aacutes si existe alguno.
	 */
	public String getPaths(Integer i)
	{
		sp=new ShortestPaths<Integer>(graph, i);
		// TODO Auto-generated method stub
		JSONObject obj=new JSONObject();
		JSONArray array=new JSONArray();
		HashMap<Integer, GraphEdge<Integer> > pathTo=sp.getPathTo();
		for (int j = 0; j < graph.getNumVertices(); j++) {
			if(pathTo.containsKey(j))
			{
				GraphEdge<Integer> edge=pathTo.get(j);
				JSONObject aux=new JSONObject();
				aux.put(j, edge.getOrigin()+", "+edge.getWeight());
				array.add(aux);
			}
		}
		obj.put("paths", array);
		
		return obj.toString();
	}
}
