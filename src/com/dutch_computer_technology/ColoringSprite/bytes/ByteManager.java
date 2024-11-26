package com.dutch_computer_technology.ColoringSprite.bytes;

import com.dutch_computer_technology.ColoringSprite.exception.ByteException;

public class ByteManager {
	
	public class Bytes {
		
		public byte[] bytes;
		public Bytes() {
			this.bytes = new byte[0];
		};
		public Bytes(byte[] bytes) {
			this.bytes = bytes;
		};
		
	};
	
	public static byte[] append(byte[] bytes, int size) {
		
		byte[] newBytes = new byte[bytes.length+size];
		System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
		return newBytes;
		
	};
	
	public static byte[] append(byte[] bytes, byte byteAdd) {
		
		byte[] newBytes = new byte[bytes.length+1];
		System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
		newBytes[bytes.length] = byteAdd;
		return newBytes;
		
	};
	
	public static byte[] append(byte[] bytes, byte[] bytesAdd) {
		
		byte[] newBytes = new byte[bytes.length+bytesAdd.length];
		System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
		System.arraycopy(bytesAdd, 0, newBytes, bytes.length, bytesAdd.length);
		return newBytes;
		
	};
	
	public static long getLong(byte[] bytes, Offset offset) throws ByteException {
		
		return getLong(bytes, offset, 8);
		
	};
	
	public static long getLong(byte[] bytes, Offset offset, int size) throws ByteException {
		
		if (size < 1) throw new ByteException("Long has a min of 1 bytes");
		if (size > 8) throw new ByteException("Long has a max of 8 bytes");
		
		if (offset.value+size > bytes.length) throw new ByteException("Out-of-Range");
		
		long val = 0;
		for (int i = 0; i < size; i++) {
			val = (val << 8) | (bytes[offset.value+i] & 0xFF);
		};
		offset.value += size;
		return val;
		
	};
	
	public static int getInt(byte[] bytes, Offset offset) throws ByteException {
		
		return getInt(bytes, offset, 4);
		
	};
	
	public static int getInt(byte[] bytes, Offset offset, int size) throws ByteException {
		
		if (size < 1) throw new ByteException("Int has a min of 1 bytes");
		if (size > 4) throw new ByteException("Int has a max of 4 bytes");
		
		if (offset.value+size > bytes.length) throw new ByteException("Out-of-Range");
		
		int val = 0;
		for (int i = 0; i < size; i++) {
			val = (val << 8) | (bytes[offset.value+i] & 0xFF);
		};
		offset.value += size;
		return val;
		
	};
	
	public static byte getByte(byte[] bytes, Offset offset) throws ByteException {
		
		if (offset.value >= bytes.length) throw new ByteException("Out-of-Range");
		
		byte b = bytes[offset.value];
		offset.value += 1;
		return b;
		
	};
	
	public static byte[] getByte(byte[] bytes, Offset offset, int size) throws ByteException {
		
		if (offset.value+size > bytes.length) throw new ByteException("Out-of-Range");
		
		byte[] mBytes = new byte[size];
		for (int i = 0; i < size; i++) {
			mBytes[i] = bytes[offset.value+i];
		};
		offset.value += size;
		return mBytes;
		
	};
	
	public static byte intToByte(int num) {
		
		return (byte) (num & 0xFF);
		
	};
	
	public static byte[] intToByte(int num, int size) throws ByteException {
		
		if (size < 1) throw new ByteException("Int has a min of 1 bytes");
		if (size > 4) throw new ByteException("Int has a max of 4 bytes");
		
		byte[] bytes = new byte[size];
		for (int i = size-1; i > -1; i--) {
			bytes[i] = (byte) (num & 0xFF);
			num >>= 8;
		};
		return bytes;
		
	};
	
	public static byte longToByte(long num) {
		
		return (byte) (num & 0xFF);
		
	};
	
	public static byte[] longToByte(long num, int size) throws ByteException {
		
		if (size < 1) throw new ByteException("Long has a min of 1 bytes");
		if (size > 8) throw new ByteException("Long has a max of 8 bytes");
		
		byte[] bytes = new byte[size];
		for (int i = size-1; i > -1; i--) {
			bytes[i] = (byte) (num & 0xFF);
			num >>= 8;
		};
		return bytes;
		
	};
	
	public static byte[] trim(byte[] bytes) {
		
		return trim(bytes, false);
		
	};
	
	public static byte[] trim(byte[] bytes, boolean atEnd) {
		
		if (bytes.length == 0) return new byte[0];
		if (atEnd) {
			int i = bytes.length-1;
			for (; i > -1; i--) {
				if (bytes[i] != 0) break;
			};
			byte[] newBytes = new byte[i+1];
			System.arraycopy(bytes, 0, newBytes, 0, newBytes.length);
			return newBytes;
		} else {
			int i = 0;
			for (; i < bytes.length; i++) {
				if (bytes[i] != 0) break;
			};
			byte[] newBytes = new byte[bytes.length-i];
			System.arraycopy(bytes, i, newBytes, 0, newBytes.length);
			return newBytes;
		}
		
	};
	
};