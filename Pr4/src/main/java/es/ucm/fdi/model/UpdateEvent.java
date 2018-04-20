package es.ucm.fdi.model;

import java.util.List;

import javax.swing.event.DocumentEvent.EventType;

import es.ucm.fdi.model.event.Event;

public class UpdateEvent {
	private EventType et;
	public UpdateEvent(EventType et) {
		this.et = et;
	}
	public UpdateEvent(es.ucm.fdi.model.EventType et2) {
		// TODO Auto-generated constructor stub
	}
	/*
	public EventType getEvent() {};
	public RoadMap getRoadMap() {};
	public List<Event> getEvenQueue() {};
	public int getCurrentTime() {};
	*/
}