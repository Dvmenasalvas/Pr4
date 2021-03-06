package es.ucm.fdi.model.event;

import java.util.List;
import java.util.Map;

import es.ucm.fdi.exceptions.SimulationException;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Vehicle;
import es.ucm.fdi.model.simulatedobject.Junction.IncomingRoad;

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
		for (String id : vehicles) {
			if (simObjects.getVehicle(id) != null) {
				simObjects.getVehicle(id).setTiempoAveria(duration);
			} else {
				throw new SimulationException(
						"Se ha intentado averiar un coche no existente con id: "
								+ id + ".");
			}
		}
	}

	public class Builder implements Event.Builder {
		@Override
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("make_vehicle_faulty"))
				return null;
			return new MakeVehicleFaulty(parseInt(sec, "time", 0),
					parseIdList(sec, "vehicles"), parseInt(sec, "duration", 0));
		}
	}

	@Override
	protected void checkParameters() {
		if (duration < 0) {
			throw new SimulationException(
					"La duracion de la averia ha de ser estrictamente positiva.");
		}

	}

	@Override
	public void describe(Map<String, String> out) {
		out.put("Tiempo", Integer.toString(time));
		out.put("Tipo", this.toString());
	}

	public String toString() {
		StringBuilder lAveriar = new StringBuilder();
		lAveriar.append("Break Vehicles [");
		for (String v : vehicles) {
			lAveriar.append(v + ",");
		}
		if (vehicles.size() != 0) {
			lAveriar.deleteCharAt(lAveriar.length() - 1);
		}
		lAveriar.append("]");

		return lAveriar.toString();
	}

}
