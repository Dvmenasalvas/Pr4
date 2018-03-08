package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

import es.ucm.fdi.util.MultiTreeMap;

public class Road extends SimObject{
	private int length;
	private int maxSpeed;
	private MultiTreeMap<Integer, Vehicle> vehicles;
	private Junction src;
	private Junction dest;
	
	public Road(String id, Junction src, Junction dest, int maxSpeed, int length) {
		this.id = id;
		this.src = src;
		this.dest = dest;
		this.maxSpeed = maxSpeed;
		this.length = length; 
		vehicles = new MultiTreeMap<Integer, Vehicle>((a, b) -> a - b);
	}
	
	public Junction getSrc() {
		return src;
	}
	
	public int getLongitud() {
		return length;
	}
	
	public Junction getDest() {
		return dest;
	}
	
	public void entraVehiculo(Vehicle v){
		vehicles.putValue(0, v);
	}
	
	public void saleVehiculo(Vehicle v){
		vehicles.removeValue(length, v);
	}
	
	public void avanza(){
		int velBase = (int) Math.min(maxSpeed, maxSpeed / Math.max(vehicles.sizeOfValues(), 1));
		int factorReduccion = 1;
		MultiTreeMap<Integer,Vehicle> actualizados = new MultiTreeMap<Integer, Vehicle>((a, b) -> a - b);
		
		for(Vehicle v : vehicles.innerValues()) {
			v.setVelocidadActual(velBase / factorReduccion);
			v.avanza();
			if(v.averiado()) factorReduccion = 2;
			actualizados.putValue(v.getLocation(), v);
		}
		vehicles = actualizados;
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		out.put("src", src.getId());
		out.put("dest", dest.getId());
		out.put("max_speed", String.valueOf(maxSpeed));
		out.put("length", String.valueOf(length));
	}

	@Override
	protected String getReportHeader() {
		return "road_report";
	}
}
