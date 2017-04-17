package co.edu.javeriana.ASHYI.logic;

/**
 ***********************************************************************************
 *Bean general de ASHYI como puente de comunicacion
 *@author ASHYI
 *
 **********************************************************************************/

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import co.edu.javeriana.ASHYI.hbm.GrafoImpl;
import co.edu.javeriana.ASHYI.hbm.GrafoRelacionesImpl;
import co.edu.javeriana.ASHYI.hbm.GrafosUsuarioImpl;
import co.edu.javeriana.ASHYI.hbm.LogCaracteristicasImpl;
import co.edu.javeriana.ASHYI.hbm.LogGrafosImpl;
import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.AshyiToolDao;
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

public class AshyiBean {

		
	/**
	 * Listado de objetivos
	 */
	private String[] objetivos;
	
	/**
	 * Nivel de actividades atomicas
	 */
	private String nivelActividades = "Universitario";
		
	/**agente interface*/
	private String agenteInterface = null;
	
	/**agente comunicacion*/
	private String agenteComunicacion = null;
	
	/**chaea*/
	int activo;
	int prag;
	int reflexivo;
	int teorico;	
	
	/**
	 * Obtener el alias del agente de comunicacion
	 * @return el alias del agente de comunicacion (puente de comunicacion entre el sistema y el SMA)
	 * */
	public String getAgenteComunicacion() {
		return agenteComunicacion;
	}

	/**
	 * Cambiar el alias del agente de comunicacion
	 * @param el alias del agente de comunicacion (puente de comunicacion entre el sistema y el SMA)
	 * */
	public void setAgenteComunicacion(String agenteComunicacion) {
		this.agenteComunicacion = agenteComunicacion;
	}	

	/**
	 * Obtener el nivel manejado en el sistema para las actividades atomicas
	 * @return nivel de actividades seleciconadas
	 */
	public String getNivelActividades() {
		return nivelActividades;
	}

	/**
	 * Cambiar el nivel manejado en el sistema para las actividades atomicas
	 * @param nivelActividades nivel de actividades seleciconadas
	 */
	public void setNivelActividades(String nivelActividades) {
		this.nivelActividades = nivelActividades;
	}

	/**
	 * Obtener el alias del agente interface
	 * @return alias de agente interface
	 */
	public String getAgenteInterface() {
		return agenteInterface;
	}

	/**
	 * Cambiar el alias del agente interface
	 * @param agenteInterface alias de agente interface
	 */
	public void setAgenteInterface(String agenteInterface) {
		this.agenteInterface = agenteInterface;
	}	
	/**
	 * @return arreglo de nombres de objetivos
	 */
	public String[] getObjetivos() {
		return objetivos;
	}
	/**
	 * @param objetivos arreglo de nombres de objetivos
	 */
	public void setObjetivos(String[] objetivos) {
		this.objetivos = objetivos;
	}
	
	/**
	 * Data Access Object ASHYI
	 */
	private AshyiToolDao ashyiToolDao;

	// End Injection

	/**
	 * @return Data Access Object ASHYI
	 */
	public AshyiToolDao getAshyiToolDao() {
		return ashyiToolDao;
	}

	/**
	 * @param ashyiToolDao Data Access Object ASHYI
	 */
	public void setAshyiToolDao(AshyiToolDao ashyiToolDao) {
		this.ashyiToolDao = ashyiToolDao;
	}
		
	/**
	 * Almacenar un objeto en la base de datos
	 * @param i objeto a almacenar
	 * @param requiresEditPermission permiso para almacenar
	 * @return almacenamiento exitoso o no
	 */
	public boolean saveCaracteristica(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();

		try {
			ashyiToolDao.saveCaracteristica(i,  elist, "Error", requiresEditPermission);
		} catch (Throwable t) {	
			// this is probably a bogus error, but find its root cause
			while (t.getCause() != null) {
				t = t.getCause();
			}
			err = t.toString();
		}

		// if we got an error from saveItem use it instead
		if (elist.size() > 0)
			err = elist.get(0);
		if (err != null) {
			//etErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}

	/**
	 * Almacenar un objeto en la base de datos
	 * @param i objeto a almacenar
	 * @return almacenamiento exitoso o no
	 */
	public boolean saveCarateristica(Object i) {
		return saveCaracteristica(i, true);
	}

	/**
	 * Obtener un arreglo con el nombre de los objetivos de una actvidad
	 * @param nombreActividad nombre de la actividad a consultar
	 * @param nR nivel de recursividad de la actividad a consultar
	 * @return arreglo con el nombre de los objetivos de una actvidad
	 */
	public String[] getObjetivosActividad(int nivelRecursividad, String nombre)
	{
		this.setObjetivos(ashyiToolDao.getObjetivosActividad(nivelRecursividad, nombre));
		
		for(String obj : this.getObjetivos())
			System.out.println("obj: "+obj);

		return this.getObjetivos();
	}
	
	/**
	 * Consultar los objetivos asociados a una actividad
	 * @param nombre nombre de la actividad a consultar
	 * @param nivel_recursividad nivel de recursividad de la actividad a consultar
	 * @return objetivos asociados a una actividad
	 */
	public List<ObjetivosActividad> getObjetivosActividad(String nombreAct, int nR)
	{
		Actividad unidad = ashyiToolDao.getactividad(nombreAct,nR);
		List<ObjetivosActividad> objUnidad = ashyiToolDao.getObjetivosActividad(unidad.getIdActividad());
		
		return objUnidad;
	}
	
	/**
	 * Consultar los recursos de una actividad
	 * @param items lista de items de los recursos
	 * @param actividad actividad a consultar
	 * @return lista de los recursos de una actividad
	 */
	public List<RecursosActividad> actividadesRecurso(List<String> items, Actividad actividad)
	{
		List<RecursosActividad> actividadesRecursos = new ArrayList<RecursosActividad>();
		for(String item : items)
		{					
			RecursosActividad aAgregar = ashyiToolDao.getActividadRecursos(actividad.getIdActividad(), String.valueOf(item));
			if(!aAgregar.getIdActividad().getNombre().equals("Does not exist"))
			{
				actividadesRecursos.add(aAgregar);
			}
		}

		return actividadesRecursos;
	}
	
//	public boolean executeAction(String myAlias, String alias, String tipo, Object datos)
//	{
//		if(bean.executeAction(myAlias, alias, tipo, datos))
//			return true;
//		return false;
//		
//	}
//	
//	public StateBESA getEstadoAgente(String alias)
//	{
//		StateBESA estado = bean.getEstadoAgente(alias);
//		
//		return estado;
//	}
	
	/**
	 * Consultar las actividades que cumplen con los objetivos dados
	 * @param objs lista de objetivos a consultar
	 * @return las actividades que cumplen con los objetivos dados
	 */
	public List<Actividad> getActividadesPorObjetivos(List<List> objs)
	{
		List<Actividad> actividades = new ArrayList<Actividad>();
		List<ObjetivosActividad> objActividades = new ArrayList<ObjetivosActividad>();
		
		for(int i = 0;i < objs.size() ; i++)
		{
			objActividades.add(ashyiToolDao.getObjetivoActividadObj((int)objs.get(i).get(0), (int)objs.get(i).get(1)));
		}
		
		for(int i = 0; i < objActividades.size(); i++)
		{
			Objetivo obj = ashyiToolDao.getObjetivo(objActividades.get(i).getIdObjetivo().getIdObjetivo());
			Actividad act = ashyiToolDao.getActividad(objActividades.get(i).getIdActividad().getIdActividad());
			
			//objetivos de la unidad, ordenados
			List<ObjetivosActividad> objActividad = ashyiToolDao.getObjetivosActividadObj(obj.getIdObjetivo());
			for(int j = 0; j<objActividad.size(); j++)
			{
				//q no sea la misma unidad y q sean actividades atomicas
				if(objActividad.get(j).getIdActividad().getIdActividad() != act.getIdActividad() && objActividad.get(j).getIdActividad().getNivel_recursividad() == 4)
				{
					actividades.add(ashyiToolDao.getActividad(objActividad.get(j).getIdActividad().getIdActividad()));
				}
			}
		}
		return actividades;
	}

	/**
	 * Consultar las caracteristicas del perfil de un usuario
	 * @param aliasAg id Usuario a consultar
	 * @return lista de caracteristicas del perfil de un usuario
	 */
	public List<CaracteristicasUsuario> getCaracteristicasUsuario(String userId) {
		
		return ashyiToolDao.getCaracteristicasUsuario(userId);		
	}

	/**
	 * Consultar las actividades de refuerzo
	 * @return lista con las actividades de refuerzo
	 */
	public List<Actividad> getActividadesRefuerzo() {
		List<Actividad> actividades = new ArrayList<Actividad>();
		
		//actividades atomicas (4) de refuerzo 
		actividades = ashyiToolDao.getActividadRefuerzo(4);
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
		List<Caracteristica> caracteristicasActividad = new ArrayList<Caracteristica>();
		if(!a.getNivel().equals("InicioUD"))
			caracteristicasActividad = ashyiToolDao.getCaracteristicasActividadDistancia(a.getIdActividad(), tipo, queConsulta, correlacion);
		return caracteristicasActividad;
	}

	/**
	 * Consultar las caracteristicas completas del sistema
	 * @return lista de las caracteristicas completas del sistema
	 */
	public List<List<Caracteristica>> getCaracteristicas() {
		List<List<Caracteristica>> caracteristicas = new ArrayList<List<Caracteristica>>();
		List<Caracteristica> estilos = ashyiToolDao.getCaracteristicasPorTipo(4);
		List<Caracteristica> personalidad = ashyiToolDao.getCaracteristicasPorTipo(7);
		List<Caracteristica> habilidades = ashyiToolDao.getCaracteristicasPorTipo(5);
		List<Caracteristica> competencias = ashyiToolDao.getCaracteristicasPorTipo(6);
		
		caracteristicas.add(estilos);
		caracteristicas.add(personalidad);
		caracteristicas.add(habilidades);
		caracteristicas.add(competencias);
		
		return caracteristicas;
	}

	/**
	 * Consultar un itemPlan por su id
	 * @param idItem id a consultar
	 * @return itemPlan consultado
	 */
	public ItemPlan getItemPlan(int idItem) {		
		return ashyiToolDao.getItemPlan(idItem);
	}

	/**
	 * Consultar las caracteristicas (pre o post) de una actividad
	 * @param actividad actividad a consultar
	 * @param tipoPrePost tipo de consulta (1 pre, 2 post)
	 * @return lista con caracteristicas (pre o post) de una actividad
	 */
	public List<Caracteristica> getCaracteristicasPrePostActividad(Actividad actividad, int tipoPrePost) {
		return ashyiToolDao.getCaracteristicasPrePostActividad(actividad, tipoPrePost);
	}

	/**
	 * Consultar el ultimo grafo ingresado en la base de datos
	 * @return id del ultimo grafo ingresado en la base de datos
	 */
	public int getIdUltimoGrafo() {
		Grafo f = ashyiToolDao.getUltimoGrafo();
		return f.getIdGrafo();
	}

	/**
	 * Consultar un usuario con base en su ID
	 * @param idUsuario id usuario a consultar
	 * @return usuario consultado
	 */
	public Usuario getUsuario(String idUsuario) {
		int id = ashyiToolDao.getUltimoUsuario(idUsuario);
		return ashyiToolDao.getUsuario(id);
	}

	/**
	 * Consultar una actividad por su nombre y nivel de recursividad
	 * @param actividadRecursidad nombre de la actividad a consultar
	 * @param i nivel de recursividad de la actividad a consultar
	 * @return actividad consultada
	 */
	public Actividad getActividad(String actividadRecursidad, int i) {
		
		return ashyiToolDao.getactividad(actividadRecursidad, i);
	}
	
	/**
	 * Consultar una actividad por su ID
	 * @param idActividad id activida a consultar
	 * @return actividad consultada
	 */
	public Actividad getActividad(int idActividad) {
		
		return ashyiToolDao.getActividad(idActividad);
	}

	/**
	 * Consultar los items de usuario asocaidos a una actividad recursiva
	 * @param userId idString usaurio asociado
	 * @param nombreActividad nombre de la actividad recursiva
	 * @return lista de items de usuario consultados
	 */
	public List<ItemsUsuario> getItemsUsuario(String userId, String nombreActividad) {
		
		List<ItemsUsuario> itemsU = new ArrayList<ItemsUsuario>();
		List<ItemPlan> items = ashyiToolDao.getItemsActivos(getUsuario(userId), nombreActividad);
		
		for(int i = 0; i< items.size(); i++)
		{
			itemsU.add(ashyiToolDao.getItemsUsuario(items.get(i), userId));
		}
		return itemsU;
	}

	/**
	 * Consultar un item usuario por sus id relacionados
	 * @param iP item plan asociado
	 * @param idU idString usaurio asociado
	 * @return item de usuario consultado
	 */
	public ItemsUsuario getItemUsuario(ItemPlan iP, String idU) {
		return ashyiToolDao.getItemsUsuario(iP, idU);
	}

	/**
	 * Consultar un rescurso por si ID
	 * @param idRecurso id del recurso a consultar
	 * @return recurso consultado
	 */
	public Recurso getRecurso(int idRecurso) {		
		return ashyiToolDao.getRecurso(idRecurso);
	}

	/**
	 * Consultar las caracteristicas de un recurso en particular
	 * @param idRecurso id del recurso a consultar
	 * @return lista de las caracteristicas del recurso
	 */
	public List<CaracteristicaRecurso> getCaracteristicasRecurso(int idRecurso) {
		return ashyiToolDao.getCaracteristicasRecurso(idRecurso);
	}

	/**
	 * Obtener una caracteristica por si ID
	 * @param id de la caracteristica
	 * @return
	 */
	public List<Caracteristica> getCaracteristica(int id) {
		
		return ashyiToolDao.getCaracteristica(id);
	}

	/**
	 * Consultar las caracteristicas asociadoas a un item de un usuario en particular
	 * @param iU item de usaurio a consultar
	 * @return lista de las caracteristicas asociadoas a un item de un usuario
	 */
	public List<CaracteristicasItemsUsuario> getCaracteristicasItemUsuario( ItemsUsuario iU) {
		return ashyiToolDao.getCaracteristicasItemUsuario(iU);
	}

	/**
	 * Consultar un item usuario con base en el id de item plan e id de usuario
	 * @param idTP id de item plan
	 * @param idUsuario id de usuario
	 * @return item usuario consultado
	 */
	public ItemsUsuario getItemUsuario(int idTP, int idUsuario) {
		
		return ashyiToolDao.getItemsUsuario(ashyiToolDao.getItemPlan(idTP), ashyiToolDao.getUsuario(idUsuario).getIdUsuarioSakai());
	}

	/**
	 * Eliminar un objeto de la base de datos
	 * @param o objeto a eliminar
	 */
	public void deleteObject(Object o) {
		ashyiToolDao.deleteObject(o);
	}

	/**
	 * Consultar la actividad inicial del plan
	 * @return actividad inicial del plan
	 */
	public Actividad getActividadInicial() {
		
		return ashyiToolDao.getActividadInicial();
	}

	/**
	 * Cambiar las actividades a ejecutar por un usuario
	 * @param actividades lista de actividades nuevas
	 * @param idUsuario idString de usuario
	 * @param idActividad id actividad asociada
	 */
	public void cambiarItemsUsuario(List actividades, String idUsuario, int idActividad) {
		
		System.out.println("Cambiando actividades: "+actividades.size());
		
		Usuario user = getUsuario(idUsuario);
		
		List<ItemsUsuario> listUI = ashyiToolDao.getItemsUsuario(idUsuario, idActividad);
		
		Actividad ac = ashyiToolDao.getActividad(idActividad);
				
		Grafo grafoUsuario = ashyiToolDao.getGrafoUsuarioActividad(user.getIdUsuario(), ac.getNombre());
		
		Grafo grafoUsuarioNuevo = grafoUsuario;
		
		List<GrafoRelaciones> listaRelaciones = ashyiToolDao.getRelacionesGrafo(grafoUsuario.getIdGrafo());
		
		System.out.println("Cambiando items: "+listUI.size()+" con act: "+ac.getIdActividad()+" grafo: "+grafoUsuario.getIdItemPlan_Inicial()+ " relaciones: "+listaRelaciones.size());
				
		int index = -1;
		for(int i = 0 ; i < listUI.size(); i++)
		{
			//han cambiado			
			if(!actividades.contains(String.valueOf(listUI.get(i).getIdItemPlan().getIdItemPlan())))
			{		
				//System.out.println("a cambiar");
				index++;
				
				if(index < actividades.size())
				{
					if((Integer)actividades.get(index) != 0)
					{
						int idCambiar = listUI.get(i).getIdItemPlan().getIdItemPlan();
						
						int idCambioNuevo = 0;
						if (actividades.get(index) instanceof Integer)
							idCambioNuevo = (int) actividades.get(index);
						if (actividades.get(index) instanceof String)
							idCambioNuevo = Integer.valueOf((String) actividades.get(index));
							
						//si es el item inicial
						if(grafoUsuario.getIdItemPlan_Inicial() == idCambiar)
						{
							//grafoUsuario.setIdItemPlan_Inicial(idCambioNuevo);
							//cambiar en el grafo nuevo
							grafoUsuarioNuevo.setIdItemPlan_Inicial(idCambioNuevo);
							//actualizar grafo
							//ashyiToolDao.update(grafoUsuario, new ArrayList<String>(), "", true);
						}
						
						//cambiar grafos relaciones
						
//						for(int j = 0; j < listaRelaciones.size(); j++)
//						{
//							if(listaRelaciones.get(j).getIdItemPlan_Destino() == idCambiar)
//							{
//								listaRelaciones.get(j).setIdItemPlan_Destino(idCambioNuevo);
//								//actualizar relacion
//								ashyiToolDao.update(listaRelaciones.get(j), new ArrayList<String>(), "", true);
//							}
//							if(listaRelaciones.get(j).getIdItemPlan_Origen() == idCambiar)
//							{
//								listaRelaciones.get(j).setIdItemPlan_Origen(idCambioNuevo);
//								//actualizar relacion
//								ashyiToolDao.update(listaRelaciones.get(j), new ArrayList<String>(), "", true);
//							}
//						}
						
						//actualizar item usuario
						ItemPlan iP = ashyiToolDao.getItemPlan((Integer) actividades.get(index));					
						listUI.get(i).setIdItemPlan(iP);
						ashyiToolDao.update(listUI.get(i), new ArrayList<String>(), "", true);
					}
				}
				
			}
		}
		
		//guardar nuevo grafo
		saveGrafoUsuario(actividades, idUsuario, idActividad, "contexto",0,0);
	}
	
	/**
	 * Almacenar el grafo de un usuario ejecutor en la base de datos
	 * @param actividades lista de id de items que conforman el grafo
	 * @param idUsuario id de usuario asociado
	 * @param actividadRecursidad id actividad recursiva asociada al grafo
	 */
	public void saveGrafoUsuario(List actividades, String idUsuario, int actividadRecursidad, String origen, double nota, int idItemPlan) {
		Grafo grafo = new GrafoImpl();
		int tamLista = actividades.size();
		grafo.setIdItemPlan_Inicial((int) actividades.get(tamLista-1));
		
		saveCaracteristica(grafo, true);
		
		int idGrafo = getIdUltimoGrafo();
		grafo.setIdGrafo(idGrafo);
		
		//actualizar grafo de usuario
		//3 unidad didactica
		Usuario user = getUsuario(idUsuario);
				
		int idActividad = getActividad(actividadRecursidad).getIdActividad();
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
			update(grafoActivo);			
		}
		GrafosUsuario gU = new GrafosUsuarioImpl(grafo, user, idActividad,fechaActual,true, origen);
		if(!existeG)
		{
			grafoActivo = gU;
		}
		saveCaracteristica(gU, true);
		this.generarLog(user.getIdUsuario(), gU.getIdGrafo().getIdGrafo(), grafoActivo.getIdGrafo().getIdGrafo(), fechaActual,ashyiToolDao.getIdUsuarioInstructor(idActividad), origen,nota,idItemPlan);
		//actualizarGrafoEstudiante(idUsuario, grafo);
		
		if(tamLista == 1)
		{
			GrafoRelaciones relacion = new GrafoRelacionesImpl();
			relacion.setIdGrafo(grafo);
			relacion.setIdItemPlan_Origen((int)actividades.get(0));
			relacion.setOrden(1);
			saveCaracteristica(relacion, true);
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
				
				saveCaracteristica(relacion, true);
				orden++;
			}
		}
	}

	/**
	 * Verificar si el item de usuario existe
	 * @param itemPlan itemplan relacionado
	 * @param usuario usuario relacionado
	 * @return existe o no
	 */
	public boolean estaItemUsuario(ItemPlan itemPlan, Usuario usuario) {
		return ashyiToolDao.estaItemUsuario(itemPlan, usuario);	 
	}

	/**
	 * Consultar el grafo de un usuario relacionado a una actividad recursiva
	 * @param usuario usuario a consultar
	 * @param idActividad id de actividad recursiva
	 * @return grafo de un usuario relacionado a una actividad recursiva
	 */
	public Grafo getGrafoUsuario(Usuario usuario, int idActividad) {
		List<GrafosUsuario> gU = ashyiToolDao.getGrafoUsuario(usuario, idActividad);
		
		GrafosUsuario grafoActivo = new GrafosUsuarioImpl();
		//hay grafos re planificados, encontrar el activo
		for(GrafosUsuario g : gU)
		{
			if(g.isActivo())
			{
				grafoActivo = g;
				break;
			}
		}
		
		return grafoActivo.getIdGrafo();
		
	}

	/**
	 * Listar las relaciones de un grafo
	 * @param idGrafo id del grafo a consultar
	 * @return Lista de las relaciones de un grafo
	 */
	public List<GrafoRelaciones> getGrafoRelaciones(int idGrafo) {
		
		return ashyiToolDao.getGrafoRelaciones(idGrafo);
	}
	
	/**
	 * Consultar los items de usuario relacionados a una actividad recursiva
	 * @param idUsuario IDString de usuario
	 * @param idActividad id de una actividad recursiva
	 * @return los items de usuario relacionados a una actividad recursiva
	 */
	public List<ItemsUsuario> getItemsPlanUsuarioActividad(String idUsuario, Integer idActividad) {		
		
		return ashyiToolDao.getItemsUsuario(idUsuario, idActividad);
	}

	/**
	 * Consultar el usuario planificador de una actividad recursiva
	 * @param idActividad id de la actividad recursiva
	 * @return usuario planificador de la actividad recursiva
	 */
	public int getIdUsuarioInstructor(Integer idActividad) {
		
		return ashyiToolDao.getIdUsuarioInstructor(idActividad);
	}

	/**
	 * Consultar el grafo de un usuario asociado a una actividad recursiva
	 * @param idUsuario id del usaurio a consultar
	 * @param idActividad id de la actividad recursiva
	 * @return Grafos encontrados
	 */
	public List<GrafosUsuario> getGrafoUsuario(int idUsuario, Integer idActividad) {
		Usuario us = ashyiToolDao.getUsuario(idUsuario);
		return ashyiToolDao.getGrafoUsuario(us, idActividad);
	}

	/**
	 * Consultar objetivo por su ID
	 * @param idObjetivo
	 * @return objetivo consultado
	 */
	public Objetivo getObjetivoPorId(Integer idObjetivo) {
		return ashyiToolDao.getObjetivoPorId(idObjetivo);
	}

	/**
	 * Consultar los objetivos asociados a una actividad
	 * @param idActividad id de la actividad a consultar
	 * @return objetivos asociados a una actividad
	 */
	public List<ObjetivosActividad> getObjetivosActividad(Integer idActividad) {
		return ashyiToolDao.getObjetivosActividad(idActividad);
	}	

	/**
	 * Consultar los objetivos de una actividad
	 * @param nR nivel de recursividad de la actividad
	 * @param nombreA nombre de la actividad
	 * @return lista de los objetivos encontrados
	 */
	public List<List> getIdsObjetivosActividad(int nR, String nombreA) {
		Actividad unidad = ashyiToolDao.getactividad(nombreA, nR);
		List<List> objUnidad = ashyiToolDao.getIdsObjetivosActividad(unidad.getIdActividad());
		return objUnidad;
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
		return ashyiToolDao.getItemPlan(ud, auxActividad, auxRecurso, tipo, orden);
	}
	
	/**
	 * Consultar los items de sakai
	 * @return items de sakai
	 */
	public List<String> getItems()
	{
		return ashyiToolDao.getItems();
	}

	/**
	 * Actualiza objeto en la base de datos
	 * @param objetoActualizar objeto a Actualizar
	 */
	public void update(Object objetoActualizar) {
		ashyiToolDao.update(objetoActualizar, new ArrayList<String>(), "", true);
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
			if(tipoEvento==2 ||tipoEvento==3)// si es de tipo 2 o 3 no tiene nota ya que apenas se creó el grafo o cambio por el contexto (tipoEvento 2= origen, tipoEvento 3= contexto)
				//en el archivo se escribe de la siguiente manera: id del estudiante, item que se le califica, id grafo por el que se cambio, id grafo antiguo, id profesor, tipo evento (0 si no cambio el grafo, 1 si se replanifico el grafo, 2 si apenas se genero un grafo, año que se genero cambio, mes que se genero cambio, dia que se genero cambio, hora, minutos, segundos, nota de la actividad
			{
				LogGrafos logG=new LogGrafosImpl(idUsuario, 0, idGrafoNuevo, idGrafoAnterior, idProfesor, tipoEvento, 0, fecha);
				saveCaracteristica(logG, true);
				archivo.write(idUsuario+";"+" "+";"+idGrafoNuevo+";"+idGrafoAnterior+";"+idProfesor+";"+tipoEvento+";"+anio+";"+mes+";"+fecha.getDate()+";"+fecha.getHours()+";"+fecha.getMinutes()+";"+fecha.getSeconds()+";"+" "+"\n");
			
			}else{
				LogGrafos logG=new LogGrafosImpl(idUsuario, idItemPlan, idGrafoNuevo, idGrafoAnterior, idProfesor, tipoEvento, nota, fecha);
				saveCaracteristica(logG, true);
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
		Usuario u=getUsuario(idUsuario);
		int idActividad = getActividad(actividadRecursividad).getIdActividad();
		List<GrafosUsuario> grafos = getGrafoUsuario(u.getIdUsuario(), idActividad);
		GrafosUsuario grafoActivo = new GrafosUsuarioImpl();
		//hay grafos re planificados, encontrar el activo
		if(grafos != null)
		{
			for(GrafosUsuario gU : grafos)
			{
				if(gU.isActivo())
				{
					grafoActivo = gU;
					break;
				}
			}			
		}
		grafoActivo.setOrigen(evento);
		grafoActivo.setFecha(new Date());
		update(grafoActivo);
		
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
		if(tipoEvento==2 ||tipoEvento==3)// si es de tipo 2 o 3 no tiene nota ya que apenas se creó el grafo o cambio por el contexto (tipoEvento 2= origen, tipoEvento 3= contexto)
		{
			LogGrafos logG=new LogGrafosImpl(u.getIdUsuario(), 0, grafoActivo.getIdGrafo().getIdGrafo(), grafoActivo.getIdGrafo().getIdGrafo(), getIdUsuarioInstructor(idActividad), tipoEvento, 0, date);
		    saveCaracteristica(logG, true);
			archivo.write(u.getIdUsuario()+";"+" "+";"+grafoActivo.getIdGrafo().getIdGrafo()+";"+grafoActivo.getIdGrafo().getIdGrafo()+";"+getIdUsuarioInstructor(idActividad)+";"+tipoEvento+";"+anio+";"+mes+";"+date.getDate()+";"+date.getHours()+";"+date.getMinutes()+";"+date.getSeconds()+";"+" "+"\n");
		}else{
			LogGrafos logG=new LogGrafosImpl(u.getIdUsuario(), idItemPlan, grafoActivo.getIdGrafo().getIdGrafo(), grafoActivo.getIdGrafo().getIdGrafo(), getIdUsuarioInstructor(idActividad), tipoEvento, nota, date);
		    saveCaracteristica(logG, true);
			archivo.write(u.getIdUsuario()+";"+idItemPlan+";"+grafoActivo.getIdGrafo().getIdGrafo()+";"+grafoActivo.getIdGrafo().getIdGrafo()+";"+getIdUsuarioInstructor(idActividad)+";"+tipoEvento+";"+anio+";"+mes+";"+date.getDate()+";"+date.getHours()+";"+date.getMinutes()+";"+date.getSeconds()+";"+nota+"\n");
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
		
		RespuestaItemsUsuario r= getRespuestasItemsUsuario(idUsuario,idItemPlan);
		if (r!=null){
			LogCaracteristicas car=new LogCaracteristicasImpl(idUsuario, idItemPlan, tipoEvento, caracteristica, date, r.getFecha(), caracteristicasActuales);
			saveCaracteristica(car, true);
		}else{
			LogCaracteristicas car=new LogCaracteristicasImpl(idUsuario, idItemPlan, tipoEvento, caracteristica, date, new Date(), caracteristicasActuales);
			saveCaracteristica(car, true);
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

	public RespuestaItemsUsuario getRespuestasItemsUsuario(Integer idUsuario,
			Integer idItemPlan) {
		return ashyiToolDao.getRespuestaItemUsuario(idUsuario, idItemPlan);
	}
	
	public List<ItemPlan> obtenerFechasItemPlan(){
		return ashyiToolDao.obtenerFechasItemPlan();
	}
	
	public List<Integer> obtenerItemsUsuario(int idItemPlan){
		return ashyiToolDao.obtenerItemsUsuario(idItemPlan);
	}
	
	public String getMail(String usuario){
		return ashyiToolDao.getMail(usuario);
	}

	public String getUsuarioSakai(Integer usu) {
		// TODO Auto-generated method stub
		return ashyiToolDao.getUsuarioSakai(usu);
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
		return ashyiToolDao.itemEsta(ud, auxActividad, auxRecurso, tipo, isRefuerzo, orden);
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
	@SuppressWarnings("finally")
	public ItemPlan addItemPlan(Actividad ud, Actividad ac, Recurso recurso,
			boolean estaActivo, int orden) {
		ItemPlan ip = null;
		try {
			ip = appendItemPlan(ud, ac, recurso, estaActivo, orden);
			boolean ret = saveCarateristica(ip);
			if (ret)
				System.out.println("success");
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			return ip;
		}
	}

	/**
	 * Crea un nuevo itemPlan.
	 * @param ud
	 * @param ac
	 * @param recurso
	 * @param estaActivo
	 * @param orden
	 * @return i el itemPlan.
	 */
	private ItemPlan appendItemPlan(Actividad ud, Actividad ac, Recurso recurso,
			boolean estaActivo, int orden) {
		ItemPlan i = ashyiToolDao.makeItemPlan(ud, ac, recurso, estaActivo, orden);
		return i;
	}

	/**
	 * Agrega un itemPlan.
	 * @param ud
	 * @param ac
	 * @param estaActivo
	 * @param orden
	 * @return ip el itemPlan
	 */
	@SuppressWarnings("finally")
	public ItemPlan addItemPlan(Actividad ud, Actividad ac, boolean estaActivo, int orden) {
		ItemPlan ip = null;
		try {
			ip = appendItemPlan(ud, ac, estaActivo, orden);
			boolean ret = saveCarateristica(ip);
			if (ret)
				System.out.println("success");
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			return ip;
		}
	}

	/**
	 * Crea un nuevo itemPlan.
	 * @param ud
	 * @param ac
	 * @param estaActivo
	 * @param orden
	 * @return i el itemPlan.
	 */
	private ItemPlan appendItemPlan(Actividad ud, Actividad ac, boolean estaActivo, int orden) {
		ItemPlan i = ashyiToolDao.makeItemPlan(ud, ac, estaActivo, orden);
		return i;
	}
	
	/**
	 * Retorna el &uacuteltimo itemPlan creado.
	 * @return el itemPlan.
	 */
	public int getUltimoItemPlan() {
		return ashyiToolDao.getUltimoItemPlan();
	}

}
