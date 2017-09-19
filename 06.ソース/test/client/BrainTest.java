package client;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import brain.Brain;

public class BrainTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		Brain bra = new Brain();


		String[][] loc = new String[3][3];

		loc[0][0] = "-";
		loc[1][0] = "-";
		loc[2][0] = "-";
		loc[0][1] = "-";
		loc[1][1] = "-";
		loc[2][1] = "-";
		loc[0][2] = "-";
		loc[1][2] = "-";
		loc[2][2] = "-";


		 assertEquals("00", bra.getLocation(loc));
	}

}
