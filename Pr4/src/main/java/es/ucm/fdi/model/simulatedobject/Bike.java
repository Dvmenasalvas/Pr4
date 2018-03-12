package es.ucm.fdi.model.simulatedobject;

import java.util.List;
import java.util.Map;

public class Bike extends Vehicle {

	public Bike(String id, int maxSpeed, List<Junction> itinerary) {
		super(id, maxSpeed, itinerary);
	}
	
	@Override
	public void setTiempoAveria(int t) {
		if(actSpeed > maxSpeed / 2) {
			super.setTiempoAveria(t);
		}
	}
	
	@Override
	protected void fillReportDetails(Map<String, String> out) {
		out.put("type", "bike");
		super.fillReportDetails(out);
	}
}
