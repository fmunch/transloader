package com.googlecode.transloader.test.function;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.DefaultTransloader;
import com.googlecode.transloader.Transloader;
import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.WithMapFields;
import com.googlecode.transloader.test.fixture.WithSetFields;

public class MinimalCloningTest extends CloningTestCase {
	public static Test suite() throws Exception {
		return new ActiveTestSuite(MinimalCloningTest.class);
	}

	public void testDoesNotCloneStrings() throws Exception {
		Object string = Triangulate.anyString();
		assertSame(string, getTransloader().wrap(string).cloneWith(IndependentClassLoader.getInstance()));
	}

	public void testClonesObjectsWithSetFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithSetFields());
	}

	public void testClonesObjectsWithMapFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithMapFields());
	}

	protected Transloader getTransloader() {
		return new DefaultTransloader(CloningStrategy.MINIMAL);
	}
}
