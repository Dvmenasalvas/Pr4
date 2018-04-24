package es.ucm.fdi.model;

public enum EventType {
	REGISTERED("registrado"), 
	RESET("reseteado"), 
	NEW_EVENT("nuevo evento"), 
	ADVANCED("avanzar"), 
	ERROR("error");
	
	private String text;

	EventType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
