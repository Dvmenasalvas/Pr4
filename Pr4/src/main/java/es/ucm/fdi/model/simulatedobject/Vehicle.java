package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

public class Vehicle extends SimObject{
	private int  velMaxima, velActual;
	private Road road;
	private Road[] itinerario;
	private int posItinerario;
	private int localizacion;
	private int tiempoAveria;
	private boolean haLlegado;
	
	public String getRoadId() {
		return road.getId();
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
				if(localizacion > road.getLongitud()) {		//Entra al cruze
					localizacion = road.getLongitud();
					road.getJunction().entraVehiculo(this);
					road = null;
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

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getReportHeader() {
		// TODO Auto-generated method stub
		return null;
	}
}
