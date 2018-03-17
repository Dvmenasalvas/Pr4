package es.ucm.fdi.model.event;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Car;

public class NewCar extends NewVehicle {
	private double faultProbability;
	private int resistance; 
	private int maxFaultDuration;
	private long seed;
	
	public NewCar(int time, String id, int maxSpeed, List<String> itinerary, double faultProbability, int resistance, int maxFaultDuration, long seed) {
		super(time, id, maxSpeed, itinerary);
		this.faultProbability = faultProbability;
		this.resistance = resistance;
		this.maxFaultDuration = maxFaultDuration;
		this.seed = seed;
	}

	public NewCar() {
	}
	
	public void execute(RoadMap simObjects) {
		checkParameters();
		simObjects.addVehicle(new Car(id, maxSpeed, toJunction(itinerary, simObjects), faultProbability, resistance, maxFaultDuration, seed));
	}

	
	public class Builder implements Event.Builder{

		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("new_vehicle") || !"car".equals(sec.getValue("type"))) return null;
			return new NewCar(parseInt(sec, "time", 0), isValidId(sec.getValue("id")), parseInt(sec, "max_speed", 0), parseIdList(sec, "itinerary"), parseDouble(sec,"fault_probability", 0), parseInt(sec, "resistance", 0),parseInt(sec, "max_fault_duration", 0), parseLong(sec, "seed", System.currentTimeMillis()));
		}
		
	}
}
