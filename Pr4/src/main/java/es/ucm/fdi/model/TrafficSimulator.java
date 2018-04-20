package es.ucm.fdi.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import es.ucm.fdi.exceptions.SimulationException;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.event.Event;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;
import es.ucm.fdi.model.simulatedobject.SimObject;
import es.ucm.fdi.model.EventType;
import es.ucm.fdi.util.MultiTreeMap;

public class TrafficSimulator {
	private MultiTreeMap<Integer, Event> events; 
	private RoadMap simObjects;
	int time;
	private List<Listener> listeners = new ArrayList<>();
	
	public void addSimulatorListener(Listener l) {
		listeners.add(l);
		UpdateEvent ue = new UpdateEvent(EventType.REGISTERED);
		SwingUtilities.invokeLater(()->l.update(ue, ""));
	}
	
	public void removeListener(Listener l) {
		listeners.remove(l);
	}
	
	private void fireUpdateEvent(EventType type, String error) {
		UpdateEvent ue = new UpdateEvent(type);
		for(Listener l : listeners) {
			 l.update(ue, error);
		 }
	}
	
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
			if(events.containsKey(time)) {
				for(Event e : events.get(time)) {		//Ejecuta eventos correspondientes a este tiempo
					try {
					e.execute(simObjects);
					} catch(SimulationException se) {
						throw new SimulationException("Error en la ejecucion del evento " + e + " en el tiempo " + time + ".", se);
					}
				}
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
			Map<String, String> report;	
			Ini rep = new Ini();
			for(SimObject o : objects) {
				report = new LinkedHashMap<String, String>();	//Mantenemos orden de insercion
				o.report(time, report);
				rep.addsection(writeReport(report, out));
			}
			try {
				rep.store(out);
			} catch (IOException e1) {
				throw new IOException("Error al escribir " + rep, e1);
			}
		}
	}
	
	private IniSection writeReport(Map<String, String> report, OutputStream out) throws IOException {
		IniSection sec = new IniSection(report.get(""));
		for(Map.Entry<String,String> e : report.entrySet()) {
			if(!e.getKey().equals("")) {
				sec.setValue(e.getKey(), e.getValue());
			}
		}
		return sec;
	}
	
	
	public interface Listener {
		 void update(UpdateEvent ue, String error);
	}
	
	public class UpdateEvent {
		private EventType et;
		
		public UpdateEvent(EventType et) {
			this.et = et;
		}
		
		public EventType getEvent() {
			return et;
		};
		public RoadMap getRoadMap() {
			return simObjects;
		};
		public List<Event> getEventsQueue() {
			return events.get(time);
		};
		public int getCurrentTime() {
			return time;
		};
	}
}
