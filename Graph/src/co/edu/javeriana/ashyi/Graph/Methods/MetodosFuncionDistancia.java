package co.edu.javeriana.ashyi.Graph.Methods;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import co.edu.javeriana.ashyi.Graph.FuncionDistancia;

/**
 * Clase que encierra los metodos usados para calular la distancia entre una actividad y un usuario
 * @author ASHYI
 * @see FuncionDistancia
 */
public class MetodosFuncionDistancia extends FuncionDistancia{
	
	/**
	 * valor minimo para normalizar el estilo de aprendizaje
	 */
	int minEstilo=0;
	/**
	 * valor maximo para normalizar el estilo de aprendizaje
	 */
	int maxEstilo=20;
	/**
	 * valor minimo nuevo para normalizar el estilo de aprendizaje
	 */
	int minNewEstilo=0;
	/**
	 * valor maximo nuevo para normalizar el estilo de aprendizaje
	 */
	int maxNewEstilo=1;
		
	/**
	 * valor minimo para normalizar la Personalidad
	 */
	int minPersonalidad=0;
	/**
	 * valor maximo para normalizar la Personalidad
	 */
	int maxPersonalidad=22;
	/**
	 * valor minimo nuevo para normalizar la Personalidad
	 */
	int minNewPersonalidad=0;
	/**
	 * valor maximo nuevo para normalizar la Personalidad
	 */
	int maxNewPersonalidad=1;

	/**
	 * Metodo para leer desde un archivo los valores de un vector 
	 * para representar un usuario o actividad
	 * @param vector arreglo a modificar
	 * @param posArchivo posicion desde donde se debe leer el archivo
	 * @return arreglo con los datos
	 */
	public double[] leerVector(double[] vector, int posArchivo)
	{
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader (isr);
		try {
			
				File archivo = new File ("Recursos/datos.txt");
				FileReader fr = new FileReader (archivo);
				BufferedReader brf = new BufferedReader(fr);
				
					String linea = brf.readLine();
					int posicionA = 0;
					while (linea!=null && posArchivo != posicionA) {
						 String[] numerosComoArray = linea.split(";");
					        for (int j = 0; j < numerosComoArray.length; j++) {
					            //System.out.println(numerosComoArray.length+" : "+numerosComoArray[j]);
					            vector[j] = Integer.parseInt(numerosComoArray[j]);
					        }
					        linea =  brf.readLine();
					        posicionA++;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		return vector;
	}
	
	/**
	 * Normalizacion por minimos y maximos para el estilo de aprendizaje
	 * @param vector arreglo con los datos que contiene el estilo a normalizar
	 * @param posicionMax hasta que posicion se debe leer los datos del estilo en el vector
	 * @return vector del usuario con los datos del estilo ya normalizados
	 */
	public double[] normalizarEstilo(double[] vector, int posicionMax)
	{
		for(int i = 0;i<posicionMax;i++)
		{
			vector[i] = (vector[i]-minEstilo)*(maxNewEstilo-minNewEstilo)/(maxEstilo-minEstilo)+minNewEstilo;
		}
		return vector;
	}
	
	/**
	 * Normalizacion por minimos y maximos para la personalidad
	 * @param vector arreglo con los datos que contiene el estilo a normalizar
	 * @param posicionMin desde que posicion se debe leer los datos de la personalidad en el vector
	 * @param posicionMax hasta que posicion se debe leer los datos de la personalidad en el vector
	 * @return vector del usuario con los datos de la personalidad ya normalizados
	 */
	public double[] normalizarPersonalidad(double[] vector, int posicionMin, int posicionMax)
	{
		for(int i = posicionMin;i<posicionMax;i++)
		{
			vector[i] = (vector[i]-minPersonalidad)*(maxNewPersonalidad-minNewPersonalidad)/(maxPersonalidad-minPersonalidad)+minNewPersonalidad;
		}
		return vector;
	}
	
	/**
	 * Medida que compara los datos de presencia o ausencia para encontrar la similitud entre dos puntos
	 * esta similitud se calcula para las caracteristicas del usuario y de la actividad
	 * @param usuario arreglo delos datos del usuario
	 * @param actividad arreglo delos datos de la actividad
	 * @param posI desde que posicion se debe leer los datos de las caracteristicas en el vector
	 * @param posF hasta que posicion se debe leer los datos de las caracteristicas en el vector
	 * @return distancia/similitud entre los dos arreglos
	 */
	public double jaccard(double[] usuario, double[] actividad, int posI, int posF)
	{
		double distanciaJ = 0;
		double dJ[] = new double [3];
		
		for(int i = posI ; i<posF;i++)
		{
			dJ[(int) (usuario[i]+actividad[i])] ++; 
		}
				
		distanciaJ = dJ[1] / (dJ[1]+dJ[2]);
		return distanciaJ;
	}
	
	//Distancias individuales
	/* (non-Javadoc)
	 * @see co.edu.javeriana.ashyi.Graph.FuncionDistancia#calculateDistance(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public double calculateDistance(double[] usuario, double[] actividad)
	{
		double distancia = 0;
		
		//euclediana
		int opcionD = 2;
		
		//Distancia de estilos
		double dE = this.distanciaE(Double.valueOf((int)opcionD), usuario, actividad, 0, 4);
		//System.out.println("Estiol: "+ dE);
		
		//Distancia de personalizaci�n
		double dP = this.Personalidad(usuario, actividad, 4, 20);
		//System.out.println("P: "+ dP);
		
		//Distancia de habilidades
		double dH = this.jaccard(usuario, actividad, 20, 39);
		//System.out.println("H: "+ dH);
		
		//Distancia de competencias
		double dC = this.jaccard(usuario, actividad, 39, 55);
		//System.out.println("C: "+ dC);
		
		//promedio ponderado
		distancia = this.promedioPonderado(dE,dP,dH,dC);
		
		return distancia;
	}
	
	/**
	 * Metodo calcular la distancia total entre dos puntos (usuario y actividad)
	 * que es afectada por unos fatores que dan peso a cada tipo de dato trabajado (estilo, personalidad, caracteristicas)
	 * @param dE distancia de estilos de aprendizaje
	 * @param dP distancia de personalidad
	 * @param dH distancia de habilidades
	 * @param dC distancia de competencias
	 * @return distancias multiplicadas por los fatores de cada uno
	 */
	public double leerFactores(double dE, double dP, double dH, double dC) {
		double distanciaT = 0;
				
		//try {
			//File archivo = new File ("resources/factores.txt");
			//FileReader fr = new FileReader (archivo);
			//BufferedReader brf = new BufferedReader(fr);
			
			//String linea = brf.readLine();
			String linea = "0.5;0.3;0.1;0.1";
			String[] factores = linea.split(";");
			
			distanciaT = ((dE * Double.valueOf(factores[0]) + dP * Double.valueOf(factores[1]) + dH * Double.valueOf(factores[2]) + dC * Double.valueOf(factores[3])) / (Double.valueOf(factores[0]) + Double.valueOf(factores[1]) + Double.valueOf(factores[2]) + Double.valueOf(factores[3])));
			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return distanciaT;
	}

	/**
	 * distancia (promedio ponderado) entre dos puntos
	 * @param dE distancia de estilos de aprendizaje
	 * @param dP distancia de personalidad
	 * @param dH distancia de habilidades
	 * @param dC distancia de competencias
	 * @return distancia (promedio ponderado) entre dos puntos
	 */
	private double promedioPonderado(double dE, double dP, double dH, double dC) {
		double distancia = 0;
		
		distancia = this.leerFactores(dE, dP, dH, dC);
		
		return distancia;
	}

	/**
	 * Calculo de distancia de personalidad entre dos puntos (usuario y actividad)
	 * @param usuario vector con los datos del usuario
	 * @param actividad vector con los datos de la actividad
	 * @param posI desde que posicion se debe leer los datos de la personalidad en el vector
	 * @param posF hasta que posicion se debe leer los datos de la personalidad en el vector
	 * @return
	 */
	private double Personalidad(double[] usuario, double[] actividad, int posI, int posF) {
		double distancia = 0;
		
		for(int i = posI;i <posF;i++ )
		{
			if(usuario[i] == actividad[i] && actividad[i] == 1)
			{
				distancia++;
			}
		}
		if(distancia != 0)
			distancia = 0;
		else
			distancia = 1;
		
		return distancia;
	}

	/**
	 * Metodo para calcular la distancia entre dos datos de usuario y  actividad
	 * metrica de la familia Minkowski (metricas basadas en la suma de las potencias de las diferencias)
	 * @param potencia raiz a usar en el metodo: 2 distancia euclediana
	 * @param usuario vector con datos del usuario
	 * @param actividad vector con datos de la actividad
	 * @param posI desde que posicion se debe leer los datos de la personalidad en el vector
	 * @param posF hasta que posicion se debe leer los datos de la personalidad en el vector
	 * @return distancia de estilo de aprendizaje entre usuario ya ctividad
	 */
	private double distanciaE(double potencia, double[] usuario, double[] actividad, int posI, int posF) {
		double distancia = 0;
		
		for(int i = posI;i <posF;i++ )
		{
			if(actividad[i] == 1)
			{
				distancia += Math.pow(actividad[i] - usuario[i],potencia);
			}
			else
				distancia += 1;
			
		}
		
		//distancia = Math.sqrt(distancia);
		double raiz = 1/potencia;
		distancia =  Math.pow(distancia, raiz);
		
		return distancia;
	}

}
