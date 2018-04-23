package es.ucm.fdi.model.event;

import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.MostCrowded;

public class NewMostCrowded extends NewRoundRobin {

	public NewMostCrowded(int time, String id, int maxTimeSlice, int minTimeSlice) {
		super(time, id, maxTimeSlice, minTimeSlice);
	}

	public NewMostCrowded() {
	}

	@Override
	public void execute(RoadMap simObjects) {
		checkParameters();
		simObjects.addJunction(new MostCrowded(id, maxTimeSlice, minTimeSlice));
	}

	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Tipo", "New MostCrowded " + id);
	}

	public class Builder implements Event.Builder {
		@Override
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("new_junction") || !"mc".equals(sec.getValue("type")))
				return null;
			return new NewMostCrowded(parseInt(sec, "time", 0), isValidId(sec.getValue("id")),
					parseInt(sec, "max_time_slice", 0), parseInt(sec, "min_time_slice", 0));
		}
	}
}
