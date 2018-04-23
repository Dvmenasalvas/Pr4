package es.ucm.fdi.model.simulatedobject;

import java.util.List;
import es.ucm.fdi.view.SimulatorTablePanel;
import java.util.Map;

public class Vehicle extends SimObject implements SimulatorTablePanel.Describable{
	protected int  maxSpeed;
	protected int actSpeed;
	private Road road;			//No necesitamos booleano arrive, ya que road sera igual a null si y solo si el vehiculo ha llegado a su destino
	private int location;
	private List<Junction> itinerary;
	private int posItinerary;
	protected int kilometrage;
	private int faultyTime;
	
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
				if(location < road.getLength()) {
					if(location + actSpeed >= road.getLength()) {		//Entra al cruze
						kilometrage += road.getLength() - location;
						location = road.getLength();
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

	@Override
	public void describe(Map<String, String> out) {
		// TODO Auto-generated method stub
		
	}
}
