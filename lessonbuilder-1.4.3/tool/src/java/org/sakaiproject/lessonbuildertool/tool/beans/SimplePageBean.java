
/**********************************************************************************
 * $URL: $
 * $Id: $
 ***********************************************************************************
 *
 * Author: Eric Jeney, jeney@rutgers.edu
 * The original author was Joshua Ryan josh@asu.edu. However little of that code is actually left
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

package org.sakaiproject.lessonbuildertool.tool.beans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.authz.cover.AuthzGroupService;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.content.api.ContentCollectionEdit;
import org.sakaiproject.content.api.ContentEntity;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.ContentResourceEdit;
import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.content.api.GroupAwareEntity.AccessMode;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.event.cover.EventTrackingService;
import org.sakaiproject.event.cover.NotificationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.id.cover.IdManager;
import org.sakaiproject.lessonbuildertool.ActividadTieneActividadImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicaActividadImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicaImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicasRecursoImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicasUsuarioImpl;
import org.sakaiproject.lessonbuildertool.DependenciaActividadImpl;
import org.sakaiproject.lessonbuildertool.ItemImpl;
import org.sakaiproject.lessonbuildertool.ObjetivoImpl;
import org.sakaiproject.lessonbuildertool.ObjetivosActividadImpl;
import org.sakaiproject.lessonbuildertool.RecursoImpl;
import org.sakaiproject.lessonbuildertool.RecursosActividadImpl;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageComment;
import org.sakaiproject.lessonbuildertool.SimplePageGroup;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.SimplePageItemImpl;
import org.sakaiproject.lessonbuildertool.SimplePageLogEntry;
import org.sakaiproject.lessonbuildertool.SimpleStudentPage;
import org.sakaiproject.lessonbuildertool.cc.CartridgeLoader;
import org.sakaiproject.lessonbuildertool.cc.Parser;
import org.sakaiproject.lessonbuildertool.cc.PrintHandler;
import org.sakaiproject.lessonbuildertool.cc.ZipLoader;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneActividad;
import org.sakaiproject.lessonbuildertool.model.ActividadesUsuario;
import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaActividad;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario;
import org.sakaiproject.lessonbuildertool.model.Control;
import org.sakaiproject.lessonbuildertool.model.DependenciaActividad;
import org.sakaiproject.lessonbuildertool.model.Item;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.ItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.Objetivo;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.Recurso;
import org.sakaiproject.lessonbuildertool.model.RecursosActividad;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.model.Tipo;
import org.sakaiproject.lessonbuildertool.model.Usuario;
import org.sakaiproject.lessonbuildertool.service.BltiInterface;
import org.sakaiproject.lessonbuildertool.service.GradebookIfc;
import org.sakaiproject.lessonbuildertool.service.GroupPermissionsService;
import org.sakaiproject.lessonbuildertool.service.LessonBuilderEntityProducer;
import org.sakaiproject.lessonbuildertool.service.LessonEntity;
import org.sakaiproject.lessonbuildertool.service.LessonSubmission;
import org.sakaiproject.lessonbuildertool.tool.producers.CaracteristicaProducer;
import org.sakaiproject.lessonbuildertool.tool.producers.ShowItemProducer;
import org.sakaiproject.lessonbuildertool.tool.producers.ShowPageProducer;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SitePage;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.time.cover.TimeService;
import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.util.FormattedText;
import org.sakaiproject.util.ResourceLoader;
import org.sakaiproject.util.Validator;
import org.springframework.web.multipart.MultipartFile;

import pt.ipb.estig.chaea.dao.ChaeaDaoImpl;
import pt.ipb.estig.chaea.logic.ChaeaLogicImpl;
import pt.ipb.estig.chaea.model.Questao;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import co.edu.javeriana.ashyi.ASHYIControlador.BeanASHYI;

/**
 * Backing bean for Simple pages
 * 
 * @author Eric Jeney <jeney@rutgers.edu>
 * @author Joshua Ryan josh@asu.edu alt^I
 */

// This bean has two related but somewhat separate uses:
// 1) It keeps common data for the producers and other code. In that use the lifetime of the bean is while
//    generating a single page. The bean has common application logic. The producers are pretty much just UI.
//    The DAO is low-level data access. This is everything else. The producers call this bean, and not the 
//    DAO directly. This layer sticks caches on top of the data access, and provides more complex logic. Security
//    is primarily in the DAO, but the DAO only checks permissions. We have to make sure we only access pages
//    and items in our site
//       Most of the caches are local. Since this bean is request-scope they are recreated for each request.
//    Thus we don't have to worry about timing out the entries.
// 2) It is used by RSF to access data. Normally the bean is associated with a specific page. However the 
//    UI often has to update attributes of a specific item. For that use, there are some item-specific variables
//    in the bean. They are only meaningful during item operations, when itemId will show which item is involved.
// While the bean is used by all the producers, the caching was designed specifically for ShowPageProducer.
// That's because it is used a lot more often than the others. ShowPageProducer should do all data access through
// the methods here that cache. There is also caching by hibernate. However this code is cheaper, partly because
// it doesn't have to do synchronization (since it applies just to processing one transaction).

/**
 * Clase que realiza las operaciones de la plataforma que tienen que ver con las entidades de Ashyi y Sakai.
 * @author ashiy
 *
 */
/**
 * @author ashiy
 *
 */
public class SimplePageBean {
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static Log log = LogFactory.getLog(SimplePageBean.class);

	public enum Status {
		NOT_REQUIRED, REQUIRED, DISABLED, COMPLETED, FAILED
	}

	// from ResourceProperites. This isn't in 2.7.1, so define it here. Let's hope it doesn't change...
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String PROP_ALLOW_INLINE = "SAKAI:allow_inline";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final Pattern YOUTUBE_PATTERN = Pattern.compile("v[=/_]([\\w-]{11}([\\?\\&][\\w\\.\\=\\&]*)?)");
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final Pattern YOUTUBE2_PATTERN = Pattern.compile("embed/([\\w-]{11}([\\?\\&][\\w\\.\\=\\&]*)?)");
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final Pattern SHORT_YOUTUBE_PATTERN = Pattern.compile("([\\w-]{11}([\\?\\&][\\w\\.\\=\\&]*)?)");
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String GRADES[] = { "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "E", "F" };
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String FILTERHTML = "lessonbuilder.filterhtml";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String LESSONBUILDER_ITEMID = "lessonbuilder.itemid";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String LESSONBUILDER_PATH = "lessonbuilder.path";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String LESSONBUILDER_BACKPATH = "lessonbuilder.backpath";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String LESSONBUILDER_ID = "sakai.lessonbuildertool";
	//	public static String IdItemPAshyi = "sakai.IdItemPAshyi";
	//	public static String IdItemUAshyi = "sakai.IdItemUAshyi";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static String PAGE = "simplepage.page";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static String SITE_UPD = "site.upd";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String contents = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String pageTitle = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String newPageTitle = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String subpageTitle = null;
	/**
	 * Objetivo unidad did&aacutectica.
	 */
	private String subpageGoal = null;
	/**
	 * Objetivo unidad did&aacutectica.
	 */
	private String subpageGoalUnidad = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean subpageNext = false;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean subpageButton = false;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private List<Long> currentPath = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Set<Long> allowedPages = null;    
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Site currentSite = null; // cache, can be null; used by getCurrentSite
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private List<GroupEntry> currentGroups = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Set<String> myGroups = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String filterHtml = ServerConfigurationService.getString(FILTERHTML);
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String selectedAssignment = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String selectedBlti = null;
	/**
	 * Actividad seleccionada.
	 */
	public String selectedActividad = null;//actividad

	// generic entity stuff. selectedEntity is the string
	// coming from the picker. We'll use the same variable for any entity type
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String selectedEntity = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String[] selectedEntities = new String[] {};
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String[] selectedGroups = new String[] {};
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String selectedQuiz = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public long removeId = 0;
	/**
	 * P&aacutegina actual.
	 */
	private SimplePage currentPage;
	/**
	 * Identificador p&aacutegina actual.
	 */
	private Long currentPageId = null;
	/**
	 * Identificador item p&aacutegina actual.
	 */
	private Long currentPageItemId = null;
	/**
	 * Identificador usuario actual.
	 */
	private String currentUserId = null;
	/**
	 * Nombre usuario actual.
	 */
	private String currentUserNameId = null;
	/**
	 * Identificador p&aacutegina anterior.
	 */
	private long previousPageId = -1;

	// Item-specific variables. These are set by setters which are called
	// by the various edit dialogs. So they're basically inputs to the
	// methods used to make changes to items. The way it works is that
	// when the user submits the form, RSF takes all the form variables,
	// calls setters for each field, and then calls the method specified
	// by the form. The setters set these variables

	/**
	 * Identificador item actual.
	 */
	public Long itemId = null;
	/**
	 * Es multimedia o no.
	 */
	public boolean isMultimedia = false;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String commentsId;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public boolean anonymous;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String comment;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String formattedComment;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String editId;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public boolean graded, sGraded;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String maxPoints, sMaxPoints;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public boolean comments;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public boolean forcedAnon;
	/**
	 * Es website o no.
	 */
	public boolean isWebsite = false;
	/**
	 * URL de la p&aacutegina.
	 */
	private String linkUrl;
	/**
	 * Alto de la pagina.
	 */
	private String height;
	/**
	 * Ancho de la p&aacutegina.
	 */
	private String width;
	/**
	 * Descripci&oacuten de la p&aacutegina.
	 */
	private String description;
	/**
	 * Nombre de la p&aacutegina.
	 */
	private String name;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean required;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean subrequirement;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean prerequisite;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean newWindow;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String dropDown;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String points;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String mimetype;
	// for BLTI, values window, inline, and null for in a new page with navigation
	// but sameWindow should also be set properly, based on the format
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String format;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String numberOfPages;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean copyPage;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String alt = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String order = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String youtubeURL;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String mmUrl;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private long youtubeId;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean hidePage;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Date releaseDate;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean hasReleaseDate;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String redirectSendingPage = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String redirectViewId = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String quiztool = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String topictool = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String assigntool = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean importtop = false;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Integer editPrivs = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String currentSiteId = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public Map<String, MultipartFile> multipartMap;

	// Caches

	// The following caches are used only during a single display of the page. I believe they
	// are so transient that we don't have to worry about synchronizing them or keeping them up to date.
	// Because the producer code tends to deal with items and even item ID's, it doesn't keep objects such
	// as Assignment or PublishedAssessment around. It calls functions here to worry about those. If we
	// don't cache, we'll be doing database lookups a lot. The worst is the code to see whether an item
	// is available. Because it checks all items above, we'd end up order N**2 in the number of items on the
	// page in database queries. It doesn't appear that assignments and assessments do any caching of their
	// own, but hibernate as we use it does.
	//   Normal code shouldn't use the caches directly, but should call something like getAssignment here,
	// which checks the cache and if necessary calls the real getAssignment. I've chosen to do caching on
	// this level, and let the DAO be actual database access. I've really only optimized what is used by
	// ShowPageProducer, as that is used every time a page is shown. Things used when you add or change
	// an item aren't as critical.
	//   If anyone is doing serious work on the code, I recommend creating an Item class that encapsulates
	// all the stuff associated with items. Then the producer would manipulate items. Thus the things in
	// these caches would be held in the Items.
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<Long, SimplePageItem> itemCache = new HashMap<Long, SimplePageItem> ();
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<Long, SimplePage> pageCache = new HashMap<Long, SimplePage> ();
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<Long, List<SimplePageItem>> itemsCache = new HashMap<Long, List<SimplePageItem>> ();
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<String, SimplePageLogEntry> logCache = new HashMap<String, SimplePageLogEntry>();
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<Long, Boolean> completeCache = new HashMap<Long, Boolean>();
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<Long, Boolean> visibleCache = new HashMap<Long, Boolean>();
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	// this one needs to be global
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static Cache groupCache = null;   // itemId => grouplist
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static Cache resourceCache = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	protected static final int DEFAULT_EXPIRATION = 10 * 60;
	/**
	 * Nombre de la actividad.
	 */
	private String nomberA = null;
	/**
	 * Tipo de la actividad.
	 */
	private String tipoA = null;
	/**
	 * Recurso de la actividad.
	 */
	private String recursoA = null;
	/**
	 * Fecha inicial de la actividad.
	 */
	private String inicialF = null;
	/**
	 * Fecha final de la actividad.
	 */
	private String finalF = null;
	/**
	 * Descripci&oacuten de la actividad.
	 */
	private String descripcionA = null;
	/**
	 * Objetivo de la actividad.
	 */
	private String objetivoA = null;
	/**
	 * Dedicaci&oacuten de la actividad.
	 */
	private String dedicacionActividad = "1";

	/**
	 * Caracter&iacutestica seleccionada para una actividad.
	 */
	private String caracteristicaSeleccionada = null;
	/**
	 * Tipo seleccionado para una actividad.
	 */
	private String tipoSeleccionado = null;
	/**
	 * Cadena que representa un diagrama de flujo.
	 */
	private String newFlowChart = null;
	/**
	 * Cadena que representa el grafo actual de la unidad did&aacutectica.
	 */
	private String existingFlowChart = null;
	/**
	 * Cadena que representa el conjunto de actividades de la unidad did&aacutectica.
	 */
	private String existingActivities = null;
	/**
	 * Representa la &uacuteltima conexi&oacuten.
	 */
	private String lastConnection = null;
	/**
	 * Representa el estilo de una actividad al presentarla gr&aacuteficamente.
	 */
	private String activitiesStyle = null;

	/**
	 * Nombre de una caracter&iacutestica.
	 */
	private String nombreC = null;
	/**
	 * Tipo de una caracter&iacutestica.
	 */
	private String tipoC = null;
	/**
	 * Item de una caracter&iacutestica.
	 */
	private String itemC = null;
	/**
	 * Test de una caracter&iacutestica.
	 */
	private String testC = null;
	/**
	 * Nombre del contexto.
	 */
	private String nombreCx = null;
	/**
	 * Tipo del contexto.
	 */
	private String tipoCx = null;

	/**
	 * Lista con los tipos de caracter&iacutesticas.
	 */
	private List<Item> tipoCaracteristicas = new ArrayList<Item>();
	/**
	 * Lista con los tipos de contexto.
	 */
	private List<Item> tipoContexto = new ArrayList<Item>();
	/**
	 * Lista con los tipos de persona.
	 */
	private List<Item> tipoPersona = new ArrayList<Item>();
	/**
	 * Lista con los tipos de actividad.
	 */
	private List<Tipo> tipoActividad = new ArrayList<Tipo>();
	/**
	 * Tipo de unidad did&aacutectica.
	 */
	private int tipoUnidad = 0;
	/**
	 * Lista con los items de caracter&iacutesticas.
	 */
	private List<Item> itemsCaracterisiticas = new ArrayList<Item>();
	/**
	 * Lista con los tipos de tipo.
	 */
	private List<Tipo> tiposTipo = new ArrayList<Tipo>();
	/**
	 * Lista de habilidades.
	 */
	private List<Caracteristica> habilidad = new ArrayList<Caracteristica>();
	/**
	 * Lista de competencias.
	 */
	private List<Caracteristica> competencia = new ArrayList<Caracteristica>();
	/**
	 * Lista de personalidades.
	 */
	private List<Caracteristica> personalidad = new ArrayList<Caracteristica>();
	/**
	 * Lista de situaciones de aprendizaje.
	 */
	private List<Caracteristica> sA = new ArrayList<Caracteristica>();
	/**
	 * Lista de estilos.
	 */
	private List<Caracteristica> estilo = new ArrayList<Caracteristica>();
	/**
	 * Lista de tipos.
	 */
	private List<Item> tipo = new ArrayList<Item>();

	/**
	 * Lista de contextos.
	 */
	private List<Caracteristica> contexto = new ArrayList<Caracteristica>();
	/**
	 * Lista de objetivos.
	 */
	private String[] objetivos;

	/**
	 * Nivel de estilo de aprendizaje.
	 */
	private String nivele = "0";
	/**
	 * Nivel de habilidad.
	 */
	private String nivelh = "0";
	/**
	 * Nivel de competencia.
	 */
	private String nivelc = "0";
	/**
	 * Nivel de personalidad.
	 */
	private String nivelp = "0";
	/**
	 * Nivel de situaci&oacuten de aprendizaje.
	 */
	private String nivelsa = "0";
	/**
	 * Nivel de precondici&oacuten.
	 */
	private String nivelhpc = "0";
	/**
	 * Nivel de postcondici&oacuten.
	 */
	private String nivelcpc = "0";
	/**
	 * Habilidad seleccionada como postcondici&oacuten.
	 */
	private String habilidadSPC;
	/**
	 * Competencia seleccionada como postcondici&oacuten.
	 */
	private String competenciaSPC;
	/**
	 * Habilidad seleccionada.
	 */
	private String habilidadS = null;
	/**
	 * Competencia seleccionada.
	 */
	private String competenciaS = null;
	/**
	 * Personalidad seleccionada.
	 */
	private String personalidadS = null;
	/**
	 * Situaci&oacuten de aprendizaje seleccionada.
	 */
	private String saS = null;
	/**
	 * Estilo seleccionado.
	 */
	private String estiloS = null;
	/**
	 * Contexto seleccionado.
	 */
	private String contextoS = null;

	/**
	 * Objetivos seleccionados.
	 */
	public String[] objetivosSeleccionados = new String[] {};
	/**
	 * Items seleccionados.
	 */
	public String[] itemsSeleccionados = new String[] {};
	/**
	 * Niveles de habilidades seleccionados.
	 */
	public String[] nivelesHSeleccionados = new String[] {};
	/**
	 * Niveles de competencias seleccionados.
	 */
	public String[] nivelesCSeleccionados = new String[] {};
	/**
	 * Habilidades seleccionadas.
	 */
	public String[] habilidadesSeleccionadas = new String[] {};
	/**
	 * Competencias seleccionadas.
	 */
	public String[] competenciasSeleccionadas = new String[] {};
	/**
	 * Personalidades seleccionadas.
	 */
	public String[] personalidadesSeleccionadas = new String[] {};
	/**
	 * Situaciones de aprendizaje seleccionadas.
	 */
	public String[] saSeleccionadas = new String[] {};
	/**
	 * Estilos seleccionados.
	 */
	public String[] estilosSeleccionados = new String[] {};
	/**
	 * Contextos seleccionados.
	 */
	public String[] contextosSeleccionados = new String[] {};
	/**
	 * Habilidades seleccionadas como postcondiciones.
	 */
	public String[] habilidadesPCSeleccionadas = new String[] {};
	/**
	 * Competencias seleccionadas como postcondiciones.
	 */
	public String[] competenciasPCSeleccionadas = new String[] {};
	/**
	 * Si la actividad es inicial, final o intermedia.
	 */
	public String opcionIniciaFinal = "";

	/**
	 * Objetivo del curso.
	 */
	private String objetivoCurso;
	/**
	 * Recurso del curso.
	 */
	private String recursoCurso;
	/**
	 * Tipo de recurso.
	 */
	private String tipoRecurso;
	/**
	 * Dedicaci&oacuten de la unidad did&aacutectica.
	 */
	private String subpageDedicacion = "1";
	/**
	 * Auxiliar para edici&oacuten.
	 */
	private int yaEditar = 0;
	/**
	 * Comentario para seleccionar una opci&oacuten.
	 */
	public static String cadenaOpciones = "Seleccione una opcion";
	/**
	 * Nombre de tipo.
	 */
	private String nombreTipo;
	/**
	 * Tipo de tipo.
	 */
	private String tipoTipo;
	/**
	 * Tipo de naturaleza.
	 */
	private String tipoNaturaleza;
	/**
	 * Tipo de formato.
	 */
	private String tipoFormato;
	/**
	 * Tipo de nivel.
	 */
	private String tipoNivel;
	/**
	 * Tipo de tipoAcceso.
	 */
	private String tipoTipoAcceso;
	/**
	 * T&iacutetulo del tema.
	 */
	private String temaTitle;

	/**
	 * Identificador auxiliar de actividad.
	 */
	private String idC;

	/**
	 * Auxiliar para llenar la p&aacutegina de una actividad.
	 */
	private String tofillAct;

	/**
	 * Par&aacutemetros de vista auxiliares.
	 */
	private GeneralViewParameters params;

	/**
	 * Nivel de las actividades.
	 */
	private String nivelActividades = "Universitario";
	/**
	 * Atributo de BESAKISS Contexto inicial.
	 */
	private Context initCtx;
	/**
	 * Atributo de BESAKISS.
	 */
	private Context envCtx;
	// TODO contexto normal
	/**
	 * Atributo de BESAKISS Bean de BESA.
	 */
	private BeanASHYI bean;

	/**
	 * Atributo de BESAKISS Agente interfaz.
	 */
	private String agenteInterface = "_";

	//chaea
	/**
	 * Atributo del test Chaea estilo activo.
	 */
	int activo;
	/**
	 * Atributo del test Chaea estilo pragm&aacutetico.
	 */
	int prag;
	/**
	 * Atributo del test Chaea estilo reflexivo.
	 */
	int reflexivo;
	/**
	 * Atributo del test Chaea estilo te&oacuterico.
	 */
	int teorico;

	/**
	 * Identificador de itemPlan Ashyi.
	 */
	public static int IdItemPAshyi = -1;
	/**
	 * Identificador de itemUsuario Ashyi.
	 */
	public static int IdItemUAshyi = -1;
	/**
	 * Identificador de la actividad.
	 */
	public static int idActividad = -1;

	/**
	 * Retorna el tipo de unidad.
	 * 
	 * @return tipoUnidad
	 */
	public int getTipoUnidad() {
		return tipoUnidad;
	}	
	/**
	 * Asigna el tipo de unidad.
	 * 
	 * @param tipoUnidad
	 */
	public void setTipoUnidad(int tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	/**
	 * Retorna el tipo de naturaleza.
	 * 
	 * @return tipoNaturaleza
	 */
	public String getTipoNaturaleza() {
		return tipoNaturaleza;
	}
	/**
	 * Asigna el tipo de naturaleza.
	 * 
	 * @param tipoNaturaleza
	 */
	public void setTipoNaturaleza(String tipoNaturaleza) {
		this.tipoNaturaleza = tipoNaturaleza;
	}
	/**
	 * Retorna el tipo de formato.
	 * 
	 * @return tipoFormato
	 */
	public String getTipoFormato() {
		return tipoFormato;
	}
	/**
	 * Asigna el tipo de formato.
	 * 
	 * @param tipoFormato
	 */
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	/**
	 * Retorna el tipo de nivel.
	 * 
	 * @return tipoNivel
	 */
	public String getTipoNivel() {
		return tipoNivel;
	}
	/**
	 * Asigna el tipo de nivel.
	 * 
	 * @param tipoNivel
	 */
	public void setTipoNivel(String tipoNivel) {
		this.tipoNivel = tipoNivel;
	}

	/**
	 * Retorna el tipo de tipoAcceso.
	 * 
	 * @return tipoTipoAcceso
	 */
	public String getTipoTipoAcceso() {
		return tipoTipoAcceso;
	}
	/**
	 * Asigna el tipo de tipoAcceso.
	 * 
	 * @param tipoTipoAcceso
	 */
	public void setTipoTipoAcceso(String tipoTipoAcceso) {
		this.tipoTipoAcceso = tipoTipoAcceso;
	}
	/**
	 * Retorna el nivelActividades.
	 * 
	 * @return nivelActividades
	 */
	public String getNivelActividades() {
		return nivelActividades;
	}
	/**
	 * Asigna el nivelActividades.
	 * 
	 * @param nivelActividades
	 */
	public void setNivelActividades(String nivelActividades) {
		this.nivelActividades = nivelActividades;
	}
	/**
	 * Retorna el tipo de recurso.
	 * 
	 * @return tipoRecurso
	 */
	public String getTipoRecurso() {
		return tipoRecurso;
	}
	/**
	 * Asigna el tipo de recurso.
	 * 
	 * @param tipoRecurso
	 */
	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	/**
	 * Retorna el agente de interfaz.
	 * 
	 * @return agenteInterface
	 */
	public String getAgenteInterface() {
		return agenteInterface;
	}
	/**
	 * Asigna el agente de interfaz.
	 * 
	 * @param agenteInterface
	 */
	public void setAgenteInterface(String agenteInterface) {
		this.agenteInterface = agenteInterface;
	}
	/**
	 * Retorna el contexto inicial.
	 * 
	 * @return envCtx el contexto inicial.
	 */
	public Context getInitCtx()
	{
		try {
			initCtx = new InitialContext();

			envCtx = (Context) initCtx.lookup("java:comp/env");

			return envCtx;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Retorna el bean de BESA.
	 * 
	 * @return bean el bean.
	 */
	public BeanASHYI getBeanBesa()
	{
		try {
			bean = (BeanASHYI) getInitCtx().lookup("bean/BeanBESAKISS");

			return bean;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retorna las personalidades seleccionadas.
	 * 
	 * @return personalidadesSeleccionadas
	 */
	public String[] getPersonalidadesSeleccionadas() {
		return personalidadesSeleccionadas;
	}
	/**
	 * Asigna las personalidades seleccionadas.
	 * 
	 * @param personalidadesSeleccionadas
	 */
	public void setPersonalidadesSeleccionadas(
			String[] personalidadesSeleccionadas) {
		this.personalidadesSeleccionadas = personalidadesSeleccionadas;
	}
	/**
	 * Retorna las situaciones de aprendizaje seleccionadas.
	 * 
	 * @return saSeleccionadas
	 */
	public String[] getSaSeleccionadas() {
		return saSeleccionadas;
	}
	/**
	 * Asigna las situaciones de aprendizaje seleccionadas.
	 * 
	 * @param saSeleccionadas
	 */
	public void setSaSeleccionadas(String[] saSeleccionadas) {
		this.saSeleccionadas = saSeleccionadas;
	}
	/**
	 * Retorna los estilos de aprendizaje seleccionados.
	 * 
	 * @return estilosSeleccionados
	 */
	public String[] getEstilosSeleccionados() {
		return estilosSeleccionados;
	}
	/**
	 * Asigna los estilos de aprendizaje seleccionados.
	 * 
	 * @param estilosSeleccionados
	 */
	public void setEstilosSeleccionados(String[] estilosSeleccionados) {
		this.estilosSeleccionados = estilosSeleccionados;
	}
	/**
	 * Retorna los contextos seleccionados.
	 * 
	 * @return contextosSeleccionados
	 */
	public String[] getContextosSeleccionados() {
		return contextosSeleccionados;
	}
	/**
	 * Asigna los contextos seleccionados.
	 * 
	 * @param contextosSeleccionados
	 */
	public void setContextosSeleccionados(String[] contextosSeleccionadss) {
		this.contextosSeleccionados = contextosSeleccionadss;
	}
	/**
	 * Retorna el recurso de la actividad.
	 * 
	 * @return recursoA
	 */
	public String getRecursoA() {
		return recursoA;
	}
	/**
	 * Asigna el recurso de la actividad.
	 * 
	 * @param recursoA
	 */
	public void setRecursoA(String recursoA) {
		this.recursoA = recursoA;
	}	
	/**
	 * Retorna el recurso del curso.
	 * 
	 * @return recursoCurso
	 */
	public String getRecursoCurso() {
		return recursoCurso;
	}
	/**
	 * Asigna el recurso del curso.
	 * 
	 * @param recursoCurso
	 */
	public void setRecursoCurso(String recursoCurso) {
		this.recursoCurso = recursoCurso;
	}
	/**
	 * Retorna la dedicaci&oacuten de la unidad did&aacutectica.
	 * 
	 * @return subpageDedicacion
	 */
	public String getSubpageDedicacion() {
		return subpageDedicacion;
	}
	/**
	 * Asigna la dedicaci&oacuten de la unidad did&aacutectica.
	 * 
	 * @param subpageDedicacion
	 */
	public void setSubpageDedicacion(String subpageDedicacion) {
		this.subpageDedicacion = subpageDedicacion;
	}
	/**
	 * Retorna la dedicaci&oacuten de la actividad.
	 * 
	 * @return dedicacionActividad
	 */
	public String getDedicacionActividad() {
		return dedicacionActividad;
	}
	/**
	 * Asigna la dedicaci&oacuten de la actividad.
	 * 
	 * @param dedicacionActividad
	 */
	public void setDedicacionActividad(String dedicacionActividad) {
		this.dedicacionActividad = dedicacionActividad;
	}
	/**
	 * Retorna si la actividad es inicial, intermedia o final.
	 * 
	 * @return opcionIniciaFinal
	 */
	public String getOpcionIniciaFinal() {
		return opcionIniciaFinal;
	}
	/**
	 * Asigna si la actividad es inicial, intermedia o final.
	 * 
	 * @param opcionIniciaFinal
	 */
	public void setOpcionIniciaFinal(String opcionIniciaFinal) {
		this.opcionIniciaFinal = opcionIniciaFinal;
	}
	/**
	 * Retorna las habilidades seleccionadas.
	 * 
	 * @return habilidadesSeleccionadas
	 */
	public String[] getHabilidadesSeleccionadas() {
		return habilidadesSeleccionadas;
	}
	/**
	 * Asigna las habilidades seleccionadas.
	 * 
	 * @param habilidadesSeleccionadas
	 */
	public void setHabilidadesSeleccionadas(String[] habilidadesSeleccionadas) {
		this.habilidadesSeleccionadas = habilidadesSeleccionadas;
	}
	/**
	 * Retorna las competencias seleccionadas.
	 * 
	 * @return competenciasSeleccionadas
	 */
	public String[] getCompetenciasSeleccionadas() {
		return competenciasSeleccionadas;
	}
	/**
	 * Asigna las competencias seleccionadas.
	 * 
	 * @param competenciasSeleccionadas
	 */
	public void setCompetenciasSeleccionadas(String[] competenciasSeleccionadas) {
		this.competenciasSeleccionadas = competenciasSeleccionadas;
	}
	/**
	 * Retorna las habilidades seleccionadas como postcondiciones.
	 * 
	 * @return habilidadesPCSeleccionadas
	 */
	public String[] getHabilidadesPCSeleccionadas() {
		return habilidadesPCSeleccionadas;
	}
	/**
	 * Asigna las habilidades seleccionadas como postcondiciones.
	 * 
	 * @param habilidadesPCSeleccionadas
	 */
	public void setHabilidadesPCSeleccionadas(
			String[] habilidadesPCSeleccionadas) {
		this.habilidadesPCSeleccionadas = habilidadesPCSeleccionadas;
	}
	/**
	 * Retorna las competencias seleccionadas como postcondiciones.
	 * 
	 * @return competenciasPCSeleccionadas
	 */
	public String[] getCompetenciasPCSeleccionadas() {
		return competenciasPCSeleccionadas;
	}
	/**
	 * Asigna las competencias seleccionadas como postcondiciones.
	 * 
	 * @param competenciasPCSeleccionadas
	 */
	public void setCompetenciasPCSeleccionadas(
			String[] competenciasPCSeleccionadas) {
		this.competenciasPCSeleccionadas = competenciasPCSeleccionadas;
	}
	/**
	 * Retorna los niveles de habilidades seleccionados.
	 * 
	 * @return nivelesHSeleccionados
	 */
	public String[] getNivelesHSeleccionados() {
		return nivelesHSeleccionados;
	}
	/**
	 * Asigna los niveles de habilidades seleccionados.
	 * 
	 * @param nivelesHSeleccionados
	 */
	public void setNivelesHSeleccionados(String[] nivelesHSeleccionados) {
		this.nivelesHSeleccionados = nivelesHSeleccionados;
	}
	/**
	 * Retorna los niveles de competencias seleccionados.
	 * 
	 * @return nivelesCSeleccionados
	 */
	public String[] getNivelesCSeleccionados() {
		return nivelesCSeleccionados;
	}
	/**
	 * Asigna los niveles de competencias seleccionados.
	 * 
	 * @param nivelesCSeleccionados
	 */
	public void setNivelesCSeleccionados(String[] nivelesCSeleccionados) {
		this.nivelesCSeleccionados = nivelesCSeleccionados;
	}
	/**
	 * Retorna el t&iacutetulo del tema.
	 * 
	 * @return temaTitle
	 */
	public String getTemaTitle() {
		return temaTitle;
	}
	/**
	 * Asigna el t&iacutetulo del tema.
	 * 
	 * @param temaTitle
	 */
	public void setTemaTitle(String temaTitle) {
		this.temaTitle = temaTitle;
	}
	/**
	 * Retorna los items seleccionados.
	 * 
	 * @return itemsSeleccionados
	 */
	public String[] getItemsSeleccionados() {
		return itemsSeleccionados;
	}
	/**
	 * Asigna los items seleccionados.
	 * 
	 * @param itemsSeleccionados
	 */
	public void setItemsSeleccionados(String[] itemsSeleccionados) {
		this.itemsSeleccionados = itemsSeleccionados;
	}
	/**
	 * Retorna el item de una caracter&iacutestica.
	 * 
	 * @return itemC
	 */
	public String getItemC() {
		return itemC;
	}
	/**
	 * Asigna el item de una caracter&iacutestica.
	 * 
	 * @param itemC
	 */
	public void setItemC(String itemC) {
		this.itemC = itemC;
	}
	/**
	 * Retorna los items de las caracter&iacutesticas.
	 * 
	 * @return itemsCaracterisiticas
	 */
	public List<Item> getItemsCaracterisiticas() {
		return itemsCaracterisiticas;
	}
	/**
	 * Asigna los items de las caracter&iacutesticas.
	 * 
	 * @param itemsCaracterisiticas
	 */
	public void setItemsCaracterisiticas(List<Item> itemsCaracterisiticas) {
		this.itemsCaracterisiticas = itemsCaracterisiticas;
	}
	/**
	 * Retorna el objetivo de la unidad did&aacutectica.
	 * 
	 * @return subpageGoalUnidad
	 */
	public String getSubpageGoalUnidad() {
		return subpageGoalUnidad;
	}
	/**
	 * Asigna el objetivo de la unidad did&aacutectica.
	 * 
	 * @param subpageGoalUnidad
	 */
	public void setSubpageGoalUnidad(String subpageGoalUnidad) {
		this.subpageGoalUnidad = subpageGoalUnidad;
	}
	/**
	 * Retorna los objetivos seleccionados.
	 * 
	 * @return objetivosSeleccionados
	 */
	public String[] getObjetivosSeleccionados() {
		return objetivosSeleccionados;
	}
	/**
	 * Asigna los objetivos seleccionados.
	 * 
	 * @param objetivosSeleccionados
	 */
	public void setObjetivosSeleccionados(String[] objetivosSeleccionados) {
		this.objetivosSeleccionados = objetivosSeleccionados;
	}
	/**
	 * Retorna los objetivos.
	 * 
	 * @return objetivos
	 */
	public String[] getObjetivos() {
		return objetivos;
	}
	/**
	 * Asigna los objetivos.
	 * 
	 * @param objetivos
	 */
	public void setObjetivos(String[] objetivos) {
		this.objetivos = objetivos;
	}
	/**
	 * Retorna los objetivos del curso.
	 * 
	 * @return objetivoCurso
	 */
	public String getObjetivoCurso() {
		return objetivoCurso;
	}
	/**
	 * Asigna los objetivos del curso.
	 * 
	 * @param objetivoCurso
	 */
	public void setObjetivoCurso(String objetivoCurso) {
		this.objetivoCurso = objetivoCurso;
	}
	/**
	 * Retorna el objetivo de la unidad did&aacutectica.
	 * 
	 * @return subpageGoal
	 */
	public String getSubpageGoal() {
		return subpageGoal;
	}
	/**
	 * Asigna el objetivo de la unidad did&aacutectica.
	 * 
	 * @param subpageGoal
	 */
	public void setSubpageGoal(String subpageGoal) {
		this.subpageGoal = subpageGoal;
	}
	/**
	 * Retorna el identificador de la caracter&iacutestica.
	 * 
	 * @return idC
	 */
	public String getIdC() {
		return idC;
	}
	/**
	 * Asigna el identificador de la caracter&iacutestica.
	 * 
	 * @param idC
	 */
	public void setIdC(String idC) {
		this.idC = idC;
	}
	/**
	 * Retorna el auxiliar para edici&oacuten.
	 * 
	 * @return yaEditar
	 */
	public int getYaEditar() {
		return yaEditar;
	}
	/**
	 * Asigna el auxiliar para edici&oacuten.
	 * 
	 * @param yaEditar
	 */
	public void setYaEditar(int yaEditar) {
		this.yaEditar = yaEditar;
	}
	/**
	 * Retorna los par&aacutemetros generales de la vista.
	 * 
	 * @return params
	 */
	public GeneralViewParameters getParams() {
		return params;
	}
	/**
	 * Asigna los par&aacutemetros generales de la vista.
	 * 
	 * @param params
	 */
	public void setParams(GeneralViewParameters params) {
		this.params = params;
	}
	/**
	 * Retorna el auxiliar para llenar la p&aacutegina de actividad.
	 * 
	 * @return tofillAct
	 */
	public String getTofillAct() {
		return tofillAct;
	}
	/**
	 * Asigna el auxiliar para llenar la p&aacutegina de actividad.
	 * 
	 * @param tofillAct
	 */
	public void setTofillAct(String tofillAct) {
		this.tofillAct = tofillAct;
	}
	/**
	 * Retorna el tipo de la unidad.
	 * 
	 * @return tipo
	 */
	public List<Item> getTipo() {
		return tipo;
	}
	/**
	 * Asigna el tipo de la unidad.
	 * 
	 * @param tipo
	 */
	public void setTipo(List<Item> tipo) {
		this.tipo = tipo;
	}
	/**
	 * Retorna el nivel de habilidades como postcondici&oacuten.
	 * 
	 * @return nivelhpc
	 */
	public String getNivelhpc() {
		return nivelhpc;
	}
	/**
	 * Asigna el nivel de habilidades como postcondici&oacuten.
	 * 
	 * @param nivelhpc
	 */
	public void setNivelhpc(String nivelhpc) {
		this.nivelhpc = nivelhpc;
	}
	/**
	 * Retorna el nivel de competencias como postcondici&oacuten.
	 * 
	 * @return nivelcpc
	 */
	public String getNivelcpc() {
		return nivelcpc;
	}
	/**
	 * Asigna el nivel de competencias como postcondici&oacuten.
	 * 
	 * @return nivelcpc
	 */
	public void setNivelcpc(String nivelcpc) {
		this.nivelcpc = nivelcpc;
	}
	/**
	 * Retorna el contexto seleccionado.
	 * 
	 * @return contextoS
	 */
	public String getContextoS() {
		return contextoS;
	}
	/**
	 * Asigna el contexto seleccionado.
	 * 
	 * @param contextoS
	 */
	public void setContextoS(String contextoS) {
		this.contextoS = contextoS;
	}
	/**
	 * Retorna la habilidad seleccionada como postcondici&oacuten.
	 * 
	 * @return habilidadSPC
	 */
	public String getHabilidadSPC() {
		return habilidadSPC;
	}
	/**
	 * Asigna la habilidad seleccionada como postcondici&oacuten.
	 * 
	 * @param habilidadSPC
	 */
	public void setHabilidadSPC(String habilidadSPC) {
		this.habilidadSPC = habilidadSPC;
	}
	/**
	 * Retorna la competencia seleccionada como postcondici&oacuten.
	 * 
	 * @return competenciaSPC
	 */
	public String getCompetenciaSPC() {
		return competenciaSPC;
	}
	/**
	 * Asigna la competencia seleccionada como postcondici&oacuten.
	 * 
	 * @param competenciaSPC
	 */
	public void setCompetenciaSPC(String competenciaSPC) {
		this.competenciaSPC = competenciaSPC;
	}
	/**
	 * Retorna la habilidad seleccionada.
	 * 
	 * @return habilidadS
	 */
	public String getHabilidadS() {
		return habilidadS;
	}
	/**
	 * Asigna la habilidad seleccionada.
	 * 
	 * @param habilidadS
	 */
	public void setHabilidadS(String habilidadS) {
		this.habilidadS = habilidadS;
	}
	/**
	 * Retorna la competencia seleccionada.
	 * 
	 * @return competenciaS
	 */
	public String getCompetenciaS() {
		return competenciaS;
	}
	/**
	 * Asignsa la competencia seleccionada.
	 * 
	 * @return competenciaS
	 */
	public void setCompetenciaS(String competenciaS) {
		this.competenciaS = competenciaS;
	}
	/**
	 * Retorna la personalidad seleccionada.
	 * 
	 * @return personalidadS
	 */
	public String getPersonalidadS() {
		return personalidadS;
	}
	/**
	 * Asigna la personalidad seleccionada.
	 * 
	 * @param personalidadS
	 */
	public void setPersonalidadS(String personalidadS) {
		this.personalidadS = personalidadS;
	}
	/**
	 * Retorna la situaci&oacuten de aprendizaje seleccionada.
	 * 
	 * @return saS
	 */
	public String getSaS() {
		return saS;
	}
	/**
	 * Asigna la situaci&oacuten de aprendizaje seleccionada.
	 * 
	 * @param saS
	 */
	public void setSaS(String saS) {
		this.saS = saS;
	}
	/**
	 * Retorna el estilo seleccionado.
	 * 
	 * @return estiloS
	 */
	public String getEstiloS() {
		return estiloS;
	}
	/**
	 * Asigna el estilo seleccionado.
	 * 
	 * @param estiloS
	 */
	public void setEstiloS(String estiloS) {
		this.estiloS = estiloS;
	}
	/**
	 * Retorna el tipo seleccionado.
	 * 
	 * @return tipoSeleccionado
	 */
	public String getTipoSeleccionado() {
		return tipoSeleccionado;
	}
	/**
	 * Asigna el tipo seleccionado.
	 * 
	 * @param tipoSeleccionado
	 */
	public void setTipoSeleccionado(String tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
	}
	/**
	 * Retorna el contexto.
	 * 
	 * @return contexto
	 */
	public List<Caracteristica> getContexto() {
		return contexto;
	}
	/**
	 * Asigna el contexto.
	 * 
	 * @param contexto
	 */
	public void setContexto(List<Caracteristica> contexto) {
		this.contexto = contexto;
	}
	/**
	 * Retorna el nivel de estilo de aprendizaje.
	 * 
	 * @return nivele
	 */
	public String getNivele() {
		return nivele;
	}
	/**
	 * Asigna el nivel de estilo de aprendizaje.
	 * 
	 * @param nivele
	 */
	public void setNivele(String nivele) {
		this.nivele = nivele;
	}
	/**
	 * Retorna el nivel de habilidad.
	 * 
	 * @return nivelh
	 */
	public String getNivelh() {
		return nivelh;
	}
	/**
	 * Asigna el nivel de habilidad.
	 * 
	 * @param nivelh
	 */
	public void setNivelh(String nivelh) {
		this.nivelh = nivelh;
	}
	/**
	 * Retorna el nivel de competencia.
	 * 
	 * @return nivelc
	 */
	public String getNivelc() {
		return nivelc;
	}
	/**
	 * Asigna el nivel de competencia.
	 * 
	 * @param nivelc
	 */
	public void setNivelc(String nivelc) {
		this.nivelc = nivelc;
	}
	/**
	 * Retorna el nivel de personalidad.
	 * 
	 * @return nivelp
	 */
	public String getNivelp() {
		return nivelp;
	}
	/**
	 * Asigna el nivel de personalidad.
	 * 
	 * @param nivelp
	 */
	public void setNivelp(String nivelp) {
		this.nivelp = nivelp;
	}
	/**
	 * Retorna el nivel de situaci&oacuten de aprendizaje.
	 * 
	 * @return nivelsa
	 */
	public String getNivelsa() {
		return nivelsa;
	}
	/**
	 * Asigna el nivel de situaci&oacuten de aprendizaje.
	 * 
	 * @param nivelsa
	 */
	public void setNivelsa(String nivelsa) {
		this.nivelsa = nivelsa;
	}
	/**
	 * Retorna el estilo de aprendizaje.
	 * 
	 * @return estilo
	 */
	public List<Caracteristica> getEstilo() {
		return estilo;
	}
	/**
	 * Asigna el estilo de aprendizaje.
	 * 
	 * @param estilo
	 */
	public void setEstilo(List<Caracteristica> estilo) {
		this.estilo = estilo;
	}
	/**
	 * Retorna la habilidad.
	 * 
	 * @return habilidad
	 */
	public List<Caracteristica> getHabilidad() {
		return habilidad;
	}
	/**
	 * Asigna la habilidad.
	 * 
	 * @param habilidad
	 */
	public void setHabilidad(List<Caracteristica> habilidad) {
		this.habilidad = habilidad;
	}
	/**
	 * Retorna la competencia.
	 * 
	 * @return competencia
	 */
	public List<Caracteristica> getCompetencia() {
		return competencia;
	}
	/**
	 * Asigna la competencia.
	 * 
	 * @param competencia
	 */
	public void setCompetencia(List<Caracteristica> competencia) {
		this.competencia = competencia;
	}
	/**
	 * Retorna la personalidad.
	 * 
	 * @return personalidad
	 */
	public List<Caracteristica> getPersonalidad() {
		return personalidad;
	}
	/**
	 * Asigna la personalidad.
	 * 
	 * @param personalidad
	 */
	public void setPersonalidad(List<Caracteristica> personalidad) {
		this.personalidad = personalidad;
	}
	/**
	 * Retorna la situaci&oacuten de aprendizaje.
	 * 
	 * @return sA
	 */
	public List<Caracteristica> getsA() {
		return sA;
	}
	/**
	 * Asigna la situaci&oacuten de aprendizaje.
	 * 
	 * @param sA
	 */
	public void setsA(List<Caracteristica> sA) {
		this.sA = sA;
	}
	/**
	 * Retorna el tipo de caracter&iacutestica.
	 * 
	 * @return tipoCaracteristicas
	 */
	public List<Item> getTipoCaracteristicas() {
		return tipoCaracteristicas;
	}
	/**
	 * Asigna el tipo de caracter&iacutestica.
	 * 
	 * @param tipoCaracteristicas
	 */
	public void setTipoCaracteristicas(List<Item> tipoCaracteristicas) {
		this.tipoCaracteristicas = tipoCaracteristicas;
	}
	/**
	 * Retorna el tipo de contexto.
	 * 
	 * @return tipoContexto
	 */
	public List<Item> getTipoContexto() {
		return tipoContexto;
	}
	/**
	 * Asigna el tipo de contexto.
	 * 
	 * @param tipoContexto
	 */
	public void setTipoContexto(List<Item> tipoContexto) {
		this.tipoContexto = tipoContexto;
	}
	/**
	 * Retorna el tipo de persona.
	 * 
	 * @return tipoPersona
	 */
	public List<Item> getTipoPersona() {
		return tipoPersona;
	}
	/**
	 * Asigna el tipo de persona.
	 * 
	 * @param tipoPersona
	 */
	public void setTipoPersona(List<Item> tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	/**
	 * Retorna el tipo de actividad.
	 * 
	 * @return tipoActividad
	 */
	public List<Tipo> getTipoActividad() {
		return tipoActividad;
	}
	/**
	 * Asigna el tipo de actividad.
	 * 
	 * @param tipoActividad
	 */
	public void setTipoActividad(List<Tipo> tipoActividad) {
		this.tipoActividad = tipoActividad;
	}
	/**
	 * Retorna la caracter&iacutestica seleccionada.
	 * 
	 * @return caracteristicaSeleccionada
	 */
	public String getCaracteristicaSeleccionada() {
		return caracteristicaSeleccionada;
	}
	/**
	 * Asigna la caracter&iacutestica seleccionada.
	 * 
	 * @param caracteristicaSeleccionada
	 */
	public void setCaracteristicaSeleccionada(String caracteristicaSeleccionada) {
		this.caracteristicaSeleccionada = caracteristicaSeleccionada;
	}
	/**
	 * Retorna el nombre del contexto.
	 * 
	 * @return nombreCx
	 */
	public String getNombreCx() {
		return nombreCx;
	}
	/**
	 * Asigna el nombre del contexto.
	 * 
	 * @param nombreCx
	 */
	public void setNombreCx(String nombreCx) {
		this.nombreCx = nombreCx;
	}
	/**
	 * Retorna el tipo del contexto.
	 * 
	 * @return tipoCx
	 */
	public String getTipoCx() {
		return tipoCx;
	}
	/**
	 * Asigna el tipo del contexto.
	 * 
	 * @param tipoCx
	 */
	public void setTipoCx(String tipoCx) {
		this.tipoCx = tipoCx;
	}
	/**
	 * Retorna el nombre de caracter&iacutestica.
	 * 
	 * @return nombreC
	 */
	public String getNombreC() {
		return nombreC;
	}
	/**
	 * Asigna el nombre de caracter&iacutestica.
	 * 
	 * @param nombreC
	 */
	public void setNombreC(String nombreC) {
		this.nombreC = nombreC;
	}
	/**
	 * Retorna el tipo de caracter&iacutestica.
	 * 
	 * @return tipoC
	 */
	public String getTipoC() {
		return tipoC;
	}
	/**
	 * Asigna el tipo de caracter&iacutestica.
	 * 
	 * @param tipoC
	 */
	public void setTipoC(String tipoC) {
		this.tipoC = tipoC;
	}
	/**
	 * Retorna el test de caracter&iacutestica.
	 * 
	 * @return testC
	 */
	public String getTestC() {
		return testC;
	}
	/**
	 * Asigna el test de caracter&iacutestica.
	 * 
	 * @param testC
	 */
	public void setTestC(String testC) {
		this.testC = testC;
	}
	/**
	 * Retorna nombre de actividad.
	 * 
	 * @return nomberA
	 */
	public String getNomberA() {
		return nomberA;
	}
	/**
	 * Asigna nombre de actividad.
	 * 
	 * @param nomberA
	 */
	public void setNomberA(String nomberA) {
		this.nomberA = nomberA;
	}
	/**
	 * Retorna tipo de actividad.
	 * 
	 * @return tipoA
	 */
	public String getTipoA() {
		return tipoA;
	}
	/**
	 * Asigna tipo de actividad.
	 * 
	 * @param tipoA
	 */
	public void setTipoA(String tipoA) {
		this.tipoA = tipoA;
	}
	/**
	 * Retorna la fecha inicial.
	 * 
	 * @return inicialF
	 */
	public String getInicialF() {
		return inicialF;
	}
	/**
	 * Asigna la fecha inicial.
	 * 
	 * @param inicialF
	 */
	public void setInicialF(String inicialF) {
		this.inicialF = inicialF;
	}
	/**
	 * Retorna la fecha final.
	 * 
	 * @return finalF
	 */
	public String getFinalF() {
		return finalF;
	}
	/**
	 * Asigna la fecha final.
	 * 
	 * @param finalF
	 */
	public void setFinalF(String finalF) {
		this.finalF = finalF;
	}
	/**
	 * Retorna el string del nuevo grafo.
	 * 
	 * @return newFlowChart
	 */
	public String getNewFlowChart() {
		return newFlowChart;
	}
	/**
	 * Asigna el string del nuevo grafo.
	 * 
	 * @param newFlowChart
	 */
	public void setNewFlowChart(String jsonFlowChart) {
		this.newFlowChart = jsonFlowChart;
	}
	/**
	 * Retorna el string del grafo de la UD existente en la base de datos.
	 * 
	 * @return existingFlowChart
	 */
	public String getExistingFlowChart() {
		return existingFlowChart;
	}
	/**
	 * Asigna el string del grafo de la UD existente en la base de datos.
	 * 
	 * @param existingFlowChart
	 */
	public void setExistingFlowChart(String existingFlowChart) {
		this.existingFlowChart = existingFlowChart;
	}
	/**
	 * Retorna el string de las actividades de la UD existentes en la base de datos.
	 * 
	 * @return existingActivities
	 */
	public String getExistingActivities() {
		return existingActivities;
	}
	/**
	 * Asigna el string de las actividades de la UD existentes en la base de datos.
	 * 
	 * @param existingActivities
	 */
	public void setExistingActivities(String existingActivities) {
		this.existingActivities = existingActivities;
	}
	/**
	 * Retorna la &uacuteltima conexi&oacuten.
	 * 
	 * @return lastConnection
	 */
	public String getLastConnection() {
		return lastConnection;
	}
	/**
	 * Asigna la &uacuteltima conexi&oacuten.
	 * 
	 * @param lastConnection
	 */
	public void setLastConnection(String lastConnection) {
		this.lastConnection = lastConnection;
	}
	/**
	 * Retorna el estilo de una actividad.
	 * 
	 * @return activitiesStyle
	 */
	public String getActivitiesStyle() {
		return activitiesStyle;
	}
	/**
	 * Asigna el estilo de una actividad.
	 * 
	 * @param activitiesStyle
	 */
	public void setActivitiesStyle(String activitiesStyle) {
		this.activitiesStyle = activitiesStyle;
	}
	/**
	 * Retorna la descripci&oacuten de actividad.
	 * 
	 * @return descripcionA
	 */
	public String getDescripcionA() {
		return descripcionA;
	}
	/**
	 * Asigna la descripci&oacuten de actividad.
	 * 
	 * @param descripcionA
	 */
	public void setDescripcionA(String descripcionA) {
		this.descripcionA = descripcionA;
	}
	/**
	 * Retorna el objetivo de la actividad.
	 * 
	 * @return objetivoA
	 */
	public String getObjetivoA() {
		return objetivoA;
	}
	/**
	 * Asigna el objetivo de la actividad.
	 * 
	 * @param objetivoA
	 */
	public void setObjetivoA(String objetivoA) {
		this.objetivoA = objetivoA;
	}
	/**
	 * Clase que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static class PathEntry {
		public Long pageId;
		public Long pageItemId;
		public String title;
	}
	/**
	 * Clase que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static class UrlItem {
		public String Url;
		public String label;
		public UrlItem(String Url, String label) {
			this.Url = Url;
			this.label = label;
		}
	}
	/**
	 * Clase que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static class GroupEntry {
		public String name;
		public String id;
	}

	// Image types
	/**
	 * Lista que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static ArrayList<String> imageTypes;

	static {
		imageTypes = new ArrayList<String>();
		imageTypes.add("bmp");
		imageTypes.add("gif");
		imageTypes.add("icns");
		imageTypes.add("ico");
		imageTypes.add("jpg");
		imageTypes.add("jpeg");
		imageTypes.add("png");
		imageTypes.add("tiff");
		imageTypes.add("tif");
	}

	// Spring Injection

	/**
	 * Atributo manejador de sesi&oacuten que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private SessionManager sessionManager;
	/**
	 * Asigna el session manager.
	 * @param sessionManager
	 */
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private ContentHostingService contentHostingService;
	/**
	 * Asigna el contentHostingService.
	 * @param contentHostingService
	 */
	public void setContentHostingService(
			ContentHostingService contentHostingService) {
		this.contentHostingService = contentHostingService;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private GradebookIfc gradebookIfc = null;
	/**
	 * Asigna el gradebookIfc.
	 * @param g
	 */
	public void setGradebookIfc(GradebookIfc g) {
		gradebookIfc = g;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private LessonEntity forumEntity = null;
	/**
	 * Asigna el forumEntity.
	 * @param e
	 */
	public void setForumEntity(Object e) {
		forumEntity = (LessonEntity) e;
	}

	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private LessonEntity quizEntity = null;
	/**
	 * Asigna el quizEntity.
	 * @param e
	 */
	public void setQuizEntity(Object e) {
		quizEntity = (LessonEntity) e;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private LessonEntity assignmentEntity = null;
	/**
	 * Asigna el assignmentEntity.
	 * @param e
	 */
	public void setAssignmentEntity(Object e) {
		assignmentEntity = (LessonEntity) e;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private LessonEntity bltiEntity = null;
	/**
	 * Asigna el bltiEntity.
	 * @param e
	 */
	public void setBltiEntity(Object e) {
		bltiEntity = (LessonEntity) e;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private LessonEntity actividadEntity = null;
	/**
	 * Asigna el actividadEntity.
	 * @param e
	 */
	public void setActividadEntity(Object e) {
		actividadEntity = (LessonEntity) e;
	}

	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private ToolManager toolManager;
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private SecurityService securityService;
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private SiteService siteService;
	/**
	 * Controlador de la interacci&oacuten con la base de datos de Sakai.
	 */
	private SimplePageToolDao simplePageToolDao;

	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private MessageLocator messageLocator;
	/**
	 * Asigna el messageLocator.
	 * @param messageLocator
	 */
	public void setMessageLocator(MessageLocator x) {
		messageLocator = x;
	}
	/**
	 * Retorna el messageLocator.
	 * @return messageLocator
	 */
	public MessageLocator getMessageLocator() {
		return messageLocator;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	static MemoryService memoryService = null;
	/**
	 * Asigna el memoryService.
	 * @param m
	 */
	public void setMemoryService(MemoryService m) {
		memoryService = m;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private HttpServletResponse httpServletResponse;
	/**
	 * Asigna el httpServletResponse.
	 * @param httpServletResponse
	 */
	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private HttpServletRequest httpServletRequest;
	/**
	 * Asigna el httpServletRequest.
	 * @param httpServletRequest
	 */
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private LessonBuilderEntityProducer lessonBuilderEntityProducer;
	/**
	 * Asigna el lessonBuilderEntityProducer.
	 * @param p
	 */
	public void setLessonBuilderEntityProducer(LessonBuilderEntityProducer p) {
		lessonBuilderEntityProducer = p;
	}
	// End Injection

	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	static Class levelClass = null;
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	static Object[] levels = null;
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	static Class ftClass = null;
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	static Method ftMethod = null;
	/**
	 * Atributo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	static Object ftInstance = setupFtStuff();
	/**
	 * M&eacutetodo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	static Object setupFtStuff () {
		Object ret = null;
		try {
			levelClass = Class.forName("org.sakaiproject.util.api.FormattedText$Level");
			levels = levelClass.getEnumConstants();
			ftClass = Class.forName("org.sakaiproject.util.api.FormattedText");
			ftMethod = ftClass.getMethod("processFormattedText", 
					new Class[] { String.class, StringBuilder.class, levelClass }); 
			ret = org.sakaiproject.component.cover.ComponentManager.get("org.sakaiproject.util.api.FormattedText");
			return ret;
		} catch (Exception e) {
			log.error("Formatted Text with levels not available: " + e);
			return null;
		}
	}

	/**
	 * M&eacutetodo que inicializa el bean.
	 */
	public void init () {	
		if (groupCache == null) {
			groupCache = memoryService.newCache("org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean.groupCache");
		}

		if (resourceCache == null) {
			resourceCache = memoryService.newCache("org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean.resourceCache");
		}
		this.existingFlowChart="";
		this.existingActivities="";
	}

	// no destroy. We want to leave the cache intact when we exit, because there's one of us
	// per request.
	/**
	 * Encuentra un item en el cache de Sakai.
	 * 
	 * @param itId identificador del item.
	 * @return ret el item encontrado.
	 */
	public SimplePageItem findItem(long itId) {
		Long itemId = itId;
		SimplePageItem ret = itemCache.get(itemId);
		if (ret != null)
			return ret;
		ret = simplePageToolDao.findItem(itemId);
		if (ret != null)
			itemCache.put(itemId, ret);
		return ret;
	}
	/**
	 * Encuentra una p&aacutegina a partir de su identificador.
	 * 
	 * @param pageId identificador de la p&aacutegina.
	 * @return ret la p&aacutegina encontrada.
	 */
	public SimplePage getPage(Long pageId) {
		SimplePage ret = pageCache.get(pageId);
		if (ret != null)
			return ret;
		ret = simplePageToolDao.getPage(pageId);
		if (ret != null)
			pageCache.put(pageId, ret);
		return ret;
	}
	/**
	 * Retorna los mensajes de error.
	 * @return errors los mensajes de error.
	 */
	public List<String> errMessages() {
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		List<String> errors = (List<String>)toolSession.getAttribute("lessonbuilder.errors");
		if (errors != null)
			toolSession.removeAttribute("lessonbuilder.errors");
		return errors;
	}
	/**
	 * Asigna un mensaje de error.
	 * @param s el mensaje de error.
	 */
	public void setErrMessage(String s) {
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		if (toolSession == null) {
			System.out.println("Lesson Builder error not in tool: " + s);
			return;
		}
		List<String> errors = (List<String>)toolSession.getAttribute("lessonbuilder.errors");
		if (errors == null)
			errors = new ArrayList<String>();
		errors.add(s);
		toolSession.setAttribute("lessonbuilder.errors", errors);
	}
	/**
	 * Asigna una pareja llave-mensaje en los mensajes existentes.
	 * @param key la llave.
	 * @param text el mensaje.
	 */
	public void setErrKey(String key, String text ) {
		if (text == null)
			text = "";
		setErrMessage(messageLocator.getMessage(key).replace("{}", text));
	}
	/**
	 * Asigna el atributo top refresh del m&oacutedulo lessonbuilder.
	 */
	public void setTopRefresh() {
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		if (toolSession == null)
			return;
		toolSession.setAttribute("lessonbuilder.topRefresh", true);
	}
	/**
	 * Retorna el atributo top refresh del m&oacutedulo lessonbuilder.
	 * @return true o false.
	 */
	public boolean getTopRefresh() {
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		if (toolSession.getAttribute("lessonbuilder.topRefresh") != null) {
			toolSession.removeAttribute("lessonbuilder.topRefresh");
			return true;
		}
		return false;
	}


	// a lot of these are setters and getters used for the form process, as 
	// described above

	/**
	 * Asigna el atributo alt.
	 * @param alt
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}
	/**
	 * Retorna la descripci&oacuten del item.
	 * @return la descripci&oacuten.
	 */
	public String getDescription() {
		if (itemId != null && itemId != -1) {
			return findItem(itemId).getDescription();
		} else {
			return null;
		}
	}
	/**
	 * Asigna la descripci&oacuten del item.
	 * @param description la descripci&oacuten.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Asigna el atributo hidePage.
	 * @param hide
	 */
	public void setHidePage(boolean hide) {
		hidePage = hide;
	}

	/**
	 * Asigna el atributo releaseDate.
	 * @param releaseDate
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	/**
	 * Retorna el atributo releaseDate.
	 * @param releaseDate
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}
	/**
	 * Asigna el atributo hasReleaseDate.
	 * @param hasReleaseDate
	 */
	public void setHasReleaseDate(boolean hasReleaseDate) {
		this.hasReleaseDate = hasReleaseDate;
	}
	/**
	 * M&eacutetodo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public void setImporttop(boolean i) {
		this.importtop = i;
	}

	// gets called for non-checked boxes also, but q will be null
	/**
	 * Asigna el atributo quiztool
	 * @param q
	 */
	public void setQuiztool(String q) {
		if (q != null)
			quiztool = q;
	}
	/**
	 * Asigna el atributo assigntool
	 * @param q
	 */
	public void setAssigntool(String q) {
		if (q != null)
			assigntool = q;
	}
	/**
	 * Asigna el atributo topictool
	 * @param q
	 */
	public void setTopictool(String q) {
		if (q != null)
			topictool = q;
	}
	/**
	 * Retorna el nombre del item.
	 * @return el nombre.
	 */
	public String getName() {
		if (itemId != null && itemId != -1) {
			return findItem(itemId).getName();
		} else {
			return null;
		}
	}
	/**
	 * Asigna el nombre del item.
	 * @param name el nombre.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Asigna el atributo required.
	 * @param required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * Asigna el atributo subrequirement.
	 * @param subrequirement
	 */
	public void setSubrequirement(boolean subrequirement) {
		this.subrequirement = subrequirement;
	}
	/**
	 * Asigna el atributo prerequisite.
	 * @param prerequisite
	 */
	public void setPrerequisite(boolean prerequisite) {
		this.prerequisite = prerequisite;
	}
	/**
	 * Asigna el atributo newWindow.
	 * @param newWindow
	 */
	public void setNewWindow(boolean newWindow) {
		this.newWindow = newWindow;
	}
	/**
	 * Asigna el atributo dropDown.
	 * @param dropDown
	 */
	public void setDropDown(String dropDown) {
		this.dropDown = dropDown;
	}
	/**
	 * Asigna el atributo points.
	 * @param points
	 */
	public void setPoints(String points) {
		this.points = points;
	}
	/**
	 * Asigna el atributo format.
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Asigna el atributo mimetype.
	 * @param mimetype
	 */
	public void setMimetype(String mimetype) {
		if (mimetype != null)
			mimetype = mimetype.toLowerCase().trim();
		this.mimetype = mimetype;
	}

	/**
	 * Retorna el t&iacutetulo de la p&aacutegina.
	 * @return el t&iacutetulo.
	 */
	public String getPageTitle() {
		return getCurrentPage().getTitle();
	}
	/**
	 * Asigna el t&iacutetulo de la p&aacutegina.
	 * @param el t&iacutetulo.
	 */
	public void setPageTitle(String title) {
		pageTitle = title;
	}
	/**
	 * Asigna un nuevo t&iacutetulo a la p&aacutegina.
	 * @param el t&iacutetulo.
	 */
	public void setNewPageTitle(String title) {
		newPageTitle = title;
	}

	/**
	 * Asigna el atributo numberOfPages.
	 * @param n
	 */
	public void setNumberOfPages(String n) {
		numberOfPages = n;
	}
	/**
	 * Asigna el atributo copyPage.
	 * @param c
	 */
	public void setCopyPage(boolean c) {
		this.copyPage = c;
	}

	/**
	 * M&eacutetodo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 * Retorna los contenidos.
	 * @return los contenidos.
	 */
	public String getContents() {
		return (itemId != null && itemId != -1 ? findItem(itemId).getHtml()
				: "");
	}
	/**
	 * M&eacutetodo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 * Asigna los contenidos.
	 * @param los contenidos.
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}
	/**
	 * Asigna el identificador del item.
	 * @param id el identificador.
	 */
	public void setItemId(Long id) {
		itemId = id;
	}
	/**
	 * Retorna el identificador del item.
	 * @return id el identificador.
	 */
	public Long getItemId() {
		return itemId;
	}
	/**
	 * Asigna el atributo isMultimedia.
	 * @param isMm.
	 */
	public void setMultimedia(boolean isMm) {
		isMultimedia = isMm;
	}
	/**
	 * Asigna el atributo isWebsite.
	 * @param isWebsite.
	 */
	public void setWebsite(boolean isWebsite) {
		this.isWebsite = isWebsite;
	}

	// hibernate interposes something between us and saveItem, and that proxy gets an
	// error after saveItem does. Thus we never see any value that saveItem might 
	// return. Hence we pass saveItem a list to which it adds the error message. If
	// there is a message from saveItem take precedence over the message we detect here,
	// since it's the root cause.
	/**
	 * Almacena un objeto en la base de datos de Sakai.
	 * @param i el objeto.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveItem(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();

		try {
			simplePageToolDao.saveItem(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena un objeto en la base de datos de Sakai.
	 * @param i el objeto.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveItem(Object i) {
		return saveItem(i, true);
	}	
	/**
	 * Almacena una actividad en la base de datos de Ashyi.
	 * @param i la actividad.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveActividad(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();
		int id = -1;
		try {
			simplePageToolDao.saveActividad(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}
	/**
	 * Actualiza una actividad en la base de datos de Ashyi.
	 * @param i la actividad.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean updateActividad(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();

		try {
			simplePageToolDao.update(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena una dependenciaActividad en la base de datos de Ashyi.
	 * @param i la dependenciaActividad.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveDependenciaActividad(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();
		int id = -1;
		try {
			simplePageToolDao.saveDependenciaActividad(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena una actividadTieneActividad en la base de datos de Ashyi.
	 * @param i la actividadTieneActividad.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveActividadTieneActividad(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();
		int id = -1;
		try {
			simplePageToolDao.saveActividadTieneActividad(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena una caracter&iacutestica en la base de datos de Ashyi.
	 * @param i la caracter&iacutestica.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveCaracteristica(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();

		try {
			simplePageToolDao.saveCaracteristica(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena un itemUsuario en la base de datos de Ashyi.
	 * @param i el itemUsuario.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveItemUsuario(Object i, boolean requiresEditPermission) { 
		String err = null;
		List<String>elist = new ArrayList<String>();

		try {
			simplePageToolDao.saveItemUsuario(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena una actividad en la base de datos de Ashyi.
	 * @param i la actividad.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveActividad(Actividad i) {
		return saveActividad(i, true);
	}

	/**
	 * Actualiza una actividad en la base de datos de Ashyi.
	 * @param i la actividad.
	 * @return true si fue posible, false si no.
	 */
	public boolean updateActividad(Actividad i) {
		return updateActividad(i, true);
	}
	/**
	 * Almacena una caracter&iacutestica en la base de datos de Ashyi.
	 * @param i la caracter&iacutestica.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveCarateristica(Object i) {
		return saveCaracteristica(i, true);
	}
	/**
	 * Almacena un control en la base de datos de Ashyi.
	 * @param i el control.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveControl(Control i) {
		return saveControl(i, true);
	}
	/**
	 * Actualiza un objeto cualquiera en la base de datos de Ashyi.
	 * @param i el objeto.
	 * @return true si fue posible, false si no.
	 */
	public boolean update(Object i) {
		return update(i, true);
	}

	// see notes for saveupdate

	// requiresEditPermission determines whether simplePageToolDao should confirm
	// edit permissions before making the update
	/**
	 * Actualiza un objeto cualquiera en la base de datos de Ashyi.
	 * @param i el objeto.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	boolean update(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();
		try {
			simplePageToolDao.update(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}

	// The permissions model assumes that all code operates on the current
	// page. When the current page is set, the set code verifies that the
	// page is in the current site. However when operating on items, we
	// have to make sure they are in the current page, or we could end up
	// hacking on an item in a completely different site. This method checks
	// that an item is OK to hack on, given the current page.
	/**
	 * Revisa si un item pertenece a la p&aacutegina.
	 * @param itemId el identificador del item.
	 * @return true si pertenece, false si no.
	 */
	private boolean itemOk(Long itemId) {
		// not specified, we'll add a new one
		if (itemId == null || itemId == -1)
			return true;
		SimplePageItem item = findItem(itemId);
		if (item.getPageId() != getCurrentPageId()) {
			return false;
		}
		return true;
	}

	// called by the producer that uses FCK to update a text block
	/**
	 * Actualiza un bloque de texto.
	 * @return resultado de la operaci&oacuten.
	 */
	public String submit() {
		String rv = "success";

		if (!itemOk(itemId))
			return "permission-failed";

		if (canEditPage()) {
			Placement placement = toolManager.getCurrentPlacement();

			StringBuilder error = new StringBuilder();

			// there's an issue with HTML security in the Sakai community.
			// a lot of people feel users shouldn't be able to add javascript, etc
			// to their HTML. I think enforcing that makes Sakai less than useful.
			// So check config options to see whether to do that check
			final Integer FILTER_DEFAULT=0;
			final Integer FILTER_HIGH=1;
			final Integer FILTER_LOW=2;
			final Integer FILTER_NONE=3;

			String html = contents;

			// figure out how to filter
			Integer filter = FILTER_DEFAULT;
			if (getCurrentPage().getOwner() != null) {
				filter = FILTER_DEFAULT; // always filter student content
			} else {
				// this is instructor content.
				// see if specified
				String filterSpec = placement.getPlacementConfig().getProperty("filterHtml");
				if (filterSpec == null)
					filterSpec = filterHtml;
				// no, default to LOW. That will allow embedding but not Javascript
				if (filterSpec == null) // should never be null. unspeciifed should give ""
					filter = FILTER_DEFAULT;
				// old specifications
				else if (filterSpec.equalsIgnoreCase("true"))
					filter = FILTER_HIGH; // old value of true produced the same result as missing
				else if (filterSpec.equalsIgnoreCase("false"))			    
					filter = FILTER_NONE;
				// new ones
				else if (filterSpec.equalsIgnoreCase("default"))			    
					filter = FILTER_DEFAULT;
				else if (filterSpec.equalsIgnoreCase("high")) 
					filter = FILTER_HIGH;
				else if (filterSpec.equalsIgnoreCase("low")) 
					filter = FILTER_LOW;
				else if (filterSpec.equalsIgnoreCase("none")) 
					filter = FILTER_NONE;
				// unspecified
				else
					filter = FILTER_DEFAULT;
			}			    
			if (filter.equals(FILTER_NONE)) {
				html = FormattedText.processHtmlDocument(contents, error);
			} else if (filter.equals(FILTER_DEFAULT)) {
				html = FormattedText.processFormattedText(contents, error);
			} else if (ftInstance != null) {
				try {
					// now filter is set. Implement it. Depends upon whether we have the anti-samy code
					Object level = null;
					if (filter.equals(FILTER_HIGH))
						level = levels[1];
					else
						level = levels[2];

					html = (String)ftMethod.invoke(ftInstance, new Object[] { contents, error, level });
				} catch (Exception e) {
					// this should never happen. If it does, emulate what the anti-samy
					// code does if antisamy is disabled. It always filters
					html = FormattedText.processFormattedText(contents, error);
				}
			} else {
				// don't have antisamy. For LOW, use old instructor behavior, since
				// LOW is the default. For high, it makes sense to filter
				if (filter.equals(FILTER_HIGH))
					html = FormattedText.processFormattedText(contents, error);
				else
					html = FormattedText.processHtmlDocument(contents, error);

			}

			// if (getCurrentPage().getOwner() != null || filterHtml 
			//		&& !"false".equals(placement.getPlacementConfig().getProperty("filterHtml")) ||
			//		"true".equals(placement.getPlacementConfig().getProperty("filterHtml"))) {
			//	html = FormattedText.processFormattedText(contents, error);
			//} else {
			//	html = FormattedText.processHtmlDocument(contents, error);

			if (html != null) {
				SimplePageItem item;
				// itemid -1 means we're adding a new item to the page, 
				// specified itemid means we're updating an existing one
				if (itemId != null && itemId != -1) {
					item = findItem(itemId);
				} else {
					item = appendItem("", "", SimplePageItem.TEXT);
				}

				item.setHtml(html);
				setItemGroups(item, selectedGroups);
				update(item);
			} else {
				rv = "cancel";
			}

			placement.save();
		} else {
			rv = "cancel";
		}

		return rv;
	}
	/**
	 * Retorna el string "cancel".
	 * @return "cancel".
	 */
	public String cancel() {
		return "cancel";
	}

	/**
	 * Procesa un recurso cargado.
	 * @return true si fue posible, false si no.
	 */
	public String processMultimedia() {
		return processResource(SimplePageItem.MULTIMEDIA, false, false);
	}
	/**
	 * Procesa un recurso cargado.
	 * @return true si fue posible, false si no.
	 */
	public String processResource() {
		return processResource(SimplePageItem.RESOURCE, false, false);
	}
	/**
	 * Procesa un recurso cargado por el estudiante.
	 * @return true si fue posible, false si no.
	 */
	public String processStudentResponse() {
		System.out.println("En process student response");
		return processResource(SimplePageItem.RESOURCE, false, true);
	}
	/**
	 * Procesa un recurso cargado.
	 * @return true si fue posible, false si no.
	 */
	public String processWebSite() {
		return processResource(SimplePageItem.RESOURCE, true, false);
	}

	// get mime type for a URL. connect to the server hosting
	// it and ask them. Sorry, but I don't think there's a better way
	/**
	 * Retorna el tipo de url.
	 * @param url
	 * @return mimeType el tipo.
	 */
	public String getTypeOfUrl(String url) {
		String mimeType = null;

		// try to find the mime type of the remote resource
		// this is only likely to be a problem if someone is pointing to
		// a url within Sakai. We think in realistic cases those that are
		// files will be handled as files, so anything that comes where
		// will be HTML. That's the default if this fails.
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			// generate cookie based on code in  RequestFilter.java
			//String suffix = System.getProperty("sakai.serverId");
			//if (suffix == null || suffix.equals(""))
			//    suffix = "sakai";
			//Session s = sessionManager.getCurrentSession();
			//conn.setRequestProperty("Cookie", "JSESSIONID=" + s.getId() + "." + suffix);
			conn.connect();
			String t = conn.getContentType();
			if (t != null && !t.equals("")) {
				int i = t.indexOf(";");
				if (i >= 0)
					t = t.substring(0, i);
				t = t.trim();
				mimeType = t;
			}
			conn.getInputStream().close();
		} catch (Exception e) {log.error("getTypeOfUrl connection error " + e);};
		return mimeType;
	}

	// return call from the file picker, used by add resource
	// the picker communicates with us by session variables
	/**
	 * Procesa un recurso cargado.
	 * @param type el tipo.
	 * @param isWebSite si es un sitio web.
	 * @param isStudentResponse si es subido por el estudiante.
	 * @return resultado de la operaci&oacuten.
	 */
	public String processResource(int type, boolean isWebSite, boolean isStudentResponse) {
		if (!canEditPage())
		{
			if(!isStudentResponse)
				return "permission-failed";
		}

		ToolSession toolSession = sessionManager.getCurrentToolSession();
		List refs = null;
		String id = null;
		String name = null;
		String mimeType = null;
		String description = null;
		String urlAshyi = "";
		if (toolSession.getAttribute(FilePickerHelper.FILE_PICKER_CANCEL) == null && toolSession.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS) != null) {

			refs = (List) toolSession.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
			if (refs == null || refs.size() != 1) {
				return "no-reference";
			}
			Reference ref = (Reference) refs.get(0);
			id = ref.getId();

			description = ref.getProperties().getProperty(ResourceProperties.PROP_DESCRIPTION);

			name = ref.getProperties().getProperty("DAV:displayname");

			// URLs are complex. There are two issues:
			// 1) The stupid helper treats a URL as a file upload. Have to make it a URL type.
			// I suspect we're intended to upload a file from the URL, but I don't think
			// any part of Sakai actually does that. So we reset Sakai's file type to URL
			// 2) Lesson builder needs to know the mime type, to know how to set up the
			// OBJECT or IFRAME. We send that out of band in the "html" field of the 
			// lesson builder item entry. I see no way to do that other than to talk
			// to the server at the other end and see what MIME type it claims.
			mimeType = ref.getProperties().getProperty("DAV:getcontenttype");
			if (mimeType.equals("text/url")) {
				mimeType = null; // use default rules if we can't find it
				String url = null;
				// part 1, fix up the type fields
				boolean pushed = false;
				try {
					pushed = pushAdvisor();
					ContentResourceEdit res = contentHostingService.editResource(id);
					res.setContentType("text/url");
					res.setResourceType("org.sakaiproject.content.types.urlResource");
					url = new String(res.getContent());
					urlAshyi = url;
					contentHostingService.commitResource(res, NotificationService.NOTI_NONE);
				} catch (Exception ignore) {
					return "no-reference";
				}finally {
					if(pushed) popAdvisor();
				}
				// part 2, find the actual data type.
				if (url != null)
					mimeType = getTypeOfUrl(url);
			}

		} else {
			return "cancel";
		}

		boolean pushed = false;
		try {
			pushed = pushAdvisor();
			contentHostingService.checkResource(id);
		} catch (PermissionException e) {
			return "permission-exception";
		} catch (IdUnusedException e) {
			// Typically Means Cancel
			return "cancel";
		} catch (TypeException e) {
			return "type-exception";
		}finally {
			if(pushed) popAdvisor();
		}

		Long itemId = (Long)toolSession.getAttribute(LESSONBUILDER_ITEMID);

		if (!itemOk(itemId))
			return "permission-failed";

		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_CANCEL);
		toolSession.removeAttribute(LESSONBUILDER_ITEMID);

		String[] split = id.split("/");

		if("application/zip".equals(mimeType) && isWebSite) {
			// We need to set the sakaiId to the resource id of the index file
			id = expandZippedResource(id);
			if (id == null)
				return "failed";

			// We set this special type for the html field in the db. This allows us to
			// map an icon onto website links in applicationContext.xml
			// originally it was a special type. The problem is that this is actually
			// an HTML file, and we may have trouble if we don't show it that way
			mimeType = "LBWEBSITE";
			// strip .ZIP off the name
			if (name == null) {
				name = split[split.length - 1];
			}
			if (name.lastIndexOf(".") > 0)
				name = name.substring(0,name.lastIndexOf("."));
		}
		SimplePageItem i;
		if (itemId != null && itemId != -1) {
			i = findItem(itemId);
			i.setSakaiId(id);
			if (mimeType != null)
				i.setHtml(mimeType);
			i.setName(name != null ? name : split[split.length - 1]);
			clearImageSize(i);
		} else {
			i = appendItem(id, (name != null ? name : split[split.length - 1]), type);
			if (mimeType != null) {
				i.setHtml(mimeType);
			}
		}

		i.setDescription(description);
		i.setSameWindow(false);
		update(i);

		String tipo = "Libre";
		if(urlAshyi.contains("ieee") || urlAshyi.contains("acm") || urlAshyi.contains("sciencedirect"))
			tipo = "Paga";
		//Almacena recurso de sakai
		Recurso r = new RecursoImpl();
		if (mimeType.equals("text/url")) 
			r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "text/url", mimeType.replace("text/", ""), nivelActividades,tipo);
		else if (mimeType.equals("text/html"))
			r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "text/html", mimeType.replace("text/", ""), nivelActividades,tipo);
		else
			r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "application", mimeType.replace("application/", ""), nivelActividades,tipo);
		saveCaracteristica(r, true);
		r.setIdRecurso(getUltimoRecurso().getIdRecurso());
		
		//carateristicas de recursos
		CaracteristicaRecurso cR = new CaracteristicasRecursoImpl();
		cR.setIdRecurso(r);
		if(tipo.equalsIgnoreCase("Paga"))
		{
			Caracteristica ca = simplePageToolDao.getCaracteristica("Campus");
			cR.setIdCaracteristica(ca);
		}else
		{
			Caracteristica ca = simplePageToolDao.getCaracteristica("Virtual");
			cR.setIdCaracteristica(ca);
		}
		
		//almacenar caracteritica contexto de recurso
		saveCarateristica(cR);

		//Actualiza actividad
		Actividad ac = simplePageToolDao.getActividad(getUltimaActividad());
		RecursosActividadImpl recurso = new RecursosActividadImpl(ac,r);

		if(isStudentResponse)
		{
			ItemsUsuario itemUsuario=simplePageToolDao.getItemsUsuarioPorIds(IdItemPAshyi, IdItemUAshyi);
			
			if(itemUsuario!=null)
			{
				System.out.println("Respuesta: "+i.getSakaiId());
				//itemUsuario.setRespuesta(i.getURL());
				boolean res = update(itemUsuario);
				if(res)
					System.out.println("Respuesta guardada");
				else
					System.out.println("No se puede guardar la respuesta");
			}
			else
				System.out.println("Item usuario nulo");
			return "successStudentResponse";
		}
		saveCaracteristica(recurso, true);
		//ac.setIdRecurso(getUltimoRecurso());
		//updateActividad(ac);

		//return "importing";
		return "succesRecursoActividad";//volver a otro recurso
	}
	

	// set default for image size for new objects
	/**
	 * Asigna el tama&ntildeo por defecto de im&aacutegenes para nuevos objetos.
	 * @param i el item.
	 */
	private void clearImageSize(SimplePageItem i) {
		// defaults to a fixed width and height, appropriate for some things, but for an
		// image, leave it blank, since browser will then use the native size
		if (i.getType() == SimplePageItem.MULTIMEDIA) {
			if (isImageType(i)) {
				i.setHeight("");
				i.setWidth("");
			}
		}
	}

	// main code for adding a new item to a page
	/**
	 * Agrega un item
	 * @param id
	 * @param name
	 * @param type
	 * @return i el nuevo item.
	 */
	private SimplePageItem appendItem(String id, String name, int type)   {
		// add at the end of the page
		List<SimplePageItem> items = getItemsOnPage(getCurrentPageId());
		// ideally the following should be the same, but there can be odd cases. So be safe
		int size = items.size();
		if (size > 0) {
			int seq = items.get(size-1).getSequence();
			if (seq > size)
				size = seq;
		}
		size++;

		SimplePageItem i = simplePageToolDao.makeItem(getCurrentPageId(), size, type, id, name);

		// defaults to a fixed width and height, appropriate for some things, but for an
		// image, leave it blank, since browser will then use the native size
		//clearImageSize(i);

		saveItem(i);
		return i;
	}

	// nueva actividad
	/**
	 * Agrega una nueva actividad.
	 * @param tipoS
	 * @param item
	 * @param name
	 * @param description
	 * @param nR nivel de Recursividad.
	 * @param dedicacion
	 * @param nivel
	 * @return i la nueva actividad.
	 */
	private Actividad appendActividad(Tipo tipoS, Item item, String name, String description, int nR, int dedicacion, String nivel)   {

		Actividad i = simplePageToolDao.makeActividad(tipoS, item, name,description, nR, dedicacion, nivel);
		//long pageId, int type, String name, String goal, String description, String dI, String dF

		// defaults to a fixed width and height, appropriate for some things, but for an
		// image, leave it blank, since browser will then use the native size
		//clearImageSize(i);

		//saveActividad(i);
		return i;
	}	

	// nueva unidad
	/**
	 * Agrega una nueva actividad de tipo unidad.
	 * @param item
	 * @param name
	 * @param nR nivel de recursividad.
	 * @param dedicacion
	 * @param nivel
	 * @return i la nueva actividad.
	 */
	private Actividad appendActividad(Item item, String name, int nR, String dedicacion, String nivel)   {

		Actividad i = simplePageToolDao.makeActividad(item, name, nR, Integer.valueOf(dedicacion), nivel);
		//long pageId, int type, String name, String goal, String description, String dI, String dF

		// defaults to a fixed width and height, appropriate for some things, but for an
		// image, leave it blank, since browser will then use the native size
		//clearImageSize(i);

		//saveActividad(i);
		return i;
	}

	// nueva caracteristica
	/**
	 * Agrega una nueva caracter&iacutestca.
	 * @param type
	 * @param name
	 * @return i la nueva caracter&iacutestica.
	 */
	private Caracteristica appendCaracteristica(Item type, String name)   {

		Caracteristica i = simplePageToolDao.makeCaracteristica(type, name);
		//long pageId, int type, String name, String goal, String description, String dI, String dF

		// defaults to a fixed width and height, appropriate for some things, but for an
		// image, leave it blank, since browser will then use the native size
		//clearImageSize(i);

		saveCarateristica(i);
		return i;
	}

	/**
	 * Agrega un nuevo tipo.
	 * @param i el tipo.
	 * @return i el tipo.
	 */
	private Tipo appendTipo(Tipo i) {
		saveCaracteristica(i, true);
		return i;
	}

	/**
	 * Agrega una nueva dependenciaActividad.
	 * @param target
	 * @param source
	 * @return i la dependenciaActividad.
	 */
	private boolean addDependenciaActividad(String target, String source)   {

		Actividad tar, sour;
		List<Actividad> list=simplePageToolDao.getActividades(Integer.parseInt(target));
		tar=list.get(0);
		list=simplePageToolDao.getActividades(Integer.parseInt(source));
		sour=list.get(0);

		//mirar si ya existe la dependencia o una entre ambas partes y cambiarla o poner una nueva
		List<DependenciaActividad> depList=simplePageToolDao.getDependenciaActividad(Integer.parseInt(target),Integer.parseInt(source));
		DependenciaActividad dep;
		if(depList.size()>0)
		{
			System.out.println("Ya existe la dependencia");
			return true;
		}
		else
		{
			depList=simplePageToolDao.getDependenciaActividad(Integer.parseInt(source),Integer.parseInt(target));
			if(depList.size()>0)
			{
				dep=new DependenciaActividadImpl(1,tar,sour,0);
				//reemplazar la dependencia en la BD
				simplePageToolDao.deleteObject(dep);
			}
			else
				dep=new DependenciaActividadImpl(1, tar, sour, 0);
		}
		return saveDependenciaActividad(dep, true);
	}
	/**
	 * Actualiza una dependenciaActividad.
	 * @param target
	 * @param source
	 * @return true si fue posible, false si no.
	 */
	private boolean updateDependenciaActividad(String target, String source)   {

		Actividad tar, sour;
		List<Actividad> list=simplePageToolDao.getActividades(Integer.parseInt(target));
		tar=list.get(0);
		list=simplePageToolDao.getActividades(Integer.parseInt(source));
		sour=list.get(0);

		//mirar si ya existe la dependencia o una entre ambas partes y cambiarla o poner una nueva
		List<DependenciaActividad> depList=simplePageToolDao.getDependenciaActividad(Integer.parseInt(target),Integer.parseInt(source));
		DependenciaActividad dep;
		if(depList.size()>0)
		{
			dep=depList.get(0);
			simplePageToolDao.deleteObject(dep);
			return true;
		}
		else
		{
			depList=simplePageToolDao.getDependenciaActividad(Integer.parseInt(source),Integer.parseInt(target));
			if(depList.size()>0)
			{
				dep=new DependenciaActividadImpl(1,sour, tar,0);
				//reemplazar la dependencia en la BD
				simplePageToolDao.deleteObject(dep);
			}
		}
		dep=new DependenciaActividadImpl(1,tar, sour,0);
		return saveDependenciaActividad(dep, true);
	}
	/**
	 * Actualiza una actividadTieneActividad.
	 * @param idSiguienteNivel
	 * @param estiloSiguienteNivel
	 * @param requiresEditPermission
	 * @return true si fue posible, false si no.
	 */
	public boolean updateActividadTieneActividad(String idSiguienteNivel, String estiloSiguienteNivel, boolean requiresEditPermission)   {
		String nombre=getCurrentPageItem(getCurrentPageId()).getName();
		Actividad acPadre = simplePageToolDao.getactividad(nombre, 3);
		int id=acPadre.getIdActividad();
		//		int id=1;
		String err = null;
		List<String>elist = new ArrayList<String>();

		ActividadTieneActividad ata=simplePageToolDao.getActividadTieneActividad(id, Integer.parseInt(idSiguienteNivel)).get(0);
		ata.setEstiloActividadSiguienteNivel(estiloSiguienteNivel);
		return simplePageToolDao.update(ata, elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);
	}

	/**
	 * Returns 0 if user has site.upd or simplepage.upd.
	 * Returns 1 if user is page owner
	 * Returns 2 otherwise
	 * @return
	 */
	public int getEditPrivs() {
		if(editPrivs != null) {
			return editPrivs;
		}
		editPrivs = 2;
		String ref = "/site/" + getCurrentSiteId();
		boolean ok = securityService.unlock(SimplePage.PERMISSION_LESSONBUILDER_UPDATE, ref);
		if(ok) editPrivs = 0;

		SimplePage page = getCurrentPage();
		if(editPrivs != 0 && page != null && getCurrentUserId().equals(page.getOwner())) {
			editPrivs = 1;
		}

		return editPrivs;
	}

	/**
	 * Returns true if user has site.upd, simplepage.upd, or is page owner.
	 * False otherwise.
	 * @return
	 */
	public boolean canEditPage() {
		if(getEditPrivs() <= 1) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * Retorna si el usuario puede ver la p&aacutegina.
	 * @return true si puede, false si no.
	 */
	public boolean canReadPage() {
		String ref = "/site/" + getCurrentSiteId();
		return securityService.unlock(SimplePage.PERMISSION_LESSONBUILDER_READ, ref);
	}
	/**
	 * Retorna si el usuario puede editar la p&aacutegina.
	 * @return true si puede, false si no.
	 */
	public boolean canEditSite() {
		String ref = "/site/" + getCurrentSiteId();
		return securityService.unlock("site.upd", ref);
	}


	/**
	 * Asigna el tool manager.
	 * @param toolManager.
	 */
	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}

	/**
	 * Asigna el securityService
	 * @param service.
	 */
	public void setSecurityService(SecurityService service) {
		securityService = service;
	}

	/**
	 * Asigna el siteService.
	 * @param service
	 */
	public void setSiteService(SiteService service) {
		siteService = service;
	}
	/**
	 * Asigna el controlador de la base de datos de Sakai.
	 * @param dao.
	 */
	public void setSimplePageToolDao(Object dao) {
		simplePageToolDao = (SimplePageToolDao) dao;
	}
	/**
	 * Retorna los items de una p&aacutegina.
	 * @param pageid
	 * @return items los items.
	 */
	public List<SimplePageItem>  getItemsOnPage(long pageid) {
		List<SimplePageItem>items = itemsCache.get(pageid);
		if (items != null)
			return items;

		items = simplePageToolDao.findItemsOnPage(pageid);

		// This code adds a global comments tool to the bottom of each
		// student page, but only if there's something else on the page
		// already and the instructor has enabled the option.
		if(items.size() > 0) {
			SimplePage page = getPage(pageid);
			if(page.getOwner() != null) {
				SimpleStudentPage student = simplePageToolDao.findStudentPage(page.getTopParent());
				if(student != null && student.getCommentsSection() != null) {
					SimplePageItem item = simplePageToolDao.findItem(student.getItemId());
					if(item != null && item.getShowComments() != null && item.getShowComments()) {
						items.add(0, simplePageToolDao.findItem(student.getCommentsSection()));
					}
				}
			}
		}

		for (SimplePageItem item: items) {
			itemCache.put(item.getId(), item);
		}

		itemsCache.put(pageid, items);
		return items;
	}
	/**
	 * Elimina un item de la p&aacutegina.
	 * @return el resultado.
	 */
	public String deleteItem()  {
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		SimplePageItem i = findItem(itemId);

		int seq = i.getSequence();

		boolean b = false;

		// if access controlled, clear it before deleting item
		if (i.isPrerequisite()) {
			i.setPrerequisite(false);
			checkControlGroup(i, false);
		}

		// Also delete gradebook entries
		if(i.getGradebookId() != null) {
			gradebookIfc.removeExternalAssessment(getCurrentSiteId(), i.getGradebookId());
		}

		if(i.getAltGradebook() != null) {
			gradebookIfc.removeExternalAssessment(getCurrentSiteId(), i.getAltGradebook());
		}


		b = simplePageToolDao.deleteItem(i);

		if (b) {
			List<SimplePageItem> list = getItemsOnPage(getCurrentPageId());
			for (SimplePageItem item : list) {
				if (item.getSequence() > seq) {
					item.setSequence(item.getSequence() - 1);
					update(item);
				}				
			}

			//eliminar unidad y dejar las actividades
			Actividad a = simplePageToolDao.getactividad(i.getName(), 3);
			if(!a.getNombre().equals("Does not exist"))
			{					
				deleteUnidad(a);
			}

			return "successDelete";
		} else {
			log.warn("deleteItem error deleting Item: " + itemId);
			return "failure";
		}
	}

	// not clear whether it's worth caching this. The first time it's called for a site
	// the pages are fetched. Beyond that it's a linear search of pages that are in memory
	// ids are sakai.assignment.grades, sakai.samigo, sakai.mneme, sakai.forums, sakai.jforum.tool
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Busca la p&aacuteginas actual en memoria.
	 * @param commonToolId
	 * @return la p&aacutegina.
	 */
	public String getCurrentTool(String commonToolId) {
		Site site = getCurrentSite();
		ToolConfiguration tool = site.getToolForCommonId(commonToolId);
		if (tool == null)
			return null;
		return tool.getId();
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Retorna el nombre de la p&aacutegina actual en memoria.
	 * @param commonToolId
	 * @return el nombre de la p&aacutegina.
	 */
	public String getCurrentToolTitle(String commonToolId) {
		Site site = getCurrentSite();
		ToolConfiguration tool = site.getToolForCommonId(commonToolId);
		if (tool == null)
			return null;
		return tool.getTitle();
	}
	/**
	 * Retorna la p&aacutegina actual.
	 * @return currentSite la p&aacutegina.
	 */
	public Site getCurrentSite() {
		if (currentSite != null) // cached value
			return currentSite;

		try {
			currentSite = siteService.getSite(getCurrentSiteId());
		} catch (Exception impossible) {
			impossible.printStackTrace();
		}

		return currentSite;
	}

	// find page to show in next link
	// If the current page is a LB page, and it has a single "next" link on it, use that

	//  If the current page is a LB page, and it has more than one
	//  "next" link on it, show no next. If there's more than one
	//  next, this is probably a page with a branching question, in
	//  which case there really isn't a single next.

	// If the current page is a LB page, and it is not finished (i.e.
	// there are required items not done), there is no next, or next
	// is grayed out.

	//  Otherwise look at the page above in the breadcrumbs. If the
	//  next item on the page is not an inline item, and the item is
	//  available, next should be the next item on that page. (If
	//  it's an inline item we need to go back to the page above so
	//  they can see the inline item next.)

	// If the current page is something like a test, there is an
	// issue. What if the next item is not available when the page is
	// displayed, because it requires that you get a passing score on
	// the current test? For the moment, if the current item is required
	// but the next is not available, show the link but test it when it's
	// clicked.

	// TODO: showpage and showitem, implement next. Should not pass a
	// path argument. That gives next. If there's a pop we do it.
	//    in showitem, check if it's available, if not, show an error
	// with a link to the page above.

	// return: new item on same level, null if none, the item arg if need to go up a level
	//   java really needs to be able to return more than one thing, item == item is being
	//   used as a flag to return up a level
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Encuentra la siguiente p&aacutegina a partir de un item.
	 * @param item 
	 * @return nextPage siguiente p&aacutegina.
	 */
	public SimplePageItem findNextPage(SimplePageItem item) {
		if(item.getType() == SimplePageItem.PAGE) {
			Long pageId = Long.valueOf(item.getSakaiId());
			List<SimplePageItem> items = getItemsOnPage(pageId);
			int nexts = 0;
			SimplePageItem nextPage = null;
			for (SimplePageItem i: items) {
				if (i.getType() == SimplePageItem.PAGE && i.getNextPage()) {
					nextPage = i;
					nexts++;
				}
			}
			// if next, use it; no next if not ready
			if (nexts == 1) {
				if (isItemAvailable(nextPage, pageId))
					return nextPage;
				return null;
			}
			// more than one, presumably you're intended to pick one of them, and
			// there is no generic next
			if (nexts > 1) {
				return null;
			}

			// if this is a next page, if there's no explicIt next it's
			// not clear that it makes sense to go anywhere. it's kind of
			// detached from its parent
			if (item.getNextPage())
				return null;

			// here for a page with no explicit next. Treat like any other item
			// except that we need to compute path op. Page must be complete or we
			// would have returned null.

		}else if(item.getType() == SimplePageItem.STUDENT_CONTENT) {
			return null;
		}

		// this should be a top level page. We're not currently doing next for that.
		// we have to trap it because now and then we have items with bogus 0 page ID, so we
		// could get a spurious next item
		if (item.getPageId() == 0L)
			return null;

		// see if there's an actual next we can go to, otherwise calling page
		SimplePageItem nextItem = simplePageToolDao.findNextItemOnPage(item.getPageId(), item.getSequence());

		// skip items which won't show because user isn't in the group
		while (nextItem != null && !isItemVisible(nextItem)) {
			nextItem = simplePageToolDao.findNextItemOnPage(nextItem.getPageId(), nextItem.getSequence());		
		}

		boolean available = false;
		if (nextItem != null) {

			int itemType = nextItem.getType();
			if (itemType == SimplePageItem.ASSIGNMENT ||
					itemType == SimplePageItem.ASSESSMENT ||
					itemType == SimplePageItem.FORUM ||
					itemType == SimplePageItem.PAGE ||
					itemType == SimplePageItem.BLTI ||
					itemType == SimplePageItem.RESOURCE && nextItem.isSameWindow()) {
				// it's easy if the next item is available. If it's not, then
				// we need to see if everything other than this item is done and
				// this one is required. In that case the issue must be that this
				// one isn't finished yet. Let's assume the user is going to finish
				// this one. We'll verify that when he actually does the next;
				if (isItemAvailable(nextItem, item.getPageId()) ||
						item.isRequired() && wouldItemBeAvailable(item, item.getPageId()))
					return nextItem;
			}
		}

		// otherwise return to calling page
		return item; // special flag
	}

	// corresponding code for outputting the link
	// perhaps I should adjust the definition of path so that normal items show on it and not just pages
	//   but at the moment path is just the pages. So when we're in a normal item, it doesn't show.
	//   that means that as we do Next between items and pages, when we go to a page it gets pushed
	//   on and when we go from a page to an item, the page has to be popped off.
	/**
	 * Agrega un nuevo link.
	 * @param tofill
	 * @param item
	 */
	public void addNextLink(UIContainer tofill, SimplePageItem item) {
		SimplePageItem nextItem = findNextPage(item);
		if (nextItem == item) { // that we need to go up a level
			List<PathEntry> path = (List<PathEntry>)sessionManager.getCurrentToolSession().getAttribute(LESSONBUILDER_PATH);
			int top;
			if (path == null)
				top = -1;
			else
				top = path.size()-1;
			// if we're on a page, have to pop it off first
			// for a normal item the end of the path already is the page above
			if (item.getType() == SimplePageItem.PAGE)
				top--;
			if (top >= 0) {
				PathEntry e = path.get(top);
				GeneralViewParameters view = new GeneralViewParameters(ShowPageProducer.VIEW_ID);
				view.setSendingPage(e.pageId);
				view.setItemId(e.pageItemId);
				view.setPath(Integer.toString(top));
				UIInternalLink.make(tofill, "next", messageLocator.getMessage("simplepage.next"), view);
				UIInternalLink.make(tofill, "next1", messageLocator.getMessage("simplepage.next"), view);
			}
		} else  if (nextItem != null) {
			GeneralViewParameters view = new GeneralViewParameters();
			int itemType = nextItem.getType();
			if (itemType == SimplePageItem.PAGE) {
				view.setSendingPage(Long.valueOf(nextItem.getSakaiId()));
				view.viewID = ShowPageProducer.VIEW_ID;
				if (item.getType() == SimplePageItem.PAGE)
					view.setPath("next");  // page to page, just a next
				else
					view.setPath("push");  // item to page, have to push the page
			} else if (itemType == SimplePageItem.RESOURCE) { /// must be a same page resource
				view.setSendingPage(Long.valueOf(item.getPageId()));
				// to the check. We need the check to set access control appropriately
				// if the user has passed.
				if (!isItemAvailable(nextItem, nextItem.getPageId()))
					view.setRecheck("true");
				view.setSource(nextItem.getItemURL(getCurrentSiteId(), getCurrentPage().getOwner()));
				view.viewID = ShowItemProducer.VIEW_ID;
			} else {
				view.setSendingPage(Long.valueOf(item.getPageId()));
				LessonEntity lessonEntity = null;
				switch (nextItem.getType()) {
				case SimplePageItem.ASSIGNMENT:
					lessonEntity = assignmentEntity.getEntity(nextItem.getSakaiId()); break;
				case SimplePageItem.ASSESSMENT:
					view.setClearAttr("LESSONBUILDER_RETURNURL_SAMIGO");
					lessonEntity = quizEntity.getEntity(nextItem.getSakaiId()); break;
				case SimplePageItem.FORUM:
					lessonEntity = forumEntity.getEntity(nextItem.getSakaiId()); break;
				case SimplePageItem.BLTI:
					if (bltiEntity != null)
						lessonEntity = bltiEntity.getEntity(nextItem.getSakaiId()); break;
				}
				// normally we won't send someone to an item that
				// isn't available. But if the current item is a test, etc, we can't
				// know whether the user will pass it, so we have to ask ShowItem to
				// to the check. We need the check to set access control appropriately
				// if the user has passed.
				if (!isItemAvailable(nextItem, nextItem.getPageId()))
					view.setRecheck("true");
				view.setSource((lessonEntity==null)?"dummy":lessonEntity.getUrl());
				if (item.getType() == SimplePageItem.PAGE)
					view.setPath("pop");  // now on a have, have to pop it off
				view.viewID = ShowItemProducer.VIEW_ID;
			}

			view.setItemId(nextItem.getId());
			view.setBackPath("push");
			UIInternalLink.make(tofill, "next", messageLocator.getMessage("simplepage.next"), view);
			UIInternalLink.make(tofill, "next1", messageLocator.getMessage("simplepage.next"), view);
		}
	}


	/**
	 *Because of the existence of chains of "next" pages, there's no static approach that will find
	 back links. Thus we keep track of the actual path the user has followed. However we have to
	 prune both path and back path when we return to an item that's already on them to avoid
	 loops of various kinds.
	 * Agrega un link previo.
	 * @param tofill
	 * @param item
	 */
	public void addPrevLink(UIContainer tofill, SimplePageItem item) {
		List<PathEntry> backPath = (List<PathEntry>)sessionManager.getCurrentToolSession().getAttribute(LESSONBUILDER_BACKPATH);
		List<PathEntry> path = (List<PathEntry>)sessionManager.getCurrentToolSession().getAttribute(LESSONBUILDER_PATH);

		// current item is last on path, so need one before that
		if (backPath == null || backPath.size() < 2)
			return;

		PathEntry prevEntry = backPath.get(backPath.size()-2);
		SimplePageItem prevItem = findItem(prevEntry.pageItemId);

		GeneralViewParameters view = new GeneralViewParameters();
		int itemType = prevItem.getType();
		if (itemType == SimplePageItem.PAGE) {
			view.setSendingPage(Long.valueOf(prevItem.getSakaiId()));
			view.viewID = ShowPageProducer.VIEW_ID;
			// are we returning to a page? If so use existing path entry
			int lastEntry = -1;
			int i = 0;
			long prevItemId = prevEntry.pageItemId;
			for (PathEntry entry: path) {
				if (entry.pageItemId == prevItemId)
					lastEntry = i;
				i++;
			}
			if (lastEntry >= 0)
				view.setPath(Integer.toString(lastEntry));
			else if (item.getType() == SimplePageItem.PAGE)
				view.setPath("next");  // page to page, just a next
			else
				view.setPath("push");  // item to page, have to push the page
		} else if (itemType == SimplePageItem.RESOURCE) { // must be a samepage resource
			view.setSendingPage(Long.valueOf(item.getPageId()));
			view.setSource(prevItem.getItemURL(getCurrentSiteId(),getCurrentPage().getOwner()));
			view.viewID = ShowItemProducer.VIEW_ID;
		}else if(itemType == SimplePageItem.STUDENT_CONTENT) {
			view.setSendingPage(prevEntry.pageId);
			view.setItemId(prevEntry.pageItemId);
			view.viewID =ShowPageProducer.VIEW_ID;

			if(item.getType() == SimplePageItem.PAGE) {
				view.setPath("pop");
			}else {
				view.setPath("next");
			}
		} else {
			view.setSendingPage(Long.valueOf(item.getPageId()));
			LessonEntity lessonEntity = null;
			switch (prevItem.getType()) {
			case SimplePageItem.ASSIGNMENT:
				lessonEntity = assignmentEntity.getEntity(prevItem.getSakaiId()); break;
			case SimplePageItem.ASSESSMENT:
				view.setClearAttr("LESSONBUILDER_RETURNURL_SAMIGO");
				lessonEntity = quizEntity.getEntity(prevItem.getSakaiId()); break;
			case SimplePageItem.FORUM:
				lessonEntity = forumEntity.getEntity(prevItem.getSakaiId()); break;
			case SimplePageItem.BLTI:
				if (bltiEntity != null)
					lessonEntity = bltiEntity.getEntity(prevItem.getSakaiId()); break;
			}
			view.setSource((lessonEntity==null)?"dummy":lessonEntity.getUrl());
			if (item.getType() == SimplePageItem.PAGE)
				view.setPath("pop");  // now on a page, have to pop it off
			view.viewID = ShowItemProducer.VIEW_ID;
		}
		view.setItemId(prevItem.getId());
		view.setBackPath("pop");
		UIInternalLink.make(tofill, "prev", messageLocator.getMessage("simplepage.back"), view);
		UIInternalLink.make(tofill, "prev1", messageLocator.getMessage("simplepage.back"), view);
	}

	/**
	 * Agrega un link a la p&aacutegina anterior.
	 * @param tofill
	 * @param item
	 */
	public void addMio(UIContainer tofill, SimplePageItem item) {
		List<PathEntry> backPath = (List<PathEntry>)sessionManager.getCurrentToolSession().getAttribute(LESSONBUILDER_BACKPATH);
		// current item is last on path, so need one before that

		PathEntry prevEntry = backPath.get(backPath.size()-1);
		SimplePageItem prevItem = findItem(prevEntry.pageItemId);

		GeneralViewParameters view = new GeneralViewParameters();
		view.setSendingPage(Long.valueOf(getCurrentPageId()));
		view.viewID = CaracteristicaProducer.VIEW_ID;
		view.setItemId(prevEntry.pageItemId);
		view.setBackPath("pop");
		UIInternalLink.make(tofill, "edit1", messageLocator.getMessage("simplepage.back"), view);
		//UIInternalLink.make(tofill, "edit1", messageLocator.getMessage("simplepage.back"), view);
	}

	/**
	 * Retorna el id del sitio actual.
	 * @return currentSiteId
	 */
	public String getCurrentSiteId() {
		if (currentSiteId != null)
			return currentSiteId;
		try {
			currentSiteId = toolManager.getCurrentPlacement().getContext();
			return currentSiteId;
		} catch (Exception impossible) {
			return null;
		}
	}

	// so access can inject the siteid
	/**
	 * Asigna el id del sitio actual.
	 * @param currentSiteId
	 */
	public void setCurrentSiteId(String siteId) {       
		currentSiteId = siteId;
	}

	// recall that code typically operates on a "current page." See below for
	// the code that sets a new current page. We also have a current item, which
	// is the item defining the page. I.e. if the page is a subpage of another
	// one, this is the item on the parent page pointing to this page.  If it's
	// a top-level page, it's a dummy item.  The item is needed in order to do
	// access checks. Whether an item is required, etc, is stored in the item.
	// in theory a page could be called from several other pages, with different
	// access control parameters. So we need to know the actual item on the page
	// page from which this page was called.

	// we need to track the pageitem because references to the same page can appear
	// in several places. In theory each one could have different status of availability
	// so we need to know which in order to check availability
	/**
	 * Actualiza el id del item de la p&aacutegina.
	 * @param item el id del item.
	 * @throws PermissionException
	 */
	public void updatePageItem(long item) throws PermissionException {
		SimplePageItem i = findItem(item);
		if (i != null) {
			if (i.getType() != SimplePageItem.STUDENT_CONTENT && (long)currentPageId != (long)Long.valueOf(i.getSakaiId())) {
				log.warn("updatePageItem permission failure " + i + " " + Long.valueOf(i.getSakaiId()) + " " + currentPageId);
				throw new PermissionException(getCurrentUserId(), "set item", Long.toString(item));
			}
		}

		currentPageItemId = item;
		sessionManager.getCurrentToolSession().setAttribute("current-pagetool-item", item);
	}

	// update our concept of the current page. it is imperative to make sure the page is in
	// the current site, or people could hack on other people's pages

	// if you call updatePageObject, consider whether you need to call updatePageItem as well
	// this combines two functions, so maybe not, but any time you're going to a new page 
	// you should do both. Make sure all Producers set the page to the one they will work on
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Actualiza la p&aacutegina.
	 * @param l el id de la p&aacutegina.
	 * @param save
	 */
	public void updatePageObject(long l, boolean save) throws PermissionException {
		if (l != previousPageId) {
			currentPage = getPage(l);
			String siteId = getCurrentSiteId();

			// get a rare error here, trying to debug it
			if(currentPage == null || currentPage.getSiteId() == null) {
				throw new PermissionException(getCurrentUserId(), "set page", Long.toString(l));
			}

			// page should always be in this site, or someone is gaming us
			if (!currentPage.getSiteId().equals(siteId))
				throw new PermissionException(getCurrentUserId(), "set page", Long.toString(l));
			previousPageId = l;

			if(save) {
				sessionManager.getCurrentToolSession().setAttribute("current-pagetool-page", l);
			}

			currentPageId = (Long)l;
		}
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Actualiza la p&aacutegina.
	 * @param l el id de la p&aacutegina.
	 * @throws PermissionException
	 */
	public void updatePageObject(long l) throws PermissionException {
		updatePageObject(l, true);
	}

	// if tool was reset, return last page from previous session, so we can give the user
	// a chance to go back
	/**
	 * Retorna la &uacuteltima p&aacutegina de la &uacuteltima sesi&oacuten si el tool fu&eacute reseteado.
	 * @return la p&aacutegina.
	 */
	public SimplePageToolDao.PageData toolWasReset() {
		if (sessionManager.getCurrentToolSession().getAttribute("current-pagetool-page") == null) {
			// no page in session, which means it was reset
			String toolId = ((ToolConfiguration) toolManager.getCurrentPlacement()).getPageId();
			return simplePageToolDao.findMostRecentlyVisitedPage(getCurrentUserId(), toolId);
		} else
			return null;
	}

	// ought to be simple, but this is typically called at the beginning of a producer, when
	// the current page isn't set yet. So if there isn't one, we use the session variable
	// to tell us what the current page is. Note that a user can add our tool using Site
	// Info. Site info knows nothing about us, so it will make an entry for the page without
	// creating it. When the user then tries to go to the page, this code will be the firsst
	// to notice it. Hence we have to create pages that don't exist
	/**
	 * Retorna el id de la p&aacutegina actual.
	 * @return currentPageId
	 */
	public long getCurrentPageId()  {
		// return ((ToolConfiguration)toolManager.getCurrentPlacement()).getPageId();

		if (currentPageId != null)
			return (long)currentPageId;

		Placement placement = toolManager.getCurrentPlacement();
		// See whether the tool is disabled in Sakai site information
		// you can either hide or disable a tool. Our page hidden is
		// really a disable, so we sync Sakai's disabled with our hidden
		// we're only checking when you first go into a tool
		Properties roleConfig = placement.getPlacementConfig();
		String roleList = roleConfig.getProperty("functions.require");
		boolean siteHidden = (roleList != null && roleList.indexOf(SITE_UPD) > -1);

		// Let's go back to where we were last time.
		Long l = (Long) sessionManager.getCurrentToolSession().getAttribute("current-pagetool-page");
		if (l != null && l != 0) {
			try {
				updatePageObject(l);
				Long i = (Long) sessionManager.getCurrentToolSession().getAttribute("current-pagetool-item");
				if (i != null && i != 0)
					updatePageItem(i);
			} catch (PermissionException e) {
				e.printStackTrace();
				log.warn("getCurrentPageId Permission failed setting to item in toolsession");
				return 0;
			}

			// currentPage should now be set
			syncHidden(currentPage, siteHidden);

			return l;
		} else {
			// No recent activity. Let's go to the top level page.

			l = simplePageToolDao.getTopLevelPageId(((ToolConfiguration) placement).getPageId());;
			// l = simplePageToolDao.getTopLevelPageId(((ToolConfiguration) toolManager.getCurrentPlacement()).getPageId());

			if (l != null) {
				try {
					updatePageObject(l);
					// this should exist except if the page was created by old code
					SimplePageItem i = simplePageToolDao.findTopLevelPageItemBySakaiId(String.valueOf(l));
					if (i == null) {
						// and dummy item, the site is the notional top level page
						i = simplePageToolDao.makeItem(0, 0, SimplePageItem.PAGE, l.toString(), currentPage.getTitle());
						saveItem(i);
					}
					updatePageItem(i.getId());
				} catch (PermissionException e) {
					log.warn("getCurrentPageId Permission failed setting to page in toolsession");
					return 0;
				}

				// currentPage should now be set
				syncHidden(currentPage, siteHidden);

				return l;
			} else {
				// No page found. Let's make a new one.
				String toolId = ((ToolConfiguration) toolManager.getCurrentPlacement()).getPageId();
				String title = getCurrentSite().getPage(toolId).getTitle(); // Use title supplied

				// during creation
				SimplePage page = simplePageToolDao.makePage(toolId, getCurrentSiteId(), title, null, null);
				if (!saveItem(page)) {
					currentPage = null;
					return 0;
				}

				try {
					updatePageObject(page.getPageId());
					l = page.getPageId();

					// and dummy item, the site is the notional top level page
					SimplePageItem i = simplePageToolDao.makeItem(0, 0, SimplePageItem.PAGE, l.toString(), title);
					saveItem(i);
					updatePageItem(i.getId());
				} catch (PermissionException e) {
					log.warn("getCurrentPageId Permission failed setting to new page");
					return 0;
				}

				// currentPage should now be set
				syncHidden(currentPage, siteHidden);

				return l;
			}
		}
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param page
	 * @param siteHidden
	 */
	private void syncHidden (SimplePage page, boolean siteHidden) {
		// only do it for top level pages
		if (page != null && page.getParent() == null) {
			// hidden in site
			if (siteHidden != page.isHidden()) {
				page.setHidden(siteHidden);
				// use quick, as we don't want permission check. even normal users can do this
				simplePageToolDao.quickUpdate(page);
			}
		}
	}
	/**
	 * Asigna el id de la p&aacutegina actual.
	 * @param p
	 */
	public void setCurrentPageId(long p) {
		currentPageId = p;
	}

	// current page must be set. 
	/**
	 * Retorna el item de la p&aacutegina actual.
	 * @param itemId identificador del item.
	 * @return ret el item.
	 */
	public SimplePageItem getCurrentPageItem(Long itemId)  {
		// if itemId is known, this is easy. but check to make sure it's
		// actually this page, to prevent the user gaming us

		if (itemId == null || itemId == -1) 
			itemId = currentPageItemId;

		if (itemId != null && itemId != -1) {
			SimplePageItem ret = findItem(itemId);
			if (ret != null && (ret.getSakaiId().equals(Long.toString(getCurrentPageId())) || ret.getType() == SimplePageItem.STUDENT_CONTENT)) {
				try {
					updatePageItem(ret.getId());
				} catch (PermissionException e) {
					log.warn("getCurrentPageItem Permission failed setting to specified item");
					return null;
				}
				return ret;
			} else {
				return null;
			}
		}
		// else must be a top level item
		SimplePage page = getPage(getCurrentPageId());

		SimplePageItem ret = simplePageToolDao.findTopLevelPageItemBySakaiId(Long.toString(getCurrentPageId()));

		if(ret == null && page.getOwner() != null) {
			ret = simplePageToolDao.findItemFromStudentPage(page.getPageId());
		}
		if (ret == null)
			return null;
		try {
			updatePageItem(ret.getId());
		} catch (PermissionException e) {
			log.warn("getCurrentPageItem Permission failed setting to top level page in tool session");
			return null;
		}
		return ret;
	}

	// called at the start of showpageproducer, with page info for the page about to be displayed
	// updates the breadcrumbs, which are kept in session variables.
	// returns string version of the new path
	/**
	 * Retorna un String con la ruta nueva de la p&aacutegina que se va a mostrar.
	 * @param op
	 * @param pageId
	 * @param pageItemId
	 * @param title
	 * @return path la ruta.
	 */
	public String adjustPath(String op, Long pageId, Long pageItemId, String title) {
		List<PathEntry> path = (List<PathEntry>)sessionManager.getCurrentToolSession().getAttribute(LESSONBUILDER_PATH);

		// if no current path, op doesn't matter. we can just do the current page
		if (path == null || path.size() == 0) {
			PathEntry entry = new PathEntry();
			entry.pageId = pageId;
			entry.pageItemId = pageItemId;
			entry.title = title;
			path = new ArrayList<PathEntry>();
			path.add(entry);
		} else if (path.get(path.size()-1).pageId.equals(pageId)) {
			// nothing. we're already there. this is to prevent 
			// oddities if we refresh the page
		} else if (op == null || op.equals("") || op.equals("next")) {
			PathEntry entry = path.get(path.size()-1); // overwrite last item
			entry.pageId = pageId;
			entry.pageItemId = pageItemId;
			entry.title = title;
		} else if (op.equals("push")) {
			// a subpage
			PathEntry entry = new PathEntry();
			entry.pageId = pageId;
			entry.pageItemId = pageItemId;
			entry.title = title;
			path.add(entry);  // put it on the end
		} else if (op.equals("pop")) {
			// a subpage
			path.remove(path.size()-1);
		} else if (op.startsWith("log")) {
			// set path to what was saved in the last log entry for this item
			// this is used for users who go directly to a page from the 
			// main list of pages.
			path = new ArrayList<PathEntry>();
			SimplePageLogEntry logEntry = getLogEntry(pageItemId);
			if (logEntry != null) {
				String items[] = null;
				if (logEntry.getPath() != null)
					items = split(logEntry.getPath(), ",");
				if (items != null) {
					for(String s: items) {
						// don't see how this could happen, but it did
						if (s.trim().equals("")) {
							log.warn("adjustPath attempt to set invalid path: invalid item: " + op + ":" + logEntry.getPath());
							return null;
						}
						SimplePageItem i = findItem(Long.valueOf(s));
						if (i == null || i.getType() != SimplePageItem.PAGE) {
							log.warn("adjustPath attempt to set invalid path: invalid item: " + op);
							return null;
						}
						SimplePage p = getPage(Long.valueOf(i.getSakaiId()));
						if (p == null || !currentPage.getSiteId().equals(p.getSiteId())) {
							log.warn("adjustPath attempt to set invalid path: invalid page: " + op);
							return null;
						}
						PathEntry entry = new PathEntry();
						entry.pageId = p.getPageId();
						entry.pageItemId = i.getId();
						entry.title = i.getName();
						path.add(entry);
					}
				}
			}
		} else {
			int index = Integer.valueOf(op); // better be number
			if (index < path.size()) {
				// if we're going back, this should actually
				// be redundant
				PathEntry entry = path.get(index); // back to specified item
				entry.pageId = pageId;
				entry.pageItemId = pageItemId;
				entry.title = title;
				if (index < (path.size()-1))
					path.subList(index+1, path.size()).clear();
			}
		}

		// have new path; set it in session variable
		sessionManager.getCurrentToolSession().setAttribute(LESSONBUILDER_PATH, path);

		// and make string representation to return
		String ret = null;
		for (PathEntry entry: path) {
			String itemString = Long.toString(entry.pageItemId);
			if (ret == null)
				ret = itemString;
			else
				ret = ret + "," + itemString;
		}
		if (ret == null)
			ret = "";
		return ret;
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param op
	 * @param pageId
	 * @param pageItemId
	 * @param title
	 */
	public void adjustBackPath(String op, Long pageId, Long pageItemId, String title) {

		List<PathEntry> backPath = (List<PathEntry>)sessionManager.getCurrentToolSession().getAttribute(LESSONBUILDER_BACKPATH);
		if (backPath == null)
			backPath = new ArrayList<PathEntry>();

		// default case going directly to something.
		// normally we want to push it, but if it's already there,
		// we're going back to it, use the old one
		if (op == null || op.equals("")) {
			// is it there already? Some would argue that we should use the first occurrence
			int lastEntry = -1;
			int i = 0;
			long itemId = pageItemId;  // to avoid having to use equals
			for (PathEntry entry: backPath) {
				if (entry.pageItemId == itemId)
					lastEntry = i;
				i++;
			}
			if (lastEntry >= 0) {
				// yes, back up to that entry
				if (lastEntry < (backPath.size()-1))
					backPath.subList(lastEntry+1, backPath.size()).clear();
				return;
			}
			// no fall through and push the new item
		}


		if (op.equals("pop")) {
			if (backPath.size() > 0)
				backPath.remove(backPath.size()-1);
		} else {  // push or no operation
			PathEntry entry = new PathEntry();
			entry.pageId = pageId;
			entry.pageItemId = pageItemId;
			entry.title = title;
			backPath.add(entry);
		}

		// have new path; set it in session variable
		sessionManager.getCurrentToolSession().setAttribute(LESSONBUILDER_BACKPATH, backPath);
	}


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Asigna el t&iacutetulo de la unidad.
	 * @param st
	 */
	public void setSubpageTitle(String st) {
		subpageTitle = st;
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Asigna el siguiente a la unidad.
	 * @param s
	 */
	public void setSubpageNext(boolean s) {
		subpageNext = s;
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Asigna el t&iacutetulo de la unidad.
	 * @param st
	 */
	public void setSubpageButton(boolean s) {
		subpageButton = s;
	}

	// called from "select page" dialog in Reorder to insert items from anoher page
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return 
	 */
	public String selectPage()   {

		if (!canEditPage())
			return "permission-failed";

		ToolSession toolSession = sessionManager.getCurrentToolSession();
		toolSession.setAttribute("lessonbuilder.selectedpage", selectedEntity);

		// doesn't do anything but call back reorder
		// the submit sets selectedEntity, which is passed to Reorder by addResultingViewBinding

		return "selectpage";
	}
	/**
	 * Crea un nuevo objeto.
	 * @return respuesta el resultado de la operaci&oacuten.
	 */
	public String createnuevoObjetivo()
	{
		String respuesta = "failure";
		if(subpageGoal!=null)
		{
			objetivoCurso = subpageGoal;
			addObjetivoCurso();
			addObjetivoTema();
			respuesta = "success";
		}
		else if(subpageGoalUnidad != null)
		{
			objetivoCurso = subpageGoalUnidad;
			addObjetivoCurso();
			addObjetivoActividad();
			respuesta = "successActividad";
		}

		return respuesta;
	}

	// called from "add subpage" dialog
	// create if itemId == null or -1, else update existing
	/**
	 * Crea un subpage.
	 * @return respuesta el resultado de la operaci&oacuten.
	 */
	public String createSubpage()   {

		//		if (!itemOk(itemId))
		//		    return "permission-failed";
		//		if (!canEditPage())
		//		    return "permission-failed";

		String title = subpageTitle;

		boolean makeNewPage = (selectedEntity == null || selectedEntity.length() == 0);
		boolean makeNewItem = (itemId == null || itemId == -1);

		// make sure the page is legit
		if (!makeNewPage) {
			SimplePage p = getPage(Long.valueOf(selectedEntity));
			if (p == null || !getCurrentSiteId().equals(p.getSiteId())) {
				log.warn("addpage tried to add invalid page: " + selectedEntity);
				return "invalidpage";
			}
		}

		if ((title == null || title.length() == 0) &&
				(selectedEntity == null || selectedEntity.length() == 0)) {
			return "notitle";
		}

		SimplePage page = getCurrentPage();

		Long parent = page.getPageId();
		Long topParent = page.getTopParent();

		// Allows students to make subpages of Student Content pages
		String owner = page.getOwner();
		Boolean groupOwned = page.isGroupOwned();

		if (topParent == null) {
			topParent = parent;
		}

		String toolId = ((ToolConfiguration) toolManager.getCurrentPlacement()).getPageId();
		SimplePage subpage = null;
		if (makeNewPage) {
			subpage = simplePageToolDao.makePage(toolId, getCurrentSiteId(), title, parent, topParent);
			subpage.setOwner(owner);
			subpage.setGroupOwned(groupOwned);
			saveItem(subpage);
			selectedEntity = String.valueOf(subpage.getPageId());
		} else {
			subpage = getPage(Long.valueOf(selectedEntity));
		}

		SimplePageItem i = null;
		if (makeNewItem)
			i = appendItem(selectedEntity, subpage.getTitle(), SimplePageItem.PAGE);
		else
			i = findItem(itemId);

		if (i == null)
			return "failure";

		if (makeNewItem) {
			i.setNextPage(subpageNext);
			if (subpageButton)
				i.setFormat("button");
			else
				i.setFormat("");
		} else {
			// when itemid is specified, we're changing pages for existing entry
			i.setSakaiId(selectedEntity);
			i.setName(subpage.getTitle());
		}

		update(i);

		if (makeNewPage) {
			// if creating new entry, go to it
			try {
				updatePageObject(subpage.getPageId());
				updatePageItem(i.getId());
			} catch (PermissionException e) {
				log.warn("createSubpage permission failed going to new page");
				return "failed";
			}
			adjustPath((subpageNext ? "next" : "push"), subpage.getPageId(), i.getId(), i.getName());

			submit();

		}

		//Datos de actividad tipo unidad didactica
		tipoUnidad = 2;
		nomberA = subpageTitle;
		//objetivoA = subpageGoal;	

		addActividad();

		return "successObjetivos";
		//return "success";
	}


	/**
	 * Elimina las p&aacuteginas del sitio.
	 * @return resultado de la operaci&oacuten.
	 */
	public String deletePages() {
		if (getEditPrivs() != 0)
			return "permission-failed";

		String siteId = getCurrentSiteId();

		for (int i = 0; i < selectedEntities.length; i++) {
			SimplePage target = getPage(Long.valueOf(selectedEntities[i]));
			if (target != null) {
				if (!target.getSiteId().equals(siteId)) {
					return "permission-failed";
				}

				// delete all the items on the page
				List<SimplePageItem> items = getItemsOnPage(target.getPageId());
				for (SimplePageItem item: items) {
					// if access controlled, clear it before deleting item
					if (item.isPrerequisite()) {
						item.setPrerequisite(false);
						checkControlGroup(item, false);
					}
					simplePageToolDao.deleteItem(item);
				}

				// remove from gradebook
				gradebookIfc.removeExternalAssessment(siteId, "lesson-builder:" + target.getPageId());

				// remove fake item if it's top level. We won't see it if it's still active
				// so this means the user has removed it in site info
				SimplePageItem item = simplePageToolDao.findTopLevelPageItemBySakaiId(selectedEntities[i]);
				if (item != null)
					simplePageToolDao.deleteItem(item);			

				// currently the UI doesn't allow you to kill top level pages until they have been
				// removed by site info, so we don't have to hack on the site pages

				// remove page
				simplePageToolDao.deleteItem(target);
			}
		}
		return "success";
	}

	//  remove a top-level page from the left margin. Does not actually delete it.
	//  this and addpages checks only edit page permission. should it check site.upd?
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de sakai.
	 * Borra una p&aacutegina.
	 * @return resultado de la operaci&oacuten.
	 */
	public String removePage() {
		if (getEditPrivs() != 0) {
			return "permission-failed";
		}

		//		if (removeId == 0)
		//		    removeId = getCurrentPageId();
		SimplePage page = getPage(removeId);

		if (page == null)
			return "no-such-page";

		if(page.getOwner() == null) {
			// this code should never be called
			return "failure";
		}else {
			SimpleStudentPage studentPage = simplePageToolDao.findStudentPageByPageId(page.getPageId());

			if(studentPage != null) {
				studentPage.setDeleted(true);
				update(studentPage, false);

				String[] path = split(adjustPath("pop", null, null, null), ",");
				Long itemId = Long.valueOf(path[path.length-1]);

				try {
					SimplePageItem item = simplePageToolDao.findItem(itemId);
					updatePageObject(Long.valueOf(item.getSakaiId()));
					updatePageItem(itemId);
				} catch (PermissionException e) {
					return "failure";
				}

				return "success";
			}else {
				return "failure";
			}
		}
	}

	// called from "save" in main edit item dialog
	
	/**
	 * Edita un item.
	 * @return resultado de la operaci&oacuten.
	 */
	public String editItem() {
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		if (name.length() < 1) {
			return "Notitle";
		}

		SimplePageItem i = findItem(itemId);
		if (i == null) {
			return "failure";
		} else {
			i.setName(name);
			i.setDescription(description);
			i.setRequired(required);
			i.setPrerequisite(prerequisite);
			i.setSubrequirement(subrequirement);
			i.setNextPage(subpageNext);
			if (subpageButton)
				i.setFormat("button");
			else
				i.setFormat("");

			if (points != "") {
				i.setRequirementText(points);
			} else {
				i.setRequirementText(dropDown);
			}

			// currently we only display HTML in the same page
			if (i.getType() == SimplePageItem.RESOURCE)
				i.setSameWindow(!newWindow);
			else
				i.setSameWindow(false);

			if (i.getType() == SimplePageItem.BLTI) {
				if (format == null || format.trim().equals(""))
					i.setFormat("");
				else
					i.setFormat(format);
				// this is redundant, but the display code uses it
				if ("window".equals(format))
					i.setSameWindow(false);
				else
					i.setSameWindow(true);

				i.setHeight(height);
			}

			update(i);

			if (i.getType() == SimplePageItem.PAGE) {
				SimplePage page = getPage(Long.valueOf(i.getSakaiId()));
				if (page != null) {
					page.setTitle(name);
					update(page);
				}
			} else {
				checkControlGroup(i, i.isPrerequisite());
			}

			setItemGroups(i, selectedGroups);

			return "successEdit"; // Shouldn't reload page
		}
	}

	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Set access control for an item to the state requested by i.isPrerequisite().
	 This code should depend only upon isPrerequisite() in the item object, not the database,
	 because we call it when deleting or updating items, before saving them to the database.
	 The caller will update the item in the database, typically after this call."
	 correct is correct value, i.e whether it hsould be there or not.
	 * @param i
	 * @param correct
	 */
	private void checkControlGroup(SimplePageItem i, boolean correct) {
		if (i.getType() == SimplePageItem.RESOURCE) {
			checkControlResource(i, correct);
			return;
		}

		if (i.getType() != SimplePageItem.ASSESSMENT && 
				i.getType() != SimplePageItem.ASSIGNMENT && 
				i.getType() != SimplePageItem.FORUM) {
			// We only do this for assignments and assessments
			// currently we can't actually set it for forum topics
			return;
		}

		if (i.getSakaiId().equals(SimplePageItem.DUMMY))
			return;

		SimplePageGroup group = simplePageToolDao.findGroup(i.getSakaiId());
		String ourGroupName = null;
		try {
			// correct is the correct setting, i.e. if there is supposed to be
			// a group or not. We only change if reality disagrees with it.
			if (correct) {
				if (group == null) {
					// create our a new access control group, and save the current tool group list with it.
					LessonEntity lessonEntity = null;
					switch (i.getType()) {
					case SimplePageItem.ASSIGNMENT:
						lessonEntity = assignmentEntity.getEntity(i.getSakaiId()); break;
					case SimplePageItem.ASSESSMENT:
						lessonEntity = quizEntity.getEntity(i.getSakaiId()); break;
					case SimplePageItem.FORUM:
						lessonEntity = forumEntity.getEntity(i.getSakaiId()); break;
					}
					if (lessonEntity != null) {
						String groups = getItemGroupString (i, lessonEntity, true);
						ourGroupName = "Access: " + getNameOfSakaiItem(i);
						String groupId = GroupPermissionsService.makeGroup(getCurrentPage().getSiteId(), ourGroupName);
						saveItem(simplePageToolDao.makeGroup(i.getSakaiId(), groupId, groups, getCurrentPage().getSiteId()));

						// update the tool access control to point to our access control group

						String[] newGroups = {groupId};
						lessonEntity.setGroups(Arrays.asList(newGroups));
					}
				}
			} else {
				if (group != null) {
					// shouldn't be under control. Delete our access control and put the 
					// groups back into the tool's list

					LessonEntity lessonEntity = null;
					switch (i.getType()) {
					case SimplePageItem.ASSIGNMENT:
						lessonEntity = assignmentEntity.getEntity(i.getSakaiId()); break;
					case SimplePageItem.ASSESSMENT:
						lessonEntity = quizEntity.getEntity(i.getSakaiId()); break;
					case SimplePageItem.FORUM:
						lessonEntity = forumEntity.getEntity(i.getSakaiId()); break;
					}
					if (lessonEntity != null) {
						String groups = group.getGroups();
						List<String> groupList = null;
						if (groups != null && !groups.equals(""))
							groupList = Arrays.asList(groups.split(","));
						lessonEntity.setGroups(groupList);
						simplePageToolDao.deleteItem(group);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "To control a resource, set hidden. /access/lessonbuilder does the actual control."
	 * @param i
	 * @param correct
	 */
	private void checkControlResource(SimplePageItem i, boolean correct) {
		String resourceId = i.getSakaiId();

		if (resourceId != null) {
			try {
				ContentResource res = contentHostingService.getResource(resourceId);
				if (res.isHidden() != correct) {
					ContentResourceEdit resEdit = contentHostingService.editResource(resourceId);
					resEdit.setAvailability(correct, resEdit.getReleaseDate(), resEdit.getRetractDate());
					contentHostingService.commitResource(resEdit, NotificationService.NOTI_NONE);
				}
			} catch (Exception ignore) {}
		}

	}

	/**
	 * Retorna la p&aacutegina actual
	 * @return currentPage la p&aacutegina.
	 */
	public SimplePage getCurrentPage() {
		getCurrentPageId();
		return currentPage;
	}
	/**
	 * Asigna la p&aacutegina actual
	 * @param p la p&aacutegina.
	 */
	public void setCurrentPage(SimplePage p) {
		currentPage = p;
	}

	/**
	 * Retorna el identificador del Tool.
	 * @param tool
	 * @return el identificador.
	 */
	public String getToolId(String tool) {
		try {
			ToolConfiguration tc = siteService.getSite(currentPage.getSiteId())
					.getToolForCommonId(tool);
			return tc.getId();
		} catch (IdUnusedException e) {
			// This really shouldn't happen.
			log.warn("getToolId 1 attempt to get tool config for " + tool
					+ " failed. Tool missing from site?");
			return null;
		} catch (java.lang.NullPointerException e) {
			log.warn("getToolId 2 attempt to get tool config for " + tool
					+ " failed. Tool missing from site?");
			return null;
		}
	}

	/**
	 * Actualiza la p&aacutegina actual.
	 */
	public void updateCurrentPage() {
		update(currentPage);
	}

	/**
	 * Retorna el path del lessonbuilder en Sakai.
	 * @return path
	 */
	public List<PathEntry> getHierarchy() {
		List<PathEntry> path = (List<PathEntry>)sessionManager.getCurrentToolSession().getAttribute(LESSONBUILDER_PATH);
		if (path == null)
			return new ArrayList<PathEntry>();

		return path;
	}

	/**
	 * Asigna la tarea seleccionada.
	 * @param selectedAssignment
	 */
	public void setSelectedAssignment(String selectedAssignment) {
		this.selectedAssignment = selectedAssignment;
	}
	/**
	 * Asigna la entidad seleccionada.
	 * @param selectedEntity
	 */
	public void setSelectedEntity(String selectedEntity) {
		this.selectedEntity = selectedEntity;
	}
	/**
	 * Asigna el quiz seleccionado.
	 * @param selectedQuiz
	 */
	public void setSelectedQuiz(String selectedQuiz) {
		this.selectedQuiz = selectedQuiz;
	}
	/**
	 * Asigna el Blti seleccionado.
	 * @param selectedBlti
	 */
	public void setSelectedBlti(String selectedBlti) {
		this.selectedBlti = selectedBlti;
	}

	/**
	 * Retorna la referencia a la tarea.
	 * @param id
	 * @return la referencia.
	 */
	public String assignmentRef(String id) {
		return "/assignment/a/" + getCurrentSiteId() + "/" + id;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by add forum dialog. Create a new item that points to a forum or
	 update an existing item, depending upon whether itemid is set."
	 * @return
	 */
	public String addForum() {
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		if (selectedEntity == null) {
			return "failure";
		} else {
			try {
				LessonEntity selectedObject = forumEntity.getEntity(selectedEntity);
				if (selectedObject == null) {
					return "failure";
				}
				SimplePageItem i;
				// editing existing item?
				if (itemId != null && itemId != -1) {
					i = findItem(itemId);
					// if no change, don't worry
					if (!i.getSakaiId().equals(selectedEntity)) {
						// if access controlled, clear restriction from old assignment and add to new
						if (i.isPrerequisite()) {
							i.setPrerequisite(false);
							checkControlGroup(i, false);
							// sakaiid and name are used in setting control
							i.setSakaiId(selectedEntity);
							i.setName(selectedObject.getTitle());
							i.setPrerequisite(true);
							checkControlGroup(i, true);
						} else {
							i.setSakaiId(selectedEntity);
							i.setName(selectedObject.getTitle());
						}

						// reset assignment-specific stuff
						i.setDescription("");
						update(i);

						//Almacena recurso de sakai
						String tipo = "Libre";
						//Almacena recurso de sakai
						Recurso r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "forum", "forum", nivelActividades,tipo);
						saveCaracteristica(r, true);
						//Actualiza actividad						
						r.setIdRecurso(getUltimoRecurso().getIdRecurso());
						
						//carateristicas de recursos
						CaracteristicaRecurso cR = new CaracteristicasRecursoImpl();
						cR.setIdRecurso(r);
						if(tipo.equalsIgnoreCase("Paga"))
						{
							Caracteristica ca = simplePageToolDao.getCaracteristica("Campus");
							cR.setIdCaracteristica(ca);
						}else
						{
							Caracteristica ca = simplePageToolDao.getCaracteristica("Ninguno en particular");
							cR.setIdCaracteristica(ca);
						}
						
						//almacenar caracteritica contexto de recurso
						saveCarateristica(cR);
						
						Actividad ac = simplePageToolDao.getActividad(getUltimaActividad());
						RecursosActividadImpl recurso = new RecursosActividadImpl(ac,getUltimoRecurso());
						saveCaracteristica(recurso, true);
						//ac.setIdRecurso(getUltimoRecurso());
						//updateActividad(ac);
					}
				} else {
					// no, add new item
					i = appendItem(selectedEntity, selectedObject.getTitle(), SimplePageItem.FORUM);
					i.setDescription("");
					update(i);

					//Almacena recurso de sakai
					String tipo = "Libre";
					//Almacena recurso de sakai
					Recurso r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "forum", "forum", nivelActividades,tipo);

					saveCaracteristica(r, true);
					r.setIdRecurso(getUltimoRecurso().getIdRecurso());
					
					//carateristicas de recursos
					CaracteristicaRecurso cR = new CaracteristicasRecursoImpl();
					cR.setIdRecurso(r);
					if(tipo.equalsIgnoreCase("Paga"))
					{
						Caracteristica ca = simplePageToolDao.getCaracteristica("Campus");
						cR.setIdCaracteristica(ca);
					}else
					{
						Caracteristica ca = simplePageToolDao.getCaracteristica("Ninguno en particular");
						cR.setIdCaracteristica(ca);
					}
					
					//almacenar caracteritica contexto de recurso
					saveCarateristica(cR);
					//Actualiza actividad
					Actividad ac = simplePageToolDao.getActividad(getUltimaActividad());
					RecursosActividadImpl recurso = new RecursosActividadImpl(ac,getUltimoRecurso());
					saveCaracteristica(recurso, true);
					//ac.setIdRecurso(getUltimoRecurso());
					//updateActividad(ac);
				}
				//return "success";
				return "successRecursoActividad";
			} catch (Exception ex) {
				ex.printStackTrace();
				return "failure";
			} finally {
				selectedEntity = null;
			}
		}

	}


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by add assignment dialog. Create a new item that points to an assigment
	 or update an existing item, depending upon whether itemid is set"
	 * @return
	 */
	public String addAssignment() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, new ResourceLoader().getLocale());		
		df.setTimeZone(TimeService.getLocalTimeZone());
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		if (selectedAssignment == null) {
			return "failure";
		} else {
			try {
				LessonEntity selectedObject = assignmentEntity.getEntity(selectedAssignment);
				if (selectedObject == null)
					return "failure";

				SimplePageItem i;
				// editing existing item?
				if (itemId != null && itemId != -1) {
					i = findItem(itemId);

					// if no change, don't worry
					LessonEntity existing = assignmentEntity.getEntity(i.getSakaiId());
					String ref = existing.getReference();
					// if same quiz, nothing to do
					if (!ref.equals(selectedAssignment)) {
						// if access controlled, clear restriction from old assignment and add to new
						if (i.isPrerequisite()) {
							i.setPrerequisite(false);
							checkControlGroup(i, false);
							// sakaiid and name are used in setting control
							i.setSakaiId(selectedAssignment);
							i.setName(selectedObject.getTitle());
							i.setPrerequisite(true);
							checkControlGroup(i, true);
						} else {
							i.setSakaiId(selectedAssignment);
							i.setName(selectedObject.getTitle());
						}
						// reset assignment-specific stuff
						// Because we don't update the due date when it changes, this raises more
						// problems than it fixes. It's also done only for assignments and not tests
						//	if (selectedObject.getDueDate() != null)
						//	 i.setDescription("(" + messageLocator.getMessage("simplepage.due") + " " + df.format(selectedObject.getDueDate()) + ")");
						//  else
						// i.setDescription(null);
						update(i);
						String tipo = "Libre";
						//Almacena recurso de sakai
						Recurso r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "assignment", "assignment", nivelActividades,tipo);
						saveCaracteristica(r, true);
						r.setIdRecurso(getUltimoRecurso().getIdRecurso());
						
						//carateristicas de recursos
						CaracteristicaRecurso cR = new CaracteristicasRecursoImpl();
						cR.setIdRecurso(r);
						if(tipo.equalsIgnoreCase("Paga"))
						{
							Caracteristica ca = simplePageToolDao.getCaracteristica("Campus");
							cR.setIdCaracteristica(ca);
						}else
						{
							Caracteristica ca = simplePageToolDao.getCaracteristica("Ninguno en particular");
							cR.setIdCaracteristica(ca);
						}
						
						//almacenar caracteritica contexto de recurso
						saveCarateristica(cR);
						//Actualiza actividad
						Actividad ac = simplePageToolDao.getActividad(getUltimaActividad());
						RecursosActividadImpl recurso = new RecursosActividadImpl(ac,getUltimoRecurso());
						saveCaracteristica(recurso, true);
						//ac.setIdRecurso(getUltimoRecurso());
						//updateActividad(ac);
					}
				} else {
					// no, add new item
					i = appendItem(selectedAssignment, selectedObject.getTitle(), SimplePageItem.ASSIGNMENT);
					//if (selectedObject.getDueDate() != null)
					//  i.setDescription("(" + messageLocator.getMessage("simplepage.due") + " " + df.format(selectedObject.getDueDate()) + ")");
					//else
					i.setDescription(null);
					update(i);

					String tipo = "Libre";
					//Almacena recurso de sakai
					Recurso r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "assignment", "assignment", nivelActividades,tipo);
					saveCaracteristica(r, true);
					r.setIdRecurso(getUltimoRecurso().getIdRecurso());
					
					//carateristicas de recursos
					CaracteristicaRecurso cR = new CaracteristicasRecursoImpl();
					cR.setIdRecurso(r);
					if(tipo.equalsIgnoreCase("Paga"))
					{
						Caracteristica ca = simplePageToolDao.getCaracteristica("Campus");
						cR.setIdCaracteristica(ca);
					}else
					{
						Caracteristica ca = simplePageToolDao.getCaracteristica("Ninguno en particular");
						cR.setIdCaracteristica(ca);
					}
					
					//almacenar caracteritica contexto de recurso
					saveCarateristica(cR);
					//Actualiza actividad
					Actividad ac = simplePageToolDao.getActividad(getUltimaActividad());
					RecursosActividadImpl recurso = new RecursosActividadImpl(ac,getUltimoRecurso());
					saveCaracteristica(recurso, true);
					//ac.setIdRecurso(getUltimoRecurso());
					//updateActividad(ac);
				}
				//return "success";
				return "successRecursoActividad";
			} catch (Exception ex) {
				ex.printStackTrace();
				return "failure";
			} finally {
				selectedAssignment = null;
			}
		}
	}


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by add blti picker. Create a new item that points to an assigment
	 or update an existing item, depending upon whether itemid is set"
	 * @return
	 */
	public String addBlti() {
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		if (selectedBlti == null || bltiEntity == null) {
			return "failure";
		} else {
			try {
				LessonEntity selectedObject = bltiEntity.getEntity(selectedBlti);
				if (selectedObject == null)
					return "failure";

				SimplePageItem i;
				// editing existing item?
				if (itemId != null && itemId != -1) {
					i = findItem(itemId);

					// if no change, don't worry
					LessonEntity existing = bltiEntity.getEntity(i.getSakaiId());
					String ref = existing.getReference();
					// if same item, nothing to do
					if (!ref.equals(selectedBlti)) {
						// if access controlled, clear restriction from old assignment and add to new
						if (i.isPrerequisite()) {
							i.setPrerequisite(false);
							// sakaiid and name are used in setting control
							i.setSakaiId(selectedBlti);
							i.setName(selectedObject.getTitle());
							i.setPrerequisite(true);
						} else {
							i.setSakaiId(selectedBlti);
							i.setName(selectedObject.getTitle());
						}
						if (format == null || format.trim().equals(""))
							i.setFormat("");
						else
							i.setFormat(format);

						// this is redundant, but the display code uses it
						if ("window".equals(format))
							i.setSameWindow(false);
						else
							i.setSameWindow(true);

						i.setHeight(height);
						setItemGroups(i, selectedGroups);
						update(i);
					}
				} else {
					// no, add new item
					i = appendItem(selectedBlti, selectedObject.getTitle(), SimplePageItem.BLTI);
					BltiInterface blti = (BltiInterface)bltiEntity.getEntity(selectedBlti);
					if (blti != null) {
						int height = blti.frameSize();
						if (height > 0)
							i.setHeight(Integer.toString(height));
						else
							i.setHeight("");
						if (format == null || format.trim().equals(""))
							i.setFormat("");
						else
							i.setFormat(format);
					}
					update(i);
				}
				return "success";
			} catch (Exception ex) {
				ex.printStackTrace();
				return "failure";
			} finally {
				selectedBlti = null;
			}
		}
	}

	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "ShowPageProducers needs the item ID list anyway. So to avoid calling the underlying
	 code twice, we take that list and translate to titles, rather than calling
	 getItemGroups again."
	 * @param itemGroups
	 * @return ret
	 */
	public String getItemGroupTitles(String itemGroups) {
		if (itemGroups == null || itemGroups.equals(""))
			return null;

		List<String> groupNames = new ArrayList<String>();
		Site site = getCurrentSite();
		String[] groupIds = split(itemGroups, ",");
		for (int i = 0; i < groupIds.length; i++) {
			Group group=site.getGroup(groupIds[i]);
			if (group != null) {
				String title = group.getTitle();
				if (title != null && !title.equals(""))
					groupNames.add(title);
				else
					groupNames.add(messageLocator.getMessage("simplepage.deleted-group"));
			} else
				groupNames.add(messageLocator.getMessage("simplepage.deleted-group"));
		}
		Collections.sort(groupNames);
		String ret = "";
		for (String name: groupNames) {
			if (ret.equals(""))
				ret = name;
			else
				ret = ret + "," + name;
		}

		return ret;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Too much existing code to convert to throw at the moment."
	 * @param i
	 * @param entity
	 * @param nocache
	 * @return groups
	 */
	public String getItemGroupString (SimplePageItem i, LessonEntity entity, boolean nocache) {
		String groups = null;
		try {
			groups = getItemGroupStringOrErr (i, entity, nocache);
		} catch (IdUnusedException exp) {
			// unfortunately some uses aren't user-visible, so it's this or
			// add error handling to all callers
			return "";
		}
		return groups;
	}

	// use this one in the future
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Too much existing code to convert to throw at the moment."
	 * @param i
	 * @param entity
	 * @param nocache
	 * @return groups
	 */
	public String getItemGroupStringOrErr (SimplePageItem i, LessonEntity entity, boolean nocache)
			throws IdUnusedException{
		StringBuilder ret = new StringBuilder("");
		Collection<String> groups = null;
		// may throw IdUnUsed
		groups = getItemGroups (i, entity, nocache);
		if (groups == null)
			return "";
		for (String g: groups) {
			ret.append(",");
			ret.append(g);
		}
		if (ret.length() == 0)
			return "";
		return ret.substring(1);
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param i item.
	 * @return release String.
	 */
	public String getReleaseString(SimplePageItem i) {
		if (i.getType() == SimplePageItem.PAGE) {
			SimplePage page = getPage(Long.valueOf(i.getSakaiId()));
			if (page == null)
				return null;
			if (page.isHidden())
				return messageLocator.getMessage("simplepage.hiddenpage");
			if (page.getReleaseDate() != null && page.getReleaseDate().after(new Date()))
				return messageLocator.getMessage("simplepage.pagenotreleased");
		}
		return null;
	}



	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 *"Return GroupEntrys for all groups associated with item
	 need group entries so we can display labels to user
	 entity is optional. pass it if you have it, to avoid requiring
	 us to get it a second time
	 idunusedexception if underlying object doesn't exist."
	 * @param i
	 * @param entity
	 * @param nocache
	 * @return ret.
	 */
	public Collection<String>getItemGroups (SimplePageItem i, LessonEntity entity, boolean nocache)
			throws IdUnusedException {

		Collection<String> ret = new ArrayList<String>();

		if (!nocache && i.getType() != SimplePageItem.PAGE 
				&& i.getType() != SimplePageItem.TEXT
				&& i.getType() != SimplePageItem.BLTI
				&& i.getType() != SimplePageItem.COMMENTS
				&& i.getType() != SimplePageItem.STUDENT_CONTENT) {
			Object cached = groupCache.get(i.getSakaiId());
			if (cached != null) {
				if (cached instanceof String)
					return null;
				return (List<String>)cached;
			}
		}

		if (entity == null) {
			switch (i.getType()) {
			case SimplePageItem.ASSIGNMENT:
				entity = assignmentEntity.getEntity(i.getSakaiId()); break;
			case SimplePageItem.ASSESSMENT:
				entity = quizEntity.getEntity(i.getSakaiId()); break;
			case SimplePageItem.FORUM:
				entity = forumEntity.getEntity(i.getSakaiId()); break;
			case SimplePageItem.RESOURCE:
			case SimplePageItem.MULTIMEDIA:
				return getResourceGroups(i, nocache);  // responsible for caching the result
				// throws IdUnusedException if necessary

			case SimplePageItem.BLTI:
				entity = bltiEntity.getEntity(i.getSakaiId());
				if (entity == null || !entity.objectExists())
					throw new IdUnusedException(i.toString());
				// fall through: groups controlled by LB
				// for the following items we don't have non-LB items so don't need itemunused
			case SimplePageItem.TEXT:
			case SimplePageItem.PAGE:
			case SimplePageItem.COMMENTS:
			case SimplePageItem.STUDENT_CONTENT:
				return getLBItemGroups(i); // for all native LB objects
			default:
				return null;
			}
		}

		// only here for object types with underlying entities
		boolean exists = false;
		try {
			pushAdvisorAlways();  // assignments won't let the student look
			if (entity != null)
				exists = entity.objectExists();
		} finally {
			popAdvisor();
		}

		if (!exists) {
			throw new IdUnusedException(i.toString());
		}

		// in principle the groups are stored in a SimplePageGroup if we
		// are doing access control, and in the tool if not. We can
		// check that with i.isPrerequisite. However I'm concerned
		// that if multiple items point to the same object, and some
		// are set with prerequisite and some are not, that things
		// could get out of kilter. So I'm going to use the
		// SimplePageGroup if it exists, and the tool if not.

		SimplePageGroup simplePageGroup = simplePageToolDao.findGroup(i.getSakaiId());
		if (simplePageGroup != null) {
			String groups = simplePageGroup.getGroups();
			if (groups != null && !groups.equals(""))
				ret = Arrays.asList(groups.split(","));
			else 
				;  // leave ret as an empty list
		} else {
			// not under our control, use list from tool
			try {
				pushAdvisorAlways();
				ret = entity.getGroups(nocache); // assignments won't let a student see
			} finally {
				popAdvisor();
			}
		}

		if (ret == null)
			groupCache.put(i.getSakaiId(), "*", DEFAULT_EXPIRATION);
		else
			groupCache.put(i.getSakaiId(), ret, DEFAULT_EXPIRATION);

		return ret;

	}

	// obviously this function must be called right after getResourceGroups
	/**
	 * Atributo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean inherited = false;
	/**
	 * 
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return inherited
	 */
	public boolean getInherited() {
		return inherited;
	}
	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "getItemGroups version for resources, since we don't have
	 an interface object. IdUnusedException if the underlying resource doesn't exist."
	 * @param i
	 * @param nocache
	 * @return
	 * @throws IdUnusedException
	 */
	public Collection<String>getResourceGroups (SimplePageItem i, boolean nocache) 
			throws IdUnusedException{
		SecurityAdvisor advisor = null;
		try {

			// do this before getting privs. It is implemented by seeing whether anon can access,
			// so the advisor will cause the wrong answer
			boolean inheritingPubView =  contentHostingService.isInheritingPubView(i.getSakaiId());

			// for isItemVisible to work, users need to be able to get this all the time
			//if(getCurrentPage().getOwner() != null) {
			advisor = new SecurityAdvisor() {
				public SecurityAdvice isAllowed(String userId, String function, String reference) {
					return SecurityAdvice.ALLOWED;
				}
			};
			securityService.pushAdvisor(advisor);
			//   }

			Collection<String> ret = null;

			ContentResource resource = null;
			try {
				resource = contentHostingService.getResource(i.getSakaiId());
			} catch (Exception ignore) {
				throw new IdUnusedException(i.toString());
			}

			Collection<String>groups = null;
			AccessMode access = resource.getAccess();
			if(AccessMode.INHERITED.equals(access) || inheritingPubView) {
				access = resource.getInheritedAccess();
				// inherited means that we can't set it locally
				// an inherited value of site is OK
				// anything else can't be changed, so we set inherited
				if (AccessMode.SITE.equals(access) && ! inheritingPubView)
					inherited = false;
				else
					inherited = true;
				if (AccessMode.GROUPED.equals(access))
					groups = resource.getInheritedGroups();
			} else {
				// we can always change local modes, even if they are public
				inherited = false;
				if (AccessMode.GROUPED.equals(access))
					groups = resource.getGroups();
			}

			if (groups != null) {
				ret = new ArrayList<String>();
				for (String group: groups) {
					int n = group.indexOf("/group/");
					ret.add(group.substring(n+7));
				}
			}

			if (!nocache) {
				if (ret == null)
					groupCache.put(i.getSakaiId(), "*", DEFAULT_EXPIRATION);
				else
					groupCache.put(i.getSakaiId(), ret, DEFAULT_EXPIRATION);
			}

			return ret;
		}finally {
			if(advisor != null) securityService.popAdvisor();
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "No obvious need to cache."
	 * @param i
	 * @return
	 */
	public Collection<String>getLBItemGroups (SimplePageItem i) {
		List<String> ret = null;

		String groupString = i.getGroups();
		if (groupString == null || groupString.equals("")) {
			return null;
		}

		String[] groupsArray = split(groupString, ",");
		return Arrays.asList(groupsArray);

	}


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Set group list in tool. We'll have an array of group ids
	 returns old list, sorted, or null if entity not found.
	 WARNING: you must check whether isprerequisite. If so, we maintain
	 the group list, so you need to do i.setGroups()."
	 * @param i
	 * @param groups
	 * @return oldGroups
	 */
	public List<String> setItemGroups (SimplePageItem i, String[] groups) {
		// can't allow groups on student pages
		if (getCurrentPage().getOwner() != null)
			return null;
		LessonEntity lessonEntity = null;
		switch (i.getType()) {
		case SimplePageItem.ASSIGNMENT:
			lessonEntity = assignmentEntity.getEntity(i.getSakaiId()); break;
		case SimplePageItem.ASSESSMENT:
			lessonEntity = quizEntity.getEntity(i.getSakaiId()); break;
		case SimplePageItem.FORUM:
			lessonEntity = forumEntity.getEntity(i.getSakaiId()); break;
		case SimplePageItem.RESOURCE:
		case SimplePageItem.MULTIMEDIA:
			return setResourceGroups (i, groups);
		case SimplePageItem.TEXT:
		case SimplePageItem.PAGE:
		case SimplePageItem.BLTI:
		case SimplePageItem.COMMENTS:
		case SimplePageItem.STUDENT_CONTENT:
			return setLBItemGroups(i, groups);
		}
		if (lessonEntity != null) {
			// need a list to sort it.
			Collection oldGroupCollection = null;
			try {
				oldGroupCollection = getItemGroups(i, lessonEntity, true);
			} catch (IdUnusedException exc) {
				return null; // no such entity
			}
			List<String>oldGroups = null;
			if (oldGroupCollection == null)
				oldGroups = new ArrayList<String>();
			else
				oldGroups = new ArrayList<String>(oldGroupCollection);

			Collections.sort(oldGroups);
			List<String>newGroups = Arrays.asList(groups);
			Collections.sort(newGroups);
			boolean difference = false;
			if (oldGroups.size() == newGroups.size()) {
				for (int n = 0; n < oldGroups.size(); n++)
					if (!oldGroups.get(n).equals(newGroups.get(n))) {
						difference = true;
						break;
					}
			} else
				difference = true;

			if (difference) {
				if (i.isPrerequisite()) {
					String groupString = "";
					for (String groupId: newGroups) {
						if (groupString.equals(""))
							groupString = groupId;
						else
							groupString = groupString + "," + groupId;
					}

					SimplePageGroup simplePageGroup = simplePageToolDao.findGroup(i.getSakaiId());
					simplePageGroup.setGroups(groupString);
					update(simplePageGroup);
				} else
					lessonEntity.setGroups(Arrays.asList(groups));
			}
			return oldGroups;
		}
		return null;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param i
	 * @param groups
	 * @return ret
	 */
	public List<String> setResourceGroups (SimplePageItem i, String[] groups) {

		ContentResourceEdit resource = null;
		List<String>ret = null;

		boolean pushed = false;
		try {
			pushed = pushAdvisor();
			resource = contentHostingService.editResource(i.getSakaiId());

			if (AccessMode.GROUPED.equals(resource.getInheritedAccess())) {
				Collection<String> oldGroups = resource.getInheritedGroups();
				if (oldGroups instanceof List)
					ret = (List<String>)oldGroups;
				else if (oldGroups != null)
					ret = new ArrayList<String>(oldGroups);
			}
			// else null

			if (groups == null || groups.length == 0) {
				if (AccessMode.GROUPED.equals(resource.getAccess()))
					resource.clearGroupAccess();
				// else must be public or site already, leave it
			} else {
				Site site = getCurrentSite();
				for (int n = 0; n < groups.length; n++) {
					Group group = site.getGroup(groups[n]);
					groups[n] = group.getReference();
				}
				resource.setGroupAccess(Arrays.asList(groups));
			}
			contentHostingService.commitResource(resource, NotificationService.NOTI_NONE);
			resource = null;

		} catch (java.lang.NullPointerException e) {
			// KNL-714 gives spurious null pointer
			setErrMessage(messageLocator.getMessage("simplepage.resourcepossibleerror"));
		} catch (Exception e) {
			setErrMessage(e.toString());
			return null;
		} finally {
			// this will generate a traceback in the case of KNL-714, but there's no way
			// to trap it. Sorry. The log entry will say
			// org.sakaiproject.content.impl.BaseContentService - cancelResource(): closed ContentResourceEdit
			// the user will also get a warning
			if (resource != null) {
				contentHostingService.cancelResource(resource);
			}
			if(pushed) popAdvisor();
		}

		return ret;

	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param i
	 * @param groups
	 * @return ret
	 */
	public List<String> setLBItemGroups (SimplePageItem i, String[] groups) {

		List<String>ret = null;
		// old value
		String groupString = i.getGroups();
		if (groupString != null && !groupString.equals("")) {
			ret = Arrays.asList(groupString.split(","));
		}

		groupString = null;
		if (groups != null) {
			for (int n = 0; n < groups.length; n++) {
				if (groupString == null)
					groupString = groups[n];
				else
					groupString = groupString + "," + groups[n];
			}
		}
		i.setGroups(groupString);
		update(i);

		return ret; // old value
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return ret
	 */
	public Set<String> getMyGroups() {
		if (myGroups != null)
			return myGroups;
		String userId = getCurrentUserId();
		Collection<Group> groups = getCurrentSite().getGroupsWithMember(userId);
		Set<String>ret = new HashSet<String>();
		if (groups == null)
			return ret;
		for (Group group: groups)
			ret.add(group.getId());
		myGroups = ret;
		return ret;
	}


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Sort the list, since it will typically be presented
	 to the user."
	 * @return currentGroups
	 */
	public List<GroupEntry> getCurrentGroups() {
		if (currentGroups != null)
			return currentGroups;

		Site site = getCurrentSite();
		Collection<Group> groups = site.getGroups();
		List<GroupEntry> groupEntries = new ArrayList<GroupEntry>();
		for (Group g: groups) {
			GroupEntry e = new GroupEntry();
			e.name = g.getTitle();
			e.id = g.getId();
			groupEntries.add(e);
		}

		Collections.sort(groupEntries,new Comparator() {
			public int compare(Object o1, Object o2) {
				GroupEntry e1 = (GroupEntry)o1;
				GroupEntry e2 = (GroupEntry)o2;
				return e1.name.compareTo(e2.name);
			}
		});
		currentGroups = groupEntries;
		return groupEntries;
	}



	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by add quiz dialog. Create a new item that points to a quiz
	 or update an existing item, depending upon whether itemid is set."
	 * @return resultado de la operaci&oacuten.
	 */
	public String addQuiz() {
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		if (selectedQuiz == null) {
			return "failure";
		} else {
			try {
				LessonEntity selectedObject = quizEntity.getEntity(selectedQuiz);
				if (selectedObject == null)
					return "failure";

				// editing existing item?
				SimplePageItem i = null;
				if (itemId != null && itemId != -1) {
					i = findItem(itemId);
					// do getEntity/getreference to normalize, in case sakaiid is old format
					LessonEntity existing = quizEntity.getEntity(i.getSakaiId());
					String ref = existing.getReference();
					// if same quiz, nothing to do
					if (!ref.equals(selectedQuiz)) {
						// if access controlled, clear restriction from old quiz and add to new
						if (i.isPrerequisite()) {
							i.setPrerequisite(false);
							checkControlGroup(i, false);
							// sakaiid and name are used in setting control
							i.setSakaiId(selectedQuiz);
							i.setName(selectedObject.getTitle());
							i.setPrerequisite(true);
							checkControlGroup(i, true);
						} else {
							i.setSakaiId(selectedQuiz);
							i.setName(selectedObject.getTitle());
						}
						// reset quiz-specific stuff
						i.setDescription("");
						update(i);

						String tipo = "Libre";
						//Almacena recurso de sakai
						Recurso r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "quiz", "quiz", nivelActividades,tipo);
						saveCaracteristica(r, true);
						r.setIdRecurso(getUltimoRecurso().getIdRecurso());
						
						//carateristicas de recursos
						CaracteristicaRecurso cR = new CaracteristicasRecursoImpl();
						cR.setIdRecurso(r);
						if(tipo.equalsIgnoreCase("Paga"))
						{
							Caracteristica ca = simplePageToolDao.getCaracteristica("Campus");
							cR.setIdCaracteristica(ca);
						}else
						{
							Caracteristica ca = simplePageToolDao.getCaracteristica("Ninguno en particular");
							cR.setIdCaracteristica(ca);
						}
						
						//almacenar caracteritica contexto de recurso
						saveCarateristica(cR);
						//Actualiza actividad
						Actividad ac = simplePageToolDao.getActividad(getUltimaActividad());
						RecursosActividadImpl recurso = new RecursosActividadImpl(ac,getUltimoRecurso());
						saveCaracteristica(recurso, true);
						//ac.setIdRecurso(getUltimoRecurso());
						//updateActividad(ac);
					}
				} else 
				{// no, add new item
					appendItem(selectedQuiz, selectedObject.getTitle(), SimplePageItem.ASSESSMENT);

					String tipo = "Libre";
					//Almacena recurso de sakai
					Recurso r = new RecursoImpl(String.valueOf(i.getId()),"L&oacutegico", "quiz", "quiz", nivelActividades,tipo);
					saveCaracteristica(r, true);
					r.setIdRecurso(getUltimoRecurso().getIdRecurso());
					
					//carateristicas de recursos
					CaracteristicaRecurso cR = new CaracteristicasRecursoImpl();
					cR.setIdRecurso(r);
					if(tipo.equalsIgnoreCase("Paga"))
					{
						Caracteristica ca = simplePageToolDao.getCaracteristica("Campus");
						cR.setIdCaracteristica(ca);
					}else
					{
						Caracteristica ca = simplePageToolDao.getCaracteristica("Ninguno en particular");
						cR.setIdCaracteristica(ca);
					}
					
					//almacenar caracteritica contexto de recurso
					saveCarateristica(cR);
					//Actualiza actividad
					Actividad ac = simplePageToolDao.getActividad(getUltimaActividad());
					RecursosActividadImpl recurso = new RecursosActividadImpl(ac,getUltimoRecurso());
					saveCaracteristica(recurso, true);
					//ac.setIdRecurso(getUltimoRecurso());
					//updateActividad(ac);
				}
				//return "success";
				return "successRecursoActividad";
			} catch (Exception ex) {
				ex.printStackTrace();
				return "failure";
			} finally {
				selectedQuiz = null;
			}
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param url
	 */
	public void setLinkUrl(String url) {
		linkUrl = url;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Doesn't seem to be used at the moment."
	 * @return resultado de la operaci&oacuten.
	 */
	public String createLink() {
		if (linkUrl == null || linkUrl.equals("")) {
			return "cancel";
		}

		String url = linkUrl;
		url = url.trim();

		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}

		appendItem(url, url, SimplePageItem.URL);

		return "success";
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param pageId
	 */
	public void setPage(long pageId) {
		sessionManager.getCurrentToolSession().setAttribute("current-pagetool-page", pageId);
		currentPageId = null;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "More setters and getters used by forms."
	 * @param height
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "More setters and getters used by forms."
	 * @return height
	 */
	public String getHeight() {
		String r = "";
		if (itemId != null && itemId > 0) {
			r = findItem(itemId).getHeight();
		}
		return (r == null ? "" : r);
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "More setters and getters used by forms."
	 * @param width
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "More setters and getters used by forms."
	 * @return width
	 */
	public String getWidth() {
		String r = "";
		if (itemId != null && itemId > 0) {
			r = findItem(itemId).getWidth();
		}
		return (r == null ? "" : r);
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return r
	 */
	public String getAlt() {
		String r = "";
		if (itemId != null && itemId > 0) {
			r = findItem(itemId).getAlt();
		}
		return (r == null ? "" : r);
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by edit multimedia dialog to change parameters in a multimedia item."
	 * @return
	 */
	public String editMultimedia() {
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		SimplePageItem i = findItem(itemId);
		if (i != null && i.getType() == SimplePageItem.MULTIMEDIA) {
			i.setHeight(height);
			i.setWidth(width);
			i.setAlt(alt);
			i.setDescription(description);
			i.setHtml(mimetype);
			update(i);
			setItemGroups(i, selectedGroups);
			return "success";
		} else {
			log.warn("editMultimedia Could not find multimedia object: " + itemId);
			return "cancel";
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by edit title dialog to change attributes of the page such as the title."
	 * @return resultado de la operaci&oacuten.
	 */
	public String editTitle()  {
		if (pageTitle == null || pageTitle.equals("")) {
			return "notitle";
		}

		// because we're using a security advisor, need to make sure it's OK ourselves
		if (!canEditPage()) {
			return "permission-failed";
		}

		Placement placement = toolManager.getCurrentPlacement();
		SimplePage page = getCurrentPage();
		SimplePageItem pageItem = getCurrentPageItem(null);
		Site site = getCurrentSite();
		boolean needRecompute = false;

		if(page.getOwner() == null && getEditPrivs() == 0) {
			// update gradebook link if necessary
			Double currentPoints = page.getGradebookPoints();
			Double newPoints = null;

			if (points != null) {
				try {
					newPoints = Double.parseDouble(points);
					if (newPoints == 0.0)
						newPoints = null;
				} catch (Exception ignore) {
					newPoints = null;
				}
			}
			// adjust gradebook entry
			boolean add = false;
			if (newPoints == null && currentPoints != null) {
				gradebookIfc.removeExternalAssessment(site.getId(), "lesson-builder:" + page.getPageId());
			} else if (newPoints != null && currentPoints == null) {
				add = gradebookIfc.addExternalAssessment(site.getId(), "lesson-builder:" + page.getPageId(), null,
						pageTitle, newPoints, null, "Lesson Builder");

				if(!add) {
					setErrMessage(messageLocator.getMessage("simplepage.no-gradebook"));
				} else
					needRecompute = true;
			} else if (currentPoints != null && 
					(!currentPoints.equals(newPoints) || !pageTitle.equals(page.getTitle()))) {
				add = gradebookIfc.updateExternalAssessment(site.getId(), "lesson-builder:" + page.getPageId(), null,
						pageTitle, newPoints, null);
				if(!add) {
					setErrMessage(messageLocator.getMessage("simplepage.no-gradebook"));
				} else if (!currentPoints.equals(newPoints))
					needRecompute = true;
			}
			if (add)
				page.setGradebookPoints(newPoints);
		}

		if (pageTitle != null && pageItem.getPageId() == 0) {
			try {
				// we need a security advisor because we're allowing users to edit the page if they
				// have
				// simplepage.upd privileges, but site.save requires site.upd.
				securityService.pushAdvisor(new SecurityAdvisor() {
					public SecurityAdvice isAllowed(String userId, String function, String reference) {
						if (function.equals(SITE_UPD) && reference.equals("/site/" + getCurrentSiteId())) {
							return SecurityAdvice.ALLOWED;
						} else {
							return SecurityAdvice.PASS;
						}
					}
				});

				SitePage sitePage = site.getPage(page.getToolId());

				for (ToolConfiguration t: sitePage.getTools()) {
					if (t.getId().equals(placement.getId()))
						t.setTitle(pageTitle);
				}

				sitePage.setTitle(pageTitle);
				siteService.save(site);
				page.setTitle(pageTitle);
				page.setHidden(hidePage);
				if (hasReleaseDate)
					page.setReleaseDate(releaseDate);
				else
					page.setReleaseDate(null);
				update(page);
				updateCurrentPage();
				placement.setTitle(pageTitle);
				placement.save();
				pageVisibilityHelper(site, page.getToolId(), !hidePage);
				pageItem.setPrerequisite(prerequisite);
				pageItem.setRequired(required);
				pageItem.setName(pageTitle);
				update(pageItem);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				securityService.popAdvisor();
			}
		} else if (pageTitle != null) {
			page.setTitle(pageTitle);
			page.setHidden(hidePage);
			if (hasReleaseDate)
				page.setReleaseDate(releaseDate);
			else
				page.setReleaseDate(null);
			update(page);
		}

		if(pageTitle != null) {
			if(pageItem.getType() == SimplePageItem.STUDENT_CONTENT) {
				SimpleStudentPage student = simplePageToolDao.findStudentPage(pageItem.getId(), page.getOwner());
				student.setTitle(pageTitle);
				update(student, false);
			} else {
				pageItem.setName(pageTitle);
				update(pageItem);
			}

			adjustPath("", pageItem.getPageId(), pageItem.getId(), pageTitle);
		}

		String collectionId = contentHostingService.getSiteCollection(getCurrentSiteId()) + "LB-CSS/";
		String uploadId = uploadFile(collectionId);
		if(uploadId != null) {
			page.setCssSheet(uploadId);

			// Make sure the relevant caches are wiped clean.
			resourceCache.expire(collectionId);
			resourceCache.expire(uploadId);
		}else {
			page.setCssSheet(dropDown);
		}

		update(page);

		// have to do this after the page itself is updated
		if (needRecompute)
			recomputeGradebookEntries(page.getPageId(), points);
		// points, not newPoints because API wants a string

		if (pageItem.getPageId() == 0) {
			return "reload";
		} else {
			return "success";
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param collectionId
	 * @return Resultado de la operaci&oacuten.
	 */
	private String uploadFile(String collectionId) {
		String name = null;
		String mimeType = null;
		MultipartFile file = null;

		if (multipartMap.size() > 0) {
			// 	user specified a file, create it
			file = multipartMap.values().iterator().next();
			if (file.isEmpty())
				file = null;
		}

		if (file != null) {
			try {
				contentHostingService.checkCollection(collectionId);
			}catch(Exception ex) {
				try {
					ContentCollectionEdit edit = contentHostingService.addCollection(collectionId);
					edit.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME, "LB-CSS");
					contentHostingService.commitCollection(edit);
				}catch(Exception e) {
					setErrMessage(messageLocator.getMessage("simplepage.permissions-general"));
					return null;
				}
			}

			//String collectionId = getCollectionId(false);
			// 	user specified a file, create it
			name = file.getOriginalFilename();
			if (name == null || name.length() == 0)
				name = file.getName();

			int i = name.lastIndexOf("/");
			if (i >= 0)
				name = name.substring(i+1);
			String base = name;
			String extension = "";
			i = name.lastIndexOf(".");
			if (i > 0) {
				base = name.substring(0, i);
				extension = name.substring(i+1);
			}

			mimeType = file.getContentType();
			try {
				ContentResourceEdit res = contentHostingService.addResource(collectionId, 
						Validator.escapeResourceName(base),
						Validator.escapeResourceName(extension),
						MAXIMUM_ATTEMPTS_FOR_UNIQUENESS);
				res.setContentType(mimeType);
				res.setContent(file.getInputStream());
				try {
					contentHostingService.commitResource(res,  NotificationService.NOTI_NONE);
					// 	there's a bug in the kernel that can cause
					// 	a null pointer if it can't determine the encoding
					// 	type. Since we want this code to work on old
					// 	systems, work around it.
				} catch (java.lang.NullPointerException e) {
					setErrMessage(messageLocator.getMessage("simplepage.resourcepossibleerror"));
				}
				return res.getId();
			} catch (org.sakaiproject.exception.OverQuotaException ignore) {
				setErrMessage(messageLocator.getMessage("simplepage.overquota"));
				return null;
			} catch (Exception e) {
				setErrMessage(messageLocator.getMessage("simplepage.resourceerror").replace("{}", e.toString()));
				log.error("addMultimedia error 1 " + e);
				return null;
			}
		}else {
			return null;
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return Resultado de la operaci&oacuten.
	 */
	public String addPages()  {
		if (!canEditPage())
			return "permission-fail";

		// javascript should have checked all this
		if (newPageTitle == null || newPageTitle.equals(""))
			return "fail";

		int numPages = 1;
		if (numberOfPages !=null && !numberOfPages.equals(""))
			numPages = Integer.valueOf(numberOfPages);

		String prefix = "";
		String suffix = "";
		int start = 1;

		if (numPages > 1) {
			Pattern pattern = Pattern.compile("(\\D*)(\\d+)(.*)");
			Matcher matcher = pattern.matcher(newPageTitle);

			if (!matcher.matches())
				return "fail";

			prefix = matcher.group(1);
			suffix = matcher.group(3);
			start = Integer.parseInt(matcher.group(2));
		}

		if (numPages == 1) {
			addPage(newPageTitle, copyPage);
		} else {
			// note sure what to do here. We have to have a maximum to prevent creating 1,000,000 pages due
			// to a typo. This allows one a week for a year. Surely that's enough. We can make it 
			// configurable if necessary.
			if (numPages > 52)
				numPages = 52;

			while (numPages > 0) {
				String title = prefix + Integer.toString(start) + suffix;
				addPage(title, null, copyPage, (numPages == 1));  // only save the last time
				numPages--;
				start++;
			}
		}

		setTopRefresh();

		return "success";
	}

	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Adds an existing page as a top level page."
	 * @return resultado de la operaci&oacuten.
	 */
	public String addOldPage() {
		if (getEditPrivs() != 0)
			return "permission-failed";

		SimplePage target = getPage(Long.valueOf(selectedEntity));
		if(target != null)
			addPage(target.getTitle(), target.getPageId(), false, true);

		return "success";
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param title
	 * @param copyCurrent
	 * @return 
	 */
	public SimplePage addPage(String title, boolean copyCurrent) {
		return addPage(title, null, copyCurrent, true);
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param title
	 * @param pageId
	 * @param copyCurrent
	 * @param doSave
	 * @return page
	 */
	public SimplePage addPage(String title, Long pageId, boolean copyCurrent, boolean doSave) {

		Site site = getCurrentSite();
		SitePage sitePage = site.addPage();

		ToolConfiguration tool = sitePage.addTool(LESSONBUILDER_ID);
		String toolId = tool.getPageId();

		SimplePage page = null;

		if(pageId == null) {
			page = simplePageToolDao.makePage(toolId, getCurrentSiteId(), title, null, null);
			saveItem(page);
		}else {
			page = getPage(pageId);
			page.setToolId(toolId);
			page.setParent(null);
			page.setTopParent(null);
			update(page);
			title = page.getTitle();
		}

		tool.setTitle(title);

		SimplePageItem item = simplePageToolDao.makeItem(0, 0, SimplePageItem.PAGE, Long.toString(page.getPageId()), title);
		saveItem(item);

		sitePage.setTitle(title);
		sitePage.setTitleCustom(true);
		if (doSave) {
			try {
				siteService.save(site);
			} catch (Exception e) {
				log.error("addPage unable to save site " + e);
			}
			currentSite = null; // force refetch, since we've changed it. note sure this is strictly needed
		}

		if (copyCurrent) {
			long oldPageId = getCurrentPageId();
			long newPageId = page.getPageId();
			for (SimplePageItem oldItem: simplePageToolDao.findItemsOnPage(oldPageId)) {
				// don't copy pages. It's not clear whether we want to deep copy or
				// not. If we do the wrong thing the user coudl end up editing the
				// wrong page and losing content.
				if (oldItem.getType() == SimplePageItem.PAGE)
					continue;
				SimplePageItem newItem = simplePageToolDao.copyItem(oldItem);
				newItem.setPageId(newPageId);
				saveItem(newItem);
			}
		}

		setTopRefresh();

		return page;
	}

	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "When a gradebook entry is added or point value for page changed, need to
	 add or update all student entries for the page
	 this only updates grades for users that are complete. Others should have 0 score, which won't change."
	 * @param pageId
	 * @param newPoints
	 */
	public void recomputeGradebookEntries(Long pageId, String newPoints) {
		Map<String, String> userMap = new HashMap<String,String>();
		List<SimplePageItem> items = simplePageToolDao.findPageItemsBySakaiId(Long.toString(pageId));
		if (items == null)
			return;
		for (SimplePageItem item : items) {
			List<String> users = simplePageToolDao.findUserWithCompletePages(item.getId());
			for (String user: users)
				userMap.put(user, newPoints);
		}

		gradebookIfc.updateExternalAssessmentScores(getCurrentSiteId(), "lesson-builder:" + pageId, userMap);
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param item
	 * @return
	 */
	public boolean isImageType(SimplePageItem item) {
		// if mime type is defined use it
		String mimeType = item.getHtml();
		if (mimeType != null && (mimeType.startsWith("http") || mimeType.equals("")))
			mimeType = null;

		if (mimeType != null && mimeType.length() > 0) {
			return mimeType.toLowerCase().startsWith("image/");
		}

		// else use extension

		String name = item.getSakaiId();

		// starts after last /
		int i = name.lastIndexOf("/");
		if (i >= 0)
			name = name.substring(i+1);

		String extension = null;	    
		i = name.lastIndexOf(".");
		if (i > 0)
			extension = name.substring(i+1);

		if (extension == null)
			return false;

		extension = extension.trim();
		extension = extension.toLowerCase();

		if (imageTypes.contains(extension)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param order
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public void fixorder() {
		List<SimplePageItem> items = getItemsOnPage(getCurrentPageId());

		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getSequence() <= 0) {
				items.remove(items.get(i));
				i--;
			}
		}

		int i = 1;
		for(SimplePageItem item: items) {
			if (item.getSequence() != i) {
				item.setSequence(i);
				update(item);
			}
			i++;
		}

	}

	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by reorder tool to do the reordering."
	 * @return resultado de la operaci&oacuten.
	 */
	public String reorder() {

		if (!canEditPage())
			return "permission-fail";

		if (order == null) {
			return "cancel";
		}

		fixorder(); // order has to be contiguous or things will break

		order = order.trim();

		List<SimplePageItem> items = getItemsOnPage(getCurrentPageId());

		// Remove items that weren't ordered due to having sequences too low.
		// Typically means they are tacked onto the end automatically.
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getSequence() <= 0) {
				items.remove(items.get(i));
				i--;
			}
		}

		List <SimplePageItem> secondItems = null;
		if (selectedEntity != null && !selectedEntity.equals("")) {
			// second page is involved
			Long secondPageId = Long.parseLong(selectedEntity);
			SimplePage secondPage = getPage(secondPageId);
			if (secondPage != null && secondPage.getSiteId().equals(getCurrentPage().getSiteId())) {
				secondItems = getItemsOnPage(secondPageId);
				if (secondItems.size() == 0)
					secondItems = null;
				else {
					for(int i = 0; i < secondItems.size(); i++) {
						if(secondItems.get(i).getSequence() <= 0) {
							secondItems.remove(secondItems.get(i));
							i--;
						}
					}
				}
			}
		}

		String[] split = split(order, " ");

		// make sure nothing is duplicated. I know it shouldn't be, but
		// I saw the Fluid reorderer get confused once.
		Set<String> used = new HashSet<String>();
		for (int i = 0; i < split.length; i++) {
			if (!used.add(split[i].trim())) {
				log.warn("reorder: duplicate value");
				setErrMessage(messageLocator.getMessage("simplepage.reorder-duplicates"));
				return "failed"; // it was already there. Oops.
			}
		}

		// keep track of which old items are used so we can remove the ones that aren't.
		// items in set are indices into "items"
		Set<Integer>keep = new HashSet<Integer>();

		// now do the reordering
		for (int i = 0; i < split.length; i++) {
			if (split[i].equals("---"))
				break;
			if (split[i].startsWith("*")) {
				// item from second page. add copy
				SimplePageItem oldItem = secondItems.get(Integer.valueOf(split[i].substring(1)) - 1);
				SimplePageItem newItem = simplePageToolDao.copyItem(oldItem);
				newItem.setPageId(getCurrentPageId());
				newItem.setSequence(i + 1);
				saveItem(newItem);
			} else {
				// existing item. update its sequence and note that it's still used
				int old = items.get(Integer.valueOf(split[i]) - 1).getSequence();
				keep.add(Integer.valueOf(split[i]) - 1);
				items.get(Integer.valueOf(split[i]) - 1).setSequence(i + 1);
				if (old != i + 1) {
					update(items.get(Integer.valueOf(split[i]) - 1));
				}

			}
		}

		// now kill all items on the page we didn't see in the new order
		for (int i = 0; i < items.size(); i++) {
			if (!keep.contains((Integer)i))
				simplePageToolDao.deleteItem(items.get(i));
		}

		itemsCache.remove(getCurrentPage().getPageId());
		// removals left gaps in order. fix it.
		fixorder();
		itemsCache.remove(getCurrentPage().getPageId());

		return "success";
	}

	// this is sort of sleasy. A simple redirect passes no data. Thus it takes
	// us back to the default page. So we advance the default page. It would probably
	// have been better to use a link rather than a command, then the link could
	// have passed the page and item.
	//	public String next() {
	//	    getCurrentPageId();  // sets item id, which is what we want
	//	    SimplePageItem item = getCurrentPageItem(null);
	//
	//	    List<SimplePageItem> items = getItemsOnPage(item.getPageId());
	//
	//	    item = items.get(item.getSequence());  // sequence start with 1, so this is the next item
	//	    updatePageObject(Long.valueOf(item.getSakaiId()));
	//	    updatePageItem(item.getId());
	//
	//	    return "redirect";
	//	}
	/**
	 * Retorna el identificador del usuario actual.
	 * @return currentUserId
	 */
	public String getCurrentUserId() {
		if (currentUserId == null)
			currentUserId = UserDirectoryService.getCurrentUser().getId();
		return currentUserId;
	}
	/**
	 * Retorna el tipo del usuario actual.
	 * @return el tipo
	 */
	public String getCurrentUserType() {
		return UserDirectoryService.getCurrentUser().getType();
	}

	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Page is complete, update gradebook entry if any
	 note that if the user has never gone to a page, the gradebook item will be missing.
	 if they gone to it but it's not complete, it will be 0. We can't explicitly set
	 a missing value, and this is the only way things will work if someone completes a page
	 and something changes so it is no longer complete."
	 * @param item
	 * @param complete
	 */
	public void trackComplete(SimplePageItem item, boolean complete ) {
		SimplePage page = getCurrentPage();
		if (page.getGradebookPoints() != null)
			gradebookIfc.updateExternalAssessmentScore(getCurrentSiteId(), "lesson-builder:" + page.getPageId(), getCurrentUserId(), 
					complete ? Double.toString(page.getGradebookPoints()) : "0.0");
	}

	/**
	 * 
	 * @param itemId
	 *            The ID in the <b>items</b> table.
	 * @param path 
	 *            breadcrumbs, only supplied it the item is a page
	 *            It is valid for code to check path == null to see
	 *            whether it is a page
	 *       Create or update a log entry when user accesses an item.
	 */
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @param path
	 */
	public void track(long itemId, String path) {
		track(itemId, path, null);
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @param path
	 * @param studentPageId
	 */
	public void track(long itemId, String path, Long studentPageId) {
		String userId = getCurrentUserId();
		if (userId == null)
			userId = ".anon";
		SimplePageLogEntry entry = getLogEntry(itemId, studentPageId);
		String toolId = ((ToolConfiguration) toolManager.getCurrentPlacement()).getPageId();

		if (entry == null) {
			entry = simplePageToolDao.makeLogEntry(userId, itemId, studentPageId);

			if (path != null && studentPageId == null) {
				boolean complete = isPageComplete(itemId);
				entry.setComplete(complete);
				entry.setPath(path);
				entry.setToolId(toolId);
				SimplePageItem i = findItem(itemId);
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.read", "/lessonbuilder/page/" + i.getSakaiId(), complete));
				trackComplete(i, complete);
				studentPageId = -1L;
			}else if(path != null) {
				entry.setPath(path);
				entry.setComplete(true);
				entry.setToolId(toolId);
				SimplePage page = getPage(studentPageId);
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.read", "/lessonbuilder/page/" + page.getPageId(), true));
			}

			saveItem(entry);
			logCache.put(itemId + "-" + studentPageId, entry);
		} else {
			if (path != null && studentPageId == null) {
				boolean wasComplete = entry.isComplete();
				boolean complete = isPageComplete(itemId);
				entry.setComplete(complete);
				entry.setPath(path);
				entry.setToolId(toolId);
				entry.setDummy(false);
				SimplePageItem i = findItem(itemId);
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.read", "/lessonbuilder/page/" + i.getSakaiId(), complete));
				if (complete != wasComplete)
					trackComplete(i, complete);
				studentPageId = -1L;
			}else if(path != null) {
				entry.setComplete(true);
				entry.setPath(path);
				entry.setToolId(toolId);
				entry.setDummy(false);
				SimplePage page = getPage(studentPageId);
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.read", "/lessonbuilder/page/" + page.getPageId(), true));
			}

			update(entry);
		}

		//SimplePageItem i = findItem(itemId);
		// todo
		// code can't work anymore. I'm not sure whether it's needed.
		// we don't update a page as complete if the user finishes a test, etc, until he
		// comes back to the page. I'm not sure I feel compelled to do this either. But
		// once we move to the new hiearchy, we'll see

		// top level doesn't have a next level, so avoid null pointer problem
		//		if (i.getPageId() != 0) {
		//		    SimplePageItem nextLevelUp = simplePageToolDao.findPageItemBySakaiId(String.valueOf(i.getPageId()));
		//		    if (isItemComplete(findItem(itemId)) && nextLevelUp != null) {
		//			track(nextLevelUp.getId(), true);
		//		    }
		//		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @return SimplePageLogEntry
	 */
	public SimplePageLogEntry getLogEntry(long itemId) {
		return getLogEntry(itemId, null);
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @param studentPageId
	 * @return SimplePageLogEntry
	 */
	public SimplePageLogEntry getLogEntry(long itemId, Long studentPageId) {
		if(studentPageId == null) {
			studentPageId = -1L;
		}

		String lookup = itemId + "-" + studentPageId;
		SimplePageLogEntry entry = logCache.get(lookup);

		if (entry != null)
			return entry;
		String userId = getCurrentUserId();
		if (userId == null)
			userId = ".anon";
		entry = simplePageToolDao.getLogEntry(userId, itemId, studentPageId);


		logCache.put(lookup, entry);

		return entry;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @return
	 */
	public boolean hasLogEntry(long itemId) {
		return (getLogEntry(itemId) != null);
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "If the item has a group requirement, are we in one of the groups.
	 this is called a lot and is fairly expensive, so results are cached."
	 * @param item
	 * @return true or false.
	 */
	public boolean isItemVisible(SimplePageItem item) {
		if (canEditPage()) {
			return true;
		}
		Boolean ret = visibleCache.get(item.getId());
		if (ret != null) {
			return (boolean)ret;
		}

		// item is page, and it is hidden or not released
		if (item.getType() == SimplePageItem.PAGE) {
			SimplePage page = getPage(Long.valueOf(item.getSakaiId()));
			if (page.isHidden())
				return false;
			if (page.getReleaseDate() != null && page.getReleaseDate().after(new Date()))
				return false;
		}

		Collection<String>itemGroups = null;
		try {
			itemGroups = getItemGroups(item, null, false);
		} catch (IdUnusedException exc) {
			visibleCache.put(item.getId(), false);
			return false; // underlying entity missing, don't show it
		}
		if (itemGroups == null || itemGroups.size() == 0) {
			// this includes items for which for which visibility doesn't apply
			visibleCache.put(item.getId(), true);
			return true;
		}

		getMyGroups();

		for (String group: itemGroups) {
			if (myGroups.contains(group)) {
				visibleCache.put(item.getId(), true);
				return true;
			}
		}

		visibleCache.put(item.getId(), false);

		return false;
	}

	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "This is called in a loop to see whether items are available. Since computing it can require
	 database transactions, we cache the results."
	 * @param item
	 * @return true or false
	 */
	public boolean isItemComplete(SimplePageItem item) {
		if (!item.isRequired()) {
			// We don't care if it has been completed if it isn't required.
			return true;
		} 
		Long itemId = item.getId();
		Boolean cached = completeCache.get(itemId);
		if (cached != null)
			return (boolean)cached;
		if (item.getType() == SimplePageItem.RESOURCE || item.getType() == SimplePageItem.URL || item.getType() == SimplePageItem.BLTI) {
			// Resource. Completed if viewed.
			if (hasLogEntry(item.getId())) {
				completeCache.put(itemId, true);
				return true;
			} else {
				completeCache.put(itemId, false);
				return false;
			}
		} else if (item.getType() == SimplePageItem.PAGE) {
			SimplePageLogEntry entry = getLogEntry(item.getId());
			if (entry == null || entry.getDummy()) {
				completeCache.put(itemId, false);
				return false;
			} else if (entry.isComplete()) {
				completeCache.put(itemId, true);
				return true;
			} else {
				completeCache.put(itemId, false);
				return false;
			}
		} else if (item.getType() == SimplePageItem.ASSIGNMENT) {
			try {
				if (item.getSakaiId().equals(SimplePageItem.DUMMY)) {
					completeCache.put(itemId, false);
					return false;
				}
				LessonEntity assignment = assignmentEntity.getEntity(item.getSakaiId());
				if (assignment == null) {
					completeCache.put(itemId, false);
					return false;
				}
				LessonSubmission submission = assignment.getSubmission(getCurrentUserId());

				if (submission == null) {
					completeCache.put(itemId, false);
					return false;
				}

				int type = assignment.getTypeOfGrade();

				if (!item.getSubrequirement()) {
					completeCache.put(itemId, true);
					return true;
				} else if (submission.getGradeString() != null) {
					// assume that assignments always use string grade. this may change
					boolean ret = isAssignmentComplete(type, submission, item.getRequirementText());
					completeCache.put(itemId, ret);
					return ret;
				} else {
					completeCache.put(itemId, false);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				completeCache.put(itemId, false);
				return false;
			}
		} else if (item.getType() == SimplePageItem.FORUM) {
			try {
				if (item.getSakaiId().equals(SimplePageItem.DUMMY)) {
					completeCache.put(itemId, false);
					return false;
				}
				User user = UserDirectoryService.getUser(getCurrentUserId());
				LessonEntity forum = forumEntity.getEntity(item.getSakaiId());
				if (forum == null)
					return false;
				// for the moment don't find grade. just see if they submitted
				if (forum.getSubmissionCount(user.getId()) > 0) {
					completeCache.put(itemId, true);
					return true;
				} else {
					completeCache.put(itemId, false);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				completeCache.put(itemId, false);
				return false;
			}
		} else if (item.getType() == SimplePageItem.ASSESSMENT) {
			if (item.getSakaiId().equals(SimplePageItem.DUMMY)) {
				completeCache.put(itemId, false);
				return false;
			}
			LessonEntity quiz = quizEntity.getEntity(item.getSakaiId());
			if (quiz == null) {
				completeCache.put(itemId, false);
				return false;
			}
			User user = null;
			try {
				user = UserDirectoryService.getUser(getCurrentUserId());
			} catch (Exception ignore) {
				completeCache.put(itemId, false);
				return false;
			}

			LessonSubmission submission = quiz.getSubmission(user.getId());

			if (submission == null) {
				completeCache.put(itemId, false);
				return false;
			} else if (!item.getSubrequirement()) {
				// All that was required was that the user submit the test
				completeCache.put(itemId, true);
				return true;
			} else {
				Double grade = submission.getGrade();
				if (grade >= Double.valueOf(item.getRequirementText())) {
					completeCache.put(itemId, true);
					return true;
				} else {
					completeCache.put(itemId, false);
					return false;
				}
			}
		} else if (item.getType() == SimplePageItem.COMMENTS) {
			List<SimplePageComment>comments = simplePageToolDao.findCommentsOnItemByAuthor((long)itemId, getCurrentUserId());
			boolean found = false;
			if (comments != null) {
				for (SimplePageComment comment: comments) {
					if (comment.getComment() != null && !comment.getComment().equals("")) {
						found = true;
						break;
					}
				}
			}
			if (found) {
				completeCache.put(itemId, true);
				return true;
			} else {
				completeCache.put(itemId, false);
				return false;
			}
		} else if (item.getType() == SimplePageItem.STUDENT_CONTENT) {
			// need option for also requiring the student to submit a comment on the content

			SimpleStudentPage student = simplePageToolDao.findStudentPage(itemId, getCurrentUserId());

			if (student != null && ! student.isDeleted()) {
				completeCache.put(itemId, true);
				return true;
			} else {
				completeCache.put(itemId, false);
				return false;
			}
		} else if (item.getType() == SimplePageItem.TEXT || item.getType() == SimplePageItem.MULTIMEDIA) {
			// In order to be considered "complete", these items
			// only have to be viewed. If this code is reached,
			// we know that that the page has already been viewed.
			completeCache.put(itemId, true);
			return true;
		} else {
			completeCache.put(itemId, false);
			return false;
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param type
	 * @param submission
	 * @param requirementString
	 * @return true or false.
	 */
	private boolean isAssignmentComplete(int type, LessonSubmission submission, String requirementString) {
		String grade = submission.getGradeString();

		if (type == SimplePageItem.ASSESSMENT) {
			if (grade.equals("Pass")) {
				return true;
			} else {
				return false;
			}
		} else if (type == SimplePageItem.TEXT) {
			if (grade.equals("Checked")) {
				return true;
			} else {
				return false;
			}
		} else if (type == SimplePageItem.PAGE) {
			if (grade.equals("ungraded")) {
				return false;
			}

			int requiredIndex = -1;
			int currentIndex = -1;

			for (int i = 0; i < GRADES.length; i++) {
				if (GRADES[i].equals(requirementString)) {
					requiredIndex = i;
				}

				if (GRADES[i].equals(grade)) {
					currentIndex = i;
				}
			}

			if (requiredIndex == -1 || currentIndex == -1) {
				return false;
			} else {
				if (requiredIndex >= currentIndex) {
					return true;
				} else {
					return false;
				}
			}
		} else if (type == SimplePageItem.ASSIGNMENT) {
			// assignment 2 uses gradebook, so we have a float value
			if (submission.getGrade() != null)
				return submission.getGrade() >= Float.valueOf(requirementString);
				// otherwise use the String
				if (Float.valueOf(Integer.valueOf(grade) / 10) >= Float.valueOf(requirementString)) {
					return true;
				} else {
					return false;
				}
		} else {
			return false;
		}
	}

	// note on completion: there's an issue with subpages. isItemComplete just looks to see if
	// the logEntry shows that the page is complete. But that's filled out when someone actually
	// visits the page. If an instructor has graded something or a student has submitted directly
	// through a tool, requirements in a subpage might have been completed without the student
	// actually visiting the page. 
	//   So for the first subpage that is both required and not completed, we want to recheck
	// the subpage to see if it's now complete. This will have to happen recursively. Of course
	// if it's OK then we need to check the next one, etc.
	//
	// This code will return false immediately when it gets the first thing that is required
	// and not completed. We just do
	// a recusrive check if the subpage is required, visited, and not already completed. The reason
	// for only checking if visited is to avoid false positives for page with no requirements. A
	// page with no required items is completed when it's visited. If there are no reqirements, the
	// recursive call will return true, but if the page hasn't been visited it's still not completed.
	// So we only want to do the recursive call if it's been visited.
	//    I think it's reasonable not to start checking status of quizes etc until the page has been
	// visited.
	//
	// no changes are needed for isItemCompleted. isPageCompleted is called at the starrt of ShowPage
	// by track. That call will update the status of subpages. So when we're doing other operations on
	// the page we can use a simple isItemComplete, because status would have been updated at the start
	// of ShowPage.
	//
	// Note that we only check the first subpage that hasn't been completed. If there's more than one,
	// information about later ones could be out of date. I'm claim that's less important, because it can't
	// affect whether anything is allowed.
	//
	// the recursive call to isItemComplete for subpages has some issues. isItemComplete will
	// use the completeCache, which may be set by isitemcomplete without doing a full recursive
	// scan. We are again depending upon the fact that the first check is done here, which does
	// the necessary recursion. The cache is request-scope. If it were longer-lived we'd have
	// a problem.

	// alreadySeen is needed in case there's a loop in the page structure. This is uncommon but
	// possible

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @return
	 */
	public boolean isPageComplete(long itemId) {
		return isPageComplete(itemId, null);
	}

	/**
	 * @param itemId
	 *            The ID of the page from the <b>items</b> table (not the page table).
	 * @return
	 */
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @param alreadySeen
	 * @return true or false.
	 */
	public boolean isPageComplete(long itemId,Set<Long>alreadySeen) {

		// Make sure student content objects aren't treated like pages.
		// TODO: Put in requirements
		if(findItem(itemId).getType() == SimplePageItem.STUDENT_CONTENT) {
			return true;
		}


		List<SimplePageItem> items = getItemsOnPage(Long.valueOf(findItem(itemId).getSakaiId()));

		for (SimplePageItem item : items) {
			if (!isItemComplete(item) && isItemVisible(item)) {
				if (item.getType() == SimplePageItem.PAGE) {
					// If we get here, must be not completed or isItemComplete would be true
					SimplePageLogEntry entry = getLogEntry(item.getId());
					// two possibilities in next check:
					// 1) hasn't seen page, can't be complete
					// 2) we've checked before; there's a loop; be safe and disallow it
					if (entry == null || entry.getDummy() ||
							(alreadySeen != null && alreadySeen.contains(item.getId()))) {
						return false;
					}
					if (alreadySeen == null)
						alreadySeen = new HashSet<Long>();
					alreadySeen.add(itemId);
					// recursive check to see whether page is complete
					boolean subOK = isPageComplete(item.getId(), alreadySeen);
					if (!subOK) {
						return false; // nope, that was our last hope
					}
					// was complete; fall through and return true
				} else
					return false;
			}
		}

		// All of them were complete.
		completeCache.put(itemId, true);
		return true;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Return list of pages needed for current page. This is the primary code
	 used by ShowPageProducer to see whether the user is allowed to the page
	 (given that they have read permission, of course)
	 Note that the same page can occur
	 multiple places, but we're passing the item, so we've got the right one."
	 * @param item
	 * @return needed
	 */
	public List<String> pagesNeeded(SimplePageItem item) {
		String currentPageId = Long.toString(getCurrentPageId());
		List<String> needed = new ArrayList<String>();

		if (!item.isPrerequisite()){
			return needed;
		}

		// authorized or maybe user is gaming us, or maybe next page code
		// sent them to something that isn't available.
		// as an optimization check haslogentry first. That will be true if
		// they have been here before. Saves us the trouble of doing full
		// access checking. Otherwise do a real check. That should only happen
		// for next page in odd situations.
		if (item.getPageId() > 0) {
			if (!hasLogEntry(item.getId()) &&
					!isItemAvailable(item, item.getPageId())) {
				SimplePage parent = getPage(item.getPageId());
				if (parent != null)
					needed.add(parent.getTitle());
				else
					needed.add("unknown page");  // not possible, it says
			}
			return needed;
		}

		// we've got a top level page.
		// get dummy items for top level pages in site
		List<SimplePageItem> items = simplePageToolDao.findItemsInSite(getCurrentSite().getId());
		// sorted by SQL

		for (SimplePageItem i : items) {
			if (i.getSakaiId().equals(currentPageId)) {
				return needed;  // reached current page. we're done
			}
			if (i.isRequired() && !isItemComplete(i) && isItemVisible(i))
				needed.add(i.getName());
		}

		return needed;

	}

	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "MaybeUpdateLinks checks to see if this page was copied from another
	 site and needs an update."
	 */
	public void maybeUpdateLinks() {
		String needsFixup = getCurrentSite().getProperties().getProperty("lessonbuilder-needsfixup");
		if (needsFixup == null || !needsFixup.equals("true"))
			return;
		lessonBuilderEntityProducer.updateEntityReferences(getCurrentSiteId());
		Site site = getCurrentSite();
		ResourcePropertiesEdit rp = site.getPropertiesEdit();
		rp.removeProperty("lessonbuilder-needsfixup");
		try {
			siteService.save(site);
		} catch (Exception e) {
			log.warn("site save in maybeUpdateLinks " + e);
		}
		currentSite = null;  // force refetch next time
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param item
	 * @return true or false
	 */
	public boolean isItemAvailable(SimplePageItem item) {
		return isItemAvailable(item, getCurrentPageId());
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param item
	 * @param pageId
	 * @return true or false
	 */
	public boolean isItemAvailable(SimplePageItem item, long pageId) {
		if (item.isPrerequisite()) {
			List<SimplePageItem> items = getItemsOnPage(pageId);

			for (SimplePageItem i : items) {
				if (i.getSequence() >= item.getSequence()) {
					break;
				} else if (i.isRequired() && isItemVisible(i)) {
					if (!isItemComplete(i)) {
						return false;
					}
				} 
			}
		}
		return true;
	}

	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Weird variant that works even if current item doesn't have prereq."
	 * @param item
	 * @param pageId
	 * @return
	 */
	public boolean wouldItemBeAvailable(SimplePageItem item, long pageId) {
		List<SimplePageItem> items = getItemsOnPage(pageId);

		for (SimplePageItem i : items) {
			if (i.getSequence() >= item.getSequence()) {
				break;
			} else if (i.isRequired() && isItemVisible(i)) {
				if (!isItemComplete(i))
					return false;
			}
		}
		return true;
	}

	/**
	 * Retorna el nombre del item de sakai.
	 * @param i el item.
	 * @return el nombre.
	 */
	public String getNameOfSakaiItem(SimplePageItem i) {
		String SakaiId = i.getSakaiId();

		if (SakaiId == null || SakaiId.equals(SimplePageItem.DUMMY))
			return null;

		if (i.getType() == SimplePageItem.ASSIGNMENT) {
			LessonEntity assignment = assignmentEntity.getEntity(i.getSakaiId());
			if (assignment == null)
				return null;
			return assignment.getTitle();
		} else if (i.getType() == SimplePageItem.FORUM) {
			LessonEntity forum = forumEntity.getEntity(i.getSakaiId());
			if (forum == null)
				return null;
			return forum.getTitle();
		} else if (i.getType() == SimplePageItem.ASSESSMENT) {
			LessonEntity quiz = quizEntity.getEntity(i.getSakaiId());
			if (quiz == null)
				return null;
			return quiz.getTitle();
		} else if (i.getType() == SimplePageItem.BLTI) {
			if (bltiEntity == null)
				return null;
			LessonEntity blti = bltiEntity.getEntity(i.getSakaiId());
			if (blti == null)
				return null;
			return blti.getTitle();
		} else
			return null;
	}

	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "We allow both ? and &. The key may be the value of something like ?v=, so we don't know
	 whether the next thing is & or ?. To be safe, use & except for the first param, which
	 uses ?. Note that RSF will turn & into &amp; in the src= attribute. THis appears to be correct,
	 as HTML is an SGML dialect.
	 If you run into trouble with &amp;, you can use ; in the following. Google seems to 
	 process it correctly. ; is a little-known alterantive to & that the RFCs do permit."
	 * @param URL
	 * @return
	 */
	private String normalizeParams(String URL) {
		URL = URL.replaceAll("[\\?\\&\\;]", "&");
		return URL.replaceFirst("\\&", "?");
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return 
	 */
	private String getYoutubeKeyFromUrl(String URL) {
		// 	see if it has a Youtube ID
		int offset = 0;
		if (URL.startsWith("http:"))
			offset = 5;
		else if (URL.startsWith("https:"))
			offset = 6;

		if (URL.startsWith("//www.youtube.com/", offset) || URL.startsWith("//youtube.com/", offset)) {
			Matcher match = YOUTUBE_PATTERN.matcher(URL);
			if (match.find()) {
				return normalizeParams(match.group(1));
			}
			match = YOUTUBE2_PATTERN.matcher(URL);
			if (match.find()) {
				return normalizeParams(match.group(1));
			}
		}else if(URL.startsWith("//youtu.be/", offset)) {
			Matcher match = SHORT_YOUTUBE_PATTERN.matcher(URL);
			if(match.find()) {
				return normalizeParams(match.group(1));
			}
		}
		return null;
	}
	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Return 11-char youtube ID for a URL, or null if it doesn't match
	 we store URLs as content objects, so we have to retrieve the object
	 in order to check. The actual URL is stored as the contents
	 of the entity."
	 * @return key
	 */
	public String getYoutubeKey(SimplePageItem i) {
		String sakaiId = i.getSakaiId();

		SecurityAdvisor advisor = null;
		try {
			if(getCurrentPage().getOwner() != null) {
				// Need to allow access into owner's home directory
				advisor = new SecurityAdvisor() {
					public SecurityAdvice isAllowed(String userId, String function, String reference) {
						if("content.read".equals(function) || "content.hidden".equals(function)) {
							return SecurityAdvice.ALLOWED;
						}else {
							return SecurityAdvice.PASS;
						}
					}
				};
				securityService.pushAdvisor(advisor);
			}
			// find the resource
			ContentResource resource = null;
			try {
				resource = contentHostingService.getResource(sakaiId);
			} catch (Exception ignore) {
				return null;
			}

			// 	make sure it's a URL
			if (resource == null ||
					// copying resources does not preserve this field, so if we do this test, things won't
					// work in copied sites
					//		!resource.getResourceType().equals("org.sakaiproject.content.types.urlResource") ||
					!resource.getContentType().equals("text/url")) {
				return null;
			}

			// 	get the actual URL
			String URL = null;
			try {
				URL = new String(resource.getContent());
			} catch (Exception ignore) {
				return null;
			}
			if (URL == null) {
				return null;
			}

			return getYoutubeKeyFromUrl(URL);

		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(advisor != null) securityService.popAdvisor();
		}

		// 	no
		return null;
	}
	/**
	 * Divide un String por un token.
	 * @param s el String.
	 * @param p el token.
	 * @return las divisiones.
	 */
	public String[] split(String s, String p) {
		if (s == null || s.equals(""))
			return new String[0];
		else
			return s.split(p);
	}

	/**
	 * Meant to guarantee that the permissions are set correctly on an assessment for a user.
	 * 
	 * @param item
	 * @param shouldHaveAccess
	 */
	public void checkItemPermissions(SimplePageItem item, boolean shouldHaveAccess) {
		checkItemPermissions(item, shouldHaveAccess, true);
	}

	/**
	 * 
	 * @param item
	 * @param shouldHaveAccess
	 * @param canRecurse
	 *            Is it allowed to delete the row in the table for the group and recurse to try
	 *            again. true for normal calls; false if called inside this code to avoid infinite loop
	 */
	// only called if the item should be under control. Also only called if the item is displayed
	// so if it's limited to a group, we'll never add people who aren't in the group, since the
	// item isn't shown to them.
	private void checkItemPermissions(SimplePageItem item, boolean shouldHaveAccess, boolean canRecurse) {
		if (SimplePageItem.DUMMY.equals(item.getSakaiId()))
			return;

		// for pages, presence of log entry is it
		if (item.getType() == SimplePageItem.PAGE) {
			Long itemId = item.getId();
			if (getLogEntry(itemId) != null)
				return;  // already ok
			// if no log entry, create a dummy entry
			if (shouldHaveAccess) {
				String userId = getCurrentUserId();
				if (userId == null)
					userId = ".anon";
				SimplePageLogEntry entry = simplePageToolDao.makeLogEntry(userId, itemId, null);
				entry.setDummy(true);
				saveItem(entry);
				logCache.put(itemId + "--1", entry);
			}
			return;
		}

		SimplePageGroup group = simplePageToolDao.findGroup(item.getSakaiId());
		if (group == null) {
			// For some reason, the group doesn't exist. Let's re-add it.
			checkControlGroup(item, true);
			group = simplePageToolDao.findGroup(item.getSakaiId());
			if (group == null) {
				return;
			}
		}

		boolean success = true;
		String groupId = group.getGroupId();

		try {
			if (shouldHaveAccess) {
				success = GroupPermissionsService.addCurrentUser(getCurrentPage().getSiteId(), getCurrentUserId(), groupId);
			} else {
				success = GroupPermissionsService.removeUser(getCurrentPage().getSiteId(), getCurrentUserId(), groupId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		// hmmm.... couldn't add or remove from group. Most likely the Sakai-level group
		// doesn't exist, although our database entry says it was created. Presumably
		// the user deleted the group for Site Info. Make very sure that's the cause,
		// or we'll create a duplicate group. I've seen failures for other reasons, such
		// as a weird permissions problem with the only maintain users trying to unjoin
		// a group.

		if (!success && canRecurse) {
			try {
				AuthzGroupService.getAuthzGroup(groupId);
				// group exists, it was something else. Who knows what
				return;
			} catch (org.sakaiproject.authz.api.GroupNotDefinedException ee) {

			} catch (Exception e) {
				// some other failure from getAuthzGroup, shouldn't be possible
				log.warn("checkItemPermissions unable to join or unjoin group " + groupId);
			}

			log.warn("checkItemPermissions: User seems to have deleted group " + groupId + ". We'll recreate it.");

			// OK, group doesn't exist. When we recreate it, it's going to have a 
			// different groupId, so we have to back out of everything and reset it

			checkControlGroup(item, false);

			// huh? checkcontrolgroup just deleted it
			//simplePageToolDao.deleteItem(group);

			// We've undone it; call ourselves again, since the code at the
			// start will recreate the group

			checkItemPermissions(item, shouldHaveAccess, false);
		}

	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param url
	 */
	public void setYoutubeURL(String url) {
		youtubeURL = url;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param id
	 */
	public void setYoutubeId(long id) {
		youtubeId = id;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public void deleteYoutubeItem() {
		itemId = findItem(youtubeId).getId();
		deleteItem();
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param url
	 */
	public void setMmUrl(String url) {
		mmUrl = url;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param multipartMap
	 */
	public void setMultipartMap(Map<String, MultipartFile> multipartMap) {
		this.multipartMap = multipartMap;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param urls
	 * @return collectionId
	 */
	public String getCollectionId(boolean urls) {
		String siteId = getCurrentPage().getSiteId();

		String pageOwner = getCurrentPage().getOwner();
		String collectionId;
		if (pageOwner == null) {
			collectionId = contentHostingService.getSiteCollection(siteId);
		}else {
			collectionId = "/user/" + pageOwner + "/stuff4/";
		}

		// folder we really want
		String folder = collectionId + Validator.escapeResourceName(getPageTitle()) + "/";
		if (urls)
			folder = folder + "urls/";

		// OK?
		try {
			contentHostingService.checkCollection(folder);
			// OK, let's use it
			return folder;
		} catch (Exception ignore) {};

		// no. create folders as needed

		// if url subdir, need an extra level
		if (urls) {

			// try creating the root. if it exists this will fail. That's OK.
			String root = collectionId + Validator.escapeResourceName(getPageTitle()) + "/";
			try {
				ContentCollectionEdit edit = contentHostingService.addCollection(root);
				edit.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME,  Validator.escapeResourceName(getPageTitle()));
				contentHostingService.commitCollection(edit);
				// well, we got that far anyway
				collectionId = root;
			} catch (Exception ignore) {
			}

		}

		// now try creating what we want
		try {
			ContentCollectionEdit edit = contentHostingService.addCollection(folder);
			if (urls)
				edit.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME, "urls");
			else
				edit.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME, Validator.escapeResourceName(getPageTitle()));

			contentHostingService.commitCollection(edit);
			return folder; // worked. use it
		} catch (Exception ignore) {};

		// didn't. do the best we can
		return collectionId;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param i
	 * @return true or false
	 */
	public boolean isHtml(SimplePageItem i) {
		StringTokenizer token = new StringTokenizer(i.getSakaiId(), ".");

		String extension = "";

		while (token.hasMoreTokens()) {
			extension = token.nextToken().toLowerCase();
		}

		// we are just starting to store the MIME type for resources now. So existing content
		// won't have them.
		String mimeType = i.getHtml();
		if (mimeType != null && (mimeType.startsWith("http") || mimeType.equals("")))
			mimeType = null;

		if (mimeType != null && (mimeType.equals("text/html") || mimeType.equals("application/xhtml+xml"))
				|| mimeType == null && (extension.equals("html") || extension.equals("htm"))) {
			return true;
		}
		return false;
	}

	/**
	 * Atributo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final int MAXIMUM_ATTEMPTS_FOR_UNIQUENESS = 100;

	 
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by dialog to add inline multimedia item, or update existing
	 item if itemid is specified."
	 */
	public void addMultimedia() {
		SecurityAdvisor advisor = null;
		try {
			if(getCurrentPage().getOwner() != null) {
				advisor = new SecurityAdvisor() {
					public SecurityAdvice isAllowed(String userId, String function, String reference) {
						return SecurityAdvice.ALLOWED;
					}
				};
				securityService.pushAdvisor(advisor);
			}
			if (!itemOk(itemId))
				return;
			if (!canEditPage())
				return;

			String name = null;
			String sakaiId = null;
			String mimeType = null;
			MultipartFile file = null;

			if (multipartMap.size() > 0) {
				// 	user specified a file, create it
				file = multipartMap.values().iterator().next();
				if (file.isEmpty())
					file = null;
			}

			if (file != null) {
				String collectionId = getCollectionId(false);
				// 	user specified a file, create it
				name = file.getOriginalFilename();
				if (name == null || name.length() == 0)
					name = file.getName();
				int i = name.lastIndexOf("/");
				if (i >= 0)
					name = name.substring(i+1);
				String base = name;
				String extension = "";
				i = name.lastIndexOf(".");
				if (i > 0) {
					base = name.substring(0, i);
					extension = name.substring(i+1);
				}

				mimeType = file.getContentType();
				try {
					ContentResourceEdit res = contentHostingService.addResource(collectionId, 
							Validator.escapeResourceName(base),
							Validator.escapeResourceName(extension),
							MAXIMUM_ATTEMPTS_FOR_UNIQUENESS);
					res.setContentType(mimeType);
					res.setContent(file.getInputStream());
					try {
						contentHostingService.commitResource(res,  NotificationService.NOTI_NONE);
						// 	there's a bug in the kernel that can cause
						// 	a null pointer if it can't determine the encoding
						// 	type. Since we want this code to work on old
						// 	systems, work around it.
					} catch (java.lang.NullPointerException e) {
						setErrMessage(messageLocator.getMessage("simplepage.resourcepossibleerror"));
					}
					sakaiId = res.getId();

					if(("application/zip".equals(mimeType) || "application/x-zip-compressed".equals(mimeType))  && isWebsite) {
						// We need to set the sakaiId to the resource id of the index file
						sakaiId = expandZippedResource(sakaiId);
						if (sakaiId == null)
							return;

						// We set this special type for the html field in the db. This allows us to
						// map an icon onto website links in applicationContext.xml
						mimeType = "LBWEBSITE";
					}		    

				} catch (org.sakaiproject.exception.OverQuotaException ignore) {
					setErrMessage(messageLocator.getMessage("simplepage.overquota"));
					return;
				} catch (Exception e) {
					setErrMessage(messageLocator.getMessage("simplepage.resourceerror").replace("{}", e.toString()));
					log.error("addMultimedia error 1 " + e);
					return;
				};
			} else if (mmUrl != null && !mmUrl.trim().equals("")) {
				// 	user specified a URL, create the item
				String url = mmUrl.trim();
				if (!url.matches("\\w+://.*")) {
					if (!url.startsWith("//"))
						url = "//" + url;
					url = "http:" + url;
				}

				name = url;
				String base = url;
				String extension = "";
				int i = url.lastIndexOf("/");
				if (i < 0) i = 0;
				i = url.lastIndexOf(".", i);
				if (i > 0) {
					extension = url.substring(i);
					base = url.substring(0,i);
				}

				String collectionId;
				SimplePage page = getCurrentPage();

				collectionId = getCollectionId(true);

				try {
					// 	urls aren't something people normally think of as resources. Let's hide them
					ContentResourceEdit res = contentHostingService.addResource(collectionId, 
							Validator.escapeResourceName(base),
							Validator.escapeResourceName(extension),
							MAXIMUM_ATTEMPTS_FOR_UNIQUENESS);
					res.setContentType("text/url");
					res.setResourceType("org.sakaiproject.content.types.urlResource");
					res.setContent(url.getBytes());
					contentHostingService.commitResource(res, NotificationService.NOTI_NONE);
					sakaiId = res.getId();
				} catch (org.sakaiproject.exception.OverQuotaException ignore) {
					setErrMessage(messageLocator.getMessage("simplepage.overquota"));
					return;
				} catch (Exception e) {
					setErrMessage(messageLocator.getMessage("simplepage.resourceerror").replace("{}", e.toString()));
					log.error("addMultimedia error 2 " + e);
					return;
				};
				// 	connect to url and get mime type
				mimeType = getTypeOfUrl(url);

			} else
				// 	nothing to do
				return;

			// 	itemId tells us whether it's an existing item
			// 	isMultimedia tells us whether resource or multimedia
			// 	sameWindow is only passed for existing items of type HTML/XHTML
			//   	for new items it should be set true for HTML/XTML, false otherwise
			//   	for existing items it should be set to the passed value for HTML/XMTL, false otherwise
			//   	it is ignored for isMultimedia, as those are always displayed inline in the current page

			SimplePageItem item = null;
			if (itemId == -1 && isMultimedia) {
				int seq = getItemsOnPage(getCurrentPageId()).size() + 1;
				item = simplePageToolDao.makeItem(getCurrentPageId(), seq, SimplePageItem.MULTIMEDIA, sakaiId, name);
			} else if(itemId == -1 && isWebsite) {
				String websiteName = name.substring(0,name.indexOf("."));
				int seq = getItemsOnPage(getCurrentPageId()).size() + 1;
				item = simplePageToolDao.makeItem(getCurrentPageId(), seq, SimplePageItem.RESOURCE, sakaiId, websiteName);
			} else if (itemId == -1) {
				int seq = getItemsOnPage(getCurrentPageId()).size() + 1;
				item = simplePageToolDao.makeItem(getCurrentPageId(), seq, SimplePageItem.RESOURCE, sakaiId, name);
			} else {
				item = findItem(itemId);
				if (item == null)
					return;
				item.setSakaiId(sakaiId);
				item.setName(name);
			}

			if (mimeType != null) {
				item.setHtml(mimeType);
			} else {
				item.setHtml(null);
			}

			// 	if this is an existing item and a resource, leave it alone
			// 	otherwise initialize to false
			if (isMultimedia || itemId == -1)
				item.setSameWindow(false);

			clearImageSize(item);
			try {
				if (itemId == -1)
					saveItem(item);
				else
					update(item);
			} catch (Exception e) {
				// 	saveItem and update produce the errors
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(advisor != null) securityService.popAdvisor();
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param path
	 * @return ret
	 * @throws FileNotFoundException
	 */
	public boolean deleteRecursive(File path) throws FileNotFoundException{
		if (!path.exists()) throw new FileNotFoundException(path.getAbsolutePath());
		boolean ret = true;
		if (path.isDirectory()){
			for (File f : path.listFiles()){
				ret = ret && deleteRecursive(f);
			}
		}
		return ret && path.delete();
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public void importCc() {
		if (!canEditPage())
			return;

		MultipartFile file = null;

		if (multipartMap.size() > 0) {
			// user specified a file, create it
			file = multipartMap.values().iterator().next();
			if (file.isEmpty())
				file = null;
		}

		if (file != null) {
			File cc = null;
			File root = null;
			try {
				cc = File.createTempFile("ccloader", "file");
				root = File.createTempFile("ccloader", "root");
				if (root.exists())
					root.delete();
				root.mkdir();
				BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cc));
				byte[] buffer = new byte[8096];
				int n = 0;
				while ((n = bis.read(buffer, 0, 8096)) >= 0) {
					if (n > 0)
						bos.write(buffer, 0, n);
				}
				bis.close();
				bos.close();

				CartridgeLoader cartridgeLoader = ZipLoader.getUtilities(cc, root.getCanonicalPath());
				Parser parser = Parser.createCartridgeParser(cartridgeLoader);

				LessonEntity quizobject = null;
				for (LessonEntity q = quizEntity; q != null; q = q.getNextEntity()) {
					if (q.getToolId().equals(quiztool))
						quizobject = q;
				}

				LessonEntity assignobject = null;
				for (LessonEntity q = assignmentEntity; q != null; q = q.getNextEntity()) {
					if (q.getToolId().equals(assigntool))
						assignobject = q;
				}


				LessonEntity topicobject = null;
				for (LessonEntity q = forumEntity; q != null; q = q.getNextEntity()) {
					if (q.getToolId().equals(topictool))
						topicobject = q;
				}

				parser.parse(new PrintHandler(this, cartridgeLoader, simplePageToolDao, quizobject, topicobject, bltiEntity, assignobject, importtop));
				setTopRefresh();
			} catch (Exception e) {
				setErrKey("simplepage.cc-error", "");
				System.out.println("exception in importcc, backtrace follows " + e);
				e.printStackTrace();
			} finally {
				if (cc != null)
					try {
						deleteRecursive(cc);
					} catch (Exception e){
						System.out.println("Unable to delete temp file " + cc);
					}
				try {
					deleteRecursive(root);
				} catch (Exception e){
					System.out.println("Unable to delete temp file " + cc);
				}
			}
		}
	}

	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Called by edit dialog to update parameters of a Youtube item."
	 */
	public void updateYoutube() {
		if (!itemOk(youtubeId))
			return;
		if (!canEditPage())
			return;

		SimplePageItem item = findItem(youtubeId);

		// find the new key, if the new thing is a legit youtube url
		String key = getYoutubeKeyFromUrl(youtubeURL);
		if (key == null) {
			setErrMessage(messageLocator.getMessage("simplepage.must_be_youtube"));
			return;
		}

		// oldkey had better work, since the youtube edit woudln't
		// be displayed if it wasn't recognized
		String oldkey = getYoutubeKey(item);

		// if there's a new youtube URL, and it's different from
		// the old one, update the URL if they are different
		if (key != null && !key.equals(oldkey)) {
			String url = "http://www.youtube.com/watch#!v=" + key;
			String siteId = getCurrentPage().getSiteId();
			String collectionId = getCollectionId(true);

			SecurityAdvisor advisor = null;
			try {
				if(getCurrentPage().getOwner() != null) {
					advisor = new SecurityAdvisor() {
						public SecurityAdvice isAllowed(String userId, String function, String reference) {
							return SecurityAdvice.ALLOWED;
						}
					};
					securityService.pushAdvisor(advisor);
				}

				ContentResourceEdit res = contentHostingService.addResource(collectionId,Validator.escapeResourceName("Youtube video " + key),Validator.escapeResourceName("swf"),MAXIMUM_ATTEMPTS_FOR_UNIQUENESS);
				res.setContentType("text/url");
				res.setResourceType("org.sakaiproject.content.types.urlResource");
				res.setContent(url.getBytes());
				contentHostingService.commitResource(res, NotificationService.NOTI_NONE);
				item.setSakaiId(res.getId());

			} catch (org.sakaiproject.exception.OverQuotaException ignore) {
				setErrMessage(messageLocator.getMessage("simplepage.overquota"));
			} catch (Exception e) {
				setErrMessage(messageLocator.getMessage("simplepage.resourceerror").replace("{}", e.toString()));
				log.error("addMultimedia error 3 " + e);
			}finally {
				if(advisor != null) securityService.popAdvisor();
			}
		}

		// even if there's some oddity with URLs, we do these updates
		item.setHeight(height);
		item.setWidth(width);
		item.setDescription(description);
		update(item);

		setItemGroups(item, selectedGroups);

	}

	/**
	 * Adds or removes the requirement to have site.upd in order to see a page
	 * i.e. hide or unhide a page
	 * @param pageId
	 *            The Id of the Page
	 * @param visible
	 * @return true for success, false for failure
	 * @throws IdUnusedException
	 *             , PermissionException
	 */
	private boolean pageVisibilityHelper(Site site, String pageId, boolean visible) throws IdUnusedException, PermissionException {
		SitePage page = site.getPage(pageId);
		List<ToolConfiguration> tools = page.getTools();
		Iterator<ToolConfiguration> iterator = tools.iterator();

		// If all the tools on a page require site.upd then only users with site.upd will see
		// the page in the site nav of Charon... not sure about the other Sakai portals floating
		// about
		while (iterator.hasNext()) {
			ToolConfiguration placement = iterator.next();
			Properties roleConfig = placement.getPlacementConfig();
			String roleList = roleConfig.getProperty("functions.require");
			String visibility = roleConfig.getProperty("sakai-portal:visible");
			boolean saveChanges = false;

			if (roleList == null) {
				roleList = "";
			}
			if (!(roleList.indexOf(SITE_UPD) > -1) && !visible) {
				if (roleList.length() > 0) {
					roleList += ",";
				}
				roleList += SITE_UPD;
				saveChanges = true;
			} else if ((roleList.indexOf(SITE_UPD) > -1) && visible) {
				roleList = roleList.replaceAll("," + SITE_UPD, "");
				roleList = roleList.replaceAll(SITE_UPD, "");
				saveChanges = true;
			}

			if (saveChanges) {
				roleConfig.setProperty("functions.require", roleList);
				if (visible)
					roleConfig.remove("sakai-portal:visible");
				else
					roleConfig.setProperty("sakai-portal:visible", "false");

				placement.save();

				siteService.save(site);
			}

		}

		return true;
	}

	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Used by edit dialog to update properties of a multimedia object."
	 */
	public void updateMovie() {
		if (!itemOk(itemId))
			return;
		if (!canEditPage())
			return;

		SimplePageItem item = findItem(itemId);
		item.setHeight(height);
		item.setWidth(width);
		item.setDescription(description);
		item.setHtml(mimetype);
		update(item);

		setItemGroups(item, selectedGroups);

	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public void addCommentsSection() {
		if(canEditPage()) {
			SimplePageItem item = appendItem("", messageLocator.getMessage("simplepage.comments-section"), SimplePageItem.COMMENTS);
			item.setDescription(messageLocator.getMessage("simplepage.comments-section"));
			update(item);

			// Must clear the cache so that the new item appears on the page
			itemsCache.remove(getCurrentPage().getPageId());
		}else {
			setErrMessage(messageLocator.getMessage("simplepage.permissions-general"));
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Admins can always edit.  Authors can edit for 30 minutes.
	 *  
	 *  The second parameter is only used to distinguish this method from
	 *  the one directly below it.  Allowing CommentsProducer to cache whether
	 *  or not the current user can edit the page, without having to hit the
	 *  database each time."
	 * @param c
	 * @param canEditPage
	 * @return
	 */
	public boolean canModifyComment(SimplePageComment c, boolean canEditPage) {
		if(canEditPage) return true;

		if(c.getAuthor().equals(UserDirectoryService.getCurrentUser().getId())){
			// Author can edit for 30 minutes.
			if(System.currentTimeMillis() - c.getTimePosted().getTime() <= 1800000) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

	// See method above
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param c
	 * @return
	 */
	public boolean canModifyComment(SimplePageComment c) {
		return canModifyComment(c, canEditPage());
	}

	
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "May add or edit comments."
	 * @return
	 */
	public String addComment() {
		boolean html = false;

		// Patch in the fancy editor's comment, if it's been used
		if(formattedComment != null && !formattedComment.equals("")) {
			comment = formattedComment;
			html = true;
		}

		StringBuilder error = new StringBuilder();
		comment = FormattedText.processFormattedText(comment, error);

		if(comment == null || comment.equals("")) {
			setErrMessage(messageLocator.getMessage("simplepage.empty-comment-error"));
			return "failure";
		}

		if(editId == null || editId.equals("")) {
			String userId = UserDirectoryService.getCurrentUser().getId();

			Double grade = null;
			if(findItem(itemId).getGradebookId() != null) {
				List<SimplePageComment> comments = simplePageToolDao.findCommentsOnItemByAuthor(itemId, userId);
				if(comments != null && comments.size() > 0) {
					grade = comments.get(0).getPoints();
				}
			}

			SimplePageComment commentObject = simplePageToolDao.makeComment(itemId, getCurrentPage().getPageId(), userId, comment, IdManager.getInstance().createUuid(), html);
			commentObject.setPoints(grade);

			saveItem(commentObject, false);
		}else {
			SimplePageComment commentObject = simplePageToolDao.findCommentById(Long.valueOf(editId));
			if(commentObject != null && canModifyComment(commentObject)) {
				commentObject.setComment(comment);
				update(commentObject, false);
			}else {
				setErrMessage(messageLocator.getMessage("simplepage.permissions-general"));
				return "failure";
			}
		}

		if(getCurrentPage().getOwner() != null) {
			SimpleStudentPage student = simplePageToolDao.findStudentPage(getCurrentPage().getTopParent());
			student.setLastCommentChange(new Date());
			update(student, false);
		}

		return "added-comment";
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return resultado de la operaci&oacuten.
	 */
	public String updateComments() {
		if(canEditPage()) {
			SimplePageItem comment = findItem(itemId);
			comment.setAnonymous(anonymous);
			setItemGroups(comment, selectedGroups);
			comment.setRequired(required);
			comment.setPrerequisite(prerequisite);

			if(maxPoints == null || maxPoints.equals("")) {
				maxPoints = "1";
			}

			if(graded) {
				int points;
				try {
					points = Integer.valueOf(maxPoints);
				}catch(Exception ex) {
					setErrMessage(messageLocator.getMessage("simplepage.integer-expected"));
					return "failure";
				}

				// TODO: should update instead of delete/add
				if(comment.getGradebookId() != null && !comment.getGradebookPoints().equals(points)) {
					gradebookIfc.removeExternalAssessment(getCurrentSiteId(), comment.getGradebookId());
				}

				if(comment.getGradebookId() == null || !comment.getGradebookPoints().equals(points)) {
					String pageTitle = "";
					String gradebookId = "";

					boolean add = true;

					if(comment.getPageId() >= 0) {
						pageTitle = getPage(comment.getPageId()).getTitle();
						gradebookId = "lesson-builder:comment:" + comment.getId();

						add = gradebookIfc.addExternalAssessment(getCurrentSiteId(), "lesson-builder:comment:" + comment.getId(), null,
								pageTitle + " Comments (item:" + comment.getId() + ")", Integer.valueOf(maxPoints), null, "Lesson Builder");
						if(!add) {
							setErrMessage(messageLocator.getMessage("simplepage.no-gradebook"));
						}else {
							comment.setGradebookTitle(pageTitle + " Comments (item:" + comment.getId() + ")");
						}
					}else {
						// Must be a student page comments tool.
						SimpleStudentPage studentPage = simplePageToolDao.findStudentPage(Long.valueOf(comment.getSakaiId()));
						SimplePageItem studentPageItem = simplePageToolDao.findItem(studentPage.getItemId());

						//pageTitle = simplePageToolDao.findStudentPage(Long.valueOf(comment.getSakaiId())).getTitle();
						gradebookId = "lesson-builder:page-comment:" + studentPageItem.getId();

					}

					if(add) {
						comment.setGradebookId(gradebookId);
						comment.setGradebookPoints(points);
						regradeComments(comment);
					}
				}
			}else if(comment.getGradebookId() != null && comment.getPageId() >= 0) {
				gradebookIfc.removeExternalAssessment(getCurrentSiteId(), comment.getGradebookId());
				comment.setGradebookId(null);
				comment.setGradebookPoints(null);
			}

			// for forced comments, the UI won't ever do this, but if
			// it does, update will fail with permissions
			update(comment);
			return "success";
		}else {
			setErrMessage(messageLocator.getMessage("simplepage.permissions-general"));
			return "failure";
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param comment
	 */
	private void regradeComments(SimplePageItem comment) {
		List<SimplePageComment> comments = simplePageToolDao.findComments(comment.getId());
		for(SimplePageComment c : comments) {
			if(c.getPoints() != null) {
				gradebookIfc.updateExternalAssessmentScore(getCurrentSiteId(), comment.getGradebookId(),
						c.getAuthor(), String.valueOf(c.getPoints()));
			}
		}
	}

	/**
	 *  M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "Comments aren't actually deleted. The comment field is set to empty.
	 * This is so that the namings remain consistent when the comment section
	 * is set to show names as anonymous.  Otherwise, deleting a post could change
	 * the numbering, which hinders discussion."
	 *	 * @param commentUUID
	 * @return
	 */
	public String deleteComment(String commentUUID) {
		SimplePageComment comment = simplePageToolDao.findCommentByUUID(commentUUID);

		if(comment != null && comment.getPageId() == getCurrentPage().getPageId()) {
			if(canModifyComment(comment)) {
				comment.setComment("");
				update(comment, false);
				return "success";
			}
		}

		setErrMessage(messageLocator.getMessage("simplepage.comment-permissions-error"));
		return "failure";
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public void addStudentContentSection() {
		if(getCurrentPage().getOwner() == null && canEditPage()) {
			SimplePageItem item = appendItem("", messageLocator.getMessage("simplepage.student-content"), SimplePageItem.STUDENT_CONTENT);
			item.setDescription(messageLocator.getMessage("simplepage.student-content"));
			update(item);

			// Must clear the cache so that the new item appears on the page
			itemsCache.remove(getCurrentPage().getPageId());
		}else {
			setErrMessage(messageLocator.getMessage("simplepage.permissions-general"));
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @return true or false
	 */
	public boolean createStudentPage(long itemId) {
		SimplePage curr = getCurrentPage();
		User user = UserDirectoryService.getCurrentUser();

		// Need to make sure the section exists
		SimplePageItem containerItem = simplePageToolDao.findItem(itemId);

		// We want to make sure each student only has one top level page per section.
		SimpleStudentPage page = simplePageToolDao.findStudentPage(itemId, user.getId());

		if(page == null && containerItem != null && containerItem.getType() == SimplePageItem.STUDENT_CONTENT && canReadPage()) {
			// First create object in lesson_builder_pages.
			String title = user.getDisplayName();
			if (containerItem.isAnonymous()) {
				List<SimpleStudentPage>  otherPages = simplePageToolDao.findStudentPages(itemId);
				int serial = 1;
				if (otherPages != null)
					serial = otherPages.size() + 1;
				title = messageLocator.getMessage("simplepage.anonymous") + " " + serial;
			}			
			SimplePage newPage = simplePageToolDao.makePage(curr.getToolId(), curr.getSiteId(), title, curr.getPageId(), null);
			newPage.setOwner(user.getId());
			newPage.setGroupOwned(false);
			saveItem(newPage, false);

			// Then attach the lesson_builder_student_pages item.
			page = simplePageToolDao.makeStudentPage(itemId, newPage.getPageId(), title, user.getId(), false);

			SimplePageItem commentsItem = simplePageToolDao.makeItem(-1, -1, SimplePageItem.COMMENTS, null, messageLocator.getMessage("simplepage.comments-section"));
			saveItem(commentsItem, false);

			page.setCommentsSection(commentsItem.getId());

			saveItem(page, false);

			commentsItem.setAnonymous(containerItem.getForcedCommentsAnonymous());
			commentsItem.setSakaiId(String.valueOf(page.getId()));
			update(commentsItem, false);

			newPage.setTopParent(page.getId());
			update(newPage, false);

			try {
				updatePageItem(containerItem.getId());
				updatePageObject(newPage.getPageId());
				adjustPath("push", newPage.getPageId(), containerItem.getId(), newPage.getTitle());
			}catch(Exception ex) {
				setErrMessage(messageLocator.getMessage("simplepage.permissions-general"));
				return false;
			}

			// Reset the edit cache so that they can actually edit their page.
			editPrivs = 1;

			return true;
		}else if(page != null) { 
			setErrMessage(messageLocator.getMessage("simplepage.page-exists"));
			return false;
		}else{
			return false;
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param itemId
	 * @return map
	 */
	public HashMap<Long, SimplePageLogEntry> cacheStudentPageLogEntries(long itemId) {
		List<SimplePageLogEntry> entries = simplePageToolDao.getStudentPageLogEntries(itemId, UserDirectoryService.getCurrentUser().getId());

		HashMap<Long, SimplePageLogEntry> map = new HashMap<Long, SimplePageLogEntry>();
		for(SimplePageLogEntry entry : entries) {
			logCache.put(entry.getItemId() + "-" + entry.getStudentPageId(), entry);
			map.put(entry.getStudentPageId(), entry);
		}

		return map;
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private void pushAdvisorAlways() {
		securityService.pushAdvisor(new SecurityAdvisor() {
			public SecurityAdvice isAllowed(String userId, String function, String reference) {
				return SecurityAdvice.ALLOWED;
			}
		});
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return true or false
	 */
	private boolean pushAdvisor() {
		if(getCurrentPage().getOwner() != null) {
			securityService.pushAdvisor(new SecurityAdvisor() {
				public SecurityAdvice isAllowed(String userId, String function, String reference) {
					return SecurityAdvice.ALLOWED;
				}
			});
			return true;
		}else {
			return false;
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private void popAdvisor() {
		securityService.popAdvisor();
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return resultado de la operaci&oacuten.
	 */
	public String updateStudent() {
		if(canEditPage()) {
			SimplePageItem page = findItem(itemId);
			page.setAnonymous(anonymous);
			page.setShowComments(comments);
			page.setForcedCommentsAnonymous(forcedAnon);
			page.setRequired(required);
			page.setPrerequisite(prerequisite);
			setItemGroups(page, selectedGroups);

			// Update the comments tools to reflect any changes
			if(comments) {
				List<SimpleStudentPage> pages = simplePageToolDao.findStudentPages(itemId);
				for(SimpleStudentPage p : pages) {
					if(p.getCommentsSection() != null) {
						SimplePageItem item = simplePageToolDao.findItem(p.getCommentsSection());
						if(item.isAnonymous() != forcedAnon) {
							item.setAnonymous(forcedAnon);
							update(item);
						}
					}
				}
			}

			if(maxPoints == null || maxPoints.equals("")) {
				maxPoints = "1";
			}

			if(sMaxPoints == null || sMaxPoints.equals("")) {
				sMaxPoints = "1";
			}

			// Handle the grading of pages
			if(graded) {
				int points;
				try {
					points = Integer.valueOf(maxPoints);
				}catch(Exception ex) {
					setErrMessage(messageLocator.getMessage("simplepage.integer-expected"));
					return "failure";
				}

				// TODO: should update instead of delete/add
				if(page.getGradebookId() != null && !page.getGradebookPoints().equals(points)) {
					gradebookIfc.removeExternalAssessment(getCurrentSiteId(), page.getGradebookId());
				}

				if(page.getGradebookId() == null || !page.getGradebookPoints().equals(points)) {
					boolean add = gradebookIfc.addExternalAssessment(getCurrentSiteId(), "lesson-builder:page:" + page.getId(), null,
							getPage(page.getPageId()).getTitle() + " Student Pages (item:" + page.getId() + ")", Integer.valueOf(maxPoints), null, "Lesson Builder");

					if(!add) {
						setErrMessage(messageLocator.getMessage("simplepage.no-gradebook"));
					}else {
						page.setGradebookId("lesson-builder:page:" + page.getId());
						page.setGradebookTitle(getPage(page.getPageId()).getTitle() + " Student Pages (item:" + page.getId() + ")");
						page.setGradebookPoints(points);
						regradeStudentPages(page);
					}
				}
			}else if(page.getGradebookId() != null) {
				gradebookIfc.removeExternalAssessment(getCurrentSiteId(), page.getGradebookId());
				page.setGradebookId(null);
				page.setGradebookPoints(null);
			}

			// Handling the grading of comments on pages
			if(sGraded) {
				int points;
				try {
					points = Integer.valueOf(sMaxPoints);
				}catch(Exception ex) {
					setErrMessage(messageLocator.getMessage("simplepage.integer-expected"));
					return "failure";
				}

				// todo: use update instead of delete, add
				if(page.getAltGradebook() != null && !page.getAltPoints().equals(points)) {
					gradebookIfc.removeExternalAssessment(getCurrentSiteId(), page.getAltGradebook());
				}

				if(page.getAltGradebook() == null || !page.getAltPoints().equals(points)) {
					String title = getPage(page.getPageId()).getTitle() + " Student Page Comments (item:" + page.getId() + ")";
					boolean add = gradebookIfc.addExternalAssessment(getCurrentSiteId(), "lesson-builder:page-comment:" + page.getId(), null,
							title, points, null, "Lesson Builder");
					// The assessment couldn't be added
					if(!add) {
						setErrMessage(messageLocator.getMessage("simplepage.no-gradebook"));
					}else {
						page.setAltGradebook("lesson-builder:page-comment:" + page.getId());
						page.setAltGradebookTitle(title);
						page.setAltPoints(points);
						regradeStudentPageComments(page);
					}
				}
			}else if(page.getAltGradebook() != null) {
				gradebookIfc.removeExternalAssessment(getCurrentSiteId(), page.getAltGradebook());
				page.setAltGradebook(null);
				page.setAltPoints(null);
				ungradeStudentPageComments(page);
			}

			update(page);

			return "success";
		}else {
			setErrMessage(messageLocator.getMessage("simplepage.permissions-general"));
			return "failure";
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param pageItem
	 */
	private void regradeStudentPageComments(SimplePageItem pageItem) {
		List<SimpleStudentPage> pages = simplePageToolDao.findStudentPages(pageItem.getId());
		for(SimpleStudentPage c : pages) {
			SimplePageItem comments = findItem(c.getCommentsSection());
			comments.setGradebookId(pageItem.getAltGradebook());
			comments.setGradebookPoints(pageItem.getAltPoints());
			update(comments);
			regradeComments(comments);
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param pageItem
	 */
	private void ungradeStudentPageComments(SimplePageItem pageItem) {
		List<SimpleStudentPage> pages = simplePageToolDao.findStudentPages(pageItem.getId());
		for(SimpleStudentPage c : pages) {
			SimplePageItem comments = findItem(c.getCommentsSection());
			comments.setGradebookId(null);
			comments.setGradebookPoints(null);
			update(comments);
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param pageItem
	 */
	private void regradeStudentPages(SimplePageItem pageItem) {
		List<SimpleStudentPage> pages = simplePageToolDao.findStudentPages(pageItem.getId());
		for(SimpleStudentPage c : pages) {
			if(c.getPoints() != null) {
				gradebookIfc.updateExternalAssessmentScore(getCurrentSiteId(), pageItem.getGradebookId(),
						c.getOwner(), String.valueOf(c.getPoints()));
			}
		}
	}

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param resourceId
	 * @return relativeUrl
	 */
	private String expandZippedResource(String resourceId) {
		String contentCollectionId = resourceId.substring(0, resourceId.lastIndexOf(".")) + "/";

		try {
			contentHostingService.removeCollection(contentCollectionId);
		} catch (Exception e) {
			log.info("Failed to delete expanded collection");
		}

		// Q: Are we running a kernel with KNL-273?
		Class contentHostingInterface = ContentHostingService.class;
		try {
			Method expandMethod = contentHostingInterface.getMethod("expandZippedResource", new Class[] { String.class });
			// Expand the website
			expandMethod.invoke(contentHostingService, new Object[] { resourceId });
		} catch (NoSuchMethodException nsme) {
			// A: No; should be impossible, UI already tested
			return null;
		} catch (Exception e) {
			// This is very strange. The kernel code will normally trap exceptions
			// and print a backtrace, robbing us of any ability to see that something
			// has gone wrong.
			log.error("Exception thrown by expandZippedResource", e);
			setErrKey("simplepage.website.cantexpand", null);
			return null;
		}

		// Now set the html ok flag

		try {
			ContentCollectionEdit cce = contentHostingService.editCollection(contentCollectionId);

			ResourcePropertiesEdit props = cce.getPropertiesEdit();
			props.addProperty(PROP_ALLOW_INLINE, "true");
			List<String> children = cce.getMembers();

			for (int j = 0; j < children.size(); j++) {
				String resId = children.get(j);
				if (resId.endsWith("/")) {
					setPropertyOnFolderRecursively(resId, PROP_ALLOW_INLINE, "true");
				}
			}

			contentHostingService.commitCollection(cce);
			// when you tell someone to create a zip file with index.html at the
			// top level, it's unclear whether they do "zip directory" or "cd; zip *"
			// make both work

			ContentCollection cc = cce;

			if (children.size() == 1 && children.get(0).endsWith("/")) {
				contentCollectionId = children.get(0);
				cc = contentHostingService.getCollection(contentCollectionId);
			}

			// Now lets work out what type it is and return the appropriate
			// index url

			String index = null;

			String name = contentCollectionId.substring(0, contentCollectionId.lastIndexOf("/"));
			name = name.substring(name.lastIndexOf("/") + 1);
			if (name.endsWith("_HTML")) {
				// This is probably Wimba Create as wc adds this suffix to the
				// zips it creates
				name = name.substring(0, name.indexOf("_HTML"));
			}

			ContentEntity ce = cc.getMember(contentCollectionId + name + ".xml");
			if (ce != null) {
				index = "index.htm";
			}

			// Test for Camtasia
			ce = cc.getMember(contentCollectionId + "ProductionInfo.xml");
			if (ce != null) {
				index = name + ".html";
			}

			// Test for Articulate
			ce = cc.getMember(contentCollectionId + "player.html");
			if (ce != null) {
				index = "player.html";
			}

			// Test for generic web site
			ce = cc.getMember(contentCollectionId + "index.html");
			if (ce != null) {
				index = "index.html";
			}

			ce = cc.getMember(contentCollectionId + "index.htm");
			if (ce != null) {
				index = "index.htm";
			}

			if (index == null) {
				// /content/group/nnnn/folder
				int i = contentCollectionId.indexOf("/", 1);
				i = contentCollectionId.indexOf("/", i+1);

				setErrKey("simplepage.website.noindex", contentCollectionId.substring(i));
				return null;
			}

			//String relativeUrl = contentCollectionId.substring(contentCollectionId.indexOf("/Lesson Builder")) + index;
			// collections end in / already
			String relativeUrl = contentCollectionId + index;
			return relativeUrl;
		} catch (Exception e) {
			log.error(e);
			setErrKey("simplepage.website.cantexpand", null);
			return null;
		}
	}


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * "See if there is a folder in which images, etc, are likely to be
	 stored for this resource. This only applies to HTML files
	 for index.html, etc, it's the containing folder
	 otherwise, if it's an HTML file, look for a folder with the same name."
	 * @param resourceId
	 * @return resourceId
	 */
	public static String associatedFolder(String resourceId) {
		int i = resourceId.lastIndexOf("/");
		String folder = null;
		String name = null;
		if (i >= 0) {
			folder = resourceId.substring(0, i+1);  // include trailing
			name = resourceId.substring(i+1);
		} else
			return null;

		String folderName = resourceId.substring(0, i);
		i = folderName.lastIndexOf("/");
		if (i >= 0)
			folderName = folderName.substring(i+1);
		else
			return null;
		if (folderName.endsWith("_HTML"))  // wimba create
			folderName = folderName.substring(0, folderName.indexOf("_HTML"));

		// folder is whole folder
		// folderName is last atom of folder name
		// name is last atom of resource id

		if (name.equals("index.html") || name.equals("index.htm") || name.equals(folderName + ".html"))
			return folder;

		if (resourceId.endsWith(".html") || resourceId.endsWith(".htm")) {
			i = resourceId.lastIndexOf(".");
			resourceId = resourceId.substring(0, i) + "/";
			// no need to check whether it actually exists
			return resourceId;
		}
		return null;
	}


	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @param resourceId
	 * @param property
	 * @param value
	 */
	private void setPropertyOnFolderRecursively(String resourceId, String property, String value) {

		try {
			if (contentHostingService.isCollection(resourceId)) {
				// collection
				ContentCollectionEdit col = contentHostingService.editCollection(resourceId);

				ResourcePropertiesEdit resourceProperties = col.getPropertiesEdit();
				resourceProperties.addProperty(property, Boolean.valueOf(value).toString());
				contentHostingService.commitCollection(col);

				List<String> children = col.getMembers();
				for (int i = 0; i < children.size(); i++) {
					String resId = children.get(i);
					if (resId.endsWith("/")) {
						setPropertyOnFolderRecursively(resId, property, value);
					}
				}

			} else {
				// resource
				ContentResourceEdit res = contentHostingService.editResource(resourceId);
				ResourcePropertiesEdit resourceProperties = res.getPropertiesEdit();
				resourceProperties.addProperty(property, Boolean.valueOf(value).toString());
				contentHostingService.commitResource(res, NotificationService.NOTI_NONE);
			}
		} catch (Exception pe) {
			pe.printStackTrace();
		}
	}

	/**
	 * Returns an ArrayList containing all of the system-wide and site-wide CSS files.
	 * 
	 * One entry may be null, to separate system-wide from site-wide.
	 * 
	 * Caches lookups, to prevent extra database hits.
	 */
	public ArrayList<ContentResource> getAvailableCss() {
		ArrayList<ContentResource> list = new ArrayList<ContentResource>();

		String collectionId = contentHostingService.getSiteCollection(getCurrentSiteId()) + "LB-CSS/";

		List<ContentResource> resources = (List<ContentResource>) resourceCache.get(collectionId);
		if(resources == null) {
			resources = contentHostingService.getAllResources(collectionId);
			if(resources == null) resources = new ArrayList<ContentResource>();

			resourceCache.put(collectionId, resources);
		}

		for(ContentResource r : resources) {
			if(r.getUrl().endsWith(".css")) {
				list.add(r);
			}
		}

		collectionId = "/public/LB-CSS/";

		resources = null;
		resources = (List<ContentResource>) resourceCache.get(collectionId);
		if(resources == null) {
			resources = contentHostingService.getAllResources(collectionId);
			if(resources == null) resources = new ArrayList<ContentResource>();

			resourceCache.put(collectionId, resources);
		}

		// Insert separator
		if(list.size() > 0 && resources.size() > 0) {
			list.add(null);
		}

		for(ContentResource r : resources) {
			if(r.getUrl().endsWith(".css")) {
				list.add(r);
			}
		}


		return list;
	}
	/**
	 * First checks if a sheet has been explicitly set.  Then checks for a default
	 * at the site level.  It then finally checks to see if there is a default on the
	 * system level.
	 * 
	 * Caches lookups to prevent too many lookups in the database.
	 */
	public ContentResource getCssForCurrentPage() {
		ContentResource resource = null;

		// I'm always using ArrayList for the resourceCache so that I can distinguish
		// between never having looked up the resource, and the resource not being there.
		// Otherwise, if I just check for null, if a resource isn't there, it will still check
		// every time.

		String collectionId = getCurrentPage().getCssSheet();
		if(getCurrentPage().getCssSheet() != null) {
			try {
				ArrayList<ContentResource> resources = (ArrayList<ContentResource>) resourceCache.get(collectionId);
				if(resources == null) {
					resource = contentHostingService.getResource(collectionId);
					resources = new ArrayList<ContentResource>();
					resources.add(resource);
					resourceCache.put(collectionId, resources);
				}

				if(resources.size() > 0) {
					return resources.get(0);
				}else {
					throw new Exception();
				}
			}catch(Exception ex) {
				if(canEditPage()) {
					setErrMessage(messageLocator.getMessage("simplepage.broken-css"));
				}

				resourceCache.put(collectionId, new ArrayList<ContentResource>());
			}
		}

		collectionId = contentHostingService.getSiteCollection(getCurrentSiteId())
				+ "LB-CSS/" + ServerConfigurationService.getString("lessonbuilder.default.css", "default.css");

		try {
			ArrayList<ContentResource> resources = (ArrayList<ContentResource>) resourceCache.get(collectionId);
			if(resources == null) {
				resource = contentHostingService.getResource(collectionId);
				resources = new ArrayList<ContentResource>();
				resources.add(resource);
				resourceCache.put(collectionId, resources);
			}

			if(resources.size() > 0) {
				return resources.get(0);
			}
		}catch(Exception ignore) {
			resourceCache.put(collectionId, new ArrayList<ContentResource>());
		}

		collectionId = "/public/LB-CSS/" + ServerConfigurationService.getString("lessonbuilder.default.css", "default.css");

		try {
			ArrayList<ContentResource> resources = (ArrayList<ContentResource>) resourceCache.get(collectionId);
			if(resources == null) {
				resource = contentHostingService.getResource(collectionId);
				resources = new ArrayList<ContentResource>();
				resources.add(resource);
				resourceCache.put(collectionId, resources);
			}

			if(resources.size() > 0) {
				return resources.get(0);
			}
		}catch(Exception ignore) {
			resourceCache.put(collectionId, new ArrayList<ContentResource>());
		}

		return null;
	}

	/**
	 * Agrega una actividad.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addActividad() {

		String respuesta = "failed";

		if(tipoUnidad == 0)
		{			
			getTiposActividad();
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, new ResourceLoader().getLocale());		
			df.setTimeZone(TimeService.getLocalTimeZone());
			if (!itemOk(itemId))
				return "permission-failed";
			if (!canEditPage())
				return "permission-failed";

			//if (selectedActividad == null) {
			//	return "failure";
			//} else {
			try {
				//			    LessonEntity selectedObject = actividadEntity.getEntity(selectedActividad);
				//			    if (selectedObject == null)
				//				return "failure";

				SimplePageItem i;

				//buscar el item de recursiva
				List<String>elist = new ArrayList<String>();
				Item item = simplePageToolDao.getItemAshyi("Actividad Atomica");
				Tipo tipoS=null;
				for(int j = 0; j<tipoActividad.size();j++)
				{
					if(tipoActividad.get(j).getNombre().equals(tipoA) && (!tipoA.equals(cadenaOpciones)))
					{
						tipoS = tipoActividad.get(j);
					}
				}	

				//4 --> actividad
				Actividad ac = appendActividad(tipoS, item, nomberA, descripcionA, 4, Integer.valueOf(dedicacionActividad),nivelActividades);
				/*if(opcionIniciaFinal.equals("Actividad Inicial"))
				{
					ac.setEs_inicial(true);
					ac.setEs_final(false);
				}
				else if(opcionIniciaFinal.equals("Actividad Final"))
				{
					ac.setEs_inicial(false);
					ac.setEs_final(true);
				}
				else if(opcionIniciaFinal.equals("Actividad Intermedia"))
				{
					ac.setEs_inicial(false);
					ac.setEs_final(false);
				}*/
				//almacenar actividad
				saveActividad(ac);
				//Agregar caracteristicas a actividad
				addCaracteristicasActividad(ac);
				//Al,acenar objetivos de actividad 3: nivel de recursividad recursiva unidad didactica
				addObjetivosActividad(ac,3);	
				//Almacenar recurso de actividad --> 2 actividad atomica
				addRecursoActividad(ac,2);
				//Agregar Actividad a Actividadde Mayor Nivel (unidad didactica) 3: nivel de recursividad recursivo
				String nombreUnidad = getCurrentPageItem(getItemId()).getName();
				addActividadToActividadMayorNivel(ac, nombreUnidad,3);

				//ac.setCaracteristicas(caracteristicas);
				//updateActividad(ac);
				//saveActividad(ac, caracteristicas);
				//}
				if(!tipoA.equals(cadenaOpciones))	
				{
					if( tipoA.equals("Quiz") || tipoA.equals("Parcial"))
						respuesta = "succesExamen";
					else if( tipoA.equals("Video") )
						respuesta = "succesMultimedia";
					else if( tipoA.equals("Tarea") )
						respuesta = "succesTarea";
					else if( tipoA.equals("Foro") )
						respuesta = "succesForo";
					else
						respuesta = "succesRecurso";
				}				
				//return "success";
			} catch (Exception ex) {
				ex.printStackTrace();
				respuesta = "failure";
			} finally {
				selectedActividad = null;
			}

		}else if(tipoUnidad == 2) // unidades d
		{	
			//buscar el item de recursiva
			List<String>elist = new ArrayList<String>();
			Item i = simplePageToolDao.getItemAshyi("Actividad Recursiva");	
			Actividad ac = appendActividad(i, nomberA, 3, subpageDedicacion, nivelActividades);
			//almacenar actividad
			saveActividad(ac);	
			//Al,acenar objetivos de actividad 2: nivel de recursividad tema
			//addObjetivosActividad(ac, 2);
			addObjetivosActividad(ac, 1);
			//Agregar Actividad a Actividadde Mayor Nivel (curso) 2: nivel de recursividad macro
			//addActividadToActividadMayorNivel(ac, getCurrentPageItem(getItemId()).getName(),2);	
			addActividadToActividadMayorNivel(ac, temaTitle,2);
			//
			respuesta = "success";
		}
		else if(tipoUnidad == 1) //temas
		{	
			//buscar el item de recursiva
			List<String>elist = new ArrayList<String>();
			Item i = simplePageToolDao.getItemAshyi("Actividad Recursiva");
			//2 --> tema
			subpageDedicacion = "100";
			Actividad ac = appendActividad(i, nomberA, 2, subpageDedicacion, nivelActividades);
			//almacenar actividad
			saveActividad(ac);	
			//Al,acenar objetivos de actividad 1: nivel de recursividad curso

			addObjetivosActividad(getCurrentSite().getTitle(), 1, ac);
			//Agregar Actividad a Actividadde Mayor Nivel (curso) 1: nivel de recursividad curso
			addActividadToActividadMayorNivel(ac, getCurrentSite().getTitle(),1);
			//
			respuesta = "succes";
		}
		//}
		return respuesta;
	}

	/**
	 * Agrega objetivos a una actividad.
	 * @param nombre
	 * @param nR nivel de recursividad.
	 * @param ac la actividad.
	 */
	private void addObjetivosActividad(String nombre, int nR, Actividad ac) {

//		int id = getUltimaActividad(ac.getNombre());

		String err = null;

//		ac.setIdActividad(id);

		String[] objs = simplePageToolDao.getObjetivosActividad(nR, nombre);		

		for(String obj : objs)
		{
			Objetivo obActividad = new ObjetivoImpl(obj);
			obActividad.setIdObjetivo(simplePageToolDao.getObjetivoActividad(obj, nR));
			ObjetivosActividadImpl objetivoAc = new ObjetivosActividadImpl(2,ac, obActividad);
			saveCaracteristica(objetivoAc, true);
		}
	}
	/**
	 * Agrega una actividad a otra de mayor nivel.
	 * @param ac la actividad.
	 * @param nombre el nombre de la actividad.
	 * @param nR el nivel de recursividad.
	 */
	public void addActividadToActividadMayorNivel(Actividad ac, String nombre, int nR) {

//		int id = getUltimaActividad(ac.getNombre());

//		ac.setIdActividad(id);

		Actividad acPadre = simplePageToolDao.getactividad(nombre, nR);

		ActividadTieneActividad acHija = new ActividadTieneActividadImpl(acPadre, ac); 

		List<String>elist = new ArrayList<String>();

		simplePageToolDao.saveCaracteristica(acHija, elist, messageLocator.getMessage("simplepage.nowrite"), true);
	}
	/**
	 * Retorna el identificador de la &uacuteltima actividad creada.
	 * @return el identificador.
	 */
	public int getUltimaActividad()
	{
		return simplePageToolDao.getUltimaActividad();
	}
	/**
	 * Retorna el identificador de la &uacuteltima actividad creada.
	 * @param el nombre de la actividad.
	 * @return el identificador.
	 */
	public int getUltimaActividad(String nombre)
	{
		return simplePageToolDao.getUltimaActividad(nombre);
	}
	/**
	 * Retorna el identificador del &uacuteltimo recurso creado.
	 * @return el recurso.
	 */
	public Recurso getUltimoRecurso()
	{
		return simplePageToolDao.getUltimoRecurso();
	}
	/**
	 * A&ntildeade objetivos de actividad.
	 * @param ac la actividad.
	 * @param nR el nivel de recursividad.
	 */
	public void addObjetivosActividad(Actividad ac, int nR)
	{
//		int id = getUltimaActividad(ac.getNombre());

		String err = null;
//	
//		ac.setIdActividad(id);

		try {

			for (int i = 0; i <objetivosSeleccionados.length ; i++)
			{
				Objetivo obj = new ObjetivoImpl();
				obj.setIdObjetivo(simplePageToolDao.getObjetivoActividad(objetivosSeleccionados[i], nR));
				obj.setNombre(objetivosSeleccionados[i]);
				ObjetivosActividadImpl objetivoAc = new ObjetivosActividadImpl(2, ac, obj);
				System.out.println("*** Actividad: " + ac.getIdActividad());
				System.out.println("*** Obj: " + obj.getIdObjetivo());

				saveCaracteristica(objetivoAc, true);
			}

		} catch (Throwable t) {	
			// this is probably a bogus error, but find its root cause
			while (t.getCause() != null) {
				t = t.getCause();
			}
			err = t.toString();
		}
	}

	public String addActividad(Tipo type, Item item, String name, String description, int nR,int dedicacion) {
		try {	 	 			    	
			Actividad ac = appendActividad(type, item, name, description, nR, dedicacion, nivelActividades);
			boolean ret =saveActividad(ac);	
			if(ret)
				return "success";
			return "failure";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "failure";
		} finally {
			selectedActividad = null;
		}
		//}
	}

	/**
	 * Agrega las caracter&iacutesticas de una actividad.
	 * @param ac
	 */
	private void addCaracteristicasActividad(Actividad ac) {		

		getContextos();
		getHabilidades();
		getCompetencias();
		getSA();

//		int id = getUltimaActividad(ac.getNombre());

		String err = null;

//		ac.setIdActividad(id);

		List<CaracteristicaActividad> caracteristicas = new ArrayList<CaracteristicaActividad>();
		CaracteristicaActividad caracteristicaNueva = new CaracteristicaActividadImpl();

		for(String habilidad : getHabilidadesSeleccionadas())
		{
			if(!habilidad.equals(cadenaOpciones))
			{
				caracteristicaNueva = new CaracteristicaActividadImpl(ac,getCaracteristicaHabilidad(habilidad),true, false,0 );
				caracteristicas.add(caracteristicaNueva);

				try {
					simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);

				} catch (Throwable t) {	
					// this is probably a bogus error, but find its root cause
					while (t.getCause() != null) {
						t = t.getCause();
					}
					err = t.toString();
				}
			}
		}


		for(String compentencias : getCompetenciasSeleccionadas())
		{
			if(!compentencias.equals(cadenaOpciones))
			{
				caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaCompetencia(compentencias),true, false,0 );
				caracteristicas.add(caracteristicaNueva);

				try {
					simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);

				} catch (Throwable t) {	
					// this is probably a bogus error, but find its root cause
					while (t.getCause() != null) {
						t = t.getCause();
					}
					err = t.toString();
				}
			}
		}

		if(!getCaracteristicaSA(saS).getNombre().equals(cadenaOpciones))
		{
			caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaSA(saS),false, false,0 );
			caracteristicas.add(caracteristicaNueva);

			try {
				simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);

			} catch (Throwable t) {	
				// this is probably a bogus error, but find its root cause
				while (t.getCause() != null) {
					t = t.getCause();
				}
				err = t.toString();
			}
		}

		if(!getCaracteristicaContexto(contextoS).getNombre().equals(cadenaOpciones))
		{
			caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaContexto(contextoS),false, false,0 );
			caracteristicas.add(caracteristicaNueva);

			try {
				simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);

			} catch (Throwable t) {	
				// this is probably a bogus error, but find its root cause
				while (t.getCause() != null) {
					t = t.getCause();
				}
				err = t.toString();
			}
		}

		for(String habilidadPC : getHabilidadesPCSeleccionadas())
		{
			if(!habilidadPC.equals(cadenaOpciones))
			{
				caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaHabilidad(habilidadPC),false, true, 0 );
				caracteristicas.add(caracteristicaNueva);

				try {
					simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);

				} catch (Throwable t) {	
					// this is probably a bogus error, but find its root cause
					while (t.getCause() != null) {
						t = t.getCause();
					}
					err = t.toString();
				}
			}
		}

		for(String competenciaPC : getCompetenciasPCSeleccionadas())
		{
			if(!competenciaPC.equals(cadenaOpciones))
			{
				caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaCompetencia(competenciaPC),false, true,0 );
				caracteristicas.add(caracteristicaNueva);

				try {
					simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);

				} catch (Throwable t) {	
					// this is probably a bogus error, but find its root cause
					while (t.getCause() != null) {
						t = t.getCause();
					}
					err = t.toString();
				}
			}
		}

		//    	for(CaracteristicaActividad c : caracteristicas)
		//		{
		//			c.setIdActividad(ac);
		//	//		ac.getCaracteristicas().add(c);		
		//		}

		//    	String err = null;
		//    	try {
		//    		simplePageToolDao.saveCaracteristicasActividades(caracteristicas);
		//			
		//		} catch (Throwable t) {	
		//			// this is probably a bogus error, but find its root cause
		//			while (t.getCause() != null) {
		//				t = t.getCause();
		//			}
		//			err = t.toString();
		//		}


	}
	/**
	 * Obtiene una situaci&oacuten de aprendizaje.
	 * @param saBuscar
	 * @return la situaci&oacuten de aprendizaje.
	 */
	public CaracteristicaImpl getCaracteristicaSA(String saBuscar)
	{
		String nombreC = cadenaOpciones,TestC=null;
		int tipoS=0;
		Item it =null;

		for(int j = 0; j<getsA().size();j++)
		{
			if(getsA().get(j).getNombre().equals(saBuscar))
			{
				tipoS = getsA().get(j).getIdCaracteristica();
				it = getsA().get(j).getIdItem();
				nombreC = getsA().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS,it,nombreC);
	}
	/**
	 * Obtiene una personalidad.
	 * @param personalidadBuscar
	 * @return la personalidad.
	 */
	public CaracteristicaImpl getCaracteristicaPersonalidad(String personalidadBuscar)
	{
		String nombreC = cadenaOpciones,TestC=null;
		int tipoS=0;
		Item it=null;

		for(int j = 0; j<getPersonalidad().size();j++)
		{
			if(getPersonalidad().get(j).getNombre().equals(personalidadBuscar))
			{
				tipoS = getPersonalidad().get(j).getIdCaracteristica();
				it = getPersonalidad().get(j).getIdItem();
				nombreC = getPersonalidad().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS, it,nombreC);
	}

	//	public TipoImpl getTipoCambio()
	//	{
	//		this.setTipo(simplePageToolDao.getTipos());
	//		String nombreC = cadenaOpciones;
	//		int tipoS=0;
	//    	for(int j = 0; j<getTipo().size();j++)
	//    	{
	//    		if(getTipo().get(j).getNombre().equals(tipoSeleccionado))
	//    		{
	//    			tipoS = getTipo().get(j).getIdTipo();
	//    			nombreC = getTipo().get(j).getNombre();
	//    		}
	//    	}
	//    	   	
	//		return new TipoImpl(tipoS,nombreC);
	//	}
	/**
	 * Obtiene un contexto.
	 * @param contextoBuscar
	 * @return el contexto.
	 */
	public Caracteristica getCaracteristicaContexto(String contextoBuscar)
	{
		String nombreC = cadenaOpciones;
		int tipoS=0;
		Item it=null;

		for(int j = 0; j<getContexto().size();j++)
		{
			if(getContexto().get(j).getNombre().equals(contextoBuscar))
			{
				tipoS = getContexto().get(j).getIdCaracteristica();
				it = getContexto().get(j).getIdItem();
				nombreC = getContexto().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS,it,nombreC);
	}
	/**
	 * Obtiene un estilo de aprendizaje.
	 * @param estiloBuscar
	 * @return el estilo.
	 */
	public CaracteristicaImpl getCaracteristicaEstilo(String estiloBuscar)
	{
		String nombreC = cadenaOpciones,TestC=null;
		int tipoS=0;
		Item it=null;

		for(int j = 0; j<getEstilo().size();j++)
		{
			if(getEstilo().get(j).getNombre().equals(estiloBuscar))
			{
				tipoS = getEstilo().get(j).getIdCaracteristica();
				it = getEstilo().get(j).getIdItem();
				nombreC = getEstilo().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS,it,nombreC);
	}
	/**
	 * Obtiene una habilidad.
	 * @param habilidadBuscar
	 * @return la habilidad.
	 */
	public CaracteristicaImpl getCaracteristicaHabilidad(String habilidadBuscar)
	{
		String nombreC = cadenaOpciones,TestC=null;
		int tipoS=0;
		Item it=null;

		for(int j = 0; j<getHabilidad().size();j++)
		{
			if(getHabilidad().get(j).getNombre().equals(habilidadBuscar))
			{
				tipoS = getHabilidad().get(j).getIdCaracteristica();
				it = getHabilidad().get(j).getIdItem();
				nombreC = getHabilidad().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS,it,nombreC);
	}
	/**
	 * Obtiene una competencia.
	 * @param competenciaBuscar
	 * @return la competencia.
	 */
	public CaracteristicaImpl getCaracteristicaCompetencia(String competenciaBuscar)
	{
		String nombreC = cadenaOpciones,TestC=null;
		int tipoS=0;
		Item it = null;

		for(int j = 0; j<getCompetencia().size();j++)
		{
			if(getCompetencia().get(j).getNombre().equals(competenciaBuscar))
			{
				tipoS = getCompetencia().get(j).getIdCaracteristica();
				it = getCompetencia().get(j).getIdItem();
				nombreC = getCompetencia().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS,it,nombreC);
	}
	/**
	 * Retorna el valor de un permiso.
	 * @return permiso.
	 */
	public int puedeEditarDominioAshyi() {

		int permiso = simplePageToolDao.getEditarDominioAshyi();
		return permiso;
		//return getDominioAshyi();
	}
	/**
	 * Retorna los tipos de contexto.
	 * @return listNombres los tipos. 
	 */
	public String[] getTiposContexto()
	{
		this.setTipoContexto(simplePageToolDao.getTiposContexto());

		String[] listNombres = new String [getTipoContexto().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getTipoContexto().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getTipoContexto().size()+1 ;i++)		
			{				
				Item c = getTipoContexto().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los tipos de tipos.
	 * @return listNombres los tipos.
	 */
	public String[] getTipoTipos()
	{
		//		tiposTipo.add(new TipoImpl(1, "Actividad"));
		//		tiposTipo.add(new TipoImpl(2, "Caracteristica"));
		//		tiposTipo.add(new TipoImpl(3, "Contexto"));

		String[] listNombres = new String [getTiposTipo().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getTiposTipo().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getTiposTipo().size()+1 ;i++)			
			{				
				Tipo c = getTiposTipo().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los items.
	 * @return listNombres los items.
	 */
	public String[] getItemsCaracteristica()
	{
		this.setItemsCaracterisiticas(simplePageToolDao.getTiposCaracteristicas());

		String[] listNombres = new String [getItemsCaracterisiticas().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getItemsCaracterisiticas().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getItemsCaracterisiticas().size()+1 ;i++)			
			{				
				Item c = getItemsCaracterisiticas().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los tipos.
	 * @return listNombres los tipos.
	 */
	public String[] getTiposCaracteristica()
	{
		this.setTipoCaracteristicas(simplePageToolDao.getTiposCaracteristicas());

		String[] listNombres = new String [getTipoCaracteristicas().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getTipoCaracteristicas().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getTipoCaracteristicas().size()+1 ;i++)			
			{				
				Item c = getTipoCaracteristicas().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los tipos de actividad.
	 * @return listNombres los tipos.
	 */
	public String[] getTiposActividad()
	{
		this.setTipoActividad(simplePageToolDao.getTiposActividad());

		String[] listNombres = new String [getTipoActividad().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getTipoActividad().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getTipoActividad().size()+1 ;i++)		
			{				
				Tipo c = getTipoActividad().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los contextos.
	 * @return listNombres los contextos.
	 */
	public String[] getContextos()
	{
		this.setContexto(simplePageToolDao.getContextos());

		String[] listNombres = new String [getContexto().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getContexto().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getContexto().size()+1 ;i++)		
			{				
				Caracteristica c = getContexto().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna las habilidades.
	 * @return listNombres las habilidades.
	 */
	public String[] getHabilidades()
	{
		this.setHabilidad(simplePageToolDao.getHabilidades());

		String[] listNombres = new String [getHabilidad().size()+1];
		listNombres[0] = cadenaOpciones;
		if (getHabilidad().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getHabilidad().size()+1 ;i++)			
				//for(i = 0; i< getHabilidad().size() ;i++)
			{
				Caracteristica c = getHabilidad().get(i-1);
				listNombres[i]=c.getNombre();				
			}
		}
		return listNombres;
	}
	/**
	 * Retorna las competencias.
	 * @return listNombres las competencias.
	 */
	public String[] getCompetencias()
	{
		this.setCompetencia(simplePageToolDao.getCompetencias());

		String[] listNombres = new String [getCompetencia().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getCompetencia().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getCompetencia().size()+1 ;i++)		
				//for(i = 0; i< getCompetencia().size() ;i++)
			{				
				Caracteristica c = getCompetencia().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna las personalidades.
	 * @return listNombres las personalidades.
	 */
	public String[] getPersonalidades()
	{
		this.setPersonalidad(simplePageToolDao.getPersonalidades());

		String[] listNombres = new String [getPersonalidad().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getPersonalidad().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getPersonalidad().size()+1 ;i++)			
			{				
				Caracteristica c = getPersonalidad().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna las situaciones de aprendizaje.
	 * @return listNombres las situaciones de aprendizaje.
	 */
	public String[] getSA()
	{
		this.setsA(simplePageToolDao.getSA());

		String[] listNombres = new String [getsA().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getsA().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getsA().size()+1 ;i++)			
			{				
				Caracteristica c = getsA().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los estilos de aprendizaje.
	 * @return listNombres los estilos de aprendizaje.
	 */
	public String[] getEstilos()
	{
		this.setEstilo(simplePageToolDao.getEstilos());

		String[] listNombres = new String [getEstilo().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getEstilo().size() > 0) 
		{
			for(int i = 1; i< getEstilo().size()+1 ;i++)			
			{
				Caracteristica c = getEstilo().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}

	/**
	 * Agrega una caracter&iacutestica.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addCaracteristica() {

		getTiposCaracteristica();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, new ResourceLoader().getLocale());		
		df.setTimeZone(TimeService.getLocalTimeZone());
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		//if (selectedActividad == null) {
		//	return "failure";
		//} else {
		try {
			//			    LessonEntity selectedObject = actividadEntity.getEntity(selectedActividad);
			//			    if (selectedObject == null)
			//				return "failure";

			SimplePageItem i;

			//int type, String name, String goal, String description, String dI, String dF
			Item item = new ItemImpl();
			for(int j = 0; j<getTipoCaracteristicas().size();j++)
			{
				if(getTipoCaracteristicas().get(j).getNombre().equals(tipoC))
				{
					item = getTipoCaracteristicas().get(j);
				}
			}			    	

			Caracteristica carac = appendCaracteristica(item, nombreC);
			//if (selectedObject.getDueDate() != null)
			//  i.setDescription("(" + messageLocator.getMessage("simplepage.due") + " " + df.format(selectedObject.getDueDate()) + ")");
			//else
			//i.setDescription(null);
			//saveCarateristica(carac);
			//}
			return "success";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "failure";
		} finally {
			selectedActividad = null;
		}
		//}
	}

	//	public String addTipo() {
	//		getTipoTipos();
	//		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, new ResourceLoader().getLocale());		
	//		df.setTimeZone(TimeService.getLocalTimeZone());
	//		if (!itemOk(itemId))
	//		    return "permission-failed";
	//		if (!canEditPage())
	//		    return "permission-failed";
	//			try {
	//
	//			    SimplePageItem i;
	//			    
	//			    Item tipoS=null;
	//		    	for(int j = 0; j<getTiposTipo().size();j++)
	//		    	{
	//		    		if(getTiposTipo().get(j).getNombre().equals(tipoTipo))
	//		    		{
	//		    			tipoS = getTiposTipo().get(j).getIdItem();
	//		    		}
	//		    	}
	//			    
	//		    	Tipo tipo = new TipoImpl(tipoS, nombreTipo);
	//		    	tipo = appendTipo(tipo);
	//			    return "success";
	//			} catch (Exception ex) {
	//			    ex.printStackTrace();
	//			    return "failure";
	//			} finally {
	//				selectedActividad = null;
	//			}
	//	}
	/**
	 * Agrega un item.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addItem() {

		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";
		try {

			SimplePageItem i;

			Item carac = appendItem(nombreTipo, testC);
			return "success";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "failure";
		} finally {
			selectedActividad = null;
		}
	}
	/**
	 * Agrega un nuevo item.
	 * @param nombreItem
	 * @param testC
	 * @return i el item.
	 */
	private Item appendItem(String nombreItem, String testC) {
		Item i = simplePageToolDao.makeItemAshyi(nombreItem, testC);
		//long pageId, int type, String name, String goal, String description, String dI, String dF

		// defaults to a fixed width and height, appropriate for some things, but for an
		// image, leave it blank, since browser will then use the native size
		//clearImageSize(i);		
		saveCarateristica(i);
		return i;
	}
	/**
	 * Retorna los tipos de tipo.
	 * @return tiposTipo los tipos de tipo.
	 */
	public List<Tipo> getTiposTipo() {

		return tiposTipo;
	}

	//	public String editarTipo()
	//	{
	//		Tipo editarTipo = new TipoImpl();
	//		GeneralViewParameters view = new GeneralViewParameters();
	//		String respuesta = "";
	//		if(!tipoSeleccionado.equals(cadenaOpciones))
	//	    {	
	//			List<NavigationCase> togo = new ArrayList<NavigationCase>();
	//			
	//			editarTipo=getTipoCambio();			
	//	    	view.viewID = CaracteristicaProducer.VIEW_ID;
	//			view.setSendingPage(getCurrentPageId());
	//			view.setItemId(Long.valueOf(editarTipo.getIdTipo()));
	//			
	//			togo.add(new NavigationCase("successTipo", view));
	//			
	//			respuesta = "successTipo";
	//	    }
	//		
	//		return respuesta;
	//	}
	/**
	 * Retorna los tipos de cambio.
	 * @return tiposTipo los tipos de cambio.
	 */
	public Item getTipoCambio()
	{
		this.setTipo(simplePageToolDao.getItemsCaracterisiticas());
		String nombreC = cadenaOpciones;
		int tipoS=0, tipoT=0;
		for(int j = 0; j<getTipo().size();j++)
		{
			if(getTipo().get(j).getNombre().equals(tipoSeleccionado))
			{
				tipoS = getTipo().get(j).getIdItem();
				nombreC = getTipo().get(j).getNombre();
			}
		}

		return new ItemImpl(tipoS,nombreC,"");
	}
	/**
	 * Edita una habilidad.
	 * @return respuesta
	 */
	public Caracteristica editarHabilidad()
	{
		getHabilidades();
		Caracteristica respuesta = new CaracteristicaImpl();
		if(!habilidadS.equals(cadenaOpciones))
		{	
			for(int j = 0; j<getHabilidad().size();j++)
			{
				if(getHabilidad().get(j).getNombre().equals(habilidadS))
				{
					respuesta.setIdCaracteristica(getHabilidad().get(j).getIdCaracteristica());
					respuesta.setNombre(getHabilidad().get(j).getNombre());
				}
			}
		}

		return respuesta;
	}
	/**
	 * Edita una situaci&oacuten de aprendizaje.
	 * @return respuesta
	 */
	public Caracteristica editarSas()
	{
		getSA();
		Caracteristica respuesta = new CaracteristicaImpl();
		if(!saS.equals(cadenaOpciones))
		{	
			for(int j = 0; j<getsA().size();j++)
			{
				if(getsA().get(j).getNombre().equals(saS))
				{
					respuesta.setIdCaracteristica(getsA().get(j).getIdCaracteristica());
					respuesta.setNombre(getsA().get(j).getNombre());
				}
			}
		}

		return respuesta;
	}
	/**
	 * Edita una competencia.
	 * @return respuesta
	 */
	public Caracteristica editarCompetencia()
	{
		getCompetencias();
		Caracteristica respuesta = new CaracteristicaImpl();
		if(!competenciaS.equals(cadenaOpciones))
		{	
			for(int j = 0; j<getCompetencia().size();j++)
			{
				if(getCompetencia().get(j).getNombre().equals(competenciaS))
				{
					respuesta.setIdCaracteristica(getCompetencia().get(j).getIdCaracteristica());
					respuesta.setNombre(getCompetencia().get(j).getNombre());
				}
			}
		}

		return respuesta;
	}
	/**
	 * Edita una personalidad.
	 * @return respuesta
	 */
	public Caracteristica editarPersonalidad()
	{
		getPersonalidades();
		Caracteristica respuesta = new CaracteristicaImpl();
		if(!personalidadS.equals(cadenaOpciones))
		{	
			for(int j = 0; j<getPersonalidad().size();j++)
			{
				if(getPersonalidad().get(j).getNombre().equals(personalidadS))
				{
					respuesta.setIdCaracteristica(getPersonalidad().get(j).getIdCaracteristica());
					respuesta.setNombre(getPersonalidad().get(j).getNombre());
				}
			}
		}

		return respuesta;
	}
	/**
	 * Edita un estilo.
	 * @return respuesta
	 */
	public Caracteristica editarEstilo()
	{
		getEstilos();
		Caracteristica respuesta = new CaracteristicaImpl();
		if(!estiloS.equals(cadenaOpciones))
		{	
			for(int j = 0; j<getEstilo().size();j++)
			{
				if(getEstilo().get(j).getNombre().equals(estiloS))
				{
					respuesta.setIdCaracteristica(getEstilo().get(j).getIdCaracteristica());
					respuesta.setNombre(getEstilo().get(j).getNombre());
				}
			}
		}

		return respuesta;
	}
	/**
	 * Edita un contexto.
	 * @return respuesta
	 */
	public Caracteristica editarContexto()
	{
		getContextos();
		Caracteristica respuesta = new CaracteristicaImpl();
		if(!contextoS.equals(cadenaOpciones))
		{	
			for(int j = 0; j<getContexto().size();j++)
			{
				if(getContexto().get(j).getNombre().equals(contextoS))
				{
					respuesta.setIdCaracteristica(getContexto().get(j).getIdCaracteristica());
					respuesta.setNombre(getContexto().get(j).getNombre());
				}
			}
		}

		return respuesta;
	}
	/**
	 * Elimina un dominio.
	 * @return resultado de la operaci&oacuten.
	 */
	public String eliminarDominio() {

		getContextos();
		getHabilidades();
		getCompetencias();

		getHabilidades();
		getCompetencias();
		getPersonalidades();
		getSA();
		getEstilos();

		getTiposCompletos();

		try {

			SimplePageItem i;

			Caracteristica editar = null;
			Caracteristica editarCx = null;
			//Tipo editarTipo = null;

			if(!habilidadesSeleccionadas[0].equals(cadenaOpciones))
			{
				editar=getCaracteristicaHabilidad(habilidadesSeleccionadas[0]);			    	
			}
			if(!estilosSeleccionados[0].equals(cadenaOpciones))
				editar=getCaracteristicaEstilo(estilosSeleccionados[0]);
			if(!saSeleccionadas[0].equals(cadenaOpciones))
				editar=getCaracteristicaSA(saSeleccionadas[0]);
			if(!competenciasSeleccionadas[0].equals(cadenaOpciones))
				editar=getCaracteristicaCompetencia(competenciasSeleccionadas[0]);
			if(!personalidadesSeleccionadas[0].equals(cadenaOpciones))
				editar=getCaracteristicaPersonalidad(personalidadesSeleccionadas[0]);
			if(!contextosSeleccionados[0].equals(cadenaOpciones))
			{
				editarCx=getCaracteristicaContexto(contextosSeleccionados[0]);
				eliminarDominio(editarCx);
			}
			//			    else if(!tipoSeleccionado.equals(cadenaOpciones))
			//			    {
			//			    	editarTipo=getTipoCambio();
			//			    	eliminarDominio(editarTipo);
			//			    }
			else			    
			{
				eliminarDominio(editar);
			}

			return "success";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "failure";
		} finally {
			selectedActividad = null;
		}
	}
	/**
	 * Elimina un dominio.
	 * @param editar
	 */
	private void eliminarDominio(Object editar) {
		simplePageToolDao.eliminar(editar);

	}
	/**
	 * Edita una caracter&iacutestica.
	 */
	public void editarCaracteristica() {
		List<Caracteristica> list = simplePageToolDao.getCaracteristica(Integer.valueOf(idC));
		Caracteristica c = list.get(0);

		c.setNombre(nombreC);

		getTiposCaracteristica();
		Item item = new ItemImpl();
		for(int j = 0; j<getTipoCaracteristicas().size();j++)
		{
			if(getTipoCaracteristicas().get(j).getNombre().equals(tipoC))
			{
				item = getTipoCaracteristicas().get(j);
			}
		}

		c.setIdItem(item);

		update(c);
	}
	/**
	 * Agrega un control.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addControl() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, new ResourceLoader().getLocale());		
		df.setTimeZone(TimeService.getLocalTimeZone());
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		try {

			SimplePageItem i;

			appendControl(getCurrentUserId(), true);

			return "success";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "failure";
		} finally {
			selectedActividad = null;
		}
		//}
	}

	/**
	 * Agrega un control.
	 * @param currentUserId2
	 * @param cambio
	 */
	private void appendControl(String currentUserId2, boolean cambio) {

		Control i = simplePageToolDao.makeControl(currentUserId2, cambio);
		//long pageId, int type, String name, String goal, String description, String dI, String dF

		// defaults to a fixed width and height, appropriate for some things, but for an
		// image, leave it blank, since browser will then use the native size
		//clearImageSize(i);

		saveControl(i);

	}
	/**
	 * Guarda un control.
	 * @param i
	 * @param requiresEditPermission
	 * @return true si se pudo, false si no.
	 */
	public boolean saveControl(Object i, boolean requiresEditPermission) {       
		String err = null;
		List<String>elist = new ArrayList<String>();

		try {
			simplePageToolDao.saveCaracteristica(i,  elist, messageLocator.getMessage("simplepage.nowrite"), requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed") + err);
			return false;
		}

		return true;
	}

	/**
	 * Retorna los tipos completos.
	 * @return listNombres los tipos.
	 */
	public String[] getTiposCompletos() {

		this.setTipo(simplePageToolDao.getItemsCaracterisiticas());

		String[] listNombres = new String [getTipo().size()+1];
		listNombres[0] = cadenaOpciones;

		if (getTipo().size() > 0) 
		{
			int i = 0;
			for(i = 1; i< getTipo().size()+1 ;i++)			
			{
				Item c = getTipo().get(i-1);
				listNombres[i]=c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Redirecciona.
	 */
	public void redireccionar() {

		// UIInternalLink.make(tofillAct, ResourcePickerProducer.VIEW_ID,
		// messageLocator.getMessage("actividad.redirigir"), params);

	}

	/**
	 * Registra un curso.
	 * @return resultado de la operaci&oacuten.
	 */
	public String registrarCurso()
	{
		String respuesta = "El curso no se ha registrado";

		respuesta = simplePageToolDao.registrarCurso(getCurrentSite().getTitle());

		//		appendItem("Recursiva");
		//		appendItem("AtÃ³mica");

		return respuesta;
	}
	/**
	 * Agrega un recurso de curso.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addRecursoCurso()
	{
		Actividad ac = simplePageToolDao.getactividad(getCurrentSite().getTitle(), 1);		
		addRecursoActividad(ac,1);	

		return "success";
	}
	/**
	 * Agrega un objetivo de curso.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addObjetivoCurso()
	{
		Objetivo obj = new ObjetivoImpl();
		try {
			int id = 0;
			obj = simplePageToolDao.getObjetivo(objetivoCurso);
			if(obj.getNombre().equals("Does not exist"))
			{
				//almacenar objetivo
				obj.setNombre(objetivoCurso);
				saveCaracteristica(obj,true);
				//relacionar objetivo y actividad
				id = simplePageToolDao.getUltimoObjetivo(obj.getNombre());
				obj.setIdObjetivo(id);
			}
		} catch (Throwable t) {	
			// this is probably a bogus error, but find its root cause
			while (t.getCause() != null) {
				t = t.getCause();
			}
		}
		Actividad ac = simplePageToolDao.getactividad(getCurrentSite().getTitle(), 1);		
		addObjetivoActividad(ac, obj);	

		return "success";
	}


	/**
	 * Agrega un objetivo de actividad.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addObjetivoActividad()
	{
		Objetivo obj = simplePageToolDao.getObjetivo(objetivoCurso);
		Actividad ac = simplePageToolDao.getactividad(getCurrentPageItem(getItemId()).getName(), 3);		
		addObjetivoActividad(ac, obj);	

		return "success";
	}
	/**
	 * Agrega un objetivo de actividad.
	 * @param ac
	 * @param obj
	 */
	private void addObjetivoActividad(Actividad ac,Objetivo obj) {	

		String err = null;			
		ObjetivosActividad objetivoNueva = new ObjetivosActividadImpl(1,ac, obj);
		saveCaracteristica(objetivoNueva,true);


	}
	/**
	 * Agrega un recurso de actividad.
	 * @param ac
	 * @param tipo
	 */
	private void addRecursoActividad(Actividad ac, int tipo) {	

		Recurso recurso = new RecursoImpl("Does not exist","No aplica","");
		try {

			if(tipo == 1)//curso
			{
				if(tipoTipoAcceso.equals("No aplica"))//recursos que no necesiten especificar lincencia
					tipoTipoAcceso = "";
				recurso = new RecursoImpl(recursoCurso, tipoNaturaleza, tipoRecurso, tipoTipoAcceso);				
				//almacenar recurso nuevo del curso
				saveCaracteristica(recurso,true);
				recurso = simplePageToolDao.getUltimoRecurso();
			}
			else
			{				
				//buscar recurso
				if(!recursoA.equals(cadenaOpciones))
					recurso = simplePageToolDao.getRecurso(recursoA);
			}

			if(!recurso.getNombre().equals("Does not exist"))
			{
				RecursosActividad recursoNuevo = new RecursosActividadImpl(ac, recurso);
				saveCaracteristica(recursoNuevo,true);

				getContextos();
				CaracteristicaRecurso cxtRecurso = new CaracteristicasRecursoImpl(recurso, getCaracteristicaContexto(contextoS));
				saveCaracteristica(cxtRecurso,true);
			}			

		} catch (Throwable t) {	
			// this is probably a bogus error, but find its root cause
			while (t.getCause() != null) {
				t = t.getCause();
			}
		}
	}
	/**
	 * Retorna los objetivos de una actividad.
	 * @param nivelRecursividad nivel de recursividad.
	 * @param nombre nombre de actividad.
	 * @return los objetivos.
	 */
	public String[] getObjetivosActividad(int nivelRecursividad, String nombre)
	{
		this.setObjetivos(simplePageToolDao.getObjetivosActividad(nivelRecursividad, nombre));

		return this.getObjetivos();
	}
	/**
	 * Guarda el grafo generado por el usuario en el flowchartproducer.
	 * @return resultado de la operaci&oacuten.
	 */
	public String saveFlowChart()
	{
		String jsonTxt = newFlowChart;
		JSONParser parser=new JSONParser();
		JSONObject jOb = new JSONObject();
		try {
			jOb = (JSONObject) parser.parse(jsonTxt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray conn = (JSONArray) jOb.get( "connections" );
		System.out.println("Number of connections "+conn.size());
		for(int i=0;i<conn.size();i++)
		{
			System.out.println("=====Connection #"+(i+1)+"=====");
			JSONObject p = (JSONObject) conn.get(i);
			String source= (String) p.get("source");
			System.out.println("source: "+source);
			String stype= (String) p.get("sourceType");
			System.out.println("source type: "+stype);
			String target= (String) p.get("target");
			System.out.println("target: "+target);
			String ttype= (String) p.get("targetType");
			System.out.println("target type: "+ttype);
			System.out.println("");

			int idTar=Integer.parseInt(target.substring(10));
			System.out.println("target id: "+idTar);
			System.out.println("antes de llamar al simplePageToolDao");
			//String a =addActividad(1, "act"+idTar, "goal", "Dest", "2014-05-28T05:40", "2014-05-29T05:40");
		}   
		return "done";
	}
	/**
	 * Carga el grafo existente en la base de datos de Ashyi para la unidad.
	 */
	public void loadExistingFlowChart()
	{
		String nombre=getCurrentPageItem(getCurrentPageId()).getName();
		Actividad acPadre = simplePageToolDao.getactividad(nombre, 3);
		int id=acPadre.getIdActividad();
		//		int id=1;
		List<DependenciaActividad> dependencias=simplePageToolDao.getDependenciasActividades();
		List<String> actividades=simplePageToolDao.getActividadTieneActividadString(id);
		this.existingFlowChart="{\"user\":\"s\",\"connections\":[";
		int size=dependencias.size();
		for (int i = 0; i < size; i++) {
			DependenciaActividad dep =dependencias.get(i);
			if(actividades.contains(""+dep.getIdActividad())&&actividades.contains(""+dep.getId_Actividad_Dependiente()))
			{
				existingFlowChart+="{";
				existingFlowChart+="\"source\":\"";
				existingFlowChart+=""+dep.getId_Actividad_Dependiente();
				existingFlowChart+="\",\"target\":\"";
				existingFlowChart+=""+dep.getIdActividad();
				existingFlowChart+="\"}";
				if(i<(size-1))
					existingFlowChart+=",";
			}
		}
		existingFlowChart+="]}";
	}
	/**
	 * Carga las actividades existentes en la base de datos de Ashyi para la unidad.
	 */
	public void loadExistingActivities()
	{
		String nombre=getCurrentPageItem(getCurrentPageId()).getName();
		Actividad acPadre = simplePageToolDao.getactividad(nombre, 3);
		int id=acPadre.getIdActividad();
		//		int id=1;

		List<String> actividades=simplePageToolDao.getActividadTieneActividadString(id);
		this.existingActivities="{\"activities\":[";
		int size=actividades.size();
		for (int i = 0; i < size; i++) {
			String aux =simplePageToolDao.getEstiloSiguienteNivelActTieneAct(id, Integer.parseInt(actividades.get(i))).get(0);
			Actividad act=simplePageToolDao.getActividad(Integer.parseInt(actividades.get(i)));
			existingActivities+="{";
			existingActivities+="\"id\":\"";
			existingActivities+=""+act.getIdActividad();
			existingActivities+="\",\"name\":\"";
			existingActivities+=""+act.getNombre();
			existingActivities+="\",\"type\":\"";
			existingActivities+="template-activityNode"+((i%10)+1);
			existingActivities+="\",";
			existingActivities+=aux;
			existingActivities+="}";
			if(i<(size-1))
			{
				existingActivities+=",";
			}
			else
			{
				existingActivities+="]}";
			}
		}
	}
	/**
	 * Guarda en la base de datos de Ashyi las conexiones del grafo generado en el flowchartproducer.
	 */
	public void saveConnection()
	{

		String jsonTxt = lastConnection;
		JSONParser parser=new JSONParser();
		JSONObject jOb = new JSONObject();
		try {
			jOb = (JSONObject) parser.parse(jsonTxt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray conn = (JSONArray) jOb.get( "connections" );
		for(int i=0;i<conn.size();i++)
		{

			JSONObject p = (JSONObject) conn.get(i);
			String source= (String) p.get("source");
			String target= (String) p.get("target");

			String idTar=target;
			String idSour=source;
			if(p.get("type")=="normal")
			{
				boolean result=updateDependenciaActividad(idTar,idSour);
				if(result)
					System.out.println("Dependency updated");
				else
					System.out.println("Error updating dependency");
			}
			else
			{
				//detach and connection
				boolean result=updateDependenciaActividad(idTar,idSour);
				if(result)
					System.out.println("Dependency updated");
				else
					System.out.println("Error updating dependency");
			}
		}

		//modificacion

		//enviar modificaciones a agente editor
		BeanASHYI bean = getBeanBesa();
		String myAlias = bean.getAgent("Editor-"+getCurrentUserNameID());
		//Graph g = new Graph();
		//bean.executeAction(myAlias, "", "EditG", g);

	}
	/**
	 * Registra un tema.
	 * @param nombre
	 */
	public void registrarTema(String nombre) {

		nomberA = nombre;
		tipoUnidad = 1;
		addActividad();
	}
	/**
	 * Retorna los tipos por nombre.
	 * @param nombre
	 * @return caracString los tipos.
	 */
	public String[] getCaracterisicasTipo(String nombre)
	{
		List<CaracteristicasTipo> listCT = simplePageToolDao.getCaracteristicasTipo(nombre);

		String[] caracString = new String[listCT.get(listCT.size()-1).getLinea()];

		int linea = 1;//primera linea de caracteristicas de tipo
		int i = 0, j=0;

		for(i = 0;i<listCT.size();i++)
		{
			caracString[i] = "";
		}

		for(CaracteristicasTipo ct:listCT)
		{
			if(linea < ct.getLinea())
			{
				j++;//cambio en vector				
			}				
			caracString[j] = caracString[i] + ", "+ ct.getIdCaracteristica().getNombre();
		}

		return caracString;
	}
	/**
	 * Retorna los tipos por nombre.
	 * @param nombre
	 * @return caracString los tipos.
	 */
	public String[] getHabilidadesTipo(String nombre)
	{
		List<CaracteristicasTipo> listCT = simplePageToolDao.getCaracteristicasTipo(nombre);

		String[] caracString = new String[listCT.get(listCT.size()-1).getLinea()];

		int i = 0, j=0;

		for(i = 0;i<listCT.size();i++)
		{
			if(listCT.get(i).getIdCaracteristica().getIdItem().getNombre() == "Habilidad")// habilidad
			{
				caracString[j] = listCT.get(i).getIdCaracteristica().getNombre();		
			}	
		}

		return caracString;
	}
	/**
	 * Retorna los tipos por nombre.
	 * @param nombre
	 * @return caracString los tipos.
	 */
	public String[] getCompetenciasTipo(String nombre)
	{
		List<CaracteristicasTipo> listCT = simplePageToolDao.getCaracteristicasTipo(nombre);

		String[] caracString = new String[listCT.get(listCT.size()-1).getLinea()];

		int i = 0, j=0;

		for(i = 0;i<listCT.size();i++)
		{
			if(listCT.get(i).getIdCaracteristica().getIdItem().getNombre() == "Competencia")// habilidad
			{
				caracString[j] = listCT.get(i).getIdCaracteristica().getNombre();		
			}	
		}

		return caracString;
	}	
	/**
	 * Actualiza una actividad.
	 */
	public void updateActivity()
	{
		String jsonTxt = activitiesStyle;
		JSONParser parser=new JSONParser();
		JSONObject jOb = new JSONObject();
		try {
			jOb = (JSONObject) parser.parse(jsonTxt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray act = (JSONArray) jOb.get( "activities" );
		for(int i=0;i<act.size();i++)
		{
			JSONObject p = (JSONObject) act.get(i);
			String idSiguienteNivel= (String) p.get("id");
			String top= (String) p.get("top");
			String left= (String) p.get("left");
			String estiloSiguienteNivel="";
			estiloSiguienteNivel="\"top\":\""+top+"\",\"left\":\""+left+"\"";
			boolean result=updateActividadTieneActividad(idSiguienteNivel, estiloSiguienteNivel, true);
			if(result)
				System.out.println("ATA updated");
			else
				System.out.println("Error updating ATA");
		}

		//modificacion
	}
	/**
	 * Retorna las opciones de actividadInicialFinal.
	 * @return listNombres
	 */
	public String[] getOpcionesIniciaFinal() {
		String[] listNombres = new String [3];// inicial, final, intermedia
		listNombres[0] = "Actividad Inicial";
		listNombres[1] = "Actividad Final";
		listNombres[2] = "Actividad Intermedia";

		return listNombres;
	}

	/**
	 * Carga el grafo completo existente en la base de datos de Ashyi.
	 */
	public void loadExistingFlowChartComplete()
	{
		HashMap<Integer, ArrayList<Integer[]> > andCon=new HashMap<Integer, ArrayList<Integer[]> >();
		String nombre=getCurrentPageItem(getCurrentPageId()).getName();
		Actividad acPadre = simplePageToolDao.getactividad(nombre, 3);
		int id=acPadre.getIdActividad();
		List<DependenciaActividad> dependencias=simplePageToolDao.getDependenciasActividades();
		List<String> actividades=simplePageToolDao.getActividadTieneActividadString(id);
		int size=dependencias.size();
		StringBuilder sbComplete=new StringBuilder("{\"user\":\"p\",");
		StringBuilder sb=new StringBuilder("\"connections\":[");
		StringBuilder sbAnd=new StringBuilder("\"andConnections\":[");

		for (int i = 0; i < size; i++) {
			DependenciaActividad dep =dependencias.get(i);
			Actividad depend=simplePageToolDao.getActividades(Integer.parseInt(""+dep.getId_Actividad_Dependiente())).get(0);
			Actividad act=simplePageToolDao.getActividades(Integer.parseInt(""+dep.getIdActividad())).get(0);
			if(actividades.contains(""+act.getIdActividad())&&actividades.contains(""+depend.getIdActividad()))
			{
				int tipo=dep.getTipoConexion();
				if(tipo==0)
				{
					sb.append("{");
					sb.append("\"source\":\"");
					sb.append(""+dep.getId_Actividad_Dependiente());
					sb.append("\",\"target\":\"");
					sb.append(""+dep.getIdActividad());
					sb.append("\"}");
					sb.append(",");
				}
				else if(tipo==1)
				{
					int idCon=dep.getIdConexion();
					ArrayList<Integer[]> temp;
					if(andCon.containsKey(idCon))
					{
						temp=andCon.get(idCon);
					}
					else
					{
						temp=new ArrayList<Integer[]>();
					}
					Integer[] ar=new Integer[2];
					ar[0]=depend.getIdActividad();
					ar[1]=act.getIdActividad();
					temp.add(ar);
					andCon.put(idCon, temp);
				}

			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]}");
		Iterator<Integer> it=andCon.keySet().iterator();
		while(it.hasNext())
		{
			int key=it.next();
			ArrayList<Integer[]>temp=andCon.get(key);
			sbAnd.append("{");
			sbAnd.append("\"source\":\"");
			sbAnd.append(""+temp.get(0)[0]);
			sbAnd.append("\",\"targets\":[");
			for (int i = 0; i < temp.size(); i++) {
				sbAnd.append("{");
				sbAnd.append("\"id\":\"");
				sbAnd.append(""+temp.get(i)[1]);
				sbAnd.append("\"}");
				sbAnd.append(",");
			}
			sbAnd.deleteCharAt(sbAnd.length()-1);
			sbAnd.append("]},");
		}
		sbAnd.deleteCharAt(sbAnd.length()-1);
		sbAnd.append("],");
		sbComplete.append(sbAnd.toString());
		sbComplete.append(sb.toString());
		this.existingFlowChart=sbComplete.toString();
		System.out.println(existingFlowChart);
	}
	/**
	 * Retorna los recursos de una actividad.
	 * @param nivelRecursividad
	 * @param nombre
	 * @return recursos los recursos.
	 */
	public String[] getRecursosActividad(int nivelRecursividad, String nombre)
	{
		List<RecursosActividad> r = simplePageToolDao.getRecursosActividad(nivelRecursividad, nombre);		

		String[] recursos = new String[r.size()+1];

		recursos[0] = cadenaOpciones;

		for(int i = 1; i<recursos.length; i++)
			recursos[i]=r.get(i-1).getIdRecurso().getNombre();

		return recursos;
	}
	/**
	 * Agrega objetivos de actividad.
	 * @return resultado de la operacion.
	 */
	public String addObjetivoTema()
	{
		Objetivo obj = simplePageToolDao.getObjetivo(objetivoCurso);
		Actividad ac = simplePageToolDao.getactividad(getCurrentPageItem(getItemId()).getName(), 2);
		addObjetivoActividad(ac, obj);	

		return "success";
	}
	/**
	 * Retorna las actividades de una unidad.
	 * @param unidad
	 * @return actividadesUnidad las actividades.
	 */
	public List<Actividad>  getActividadesEnUnidad(String unidad) {
		int idUnidad = 0;
		Actividad a =simplePageToolDao.getactividad(unidad, 3);
		List<Actividad> actividadesUnidad = new ArrayList<Actividad>();
		if(!a.getNombre().equals("Does not exist"))
		{
			idUnidad = a.getIdActividad();
			List<SimplePageItem>items = getItemsOnPage(currentPage.getPageId());
			List<ActividadTieneActividad> actividades = simplePageToolDao.getActividadTieneActividades(idUnidad);			

			//if(items.size() == actividades.size())
			//{
			for(SimplePageItem item : items)
			{
				for(ActividadTieneActividad actvidad : actividades)
				{					
					Actividad aAgregar = simplePageToolDao.getActividadRecurso(actvidad.getIdActividadSiguienteNivel().getIdActividad(), String.valueOf(item.getId()));
					if(!aAgregar.getNombre().equals("Does not exist"))
					{
						actividadesUnidad.add(aAgregar);
					}
				}
			}
		}

		return actividadesUnidad;

	}
	/**
	 * Actualiza una actividad.
	 */
	public void updateActividad() {

		getTiposActividad();

		try {

			//buscar el item de recursiva
			List<String>elist = new ArrayList<String>();
			Item item = simplePageToolDao.getItemAshyi("Actividad Atomica");
			Tipo tipoS=null;
			for(int j = 0; j<tipoActividad.size();j++)
			{
				if(tipoActividad.get(j).getNombre().equals(tipoA) && (!tipoA.equals(cadenaOpciones)))
				{
					tipoS = tipoActividad.get(j);
				}
			}	

			Actividad acAnterior = simplePageToolDao.getActividad(Integer.valueOf(idC));

			acAnterior.setIdTipo(tipoS);
			acAnterior.setIdItem(item);
			acAnterior.setNombre(nomberA);
			acAnterior.setDescripcion(descripcionA);
			acAnterior.setDedicacion(Integer.valueOf(dedicacionActividad));

			/*if(opcionIniciaFinal.equals("Actividad Inicial"))
			{
				acAnterior.setEs_inicial(true);
				acAnterior.setEs_final(false);
			}
			else if(opcionIniciaFinal.equals("Actividad Final"))
			{
				acAnterior.setEs_inicial(false);
				acAnterior.setEs_final(true);
			}
			else if(opcionIniciaFinal.equals("Actividad Intermedia"))
			{
				acAnterior.setEs_inicial(false);
				acAnterior.setEs_final(false);
			}*/

			update(acAnterior);
			//Agregar caracteristicas a actividad

			acAnterior.setCaracteristicas(simplePageToolDao.getCaracteristicasActividad(Integer.valueOf(idC)));
			////			List<CaracteristicaActividad> lC = simplePageToolDao.getCaracteristicasActividad(idActividad);
			acAnterior.setObjetivo(simplePageToolDao.getObjetivosActividad(Integer.valueOf(idC)));
			////			List<ObjetivosActividad> lO = simplePageToolDao.getObjetivosActividad(idActividad);
			acAnterior.setRecursos(simplePageToolDao.getRecursosActividad(Integer.valueOf(idC)));
			//actualizar caracteristicas
			upateCaracteristicasActividad(acAnterior);
			//Actualizar objetivos
			upateObjetivosActividad(acAnterior,3);	
			//actualizar recursos
			upateRecursoActividad(acAnterior,2);


		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}		
	/**
	 * Actualiza las caracter&iacutesticas de una actividad.
	 * @param ac la actividad.
	 */
	public void upateCaracteristicasActividad(Actividad ac) {		

		getContextos();
		getHabilidades();
		getCompetencias();
		getSA();

		String err = "";

		int nHPre = 0, nHPost = 0, nCPre = 0, nCPost = 0, nCxt = 0, nSas = 0;

		CaracteristicaActividad caracteristicaNueva = new CaracteristicaActividadImpl();
		List<CaracteristicaActividad> c = new ArrayList<CaracteristicaActividad>();

		boolean caracteristicaEliminar = false;
		for(int i = 0; i < ac.getCaracteristicas().size() ; i++)
		{
			caracteristicaEliminar = false;
			CaracteristicaActividad cA = ac.getCaracteristicas().get(i);
			if(cA .getIdCaracteristica().getIdItem().getIdItem() == 5)
			{
				if(cA.isPrecondicion())
				{
					nHPre++;
					for(String habilidad : getHabilidadesSeleccionadas())
					{
						if(cA.getIdCaracteristica().getNombre().equals(habilidad))
						{
							caracteristicaEliminar = true;
							break;
						}
					}
				}

				if(cA.isPostcondicion())
				{
					nHPost++;
					for(String habilidad : getHabilidadesPCSeleccionadas())
					{
						if(cA.getIdCaracteristica().getNombre().equals(habilidad))
						{
							caracteristicaEliminar = true;
							break;
						}
					}
				}
			}

			if(cA.getIdCaracteristica().getIdItem().getIdItem() == 6)
			{
				if(cA.isPrecondicion())
				{
					nCPre++;
					for(String compentencias : getCompetenciasSeleccionadas())
					{
						if(cA.getIdCaracteristica().getNombre().equals(compentencias))
						{
							caracteristicaEliminar = true;
							break;
						}
					}	
				}
				if(cA.isPostcondicion())
				{
					nCPost++;				
					for(String compentencias : getCompetenciasPCSeleccionadas())
					{
						if(cA.getIdCaracteristica().getNombre().equals(compentencias))
						{
							caracteristicaEliminar = true;
							break;
						}
					}	
				}
			}

			if(cA.getIdCaracteristica().getIdItem().getIdItem() == 8)
			{
				nCxt++;
				if(cA.getIdCaracteristica().getNombre().equals(contextoS))
				{
					caracteristicaEliminar = true;
				}			
			}

			if(cA.getIdCaracteristica().getIdItem().getIdItem() == 9)
			{
				nSas++;
				if(cA.getIdCaracteristica().getNombre().equals(saS))
				{
					caracteristicaEliminar = true;
				}			
			}

			if(!caracteristicaEliminar)
			{
				ac.getCaracteristicas().remove(cA);
				simplePageToolDao.deleteObject(cA);
			}
		}

		for(String habilidad : getHabilidadesSeleccionadas())
		{
			if(!habilidad.equals(cadenaOpciones))
			{
				boolean estar = false;
				for(CaracteristicaActividad cA: ac.getCaracteristicas())
				{
					if(cA.getIdCaracteristica().getIdItem().getIdItem() == 5)
					{
						if(cA.isPrecondicion())
						{
							if(habilidad.equals(cA.getIdCaracteristica().getNombre()))
							{
								estar = true;
								break;
							}
						}
					}
				}

				if(!estar || nHPre == 0)
				{
					caracteristicaNueva = new CaracteristicaActividadImpl(ac,getCaracteristicaHabilidad(habilidad),true, false,0 );
					c.add(caracteristicaNueva);
					simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);
				}				

			}

		}


		for(String compentencias : getCompetenciasSeleccionadas())
		{
			if(!compentencias.equals(cadenaOpciones))
			{				
				boolean estar = false;
				for(CaracteristicaActividad cA: ac.getCaracteristicas())
				{
					if(cA.getIdCaracteristica().getIdItem().getIdItem() == 6)
					{
						if(cA.isPrecondicion())
						{
							if(compentencias.equals(cA.getIdCaracteristica().getNombre()))
							{
								estar = true;
								break;
							}
						}
					}
				}

				if(!estar || nCPre == 0)
				{
					caracteristicaNueva = new CaracteristicaActividadImpl(ac,getCaracteristicaCompetencia(compentencias),true, false,0 );
					c.add(caracteristicaNueva);
					simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);
				}
			}
		}

		if(!getCaracteristicaSA(saS).getNombre().equals(cadenaOpciones))
		{			
			boolean estar = false;
			for(CaracteristicaActividad cA: ac.getCaracteristicas())
			{
				if(saS.equals(cA.getIdCaracteristica().getNombre()))
				{
					estar = true;
					break;
				}
			}

			if(!estar || nSas == 0)
			{
				caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaSA(saS),false, false,0 );
				c.add(caracteristicaNueva);
				simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);
			}
		}

		if(!getCaracteristicaContexto(contextoS).getNombre().equals(cadenaOpciones))
		{
			boolean estar = false;
			for(CaracteristicaActividad cA: ac.getCaracteristicas())
			{
				if(contextoS.equals(cA.getIdCaracteristica().getNombre()))
				{
					estar = true;
					break;
				}
			}

			if(!estar || nCxt == 0)
			{
				caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaContexto(contextoS),false, false,0 );
				c.add(caracteristicaNueva);
				simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);
			}
		}

		for(String habilidadPC : getHabilidadesPCSeleccionadas())
		{
			if(!habilidadPC.equals(cadenaOpciones))
			{				
				boolean estar = false;
				for(CaracteristicaActividad cA: ac.getCaracteristicas())
				{
					if(cA.getIdCaracteristica().getIdItem().getIdItem() == 5)
					{
						if(cA.isPostcondicion())
						{
							if(habilidadPC.equals(cA.getIdCaracteristica().getNombre()))
							{
								estar = true;
								break;
							}
						}
					}
				}

				if(!estar || nHPost == 0)
				{
					caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaHabilidad(habilidadPC),false, true, 0 );					
					c.add(caracteristicaNueva);
					simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);
				}
			}
		}

		for(String competenciaPC : getCompetenciasPCSeleccionadas())
		{
			if(!competenciaPC.equals(cadenaOpciones))
			{				
				boolean estar = false;
				for(CaracteristicaActividad cA: ac.getCaracteristicas())
				{
					if(cA.getIdCaracteristica().getIdItem().getIdItem() == 6)
					{
						if(cA.isPostcondicion())
						{
							if(competenciaPC.equals(cA.getIdCaracteristica().getNombre()))
							{
								estar = true;
								break;
							}
						}
					}
				}

				if(!estar || nCPost == 0)
				{
					caracteristicaNueva = new CaracteristicaActividadImpl(ac, getCaracteristicaCompetencia(competenciaPC),false, true,0 );
					c.add(caracteristicaNueva);
					simplePageToolDao.saveCaracteristicasActividades(caracteristicaNueva);
				}
			}
		}
	}
	/**
	 * Elimina una actividad.
	 * @return resultado de la operaci&oacuten.
	 */
	public String deleteActividad()
	{
		Actividad a = simplePageToolDao.getActividad(Integer.valueOf(idC));

		List<CaracteristicaActividad> lC = simplePageToolDao.getCaracteristicasActividad(Integer.valueOf(idC));

		for(int i = 0 ; i<lC.size() ; i++)
		{				
			lC.get(i).setIdActividad(a);		

			simplePageToolDao.deleteObject(lC.get(i));
			simplePageToolDao.flush();
		}
		//a.getCaracteristicas().clear();
		//update(a);	

		List<ObjetivosActividad> lO = simplePageToolDao.getObjetivosActividad(Integer.valueOf(idC));
		//a.setObjetivo(lO);
		for(int i = 0 ; i<lO.size() ; i++)
		{
			lO.get(i).setIdActividad(a);
			simplePageToolDao.deleteObject(lO.get(i));
			simplePageToolDao.flush();
		}
		//a.getObjetivo().clear();
		//update(a);	

		List<RecursosActividad> lR = simplePageToolDao.getRecursosActividad(Integer.valueOf(idC));
		//a.setRecursos(lR);
		for(int i = 0 ; i<lR.size() ; i++)
		{
			//Si es el recurso de sakai
			if(!lR.get(i).getIdRecurso().getIdItemSakai().isEmpty())
			{				
				List<SimplePageItem> itemList = (List<SimplePageItem>) getItemsOnPage(getCurrentPage().getPageId());
				for(int j = 0;j<itemList.size();j++)
				{
					if(itemList.get(j).getId() == Long.valueOf(lR.get(i).getIdRecurso().getIdItemSakai()))
					{
						//Eliminar el item de la lista
						itemId = itemList.get(j).getId();
						deleteItem();
					}
				}
			}
			lR.get(i).setIdActividad(a);
			simplePageToolDao.deleteObject(lR.get(i));
			simplePageToolDao.flush();
		}
		//a.getRecursos().clear();
		//update(a);
		Actividad unidad = simplePageToolDao.getActividadAltoNivel(a.getIdActividad());
		ActividadTieneActividad aTa = new ActividadTieneActividadImpl(unidad,a);
		simplePageToolDao.deleteObject(aTa);
		simplePageToolDao.flush();
		//eliminar actividad
		simplePageToolDao.deleteActividad(a);
		simplePageToolDao.flush();

		return "success";
	}
	/**
	 * Actualiza los objetivos de una actividad.
	 * @param ac la actividad.
	 * @param nR nivel de recursividad.
	 */
	public void upateObjetivosActividad(Actividad ac, int nR) {

		boolean eliminar = false;
		for(int i = 0; i<ac.getObjetivo().size(); i++)
		{
			ObjetivosActividad oA = ac.getObjetivo().get(i);
			eliminar = false;
			for(String objetivo : getObjetivosSeleccionados())
			{
				if(oA.getIdObjetivo().getNombre().equals(objetivo))
				{
					eliminar = true;
					break;
				}
			}

			if(!eliminar)
			{
				ac.getObjetivo().remove(oA);
				simplePageToolDao.deleteObject(oA);
			}
		}

		for(String objetivo : getObjetivosSeleccionados())
		{				
			boolean estar = false;
			for(ObjetivosActividad oA: ac.getObjetivo())
			{
				if(objetivo.equals(oA.getIdObjetivo().getNombre()))
				{
					estar = true;
					break;
				}
			}

			if(!estar)
			{
				Objetivo obj = new ObjetivoImpl();
				obj.setIdObjetivo(simplePageToolDao.getObjetivoActividad(objetivo, nR));
				obj.setNombre(objetivo);
				ObjetivosActividadImpl objetivoAc = new ObjetivosActividadImpl(2,ac, obj);

				saveCaracteristica(objetivoAc, true);
			}
		}

	}
	/**
	 * Actualiza los recursos de una actividad.
	 * @param ac la actividad.
	 * @param tipo
	 */
	public void upateRecursoActividad(Actividad ac, int tipo) {

		Recurso recurso = new RecursoImpl("Does not exist","No aplica","");
		for(RecursosActividad rA : ac.getRecursos())
		{
			if(rA.getIdRecurso().getIdItemSakai().isEmpty())
			{
				if(!rA.getIdRecurso().getNombre().equals(recursoA))
				{
					//eliminar el anterior
					ac.getRecursos().remove(rA);
					simplePageToolDao.deleteObject(rA);

					//buscar recurso
					if(!recursoA.equals(cadenaOpciones))
						recurso = simplePageToolDao.getRecurso(recursoA);


					if(!recurso.getNombre().equals("Does not exist"))
					{
						RecursosActividad recursoNuevo = new RecursosActividadImpl(ac, recurso);
						saveCaracteristica(recursoNuevo,true);
					}	
				}
			}
		}
	}

	/**
	 * Agrega un agente.
	 * @param nombre
	 * @param tipo
	 * @return aliasA
	 */
	public String addAgent(String nombre, int tipo)
	{
		BeanASHYI bean = getBeanBesa();

		System.out.println("Verificar existencia de agente");
		String aliasA = "";
		if(!bean.existeAgente(nombre))	
		{
			System.out.println("Agente "+nombre+" no existe");
			//aliasA = bean.crearAgente(sessionManager.getCurrentSession().getUserEid());
			aliasA = bean.crearAgente(nombre,tipo,"");

		}
		aliasA = bean.getAgent(nombre);
		System.out.println("Agente "+aliasA+" creado");	
		return aliasA;
	}
	/**
	 * Retorna el currentUserNameId
	 * @return currentUserNameId
	 */
	public String getCurrentUserNameID() {
		if (currentUserNameId == null)
			currentUserNameId = UserDirectoryService.getCurrentUser().getDisplayName() + "-" + getCurrentUserId() + "-" + getCurrentUserType();
		return currentUserNameId;
	}
	/**
	 * Detecta el dispositivo.
	 * @return true si es desktop, false si es mobile.
	 */
	public String detectDevice()
	{		
		String deviceType = "";
		if(httpServletRequest.getHeader("User-Agent").indexOf("Mobile") != -1) {
			//you're in mobile land
			deviceType = "mobile";  
		} else {
			//nope, this is probably a desktop
			deviceType = "desktop";
		}		

		return deviceType;
	}
	/**
	 * Detecta la red.
	 * @return true dentro del campus, false fuera del campus.
	 */
	public boolean detectNet()
	{
		boolean dentro = false;
		String ipR = httpServletRequest.getRemoteAddr();
		String[] numR = ipR.split("\\.");
		System.out.println("!!!!!!!!!!!!: "+ipR);
		System.out.println("!!!!!!!!!!!!: "+detectDevice());
		String ipS = httpServletRequest.getLocalAddr();

		String[] num = ipS.split("\\.");

		if(num[0].equals(numR[0]))
		{
			System.out.println("!!!!!!!!: est&aacute dentro");
			dentro = true;
		}
		//InetAddress address = InetAddress.getByName("javeriana.edu.co"); 
		return dentro;		
	}
	/**
	 * Detecta el idioma.
	 * @return language
	 */
	public String detectLenguage()
	{		
		String language = httpServletRequest.getLocale().getDisplayLanguage();
		return language;
	}
	/**
	 * Revisa el chaea del usuario
	 * @param idUsuario
	 * @return 
	 */
	public boolean chaea(int idUsuario)
	{
		ChaeaDaoImpl dao = new ChaeaDaoImpl();
		ChaeaLogicImpl chaea = new ChaeaLogicImpl();
		dao.setDataSource(simplePageToolDao.getDataSource());
		chaea.setDao(dao);
		Map<Character, Double> resultadosAluno = chaea.getResultadosAluno(Long.valueOf(1),getCurrentUserId());
		double a = resultadosAluno.get(Questao.TIPO_ACTIVO);
		double p = resultadosAluno.get(Questao.TIPO_PRAGMATICO);
		double r = resultadosAluno.get(Questao.TIPO_REFLEXIVO);
		double t = resultadosAluno.get(Questao.TIPO_TEORICO);		

		if(a != 0.0 && p != 0.0 && r != 0.0 && t != 0.0)
		{			
			activo = (int) a;
			prag = (int) p;
			reflexivo = (int) r;
			teorico = (int) t;
			System.out.println("CHAEA::::::::::::::::::::::::: "+activo + ", "+ prag + ", "+ reflexivo + ", "+ teorico);
			return true;
		}

		return false;
	}
	/**
	 * Guarda los estilos de aprendizaje de un usuario.
	 * @param idUsuario id de usuario.
	 */
	public void saveEstilos(int idUsuario)
	{			
		Caracteristica estiloActivo = getCaracteristicaEstilo("Activo");
		Caracteristica estiloPrag = getCaracteristicaEstilo("Pragmatico");
		Caracteristica estiloReflexivo = getCaracteristicaEstilo("Reflexivo");
		Caracteristica estiloTeorico = getCaracteristicaEstilo("Teorico");

		Usuario user = simplePageToolDao.getUsuario(idUsuario);

		CaracteristicasUsuario cEstiloA = new  CaracteristicasUsuarioImpl(user, estiloActivo,(int) activo);
		saveCarateristica(cEstiloA);

		CaracteristicasUsuario cEstiloP = new  CaracteristicasUsuarioImpl(user, estiloActivo,(int) prag);
		saveCarateristica(cEstiloA);

		CaracteristicasUsuario cEstiloR = new  CaracteristicasUsuarioImpl(user, estiloActivo,(int) reflexivo);
		saveCarateristica(cEstiloA);

		CaracteristicasUsuario cEstiloT = new  CaracteristicasUsuarioImpl(user, estiloActivo,(int) teorico);
		saveCarateristica(cEstiloA);

	}

	/**
	 * Borra una unidad did&aacutectica.
	 * @param Unidad
	 */
	public void deleteUnidad(Actividad Unidad)
	{
		List<ActividadTieneActividad> lA = simplePageToolDao.getActividadTieneActividades(Unidad.getIdActividad());
		//a.setObjetivo(lO);
		for(int i = 0 ; i<lA.size() ; i++)
		{
			lA.get(i).setIdActividad(Unidad);
			simplePageToolDao.deleteObject(lA.get(i));
			simplePageToolDao.flush();
		}

		Actividad tema = simplePageToolDao.getactividad(getCurrentPage().getTitle(),2);
		lA = simplePageToolDao.getActividadTieneActividad(tema.getIdActividad(), Unidad.getIdActividad());
		for(int i = 0 ; i<lA.size() ; i++)
		{
			lA.get(i).setIdActividad(tema);
			simplePageToolDao.deleteObject(lA.get(i));
			simplePageToolDao.flush();
		}

		List<ObjetivosActividad> lO = simplePageToolDao.getObjetivosActividad(Unidad.getIdActividad());
		//a.setObjetivo(lO);
		for(int i = 0 ; i<lO.size() ; i++)
		{
			lO.get(i).setIdActividad(Unidad);
			simplePageToolDao.deleteObject(lO.get(i));
			simplePageToolDao.flush();
		}

		//eliminar actividad
		simplePageToolDao.deleteActividad(Unidad);
		simplePageToolDao.flush();
	}

	/**
	 * Retorna los items del ejecutor de la unidad.
	 * @param nR nivel de recursividad.
	 * @param currentUserId
	 * @param nombreActividad
	 * @return itemsSakai
	 */
	public List<SimplePageItem> getItemsEjecutorUnidad(int nR, String currentUserId, String nombreActividad) 
	{
		List<SimplePageItem> itemsSakai = new ArrayList<SimplePageItem>();

		Usuario usuario = simplePageToolDao.getUsuario(currentUserId);
		List<ItemPlan> items = simplePageToolDao.getItemsActivos(usuario, nombreActividad);
		for(ItemPlan item: items)
		{
			if(!item.getIdActividad().getNivel().equalsIgnoreCase("InicioUD"))
			{
				if(item.getIdRecurso() != null)
					itemsSakai.add(simplePageToolDao.getItemSakai(item));
				else
					itemsSakai.add(simplePageToolDao.getItemSakaiInicial());
			}
		}
		return itemsSakai;
	}

	/**
	 * Retorna las actividades en unidad.
	 * @param userId
	 * @param nombreActividad
	 * @return actividades
	 */
	public List<Actividad> getActividadesEnUnidadEjecutor(String userId, String nombreActividad) 
	{
		List<Actividad> actividades = new ArrayList<Actividad>();

		Usuario usuario = simplePageToolDao.getUsuario(userId);
		List<ItemPlan> items = simplePageToolDao.getItemsActivos(usuario, nombreActividad);
		for(ItemPlan item: items)
		{
			if(!item.getIdActividad().getNivel().equalsIgnoreCase("InicioUD"))
				actividades.add(simplePageToolDao.getActividad(item.getIdActividad().getIdActividad()));
		}
		return actividades;
	}

	/**
	 * Retorna los itemsUsuario de un usuario.
	 * @param userId
	 * @param nombreActividad
	 * @return itemsU
	 */
	public List<ItemsUsuario> getItemsUsuario(String userId, String nombreActividad) {

		List<ItemsUsuario> itemsU = new ArrayList<ItemsUsuario>();
		List<ItemPlan> items = simplePageToolDao.getItemsActivos(simplePageToolDao.getUsuario(userId), nombreActividad);
		for(int i = 0; i< items.size(); i++)
			if(!items.get(i).getIdActividad().getNivel().equalsIgnoreCase("InicioUD"))
				itemsU.add(simplePageToolDao.getItemsUsuario(items.get(i), userId));
		return itemsU;
	}

	/**
	 * Retorna los itemPlans de una unidad.
	 * @param nR nivel de recursividad.
	 * @param idActividad
	 * @param nombreActividad
	 * @return itemsSakai
	 */
	public List<SimplePageItem> getItemsPlanUnidad(int nR, int idActividad, String nombreActividad) {

		List<SimplePageItem> itemsSakai = new ArrayList<SimplePageItem>();

		List<ItemPlan> items = simplePageToolDao.getItemsActivosActividad(nombreActividad, nR);
		for(ItemPlan item: items)
		{
			if(item.getIdActividad().getIdActividad() == idActividad)
				itemsSakai.add(simplePageToolDao.getItemSakai(item));
		}
		return itemsSakai;
	}

	/**
	 * Retorna las actividades del plan en una unidad.
	 * @param nR nivel de recursividad.
	 * @param idActividad
	 * @param nombreActividad
	 * @return actividades
	 */
	public List<Actividad> getActividadesEnUnidadPlan(int nR, int idActividad, String nombreActividad)
	{
		List<Actividad> actividades = new ArrayList<Actividad>();

		List<ItemPlan> items = simplePageToolDao.getItemsActivosActividad(nombreActividad, nR);
		for(ItemPlan item: items)
		{
			if(item.getIdActividad().getIdActividad() == idActividad)
				actividades.add(simplePageToolDao.getActividad(item.getIdActividad().getIdActividad()));
		}
		return actividades;
	}

	/**
	 * Retorna los itemsUsuario del plan en una unidad.
	 * @param nR
	 * @param idActividad
	 * @param nombreActividad
	 * @return itemsU
	 */
	public List<List> getItemsUsuarioPlan(int nR, int idActividad, String nombreActividad)
	{
		List<ItemsUsuario> itemsU = new ArrayList<ItemsUsuario>();

		List devolver = new ArrayList();

		List<String> nomUsuarios = new ArrayList<String>();

		List<User> usaurios = getUsuarios();

		List<Usuario> usauriosActividad = simplePageToolDao.getUsuariosAshyi();

		for(int i = 0; i < usaurios.size(); i++)
		{
			for(Usuario usA : usauriosActividad)
			{
				if(usaurios.get(i).getId().equals(usA.getIdUsuarioSakai()))
				{
					List<ItemPlan> items = simplePageToolDao.getItemsActivosActividad(nombreActividad, nR);
					for(ItemPlan item: items)
					{
						if(item.getIdActividad().getIdActividad() == idActividad)
						{
							Actividad ac = simplePageToolDao.getActividad(idActividad);
							List<ItemsUsuario> actividadesU = getItemsUsuario(usA.getIdUsuarioSakai(), nombreActividad);
							for(ItemsUsuario it: actividadesU)
							{
								if(it.getIdItemPlan().getIdItemPlan() == item.getIdItemPlan())
								{
									itemsU.add(it);
									nomUsuarios.add(usaurios.get(i).getDisplayName());
									break;
								}
							}	
							break;
						}						
					}
					break;
				}

			}
		}
		devolver.add(itemsU);
		devolver.add(nomUsuarios);
		return devolver;
	}

	/**
	 * Retorna los usuarios.
	 * @return usuarios
	 */
	public List getUsuarios()
	{
		return UserDirectoryService.getUsers();
	}

	/**
	 * Retorna los usuarios de Ashyi.
	 * @return usuarios
	 */
	public List<User> getUsuariosAshyi()
	{
		String idSite = getCurrentSiteId();
		List<User> usuarios = simplePageToolDao.getUsuariosSite(idSite);
		for(int i = 0; i<usuarios.size(); i++)
		{
			User us = usuarios.get(i);
			//System.out.println("!!!!!!!!! tipo usu: "+us.getDisplayName()+" tipo: "+us.getType());
			if(!us.getType().equalsIgnoreCase("Student"))
			{
				usuarios.remove(i);
				i--;
			}
		}

		return usuarios;
	}

	/**
	 * Asigna el itemPlan de Ashyi.
	 * @param idTP
	 */
	public void setItemPAshyi(int idTP) {
		this.IdItemPAshyi=idTP;
	}

	/**
	 * Asigna el itemUsuario de Ashyi.
	 * @param idTU
	 */
	public void setItemUAshyi(int idTU) {
		this.IdItemUAshyi=idTU;
	}
	
	/**
	 * Asigna el idActividad.
	 * @param idActividad
	 */
	public void setIdActividad(int idActividad) {
		this.idActividad=idActividad;
	}

	/**
	 * Retorna los nombres de las actividades pasadas por par&aacutemetro.
	 * @param actividades
	 * @return nombres
	 */
	public String[] getNombresActividades(List<ActividadTieneActividad> actividades) {
	
		String[] nombres = new String[actividades.size()];
		
		for(int i = 0; i < actividades.size(); i++)
			nombres[i] = actividades.get(i).getIdActividadSiguienteNivel().getNombre();
		
		return nombres;
	}

}
