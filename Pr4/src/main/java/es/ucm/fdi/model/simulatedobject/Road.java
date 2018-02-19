package es.ucm.fdi.model.SimulatedObjects;

import java.util.ArrayList;

import es.ucm.fdi.util.MultiTreeMap;

public class Road {
	private int longitud;
	private int maxVel;
	private int velBase;
	private MultiTreeMap<Integer ,Vehicle> vehiculos;
	
	public void entraVehiculo(Vehicle v){
		vehiculos.putValue(0, v);
	}
	
	public void saleVehiculo(int localizacion, Vehicle v){
		vehiculos.removeValue(localizacion, v);
	}
	
	private int posUltimoAveriado() {
		int pos = -1;
		for(Vehicle v : vehiculos.innerValues()) {
			if(pos == -1 && v.getTiempoAveria() > 0) pos = v.getLocalizacion();
		}
		return pos;
	}
	
	public void avanza(){
		velBase = (int) Math.min(maxVel, maxVel / Math.max(vehiculos.sizeOfValues(), 1));
		int ultAveriado = posUltimoAveriado();
		int factorReduccion = 2;
		for(Vehicle v : vehiculos.innerValues()) {
			if(ultAveriado <= v.getLocalizacion()) factorReduccion = 1;
			v.setVelocidadActual(velBase / factorReduccion);
			v.avanza();
		}
	}
	
	public String generaInforme() {
		String informe = "[road_report]\n";
		informe += "id = " + '\n';
		informe += "time = " + '\n';
		informe += "state = ";
		for(Vehicle v : vehiculos.innerValues()) {
			informe += "(" + cocheid + "," + v.getLocalizacion() + ")," ;
		}
		return informe;
	}
}
