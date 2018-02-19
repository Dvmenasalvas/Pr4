package es.ucm.fdi.model.SimulatedObjects;

public class Vehicle {
	private int  velMaxima, velActual;
	private Road carretera;
	private Junction[] itinerario;
	private int localizacion;
	private int tiempoAveria;
	private boolean haLlegado;
	
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
		informe += "location = (" + carretera + "," + localizacion + ")";
		return informe;
	}
	
	public void avanza(){
		if(tiempoAveria > 0) tiempoAveria++;
		else {
			//Comprobar si cruze y llamar moverASiguienteCarretera
			localizacion += velActual;
			if(localizacion > carretera.getLongitud()) {
				localizacion = carretera.getLongitud();
				//Entra cola cruce
			}
		}
	}
	
	private void moverASiguienteCarretera () {
		//Asigna nueva carretera
		localizacion = 0;
		//If ultima carretera
		haLlegado = true;
	}
}
