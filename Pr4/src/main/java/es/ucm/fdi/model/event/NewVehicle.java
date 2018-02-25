package es.ucm.fdi.model.event;

import java.util.List;

import es.ucm.fdi.ini.IniSection;

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

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
	public class Builder implements EventBuilder{
		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("new_vehicle")) return null;
			return new NewVehicle(parseInt(sec, "time", 0), sec.getValue("id"), parseInt(sec, "max_speed", 0), parseIdList(sec, "itinerary"));
		}
	}

}
