package com.codahale.voldemort.sets.util;

/**
 * An {@link AbstractLongInputStream} which parses sequences of 64-bit integers
 * stored as 8-byte, big-endian blocks.
 * 
 * @author coda
 */
public class FLongInputStream extends AbstractLongInputStream {
	
	/**
	 * Creates a {@link FLongInputStream} to read an array of bytes.
	 * 
	 * @param buf the array of bytes
	 */
	public FLongInputStream(byte[] buf) {
		super(buf);
	}
	
	@Override
	public long readLong() {
		return	(((long)next() << 56) +
				((long)(next() & 255) << 48) +
				((long)(next() & 255) << 40) +
				((long)(next() & 255) << 32) +
				((long)(next() & 255) << 24) +
					  ((next() & 255) << 16) +
					  ((next() & 255) <<  8) +
					  ((next() & 255) <<  0));
	}
	
	private int next() {
		if (available() <= 0) {
			throw new IllegalArgumentException();
		}
		
		return read();
	}
}
