
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.lessonbuildertool.ActividadTieneActividadImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicaActividadImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicaImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicasItemsUsuarioImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicasRecursoImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicasUsuarioImpl;
import org.sakaiproject.lessonbuildertool.DependenciaActividadImpl;
import org.sakaiproject.lessonbuildertool.GrafoImpl;
import org.sakaiproject.lessonbuildertool.GrafoRelacionesImpl;
import org.sakaiproject.lessonbuildertool.GrafosUsuarioImpl;
import org.sakaiproject.lessonbuildertool.ItemImpl;
import org.sakaiproject.lessonbuildertool.ObjetivoImpl;
import org.sakaiproject.lessonbuildertool.ObjetivosActividadImpl;
import org.sakaiproject.lessonbuildertool.PalabraClaveActividadImpl;
import org.sakaiproject.lessonbuildertool.PalabraClaveImpl;
import org.sakaiproject.lessonbuildertool.RecursoImpl;
import org.sakaiproject.lessonbuildertool.RecursosActividadImpl;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.SimplePageLogEntry;
import org.sakaiproject.lessonbuildertool.SimpleStudentPage;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneActividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneItemPlan;
import org.sakaiproject.lessonbuildertool.model.AshyiToolDao;
import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaActividad;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario;
import org.sakaiproject.lessonbuildertool.model.Control;
import org.sakaiproject.lessonbuildertool.model.DependenciaActividad;
import org.sakaiproject.lessonbuildertool.model.Grafo;
import org.sakaiproject.lessonbuildertool.model.GrafoRelaciones;
import org.sakaiproject.lessonbuildertool.model.GrafosUsuario;
import org.sakaiproject.lessonbuildertool.model.Item;
import org.sakaiproject.lessonbuildertool.model.ItemPlan;
import org.sakaiproject.lessonbuildertool.model.ItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.Objetivo;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.PalabraClave;
import org.sakaiproject.lessonbuildertool.model.PalabraClaveActividad;
import org.sakaiproject.lessonbuildertool.model.Recurso;
import org.sakaiproject.lessonbuildertool.model.RecursosActividad;
import org.sakaiproject.lessonbuildertool.model.RespuestaItemsUsuario;
import org.sakaiproject.lessonbuildertool.model.SimplePageToolDao;
import org.sakaiproject.lessonbuildertool.model.Tipo;
import org.sakaiproject.lessonbuildertool.model.Usuario;
import org.sakaiproject.lessonbuildertool.service.GradebookIfc;
import org.sakaiproject.lessonbuildertool.service.LessonBuilderEntityProducer;
import org.sakaiproject.lessonbuildertool.service.LessonEntity;
import org.sakaiproject.lessonbuildertool.tool.view.GeneralViewParameters;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
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
import org.sakaiproject.util.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import pt.ipb.estig.chaea.dao.ChaeaDaoImpl;
import pt.ipb.estig.chaea.logic.ChaeaLogicImpl;
import pt.ipb.estig.chaea.model.Questao;
import uk.org.ponder.messageutil.MessageLocator;
import co.edu.javeriana.ashyi.ASHYIControlador.BeanASHYI;
import co.edu.javeriana.ashyi.Graph.Graph;
import co.edu.javeriana.ashyi.kiss.Data.Data;
import co.edu.javeriana.ashyi.kiss.Data.MainAgentData;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Clase que realiza las operaciones de la plataforma que tienen que ver con las entidades de ASHYI.
 * @author ashiy
 *
 */
public class AshyiBean {
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static Log log = LogFactory.getLog(AshyiBean.class);

	/**
	 * Enumerado que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public enum Status {
		NOT_REQUIRED, REQUIRED, DISABLED, COMPLETED, FAILED
	}

	// from ResourceProperites. This isn't in 2.7.1, so define it here. Let's
	// hope it doesn't change...
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String PROP_ALLOW_INLINE = "SAKAI:allow_inline";
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final Pattern YOUTUBE_PATTERN = Pattern
			.compile("v[=/_]([\\w-]{11}([\\?\\&][\\w\\.\\=\\&]*)?)");
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final Pattern YOUTUBE2_PATTERN = Pattern
			.compile("embed/([\\w-]{11}([\\?\\&][\\w\\.\\=\\&]*)?)");
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final Pattern SHORT_YOUTUBE_PATTERN = Pattern
			.compile("([\\w-]{11}([\\?\\&][\\w\\.\\=\\&]*)?)");
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final String GRADES[] = { "A+", "A", "A-", "B+", "B", "B-",
		"C+", "C", "C-", "D+", "D", "D-", "E", "F" };
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
	 * Objetivo de la unidad did&aacutectica.
	 */
	private String subpageGoalUnidad = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String subpageGoalActividad = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String palabraClave = null;
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
	private Site currentSite = null; // cache, can be null; used by
	// getCurrentSite

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
	private String filterHtml = ServerConfigurationService
			.getString(FILTERHTML);
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String selectedAssignment = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String selectedBlti = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String selectedActividad = null;// actividad

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
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private SimplePage currentPage;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Long currentPageId = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Long currentPageItemId = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String currentUserId = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private String currentUserNameId = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private long previousPageId = -1;

	// Item-specific variables. These are set by setters which are called
	// by the various edit dialogs. So they're basically inputs to the
	// methods used to make changes to items. The way it works is that
	// when the user submits the form, RSF takes all the form variables,
	// calls setters for each field, and then calls the method specified
	// by the form. The setters set these variables

	/**
	 * Identificador de un item asociado al Bean..
	 */
	public Long itemId = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
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
	public boolean graded;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public boolean sGraded;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String maxPoints;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public String sMaxPoints;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public boolean comments;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public boolean forcedAnon;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	public boolean isWebsite = false;
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

	// The following caches are used only during a single display of the page. I
	// believe they
	// are so transient that we don't have to worry about synchronizing them or
	// keeping them up to date.
	// Because the producer code tends to deal with items and even item ID's, it
	// doesn't keep objects such
	// as Assignment or PublishedAssessment around. It calls functions here to
	// worry about those. If we
	// don't cache, we'll be doing database lookups a lot. The worst is the code
	// to see whether an item
	// is available. Because it checks all items above, we'd end up order N**2
	// in the number of items on the
	// page in database queries. It doesn't appear that assignments and
	// assessments do any caching of their
	// own, but hibernate as we use it does.
	// Normal code shouldn't use the caches directly, but should call something
	// like getAssignment here,
	// which checks the cache and if necessary calls the real getAssignment.
	// I've chosen to do caching on
	// this level, and let the DAO be actual database access. I've really only
	// optimized what is used by
	// ShowPageProducer, as that is used every time a page is shown. Things used
	// when you add or change
	// an item aren't as critical.
	// If anyone is doing serious work on the code, I recommend creating an Item
	// class that encapsulates
	// all the stuff associated with items. Then the producer would manipulate
	// items. Thus the things in
	// these caches would be held in the Items.

	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<Long, SimplePageItem> itemCache = new HashMap<Long, SimplePageItem>();
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<Long, SimplePage> pageCache = new HashMap<Long, SimplePage>();
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private Map<Long, List<SimplePageItem>> itemsCache = new HashMap<Long, List<SimplePageItem>>();
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
	// this one needs to be global
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static Cache groupCache = null; // itemId => grouplist
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	private static Cache resourceCache = null;
	/**
	 * Atributo que ven&iacutea en el m&oacutedulo lessonbuilder de Sakai.
	 */
	protected static final int DEFAULT_EXPIRATION = 10 * 60;
	/**
	 * Nombre de una actividad.
	 */
	private String nomberA = null;
	/**
	 * Tipo de una actividad.
	 */
	private String tipoA = null;
	/**
	 * Recurso de una actividad.
	 */
	private String recursoA = null;
	/**
	 * Fecha inicial de una actividad.
	 */
	private String inicialF = null;
	/**
	 * Fecha final de una actividad.
	 */
	private String finalF = null;
	/**
	 * Descripci&oacuten de una actividad.
	 */
	private String descripcionA = null;
	/**
	 * Objetivo de una actividad.
	 */
	private String objetivoA = null;
	/**
	 * Dedicaci&oacuten de una actividad.
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
	 * Cadena que representa el repertes actual de la unidad did&aacutectica.
	 */
	private String reportesLabel = null;
	/**
	 * Cadena que representa el repertes actual de la unidad did&aacutectica.
	 */
	private String reportesLabel2 = null;
	
	/**
	 * Cadena que representa el repertes actual de la unidad did&aacutectica.
	 */
	private String usuActivos = null;
	
	/**
	 * Cadena que representa el id de los usuarios.
	 */
	private String usuarios = null;
	
	/**
	 * Cadena que representa los datos del log de caracteristicas.
	 */
	private String datosTabla = null;
	/**
	 * Cadena que representa los datos del log de Grafos.
	 */
	private String datosTabla2 = null;
	
	/**
	 * Cadena que representa los datos del log de grafos.
	 */
	private String datosGrafos = null;
	/**
	 * Cadena que representa los datos del log de grafos.
	 */
	private String datosGrafos2 = null;
	
	/**
	 * Cadena que representa el conjunto de actividades de la unidad did&aacutectica.
	 */
	private String existingActivities = null;
	/**
	 * Representa la &uacuteltima conexi&oacuten.
	 */
	private String lastConnection = null;
	/**
	 * Representa el estilo de un itemPlan al presentarlo gr&aacuteficamente.
	 */
	private String itemPlansStyle = null;

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
	 * Identificador de la actividad.
	 */
	private String idActividad = "";
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

	// BESAKISS
	/**
	 * Atributo de BESAKISS Contexto inicial.
	 */
	private Context initCtx;
	/**
	 * Atributo de BESAKISS.
	 */
	private Context envCtx;
	// TODO contexto normal
	//private static BeanBESA bean;
	/**
	 * Atributo de BESAKISS Bean de BESA.
	 */
	private BeanASHYI bean;

	// agente interface
	/**
	 * Atributo de BESAKISS Agente interfaz.
	 */
	private String agenteInterface = "_";

	// agente comunicacion
	/**
	 * Atributo de BESAKISS Agente de comunicaci&oacuten.
	 */
	private String agenteComunicacion = "_";

	/**
	 * Lista de actividades a mostrar.
	 */
	private List<Actividad> actividadesMostrar = new ArrayList<Actividad>();
	/**
	 * Lista de actividades de refuerzo a mostrar.
	 */
	private List<Actividad> actividadesRefuerzoMostrar = new ArrayList<Actividad>();

	// chaea
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
	 * Tipo de dispositivo.
	 */
	private String deviceType = "";
	/**
	 * Tipo de acceso.
	 */
	private String accessType = "";
	/**
	 * Retroalimentaci&oacuten del estudiante.
	 */
	private String retroalimentacionE = "";
	/**
	 * Retroalimentaci&oacuten del profesor.
	 */
	private String retroalimentacionP = "";
	/**
	 * Nota asignada.
	 */
	private String notaA = "";
	/**
	 * Auxiliar para carga de archivos como recurso de un itemUsuario.
	 */
	private String recursoUpload = "";
	/**
	 * Identificador de itemUsuario.
	 */
	private String idTU = "";
	/**
	 * Identificador de itemPlan.
	 */
	private String idTP = "";
	/**
	 * Identificador de recurso del itemUsuario.
	 */
	private String idRIU = "";
	/**
	 * Objetivo de unidad did&aacutectica.
	 */
	public String objetivoUD = "";
	/**
	 * Fecha inicial de un objetivo de unidad did&aacutectica.
	 */
	public String fechaInicialObjetivoUD = "";
	/**
	 * Fecha final de un objetivo de unidad did&aacutectica.
	 */
	public String fechaFinalObjetivoUD = "";
	/**
	 * Retorna el identificador del recurso del itemUsuario.
	 * 
	 * @return idRIU
	 */
	public String getIdRIU() {
		return idRIU;
	}
	/**
	 * Asigna el identificador del recurso del itemUsuario.
	 * 
	 * @param idRIU
	 */
	public void setIdRIU(String idRIU) {
		this.idRIU = idRIU;
	}

	/**
	 * Retorna el identificador del itemUsuario.
	 * 
	 * @return IdTU
	 */
	public String getIdTU() {
		return idTU;
	}
	/**
	 * Asigna el identificador del itemUsuario.
	 * 
	 * @param IdTU
	 */
	public void setIdTU(String idTU) {
		this.idTU = idTU;
	}
	/**
	 * Retorna el identificador del itemPlan.
	 * 
	 * @return IdTP
	 */
	public String getIdTP() {
		return idTP;
	}
	/**
	 * Asigna el identificador del itemPlan.
	 * 
	 * @param idTP
	 */
	public void setIdTP(String idTP) {
		this.idTP = idTP;
	}
	/**
	 * Retorna el identificador de la actividad.
	 * 
	 * @return idActividad
	 */
	public String getIdActividad() {
		return idActividad;
	}
	/**
	 * Asigna el identificador de la actividad.
	 * 
	 * @param idActividad
	 */
	public void setIdActividad(String idActividad) {
		this.idActividad = idActividad;
	}

	/**
	 * Retorna el string de carga del recurso.
	 * 
	 * @return recursoUpload
	 */
	public String getRecursoUpload() {
		return recursoUpload;
	}
	/**
	 * Asigna el string de carga del recurso.
	 * 
	 * @param recursoUpload
	 */
	public void setRecursoUpload(String recursoUpload) {
		this.recursoUpload = recursoUpload;
	}
	/**
	 * Retorna la retroalimentaci&oacuten del estudiante.
	 * 
	 * @return recursoUpload
	 */
	public String getRetroalimentacionE() {
		return retroalimentacionE;
	}
	/**
	 * Asigna la retroalimentaci&oacuten del estudiante.
	 * 
	 * @param recursoUpload
	 */
	public void setRetroalimentacionE(String retroalimentacionE) {
		this.retroalimentacionE = retroalimentacionE;
	}
	/**
	 * Retorna la retroalimentaci&oacuten del profesor.
	 * 
	 * @return recursoUpload
	 */
	public String getRetroalimentacionP() {
		return retroalimentacionP;
	}
	/**
	 * Asigna la retroalimentaci&oacuten del profesor.
	 * 
	 * @param recursoUpload
	 */
	public void setRetroalimentacionP(String retroalimentacionP) {
		this.retroalimentacionP = retroalimentacionP;
	}
	/**
	 * Retorna la nota asignada.
	 * 
	 * @return notaA
	 */
	public String getNotaA() {
		return notaA;
	}
	/**
	 * Asigna la nota del estudiante.
	 * 
	 * @param notaA
	 */
	public void setNotaA(String notaA) {
		this.notaA = notaA;
	}
	/**
	 * Retorna el tipo de dispositivo.
	 * 
	 * @return deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * Asigna el tipo de dispositivo.
	 * 
	 * @param deviceType
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * Retorna el tipo de acceso.
	 * 
	 * @return accessType
	 */
	public String getAccessType() {
		return accessType;
	}
	/**
	 * Asigna el tipo de acceso.
	 * 
	 * @param accessType
	 */
	public void setAccessType(String accesType) {
		this.accessType = accesType;
	}
	/**
	 * Retorna las actividades de refuerzo a mostrar.
	 * 
	 * @return actividadesRefuerzoMostrar
	 */
	public List<Actividad> getActividadesRefuerzoMostrar() {
		return actividadesRefuerzoMostrar;
	}
	/**
	 * Asigna las actividades de refuerzo a mostrar.
	 * 
	 * @param actividadesRefuerzoMostrar
	 */
	public void setActividadesRefuerzoMostrar(List actividadesMostrar) {

		for (int i = 0; i < actividadesMostrar.size(); i++)
			this.actividadesRefuerzoMostrar.add(ashyiToolDao
					.getActividad((Integer) actividadesMostrar.get(i)));
	}

	/**
	 * Retorna las actividades a mostrar.
	 * 
	 * @return actividadesMostrar
	 */
	public List<Actividad> getActividadesMostrar() {
		return actividadesMostrar;
	}
	/**
	 * Asigna las actividades a mostrar.
	 * 
	 * @param actividadesMostrar
	 */
	public void setActividadesMostrar(List actividadesMostrar) {

		if (this.actividadesMostrar.size() == 0) {
			for (int i = 0; i < actividadesMostrar.size(); i++)
			{
				this.actividadesMostrar.add(ashyiToolDao.getActividad((Integer) actividadesMostrar.get(i)));
			}
		}
	}
	/**
	 * Retorna el agente de comunicaci&oacuten.
	 * 
	 * @return agenteComunicacion
	 */
	public String getAgenteComunicacion() {
		return agenteComunicacion;
	}
	/**
	 * Asigna el agente de comunicaci&oacuten.
	 * 
	 * @param agenteComunicacion
	 */
	public void setAgenteComunicacion(String agenteComunicacion) {
		this.agenteComunicacion = agenteComunicacion;
	}
	/**
	 * Retorna el objetivo de la unidad did&aacutectica.
	 * 
	 * @return subpageGoalActividad
	 */
	public String getSubpageGoalActividad() {
		return subpageGoalActividad;
	}
	/**
	 * Asigna el objetivo de la unidad did&aacutectica.
	 * 
	 * @param subpageGoalActividad
	 */
	public void setSubpageGoalActividad(String subpageGoalActividad) {
		this.subpageGoalActividad = subpageGoalActividad;
	}
	/**
	 * Retorna la palabra clave.
	 * 
	 * @return palabraClave
	 */
	public String getPalabraClave() {
		return palabraClave;
	}
	/**
	 * Asigna la palabra clave.
	 * 
	 * @param palabraClave
	 */
	public void setPalabraClave(String palabraClave) {
		this.palabraClave = palabraClave;
	}
	/**
	 * Retorna el nivel de las actividades.
	 * 
	 * @return nivelActividades
	 */
	public String getNivelActividades() {
		return nivelActividades;
	}
	/**
	 * Asigna el nivel de las actividades.
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
	public Context getInitCtx() {
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
	public BeanASHYI getBeanBesa() {
		try {
			// TODO contexto normal
			bean = (BeanASHYI) getInitCtx().lookup("bean/BeanBESAKISS");	
			//		if(getCurrentPage().getParent() == null)// si es un tema
			//			agenteComunicacion = "MainASHYIAgent-" + getCurrentSite().getTitle()+"-"+getCurrentPage().getTitle();

			agenteComunicacion = "MainASHYIAgent-" + getCurrentSite().getTitle();
			bean.crearAgenteMain(agenteComunicacion);
			return bean;
		} catch (NamingException e) {
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
	 * Asigna los tipos de tipo.
	 * 
	 * @param tipo
	 */
	public void setTiposTipo(List<Tipo> tiposTipo) {
		this.tiposTipo = tiposTipo;
	}
	/**
	 * Retorna el nombre del tipo.
	 * 
	 * @return nombreTipo
	 */
	public String getNombreTipo() {
		return nombreTipo;
	}
	/**
	 * Asigna el nombre del tipo.
	 * 
	 * @param nombreTipo
	 */
	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}
	/**
	 * Retorna el tipo del tipo.
	 * 
	 * @return tipoTipo
	 */
	public String getTipoTipo() {
		return tipoTipo;
	}
	/**
	 * Asigna el tipo del tipo.
	 * 
	 * @param tipoTipo
	 */
	public void setTipoTipo(String tipoTipo) {
		this.tipoTipo = tipoTipo;
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
	 * Retorna el estilo de un itemPlan.
	 * 
	 * @return itemPlansStyle
	 */
	public String getItemPlansStyle() {
		return itemPlansStyle;
	}
	/**
	 * Asigna el estilo de un itemPlan.
	 * 
	 * @param itemPlansStyle
	 */
	public void setItemPlansStyle(String itemPlansStyle) {
		this.itemPlansStyle = itemPlansStyle;
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
	 * Controlador de la interacci&oacuten con la base de datos de Ashyi.
	 */
	private AshyiToolDao ashyiToolDao;
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
	static Object setupFtStuff() {
		Object ret = null;
		try {
			levelClass = Class
					.forName("org.sakaiproject.util.api.FormattedText$Level");
			levels = levelClass.getEnumConstants();
			ftClass = Class.forName("org.sakaiproject.util.api.FormattedText");
			ftMethod = ftClass.getMethod("processFormattedText", new Class[] {
					String.class, StringBuilder.class, levelClass });
			ret = org.sakaiproject.component.cover.ComponentManager
					.get("org.sakaiproject.util.api.FormattedText");
			return ret;
		} catch (Exception e) {
			log.error("Formatted Text with levels not available: " + e);
			return null;
		}
	}
	/**
	 * M&eacutetodo que inicializa el bean.
	 */
	public void init() {
		if (groupCache == null) {
			groupCache = memoryService
					.newCache("org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean.groupCache");
		}

		if (resourceCache == null) {
			resourceCache = memoryService
					.newCache("org.sakaiproject.lessonbuildertool.tool.beans.SimplePageBean.resourceCache");
		}
		this.existingFlowChart = "";
		this.existingActivities = "";
	}

	// no destroy. We want to leave the cache intact when we exit, because
	// there's one of us
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
		List<String> errors = (List<String>) toolSession
				.getAttribute("lessonbuilder.errors");
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
		List<String> errors = (List<String>) toolSession
				.getAttribute("lessonbuilder.errors");
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
	public void setErrKey(String key, String text) {
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
	 * M&eacutetodo que ya ven&iacutea en el bean con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public void setImporttop(boolean i) {
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

	// hibernate interposes something between us and saveItem, and that proxy
	// gets an
	// error after saveItem does. Thus we never see any value that saveItem
	// might
	// return. Hence we pass saveItem a list to which it adds the error message.
	// If
	// there is a message from saveItem take precedence over the message we
	// detect here,
	// since it's the root cause.
	/**
	 * Almacena un objeto en la base de datos de Sakai.
	 * @param i el objeto.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveItem(Object i, boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();

		try {
			simplePageToolDao.saveItem(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
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
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {
			ashyiToolDao.saveActividad(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena un grafoRelaciones en la base de datos de Ashyi.
	 * @param i el grafoRelaciones.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveGrafoRelaciones(Object i, boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {
			GrafoRelaciones g=(GrafoRelaciones) i;
			//System.out.println("Guardando relacion: "+g.getIdItemPlan_Origen()+" y "+g.getIdItemPlan_Destino());
			ashyiToolDao.saveGrafoRelaciones(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena un grafo en la base de datos de Ashyi.
	 * @param i el grafo.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveGrafo(Object i, boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {
			ashyiToolDao.saveGrafo(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena un grafosUsuario en la base de datos de Ashyi.
	 * @param i el grafosUsuario.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveGrafosUsuario(Object i, boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {
			ashyiToolDao.saveGrafosUsuario(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena un itemPlan en la base de datos de Ashyi.
	 * @param i el itemPlan.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveItemPlan(Object i, boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {

			ashyiToolDao.saveItemPlan(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
			return false;
		}

		return true;
	}
	/**
	 * Actualiza un itemPlan en la base de datos de Ashyi.
	 * @param i el itemPlan.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean updateItemPlan(Object i, boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {
			ashyiToolDao.update(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
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
		List<String> elist = new ArrayList<String>();

		try {
			ashyiToolDao.update(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
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
	public boolean saveDependenciaActividad(Object i,
			boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {
			ashyiToolDao.saveDependenciaActividad(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
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
	public boolean saveActividadTieneActividad(Object i,
			boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {
			ashyiToolDao.saveActividadTieneActividad(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
			return false;
		}

		return true;
	}
	/**
	 * Almacena una actividadTieneItemPlan en la base de datos de Ashyi.
	 * @param i la actividadTieneItemPlan.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveActividadTieneItemPlan(Object i,
			boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		int id = -1;
		try {
			ashyiToolDao.saveActividadTieneItemPlan(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);

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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
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
		List<String> elist = new ArrayList<String>();

		try {
			ashyiToolDao.saveCaracteristica(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
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
	 * Almacena un grafoRelaciones en la base de datos de Ashyi.
	 * @param i el grafoRelaciones.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveGrafoRelaciones(GrafoRelaciones i) {
		return saveGrafoRelaciones(i, true);
	}
	/**
	 * Almacena un grafo en la base de datos de Ashyi.
	 * @param i el grafo.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveGrafo(Grafo i) {
		return saveGrafo(i, true);
	}
	/**
	 * Almacena un itemPlan en la base de datos de Ashyi.
	 * @param i el itemPlan.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveItemPlan(ItemPlan i) {
		return saveItemPlan(i, true);
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
	/**
	 * Almacena un grafosUsuario en la base de datos de Ashyi.
	 * @param i el grafosUsuario.
	 * @return true si fue posible, false si no.
	 */
	public boolean saveGrafosUsuario(GrafosUsuario i) {
		return saveGrafosUsuario(i, true);
	}

	// see notes for saveupdate

	// requiresEditPermission determines whether ashyiToolDao should confirm
	// edit permissions before making the update
	/**
	 * Actualiza un objeto cualquiera en la base de datos de Ashyi.
	 * @param i el objeto.
	 * @param requiresEditPermission si el usuario requiere permisos de edici&oacuten.
	 * @return true si fue posible, false si no.
	 */
	boolean update(Object i, boolean requiresEditPermission) {
		String err = null;
		List<String> elist = new ArrayList<String>();
		try {
			ashyiToolDao.update(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
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
	/**
	 * Retorna el string "cancel".
	 * @return "cancel".
	 */
	public String cancel() {
		return "cancel";
	}
	
	/**
	 * Retorna el string "back".
	 * @return "back".
	 */
	public String back() {
		return "back";
	}

	// nueva actividad

	/**
	 * Crea una nueva actividad.
	 * @param tipoS
	 * @param item
	 * @param name
	 * @param description
	 * @param nR
	 * @param dedicacion
	 * @param nivel
	 * @return i la actividad.
	 */
	private Actividad appendActividad(Tipo tipoS, Item item, String name,
			String description, int nR, int dedicacion, String nivel) {

		Actividad i = ashyiToolDao.makeActividad(tipoS, item, name,
				description, nR, dedicacion, nivel);
		// long pageId, int type, String name, String goal, String description,
		// String dI, String dF

		// defaults to a fixed width and height, appropriate for some things,
		// but for an
		// image, leave it blank, since browser will then use the native size
		// clearImageSize(i);

		// saveActividad(i);
		return i;
	}

	// nuevo item plan

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

	// nuevo item plan
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

	// nueva unidad
	/**
	 * Crea una nueva actividad.
	 * @param item
	 * @param name
	 * @param nR
	 * @param dedicacion
	 * @param nivel
	 * @return i la actividad.
	 */
	private Actividad appendActividad(Item item, String name, int nR,
			String dedicacion, String nivel) {

		Actividad i = ashyiToolDao.makeActividad(item, name, nR,
				Integer.valueOf(dedicacion), nivel);
		// long pageId, int type, String name, String goal, String description,
		// String dI, String dF

		// defaults to a fixed width and height, appropriate for some things,
		// but for an
		// image, leave it blank, since browser will then use the native size
		// clearImageSize(i);

		// saveActividad(i);
		return i;
	}

	// nueva caracteristica
	/**
	 * Agrega una nueva caracter&iacutestica.
	 * @param type
	 * @param name
	 * @return i la caracter&iacutestica.
	 */
	private Caracteristica appendCaracteristica(Item type, String name) {

		Caracteristica i = ashyiToolDao.makeCaracteristica(type, name);
		// long pageId, int type, String name, String goal, String description,
		// String dI, String dF

		// defaults to a fixed width and height, appropriate for some things,
		// but for an
		// image, leave it blank, since browser will then use the native size
		// clearImageSize(i);

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
	private boolean addDependenciaActividad(String target, String source) {

		Actividad tar, sour;
		List<Actividad> list = ashyiToolDao.getActividades(Integer
				.parseInt(target));
		tar = list.get(0);
		list = ashyiToolDao.getActividades(Integer.parseInt(source));
		sour = list.get(0);

		// mirar si ya existe la dependencia o una entre ambas partes y
		// cambiarla o poner una nueva
		List<DependenciaActividad> depList = ashyiToolDao
				.getDependenciaActividad(Integer.parseInt(target),
						Integer.parseInt(source));
		DependenciaActividad dep;
		if (depList.size() > 0) {
			System.out.println("Ya existe la dependencia");
			return true;
		} else {
			depList = ashyiToolDao.getDependenciaActividad(
					Integer.parseInt(source), Integer.parseInt(target));
			if (depList.size() > 0) {
				dep = new DependenciaActividadImpl(1, tar, sour, 0);
				// reemplazar la dependencia en la BD
				ashyiToolDao.deleteObject(dep);
				ashyiToolDao.flush();
			} else
				dep = new DependenciaActividadImpl(1, tar, sour, 0);
		}
		return saveDependenciaActividad(dep, true);
	}

	/**
	 * Actualiza una dependenciaActividad.
	 * @param target
	 * @param source
	 * @return true si fue posible, false si no.
	 */
	private boolean updateDependenciaActividad(String target, String source) {

		Actividad tar, sour;
		List<Actividad> list = ashyiToolDao.getActividades(Integer
				.parseInt(target));

		tar = list.get(0);
		list = ashyiToolDao.getActividades(Integer.parseInt(source));
		sour = list.get(0);

		// mirar si ya existe la dependencia o una entre ambas partes y
		// cambiarla o poner una nueva
		List<DependenciaActividad> depList = ashyiToolDao
				.getDependenciaActividad(Integer.parseInt(target),
						Integer.parseInt(source));
		DependenciaActividad dep;
		if (depList.size() > 0) {
			dep = depList.get(0);
			ashyiToolDao.deleteObject(dep);
			ashyiToolDao.flush();
			return true;
		} else {
			depList = ashyiToolDao.getDependenciaActividad(
					Integer.parseInt(source), Integer.parseInt(target));
			if (depList.size() > 0) {
				dep = new DependenciaActividadImpl(1, sour, tar, 0);
				// reemplazar la dependencia en la BD
				ashyiToolDao.deleteObject(dep);
				ashyiToolDao.flush();
			}
		}
		dep = new DependenciaActividadImpl(1, tar, sour, 0);
		return saveDependenciaActividad(dep, true);
	}

	/**
	 * Actualiza una actividadTieneActividad.
	 * @param idSiguienteNivel
	 * @param estiloSiguienteNivel
	 * @param requiresEditPermission
	 * @return true si fue posible, false si no.
	 */
	public boolean updateActividadTieneActividad(String idSiguienteNivel,
			String estiloSiguienteNivel, boolean requiresEditPermission) {
		String nombre = getCurrentPage().getTitle();
		Actividad acPadre = ashyiToolDao.getactividad(nombre, 3);
		int id = acPadre.getIdActividad();
		// int id=1;
		String err = null;
		List<String> elist = new ArrayList<String>();

		ActividadTieneActividad ata = ashyiToolDao.getActividadTieneActividad(
				id, Integer.parseInt(idSiguienteNivel)).get(0);
		ata.setEstiloActividadSiguienteNivel(estiloSiguienteNivel);
		return ashyiToolDao.update(ata, elist,
				messageLocator.getMessage("simplepage.nowrite"),
				requiresEditPermission);
	}

	/**
	 * Actualiza una actividadTieneItemPlan.
	 * @param idItemPlan
	 * @param estiloItemPlan
	 * @param requiresEditPermission
	 * @return true si fue posible, false si no.
	 */
	public boolean updateActividadTieneItemPlan(String idItemPlan,
			String estiloItemPlan, boolean requiresEditPermission) {
		String nombre = getCurrentPage().getTitle();
		Actividad acPadre = ashyiToolDao.getactividad(nombre, 3);
		int id = acPadre.getIdActividad();
		String err = null;
		List<String> elist = new ArrayList<String>();

		ActividadTieneItemPlan atip = ashyiToolDao.getActividadTieneItemPlan(
				id, Integer.parseInt(idItemPlan)).get(0);
		atip.setEstiloItemPlan(estiloItemPlan);
		return ashyiToolDao.update(atip, elist,
				messageLocator.getMessage("simplepage.nowrite"),
				requiresEditPermission);
	}

	/**
	 * Returns 0 if user has site.upd or simplepage.upd. Returns 1 if user is
	 * page owner Returns 2 otherwise
	 * 
	 * @return
	 */
	public int getEditPrivs() {
		if (editPrivs != null) {
			return editPrivs;
		}
		editPrivs = 2;
		String ref = "/site/" + getCurrentSiteId();
		boolean ok = securityService.unlock(
				SimplePage.PERMISSION_LESSONBUILDER_UPDATE, ref);
		if (ok)
			editPrivs = 0;

		SimplePage page = getCurrentPage();
		if (editPrivs != 0 && page != null
				&& getCurrentUserId().equals(page.getOwner())) {
			editPrivs = 1;
		}

		return editPrivs;
	}

	/**
	 * Returns true if user has site.upd, simplepage.upd, or is page owner.
	 * False otherwise.
	 * 
	 * @return
	 */
	public boolean canEditPage() {
		if (getEditPrivs() <= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Retorna si el usuario puede ver la p&aacutegina.
	 * @return true si puede, false si no.
	 */
	public boolean canReadPage() {
		String ref = "/site/" + getCurrentSiteId();
		return securityService.unlock(SimplePage.PERMISSION_LESSONBUILDER_READ,
				ref);
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
	 * Asigna el controlador de la base de datos de Ashyi.
	 * @param dao.
	 */
	public void setAshyiToolDao(Object dao) {
		ashyiToolDao = (AshyiToolDao) dao;
	}

	/**
	 * Retorna los items de una p&aacutegina.
	 * @param pageid
	 * @return items los items.
	 */
	public List<SimplePageItem> getItemsOnPage(long pageid) {
		List<SimplePageItem> items = itemsCache.get(pageid);
		if (items != null)
			return items;

		items = simplePageToolDao.findItemsOnPage(pageid);

		// This code adds a global comments tool to the bottom of each
		// student page, but only if there's something else on the page
		// already and the instructor has enabled the option.
		if (items.size() > 0) {
			SimplePage page = getPage(pageid);
			if (page.getOwner() != null) {
				SimpleStudentPage student = simplePageToolDao
						.findStudentPage(page.getTopParent());
				if (student != null && student.getCommentsSection() != null) {
					SimplePageItem item = simplePageToolDao.findItem(student
							.getItemId());
					if (item != null && item.getShowComments() != null
							&& item.getShowComments()) {
						items.add(0, simplePageToolDao.findItem(student
								.getCommentsSection()));
					}
				}
			}
		}

		for (SimplePageItem item : items) {
			itemCache.put(item.getId(), item);
		}

		itemsCache.put(pageid, items);
		return items;
	}

	/**
	 * Elimina un item de la p&aacutegina.
	 * @return el resultado.
	 */
	public String deleteItem() {
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
			// checkControlGroup(i, false);
		}

		// Also delete gradebook entries
		if (i.getGradebookId() != null) {
			gradebookIfc.removeExternalAssessment(getCurrentSiteId(),
					i.getGradebookId());
		}

		if (i.getAltGradebook() != null) {
			gradebookIfc.removeExternalAssessment(getCurrentSiteId(),
					i.getAltGradebook());
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

			return "successDelete";
		} else {
			log.warn("deleteItem error deleting Item: " + itemId);
			return "failure";
		}
	}

	// not clear whether it's worth caching this. The first time it's called for
	// a site
	// the pages are fetched. Beyond that it's a linear search of pages that are
	// in memory
	// ids are sakai.assignment.grades, sakai.samigo, sakai.mneme, sakai.forums,
	// sakai.jforum.tool

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
	/**
	 * Retorna el identificador de la p&aacutegina actual.
	 * @return currentSiteId el id de la p&aacutegina.
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
	 * Asigna el identificador de la p&aacutegina actual.
	 * @param currentSiteId el id de la p&aacutegina.
	 */
	public void setCurrentSiteId(String siteId) {
		currentSiteId = siteId;
	}

	// recall that code typically operates on a "current page." See below for
	// the code that sets a new current page. We also have a current item, which
	// is the item defining the page. I.e. if the page is a subpage of another
	// one, this is the item on the parent page pointing to this page. If it's
	// a top-level page, it's a dummy item. The item is needed in order to do
	// access checks. Whether an item is required, etc, is stored in the item.
	// in theory a page could be called from several other pages, with different
	// access control parameters. So we need to know the actual item on the page
	// page from which this page was called.

	// we need to track the pageitem because references to the same page can
	// appear
	// in several places. In theory each one could have different status of
	// availability
	// so we need to know which in order to check availability
	/**
	 * Actualiza el id del item de la p&aacutegina.
	 * @param item el id del item.
	 * @throws PermissionException
	 */
	public void updatePageItem(long item) throws PermissionException {
		SimplePageItem i = findItem(item);
		if (i != null) {
			if (i.getType() != SimplePageItem.STUDENT_CONTENT
					&& (long) currentPageId != (long) Long.valueOf(i
							.getSakaiId())) {
				log.warn("updatePageItem permission failure " + i + " "
						+ Long.valueOf(i.getSakaiId()) + " " + currentPageId);
				throw new PermissionException(getCurrentUserId(), "set item",
						Long.toString(item));
			}
		}

		currentPageItemId = item;
		sessionManager.getCurrentToolSession().setAttribute(
				"current-pagetool-item", item);
	}

	// update our concept of the current page. it is imperative to make sure the
	// page is in
	// the current site, or people could hack on other people's pages

	// if you call updatePageObject, consider whether you need to call
	// updatePageItem as well
	// this combines two functions, so maybe not, but any time you're going to a
	// new page
	// you should do both. Make sure all Producers set the page to the one they
	// will work on
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * Actualiza la p&aacutegina.
	 * @param l el id de la p&aacutegina.
	 * @param save
	 */
	public void updatePageObject(long l, boolean save)
			throws PermissionException {
		if (l != previousPageId) {
			currentPage = getPage(l);
			String siteId = getCurrentSiteId();

			// get a rare error here, trying to debug it
			if (currentPage == null || currentPage.getSiteId() == null) {
				throw new PermissionException(getCurrentUserId(), "set page",
						Long.toString(l));
			}

			// page should always be in this site, or someone is gaming us
			if (!currentPage.getSiteId().equals(siteId))
				throw new PermissionException(getCurrentUserId(), "set page",
						Long.toString(l));
			previousPageId = l;

			if (save) {
				sessionManager.getCurrentToolSession().setAttribute(
						"current-pagetool-page", l);
			}

			currentPageId = (Long) l;
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

	// if tool was reset, return last page from previous session, so we can give
	// the user
	// a chance to go back
	/**
	 * Retorna la &uacuteltima p&aacutegina de la &uacuteltima sesi&oacuten si el tool fu&eacute reseteado.
	 * @return la p&aacutegina.
	 */
	public SimplePageToolDao.PageData toolWasReset() {
		if (sessionManager.getCurrentToolSession().getAttribute(
				"current-pagetool-page") == null) {
			// no page in session, which means it was reset
			String toolId = ((ToolConfiguration) toolManager
					.getCurrentPlacement()).getPageId();
			return simplePageToolDao.findMostRecentlyVisitedPage(
					getCurrentUserId(), toolId);
		} else
			return null;
	}

	// ought to be simple, but this is typically called at the beginning of a
	// producer, when
	// the current page isn't set yet. So if there isn't one, we use the session
	// variable
	// to tell us what the current page is. Note that a user can add our tool
	// using Site
	// Info. Site info knows nothing about us, so it will make an entry for the
	// page without
	// creating it. When the user then tries to go to the page, this code will
	// be the firsst
	// to notice it. Hence we have to create pages that don't exist
	/**
	 * Retorna el id de la p&aacutegina actual.
	 * @return currentPageId
	 */
	public long getCurrentPageId() {
		// return
		// ((ToolConfiguration)toolManager.getCurrentPlacement()).getPageId();

		if (currentPageId != null)
			return (long) currentPageId;

		Placement placement = toolManager.getCurrentPlacement();
		// See whether the tool is disabled in Sakai site information
		// you can either hide or disable a tool. Our page hidden is
		// really a disable, so we sync Sakai's disabled with our hidden
		// we're only checking when you first go into a tool
		Properties roleConfig = placement.getPlacementConfig();
		String roleList = roleConfig.getProperty("functions.require");
		boolean siteHidden = (roleList != null && roleList.indexOf(SITE_UPD) > -1);

		// Let's go back to where we were last time.
		Long l = (Long) sessionManager.getCurrentToolSession().getAttribute(
				"current-pagetool-page");
		if (l != null && l != 0) {
			try {
				updatePageObject(l);
				Long i = (Long) sessionManager.getCurrentToolSession()
						.getAttribute("current-pagetool-item");
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

			l = simplePageToolDao
					.getTopLevelPageId(((ToolConfiguration) placement)
							.getPageId());
			;
			// l = simplePageToolDao.getTopLevelPageId(((ToolConfiguration)
			// toolManager.getCurrentPlacement()).getPageId());

			if (l != null) {
				try {
					updatePageObject(l);
					// this should exist except if the page was created by old
					// code
					SimplePageItem i = simplePageToolDao
							.findTopLevelPageItemBySakaiId(String.valueOf(l));
					if (i == null) {
						// and dummy item, the site is the notional top level
						// page
						i = simplePageToolDao.makeItem(0, 0,
								SimplePageItem.PAGE, l.toString(),
								currentPage.getTitle());
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
				String toolId = ((ToolConfiguration) toolManager
						.getCurrentPlacement()).getPageId();
				String title = getCurrentSite().getPage(toolId).getTitle(); // Use
				// title
				// supplied

				// during creation
				SimplePage page = simplePageToolDao.makePage(toolId,
						getCurrentSiteId(), title, null, null);
				if (!saveItem(page)) {
					currentPage = null;
					return 0;
				}

				try {
					updatePageObject(page.getPageId());
					l = page.getPageId();

					// and dummy item, the site is the notional top level page
					SimplePageItem i = simplePageToolDao.makeItem(0, 0,
							SimplePageItem.PAGE, l.toString(), title);
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
	private void syncHidden(SimplePage page, boolean siteHidden) {
		// only do it for top level pages
		if (page != null && page.getParent() == null) {
			// hidden in site
			if (siteHidden != page.isHidden()) {
				page.setHidden(siteHidden);
				// use quick, as we don't want permission check. even normal
				// users can do this
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
	public SimplePageItem getCurrentPageItem(Long itemId) {
		// if itemId is known, this is easy. but check to make sure it's
		// actually this page, to prevent the user gaming us

		if (itemId == null || itemId == -1)
			itemId = currentPageItemId;

		if (itemId != null && itemId != -1) {
			SimplePageItem ret = findItem(itemId);
			if (ret != null
					&& (ret.getSakaiId().equals(
							Long.toString(getCurrentPageId())) || ret.getType() == SimplePageItem.STUDENT_CONTENT)) {
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

		SimplePageItem ret = simplePageToolDao
				.findTopLevelPageItemBySakaiId(Long
						.toString(getCurrentPageId()));

		if (ret == null && page.getOwner() != null) {
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

	// called at the start of showpageproducer, with page info for the page
	// about to be displayed
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
	public String adjustPath(String op, Long pageId, Long pageItemId,
			String title) {
		List<PathEntry> path = (List<PathEntry>) sessionManager
				.getCurrentToolSession().getAttribute(LESSONBUILDER_PATH);

		// if no current path, op doesn't matter. we can just do the current
		// page
		if (path == null || path.size() == 0) {
			PathEntry entry = new PathEntry();
			entry.pageId = pageId;
			entry.pageItemId = pageItemId;
			entry.title = title;
			path = new ArrayList<PathEntry>();
			path.add(entry);
		} else if (path.get(path.size() - 1).pageId.equals(pageId)) {
			// nothing. we're already there. this is to prevent
			// oddities if we refresh the page
		} else if (op == null || op.equals("") || op.equals("next")) {
			PathEntry entry = path.get(path.size() - 1); // overwrite last item
			entry.pageId = pageId;
			entry.pageItemId = pageItemId;
			entry.title = title;
		} else if (op.equals("push")) {
			// a subpage
			PathEntry entry = new PathEntry();
			entry.pageId = pageId;
			entry.pageItemId = pageItemId;
			entry.title = title;
			path.add(entry); // put it on the end
		} else if (op.equals("pop")) {
			// a subpage
			path.remove(path.size() - 1);
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
					for (String s : items) {
						// don't see how this could happen, but it did
						if (s.trim().equals("")) {
							log.warn("adjustPath attempt to set invalid path: invalid item: "
									+ op + ":" + logEntry.getPath());
							return null;
						}
						SimplePageItem i = findItem(Long.valueOf(s));
						if (i == null || i.getType() != SimplePageItem.PAGE) {
							log.warn("adjustPath attempt to set invalid path: invalid item: "
									+ op);
							return null;
						}
						SimplePage p = getPage(Long.valueOf(i.getSakaiId()));
						if (p == null
								|| !currentPage.getSiteId().equals(
										p.getSiteId())) {
							log.warn("adjustPath attempt to set invalid path: invalid page: "
									+ op);
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
				if (index < (path.size() - 1))
					path.subList(index + 1, path.size()).clear();
			}
		}

		// have new path; set it in session variable
		sessionManager.getCurrentToolSession().setAttribute(LESSONBUILDER_PATH,
				path);

		// and make string representation to return
		String ret = null;
		for (PathEntry entry : path) {
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
	public void adjustBackPath(String op, Long pageId, Long pageItemId,
			String title) {

		List<PathEntry> backPath = (List<PathEntry>) sessionManager
				.getCurrentToolSession().getAttribute(LESSONBUILDER_BACKPATH);
		if (backPath == null)
			backPath = new ArrayList<PathEntry>();

		// default case going directly to something.
		// normally we want to push it, but if it's already there,
		// we're going back to it, use the old one
		if (op == null || op.equals("")) {
			// is it there already? Some would argue that we should use the
			// first occurrence
			int lastEntry = -1;
			int i = 0;
			long itemId = pageItemId; // to avoid having to use equals
			for (PathEntry entry : backPath) {
				if (entry.pageItemId == itemId)
					lastEntry = i;
				i++;
			}
			if (lastEntry >= 0) {
				// yes, back up to that entry
				if (lastEntry < (backPath.size() - 1))
					backPath.subList(lastEntry + 1, backPath.size()).clear();
				return;
			}
			// no fall through and push the new item
		}

		if (op.equals("pop")) {
			if (backPath.size() > 0)
				backPath.remove(backPath.size() - 1);
		} else { // push or no operation
			PathEntry entry = new PathEntry();
			entry.pageId = pageId;
			entry.pageItemId = pageItemId;
			entry.title = title;
			backPath.add(entry);
		}

		// have new path; set it in session variable
		sessionManager.getCurrentToolSession().setAttribute(
				LESSONBUILDER_BACKPATH, backPath);
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

	// called from "select page" dialog in Reorder to insert items from anoher
	// page

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return 
	 */
	public String selectPage() {

		if (!canEditPage())
			return "permission-failed";

		ToolSession toolSession = sessionManager.getCurrentToolSession();
		toolSession.setAttribute("lessonbuilder.selectedpage", selectedEntity);

		// doesn't do anything but call back reorder
		// the submit sets selectedEntity, which is passed to Reorder by
		// addResultingViewBinding

		return "selectpage";
	}

	/**
	 * Crea un nuevo objeto.
	 * @return respuesta el resultado de la operaci&oacuten.
	 */
	public String createnuevoObjetivo() {
		String respuesta = "failure";
		if (subpageGoal != null) {
			objetivoCurso = subpageGoal;
			for (int i = 0; i < objetivosSeleccionados.length; i++) {
				objetivoCurso += "," + objetivosSeleccionados[i];
			}

			addObjetivoCurso();
			// addObjetivoTema();
			respuesta = "success";
		} else if (subpageGoalUnidad != null) {
			objetivoCurso = subpageGoalUnidad;
			for (int i = 0; i < objetivosSeleccionados.length; i++) {
				objetivoCurso += "," + objetivosSeleccionados[i];
			}
			//addObjetivoCurso();
			addObjetivoActividad(3);
			respuesta = "successU";
		} else if (subpageGoalActividad != null) {
			objetivoCurso = subpageGoalActividad;
			for (int i = 0; i < objetivosSeleccionados.length; i++) {
				objetivoCurso += "," + objetivosSeleccionados[i];
			}
			// addObjetivoCurso();
			addObjetivoActividad(3);
			respuesta = "successActividad";
		}

		return respuesta;
	}

	/**
	 * Crea una nueva palabra.
	 * @return respuesta el resultado de la operaci&oacuten.
	 */
	public String createnuevaPalabra() {
		String respuesta = "failure";
		addPalabraCurso();
		respuesta = "success";

		return respuesta;
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
	 * Atributo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean inherited = false;

	/**
	 * Atributo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	private boolean hechaH;

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
	 * @param i
	 * @param groups
	 * @return ret
	 */
	public List<String> setLBItemGroups(SimplePageItem i, String[] groups) {

		List<String> ret = null;
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
		Set<String> ret = new HashSet<String>();
		if (groups == null)
			return ret;
		for (Group group : groups)
			ret.add(group.getId());
		myGroups = ret;
		return ret;
	}

	// sort the list, since it will typically be presented
	// to the user
	/**
	 * Retorna los grupos actuales.
	 * @return currentGroups
	 */
	public List<GroupEntry> getCurrentGroups() {
		if (currentGroups != null)
			return currentGroups;

		Site site = getCurrentSite();
		Collection<Group> groups = site.getGroups();
		List<GroupEntry> groupEntries = new ArrayList<GroupEntry>();
		for (Group g : groups) {
			GroupEntry e = new GroupEntry();
			e.name = g.getTitle();
			e.id = g.getId();
			groupEntries.add(e);
		}

		Collections.sort(groupEntries, new Comparator() {
			public int compare(Object o1, Object o2) {
				GroupEntry e1 = (GroupEntry) o1;
				GroupEntry e2 = (GroupEntry) o2;
				return e1.name.compareTo(e2.name);
			}
		});
		currentGroups = groupEntries;
		return groupEntries;
	}

	/**
	 * Asigna la p&aacutegina
	 * @param pageId el identificador de la p&aacutegina.
	 */
	public void setPage(long pageId) {
		sessionManager.getCurrentToolSession().setAttribute(
				"current-pagetool-page", pageId);
		currentPageId = null;
	}

	/**
	 * Retorna la altura del item.
	 * @return r
	 */
	public String getHeight() {
		String r = "";
		if (itemId != null && itemId > 0) {
			r = findItem(itemId).getHeight();
		}
		return (r == null ? "" : r);
	}
	/**
	 * Retorna el ancho del item.
	 * @return r
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

	// page is complete, update gradebook entry if any
	// note that if the user has never gone to a page, the gradebook item will
	// be missing.
	// if they gone to it but it's not complete, it will be 0. We can't
	// explicitly set
	// a missing value, and this is the only way things will work if someone
	// completes a page
	// and something changes so it is no longer complete.
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return 
	 */
	public void trackComplete(SimplePageItem item, boolean complete) {
		SimplePage page = getCurrentPage();
		if (page.getGradebookPoints() != null)
			gradebookIfc.updateExternalAssessmentScore(getCurrentSiteId(),
					"lesson-builder:" + page.getPageId(), getCurrentUserId(),
					complete ? Double.toString(page.getGradebookPoints())
							: "0.0");
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return 
	 */
	public SimplePageLogEntry getLogEntry(long itemId) {
		return getLogEntry(itemId, null);
	}
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return entry
	 */
	public SimplePageLogEntry getLogEntry(long itemId, Long studentPageId) {
		if (studentPageId == null) {
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
	 * @return 
	 */
	public boolean hasLogEntry(long itemId) {
		return (getLogEntry(itemId) != null);
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
			LessonEntity assignment = assignmentEntity
					.getEntity(i.getSakaiId());
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

	// we allow both ? and &. The key may be the value of something like ?v=, so
	// we don't know
	// whether the next thing is & or ?. To be safe, use & except for the first
	// param, which
	// uses ?. Note that RSF will turn & into &amp; in the src= attribute. THis
	// appears to be correct,
	// as HTML is an SGML dialect.
	// If you run into trouble with &amp;, you can use ; in the following.
	// Google seems to
	// process it correctly. ; is a little-known alterantive to & that the RFCs
	// do permit
	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
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
		// see if it has a Youtube ID
		int offset = 0;
		if (URL.startsWith("http:"))
			offset = 5;
		else if (URL.startsWith("https:"))
			offset = 6;

		if (URL.startsWith("//www.youtube.com/", offset)
				|| URL.startsWith("//youtube.com/", offset)) {
			Matcher match = YOUTUBE_PATTERN.matcher(URL);
			if (match.find()) {
				return normalizeParams(match.group(1));
			}
			match = YOUTUBE2_PATTERN.matcher(URL);
			if (match.find()) {
				return normalizeParams(match.group(1));
			}
		} else if (URL.startsWith("//youtu.be/", offset)) {
			Matcher match = SHORT_YOUTUBE_PATTERN.matcher(URL);
			if (match.find()) {
				return normalizeParams(match.group(1));
			}
		}
		return null;
	}

	/*
	 * return 11-char youtube ID for a URL, or null if it doesn't match we store
	 * URLs as content objects, so we have to retrieve the object in order to
	 * check. The actual URL is stored as the contents of the entity
	 */

	/**
	 * M&eacutetodo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 * @return 
	 */
	public String getYoutubeKey(SimplePageItem i) {
		String sakaiId = i.getSakaiId();

		SecurityAdvisor advisor = null;
		try {
			if (getCurrentPage().getOwner() != null) {
				// Need to allow access into owner's home directory
				advisor = new SecurityAdvisor() {
					public SecurityAdvice isAllowed(String userId,
							String function, String reference) {
						if ("content.read".equals(function)
								|| "content.hidden".equals(function)) {
							return SecurityAdvice.ALLOWED;
						} else {
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

			// make sure it's a URL
			if (resource == null ||
					// copying resources does not preserve this field, so if we do this
					// test, things won't
					// work in copied sites
					// !resource.getResourceType().equals("org.sakaiproject.content.types.urlResource")
					// ||
					!resource.getContentType().equals("text/url")) {
				return null;
			}

			// get the actual URL
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

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (advisor != null)
				securityService.popAdvisor();
		}

		// no
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
	 * 
	 * @param item
	 * @param shouldHaveAccess
	 * @param canRecurse
	 *            Is it allowed to delete the row in the table for the group and
	 *            recurse to try again. true for normal calls; false if called
	 *            inside this code to avoid infinite loop
	 */
	/**
	 * Atributo que ven&iacutea con el m&oacutedulo lessonbuilder de Sakai.
	 */
	public static final int MAXIMUM_ATTEMPTS_FOR_UNIQUENESS = 100;

	// called by dialog to add inline multimedia item, or update existing
	// item if itemid is specified
	/**
	 * Agrega una actividad.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addActividad() {

		String respuesta = "failed";

		if (tipoUnidad == 0) {
			getTiposActividad();
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
					DateFormat.SHORT, new ResourceLoader().getLocale());
			df.setTimeZone(TimeService.getLocalTimeZone());
			if (!itemOk(itemId))
				return "permission-failed";
			if (!canEditPage())
				return "permission-failed";

			// if (selectedActividad == null) {
			// return "failure";
			// } else {
			try {
				// LessonEntity selectedObject =
				// actividadEntity.getEntity(selectedActividad);
				// if (selectedObject == null)
				// return "failure";

				SimplePageItem i;

				// buscar el item de recursiva
				List<String> elist = new ArrayList<String>();
				Item item = ashyiToolDao.getItemAshyi("Actividad Atomica");
				Tipo tipoS = null;
				for (int j = 0; j < tipoActividad.size(); j++) {
					if (tipoActividad.get(j).getNombre().equals(tipoA)
							&& (!tipoA.equals(cadenaOpciones))) {
						tipoS = tipoActividad.get(j);
					}
				}

				// 4 --> actividad
				Actividad ac = appendActividad(tipoS, item, nomberA,
						descripcionA, 4, Integer.valueOf(dedicacionActividad),
						nivelActividades);
				/*
				 * if (opcionIniciaFinal.equals("Actividad Inicial")) {
				 * ac.setEs_inicial(true); ac.setEs_final(false);
				 * ac.setEs_refuerzo(false); } else if
				 * (opcionIniciaFinal.equals("Actividad Final")) {
				 * ac.setEs_inicial(false); ac.setEs_final(true);
				 * ac.setEs_refuerzo(false); } else if
				 * (opcionIniciaFinal.equals("Actividad Intermedia")) {
				 * ac.setEs_inicial(false); ac.setEs_final(false);
				 * ac.setEs_refuerzo(false); }
				 */
				// almacenar actividad
				saveActividad(ac);
				if (opcionIniciaFinal.equals("Actividad Refuerzo")) {
					// ac.setEs_inicial(false);
					// ac.setEs_final(false);
					ac.setEs_refuerzo(true);
					update(ac);
					updateObjetivosActividad(ac, 3);					
				}
				else
				{
					// Al,acenar objetivos de actividad 3: nivel de recursividad
					// recursiva unidad didactica
					addObjetivosActividad(ac, 3);
				}				
				// Agregar caracteristicas a actividad
				addCaracteristicasActividad(ac);

				// Almacenar recurso de actividad --> 2 actividad atomica
				addRecursoActividad(ac, 2);
				// Agregar Actividad a Actividadde Mayor Nivel (unidad
				// didactica) 3: nivel de recursividad recursivo
				String nombreUnidad = getCurrentPageItem(getItemId()).getName();
				addActividadToActividadMayorNivel(ac, nombreUnidad, 3);


				// ac.setCaracteristicas(caracteristicas);
				// updateActividad(ac);
				// saveActividad(ac, caracteristicas);
				// }
				if (!tipoA.equals(cadenaOpciones)) {
					// if( tipoA.equals("Quiz") || tipoA.equals("Parcial"))
					// respuesta = "succesExamen";
					// else if( tipoA.equals("Video") )
					// respuesta = "succesMultimedia";
					// else if( tipoA.equals("Tarea") )
					// respuesta = "succesTarea";
					// else if( tipoA.equals("Foro") )
					// respuesta = "succesForo";
					// else
					// respuesta = "succesRecurso";
					respuesta = "succesRecurso";
				}
				// return "success";
			} catch (Exception ex) {
				ex.printStackTrace();
				respuesta = "failure";
			} finally {
				selectedActividad = null;
			}

		} else if (tipoUnidad == 2) // unidades d
		{
			// buscar el item de recursiva
			List<String> elist = new ArrayList<String>();
			Item i = ashyiToolDao.getItemAshyi("Actividad Recursiva");
			Actividad ac = appendActividad(i, nomberA, 3, subpageDedicacion,
					nivelActividades);
			// almacenar actividad
			saveActividad(ac);
			// Al,acenar objetivos de actividad 2: nivel de recursividad tema
			addObjetivosActividad(ac, 2);
			// Agregar Actividad a Actividadde Mayor Nivel (curso) 2: nivel de
			// recursividad macro
			// addActividadToActividadMayorNivel(ac,
			// getCurrentPageItem(getItemId()).getName(),2);
			addActividadToActividadMayorNivel(ac, temaTitle, 2);
			//
			respuesta = "succes";
		} else if (tipoUnidad == 1) // temas
		{
			// buscar el item de recursiva
			List<String> elist = new ArrayList<String>();
			Item i = ashyiToolDao.getItemAshyi("Actividad Recursiva");
			// 2 --> tema
			subpageDedicacion = "100";
			Actividad ac = appendActividad(i, nomberA, 2, subpageDedicacion,
					nivelActividades);
			// almacenar actividad
			saveActividad(ac);
			// Al,acenar objetivos de actividad 1: nivel de recursividad curso

			addObjetivosActividad(getCurrentSite().getTitle(), 1, ac);
			// Agregar Actividad a Actividadde Mayor Nivel (curso) 1: nivel de
			// recursividad curso
			addActividadToActividadMayorNivel(ac, getCurrentSite().getTitle(),
					1);
			//
			respuesta = "succes";
		}
		// }
		return respuesta;
	}

	/**
	 * A&ntildeade objetivos a una actividad.
	 * @param nombre de la actividad.
	 * @param nR nivel de recursividad.
	 * @param ac actividad.
	 */
	private void addObjetivosActividad(String nombre, int nR, Actividad ac) {

		//		int id = getUltimaActividad(ac.getNombre());

		String err = null;

		//		ac.setIdActividad(id);

		String[] objs = ashyiToolDao.getObjetivosActividad(nR, nombre);

		for (String obj : objs) {
			Objetivo obActividad = new ObjetivoImpl(obj);
			obActividad.setIdObjetivo(ashyiToolDao
					.getObjetivoActividad(obj, nR, getCurrentSite().getTitle()));
			ObjetivosActividadImpl objetivoAc = new ObjetivosActividadImpl(1,ac,
					obActividad);
			saveCaracteristica(objetivoAc, true);
		}
	}

	/**
	 * Agrega una actividad a otra de mayor nivel.
	 * @param ac la actividad.
	 * @param nombre el nombre de la actividad.
	 * @param nR el nivel de recursividad.
	 */
	public void addActividadToActividadMayorNivel(Actividad ac, String nombre,
			int nR) {

		//		int id = getUltimaActividad(ac.getNombre());
		//
		//		ac.setIdActividad(id);

		Actividad acPadre = ashyiToolDao.getactividad(nombre, nR);

		ActividadTieneActividad acHija = new ActividadTieneActividadImpl(
				acPadre, ac);

		List<String> elist = new ArrayList<String>();

		ashyiToolDao.saveCaracteristica(acHija, elist,
				messageLocator.getMessage("simplepage.nowrite"), true);
	}

	/**
	 * Retorna el identificador de la &uacuteltima actividad creada.
	 * @return el identificador.
	 */
	public int getUltimaActividad() {
		return ashyiToolDao.getUltimaActividad();
	}
	/**
	 * Retorna el identificador de la &uacuteltima actividad creada.
	 * @param el nombre de la actividad.
	 * @return el identificador.
	 */
	public int getUltimaActividad(String nombre) {
		return ashyiToolDao.getUltimaActividad(nombre);
	}
	/**
	 * Retorna el identificador del &uacuteltimo recurso creado.
	 * @return el recurso.
	 */
	public Recurso getUltimoRecurso() {
		return ashyiToolDao.getUltimoRecurso();
	}

	/**
	 * Actualiza los objetivos de una actividad.
	 * @param ac la actividad.
	 * @param nR el nivel de recursividad.
	 */
	public void updateObjetivosActividad(Actividad ac, int nR) {
		//		int id = getUltimaActividad(ac.getNombre());

		String err = null;

		//		ac.setIdActividad(id);

		try {
			for (int i = 0; i < objetivosSeleccionados.length; i++) {
				Objetivo obj = new ObjetivoImpl();
				obj.setIdObjetivo(ashyiToolDao.getObjetivoActividad(
						objetivosSeleccionados[i], nR, getCurrentPage().getTitle()));
				obj.setNombre(objetivosSeleccionados[i]);
				ObjetivosActividadImpl objetivoAc = new ObjetivosActividadImpl(1,ac, obj);
				//almacenar objetivo en actividad de refuerzo
				//saveCaracteristica(objetivoAc, true);

				//eliminar relaci&oacuten entre objetivo y unidad
				Actividad unidad = ashyiToolDao.getactividad(getCurrentPage().getTitle(), 3);
				List<ObjetivosActividad> objUnidad = ashyiToolDao.getObjetivosActividad(unidad.getIdActividad());

				for(ObjetivosActividad objU : objUnidad)
					if(objU.getIdObjetivo().getIdObjetivo() == objetivoAc.getIdObjetivo().getIdObjetivo())
					{
						ashyiToolDao.deleteObject(objU);
						break;
					}

				addObjetivoActividad(ac, obj);
			}

		} catch (Throwable t) {
			// this is probably a bogus error, but find its root cause
			while (t.getCause() != null) {
				t = t.getCause();
			}
			err = t.toString();
		}
	}

	/**
	 * A&ntildeade objetivos de actividad.
	 * @param ac la actividad.
	 * @param nR el nivel de recursividad.
	 */
	public void addObjetivosActividad(Actividad ac, int nR) {
		//		int id = getUltimaActividad(ac.getNombre());

		String err = null;

		//		ac.setIdActividad(id);

		try {

			for (int i = 0; i < objetivosSeleccionados.length; i++) {
				Objetivo obj = new ObjetivoImpl();
				obj.setIdObjetivo(ashyiToolDao.getObjetivoActividad(
						objetivosSeleccionados[i], nR, getCurrentPage().getTitle()));
				obj.setNombre(objetivosSeleccionados[i]);
				ObjetivosActividadImpl objetivoAc = new ObjetivosActividadImpl(2,
						ac, obj);

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


	/**
	 * Agrega una nueva actividad.
	 * @param type
	 * @param item
	 * @param name
	 * @param description
	 * @param nR
	 * @param dedicacion
	 * @return resultado de la operaci&oacuten.
	 */
	public String addActividad(Tipo type, Item item, String name,
			String description, int nR, int dedicacion) {
		try {
			Actividad ac = appendActividad(type, item, name, description, nR,
					dedicacion, nivelActividades);
			boolean ret = saveActividad(ac);
			if (ret)
				return "success";
			return "failure";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "failure";
		} finally {
			selectedActividad = null;
		}
		// }
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
		ItemPlan ip = null;
		try {
			ip = appendItemPlan(ud, ac, recurso, estaActivo, orden);
			boolean ret = saveItemPlan(ip);
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
	 * Agrega un itemPlan.
	 * @param ud
	 * @param ac
	 * @param estaActivo
	 * @param orden
	 * @return ip el itemPlan
	 */
	public ItemPlan addItemPlan(Actividad ud, Actividad ac, boolean estaActivo, int orden) {
		ItemPlan ip = null;
		try {
			ip = appendItemPlan(ud, ac, estaActivo, orden);
			boolean ret = saveItemPlan(ip);
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
	 * Agrega las caracter&iacutesticas de una actividad.
	 * @param ac la actividad.
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

		for (String habilidad : getHabilidadesSeleccionadas()) {
			if (!habilidad.equals(cadenaOpciones)) {
				caracteristicaNueva = new CaracteristicaActividadImpl(ac,
						getCaracteristicaHabilidad(habilidad), true, false, 0);
				caracteristicas.add(caracteristicaNueva);

				try {
					ashyiToolDao
					.saveCaracteristicasActividades(caracteristicaNueva);

				} catch (Throwable t) {
					// this is probably a bogus error, but find its root cause
					while (t.getCause() != null) {
						t = t.getCause();
					}
					err = t.toString();
				}
			}
		}

		for (String compentencias : getCompetenciasSeleccionadas()) {
			if (!compentencias.equals(cadenaOpciones)) {
				caracteristicaNueva = new CaracteristicaActividadImpl(ac,
						getCaracteristicaCompetencia(compentencias), true,
						false, 0);
				caracteristicas.add(caracteristicaNueva);

				try {
					ashyiToolDao
					.saveCaracteristicasActividades(caracteristicaNueva);

				} catch (Throwable t) {
					// this is probably a bogus error, but find its root cause
					while (t.getCause() != null) {
						t = t.getCause();
					}
					err = t.toString();
				}
			}
		}

		if (!getCaracteristicaSA(saS).getNombre().equals(cadenaOpciones)) {
			caracteristicaNueva = new CaracteristicaActividadImpl(ac,
					getCaracteristicaSA(saS), false, false, 0);
			caracteristicas.add(caracteristicaNueva);

			try {
				ashyiToolDao
				.saveCaracteristicasActividades(caracteristicaNueva);

			} catch (Throwable t) {
				// this is probably a bogus error, but find its root cause
				while (t.getCause() != null) {
					t = t.getCause();
				}
				err = t.toString();
			}
		}

		if (!getCaracteristicaContexto(contextoS).getNombre().equals(
				cadenaOpciones)) {
			caracteristicaNueva = new CaracteristicaActividadImpl(ac,
					getCaracteristicaContexto(contextoS), false, false, 0);
			caracteristicas.add(caracteristicaNueva);

			try {
				ashyiToolDao
				.saveCaracteristicasActividades(caracteristicaNueva);

			} catch (Throwable t) {
				// this is probably a bogus error, but find its root cause
				while (t.getCause() != null) {
					t = t.getCause();
				}
				err = t.toString();
			}
		}

		for (String habilidadPC : getHabilidadesPCSeleccionadas()) {
			if (!habilidadPC.equals(cadenaOpciones)) {
				caracteristicaNueva = new CaracteristicaActividadImpl(ac,
						getCaracteristicaHabilidad(habilidadPC), false, true, 0);
				caracteristicas.add(caracteristicaNueva);

				try {
					ashyiToolDao
					.saveCaracteristicasActividades(caracteristicaNueva);

				} catch (Throwable t) {
					// this is probably a bogus error, but find its root cause
					while (t.getCause() != null) {
						t = t.getCause();
					}
					err = t.toString();
				}
			}
		}

		for (String competenciaPC : getCompetenciasPCSeleccionadas()) {
			if (!competenciaPC.equals(cadenaOpciones)) {
				caracteristicaNueva = new CaracteristicaActividadImpl(ac,
						getCaracteristicaCompetencia(competenciaPC), false,
						true, 0);
				caracteristicas.add(caracteristicaNueva);

				try {
					ashyiToolDao
					.saveCaracteristicasActividades(caracteristicaNueva);

				} catch (Throwable t) {
					// this is probably a bogus error, but find its root cause
					while (t.getCause() != null) {
						t = t.getCause();
					}
					err = t.toString();
				}
			}
		}

		// for(CaracteristicaActividad c : caracteristicas)
		// {
		// c.setIdActividad(ac);
		// // ac.getCaracteristicas().add(c);
		// }

		// String err = null;
		// try {
		// ashyiToolDao.saveCaracteristicasActividades(caracteristicas);
		//
		// } catch (Throwable t) {
		// // this is probably a bogus error, but find its root cause
		// while (t.getCause() != null) {
		// t = t.getCause();
		// }
		// err = t.toString();
		// }

	}

	/**
	 * Obtiene una situaci&oacuten de aprendizaje.
	 * @param saBuscar
	 * @return la situaci&oacuten de aprendizaje.
	 */
	public CaracteristicaImpl getCaracteristicaSA(String saBuscar) {
		String nombreC = cadenaOpciones, TestC = null;
		int tipoS = 0;
		Item it = null;

		for (int j = 0; j < getsA().size(); j++) {
			if (getsA().get(j).getNombre().equals(saBuscar)) {
				tipoS = getsA().get(j).getIdCaracteristica();
				it = getsA().get(j).getIdItem();
				nombreC = getsA().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS, it, nombreC);
	}

	/**
	 * Obtiene una personalidad.
	 * @param personalidadBuscar
	 * @return la personalidad.
	 */
	public CaracteristicaImpl getCaracteristicaPersonalidad(
			String personalidadBuscar) {
		String nombreC = cadenaOpciones, TestC = null;
		int tipoS = 0;
		Item it = null;

		for (int j = 0; j < getPersonalidad().size(); j++) {
			if (getPersonalidad().get(j).getNombre().equals(personalidadBuscar)) {
				tipoS = getPersonalidad().get(j).getIdCaracteristica();
				it = getPersonalidad().get(j).getIdItem();
				nombreC = getPersonalidad().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS, it, nombreC);
	}

	// public TipoImpl getTipoCambio()
	// {
	// this.setTipo(ashyiToolDao.getTipos());
	// String nombreC = cadenaOpciones;
	// int tipoS=0;
	// for(int j = 0; j<getTipo().size();j++)
	// {
	// if(getTipo().get(j).getNombre().equals(tipoSeleccionado))
	// {
	// tipoS = getTipo().get(j).getIdTipo();
	// nombreC = getTipo().get(j).getNombre();
	// }
	// }
	//
	// return new TipoImpl(tipoS,nombreC);
	// }

	/**
	 * Obtiene un contexto.
	 * @param contextoBuscar
	 * @return el contexto.
	 */
	public Caracteristica getCaracteristicaContexto(String contextoBuscar) {
		String nombreC = cadenaOpciones;
		int tipoS = 0;
		Item it = null;

		for (int j = 0; j < getContexto().size(); j++) {
			if (getContexto().get(j).getNombre().equals(contextoBuscar)) {
				tipoS = getContexto().get(j).getIdCaracteristica();
				it = getContexto().get(j).getIdItem();
				nombreC = getContexto().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS, it, nombreC);
	}

	/**
	 * Obtiene un estilo de aprendizaje.
	 * @param estiloBuscar
	 * @return el estilo.
	 */
	public CaracteristicaImpl getCaracteristicaEstilo(String estiloBuscar) {
		String nombreC = cadenaOpciones, TestC = null;
		int tipoS = 0;
		Item it = null;

		for (int j = 0; j < getEstilo().size(); j++) {
			if (getEstilo().get(j).getNombre().equals(estiloBuscar)) {
				tipoS = getEstilo().get(j).getIdCaracteristica();
				it = getEstilo().get(j).getIdItem();
				nombreC = getEstilo().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS, it, nombreC);
	}

	/**
	 * Obtiene una habilidad.
	 * @param habilidadBuscar
	 * @return la habilidad.
	 */
	public CaracteristicaImpl getCaracteristicaHabilidad(String habilidadBuscar) {
		String nombreC = cadenaOpciones, TestC = null;
		int tipoS = 0;
		Item it = null;

		for (int j = 0; j < getHabilidad().size(); j++) {
			if (getHabilidad().get(j).getNombre().equals(habilidadBuscar)) {
				tipoS = getHabilidad().get(j).getIdCaracteristica();
				it = getHabilidad().get(j).getIdItem();
				nombreC = getHabilidad().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS, it, nombreC);
	}

	/**
	 * Obtiene una competencia.
	 * @param competenciaBuscar
	 * @return la competencia.
	 */
	public CaracteristicaImpl getCaracteristicaCompetencia(
			String competenciaBuscar) {
		String nombreC = cadenaOpciones, TestC = null;
		int tipoS = 0;
		Item it = null;

		for (int j = 0; j < getCompetencia().size(); j++) {
			if (getCompetencia().get(j).getNombre().equals(competenciaBuscar)) {
				tipoS = getCompetencia().get(j).getIdCaracteristica();
				it = getCompetencia().get(j).getIdItem();
				nombreC = getCompetencia().get(j).getNombre();
			}
		}

		return new CaracteristicaImpl(tipoS, it, nombreC);
	}

	/**
	 * Retorna el valor de un permiso.
	 * @return permiso.
	 */
	public int puedeEditarDominioAshyi() {

		int permiso = ashyiToolDao.getEditarDominioAshyi();
		return permiso;
		// return getDominioAshyi();
	}

	/**
	 * Retorna los tipos de contexto.
	 * @return listNombres los tipos. 
	 */
	public String[] getTiposContexto() {
		this.setTipoContexto(ashyiToolDao.getTiposContexto());

		String[] listNombres = new String[getTipoContexto().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getTipoContexto().size() > 0) {
			int i = 0;
			for (i = 1; i < getTipoContexto().size() + 1; i++) {
				Item c = getTipoContexto().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}

	/**
	 * Retorna los tipos de tipos.
	 * @return listNombres los tipos.
	 */
	public String[] getTipoTipos() {
		// tiposTipo.add(new TipoImpl(1, "Actividad"));
		// tiposTipo.add(new TipoImpl(2, "Caracteristica"));
		// tiposTipo.add(new TipoImpl(3, "Contexto"));

		String[] listNombres = new String[getTiposTipo().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getTiposTipo().size() > 0) {
			int i = 0;
			for (i = 1; i < getTiposTipo().size() + 1; i++) {
				Tipo c = getTiposTipo().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}

	/**
	 * Retorna los items.
	 * @return listNombres los items.
	 */
	public String[] getItemsCaracteristica() {
		this.setItemsCaracterisiticas(ashyiToolDao.getTiposCaracteristicas());

		String[] listNombres = new String[getItemsCaracterisiticas().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getItemsCaracterisiticas().size() > 0) {
			int i = 0;
			for (i = 1; i < getItemsCaracterisiticas().size() + 1; i++) {
				Item c = getItemsCaracterisiticas().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los tipos.
	 * @return listNombres los tipos.
	 */
	public String[] getTiposCaracteristica() {
		this.setTipoCaracteristicas(ashyiToolDao.getTiposCaracteristicas());

		String[] listNombres = new String[getTipoCaracteristicas().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getTipoCaracteristicas().size() > 0) {
			int i = 0;
			for (i = 1; i < getTipoCaracteristicas().size() + 1; i++) {
				Item c = getTipoCaracteristicas().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}

	/**
	 * Retorna los tipos de actividad.
	 * @return listNombres los tipos.
	 */
	public String[] getTiposActividad() {
		this.setTipoActividad(ashyiToolDao.getTiposActividad());

		String[] listNombres = new String[getTipoActividad().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getTipoActividad().size() > 0) {
			int i = 0;
			for (i = 1; i < getTipoActividad().size() + 1; i++) {
				Tipo c = getTipoActividad().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los contextos.
	 * @return listNombres los contextos.
	 */
	public String[] getContextos() {
		this.setContexto(ashyiToolDao.getContextos());

		String[] listNombres = new String[getContexto().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getContexto().size() > 0) {
			int i = 0;
			for (i = 1; i < getContexto().size() + 1; i++) {
				Caracteristica c = getContexto().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna las habilidades.
	 * @return listNombres las habilidades.
	 */
	public String[] getHabilidades() {
		this.setHabilidad(ashyiToolDao.getHabilidades());

		String[] listNombres = new String[getHabilidad().size() + 1];
		listNombres[0] = cadenaOpciones;
		if (getHabilidad().size() > 0) {
			int i = 0;
			for (i = 1; i < getHabilidad().size() + 1; i++)
				// for(i = 0; i< getHabilidad().size() ;i++)
			{
				Caracteristica c = getHabilidad().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna las competencias.
	 * @return listNombres las competencias.
	 */
	public String[] getCompetencias() {
		this.setCompetencia(ashyiToolDao.getCompetencias());

		String[] listNombres = new String[getCompetencia().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getCompetencia().size() > 0) {
			int i = 0;
			for (i = 1; i < getCompetencia().size() + 1; i++)
				// for(i = 0; i< getCompetencia().size() ;i++)
			{
				Caracteristica c = getCompetencia().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna las personalidades.
	 * @return listNombres las personalidades.
	 */
	public String[] getPersonalidades() {
		this.setPersonalidad(ashyiToolDao.getPersonalidades());

		String[] listNombres = new String[getPersonalidad().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getPersonalidad().size() > 0) {
			int i = 0;
			for (i = 1; i < getPersonalidad().size() + 1; i++) {
				Caracteristica c = getPersonalidad().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna las situaciones de aprendizaje.
	 * @return listNombres las situaciones de aprendizaje.
	 */
	public String[] getSA() {
		this.setsA(ashyiToolDao.getSA());

		String[] listNombres = new String[getsA().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getsA().size() > 0) {
			int i = 0;
			for (i = 1; i < getsA().size() + 1; i++) {
				Caracteristica c = getsA().get(i - 1);
				listNombres[i] = c.getNombre();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los estilos de aprendizaje.
	 * @return listNombres los estilos de aprendizaje.
	 */
	public String[] getEstilos() {
		this.setEstilo(ashyiToolDao.getEstilos());

		String[] listNombres = new String[getEstilo().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getEstilo().size() > 0) {
			for (int i = 1; i < getEstilo().size() + 1; i++) {
				Caracteristica c = getEstilo().get(i - 1);
				listNombres[i] = c.getNombre();
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
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.SHORT, new ResourceLoader().getLocale());
		df.setTimeZone(TimeService.getLocalTimeZone());
		if (!itemOk(itemId))
			return "permission-failed";
		if (!canEditPage())
			return "permission-failed";

		// if (selectedActividad == null) {
		// return "failure";
		// } else {
		try {
			// LessonEntity selectedObject =
			// actividadEntity.getEntity(selectedActividad);
			// if (selectedObject == null)
			// return "failure";

			SimplePageItem i;

			// int type, String name, String goal, String description, String
			// dI, String dF
			Item item = new ItemImpl();
			for (int j = 0; j < getTipoCaracteristicas().size(); j++) {
				if (getTipoCaracteristicas().get(j).getNombre().equals(tipoC)) {
					item = getTipoCaracteristicas().get(j);
				}
			}

			Caracteristica carac = appendCaracteristica(item, nombreC);
			// if (selectedObject.getDueDate() != null)
			// i.setDescription("(" +
			// messageLocator.getMessage("simplepage.due") + " " +
			// df.format(selectedObject.getDueDate()) + ")");
			// else
			// i.setDescription(null);
			// saveCarateristica(carac);
			// }
			return "success";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "failure";
		} finally {
			selectedActividad = null;
		}
		// }
	}

	// public String addTipo() {
	// getTipoTipos();
	// DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
	// DateFormat.SHORT, new ResourceLoader().getLocale());
	// df.setTimeZone(TimeService.getLocalTimeZone());
	// if (!itemOk(itemId))
	// return "permission-failed";
	// if (!canEditPage())
	// return "permission-failed";
	// try {
	//
	// SimplePageItem i;
	//
	// Item tipoS=null;
	// for(int j = 0; j<getTiposTipo().size();j++)
	// {
	// if(getTiposTipo().get(j).getNombre().equals(tipoTipo))
	// {
	// tipoS = getTiposTipo().get(j).getIdItem();
	// }
	// }
	//
	// Tipo tipo = new TipoImpl(tipoS, nombreTipo);
	// tipo = appendTipo(tipo);
	// return "success";
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return "failure";
	// } finally {
	// selectedActividad = null;
	// }
	// }

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
		Item i = ashyiToolDao.makeItemAshyi(nombreItem, testC);
		// long pageId, int type, String name, String goal, String description,
		// String dI, String dF

		// defaults to a fixed width and height, appropriate for some things,
		// but for an
		// image, leave it blank, since browser will then use the native size
		// clearImageSize(i);
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

	// public String editarTipo()
	// {
	// Tipo editarTipo = new TipoImpl();
	// GeneralViewParameters view = new GeneralViewParameters();
	// String respuesta = "";
	// if(!tipoSeleccionado.equals(cadenaOpciones))
	// {
	// List<NavigationCase> togo = new ArrayList<NavigationCase>();
	//
	// editarTipo=getTipoCambio();
	// view.viewID = CaracteristicaProducer.VIEW_ID;
	// view.setSendingPage(getCurrentPageId());
	// view.setItemId(Long.valueOf(editarTipo.getIdTipo()));
	//
	// togo.add(new NavigationCase("successTipo", view));
	//
	// respuesta = "successTipo";
	// }
	//
	// return respuesta;
	// }
	/**
	 * Retorna los tipos de cambio.
	 * @return tiposTipo los tipos de cambio.
	 */
	public Item getTipoCambio() {
		this.setTipo(ashyiToolDao.getItemsCaracterisiticas());
		String nombreC = cadenaOpciones;
		int tipoS = 0, tipoT = 0;
		for (int j = 0; j < getTipo().size(); j++) {
			if (getTipo().get(j).getNombre().equals(tipoSeleccionado)) {
				tipoS = getTipo().get(j).getIdItem();
				nombreC = getTipo().get(j).getNombre();
			}
		}

		return new ItemImpl(tipoS, nombreC, "");
	}

	/**
	 * Edita una habilidad.
	 * @return respuesta
	 */
	public Caracteristica editarHabilidad() {
		getHabilidades();
		Caracteristica respuesta = new CaracteristicaImpl();
		if (!habilidadS.equals(cadenaOpciones)) {
			for (int j = 0; j < getHabilidad().size(); j++) {
				if (getHabilidad().get(j).getNombre().equals(habilidadS)) {
					respuesta.setIdCaracteristica(getHabilidad().get(j)
							.getIdCaracteristica());
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
	public Caracteristica editarSas() {
		getSA();
		Caracteristica respuesta = new CaracteristicaImpl();
		if (!saS.equals(cadenaOpciones)) {
			for (int j = 0; j < getsA().size(); j++) {
				if (getsA().get(j).getNombre().equals(saS)) {
					respuesta.setIdCaracteristica(getsA().get(j)
							.getIdCaracteristica());
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
	public Caracteristica editarCompetencia() {
		getCompetencias();
		Caracteristica respuesta = new CaracteristicaImpl();
		if (!competenciaS.equals(cadenaOpciones)) {
			for (int j = 0; j < getCompetencia().size(); j++) {
				if (getCompetencia().get(j).getNombre().equals(competenciaS)) {
					respuesta.setIdCaracteristica(getCompetencia().get(j)
							.getIdCaracteristica());
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
	public Caracteristica editarPersonalidad() {
		getPersonalidades();
		Caracteristica respuesta = new CaracteristicaImpl();
		if (!personalidadS.equals(cadenaOpciones)) {
			for (int j = 0; j < getPersonalidad().size(); j++) {
				if (getPersonalidad().get(j).getNombre().equals(personalidadS)) {
					respuesta.setIdCaracteristica(getPersonalidad().get(j)
							.getIdCaracteristica());
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
	public Caracteristica editarEstilo() {
		getEstilos();
		Caracteristica respuesta = new CaracteristicaImpl();
		if (!estiloS.equals(cadenaOpciones)) {
			for (int j = 0; j < getEstilo().size(); j++) {
				if (getEstilo().get(j).getNombre().equals(estiloS)) {
					respuesta.setIdCaracteristica(getEstilo().get(j)
							.getIdCaracteristica());
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
	public Caracteristica editarContexto() {
		getContextos();
		Caracteristica respuesta = new CaracteristicaImpl();
		if (!contextoS.equals(cadenaOpciones)) {
			for (int j = 0; j < getContexto().size(); j++) {
				if (getContexto().get(j).getNombre().equals(contextoS)) {
					respuesta.setIdCaracteristica(getContexto().get(j)
							.getIdCaracteristica());
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
			// Tipo editarTipo = null;

			if (!habilidadesSeleccionadas[0].equals(cadenaOpciones)) {
				editar = getCaracteristicaHabilidad(habilidadesSeleccionadas[0]);
			}
			if (!estilosSeleccionados[0].equals(cadenaOpciones))
				editar = getCaracteristicaEstilo(estilosSeleccionados[0]);
			if (!saSeleccionadas[0].equals(cadenaOpciones))
				editar = getCaracteristicaSA(saSeleccionadas[0]);
			if (!competenciasSeleccionadas[0].equals(cadenaOpciones))
				editar = getCaracteristicaCompetencia(competenciasSeleccionadas[0]);
			if (!personalidadesSeleccionadas[0].equals(cadenaOpciones))
				editar = getCaracteristicaPersonalidad(personalidadesSeleccionadas[0]);
			if (!contextosSeleccionados[0].equals(cadenaOpciones)) {
				editarCx = getCaracteristicaContexto(contextosSeleccionados[0]);
				eliminarDominio(editarCx);
			}
			// else if(!tipoSeleccionado.equals(cadenaOpciones))
			// {
			// editarTipo=getTipoCambio();
			// eliminarDominio(editarTipo);
			// }
			else {
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
		ashyiToolDao.eliminar(editar);

	}

	/**
	 * Edita una caracter&iacutestica.
	 */
	public void editarCaracteristica() {
		List<Caracteristica> list = ashyiToolDao.getCaracteristica(Integer
				.valueOf(idC));
		Caracteristica c = list.get(0);

		c.setNombre(nombreC);

		getTiposCaracteristica();
		Item item = new ItemImpl();
		for (int j = 0; j < getTipoCaracteristicas().size(); j++) {
			if (getTipoCaracteristicas().get(j).getNombre().equals(tipoC)) {
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
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.SHORT, new ResourceLoader().getLocale());
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
		// }
	}

	/**
	 * Agrega un control.
	 * @param currentUserId2
	 * @param cambio
	 */
	private void appendControl(String currentUserId2, boolean cambio) {

		Control i = ashyiToolDao.makeControl(currentUserId2, cambio);
		// long pageId, int type, String name, String goal, String description,
		// String dI, String dF

		// defaults to a fixed width and height, appropriate for some things,
		// but for an
		// image, leave it blank, since browser will then use the native size
		// clearImageSize(i);

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
		List<String> elist = new ArrayList<String>();

		try {
			ashyiToolDao.saveCaracteristica(i, elist,
					messageLocator.getMessage("simplepage.nowrite"),
					requiresEditPermission);
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
			setErrMessage(messageLocator.getMessage("simplepage.savefailed")
					+ err);
			return false;
		}

		return true;
	}

	/**
	 * Retorna los tipos completos.
	 * @return listNombres los tipos.
	 */
	public String[] getTiposCompletos() {

		this.setTipo(ashyiToolDao.getItemsCaracterisiticas());

		String[] listNombres = new String[getTipo().size() + 1];
		listNombres[0] = cadenaOpciones;

		if (getTipo().size() > 0) {
			int i = 0;
			for (i = 1; i < getTipo().size() + 1; i++) {
				Item c = getTipo().get(i - 1);
				listNombres[i] = c.getNombre();
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
	public String registrarCurso() {
		String respuesta = "El curso no se ha registrado";

		respuesta = ashyiToolDao.registrarCurso(getCurrentSite().getTitle());

		// appendItem("Recursiva");
		// appendItem("AtÃ³mica");

		return respuesta;
	}
	/**
	 * Agrega un recurso de curso.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addRecursoCurso() {
		Actividad ac = ashyiToolDao
				.getactividad(getCurrentSite().getTitle(), 1);
		addRecursoActividad(ac, 1);

		return "success";
	}
	/**
	 * Agrega un objetivo de curso.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addObjetivoCurso() {
		Objetivo obj = new ObjetivoImpl();
		try {
			int id = 0;
			obj = ashyiToolDao.getObjetivo(objetivoCurso);
			if (obj.getNombre().equals("Does not exist")) {
				// almacenar objetivo
				obj.setNombre(objetivoCurso);
				saveCaracteristica(obj, true);
				// relacionar objetivo y actividad
				id = ashyiToolDao.getUltimoObjetivo(obj.getNombre());
				obj.setIdObjetivo(id);
			}
		} catch (Throwable t) {
			// this is probably a bogus error, but find its root cause
			while (t.getCause() != null) {
				t = t.getCause();
			}
		}
		Actividad ac = ashyiToolDao
				.getactividad(getCurrentSite().getTitle(), 1);
		addObjetivoActividad(ac, obj);

		return "success";
	}
	/**
	 * Agrega un objetivo de actividad.
	 * @param nR nivel de recursividad.
	 * @return resultado de la operaci&oacuten.
	 */
	public String addObjetivoActividad(int nR) {
		Objetivo obj = ashyiToolDao.getObjetivo(objetivoCurso);
		if (obj.getNombre().equals("Does not exist")) {
			// almacenar objetivo
			obj.setNombre(objetivoCurso);
			saveCaracteristica(obj, true);
			// relacionar objetivo y actividad
			int id = ashyiToolDao.getUltimoObjetivo(obj.getNombre());
			obj.setIdObjetivo(id);
		}
		if(!idActividad.equals(""))
		{
			String actividad = ashyiToolDao.getActividad(Integer.valueOf(idActividad)).getNombre();
			Actividad ac = ashyiToolDao.getactividad(
					//getCurrentPageItem(getItemId()).getName(), nR);
					actividad, nR);
			addObjetivoActividad(ac, obj);
		}

		return "success";
	}

	/**
	 * Agrega un objetivo de actividad.
	 * @param ac
	 * @param obj
	 */
	private void addObjetivoActividad(Actividad ac, Objetivo obj) {

		ObjetivosActividad objetivoNueva = new ObjetivosActividadImpl(1,ac, obj);
		saveCaracteristica(objetivoNueva, true);
	}

	/**
	 * Agrega un recurso de actividad.
	 * @param ac
	 * @param tipo
	 */
	private void addRecursoActividad(Actividad ac, int tipo) {

		Recurso recurso = new RecursoImpl("Does not exist", "No aplica", "");
		try {

			if (tipo == 1)// curso
			{
				if (tipoNaturaleza.equals("No aplica"))// recursos que no necesiten especificar
					if(tipoTipoAcceso.equals("No aplica"))//recursos que no necesiten especificar lincencia
						tipoTipoAcceso = "";
				recurso = new RecursoImpl(recursoCurso, tipoNaturaleza, tipoRecurso, tipoTipoAcceso);				
				//almacenar recurso nuevo del curso
				saveCaracteristica(recurso,true);
				recurso = simplePageToolDao.getUltimoRecurso();
			} else {
				// buscar recurso
				if (!recursoA.equals(cadenaOpciones))
					recurso = ashyiToolDao.getRecurso(recursoA);
			}

			if (!recurso.getNombre().equals("Does not exist")) {
				RecursosActividad recursoNuevo = new RecursosActividadImpl(ac,
						recurso);
				saveCaracteristica(recursoNuevo, true);

				getContextos();
				CaracteristicaRecurso cxtRecurso = new CaracteristicasRecursoImpl(
						recurso, getCaracteristicaContexto(contextoS));
				if (ashyiToolDao.getCaracteristicaRecursos(
						cxtRecurso.getIdRecurso(),
						cxtRecurso.getIdCaracteristica()).equals(null))
					saveCaracteristica(cxtRecurso, true);
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
	public String[] getObjetivosActividad(int nivelRecursividad, String nombre) {
		this.setObjetivos(ashyiToolDao.getObjetivosActividad(nivelRecursividad,
				nombre));

		return this.getObjetivos();
	}

	/**
	 * Guarda el grafo generado por el usuario en el flowchartproducer.
	 * @return resultado de la operaci&oacuten.
	 */
	public String saveFlowChart() {
		String jsonTxt = newFlowChart;
		JSONParser parser = new JSONParser();
		JSONObject jOb = new JSONObject();
		try {
			jOb = (JSONObject) parser.parse(jsonTxt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray conn = (JSONArray) jOb.get("connections");
		System.out.println("Number of connections " + conn.size());
		for (int i = 0; i < conn.size(); i++) {
			System.out.println("=====Connection #" + (i + 1) + "=====");
			JSONObject p = (JSONObject) conn.get(i);
			String source = (String) p.get("source");
			System.out.println("source: " + source);
			String stype = (String) p.get("sourceType");
			System.out.println("source type: " + stype);
			String target = (String) p.get("target");
			System.out.println("target: " + target);
			String ttype = (String) p.get("targetType");
			System.out.println("target type: " + ttype);
			System.out.println("");

			int idTar = Integer.parseInt(target.substring(10));
			//System.out.println("target id: " + idTar);
			//System.out.println("antes de llamar al ashyiToolDao");
			// String a =addActividad(1, "act"+idTar, "goal", "Dest",
			// "2014-05-28T05:40", "2014-05-29T05:40");
		}
		return "done";
	}
	/**
	 * Carga el grafo existente en la base de datos de Ashyi para la unidad.
	 */
	public void loadExistingFlowChart() {
		String nombre = getCurrentPageItem(getCurrentPageId()).getName();
		Actividad acPadre = ashyiToolDao.getactividad(nombre, 3);
		int id = acPadre.getIdActividad();
		// int id=1;
		List<DependenciaActividad> dependencias = ashyiToolDao
				.getDependenciasActividades();
		List<String> actividades = ashyiToolDao
				.getActividadTieneActividadString(id);
		this.existingFlowChart = "{\"user\":\"s\",\"connections\":[";
		int size = dependencias.size();
		for (int i = 0; i < size; i++) {
			DependenciaActividad dep = dependencias.get(i);
			if (actividades.contains("" + dep.getIdActividad())
					&& actividades.contains(""
							+ dep.getId_Actividad_Dependiente())) {
				existingFlowChart += "{";
				existingFlowChart += "\"source\":\"";
				existingFlowChart += "" + dep.getId_Actividad_Dependiente();
				existingFlowChart += "\",\"target\":\"";
				existingFlowChart += "" + dep.getIdActividad();
				existingFlowChart += "\"}";
				if (i < (size - 1))
					existingFlowChart += ",";
			}
		}
		existingFlowChart += "]}";
	}
	/**
	 * Guarda en la base de datos de Ashyi las conexiones del grafo generado en el flowchartproducer.
	 */
	public String saveConnection() {
		//System.out.println("Entre a save connection");

		Actividad a = ashyiToolDao.getactividad(getCurrentPage().getTitle(), 3);
		Usuario idUsuario=ashyiToolDao.getUsuario(ashyiToolDao.getUltimoUsuario(getCurrentUserId()));

		List<GrafosUsuario> lista=ashyiToolDao.getGrafoUsuario(idUsuario,a.getIdActividad());
		Grafo grafo=null;

		if(lista.isEmpty())
		{
			//			System.out.println("!!!!!!: "+a.getIdActividad());
			grafo=new GrafoImpl();
			saveGrafo(grafo);
			grafo.setIdGrafo(ashyiToolDao.getUltimoGrafo());
			//			System.out.println("!!!!!!: "+grafo.getIdGrafo());
			Date fecha = new Date();
			GrafosUsuario gu=new GrafosUsuarioImpl(grafo, idUsuario, a.getIdActividad(), fecha, true,"origen");
			saveGrafosUsuario(gu);
			//			System.out.println("Entre a save connection, grafo nuevo con id "+gu.getIdActividad());
			//			System.out.println("Entre a save connection, grafo nuevo con id "+gu.getIdGrafo());
		}
		else
		{
			GrafosUsuario grafoActivo = null;
			for(GrafosUsuario gU : lista)				
				if(gU.isActivo())
				{	
					grafoActivo = gU;
					break;
				}

			grafo=grafoActivo.getIdGrafo();
		}


		//Graph grafoProfesor=getGrafoUsuarioAg(getAgenteInterface());
		//setGrafoUsuarioAg(getAgenteInterface(), grafoProfesor);

		//Borrar relaciones ya existentes
		ashyiToolDao.deleteGrafoRelaciones(grafo);

		String jsonTxt = lastConnection;
		JSONParser parser = new JSONParser();
		JSONObject jOb = new JSONObject();
		try {
			jOb = (JSONObject) parser.parse(jsonTxt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray conn = (JSONArray) jOb.get("graphConnections");
		//		System.out.println("Cantidad de relaciones "+conn.size());
		for (int i = 0; i < conn.size(); i++) {

			JSONObject p = (JSONObject) conn.get(i);
			String source = (String) p.get("source");
			String target = (String) p.get("target");
			String orden = (String) p.get("order");

			Integer idTar = Integer.parseInt(target);
			Integer idSour = Integer.parseInt(source);

			//grafoProfesor.addEdge(idTar, idSour, 1.0);
			//Guardar relacion en la base de datos
			GrafoRelaciones relacion=new GrafoRelacionesImpl();
			relacion.setIdGrafo(grafo);
			relacion.setIdItemPlan_Origen(idSour);
			relacion.setIdItemPlan_Destino(idTar);
			relacion.setOrden(Integer.parseInt(orden));
			saveGrafoRelaciones(relacion);
			//			System.out.println("Despues de saveGrafoRelaciones");
		}
		return "successGraphEdition";
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
	public String[] getCaracterisicasTipo(String nombre) {
		List<CaracteristicasTipo> listCT = ashyiToolDao
				.getCaracteristicasTipo(nombre);

		String[] caracString = new String[listCT.get(listCT.size() - 1)
		                                  .getLinea()];

		int linea = 1;// primera linea de caracteristicas de tipo
		int i = 0, j = 0;

		for (i = 0; i < listCT.size(); i++) {
			caracString[i] = "";
		}

		for (CaracteristicasTipo ct : listCT) {
			if (linea < ct.getLinea()) {
				j++;// cambio en vector
			}
			caracString[j] = caracString[i] + ", "
					+ ct.getIdCaracteristica().getNombre();
		}

		return caracString;
	}
	/**
	 * Retorna los tipos por nombre.
	 * @param nombre
	 * @return caracString los tipos.
	 */
	public String[] getHabilidadesTipo(String nombre) {
		List<CaracteristicasTipo> listCT = ashyiToolDao
				.getCaracteristicasTipo(nombre);

		String[] caracString = new String[listCT.get(listCT.size() - 1)
		                                  .getLinea()];

		int i = 0, j = 0;

		for (i = 0; i < listCT.size(); i++) {
			if (listCT.get(i).getIdCaracteristica().getIdItem().getNombre() == "Habilidad")// habilidad
			{
				caracString[j] = listCT.get(i).getIdCaracteristica()
						.getNombre();
			}
		}

		return caracString;
	}
	/**
	 * Retorna los tipos por nombre.
	 * @param nombre
	 * @return caracString los tipos.
	 */
	public String[] getCompetenciasTipo(String nombre) {
		List<CaracteristicasTipo> listCT = ashyiToolDao
				.getCaracteristicasTipo(nombre);

		String[] caracString = new String[listCT.get(listCT.size() - 1)
		                                  .getLinea()];

		int i = 0, j = 0;

		for (i = 0; i < listCT.size(); i++) {
			if (listCT.get(i).getIdCaracteristica().getIdItem().getNombre() == "Competencia")// habilidad
			{
				caracString[j] = listCT.get(i).getIdCaracteristica()
						.getNombre();
			}
		}

		return caracString;
	}
	/**
	 * Actualiza un itemPlan.
	 */
	public void updateItemPlan() {
		String jsonTxt = itemPlansStyle;
		JSONParser parser = new JSONParser();
		JSONObject jOb = new JSONObject();
		try {
			jOb = (JSONObject) parser.parse(jsonTxt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray act = (JSONArray) jOb.get("itemPlans");
		for (int i = 0; i < act.size(); i++) {
			JSONObject p = (JSONObject) act.get(i);
			String idItemPlan = (String) p.get("id");
			String top = (String) p.get("top");
			String left = (String) p.get("left");
			String estiloItemPlan = "";
			estiloItemPlan = "\"top\":\"" + top + "\",\"left\":\"" + left
					+ "\"";
			boolean result = updateActividadTieneItemPlan(idItemPlan,
					estiloItemPlan, true);
			if (result)
				System.out.println("ATIP updated");
			else
				System.out.println("Error updating ATIP");
		}

		// modificacion
	}

	/**
	 * Retorna las opciones de actividadInicialFinal.
	 * @return listNombres
	 */
	public String[] getOpcionesIniciaFinal() {
		String[] listNombres = new String[3];// inicial, final, intermedia
		//		listNombres[0] = "Actividad Inicial";
		//		listNombres[1] = "Actividad Final";
		listNombres[0] = cadenaOpciones;
		listNombres[1] = "Actividad Intermedia";
		listNombres[2] = "Actividad Refuerzo";

		return listNombres;
	}

	/**
	 * Carga el grafo con nodos AND existente en la base de datos de Ashyi.
	 */
	public void loadExistingFlowChartWithAndNodes() {
		HashMap<Integer, ArrayList<Integer[]>> andCon = new HashMap<Integer, ArrayList<Integer[]>>();
		String nombre = getCurrentPage().getTitle();
		Actividad acPadre = ashyiToolDao.getactividad(nombre, 3);
		int id = acPadre.getIdActividad();
		List<DependenciaActividad> dependencias = ashyiToolDao
				.getDependenciasActividades();
		List<String> actividades = ashyiToolDao
				.getActividadTieneActividadString(id);
		int size = dependencias.size();
		StringBuilder sbComplete = new StringBuilder("{\"user\":\"p\",");
		StringBuilder sb = new StringBuilder("\"connections\":[");
		StringBuilder sbAnd = new StringBuilder("\"andConnections\":[");

		for (int i = 0; i < size; i++) {
			DependenciaActividad dep = dependencias.get(i);
			Actividad depend = ashyiToolDao.getActividades(
					dep.getId_Actividad_Dependiente().getIdActividad()).get(0);
			Actividad act = ashyiToolDao.getActividades(
					dep.getIdActividad().getIdActividad()).get(0);
			if (actividades.contains("" + act.getIdActividad())
					&& actividades.contains("" + depend.getIdActividad())) {
				int tipo = dep.getTipoConexion();
				if (tipo == 0) {
					sb.append("{");
					sb.append("\"source\":\"");
					sb.append("" + dep.getId_Actividad_Dependiente());
					sb.append("\",\"target\":\"");
					sb.append("" + dep.getIdActividad());
					sb.append("\"}");
					sb.append(",");
				} else if (tipo == 1) {
					int idCon = dep.getIdConexion();
					ArrayList<Integer[]> temp;
					if (andCon.containsKey(idCon)) {
						temp = andCon.get(idCon);
					} else {
						temp = new ArrayList<Integer[]>();
					}
					Integer[] ar = new Integer[2];
					ar[0] = depend.getIdActividad();
					ar[1] = act.getIdActividad();
					temp.add(ar);
					andCon.put(idCon, temp);
				}

			}
		}
		if (sb.charAt(sb.length() - 1) != '[')
			sb.deleteCharAt(sb.length() - 1);
		sb.append("]}");
		Iterator<Integer> it = andCon.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			ArrayList<Integer[]> temp = andCon.get(key);
			sbAnd.append("{");
			sbAnd.append("\"source\":\"");
			sbAnd.append("" + temp.get(0)[0]);
			sbAnd.append("\",\"targets\":[");
			for (int i = 0; i < temp.size(); i++) {
				sbAnd.append("{");
				sbAnd.append("\"id\":\"");
				sbAnd.append("" + temp.get(i)[1]);
				sbAnd.append("\"}");
				sbAnd.append(",");
			}
			if (sbAnd.charAt(sbAnd.length() - 1) == ',')
				sbAnd.deleteCharAt(sbAnd.length() - 1);
			sbAnd.append("]},");
		}
		if (sbAnd.charAt(sbAnd.length() - 1) == ',')
			sbAnd.deleteCharAt(sbAnd.length() - 1);
		sbAnd.append("],");
		sbComplete.append(sbAnd.toString());
		sbComplete.append(sb.toString());
		this.existingFlowChart = sbComplete.toString();
	}

	/**
	 * Retorna los recursos de una actividad.
	 * @param nivelRecursividad
	 * @param nombre
	 * @return recursos los recursos.
	 */
	public String[] getRecursosActividad(int nivelRecursividad, String nombre) {
		List<RecursosActividad> r = ashyiToolDao.getRecursosActividad(
				nivelRecursividad, nombre);

		String[] recursos = new String[r.size() + 1];

		recursos[0] = cadenaOpciones;

		for (int i = 1; i < recursos.length; i++)
			recursos[i] = r.get(i - 1).getIdRecurso().getNombre();

		return recursos;
	}

	/**
	 * Agrega objetivos de actividad.
	 * @return resultado de la operacion.
	 */
	public String addObjetivoTema() {
		Objetivo obj = ashyiToolDao.getObjetivo(objetivoCurso);
		Actividad ac = ashyiToolDao.getactividad(
				getCurrentPageItem(getItemId()).getName(), 2);
		addObjetivoActividad(ac, obj);

		return "success";
	}

	/**
	 * Retorna las actividades de una unidad.
	 * @param unidad
	 * @return actividadesUnidad las actividades.
	 */
	public List<Actividad> getActividadesEnUnidad(String unidad) {
		int idUnidad = 0;
		Actividad a = simplePageToolDao.getactividad(unidad, 3);
		List<Actividad> actividadesUnidad = new ArrayList<Actividad>();
		if (!a.getNombre().equals("Does not exist")) {
			idUnidad = a.getIdActividad();
			List<SimplePageItem> items = getItemsOnPage(currentPage.getPageId());
			List<ActividadTieneActividad> actividades = simplePageToolDao
					.getActividadTieneActividades(idUnidad);

			// if(items.size() == actividades.size())
			// {
			for (SimplePageItem item : items) {
				for (ActividadTieneActividad actvidad : actividades) {
					Actividad aAgregar = simplePageToolDao.getActividadRecurso(
							actvidad.getIdActividadSiguienteNivel()
							.getIdActividad(), String.valueOf(item
									.getId()));
					if (!aAgregar.getNombre().equals("Does not exist")) {
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

			// buscar el item de recursiva
			List<String> elist = new ArrayList<String>();
			Item item = ashyiToolDao.getItemAshyi("Actividad Atomica");
			Tipo tipoS = null;
			for (int j = 0; j < tipoActividad.size(); j++) {
				if (tipoActividad.get(j).getNombre().equals(tipoA)
						&& (!tipoA.equals(cadenaOpciones))) {
					tipoS = tipoActividad.get(j);
				}
			}

			Actividad acAnterior = ashyiToolDao.getActividad(Integer
					.valueOf(idC));

			acAnterior.setIdTipo(tipoS);
			acAnterior.setIdItem(item);
			acAnterior.setNombre(nomberA);
			acAnterior.setDescripcion(descripcionA);
			acAnterior.setDedicacion(Integer.valueOf(dedicacionActividad));

			/*
			 * if (opcionIniciaFinal.equals("Actividad Inicial")) {
			 * acAnterior.setEs_inicial(true); acAnterior.setEs_final(false); }
			 * else if (opcionIniciaFinal.equals("Actividad Final")) {
			 * acAnterior.setEs_inicial(false); acAnterior.setEs_final(true); }
			 * else if (opcionIniciaFinal.equals("Actividad Intermedia")) {
			 * acAnterior.setEs_inicial(false); acAnterior.setEs_final(false); }
			 */

			update(acAnterior);
			// Agregar caracteristicas a actividad

			acAnterior.setCaracteristicas(ashyiToolDao
					.getCaracteristicasActividad(Integer.valueOf(idC)));
			// // List<CaracteristicaActividad> lC =
			// ashyiToolDao.getCaracteristicasActividad(idActividad);
			acAnterior.setObjetivo(ashyiToolDao.getObjetivosActividad(Integer
					.valueOf(idC)));
			// // List<ObjetivosActividad> lO =
			// ashyiToolDao.getObjetivosActividad(idActividad);
			acAnterior.setRecursos(ashyiToolDao.getRecursosActividad(Integer
					.valueOf(idC)));
			// actualizar caracteristicas
			upateCaracteristicasActividad(acAnterior);
			// Actualizar objetivos
			upateObjetivosActividad(acAnterior, 3);
			// actualizar recursos
			upateRecursoActividad(acAnterior, 2);

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
		for (int i = 0; i < ac.getCaracteristicas().size(); i++) {
			caracteristicaEliminar = false;
			CaracteristicaActividad cA = ac.getCaracteristicas().get(i);
			if (cA.getIdCaracteristica().getIdItem().getIdItem() == 5) {
				if (cA.isPrecondicion()) {
					nHPre++;
					for (String habilidad : getHabilidadesSeleccionadas()) {
						if (cA.getIdCaracteristica().getNombre()
								.equals(habilidad)) {
							caracteristicaEliminar = true;
							break;
						}
					}
				}

				if (cA.isPostcondicion()) {
					nHPost++;
					for (String habilidad : getHabilidadesPCSeleccionadas()) {
						if (cA.getIdCaracteristica().getNombre()
								.equals(habilidad)) {
							caracteristicaEliminar = true;
							break;
						}
					}
				}
			}

			if (cA.getIdCaracteristica().getIdItem().getIdItem() == 6) {
				if (cA.isPrecondicion()) {
					nCPre++;
					for (String compentencias : getCompetenciasSeleccionadas()) {
						if (cA.getIdCaracteristica().getNombre()
								.equals(compentencias)) {
							caracteristicaEliminar = true;
							break;
						}
					}
				}
				if (cA.isPostcondicion()) {
					nCPost++;
					for (String compentencias : getCompetenciasPCSeleccionadas()) {
						if (cA.getIdCaracteristica().getNombre()
								.equals(compentencias)) {
							caracteristicaEliminar = true;
							break;
						}
					}
				}
			}

			if (cA.getIdCaracteristica().getIdItem().getIdItem() == 8) {
				nCxt++;
				if (cA.getIdCaracteristica().getNombre().equals(contextoS)) {
					caracteristicaEliminar = true;
				}
			}

			if (cA.getIdCaracteristica().getIdItem().getIdItem() == 9) {
				nSas++;
				if (cA.getIdCaracteristica().getNombre().equals(saS)) {
					caracteristicaEliminar = true;
				}
			}

			if (!caracteristicaEliminar) {
				ac.getCaracteristicas().remove(cA);
				ashyiToolDao.deleteObject(cA);
				ashyiToolDao.flush();
			}
		}

		for (String habilidad : getHabilidadesSeleccionadas()) {
			if (!habilidad.equals(cadenaOpciones)) {
				boolean estar = false;
				for (CaracteristicaActividad cA : ac.getCaracteristicas()) {
					if (cA.getIdCaracteristica().getIdItem().getIdItem() == 5) {
						if (cA.isPrecondicion()) {
							if (habilidad.equals(cA.getIdCaracteristica()
									.getNombre())) {
								estar = true;
								break;
							}
						}
					}
				}

				if (!estar || nHPre == 0) {
					caracteristicaNueva = new CaracteristicaActividadImpl(ac,
							getCaracteristicaHabilidad(habilidad), true, false,
							0);
					c.add(caracteristicaNueva);
					ashyiToolDao
					.saveCaracteristicasActividades(caracteristicaNueva);
				}

			}

		}

		for (String compentencias : getCompetenciasSeleccionadas()) {
			if (!compentencias.equals(cadenaOpciones)) {
				boolean estar = false;
				for (CaracteristicaActividad cA : ac.getCaracteristicas()) {
					if (cA.getIdCaracteristica().getIdItem().getIdItem() == 6) {
						if (cA.isPrecondicion()) {
							if (compentencias.equals(cA.getIdCaracteristica()
									.getNombre())) {
								estar = true;
								break;
							}
						}
					}
				}

				if (!estar || nCPre == 0) {
					caracteristicaNueva = new CaracteristicaActividadImpl(ac,
							getCaracteristicaCompetencia(compentencias), true,
							false, 0);
					c.add(caracteristicaNueva);
					ashyiToolDao
					.saveCaracteristicasActividades(caracteristicaNueva);
				}
			}
		}

		if (!getCaracteristicaSA(saS).getNombre().equals(cadenaOpciones)) {
			boolean estar = false;
			for (CaracteristicaActividad cA : ac.getCaracteristicas()) {
				if (saS.equals(cA.getIdCaracteristica().getNombre())) {
					estar = true;
					break;
				}
			}

			if (!estar || nSas == 0) {
				caracteristicaNueva = new CaracteristicaActividadImpl(ac,
						getCaracteristicaSA(saS), false, false, 0);
				c.add(caracteristicaNueva);
				ashyiToolDao
				.saveCaracteristicasActividades(caracteristicaNueva);
			}
		}

		if (!getCaracteristicaContexto(contextoS).getNombre().equals(
				cadenaOpciones)) {
			boolean estar = false;
			for (CaracteristicaActividad cA : ac.getCaracteristicas()) {
				if (contextoS.equals(cA.getIdCaracteristica().getNombre())) {
					estar = true;
					break;
				}
			}

			if (!estar || nCxt == 0) {
				caracteristicaNueva = new CaracteristicaActividadImpl(ac,
						getCaracteristicaContexto(contextoS), false, false, 0);
				c.add(caracteristicaNueva);
				ashyiToolDao
				.saveCaracteristicasActividades(caracteristicaNueva);
			}
		}

		for (String habilidadPC : getHabilidadesPCSeleccionadas()) {
			if (!habilidadPC.equals(cadenaOpciones)) {
				boolean estar = false;
				for (CaracteristicaActividad cA : ac.getCaracteristicas()) {
					if (cA.getIdCaracteristica().getIdItem().getIdItem() == 5) {
						if (cA.isPostcondicion()) {
							if (habilidadPC.equals(cA.getIdCaracteristica()
									.getNombre())) {
								estar = true;
								break;
							}
						}
					}
				}

				if (!estar || nHPost == 0) {
					caracteristicaNueva = new CaracteristicaActividadImpl(ac,
							getCaracteristicaHabilidad(habilidadPC), false,
							true, 0);
					c.add(caracteristicaNueva);
					ashyiToolDao
					.saveCaracteristicasActividades(caracteristicaNueva);
				}
			}
		}

		for (String competenciaPC : getCompetenciasPCSeleccionadas()) {
			if (!competenciaPC.equals(cadenaOpciones)) {
				boolean estar = false;
				for (CaracteristicaActividad cA : ac.getCaracteristicas()) {
					if (cA.getIdCaracteristica().getIdItem().getIdItem() == 6) {
						if (cA.isPostcondicion()) {
							if (competenciaPC.equals(cA.getIdCaracteristica()
									.getNombre())) {
								estar = true;
								break;
							}
						}
					}
				}

				if (!estar || nCPost == 0) {
					caracteristicaNueva = new CaracteristicaActividadImpl(ac,
							getCaracteristicaCompetencia(competenciaPC), false,
							true, 0);
					c.add(caracteristicaNueva);
					ashyiToolDao
					.saveCaracteristicasActividades(caracteristicaNueva);
				}
			}
		}
	}

	/**
	 * Elimina una actividad.
	 * @return resultado de la operaci&oacuten.
	 */
	public String deleteActividad() {
		Actividad a = ashyiToolDao.getActividad(Integer.valueOf(idC));

		List<CaracteristicaActividad> lC = ashyiToolDao
				.getCaracteristicasActividad(Integer.valueOf(idC));

		for (int i = 0; i < lC.size(); i++) {
			lC.get(i).setIdActividad(a);

			ashyiToolDao.deleteObject(lC.get(i));
			ashyiToolDao.flush();
		}
		// a.getCaracteristicas().clear();
		// update(a);

		List<ObjetivosActividad> lO = ashyiToolDao
				.getObjetivosActividad(Integer.valueOf(idC));
		// a.setObjetivo(lO);
		for (int i = 0; i < lO.size(); i++) {
			lO.get(i).setIdActividad(a);
			ashyiToolDao.deleteObject(lO.get(i));
			ashyiToolDao.flush();
		}
		// a.getObjetivo().clear();
		// update(a);

		List<RecursosActividad> lR = ashyiToolDao.getRecursosActividad(Integer
				.valueOf(idC));
		// a.setRecursos(lR);
		for (int i = 0; i < lR.size(); i++) {
			// Si es el recurso de sakai
			if (!lR.get(i).getIdRecurso().getIdItemSakai().isEmpty()) {
				List<SimplePageItem> itemList = (List<SimplePageItem>) getItemsOnPage(getCurrentPage()
						.getPageId());
				for (int j = 0; j < itemList.size(); j++) {
					if (itemList.get(j).getId() == Long.valueOf(lR.get(i)
							.getIdRecurso().getIdItemSakai())) {
						// Eliminar el item de la lista
						itemId = itemList.get(j).getId();
						deleteItem();
					}
				}
			}
			lR.get(i).setIdActividad(a);
			ashyiToolDao.deleteObject(lR.get(i));
			ashyiToolDao.flush();
		}
		// a.getRecursos().clear();
		// update(a);
		Actividad unidad = ashyiToolDao.getActividadAltoNivel(a
				.getIdActividad());
		ActividadTieneActividad aTa = new ActividadTieneActividadImpl(unidad, a);
		ashyiToolDao.deleteObject(aTa);
		ashyiToolDao.flush();
		// eliminar actividad
		ashyiToolDao.deleteActividad(a);
		ashyiToolDao.flush();

		return "success";
	}

	/**
	 * Actualiza los objetivos de una actividad.
	 * @param ac la actividad.
	 * @param nR nivel de recursividad.
	 */
	public void upateObjetivosActividad(Actividad ac, int nR) {

		boolean eliminar = false;
		for (int i = 0; i < ac.getObjetivo().size(); i++) {
			ObjetivosActividad oA = ac.getObjetivo().get(i);
			eliminar = false;
			for (String objetivo : getObjetivosSeleccionados()) {
				if (oA.getIdObjetivo().getNombre().equals(objetivo)) {
					eliminar = true;
					break;
				}
			}

			if (!eliminar) {
				ac.getObjetivo().remove(oA);
				ashyiToolDao.deleteObject(oA);
				ashyiToolDao.flush();
			}
		}

		for (String objetivo : getObjetivosSeleccionados()) {
			boolean estar = false;
			for (ObjetivosActividad oA : ac.getObjetivo()) {
				if (objetivo.equals(oA.getIdObjetivo().getNombre())) {
					estar = true;
					break;
				}
			}

			if (!estar) {
				Objetivo obj = new ObjetivoImpl();
				obj.setIdObjetivo(ashyiToolDao.getObjetivoActividad(objetivo,
						nR, getCurrentPage().getTitle()));
				obj.setNombre(objetivo);
				ObjetivosActividadImpl objetivoAc = new ObjetivosActividadImpl(2,
						ac, obj);

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

		Recurso recurso = new RecursoImpl("Does not exist", "No aplica", "");
		for (RecursosActividad rA : ac.getRecursos()) {
			if (rA.getIdRecurso().getIdItemSakai().isEmpty()) {
				if (!rA.getIdRecurso().getNombre().equals(recursoA)) {
					// eliminar el anterior
					ac.getRecursos().remove(rA);
					ashyiToolDao.deleteObject(rA);
					ashyiToolDao.flush();

					// buscar recurso
					if (!recursoA.equals(cadenaOpciones))
						recurso = ashyiToolDao.getRecurso(recursoA);

					if (!recurso.getNombre().equals("Does not exist")) {
						RecursosActividad recursoNuevo = new RecursosActividadImpl(
								ac, recurso);
						saveCaracteristica(recursoNuevo, true);
					}
				}
			}
		}
	}

	//	public String addAgent(String nombre, int tipo) {
	//		// if(bean.getFoo().equals("Default Foo"))
	//		getBeanBesa();
	//		System.out.println("Verificar existencia de agente");
	//		String aliasA = "";
	//		if (!bean.existeAgente(nombre)) {
	//			System.out.println("Agente " + nombre + " no existe");
	//			// aliasA =
	//			// bean.crearAgente(sessionManager.getCurrentSession().getUserEid());
	//			aliasA = bean.crearAgente(nombre, tipo);
	//
	//		}
	//		aliasA = bean.getAgent(nombre);
	//		System.out.println("Agente " + aliasA + " dispuesto");
	//		return aliasA;
	//	}

	/**
	 * Retorna el currentUserNameId
	 * @return currentUserNameId
	 */
	public String getCurrentUserNameID() {
		if (currentUserNameId == null)
			currentUserNameId = UserDirectoryService.getCurrentUser()
			.getDisplayName()
			+ "-"
			+ getCurrentUserId()
			+ "-"
			+ getCurrentUserType();
		return currentUserNameId;
	}
	/**
	 * Retorna el currentUserNameId
	 * @return currentUserNameId
	 */
	public String getCurrentUserName() {
		if (currentUserNameId == null)
			currentUserNameId = UserDirectoryService.getCurrentUser()
			.getFirstName()
			+ "-"
			+ getCurrentUserId()
			+ "-"
			+ getCurrentUserType();
		return currentUserNameId;
	}

	/**
	 * Detecta el dispositivo.
	 * @return true si es desktop, false si es mobile.
	 */
	public boolean detectDevice() {

		if (httpServletRequest.getHeader("User-Agent").indexOf("Mobile") != -1) {
			// you're in mobile land
			deviceType = "mobile";
			return false;
		} else {
			// nope, this is probably a desktop
			deviceType = "desktop";
			return true;
		}

		//return deviceType;
	}

	/**
	 * Detecta la red.
	 * @return true dentro del campus, false fuera del campus.
	 */
	public boolean detectNet() {
		boolean dentro = false;
		String ipR = httpServletRequest.getRemoteAddr();
		String[] numR = ipR.split("\\.");
		//System.out.println("!!!!!!!!!!!!: " + ipR);
		String ipS = httpServletRequest.getLocalAddr();

		String[] num = ipS.split("\\.");

		if (num[0].equals(numR[0])) {
			//System.out.println("!!!!!!!!: est&aacute dentro");
			accessType = "campus";
			dentro = true;
		}
		else
		{
			accessType = "outside";
			dentro = false;
		}
		// InetAddress address = InetAddress.getByName("javeriana.edu.co");
		return dentro;
	}

	/**
	 * Detecta el idioma.
	 * @return language
	 */
	public String detectLenguage() {
		String language = httpServletRequest.getLocale().getDisplayLanguage();
		return language;
	}

	/**
	 * Consulta en las tablas de chaea, los resultados del test
	 * @return si encuentra resultados o no
	 */
	public boolean consultaEstilosChaea()
	{
		ChaeaDaoImpl dao = new ChaeaDaoImpl();
		ChaeaLogicImpl chaea = new ChaeaLogicImpl();
		dao.setDataSource(ashyiToolDao.getDataSource());
		chaea.setDao(dao);		
		long isTest = ashyiToolDao.getIdChaeaTest(getCurrentSiteId());
		if(isTest != 0)
		{
			Map<Character, Double> resultadosAluno = chaea.getResultadosAluno(isTest, getCurrentUserId());
			double a = resultadosAluno.get(Questao.TIPO_ACTIVO);
			double p = resultadosAluno.get(Questao.TIPO_PRAGMATICO);
			double r = resultadosAluno.get(Questao.TIPO_REFLEXIVO);
			double t = resultadosAluno.get(Questao.TIPO_TEORICO);
			if (a != 0.0 && p != 0.0 && r != 0.0 && t != 0.0) {
				activo = (int) a;
				prag = (int) p;
				reflexivo = (int) r;
				teorico = (int) t;

				return true;
			}
		}

		return false;
	}

	/**
	 * Revisa el chaea del usuario
	 * @param idUsuario
	 * @return 
	 */
	public boolean chaea(int idUsuario) {
		//		ChaeaDaoImpl dao = new ChaeaDaoImpl();
		//		ChaeaLogicImpl chaea = new ChaeaLogicImpl();
		//		dao.setDataSource(ashyiToolDao.getDataSource());
		//		chaea.setDao(dao);
		//		Map<Character, Double> resultadosAluno = chaea.getResultadosAluno(
		//				Long.valueOf(1), getCurrentUserId());
		//		double a = resultadosAluno.get(Questao.TIPO_ACTIVO);
		//		double p = resultadosAluno.get(Questao.TIPO_PRAGMATICO);
		//		double r = resultadosAluno.get(Questao.TIPO_REFLEXIVO);
		//		double t = resultadosAluno.get(Questao.TIPO_TEORICO);
		//
		//		if (a != 0.0 && p != 0.0 && r != 0.0 && t != 0.0) {
		//			activo = (int) a;
		//			prag = (int) p;
		//			reflexivo = (int) r;
		//			teorico = (int) t;
		if(consultaEstilosChaea())	
		{
			int veces = 0;

			List<CaracteristicasUsuario> cUsuario = ashyiToolDao.getEstilosUsuario(idUsuario);
			if (cUsuario.size() > 0) {
				for (int i = 0; i < cUsuario.size(); i++) {
					if (cUsuario.get(i).getIdCaracteristica().getIdItem()
							.getIdItem() == 4) {
						veces++;
					}
				}
				if (veces == 4)
					return false;
				else
					return true;
			} else {
				return true;
			}
		}

		return false;
	}

	/**
	 * Guarda los estilos de aprendizaje de un usuario.
	 * @param idUsuario id de usuario.
	 */
	public void saveEstilos(int idUsuario) {
		getEstilos();
		Caracteristica estiloActivo = getCaracteristicaEstilo("Activo");
		Caracteristica estiloPrag = getCaracteristicaEstilo("Pragmatico");
		Caracteristica estiloReflexivo = getCaracteristicaEstilo("Reflexivo");
		Caracteristica estiloTeorico = getCaracteristicaEstilo("Teorico");

		Usuario user = ashyiToolDao.getUsuario(idUsuario);

		CaracteristicasUsuario cEstiloA = new CaracteristicasUsuarioImpl(user,
				estiloActivo, (int) activo);
		saveCarateristica(cEstiloA);

		CaracteristicasUsuario cEstiloP = new CaracteristicasUsuarioImpl(user,
				estiloPrag, (int) prag);
		saveCarateristica(cEstiloP);

		CaracteristicasUsuario cEstiloR = new CaracteristicasUsuarioImpl(user,
				estiloReflexivo, (int) reflexivo);
		saveCarateristica(cEstiloR);

		CaracteristicasUsuario cEstiloT = new CaracteristicasUsuarioImpl(user,
				estiloTeorico, (int) teorico);
		saveCarateristica(cEstiloT);

	}

	/**
	 * A&ntildeade una palabra clave al curso.
	 */
	private void addPalabraCurso() {

		PalabraClave palabra = new PalabraClaveImpl();
		try {
			int id = 0;
			palabra = ashyiToolDao.getPalabra(palabraClave);
			if (palabra.getPalabra().equals("Does not exist")) {
				// almacenar objetivo
				palabra.setPalabra(palabraClave);
				if (tipoNaturaleza.equals("Verbo"))
					palabra.setTipo(1);
				if (tipoNaturaleza.equals("Complemento"))
					palabra.setTipo(2);
				saveCaracteristica(palabra, true);
				// relacionar objetivo y actividad
				id = ashyiToolDao.getUltimaPalabra(palabra.getPalabra());
				palabra.setIdPalabraClave(id);
			}
		} catch (Throwable t) {
			// this is probably a bogus error, but find its root cause
			while (t.getCause() != null) {
				t = t.getCause();
			}
		}
		Actividad ac = ashyiToolDao
				.getactividad(getCurrentSite().getTitle(), 1);
		addPalabraCurso(ac, palabra);

	}

	/**
	 * A&ntildeade una palabra clave al curso.
	 * @param ac la actividad.
	 * @param palabra la palabra clave.
	 */
	private void addPalabraCurso(Actividad ac, PalabraClave palabra) {

		String err = null;
		PalabraClaveActividad palabraNueva = new PalabraClaveActividadImpl(
				palabra, ac);
		saveCaracteristica(palabraNueva, true);

	}

	/**
	 * Retorna los verbos del curso.
	 * @return listNombres los verbos.
	 */
	public String[] getVerbosCurso() {

		List<PalabraClave> verbosCurso = ashyiToolDao.getPalabrasClave(1,
				getCurrentSite().getTitle(), 1);

		String[] listNombres = new String[verbosCurso.size() + 1];
		listNombres[0] = cadenaOpciones;

		if (verbosCurso.size() > 0) {
			int i = 0;
			for (i = 1; i < verbosCurso.size() + 1; i++) {
				PalabraClave c = verbosCurso.get(i - 1);
				listNombres[i] = c.getPalabra();
			}
		}
		return listNombres;
	}
	/**
	 * Retorna los complementos del curso.
	 * @return listNombres los complementos.
	 */
	public String[] getComplementosCurso() {

		List<PalabraClave> complementosCurso = ashyiToolDao.getPalabrasClave(2,
				getCurrentSite().getTitle(), 1);

		String[] listNombres = new String[complementosCurso.size() + 1];
		listNombres[0] = cadenaOpciones;
		if (complementosCurso.size() > 0) {
			int i = 0;
			for (i = 1; i < complementosCurso.size() + 1; i++) {
				PalabraClave c = complementosCurso.get(i - 1);
				listNombres[i] = c.getPalabra();
			}
		}
		return listNombres;
	}

	/**
	 * Elimina una unidad.
	 * @param Unidad
	 */
	public void deleteUnidad(Actividad Unidad) {
		List<ActividadTieneActividad> lA = simplePageToolDao
				.getActividadTieneActividades(Unidad.getIdActividad());
		// a.setObjetivo(lO);
		for (int i = 0; i < lA.size(); i++) {
			lA.get(i).setIdActividad(Unidad);
			simplePageToolDao.deleteObject(lA.get(i));
			simplePageToolDao.flush();
		}

		Actividad tema = simplePageToolDao.getactividad(getCurrentPage()
				.getTitle(), 2);
		lA = simplePageToolDao.getActividadTieneActividad(
				tema.getIdActividad(), Unidad.getIdActividad());
		for (int i = 0; i < lA.size(); i++) {
			lA.get(i).setIdActividad(tema);
			simplePageToolDao.deleteObject(lA.get(i));
			simplePageToolDao.flush();
		}

		List<ObjetivosActividad> lO = simplePageToolDao
				.getObjetivosActividad(Unidad.getIdActividad());
		// a.setObjetivo(lO);
		for (int i = 0; i < lO.size(); i++) {
			lO.get(i).setIdActividad(Unidad);
			simplePageToolDao.deleteObject(lO.get(i));
			simplePageToolDao.flush();
		}

		// eliminar actividad
		simplePageToolDao.deleteActividad(Unidad);
		simplePageToolDao.flush();
	}

	/**
	 * Retorna los objetivos de una actividad.
	 * @param nombreAct nombre de la actividad.
	 * @param nR nivel de recursividad.
	 * @return objUnidad los objetivos. 
	 */
	public List<ObjetivosActividad> getObjetivosActividad(String nombreAct,
			int nR) {
		Actividad unidad = ashyiToolDao.getactividad(nombreAct, nR);
		List<ObjetivosActividad> objUnidad = ashyiToolDao
				.getObjetivosActividad(unidad.getIdActividad());

		return objUnidad;
	}

	/**
	 * Env&iacutea un evento BESA.
	 * @param myAlias
	 * @param alias
	 * @param tipo
	 * @param datos
	 * @return
	 */
	public boolean executeAction(String myAlias, String alias, String tipo, Object datos) {
		getBeanBesa();
		//verificar que existan los agentes a manejar
		List contexto = new ArrayList();
		detectDevice();
		detectNet();
		System.out.println("Contexto: "+getAccessType()+" , "+getDeviceType()+" act: "+getIdActividad(getCurrentPage().getTitle(),3).getIdActividad());
		contexto.add(getAccessType());
		contexto.add(getDeviceType());
		contexto.add(getIdActividad(getCurrentPage().getTitle(),3).getIdActividad());

		if(!myAlias.equals("") || myAlias != null)
		{			
			boolean existe = bean.existeAgente(myAlias);
			if(!existe)
			{
				if(myAlias.contains("Student"))
				{
					contexto.add(myAlias.replace("Executor-", ""));
					bean.crearAgente(myAlias, 3, getCurrentPage().getTitle());
					bean.executeAction("Executor-" + getCurrentUserName(), "","USER", contexto);
				}
				if(myAlias.contains("Instructor"))
					bean.crearAgente(myAlias, 2, getCurrentPage().getTitle());	
			}
		}

		if(!alias.equals("") || alias != null)
		{
			boolean existe = bean.existeAgente(alias);
			if(!existe)
			{
				if(myAlias.contains("Student"))
				{
					contexto.add(myAlias.replace("Executor-", ""));
					bean.crearAgente(alias, 3, getCurrentPage().getTitle());					
					bean.executeAction("Executor-" + getCurrentUserName(), "","USER", contexto);
				}
				if(myAlias.contains("Instructor"))
					bean.crearAgente(alias, 2, getCurrentPage().getTitle());

			}
		}
		if (bean.executeAction(myAlias, alias, tipo, datos))
			return true;
		return false;

	}

	/**
	 * Retorna el estado de un agente BESA.
	 * @param alias del agente
	 * @return estado
	 */
	public Data getDatosAgente(String alias) {
		getBeanBesa();
		Data estado = bean.getDatosAgente(alias);

		return estado;
	}

	/**
	 * Retorna los identificadores de los objetivosActividad.
	 * @param nR nivel de recursividad.
	 * @param nombreA nombre de actividad.
	 * @return objUnidad los ids.
	 */
	public List getIdsObjetivosActividad(int nR, String nombreA) {

		Actividad unidad = ashyiToolDao.getactividad(nombreA, nR);
		List<List> objUnidad = ashyiToolDao.getIdsObjetivosActividad(unidad
				.getIdActividad());

		return objUnidad;
	}

	/**
	 * Revisa la personalidad y habilidad del usuario.
	 * @param idUsuario
	 * @param idUsarioSakai
	 * @return
	 */
	public int PH(int idUsuario, String idUsarioSakai) {
		int h = ashyiToolDao.getCaracteristicasUsuario(idUsuario, 5);// habilidad
		int p = ashyiToolDao.getCaracteristicasUsuario(idUsuario, 7);// personalidad

		boolean habilidades = false;
		boolean personalidad = false;

		int idPollP = ashyiToolDao.getIdPollPyH(getCurrentSiteId(), 1);
		int idPollH = ashyiToolDao.getIdPollPyH(getCurrentSiteId(), 2);

		if (p == 1)// si hay una personalidad y minimo una habilidad
		{
			personalidad = true;
		} else {
			// verificar si el usuario lleno encuesta personalidad
			// 2 es el id de la encuesta en la BD para habilidades
			personalidad = ashyiToolDao.getRespuestasUsuario(idUsarioSakai, idPollP);
		}

		if (h > 0) {
			habilidades = true;
		} else {
			// verificar si el usuario lleno encuesta habilidad
			// 1 es el id de la encuesta en la BD para habilidades
			habilidades = ashyiToolDao.getRespuestasUsuario(idUsarioSakai, idPollH);
		}

		if (habilidades && personalidad)
			return 2;
		else
			return 0;
	}

	/**
	 * Retorna las actividades de refuerzo de cierto nivel.
	 * @param NActividades el nivel.
	 * @return las actividades.
	 */
	public boolean getActividadesRefuerzo(String NActividades) {

		return ashyiToolDao.getActividadesRefuerzo(NActividades);
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
	 * Retorna un itemPlan por su identificador
	 * @param idItem el id.
	 * @return el itemPlan.
	 */
	public ItemPlan getItemPlan(int idItem) {
		return ashyiToolDao.getItemPlan(idItem);
	}

	/**
	 * Retorna un itemPlan busc&aacutendolo a partir de los par&aacutemetros pasados.
	 * @param ud
	 * @param auxActividad
	 * @param auxRecurso
	 * @param tipo
	 * @param orden
	 * @return el itemPlan.
	 */
	public ItemPlan getItemPlan(Actividad ud, Actividad auxActividad, Recurso auxRecurso, int tipo, int orden) {
		return ashyiToolDao.getItemPlan(ud, auxActividad, auxRecurso, tipo, orden);
	}

	/**
	 * Revisa si un usuario tiene un GrafosUsuario para la unidad.
	 * @param usuarioSakai
	 * @param titulo
	 * @return true si tiene, false si no.
	 */
	public boolean isGrafoUsuario(String usuarioSakai, String titulo) {
		return ashyiToolDao.isGrafoUsuario(usuarioSakai, titulo);
	}

	/**
	 * Agrega un agente.
	 * @param nombre
	 * @param tipo
	 * @param title
	 * @return aliasA el alias del agente.
	 */
	public String addAgent(String nombre, int tipo, String title) {
		// if(bean.getFoo().equals("Default Foo"))
		getBeanBesa();
		System.out.println("Verificar existencia de agente");
		String aliasA = "";
		if (!bean.existeAgente(nombre)) {
			System.out.println("Agente " + nombre + " no existe");
			// aliasA =
			// bean.crearAgente(sessionManager.getCurrentSession().getUserEid());
			aliasA = bean.crearAgente(nombre, tipo, title);

		}
		aliasA = bean.getAgent(nombre);
		System.out.println("Agente " + aliasA + " dispuesto");
		return aliasA;
	}

	/**
	 * Retorna los grafosUsuarios de un agente.
	 * @param aliasAg alias del agente.
	 * @return el grafosUsuario.
	 */
	public Graph getGrafoUsuarioAg(String aliasAg)
	{
		// actividad
		Actividad a = ashyiToolDao.getactividad(getCurrentPage().getTitle(), 3);
		return bean.getGrafoUsuario(aliasAg, a.getIdActividad());
	}

	/**
	 * Retorna el id de una actividad.
	 * @param title el nombre.
	 * @param nivelRecursividad
	 * @return el id.
	 */
	public Actividad getIdActividad(String title, int nivelRecursividad) {
		return ashyiToolDao.getactividad(title, nivelRecursividad);
	}

	/**
	 * Revisa si hay un cambio de contexto.
	 * @param agenteInterface
	 * @param contexto
	 * @return
	 */
	public boolean isCambioContexto(String agenteInterface, List contexto) {
		return bean.isCambioContexto(agenteInterface, contexto);
	}

	/**
	 * Verifica los objetivos de usuario.
	 * @return resultado de la operaci&oacuten.
	 */
	public String verificarOU()
	{
		//		for(int i = 0; i < objetivosSeleccionados.length; i++)
		//		{
		//			int idObj = ashyiToolDao.verificarOU(objetivosSeleccionados[i]);
		//			if(idObj == 0)
		//			{
		//				
		//			}
		//		}
		return "success";		
	}

	/**
	 * Asigna un grafosUsuario a un agente por su alias.
	 * @param aliasAg alias del agente.
	 * @param grafo el grafo.
	 */
	public void setGrafoUsuarioAg(String aliasAg, Graph grafo)
	{
		Actividad a = ashyiToolDao.getactividad(getCurrentPage().getTitle(), 3);
		bean.setGrafoUsuarioAg(aliasAg, a.getIdActividad(), grafo);
	}

	/**
	 * Asigna la respuesta del estudiante.
	 */
	public void setRespuestaEstudiante()
	{
		System.out.println("Ruta del archivo de respuesta: "+this.recursoUpload);
		URL url;
		/*try {
			url = new URL(this.recursoUpload);
			try {
				File file = new File(url.toURI());
				System.out.println("archivo obtenido: "+file);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	/**
	 * Retorna las caracter&iacutesticas de una actividad a distancia.
	 * @param iPlan
	 * @param tipo
	 * @return nombresC
	 */
	public String[] getCompHabPostItemUsuario(ItemsUsuario iPlan, int tipo) {

		List<Caracteristica> listaC = getCompHabItemUsuario(iPlan,tipo, 1);
		String[] nombresC = new String[listaC.size()];
		for(int i = 0; i < listaC.size(); i++)
			nombresC[i] = listaC.get(i).getNombre();
		return nombresC;

	}

	/**
	 * Retorna las caracter&iacutesticas de una actividad a distancia.
	 * @param iPlan el itemPlan.
	 * @param tipo el tipo.
	 * @param consulta la consulta.
	 * @return listaC las caracter&iacutesticas.
	 */
	public List<Caracteristica> getCompHabItemUsuario(ItemsUsuario iPlan, int tipo, int consulta) {

		//1 no consulta las caracteristicas de los tipos

		List<Caracteristica> listaC = ashyiToolDao.getCaracteristicasActividadDistancia(iPlan.getIdItemPlan().getIdActividad().getIdActividad(), tipo, consulta);

		return listaC;

	}

	/**
	 * Retorna las caracter&iacutesticas llenas de un itemUsuario.
	 * @param iPlan
	 * @return listaC las caracter&iacutesticas.
	 */
	public List<Caracteristica> getCaraLlenasItemUsuario(ItemsUsuario iPlan) {

		List<Caracteristica> listaC = ashyiToolDao.getCaracteristicasLlenasItemUsuario(iPlan);

		return listaC;

	}

	/**
	 * Actualiza un itemUsuario incompleto.
	 * @return resultado de la operaci&oacuten.
	 */
	public String updateItemUsuarioPlanIncompleto()
	{
		System.out.println("Actualizando item usuario ");
		ItemsUsuario iU = ashyiToolDao.getItemUsuario(Integer.valueOf(idTP), Integer.valueOf(idTU));		
		iU.setNota(Double.valueOf(notaA));
		iU.setRealizada(true);		

		//actualizar itemusuario
		update(iU);

		List<CaracteristicasItemsUsuario> caracteristicasIU = ashyiToolDao.getCaracteristicasItemUsuario(iU);

		for(CaracteristicasItemsUsuario caIU : caracteristicasIU)
		{
			ashyiToolDao.deleteObject(caIU);
		}

		//2 post condiciones, 1 sin caracteristicas de tipo
		List<Caracteristica> caracteristicas = getCompHabItemUsuario(iU, 2, 1);
		//2 post condiciones, 2 con caracteristicas de tipo
		List<Caracteristica> caracteristicasAcCompletas = getCompHabItemUsuario(iU, 2, 2);

		//consultar las caracteristicas del sistema
		List<List<Caracteristica>> caracteristicasCompletas = getCaracteristicas();
		//double actividad[] = construirActividad(iU.getIdItemPlan(), caracteristicasCompletas, caracteristicasAcCompletas);

		for(Caracteristica caIU : caracteristicas)
		{
			CaracteristicasItemsUsuario cIU = new CaracteristicasItemsUsuarioImpl();
			boolean almacenada = false;
			for(String comp : competenciasSeleccionadas)
			{				
				if(caIU.getNombre().equals(comp))
				{					
					cIU = new CaracteristicasItemsUsuarioImpl(caIU,iU.getIdItemPlan(), iU.getIdUsuario(), true,new Date());
					//almacenar resultado de caracteristicas
					saveCarateristica(cIU);
					//System.out.println("!!ca Almacena: "+cIU.isEstado());
					almacenada = true;					
				}								
			}
			if(!almacenada)
			{					
				cIU = new CaracteristicasItemsUsuarioImpl(caIU,iU.getIdItemPlan(), iU.getIdUsuario(), false,new Date());
				//almacenar resultado de caracteristicas
				saveCarateristica(cIU);
				//System.out.println("!!ca Almacena fuera: "+cIU.isEstado());
			}
		}

		//enviar cambios a perfil de estudiante

		List<User> usuariosAshyi = getUsuariosAshyi();

		String idUsuario = iU.getIdUsuario().getIdUsuarioSakai();

		String Eid = "";

		for(User us : usuariosAshyi)
			if(us.getId().equals(idUsuario))
			{
				Eid = us.getEid();
				break;
			}

		try {
			User usuario = UserDirectoryService.getUserByEid(Eid);
			String aliasUs = usuario.getFirstName()
					+ "-"
					+ idUsuario
					+ "-"
					+ usuario.getType();

			//System.out.println("User EID: "+usuario.getEid());

			List datos = new ArrayList();
			getBeanBesa();
			datos.add(iU.getIdItemPlan().getIdItemPlan());
			datos.add(ashyiToolDao.getactividad(getCurrentPage().getTitle(), 3).getIdActividad());

			executeAction("Executor-" + aliasUs, getAgenteComunicacion(),"PROFILE_CHANGE", datos);

		} catch (UserNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failure";
		}

		return "success";
	}

	/**
	 * Actualiza un itemUsuario.
	 * @return resultado de la operaci&oacuten.
	 */
	public String updateItemUsuarioPlan()
	{
		System.out.println("Actualizando item usuario: "+idTP+"-"+idTU+"-"+idRIU);
		ItemsUsuario iU = ashyiToolDao.getItemUsuario(Integer.valueOf(idTP), Integer.valueOf(idTU));
		RespuestaItemsUsuario rIU = ashyiToolDao.getRespuestaItemUsuario(Integer.valueOf(idTP), Integer.valueOf(idTU), Integer.valueOf(idRIU));	
		rIU.setRetroalimentacion_profesor(retroalimentacionP);
		iU.setNota(Double.valueOf(notaA));
		iU.setRealizada(true);

		//actualizar respuesta
		update(rIU);
		//actualizar itemusuario
		update(iU);

		List<CaracteristicasItemsUsuario> caracteristicasIU = ashyiToolDao.getCaracteristicasItemUsuario(iU);

		for(CaracteristicasItemsUsuario caIU : caracteristicasIU)
		{
			ashyiToolDao.deleteObject(caIU);
		}

		//2 post condiciones, 1 sin caracteristicas de tipo
		List<Caracteristica> caracteristicas = getCompHabItemUsuario(iU, 2, 1);
		//2 post condiciones, 2 con caracteristicas de tipo
		List<Caracteristica> caracteristicasAcCompletas = getCompHabItemUsuario(iU, 2, 2);

		//consultar las caracteristicas del sistema
		List<List<Caracteristica>> caracteristicasCompletas = getCaracteristicas();
		//double actividad[] = construirActividad(iU.getIdItemPlan(), caracteristicasCompletas, caracteristicasAcCompletas);

		for(Caracteristica caIU : caracteristicas)
		{
			CaracteristicasItemsUsuario cIU = new CaracteristicasItemsUsuarioImpl();
			boolean almacenada = false;
			for(String comp : competenciasSeleccionadas)
			{				
				if(caIU.getNombre().equals(comp))
				{					
					cIU = new CaracteristicasItemsUsuarioImpl(caIU,iU.getIdItemPlan(), iU.getIdUsuario(), true,new Date());
					//almacenar resultado de caracteristicas
					saveCarateristica(cIU);
					//System.out.println("!!ca Almacena: "+cIU.isEstado());
					almacenada = true;					
				}								
			}
			if(!almacenada)
			{					
				cIU = new CaracteristicasItemsUsuarioImpl(caIU,iU.getIdItemPlan(), iU.getIdUsuario(), false,new Date());
				//almacenar resultado de caracteristicas
				saveCarateristica(cIU);
				//System.out.println("!!ca Almacena fuera: "+cIU.isEstado());
			}
		}

		//enviar cambios a perfil de estudiante

		List<User> usuariosAshyi = getUsuariosAshyi();

		String idUsuario = iU.getIdUsuario().getIdUsuarioSakai();

		String Eid = "";

		for(User us : usuariosAshyi)
			if(us.getId().equals(idUsuario))
			{
				Eid = us.getEid();
				break;
			}

		try {
			User usuario = UserDirectoryService.getUserByEid(Eid);
			String aliasUs = usuario.getFirstName()
					+ "-"
					+ idUsuario
					+ "-"
					+ usuario.getType();

			System.out.println("User EID: "+usuario.getEid());

			List datos = new ArrayList();
			getBeanBesa();
			datos.add(iU.getIdItemPlan().getIdItemPlan());
			datos.add(ashyiToolDao.getactividad(getCurrentPage().getTitle(), 3).getIdActividad());

			executeAction("Executor-" + aliasUs, getAgenteComunicacion(),"PROFILE_CHANGE", datos);

		} catch (UserNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failure";
		}

		return "success";
	}

	/**
	 * Actualiza un itemUsuario.
	 * @return resultado de la operaci&oacuten.
	 */
	public String updateItemUsuarioEjecutor()
	{
		this.uploadExportFile();

		ItemsUsuario iU = ashyiToolDao.getItemUsuario(Integer.valueOf(idTP), Integer.valueOf(idTU));
		RespuestaItemsUsuario riU = ashyiToolDao.getRespuestaItemUsuario(Integer.valueOf(idTP), Integer.valueOf(idTU), Integer.valueOf(idRIU));
		riU.setRetroalimentacion_usuario(retroalimentacionE);
		//actualizar respuesta
		update(riU);

		return "success";
	}

	/**
	 * Retorna las caracter&iacutesticas llenas de un ItemUsuario.
	 * @param iPlan
	 * @return nombres las caracter&iacutesticas.
	 */
	public String[] getCaracLlenasItemUsuario(ItemsUsuario iPlan) {
		List<Caracteristica> car = getCaraLlenasItemUsuario(iPlan);

		String[] nombres = new String[car.size()];

		for(int i = 0; i < car.size() ; i++)
			nombres[i] = car.get(i).getNombre();
		if(car.size() > 0)
			return nombres;

		return null;
	}

	/**
	 * Crea una actividad
	 * @param item
	 * @param caracteristicas
	 * @param caracteristicasActividad
	 * @return 
	 */
	public double[] construirActividad(ItemPlan item,List<List<Caracteristica>> caracteristicas, List<Caracteristica> caracteristicasActividad) 
	{
		//Consulta consAHYI = new Consulta();
		double[] actividad = new double[55];

		//1 pre condiciones y 2 consulta tipo
		//List<Caracteristica> caracteristicasActividad = getCaracteristicasAc(item, consAHYI,1,2);
		//List<Caracteristica> caracteristicasActividad = mapa.get(item.getIdItemPlan());	

		int pos = 0;
		//construir el vector de actividad seg&uacuten el orden de las caracteristicas
		for(int i = 0; i < caracteristicas.size(); i++)
		{
			for(Caracteristica cG : caracteristicas.get(i))
			{
				int encuentra = 0;
				for(Caracteristica cA : caracteristicasActividad )
				{
					if(cG.getIdCaracteristica() == cA.getIdCaracteristica())
					{
						actividad[pos] = 1;
						encuentra++;
						break;
					}
				}
				if(encuentra == 0)
					actividad[pos] = 0;
				pos++;
			}
		}
		return actividad;
	}

	/**
	 * Obtiene una lista con los estilos, personalidades, habilidades y competencias.
	 * @return caracteristicas la lista.
	 */
	public List<List<Caracteristica>> getCaracteristicas() 
	{
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
	 * Retorna el &uacuteltimo itemPlan creado.
	 * @return el itemPlan.
	 */
	public int getUltimoItemPlan() {

		return ashyiToolDao.getUltimoItemPlan();
	}

	/**
	 * Retorna los items de un grafo.
	 * @param idActividad
	 * @return los items.
	 */
	public List<Integer> getItemsGrafo(Integer idActividad) {

		String idUsuarioSakai = getCurrentUserId();
		Integer idUsuario = ashyiToolDao.getUltimoUsuario(idUsuarioSakai);

		return ashyiToolDao.getItemsGrafo(idUsuario, idActividad);

	}

	/**
	 * Deshabilita una actividad.
	 * @param idActividad
	 * @param opcion
	 * @return resultado de la operaci&oacuten.
	 */
	public boolean desHabilitarActividad(Integer idActividad, int opcion) {
		List<ItemPlan> iPs = ashyiToolDao.getItemsPlanActividad(idActividad);
		for(ItemPlan iP : iPs)
		{
			if(opcion == 1)
			{
				System.out.println("Deshabilitar actividad: "+idActividad);
				//				if(ashyiToolDao.estaItemEnUsuarios(iP.getIdItemPlan()))
				//				{
				System.out.println("Actividad en varios items usuario "+idActividad);
				if(ashyiToolDao.hayActividadesMismoObjetivo(idActividad))
				{					
					iP.setEstaActivo(false);
				}
				else
					return false;
				//				}
			}

			if(opcion == 2)
			{
				System.out.println("Habilitar actividad: "+idActividad);
				iP.setEstaActivo(true);
			}
			update(iP);
		}

		return true;

	}

	/**
	 * Revisa un cambio en las actividades de la unidad.
	 * @param nombre nombre de unidad.
	 * @param nR nivel de recursividad.
	 * @return true si hubo, false si no.
	 */
	public boolean isCambioActividadesUnidad(String nombre, int nR) {

		Actividad unidad = ashyiToolDao.getactividad(nombre, nR);

		MainAgentData datosAgMain = (MainAgentData) bean.getDatosAgente(getAgenteComunicacion());

		if (datosAgMain.getEstadoActividadesMapa(unidad.getIdActividad()))
			return true;

		return false;
	}

	/**
	 * Retorna los usuarios Ashyi.
	 * @return usuarios los usuarios.
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
	 * Carga un archivo de exports.
	 * @return
	 */
	public String uploadExportFile()
	{
		System.out.println("Number of uploaded files: " + multipartMap.size());
		System.out.println("Number of uploaded files: " + multipartMap.keySet());		
		System.out.println("Archivo: "+multipartMap.get("archivo1").getContentType());
		System.out.println("Archivo: "+multipartMap.get("archivo1").getOriginalFilename());

		//String[] extension = multipartMap.get("archivo1").getName().split("\\.");
		String nombre = multipartMap.get("archivo1").getOriginalFilename();

		System.out.println("Nombre: "+nombre);

		String nombreArchivo = System.currentTimeMillis()+"-"+idTP+"-"+idTU+"-"+nombre;

		System.out.println("NombreArchivo: "+nombreArchivo);

		try {
			InputStreamAFile(multipartMap.get("archivo1").getInputStream(), nombreArchivo, idTP, idTU, idRIU);
			confirmacionEnvio();
		} catch (IOException e) {
			rechazoEnvio();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Retorna propiedades de Ashyi
	 * @param propiedad la propiedad.
	 * @return
	 */
	public String getPropiedadAshyi(String propiedad)
	{
		try{
			Properties propiedades = new Properties();

			File miDir = new File (".");
			try {
				System.out.println ("Directorio actual: " + miDir.getCanonicalPath());
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			/**Cargamos el archivo desde la ruta especificada*/
			propiedades.load(new FileInputStream("../resources/extrasASHYI.properties"));

			/**Obtenemos los parametros definidos en el archivo*/
			String nombre = propiedades.getProperty(propiedad);

			return nombre;

		}catch(IOException e){
			System.out.println("Se produjo el error : "+e.toString());
			return null;
		}
	}

	/**
	 * Revisa el sistema operativo y obtiene la ruta del archivo de respuesta del itemUsuario, luego actualiza el itemUsuario con la respuesta.
	 * @param entrada
	 * @param nombreArchivo
	 * @param idTP
	 * @param idTU
	 * @param idRIU
	 */
	public void InputStreamAFile(InputStream entrada, String nombreArchivo, String idTP, String idTU, String idRIU){
		try{
			System.out.println("NombreArchivoF: "+nombreArchivo);
			//String ruta = getPropiedadAshyi("ashyibean.pathArchivos");
			String sSistemaOperativo = System.getProperty("os.name");
			//System.out.println(sSistemaOperativo);
			File f=null;
			if(sSistemaOperativo.contains("Windows"))
				f=new File(messageLocator.getMessage("ashyibean.pathArchivosW"),nombreArchivo);//Aqui le dan el nombre y/o con la ruta del archivo salida
			else
				f=new File(messageLocator.getMessage("ashyibean.pathArchivosL"),nombreArchivo);//Aqui le dan el nombre y/o con la ruta del archivo salida
			OutputStream salida=new FileOutputStream(f);
			byte[] buf =new byte[1024];//Actualizado me olvide del 1024
			int len;
			while((len=entrada.read(buf))>0){
				salida.write(buf,0,len);
			}
			salida.close();
			entrada.close();

			//almacenar en respuesta
			actualizarRespuestaIP(Integer.valueOf(idTP), Integer.valueOf(idTU), Integer.valueOf(idRIU), nombreArchivo);

			System.out.println("Se realizo la conversion con exito");
		}catch(IOException e){
			System.out.println("Se produjo el error : "+e.toString());
		}
	}

	/**
	 * Actualiza la respuesta de un itemUsuario.
	 * @param idTP el item Plan.
	 * @param idTU el item Usuario.
	 * @param idRIU el id de recurso.
	 * @param nombreArchivo
	 */
	private void actualizarRespuestaIP(Integer idTP, Integer idTU, Integer idRIU, String nombreArchivo) {

		System.out.println("IDrespuesta: "+idRIU);

		RespuestaItemsUsuario riU = ashyiToolDao.getRespuestaItemUsuario(idTP, idTU, idRIU);

		riU.setArchivo(nombreArchivo);

		update(riU);
	}

	/**
	 * Obtiene las respuestas de un itemUsuario.
	 * @param idTP2 itemPlan.
	 * @param idTU2 itemUsuario.
	 * @return nombres las respuestas.
	 */
	public String[] getRespuestasItemUsuario(int idTP2, int idTU2) {	

		List<RespuestaItemsUsuario> rtas = ashyiToolDao.getRespuestasItemUsuario(idTP2, idTU2);
		String[] nombres = new String[rtas.size()];
		for(int i = 0; i < rtas.size(); i++)
			nombres[i] = rtas.get(i).getFecha().toString();
		return nombres;
	}

	/**
	 * Obtiene las respuestas de un itemUsuario.
	 * @param idTP2 itemPlan.
	 * @param idTU2 itemUsuario.
	 * @return las respuestas.
	 */
	public List<RespuestaItemsUsuario> getRespuestasItemUsuarioList(int idTP2, int idTU2) {	

		return ashyiToolDao.getRespuestasItemUsuario(idTP2, idTU2);
	}

	/**
	 * Retorna el objetivo actual que se est&aacute actualizando de la unidad.
	 * @return objetivoUD.
	 */
	public String getObjetivoUD() {
		return objetivoUD;
	}
	/**
	 * Asigna el objetivo actual que se est&aacute actualizando de la unidad.
	 * @param objetivoUD.
	 */
	public void setObjetivoUD(String objetivoUD) {
		this.objetivoUD = objetivoUD;
	}
	/**
	 * Retorna la fecha inicial del objetivo actual que se est&aacute actualizando de la unidad.
	 * @return fechaInicialObjetivoUD.
	 */
	public String getFechaInicialObjetivoUD() {
		return fechaInicialObjetivoUD;
	}
	/**
	 * Asigna la fecha inicial del objetivo actual que se est&aacute actualizando de la unidad.
	 * @param fechaInicialObjetivoUD.
	 */
	public void setFechaInicialObjetivoUD(String fechaInicialObjetivoUD) {
		this.fechaInicialObjetivoUD = fechaInicialObjetivoUD;
	}
	/**
	 * Retorna la fecha final del objetivo actual que se est&aacute actualizando de la unidad.
	 * @return fechaFinalObjetivoUD.
	 */
	public String getFechaFinalObjetivoUD() {
		return fechaFinalObjetivoUD;
	}
	/**
	 * Asigna la fecha final del objetivo actual que se est&aacute actualizando de la unidad.
	 * @param fechaFinalObjetivoUD.
	 */
	public void setFechaFinalObjetivoUD(String fechaFinalObjetivoUD) {
		this.fechaFinalObjetivoUD = fechaFinalObjetivoUD;
	}

	/**
	 * Revisa si las fechas inicial y final asignadas al objetivoUD son v&aacutelidas.
	 * @param fechaInicialObjetivoUD
	 * @param fechaFinalObjetivoUD
	 * @return true si lo son, false si no.
	 */
	public boolean fechasSonValidas(String fechaInicialObjetivoUD, String fechaFinalObjetivoUD)
	{
		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a");
			Date date1 = formatter.parse(fechaInicialObjetivoUD.substring(0, fechaInicialObjetivoUD.length()));	
			Date date2 = formatter.parse(fechaFinalObjetivoUD.substring(0, fechaFinalObjetivoUD.length()));

			//if(date1.before(date2)&&date1.after(new Date()))
			if(date1.before(date2))
			{
				return true;
			}
			else
				return false;
			//System.out.println("tiempo "+tiempo_duracion);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Actualiza los itemPlans del objetivoUD con las fechas fechaInicialObjetivoUD y fechaFinalObjetivoUD.
	 * @return resultado de la operaci&oacuten.
	 */
	public String updateObjetivoUnidad()
	{	
		//System.out.println("!!!!!!!!!!!!!!: "+fechaInicialObjetivoUD+"  !!!!!!!!!!!!!: "+fechaFinalObjetivoUD);
		Actividad ud=ashyiToolDao.getactividad(getCurrentPage().getTitle(), 3);
		List<ItemPlan> items=ashyiToolDao.getItemPlansUD(ud);

		if(!fechasSonValidas(fechaInicialObjetivoUD, fechaFinalObjetivoUD))
			return "";

		for (int i = 0; i < items.size(); i++) {
			ItemPlan item=items.get(i);
			Actividad ac=item.getIdActividad();
			ObjetivosActividad oa=ashyiToolDao.getObjetivosActividad(ac.getIdActividad()).get(0);
			Objetivo obj=oa.getIdObjetivo();

			System.out.println("Buscando objetivo: "+Integer.valueOf(objetivoUD)+" en el obj del item: "+obj.getIdObjetivo()+" del item: "+item.getIdItemPlan());
			if(obj.getIdObjetivo() == Integer.valueOf(objetivoUD).intValue())
			{
				item.setFechas(fechaInicialObjetivoUD, fechaFinalObjetivoUD);
				update(item);
			}
		}
		return "success";
	}

	/**
	 * Consulta los usuarios de sakai tipo instructor
	 * @return lista de usuarios instructor
	 */
	public List<User> getInstructoresAshyi()
	{
		String idSite = getCurrentSiteId();
		List<User> usuarios = simplePageToolDao.getUsuariosSite(idSite);
		for(int i = 0; i<usuarios.size(); i++)
		{
			User us = usuarios.get(i);
			//System.out.println("!!!!!!!!! tipo usu: "+us.getDisplayName()+" tipo: "+us.getType());
			if(!us.getType().equalsIgnoreCase("Instructor"))
			{
				usuarios.remove(i);
				i--;
			}
		}

		return usuarios;
	}

	/**
	 * Consulta la lista de grafos de un usuario
	 * Estos grafos deben pertenecer a una actividad en especifico
	 * @param idUnidad id actividad a consultar
	 * @return lista de grafos de un usuario en una actividad
	 */
	public List<GrafosUsuario> getInstructorsGraphs(Integer idUnidad) {

		List<User> instructores = getInstructoresAshyi();

		for(int i = 0; i < instructores.size(); i++)
		{
			Usuario us = ashyiToolDao.getUsuario(ashyiToolDao.getUltimoUsuario(instructores.get(i).getId()));
			if(us.getIdUsuario() != null)
			{
				List<GrafosUsuario> gUs = ashyiToolDao.getGrafoUsuario(us, idUnidad);
				if(!gUs.isEmpty())
				{
					return gUs;
				}
			}
		}

		return null;
	}

	public String getServerName() {
		String url = httpServletRequest.getRequestURL().toString();
		String partes[] = url.split("portal");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! servidor:   "+partes[0]);
		return partes[0];
	}

	public List<GrafosUsuario> getGrafosActividad(Actividad actAnterior) {
		return ashyiToolDao.getGrafosActividad(actAnterior);		 
	}

	public List getItemPlansUD(Actividad actAnterior) {

		return ashyiToolDao.getItemPlansUD(actAnterior);
	}

	public String getReportesLabel() {
		return reportesLabel;
	}
	public void setReportesLabel(String reportesLabel) {
		this.reportesLabel = reportesLabel;
	}
	public String getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(String usuarios) {
		this.usuarios = usuarios;
	}
	public String getDatosTabla() {
		return datosTabla;
	}
	public void setDatosTabla(String datosTabla) {
		this.datosTabla = datosTabla;
	}
	public String getDatosGrafos() {
		return datosGrafos;
	}
	public void setDatosGrafos(String datosGrafos) {
		this.datosGrafos = datosGrafos;
	}
	public String getDatosGrafos2() {
		return datosGrafos2;
	}
	public void setDatosGrafos2(String datosGrafos2) {
		this.datosGrafos2 = datosGrafos2;
	}
	public String getDatosTabla2() {
		return datosTabla2;
	}
	public void setDatosTabla2(String datosTabla2) {
		this.datosTabla2 = datosTabla2;
	}
	
	/**
	 * Busca los recursos referentes a un objetivo de una avtividad
	 * @param idObjetivo id objetivo a buscar
	 * @param idActividad id de ka unidad didactica
	 * @return lista de los nombres de los archivos relacionados
	 */
	public List<String> getRespuestasObjetivoUnidad(int idObjetivo, int idActividad) {
				
		return ashyiToolDao.getRespuestasObjetivoUnidad(idObjetivo, idActividad);
	}
	
	public String getUsuActivos() {
		return usuActivos;
	}
	public void setUsuActivos(String usuActivos) {
		this.usuActivos = usuActivos;
	}
	
	/**
	 * Busca los nombres de los usuarios asociados a los recursos referentes a un objetivo de una avtividad
	 * @param idObjetivo id objetivo a buscar
	 * @param idActividad id de ka unidad didactica
	 * @return lista de los nombres de los usuarios duenios de los archivos relacionados
	 */
	public List<String> getUsuariosRespuestasObjetivoUnidad(int idObjetivo, int idActividad) {
		
		return ashyiToolDao.getUsuariosRespuestasObjetivoUnidad(idObjetivo, idActividad);
	}
	
	
	
public String getReportesLabel2() {
		return reportesLabel2;
	}
	public void setReportesLabel2(String reportesLabel2) {
		this.reportesLabel2 = reportesLabel2;
	}
public void confirmacionEnvio() {

		
		Usuario us = ashyiToolDao.getUsuario(Integer.parseInt(idTU));
		String usuaSakai=us.getIdUsuarioSakai();
		List<User> usuSakai=getUsuariosAshyi();
		String mail="";
		for(User u:usuSakai){
			if(u.getId().equals(usuaSakai)){
				System.out.println("EMAIL.EMAIL"+u.getEmail());
				mail=u.getEmail();


				break;
			}
		}
		
		final String username = "jgonzalez.v@javeriana.edu.co";
		final String password = "contraseña";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			System.out.println("EMAIL ESTUDIANTE2:"+mail);
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(mail));
			message.setSubject("Confirmacion envio");
			message.setText("El envio de la actividad se realizó satisfactoriamente");
 
			Transport.send(message);
 

 
		} catch (MessagingException e) {
		    
			throw new RuntimeException(e);
		}
	}
	
	public void rechazoEnvio() {
	
		
		Usuario us = ashyiToolDao.getUsuario(Integer.parseInt(idTU));
		String usuaSakai=us.getIdUsuarioSakai();
		List<User> usuSakai=getUsuariosAshyi();
		String mail="";
		for(User u:usuSakai){
			if(u.getId().equals(usuaSakai)){
				System.out.println("EMAIL.EMAIL"+u.getEmail());
				mail=u.getEmail();
	
	
				break;
			}
		}
		
		final String username = "jgonzalez.v@javeriana.edu.co";
		final String password = "contraseña";
	
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.port", "587");
	
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
	
		try {
	
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			System.out.println("EMAIL ESTUDIANTE2:"+mail);
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(mail));
			message.setSubject("Rechazo envio");
			message.setText("El envio de la actividad no se realizó correctamente. Vuelva a intentarlo");
	
			Transport.send(message);
	
	
	
		} catch (MessagingException e) {
		    
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Obtener el parametro id del grafo a consultar
	 * @return id del grafo
	 */
	public int getIdGrafoURL() {
		
		return Integer.valueOf(httpServletRequest.getParameter("id"));
	}
}
