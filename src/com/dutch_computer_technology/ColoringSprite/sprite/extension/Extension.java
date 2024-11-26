package com.dutch_computer_technology.ColoringSprite.sprite.extension;

public interface Extension {
	
	/**
	 * Get key of {@code Extension},
	 * 0 - 65535
	 * 
	 * @return {@code int}
	 */
	public int getKey();
	
	/**
	 * Convert {@code Extension} to {@code byte[]} using {@code int} version
	 * 
	 * @param version {@code int}, Version of codec to expect
	 * @return {@code byte[]}
	 */
	public byte[] toByte(int version);
	
	/**
	 * Encode {@code byte[]) spriteData using {@code int} version
	 * 
	 * @param bytes {@code byte}, spriteData
	 * @param version {@code int}, Version of codec to expect
	 * @return {@code byte[]}, Encoded spriteData
	 */
	public default byte[] encode(byte[] bytes, int version) {
		return bytes;
	};
	
	/**
	 * Decode {@code byte[]) spriteData using {@code int} version
	 * 
	 * @param bytes {@code byte}, spriteData
	 * @param version {@code int}, Version of codec to expect
	 * @return {@code byte[]}, Decoded spriteData
	 */
	public default byte[] decode(byte[] bytes, int version) {
		return bytes;
	};
	
};