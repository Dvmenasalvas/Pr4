package es.ucm.fdi.model.simulatedobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import es.ucm.fdi.util.MultiTreeMap;

public class Road extends SimObject{
	private int longitud;
	private int maxVel;
	private List<Vehicle> vehiculos;
	private Junction junction;
	
	public int getLongitud() {
		return longitud;
	}
	
	public Junction getJunction() {
		return junction;
	}
	
	public void entraVehiculo(Vehicle v){
		vehiculos.add(v);
	}
	
	public void saleVehiculo(Vehicle v){
		vehiculos.remove(v);
	}
	
	private int posUltimoAveriado() {
		int pos = -1;
		for(Vehicle v : vehiculos) {
			if(v.getLocalizacion() > pos && v.getTiempoAveria() > 0) pos = v.getLocalizacion();
		}
		return pos;
	}
	
	public void avanza(){
		int velBase = (int) Math.min(maxVel, maxVel / Math.max(vehiculos.size(), 1));
		int ultAveriado = posUltimoAveriado();
		int factorReduccion = 2;
		for(Vehicle v : vehiculos) {
			if(ultAveriado <= v.getLocalizacion()) factorReduccion = 1;
			v.setVelocidadActual(velBase / factorReduccion);
			v.avanza();
		}
	}
	
	public String generaInforme() {
		String informe = "[road_report]\n";
		/*
		informe += "id = " + '\n';
		informe += "time = " + '\n';
		informe += "state = ";
		for(Vehicle v : vehiculos.innerValues()) {
			informe += "(" + v.getId() + "," + v.getLocalizacion() + "),";
		}
		informe = informe.substring(0, informe.length() - 2);
		*/
		return informe;
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getReportHeader() {
		// TODO Auto-generated method stub
		return null;
	}
}
