package es.ucm.fdi.model.simulatedobject;

import java.util.List;
import java.util.Map;

public class Vehicle extends SimObject{
	protected int  maxSpeed;
	protected int actSpeed;
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
	
	public boolean arrived() {
		return road == null;
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
		actSpeed = 0;
	}
	
	public void setVelocidadActual(int v){
		if(faultyTime == 0) {
			if(v > maxSpeed) actSpeed = maxSpeed;
			else actSpeed = v;
		}
	}
	
	public void avanza(){
		if(faultyTime > 0) {
			faultyTime--;
		} else {
			if(road != null) {
				if(location < road.getLongitud()) {
					if(location + actSpeed >= road.getLongitud()) {		//Entra al cruze
						kilometrage += road.getLongitud() - location;
						location = road.getLongitud();
						actSpeed = 0;
						road.getDest().entraVehiculo(this);
					} else {
						kilometrage += actSpeed;
						location += actSpeed;
					}
				}
			}
		}
	}
	
	public void moverASiguienteCarretera () {
		posItinerary++;
		road.saleVehiculo(this, location);
		if(posItinerary < itinerary.size() - 1) {
			location = 0;
			road = itinerary.get(posItinerary).carreteraSaliente(itinerary.get(posItinerary + 1));
			road.entraVehiculo(this);
		} else {
			road = null;
			arrived = true;
		}
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		out.put("speed", String.valueOf(actSpeed));
		out.put("kilometrage", String.valueOf(kilometrage));
		out.put("faulty", String.valueOf(faultyTime));
		if(road == null) {
			out.put("location", "arrived");
		} else {
			out.put("location", "(" + road.getId() + "," + String.valueOf(location) + ")");
		}
	}

	@Override
	protected String getReportHeader() {
		return "vehicle_report";
	}
}
