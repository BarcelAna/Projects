package hr.fer.oprpp1.hw05.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilTests {

	@Test
	void hexToByteTest() {
		byte[] result = Util.hexToByte("01aE22");
		assertEquals(1, result[0]);
		assertEquals(-82, result[1]);
		assertEquals(34, result[2]);
	}
	
	@Test
	void hexToByteExceptionTest() {
		assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("01aE2"));
		assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("01ag22"));
	}
	
	@Test
	void byteToHex() {
		assertEquals("01ae22", Util.byteToHex(new byte[] {1, -82, 34}));
	}

}
