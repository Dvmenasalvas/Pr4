package es.ucm.fdi.model.simulatedobject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class Junction extends SimObject{
	private Map<Road, IncomingRoad> carreterasEntrantes; //Mapa y lista ordenada para semaforo
	private List<IncomingRoad> semaforos;
	private int verde;
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
		semaforos.get(verde).primeroActua();
		if(verde != -1) {
			semaforos.get(verde).cambiarSemaforo();
		}
		if(verde < semaforos.size() - 1) {
			verde++;
		} else {
			verde = 0;
		}
		semaforos.get(verde).cambiarSemaforo();
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
			queues = queues.substring(0, queues.length() - 1);
			queues += "]), ";
		}
		queues = queues.substring(0, queues.length() - 2);
		
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

		public IncomingRoad(Road road) {
			vehicles = new ArrayDeque<Vehicle>();
			this.road = road;
			semaforo = false;
		}
		
		public void cambiarSemaforo() {
			if(semaforo) semaforo = false;
			else semaforo = true;
		}
		
		public void primeroActua() {
			if(!vehicles.peek().averiado())
				vehicles.poll().moverASiguienteCarretera();
		}
		
		public void entraVehiculo(Vehicle v) {
			vehicles.add(v);
		}
		
	}
}
