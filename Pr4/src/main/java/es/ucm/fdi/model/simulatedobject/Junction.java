package es.ucm.fdi.model.simulatedobject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class Junction extends SimObject{
	Map<Road, IncomingRoad> carreterasEntrantes; //Mapa y lista ordenada para semaforo
	List<IncomingRoad> semaforos;
	int verde;
	private Map<Junction, Road> carreterasSalientes;
	
	public Junction(String id) {
		this.id = id;
		carreterasEntrantes = new HashMap<Road, IncomingRoad>();
		semaforos = new ArrayList<IncomingRoad>();
		carreterasSalientes = new HashMap<Junction, Road>();
		verde = -1;
	}
	
	public void entraVehiculo(Vehicle v) {
		carreterasEntrantes.get(v.getRoad()).entraVehiculo(v);
	}
	
	public void añadirCarreteraSaliente(Road r, Junction dest) {
		if(!carreterasSalientes.containsKey(dest)) {
			carreterasSalientes.put(dest, r);
		}
	}
	
	public void añadirCarreteraEntrante(Road r) {
		if(!carreterasEntrantes.containsKey(r)) {
			IncomingRoad ir = new IncomingRoad(r, 1);
			carreterasEntrantes.put(r, ir);
			semaforos.add(ir);
		}
	}
	
	public void avanza() {
		if(verde != -1) { 	//Si hay algun semaforo en verde, este actua
			semaforos.get(verde).primeroActua();
		}
		actualizarSemaforos();
	}
	
	void actualizarSemaforos() { //Pone el actual en rojo(si hay) y el siguiente en verde
		if(verde != -1) {
			semaforos.get(verde).cambiarSemaforo();
		}
		siguienteSemaforo();
		if(verde != -1) {
			semaforos.get(verde).cambiarSemaforo();
		}
	}
	
	void siguienteSemaforo() { 	//Pasa al siguiente semaforo, si hay alguno
		if(verde < semaforos.size() - 1) {	
			verde++;
		} else if(semaforos.size() != 0){
			verde = 0;
		}
	}
	
	public Road carreteraSaliente(Junction destino) {
		if(carreterasSalientes.containsKey(destino)) {
			return carreterasSalientes.get(destino);
		} else {
			return null;
		}
	}
	
	@Override
	protected void fillReportDetails(Map<String, String> out) {
		String queues = "";
		String sem = "";
		for(IncomingRoad ir : semaforos) {
			if(ir.semaforo) {
				sem = "green";
			} else {
				sem = "red";
			}
			
			queues += "(" + ir.road.getId() + "," + sem + ",[";
			for(Vehicle v : ir.vehicles) {
				queues += v.getId() + ",";
			}
			if(ir.vehicles.size() > 0) {
				queues = queues.substring(0, queues.length() - 1);
			}
			queues += "]),";
		}
		if(semaforos.size() != 0) {
			queues = queues.substring(0, queues.length() - 1);
		}
		
		out.put("queues", queues);
	}

	@Override
	protected String getReportHeader() {
		return "junction_report";
	}

	public class IncomingRoad{
		private Queue<Vehicle> vehicles;
		private Road road;
		private boolean semaforo;
		private int timeSlice;
		private int time;
		private int usedTime;

		public IncomingRoad(Road road, int timeSlice) {
			vehicles = new ArrayDeque<Vehicle>();
			this.road = road;
			semaforo = false;
			time = 0;
			usedTime = 0;
			this.timeSlice = timeSlice;
		}
		
		boolean intervaloConsumido() {
			return time == timeSlice;
		}
		
		
		
		public void cambiarSemaforo() {
			if(semaforo) semaforo = false;
			else semaforo = true;
		}
		
		public void primeroActua() {
			if(vehicles.size() > 0) {
				if(!vehicles.peek().averiado()) {
					vehicles.poll().moverASiguienteCarretera();
					usedTime++;
				}
			}
			time++;
		}
		
		public void entraVehiculo(Vehicle v) {
			vehicles.add(v);
		}
		
	}
}
