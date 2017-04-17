/**********************************************************************************
 * $URL: $
 * $Id: $
 ***********************************************************************************
 *
 * Author: Eric Jeney, jeney@rutgers.edu
 *
 * Copyright (c) 2010 Rutgers, the State University of New Jersey
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");                                                                
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.lessonbuildertool.model;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.sakaiproject.db.cover.SqlService;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageComment;
import org.sakaiproject.lessonbuildertool.SimplePageGroup;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.SimplePageLogEntry;
import org.sakaiproject.lessonbuildertool.SimpleStudentPage;
import org.sakaiproject.lessonbuildertool.SimplePageProperty;
import org.sakaiproject.user.api.User;

/**
 * Maneja las operaciones con la base de datos de Sakai y algunas de Ashyi.
 * @author ashiy
 */
/**
 * @author ashiy
 *
 */
public interface SimplePageToolDao {
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
	 * Retorna los items de la p&aacutegina.
	 * @param id el identificador
	 * @return List<SimplePageItem> - los items.
	 */
	public List<SimplePageItem> findItemsOnPage(long pageId);


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "returns the next item on the page. argument is sequence no. I.e. finds item with sequence + 1 on specified page. just pages.
	 * @param pageId
	 * @param sequence
	 * @return SimplePageItem - el item.
	 */
	public SimplePageItem findNextPageItemOnPage(long pageId, int sequence);


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Returns the next item on the page. argument is sequence no. I.e.
     finds item with sequence + 1 on specified page. any item type.
	 * @param pageId
	 * @param sequence
	 * @return SimplePageItem - el item.
	 */
	public SimplePageItem findNextItemOnPage(long pageId, int sequence);


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "For a specific tool (i.e. instance of lessonbuilder), find the most
     recently visited page. Used so we can offer to return user to where he was last."
	 * @param userId
	 * @param tooldId
	 * @return PageData - la p&aacutegina.
	 */
	public PageData findMostRecentlyVisitedPage(final String userId, final String tooldId);


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "List of top-level pages in the site. However we return the items for the
     pages, not the pages themselves. Ordered by site order, so this should be the
     same order in which they are shown in the left margin."
	 * @param userId
	 * @param tooldId
	 * @return List<SimplePageItem> - los items.
	 */
	public List<SimplePageItem> findItemsInSite(String siteId);
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param siteId
	 * @return List<SimplePageItem> - los items.
	 */
	public List<SimplePageItem> findDummyItemsInSite(String siteId);
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param siteId
	 * @return List<SimplePageItem> - los items.
	 */
	public List<SimplePageItem> findTextItemsInSite(String siteId);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param id
	 * @return SimplePageItem - un SimplePageItem.
	 */
	public SimplePageItem findItem(long id);
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param attribute
	 * @return SimplePageProperty - una SimplePageProperty.
	 */
	public SimplePageProperty findProperty(String attribute);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param attribute
	 * @param value
	 * @return SimplePageProperty - una SimplePageProperty.
	 */
	public SimplePageProperty makeProperty(String attribute, String value);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param commentWidgetId
	 * @return List<SimplePageComment> - los comments.
	 */
	public List<SimplePageComment> findComments(long commentWidgetId);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param commentItemIds
	 * @return List<SimplePageComment> - los comments.
	 */
	public List<SimplePageComment> findCommentsOnItems(List<Long> commentItemIds);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param commentItemIds
	 * @param author
	 * @return List<SimplePageComment> - los comments.
	 */
	public List<SimplePageComment> findCommentsOnItemsByAuthor(List<Long> commentItemIds, String author);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param commentWidgetId
	 * @param author
	 * @return List<SimplePageComment> - los comments.
	 */
	public List<SimplePageComment> findCommentsOnItemByAuthor(long commentWidgetId, String author);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param pageId
	 * @param author
	 * @return List<SimplePageComment> - los comments.
	 */
	public List<SimplePageComment> findCommentsOnPageByAuthor(long pageId, String author);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param commentId
	 * @return SimplePageComment - un SimplePageComment.
	 */
	public SimplePageComment findCommentById(long commentId);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param commentUUID
	 * @return SimplePageComment - un SimplePageComment.
	 */
	public SimplePageComment findCommentByUUID(String commentUUID);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param sakaiId
	 * @return SimplePageItem - un SimplePageItem.
	 */
	public SimplePageItem findCommentsToolBySakaiId(String sakaiId);

    // this is a generic one. Only use it for nearly unique sakaiids.
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param sakaiId
	 * @return List<SimplePageItem> - los SimplePageItem.
	 */
	public List<SimplePageItem> findItemsBySakaiId(String sakaiId);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @param owner
	 * @return SimpleStudentPage - un SimpleStudentPage.
	 */
	public SimpleStudentPage findStudentPage(long itemId, String owner);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param id
	 * @return SimpleStudentPage - un SimpleStudentPage.
	 */
	public SimpleStudentPage findStudentPage(long id);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param pageId
	 * @return SimpleStudentPage - un SimpleStudentPage.
	 */
	public SimpleStudentPage findStudentPageByPageId(long pageId);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @return List<SimpleStudentPage> - los SimpleStudentPage.
	 */
	public List<SimpleStudentPage> findStudentPages(long itemId);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Finds the SimplePageItem based on the pageId of a page created
	 * in the Student Content tool."
	 * @param pageId ID of the student SimplePage object
	 * @return SimplePageItem of the collection that this page belongs to.
	 */
	public SimplePageItem findItemFromStudentPage(long pageId);
	

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Find the item corresponding to a top level page. the page id is
     stored as the sakaiId of the item, which is the reason for the method name
     of course the String argument is just a long converted to a string."
	 * @param id
	 * @return SimplePageItem - un SimplePageItem.
	 */
	public SimplePageItem findTopLevelPageItemBySakaiId(String id);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Find all items with given page ID."
	 * @param id
	 * @return List<SimplePageItem> - los SimplePageItem.
	 */
	public List<SimplePageItem> findPageItemsBySakaiId(String id);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Find resource items with access control involving specified sakaiid."
	 * @param id
	 * @param siteid
	 * @return List - los recursos.
	 */
	public List findControlledResourcesBySakaiId(String id, String siteid);

    // basically, this is the Hibernate save. It works with any of our object types.
    // Checks for canEditPage, Except for SimplePageLog, where the code is assumed to 
    //   only write things it's allowed to. NB the limitation of canEditPage. You had
    //   better be updating an item or page in the current site, or canEditPage will give
    //   the wrong answer.
    // Also generates events showing the update.
    // elist is a list where saveItem will add error messages, nowriteerr is the message to use if
    //   the user doesn't have write permission. See saveitem in SimplePageBean for why we need
    //   to use this convoluted approach to getting back errors
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param o
	 * @param elist
	 * @param nowriteerr
	 * @param requiresEditPermission
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean saveItem(Object o, List<String> elist, String nowriteerr, boolean requiresEditPermission);

    // just do the save, no permission checking and no logging
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param o
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean quickSaveItem(Object o);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param o
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean deleteItem(Object o);

    // see saveItem for details and caveats, same function except update instead of save
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param o
	 * @param elist
	 * @param nowriteerr
	 * @param requiresEditPermission
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean update(Object o, List<String> elist, String nowriteerr, boolean requiresEditPermission);

    // version without permission checking and logging
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param o
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean quickUpdate(Object o);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param toolId
	 * @return Long - el pageId.
	 */
	public Long getTopLevelPageId(String toolId);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param pageId
	 * @return SimplePage - una SimplePage.
	 */
	public SimplePage getPage(long pageId);

    // list of all pages in the site, not just top level
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param siteId
	 * @return List<SimplePage> - las SimplePage.
	 */
	public List<SimplePage> getSitePages(String siteId);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "studentPageId is only to be used if it is a student-made page.  It corresponds to the <b>pageId</b>
	 * found in either lesson_builder_student_pages or lesson_builder_pages.
	 * 
	 * There should only be one log entry for each combination."
	 * @param userId
	 * @param itemId
	 * @param studentPageId
	 * @return SimplePageLogEntry - un SimplePageLogEntry.
	 */
	public SimplePageLogEntry getLogEntry(String userId, long itemId, Long studentPageId);
	
    // includes the dummy entries for preauthoized pages, but that's OK
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param pageId
	 * @param userId
	 * @param owner
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean isPageVisited(long pageId, String userId, String owner);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @param userId
	 * @return
	 */
	public List<SimplePageLogEntry> getStudentPageLogEntries(long itemId, String userId);

    // users with log entries showing item complete
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @return
	 */
	public List<String> findUserWithCompletePages(Long itemId);

    // find group controlling a given item. Note that the argument is the
    // sakaiId, not the item ID. So if the same assignment, etc., appears
    // on several pages, the same group controls all of the appearances
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @return
	 */
	public SimplePageGroup findGroup(String itemId);

    // constructors, so code doesn't have to use the Impl's directly
    public SimplePage makePage(String toolId, String siteId, String title, Long parent, Long topParent);

    /**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param id
     * @param pageId
     * @param sequence
     * @param type
     * @param sakaiId
     * @param name
     * @return
     */
    public SimplePageItem makeItem(long id, long pageId, int sequence, int type, String sakaiId, String name);

    /**
     * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param pageId
     * @param sequence
     * @param type
     * @param sakaiId
     * @param name
     * @return
     */
    public SimplePageItem makeItem(long pageId, int sequence, int type, String sakaiId, String name);

    /**
     * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param itemId
     * @param groupId
     * @param groups
     * @param siteId
     * @return
     */
    public SimplePageGroup makeGroup(String itemId, String groupId, String groups, String siteId);

    /**
     * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param userId
     * @param itemId
     * @param studentPageId
     * @return
     */
    public SimplePageLogEntry makeLogEntry(String userId, long itemId, Long studentPageId);
    
    /**
     * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param itemId
     * @param pageId
     * @param author
     * @param comment
     * @param UUID
     * @param html
     * @return
     */
    public SimplePageComment makeComment(long itemId, long pageId, String author, String comment, String UUID, boolean html);

    /**
     * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param itemId
     * @param pageId
     * @param title
     * @param author
     * @param groupOwned
     * @return
     */
    public SimpleStudentPage makeStudentPage(long itemId, long pageId, String title, String author, boolean groupOwned);
    
    /**
     * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param old
     * @return
     */
    public SimplePageItem copyItem(SimplePageItem old);

    /**
     * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param gradebookUid
     * @return
     */
    public List<SimplePageItem>findGradebookItems(String gradebookUid);

    // items in lesson_builder_groups for specified site, map of itemId to groups
    /**
     * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
     * @param siteId
     * @return
     */
    public Map<String,String> getExternalAssigns(String siteId);
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
    public Actividad makeActividad(Tipo tipo, Item item, String name, String description, int nR, int dedicacion, String nivel);
    /**
	 * Crea una nueva actividad a partir de los par&aacutemetros recibidos.
	 * @param item
	 * @param name
	 * @param nR
	 * @param dedicacion
	 * @param nivel
	 * @return Actividad - la nueva actividad creada.
	 */
	public Actividad makeActividad(Item item, String name, int nR, int dedicacion, String nivel);
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
	 * Retorna el identificador de la &uacuteltima actividad guardada en la base de datos.
	 * @param nombre
	 * @return Integer - el identificador.
	 */
	public int getUltimaActividad(String nombre);
	/**
	 * Retorna el identificador de la &uacuteltima actividad guardada en la base de datos.
	 * @return Integer - el identificador.
	 */
	public int getUltimaActividad();
	
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
	public int getEditarDominioAshyi();
		
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
	public List<Caracteristica> getCaracteristica(int id);
	/**
	 * Retorna un tipo.
	 * @param id el identificador del tipo.
	 * @return List<Tipo> - la lista.
	 */
	public List<Tipo> getTipo(int id);
	
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
	public Actividad getactividad(String titulo, int nivelRecursividad);
	
	/**
	 * Retorna el identificador del &uacuteltimo objetivo guardado en la base de datos.
	 * @param nombre nombre del objetivo.
	 * @return Integer - el identificador del objetivo.
	 */
	public int getUltimoObjetivo(String nombre);

	/**
	 * Retorna los objetivos de una actividad a partir de su nombre y nivel de recursividad.
	 * @param nivelRecursividad nivel de recursividad de la actividad.
	 * @param nombre nombre de la actividad.
	 * @return String[] - arreglo con los nombres de los objetivos.
	 */
	public int getObjetivoActividad(String nombre, int nR);
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
	public Actividad getActividad(int idActividad);

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
	public int getUltimoItem();
	
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
	public Item getItemId(int id);

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
	public List<DependenciaActividad> getDependenciaActividad(int act, int actDependiente);
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
	public List<ActividadTieneActividad> getActividadTieneActividad(int id, int idSiguienteNivel);
	/**
	 * Retorna el estilo de la actividad hijo en una actividadTieneActividad a partir del identificador de la actividad padre y la actividad de siguiente nivel.
	 * @param id
	 * @return List<String> - la actividadTieneActividad.
	 */
	public List<String> getEstiloSiguienteNivelActTieneAct(int id, int idSiguienteNivel);
	/**
	 * Retorna los identificadores de las actividadTieneActividad a partir del identificador de la actividad padre.
	 * @param id
	 * @return List<String> - los identificadores.
	 */
	public List<String> getActividadTieneActividadString(int id);
	
	/**
	 * Retorna una actividad a partir de su identificador.
	 * @param id
	 * @return List<Actividad> - la actividad.
	 */
	public List<Actividad> getActividades(int id);
	
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
	public List<RecursosActividad> getRecursosActividad(int nivelRecursividad, String nombre);
	/**
	 * Retorna un objetivo a partir de su nombre.
	 * @param objetivo
	 * @return Objetivo - el objetivo.
	 */
	public Objetivo getObjetivo(String objetivo);
	/**
	 * Retorna el recurso de una actividad a partir delnivel de recursividad y el identificador del recurso.
	 * @param actividad
	 * @param idRecursoSakai
	 * @return RecursosActividad - el recurso.
	 */
	public Actividad getActividadRecurso(int actividad, String idRecursoSakai);

	/**
	 * Retorna una actividad completa a partir de su identificador.
	 * @param id
	 * @return Actividad - la actividad.
	 */
	public Actividad getActividadCompleta(int id);
	/**
	 * Retorna las caracter&iacutesticas de una actividad a partir de su identificador.
	 * @param idActividad
	 * @return List<CaracteristicaActividad> - las caracter&iacutesticas.
	 */
	public List<CaracteristicaActividad> getCaracteristicasActividad(int idActividad);
	/**
	 * Retorna los objetivos de una actividad a partir de su identificador.
	 * @param idActividad
	 * @return List<ObjetivosActividad> - los objetivos.
	 */
	public List<ObjetivosActividad> getObjetivosActividad(int idActividad);
	
	/**
	 * Retorna los objetivos de una actividad a partir de su nombre y nivel de recursividad.
	 * @param nivelRecursividad nivel de recursividad de la actividad.
	 * @param nombre nombre de la actividad.
	 * @return String[] - arreglo con los nombres de los objetivos.
	 */
	public String[] getObjetivosActividad(Integer nivelRecursividad, String nombre);
	
	/**
	 * Retorna los recursos de una actividad a partir de su identificador.
	 * @param idActividad
	 * @return List<RecursosActividad> - los recursos.
	 */
	public List<RecursosActividad> getRecursosActividad(int idActividad);
	/**
	 * Retorna la actividad padre en una actividadTieneActividad a partir del identificador de la actividad de siguiente nivel.
	 * @param id
	 * @return Actividad - la actividad padre.
	 */
	public Actividad getActividadAltoNivel(int id);

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
	public Usuario getUsuario(int idUsuario);

	/**
	 * Retorna el identificador del &uacuteltimo usuario guardado en la base de datos de Ashyi a partir de su identificador de Sakai.
	 * @param idUsuarioSakai
	 * @return Integer - el identificador del usuario.
	 */
	public int getUltimoUsuario(String idUsuarioSakai);
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
	public List<CaracteristicasUsuario> getEstilosUsuario(int idUsuario);

	/**
	 * Retorna las actividades de un usuario a partir de su identificador.
	 * @param userId
	 * @return List<ActividadesUsuario> - las actividades.
	 */
	public List<ActividadesUsuario> getActividadesUsuario(int userId);
	/**
	 * Retorna un usuario a partir de su identificador.
	 * @param userId
	 * @return Usuario - el usuario.
	 */
	public Usuario getUsuario(String userId);

	/**
	 * Retorna los SimplePageItems
	 * @return List<SimplePageItem> - los items.
	 */
	public List<SimplePageItem> getItems();
	/**
	 * Retorna los ItemPlan activos.
	 * @return List<ItemPlan> - los itemPlans activos.
	 */
	public List<ItemPlan> getItemsActivos(Usuario usuario, String nombreActividad);

	/**
	 * Retorna un itemSakai por el identificador del itemPlan.
	 * @param idItemPlan
	 * @return SimplePageItem - el itemSakai.
	 */
	public SimplePageItem getItemSakai(ItemPlan idItemPlan);
	/**
	 * Retorna un itemPlan a partir de su identificador.
	 * @param id
	 * @return ItemPlan - el itemPlan.
	 */
	public ItemPlan getItemPlan(int id);
	
	/**
	 * Retorna los identificadores de los itemPlans de un grafo por su id.
	 * @param idGrafo
	 * @return List<Integer> - los identificadores.
	 */
	public List<Integer> getIdItemsPlan(int idGrafo);
	/**
	 * Retorna los identificadores de los item de un grafo por su id.
	 * @param idGrafo
	 * @return List<Integer> - los identificadores.
	 */
	public List<Integer> getIdItemsGrafo(int idGrafo);
	/**
	 * Retorna los itemsUsuario por el itemPlan y el identificador del usuario.
	 * @param itemPlan
	 * @param userId
	 * @return ItemsUsuario - el itemUsuario.
	 */
	public ItemsUsuario getItemsUsuario(ItemPlan itemPlan, String userId);
	/**
	 * Retorna los itemsUsuario por el identificador del itemPlan y el identificador del usuario.
	 * @param idIitemPlan
	 * @param userId
	 * @return ItemsUsuario - el itemUsuario.
	 */
	public ItemsUsuario getItemsUsuarioPorIds(int idIitemPlan, int idUsuario);

	/**
	 * Retorna los itemPlans activos por actividad
	 * @param nombreActividad
	 * @param nR - nivel de recursividad.
	 * @return List<ItemPlan> - los itemPlans.
	 */
	public List<ItemPlan> getItemsActivosActividad(String nombreActividad, int nR);

	/**
	 * Retorna los usuarios Ashyi.
	 * @return List<Usuario> - los usuarios.
	 */
	public List<Usuario> getUsuariosAshyi();
	/**
	 * Retorna los usuarios del sitio por su identificador.
	 * @param idSite
	 * @return List<User> - los usuarios.
	 */
	public List<User> getUsuariosSite(String idSite);
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param idUsuarioSakai
	 * @param idSite
	 * @return
	 */
	public String getUserSite(String idUsuarioSakai, String idSite);

	/**
	 * Guarda un itemUsuario en la base de datos.
	 * @param i
	 * @param elist
	 * @param message
	 * @param requiresEditPermission
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean saveItemUsuario(Object i, List<String> elist, String message, boolean requiresEditPermission);
	/**
	 * Retorna una caracter&iacutestica por su nombre.
	 * @param nombre - el nombre de la caracter&iacutestica.
	 * @return Caracteristica - la caracter&iacutestica.
	 */
	public Caracteristica getCaracteristica(String nombre);

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return SimplePageItem - el item.
	 */
	public SimplePageItem getItemSakaiInicial();
	
	/**
	 * Retorna las actividadTieneActividad a partir del identificador de la actividad padre.
	 * @param id
	 * @return List<ActividadTieneActividad> - las actividadTieneActividad.
	 */
	public List<ActividadTieneActividad> getActividadTieneActividades(Integer id);
	
	/**
	 * Retirma el item de lesson builder, según su página
	 * @param page
	 * @return
	 */
	public SimplePageItem getItemByPage(SimplePage page);
	
}
