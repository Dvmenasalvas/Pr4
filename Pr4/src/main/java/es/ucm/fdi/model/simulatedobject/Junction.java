package es.ucm.fdi.model.simulatedobject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class Junction extends SimObject{
	Map<Road, IncomingRoad> carreterasEntrantes; //Mapa y lista ordenada para carreteras entrantes
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
			IncomingRoad ir = new IncomingRoad(r);
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
		StringBuilder queues = new StringBuilder();
		String sem ;
		for(IncomingRoad ir : semaforos) {
			sem = ir.writeSem();
			
			
			queues.append("(" + ir.road.getId() + "," + sem + ",[");
			for(Vehicle v : ir.vehicles) {
				queues.append(v.getId() + ",");
			}
			if(ir.vehicles.size() > 0) {
				queues.deleteCharAt(queues.length() - 1);
			}
			queues.append("]),");
		}
		if(semaforos.size() != 0) {
			queues.deleteCharAt(queues.length() - 1);
		}
		
		out.put("queues", queues.toString());
	}

	@Override
	protected String getReportHeader() {
		return "junction_report";
	}

	public class IncomingRoad{
		Queue<Vehicle> vehicles;
		Road road;
		boolean semaforo;

		public IncomingRoad(Road road) {
			vehicles = new ArrayDeque<Vehicle>();
			this.road = road;
			semaforo = false;
		}	
			
		public String writeSem() {
			if(semaforo) {
				return "green";
			} else {
				return "red";
			}
		}

		public void cambiarSemaforo() {
			if(semaforo) semaforo = false;
			else semaforo = true;
		}
		
		public void primeroActua() {
			if(vehicles.size() > 0) {
				if(!vehicles.peek().averiado()) {
					vehicles.poll().moverASiguienteCarretera();
				}
			}
		}
		
		public void entraVehiculo(Vehicle v) {
			vehicles.add(v);
		}

		
	}
}
