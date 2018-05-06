package es.ucm.fdi.model.event;

import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;

public class NewJunction extends Event {
	protected String id;

	public NewJunction(int time, String id) {
		this.time = time;
		this.id = id;
	}

	public NewJunction() {
	}

	@Override
	public void execute(RoadMap simObjects) {
		checkParameters();
		simObjects.addJunction(new Junction(id));
	}

	@Override
	public void describe(Map<String, String> out) {
		out.put("Tiempo", Integer.toString(time));
		out.put("Tipo", "New Junction " + id);
	}

	public class Builder implements Event.Builder {
		@Override
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("new_junction"))
				return null;
			return new NewJunction(parseInt(sec, "time", 0),
					isValidId(sec.getValue("id")));
		}
	}

	@Override
	protected void checkParameters() {
	}

}
