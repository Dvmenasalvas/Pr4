package es.ucm.fdi.model.event;

import java.util.List;

import es.ucm.fdi.ini.IniSection;

public interface EventBuilder {
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
	
	public default List<String> parseIdList(IniSection sec, String key) {
		return null;
	}
}
