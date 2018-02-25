package es.ucm.fdi.model.event;

import es.ucm.fdi.ini.IniSection;

public class NewJunction extends Event {
	private String id;

	public NewJunction(int time, String id) {
		this.time = time;
		this.id = id;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
	public class Builder implements EventBuilder{

		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("new_junction")) return null;
			return new NewJunction(parseInt(sec, "time", 0), sec.getValue("id"));
		}
	}

}
