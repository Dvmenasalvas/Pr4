package es.ucm.fdi.model.event;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Road;

public class NewRoad extends Event {
	String id;
	String src;
	String dest;
	int maxSpeed;
	int length;

	public NewRoad(int time, String id, String src, String dest, int maxSpeed, int length) {
		this.time = time;
		this.id = id;
		this.src = src;
		this.dest = dest;
		this.maxSpeed = maxSpeed;
		this.length = length;
	}
	
	public NewRoad() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(RoadMap simObjects) {
		simObjects.addRoad(new Road(id, simObjects.getJunction(src), simObjects.getJunction(dest), maxSpeed, length));
	}
	
	public class Builder implements Event.Builder{
		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("new_road")) return null;
			return new NewRoad(parseInt(sec, "time", 0), sec.getValue("id"), sec.getValue("src"), sec.getValue("dest"), parseInt(sec, "max_speed", 0), parseInt(sec, "length", 0));
		}
	}

}
