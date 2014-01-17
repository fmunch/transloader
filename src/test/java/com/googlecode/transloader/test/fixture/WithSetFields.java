package com.googlecode.transloader.test.fixture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.googlecode.transloader.test.Triangulate;

// TODO find a way to clone HashSets and TreeSets without serialization so that non-Serializable Objects can be put into them!
/*
 * All elements must be Serializable only because the this$0 field for anonymous classes is final (and
 * pre-Java-5-JVMs prevent setting any final fields, including instance ones, even by reflection). This affects all
 * HashSets and TreeSets because their implementation in the Sun JRE is backed by a Map which has a keySet field
 * which is assigned an anonymous class.
 */
public class WithSetFields extends NonCommonJavaObject {

	private Set set = new TreeSet(new ToStringComparator());
	{
		set.add(Triangulate.anyInteger());
		set.add(Triangulate.anyString());
		set.add(Triangulate.anyInteger());
	}

	private Set customSet = new OrderedToStringHashSet();
	{
		customSet.add(Triangulate.anyString());
		customSet.add(Triangulate.anyInteger());
		customSet.add(Triangulate.anyString());
	}

	private Set empty = Collections.EMPTY_SET;
	private Set unmodifiable = Collections.unmodifiableSet(set);
	private Set synchronizedSet = Collections.synchronizedSet(set);
	private Set singelton = Collections.singleton(Triangulate.anyInteger());

	private static class ToStringComparator implements Comparator, Serializable {
		public int compare(Object o1, Object o2) {
			return o1.toString().compareTo(o2.toString());
		}
	}

	private static class OrderedToStringHashSet extends HashSet {
		public String toString() {
			List presentationList = new ArrayList(this);
			Collections.sort(presentationList, new ToStringComparator());
			return presentationList.toString();
		}
	}
}
