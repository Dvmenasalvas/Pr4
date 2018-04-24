package es.ucm.fdi.model.event;

import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.RoundRobin;

public class NewRoundRobin extends NewJunction {
	int maxTimeSlice;
	int minTimeSlice;

	public NewRoundRobin(int time, String id, int maxTimeSlice, int minTimeSlice) {
		super(time, id);
		this.maxTimeSlice = maxTimeSlice;
		this.minTimeSlice = minTimeSlice;
	}

	public NewRoundRobin() {
	}

	@Override
	public void execute(RoadMap simObjects) {
		checkParameters();
		simObjects.addJunction(new RoundRobin(id, maxTimeSlice, minTimeSlice));
	}

	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Tipo", "New RoundRobin " + id);
	}

	public class Builder implements Event.Builder {
		@Override
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("new_junction") || !"rr".equals(sec.getValue("type")))
				return null;
			return new NewRoundRobin(parseInt(sec, "time", 0), isValidId(sec.getValue("id")),
					parseInt(sec, "max_time_slice", 0), parseInt(sec, "min_time_slice", 0));
		}
	}
}
