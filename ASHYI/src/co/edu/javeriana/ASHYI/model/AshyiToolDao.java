/***********************************************************************************
 *Interface del Dao de la aplicacion
 *@author ASHYI
 *@see AshyiToolDaoImpl
 *
 **********************************************************************************/

package co.edu.javeriana.ASHYI.model;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AshyiToolDao {
	
	public DataSource getDataSource();
	
	/**
	 * Consultar las caracteristicas de un recurso en particular
	 * @param idRecurso id del recurso a consultar
	 * @return lista de las caracteristicas del recurso
	 */
	public List<CaracteristicaRecurso> getCaracteristicasRecurso(Integer idRecurso);

	/**
	 * Consultar un rescurso por si ID
	 * @param idRecurso id del recurso a consultar
	 * @return recurso consultado
	 */
	public Recurso getRecurso(Integer idRecurso);

	/**
	 * Consultar un item usuario con base en el id de item plan e id de usuario
	 * @param idTP id de item plan
	 * @param idUsuario id de usuario
	 * @return item usuario consultado
	 */
	public ItemsUsuario getItemsUsuario(ItemPlan itemPlan, String userId);

	/** 
	 * Consultar un usuario por si id string
	 * @param userId id string del usuario a consultar
	 * @return usuario consultado
	 */
	public Usuario getUsuario(String userId);

	/**
	 * Consultar los item plan activos en la actividad recursiva
	 * @param usuario usuario asociado
	 * @param nombreActividad nombre de la actividad recursiva
	 * @return lista de items plan activos
	 */
	public List<ItemPlan> getItemsActivos(Usuario usuario, String nombreActividad);

	/**
	 * Consultar los id de itema plan que conforman una relacion en un grafo
	 * @param idGrafo a consultar
	 * @return lista de id de items plan de un grafo
	 */
	public List<Integer> getIdItemsGrafo(Integer idGrafo);

	/**
	 * Consultar los id de itema plan que conforman una relacion en un grafo
	 * @param idGrafo a consultar
	 * @return lista de id de items plan de un grafo
	 */
	public List<Integer> getIdItemsPlan(Integer idGrafo);
	
	/**
	 * Consultar los recursos de una actividad
	 * @param nivelRecursividad nivel de recursividad de la actividad a consultar
	 * @param nombre nombre de recursividad de la actividad a consultar
	 * @return lista de los recursos de una actividad
	 */
	public List<RecursosActividad> getRecursosActividad(int nivelRecursividad, String nombre);
	
	/**
	 * Eliminar una actividad de la base de datos
	 * @param a actividad a eliminar
	 */
	public void deleteActividad(Actividad a);

	/**
	 * Consultar un usuario con base en su ID
	 * @param idUsuario id usuario a consultar
	 * @return usuario consultado
	 */
	public Usuario getUsuario(Integer idUsuario);
	
	/**
	 * Consultar el item plan asociado a una actividad y un recurso
	 * @param idActividad actividad asociada
	 * @param idRecurso recurso asociado
	 * @return Item plan consultado
	 */
	public ItemPlan getItemPlan(Actividad idActividad, Recurso idRecurso);

	/**
	 * Consultar el id del grafo de usuario asociado a una actividad
	 * @param idUsuario id de usuario asociado
	 * @param nombreActividad nombre de la actividad asociada
	 * @return id del grafo de usuario asociado a una actividad
	 */
	public Integer getIdGrafoUsuarioActividad(Integer idUsuario, String nombreActividad);
	
	/**
	 * Consultar un grafo por su ID
	 * @param idGrafo id del grafo a consultar
	 * @return grafo consultado
	 */
	public Grafo getGrafo(int idGrafo);
	
	/**
	 * Consultar el grafo de usuario asociado a una actividad
	 * @param idUsuario id de usuario asociado
	 * @param nombreActividad nombre de la actividad asociada
	 * @return grafo de usuario asociado a una actividad
	 */
	public Grafo getGrafoUsuarioActividad(Integer idUsuario, String nombreActividad);

	/**
	 * Consultar el ultimo grafo ingresado en la base de datos
	 * @return ultimo grafo ingresado en la base de datos
	 */
	public Grafo getUltimoGrafo();

	/**
	 * Consultar las caracteristicas (pre o post) de una actividad
	 * @param actividad actividad a consultar
	 * @param tipoPrePost tipo de consulta (1 pre, 2 post)
	 * @return lista con caracteristicas (pre o post) de una actividad
	 */
	public List<Caracteristica> getCaracteristicasPrePostActividad(Actividad actividad, Integer tipoPrePost);

	/**
	 * Consultar un itemPlan por su id
	 * @param idItem id a consultar
	 * @return itemPlan consultado
	 */
	public ItemPlan getItemPlan(Integer idItem);

	/**
	 * Consultar las caracteristicas segun su tipo (4-estilos, 7-personalidad, 5-habilidades, 6-competencias)
	 * @return lista de las caracteristicas completas del sistema
	 */
	public List<Caracteristica> getCaracteristicasPorTipo(Integer tipo);

	/**
	 * Consultar las actividades de refuerzo
	 * @param i nivel de recursividad i.e.actividades atomicas (4) de refuerzo 
	 * @return lista con las actividades de refuerzo
	 */
	public List<Actividad> getActividadRefuerzo(Integer i);

	/**
	 * Consultar las caracteristicas del perfil de un usuario
	 * @param aliasAg id Usuario a consultar
	 * @return lista de caracteristicas del perfil de un usuario
	 */
	public List<CaracteristicasUsuario> getCaracteristicasUsuario(String userId);

	/**
	 * Consultar un objetivo de una actividad
	 * @param idObj id objetivo a consultar
	 * @param idAct id actividad a consultar
	 * @return objetivo de una actividad consultado
	 */
	public ObjetivosActividad getObjetivoActividadObj(Integer idObj, Integer idAct);

	/**
	 * Consultar los objetivos de una actividad
	 * @param idObjetivo id objetivo a consultar
	 * @return objetivos de una actividad consultado
	 */
	public List<ObjetivosActividad> getObjetivosActividadObj(Integer idObjetivo);

	/**
	 * Flush in data base
	 */
	public void flush();

	/**
	 * Consultar la caracteristica de un recurso
	 * @param idRecurso recurso a consultar
	 * @param idCaracteristica caracteristica asociada
	 * @return caracteristica de recurso consultada
	 */
	public CaracteristicaRecurso getCaracteristicaRecursos(Recurso idRecurso, Caracteristica idCaracteristica);

	/**
	 * Consultar el ultimo usuario ejecutor agregado a la base de datos
	 * @param idUsuarioSakai id String de usuario a consultar
	 * @return id del ultimo usuario ingresado en la base de datos
	 */
	public Integer getUltimoUsuario(String idUsuarioSakai);

	/**
	 * Verificar si un usuario est[a almacenado en la base de datos
	 * @param usuario usuario a consultar
	 * @return si est[a almacenado o no
	 */
	public boolean isItUsuario(Usuario usuario);
	
	/**
	 * Obtener un arreglo con el nombre de los objetivos de una actvidad
	 * @param nombre nombre de la actividad a consultar
	 * @param nivelRecursividad nivel de recursividad de la actividad a consultar
	 * @return arreglo con el nombre de los objetivos de una actvidad
	 */
	public String[] getObjetivosActividad(Integer nivelRecursividad, String nombre);
	
	/**
	 * Consultar los recursos de una actividad atomica
	 * @param idActividad id de actividad a consultar
	 * @return lista de los recursos de una actividad atomica
	 */
	public List<RecursosActividad> getRecursosActividad(Integer idActividad);

	/**
	 * Consultar los objetivos asociados a una actividad
	 * @param idActividad id de la actividad a consultar
	 * @return objetivos asociados a una actividad
	 */
	public List<ObjetivosActividad> getObjetivosActividad(Integer idActividad);

	/**
	 * Consultar un objetivo por su id
	 * @param idObjetivo id de objetivo a consultar
	 * @return objetivo consultado
	 */
	public Objetivo getObjetivo(Integer idObjetivo);

	/**
	 * Consultar las caracteristicas de tipo de una actividad
	 * @param idActividad id de la actividad a consultar
	 * @return lista de las caracteristicas de tipo de una actividad
	 */
	public List<CaracteristicaActividad> getCaracteristicasActividad(Integer idActividad);

	/**
	 * Consultar las caracteristicas (pre o post) de una actividad
	 * @param idActividad id de la actividad a consultar
	 * @param tipo tipo de consulta (1 pre, 2 post)
	 * @param queConsulta  2 consultar caracteristicas de tipo
	 * @param correlacion false --> sin personalidad en el tipo o true --> con personalidad en el tipo
	 * @return lista de las caracteristicas (pre o post) de una actividad
	 */
	public List<Caracteristica> getCaracteristicasActividadDistancia(Integer idActividad, Integer tipo, Integer queConsulta, boolean correlacion);

	/**
	 * Consultar las caracteristicas de tipo de una actividad
	 * @param idActividad id de la actividad a consultar
	 * @return lista de las caracteristicas de tipo de una actividad
	 */
	public List<Caracteristica> getCaracteristicasTipoActividad(Integer idActividad);
	
	/**
	 * Consultar las caracteristicas de tipo de una actividad sin personalidad
	 * @param idActividad id de la actividad a consultar
	 * @return lista de las caracteristicas de tipo de una actividad
	 */
	public List<Caracteristica> getCaracteristicasCaracteristicasActividad(Integer idActividad);
	
	/**
	 * Consultar las caracteristicas de una caracteristica
	 * @param caracteristica caracteristica a consultar
	 * @return lista de las caracteristicas de una caracteristica de una actividad
	 */
	public List<CaracteristicaCaracteristicas> getCaracteristicasCaracteristica(Caracteristica caracteristica);

	/**
	 * Consultar si una actividad tiene relaciondo un recurso en especifico
	 * @param idActividad is de actividad a consultar
	 * @param idRecursoSakai id de recurso a verificar
	 * @return actividad que contiene el recurso
	 */
	public Actividad getActividadRecurso(Integer idActividad, String idRecursoSakai);
	
//	public Actividad getActividadRecurso(int idActividad, String idRecursoSakai);

	/**
	 * Consultar un objetivo por su nombre
	 * @param objetivo nombre de objetivo
	 * @return objetivo consultado
	 */
	public Objetivo getObjetivo(String objetivo);

	/**
	 * Consultar los recursos de una actividad
	 * @param nivelRecursividad nivel de recursividad de una actividad
	 * @param nombre de la actividad
	 * @return lista de los recursos de una actividad
	 */
	public List<RecursosActividad> getRecursosActividad(Integer nivelRecursividad, String nombre);

	/**
	 * Consultar las caracteristicas de un tipo
	 * @param nombreTipo nombre del tipo a consultar
	 * @return lista de las caracteristicas de un tipo
	 */
	public List<CaracteristicasTipo> getCaracteristicasTipo(String nombreTipo);

	/**
	 * Eliminar un objeto de la base de datos
	 * @param o objeto a eliminar
	 */
	public boolean deleteObject(Object o);

	/**
	 * Consultar una actividad por su nombre y nivel de recursividad
	 * @param actividadRecursidad nombre de la actividad a consultar
	 * @param i nivel de recursividad de la actividad a consultar
	 * @return actividad consultada
	 */
	public Actividad getactividad(String titulo, Integer nivelRecursividad);
	
	/**
	 * Consultar una actividad por su ID
	 * @param idActividad id activida a consultar
	 * @return actividad consultada
	 */
	public Actividad getActividad(Integer id);

	/**
	 * Consultar un item por su ID
	 * @param id identificador de item a consultar
	 * @return item consultado
	 */
	public Item getItemId(Integer id);

	/**
	 * Consultar un tipo por su ID
	 * @param id identificador de tipo a consultar
	 * @return tipo consultado
	 */
	public List<Tipo> getTipo(Integer id);

	/**
	 * Obtener una caracteristica por si ID
	 * @param id de la caracteristica
	 * @return
	 */
	public List<Caracteristica> getCaracteristica(Integer id);

	/**
	 * Eliminar un objeto de la base de datos
	 * @param editar objeto a eliminar
	 */
	public void eliminar(Object editar);

	/**
	 * Consultar las Estilos del sistema
	 * @return lista de Estilos
	 */
	public List<Caracteristica> getEstilos();

	/**
	 * Consultar las situaciones de aprendizaje del sistema
	 * @return lista de situaciones de aprendizaje
	 */
	public List<Caracteristica> getSA();

	/**
	 * Consultar las Personalidades del sistema
	 * @return lista de Personalidades
	 */
	public List<Caracteristica> getPersonalidades();

	/**
	 * Consultar las Competencias del sistema
	 * @return lista de Competencias
	 */
	public List<Caracteristica> getCompetencias();

	/**
	 * Consultar las habilidades del sistema
	 * @return lista de habilidades
	 */
	public List<Caracteristica> getContextos();

	/**
	 * Consultar las habilidades del sistema
	 * @return lista de habilidades
	 */
	public List<Caracteristica> getHabilidades();
	
	/**
	 * Actualizar un objeto en la base de datos
	 * @param o objeto a actualizar
	 * @param elist
	 * @param nowriteerr
	 * @param requiresEditPermission
	 * @return operacion exitoso o fallida
	 */
	public boolean update(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission);

	/**
	 * Eliminar un objeto de la base de datos
	 * @param o objeto a eliminar
	 * @return operacion exitoso o fallida
	 */
	public boolean deleteItem(Object o);

	/**
	 * Almacenar un objeto en la base de datos
	 * @param i objeto a almacenar
	 * @param requiresEditPermission permiso para almacenar
	 * @return almacenamiento exitoso o no
	 */
	public boolean saveCaracteristica(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission);

	/**
	 * Consultar el ultimo recurso almacenado
	 * @return
	 */
	public Recurso getUltimoRecurso();

	/**
	 * Consultar la ultima actividad almacenada 
	 * @return id de la actividad consultada
	 */
	public Integer getUltimaActividad();

	/**
	 * Consultar la ultima actividad almacenada con un nombre en especial
	 * @param nombre nombre de la actividad a consultar
	 * @return id de la actividad consultada
	 */
	public Integer getUltimaActividad(String nombre);

	/**
	 * Almacenar una actividad en la base de datos
	 * @param o actividad a almacenar
	 * @param elist
	 * @param nowriteerr
	 * @param requiresEditPermission
	 * @return proceso axitoso o fallido
	 */
	public boolean saveActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission);
	
	/**
	 * Consultar las caracteristicas asociadoas a un item de un usuario en particular
	 * @param iU item de usaurio a consultar
	 * @return lista de las caracteristicas asociadoas a un item de un usuario
	 */
	public List<CaracteristicasItemsUsuario> getCaracteristicasItemUsuario(ItemsUsuario iPlan);

	/**
	 * Consultar la actividad inicial del plan
	 * @return actividad inicial del plan
	 */
	public Actividad getActividadInicial();

	/**
	 * Consultar los items de usuario relacionados a una actividad recursiva
	 * @param idUsuario IDString de usuario
	 * @param idActividad id de una actividad recursiva
	 * @return los items de usuario relacionados a una actividad recursiva
	 */
	public List<ItemsUsuario> getItemsUsuario(String idUsuario, int idActividad);

	/**
	 * Consultar las relaciones de un grafo en particular 
	 * @param idGrafo id de grafo asociado
	 * @return listado de las relaciones de un grafo en particular
	 */
	public List<GrafoRelaciones> getRelacionesGrafo(int idGrafo);

	/**
	 * Verificar si el item de usuario existe
	 * @param itemPlan itemplan relacionado
	 * @param usuario usuario relacionado
	 * @return existe o no
	 */
	public boolean estaItemUsuario(ItemPlan itemPlan, Usuario usuario);
	
	/**
	 * Consultar el grafo de un usuario asociado a una actividad recursiva
	 * @param Usuario usaurio a consultar
	 * @param idActividad id de la actividad recursiva
	 * @return Grafos encontrados
	 */
	public List<GrafosUsuario> getGrafoUsuario(Usuario idUsuario, Integer idActividad);
	
	/**
	 * Eliminar de la base de datos las relaciones de un grafo
	 * @param idGrafo grafo asociado
	 */
	public void deleteGrafoRelaciones(Grafo idGrafo);

	/**
	 * Listar las relaciones de un grafo
	 * @param idGrafo id del grafo a consultar
	 * @return Lista de las relaciones de un grafo
	 */
	public List<GrafoRelaciones> getGrafoRelaciones(Integer idGrafo);

	/**
	 * Consultar el usuario planificador de una actividad recursiva
	 * @param idActividad id de la actividad recursiva
	 * @return usuario planificador de la actividad recursiva
	 */
	public int getIdUsuarioInstructor(Integer idActividad);

	/**
	 * Consultar objetivo por su ID
	 * @param idObjetivo
	 * @return objetivo consultado
	 */
	public Objetivo getObjetivoPorId(Integer idObjetivo);

	/**
	 * Consultar los items de sakai
	 * @return items de sakai
	 */
	public List<String> getItems();
	
	/**
	 * Consultar el ultimo item almacenado en la base de datos
	 * @return item ultimo item almacenado en la base de datos
	 */
	public Integer getUltimoItem();
	
	/**
	 * Consultar el ultimo objetivo almacenado en la base de datos, con un nombre en especifico
	 * @param nombre
	 * @return ultimo objetivo almacenado en la base de datos
	 */
	public Integer getUltimoObjetivo(String nombre);
	
	/**
	 * Consultar el ultimo objetivo almacenado en la base de datos
	 * @return ultimo objetivo almacenado en la base de datos
	 */
	public Integer getUltimoObjetivo();

	
	/**
	 * Consultar los objetivos de una actividad
	 * @param idActividad ID de la actividad
	 * @return lista de los objetivos encontrados
	 */
	public List<List> getIdsObjetivosActividad(Integer idActividad);
	
	/**
	 * Consulta un item plan segun la actividad y recurso relacionado
	 * @param ud actividad recursiva asociada
	 * @param auxActividad actividad atomica relacionada
	 * @param auxRecurso recurso asociado
	 * @param tipo tipo de recurso 1- con 2 - sin
	 * @param orden orden del item plan
	 * @return item plan consultado
	 */
	public ItemPlan getItemPlan(Actividad ud, Actividad auxActividad, Recurso auxRecurso,Integer tipo, Integer orden);
	
	public RecursosActividad getActividadRecursos(Integer idActividad, String idRecursoSakai);
	
	public RespuestaItemsUsuario getRespuestaItemUsuario(int idUsuario,int idItemPlan);
	
	public List<ItemPlan> obtenerFechasItemPlan();
	
	public List<Integer> obtenerItemsUsuario(int idItemPlan);
	
	public String getMail(String idUsuarioSakai);

	public String getUsuarioSakai(Integer usu);

	/**
	 * Crea un nuevo ItemPlan a partir de los par&aacutemetros recibidos.
	 * @param ud
	 * @param actividad
	 * @param estaActivo
	 * @param orden
	 * @return ItemPlan - el nuevo itemPlan creado.
	 */
	public ItemPlan makeItemPlan(Actividad ud, Actividad actividad,boolean estaActivo, Integer orden);
	
	/**
	 * Crea un nuevo ItemPlan a partir de los par&aacutemetros recibidos.
	 * @param ud unidad did&aacutectica.
	 * @param actividad
	 * @param recurso
	 * @param estaActivo
	 * @param orden
	 * @return ItemPlan - el nuevo itemPlan creado.
	 */
	public ItemPlan makeItemPlan(Actividad ud, Actividad actividad, Recurso recurso, boolean estaActivo, Integer orden);
	
	/**
	 * Retorna el identificador del &uacuteltimo itemPlan guardado en la base de datos.
	 * @return Integer - el identificador.
	 */
	public Integer getUltimoItemPlan();
	
	/**
	 * Retorna si un itemPlan existe en la base de datos.
	 * @param ud unidad did&aacutectica.
	 * @param auxActividad
	 * @param auxRecurso
	 * @param tipo
	 * @param isRefuerzo
	 * @param orden
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean itemEsta(Actividad ud, Actividad auxActividad, Recurso auxRecurso, Integer tipo, boolean isRefuerzo, Integer orden);
}
