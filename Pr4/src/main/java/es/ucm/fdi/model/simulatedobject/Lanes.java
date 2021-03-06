package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

/**
 * Hereda de Road, la particulariedad que tiene es que es una carretera con un número de carriles y
 * existen nuevas reglas para determinar el factor de reducción de velocidad, pero el método avanza
 * es el mismo que el de Road
 */
public class Lanes extends Road {
	int lanes;

	public Lanes(String id, Junction src, Junction dest, int maxSpeed,
			int length, int numLanes) {
		super(id, src, dest, maxSpeed, length);
		this.lanes = numLanes;
	}

	@Override
	int factorReduccion(int averiados) {
		if (lanes > averiados)
			return 1;
		else
			return 2;
	}

	@Override
	int velocidadBase() {
		return (int) Math.min(maxSpeed,
				maxSpeed * lanes / Math.max(vehicles.sizeOfValues(), 1) + 1);
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		out.put("type", "lanes");
		super.fillReportDetails(out);
	}
}
