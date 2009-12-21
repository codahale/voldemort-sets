package com.codahale.voldemort.sets.util.tests;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.codahale.voldemort.sets.util.VLongInputStream;

@RunWith(Enclosed.class)
public class VLongInputStreamTest {
	public static class Deserializing_A_Sequence_Of_Longs {
		private final byte[] bytes = new byte[] {
			 0,
			-2, -1, -1, -1, -1, -1, -1, -1, -1, 1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, 1
		};
		
		@Test
		public void itDeserializesTheBlocksIntoLongs() throws Exception {
			final VLongInputStream input = new VLongInputStream(bytes);
			
			assertThat(input.readLong()).isEqualTo(0L);
			assertThat(input.readLong()).isEqualTo(Long.MAX_VALUE);
			assertThat(input.readLong()).isEqualTo(Long.MIN_VALUE);
		}
	}
	
	public static class Deserializing_A_Malformed_Sequence {
		private final byte[] bytes = new byte[] {
			-5,
			-2, -1, -1, -1, -1, -1, -1, -1, -1, 1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, 1
		};
		
		@Test
		public void itDeserializesTheBlocksIntoLongs() throws Exception {
			final VLongInputStream input = new VLongInputStream(bytes);
			
			try {
				input.readLong();
				input.readLong();
				input.readLong();
				fail("should have thrown an IllegalArgumentException but didn't");
			} catch (IllegalArgumentException e) {
				assertThat(e).isNotNull();
			}
		}
	}
}
