package es.ucm.fdi.control.eventbuilder;

import es.ucm.fdi.ini.IniSection;

public interface Eventbuilder {
	public void parse();
	public boolean isValidId(String id);
	public void parseInt(IniSection sec, String key, int def);
	public void parseIdList(IniSection sec, String key);
}
