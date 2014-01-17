package com.googlecode.transloader.test.function;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.InvocationDescription;
import com.googlecode.transloader.ObjectWrapper;
import com.googlecode.transloader.TransloaderException;
import com.googlecode.transloader.Transloader;
import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.test.BaseTestCase;
import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.NonCommonJavaObject;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;
import com.googlecode.transloader.test.fixture.NonCommonJavaTypeWithMethods;
import com.googlecode.transloader.test.fixture.WithMapFields;
import com.googlecode.transloader.test.fixture.WithMethods;
import com.googlecode.transloader.test.fixture.WithPrimitiveFields;
import com.googlecode.transloader.test.fixture.WithStringField;

public class GeneralTransloaderTest extends BaseTestCase {
	private Object foreignObject;
	private Object foreignObjectWithMethods;
	private Transloader transloader = Transloader.DEFAULT;
	private ClassLoader dummyClassLoader = (ClassLoader) Triangulate.anyInstanceOf(ClassLoader.class);

	public static Test suite() throws Exception {
		return new ActiveTestSuite(GeneralTransloaderTest.class);
	}

	protected void setUp() throws Exception {
		foreignObject = getNewInstanceFromOtherClassLoader(WithMapFields.class);
		foreignObjectWithMethods = getNewInstanceFromOtherClassLoader(WithMethods.class);
	}

	private static Object getNewInstanceFromOtherClassLoader(Class clazz) throws Exception {
		return IndependentClassLoader.getInstance().loadClass(clazz.getName()).newInstance();
	}

	public void testReportsIsNullWhenGivenNull() throws Exception {
		assertTrue(transloader.wrap(null).isNull());
	}

	public void testReportsIsNotNullWhenGivenNonNullObject() throws Exception {
		assertFalse(transloader.wrap(new Object()).isNull());
	}

	public void testReportsIsNotInstanceOfUnrelatedType() throws Exception {
		assertFalse(transloader.wrap(new Object()).isInstanceOf(NonCommonJavaType.class.getName()));
	}

	public void testReportsIsInstanceOfSameClass() throws Exception {
		assertTrue(transloader.wrap(foreignObject).isInstanceOf(foreignObject.getClass().getName()));
	}

	public void testReportsIsInstanceOfSuperClass() throws Exception {
		assertTrue(transloader.wrap(foreignObject).isInstanceOf(NonCommonJavaObject.class.getName()));
	}

	public void testReportsIsInstanceOfImplementedInterface() throws Exception {
		assertTrue(transloader.wrap(foreignObject).isInstanceOf(NonCommonJavaType.class.getName()));
	}

	public void testReturnsNullWhenAskedToCloneNull() throws Exception {
		assertNull(transloader.wrap((Object) null).cloneWith(dummyClassLoader));
	}

	public void testReturnsCloneReturnedFromGivenCloningStrategy() throws Exception {
		final Object expectedOriginal = new Object();
		final ClassLoader expectedClassloader = new ClassLoader() {
		};
		final Object expectedClone = new Object();
		CloningStrategy cloningStrategy = new CloningStrategy() {
			public Object cloneObjectUsingClassLoader(Object original, ClassLoader cloneClassLoader) throws Exception {
				assertSame(expectedOriginal, original);
				assertSame(expectedClassloader, cloneClassLoader);
				return expectedClone;
			}
		};
		assertSame(expectedClone, new ObjectWrapper(expectedOriginal, cloningStrategy).cloneWith(expectedClassloader));
	}

	public void testWrapsExceptionThrownByGivenCloningStrategy() throws Exception {
		final Object expectedOriginal = new Object();
		final Exception expectedException = new Exception(Triangulate.anyString());
		final CloningStrategy throwingCloningStrategy = new CloningStrategy() {
			public Object cloneObjectUsingClassLoader(Object original, ClassLoader cloneClassLoader) throws Exception {
				throw expectedException;
			}
		};
		Thrower thrower = new Thrower() {
			public void executeUntilThrow() throws Throwable {
				new ObjectWrapper(expectedOriginal, throwingCloningStrategy).cloneWith(dummyClassLoader);
			}
		};
		assertThrows(thrower,
				new TransloaderException("Unable to clone '" + expectedOriginal + "'.", expectedException));
	}

	public void testProvidesWrappedObjectOnRequest() throws Exception {
		final Object expected = new Object();
		assertSame(expected, transloader.wrap(expected).getUnwrappedSelf());
	}

	public void testPassesAndReturnsStringsToAndFromInvocations() throws Exception {
		ObjectWrapper objectWrapper = transloader.wrap(foreignObjectWithMethods);
		String expectedStringFieldValue = Triangulate.anyString();
		objectWrapper.invoke(new InvocationDescription("setStringField", expectedStringFieldValue));
		assertEquals(expectedStringFieldValue, objectWrapper.invoke(new InvocationDescription("getStringField")));
	}

	public void testClonesParametersOfNonCommonJavaTypesInInvocations() throws Exception {
		NonCommonJavaType first = new WithStringField(Triangulate.anyString());
		NonCommonJavaType second = new WithPrimitiveFields();
		String expected = new WithMethods().concatenate(first, second);
		Class[] paramTypes = {NonCommonJavaType.class, NonCommonJavaType.class};
		Object[] params = {first, second};
		String actual =
				(String) transloader.wrap(foreignObjectWithMethods).invoke(
						new InvocationDescription("concatenate", paramTypes, params));
		assertEqualExceptForClassLoader(expected, actual);
	}

	public void testCreatesAnImplementationOfAGivenInterfaceThatCallsThroughToTheWrappedObject() throws Exception {
		String expectedStringFieldValue = Triangulate.anyString();
		Transloader.DEFAULT.wrap(foreignObjectWithMethods).invoke(
				new InvocationDescription("setStringField", expectedStringFieldValue));
		NonCommonJavaTypeWithMethods withMethods =
				(NonCommonJavaTypeWithMethods) transloader.wrap(foreignObjectWithMethods).makeCastableTo(
						NonCommonJavaTypeWithMethods.class);
		assertEquals(expectedStringFieldValue, withMethods.getStringField());
	}
}
