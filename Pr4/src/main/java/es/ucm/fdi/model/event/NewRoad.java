package es.ucm.fdi.model.event;

import java.io.IOException;
import java.util.Map;

import es.ucm.fdi.exceptions.SimulationException;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Road;

public class NewRoad extends Event {
	String id;
	String src;
	String dest;
	int maxSpeed;
	int length;

	public NewRoad(int time, String id, String src, String dest, int maxSpeed,
			int length) {
		this.time = time;
		this.id = id;
		this.src = src;
		this.dest = dest;
		this.maxSpeed = maxSpeed;
		this.length = length;
	}

	public NewRoad() {
	}

	@Override
	public void execute(RoadMap simObjects) {
		checkParameters();
		checkParameters(simObjects);
		simObjects.addRoad(new Road(id, simObjects.getJunction(src),
				simObjects.getJunction(dest), maxSpeed, length));
	}

	@Override
	protected void checkParameters() {
		if (maxSpeed <= 0) {
			throw new SimulationException(
					"Error en la ejecucion del evento de creacion de la carretera "
							+ id
							+ " la velocidad maxima ha de ser estrictamente positiva.");
		}

		if (length <= 0) {
			throw new SimulationException(
					"Error en la ejecucion del evento de creacion de la carretera "
							+ id
							+ " la longitud ha de ser estrictamente positiva.");
		}
	}

	protected void checkParameters(RoadMap simObjects) {
		if (simObjects.getJunction(src) == null) {
			throw new SimulationException(
					"Error en la ejecucion del evento de creacion de la carretera "
							+ id + " el cruce de salida es nulo.");
		}

		if (simObjects.getJunction(dest) == null) {
			throw new SimulationException(
					"Error en la ejecucion del evento de creacion de la carretera "
							+ id + " el cruce de llegada es nulo.");
		}
	}

	@Override
	public void describe(Map<String, String> out) {
		out.put("Tiempo", Integer.toString(time));
		out.put("Tipo", "New Road " + id);
	}

	public class Builder implements Event.Builder {
		@Override
		public Event parse(IniSection sec) throws IOException {
			if (!sec.getTag().equals("new_road"))
				return null;
			if (sec.getValue("src") == null || sec.getValue("dest") == null)
				throw new IOException("La carretera " + sec.getValue("id")
						+ " esta mal definida en el archivo de lectura.");
			else
				return new NewRoad(parseInt(sec, "time", 0),
						isValidId(sec.getValue("id")), sec.getValue("src"),
						sec.getValue("dest"), parseInt(sec, "max_speed", 0),
						parseInt(sec, "length", 0));
		}
	}

}
