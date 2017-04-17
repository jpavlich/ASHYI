package co.edu.javeriana.ASHYI.logic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import co.edu.javeriana.ASHYI.hbm.GrafoImpl;
import co.edu.javeriana.ASHYI.hbm.GrafoRelacionesImpl;
import co.edu.javeriana.ASHYI.hbm.GrafosUsuarioImpl;
import co.edu.javeriana.ASHYI.hbm.ItemsUsuarioImpl;
import co.edu.javeriana.ASHYI.hbm.LogCaracteristicasImpl;
import co.edu.javeriana.ASHYI.hbm.LogGrafosImpl;
import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.CaracteristicaRecurso;
import co.edu.javeriana.ASHYI.model.CaracteristicasItemsUsuario;
import co.edu.javeriana.ASHYI.model.CaracteristicasUsuario;
import co.edu.javeriana.ASHYI.model.Grafo;
import co.edu.javeriana.ASHYI.model.GrafoRelaciones;
import co.edu.javeriana.ASHYI.model.GrafosUsuario;
import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ASHYI.model.ItemsUsuario;
import co.edu.javeriana.ASHYI.model.LogCaracteristicas;
import co.edu.javeriana.ASHYI.model.LogGrafos;
import co.edu.javeriana.ASHYI.model.Objetivo;
import co.edu.javeriana.ASHYI.model.ObjetivosActividad;
import co.edu.javeriana.ASHYI.model.Recurso;
import co.edu.javeriana.ASHYI.model.RecursosActividad;
import co.edu.javeriana.ASHYI.model.RespuestaItemsUsuario;
import co.edu.javeriana.ASHYI.model.Usuario;
/**
 * @author ASHYI
 * Puente de consulta entre agentes y base de datos
 */
public class Consulta {

	/**
	 * Contexto de aplicacion
	 */
	public ApplicationContext appContext;
	
	// ItemPlans organizados por objetivos de actividad
		Map<ObjetivosActividad, ArrayList<ItemPlan>> itemPlansPorObjetivo;
		Map<ObjetivosActividad, ArrayList<ItemPlan>> refuerzosPorObjetivo;
		//ArrayList<ItemPlan> refuerzos;
		ArrayList<ObjetivosActividad> objetivos;
		// Grafo de itemPlan
		//Graph<Integer> grafoActividades;
	
	/**
	 * Bean configurado para la aplicacion
	 * @see AshyiBean
	 */
	public AshyiBean bean;
	
	/**
	 * Constructor
	 */
	public Consulta() {
		this.appContext = new ClassPathXmlApplicationContext("co/edu/javeriana/ASHYI/persistence/components.xml");
		this.bean =  (AshyiBean)appContext.getBean("ashyibean");
	}
		
	/**
	 * @return Contexto de aplicacion
	 */
	public ApplicationContext getAppContext() {
		
		return appContext;
	}
	/**
	 * @return Bean configurado para la aplicacion
	 */
	public AshyiBean getBean() {
		
		return bean;
	}
	
	/**
	 * Obtener un arreglo con el nombre de los objetivos de una actvidad
	 * @param nombreActividad nombre de la actividad a consultar
	 * @param nR nivel de recursividad de la actividad a consultar
	 * @return arreglo con el nombre de los objetivos de una actvidad
	 */
	public String[] getNombresObjetivos(String nombreActividad, int nR)
	{
		String[] objs= bean.getObjetivosActividad(nR, nombreActividad);
		for (String obj : objs)
		{
			System.out.println("!!!!!!!!!!!!!!!!: "+obj);
		}
		
		return objs;
	}
	
	/**
	 * Obtener una lista de los objetivos de una actvidad
	 * @param nombreActividad nombre de la actividad a consultar
	 * @param nR nivel de recursividad de la actividad a consultar
	 * @return lista de los objetivos de una actvidad
	 */
	public List<ObjetivosActividad> getObjetivos(String nombreActividad, int nR)
	{		
		List<ObjetivosActividad> objs= bean.getObjetivosActividad(nombreActividad, nR);
		for (ObjetivosActividad obj : objs)
		{
			System.out.println("!!!!!!!!!!!!!!!!: "+obj.getIdObjetivo().getNombre());
		}
		
		return objs;
	}
	
	/**
	 * Consultar las actividades que cumplan con los objetivos dados
	 * @param objs lista de objetivos a consultar
	 * @return lista de actividades que cumplan con los objetivos dados
	 */
	public List<Actividad> getActividades(List<List> objs)
	{
		List<Actividad> actividades = bean.getActividadesPorObjetivos(objs);
		return actividades;
	}
	
//	public void closeSession()
//	{
//		//getBean().getAshyiToolDao().getSessionFactory().close();
//	}

	/**
	 * Consultar las caracteristicas del perfil de un usuario
	 * @param aliasAg id Usuario a consultar
	 * @return lista de caracteristicas del perfil de un usuario
	 */
	public List getCaracteristicasUsuario(String aliasAg) {
		List<CaracteristicasUsuario> caracteristicas = bean.getCaracteristicasUsuario(aliasAg);
		return caracteristicas;
	}

	/**
	 * Consultar las actividades de refuerzo
	 * @return lista con las actividades de refuerzo
	 */
	public List<Actividad> getActividadesRefuerzo() {
		List<Actividad> actividades = bean.getActividadesRefuerzo();
		return actividades;
	}
	
	/**
	 * Consultar las caracteristicas (pre o post) de una actividad
	 * @param a actividad a consultar
	 * @param tipo tipo de consulta (1 pre, 2 post)
	 * @param queConsulta  2 consultar caracteristicas de tipo
	 * @param correlacion false --> sin personalidad en el tipo o true --> con personalidad en el tipo
	 * @return lista de las caracteristicas (pre o post) de una actividad
	 */
	public List<Caracteristica> getCaracteristicasActividad(Actividad a, int tipo, int queConsulta, boolean correlacion) {
		List<Caracteristica> caracteristicasActividad = bean.getCaracteristicasActividad(a,tipo, queConsulta, correlacion);
		return caracteristicasActividad;
	}

	/**
	 * Consultar las caracteristicas completas del sistema
	 * @return lista de las caracteristicas completas del sistema
	 */
	public List<List<Caracteristica>> getCaracteristicas() {
		List<List<Caracteristica>> caracteristicasActividad = bean.getCaracteristicas();
		return caracteristicasActividad;
	}

	/**
	 * Consultar un itemPlan por su id
	 * @param idItem id a consultar
	 * @return itemPlan consultado
	 */
	public ItemPlan getItemPlan(int idItem) {
		return bean.getItemPlan(idItem);
	}

	/**
	 * Consultar las caracteristicas (pre o post) de una actividad
	 * @param actividad actividad a consultar
	 * @param tipoPrePost tipo de consulta (1 pre, 2 post)
	 * @return lista con caracteristicas (pre o post) de una actividad
	 */
	public List<Caracteristica> getPostPreActividad(Actividad actividad, int tipoPrePost) {
		List<Caracteristica> caracteristicasActividad = bean.getCaracteristicasPrePostActividad(actividad, tipoPrePost);
		return caracteristicasActividad;
	}
	
	/**
	 * Almacenar el grafo de un usuario ejecutor en la base de datos
	 * @param actividades lista de id de items que conforman el grafo
	 * @param idUsuario id de usuario asociado
	 * @param actividadRecursidad id actividad recursiva asociada al grafo
	 */
	public void saveGrafoUsuario(List actividades, String idUsuario, int actividadRecursidad, String origen, double nota,Integer idItemPlan) {
		Grafo grafo = new GrafoImpl();
		int tamLista = actividades.size();
		grafo.setIdItemPlan_Inicial((int) actividades.get(tamLista-1));
		
		bean.saveCaracteristica(grafo, true);
		
		int idGrafo = bean.getIdUltimoGrafo();
		grafo.setIdGrafo(idGrafo);
		
		//actualizar grafo de usuario
		//3 unidad didactica
		Usuario user = getUsuario(idUsuario);
				
		int idActividad = bean.getActividad(actividadRecursidad).getIdActividad();
		Date fechaActual=new Date();
		List<GrafosUsuario> grafos = getGrafoUsuario(user.getIdUsuario(), idActividad);
		GrafosUsuario grafoActivo = new GrafosUsuarioImpl();
		boolean existeG = false;
		//hay grafos re planificados, encontrar el activo
		if(grafos != null)
		{
			for(GrafosUsuario gU : grafos)
			{
				if(gU.isActivo())
				{
					existeG = true;
					grafoActivo = gU;
					break;
				}
			}
			
			//actualizar grafo de usuario
			grafoActivo.setActivo(false);
			grafoActivo.setOrigen(origen);
			grafoActivo.setFecha(new Date());
			bean.update(grafoActivo);			
		}
		GrafosUsuario gU = new GrafosUsuarioImpl(grafo, user, idActividad,fechaActual,true,origen);
		if(!existeG)
		{
			grafoActivo = gU;
		}
		bean.saveCaracteristica(gU, true);
		this.generarLog(user.getIdUsuario(), gU.getIdGrafo().getIdGrafo(), grafoActivo.getIdGrafo().getIdGrafo(), fechaActual,bean.getIdUsuarioInstructor(idActividad), origen,nota,idItemPlan);
		//actualizarGrafoEstudiante(idUsuario, grafo);
		
		if(tamLista == 1)
		{
			GrafoRelaciones relacion = new GrafoRelacionesImpl();
			relacion.setIdGrafo(grafo);
			relacion.setIdItemPlan_Origen((int)actividades.get(0));
			relacion.setOrden(1);
			bean.saveCaracteristica(relacion, true);
		}
		else
		{
			int orden = 1;
			for(int i = 0; i<tamLista-1; i++)
			{
				GrafoRelaciones relacion = new GrafoRelacionesImpl();
				relacion.setIdGrafo(grafo);
				relacion.setOrden(orden);
				relacion.setIdItemPlan_Origen((int)actividades.get(i));
				relacion.setIdItemPlan_Destino((int)actividades.get(i+1));
				
				bean.saveCaracteristica(relacion, true);
				orden++;
			}
		}
	}

	/**
	 * Crear y Almacenar los items usuario de acuerdo al grafo de actividades de un usaurio 
	 * @param actividades lista de id de items asociados al grafo
	 * @param idUsuario id de usuario asociado
	 */
	public void saveItemsUsuario(List actividades, String idUsuario) {
		
		int tamLista = actividades.size();
		
		if(tamLista == 1)
		{
			ItemsUsuario iU = new ItemsUsuarioImpl();
			iU.setIdItemPlan(getItemPlan((int)actividades.get(0)));
			iU.setIdUsuario(getUsuario(idUsuario));
			iU.setEstaActivo(true);
			if(!bean.estaItemUsuario(iU.getIdItemPlan(), iU.getIdUsuario()))
				bean.saveCaracteristica(iU, true);
		}	
		else
		{
			for(int i = 0; i<tamLista; i++)
			{
				ItemsUsuario iU = new ItemsUsuarioImpl();
				iU.setIdItemPlan(getItemPlan((int)actividades.get(i)));
				iU.setIdUsuario(getUsuario(idUsuario));
				iU.setEstaActivo(true);
				if(!bean.estaItemUsuario(iU.getIdItemPlan(), iU.getIdUsuario()))
					bean.saveCaracteristica(iU, true);
			}
		}
		
	}

	/**
	 * Consultar un usuario con base en su ID
	 * @param idUsuario
	 * @return usuario consultado
	 */
	public Usuario getUsuario(String idUsuario) {
		return bean.getUsuario(idUsuario);
	}

	/**
	 * Verificar si el contexto dado concuerda con las caracteristicas de una actividad asociada a un item plan
	 * @param aU id de item plan a consultar
	 * @param contextoLlega lista de caracteristicas contextuales
	 * @return si la actividad concuerda o no con el contexto
	 */
	public boolean isContextoActividad(int aU, List contextoLlega) {
		
		System.out.println("Buscando contexto");
		ItemPlan iP =  bean.getItemPlan(aU);
		
		int acceso = 0;
		int dispositivo = 0;
		
		if(iP.getIdRecurso() != null)
		{
			Recurso r = bean.getRecurso(iP.getIdRecurso().getIdRecurso());
			//System.out.println("Recurso de item: "+r.getIdRecurso());
			List<CaracteristicaRecurso> cR = bean.getCaracteristicasRecurso(r.getIdRecurso()); 
			//System.out.println("Caracteristicas R: "+cR.size());			
			
			String tipoAcceso = (String) contextoLlega.get(0);
			
			System.out.println("Acceso contexto "+tipoAcceso);
			
			for(int i = 0; i<cR.size(); i++)
			{
				if(tipoAcceso.equalsIgnoreCase("outside"))
				{
					Caracteristica c = getCaracteristica(cR.get(i).getIdCaracteristica().getIdCaracteristica()).get(0);
					if((c.getNombre().equalsIgnoreCase("Campus"))
							|| (c.getNombre().equalsIgnoreCase("Salon de clase")))
					{
						acceso++;
						break;
					}
				}
			}			
			
			if(((String) contextoLlega.get(1)).equalsIgnoreCase("mobile"))
			{
				if(r.getFormato().equalsIgnoreCase("GIF") || r.getFormato().equalsIgnoreCase("FLV")|| r.getFormato().equalsIgnoreCase("flash"))
				{
					dispositivo++;
				}
			}
		}
		
		if(acceso == 0 || dispositivo > 0)
			return false;
		else
			return true;
	}
	
	/**
	 * Consultar los items de un usuario relacionados a una actividad recursiva
	 * @param userId Id de usaurio asociado
	 * @param nombreActividad nombre de la actividad recursiva
	 * @return lista de los items de un usuario
	 */
	public List<ItemsUsuario> getItemsUsuario(String userId, String nombreActividad) {
		
		return bean.getItemsUsuario(userId, nombreActividad); 
	}

	/**
	 * Buscar actividades que concuerden con los objetivos y contexto dados
	 * @param itemUsuario id del itemplan a buscar
	 * @param itemsMain lista de items del grafo general
	 * @param contextoLlega lista de caracteristicas contextuales
	 * @return actividades que concuerden con los objetivos y contexto dados
	 */
	public List<Integer> objetivosCxtActividades(int itemUsuario, List itemsMain, List contextoLlega) {
		
		List<Integer> idsCambio = new ArrayList<Integer>();
		
		ItemPlan iP =  bean.getItemPlan(itemUsuario);
		
		Actividad ac = bean.getActividad(iP.getIdActividad().getIdActividad());
		List<ObjetivosActividad> objActUsuario = bean.getObjetivosActividad(ac.getNombre(), ac.getNivel_recursividad());
		
		for(int i = 0; i < itemsMain.size(); i++)
		{
			ItemPlan iPA =  bean.getItemPlan((int) itemsMain.get(i));
			
			if(iPA.getIdItemPlan() != iP.getIdItemPlan())
			{			
				Actividad acA = bean.getActividad(iPA.getIdActividad().getIdActividad());
				List<ObjetivosActividad> objAct = bean.getObjetivosActividad(acA.getNombre(), acA.getNivel_recursividad());
								
				//buscar por objetivo
				int objContenidos = 0;
				for(int j = 0; j < objActUsuario.size(); j++ )
				{
					for(int h = 0; h < objAct.size(); h++ )
					{
						if(objAct.get(h).getIdObjetivo().getIdObjetivo() == objActUsuario.get(j).getIdObjetivo().getIdObjetivo())
						{							
							objContenidos++;
							break;
						}
					}
				}
				//si concuerda en objetivos
				if(objContenidos == objActUsuario.size())
				{
					//buscar por contexto
					if(isContextoActividad((int) itemsMain.get(i),contextoLlega))
	        		 {
						idsCambio.add((int) itemsMain.get(i));
	        		 }
				}
			}
		}
				
		return idsCambio;
	}
	
//	public void con(String n, int nr)
//	{
//		bean.getObjetivosActividad(nr, n);
//	}
	
	/**
	 * Obtener una caracteristica por si ID
	 * @param id de la caracteristica
	 * @return
	 */
	public List<Caracteristica> getCaracteristica(int id) {
		return bean.getCaracteristica(id);		
	}

	/**
	 * ALamcenar en la base de datos un objeto
	 * @param o objeto a almacenar
	 */
	public void saveObject(Object o) {
		bean.saveCarateristica(o);
	}

	/**
	 * Consultar las caracteristicas asociadoas a un item de un usuario en particular
	 * @param iU item de usaurio a consultar
	 * @return lista de las caracteristicas asociadoas a un item de un usuario
	 */
	public List<CaracteristicasItemsUsuario> getCaracteristicasItemsUsuario(ItemsUsuario iU) {
		
		return bean.getCaracteristicasItemUsuario(iU);
	}

	/**
	 * Consultar un item usuario con base en el id de item plan e id de usuario
	 * @param idTP id de item plan
	 * @param idUsuario id de usuario
	 * @return item usuario consultado
	 */
	public ItemsUsuario getItemUsuario(int idTP, int idUsuario) {
		
		return bean.getItemUsuario(idTP, idUsuario);
	}

	/**
	 * Eliminar un objeto de la base de datos
	 * @param o objeto a eliminar
	 */
	public void deleteObject(Object o) {
		bean.deleteObject(o);		
	}

	/**
	 * Consultar la actividad inicial del plan
	 * @return actividad inicial del plan
	 */
	public Actividad getActividadInicial() {
		
		return bean.getActividadInicial();
	}

	/**
	 * Consultar los objetivos asociados a una actividad
	 * @param nombre nombre de la actividad a consultar
	 * @param nivel_recursividad nivel de recursividad de la actividad a consultar
	 * @return objetivos asociados a una actividad
	 */
	public List<ObjetivosActividad> getObjetivosActividad(String nombre, int nivel_recursividad) {
		
		return bean.getObjetivosActividad(nombre, nivel_recursividad);
	}

	/**
	 * Cambiar las actividades a ejecutar por un usuario
	 * @param actividades lista de actividades nuevas
	 * @param idUsuario idString de usuario
	 * @param idActividad id actividad asociada
	 */
	public void cambiarItemsUsuario(List actividades, String idUsuario, int idActividad) {	
		
		bean.cambiarItemsUsuario(actividades, idUsuario, idActividad);
	}

	/**
	 * Consultar una actividad por su ID
	 * @param idActividad id activida a consultar
	 * @return actividad consultada
	 */
	public Actividad getActividad(Integer idActividad) {
		return bean.getActividad(idActividad);
	}

	/**
	 * Consultar el grafo de un usuario relacionado a una actividad recursiva
	 * @param usuario usuario a consultar
	 * @param idActividad id de actividad recursiva
	 * @return grafo de un usuario relacionado a una actividad recursiva
	 */
	public Grafo getGrafoUsuario( Usuario usuario, int idActividad) {
		
		return bean.getGrafoUsuario(usuario, idActividad);
	}

	/**
	 * Listar las relaciones de un grafo
	 * @param idGrafo id del grafo a consultar
	 * @return Lista de las relaciones de un grafo
	 */
	public List<GrafoRelaciones> getGrafoRelaciones(int idGrafo) {
		
		return bean.getGrafoRelaciones(idGrafo);
	}

	/**
	 * Consultar los items de usuario relacionados a una actividad recursiva
	 * @param idUsuario IDString de usuario
	 * @param idActividad id de una actividad recursiva
	 * @return los items de usuario relacionados a una actividad recursiva
	 */
	public List getItemsPlanUsuarioActividad(String idUsuario, Integer idActividad) {
		
		List idItems = new ArrayList<>();
		List<ItemsUsuario> items = bean.getItemsPlanUsuarioActividad(idUsuario, idActividad);
		
		for(int i = 0; i < items.size(); i++)
			idItems.add(items.get(i).getIdItemPlan().getIdItemPlan());
		
		return idItems;
	}

	/**
	 * Consultar el usuario planificador de una actividad recursiva
	 * @param idActividad id de la actividad recursiva
	 * @return usuario planificador de la actividad recursiva
	 */
	public int getIdUsuarioInstructor(Integer idActividad) {
		
		return bean.getIdUsuarioInstructor(idActividad);
	}

	/**
	 * Consultar el grafo de un usuario asociado a una actividad recursiva
	 * @param idUsuario id del usaurio a consultar
	 * @param idActividad id de la actividad recursiva
	 * @return Grafos encontrados
	 */
	public List<GrafosUsuario> getGrafoUsuario(int idUsuario, Integer idActividad) {
		return bean.getGrafoUsuario(idUsuario, idActividad);
	}

	/**
	 * Consultar objetivo por su ID
	 * @param idObjetivo
	 * @return objetivo consultado
	 */
	public Objetivo getObjetivoPorId(Integer idObjetivo) {
		
		return bean.getObjetivoPorId(idObjetivo);
	}

	/**
	 * Consultar los objetivos asociados a una actividad
	 * @param idActividad id de la actividad a consultar
	 * @return objetivos asociados a una actividad
	 */
	public List<ObjetivosActividad> getObjetivosActividad(Integer idActividad) {
		return bean.getObjetivosActividad(idActividad);
	}
	
	/**
	 * Consultar los objetivos de una actividad
	 * @param nR nivel de recursividad de la actividad
	 * @param nombreA nombre de la actividad
	 * @return lista de los objetivos encontrados
	 */
	public List<List> getIdsObjetivosActividad(int nR, String nombreA) {

		return bean.getIdsObjetivosActividad(nR,nombreA);
	}

	/**
	 * Consulta un item plan segun la actividad y recurso relacionado
	 * @param ud actividad recursiva asociada
	 * @param auxActividad actividad atomica relacionada
	 * @param auxRecurso recurso asociado
	 * @param tipo tipo de recurso 1- con 2 - sin
	 * @param orden orden del item plan
	 * @return item plan consultado
	 */
	public ItemPlan getItemPlan(Actividad ud, Actividad auxActividad, Recurso auxRecurso, int tipo, int orden) {
		return bean.getItemPlan(ud, auxActividad, auxRecurso,tipo, orden);
	}
	
	/**
	 * Consultar los items de sakai
	 * @return items de sakai
	 */
	public List<String> getItems()
	{
		return bean.getItems();
	}
	
	public List<RecursosActividad> actividadesRecurso(List items, Actividad actividad)
	{
		return bean.actividadesRecurso(items, actividad);
	}
	
public void generarLog(int idUsuario, int idGrafoNuevo,int idGrafoAnterior,Date fecha, int idProfesor, String evento , double nota,Integer idItemPlan) {
		
		try {
			int tipoEvento=-1;
			if(evento.equals("mismo grafo")){
				tipoEvento=0;
			}else if(evento.equals("replanificacion")){
				tipoEvento=1;
			}else if(evento.equals("origen")){
				tipoEvento=2;
			}else if(evento.equals("contexto")){
				tipoEvento=3;
			}
			
			FileWriter archivo;
			//archivo = new FileWriter("/opt/archivoLog.txt",true);
			String sSistemaOperativo = System.getProperty("os.name");
			//System.out.println(sSistemaOperativo);
			String nombreArchivo = "AshyiLog.txt";
			if(sSistemaOperativo.contains("Windows"))
				archivo=new FileWriter("C:\\Apache\\apache-tomcat-7.0.42\\webapps\\archivosASHYI\\"+nombreArchivo,true);//Aqui le dan el nombre y/o con la ruta del archivo salida
			else
				archivo=new FileWriter("/opt/tomcat/webapps/archivosASHYI/"+nombreArchivo,true);//Aqui le dan el nombre y/o con la ruta del archivo salida
			int mes=fecha.getMonth()+1;
			int anio=fecha.getYear()+1900;
			//en el archivo se escribe de la siguiente manera: id del estudiante, item que se le califica, id grafo por el que se cambio, id grafo antiguo, id profesor, tipo evento (0 si no cambio el grafo, 1 si se replanifico el grafo, 2 si apenas se genero un grafo, año que se genero cambio, mes que se genero cambio, dia que se genero cambio, hora, minutos, segundos, nota de la actividad
			if(tipoEvento==2 ||tipoEvento==3){
				LogGrafos logG=new LogGrafosImpl(idUsuario, 0, idGrafoNuevo, idGrafoAnterior, idProfesor, tipoEvento, 0, fecha);
				bean.saveCaracteristica(logG, true);
				archivo.write(idUsuario+";"+" "+";"+idGrafoNuevo+";"+idGrafoAnterior+";"+idProfesor+";"+tipoEvento+";"+anio+";"+mes+";"+fecha.getDate()+";"+fecha.getHours()+";"+fecha.getMinutes()+";"+fecha.getSeconds()+";"+" "+"\n");
			}else{
				LogGrafos logG=new LogGrafosImpl(idUsuario, idItemPlan, idGrafoNuevo, idGrafoAnterior, idProfesor, tipoEvento, nota, fecha);
				bean.saveCaracteristica(logG, true);//TODO agregar al saveCaracteristica las nuevas tablas que se incluyeron (logCaracteristicas) y (logGrafos)
				archivo.write(idUsuario+";"+idItemPlan+";"+idGrafoNuevo+";"+idGrafoAnterior+";"+idProfesor+";"+tipoEvento+";"+anio+";"+mes+";"+fecha.getDate()+";"+fecha.getHours()+";"+fecha.getMinutes()+";"+fecha.getSeconds()+";"+nota+"\n");
			}
			archivo.flush();
			archivo.close();
			System.out.println("se genero archivo");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error generación archivo");
		}
		
	}
	
	public void generarLogSinCambios(String idUsuario, String evento, int actividadRecursividad,double nota,Integer idItemPlan) {
		try {
			int tipoEvento=3;
			if(evento.equals("mismo grafo")){
				tipoEvento=0;
			}else if(evento.equals("replanificacion")){
				tipoEvento=1;
			}else if(evento.equals("origen")){
				tipoEvento=2;
			}else if(evento.equals("contexto")){
				tipoEvento=3;
			}
			
			FileWriter archivo;
			Usuario u=bean.getUsuario(idUsuario);
			int idActividad = getActividad(actividadRecursividad).getIdActividad();
			List<GrafosUsuario> grafos = getGrafoUsuario(u.getIdUsuario(), idActividad);
			GrafosUsuario grafoActivo = new GrafosUsuarioImpl();
			boolean existeG = false;
			//hay grafos re planificados, encontrar el activo
			if(grafos != null)
			{
				for(GrafosUsuario gU : grafos)
				{
					if(gU.isActivo())
					{
						existeG = true;
						grafoActivo = gU;
						break;
					}
				}		
			}
			grafoActivo.setOrigen(evento);
			grafoActivo.setFecha(new Date());
			bean.update(grafoActivo);
			
			String nombreArchivo = "AshyiLog.txt";
			String sSistemaOperativo = System.getProperty("os.name");
			if(sSistemaOperativo.contains("Windows"))
				archivo=new FileWriter("C:\\Apache\\apache-tomcat-7.0.42\\webapps\\archivosASHYI\\"+nombreArchivo,true);//Aqui le dan el nombre y/o con la ruta del archivo salida
			else
				archivo=new FileWriter("/opt/tomcat/webapps/archivosASHYI/"+nombreArchivo,true);//Aqui le dan el nombre y/o con la ruta del archivo salida
			
			Date date=new Date();
			int mes=date.getMonth()+1;
			int anio=date.getYear()+1900;// El método getYear obtiene el año y le resta 1900, por lo que para obtener el año real se le suman los 1900
			//en el archivo se escribe de la siguiente manera: id del estudiante, item que se le califica, id grafo por el que se cambio, id grafo antiguo, id profesor, tipo evento (0 si no cambio el grafo, 1 si se replanifico el grafo, 2 si apenas se genero un grafo, año que se genero cambio, mes que se genero cambio, dia que se genero cambio, hora, minutos, segundos, nota de la actividad
			if(tipoEvento==2 ||tipoEvento==3){
				LogGrafos logG=new LogGrafosImpl(u.getIdUsuario(), 0, grafoActivo.getIdGrafo().getIdGrafo(), grafoActivo.getIdGrafo().getIdGrafo(), bean.getIdUsuarioInstructor(idActividad), tipoEvento, 0, date);
				bean.saveCaracteristica(logG, true);
			archivo.write(u.getIdUsuario()+";"+" "+";"+grafoActivo.getIdGrafo().getIdGrafo()+";"+grafoActivo.getIdGrafo().getIdGrafo()+";"+bean.getIdUsuarioInstructor(idActividad)+";"+tipoEvento+";"+anio+";"+mes+";"+date.getDate()+";"+date.getHours()+";"+date.getMinutes()+";"+date.getSeconds()+";"+" "+"\n");
			}else{
				LogGrafos logG=new LogGrafosImpl(u.getIdUsuario(), idItemPlan, grafoActivo.getIdGrafo().getIdGrafo(), grafoActivo.getIdGrafo().getIdGrafo(), bean.getIdUsuarioInstructor(idActividad), tipoEvento, nota, date);
				bean.saveCaracteristica(logG, true);
				archivo.write(u.getIdUsuario()+";"+idItemPlan+";"+grafoActivo.getIdGrafo().getIdGrafo()+";"+grafoActivo.getIdGrafo().getIdGrafo()+";"+bean.getIdUsuarioInstructor(idActividad)+";"+tipoEvento+";"+anio+";"+mes+";"+date.getDate()+";"+date.getHours()+";"+date.getMinutes()+";"+date.getSeconds()+";"+nota+"\n");
			}
			archivo.flush();
			archivo.close();
			System.out.println("se genero archivo");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error generación archivo");
		}
		
	}

	public void generarLogCaracteristicas(Integer idUsuario,
			int caracteristica, String evento,Integer idItemPlan, Date date) {
		// TODO Auto-generated method stub
		try {
			int tipoEvento=4;
			if(evento.equals("Caracteristica actual")){
				tipoEvento=0;
			}else if(evento.equals("Competencia/habilidad obtenida. ya la tenía")){
				tipoEvento=1;
			}else if(evento.equals("Competencia/habilidad obtenida. no la tenía, la obtuvo")){
				tipoEvento=2;
			}else if(evento.equals("Competencia/habilidad no obtenida. La tenía, no la obtuvo")){
				tipoEvento=3;
			}
			int mes=date.getMonth()+1;
			int anio=date.getYear()+1900;
			FileWriter archivo;
			String nombreArchivo;
			
			int caracteristicasActuales;
			if (tipoEvento==0){
			  caracteristicasActuales=0;
			}else{
				caracteristicasActuales=1;
			}
			
			 RespuestaItemsUsuario r= bean.getRespuestasItemsUsuario(idUsuario,idItemPlan);
		if (r!=null){
			LogCaracteristicas car=new LogCaracteristicasImpl(idUsuario, idItemPlan, tipoEvento, caracteristica, date, r.getFecha(), caracteristicasActuales);
			bean.saveCaracteristica(car, true);
		}else{
			LogCaracteristicas car=new LogCaracteristicasImpl(idUsuario, idItemPlan, tipoEvento, caracteristica, date, new Date(), caracteristicasActuales);
			bean.saveCaracteristica(car, true);
		}
			
			 nombreArchivo = "caracteristicasLog.txt";
			String sSistemaOperativo = System.getProperty("os.name");
			if(sSistemaOperativo.contains("Windows"))
				archivo=new FileWriter("C:\\Apache\\apache-tomcat-7.0.42\\webapps\\archivosASHYI\\"+nombreArchivo,true);//Aqui le dan el nombre y/o con la ruta del archivo salida
			else
				archivo=new FileWriter("/opt/tomcat/webapps/archivosASHYI/"+nombreArchivo,true);//Aqui le dan el nombre y/o con la ruta del archivo salida
				
			archivo.write(idUsuario+";"+idItemPlan+";"+tipoEvento+";"+caracteristica+";"+anio+";"+mes+";"+date.getDate()+";"+date.getHours()+";"+date.getMinutes()+";"+date.getSeconds()+"\n");
			
			archivo.flush();
			archivo.close();
			System.out.println("se genero archivo de caracteristicas");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error al generar archivo de caracteristicas");
		}
	}
	
	public List<ItemPlan> obtenerFechasItemPlan(){
		return bean.obtenerFechasItemPlan();
	}
	
	public List<Integer> obtenerItemsUsuario(int idItemPlan){
		return bean.obtenerItemsUsuario(idItemPlan);
	}
	
	public String getMail(String usuario){
		return bean.getMail(usuario);
	}

	public String getUsuarioSakai(Integer usu) {
		return bean.getUsuarioSakai(usu);
	}
	
	/**
	 * Consultar una actividad por nombre y nivel de recursividad.
	 * @param nombre
	 * @param nivelRecursividad
	 * @return Actividad la actividad encontrada.
	 */
	public Actividad getActividad(String nombre, Integer nivelRecursividad) {
		return bean.getActividad(nombre, nivelRecursividad);
	}
	
	/**
	 * Retorna si un item est&aacute.
	 * @param ud
	 * @param auxActividad
	 * @param auxRecurso
	 * @param tipo
	 * @param isRefuerzo
	 * @param orden
	 * @return true si est&aacute, false si no.
	 */
	public boolean itemEsta(Actividad ud, Actividad auxActividad, Recurso auxRecurso, int tipo, boolean isRefuerzo, int orden) {
		return bean.itemEsta(ud, auxActividad, auxRecurso, tipo, isRefuerzo, orden);
	}
	
	/**
	 * Agrega un itemPlan.
	 * @param ud
	 * @param ac
	 * @param recurso
	 * @param estaActivo
	 * @param orden
	 * @return resultado de la operaci&oacuten.
	 */
	public ItemPlan addItemPlan(Actividad ud, Actividad ac, Recurso recurso,
			boolean estaActivo, int orden) {
		return bean.addItemPlan(ud, ac, recurso, estaActivo, orden);
	}
	
	/**
	 * Agrega un itemPlan.
	 * @param ud
	 * @param ac
	 * @param estaActivo
	 * @param orden
	 * @return ip el itemPlan
	 */
	public ItemPlan addItemPlan(Actividad ud, Actividad ac, boolean estaActivo, int orden) {
		return bean.addItemPlan(ud, ac, estaActivo, orden);
	}

	/**
	 * Retorna el &uacuteltimo itemPlan creado.
	 * @return el itemPlan.
	 */
	public int getUltimoItemPlan() {
		return bean.getUltimoItemPlan();
	}
}
