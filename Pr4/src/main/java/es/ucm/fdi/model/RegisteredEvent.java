package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import es.ucm.fdi.model.simulator.EventType;
import es.ucm.fdi.model.simulator.Listener;
import es.ucm.fdi.model.simulator.Listener.UpdateEvent;

public class RegisteredEvent {
	private List<Listener> listeners = new ArrayList<>();
	public void addSimulatorListener(Listener l) {
		listeners.add(l);
		EventType et = EventType.REGISTERED;
		UpdateEvent ue = new UpdateEvent(et);
		// evita pseudo-recursividad
		SwingUtilities.invokeLater(()->l.registered(ue));
		}
	
	public void removeListener(Listener l) {
	listeners.remove(l);
	}
	// uso interno, evita tener que escribir el mismo bucle muchas veces
	private void fireUpdateEvent(EventType type, String error) {
	// envia un evento apropiado a todos los listeners
	}
}
