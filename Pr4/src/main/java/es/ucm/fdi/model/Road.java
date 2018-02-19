package es.ucm.fdi.model;

import java.util.ArrayList;

public class Road {
	private int longitud;
	private int maxVel, baseVel;
	private ArrayList<Vehicle> lista;
	
	public void entraVehiculo(Vehicle v){
		lista.add(0, v);
	}
	
	public void saleVehiculo(Vehicle v){
		lista.remove(v);
	}
	
	public void avanza(){
		baseVel = Math.min(maxVel, maxVel / Math.max(lista.size(), 1));
		for(int i = 0; i < lista.size(); i++){
			lista[i].
		}
	}
}
