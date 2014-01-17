package com.googlecode.transloader.test.fixture;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.googlecode.transloader.test.Triangulate;

public class WithMapFields extends NonCommonJavaObject {
	private SortedMap map = new TreeMap();
	{
		map.put(Triangulate.anyInteger(), Triangulate.anyString());
		map.put(Triangulate.anyInteger(), new SerializableWithFinalFields(Triangulate.anyInteger()));
	}

	private Map unmodifiable = Collections.unmodifiableMap(map);
	private Map synchronizedMap = Collections.synchronizedSortedMap(map);
	private Map singleton = Collections.singletonMap(Triangulate.anyInteger(), Triangulate.anyString());
}
