package es.ucm.fdi.model.simulatedobject;

public class SimObject {
	private String id;
	
	public String getId() {
		return id;
	}
	
	public SimObject() {
		// TODO Auto-generated constructor stub
	}
	
	public void advance() {}
	
	public String report() {
		String rep = "";
		return rep;
	}
	
	public boolean equals(SimObject o) {
		return id == o.getId();
	}
}
