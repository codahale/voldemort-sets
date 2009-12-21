package com.codahale.voldemort.sets;

import java.util.Comparator;

public class ReverseSortedDoubleSet extends SortedDoubleSet {
	private static final Comparator<Double> REVERSE = new Comparator<Double>() {
		@Override
		public int compare(Double o1, Double o2) {
			return o2.compareTo(o1);
		}
	};
	
	@Override
	protected Comparator<Double> getComparator() {
		return REVERSE;
	}
}
