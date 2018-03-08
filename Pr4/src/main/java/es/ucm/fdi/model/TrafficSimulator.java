package es.ucm.fdi.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.event.Event;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;
import es.ucm.fdi.model.simulatedobject.SimObject;
import es.ucm.fdi.util.MultiTreeMap;

public class TrafficSimulator {
	private MultiTreeMap<Integer, Event> events; 
	private RoadMap simObjects;
	int time;
	
	public TrafficSimulator() {
		simObjects = new RoadMap();
		events = new MultiTreeMap<Integer,Event>();
		time = 0;
	}
	
	public void insertaEvento(Event e) {
		if(e.getTime() >= time && e != null) {
			events.putValue(e.getTime(), e);
		}
	}
	
	public void ejecuta(int pasosSimulacion, OutputStream out) throws IOException {
		int limiteTiempo = time + pasosSimulacion;
		while(time < limiteTiempo) {
			for(Event e : events.get(time)) {		//Ejecuta eventos correspondientes a este tiempo
				e.execute(simObjects);
			}
			
			for(Road r : simObjects.getRoads()) {		//Invoca a avanzar en carreteras
				r.avanza();
			}
			
			for(Junction j : simObjects.getJunctions()) {	//Invoca a avanzar en cruzes
				j.avanza();
			}
			
			time++;
			
			try {
				writeObjects(simObjects.getJunctions(), out);	//Informe de cruzes
				writeObjects(simObjects.getRoads(), out);		//Informe de carreteras
				writeObjects(simObjects.getVehicles(), out);	//Informe de vehiculos
			} catch (IOException e1) {
				throw new IOException("Excepcion de escritura de objetos en tiempo: " + time, e1);
			}
		}
	}
	
	private void writeObjects(List<? extends SimObject> objects, OutputStream out) throws IOException {
		if(out != null) {
			Map<String, String> report = new HashMap<String, String>();	
			for(SimObject o : objects) {
				o.report(time, report);
				writeReport(report, out);
			
			}
		}
	}
	
	private void writeReport(Map<String, String> report, OutputStream out) throws IOException {
		IniSection sec = new IniSection(report.get(""));
		for(Map.Entry<String,String> e : report.entrySet()) {
				sec.setValue(e.getKey(), e.getValue());
		}
		try {
			sec.store(out);
		} catch (IOException e1) {
			throw new IOException("Error al escribir " + report.get("") + " con id " + report.get("id"), e1);
		}
	}

}
