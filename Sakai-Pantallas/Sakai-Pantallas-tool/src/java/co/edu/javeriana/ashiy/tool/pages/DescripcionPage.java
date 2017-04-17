package co.edu.javeriana.ashiy.tool.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.DefaultItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import co.edu.javeriana.ashiy.model.Tema;
import co.edu.javeriana.ashiy.model.Thing;

/**
 * An example page. This interacts with a list of items from the database
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class DescripcionPage extends BasePage {

	private Tema tema;
	private ListChoice<String> listaSubTemas;
	private String subtemaSeleccionado;
	
	public DescripcionPage(Tema temaD) {
		this.tema = temaD;
		subtemaSeleccionado = "";

        // Add a form with 2 SubmitLinks that can be called
        
        add(new Label("nombreTema",tema.getNombre()));
        add(new Label("descripcionTema",tema.getDescripcion()));
        
        List<String> nombres = new ArrayList<String>();
        
        for(int i = 0;i<tema.getContenido().size();i++)
        {
        	nombres.add(tema.getContenido().get(i).getNombre());
        }
        
        listaSubTemas = new ListChoice<String>("subtemas",
				new PropertyModel(this, "subtemaSeleccionado"), nombres);
 
        listaSubTemas.setMaxRows(tema.getContenido().size());
        Form<Tema> form = new Form<Tema>("form", new CompoundPropertyModel(tema)){        
       
	        	@Override
	            public void onSubmit()
	            {
	        		for(int i = 0;i<tema.getContenido().size();i++)
					{
	        			if(tema.getContenido().get(i).getNombre().equals(subtemaSeleccionado))
						{
	        				setResponsePage(new SubTemaPage(tema.getContenido().get(i)));
						}
					}
	            };
	        };
	        add(form);
	        form.add(listaSubTemas);
        
	}
	
}
