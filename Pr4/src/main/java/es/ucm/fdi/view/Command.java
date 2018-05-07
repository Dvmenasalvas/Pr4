package es.ucm.fdi.view;

/**
 * Clase enumerada con los comandos correspondinetes a cada botón de la toolbar de la ventana
 * principal
 */

public enum Command {
	LoadEvents("Cargar Eventos"), SaveEvents("Guardar Eventos"), CleanEvents(
			"Limpiar Eventos"), InsertEvents("Insertar Eventos"), Execute(
					"Ejecutar"), Reset("Reiniciar"), GenerateReports(
							"Generar Informes"), CleanReports(
									"Limpiar Informes"), SaveReports(
											"Guardar Informes"), Exit("Salir"),
	Delay("Parar");

	private String text;

	Command(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
