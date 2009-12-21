package com.codahale.voldemort.sets.util;

/**
 * An {@link AbstractLongInputStream} which parses sequences of 64-bit integers
 * stored as variable-length blocks.
 * 
 * @author coda
 */
public class VLongInputStream extends AbstractLongInputStream {
	/**
	 * Creates a {@link VLongInputStream} to read an array of bytes.
	 * 
	 * @param buf the array of bytes
	 */
	public VLongInputStream(byte[] buf) {
		super(buf);
	}
	
	@Override
	public long readLong() {
		long n = 0;
		for (int shift = 0;; shift += 7) {
			long b = read();
			if (b >= 0) {
				n |= (b & 0x7F) << shift;
				if ((b & 0x80) == 0) {
					break;
				}
			} else {
				throw new IllegalArgumentException();
			}
		}
		n = (n >>> 1) ^ -(n & 1); // back to two's-complement
		return n;
	}
}
