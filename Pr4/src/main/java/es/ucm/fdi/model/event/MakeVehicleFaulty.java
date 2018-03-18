package es.ucm.fdi.model.event;

import java.util.List;

import es.ucm.fdi.exceptions.SimulationException;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;

public class MakeVehicleFaulty extends Event {
	private List<String> vehicles;
	private int duration;
	
	public MakeVehicleFaulty(int time, List<String> vehicles, int duration) {
		this.time = time;
		this.vehicles = vehicles;
		this.duration = duration;
	}
	
	public MakeVehicleFaulty() {
	}

	@Override
	public void execute(RoadMap simObjects) {
		checkParameters();
		for(String id: vehicles) {
			simObjects.averiar(id,duration);
		}
	}
	
	public class Builder implements Event.Builder{
		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("make_vehicle_faulty")) return null;
			return new MakeVehicleFaulty(parseInt(sec, "time", 0), parseIdList(sec,"vehicles"), parseInt(sec, "duration", 0));
		}
	}

	@Override
	protected void checkParameters() {
		if(duration < 0) {
			throw new SimulationException("Error en la ejecucion del evento " + this + " la duracion de la averia ha de ser estrictamente positiva.");
		}
		
	}

	
}
