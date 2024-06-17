package hr.fer.oprpp1.hw05.crypto;

/**
 * Class Util offers utility methods for conversion of byte array to hex-encoded string and vice versa.
 * @author anace
 *
 */
public class Util {
	/**
	 * Array of all hexadecimal digits
	 */
	private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	/**
	 * Converts byte array to hex-encoded string
	 * @param arr
	 * @return hex-encoded string representation of byte array
	 */
	public static String byteToHex(byte[] arr) {
		if(arr.length == 0)
			return "";
		
		String result = "";
		
		for(byte b : arr) {
			result += hexDigits[(b & 0xf0) >>> 4];
			result += hexDigits[b & 0x0f];
		}

		return result;
	}
	
	/**
	 * Converts hex-encoded string to byte array
	 * @param String keytext
	 * @return byte array representation of given string
	 * @throws IllegalArgumentException - if given hex-String is not in appropriate format
	 */
	public static byte[] hexToByte(String keyText) {
		if(keyText.length() % 2 != 0) {
			throw new IllegalArgumentException("Hex-encoded string must have even number of characters.");
		}
		
		byte[] result = new byte[keyText.length() / 2];
		
		for(int i = 0; i < keyText.length(); i+=2) {
			int front = Character.digit(keyText.charAt(i), 16);
			int back = Character.digit(keyText.charAt(i+1), 16);
			
			if(front < 0 || back < 0) {
				throw new IllegalArgumentException("Detected a Non-hex character");
			}
			
			result[i / 2] = (byte) ((front << 4) | back);
		}
		return result;
	}
}
