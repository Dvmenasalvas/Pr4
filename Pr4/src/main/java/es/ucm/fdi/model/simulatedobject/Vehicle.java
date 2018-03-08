package es.ucm.fdi.model.simulatedobject;

import java.util.List;
import java.util.Map;

public class Vehicle extends SimObject{
	private int  maxSpeed, actSpeed;
	private Road road;
	private List<Junction> itinerary;
	private int posItinerary;
	private int location;
	private int kilometrage;
	private int faultyTime;
	private boolean arrived;
	
	public Vehicle(String id, int maxSpeed, List<Junction> itinerary) {
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.itinerary = itinerary;
		road = itinerary.get(0).carreteraSaliente(itinerary.get(1));
		road.entraVehiculo(this);
		posItinerary = 0;
		location = 0;
		kilometrage = 0;
		faultyTime = 0;
		arrived = false;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public boolean averiado() {
		return faultyTime > 0;
	}
	
	public int getTiempoAveria() {
		return faultyTime;
	}
	
	public int getLocation() {
		return location;
	}
	
	public void setTiempoAveria(int t) {
		faultyTime += t;
	}
	
	public void setVelocidadActual(int v){
		if(v > maxSpeed) actSpeed = maxSpeed;
		else actSpeed = v;
	}
	
	public void avanza(){
		if(faultyTime > 0) {
			faultyTime--;
		} else {
			if(location < road.getLongitud()) {
				if(location + actSpeed >= road.getLongitud()) {		//Entra al cruze
					kilometrage += road.getLongitud() - location;
					location = road.getLongitud();
					road.getDest().entraVehiculo(this);
					road = null;
					posItinerary++;
				} else {
					kilometrage += actSpeed;
					location += actSpeed;
				}
			}
		}
	}
	
	public void moverASiguienteCarretera () {
		if(posItinerary < itinerary.size() - 1) {
			location = 0;
			road = itinerary.get(posItinerary).carreteraSaliente(itinerary.get(posItinerary - 1));
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
