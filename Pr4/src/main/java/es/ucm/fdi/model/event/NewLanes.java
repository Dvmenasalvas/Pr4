package es.ucm.fdi.model.event;

import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Lanes;

public class NewLanes extends NewRoad {
	int lanes;

	public NewLanes(int time, String id, String src, String dest, int maxSpeed, int length,
			int lanes) {
		super(time, id, src, dest, maxSpeed, length);
		this.lanes = lanes;
	}

	public NewLanes() {
	}

	@Override
	public void execute(RoadMap simObjects) {
		checkParameters();
		checkParameters(simObjects);
		simObjects.addRoad(new Lanes(id, simObjects.getJunction(src), simObjects.getJunction(dest),
				maxSpeed, length, lanes));
	}

	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Tipo", "New Lanes " + id);
	}

	public class Builder implements Event.Builder {
		@Override
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("new_road") || !"lanes".equals(sec.getValue("type")))
				return null;
			return new NewLanes(parseInt(sec, "time", 0), isValidId(sec.getValue("id")),
					sec.getValue("src"), sec.getValue("dest"), parseInt(sec, "max_speed", 0),
					parseInt(sec, "length", 0), parseInt(sec, "lanes", 1));
		}
	}

}
