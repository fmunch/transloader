package com.googlecode.transloader.test.function;

import java.util.Map;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.Transloader;
import com.googlecode.transloader.clone.reflect.CyclicReferenceSafeTraverser;
import com.googlecode.transloader.clone.reflect.FieldDescription;
import com.googlecode.transloader.clone.reflect.FieldReflector;
import com.googlecode.transloader.clone.reflect.CyclicReferenceSafeTraverser.Traversal;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;

// TODO minimal clones of Sets and Maps can be compared by Strings but maximal clones cannot without NullPointerExceptions, so find out why
public class MaximalCloningTest extends CloningTestCase {
	private static CyclicReferenceSafeTraverser CYCLIC_REFERENCE_TRAVERSER = new CyclicReferenceSafeTraverser();

	public static Test suite() throws Exception {
		return new ActiveTestSuite(MaximalCloningTest.class);
	}

	protected Object assertDeeplyClonedToOtherClassLoader(NonCommonJavaType original) throws Exception {
		Object clone = super.assertDeeplyClonedToOtherClassLoader(original);
		assertDeeplyNotTheSame(original, clone);
		return clone;
	}

	private void assertDeeplyNotTheSame(final Object original, final Object clone) throws Exception {
		// TODO there's a pattern here and elsewhere of safely traversing fields, so abstract it if possible
		Traversal notSameTraversal = new Traversal() {
			public Object traverse(Object currentObjectInGraph, Map referenceHistory) throws Exception {
				assertNotSame(original, clone);
				FieldReflector originalReflector = new FieldReflector(original);
				FieldReflector cloneReflector = new FieldReflector(clone);
				FieldDescription[] fieldDescriptions = originalReflector.getAllInstanceFieldDescriptions();
				for (int i = 0; i < fieldDescriptions.length; i++) {
					FieldDescription description = fieldDescriptions[i];
					if (!description.isPrimitive())
						assertDeeplyNotTheSame(originalReflector.getValue(description),
								cloneReflector.getValue(description));
				}
				return null;
			}
		};
		CYCLIC_REFERENCE_TRAVERSER.performWithoutFollowingCircles(notSameTraversal, original);
	}

	protected Transloader getTransloader() {
		return Transloader.DEFAULT;
	}
}
