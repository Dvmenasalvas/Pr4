package es.ucm.fdi.model;

/**
 * clase enumerada en la que se detallan los tipos de eventos
 * que nuestro único listener, MainWindow puede recibir(entender 
 * la diferencia entre estos eventos y los eventos que se pasan
 * al simulador)
 * */

public enum EventType {
	REGISTERED("Bienvenido al simulador!!"), 
	RESET("El simulador se ha reiniciado correctamente"), 
	NEW_EVENT("Ha habido un nuevo evento en el simulador"), 
	ADVANCED("El simulador ha avanzado correctamente"), 
	ERROR("ERROR, hemos detectado un error no deseado");
	
	private String text;

	EventType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
