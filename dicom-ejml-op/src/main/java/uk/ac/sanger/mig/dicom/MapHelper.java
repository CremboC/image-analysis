package uk.ac.sanger.mig.dicom;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapHelper {
	/**
	 * Flips keys with values in a map
	 * @param map
	 * @return
	 */
	public static <K, V> Map<V, K> flipMap(Map<K, V> map) {
		Map<V, K> nMap = new HashMap<V, K>();
		
		for (Entry<K, V> entry : map.entrySet()) {
			nMap.put(entry.getValue(), entry.getKey());
		}
		
		return nMap;
	}
}
