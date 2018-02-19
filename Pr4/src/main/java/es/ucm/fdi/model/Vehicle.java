package es.ucm.fdi.model;

public class Vehicle {
	private int  velMaxima, velActual;
	private Road carretera;
	private int localizacion;
	private int tiempoAveria;
	private boolean haLlegado;
	
	public void setTiempoAveria(int t) {
		tiempoAveria += t;
	}
	
	public void setVelocidadActual(int v){
		if(v > velMaxima) velActual = velMaxima;
		else velActual = v;
	}
	
	public String generalInforme(){
		String entrada = "[vehicle_report]\n";
		entrada += "id = " + '\n';
		entrada += "time = " + '\n';
		entrada += "speed = " + velActual + '\n';
		entrada += "kilometrage = " + '\n';
		entrada += "faulty = " + tiempoAveria + '\n';
		//Comprobar si ha llegado
		entrada += "location = (" + carretera + "," + localizacion + ")";
		return entrada;
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
