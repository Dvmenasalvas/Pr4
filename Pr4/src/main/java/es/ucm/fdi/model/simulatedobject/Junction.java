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
	
	public String generaInforme() {
		String informe = "[junction_report]\n";
		/*
		informe += "id = " + '\n';
		informe += "time = " + '\n';
		informe += "queues = ";
		for(Road r : colas.keySet()) {
			informe += "(" + r.getId();
			if(r.equals(semaforoVerde)) {
				informe += ",green,[";
			} else {
				informe += ",red,[";
			}
			for(Vehicle v : colas.get(r)) {
				informe += v.getId() + ",";
			}
			informe = informe.substring(0, informe.length() - 2);
			informe += "]),";
		}
		informe = informe.substring(0, informe.length() - 2);
		*/
		return informe;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getReportHeader() {
		// TODO Auto-generated method stub
		return null;
	}
}
