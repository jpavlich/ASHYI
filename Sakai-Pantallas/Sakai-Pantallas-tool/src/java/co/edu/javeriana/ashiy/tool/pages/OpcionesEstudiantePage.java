package co.edu.javeriana.ashiy.tool.pages;

import co.edu.javeriana.ashiy.model.Actividad;
import co.edu.javeriana.ashiy.model.Curso;
import co.edu.javeriana.ashiy.model.SubTema;
import co.edu.javeriana.ashiy.model.Tema;
import co.edu.javeriana.ashiy.model.unidadDidactica;
import edu.javeriana.BeanBESA;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.SessionManager;

public class OpcionesEstudiantePage extends BasePage
{
  private String nombreUsuario;
  private List<Curso> cursos;
  private String cursoSeleccionado;
  private ListChoice<String> listaTemas;

  public OpcionesEstudiantePage()
  {
    SessionManager sessionManager = (SessionManager)ComponentManager.get(SessionManager.class);
    try
    {
      Context initCtx = new InitialContext();

      Context envCtx = (Context)initCtx.lookup("java:comp/env");
      BeanBESA bean = (BeanBESA)envCtx.lookup("bean/MyBeanFactory");

      System.out.println("Verificar existencia de agente");
      String aliasA = "";
      if (!bean.existeAgente(sessionManager.getCurrentSession().getUserEid()))
      {
        System.out.println("Agente " + sessionManager.getCurrentSession().getUserEid() + " no existe");

        aliasA = bean.crearAgente(sessionManager.getCurrentSession().getUserEid());
      }

      System.out.println("Obteniendo agente");
      String aliasAP = bean.getAgent(sessionManager.getCurrentSession().getUserEid());
      this.nombreUsuario = aliasAP;

      add(new Component[] { new Label("id", this.nombreUsuario) });

      System.out.println("Agente " + aliasA + " creado");

      this.cursos = new ArrayList<Curso>();
      this.cursoSeleccionado = "";

      List<String> nombres = new ArrayList<String>();

      List<SubTema> subtemas = new ArrayList<SubTema>();
      List<unidadDidactica> unidades = new ArrayList<unidadDidactica>();
      
      List<Tema> temas = new ArrayList<Tema>();
      
      List<Actividad> actividadesU = new ArrayList<Actividad>();
		for(int i = 0;i<5;i++)
		{
			if(i==0)
			{
				actividadesU.add(new Actividad("Actividad_"+i, "Esta es la actividad_"+i, null, true));
			}
			else
			{
				actividadesU.add(new Actividad("Actividad_"+i, "Esta es la actividad_"+i, null, false));
			}
		}

      for (int i = 0; i < 5; i++)
      {
    	  unidades.add(new unidadDidactica(actividadesU, "Unidad_" + i));
      }
      
      for (int i = 0; i < 5; i++)
      {
        subtemas.add(new SubTema("Subtema_" + i, "Este es el subtema " + i, unidades));
        System.out.println("subtemas creados");
      }

      for (int i = 0; i < 7; i++)
      {
        temas.add(new Tema("Tema es " + i + 1, "Este es el tema " + i + 1, subtemas));
      }

      for (int i = 0; i < 7; i++) {
        this.cursos.add(new Curso("Curso Seminario " + i + 1, "Este es el curso " + i + 1, temas));

        nombres.add("Curso Seminario " + i + 1);
      }

      add( new ListView<Curso>("listCursos", cursos)
      {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected void populateItem(ListItem<Curso> item)
        {
          Curso t = (Curso)item.getModelObject();
          item.add(new Label("nombre", t.getNombre()));
        }
       });
      
      
      this.listaTemas = new ListChoice<String>("cursos", new PropertyModel(this, "cursoSeleccionado"), nombres);

      this.listaTemas.setMaxRows(temas.size());

      Form<?> formBotones = new Form("formBotones")
      {
        public void onSubmit() {
          for (int i = 0; i < OpcionesEstudiantePage.this.cursos.size(); i++)
          {
            if (((Curso)OpcionesEstudiantePage.this.cursos.get(i)).getNombre().equals(OpcionesEstudiantePage.this.cursoSeleccionado))
            {
              setResponsePage(new TemaPage((Curso)OpcionesEstudiantePage.this.cursos.get(i)));
            }
          }
        }
      };
      add(new Component[] { formBotones });
      formBotones.add(new Component[] { this.listaTemas });

      Form formPerfil = new Form("formPerfil")
      {
        public void onSubmit() {
          info("Form.onSubmit executed");
        }
      };
      add(new Component[] { formPerfil });
    }
    catch (NamingException e)
    {
      e.printStackTrace();
    }
  }

  public String getNombreUsuario()
  {
    return this.nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }
}