package clib.preference.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CPreferenceRepository {

	private Map<String, String> map = new LinkedHashMap<String, String>();

	public void put(String key, String value) {
		map.put(key, value);
	}

	public String get(String key) {
		return map.get(key);
	}

	protected Map<String, String> getMap() {
		return map;
	}

	public void clear() {
		map.clear();
	}

	public int size() {
		return map.size();
	}

	public List<String> keys() {
		return new ArrayList<String>(map.keySet());
	}

	public boolean exists(String key) {
		return map.containsKey(key);
	}

}
