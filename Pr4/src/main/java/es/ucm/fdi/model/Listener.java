package es.ucm.fdi.model;

import java.awt.Event;
import java.util.List;

import javax.swing.event.DocumentEvent.EventType;

public interface Listener {
	void registered(UpdateEvent ue);
	void reset(UpdateEvent ue);
	void newEvent(UpdateEvent ue);
	void advanced(UpdateEvent ue);
	void error(UpdateEvent ue, String error);
	
	public class UpdateEvent {
		private EventType et;
		public UpdateEvent(EventType et) {
			this.et = et;
		}
		public UpdateEvent(es.ucm.fdi.model.EventType et2) {
			// TODO Auto-generated constructor stub
		}
		public EventType getEvent() {...}
		public RoadMap getRoadMap() {...}
		public List<Event> getEvenQueue() {...}
		public int getCurrentTime() {...}
}
