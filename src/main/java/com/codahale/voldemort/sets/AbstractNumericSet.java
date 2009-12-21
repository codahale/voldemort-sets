package com.codahale.voldemort.sets;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import voldemort.store.Store;
import voldemort.store.views.AbstractViewTransformation;
import voldemort.store.views.View;
import voldemort.versioning.InconsistentDataException;
import voldemort.versioning.Versioned;

/**
 * An abstract base class for Voldemort {@link View} classes which store sets of
 * numeric types.
 * 
 * @author coda
 * @param <E> the numeric type stored in this set
 */
public abstract class AbstractNumericSet<E extends Number> extends AbstractViewTransformation<Object, String, byte[]> {
	public static final String ADD_COMMAND = "ADD ";
	public static final String REM_COMMAND = "REM ";
	private static final Pattern COMMA = Pattern.compile(",");
	
	/**
	 * Given a serialized set of numbers, deserialize it into a {@link Set}.
	 * 
	 * @param s a serialized set of numbers
	 * @return a deserialized set of numbers
	 */
	protected abstract Set<E> deserialize(byte[] s);
	
	/**
	 * Given a set of numbers, serialize it into an array of bytes.
	 * 
	 * @param numbers a set of numbers
	 * @return {@code numbers}, serialized as an array of bytes
	 */
	protected abstract byte[] serialize(Set<E> numbers);
	
	/**
	 * Given a string, parse it as a signed decimal.
	 * 
	 * @param s a signed decimal
	 * @return a numeric type
	 */
	protected abstract E parse(String s);
	
	@Override
	public String storeToView(Store<Object, byte[]> store, Object k, byte[] s) {
		if (s == null) {
			return null;
		}
		
		final StringBuilder b = new StringBuilder();
		final Set<E> numbers = deserialize(s);
		for (E n : numbers) {
			b.append(n).append(',');
		}
		b.delete(b.length() - 1, b.length());
		return b.toString();
	}

	@Override
	public byte[] viewToStore(Store<Object, byte[]> store, Object k, String v) {
		final List<Versioned<byte[]>> items = store.get(k);
		final Set<E> numbers = deserialize(getLatest(k, v, items));
		
		if (v.startsWith(ADD_COMMAND)) {
			final String[] strings = COMMA.split(v.substring(ADD_COMMAND.length()));
			for (String s : strings) {
				numbers.add(parse(s.trim()));
			}
		} else if (v.startsWith(REM_COMMAND)) {
			final String[] strings = COMMA.split(v.substring(REM_COMMAND.length()));
			for (String s : strings) {
				numbers.remove(parse(s.trim()));
			}
		} else {
			throw new UnsupportedOperationException("Unknown command: " + v);
		}
		
		return serialize(numbers);
	}

	private byte[] getLatest(Object k, String v, List<Versioned<byte[]>> items) {
		if(items.size() == 0) {
			return new byte[] {};
		} else if(items.size() == 1) {
			return items.get(0).getValue();
		} else {
			throw new InconsistentDataException(
				"Unresolved versions returned from put(" + k + ", " + v + ")",
				items
			);
		}
	}
}
