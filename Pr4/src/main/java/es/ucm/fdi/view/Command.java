package es.ucm.fdi.view;

public enum Command {
	LoadEvents("Cargar Eventos"), 
	SaveEvents("Guardar Eventos"), 
	CleanEvents("Limpiar Eventos"), 
	InsertEvents("Insertar Eventos"), 
	Execute("Ejecutar"), 
	Reset("Reiniciar"), 
	GenerateReports("Generar Informes"), 
	CleanReports("Limpiar Informes"), 
	SaveReports("Guardar Informes"), 
	Exit("Salir");

	private String text;

	Command(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
