package es.ucm.fdi.model;

import java.util.*;

import es.ucm.fdi.exceptions.SimulationException;
import es.ucm.fdi.model.simulatedobject.*;

/**
 * RoadMap es un mapa en el que se almacenan todos las instancias de simObject del progrma, se
 * almacenan en distintas estructuras de datos, una lista para cada hijo directo de SimObject y un
 * mapa en el que se almacenan todos identificados por su id, se implementan distintos métodos para
 * añadir nuevos SimObject o para buscarlos en el RoadMap
 */

public class RoadMap {
	// búsqueda por ids, unicidad
	private Map<String, SimObject> simObjects;
	// listados reales
	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	// listados read-only, via Collections.unmodifiableList();
	private List<Junction> junctionsRO;
	private List<Road> roadsRO;
	private List<Vehicle> vehiclesRO;

	public RoadMap() {
		simObjects = new HashMap<String, SimObject>();
		junctions = new ArrayList<Junction>();
		roads = new ArrayList<Road>();
		vehicles = new ArrayList<Vehicle>();
		// listados read-only, via Collections.unmodifiableList();
		junctionsRO = Collections.unmodifiableList(junctions);
		roadsRO = Collections.unmodifiableList(roads);
		vehiclesRO = Collections.unmodifiableList(vehicles);

	}

	// búsqueda por ids, unicidad
	public SimObject getSimObject(String id) {
		if (simObjects.get(id) != null)
			return simObjects.get(id);
		else
			throw new SimulationException(
					"Se ha intentado llamar al objeto " + id + " inexistente.");
	}

	public Junction getJunction(String id) {
		if (simObjects.get(id) instanceof Junction)
			return (Junction) simObjects.get(id);
		else
			throw new SimulationException(
					"Se ha intentado llamar al cruze: " + id + " inexistente.");
	}

	public Road getRoad(String id) {
		if (simObjects.get(id) instanceof Road)
			return (Road) simObjects.get(id);
		else
			throw new SimulationException(
					"Se ha intentado llamar a la carretera: " + id
							+ " inexistente.");
	}

	public Vehicle getVehicle(String id) {
		if (simObjects.get(id) instanceof Vehicle)
			return (Vehicle) simObjects.get(id);
		else
			throw new SimulationException("Se ha intentado llamar al vehiculo: "
					+ id + " inexistente.");
	}

	// listado (sólo lectura)
	public List<Junction> getJunctions() {
		return junctionsRO;
	}

	public List<Road> getRoads() {
		return roadsRO;
	}

	public List<Vehicle> getVehicles() {
		return vehiclesRO;
	}

	// inserción de objetos (solo se deben llamar desde execute)
	public void addJunction(Junction j) {
		if (!simObjects.containsKey(j.getId())) {
			simObjects.put(j.getId(), j);
			junctions.add(j);
		} else {
			idDuplicado(j.getId());
		}
	}

	public void addRoad(Road r) {
		if (!simObjects.containsKey(r.getId())) {
			simObjects.put(r.getId(), r);
			roads.add(r);
		} else {
			idDuplicado(r.getId());
		}
	}

	public void addVehicle(Vehicle v) {
		if (!simObjects.containsKey(v.getId())) {
			simObjects.put(v.getId(), v);
			vehicles.add(v);
		} else {
			idDuplicado(v.getId());
		}
	}

	private void idDuplicado(String id) {
		throw new SimulationException("El id: " + id + " esta duplicado.");
	}
}
