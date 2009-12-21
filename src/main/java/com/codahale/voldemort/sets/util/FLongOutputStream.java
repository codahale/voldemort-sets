package com.codahale.voldemort.sets.util;

/**
 * An {@link AbstractLongOutputStream} which writes sequences of 64-bit integers
 * stored as 8-byte, big-endian blocks.
 * 
 * @author coda
 */
public class FLongOutputStream extends AbstractLongOutputStream {
	private static final int LONG_SIZE_IN_BYTES = Long.SIZE / 8;
	
	/**
     * Creates a new {@link FLongOutputStream}, with a buffer capacity suitable
     * for storing an expected number of integers.
     * 
     * @param expectedCount the expected number of integers to be written
     */
	public FLongOutputStream(int expectedCount) {
		super(expectedCount * LONG_SIZE_IN_BYTES);
	}
	
	@Override
	public void writeLong(long v) {
		write((byte)(v >>> 56));
		write((byte)(v >>> 48));
		write((byte)(v >>> 40));
		write((byte)(v >>> 32));
		write((byte)(v >>> 24));
		write((byte)(v >>> 16));
		write((byte)(v >>>  8));
		write((byte)(v >>>  0));
	}
}
