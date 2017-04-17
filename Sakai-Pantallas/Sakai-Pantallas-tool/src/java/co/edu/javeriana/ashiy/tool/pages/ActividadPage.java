package co.edu.javeriana.ashiy.tool.pages;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.tool.api.SessionManager;

import co.edu.javeriana.ashiy.model.Actividad;
import co.edu.javeriana.ashiy.model.SubTema;
import co.edu.javeriana.ashiy.model.Tema;
import co.edu.javeriana.ashiy.model.unidadDidactica;
import edu.javeriana.BeanBESA;

/**
 * An example page
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class ActividadPage extends BasePage {

	private String nombreUsuario;
	private Actividad actividad;
	//private List<Link<String>> linksTemas;
	private String actividadSeleccionada;

	public ActividadPage(Actividad actividadR) {
		this.actividad = actividadR;
        // Add a form with 2 SubmitLinks that can be called
        
        add(new Label("nombreActividad",actividad.getNombre()));
        add(new Label("descripcionActividad",actividad.getDescripcion()));
        
       
        Form<?> form = new Form("formActR"){        
       
	        	@Override
	            public void onSubmit()
	            {
	        		
	            };
	        };
	        add(form);
		
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
	
}
