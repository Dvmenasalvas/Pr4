package es.ucm.fdi.model.simulatedobject;

import java.util.Map;



public class RoundRobin extends Junction {
	int maxTimeSlice;
	int minTimeSlice;
	
	public RoundRobin(String id, int maxTimeSlice, int minTimeSlice) {
		super(id);
		this.maxTimeSlice = maxTimeSlice;
		this.minTimeSlice = minTimeSlice;
	}
	
	@Override
	public void añadirCarreteraEntrante(Road r) {
		if(!carreterasEntrantes.containsKey(r)) {
			IncomingRoad ir = new IncomingRoad(r);
			carreterasEntrantes.put(r, ir);
			semaforos.add(ir);
		}
	}
	
	@Override
	public void avanza() {
		if(verde != -1) {
			IncomingRoad actual = (IncomingRoad) semaforos.get(verde);
			if(!actual.timeSliceConsumed()) {	//Si no se ha consumido el tiempo, deja que el semaforo actue
				if(actual.primeroActua()) {
					actual.usedTime++;
				}
				actual.pastTime++;
			}
			if(actual.timeSliceConsumed()){	//Si se ha consumido, actualiza los intervalos de tiempo y pasa al siguiente
				actual.cambiarSemaforo();
				actual.updateTimeSlice();
				siguienteSemaforo();
				semaforos.get(verde).cambiarSemaforo();
			}
		} else {
			siguienteSemaforo();	//Comprueba si hay algun semaforo para empezar
			semaforos.get(verde).cambiarSemaforo();
		}
	}
	
	
	@Override
	protected void fillReportDetails(Map<String, String> out) {
		super.fillReportDetails(out);
		out.put("type", "rr");
	}
	
	@Override
	protected String semaforo(Junction.IncomingRoad ir) {
		if(ir.semaforo) {
			return "green:" + (((IncomingRoad) ir).timeSlice - ((IncomingRoad) ir).pastTime);
		} else {
			return "red";
		}
	}
	
	public class IncomingRoad extends Junction.IncomingRoad{
		int timeSlice;
		int usedTime;
		int pastTime;
		
		public IncomingRoad(Road road) {
			super(road);
			timeSlice = maxTimeSlice;
			pastTime = 0;
			usedTime = 0;
		}

		public boolean timeSliceConsumed() {
			return pastTime >= timeSlice;
		}
		
		public void updateTimeSlice() {
			if(usedTime == pastTime) {
				timeSlice = Math.min(timeSlice + 1, maxTimeSlice);
			} else if(usedTime == 0) {
				timeSlice = Math.max(timeSlice - 1, minTimeSlice);
			}
			usedTime = 0;
			pastTime = 0;
		}
	}
}