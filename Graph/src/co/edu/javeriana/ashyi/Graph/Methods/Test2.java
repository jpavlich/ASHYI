package co.edu.javeriana.ashyi.Graph.Methods;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class Test2 {

	public static void main(String[] args) {
		
		try {
			File archivo = new File ("src/main/resources/factores.txt");
			FileReader fr = new FileReader (archivo);
			BufferedReader brf = new BufferedReader(fr);
			
			String linea = brf.readLine();
			String[] factores = linea.split(";");
			
			System.out.println(factores[0]);
			
			//distanciaT = ((dE * Double.valueOf(factores[0]) + dP * Double.valueOf(factores[1]) + dH * Double.valueOf(factores[2]) + dC * Double.valueOf(factores[3])) / (Double.valueOf(factores[0]) + Double.valueOf(factores[1]) + Double.valueOf(factores[2]) + Double.valueOf(factores[3])));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		double estudiante[] = new double[55];
//		double actividad[] = new double[55];
//		
//		Metodos m = new Metodos();
//		
////		estudiante = m.leerVector(estudiante, 1);
////		actividad = m.leerVector(actividad, 2);
//		
//		InputStreamReader isr = new InputStreamReader(System.in);
//        BufferedReader br = new BufferedReader (isr);
//			try {
//				
//				System.out.println("Seleccione el m�todo de distancia para estilos de aprendizaje: \n1. Manhattan\n2. Euclediana\n3. Minkowski");
//				double opcionD = Double.valueOf(br.readLine());
//				
//				File archivo = new File ("/main/resources/datos.txt");
//				FileReader fr = new FileReader (archivo);
//				BufferedReader brf = new BufferedReader(fr);
//				String linea = brf.readLine();
//				int posicionA = 1;
//				while (linea!=null ) {
//					 String[] numerosComoArray = linea.split(";");
//				        for (int j = 0; j < numerosComoArray.length; j++) {
//				            if(posicionA % 2 == 0)
//				            	actividad[j] = Integer.parseInt(numerosComoArray[j]);
//				            else
//				            	estudiante[j] = Integer.parseInt(numerosComoArray[j]);
//				        }				        				        
//				
//				    if(posicionA % 2 == 0)
//				    {
//						estudiante = m.normalizarEstilo(estudiante, 4);
//								
//						double distanciaTotal = m.distanciaMultiple(opcionD,estudiante,actividad);
//					
//						System.out.println("Distancia Total entre actividad y estudiante: "+distanciaTotal);
//				    }
//				    
//				    linea =  brf.readLine();
//				    posicionA++;
//				}
//			} catch (NumberFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
	}

}

