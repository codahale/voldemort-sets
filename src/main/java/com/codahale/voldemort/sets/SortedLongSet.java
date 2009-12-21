package com.codahale.voldemort.sets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.codahale.voldemort.sets.util.VLongInputStream;

public class SortedLongSet extends LongSet {
	@Override
	protected Set<Long> deserialize(byte[] s) {
		final List<Long> intermediate = new ArrayList<Long>(s.length);
		final VLongInputStream input = new VLongInputStream(s);
		while (input.available() > 0) {
			intermediate.add(Long.valueOf(input.readLong()));
		}
		
		final Set<Long> numbers = new TreeSet<Long>(getComparator());
		numbers.addAll(intermediate); 	// addAll() is O(n), looping over the values
										// and calling add() is O(n log n)
		return numbers;
	}
	
	protected Comparator<Long> getComparator() {
		return null; // use natural ordering
	}
}
