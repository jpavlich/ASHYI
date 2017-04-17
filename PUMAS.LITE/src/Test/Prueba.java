	package Test;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.ASHYI.logic.Consulta;
import co.edu.javeriana.ASHYI.model.Actividad;

public class Prueba {
	
	private static String [] objs;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Consulta c = new Consulta();
//		ApplicationContext appContext = new ClassPathXmlApplicationContext("co/edu/javeriana/ASHYI/persistence/components.xml");
//				
//		AshyiBean bean = (AshyiBean)appContext.getBean("ashyibean");
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
		
//		List ac = new ArrayList<>();
//		for(int i  = 0; i < 5; i++)
//			ac.add(i*1.7);
//			ASHYIExecutionGuard ex = new ASHYIExecutionGuard();
//			
////			String aliasAg = "Andres-ccfec5d5-61ca-419e-ab8c-09db63155087-Student";
////			String idUsuario = ex.getIdUsuarioFormAlias(aliasAg);
////			List actividades = new ArrayList<>();
////			actividades.add(4);
////			actividades.add(5);
////			actividades.add(6);
////			actividades.add(7);
////			actividades.add(36);
////			c.cambiarItemsUsuario(actividades, idUsuario, 3);
////			System.out.println("alias: "+idUsuario);
//			ex.prueba(c);
			
//			List ac = new ArrayList<>();
//			//36, 14, 40, 21, 41, 28, 42, 35, 43
//			ac.add(36);
//			ac.add(14);
//			ac.add(40);
//			ac.add(21);
//			ac.add(41);
//			ac.add(28);
//			ac.add(42);
//			ac.add(35);
//			ac.add(43);
//			
//			System.out.println(verificarContieneId(ac, 14));
			
			
//List<Caracteristica> l = c.getCaracteristica(52);
//		
//		for(Caracteristica d: l)
//			System.out.println("CAr: "+d.getNombre());
//			
//			int tamLista = ac.size();
//			
//			if(tamLista == 1)
//			{
//				System.out.println("Ac sola: "+ac.get(0));
//			}
//			else
//			{
//				for(int i = tamLista-1; i > 0; i--)
//				{
//					System.out.println("Ac: "+ac.get(i-1));
//					System.out.println("Ac: "+ac.get(i));
//					
//				}
//			}
		
		List<Actividad> acs = new ArrayList<Actividad>();
		List<Actividad> aR = new ArrayList<Actividad>();
		
		acs.add(c.getActividad(6));
		acs.add(c.getActividad(7));
		acs.add(c.getActividad(8));
		acs.add(c.getActividad(9));
		acs.add(c.getActividad(48));
		acs.add(c.getActividad(49));
		acs.add(c.getActividad(50));
		acs.add(c.getActividad(51));
		acs.add(c.getActividad(52));
		acs.add(c.getActividad(53));
		
		aR.add(c.getActividad(17));
		aR.add(c.getActividad(18));
		aR.add(c.getActividad(19));
		aR.add(c.getActividad(20));
		aR.add(c.getActividad(21));
		aR.add(c.getActividad(22));
		aR.add(c.getActividad(23));
		
		//c.onReceiveActivities(acs, aR, 3);
	}
	
	
	
private static boolean verificarContieneId(List actividades, Integer idItemPlan) {
		
		for(int i = 0; i <actividades.size(); i++)
		{
			if(actividades.get(i) instanceof Integer)
				if(((int)actividades.get(i)) == idItemPlan)
					return true;
			if(actividades.get(i) instanceof String)
				if((Integer.valueOf((String)actividades.get(i))) == idItemPlan)
					return true;
		}
		
		return false;		
	}

	public static String[] getObjs() {
		return objs;
	}

	public static void setObjs(String[] objs) {
		Prueba.objs = objs;
	}
	
	
}

