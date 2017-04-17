package co.edu.javeriana.ashyi.kiss.Actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import co.edu.javeriana.ASHYI.hbm.GrafoRelacionesImpl;
import co.edu.javeriana.ASHYI.hbm.ItemPlanImpl;
import co.edu.javeriana.ASHYI.hbm.ItemsUsuarioImpl;
import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicasUsuario;
import co.edu.javeriana.ASHYI.model.Grafo;
import co.edu.javeriana.ASHYI.model.GrafoRelaciones;
import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ASHYI.model.ItemsUsuario;
import co.edu.javeriana.ASHYI.model.Usuario;
import co.edu.javeriana.ashyi.Graph.Camino;
import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.Graph.GraphEdge;
import co.edu.javeriana.ashyi.Graph.ShortestPaths;
import co.edu.javeriana.ashyi.Graph.Methods.MetodosFuncionDistancia;
import co.edu.javeriana.ashyi.kiss.Message;
import co.edu.javeriana.ashyi.kiss.Agents.Agent;
import co.edu.javeriana.ashyi.kiss.Data.ExecutorASHYIAgentData;

public class ExecutorCalculateDistanceG<T> extends MessagePassingAction<T>{
	
	public ExecutorCalculateDistanceG(Message mensaje) {
		super(mensaje);
	}

public T execute() {
		try {
			Message datos = (Message)this.getMensaje();
			calculateDistance(datos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Calcula la distancia entre una actividad y un usuario ejecutor
	 * recorriendo el grafo general y calculando las distancias individuales a cada nodo
	 * @param datosLlegan datos que llegan del evento
	 * @throws IOException
	 */
	private void calculateDistance(Message datosLlegan) throws IOException {

		Agent myAgent = this.getAgent().getAdmLocal().getAgent(this.getAgent().getID());

		ExecutorASHYIAgentData datos = (ExecutorASHYIAgentData) myAgent.getDatos();

		List<Object> listaDatos = (List<Object>) datosLlegan.getMessage();
		List<CaracteristicasUsuario> perfil = (List) listaDatos.get(0);
		Graph<Integer> grafo = (Graph) listaDatos.get(1);

		double usuario[] = new double[55];
		int pos = 0;

		// List estilos = perfil.get(0);
		// List personalidad = perfil.get(1);
		// List habilidades = perfil.get(2);
		// List competencias = perfil.get(3);

		// for(int i = 0; i<estilos.size(); i++)
		// {
		// usuario[pos] =
		// ((CaracteristicasUsuario)estilos.get(i)).getNivel();
		// pos++;
		// }
		//
		// for(int i = 0; i<personalidad.size(); i++)
		// {
		// usuario[pos] =
		// ((CaracteristicasUsuario)personalidad.get(i)).getNivel();
		// pos++;
		// }
		//
		// for(int i = 0; i<habilidades.size(); i++)
		// {
		// usuario[pos] =
		// ((CaracteristicasUsuario)habilidades.get(i)).getNivel();
		// pos++;
		// }
		//
		// for(int i = 0; i<competencias.size(); i++)
		// {
		// usuario[pos] =
		// ((CaracteristicasUsuario)competencias.get(i)).getNivel();
		// pos++;
		// }
		Consulta consAHYI = new Consulta();
		// construir arreglo de usuario
		usuario = construirusuario(perfil, consAHYI);
		// System.out.println("Grafo de unidad "+listaDatos.get(2)+" tiene vertices: "+grafo.getNumVertices());

		MetodosFuncionDistancia m = new MetodosFuncionDistancia();
		usuario = m.normalizarEstilo(usuario, 4);

		Graph grafoActividadesusuario = new Graph<ItemPlanImpl>();
		Integer idItemsInicial = 0;

		List<Double> distanciasIniciales = new ArrayList<Double>();
		double minDinicial = 10;
		int opcionD = 2;// ecuclediana

		// consultar las caracteristicas del sistema
		List<List<Caracteristica>> caracteristicas = consAHYI.getCaracteristicas();

		//1 --> precondiciones, false --> sin personalidad en el tipo o true --> con personalidad en el tipo
		Map<Integer, List<Caracteristica>> mapa = getCaracteristicasActividad(consAHYI, grafo, 1, false);
		//2 --> postcondiciones, false --> sin personalidad en el tipo o true --> con personalidad en el tipo
		Map<Integer, List<Caracteristica>> mapaPost = getCaracteristicasActividad(consAHYI, grafo, 2, false);

		Map<Integer, ItemPlan> mapaItems = getItemPlanActividades(consAHYI, grafo);
		
		// encontrar items iniciales
		for (GraphEdge edge : grafo.getEdges()) {
			ItemPlan itemTemp = consAHYI.getItemPlan((int) edge.getOrigin());
			if (itemTemp.isEstaActivo()) {
				if (itemTemp.getOrden() == 1)// primera actividad
				{
					//if (!idItemsInicial.contains(itemTemp.getIdItemPlan())) {
						idItemsInicial=itemTemp.getIdItemPlan();
						break;

						// cual es la actividad inicial mas pequenia
//						double actividad[] = construirActividad(itemTemp, caracteristicas, mapa);
//						double d = m.distanciaMultiple(opcionD, usuario, actividad);
//						distanciasIniciales.add(d);
//						if (d < minDinicial) {
//							minDinicial = d;
//							idItemInicial = itemTemp.getIdItemPlan();
//						}
//					}
				}
			}
		}

		Camino caminoMasCorto = new Camino();

		// encontrar el orden mayor
		ItemPlan itemInicial = consAHYI.getItemPlan(idItemsInicial);
		ArrayList<Integer> listaAdyacencia = grafo.adjList(idItemsInicial);
		// for(int id:listaAdyacencia)
		// System.out.println("!!!!!!!!!!!!!!!!!!: "+id);
		Map<Integer, LinkedHashSet<GraphEdge>> mapaL = grafo.getGraph();
		Set<GraphEdge> hs = new LinkedHashSet<>();
		hs.addAll(mapaL.get(itemInicial.getIdItemPlan()));
		// for(GraphEdge e:hs)
		// System.out.println("!!!!!!!!: "+e);
		double distanciaRecorrida = 0;
		List<Integer> camino = new ArrayList<Integer>();
		
		System.out.println("Iniciando calculo de camino mas corto con item inicial: " + idItemsInicial);

		int idNodoActual = itemInicial.getIdItemPlan();
		camino.add(idNodoActual);
		//buscarItem(grafo, idNodoActual, mapaItems, usuario, mapa, mapaPost, caracteristicas, distanciaRecorrida, camino, caminoMasCorto);
		ShortestPaths sp = new ShortestPaths(); 
		sp.CaminoMasCorto(grafo, idNodoActual, mapaItems, usuario, mapa, mapaPost, caracteristicas, distanciaRecorrida, camino, caminoMasCorto);

		System.out.println("Camino mas corto: distacia: " + caminoMasCorto.getDistanciaMasCorta() + " camino: "
				+ caminoMasCorto.getCaminoMasCorto().toString());

		// for(int idItem: grafo.getVertices())
		// {
		// ItemPlan item = consAHYI.getItemPlan(idItem);
		// //consultar post condiciones de la actividad (2)
		// //List<Caracteristica> posCondiciones =
		// consAHYI.getPostPreActividad(item.getIdActividad(), 2);
		// //usuarioTemp = modificarusuario(usuarioTemp,
		// posCondiciones);
		//
		// if(item.getOrden() == 1)//primera actividad
		// {
		// //cual es la actividad inicial mas pequenia
		// double actividad[] = construirActividad(item);
		// double d = m.distanciaMultiple(opcionD,usuario,actividad);
		// distanciasIniciales.add(d);
		// if(d < minDinicial)
		// {
		// minDinicial = d;
		// idItemInicial = idItem;
		// }
		// }
		// }
		//
		// //encontrar el orden mayor
		// for(GraphEdge edge: grafo.getEdges())
		// {
		// double actividad[] = new double[55];
		// //siguiente actividad
		// ItemPlan itemTemp = consAHYI.getItemPlan((int)edge.getDestination());
		// if(itemTemp.isEstaActivo())
		// {
		// actividad = construirActividad(itemTemp);
		// for(int i = 0;i < actividad.length; i++)
		// System.out.println("!!!!!!!!! actividad de item: "+itemTemp.getIdItemPlan()+" ->"+actividad[i]);
		//
		// double distanciaTotal = 0;
		// distanciaTotal = m.distanciaMultiple(opcionD,usuario,actividad);
		//
		// System.out.println("Distancia Total entre actividad y usuario: "+distanciaTotal);
		//
		// //guardar distancia
		// edge.setWeight(distanciaTotal);
		// }
		// else
		// edge.setWeight(Double.POSITIVE_INFINITY);
		// }

		// id actividades mayor nivel

		// List<Integer> idActividades = (List<Integer>) listaDatos.get(2);
		//
		// for(GraphEdge edge: grafo.getEdges())
		// {
		// System.out.println("!!!!!!! grafo: "+edge);
		// }
		//
		// //calcular la ruta mas corta
		// ShortestPaths sp = new ShortestPaths(grafo, idItemInicial);
		// Map<Integer, GraphEdge> mapaD = sp.getPathTo();
		// Map<Integer, Double> distancias = sp.getDistanceTo();

		// lista de actividades
		List actividades = new ArrayList<>();

		// partir de la actividad del orden mayor de menor distancia, buscarla
		// en el map y hacer el camino de para atr�s
		// guardar eso como grafo del usuario
		// int i = 0;
		// if(idActividades.size()>0)
		// {
		// int menor=idActividades.get(0);
		// for(Integer id : idActividades)
		// {
		// if(distancias.containsKey(id)&&(distancias.get(id)<=distancias.get(menor)))
		// {
		// menor=id;
		// }
		// }
		// //ya sabemos cual es la actividad final con menor distancia desde la
		// inicial
		// Integer actual=menor;
		// for(Integer id : mapaD.keySet())
		// {
		// System.out.println("!!!!!!!!!!!!!!!!!!!!!! id has distancia "+id);
		// System.out.println("!!!!!!!!!!!!!!!!!!!!!! objeto has distancia "+mapaD.get(id));
		// }
		// }

		String idUsuario = this.getAgent().getID();
		idUsuario = idUsuario.replace("Executor-", "");
		idUsuario = getIdUsuarioFormAlias(idUsuario);
        double nota=0;
        Integer idItemPLan=0;
		int tipoRuta = (int) listaDatos.get(4);

		if (tipoRuta == 1)// cambios de actividades
		{
			System.out.println("Grafo por cambio en actividades");
			Usuario us = consAHYI.getUsuario(idUsuario);
			List actividadesUsuario = datos.getActividadesMapa((int) listaDatos.get(3));
			Graph grafoUsuario = datos.getGrafoEjecutarMapa((int) listaDatos.get(3));

			if (actividadesUsuario == null) {
				actividadesUsuario = new ArrayList<>();
				actividadesUsuario = consAHYI.getItemsPlanUsuarioActividad(idUsuario, (Integer) listaDatos.get(3));
			}

			System.out.println("Actividades: " + actividadesUsuario.size());
			int contieneTodas = 0;
			// si se elimino una actividad del camino
			if (caminoMasCorto.getCaminoMasCorto().size() < actividadesUsuario.size()) {
				System.out.println("se elimino una actividad del camino");
				for (int i = 0; i < actividadesUsuario.size(); i++) {
					boolean rta = false;
					if (actividadesUsuario.get(i) instanceof Integer)
						rta = verificarContieneId(caminoMasCorto.getCaminoMasCorto(),
								(Integer) actividadesUsuario.get(i));
					else if (actividadesUsuario.get(i) instanceof String)
						rta = verificarContieneId(caminoMasCorto.getCaminoMasCorto(),
								Integer.valueOf((String) actividadesUsuario.get(i)));
					if (!rta) {
						actividadesUsuario.remove(i);
						i--;
						break;
					}
				}
				for(int i = 0; i < caminoMasCorto.getCaminoMasCorto().size(); i++){

					ItemPlan iPnew = consAHYI.getItemPlan(caminoMasCorto.getCaminoMasCorto().get(i));
					if (!iPnew.getIdActividad().getNivel().equals("InicioUD")) {
							Integer idIPOld = buscarItemMismoNivel(iPnew.getOrden(), actividadesUsuario, consAHYI);
							if (idIPOld != 0) {
								ItemsUsuario iU = consAHYI.getItemUsuario(idIPOld, us.getIdUsuario());
								if (iU.isRealizada()){
									nota=iU.getNota();
									idItemPLan=iU.getIdItemPlan().getIdItemPlan();
								}
								}
						} else {
							ItemsUsuario iU = consAHYI.getItemUsuario(caminoMasCorto.getCaminoMasCorto().get(i),
									us.getIdUsuario());
							if (iU.isRealizada()) {
								nota=iU.getNota();
								idItemPLan=iU.getIdItemPlan().getIdItemPlan();
							}
						}
					}
			} else if (caminoMasCorto.getCaminoMasCorto().size() >= actividadesUsuario.size()) {
				System.out.println("se agrega una actividad del camino");
				
				for(int i = 0; i < caminoMasCorto.getCaminoMasCorto().size(); i++){

					ItemPlan iPnew = consAHYI.getItemPlan(caminoMasCorto.getCaminoMasCorto().get(i));
					if (!iPnew.getIdActividad().getNivel().equals("InicioUD")) {
							Integer idIPOld = buscarItemMismoNivel(iPnew.getOrden(), actividadesUsuario, consAHYI);
							if (idIPOld != 0) {
								ItemsUsuario iU = consAHYI.getItemUsuario(idIPOld, us.getIdUsuario());
								if (iU.isRealizada()){
									nota=iU.getNota();
									idItemPLan=iU.getIdItemPlan().getIdItemPlan();
								}
								}
						} else {
							ItemsUsuario iU = consAHYI.getItemUsuario(caminoMasCorto.getCaminoMasCorto().get(i),
									us.getIdUsuario());
							if (iU.isRealizada()) {
								nota=iU.getNota();
								idItemPLan=iU.getIdItemPlan().getIdItemPlan();
							}
						}
					}
				
				for (int i = 0; i < caminoMasCorto.getCaminoMasCorto().size(); i++) {
					ItemPlan iPnew = consAHYI.getItemPlan(caminoMasCorto.getCaminoMasCorto().get(i));
					if (!iPnew.getIdActividad().getNivel().equals("InicioUD")) {
						boolean rta = verificarContieneId(actividadesUsuario, caminoMasCorto.getCaminoMasCorto().get(i));
						//System.out.println("contiene; " + rta);
						//System.out.println("ac; " + actividadesUsuario);
						//System.out.println("camino; " + caminoMasCorto.getCaminoMasCorto().get(i));
						// si no la contiene, es porq hay algun cambio en el
						// camino
						if (!rta) {
							System.out.println("no la contiene, es porque hay algun cambio en el camino");
							Integer idIPOld = buscarItemMismoNivel(iPnew.getOrden(), actividadesUsuario, consAHYI);

							// verificar si ya se realizo la actividad, si no es
							// asi, cambiar items
							if (idIPOld != 0) {
								int index = buscarItemMismoNivelIndex(iPnew.getOrden(), actividadesUsuario, consAHYI);
								ItemsUsuario iU = consAHYI.getItemUsuario(idIPOld, us.getIdUsuario());
								if (!iU.isRealizada()) {
									actividadesUsuario.set(index, caminoMasCorto.getCaminoMasCorto().get(i));
									consAHYI.deleteObject(iU);
								} else {
									
									if (iU.getNota() >= 3)// si realizo la actividad y la paso, revisar si hay otras para eliminarlas
									{
										for (int j = i + 1; j < caminoMasCorto.getCaminoMasCorto().size(); j++) {
											ItemPlan iPT = consAHYI.getItemPlan(caminoMasCorto.getCaminoMasCorto().get(
													j));

											if (iU.getIdItemPlan().getIdActividad().getIdActividad().intValue() == iPT
													.getIdActividad().getIdActividad().intValue()) {
												actividadesUsuario.remove(j);
												caminoMasCorto.getCaminoMasCorto().remove(j);
												consAHYI.deleteObject(iU);
												j--;
											}
										}
									}
								}
							} else// no hay actividad del mismo tipo para
									// cambiar
							{
								int indexActividadAnterior = buscarIndexActividadAnterior(iPnew.getOrden(),
										actividadesUsuario, consAHYI);
								List listaTemp = new ArrayList<>();
								int posicion = 0;
								for (int j = 0; j < actividadesUsuario.size(); j++) {
									if (j == indexActividadAnterior + 1)// index del item a aniadir
									{
										listaTemp.add(iPnew.getIdItemPlan());// agregar  el nuevo
										listaTemp.add(actividadesUsuario.get(i));// agregar el siguiente
									} else
										listaTemp.add(actividadesUsuario.get(i));
								}

								actividadesUsuario.clear();
								actividadesUsuario.addAll(listaTemp);
							}
						} else {
							contieneTodas++;
							System.out.println("Contiene el itemPlan");
							ItemsUsuario iU = consAHYI.getItemUsuario(caminoMasCorto.getCaminoMasCorto().get(i),
									us.getIdUsuario());
							
							if (iU.isRealizada()) {								
								if (iU.getNota() >= 3)// si realizo la actividad y la paso, revisar si hay otras para eliminarlas
								{
									for (int j = i + 1; j < caminoMasCorto.getCaminoMasCorto().size(); j++) {
										ItemPlan iPT = consAHYI.getItemPlan(caminoMasCorto.getCaminoMasCorto().get(j));

										if (iU.getIdItemPlan().getIdActividad().getIdActividad().intValue() == iPT
												.getIdActividad().getIdActividad().intValue()) {
											ItemsUsuario iUT = consAHYI.getItemUsuario(iPT.getIdItemPlan(),
													us.getIdUsuario());
											for(int y = 0; y< actividadesUsuario.size(); y++)
											{
												if(((int)actividadesUsuario.get(y)) == iPT.getIdItemPlan().intValue())
												{
													actividadesUsuario.remove(y);
													y--;
												}
											}
											//if(actividadesUsuario.size() == caminoMasCorto.getCaminoMasCorto().size())
												
											caminoMasCorto.getCaminoMasCorto().remove(j);
											if(iUT != null)
												consAHYI.deleteObject(iUT);
											j--;
										}
									}
								}
							}
						}
					}
				}
			}
			//si el grafo a generar es el mismo que el que ya tiene, no hacer nada
			if(contieneTodas != caminoMasCorto.getCaminoMasCorto().size()-1 && contieneTodas != actividadesUsuario.size()-1)
			{
				actividades = pasarActividades(actividades, actividadesUsuario);
				//System.out.println("tammm: " + actividades.size());
				// actividades.addAll(actividadesUsuario);
	
				// actualizar relaciones grafo
				//actualizarGrafoRelaciones(actividades, (int) listaDatos.get(3), us, consAHYI);
				
				//guardar grafo nuevo
				consAHYI.saveGrafoUsuario(actividades, idUsuario, (int) listaDatos.get(3),"replanificacion",nota,idItemPLan);
				
				//actualizar items
				actualizarItemsUsuario(actividades, (int) listaDatos.get(3), us, consAHYI);
			}else{
				consAHYI.generarLogSinCambios(idUsuario, "mismo grafo",  (int) listaDatos.get(3),nota,idItemPLan);
			}
		} else// inicial
		{
			//System.out.println("Grasfo inicial");
			if (caminoMasCorto.getCaminoMasCorto().size() == 1) {
				GraphEdge<Integer> edge = new GraphEdge<Integer>(caminoMasCorto.getCaminoMasCorto().get(0), 0, 0);
				grafoActividadesusuario.addEdge(edge.getOrigin(), edge.getDestination(), edge.getWeight());
			} else {
				for (int i = 0; i < caminoMasCorto.getCaminoMasCorto().size() - 1; i++) {
					GraphEdge<Integer> edge = new GraphEdge<Integer>(caminoMasCorto.getCaminoMasCorto().get(i),
							caminoMasCorto.getCaminoMasCorto().get(i + 1), 0);
					actividades.add(caminoMasCorto.getCaminoMasCorto().get(i));
					grafoActividadesusuario.addEdge(edge.getOrigin(), edge.getDestination(), edge.getWeight());

				}
			}
			actividades.add(caminoMasCorto.getCaminoMasCorto().get(caminoMasCorto.getCaminoMasCorto().size() - 1));

			// Almacenar en base de datos
			consAHYI.saveGrafoUsuario(actividades, idUsuario, (int) listaDatos.get(3),"origen",nota,idItemPLan);
			consAHYI.saveItemsUsuario(actividades, idUsuario);
		}
		// cambiar estado
		datos.setActividadesMapa(actividades, (int) listaDatos.get(3));
		datos.setGrafoEjecutarMapa(grafoActividadesusuario, (int) listaDatos.get(3));
		datos.setEstaCalculadoMapa(true, (int) listaDatos.get(3));

	}
	
	/**
	 * Con base en los datos del perfil de usuario ejecutor, construir un arreglo con los valores del mismo
	 * @param perfil caracteristicas del usaurio ejecutor
	 * @param consAHYI variable para la consulta / puente @see Consulta
	 * @return arreglo de las caracteristicas del usuario ejecutor
	 */
	private double[] construirusuario(List<CaracteristicasUsuario> perfil, Consulta consAHYI) {
		double[] usuario = new double[55];
		// consultar las caracteristicas del sistema
		// Consulta consAHYI = new Consulta();
		List<List<Caracteristica>> caracteristicas = consAHYI.getCaracteristicas();

		int pos = 0;
		// organizar el vector de usuario seg�n el orden de las
		// caracteristicas
		for (int i = 0; i < caracteristicas.size(); i++)
			for (Caracteristica cG : caracteristicas.get(i)) {
				for (CaracteristicasUsuario cU : perfil) {
					if (cG.getIdCaracteristica() == cU.getIdCaracteristica().getIdCaracteristica()) {
						usuario[pos] = cU.getNivel();
						break;
					} else
						usuario[pos] = 0;
				}
				pos++;
			}

		return usuario;
	}
	
	/**
	 * Obtener las caracteristicas de las actividades del grafo de una actividad recursiva
	 * @param c variable para la consulta / puente @see Consulta
	 * @param grafo Grafo de actividades de la actividad recursiva
	 * @param tipo Tipo de consulta (pre 1 - post 2)
	 * @param correlacion  false --> sin personalidad en el tipo o true --> con personalidad en el tipo
	 * @return mapa con las carateristicas de las actividades
	 */
	public Map<Integer, List<Caracteristica>> getCaracteristicasActividad(Consulta c, Graph<Integer> grafo, int tipo, boolean correlacion) {
		Map<Integer, List<Caracteristica>> mapa = new LinkedHashMap<Integer, List<Caracteristica>>();
		for (int vertice : grafo.getVertices()) {

			System.out.println("!!!verticeN�: "+vertice);
			ItemPlan item = c.getItemPlan(vertice);
			System.out.println("!!!itemN�: "+item.getIdItemPlan());
			// 1 pre condiciones y 2 consulta tipo
			List<Caracteristica> caracteristicasActividad = getCaracteristicasAc(item, c, tipo, 2, correlacion);
			// for(Caracteristica d: caracteristicasActividad)
			// {
			// System.out.println("CAr: "+vertice+"  : "+d.getIdCaracteristica());
			// }
			mapa.put(vertice, caracteristicasActividad);
		}
		return mapa;
	}

	/**
	 * Obtener los ItemPlan de las actividades del grafo de una actividad recursiva
	 * @param c variable para la consulta / puente @see Consulta
	 * @param grafo Grafo de actividades de la actividad recursiva
	 * @return mapa con los ItemPlan de las actividades
	 */
	public Map<Integer, ItemPlan> getItemPlanActividades(Consulta c, Graph<Integer> grafo) {
		Map<Integer, ItemPlan> mapa = new LinkedHashMap<Integer, ItemPlan>();
		for (int vertice : grafo.getVertices()) {
			ItemPlan item = c.getItemPlan(vertice);
			mapa.put(vertice, item);
		}
		return mapa;
	}
	
	/**
	 * Extraer el ID de usuario del alias completo del agente 
	 * @param aliasAg Alias del agente
	 * @return ID de usuario
	 */
	public String getIdUsuarioFormAlias(String aliasAg) {
		String newAlias = "";
		String[] subStrings = aliasAg.split("-");
		for (int i = 0; i < subStrings.length; i++) {
			// System.out.println(subStrings[i]);
			if (i > 0 && i < subStrings.length - 1) {
				newAlias += subStrings[i];
				if (i < subStrings.length - 2)
					newAlias += "-";
			}
		}

		return newAlias;
	}

	/**
	 * Segun las caracteristicas dadas (alncanzadas / no alcanzadas), modificar el arreglo del usuario ejecutor
	 * @param usuario arreglo de las caracteristicas del usuario ejecutor
	 * @param caracteristicasAc Caracteristicas post de una actividad
	 * @param caracteristicas Lista completa de caracteristicas del sistema
	 * @return arreglo modificado de las caracteristicas del usuario ejecutor
	 */
	private double[] modificarusuario(double[] usuario, List<Caracteristica> caracteristicasAc,
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
					if (cAc.getIdCaracteristica() == cG.getIdCaracteristica()) // si
																				// es
																				// la
																				// post
																				// condicion
																				// de
																				// la
																				// actividad
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
	
	/**
	 * Verificar si la lista de actividades dada contiene un id de un determinado item
	 * @param actividades lista de actividades para buscar
	 * @param idItemPlan id de item a buscar
	 * @return contiene el item o no
	 */
	private boolean verificarContieneId(List actividades, Integer idItemPlan) {

		List<Integer> listN = new ArrayList<Integer>();

		for (int i = 0; i < actividades.size(); i++) {
			if (actividades.get(i) instanceof Integer) {
				listN.add((Integer) actividades.get(i));
			}
			if (actividades.get(i) instanceof String) {
				listN.add(Integer.valueOf((String) actividades.get(i)));
			}
		}

		return listN.contains(idItemPlan);
	}
	
	/**
	 * Busca el indice de un item del mismo nivel al necesario
	 * @param orden a buscar
	 * @param actividadesUsuario lista de actividades para buscar
	 * @param c variable para la consulta / puente @see Consulta
	 * @return indice del item del mismo orden encontrado
	 */
	private int buscarItemMismoNivelIndex(int orden, List actividadesUsuario, Consulta c) {
		int idItem = 0;
		for (int i = 0; i < actividadesUsuario.size(); i++) {
			ItemPlan iP = new ItemPlanImpl();
			if (actividadesUsuario.get(i) instanceof Integer)
				iP = c.getItemPlan((int) actividadesUsuario.get(i));
			else if (actividadesUsuario.get(i) instanceof String)
				iP = c.getItemPlan(Integer.valueOf((String) actividadesUsuario.get(i)));
			if (iP.getOrden() == orden) {
				idItem = i;
				break;
			}
		}

		return idItem;
	}

	/**
	 * Busca un item del mismo nivel al necesario
	 * @param orden a buscar
	 * @param actividadesUsuario lista de actividades para buscar
	 * @param c variable para la consulta / puente @see Consulta
	 * @return id del item del mismo orden encontrado
	 */
	private Integer buscarItemMismoNivel(int orden, List actividadesUsuario, Consulta c) {
		int idItem = 0;
		for (int i = 0; i < actividadesUsuario.size(); i++) {
			ItemPlan iP = new ItemPlanImpl();
			if (actividadesUsuario.get(i) instanceof Integer)
				iP = c.getItemPlan((int) actividadesUsuario.get(i));
			else if (actividadesUsuario.get(i) instanceof String)
				iP = c.getItemPlan(Integer.valueOf((String) actividadesUsuario.get(i)));
			if (iP.getOrden() == orden) {
				idItem = iP.getIdItemPlan();
				break;
			}
		}

		return idItem;
	}
	
	/**
	 * Encuentra el indice de
	 * @param orden de item a buscar 
	 * @param actividadesUsuario lista de actividades para buscar
	 * @param c variable para la consulta / puente @see Consulta
	 * @return indice del item del mismo orden encontrado
	 */
	private int buscarIndexActividadAnterior(int orden, List actividadesUsuario, Consulta c) {
		int idItem = 0;
		for (int i = 0; i < actividadesUsuario.size(); i++) {
			ItemPlan iP = new ItemPlanImpl();
			if (actividadesUsuario.get(i) instanceof Integer)
				iP = c.getItemPlan((int) actividadesUsuario.get(i));
			else if (actividadesUsuario.get(i) instanceof String)
				iP = c.getItemPlan(Integer.valueOf((String) actividadesUsuario.get(i)));
			if (iP.getOrden() == orden - 1) {
				idItem = i;
				break;
			}
		}

		return idItem;
	}
	
	/**
	 * Convertir las actividades a una lista con objetos necesarios
	 * @param actividades a retornar
	 * @param actividadesUsuario a convertir
	 * @return
	 */
	private List pasarActividades(List actividades, List actividadesUsuario) {

		for (int i = 0; i < actividadesUsuario.size(); i++) {
			if (actividadesUsuario.get(i) instanceof Integer) {
				actividades.add((Integer) actividadesUsuario.get(i));
			}
			if (actividadesUsuario.get(i) instanceof String) {
				actividades.add(Integer.valueOf((String) actividadesUsuario.get(i)));
			}
		}

		return actividades;
	}
	
	/**
	 * Actualiza los items usuario del ejecutor en la base de datos
	 * @param actividades lista actividades a actualizar
	 * @param idActividad id actividad recursiva asociada
	 * @param usuario usuario ejecutor involucrado
	 * @param c variable para la consulta / puente @see Consulta
	 */
	private void actualizarItemsUsuario(List actividades, int idActividad, Usuario usuario, Consulta c) {

		for (int i = 0; i < actividades.size(); i++) {
			ItemPlan iP = new ItemPlanImpl();
			if (actividades.get(i) instanceof Integer)
				iP = c.getItemPlan((int) actividades.get(i));
			else if (actividades.get(i) instanceof String)
				iP = c.getItemPlan(Integer.valueOf((String) actividades.get(i)));

			ItemsUsuario iU = c.getItemUsuario(iP.getIdItemPlan(), usuario.getIdUsuario());

			if (iU == null) {
				iU = new ItemsUsuarioImpl();
				iU.setIdItemPlan(iP);
				iU.setIdUsuario(usuario);
				iU.setEstaActivo(true);

				c.saveObject(iU);
			}
		}
	}

	/**
	 * Actualizar las relaciones del grafo del usuario ejecutor en la base de datos
	 * @param actividades lista actividades a actualizar
	 * @param idActividad id actividad recursiva asociada
	 * @param usuario usuario ejecutor involucrado
	 * @param c variable para la consulta / puente @see Consulta
	 */
	private void actualizarGrafoRelaciones(List actividades, int idActividad, Usuario usuario, Consulta c) {

		Grafo grafoU = c.getGrafoUsuario(usuario, idActividad);

		List<GrafoRelaciones> gR = c.getGrafoRelaciones(grafoU.getIdGrafo());

		for (int i = 0; i < gR.size(); i++)
			c.deleteObject(gR.get(i));

		if (actividades.size() == 1) {
			GrafoRelaciones relacion = new GrafoRelacionesImpl();
			relacion.setIdGrafo(grafoU);
			ItemPlan iP = new ItemPlanImpl();
			if (actividades.get(0) instanceof Integer)
				relacion.setIdItemPlan_Origen((int) actividades.get(0));
			else if (actividades.get(0) instanceof String)
				relacion.setIdItemPlan_Origen(Integer.valueOf((String) actividades.get(0)));

			relacion.setOrden(1);
			c.saveObject(relacion);
		} else {
			int orden = 1;
			for (int j = 0; j < actividades.size() - 1; j++) {
				GrafoRelaciones relacion = new GrafoRelacionesImpl();
				relacion.setIdGrafo(grafoU);
				relacion.setOrden(orden);
				if (actividades.get(j) instanceof Integer) {
					relacion.setIdItemPlan_Origen((int) actividades.get(j));
					relacion.setIdItemPlan_Destino((int) actividades.get(j + 1));
				} else if (actividades.get(j) instanceof String) {
					relacion.setIdItemPlan_Origen(Integer.valueOf((String) actividades.get(j)));
					relacion.setIdItemPlan_Destino(Integer.valueOf((String) actividades.get(j + 1)));
				}

				c.saveObject(relacion);
				orden++;
			}
		}
	}
	
	/**
	 * Encontrar las caracteristicas pre o post condicion de una actividad 
	 * @param item ItemPlan a buscar
	 * @param consAHYI variable para la consulta / puente @see Consulta
	 * @param tipo tipo de consulta (pre (1) o post(2))
	 * @param queConsulta tipo de consulta (TipoActividad (2))
	 * @param correlacion false --> sin personalidad en el tipo o true --> con personalidad en el tipo
	 * @return caracteristicas pre o post condicion de una actividad
	 */
	public List<Caracteristica> getCaracteristicasAc(ItemPlan item, Consulta consAHYI, int tipo, int queConsulta, boolean correlacion) {
		Actividad a = item.getIdActividad();
		return consAHYI.getCaracteristicasActividad(a, tipo, queConsulta, correlacion);
	}

}