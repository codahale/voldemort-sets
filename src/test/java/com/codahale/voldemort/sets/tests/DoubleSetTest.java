package com.codahale.voldemort.sets.tests;


import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import voldemort.store.Store;
import voldemort.versioning.InconsistentDataException;
import voldemort.versioning.Versioned;

import com.codahale.voldemort.sets.DoubleSet;

@RunWith(Enclosed.class)
public class DoubleSetTest {
	private static abstract class Context {
		protected Store<Object, byte[]> store;
		protected DoubleSet view;
		
		@SuppressWarnings("unchecked")
		public void setup() throws Exception {
			this.store = mock(Store.class);
			this.view = new DoubleSet();
		}
	}
	
	public static class Adding_An_Element_To_A_Nonexistent_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
			
			final List<Versioned<byte[]>> results = new ArrayList<Versioned<byte[]>>();
			when(store.get(Mockito.anyString())).thenReturn(results);
		}
		
		@Test
		public void itReadsTheExistingDataFromTheStore() throws Exception {
			view.viewToStore(store, "key", "ADD 1");
			
			verify(store).get("key");
		}
		
		@Test
		public void itWritesANewSetToStore() throws Exception {
			assertThat(view.viewToStore(store, "key", "ADD 1")).isEqualTo(
				new byte[] { 63, -16, 0, 0, 0, 0, 0, 0 }
			);
		}
	}
	
	public static class Adding_Multiple_Elements_To_A_Nonexistent_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();

			final List<Versioned<byte[]>> results = new ArrayList<Versioned<byte[]>>();
			when(store.get(Mockito.anyString())).thenReturn(results);
		}

		@Test
		public void itReadsTheExistingDataFromTheStore() throws Exception {
			view.viewToStore(store, "key", "ADD 1, 2, 3");

			verify(store).get("key");
		}

		@Test
		public void itWritesANewSetToStore() throws Exception {
			assertThat(view.viewToStore(store, "key", "ADD 1, 2, 3")).isEqualTo(
				new byte[] {
					64, 0, 0, 0, 0, 0, 0, 0,
					64, 8, 0, 0, 0, 0, 0, 0,
					63, -16, 0, 0, 0, 0, 0, 0
				}
			);
		}
	}

	public static class Adding_An_Element_To_A_Existing_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();

			final List<Versioned<byte[]>> results = new ArrayList<Versioned<byte[]>>();
			results.add(Versioned.value(new byte[] {
				64, 0, 0, 0, 0, 0, 0, 0,
				64, 8, 0, 0, 0, 0, 0, 0,
				63, -16, 0, 0, 0, 0, 0, 0
			}));
			when(store.get(Mockito.anyString())).thenReturn(results);
		}

		@Test
		public void itReadsTheExistingDataFromTheStore() throws Exception {
			view.viewToStore(store, "key", "ADD 4");

			verify(store).get("key");
		}

		@Test
		public void itWritesANewSetToStore() throws Exception {
			assertThat(view.viewToStore(store, "key", "ADD 4")).isEqualTo(
				new byte[] {
					64, 8, 0, 0, 0, 0, 0, 0,
					64, 0, 0, 0, 0, 0, 0, 0,
					63, -16, 0, 0, 0, 0, 0, 0,
					64, 16, 0, 0, 0, 0, 0, 0
				}
			);
		}
	}

	public static class Adding_Multiple_Elements_To_A_Existing_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();

			final List<Versioned<byte[]>> results = new ArrayList<Versioned<byte[]>>();
			results.add(Versioned.value(new byte[] {
				64, 0, 0, 0, 0, 0, 0, 0,
				64, 8, 0, 0, 0, 0, 0, 0,
				63, -16, 0, 0, 0, 0, 0, 0
			}));
			when(store.get(Mockito.anyString())).thenReturn(results);
		}

		@Test
		public void itReadsTheExistingDataFromTheStore() throws Exception {
			view.viewToStore(store, "key", "ADD 3, 4, 5");

			verify(store).get("key");
		}

		@Test
		public void itWritesANewSetToStore() throws Exception {
			assertThat(view.viewToStore(store, "key", "ADD 3, 4, 5")).isEqualTo(
				new byte[] {
					64, 8, 0, 0, 0, 0, 0, 0,
					64, 0, 0, 0, 0, 0, 0, 0,
					63, -16, 0, 0, 0, 0, 0, 0,
					64, 16, 0, 0, 0, 0, 0, 0,
					64, 20, 0, 0, 0, 0, 0, 0
				}
			);
		}
	}

	public static class Removing_An_Element_From_A_Nonexistent_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();

			final List<Versioned<byte[]>> results = new ArrayList<Versioned<byte[]>>();
			when(store.get(Mockito.anyString())).thenReturn(results);
		}

		@Test
		public void itReadsTheExistingDataFromTheStore() throws Exception {
			view.viewToStore(store, "key", "REM 1");

			verify(store).get("key");
		}

		@Test
		public void itWritesANewSetToStore() throws Exception {
			assertThat(view.viewToStore(store, "key", "REM 1")).isEqualTo(
				new byte[] { }
			);
		}
	}

	public static class Removing_An_Element_From_A_Existing_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();

			final List<Versioned<byte[]>> results = new ArrayList<Versioned<byte[]>>();
			results.add(Versioned.value(new byte[] {
				64, 0, 0, 0, 0, 0, 0, 0,
				64, 8, 0, 0, 0, 0, 0, 0,
				63, -16, 0, 0, 0, 0, 0, 0
			}));
			when(store.get(Mockito.anyString())).thenReturn(results);
		}

		@Test
		public void itReadsTheExistingDataFromTheStore() throws Exception {
			view.viewToStore(store, "key", "REM 3");

			verify(store).get("key");
		}

		@Test
		public void itWritesANewSetToStore() throws Exception {
			assertThat(view.viewToStore(store, "key", "REM 3")).isEqualTo(
				new byte[] {
					64, 0, 0, 0, 0, 0, 0, 0,
					63, -16, 0, 0, 0, 0, 0, 0
				}
			);
		}
	}

	public static class Getting_A_Nonexistent_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}

		@Test
		public void itConvertsTheExistingDataToACommaSeparatedList() throws Exception {
			assertThat(view.storeToView(store, "key", null)).isNull();
		}
	}

	public static class Getting_A_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}

		@Test
		public void itConvertsTheExistingDataToACommaSeparatedList() throws Exception {
			assertThat(view.storeToView(store, "key", new byte[] {
				64, 0, 0, 0, 0, 0, 0, 0,
				64, 8, 0, 0, 0, 0, 0, 0,
				63, -16, 0, 0, 0, 0, 0, 0
			})).isEqualTo("3.0,2.0,1.0");
		}
	}

	public static class Modifying_An_Inconsistent_Set extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();

			final List<Versioned<byte[]>> results = new ArrayList<Versioned<byte[]>>();
			results.add(Versioned.value(new byte[] { 2, 4, 6 }));
			results.add(Versioned.value(new byte[] { 2, 4, 6 }));
			when(store.get(Mockito.anyString())).thenReturn(results);
		}

		@Test
		public void itReadsTheExistingDataFromTheStore() throws Exception {
			try {
				view.viewToStore(store, "key", "REM 3");
				fail("should have thrown an InconsistentDataException but didn't");
			} catch (InconsistentDataException e) {
				assertThat(e.getMessage()).isEqualTo("Unresolved versions returned from put(key, REM 3)");
			}
		}
	}
}
