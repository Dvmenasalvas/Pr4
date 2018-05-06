package es.ucm.fdi.launcher;

import es.ucm.fdi.model.TrafficSimulator.SimulatorListener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;

public class BatchErrorListener implements SimulatorListener {

	@Override
	public void registered(UpdateEvent ue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset(UpdateEvent ue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newEvent(UpdateEvent ue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void advanced(UpdateEvent ue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(UpdateEvent ue, String error) {
		System.err.println(error);
	}

}
