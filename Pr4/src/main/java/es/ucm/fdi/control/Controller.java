package es.ucm.fdi.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.TrafficSimulator;
import es.ucm.fdi.model.TrafficSimulator.SimulatorListener;
import es.ucm.fdi.model.event.*;

/**
 * Controller es una clase por encima del simulador que parsea
 * los eventos relacionados con nuevo vehiculos o carreteras y ejecuta 
 * el propio simulador, también se instancia una sola vez
 * */

public class Controller {
	private TrafficSimulator ts;

	public Controller(TrafficSimulator ts) {
		this.ts = ts;
	}

	private final static Event.Builder[] bs = new Event.Builder[] {
			new MakeVehicleFaulty().new Builder(),
			new NewRoundRobin().new Builder(),
			new NewMostCrowded().new Builder(), new NewJunction().new Builder(),
			new NewLanes().new Builder(), new NewDirt().new Builder(),
			new NewRoad().new Builder(), new NewCar().new Builder(),
			new NewBike().new Builder(), new NewVehicle().new Builder() };

	public static Event parseSec(IniSection sec) throws IOException {
		Event e = null;
		for (Event.Builder eb : bs) {
			if ((e = eb.parse(sec)) != null)
				break;
		}
		return e;
	}

	public void ejecuta(int pasos, OutputStream out) {
		try {
			ts.ejecuta(pasos, out);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insertarEventos(Ini ini) {
			for (IniSection sec : ini.getSections()) {
				Event e;
				try {
					e = Controller.parseSec(sec);
					ts.insertaEvento(e);
				} catch (IOException e1) {
					System.err.println("Error perseando el evento: \n" + sec);
					e1.printStackTrace();
				}
			}
	}

	public void insertarEventos(String eventos) {
		Ini ini;
		try {
			ini = new Ini(new ByteArrayInputStream(eventos.getBytes()));
			insertarEventos(ini);
		} catch (IOException e) {
			System.err.println("Error al convertir el fichero: " + eventos + " a .ini");
			e.printStackTrace();
		}
	}

	public void addSimulatorListener(SimulatorListener l) {
		ts.addSimulatorListener(l);
	}

	public void reset() {
		ts.reset();
	}

	public String generateReports() {
		return ts.stringReport();
	}
}
