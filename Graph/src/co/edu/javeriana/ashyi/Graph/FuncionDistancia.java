package co.edu.javeriana.ashyi.Graph;

/**
 * @author ASHYI
 * Interfaz para la funcion de distancia
 *
 */
public abstract class FuncionDistancia<T1, T2> {

	/**
	 * Calcular la distancia entre dos puntos (usuario y actividad)
	 * @param opcionD tipo de metodo a seleccionar
	 * @param usuario representa al punto usuario
	 * @param actividad representa al punto actividad
	 * @return la distancia entre los dos puntos (usuario y actividad)
	 */
	public double calculateDistance(T1 usuario, T2 actividad)
	{
		return Double.MAX_VALUE;
	}
	
	//public Double calculateDistance(Double opcionD,1 Double[] usuario, Double[] actividad);
}
