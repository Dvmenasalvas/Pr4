package es.ucm.fdi.model.event;

public abstract class Event {
	int time;
	
	public int getTime() {
		return time;
	}
	
	public abstract void execute();
}
