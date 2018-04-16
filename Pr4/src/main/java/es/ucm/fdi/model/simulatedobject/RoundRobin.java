/*package es.ucm.fdi.model.simulatedobject;
//Lo dejamos para la siguiente entrega
public class RoundRobin extends Junction {
	int maxTimeSlice;
	int minTimeSlice;
	
	public RoundRobin(String id, int maxTimeSlice, 
	int minTimeSlice) {
		super(id);
		this.maxTimeSlice = maxTimeSlice;
		this.minTimeSlice = minTimeSlice;
	}
	
	@Override
	public void avanza() {
		if(verde != 1) {
			if(!semaforos.get(verde).intervaloConsumido()) {
				semaforos.get(verde).primeroActua();
			} else {
				
			}
			siguienteSemaforo();
		}
	}

	void updateTimeSlice() {
		if(time == usedTime) {
			timeSlice = Math.min(a, b)
		}
		time = 0;
		usedTime = 0;
	}
}*/
