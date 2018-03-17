package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

import es.ucm.fdi.util.MultiTreeMap;

public class Road extends SimObject{
	private int length;
	protected int maxSpeed;
	protected MultiTreeMap<Integer, Vehicle> vehicles;
	private Junction src;
	private Junction dest;
	
	public Road(String id, Junction src, Junction dest, int maxSpeed, int length) {
		this.id = id;
		this.src = src;
		this.dest = dest;
		this.maxSpeed = maxSpeed;
		this.length = length; 
		vehicles = new MultiTreeMap<Integer, Vehicle>((a, b) -> b - a);
	}
	
	public Junction getSrc() {
		return src;
	}
	
	public int getLength() {
		return length;
	}
	
	public Junction getDest() {
		return dest;
	}
	
	public void entraVehiculo(Vehicle v){
		vehicles.putValue(0, v);
	}
	
	public void saleVehiculo(Vehicle v, int loc){
		vehicles.removeValue(loc, v);
	}
	
	public void avanza(){
		int velBase = velocidadBase();
		int averiados = 0;
		MultiTreeMap<Integer,Vehicle> actualizados = new MultiTreeMap<Integer, Vehicle>((a, b) -> b - a);
		
		for(Vehicle v : vehicles.innerValues()) {
			if(v.getLocation() < length) {				//Si el vehiculo no esta en un cruze, actualizamos su velocidad
				v.setVelocidadActual(velBase / factorReduccion(averiados));
			}
			v.avanza();
			if(v.averiado()) averiados++;
			if(!v.arrived()) {
				actualizados.putValue(v.getLocation(), v);
			}
		}
		vehicles = actualizados;
	}
	
	int factorReduccion(int averiados) {
		if(averiados == 0) return 1;
		else return 2;
	}
	
	int velocidadBase() {
		return (int) Math.min(maxSpeed, maxSpeed / Math.max(vehicles.sizeOfValues(), 1) + 1);
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		String state = "";
		for(Vehicle v : vehicles.innerValues()) {
			state += "(" + v.getId() + "," + v.getLocation() + "),";
		}
		if(vehicles.size() > 0) {
			
			state = state.substring(0, state.length() - 1);
		}
		out.put("state", state);
	}

	@Override
	protected String getReportHeader() {
		return "road_report";
	}
}
