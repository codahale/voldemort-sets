package com.codahale.voldemort.sets.util.tests;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.codahale.voldemort.sets.util.VLongOutputStream;

@RunWith(Enclosed.class)
public class VLongOutputStreamTest {
	public static class Serializing_A_Sequence_Of_Longs {
		@Test
		public void itStoresLongsAsVariableLengthZigZagBlocks() throws Exception {
			final VLongOutputStream output = new VLongOutputStream(3);
			output.writeLong(0);
			output.writeLong(Long.MAX_VALUE);
			output.writeLong(Long.MIN_VALUE);
			
			assertThat(output.toByteArray()).isEqualTo(new byte[] {
				 0,
				-2, -1, -1, -1, -1, -1, -1, -1, -1, 1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, 1
			});
		}
	}
}
