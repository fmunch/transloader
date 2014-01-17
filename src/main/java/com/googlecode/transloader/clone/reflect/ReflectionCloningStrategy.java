package com.googlecode.transloader.clone.reflect;

import java.util.Map;

import com.googlecode.transloader.Assert;
import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.clone.reflect.CyclicReferenceSafeTraverser.Traversal;

/**
 * A <code>CloningStrategy</code> that uses Java Reflection as its mechanism. Can clone whole object graphs or just
 * necessary parts depending on how it is configured.
 * 
 * @author Jeremy Wales
 */
public final class ReflectionCloningStrategy implements CloningStrategy {
	private final CyclicReferenceSafeTraverser cyclicReferenceSafeTraverser = new CyclicReferenceSafeTraverser();

	private final CloningDecisionStrategy decider;
	private final InnerCloner arrayCloner;
	private final InnerCloner normalObjectCloner;
	private final CloningStrategy fallbackCloner;

	/**
	 * Contructs a new <code>ReflectionCloningStrategy</code> with its dependencies injected.
	 * 
	 * @param cloningDecisionStrategy the strategy by which the decision to clone or not to clone a particular given
	 *            object is made
	 * @param instantiator the strategy by which to use instantiate normal objects (as opposed to arrays, for which
	 *            standard reflection is always adequate)
	 * @param fallbackCloningStrategy the <code>CloningStrategy</code> to fall back to when <code>this</code>
	 *            strategy fails
	 */
	public ReflectionCloningStrategy(CloningDecisionStrategy cloningDecisionStrategy,
			InstantiationStrategy instantiator, CloningStrategy fallbackCloningStrategy) {
		Assert.areNotNull(cloningDecisionStrategy, instantiator, fallbackCloningStrategy);
		decider = cloningDecisionStrategy;
		arrayCloner = new InnerArrayCloner(this);
		normalObjectCloner = new InnerNormalObjectCloner(this, instantiator);
		fallbackCloner = fallbackCloningStrategy;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation uses {@link CyclicReferenceSafeTraverser} to sucessfully handle cyclic references in the
	 * given object graph.
	 * </p>
	 * 
	 * @return a completely or partially cloned object graph, depending on the <code>CloningDecisionStrategy</code>
	 *         injected in
	 *         {@link #ReflectionCloningStrategy(CloningDecisionStrategy, InstantiationStrategy, CloningStrategy)},
	 *         with potentially the <code>original</code> itself being the top-level object in the graph returned if
	 *         it was not cloned
	 */
	public Object cloneObjectUsingClassLoader(final Object original, final ClassLoader targetClassLoader)
			throws Exception {
		Assert.areNotNull(original, targetClassLoader);
		Traversal cloningTraversal = new Traversal() {
			public Object traverse(Object currentObject, Map referenceHistory) throws Exception {
				return ReflectionCloningStrategy.this.clone(currentObject, targetClassLoader, referenceHistory);
			}
		};
		return cyclicReferenceSafeTraverser.performWithoutFollowingCircles(cloningTraversal, original);
	}

	private Object clone(Object original, ClassLoader targetClassLoader, Map cloneHistory) throws Exception {
		if (original == null) return null;
		try {
			return performIntendedCloning(original, targetClassLoader, cloneHistory);
		} catch (Exception e) {
			return performFallbackCloning(original, targetClassLoader);
		}
	}

	private Object performIntendedCloning(Object original, ClassLoader targetClassLoader, Map cloneHistory)
			throws Exception {
		InnerCloner innerCloner = original.getClass().isArray() ? arrayCloner : normalObjectCloner;
		Object clone = original;
		if (decider.shouldCloneObjectItself(original, targetClassLoader))
			clone = innerCloner.instantiateClone(original, targetClassLoader);
		cloneHistory.put(original, clone);
		if (decider.shouldCloneObjectContent(original, targetClassLoader))
			innerCloner.cloneContent(original, clone, targetClassLoader);
		return clone;
	}

	private Object performFallbackCloning(Object original, ClassLoader targetClassLoader) throws Exception {
		return fallbackCloner.cloneObjectUsingClassLoader(original, targetClassLoader);
	}
}
