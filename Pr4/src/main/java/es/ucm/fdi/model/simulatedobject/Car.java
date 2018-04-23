package es.ucm.fdi.model.simulatedobject;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Car extends Vehicle {
	private int resistance;
	private int ultAveria;
	private double faultProbability;
	private int maxFaultDuration;
	private Random randomNumber;

	public Car(String id, int maxSpeed, List<Junction> itinerary, double faultProbability,
			int resistance, int maxFaultDuration, long seed) {
		super(id, maxSpeed, itinerary);
		this.resistance = resistance;
		ultAveria = 0;
		this.faultProbability = faultProbability;
		this.maxFaultDuration = maxFaultDuration;
		randomNumber = new Random(seed);
	}

	@Override
	public void avanza() {
		if (!averiado() && ultAveria > resistance && randomNumber.nextDouble() < faultProbability) {
			setTiempoAveria(randomNumber.nextInt(maxFaultDuration) + 1);
			ultAveria = 0;
		}
		int aux = kilometrage;
		super.avanza();
		ultAveria += kilometrage - aux;
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		out.put("type", "car");
		super.fillReportDetails(out);
	}
}
