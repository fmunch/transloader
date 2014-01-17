package com.googlecode.transloader.clone.reflect;

import java.util.Map;

import org.apache.commons.collections.map.IdentityMap;

import com.googlecode.transloader.Assert;

/**
 * A utility for traversing through entire object graphs which may contain cyclic references, without thereby entering
 * an endless loop. The implementation is thread-safe.
 * 
 * @author Jeremy Wales
 */
public final class CyclicReferenceSafeTraverser {
	private final ThreadLocal referenceHistoryForThread = new ThreadLocal();

	/**
	 * Executes the given the traversal over the current location in the object graph if it has not already been
	 * traversed in the current journey through the graph.
	 * 
	 * @param traversal the traversal to execute
	 * @param currentObjectInGraph the location in the object graph over which to perform the traversal
	 * @return the result of performing the traversal
	 * @throws Exception can throw any <code>Exception</code> from the traversal itself
	 */
	public Object performWithoutFollowingCircles(Traversal traversal, Object currentObjectInGraph) throws Exception {
		Assert.areNotNull(traversal, currentObjectInGraph);
		Map referenceHistory = getReferenceHistory();
		if (referenceHistory.containsKey(currentObjectInGraph)) return referenceHistory.get(currentObjectInGraph);
		referenceHistory.put(currentObjectInGraph, null);
		Object result = traversal.traverse(currentObjectInGraph, referenceHistory);
		// TODO make it so removals can only happen here
		// TODO test drive putting the removal in a finally block to prevent memory leaks following Exceptions
		referenceHistory.remove(currentObjectInGraph);
		return result;
	}

	private Map getReferenceHistory() {
		Map referenceHistory = (Map) referenceHistoryForThread.get();
		if (referenceHistory == null) {
			referenceHistoryForThread.set(referenceHistory = new IdentityMap());
		}
		return referenceHistory;
	}

	/**
	 * The callback interface for implementing a traversal over an object graph.
	 * 
	 * @author Jeremy Wales
	 */
	public static interface Traversal {
		/**
		 * Performs some logic on the given current location in the object graph. May update the given reference history
		 * to associate the given current object with the result of traversing it so that the result can be reused next
		 * time the same object is encountered in the same journey over the graph.
		 * 
		 * @param currentObjectInGraph the location in the object graph over which to perform the traversal
		 * @param referenceHistory the history of objects already traversed and the results of traversing them
		 * @return the result of traversing <code>currentObjectInGraph</code>
		 * @throws Exception can throw any <code>Exception</code> depending on the implementation
		 */
		Object traverse(Object currentObjectInGraph, Map referenceHistory) throws Exception;
	}
}
