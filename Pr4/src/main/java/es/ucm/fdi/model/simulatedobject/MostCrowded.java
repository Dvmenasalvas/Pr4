package es.ucm.fdi.model.simulatedobject;

import java.util.Map;

import es.ucm.fdi.model.simulatedobject.RoundRobin.IncomingRoad;

public class MostCrowded extends RoundRobin {

	public MostCrowded(String id, int maxTimeSlice, int minTimeSlice) {
		super(id, maxTimeSlice, minTimeSlice);
	}

	@Override
	public void añadirCarreteraEntrante(Road r) {
		if (!carreterasEntrantes.containsKey(r)) {
			IncomingRoad ir = new IncomingRoad(r);
			carreterasEntrantes.put(r, ir);
			semaforos.add(ir);
		}
	}

	@Override
	public void avanza() {
		IncomingRoad actual = null;
		if (verde != -1) {
			actual = (IncomingRoad) semaforos.get(verde);
			if (!actual.timeSliceConsumed()) { // Si no se ha consumido el tiempo, deja que el
												// semaforo actue
				if (actual.primeroActua()) {
					actual.usedTime++;
				}
				actual.pastTime++;
			}
			if (actual.timeSliceConsumed()) { // Si se ha consumido, actualiza los intervalos de
												// tiempo y pasa al siguiente
				actual.cambiarSemaforo();
				siguienteSemaforo(actual);
				semaforos.get(verde).cambiarSemaforo();
				((IncomingRoad) semaforos.get(verde)).updateTimeSlice();
			}
		} else {
			siguienteSemaforo(actual); // Comprueba si hay algun semaforo para empezar y si es asi
										// lo pone en verde
			semaforos.get(verde).cambiarSemaforo();
			((IncomingRoad) semaforos.get(verde)).updateTimeSlice();
		}
	}

	protected void siguienteSemaforo(IncomingRoad actual) {
		if (semaforos.size() != 0) { // Elegimos el siguiente semaforo
			int max = -1;
			for (int i = 0; i < semaforos.size(); i++) {
				IncomingRoad ir = (IncomingRoad) semaforos.get(i);
				if (ir.cochesEsperando() > max && !ir.equals(actual)) {
					max = ir.cochesEsperando();
					verde = i;
				}
			}
		}

	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		super.fillReportDetails(out);
		out.put("type", "mc");
	}

	public class IncomingRoad extends RoundRobin.IncomingRoad {

		public IncomingRoad(Road road) {
			super(road);
			timeSlice = 0;
		}

		public int cochesEsperando() {
			return vehicles.size();
		}

		@Override
		public void updateTimeSlice() {
			timeSlice = Math.max(vehicles.size() / 2, 1);
			usedTime = 0;
			pastTime = 0;
		}

	}
}
