package com.codahale.voldemort.sets.util;

import java.io.ByteArrayInputStream;

/**
 * An abstract input stream for reading serialized sequences of 64-bit integers.
 * 
 * @author coda
 */
public abstract class AbstractLongInputStream extends ByteArrayInputStream {
	public AbstractLongInputStream(byte[] buf) {
		super(buf);
	}
	
	/**
	 * Reads the next long from the data.
	 * 
	 * @return the next long from the data
	 */
	public abstract long readLong();
}
