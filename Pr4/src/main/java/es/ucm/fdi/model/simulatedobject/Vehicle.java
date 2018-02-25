package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

public class Vehicle extends SimObject{
	private int  maxSpeed, actSpeed;
	private Road road;
	private Road[] itinerary;
	private int posItinerary;
	private int location;
	private int kilometrage;
	private int faultyTime;
	private boolean arrived;
	
	public String getRoadId() {
		return road.getId();
	}
	
	public int getTiempoAveria() {
		return faultyTime;
	}
	
	public int getLocalizacion() {
		return location;
	}
	
	public void setTiempoAveria(int t) {
		faultyTime += t;
	}
	
	public void setVelocidadActual(int v){
		if(v > maxSpeed) actSpeed = maxSpeed;
		else actSpeed = v;
	}
	
	public String generalInforme(){
		String informe = "[vehicle_report]\n";
	
		return informe;
	}
	
	public void avanza(){
		if(faultyTime > 0) {
			faultyTime--;
		} else {
			if(location < road.getLongitud()) {
				if(location + actSpeed > road.getLongitud()) {		//Entra al cruze
					kilometrage += road.getLongitud() - location;
					location = road.getLongitud();
					road.getJunction().entraVehiculo(this);
					road = null;
				} else {
					kilometrage += actSpeed;
					location += actSpeed;
				}
			}
		}
	}
	
	public void moverASiguienteCarretera () {
		posItinerary++;
		if(posItinerary < itinerary.length) {
			location = 0;
			road = itinerary[posItinerary];
		} else {
			arrived = true;
		}
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		out.put("speed", String.valueOf(actSpeed));
		out.put("kilometrage", String.valueOf(kilometrage));
		out.put("faulty", String.valueOf(faultyTime));
		out.put("location", "(" + road.getId() + "," + String.valueOf(location) + ")");
	}

	@Override
	protected String getReportHeader() {
		return "vehicle_report";
	}
}
