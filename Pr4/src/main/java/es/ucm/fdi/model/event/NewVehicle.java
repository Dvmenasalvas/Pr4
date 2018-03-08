package es.ucm.fdi.model.event;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;
import es.ucm.fdi.model.simulatedobject.Vehicle;

public class NewVehicle extends Event {
	private String id;
	private int maxSpeed;
	private List<String> itinerary;
	
	public NewVehicle(int time, String id, int maxSpeed, List<String> itinerary) {
		this.time = time;
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.itinerary = itinerary;
	}

	public NewVehicle() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(RoadMap simObjects) {
		List<Junction> it = new ArrayList<Junction>();
		for(String id : itinerary) {
			it.add(simObjects.getJunction(id));
		}
		simObjects.addVehicle(new Vehicle(id, maxSpeed, it));
	}
	
	public class Builder implements Event.Builder{
		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("new_vehicle")) return null;
			return new NewVehicle(parseInt(sec, "time", 0), sec.getValue("id"), parseInt(sec, "max_speed", 0), parseIdList(sec, "itinerary"));
		}
	}

}
