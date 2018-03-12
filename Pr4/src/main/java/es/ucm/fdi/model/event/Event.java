package es.ucm.fdi.model.event;

import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;

public abstract class Event {
	int time;
	
	public int getTime() {
		return time;
	}
	
	public abstract void execute(RoadMap simObjects);
	
	public interface Builder {
		public Event parse(IniSection sec);
		
		public default boolean isValidId(String id) {
			return false;
		}
		
		public default int parseInt(IniSection sec, String key, int def) {
			if(sec.getValue(key) == null) {
				return def;
			} else {
				return Integer.parseInt(sec.getValue(key));
			}
		}
		
		public default double parseDouble(IniSection sec, String key, double def) {
			if(sec.getValue(key) == null) {
				return def;
			} else {
				return Double.parseDouble(sec.getValue(key));
			}
		}
		
		public default long parseLong(IniSection sec, String key, long def) {
			if(sec.getValue(key) == null) {
				return def;
			} else {
				return Long.parseLong(sec.getValue(key));
			}
		}
		
		public default List<String> parseIdList(IniSection sec, String key) {
			String ids[] = sec.getValue(key).split("[, ]+"); 
			List<String> listaIds = Arrays.asList(ids);
			return listaIds;
		}
	}
	
	
	
}
