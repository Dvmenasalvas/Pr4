package es.ucm.fdi.model;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.model.event.Event;
import es.ucm.fdi.model.simulatedobject.*;

public class TrafficSimulator {
	private List<Event> events; //LinkedList
	private RoadMap simObjects;
	int time;
	
	public TrafficSimulator() {
		
	}
	
	private int busquedaBin(Event e, List<Event> list, int ini, int fin) {
		if(ini == fin - 1) {
			if(e.getTime() < list.get(ini).getTime()) return ini;
			else return ini + 1;
		} else {
			int mid = ini + (fin - ini) / 2;
			if(e.getTime() < list.get(mid).getTime()) return busquedaBin(e, list, ini, mid);
			else return busquedaBin(e, list, mid, fin);
		}
	}
	
	public void insertaEvento(Event e) {
		if(e.getTime() >= time) {
			events.add(busquedaBin(e, events, 0, events.size()), e);
		}
	}
	
	public void ejecuta(int pasosSimulacion, OutputStream out) {
		int limiteTiempo = time + pasosSimulacion;
		int eventoActual = 0;
		while(time < limiteTiempo) {
			while(events.get(eventoActual).getTime() == time) {		//Ejecutar eventos correspondientes a este tiempo
				events.get(eventoActual).execute();
				eventoActual++;
			}
			
			for(Road r : simObjects.getRoads()) {		//Invoca a avanzar en carreteras
				r.avanza();
			}
			
			for(Junction j : simObjects.getJunctions()) {	//Invoca a avanzar en cruzes
				j.avanza();
			}
			
			time++;
			
			if(out != null) {
				Map<String, String> report = new TreeMap<String, String>();	
				for(Junction j : simObjects.getJunctions()) {	//Informe de cruzes
					j.report(time, report);
					writeReport(report, out);
				}
				
				report = new TreeMap<String, String>();
				for(Road r : simObjects.getRoads()) {		//Informe de carreteras
					r.report(time, report);
					writeReport(report, out);
				}
				
				report = new TreeMap<String, String>();
				for(Vehicle v : simObjects.getVehicles()) {		//Informe de vehiculos
					v.report(time, report);
					writeReport(report, out);
				}
				
			}
			
		}
	}
	
	private void writeReport(Map<String, String> report, OutputStream out) {
		
	}

}
