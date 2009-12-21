package com.codahale.voldemort.sets;

import java.util.HashSet;
import java.util.Set;

import com.codahale.voldemort.sets.util.VLongInputStream;
import com.codahale.voldemort.sets.util.VLongOutputStream;

public class LongSet extends AbstractNumericSet<Long> {
	@Override
	protected Set<Long> deserialize(byte[] s) {
		final Set<Long> numbers = new HashSet<Long>(s.length);
		final VLongInputStream input = new VLongInputStream(s);
		while (input.available() > 0) {
			numbers.add(Long.valueOf(input.readLong()));
		}
		return numbers;
	}

	@Override
	protected Long parse(String s) {
		return Long.valueOf(Long.parseLong(s));
	}

	@Override
	protected byte[] serialize(Set<Long> numbers) {
		final VLongOutputStream output = new VLongOutputStream(numbers.size());
		for (Long l : numbers) {
			output.writeLong(l.longValue());
		}
		return output.toByteArray();
	}

}
