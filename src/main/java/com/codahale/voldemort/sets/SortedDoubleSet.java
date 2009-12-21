package com.codahale.voldemort.sets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.codahale.voldemort.sets.util.FLongInputStream;

public class SortedDoubleSet extends DoubleSet {
	@Override
	protected Set<Double> deserialize(byte[] s) {
		final List<Double> intermediate = new ArrayList<Double>(s.length);
		final FLongInputStream input = new FLongInputStream(s);
		while (input.available() > 0) {
			intermediate.add(Double.valueOf(Double.longBitsToDouble(input.readLong())));
		}
		
		final Set<Double> numbers = new TreeSet<Double>(getComparator());
		numbers.addAll(intermediate);
		return numbers;
	}
	
	protected Comparator<Double> getComparator() {
		return null; // use natural ordering
	}
}
