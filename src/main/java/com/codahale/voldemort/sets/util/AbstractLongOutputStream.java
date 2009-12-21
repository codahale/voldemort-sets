package com.codahale.voldemort.sets.util;

import java.io.ByteArrayOutputStream;

/**
 * An abstract output stream for writing sequences of serialized 64-bit
 * integers.
 * 
 * @author coda
 */
public abstract class AbstractLongOutputStream extends ByteArrayOutputStream {
	public AbstractLongOutputStream(int size) {
		super(size);
	}
	
	/**
	 * Writes the specified 64-bit integer to the stream.
	 * 
	 * @param n the 64-bit integer
	 */
	public abstract void writeLong(long n);
}
