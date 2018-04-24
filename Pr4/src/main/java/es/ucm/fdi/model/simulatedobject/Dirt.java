package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

public class Dirt extends Road {

	public Dirt(String id, Junction src, Junction dest, int maxSpeed, int length) {
		super(id, src, dest, maxSpeed, length);
	}

	@Override
	int factorReduccion(int averiados) {
		return 1 + averiados;
	}

	@Override
	int velocidadBase() {
		return maxSpeed;
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		out.put("type", "dirt");
		super.fillReportDetails(out);
	}

}
