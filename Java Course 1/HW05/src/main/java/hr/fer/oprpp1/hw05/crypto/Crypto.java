package hr.fer.oprpp1.hw05.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static hr.fer.oprpp1.hw05.crypto.Util.*;

/**
 * Program that allows the user to encrypt/decrypt file using AES crypto-algorithm
 * or calculate and check the SHA-256 file digest
 * @param args
 */
public class Crypto {
	
	/**
	 * maximum content size that can be read from file or written to file
	 */
	private static final int MAX_INPUT_LENGTH = 4096;

	/**
	 * Calculates given file digest and writes the result in console
	 * @param fileToDigest
	 */
	private static void checksa(Path fileToDigest) {
		String expectedSha;
		
		System.out.println("Please provide expected sha-256 digest for " + fileToDigest.getFileName());
		System.out.print("> ");
		
		try(Scanner sc = new Scanner(System.in)) {
			expectedSha = sc.nextLine();
		}
		
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		try(InputStream is = Files.newInputStream(fileToDigest)) {
			int r;
			while(true) {
				byte[] byteArr = new byte[MAX_INPUT_LENGTH];
				r = is.read(byteArr);
				if(r < 1) break;
				sha.update(byteArr, 0, r);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] fileDigest = sha.digest();
		String resultSha = byteToHex(fileDigest);
		
		System.out.println("Digesting completed. Digest of " + fileToDigest.getFileName() + " "
				+ (expectedSha.equals(resultSha) ? "matches expected digest." : "does not match the expected digest. Digest was: " + resultSha)
		);
	}

	/**
	 * Applies AES algorithm on passed source file and writes the result do given destination file.
	 * If encrypt parameter is set to true that source file is encrypted, otherwise it is decrypted.
	 * @param sourceFile
	 * @param destinationFile
	 * @param encrypt
	 */
	private static void applyAES(Path sourceFile, Path destinationFile, boolean encrypt) {
		String keyText;
		String ivText;
		System.out.println("Please provide password as hex-encoded text (16-bytes, i.e. 32 hex-digits):");
		System.out.print("> ");
		try(Scanner sc = new Scanner(System.in)) {
			keyText = sc.nextLine();
			System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
			System.out.print("> ");
			ivText = sc.nextLine();
		}
		
		SecretKeySpec keySpec = new SecretKeySpec(hexToByte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(hexToByte(ivText));
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		try {
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		byte[] cipherText;
		try(InputStream is = Files.newInputStream(sourceFile); OutputStream os = Files.newOutputStream(destinationFile)) {
			int r;
			byte[] byteArr = new byte[MAX_INPUT_LENGTH];
			while((r = is.read(byteArr)) >= 1) {
				cipherText = cipher.update(byteArr, 0, r);
				os.write(cipherText, 0, cipherText.length);
			}
			cipherText = cipher.doFinal();
			os.write(cipherText, 0, cipherText.length);
			System.out.println(encrypt? "Encryption" : "Decryption" + " completed. Generated file " + destinationFile + " based on file " + sourceFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
			
	}
	
	public static void main(String[] args) {
		String functionName = args[0];
		
		if(functionName.equals("checksha")) {
			Path fileToDigest = Paths.get(args[1]);
			Crypto.checksa(fileToDigest);
		} else if(functionName.equals("encrypt")) {
			Path sourceFile = Paths.get(args[1]);
			Path destinationFile = Paths.get(args[2]);
			Crypto.applyAES(sourceFile, destinationFile, true);
		} else if(functionName.equals("decrypt")) {
			Path sourceFile = Paths.get(args[1]);
			Path destinationFile = Paths.get(args[2]);
			Crypto.applyAES(sourceFile, destinationFile, false);
		}
	}


	
}
