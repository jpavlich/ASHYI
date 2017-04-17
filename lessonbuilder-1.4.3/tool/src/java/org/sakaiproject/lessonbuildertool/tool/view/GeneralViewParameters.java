
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


package org.sakaiproject.lessonbuildertool.tool.view;

import org.sakaiproject.lessonbuildertool.ItemsUsuarioImpl;
import org.sakaiproject.lessonbuildertool.model.ItemsUsuario;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class GeneralViewParameters extends SimpleViewParameters {
	public static final int COMMENTS = 1;
	public static final int STUDENT_CONTENT = 2;
	public static final int STUDENT_PAGE = 3;
	
	
	private String errorMessage = "";
	private long sendingPage = -1;

	private String title = "";
	private String source = "";
	private String id = "";
	private String clearAttr = "";
	private Long itemId = -1L;
    // the word "push", a number to return to that level in the path, or "next" to replace the top level
	// or "none" if this page shouldn't be recorded at all
	private String path = "";
	private String returnView = "";
	private String recheck = "";
	private String backPath = "";
	public boolean newTopLevel = false; // For page picker, whether this should be a new top level or subpage
	
	public int addTool = -1;
	public boolean postedComment = false;
	public long studentItemId;
	
	public String author; // An author whose comments should be highlighted

	
	public int idActividad = -1;
	public String tipoActividad = "";
	public String posicionActividad = "";
	
	public int idItemPAshyi = -1;
	public int idItemUAshyi = -1;
	public int idItemRAshyi = -1;
	
	public String sourcePage = "";
	
	public int idUnidadDidactica =-1;
	public String objetivo= "";

	public GeneralViewParameters() {
		super();
	}

	public GeneralViewParameters(String VIEW_ID) {
		super(VIEW_ID);
	}

	/**
	 * To be used when creating iFrame pages.
	 * 
	 * @param VIEW_ID
	 * @param title
	 * @param source
	 */
	public GeneralViewParameters(String VIEW_ID, String title, String source, String id) {
		super(VIEW_ID);

		this.title = title;
		this.source = source;
		this.id = id;
	}

	public GeneralViewParameters(String VIEW_ID, long sendingPage) {
		super(VIEW_ID);
		this.sendingPage = sendingPage;
	}

	public GeneralViewParameters(String VIEW_ID, String error) {
		super(VIEW_ID);
		errorMessage = error;
	}

	public void setErrorMessage(String error) {
		errorMessage = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setSendingPage(long l) {
		sendingPage = l;
	}

	public long getSendingPage() {
		return sendingPage;
	}

	public void setPath(String p) {
		path = p;
	}

	public String getPath() {
		return path;
	}

	public void setBackPath(String p) {
		backPath = p;
	}

	public String getBackPath() {
		return backPath;
	}


	public void setReturnView(String p) {
		returnView = p;
	}

	public String getReturnView() {
		return returnView;
	}

	public void setRecheck(String p) {
		recheck = p;
	}

	public String getRecheck() {
		return recheck;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setClearAttr(String clearAttr) {
		this.clearAttr = clearAttr;
	}

	public String getClearAttr() {
		return clearAttr;
	}

	public int getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	public String getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(String tipoActividad) {
		this.tipoActividad = tipoActividad;
	}
	
	public String getPosicionActividad() {
		return posicionActividad;
	}

	public void setPosicionActividad(String posicionActividad) {
		this.posicionActividad = posicionActividad;
	}
	
	public int getIdItemPAshyi() {
		return idItemPAshyi;
	}

	public void setIdItemPAshyi(int idItemPAshyi) {
		this.idItemPAshyi = idItemPAshyi;
	}

	public int getIdItemUAshyi() {
		return idItemUAshyi;
	}

	public void setIdItemUAshyi(int idItemUAshyi) {
		this.idItemUAshyi = idItemUAshyi;
	}
	
	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

	public int getIdItemRAshyi() {
		return idItemRAshyi;
	}

	public void setIdItemRAshyi(int idItemRAshyi) {
		this.idItemRAshyi = idItemRAshyi;
	}

	public int getIdUnidadDidactica() {
		return idUnidadDidactica;
	}

	public void setIdUnidadDidactica(int idUnidadDidactica) {
		this.idUnidadDidactica = idUnidadDidactica;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	
	
	
}
