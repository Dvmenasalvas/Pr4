package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

/**
 * Sim Object es una clase abstracta de la que luego heredaran Road, Vehicle y Junction, cada
 * SimObject posee una id única, un método para avanzar en cada turno del simulador y un método para
 * escribir un report de que valor tienen los parámetros en un determinado turno del simulador
 */
public abstract class SimObject {
	String id;

	public String getId() {
		return id;
	}

	protected abstract void fillReportDetails(Map<String, String> out);

	protected abstract String getReportHeader();

	public abstract void avanza();

	public void report(int time, Map<String, String> out) {
		out.put("", getReportHeader());
		out.put("id", id);
		out.put("time", String.valueOf(time));
		fillReportDetails(out);
	}

	public boolean equals(SimObject o) {
		return id == o.getId();
	}

	// Hashcode
}
