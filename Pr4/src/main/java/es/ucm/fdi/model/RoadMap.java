package es.ucm.fdi.model;

import java.util.*;

import es.ucm.fdi.model.simulatedobject.*;

public class RoadMap {
	// búsqueda por ids, unicidad
	private Map<String, SimObject> simObjects;
	// listados reales
	private List<Junction> junctions = new ArrayList<Junction>();
	private List<Road> roads = new ArrayList<Road>();
	private List<Vehicle> vehicles = new ArrayList<Vehicle>();
	// listados read-only, via Collections.unmodifiableList();
	private List<Junction> junctionsRO;
	private List<Road> roadsRO;
	private List<Vehicle> vehiclesRO; 
	
	
	public RoadMap() {
		// TODO Auto-generated constructor stub
	}
	
	// búsqueda por ids, unicidad
	public SimObject getSimObject(String id) {
		return simObjects.get(id);
	}
	
	public Junction getJunction(String id){
		if(simObjects.get(id) instanceof Junction) return (Junction) simObjects.get(id);
		else return null;
	}
	
	public Road getRoad(String id){
		if(simObjects.get(id) instanceof Road) return (Road) simObjects.get(id);
		else return null;
	}
	
	public Vehicle getVehicle(String id){
		if(simObjects.get(id) instanceof Vehicle) return (Vehicle) simObjects.get(id);
		else return null;
	}
	
	// listado (sólo lectura)
	public List<Junction> getJunctions(){
		return junctionsRO;
	}
	
	public List<Road> getRoads(){
		return roadsRO;
	}
	
	public List<Vehicle> getVehicles(){
		return vehiclesRO;
	}
	
	// inserción de objetos (package-protected)
	void addJunction(Junction j) {
		if(!simObjects.containsKey(j.getId())) {
			simObjects.put(j.getId(), j);
			junctions.add(j);
			junctionsRO = Collections.unmodifiableList(junctions);
		}
	}
	
	void addRoad(Road r) {
		if(!simObjects.containsKey(r.getId())) {
			simObjects.put(r.getId(), r);
			roads.add(r);
			roadsRO = Collections.unmodifiableList(roads);
		}
	}
	
	void addVehicle(Vehicle v) {
		if(!simObjects.containsKey(v.getId())) {
			simObjects.put(v.getId(), v);
			vehicles.add(v);
			vehiclesRO = Collections.unmodifiableList(vehicles);
		}
	}
	
}
