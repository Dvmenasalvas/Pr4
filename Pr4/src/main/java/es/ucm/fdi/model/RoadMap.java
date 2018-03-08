package es.ucm.fdi.model;

import java.util.*;

import es.ucm.fdi.model.simulatedobject.*;

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
	
	// inserción de objetos (solo se deben llamar desde execute)
	public void addJunction(Junction j) {
		if(!simObjects.containsKey(j.getId())) {
			simObjects.put(j.getId(), j);
			junctions.add(j);
		}
	}
	
	public void addRoad(Road r) {
		if(!simObjects.containsKey(r.getId())) {
			simObjects.put(r.getId(), r);
			roads.add(r);
			r.getSrc().añadirCarreteraSaliente(r,r.getDest());
			r.getDest().añadirCarreteraEntrante(r);
		}
	}
	
	public void addVehicle(Vehicle v) {
		if(!simObjects.containsKey(v.getId())) {
			simObjects.put(v.getId(), v);
			vehicles.add(v);
		}
	}
	//Metodo para averiar coches
	public void averiar(String id, int t) {
		if(getVehicle(id) != null) {
			getVehicle(id).setTiempoAveria(t);
		}
	}
	
	
}
