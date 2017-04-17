package edu.javeriana;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;

public class BeanBESA {

	private String foo = "Default Foo";
	private int bar = 0;
	private  AdmBESA contenedor = AdmBESA.getInstance("confbesa.xml");
	//private Map<String, AgenteP> userAgent = new ConcurrentHashMap<String, AgenteP>();
	//private List<AgenteP> runnables = new ArrayList< AgenteP>();

	public BeanBESA() {
		System.out.println("Contenedor creado");
		System.out.println("Init MyBean");
//		for (int i = 0; i < 10; i++) {
//			Runnable r = new RunnableImpl();
//			Thread t = new Thread(r, Math.random() + "");
//			runnables.put(i + "", r);
//			t.start();
//		}
		
		
	}
	
	public String crearAgente(String alias)
	{
		try {
			DatosEstado datos = new DatosEstado(1, "Soy agente "+alias);
			EstadoAgente estado = new EstadoAgente(datos);
			StructBESA estructura = new StructBESA();
			estructura.bindGuard(GuardaMensaje.class);
			AgenteP agente = new AgenteP(alias, estado, estructura, 0.91);
			agente.start();
			
//			runnables.add(agente);
//			userAgent.put(alias,agente);
			return agente.getAlias();
		} catch (KernellAgentExceptionBESA e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public AgHandlerBESA obtenerAgente(String alias)
	{
		System.out.println("Buscando alias");
		AgHandlerBESA handler = null;
		try {
			if(contenedor.doesAgentExist(alias))
			{
				System.out.println("Agente existe");
				handler = contenedor.getHandlerByAlias(alias);
			}else
			{
				System.out.println("Agente no existe");
			}
			
		} catch (KernellAgentExceptionBESA e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ExceptionBESA e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handler;
	}
	
	public boolean existeAgente(String alias)
	{
		return contenedor.doesAgentExist(alias);
	}

	public String getFoo() {
		return (this.foo);
	}

	public void setFoo(String foo) {
		this.foo = foo;
	}

	public int getBar() {
		return (this.bar);
	}

	public void setBar(int bar) {
		this.bar = bar;
	}

//	public AgenteP getRunnable(int posicion) {
//		return runnables.get(posicion);
//	}
//	
//	public AgenteP getUserAgent(String alias) {
//		return userAgent.get(alias);
//	}
//	
	public String getAgent(String alias) {
		System.out.println("buscando handler en besa");
		AgHandlerBESA handler = obtenerAgente(alias);
		if(handler!=null)
		{
			System.out.println("Agente "+handler.getAlias()+" encontrado");
			return handler.getAlias();
		}
			
		return "";
	}
	
//	public AgenteP getAgentByAlias(String alias) {
//		return contenedor.getAgByAlias();
//	}

//	public boolean hasAgent(String alias) {
//		return userAgent.containsKey(alias);
//	}

	public AdmBESA getContenedor() {
		return contenedor;
	}

	public void setContenedor(AdmBESA contenedor) {
		this.contenedor = contenedor;
	}

//	public List<AgenteP> getRunnables() {
//		return runnables;
//	}
//	
//
//	public List<AgenteP> getUserAgents()
//	{
//		List <AgenteP> list=new ArrayList<AgenteP>();
//		list.addAll(userAgent.values());
//		return list;
//	}
//	
//	public void setRunnables(ArrayList<AgenteP> runnables) {
//		this.runnables = runnables;
//	}
	

//	public class RunnableImpl implements Runnable {
//
//		public RunnableImpl() {
//
//		}
//
//		private volatile boolean running = true;
//		private volatile int counter = 0;
//		private long threadId;
//		private String threadName;
//
//		public void run() {
//			threadId = Thread.currentThread().getId();
//			threadName = Thread.currentThread().getName();
//
//			while (running) {
//				System.out.println(threadName
//						+ " is alive. " + counter);
//				counter++;
//				try {
//					Thread.sleep(1000l);
//				} catch (InterruptedException ex) {
//					ex.printStackTrace();
//				}
//			}
//		}
//
//		public String getInfo() {
//			return threadName + " " + counter;
//		}
//
//		public void setRunning(boolean running) {
//			this.running = running;
//		}
//	}

}
