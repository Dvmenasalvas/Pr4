package es.ucm.fdi.model.simulatedobject;

import java.util.List;
import java.util.Map;

import es.ucm.fdi.model.simulatedobject.Junction.IncomingRoad;

//Lo dejamos para la siguiente entrega

public class RoundRobin extends Junction {
	int maxTimeSlice;
	int minTimeSlice;
	List<IncomingRoad> semaforos;
	
	public RoundRobin(String id, int maxTimeSlice, int minTimeSlice) {
		super(id);
		this.maxTimeSlice = maxTimeSlice;
		this.minTimeSlice = minTimeSlice;
	}
	
	@Override
	public void avanza() {
		if(verde != -1) {
			if(!semaforos.get(verde).intervaloConsumido()) {
				semaforos.get(verde).primeroActua();
				semaforos.get(verde).usedTime++;
			} else {
				semaforos.get(verde).updateTimes();
				semaforos.get(verde).cambiarSemaforo();
				siguienteSemaforo();
				semaforos.get(verde).cambiarSemaforo();
			}
		} else {
			siguienteSemaforo();
		}
	}
	
	@Override
	protected void fillReportDetails(Map<String, String> out) {
		StringBuilder queues = new StringBuilder();
		String sem ;
		for(IncomingRoad ir : semaforos) {
			sem = ir.writeSem();
			
			queues.append("(" + ir.road.getId() + "," + sem + ",[");
			for(Vehicle v : ir.vehicles) {
				queues.append(v.getId() + ",");
			}
			if(ir.vehicles.size() > 0) {
				queues.deleteCharAt(queues.length() - 1);
			}
			queues.append("]),");
		}
		if(semaforos.size() != 0) {
			queues.deleteCharAt(queues.length() - 1);
		}
		
		out.put("queues", queues.toString());
	}
	
	
	public class IncomingRoad extends Junction.IncomingRoad{
		int timeSlice;
		int usedTime;
		int junctionedCars;
		
		public IncomingRoad(Road road) {
			super(road);
			timeSlice = maxTimeSlice;
			usedTime = 0;
			junctionedCars = 0;
		}
		
		@Override
		public String writeSem() {
			String sem;
			if(semaforo) {
				sem = "green:";
				sem += timeSlice - usedTime;
			} else {
				sem = "red";
			}
			return sem;
		}
		
		public void updateTimes() {
			if(junctionedCars == timeSlice) {		//Actualiza timeSlice
				timeSlice = Math.min(timeSlice + 1, maxTimeSlice);
			} else if (junctionedCars == 0) {
				timeSlice = Math.max(timeSlice - 1,  minTimeSlice);
			}
			
			usedTime = 0;
			junctionedCars = 0;
		}

		public boolean intervaloConsumido() {
			return usedTime == timeSlice;
		}
		
	}
}

