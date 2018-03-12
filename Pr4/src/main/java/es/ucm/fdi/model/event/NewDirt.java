package es.ucm.fdi.model.event;

import es.ucm.fdi.ini.IniSection;

public class NewDirt extends NewRoad {

	public NewDirt(int time, String id, String src, String dest, int maxSpeed, int length) {
		super(time, id, src, dest, maxSpeed, length);
		// TODO Auto-generated constructor stub
	}

	public NewDirt() {
		// TODO Auto-generated constructor stub
	}
	
	public class Builder implements Event.Builder{
		@Override
		public Event parse(IniSection sec) {
			if(!sec.getTag().equals("new_road") || !"dirt".equals(sec.getValue("type"))) return null;
			return new NewDirt(parseInt(sec, "time", 0), sec.getValue("id"), sec.getValue("src"), sec.getValue("dest"), parseInt(sec, "max_speed", 0), parseInt(sec, "length", 0));
		}
	}

}
