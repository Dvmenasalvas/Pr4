package es.ucm.fdi.model.simulatedobject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import es.ucm.fdi.view.SimulatorTablePanel;

/**
 * comentar
 **/

public class Junction extends SimObject
		implements SimulatorTablePanel.Describable {
	// Mapa y lista ordenada para carreteras entrantes
	Map<Road, IncomingRoad> carreterasEntrantes;
	protected List<IncomingRoad> semaforos;
	int verde;
	private Map<Junction, Road> carreterasSalientes;

	public Junction(String id) {
		this.id = id;
		carreterasEntrantes = new HashMap<Road, IncomingRoad>();
		semaforos = new ArrayList<IncomingRoad>();
		carreterasSalientes = new HashMap<Junction, Road>();
		verde = -1;
	}

	public int getVerde() {
		return verde;
	}

	public List<IncomingRoad> getIncomingRoad() {
		return semaforos;
	}

	public void entraVehiculo(Vehicle v) {
		carreterasEntrantes.get(v.getRoad()).entraVehiculo(v);
	}

	public void añadirCarreteraEntrante(Road r) {
		if (!carreterasEntrantes.containsKey(r)) {
			IncomingRoad ir = new IncomingRoad(r);
			carreterasEntrantes.put(r, ir);
			semaforos.add(ir);
		}
	}

	public void añadirCarreteraSaliente(Road r, Junction dest) {
		if (!carreterasSalientes.containsKey(dest)) {
			carreterasSalientes.put(dest, r);
		}
	}

	public void avanza() {
		// Si hay algun semaforo en verde, este actua
		if (verde != -1) {
			semaforos.get(verde).primeroActua();
		}
		actualizarSemaforos();
	}

	void actualizarSemaforos() {
		// Pone el actual en rojo(si hay) y el siguiente en verde
		if (verde != -1) {
			semaforos.get(verde).cambiarSemaforo();
		}
		siguienteSemaforo();
		if (verde != -1) {
			semaforos.get(verde).cambiarSemaforo();
		}
	}

	void siguienteSemaforo() {
		// Pasa al siguiente semaforo, si hay alguno
		if (verde < semaforos.size() - 1) {
			verde++;
		} else if (semaforos.size() != 0) {
			verde = 0;
		}
	}

	public Road carreteraSaliente(Junction destino) {
		if (carreterasSalientes.containsKey(destino)) {
			return carreterasSalientes.get(destino);
		} else {
			return null;
		}
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		StringBuilder queues = new StringBuilder();

		for (IncomingRoad ir : semaforos) {
			queues.append(semInfo(ir));
		}
		if (semaforos.size() != 0) {
			queues.deleteCharAt(queues.length() - 1);
		}
		out.put("queues", queues.toString());
	}

	private String semInfo(IncomingRoad ir) {
		StringBuilder incomingRoad = new StringBuilder();
		String sem = semaforo(ir);

		incomingRoad.append("(" + ir.road.getId() + "," + sem + ",[");
		for (Vehicle v : ir.vehicles) {
			incomingRoad.append(v.getId() + ",");
		}
		if (ir.vehicles.size() > 0) {
			incomingRoad.deleteCharAt(incomingRoad.length() - 1);
		}
		incomingRoad.append("]),");

		return incomingRoad.toString();
	}

	protected String semaforo(IncomingRoad ir) {
		if (ir.semaforo) {
			return "green";
		} else {
			return "red";
		}

	}

	@Override
	protected String getReportHeader() {
		return "junction_report";
	}

	public boolean getConcreteSemaforo(Road r) {
		return carreterasEntrantes.get(r).getSemaforo();
	}

	public class IncomingRoad {
		Queue<Vehicle> vehicles;
		Road road;

		protected boolean semaforo;

		public IncomingRoad(Road road) {
			vehicles = new ArrayDeque<Vehicle>();
			this.road = road;
			semaforo = false;
		}

		public String writeSem() {
			if (semaforo) {
				return "green";
			} else {
				return "red";
			}
		}

		public Road getRoad() {
			return road;
		}

		public boolean getSemaforo() {
			return semaforo;
		}

		public void cambiarSemaforo() {
			semaforo = !semaforo;
		}

		public boolean primeroActua() {
			boolean avanzado = false;
			if (vehicles.size() > 0) {
				if (!vehicles.peek().averiado()) {
					vehicles.poll().moverASiguienteCarretera();
					avanzado = true;
				}
			}
			return avanzado;
		}

		public void entraVehiculo(Vehicle v) {
			vehicles.add(v);
		}

	}

	@Override
	public void describe(Map<String, String> out) {
		out.put("ID", id);

		StringBuilder green = new StringBuilder();
		StringBuilder red = new StringBuilder();
		boolean redEmpty = true;
		boolean greenEmpty = true;
		green.append("[");
		red.append("[");

		for (IncomingRoad ir : semaforos) {
			if (ir.semaforo) {
				green.append(semInfo(ir));
				greenEmpty = false;
			} else {
				red.append(semInfo(ir));
				redEmpty = false;
			}
		}
		if (!greenEmpty) {
			green.deleteCharAt(green.length() - 1);
		}
		if (!redEmpty) {
			red.deleteCharAt(red.length() - 1);
		}
		green.append("]");
		red.append("]");

		out.put("Verde", green.toString());
		out.put("Rojo", red.toString());
	}
}
