package es.ucm.fdi.view;

public enum Command {
	LoadEvents("Cargar Eventos"), SaveEvents("Guardar Eventos"), CleanEvents(
			"Limpiar Eventos"), InsertEvents("Insertar Eventos"), Execute(
					"Ejecutar"), Reset("Reiniciar"), GenerateReports(
							"Generar Informes"), CleanReports(
									"Limpiar Informes"), SaveReports(
											"Guardar Informes"), Exit("Salir");
	/**
	 * NewRRJunction("Nuevo cruce RR"), NewMCJunction("Nuevo Cruce MC"), NewJunction("Nuevo Cruce"),
	 * NewDirtRoad("Nueva Carretera sin asfaltar"), NewLanesRoad("Nueva Autopista"), NewRoad("Nueva
	 * Carretera"), NewBike("Nueva Bici"), NewCar("Nuevo Coche"), NewVehicle("Nuevo vehiculo"),
	 * NewVehicleFaulty("Nueva averia");
	 **/

	private String text;

	Command(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
