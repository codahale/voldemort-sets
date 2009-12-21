package com.codahale.voldemort.sets;

import java.util.Comparator;

public class ReverseSortedLongSet extends SortedLongSet {
	private static final Comparator<Long> REVERSE = new Comparator<Long>() {
		@Override
		public int compare(Long o1, Long o2) {
			return o2.compareTo(o1);
		}
	};
	
	@Override
	protected Comparator<Long> getComparator() {
		return REVERSE;
	}
}
