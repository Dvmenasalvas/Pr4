package es.ucm.fdi.model.event;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Bike;

public class NewBike extends NewVehicle {

	public NewBike(int time, String id, int maxSpeed, List<String> itinerary) {
		super(time, id, maxSpeed, itinerary);
	}

	public NewBike() {
	}
	
	public void execute(RoadMap simObjects) {
		checkParameters();
		simObjects.addVehicle(new Bike(id, maxSpeed, toJunction(itinerary, simObjects)));
	}

	public class Builder implements Event.Builder{

		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("new_vehicle") || !"bike".equals(sec.getValue("type"))) return null;
			return new NewBike(parseInt(sec, "time", 0), isValidId(sec.getValue("id")), parseInt(sec, "max_speed", 0), parseIdList(sec, "itinerary"));
		}
		
	}
}
