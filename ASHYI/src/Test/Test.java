package Test;

import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import co.edu.javeriana.ASHYI.logic.AshyiBean;
import co.edu.javeriana.ASHYI.logic.Consulta;
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		ApplicationContext appContext = new ClassPathXmlApplicationContext("co/edu/javeriana/ASHYI/persistence/components.xml");
//				
//		AshyiBean bean = (AshyiBean)appContext.getBean("ashyibean");
//		
//		String [] objs= bean.getObjetivosActividad(3, "Entornos digitales");
//		for(String obj : objs)
//		{
//			System.out.println("!!!!!!!!!!!!!!!!: "+obj);
//		}
		// String aliasAg = "yolima-c2e115eb-72b3-4866-8dc1-823c09760e71-Student";
		 String aliasAg = "dd029c12-d8f9-40e9-a893-a884f1216fb8";
//    	 String newAlias = "";
//    	 String[] subStrings = aliasAg.split("-");
//         for (int i = 0; i < subStrings.length; i++) {
//            // System.out.println(subStrings[i]);
//        	 if(i>0 && i<subStrings.length-1)
//        	 {
//        		 newAlias += subStrings[i];
//        		 if(i<subStrings.length-2)
//        			 newAlias += "-";
//        	 }
//         } System.out.println(newAlias);
		 
		 
         Consulta c = new Consulta();
         c.getCaracteristicasUsuario(aliasAg);
        
	}

}
