package es.ucm.fdi.model.simulatedobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ucm.fdi.view.SimulatorTablePanel;

import es.ucm.fdi.util.MultiTreeMap;

public class Road extends SimObject implements SimulatorTablePanel.Describable {
	private int length;
	protected int maxSpeed;
	protected MultiTreeMap<Integer, Vehicle> vehicles;
	private Junction src;
	private Junction dest;

	public Road(String id, Junction src, Junction dest, int maxSpeed,
			int length) {
		this.id = id;
		this.src = src;
		this.dest = dest;
		this.maxSpeed = maxSpeed;
		this.length = length;
		vehicles = new MultiTreeMap<Integer, Vehicle>((a, b) -> b - a);
		src.añadirCarreteraSaliente(this, dest);
		dest.añadirCarreteraEntrante(this);
	}

	public Junction getSrc() {
		return src;
	}

	public int getLength() {
		return length;
	}

	public MultiTreeMap<Integer, Vehicle> getVehicles() {
		return vehicles;
	}

	public int numVehicles() {
		return vehicles.size();
	}

	public List<Integer> vehiclesPosition() {
		List<Integer> p = new ArrayList<Integer>();
		for (Vehicle v : vehicles.innerValues()) {
			p.add(v.getLocation());
		}
		return p;
	}

	public Junction getDest() {
		return dest;
	}

	public void entraVehiculo(Vehicle v) {
		vehicles.putValue(0, v);
	}

	public void saleVehiculo(Vehicle v, int loc) {
		vehicles.removeValue(loc, v);
	}

	public void avanza() {
		int velBase = velocidadBase();
		int averiados = 0;
		MultiTreeMap<Integer, Vehicle> actualizados = new MultiTreeMap<Integer, Vehicle>(
				(a, b) -> b - a);

		for (Vehicle v : vehicles.innerValues()) {
			if (v.getLocation() < length) { // Si el vehiculo no esta en un cruze, actualizamos su
											// velocidad
				v.setVelocidadActual(velBase / factorReduccion(averiados));
			}
			v.avanza();
			if (v.averiado())
				averiados++;
			if (!v.arrived()) {
				actualizados.putValue(v.getLocation(), v);
			}
		}
		vehicles = actualizados;
	}

	int factorReduccion(int averiados) {
		if (averiados == 0)
			return 1;
		else
			return 2;
	}

	int velocidadBase() {
		return (int) Math.min(maxSpeed,
				maxSpeed / Math.max(vehicles.sizeOfValues(), 1) + 1);
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		StringBuilder state = new StringBuilder();
		for (Vehicle v : vehicles.innerValues()) {
			state.append("(" + v.getId() + "," + v.getLocation() + "),");
		}
		if (vehicles.size() > 0) {
			state.deleteCharAt(state.length() - 1);
		}
		out.put("state", state.toString());
	}

	@Override
	protected String getReportHeader() {
		return "road_report";
	}

	@Override
	public void describe(Map<String, String> out) {
		out.put("ID", id);
		out.put("Inicio", src.id);
		out.put("Final", dest.id);
		out.put("Longitud", Integer.toString(length));
		out.put("Maxima Velocidad", Integer.toString(maxSpeed));

		StringBuilder vehiclesOut = new StringBuilder();
		vehiclesOut.append("[");
		for (Vehicle v : vehicles.valuesList()) {
			vehiclesOut.append(v.id + ",");
		}
		if (vehicles.size() != 0) {
			vehiclesOut.deleteCharAt(vehiclesOut.length() - 1);
		}
		vehiclesOut.append("]");

		out.put("Vehiculos", vehiclesOut.toString());
	}
}
