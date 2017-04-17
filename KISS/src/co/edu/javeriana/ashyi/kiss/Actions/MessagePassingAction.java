package co.edu.javeriana.ashyi.kiss.Actions;

import co.edu.javeriana.ashyi.kiss.Message;

public abstract class MessagePassingAction<T> extends Action<T> {

	private Message mensaje;

	public MessagePassingAction(Message mensaje)
	{
		this.mensaje = mensaje;
	}

	public Message getMensaje() {
		return mensaje;
	}

	public void setMensaje(Message mensaje) {
		this.mensaje = mensaje;
	}

	
}
