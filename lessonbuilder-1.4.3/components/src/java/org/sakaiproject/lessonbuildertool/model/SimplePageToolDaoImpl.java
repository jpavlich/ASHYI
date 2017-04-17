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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.cover.SqlService;
import org.sakaiproject.event.cover.EventTrackingService;
import org.sakaiproject.lessonbuildertool.ActividadImpl;
import org.sakaiproject.lessonbuildertool.CaracteristicaImpl;
import org.sakaiproject.lessonbuildertool.ControlImpl;
import org.sakaiproject.lessonbuildertool.GrafosUsuarioImpl;
import org.sakaiproject.lessonbuildertool.ItemImpl;
import org.sakaiproject.lessonbuildertool.ItemPlanImpl;
import org.sakaiproject.lessonbuildertool.ObjetivoImpl;
import org.sakaiproject.lessonbuildertool.RecursoImpl;
import org.sakaiproject.lessonbuildertool.SimplePage;
import org.sakaiproject.lessonbuildertool.SimplePageComment;
import org.sakaiproject.lessonbuildertool.SimplePageCommentImpl;
import org.sakaiproject.lessonbuildertool.SimplePageGroup;
import org.sakaiproject.lessonbuildertool.SimplePageGroupImpl;
import org.sakaiproject.lessonbuildertool.SimplePageImpl;
import org.sakaiproject.lessonbuildertool.SimplePageItem;
import org.sakaiproject.lessonbuildertool.SimplePageItemImpl;
import org.sakaiproject.lessonbuildertool.SimplePageLogEntry;
import org.sakaiproject.lessonbuildertool.SimplePageLogEntryImpl;
import org.sakaiproject.lessonbuildertool.SimplePageProperty;
import org.sakaiproject.lessonbuildertool.SimplePagePropertyImpl;
import org.sakaiproject.lessonbuildertool.SimpleStudentPage;
import org.sakaiproject.lessonbuildertool.SimpleStudentPageImpl;
import org.sakaiproject.lessonbuildertool.TipoImpl;
import org.sakaiproject.lessonbuildertool.UsuarioImpl;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.sakaiproject.lessonbuildertool.model.Actividad;
import org.sakaiproject.lessonbuildertool.model.ActividadTieneActividad;
import org.sakaiproject.lessonbuildertool.model.Caracteristica;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaActividad;
import org.sakaiproject.lessonbuildertool.model.CaracteristicaRecurso;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasTipo;
import org.sakaiproject.lessonbuildertool.model.CaracteristicasUsuario;
import org.sakaiproject.lessonbuildertool.model.Control;
import org.sakaiproject.lessonbuildertool.model.DependenciaActividad;
import org.sakaiproject.lessonbuildertool.model.Item;
import org.sakaiproject.lessonbuildertool.model.Objetivo;
import org.sakaiproject.lessonbuildertool.model.ObjetivosActividad;
import org.sakaiproject.lessonbuildertool.model.Recurso;
import org.sakaiproject.lessonbuildertool.model.RecursosActividad;
import org.sakaiproject.lessonbuildertool.model.Tipo;
import org.sakaiproject.lessonbuildertool.model.Usuario;

/**
 * @author ashiy
 * Implementaci&oacuten de la interface SimplePageToolDao.
 */
/**
 * @author ashiy
 *
 */
public class SimplePageToolDaoImpl extends HibernateDaoSupport implements SimplePageToolDao {
	/**
	 * Atributo del SimplePageToolDaoImpl.
	 */
	private static Log log = LogFactory.getLog(SimplePageToolDaoImpl.class);
	/**
	 * Atributo del SimplePageToolDaoImpl.
	 */
	private ToolManager toolManager;
	/**
	 * Atributo del SimplePageToolDaoImpl.
	 */
	private SecurityService securityService;
	/**
	 * Atributo del SimplePageToolDaoImpl.
	 */
	private static String SITE_UPD = "site.upd";
	/**
	 * Atributo del SimplePageToolDaoImpl.
	 */
	private DataSource dataSource;
	/**
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getDataSource()
	 */
	public DataSource getDataSource() {
		return this.dataSource;
	}

	// part of HibernateDaoSupport; this is the only context in which it is OK
	// to modify the template configuration
	/* (non-Javadoc)
	 * @see org.springframework.dao.support.DaoSupport#initDao()
	 */
	protected void initDao() throws Exception {
		super.initDao();
		getHibernateTemplate().setCacheQueries(true);
		log.info("initDao template " + getHibernateTemplate());
	}

	// the permissions model here is preliminary. I'm not convinced that all the code in
	// upper layers checks where it should, so the Dao is supplying an extra layer of
	// protection. As far as I can tell, any database change should be done by
	// someone with update privs, except that add or update to the log is done on
	// behalf of normal people. I've checked all the code that does save or update for
	// log entries and it looks OK.

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#canEditPage()
	 */
	public boolean canEditPage() {
		String ref = null;
		// no placement, startup testing, should be an advisor in place
		try {
			ref = "/site/" + toolManager.getCurrentPlacement().getContext();
		} catch (java.lang.NullPointerException ignore) {
			ref = "";
		}
		return securityService.unlock(SimplePage.PERMISSION_LESSONBUILDER_UPDATE, ref);
	}

	/**
	 * Retorna si se puede editar una p&aacutegina.
	 * @param pageId
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean canEditPage(long pageId) {
		boolean canEdit = canEditPage();
		// forced comments have a pageid of -1, because they are associated with
		// more than one page. But the student can't edit them anyway, so fail it
		if(!canEdit && pageId != -1L) {
			SimplePage page = getPage(pageId);
			if(UserDirectoryService.getCurrentUser().getId()
					.equals(page.getOwner())) {
				canEdit = true;
			}
		}

		return canEdit;
	}
	/**
	 * Retorna si se puede editar una p&aacutegina.
	 * @param owner
	 * @return true o false - seg&uacuten el resultado.
	 */
	public boolean canEditPage(String owner) {
		boolean canEdit = canEditPage();
		if(owner != null && !canEdit) {
			if(owner.equals(UserDirectoryService.getCurrentUser().getId())) {
				canEdit = true;
			}
		}

		return canEdit;
	}

	/**
	 * Asigna el atributo securityService.
	 * @param service
	 */
	public void setSecurityService(SecurityService service) {
		securityService = service;
	}

	/**
	 * Asigna el atributo toolManager.
	 * @param service
	 */
	public void setToolManager(ToolManager service) {
		toolManager = service;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findItemsOnPage(long)
	 */
	public List<SimplePageItem> findItemsOnPage(long pageId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("pageId", pageId));
		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		Collections.sort(list, new Comparator<SimplePageItem>() {
			public int compare(SimplePageItem a, SimplePageItem b) {
				return Integer.valueOf(a.getSequence()).compareTo(b.getSequence());
			}
		});

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#flush()
	 */
	public void flush() {
		getHibernateTemplate().flush();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findItemsInSite(java.lang.String)
	 */
	public List<SimplePageItem> findItemsInSite(String siteId) {
		Object [] fields = new Object[1];
		fields[0] = siteId;
		List<String> ids = SqlService.dbRead("select b.id from lesson_builder_pages a,lesson_builder_items b,SAKAI_SITE_PAGE c where a.siteId = ? and a.parent is null and a.pageId = b.sakaiId and b.type = 2 and b.pageId = 0 and a.toolId = c.PAGE_ID order by c.SITE_ORDER", fields, null);

		List<SimplePageItem> result = new ArrayList<SimplePageItem>();

		if (result != null) {
			for (String id: ids) {
				SimplePageItem i = findItem(new Long(id));
				result.add(i);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findDummyItemsInSite(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findDummyItemsInSite(java.lang.String)
	 */
	public List<SimplePageItem> findDummyItemsInSite(String siteId) {
		Object [] fields = new Object[1];
		fields[0] = siteId;
		List<String> ids = SqlService.dbRead("select b.id from lesson_builder_pages a,lesson_builder_items b where a.siteId = ? and a.pageId = b.pageId and b.sakaiId = '/dummy'", fields, null);

		List<SimplePageItem> result = new ArrayList<SimplePageItem>();

		if (result != null) {
			for (String id: ids) {
				SimplePageItem i = findItem(new Long(id));
				result.add(i);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findTextItemsInSite(java.lang.String)
	 */
	public List<SimplePageItem> findTextItemsInSite(String siteId) {
		Object [] fields = new Object[1];
		fields[0] = siteId;
		List<String> ids = SqlService.dbRead("select b.id from lesson_builder_pages a,lesson_builder_items b where a.siteId = ? and a.pageId = b.pageId and b.type = 5", fields, null);

		List<SimplePageItem> result = new ArrayList<SimplePageItem>();

		if (result != null) {
			for (String id: ids) {
				SimplePageItem i = findItem(new Long(id));
				result.add(i);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findMostRecentlyVisitedPage(java.lang.String, java.lang.String)
	 */
	public PageData findMostRecentlyVisitedPage(final String userId, final String toolId) {
		Object [] fields = new Object[4];
		fields[0] = userId;
		fields[1] = toolId;
		fields[2] = userId;
		fields[3] = toolId;

		List<PageData> rv = SqlService.dbRead("select a.itemId, a.id, b.sakaiId, b.name from lesson_builder_log a, lesson_builder_items b where a.userId=? and a.toolId=? and a.lastViewed = (select max(lastViewed) from lesson_builder_log where userId=? and toolId = ?) and a.itemId = b.id", fields, new SqlReader() {
			public Object readSqlResultRecord(ResultSet result) {
				try {
					PageData ret = new PageData();
					ret.itemId = result.getLong(1);
					ret.pageId = result.getLong(3);
					ret.name = result.getString(4);

					return ret;
				} catch (SQLException e) {
					log.warn("findMostRecentlyVisitedPage: " + toolId + " : " + e);
					return null;
				}
			}
		});


		if (rv != null && rv.size() > 0)
			return rv.get(0);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findItem(long)
	 */
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findItem(long)
	 */
	public SimplePageItem findItem(long id) {

		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("id", id));
		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findProperty(java.lang.String)
	 */
	public SimplePageProperty findProperty(String attribute) {

		DetachedCriteria d = DetachedCriteria.forClass(SimplePageProperty.class).add(Restrictions.eq("attribute", attribute));

		List<SimplePageProperty> list = null;
		try {
			list = getHibernateTemplate().findByCriteria(d);
		} catch (org.hibernate.ObjectNotFoundException e) {
			return null;
		}

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeProperty(java.lang.String, java.lang.String)
	 */
	public SimplePageProperty makeProperty(String attribute, String value) {
		return new SimplePagePropertyImpl(attribute, value);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findComments(long)
	 */
	public List<SimplePageComment> findComments(long commentWidgetId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageComment.class).add(Restrictions.eq("itemId", commentWidgetId));
		List<SimplePageComment> list = getHibernateTemplate().findByCriteria(d);

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findCommentsOnItems(java.util.List)
	 */
	public List<SimplePageComment> findCommentsOnItems(List<Long> commentItemIds) {
		if ( commentItemIds == null || commentItemIds.size() == 0)
			return new ArrayList<SimplePageComment>();

		DetachedCriteria d = DetachedCriteria.forClass(SimplePageComment.class).add(Restrictions.in("itemId", commentItemIds));
		List<SimplePageComment> list = getHibernateTemplate().findByCriteria(d);

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findCommentsOnItemsByAuthor(java.util.List, java.lang.String)
	 */
	public List<SimplePageComment> findCommentsOnItemsByAuthor(List<Long> commentItemIds, String author) {
		if ( commentItemIds == null || commentItemIds.size() == 0)
			return new ArrayList<SimplePageComment>();

		DetachedCriteria d = DetachedCriteria.forClass(SimplePageComment.class).add(Restrictions.in("itemId", commentItemIds))
				.add(Restrictions.eq("author", author));
		List<SimplePageComment> list = getHibernateTemplate().findByCriteria(d);

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findCommentsOnItemByAuthor(long, java.lang.String)
	 */
	public List<SimplePageComment> findCommentsOnItemByAuthor(long commentWidgetId, String author) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageComment.class)
				.add(Restrictions.eq("itemId", commentWidgetId))
				.add(Restrictions.eq("author", author));

		List<SimplePageComment> list = getHibernateTemplate().findByCriteria(d);

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findCommentsOnPageByAuthor(long, java.lang.String)
	 */
	public List<SimplePageComment> findCommentsOnPageByAuthor(long pageId, String author) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageComment.class)
				.add(Restrictions.eq("pageId", pageId))
				.add(Restrictions.eq("author", author));

		List<SimplePageComment> list = getHibernateTemplate().findByCriteria(d);

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findCommentById(long)
	 */
	public SimplePageComment findCommentById(long commentId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageComment.class).add(Restrictions.eq("id", commentId));
		List<SimplePageComment> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findCommentByUUID(java.lang.String)
	 */
	public SimplePageComment findCommentByUUID(String commentUUID) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageComment.class).add(Restrictions.eq("UUID", commentUUID));
		List<SimplePageComment> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findCommentsToolBySakaiId(java.lang.String)
	 */
	public SimplePageItem findCommentsToolBySakaiId(String sakaiId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("sakaiId", sakaiId));
		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		// We loop through and check type here in-case something else has the same
		// sakaiId, and to prevent creating a new index for something that probably
		// doesn't really need it.  There shouldn't be more than a couple of matches
		// with different types.
		for(SimplePageItem item : list) {
			if(item.getType() == SimplePageItem.COMMENTS) {
				return item;
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findItemsBySakaiId(java.lang.String)
	 */
	public List<SimplePageItem> findItemsBySakaiId(String sakaiId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("sakaiId", sakaiId));
		return getHibernateTemplate().findByCriteria(d);
	}


	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findStudentPage(long, java.lang.String)
	 */
	public SimpleStudentPage findStudentPage(long itemId, String owner) {
		DetachedCriteria d = DetachedCriteria.forClass(SimpleStudentPage.class).add(Restrictions.eq("itemId", itemId))
				.add(Restrictions.eq("owner", owner)).add(Restrictions.eq("deleted", false));
		List<SimpleStudentPage> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findStudentPage(long)
	 */
	public SimpleStudentPage findStudentPage(long id) {
		DetachedCriteria d = DetachedCriteria.forClass(SimpleStudentPage.class).add(Restrictions.eq("id", id));
		List<SimpleStudentPage> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findStudentPageByPageId(long)
	 */
	public SimpleStudentPage findStudentPageByPageId(long pageId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimpleStudentPage.class).add(Restrictions.eq("pageId", pageId));
		List<SimpleStudentPage> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findStudentPages(long)
	 */
	public List<SimpleStudentPage> findStudentPages(long itemId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimpleStudentPage.class).add(Restrictions.eq("itemId", itemId));
		List<SimpleStudentPage> list = getHibernateTemplate().findByCriteria(d);

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findItemFromStudentPage(long)
	 */
	public SimplePageItem findItemFromStudentPage(long pageId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimpleStudentPage.class).add(Restrictions.eq("pageId", pageId));

		List<SimpleStudentPage> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0) {
			return findItem(list.get(0).getItemId());
		}else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findTopLevelPageItemBySakaiId(java.lang.String)
	 */
	public SimplePageItem findTopLevelPageItemBySakaiId(String id) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("sakaiId", id))
				.add(Restrictions.eq("pageId", 0L))
				.add(Restrictions.eq("type",SimplePageItem.PAGE));

		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		if (list == null || list.size() < 1)
			return null;

		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findPageItemsBySakaiId(java.lang.String)
	 */
	public List<SimplePageItem> findPageItemsBySakaiId(String id) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("sakaiId", id)).
				add(Restrictions.eq("type",SimplePageItem.PAGE));

		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findControlledResourcesBySakaiId(java.lang.String, java.lang.String)
	 */
	public List findControlledResourcesBySakaiId(String id, String siteId) {
		Object [] fields = new Object[2];
		fields[0] = id;
		fields[1] = siteId;
		List ids = SqlService.dbRead("select a.id from lesson_builder_items a, lesson_builder_pages b where a.sakaiId = ? and ( a.type=1 or a.type=7) and a.prerequisite = 1 and a.pageId = b.pageId and b.siteId = ?", fields, null);
		return ids;

	}


	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findNextPageItemOnPage(long, int)
	 */
	public SimplePageItem findNextPageItemOnPage(long pageId, int sequence) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("pageId", pageId)).
				add(Restrictions.eq("sequence", sequence+1)).
				add(Restrictions.eq("type",SimplePageItem.PAGE));

		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		if (list == null || list.size() < 1)
			return null;

		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findNextItemOnPage(long, int)
	 */
	public SimplePageItem findNextItemOnPage(long pageId, int sequence) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("pageId", pageId)).
				add(Restrictions.eq("sequence", sequence+1));

		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		if (list == null || list.size() < 1)
			return null;

		return list.get(0);
	}

	/**
	 * Retorna una acausa de una excepci&oacuten.
	 * @param t - la excepci&oacuten.
	 * @param elist - la lista de errores.
	 */
	public void getCause(Throwable t, List<String>elist) {
		while (t.getCause() != null) {
			t = t.getCause();
		}
		log.warn("error saving or updating: " + t.toString());
		elist.add(t.getLocalizedMessage());
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#saveItem(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveItem(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {

		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof SimplePageItem && canEditPage(((SimplePageItem)o).getPageId()))
				&& !(o instanceof SimplePage && canEditPage(((SimplePage)o).getOwner()))
				&& !(o instanceof SimplePageLogEntry)
				&& !(o instanceof SimplePageGroup)) {
			elist.add(nowriteerr);
			return false;
		}

		try {
			getHibernateTemplate().save(o);

			if (o instanceof SimplePageItem) {
				SimplePageItem i = (SimplePageItem)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getId(), true));
			} else if (o instanceof SimplePage) {
				SimplePage i = (SimplePage)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/page/" + i.getPageId(), true));
			} 

			if(o instanceof SimplePageItem || o instanceof SimplePage) {
				updateStudentPage(o);
			}

			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}


	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#saveActividad(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {

		if(requiresEditPermission && !(o instanceof Actividad ) ){
			elist.add(nowriteerr);
			return false;
		}

		try {
			getHibernateTemplate().save(o);
			//getHibernateTemplate().setFlushMode(FlushMode.COMMIT);
			getHibernateTemplate().flush();
			getHibernateTemplate().clear();
			//getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction().commit();

			//		    	


			//    			i.setCaracteristicas(caracteristicaNueva);
			//		    	
			//		    	getHibernateTemplate().merge(i);

			//getHibernateTemplate().merge(i);
			//    			i.setIdActividad(((Actividad)ac).getIdActividad());
			//    			getHibernateTemplate().flush();
			//    			
			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}	

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUltimaActividad(java.lang.String)
	 */
	public int getUltimaActividad(String nombre)
	{
		int id = 0;

		Object [] fields = new Object[1];
		fields[0] = nombre;
		List<String> list = SqlService.dbRead("SELECT MAX(idActividad) AS id FROM actividad WHERE nombre = ?",fields,null);		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUltimaActividad()
	 */
	public int getUltimaActividad()
	{
		int id = 0;

		List<String> list = SqlService.dbRead("SELECT MAX(idActividad) AS id FROM actividad");		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUltimoRecurso()
	 */
	public Recurso getUltimoRecurso()
	{
		List<String> list = SqlService.dbRead("SELECT MAX(idRecurso) AS id FROM recurso");		

		int id=0;

		if(list.size() > 0)    	
		{
			id = Integer.valueOf(list.get(0)) ;
			DetachedCriteria d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",id));

			List<Recurso> listR = getHibernateTemplate().findByCriteria(d);

			if(listR.size() > 0)
			{
				return listR.get(0);
			}
		}

		return new RecursoImpl("Does not exist","No aplica","");
	}


	//public boolean saveCaracteristicasActividades(List<CaracteristicaActividad> caracteristicas)
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#saveCaracteristicasActividades(org.sakaiproject.lessonbuildertool.model.CaracteristicaActividad)
	 */
	public boolean saveCaracteristicasActividades(CaracteristicaActividad caracteristicas)
	{
		getHibernateTemplate().save(caracteristicas);
		getHibernateTemplate().flush();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#saveCaracteristica(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveCaracteristica(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {

		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof Caracteristica ) 
				&& !(o instanceof RecursosActividad ) 
				&& !(o instanceof ObjetivosActividad ) 
				&& !(o instanceof Objetivo)
				&& !(o instanceof Recurso) 
				&& !(o instanceof ActividadTieneActividad)
				&& !(o instanceof Control)
				&& !(o instanceof Item)
				&& !(o instanceof CaracteristicaRecurso)
				&& !(o instanceof Usuario)
				&& !(o instanceof CaracteristicasUsuario)
				&& !(o instanceof Tipo)
				&& !(o instanceof ItemsUsuario)) {
			elist.add(nowriteerr);
			return false;
		}

		try {
			getHibernateTemplate().save(o);
			//getHibernateTemplate().flush();

			if (o instanceof CaracteristicasUsuario) {
				CaracteristicasUsuario i = (CaracteristicasUsuario)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdUsuario(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof Usuario) {
				Usuario i = (Usuario)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdUsuario(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof CaracteristicaRecurso) {
				CaracteristicaRecurso i = (CaracteristicaRecurso)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdCaracteristica(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof Caracteristica) {
				Caracteristica i = (Caracteristica)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdCaracteristica(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof ObjetivosActividad) {
				ObjetivosActividad i = (ObjetivosActividad)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" , true));
				//		updateStudentPage(o);
			} 

			if (o instanceof RecursosActividad) {
				RecursosActividad i = (RecursosActividad)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdRecursosActividad(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof Objetivo) {
				Objetivo i = (Objetivo)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdObjetivo(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof ActividadTieneActividad) {
				ActividadTieneActividad i = (ActividadTieneActividad)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
				//		updateStudentPage(o);
			} 	 

			if (o instanceof Recurso) {
				Recurso i = (Recurso)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdRecurso(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof Control) {
				Control i = (Control)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdcontrol_ashyi(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof Tipo) {
				Tipo i = (Tipo)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdTipo(), true));
				//		updateStudentPage(o);
			} 

			if (o instanceof Item) {
				Item i = (Item)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdItem(), true));
				//		updateStudentPage(o);
			}

			if (o instanceof ItemsUsuario) {
				ItemsUsuario i = (ItemsUsuario)o;
				EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdItemPlan(), true));
				//		updateStudentPage(o);
			}

			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#saveItemUsuario(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveItemUsuario(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {
		getHibernateTemplate().save(o);
		return true;
	}

	// for use within copytransfer. We don't need to do permissions, and it probably
	// doesn't make sense to log every item created
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#quickSaveItem(java.lang.Object)
	 */
	public boolean quickSaveItem(Object o) {
		try {
			Object id = getHibernateTemplate().save(o);
			return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.warn("Hibernate could not save: " + e.toString());
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#deleteItem(java.lang.Object)
	 */
	public boolean deleteItem(Object o) {
		/*
		 * If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 * permissions on it. If the item isn't SimplePageItem or SimplePage, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(!(o instanceof SimplePageItem && canEditPage(((SimplePageItem)o).getPageId()))
				&& !(o instanceof SimplePage && canEditPage(((SimplePage)o).getOwner()))
				&& (o instanceof SimplePage || o instanceof SimplePageItem)) {
			return false;
		}

		if (o instanceof SimplePageItem) {
			SimplePageItem i = (SimplePageItem)o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/item/" + i.getId(), true));
		} else if (o instanceof SimplePage) {
			SimplePage i = (SimplePage)o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/page/" + i.getPageId(), true));
		} else if(o instanceof SimplePageComment) {
			SimplePageComment i = (SimplePageComment) o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.delete", "/lessonbuilder/comment/" + i.getId(), true));
		}

		try {
			getHibernateTemplate().delete(o);
			return true;
		} catch (DataAccessException e) {
			try {

				/* If we have multiple objects of the same item, you must merge them
				 * before deleting.  If the first delete fails, we merge and try again.
				 */
				getHibernateTemplate().delete(getHibernateTemplate().merge(o));

				return true;
			}catch(DataAccessException ex) {
				ex.printStackTrace();
				log.warn("Hibernate could not delete: " + e.toString());
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#update(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean update(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission) {
		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof SimplePageItem && canEditPage(((SimplePageItem)o).getPageId()))
				&& !(o instanceof SimplePage && canEditPage(((SimplePage)o).getOwner()))
				&& !(o instanceof SimplePageLogEntry)
				&& !(o instanceof SimplePageGroup)
				&& !(o instanceof SimplePage)
				&& !(o instanceof Actividad)
				&& !(o instanceof ActividadTieneActividad)
				&& !(o instanceof ItemsUsuario)) {
			elist.add(nowriteerr);
			return false;
		}

		if (o instanceof SimplePageItem) {
			SimplePageItem i = (SimplePageItem)o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.update", "/lessonbuilder/item/" + i.getId(), true));
		} else if (o instanceof SimplePage) {
			SimplePage i = (SimplePage)o;
			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.update", "/lessonbuilder/page/" + i.getPageId(), true));
		}

		try {
			if(!(o instanceof SimplePageLogEntry)&& !(o instanceof Actividad)) {
				getHibernateTemplate().merge(o);
			}
			else {
				// Updating seems to always update the timestamp on the log correctly,
				// while merging doesn't always get it right.  However, it's possible that
				// update will fail, so we do both, in order of preference.
				try {
					getHibernateTemplate().update(o);
				}catch(DataAccessException ex) {
					log.warn("Wasn't able to update log entry, timing might be a bit off.");
					getHibernateTemplate().merge(o);
				}
			}

			if(o instanceof SimplePageItem || o instanceof SimplePage) {
				updateStudentPage(o);
			}

			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	// ditto for update
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#quickUpdate(java.lang.Object)
	 */
	public boolean quickUpdate(Object o) {
		try {
			getHibernateTemplate().merge(o);
			return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getTopLevelPageId(java.lang.String)
	 */
	public Long getTopLevelPageId(String toolId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePage.class).add(Restrictions.eq("toolId", toolId)).add(Restrictions.isNull("parent"));

		List list = getHibernateTemplate().findByCriteria(d);

		if (list.size() > 1) {
			log.warn("Problem finding which page we should be on.  Doing the best we can.");
		}

		if (list != null && list.size() > 0) {
			return ((SimplePage) list.get(0)).getPageId();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getPage(long)
	 */
	public SimplePage getPage(long pageId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePage.class).add(Restrictions.eq("pageId", pageId));

		List l = getHibernateTemplate().findByCriteria(d);

		if (l != null && l.size() > 0) {
			return (SimplePage) l.get(0);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getSitePages(java.lang.String)
	 */
	public List<SimplePage> getSitePages(String siteId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePage.class).add(Restrictions.eq("siteId", siteId));

		List<SimplePage> l = getHibernateTemplate().findByCriteria(d);

		if (l != null && l.size() > 0) {
			return l;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getLogEntry(java.lang.String, long, java.lang.Long)
	 */
	public SimplePageLogEntry getLogEntry(String userId, long itemId, Long studentPageId) {
		if(studentPageId.equals(-1L)) studentPageId = null;

		DetachedCriteria d = DetachedCriteria.forClass(SimplePageLogEntry.class).add(Restrictions.eq("userId", userId))
				.add(Restrictions.eq("itemId", itemId));

		if(studentPageId != null) {
			d.add(Restrictions.eq("studentPageId", studentPageId));
		}else {
			d.add(Restrictions.isNull("studentPageId"));
		}

		List l = getHibernateTemplate().findByCriteria(d);

		if (l != null && l.size() > 0) {
			return (SimplePageLogEntry) l.get(0);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#isPageVisited(long, java.lang.String, java.lang.String)
	 */
	public boolean isPageVisited(long pageId, String userId, String owner) {
		// if this is a student page, it's most likely the top level, so do that query first
		if (owner != null) {
			Object [] fields = new Object[3];
			fields[0] = pageId;
			fields[1] = pageId;
			fields[2] = userId;
			List<String> ones = SqlService.dbRead("select 1 from lesson_builder_student_pages a, lesson_builder_log b where a.pageId=? and a.itemId = b.itemId and b.studentPageId=? and b.userId=?", fields, null);
			if (ones != null && ones.size() > 0)
				return true;
		}

		Object [] fields = new Object[2];
		fields[0] = Long.toString(pageId);
		fields[1] = userId;
		List<String> ones = SqlService.dbRead("select 1 from lesson_builder_items a, lesson_builder_log b where a.sakaiId=? and a.type=2 and a.id=b.itemId and b.userId=?", fields, null);
		if (ones != null && ones.size() > 0)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getStudentPageLogEntries(long, java.lang.String)
	 */
	public List<SimplePageLogEntry> getStudentPageLogEntries(long itemId, String userId) {		
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageLogEntry.class).add(Restrictions.eq("userId", userId))
				.add(Restrictions.eq("itemId", itemId))
				.add(Restrictions.isNotNull("studentPageId"));

		List<SimplePageLogEntry> entries = getHibernateTemplate().findByCriteria(d);

		return entries;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findUserWithCompletePages(java.lang.Long)
	 */
	public List<String> findUserWithCompletePages(Long itemId){
		Object [] fields = new Object[1];
		fields[0] = itemId;

		List<String> users = SqlService.dbRead("select a.userId from lesson_builder_log a where a.itemId = ? and a.complete = true", fields, null);

		return users;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findGroup(java.lang.String)
	 */
	public SimplePageGroup findGroup(String itemId) {
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageGroup.class).add(Restrictions.eq("itemId", itemId));

		List l = getHibernateTemplate().findByCriteria(d);

		if (l != null && l.size() > 0) {
			return (SimplePageGroup) l.get(0);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makePage(java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public SimplePage makePage(String toolId, String siteId, String title, Long parent, Long topParent) {
		return new SimplePageImpl(toolId, siteId, title, parent, topParent);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeItem(long, long, int, int, java.lang.String, java.lang.String)
	 */
	public SimplePageItem makeItem(long id, long pageId, int sequence, int type, String sakaiId, String name) {
		return new SimplePageItemImpl(id, pageId, sequence, type, sakaiId, name);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeItem(long, int, int, java.lang.String, java.lang.String)
	 */
	public SimplePageItem makeItem(long pageId, int sequence, int type, String sakaiId, String name) {
		return new SimplePageItemImpl(pageId, sequence, type, sakaiId, name);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeActividad(org.sakaiproject.lessonbuildertool.model.Tipo, org.sakaiproject.lessonbuildertool.model.Item, java.lang.String, java.lang.String, int, int, java.lang.String)
	 */
	public Actividad makeActividad(Tipo tipo, Item item, String name, String description, int nR, int dedicacion, String nivel) {		
		return new ActividadImpl(tipo, item, name, description, nR, dedicacion, nivel);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeActividad(org.sakaiproject.lessonbuildertool.model.Item, java.lang.String, int, int, java.lang.String)
	 */
	public Actividad makeActividad(Item item, String name, int nR,int dedicacion, String nivel) {
		return new ActividadImpl(item, name, nR, dedicacion, nivel);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeCaracteristica(org.sakaiproject.lessonbuildertool.model.Item, java.lang.String)
	 */
	public CaracteristicaImpl makeCaracteristica(Item item, String name) {		
		return new CaracteristicaImpl(item, name);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeTipo(java.lang.String)
	 */
	public TipoImpl makeTipo(String name) {
		return new TipoImpl(name);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeItemAshyi(java.lang.String, java.lang.String)
	 */
	public Item makeItemAshyi(String name, String testC)
	{
		return new ItemImpl( name, testC);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeControl(java.lang.String, boolean)
	 */
	public ControlImpl makeControl(String usuarioId, boolean cambio) {
		int cambiar = 1;
		if(cambio)
			cambiar = 0;
		return new ControlImpl(usuarioId, cambiar);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeGroup(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public SimplePageGroup makeGroup(String itemId, String groupId, String groups, String siteId) {
		return new SimplePageGroupImpl(itemId, groupId, groups, siteId);
	}


	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeLogEntry(java.lang.String, long, java.lang.Long)
	 */
	public SimplePageLogEntry makeLogEntry(String userId, long itemId, Long studentPageId) {
		return new SimplePageLogEntryImpl(userId, itemId, studentPageId);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeComment(long, long, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public SimplePageComment makeComment(long itemId, long pageId, String author, String comment, String UUID, boolean html) {
		return new SimplePageCommentImpl(itemId, pageId, author, comment, UUID, html);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#makeStudentPage(long, long, java.lang.String, java.lang.String, boolean)
	 */
	public SimpleStudentPage makeStudentPage(long itemId, long pageId, String title, String author, boolean groupOwned) {
		return new SimpleStudentPageImpl(itemId, pageId, title, author, groupOwned);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#copyItem(org.sakaiproject.lessonbuildertool.SimplePageItem)
	 */
	public SimplePageItem copyItem(SimplePageItem old) {
		SimplePageItem item =  new SimplePageItemImpl();
		item.setPageId(old.getPageId());
		item.setSequence(old.getSequence());
		item.setType(old.getType());
		item.setSakaiId(old.getSakaiId());
		item.setName(old.getName());
		item.setHtml(old.getHtml());
		item.setDescription(old.getDescription());
		item.setHeight(old.getHeight());
		item.setWidth(old.getWidth());
		item.setAlt(old.getAlt());
		item.setNextPage(old.getNextPage());
		item.setFormat(old.getFormat());
		item.setRequired(old.isRequired());
		item.setAlternate(old.isAlternate());
		item.setPrerequisite(old.isPrerequisite());
		item.setSubrequirement(old.getSubrequirement());
		item.setRequirementText(old.getRequirementText());
		return item;
	}

	/**
	 * @param o
	 */
	private void updateStudentPage(Object o) {
		SimplePage page;

		if(o instanceof SimplePageItem) {
			SimplePageItem item = (SimplePageItem) o;
			page = getPage(item.getPageId());
		}else if(o instanceof SimplePage) {
			page = (SimplePage) o;
		}else {
			return;
		}

		if(page != null && page.getTopParent() != null) {
			SimpleStudentPage studentPage = findStudentPage(page.getTopParent());
			if(studentPage != null) {
				studentPage.setLastUpdated(new Date());
				quickUpdate(studentPage);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#findGradebookItems(java.lang.String)
	 */
	public List<SimplePageItem>findGradebookItems(final String gradebookUid) {

		String hql = "select item from org.sakaiproject.lessonbuildertool.SimplePageItem item, org.sakaiproject.lessonbuildertool.SimplePage page where item.pageId = page.pageId and page.siteId = :site and (item.gradebookId is not null or item.altGradebook is not null)";
		return getHibernateTemplate().findByNamedParam(hql, "site", gradebookUid);
	}

	// items in lesson_builder_groups for specified site, map of itemId to groups
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getExternalAssigns(java.lang.String)
	 */
	public Map<String,String> getExternalAssigns(String siteId) {

		DetachedCriteria d = DetachedCriteria.forClass(SimplePageGroup.class)
				.add(Restrictions.eq("siteId", siteId));

		List<SimplePageGroup> list = getHibernateTemplate().findByCriteria(d);

		Map<String,String>ret = new HashMap<String,String>();	
		for (SimplePageGroup group: list)
			ret.put(group.getItemId(), group.getGroups());

		return ret;

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemsCaracterisiticas()
	 */
	public List<Item> getItemsCaracterisiticas()
	{
		DetachedCriteria d = DetachedCriteria.forClass(Item.class);

		List<Item> list = getHibernateTemplate().findByCriteria(d);

		return  list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getTiposCaracteristicas()
	 */
	public List<Item> getTiposCaracteristicas() {

		//id item > 3, para los tipos de caracteristicas y no sea 8 porque 8 es contexto

		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.gt("idItem",3)).add(Restrictions.ne("idItem",8));

		List<Item> list = getHibernateTemplate().findByCriteria(d);


		//		List<String> list = SqlService.dbRead("select nombre from tipo where tipoTipo = 2");
		//		List<String> list1 = SqlService.dbRead("select idTipo from tipo where tipoTipo = 2");


		//		if (list.size() > 0) {
		//			int i = 0;
		////			for(Caracteristica c : list)
		////			{
		////				listNombres[i]=c.getNombre();
		////				i++;
		////			}
		//			for(i =0; i< list.size();i++)
		//			{
		//				listTipos.add(new TipoImpl(Integer.valueOf(list1.get(i)),
		//						Integer.valueOf(list1.get(i)),
		//						list.get(i)));
		//			}
		//		}

		//return listTipos;
		return  list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getEditarDominioAshyi()
	 */
	public int getEditarDominioAshyi() {
		DetachedCriteria d = DetachedCriteria.forClass(Control.class);//.add(Restrictions.eq("toolId", toolId)).add(Restrictions.isNull("parent"));

		d.addOrder(Order.asc("idcontrol_ashyi"));
		List<Control> list = getHibernateTemplate().findByCriteria(d);

		if (list.size() > 0) {

			if(list.get(list.size() - 1).getEditar_dominio() == 1)
				return 1;
		}
		else if (list.size() == 0)
		{
			return 1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getTiposContexto()
	 */
	public List<Item> getTiposContexto() {

		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",8));

		List<Item> list = getHibernateTemplate().findByCriteria(d);

		//		List<Tipo> listTipos = new ArrayList<Tipo>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from tipo where tipoTipo = 3");
		//		List<String> list1 = SqlService.dbRead("select idTipo from tipo where tipoTipo = 3");
		//
		//
		//		if (list.size() > 0) {
		//			int i = 0;
		////			for(Caracteristica c : list)
		////			{
		////				listNombres[i]=c.getNombre();
		////				i++;
		////			}
		//			for(i =0; i< list.size();i++)
		//			{
		//				listTipos.add(new TipoImpl(Integer.valueOf(list1.get(i)),
		//						Integer.valueOf(list1.get(i)),
		//						list.get(i)));
		//			}
		//		}
		//				
		//		return listTipos;
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getTiposActividad()
	 */
	public List<Tipo> getTiposActividad() {

		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).addOrder(Order.asc("idTipo"));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);

		//		List<Tipo> listTipos = new ArrayList<Tipo>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from tipo where tipoTipo = 1");
		//		List<String> list1 = SqlService.dbRead("select idTipo from tipo where tipoTipo = 1");
		//
		//
		//		if (list.size() > 0) {
		//			int i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				listTipos.add(new TipoImpl(Integer.valueOf(list1.get(i)),
		//						Integer.valueOf(list1.get(i)),
		//						list.get(i)));
		//			}
		//		}
		//				
		//		return listTipos;
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getHabilidades()
	 */
	public List<Caracteristica> getHabilidades() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",5)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 12");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 12");
		//
		//
		//		if (list.size() > 0) {
		//			int i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(12,12,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getContextos()
	 */
	public List<Caracteristica> getContextos() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",8)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 12");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 12");
		//
		//
		//		if (list.size() > 0) {
		//			int i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(12,12,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getCompetencias()
	 */
	public List<Caracteristica> getCompetencias() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",6)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 13");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 13");
		//
		//
		//		if (list.size() > 0) {
		//			int i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(13,13,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getPersonalidades()
	 */
	public List<Caracteristica> getPersonalidades() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",7)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 11");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 11");
		//
		//
		//		if (list.size() > 0) {
		//			int i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(11,11,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getSA()
	 */
	public List<Caracteristica> getSA() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",9)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 14");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 14");
		//
		//
		//		if (list.size() > 0) {
		//			int i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(14,14,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
	}

	public List<Caracteristica> getEstilos() {

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idItem.idItem",4)).addOrder(Order.asc("idCaracteristica"));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;

		//		List<Caracteristica> listC = new ArrayList<Caracteristica>();
		//		
		//		List<String> list = SqlService.dbRead("select nombre from caracteristica where idTipo = 10");
		//		List<String> list1 = SqlService.dbRead("select idCaracteristica from caracteristica where idTipo = 10");
		//
		//
		//		if (list.size() > 0) {
		//			int i = 0;
		//			for(i =0; i< list.size();i++)
		//			{
		//				TipoImpl t = new TipoImpl(10,15,"");
		//				listC.add(new CaracteristicaImpl(Integer.valueOf(list1.get(i)),t,list.get(i),""));
		//			}
		//		}
		//				
		//		return listC;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getTipos()
	 */
	public List<Tipo> getTipos(){

		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).addOrder(Order.asc("idTipo"));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#eliminar(java.lang.Object)
	 */
	public void eliminar(Object editar) {

		getHibernateTemplate().delete(editar);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getCaracteristica(int)
	 */
	public List<Caracteristica> getCaracteristica(int id){

		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idCaracteristica",id));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getTipo(int)
	 */
	public List<Tipo> getTipo(int id){

		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).add(Restrictions.eq("idTipo",id));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemId(int)
	 */
	public Item getItemId(int id){

		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",id));

		List<Item> list = getHibernateTemplate().findByCriteria(d);
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getActividad(int)
	 */
	public Actividad getActividad(int id){		
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("idActividad",id));
		List<Actividad> list = getHibernateTemplate().findByCriteria(d);		

		if(list.size()==0)
			return new ActividadImpl();
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemAshyi(java.lang.String)
	 */
	public Item getItemAshyi(String nombre)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("nombre",nombre));

		List<Item> listT = getHibernateTemplate().findByCriteria(d);

		if(listT.size() > 0)
		{
			return listT.get(0);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#registrarCurso(java.lang.String)
	 */
	public String registrarCurso(String titulo)
	{
		String respuesta = "El curso no se ha registrado";

		Actividad ac = getactividad(titulo,1);

		if(ac == null)
		{
			Item i = getItemAshyi("Actividad Macro");

			if(i != null)
			{
				List<String>elist = new ArrayList<String>();

				Actividad a = new ActividadImpl(i, titulo, 1, 100, "Universitario");

				boolean respSave = saveActividad(a,elist, "Error Almacenar Actividad", true);

				if(respSave)
					respuesta = "Curso registrado";
			}
		}
		else
		{
			respuesta = "Curso registrado";
		}		

		return respuesta;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getTipo(org.sakaiproject.lessonbuildertool.model.Item, java.lang.String)
	 */
	public Tipo getTipo(Item item, String nombre) {
		DetachedCriteria d = DetachedCriteria.forClass(Tipo.class).add(Restrictions.eq("idItem.idItem",item.getIdItem())).add(Restrictions.eq("nombre",nombre));

		List<Tipo> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getactividad(java.lang.String, int)
	 */
	public Actividad getactividad(String titulo, int nivelRecursividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("nombre",titulo)).add(Restrictions.eq("nivel_recursividad",nivelRecursividad));

		List<Actividad> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			return list.get(0);
		}
		Actividad a = new ActividadImpl();
		a.setNombre("Does not exist");
		return a;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUltimoItem()
	 */
	public int getUltimoItem()
	{
		int id = 0;
		List<String> list = SqlService.dbRead("SELECT MAX(idItem) AS id FROM item");		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUltimoObjetivo(java.lang.String)
	 */
	public int getUltimoObjetivo(String nombre)
	{
		int id = 0;

		Object [] fields = new Object[1];
		fields[0] = nombre;

		List<String> list = SqlService.dbRead("SELECT MAX(idObjetivo) AS id FROM objetivo WHERE nombre = ?",fields,null);		

		if(list.size() == 0)
			id = 1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/**
	 * Retorna los objetivos de una actividad a partir de su nombre y nivel de recursividad.
	 * @param nivelRecursividad nivel de recursividad de la actividad.
	 * @param nombre nombre de la actividad.
	 * @return String[] - arreglo con los nombres de los objetivos.
	 */
	public String[] getObjetivosActividad(int nivelRecursividad, String nombre) {

		Object [] fields = new Object[2];
		fields[0] = nivelRecursividad;
		fields[1] = nombre;

		List<String> list = SqlService.dbRead("select o.nombre from objetivo o, objetivos_actividad oa, actividad a where a.nivel_recursividad = ? and a.nombre = ? and oa.idActividad = a.idActividad and oa.idObjetivo = o.idObjetivo and oa.tipo = 1", fields, null);

		String[] listNombres = new String [list.size()];

		if (list.size() > 0) {
			int i = 0;
			for(String c : list)
			{
				listNombres[i]=c;
				i++;
			}
		}
		return listNombres;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getObjetivoActividad(java.lang.String, int)
	 */
	public int getObjetivoActividad(String nombre, int nR)
	{
		Object [] fields = new Object[2];
		fields[0] = nombre;
		fields[1] = nR;

		List<String> list = SqlService.dbRead("select o.idObjetivo from objetivo o, objetivos_actividad oa, actividad a where o.nombre = ? and o.idObjetivo = oa.idObjetivo and oa.idActividad = a.idActividad and a.nivel_recursividad = ?", fields, null);

		int id = -1;
		String[] listNombres = new String [list.size()];

		if (list.size() > 0) {
			id = Integer.valueOf(list.get(0));
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemName(java.lang.Long)
	 */
	public String getItemName(Long itemId)
	{		
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("id",itemId));

		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			return list.get(0).getName();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getDependenciasActividades()
	 */
	public List<DependenciaActividad> getDependenciasActividades()
	{
		DetachedCriteria d = DetachedCriteria.forClass(DependenciaActividad.class).addOrder(Order.asc("idActividad"));
		List<DependenciaActividad> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getDependenciaActividad(int, int)
	 */
	public List<DependenciaActividad> getDependenciaActividad(int act, int actDependiente)
	{
		DetachedCriteria d = DetachedCriteria.forClass(DependenciaActividad.class).add(Restrictions.eq("idActividad",act)).add(Restrictions.eq("Id_Actividad_Dependiente",actDependiente));
		List<DependenciaActividad> list = getHibernateTemplate().findByCriteria(d);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#saveDependenciaActividad(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveDependenciaActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission)
	{
		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof DependenciaActividad )) {
			elist.add(nowriteerr);
			return false;
		}

		try {		

			if (o instanceof DependenciaActividad) {
				int id = -1;
				DependenciaActividad i = (DependenciaActividad)o;	
				id = (Integer) getHibernateTemplate().save(i);
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
				System.out.println("en saveDependenciaActividad en simplepagetooldao");
				//			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
			} 
			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public List<ActividadTieneActividad> getActividadTieneActividades(int id)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividad.idActividad",id));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);
		if(list.size() > 0)
		{
			for(int i = 0; i < list.size(); i++)
			{
				list.get(i).setIdActividad(getActividad(list.get(i).getIdActividad().getIdActividad()));
			}
		}
		return list;

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getActividadTieneActividad(int, int)
	 */
	public List<ActividadTieneActividad> getActividadTieneActividad(int id, int idSiguienteNivel)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividadSiguienteNivel.idActividad",idSiguienteNivel)).add(Restrictions.eq("idActividad.idActividad",id));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);
		return list;

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getActividadTieneActividadString(int)
	 */
	public List<String> getActividadTieneActividadString(int id)
	{

		Object [] fields = new Object[1];
		fields[0] = id;
		List<String> list = SqlService.dbRead("select a.idActividadSiguienteNivel.idActividad, a.estiloActividadSiguienteNivel from ACTIVIDAD_TIENE_ACTIVIDAD a where a.idActividad.idActividad = ?", fields, null);

		List<String> listNombres=new ArrayList<String>();

		if (list.size() > 0) {
			int i = 0;	
			for(String c : list)
			{
				listNombres.add(c);
				i++;
			}
		}

		return listNombres;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getEstiloSiguienteNivelActTieneAct(int, int)
	 */
	public List<String> getEstiloSiguienteNivelActTieneAct(int id, int idSiguienteNivel)
	{

		Object [] fields = new Object[2];
		fields[0] = id;
		fields[1] = idSiguienteNivel;
		List<String> list = SqlService.dbRead("select a.estiloActividadSiguienteNivel from ACTIVIDAD_TIENE_ACTIVIDAD a where a.idActividad.idActividad = ? and a.idActividadSiguienteNivel.idActividad = ?", fields, null);

		List<String> listNombres=new ArrayList<String>();

		if (list.size() > 0) {
			int i = 0;	
			for(String c : list)
			{
				listNombres.add(c);
				i++;
			}
		}

		return listNombres;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getActividades(int)
	 */
	public List<Actividad> getActividades(int id){		
		DetachedCriteria d = DetachedCriteria.forClass(Actividad.class).add(Restrictions.eq("idActividad",id));
		List<Actividad> list = getHibernateTemplate().findByCriteria(d);		

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#deleteObject(java.lang.Object)
	 */
	public boolean deleteObject(Object o) {
		if (o instanceof DependenciaActividad) {
			DependenciaActividad i = (DependenciaActividad)o;
		}
		if (o instanceof CaracteristicaActividad) {
			CaracteristicaActividad i = (CaracteristicaActividad)o;
		}
		if (o instanceof Actividad) {
			Actividad i = (Actividad)o;
		}
		try {
			getHibernateTemplate().delete(o);
			return true;
		} catch (DataAccessException e) {
			try {

				/* If we have multiple objects of the same item, you must merge them
				 * before deleting.  If the first delete fails, we merge and try again.
				 */
				getHibernateTemplate().delete(getHibernateTemplate().merge(o));

				return true;
			}catch(DataAccessException ex) {
				ex.printStackTrace();
				log.warn("Hibernate could not delete: " + e.toString());
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getCaracteristicasTipo(java.lang.String)
	 */
	public List<CaracteristicasTipo> getCaracteristicasTipo(String nombreTipo){	

		DetachedCriteria dT = DetachedCriteria.forClass(Tipo.class).add(Restrictions.eq("nombre",nombreTipo));
		List<Tipo> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{

			DetachedCriteria d = DetachedCriteria.forClass(CaracteristicasTipo.class).add(Restrictions.eq("idTipo",list.get(0))).addOrder(Order.asc("linea"));
			List<CaracteristicasTipo> listC = getHibernateTemplate().findByCriteria(d);	

			return listC;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#saveActividadTieneActividad(java.lang.Object, java.util.List, java.lang.String, boolean)
	 */
	public boolean saveActividadTieneActividad(Object o, List<String>elist, String nowriteerr, boolean requiresEditPermission)
	{
		/*
		 * This checks a lot of conditions:
		 * 1) If o is SimplePageItem or SimplePage, it makes sure it gets the right page and checks the
		 *    permissions on it.
		 * 2) If it's a log entry, it lets it go.
		 * 3) If requiresEditPermission is set to false, it lets it go.
		 * 
		 * Essentially, if any of those say that the edit is fine, it won't throw the error.
		 */
		if(requiresEditPermission && !(o instanceof ActividadTieneActividad )) {
			elist.add(nowriteerr);
			return false;
		}

		try {		

			if (o instanceof ActividadTieneActividad) {
				int id = -1;
				ActividadTieneActividad i = (ActividadTieneActividad)o;	
				id = (Integer) getHibernateTemplate().save(i);
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
				System.out.println("en saveActividadTieneActividad en simplepagetooldao");
				//			EventTrackingService.post(EventTrackingService.newEvent("lessonbuilder.create", "/lessonbuilder/item/" + i.getIdActividad(), true));
			} 
			return true;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			getCause(e, elist);
			return false;
		} catch (org.hibernate.exception.DataException e) {
			getCause(e, elist);
			return false;
		} catch (DataAccessException e) {
			getCause(e, elist);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemPlan(int)
	 */
	public ItemPlan getItemPlan(int id) {

		DetachedCriteria d = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idItemPlan", id));
		List<ItemPlan> list = getHibernateTemplate().findByCriteria(d);
		if(list.size() > 0)
			return list.get(0);
		return new ItemPlanImpl();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getRecurso(java.lang.String)
	 */
	public Recurso getRecurso(String nombre)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("nombre",nombre));
		List<Recurso> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getRecursosActividad(int, java.lang.String)
	 */
	public List<RecursosActividad> getRecursosActividad(int nivelRecursividad, String nombre)
	{	
		Actividad ac = this.getactividad(nombre,nivelRecursividad);

		DetachedCriteria d = DetachedCriteria.forClass(RecursosActividad.class).add(Restrictions.eq("idActividad.idActividad",ac.getIdActividad()));
		List<RecursosActividad> listC = getHibernateTemplate().findByCriteria(d);	

		if(listC.size()>0)
		{
			for(int i = 0; i<listC.size();i++)
			{
				d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",listC.get(i).getIdRecurso().getIdRecurso()));
				List<Recurso> listT = getHibernateTemplate().findByCriteria(d);

				listC.get(i).setIdRecurso(listT.get(0));
			}
			return listC;
		}
		return new ArrayList<RecursosActividad>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getObjetivo(java.lang.String)
	 */
	public Objetivo getObjetivo(String objetivo)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Objetivo.class).add(Restrictions.eq("nombre",objetivo));
		List<Objetivo> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return new ObjetivoImpl("Does not exist");
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getActividadRecurso(int, java.lang.String)
	 */
	public Actividad getActividadRecurso(int idActividad, String idRecursoSakai)
	{
		Object [] fields = new Object[2];
		fields[0] = idActividad;
		fields[1] = idRecursoSakai;

		Actividad a = new ActividadImpl();

		a = this.getActividad(idActividad);

		List<RecursosActividad> rA =this.getRecursosActividad(a.getNivel_recursividad(),a.getNombre());

		int estar = 0;
		for(int i = 0;i<rA.size();i++)
		{
			if(rA.get(i).getIdRecurso().getIdItemSakai().equals(idRecursoSakai))
			{
				estar++;
			}

		}
		if(estar != 0)
		{
			return a;
		}
		a = new ActividadImpl();
		a.setNombre("Does not exist");
		return a;
	}	

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getCaracteristicasActividad(int)
	 */
	public List<CaracteristicaActividad> getCaracteristicasActividad(int idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicaActividad.class).add(Restrictions.eq("idActividad.idActividad",idActividad));
		List<CaracteristicaActividad> listC = getHibernateTemplate().findByCriteria(d);	

		if(listC.size()>0)
		{
			for(int i = 0; i<listC.size();i++)
			{
				d = DetachedCriteria.forClass(Item.class).add(Restrictions.eq("idItem",listC.get(i).getIdCaracteristica().getIdItem().getIdItem()));
				List<Item> listT = getHibernateTemplate().findByCriteria(d);

				listC.get(i).getIdCaracteristica().setIdItem(listT.get(0));
			}
			return listC;
		}
		return new ArrayList<CaracteristicaActividad>();
	}

	/**
	 * Retorna un objetivo a partir de su nombre.
	 * @param idObjetivo
	 * @return Objetivo - el objetivo.
	 */
	public Objetivo getObjetivo(int idObjetivo)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Objetivo.class).add(Restrictions.eq("idObjetivo",idObjetivo));
		List<Objetivo> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return new ObjetivoImpl("Does not exist");
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getObjetivosActividad(int)
	 */
	public List<ObjetivosActividad> getObjetivosActividad(int idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ObjetivosActividad.class).add(Restrictions.eq("idActividad.idActividad",idActividad));
		List<ObjetivosActividad> listO = getHibernateTemplate().findByCriteria(d);		

		if(listO.size()>0)
		{
			for(int i = 0;i<listO.size();i++)
			{
				d = DetachedCriteria.forClass(Objetivo.class).add(Restrictions.eq("idObjetivo",listO.get(i).getIdObjetivo().getIdObjetivo()));
				List<Objetivo> listT = getHibernateTemplate().findByCriteria(d);

				listO.get(i).setIdObjetivo(listT.get(0));
			}
			return listO;
		}

		return new ArrayList<ObjetivosActividad>();
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getObjetivosActividad(java.lang.Integer, java.lang.String)
	 */
	public String[] getObjetivosActividad(Integer nivelRecursividad, String nombre) {

		Object [] fields = new Object[2];
		fields[0] = nivelRecursividad;
		fields[1] = nombre;

		List<String> list = SqlService.dbRead("select o.nombre from objetivo o, objetivos_actividad oa, actividad a where a.nivel_recursividad = ? and a.nombre = ? and oa.idActividad = a.idActividad and oa.idObjetivo = o.idObjetivo and oa.tipo = 1", fields, null);


		String[] listNombres = new String [list.size()];

		if (list.size() > 0) {
			Integer i = 0;
			for(String c : list)
			{
				listNombres[i]=c;
				i++;
			}
		}
		return listNombres;
	}	

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getRecursosActividad(int)
	 */
	public List<RecursosActividad> getRecursosActividad(int idActividad)
	{
		DetachedCriteria d = DetachedCriteria.forClass(RecursosActividad.class).add(Restrictions.eq("idActividad.idActividad",idActividad));
		List<RecursosActividad> listR = getHibernateTemplate().findByCriteria(d);

		if(listR.size()>0)
		{
			for(int i = 0;i<listR.size();i++)
			{
				d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",listR.get(i).getIdRecurso().getIdRecurso()));
				List<Recurso> listT = getHibernateTemplate().findByCriteria(d);

				listR.get(i).setIdRecurso(listT.get(0));
			}

			return listR;
		}
		return new ArrayList<RecursosActividad>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getActividadCompleta(int)
	 */
	public Actividad getActividadCompleta(int id){	

		Actividad ac = getActividad(id);

		ac.setCaracteristicas(this.getCaracteristicasActividad(ac.getIdActividad()));
		ac.setObjetivo(this.getObjetivosActividad(ac.getIdActividad()));
		ac.setRecursos(this.getRecursosActividad(ac.getIdActividad()));

		return ac;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getActividadAltoNivel(int)
	 */
	public Actividad getActividadAltoNivel(int id)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividadSiguienteNivel.idActividad",id));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);

		Actividad ac = new ActividadImpl();
		if(list.size() > 0)
		{
			ac = this.getActividad(list.get(0).getIdActividad().getIdActividad());
		}

		return ac;

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#deleteActividad(org.sakaiproject.lessonbuildertool.model.Actividad)
	 */
	public void deleteActividad(Actividad a)
	{		
		Object [] fields = new Object[1];
		fields[0] = a.getIdActividad();
		boolean elimina  = SqlService.dbWrite("delete from actividad where idActividad = ?", fields);

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#isItUsuario(org.sakaiproject.lessonbuildertool.model.Usuario)
	 */
	public boolean isItUsuario(Usuario usuario)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Usuario.class).add(Restrictions.eq("idUsuarioSakai",usuario.getIdUsuarioSakai())).add(Restrictions.eq("rol",usuario.getRol())).add(Restrictions.eq("nivel", usuario.getNivel()));

		List<Usuario> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUsuario(int)
	 */
	public Usuario getUsuario(int idUsuario)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Usuario.class).add(Restrictions.eq("idUsuario",idUsuario));

		List<Usuario> list = getHibernateTemplate().findByCriteria(d);		

		if(list.size()==0)
			return new UsuarioImpl();
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUltimoUsuario(java.lang.String)
	 */
	public int getUltimoUsuario(String idUsuarioSakai)
	{
		int id = 0;

		Object [] fields = new Object[1];
		fields[0] = idUsuarioSakai;

		List<String> list = SqlService.dbRead("SELECT MAX(idUsuario) AS id FROM usuario WHERE idUsuarioSakai = ?",fields,null);		

		if(list.size() == 0)
			id = -1;
		else
		{
			id = Integer.valueOf(list.get(0)) ;
		}

		return id;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getCaracteristicaRecursos(org.sakaiproject.lessonbuildertool.model.Recurso, org.sakaiproject.lessonbuildertool.model.Caracteristica)
	 */
	public CaracteristicaRecurso getCaracteristicaRecursos(Recurso idRecurso, Caracteristica idCaracteristica)
	{

		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicaRecurso.class).add(Restrictions.eq("idRecurso.idRecurso",idRecurso)).add(Restrictions.eq("idCaracteristica.idCaracteristica", idCaracteristica));
		List<CaracteristicaRecurso> listC = getHibernateTemplate().findByCriteria(d);	

		if(listC.size()>0)
		{
			d = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",listC.get(0).getIdRecurso().getIdRecurso()));
			List<Recurso> listT = getHibernateTemplate().findByCriteria(d);

			listC.get(0).setIdRecurso(listT.get(0));
			return listC.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getEstilosUsuario(int)
	 */
	public List<CaracteristicasUsuario> getEstilosUsuario(int idUsuario)
	{
		DetachedCriteria d = DetachedCriteria.forClass(CaracteristicasUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",idUsuario));
		List<CaracteristicasUsuario> listC = getHibernateTemplate().findByCriteria(d);	

		if(listC.size()>0)
		{
			for(int i = 0; i<listC.size();i++)
			{
				d = DetachedCriteria.forClass(Usuario.class).add(Restrictions.eq("idUsuario",listC.get(i).getIdUsuario().getIdUsuario()));
				List<Usuario> listT = getHibernateTemplate().findByCriteria(d);

				d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("idCaracteristica",listC.get(i).getIdCaracteristica().getIdCaracteristica()));
				List<Caracteristica> listCa = getHibernateTemplate().findByCriteria(d);

				listC.get(i).setIdCaracteristica(listCa.get(0));

				listC.get(i).setIdUsuario(listT.get(0));
			}
			return listC;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getActividadesUsuario(int)
	 */
	public List<ActividadesUsuario> getActividadesUsuario(int userId)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadesUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",userId));
		List<ActividadesUsuario> listA = getHibernateTemplate().findByCriteria(d);	
		if(listA.size() > 0)
		{
			for(int i = 0; i < listA.size(); i++)
			{
				listA.get(i).setIdActividad(getActividad(listA.get(i).getIdActividad().getIdActividad()));
			}
			return listA;
		}
		return new ArrayList<ActividadesUsuario>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUsuario(java.lang.String)
	 */
	public Usuario getUsuario(String userId)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Usuario.class).add(Restrictions.eq("idUsuarioSakai",userId));

		List<Usuario> list = getHibernateTemplate().findByCriteria(d);		

		if(list.size()==0)
			return new UsuarioImpl();
		return list.get(0);
	}

	/**
	 * @param items
	 * @param actividades
	 * @return
	 */
	/**
	 * @param items
	 * @param actividades
	 * @return
	 */
	public List<Actividad> actividadesRecurso(List<SimplePageItem> items, List<Actividad> actividades)
	{
		List<Actividad> actividadesRecursos = new ArrayList<Actividad>();
		for(SimplePageItem item : items)
		{
			for(Actividad actvidad : actividades)
			{					
				Actividad aAgregar = getActividadRecurso(actvidad.getIdActividad(), String.valueOf(item.getId()));
				if(!aAgregar.getNombre().equals("Does not exist"))
				{
					actividadesRecursos.add(aAgregar);
				}
			}
		}

		return actividadesRecursos;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItems()
	 */
	public List<SimplePageItem> getItems()
	{
		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.ne("type", 2));
		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getIdItemsGrafo(int)
	 */
	public List<Integer> getIdItemsGrafo(int idGrafo)
	{
		List<Integer> list = new ArrayList<Integer>();

		DetachedCriteria d = DetachedCriteria.forClass(GrafoRelaciones.class).add(Restrictions.eq("idGrafo.idGrafo", idGrafo)).addOrder(Order.asc("orden"));
		List<GrafoRelaciones> listG = getHibernateTemplate().findByCriteria(d);

		if(listG.size() > 0)
		{
			for(GrafoRelaciones iG: listG)
			{
				if(!list.contains(iG.getIdItemPlan_Origen()))
					list.add(iG.getIdItemPlan_Origen());
				if(!list.contains(iG.getIdItemPlan_Destino()))
					list.add(iG.getIdItemPlan_Destino());
			}
		}

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getIdItemsPlan(int)
	 */
	public List<Integer> getIdItemsPlan(int idGrafo)
	{
		List<Integer> list = new ArrayList<Integer>();

		DetachedCriteria d = DetachedCriteria.forClass(Grafo.class).add(Restrictions.eq("idGrafo", idGrafo));
		List<Grafo> listG = getHibernateTemplate().findByCriteria(d);

		if(listG.size() > 0)
		{
			//list.add(listG.get(0).getIdItemPlan_Inicial());
			list.addAll(getIdItemsGrafo(idGrafo));
		}

		return list;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemsActivos(org.sakaiproject.lessonbuildertool.model.Usuario, java.lang.String)
	 */
	public List<ItemPlan> getItemsActivos(Usuario usuario, String nombreActividad)
	{
		usuario = getUsuario(usuario.getIdUsuario());

		int idGrafo = getGrafoUsuarioActividad(usuario.getIdUsuario(), nombreActividad);

		List<Integer> idsItems = getIdItemsPlan(idGrafo);
		List<ItemPlan> list = new ArrayList<ItemPlan>();

		if(idsItems.size() > 0)
		{
			for(int i = 0;i <idsItems.size(); i++)
			{
				ItemPlan item = getItemPlan(idsItems.get(i));
				list.add(item);
			}
		}

		return list;
	}

	/**
	 * Retorna el grafoUsuario de un usuario para la actividad pasada por par&aacutemetro
	 * @param idUsuario
	 * @param nombreActividad
	 * @return int - el identificador.
	 */
	public int getGrafoUsuarioActividad(int idUsuario, String nombreActividad) {

		//3 unidad
		Actividad actividad = getactividad(nombreActividad, 3);

		DetachedCriteria d = DetachedCriteria.forClass(GrafosUsuario.class).add(Restrictions.eq("idUsuario.idUsuario", idUsuario)).add(Restrictions.eq("idActividad", actividad.getIdActividad()));
		List<GrafosUsuario> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
		{
			GrafosUsuario grafoActivo = new GrafosUsuarioImpl();
			//hay grafos re planificados, encontrar el activo
			for(GrafosUsuario gU : list)
			{
				if(gU.isActivo())
				{
					grafoActivo = gU;
					break;
				}
			}
			return grafoActivo.getIdGrafo().getIdGrafo();
		}

		return 0;
	}

	/**
	 * Retorna un itemPlan.
	 * @param idActividad
	 * @param idRecurso
	 * @return ItemPlan - el itemPlan.
	 */
	public ItemPlan getItemPlan(Actividad idActividad, Recurso idRecurso) {
		DetachedCriteria d = DetachedCriteria.forClass(ItemPlan.class).add(Restrictions.eq("idActividad.idActividad", idActividad)).add(Restrictions.eq("idRecurso.idRecurso", idRecurso));
		List<ItemPlan> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
			return list.get(0);

		return new ItemPlanImpl();
	}

	/**
	 * Retorna un recurso a partir de su identificador.
	 * @param idRecurso
	 * @return Recurso - el recurso.
	 */
	public Recurso getRecurso(int idRecurso)
	{
		DetachedCriteria dT = DetachedCriteria.forClass(Recurso.class).add(Restrictions.eq("idRecurso",idRecurso));
		List<Recurso> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemSakai(org.sakaiproject.lessonbuildertool.model.ItemPlan)
	 */
	public SimplePageItem getItemSakai(ItemPlan idItemPlan)
	{
		Recurso recursoItem = getRecurso(idItemPlan.getIdRecurso().getIdRecurso());
		SimplePageItem item = findItem(Long.valueOf(recursoItem.getIdItemSakai()));
		return item;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemsUsuario(org.sakaiproject.lessonbuildertool.model.ItemPlan, java.lang.String)
	 */
	public ItemsUsuario getItemsUsuario(ItemPlan itemPlan, String userId)
	{
		Usuario us = getUsuario(userId);
		DetachedCriteria dT = DetachedCriteria.forClass(ItemsUsuario.class).add(Restrictions.eq("idUsuario",us)).add(Restrictions.eq("idItemPlan",itemPlan));
		List<ItemsUsuario> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		
			for(ItemsUsuario iu : list)
			{
				iu.setIdItemPlan(getItemPlan(iu.getIdItemPlan().getIdItemPlan()));
				iu.setIdUsuario(getUsuario(iu.getIdUsuario().getIdUsuario()));
			}
			return list.get(0);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemsActivosActividad(java.lang.String, int)
	 */
	public List<ItemPlan> getItemsActivosActividad(String nombreActividad, int nR)
	{	
		List<GrafosUsuario> grafosActividad = getGrafoUsuarioActividad(nombreActividad, nR);
		
		GrafosUsuario grafoActivo = null;
		for(GrafosUsuario gU : grafosActividad)				
			if(gU.isActivo())
			{	
				grafoActivo = gU;
				break;
			}	

		List<ItemPlan> list = new ArrayList<ItemPlan>();

		for(GrafosUsuario gA: grafosActividad)
		{		
			List<Integer> idsItems = getIdItemsPlan(gA.getIdGrafo().getIdGrafo());

			if(idsItems.size() > 0)
			{
				for(int i = 0;i <idsItems.size(); i++)
				{
					ItemPlan item = getItemPlan(idsItems.get(i));
					list.add(item);
				}
			}
		}

		return list;
	}

	/**
	 * @param nombreActividad
	 * @param nR
	 * @return
	 */
	public List<GrafosUsuario> getGrafoUsuarioActividad(String nombreActividad, int nR) {
		//3 unidad
		Actividad actividad = getactividad(nombreActividad, nR);

		DetachedCriteria d = DetachedCriteria.forClass(GrafosUsuario.class).add(Restrictions.eq("idActividad", actividad.getIdActividad()));
		List<GrafosUsuario> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
			return list;

		return new ArrayList<GrafosUsuario>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUsuariosAshyi()
	 */
	public List<Usuario> getUsuariosAshyi()
	{
		DetachedCriteria d = DetachedCriteria.forClass(Usuario.class);
		List<Usuario> list = getHibernateTemplate().findByCriteria(d);

		if(list.size() > 0)
			return list;
		return new ArrayList<Usuario>();
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUsuariosSite(java.lang.String)
	 */
	public List<User> getUsuariosSite(String idSite)
	{
		List<User> usauriosSite = new ArrayList<User>();
		List<Usuario> us = getUsuariosAshyi();
		for(Usuario usaurio: us)
		{
			User usuario;
			try {
				usuario = UserDirectoryService.getUser(getUserSite(usaurio.getIdUsuarioSakai(), idSite));
				if(usuario != null)
					usauriosSite.add(usuario);
			} catch (UserNotDefinedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return usauriosSite;
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getUserSite(java.lang.String, java.lang.String)
	 */
	public String getUserSite(String idUsuarioSakai, String idSite) {
		Object [] fields = new Object[2];
		fields[0] = idSite;
		fields[1] = idUsuarioSakai;

		List<String> ids = SqlService.dbRead("select su.USER_ID from usuario u,sakai_site_user s, sakai_user su where s.SITE_ID = ? and s.USER_ID = ? and s.USER_ID = u.idUsuarioSakai and su.USER_ID = s.USER_ID;", fields, null);

		if(!ids.isEmpty())
		{
			return ids.get(0);
		}


		return "";
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemsUsuarioPorIds(int, int)
	 */
	public ItemsUsuario getItemsUsuarioPorIds(int idItemPlan, int idUsuario) {
		DetachedCriteria dT = DetachedCriteria.forClass(ItemsUsuario.class).add(Restrictions.eq("idUsuario.idUsuario",idUsuario)).add(Restrictions.eq("idItemPlan.idItemPlan",idItemPlan));
		List<ItemsUsuario> list = getHibernateTemplate().findByCriteria(dT);	

		if(list.size() > 0)
		{		

			return list.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getCaracteristica(java.lang.String)
	 */
	public Caracteristica getCaracteristica(String nombre)
	{
		DetachedCriteria d = DetachedCriteria.forClass(Caracteristica.class).add(Restrictions.eq("nombre",nombre))
				.add(Restrictions.eq("idItem.idItem",8));

		List<Caracteristica> list = getHibernateTemplate().findByCriteria(d);
		return list.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemSakaiInicial()
	 */
	public SimplePageItem getItemSakaiInicial()
	{
		 return getItems().get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.AshyiToolDao#getActividadTieneActividades(java.lang.Integer)
	 */
	public List<ActividadTieneActividad> getActividadTieneActividades(Integer id)
	{
		DetachedCriteria d = DetachedCriteria.forClass(ActividadTieneActividad.class).add(Restrictions.eq("idActividad.idActividad",id));
		List<ActividadTieneActividad> list = getHibernateTemplate().findByCriteria(d);

		for(int i = 0; i < list.size(); i++)
		{			
			list.get(i).setIdActividad(getActividad(list.get(i).getIdActividad().getIdActividad()));
			list.get(i).setIdActividadSiguienteNivel(getActividad(list.get(i).getIdActividadSiguienteNivel().getIdActividad()));

			if(list.get(i).getIdActividadSiguienteNivel().isEs_refuerzo())
			{
				list.remove(i);
				i--;
			}
		}
		return list;

	}
	
	/* (non-Javadoc)
	 * @see org.sakaiproject.lessonbuildertool.model.SimplePageToolDao#getItemId(int)
	 */
	public SimplePageItem getItemByPage(SimplePage page){

		DetachedCriteria d = DetachedCriteria.forClass(SimplePageItem.class).add(Restrictions.eq("sakaiId",String.valueOf(page.getPageId())))
				.add(Restrictions.eq("name",page.getTitle()))
				.add(Restrictions.eq("pageId",page.getParent()));

		List<SimplePageItem> list = getHibernateTemplate().findByCriteria(d);
		return list.get(0);
	}
}
