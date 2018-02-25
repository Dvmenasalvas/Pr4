package es.ucm.fdi.model.event;

import java.util.List;

import es.ucm.fdi.ini.IniSection;

public class MakeVehicleFaulty extends Event {
	private List<String> vehicles;
	private int duration;
	
	public MakeVehicleFaulty(int time, List<String> vehicles, int duration) {
		this.time = time;
		this.vehicles = vehicles;
		this.duration = duration;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public class Builder implements EventBuilder{
		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("make_vehicle_faulty")) return null;
			return new MakeVehicleFaulty(parseInt(sec, "time", 0), parseIdList(sec,"vehicles"), parseInt(sec, "duration", 0));
		}
	}
}
