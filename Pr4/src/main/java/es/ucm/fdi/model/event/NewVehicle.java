package es.ucm.fdi.model.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.exceptions.SimulationException;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Vehicle;

public class NewVehicle extends Event {
	protected String id;
	protected int maxSpeed;
	protected List<String> itinerary;
	
	public NewVehicle(int time, String id, int maxSpeed, List<String> itinerary) {
		this.time = time;
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.itinerary = itinerary;
	}

	public NewVehicle() {
	}

	@Override
	public void execute(RoadMap simObjects) {
		checkParameters();
		simObjects.addVehicle(new Vehicle(id, maxSpeed, toJunction(itinerary, simObjects)));
	}
	
	protected void checkParameters() {
		if(maxSpeed <= 0) {
			throw new SimulationException("Error en la ejecucion del evento creador del vehiculo " + id + " la velocidad maxima ha de ser estrictamente positiva.");
		}
		
		if(itinerary.size() < 2) {
			throw new SimulationException("Error en la ejecucion del evento " + id + " el itinerario ha de tener, al menos, dos elementos.");
		}
	}
	
	List<Junction> toJunction(List<String> l, RoadMap simObjects){
		List<Junction> it = new ArrayList<Junction>();
		for(String id : l) {
			it.add(simObjects.getJunction(id));
		}
		return it;
	}
	
	@Override
	public void describe(Map<String, String> out) {
		out.put("Tiempo", Integer.toString(time));
		out.put("Tipo", "New Vehicle " + id);
	}
	
	public class Builder implements Event.Builder{
		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("new_vehicle")) return null;
			return new NewVehicle(parseInt(sec, "time", 0), isValidId(sec.getValue("id")), parseInt(sec, "max_speed", 0), parseIdList(sec, "itinerary"));
		}
	}

}
