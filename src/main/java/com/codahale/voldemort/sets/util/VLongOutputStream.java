package com.codahale.voldemort.sets.util;

/**
 * An {@link AbstractLongOutputStream} which writes sequences of 64-bit integers
 * stored as variable-length blocks.
 * <p>
 * Numbers are stored using Avro's variable-length zig-zag encoding. While edge
 * cases like {@link Long#MAX_VALUE} and {@link Long#MIN_VALUE} can take up to
 * 10 bytes to encode, most values can be stored in much less space.
 * <p>
 * This encoding scheme requires more CPU than fixed-length encoding, but
 * requires less I/O when serializing and deserializing.
 * 
 * @author coda
 * @see <a href="http://hadoop.apache.org/avro/docs/current/spec.html#binary_encode_primitive">Avro Specification</a>
 */
public class VLongOutputStream extends AbstractLongOutputStream {
	private static final int MAXIMUM_SIZE_IN_BYTES = (Long.SIZE / 8) + 2;
	
	/**
     * Creates a new {@link VLongOutputStream}, with a buffer capacity suitable
     * for storing an expected number of integers.
     * 
     * @param expectedCount the expected number of integers to be written
     */
	public VLongOutputStream(int expectedCount) {
		super(expectedCount * MAXIMUM_SIZE_IN_BYTES);
	}
	
	@Override
	public void writeLong(long n) {
		long l = n;
		l = (l << 1) ^ (l >> 63); // move sign to low-order bit
		while ((l & ~0x7F) != 0) {
			write((byte) ((l & 0x7f) | 0x80));
			l >>>= 7;
		}
		write((byte) l);
	}
}
