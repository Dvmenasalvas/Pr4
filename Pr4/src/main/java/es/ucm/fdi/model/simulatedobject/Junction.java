package es.ucm.fdi.model.simulatedobject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import es.ucm.fdi.util.MultiTreeMap;

public class Junction {
	private ArrayList<CarreteraEntrante> carreteras;
	private String id;

	public void entraVehiculo(Vehicle v) {
		carreteras.get(v.getRoad()).entraVehiculo(v);
	}
	
	public void avanza() {
		carreteras.g
	}
	
	public String generaInforme() {
		String informe = "[junction_report]\n";
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
		return informe;
	}

	private class CarreteraEntrante{
		private boolean semaforo;
		private ArrayDeque<Vehicle> vehiculos;
		private String carreteraId;
		
		public boolean getSemaforo() {
			return semaforo;
		}
		
		public void verde() {
			semaforo = true;
		}
		
		public void rojo() {
			semaforo = false;
		}
		
		public void entraVehiculo(Vehicle v) {
			vehiculos.push(v);
		}
		
	}
}
