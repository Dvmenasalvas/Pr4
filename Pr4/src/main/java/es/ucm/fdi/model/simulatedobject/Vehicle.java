package es.ucm.fdi.model.simulatedobject;

public class Vehicle {
	private int  velMaxima, velActual;
	private Road road;
	private Road[] itinerario;
	private int posItinerario;
	private int localizacion;
	private int tiempoAveria;
	private boolean haLlegado;
	private String id;
	
	public Road getRoad() {
		return road;
	}
	
	public String getId() {
		return id;
	}
	
	public int getTiempoAveria() {
		return tiempoAveria;
	}
	
	public int getLocalizacion() {
		return localizacion;
	}
	
	public void setTiempoAveria(int t) {
		tiempoAveria += t;
	}
	
	public void setVelocidadActual(int v){
		if(v > velMaxima) velActual = velMaxima;
		else velActual = v;
	}
	
	public String generalInforme(){
		String informe = "[vehicle_report]\n";
		informe += "id = " + '\n';
		informe += "time = " + '\n';
		informe += "speed = " + velActual + '\n';
		informe += "kilometrage = " + '\n';
		informe += "faulty = " + tiempoAveria + '\n';
		//Comprobar si ha llegado
		informe += "location = (" + road + "," + localizacion + ")";
		return informe;
	}
	
	public void avanza(){
		if(tiempoAveria > 0) {
			tiempoAveria--;
		} else {
			if(localizacion < road.getLongitud()) {
				localizacion += velActual;
				if(localizacion > road.getLongitud()) {
					localizacion = road.getLongitud();
					//Entra cola cruce
				}
			}
		}
	}
	
	public void moverASiguienteCarretera () {
		posItinerario++;
		if(posItinerario < itinerario.length) {
			localizacion = 0;
			road = itinerario[posItinerario];
		} else {
			haLlegado = true;
		}
	}
}
