package es.ucm.fdi.launcher;

import static org.junit.Assert.fail;

import org.junit.Test;

import es.ucm.fdi.exceptions.SimulationException;

public class MaintTest {

	final static String BASE = "src/main/resources/";
	
	public MaintTest() {
	}

	@Test
	public void testError() throws Exception{
		try {
			Main.test(BASE + "err");
			fail("No se ha lanzado la excepcion esperada.");
		} catch (SimulationException se) {
			//Este es el comportamiento esperado
		}
	}
	
	@Test
	public void testBasic() throws Exception{
		if(!Main.test(BASE + "basic")) {
			fail("No se han pasado todos los test de la parte basica.");
		}
	}
	
	@Test
	public void testAdvanced() throws Exception{
		if(!Main.test(BASE + "advanced")) {
			fail("No se han pasado todos los test de la parte avanzada.");
		}
	}
	
	
}
