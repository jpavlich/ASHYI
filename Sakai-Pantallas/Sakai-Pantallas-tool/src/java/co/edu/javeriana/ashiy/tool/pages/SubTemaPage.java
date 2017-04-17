package co.edu.javeriana.ashiy.tool.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.model.PropertyModel;

import co.edu.javeriana.ashiy.model.SubTema;
import co.edu.javeriana.ashiy.model.Tema;

/**
 * An example page
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class SubTemaPage extends BasePage {
	
	private SubTema subtema;
	private String unidadSeleccionada;
	private ListChoice<String> listaUnidades;
	

	public SubTemaPage(SubTema subtem) {
		
		this.subtema = subtem;

	    add(new Label("nombreSubtema", this.subtema.getNombre()));
	    add(new Label("descripcionsubTema", this.subtema.getDescripcion()));

	    this.unidadSeleccionada = "";

	    List<String> nombres = new ArrayList<String>();
	    for (int i = 0; i < this.subtema.getUnidades().size(); i++)
	    {
	      nombres.add((this.subtema.getUnidades().get(i)).getNombre());
	    }

	    listaUnidades = new ListChoice("unidades", new PropertyModel(this, "unidadSeleccionada"), nombres);

	    listaUnidades.setMaxRows(subtema.getUnidades().size());

	    Form<?> form = new Form("formSTD")
	    {
	      protected void onSubmit()
	      {
	        for (int i = 0; i < subtema.getUnidades().size(); i++)
	        {
	          if (subtema.getUnidades().get(i).getNombre().equals(unidadSeleccionada))
	          {
	            setResponsePage(new UnidadDidactica(subtema.getUnidades().get(i)));
	          }
	        }
	      }
	    };
	    add( form );
	    form.add( listaUnidades );
		
	}
}
