package es.ucm.fdi.model.event;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.view.SimulatorTablePanel;

/**
 * Un evento es un nuevo suceso que le llega al simulador en el turno time
 * un evento puede ser o bien un nuevo SimObject o bien un que un vehículo
 * se ha averiado, Event es una clase abstracta que implementa los métodos
 * comunes que las clases hijas necsitan para leer en formato INI los parámetros
 * de cada evento concreto, tales como leer enteros, leer id, etc
 * Además en cada clase que herede de Event debe haber implementado un método
 * execute que añada al RoadMap el nuevo objeto y un método checkParameters
 * que se asegure de que los parámetros necesarios para determinar el evento
 * estén completos y sean coherentes con las reglas del programa
 * */

public abstract class Event implements SimulatorTablePanel.Describable {
	int time;

	public int getTime() {
		return time;
	}

	public abstract void execute(RoadMap simObjects);

	protected abstract void checkParameters();

	public interface Builder {
		public Event parse(IniSection sec) throws IOException;

		public default String isValidId(String id) throws IllegalArgumentException {
			if (id.matches("[a-zA-Z0-9_]+"))
				return id;
			else
				throw new IllegalArgumentException("El id: " + id + " no es valido.");
		}

		public default int parseInt(IniSection sec, String key, int def) {
			if (sec.getValue(key) == null) {
				return def;
			} else {
				return Integer.parseInt(sec.getValue(key));
			}
		}

		public default double parseDouble(IniSection sec, String key, double def) {
			if (sec.getValue(key) == null) {
				return def;
			} else {
				return Double.parseDouble(sec.getValue(key));
			}
		}

		public default long parseLong(IniSection sec, String key, long def) {
			if (sec.getValue(key) == null) {
				return def;
			} else {
				return Long.parseLong(sec.getValue(key));
			}
		}

		public default List<String> parseIdList(IniSection sec, String key) {
			String ids[] = sec.getValue(key).split("[, ]+");
			List<String> listaIds = Arrays.asList(ids);
			return listaIds;
		}
	}

}
