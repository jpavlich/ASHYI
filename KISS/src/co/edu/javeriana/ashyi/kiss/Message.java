package co.edu.javeriana.ashyi.kiss;

import co.edu.javeriana.ashyi.kiss.Data.Data;


/**
 * @author ASHYI
 * Datos manejados para los eventos en PUMAS_Lite
 */
public class Message extends Data{
	
	//private Grafo g;
	/**
	 * id unico que identifica al mensaje
	 */
	private String messageId; //id unico que identifica al mensaje	
    /**
     * Objeto a enviar en el mensaje
     */
    private Object message;
    /**
     * alias del agente quien recibe el mensaje
     * en caso de tener un destinatario especifico en lugar del usual
     */
    private String receiverAgent; //en caso de tener un destinatario especifico en lugar del usual
    /**
     * alias del agente quien envia el mensaje
     */
    private String senderAgent;
    /**
     * alias auxiliar de agente
     */
    private String aliasAgentAux; //si es necesario otro    
	/**
	 * Clase de la guarda a activar en el mensaje
	 */
	private Class guardToActivate;
    
	/**
	 * Constructor base
	 */
	public Message() {
		super();
	}
	
	/**
	 * @param messageId id unico que identifica al mensaje
	 * @param messageType Tipo de mensaje
	 * @param message Objeto a enviar en el mensaje
	 * @param receiverAgent alias del agente quien recibe el mensaje
	 * @param senderAgent alias del agente quien envia el mensaje
	 */
	public Message(String messageId, Object message,
			String receiverAgent, String senderAgent) {
		super();
		this.messageId = messageId;
		this.message = message;
		this.receiverAgent = receiverAgent;
		this.senderAgent = senderAgent;
		this.guardToActivate = guardToActivate;
	}
	
	/**
	 * @param messageType Tipo de mensaje
	 * @param message Objeto a enviar en el mensaje
	 * @param receiverAgent alias del agente quien recibe el mensaje
	 * @param senderAgent alias del agente quien envia el mensaje
	 * @param aliasAgentAux alias auxiliar de agente
	 */
	public Message(Object message, String receiverAgent, String senderAgent,
			String aliasAgentAux) {
		super();
		this.message = message;
		this.receiverAgent = receiverAgent;
		this.senderAgent = senderAgent;
		this.aliasAgentAux = aliasAgentAux;
	}



	/**
	 * @param messageType Tipo de mensaje
	 * @param message Objeto a enviar en el mensaje
	 * @param receiverAgent alias del agente quien recibe el mensaje
	 * @param senderAgent alias del agente quien envia el mensaje
	 */
	public Message(Object message, String receiverAgent, String senderAgent) {
		super();
		this.message = message;
		this.receiverAgent = receiverAgent;
		this.senderAgent = senderAgent;
	}

	/**
	 * @param messageType Tipo de mensaje
	 * @param message Objeto a enviar en el mensaje
	 * @param senderAgent alias del agente quien envia el mensaje
	 */
	public Message(Object message, String senderAgent) {
		super();
		this.message = message;
		this.senderAgent = senderAgent;
	}

	/**
	 * @return id unico que identifica al mensaje
	 */
	public String getMessageId() {
		return messageId;
	}


	/**
	 * @param messageId id unico que identifica al mensaje
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}


	/**
	 * @return Objeto a enviar en el mensaje
	 */ 
	public Object getMessage() {
		return message;
	}


	/**
	 * @param message Objeto a enviar en el mensaje
	 */
	public void setMessage(Object message) {
		this.message = message;
	}


	/**
	 * @return alias del agente quien recibe el mensaje
	 */ 
	public String getReceiverAgent() {
		return receiverAgent;
	}


	/**
	 * @param receiverAgent alias del agente quien recibe el mensaje
	 */
	public void setReceiverAgent(String receiverAgent) {
		this.receiverAgent = receiverAgent;
	}


	/**
	 * @return alias del agente quien envia el mensaje
	 */
	public String getSenderAgent() {
		return senderAgent;
	}


	/**
	 * @param senderAgent alias del agente quien envia el mensaje
	 */
	public void setSenderAgent(String senderAgent) {
		this.senderAgent = senderAgent;
	}


	/**
	 * @return  Clase de la guarda a activar en el mensaje
	 */
	public Class getGuardToActivate() {
		return guardToActivate;
	}


	/**
	 * @param guardToActivate Clase de la guarda a activar en el mensaje
	 */
	public void setGuardToActivate(Class guardToActivate) {
		this.guardToActivate = guardToActivate;
	}
	
	 /**
	 * @return alias auxiliar de agente
	 */
	public String getAliasAgentAux() {
		return aliasAgentAux;
	}

	/**
	 * @param aliasAgentAux alias auxiliar de agente
	 */
	public void setAliasAgentAux(String aliasAgentAux) {
		this.aliasAgentAux = aliasAgentAux;
	}
    
}
