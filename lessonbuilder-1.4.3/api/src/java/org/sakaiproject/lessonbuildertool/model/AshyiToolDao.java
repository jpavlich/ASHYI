/**********************************************************************************
 * $URL: $
 * $Id: $
 ***********************************************************************************
 *Interface del Dao de la aplicaci&oacuten
 *@author ASHYI
 *@see AshyiToolDaoImpl
 *
 **********************************************************************************/

package org.sakaiproject.lessonbuildertool.model;

import java.util.List;

import javax.sql.DataSource;

import org.sakaiproject.lessonbuildertool.SimplePageItem;


/**
 * Maneja las operaciones con la base de datos de Ashyi.
 * @author ashiy
 */
public interface AshyiToolDao {

	/**
	 * @author ashiy
	 * Informaci&oacuten de una p&aacutegina.
	 */
	public class PageData {
	    public Long itemId;
	    public Long pageId;
	    public String name;
	}

    // can edit pages in current site. Make sure that the page you are going to
    // edit is actually part of the current site.
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Make sure that the page you are going to edit is actually part of the current site."
	 * @return true or false - seg&uacuten el resultado.
	 */
	public boolean canEditPage();

    // session flush
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Hace flush de la sesi&oacuten.
	 */
	public void flush();
	
	/**
	 * Busca un item por su identificador.
	 * @param id el identificador
	 * @return SimplePageItem - el item encontrado.
	 */
	public SimplePageItem findItem(long id);

	// see saveItem for details and caveats, same function except delete instead of save
	/**
	 * ELimina un objeto.
	 * @param o el objeto
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean deleteItem(Object o);

    // see saveItem for details and caveats, same function except update instead of save
	/**
	 * Actualiza un objeto en la base de datos.
	 * @param o el objeto.
	 * @param elist lista de errores.
	 * @param nowriteerr 
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean update(Object o, List<String> elist, String nowriteerr, boolean requiresEditPermission);

    /**
     * Crea una nueva actividad a partir de los par&aacutemetros recibidos.
     * @param tipo
     * @param item
     * @param name
     * @param description
     * @param nR nivel de recursividad.
     * @param dedicacion
     * @param nivel
     * @return Actividad - la nueva actividad creada.
     */
    public Actividad makeActividad(Tipo tipo, Item item, String name, String description, Integer nR, Integer dedicacion, String nivel);
	
	/**
	 * Crea una nueva actividad a partir de los par&aacutemetros recibidos.
	 * @param item
	 * @param name
	 * @param nR
	 * @param dedicacion
	 * @param nivel
	 * @return Actividad - la nueva actividad creada.
	 */
	public Actividad makeActividad(Item item, String name, Integer nR, Integer dedicacion, String nivel);
	
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
	 * Crea un nuevo ItemPlan a partir de los par&aacutemetros recibidos.
	 * @param ud
	 * @param actividad
	 * @param estaActivo
	 * @param orden
	 * @return ItemPlan - el nuevo itemPlan creado.
	 */
	public ItemPlan makeItemPlan(Actividad ud, Actividad actividad,boolean estaActivo, Integer orden);
	
	/**
	 * Crea una nueva Caracter&iacutestica a partir de los par&aacutemetros recibidos.
	 * @param item
	 * @param name
	 * @return Caracteristica - la nueva caracter&iacutestica.
	 */
	public Caracteristica makeCaracteristica(Item item, String name);
	
	/**
	 * Crea un nuevo Tipo a partir de los par&aacutemetros recibidos.
	 * @param name
	 * @return Tipo - el nuevo tipo.
	 */
	public Tipo makeTipo(String name);

	/**
	 * Guarda una actividad en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveActividad(Object i, List<String> elist, String message,boolean requiresEditPermission);
	
	/**
	 * Guarda un GrafoRelaciones en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveGrafoRelaciones(Object i, List<String> elist, String message,boolean requiresEditPermission);
	/**
	 * Guarda un Grafo en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveGrafo(Object i, List<String> elist, String message,boolean requiresEditPermission);
	/**
	 * Guarda un GrafosUsuario en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveGrafosUsuario(Object i, List<String> elist, String message,boolean requiresEditPermission);
	/**
	 * Guarda un ItemPlan en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveItemPlan(Object i, List<String> elist, String message,boolean requiresEditPermission);

	/**
	 * Retorna el identificador de la &uacuteltima actividad guardada en la base de datos.
	 * @param nombre
	 * @return Integer - el identificador.
	 */
	public Integer getUltimaActividad(String nombre);
	/**
	 * Retorna el identificador de la &uacuteltima actividad guardada en la base de datos.
	 * @return Integer - el identificador.
	 */
	public Integer getUltimaActividad();
	/**
	 * Retorna el identificador del &uacuteltimo Grafo guardado en la base de datos.
	 * @return Integer - el identificador.
	 */
	public Integer getUltimoGrafo();
	/**
	 * Retorna el &uacuteltimo Grafo completo guardado en la base de datos.
	 * @return Grafo - el grafo.
	 */
	public Grafo getUltimoGrafoCompleto();
	
	//public boolean saveCaracteristicasActividades(List<CaracteristicaActividad> caracteristicas);
	/**
	 * Guarda una CaracteristicaActividad en la base de datos.
	 * @param caracteristicas
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveCaracteristicasActividades(CaracteristicaActividad caracteristicas);
		
	/**
	 * Guarda una Caracteristica en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveCaracteristica(Object i, List<String> elist, String message,boolean requiresEditPermission);

	/**
	 * Retorna si es posible editar el dominio Ashyi
	 * @return Integer - el permiso.
	 */
	public Integer getEditarDominioAshyi();
		
	/**
	 * Retorna los tipos de caracter&iacutesticas.
	 * @return List<Item> - la lista.
	 */
	public List<Item> getTiposCaracteristicas();

	/**
	 * Crea un nuevo control.
	 * @param currentUserId2
	 * @param cambio
	 * @return Control - el nuevo control.
	 */
	public Control makeControl(String currentUserId2, boolean cambio);
	/**
	 * Retorna los tipos de contexto.
	 * @return List<Item> - la lista.
	 */
	public List<Item> getTiposContexto();
	/**
	 * Retorna los tipos de actividad.
	 * @return List<Item> - la lista.
	 */
	public List<Tipo> getTiposActividad();
	/**
	 * Retorna las habilidades.
	 * @return List<Caracteristica> - la lista.
	 */
	public List<Caracteristica> getHabilidades();
	/**
	 * Retorna las competencias.
	 * @return List<Caracteristica> - la lista.
	 */
	public List<Caracteristica> getCompetencias();
	/**
	 * Retorna las personalidades.
	 * @return List<Caracteristica> - la lista.
	 */
	public List<Caracteristica> getPersonalidades();
	/**
	 * Retorna las situaciones de aprendizaje.
	 * @return List<Caracteristica> - la lista.
	 */
	public List<Caracteristica> getSA();
	/**
	 * Retorna los estilos de aprendizaje.
	 * @return List<Caracteristica> - la lista.
	 */
	public List<Caracteristica> getEstilos();
	/**
	 * Retorna los tipos.
	 * @return List<Tipo> - la lista.
	 */
	public List<Tipo> getTipos();

	/**
	 * Elimina un objeto.
	 * @param editar el objeto a eliminar.
	 */
	public void eliminar(Object editar);
	
	/**
	 * Retorna una caracter&iacutestica.
	 * @param id el identificador de la caracter&iacutestica.
	 * @return List<Caracteristica> - la lista.
	 */
	public List<Caracteristica> getCaracteristica(Integer id);
	/**
	 * Retorna un tipo.
	 * @param id el identificador del tipo.
	 * @return List<Tipo> - la lista.
	 */
	public List<Tipo> getTipo(Integer id);
	
	/**
	 * Registra un curso.
	 * @param titulo t&iacutetulo del curso.
	 * @return String - resultado de la operaci&oacuten.
	 */
	public String registrarCurso(String titulo);
	
	/**
	 * Retorna una actividad a partir del t&iacutetulo y el nivel de recursividad.
	 * @param titulo t&iacutetulo de la actividad.
	 * @param nivelRecursividad nivel de recursividad de la actividad.
	 * @return Actividad - la actividad encontrada.
	 */
	public Actividad getactividad(String titulo, Integer nivelRecursividad);
	
	/**
	 * Retorna el identificador del &uacuteltimo objetivo guardado en la base de datos.
	 * @param nombre nombre del objetivo.
	 * @return Integer - el identificador del objetivo.
	 */
	public Integer getUltimoObjetivo(String nombre);

	/**
	 * Retorna los objetivos de una actividad a partir de su nombre y nivel de recursividad.
	 * @param nivelRecursividad nivel de recursividad de la actividad.
	 * @param nombre nombre de la actividad.
	 * @return String[] - arreglo con los nombres de los objetivos.
	 */
	public String[] getObjetivosActividad(Integer nivelRecursividad, String nombre);
	
	/**
	 * Retorna el identificador de un objetivo de actividad.
	 * @param nombre nombre del objetivo. 
	 * @param nR nivel de recursividad.
	 * @param nombreAc nombre de la actividad.
	 * @return Integer - el identificador.
	 */
	public Integer getObjetivoActividad(String nombre, Integer nR, String nombreAc);

	/**
	 * Retorna el nombre de un Item a partir de su identificador.
	 * @param itemId el identificador.
	 * @return String - el nombre del Item.
	 */
	public String getItemName(Long itemId);

	/**
	 * Retorna una actividad a partir de su identificador.
	 * @param idActividad
	 * @return Actividad - la actividad.
	 */
	public Actividad getActividad(Integer idActividad);

	/**
	 * Retorna el &uacuteltimo recurso guardado en la base de datos.
	 * @return Recurso - el recurso.
	 */
	public Recurso getUltimoRecurso();

	/**
	 *
	 * Retorna los items de las caracter&iacutesticas.
	 * 
	 * @return List<Item> - los items.
	 */
	public List<Item> getItemsCaracterisiticas();

	/**
	 * Agrega un nuevo item.
	 * @param name
	 * @param testC
	 * @return Item - el item.
	 */
	public Item makeItemAshyi(String name, String testC);
	/**
	 * Retorna un itemAshyi a partir de su nombre.
	 * @param nombre
	 * @return Item - el item.
	 */
	public Item getItemAshyi(String nombre);
	
	/**
	 * Retorna el identificador del &uacuteltimo item guardado en la base de datos.
	 * @return Integer - el identificador.
	 */
	public Integer getUltimoItem();
	
	/**
	 * Retorna un tipo a partir de su item y nombre.
	 * @param item
	 * @param nombre
	 * @return Tipo - el tipo.
	 */
	public Tipo getTipo(Item item, String nombre);

	/**
	 * Retorna los contextos.
	 * @return List<Caracteristica> - los contextos.
	 */
	public List<Caracteristica> getContextos();
	
	/**
	 * Retorna un item a partir de su identificador.
	 * @param id
	 * @return Item - el item.
	 */
	public Item getItemId(Integer id);

    /**
     * Retorna las dependenciaActividad.
     * @return List<DependenciaActividad> - las dependenciaActividad.
     */
    public List<DependenciaActividad> getDependenciasActividades();

	/**
	 * Retorna las dependenciaActividad.
	 * @param act
	 * @param actDependiente
	 * @return List<DependenciaActividad> - las dependenciaActividad.
	 */
	public List<DependenciaActividad> getDependenciaActividad(Integer act, Integer actDependiente);

	/**
	 * Guarda una dependenciaActividad en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveDependenciaActividad(Object i, List<String> elist, String message,boolean requiresEditPermission);
	
	/**
	 * Retorna las actividadTieneActividad a partir del identificador de la actividad padre.
	 * @param id
	 * @return List<ActividadTieneActividad> - las actividadTieneActividad.
	 */
	public List<ActividadTieneActividad> getActividadTieneActividades(Integer id);
	/**
	 * Retorna una actividadTieneActividad a partir del identificador de la actividad padre y la actividad de siguiente nivel.
	 * @param id
	 * @return List<ActividadTieneActividad> - la actividadTieneActividad.
	 */
	public List<ActividadTieneActividad> getActividadTieneActividad(Integer id, Integer idSiguienteNivel);
	/**
	 * Retorna una actividadTieneItemPlan a partir del identificador de la actividad y el itemPlan.
	 * @param id
	 * @return List<ActividadTieneItemPlan> - la actividadTieneActividad.
	 */
	public List<ActividadTieneItemPlan> getActividadTieneItemPlan(Integer id, Integer idItemPlan);
	/**
	 * Retorna el estilo de la actividad hijo en una actividadTieneActividad a partir del identificador de la actividad padre y la actividad de siguiente nivel.
	 * @param id
	 * @return List<String> - la actividadTieneActividad.
	 */
	public List<String> getEstiloSiguienteNivelActTieneAct(Integer id, Integer idSiguienteNivel);
	/**
	 * Retorna los identificadores de las actividadTieneActividad a partir del identificador de la actividad padre.
	 * @param id
	 * @return List<String> - los identificadores.
	 */
	public List<String> getActividadTieneActividadString(Integer id);
	
	/**
	 * Retorna una actividad a partir de su identificador.
	 * @param id
	 * @return List<Actividad> - la actividad.
	 */
	public List<Actividad> getActividades(Integer id);
	
	/**
	 * Elimina un objeto de la base de datos.
	 * @param o
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean deleteObject(Object o);
	
	/**
	 * Retorna los tipos por nombre.
	 * @param nombreTipo
	 * @return List<CaracteristicasTipo> - los tipos.
	 */
	public List<CaracteristicasTipo> getCaracteristicasTipo(String nombreTipo);
	/**
	 * Guarda una actividadTieneActividad en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveActividadTieneActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission);
	/**
	 * Guarda una actividadTieneItemPlan en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveActividadTieneItemPlan(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission);
	
	/**
	 * Retorna un recurso a partir de su nombre.
	 * @param nombre
	 * @return Recurso - el recurso.
	 */
	public Recurso getRecurso(String nombre);

	/**
	 * Retorna los recursos de una actividad a partir de su nivel de recursividad y su nombre.
	 * @param nivelRecursividad
	 * @param nombre
	 * @return List<RecursosActividad> - los recursos.
	 */
	public List<RecursosActividad> getRecursosActividad(Integer nivelRecursividad, String nombre);

	/**
	 * Retorna un objetivo a partir de su nombre.
	 * @param objetivo
	 * @return Objetivo - el objetivo.
	 */
	public Objetivo getObjetivo(String objetivo);
	/**
	 * Retorna un objetivo a partir de su identificador.
	 * @param idObjetivo
	 * @return Objetivo - el objetivo.
	 */
	public Objetivo getObjetivoPorId(Integer idObjetivo);
	/**
	 * Retorna el recurso de una actividad a partir delnivel de recursividad y el identificador del recurso.
	 * @param actividad
	 * @param idRecursoSakai
	 * @return RecursosActividad - el recurso.
	 */
	public RecursosActividad getActividadRecurso(Integer actividad, String idRecursoSakai);

	/**
	 * Retorna una actividad completa a partir de su identificador.
	 * @param id
	 * @return Actividad - la actividad.
	 */
	public Actividad getActividadCompleta(Integer id);
	/**
	 * Retorna las caracter&iacutesticas de una actividad a partir de su identificador.
	 * @param idActividad
	 * @return List<CaracteristicaActividad> - las caracter&iacutesticas.
	 */
	public List<CaracteristicaActividad> getCaracteristicasActividad(Integer idActividad);
	/**
	 * Retorna los objetivos de una actividad a partir de su identificador.
	 * @param idActividad
	 * @return List<ObjetivosActividad> - los objetivos.
	 */
	public List<ObjetivosActividad> getObjetivosActividad(Integer idActividad);
	/**
	 * Retorna los recursos de una actividad a partir de su identificador.
	 * @param idActividad
	 * @return List<RecursosActividad> - los recursos.
	 */
	public List<RecursosActividad> getRecursosActividad(Integer idActividad);
	
	/**
	 * Retorna la actividad padre en una actividadTieneActividad a partir del identificador de la actividad de siguiente nivel.
	 * @param id
	 * @return Actividad - la actividad padre.
	 */
	public Actividad getActividadAltoNivel(Integer id);

	/**
	 * Elimina una actividad de la base de datos.
	 * @param a
	 */
	public void deleteActividad(Actividad a);

	/**
	 * Retorna si un usuario existe.
	 * @param usuario
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean isItUsuario(Usuario usuario);
	
	/**
	 * Retorna el datasource.
	 * @return DataSource - el datasource.
	 */
	public DataSource getDataSource();

	/**
	 * Retorna un usuario a partir de su identificador.
	 * @param idUsuario
	 * @return Usuario - el usuario.
	 */
	public Usuario getUsuario(Integer idUsuario);
	
	
	public List<LogCaracteristicas> getLogCaracteristicasUsuario(Usuario usuario);

	/**
	 * Retorna el identificador del &uacuteltimo usuario guardado en la base de datos de Ashyi a partir de su identificador de Sakai.
	 * @param idUsuarioSakai
	 * @return Integer - el identificador del usuario.
	 */
	public Integer getUltimoUsuario(String idUsuarioSakai);

	/**
	 * Retorna las caracter&iacutesticas de un recurso.
	 * @param idRecurso
	 * @param GrafoUsuario
	 * @return CaracteristicaRecurso - las caracter&iacutesticasRecurso.
	 */
	public CaracteristicaRecurso getCaracteristicaRecursos(Recurso idRecurso, Caracteristica GrafoUsuario);
	
	/**
	 * Retorna las caracter&iacutesticas de un usuario a partir de su identificador.
	 * @param idUsuario
	 * @return List<CaracteristicasUsuario> - las caracter&iacutesticas.
	 */
	public List<CaracteristicasUsuario> getEstilosUsuario(Integer idUsuario);

	/**
	 * Retorna una palabraClave a partir de su nombre.
	 * @param palabraClave
	 * @return PalabraClave - la palabraClave.
	 */
	public PalabraClave getPalabra(String palabraClave);

	/**
	 * Retorna el identificador de la &uacuteltima palabraClave guardada en la base de datos.
	 * @param palabra
	 * @return Integer - el identificador.
	 */
	public Integer getUltimaPalabra(String palabra);

	/**
	 * Retorna las palabrasClave de una actividad.
	 * @param tipo
	 * @param nombreActividad
	 * @param nR nivel de recursividad.
	 * @return List<PalabraClave> - las palabraClave.
	 */
	public List<PalabraClave> getPalabrasClave(Integer tipo, String nombreActividad, Integer nR);

	/**
	 * Retorna los identificadores de los objetivos de una actividad a partir de su identificador.
	 * @param idActividad
	 * @return List - los identificadores.
	 */
	public List getIdsObjetivosActividad(Integer idActividad);
	
	/**
	 * Retorna los recursosActividad.
	 * @param items
	 * @param actividad
	 * @return List<RecursosActividad> - los recursosActividad.
	 */
	public List<RecursosActividad> actividadesRecurso(List<SimplePageItem> items, Actividad actividad);

	/**
	 * Retorna las caracteristicas de un usuario.
	 * @param idUsuario
	 * @param tipoC
	 * @return Integer - las CaracteristicasUsuario.
	 */
	public Integer getCaracteristicasUsuario(Integer idUsuario, Integer tipoC);
	
	/**
	 * Retorna si un usuario ha llenado una encuesta.
	 * @param idUsuario
	 * @param encuesta
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean getRespuestasUsuario(String idUsuario, Integer encuesta);
	
	/**
	 * Retorna si hay actividades de refuerzo de cierto nivel.
	 * @param nActividades
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean getActividadesRefuerzo(String nActividades);

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

	/**
	 * Retorna un itemPlan a partir de su identificador.
	 * @param idItem
	 * @return ItemPlan - el itemPlan.
	 */
	public ItemPlan getItemPlan(Integer idItem);

	/**
	 * Retorna un itemPlan.
	 * @param ud unidad did&aacutectica
	 * @param auxActividad
	 * @param auxRecurso
	 * @param tipo
	 * @param orden
	 * @return ItemPlan - el itemPlan.
	 */
	public ItemPlan getItemPlan(Actividad ud, Actividad auxActividad, Recurso auxRecurso, Integer tipo, Integer orden);

	/**
	 * Retorna si un usuario tiene GrafosUsuario para una unidadDidactica.
	 * @param usuarioSakai
	 * @param titulo identificador de la ud.
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean isGrafoUsuario(String usuarioSakai, String titulo);
	
	/**
	 * Retorna el GrafosUsuario de un usuario en una unidadDidactica.
	 * @param idUsuario
	 * @param idActividad
	 * @return List<GrafosUsuario> - el GrafosUsuario.
	 */
	public List<GrafosUsuario> getGrafoUsuario(Usuario idUsuario, Integer idActividad);
	
	/**
	 * Elimina de la base de datos todas las GrafoRelaciones de un grafo a partir del grafo.
	 * @param idGrafo
	 */
	public void deleteGrafoRelaciones(Grafo idGrafo);
	
	/**
	 * Retorna un itemsUsuario a partir del itemPlan y el identificador del usuario.
	 * @param idTP
	 * @param idTU
	 * @return ItemsUsuario - el itemsUsuario.
	 */
	public ItemsUsuario getItemUsuario(Integer idTP, Integer idTU);

	/**
	 * Retorna las grafoRelaciones de un grafo a partir de su identificador.
	 * @param idGrafo
	 * @return List<GrafoRelaciones> - las GrafoRelaciones.
	 */
	public List<GrafoRelaciones> getGrafoRelaciones(Integer idGrafo);

	/**
	 * Retorna las caracter&iacutesticas para actividades a distancia.
	 * @param idActividad
	 * @param tipo
	 * @param queConsulta
	 * @return List<Caracteristica> - las caracter&iacutesticas.
	 */
	public List<Caracteristica> getCaracteristicasActividadDistancia(Integer idActividad, Integer tipo, Integer queConsulta);
	/**
	 * Retorna las caracter&iacutesticas para actividades pre y post.
	 * @param idActividad
	 * @param tipo
	 * @param queConsulta
	 * @return List<Caracteristica> - las caracter&iacutesticas.
	 */
	public List<Caracteristica> getCaracteristicasPrePostActividad(Actividad actividad, Integer tipoPrePost);
	/**
	 * Retorna las caracter&iacutesticas tipo de una actividad.
	 * @param idActividad
	 * @return List<Caracteristica> - las caracter&iacutesticas.
	 */
	public List<Caracteristica> getCaracteristicasTipoActividad(Integer idActividad);
	/**
	 * Retorna las caracter&iacutesticas llenas de un ItemsUsuario.
	 * @param iPlan
	 * @return List<Caracteristica> - las caracter&iacutesticas.
	 */
	public List<Caracteristica> getCaracteristicasLlenasItemUsuario(ItemsUsuario iPlan);

	/**
	 * Retorna las CaracteristicasItemUssuario a partir del ItemsUsuario.
	 * @param iU
	 * @return List<CaracteristicasItemsUsuario> - las CaracteristicasItemsUsuario.
	 */
	public List<CaracteristicasItemsUsuario> getCaracteristicasItemUsuario(ItemsUsuario iU);

	/**
	 * Retorna caracter&iacutesticas por tipo.
	 * @param i el tipo.
	 * @return List<Caracteristica> - las caracter&iacutesticas.
	 */
	public List<Caracteristica> getCaracteristicasPorTipo(Integer i);

	/**
	 * Retorna una nueva actividad de inicio de unidad did&aacutectica, si no existe la crea.
	 * @return Actividad - actividad de inicio.
	 */
	public Actividad getActividadInicial();
	/**
	 * Guarda un objetivoActividad en la base de datos.
	 * @param i el objeto.
	 * @param elist lista de errores.
	 * @param message
	 * @param requiresEditPermission si requiere permisos de edici&oacuten.
	 * @return true o false - seg&uacuten el resultado de la operaci&oacuten.
	 */
	public boolean saveObjetivoActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission);
	/**
	 * Retorna el identificador del &uacuteltimo itemPlan guardado en la base de datos.
	 * @return Integer - el identificador.
	 */
	public Integer getUltimoItemPlan();
	/**
	 * Retorna el identificador del &uacuteltimo objetivo guardado en la base de datos.
	 * @return Integer - el identificador.
	 */
	public Integer getUltimoObjetivo();

	/**
	 * Retorna los identificadores de los itemPlans de un grafo.
	 * @param idUsuario
	 * @param idActividad
	 * @return List<Integer> - los identificadores.
	 */
	public List<Integer> getItemsGrafo(Integer idUsuario, Integer idActividad);
	/**
	 * Retorna los itemPlans de una actividad.
	 * @param idActividad
	 * @return List<ItemPlan> - los itemPlans.
	 */
	public List<ItemPlan> getItemsPlanActividad(Integer idActividad);

	/**
	 * Retorna si existe un itemUsuario con un itemPlan, a partir del identificador del itemPlan.
	 * @param idItemPlan
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean estaItemEnUsuarios(int idItemPlan);

	/**
	 * Retorna si hay actividades con el mismo objetivo que una actividad a partir de su identificador.
	 * @param idActividad
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean hayActividadesMismoObjetivo(Integer idActividad);
	
	/**
	 * Retorna un grafo a partir de su identificador.
	 * @param idGrafo
	 * @return Grafo - el grafo.
	 */
	public Grafo getGrafo(int idGrafo);

	/**
	 * Retorna una RespuestaItemsUsuario a partir del identificador de itemPlan, usuario y respuesta itemsUsuario.
	 * @param idTP
	 * @param idTU
	 * @param idRIU
	 * @return RespuestaItemsUsuario - la RespuestaItemsUsuario.
	 */
	public RespuestaItemsUsuario getRespuestaItemUsuario(Integer idTP,Integer idTU, Integer idRIU);

	/**
	 * Retorna las RespuestaItemsUsuario a partir del identificador de itemPlan y de usuario.
	 * @param idTP2
	 * @param idTU2
	 * @return List<RespuestaItemsUsuario> - las RespuestaItemsUsuario.
	 */
	public List<RespuestaItemsUsuario> getRespuestasItemUsuario(int idTP2, int idTU2);
	/**
	 * Retorna el identificador de la &uacuteltima respuesta guardada en la base de datos.
	 * @return Integer - el identificador.
	 */
	public Integer getUltimaRespuesta();

	/**
	 * Retorna una RespuestaItemsUsuario a partir de su identificador.
	 * @param idRespuesta
	 * @return RespuestaItemsUsuario - la RespuestaItemsUsuario.
	 */
	public RespuestaItemsUsuario getRespuestaItemUsuario(Integer idRespuesta);
	
	/**
	 * Retorna los itemPlans de una unidad did&aacutectica a partir de su identificador.
	 * @param idActividad
	 * @return List - los itemPlans.
	 */
	public List getItemPlansPorUnidadDidactica(Integer idActividad);
	
	/**
	 * Guarda un objeto en la base de datos.
	 * @param o
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean saveObject(Object o);
	
	/**
	 * Retorna los itemPlans de una unidad did&aacutectica a partir de la ud.
	 * @param ud
	 * @return List - los itemPlans.
	 */
	public List getItemPlansUD(Actividad ud);

	/**
	 * Retorna los identificadores de los objetivos de una actividad.
	 * @param i nivel de recursividad.
	 * @param title nombre de actividad.
	 * @return List<Integer> - los identificadores.
	 */
	public List<Integer> getIdObjetivosActividad(int i, String title);
	/**
	 * Retorna el estado de una actividad, si ha sido realizada por el usuario.
	 * @param ip el itemPlan.
	 * @param idUsuario el usuario.
	 * @return Integer - el estado.
	 */
	public Integer getEstadoItemsUsuario(Integer ip, Integer idUsuario);
	
	/**
	 * Retorna el id del test de chaea según el id del sitio
	 * @param currentSiteId
	 * @return currentSiteId
	 */
	public long getIdChaeaTest(String currentSiteId);

	/**
	 * Retorna el id de la encuesta de personalidad según el id del sitio
	 * @param currentSiteId
	 * @param tipo 
	 * @return currentSiteId
	 */
	public int getIdPollPyH(String currentSiteId, int tipo);

	public List<GrafosUsuario> getGrafosActividad(Actividad actAnterior);

	public 	List <Integer> getUsuariosLogCaracteristicas();

	public List<LogGrafos> getLogGrafosUsuario(Usuario usuario);

	/**
	 * Busca los recursos referentes a un objetivo de una avtividad
	 * @param idObjetivo id objetivo a buscar
	 * @param idActividad id de ka unidad didactica
	 * @return lista de los nombres de los archivos relacionados
	 */
	public List<String> getRespuestasObjetivoUnidad(int idObjetivo,int idActividad);		
	
	public List<Integer> getUsuariosActivos();

	/**
	 * Busca los nombres de los usuarios asociados a los recursos referentes a un objetivo de una avtividad
	 * @param idObjetivo id objetivo a buscar
	 * @param idActividad id de ka unidad didactica
	 * @return lista de los nombres de los usuarios duenios de los archivos relacionados
	 */
	public List<String> getUsuariosRespuestasObjetivoUnidad(int idObjetivo, int idActividad);
	
	/**
	 * Retorna los identificadores de los itemPlans de un grafo por su id.
	 * @param idGrafo
	 * @return List<Integer> - los identificadores.
	 */
	public List<Integer> getIdItemsPlan(int idGrafo);
}
