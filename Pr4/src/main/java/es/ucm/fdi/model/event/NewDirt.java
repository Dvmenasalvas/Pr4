package es.ucm.fdi.model.event;

import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Dirt;

public class NewDirt extends NewRoad {

	public NewDirt(int time, String id, String src, String dest, int maxSpeed,
			int length) {
		super(time, id, src, dest, maxSpeed, length);
	}

	public NewDirt() {
	}

	@Override
	public void execute(RoadMap simObjects) {
		checkParameters();
		checkParameters(simObjects);
		simObjects.addRoad(new Dirt(id, simObjects.getJunction(src),
				simObjects.getJunction(dest), maxSpeed, length));
	}

	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Tipo", "New Road " + id);
	}

	public class Builder implements Event.Builder {
		@Override
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("new_road")
					|| !"dirt".equals(sec.getValue("type")))
				return null;
			return new NewDirt(parseInt(sec, "time", 0),
					isValidId(sec.getValue("id")), sec.getValue("src"),
					sec.getValue("dest"), parseInt(sec, "max_speed", 0),
					parseInt(sec, "length", 0));
		}
	}

}
