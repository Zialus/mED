import static org.junit.Assert.*;

import fcup.Buffer;
import fcup.Cursor;
import org.junit.Test;

public class BufferTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewEmptyBuffer() {

		Buffer b = new Buffer();
		assertEquals( 1, b.getNumLines());
		assertEquals( new Cursor(0,0) , b.getCursor());

	}

	@Test
	public void testSetCursorToValidPosition(){

		Buffer b = new Buffer("xalala");
		b.setCursor(new Cursor(0,3));
		assertEquals(new Cursor(0,3),b.getCursor());

	}

}