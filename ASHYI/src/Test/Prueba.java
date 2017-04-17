	package Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import co.edu.javeriana.ASHYI.model.Actividad;
import co.edu.javeriana.ASHYI.model.ItemPlan;
import co.edu.javeriana.ASHYI.model.Objetivo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import co.edu.javeriana.ASHYI.hbm.ItemsUsuarioImpl;
import co.edu.javeriana.ASHYI.logic.AshyiBean;
import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.Caracteristica;
import co.edu.javeriana.ASHYI.model.ItemsUsuario;
import co.edu.javeriana.ASHYI.model.ObjetivosActividad;

public class Prueba {
	
	private static String [] objs;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Consulta c = new Consulta();
		ApplicationContext appContext = new ClassPathXmlApplicationContext("co/edu/javeriana/ASHYI/persistence/components.xml");
				
		AshyiBean bean = (AshyiBean)appContext.getBean("ashyibean");
//		
//		//objs= bean.getObjetivosActividad(Integer.valueOf(args[0]), args[1]);
//		objs= bean.getObjetivosActividad(3, "Entornos digitales");
//		for(String obj : objs)
//		{
//			System.out.println("!!!!!!!!!!!!!!!!: "+obj);
//		}
//		
//		bean.getAshyiToolDao().getSessionFactory().close();
		
//		List<ObjetivosActividad> objs = c.getObjetivos("Entornos digitales", 3);
//		System.out.println("!!!!!!!!!!!!!!!!!!!: "+objs.size() + " objetivos encontrados");
//		String [] objs2 = c.getNombresObjetivos("Entornos digitales", 3);
//		//List<ObjetivosActividad> objs = c.get("Entornos digitales", 3);
//		
//		System.out.println("!!!!!!!!!!!!!!!!!!!: "+objs2.length + " objetivos encontrados");
				
//		List l = new ArrayList<>();
//		l.add(1);
//		//c.saveItemsUsuario(l,"c2e115eb-72b3-4866-8dc1-823c09760e71");
//		
//		c.getItemsUsuario("c2e115eb-72b3-4866-8dc1-823c09760e71", "Unidad 1");
		
//		String a = "Executor-yolima-c2e115eb-72b3-4866-8dc1-823c09760e71-Student";
//		//String aliasAg = a.replace("Executor-", "");
//   	 String newAlias = "";
//   	 String[] subStrings = a.split("-");
//        for (int i = 2; i < subStrings.length; i++) {
//            //System.out.println(subStrings[i]);
//       	 if(i>0 && i<subStrings.length-1)
//       	 {
//       		 newAlias += subStrings[i];
//       		 if(i<subStrings.length-2)
//       			 newAlias += "-";
//       	 }
//        }
//        
//        System.out.println("ss: "+newAlias);
		
		//c.con("Unidad 1", 3);
//		List<Caracteristica> l = c.getCaracteristica(52);
//		
//		for(Caracteristica d: l)
//			System.out.println("CAr: "+d.getNombre());
		
//		Actividad inicial=c.getActividadInicial();
//		System.out.println("Actividad inicial: "+inicial.getNombre()+" id: "+inicial.getIdActividad());
//		ObjetivosActividad objetivoActual = c.getObjetivosActividad(inicial.getNombre(), inicial.getNivel_recursividad()).get(0);
//		System.out.println("Objetivo inicial: "+objetivoActual.getIdObjetivo().getNombre()+" id: "+objetivoActual.getIdObjetivo().getIdObjetivo());
//		
//		String[] extension = "TODO.txt".split("\\.");
//		System.out.println("tam: "+extension.length);
//		System.out.println("lugar: "+extension[extension.length-1]);
		
//		ItemPlan item=c.getItemPlan(4);
//		System.out.println(item.getIdItemPlan());
//		Actividad ac=item.getIdActividad();
//		System.out.println(ac.getNombre());
//		ObjetivosActividad oa=c.getObjetivosActividad(ac.getIdActividad()).get(0);
//		Objetivo obj=oa.getIdObjetivo();
//		System.out.println(obj.getNombre());
		
try {
			System.out.println("fechasss : "+"28-Jul-2014 04:50:47 PM" +" s: "+"1-Ago-2014 05:41:37 AM");
		SimpleDateFormat formatter;
				
		formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = formatter.parse("2014-07-30 10:39:57");
		final String NEW_FORMAT = "dd-MMM-yyyy hh:mm:ss a";

		String newDateString;

		formatter.applyPattern(NEW_FORMAT);
		newDateString = formatter.format(date);
		
		System.out.println("fecha antes: "+date+" ASDFGBN: "+newDateString);
		
		formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		Date date1 = formatter.parse("28-Jul-2014 04:50:47 PM".substring(0, "28-Jul-2014 04:50:47 PM".length()));
		
		System.out.println("fechasss Inicial: "+date1 +" s: "+date1.toString());
		Date date2 = formatter.parse("1-Ago-2014 05:41:37 AM".substring(0, "1-Ago-2014 05:41:37 AM".length()));
		
		System.out.println("fechasss final: "+date2 +" s: "+date2.toString());
		long fechaInicialMs = date1.getTime();
		long fechaFinalMs = date2.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		float dias = (float) Math.floor(diferencia / (1000 * 60 * 60));		
		
		float tiempo_duracion = dias;	
		System.out.println("fecja1: "+date1+" fecja2: "+date2+" tiempo "+tiempo_duracion);
		
		
		Date d = new Date();
		 Calendar calendar = Calendar.getInstance();
		   calendar.setTime(date1);
		   calendar.set(Calendar.DAY_OF_WEEK, 1);
		   System.out.println(calendar.getMinimalDaysInFirstWeek());
		
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static String[] getObjs() {
		return objs;
	}

	public static void setObjs(String[] objs) {
		Prueba.objs = objs;
	}
	
	
}

