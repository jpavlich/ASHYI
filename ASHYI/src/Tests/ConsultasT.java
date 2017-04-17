package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Test.Prueba;


public class ConsultasT {

	private Prueba t;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String [] v = new String[2];
		v[0] = "3";
		v[1] = "Entornos digitales";
		t.main(v);
		Assert.assertTrue("Nop", t.getObjs().length > 0);
	}

}