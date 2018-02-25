package es.ucm.fdi.model.simulatedobject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import es.ucm.fdi.util.MultiTreeMap;

public class Junction extends SimObject{
	private Map<String, CarreteraEntrante> carreteras;

	public void entraVehiculo(Vehicle v) {
		carreteras.get(v.getRoadId()).entraVehiculo(v);
	}
	
	public void avanza() {
		
	}

	public class CarreteraEntrante{
		private boolean semaforo;
		private Queue<Vehicle> vehiculos;
		private String carreteraId;
		
		public void entraVehiculo(Vehicle v) {
			vehiculos.add(v);
		}
		
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		//Necesitamos colas
	}

	@Override
	protected String getReportHeader() {
		return "junction_report";
	}
}
