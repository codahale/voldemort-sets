package com.codahale.voldemort.sets;

import java.util.HashSet;
import java.util.Set;

import com.codahale.voldemort.sets.util.FLongInputStream;
import com.codahale.voldemort.sets.util.FLongOutputStream;

public class DoubleSet extends AbstractNumericSet<Double> {

	@Override
	protected Set<Double> deserialize(byte[] s) {
		final Set<Double> numbers = new HashSet<Double>(s.length);
		final FLongInputStream input = new FLongInputStream(s);
		while (input.available() > 0) {
			numbers.add(Double.valueOf(Double.longBitsToDouble(input.readLong())));
		}
		return numbers;
	}

	@Override
	protected Double parse(String s) {
		return Double.valueOf(s);
	}

	@Override
	protected byte[] serialize(Set<Double> numbers) {
		final FLongOutputStream output = new FLongOutputStream(numbers.size());
		for (Double d : numbers) {
			output.writeLong(Double.doubleToRawLongBits(d.longValue()));
		}
		return output.toByteArray();
	}

}
