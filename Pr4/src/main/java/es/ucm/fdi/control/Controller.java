package es.ucm.fdi.control;

import java.io.IOException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.event.*;

public class Controller {
	private final static Event.Builder[] bs = new Event.Builder[] {
			new MakeVehicleFaulty().new Builder(), 
			new NewRoundRobin().new Builder(),
			new NewMostCrowded().new Builder(),
			new NewJunction().new Builder(), 
			new NewLanes().new Builder(), 
			new NewDirt().new Builder(),
			new NewRoad().new Builder(), 
			new NewCar().new Builder(), 
			new NewBike().new Builder(), 
			new NewVehicle().new Builder()
			};
	
	public static Event parseSec(IniSection sec) throws IOException {
		Event e = null;
		for(Event.Builder eb : bs) {
			if((e = eb.parse(sec)) != null) break;
		}
		return e;
	}

}
