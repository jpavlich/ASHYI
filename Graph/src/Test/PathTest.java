package Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.Graph.ShortestPaths;

/**
 * @author co.edu.javeriana.ashyi
 * 
 * Clase que prueba el algoritmo de ShortestPaths a partir de dos grafos de ejemplo.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PathTest {
	/**
	 * Clase de JUnit para ejecutar las pruebas.
	 */
	TestMain test=new TestMain();
	
	/**
	 * Ejecuta las dos pruebas.
	 */
	//@Test
	public void test() throws IOException {
		test1GrafoValoresDecimales();
		test2GrafoValoresEnteros();
	}

	/**
	 * Prueba con el grafo 1 que se encuentra en src/Test/graphSample1.txt.
	 * Compara los caminos m&aacutes cortos obtenidos a partir de cada v&eacutertices del grafo con los resultados que hay en
	 * src/Test/TestResultsGraph1.txt
	 */
	@Test
	public void test1GrafoValoresDecimales() throws IOException {
		System.out.println("===========================================================Test 1===========================================================");
		String [] arr = new String[1];
		ArrayList<String> results = new ArrayList<String>();
		String fileName="src/Test/graphSample1.txt";
		arr[0]=fileName;
		//test.main(arr);
		Graph<Integer> grafo = test.crearGrafo(fileName);
		String resultado = grafo.getGraph().toString();
		String resultadoCorrecto = "{8=[8->7: 1.0], 7=[7->6: 1.6], 2=[2->3: 3.0, 2->0: 4.3], 3=[3->5: 1.0], 0=[0->6: 2.0, 0->1: 7.0, 0->5: 4.8], 6=[6->4: 3.0, 6->9: 4.0], 9=[9->10: 1.2, 9->11: 1.3, 9->12: 10.0], 10=[], 11=[11->12: 6.4], 12=[], 4=[], 5=[5->4: 9.0], 1=[]}";
		Assert.assertTrue("Grafo mal creado", resultado.equalsIgnoreCase(resultadoCorrecto));

		ShortestPaths<Integer> sp = test.crearCamino(grafo);
		resultado = sp.getPathTo().toString();
		System.out.println("Camino: "+resultado);
		resultadoCorrecto = "{4=6->4: 3.0, 6=7->6: 1.6, 7=8->7: 1.0, 9=6->9: 4.0, 10=9->10: 1.2, 11=9->11: 1.3, 12=11->12: 6.4}";
		Assert.assertTrue("Path mal creado", resultado.equalsIgnoreCase(resultadoCorrecto));
		
		int numVertices=0;
		BufferedReader br = new BufferedReader(new FileReader("src/Test/TestResultsGraph1.txt"));
	    try {
	        String line = br.readLine();
	        numVertices=Integer.parseInt(line);
	        line = br.readLine();
	        while (line != null) {
	        	results.add(line);
	        	line = br.readLine();
	        }
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        br.close();
	    }
	    for (int i = 0; i < numVertices; i++) {
	    	System.out.println(test.getPaths(i));
	    	System.out.println(results.get(i));
	    	Assert.assertEquals("Error en creacion de path",results.get(i), test.getPaths(i));
		}
	}
	
	/**
	 * Prueba con el grafo 3 que se encuentra en src/Test/graphSample4.txt.
	 * Compara los caminos m&aacutes cortos obtenidos a partir de cada v&eacutertices del grafo con los resultados que hay en
	 * src/Test/TestResultsGraph2.txt
	 */
	@Test
	public void test2GrafoValoresEnteros() throws IOException {
		System.out.println("===========================================================Test 2===========================================================");
		String [] arr = new String[1];
		ArrayList<String> results = new ArrayList<String>();
		String fileName="src/Test/graphSample2.txt";
		arr[0]=fileName;
		test.main(arr);
		
		int numVertices=0;
		BufferedReader br = new BufferedReader(new FileReader("src/Test/TestResultsGraph2.txt"));
	    try {
	        String line = br.readLine();
	        numVertices=Integer.parseInt(line);
	        line = br.readLine();
	        while (line != null) {
	        	results.add(line);
	        	line = br.readLine();
	        }
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        br.close();
	    }
	    for (int i = 0; i < numVertices; i++) {
	    	System.out.println(test.getPaths(i));
	    	System.out.println(results.get(i));
	    	Assert.assertEquals(results.get(i), test.getPaths(i));
		}
	}
}

