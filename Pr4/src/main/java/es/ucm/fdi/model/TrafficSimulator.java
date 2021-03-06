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
import es.ucm.fdi.view.SimulatorTablePanel;

/**
 * TrafficSimulator se instancia solo una vez y es el encargado de invocar a los métodos avanza
 * correspondientes en cada nuevo turno, gestiona también los pasos de la simulación y a su vez se
 * encarga de la gestión de eventos del tipo EventType, para ello posee una clase interna,
 * UpdateEvent, descrita posteriormente y una interfaz SimulatorListener que deben implementar todos
 * los listneres que estén escuchando los eventos. Cuando ocurre un nuevo evento TrafficSimulator se
 * lo hace saber a todos los listeners de una lista que tiene como atributo, lanzándolos mediante el
 * método fireUpdateEvent que básicamente pide a cada listener que haga lo que tenga que hacer para
 * gestionar el nuevo evento que ha sucedido
 */

public class TrafficSimulator {
	private MultiTreeMap<Integer, Event> events;
	private RoadMap simObjects;
	int time;
	private List<SimulatorListener> listeners = new ArrayList<>();

	public void addSimulatorListener(SimulatorListener l) {
		listeners.add(l);
		UpdateEvent ue = new UpdateEvent(EventType.REGISTERED);
		SwingUtilities.invokeLater(() -> l.registered(ue));
	}

	public void removeListener(SimulatorListener l) {
		listeners.remove(l);
	}

	private void fireUpdateEvent(EventType type, String error) {
		UpdateEvent ue = new UpdateEvent(type);
		SwingUtilities.invokeLater(() -> {
		switch (type) {
		case ADVANCED:
			for (SimulatorListener l : listeners) {
				l.advanced(ue);
			}
			break;
		case ERROR:
			for (SimulatorListener l : listeners) {
				l.error(ue, error);
			}
			break;
		case NEW_EVENT:
			for (SimulatorListener l : listeners) {
				l.newEvent(ue);
			}
			break;
		case RESET:
			for (SimulatorListener l : listeners) {
				l.reset(ue);
			}
			break;
		default:
			break;
		}
		});

	}

	public TrafficSimulator() {
		simObjects = new RoadMap();
		events = new MultiTreeMap<Integer, Event>();
		time = 0;
	}

	public void insertaEvento(Event e) {
		if (e != null && e.getTime() >= time) {
			events.putValue(e.getTime(), e);
			fireUpdateEvent(EventType.NEW_EVENT, "");
		}
	}
	
	public RoadMap getRoadMap(){
		return simObjects;
	}

	public void reset() {
		events = new MultiTreeMap<Integer, Event>();
		simObjects = new RoadMap();
		time = 0;
		fireUpdateEvent(EventType.RESET, "");
	}

	public void ejecuta(int pasosSimulacion, OutputStream out){
		int limiteTiempo = time + pasosSimulacion;
		while (time < limiteTiempo) {
			if (events.containsKey(time)) {
				// Ejecuta eventos correspondientes a este tiempo
				for (Event e : events.get(time)) {
					try {
						e.execute(simObjects);
					} catch (SimulationException se) {
						fireUpdateEvent(EventType.ERROR,
								"Error en la ejecucion del evento "
										+ e.toString() + " en el tiempo " + time
										+ "\n" + se.getMessage() + '\n'
										+ "Este evento no se ejecutara.");
					}
				}
			}

			for (Road r : simObjects.getRoads()) {
				r.avanza();
			}

			for (Junction j : simObjects.getJunctions()) {
				j.avanza();
			}

			time++;

			try {
				writeObjects(simObjects.getJunctions(), out);
				writeObjects(simObjects.getRoads(), out);
				writeObjects(simObjects.getVehicles(), out);
			} catch (IOException e1) {
				fireUpdateEvent(EventType.ERROR,
						"Excepcion de escritura de objetos en tiempo: "
						+ time + '\n' +
						e1.getMessage() + '\n'
						+ "Este informe no se generara correctamente.");
			} finally {
				fireUpdateEvent(EventType.ADVANCED, "");
			}
		}
	}

	public String stringReport() {
		return report(simObjects.getJunctions()).toString()
				+ report(simObjects.getRoads()).toString()
				+ report(simObjects.getVehicles()).toString();

	}

	private void writeObjects(List<? extends SimObject> objects,
			OutputStream out) throws IOException {
		if (out != null) {
			Ini rep = report(objects);
			try {
				rep.store(out);
			} catch (IOException e1) {
				throw new IOException("Error al escribir " + rep, e1);
			}
		}
	}

	public Ini report(List<? extends SimObject> objects) {
		Map<String, String> report;
		Ini rep = new Ini();
		for (SimObject o : objects) {
			report = new LinkedHashMap<String, String>(); // Mantenemos orden de insercion
			o.report(time, report);
			rep.addsection(writeReport(report));
		}
		return rep;
	}

	private IniSection writeReport(Map<String, String> report) {
		IniSection sec = new IniSection(report.get(""));
		for (Map.Entry<String, String> e : report.entrySet()) {
			if (!e.getKey().equals("")) {
				sec.setValue(e.getKey(), e.getValue());
			}
		}
		return sec;
	}

	public interface SimulatorListener {
		void registered(UpdateEvent ue);

		void reset(UpdateEvent ue);

		void newEvent(UpdateEvent ue);

		void advanced(UpdateEvent ue);

		void error(UpdateEvent ue, String error);
	}

	/**
	 * Esta clase implementa los métodos que puede ser que utilice el listener para gestionar un
	 * determinado evento tales como pedirle al simulador el roadmap, pedirle una cola de
	 * eventos(newVehicle, newRoad, etc) o pedirle el turno en el que está
	 */
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

		public List<SimulatorTablePanel.Describable> getEventsQueue() {
			List<SimulatorTablePanel.Describable> queue = new ArrayList<SimulatorTablePanel.Describable>();
			for (Event e : events.innerValues()) {
				if (e.getTime() >= time) {
					queue.add(e);
				}
			}
			return queue;
		};

		public int getCurrentTime() {
			return time;
		};
	}
}
