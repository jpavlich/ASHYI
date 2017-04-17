package edu.javeriana;

import java.io.Serializable;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernellAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;

public class AgenteP extends AgentBESA implements Serializable{

	public AgenteP(String alias, StateBESA estado, StructBESA estructura, double passwd) throws KernellAgentExceptionBESA {
        super(alias, estado, estructura, passwd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setupAgent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdownAgent() {
		// TODO Auto-generated method stub
		
	}

}
