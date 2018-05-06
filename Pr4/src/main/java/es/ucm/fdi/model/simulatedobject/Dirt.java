package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

/**
 * Hereda de Road, la particulariedad que existen nuevas reglas para determinar el factor de
 * reducción de velocidad y la velocidad base, pero el método avanza es el mismo que el de Road
 */
public class Dirt extends Road {

	public Dirt(String id, Junction src, Junction dest, int maxSpeed,
			int length) {
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
