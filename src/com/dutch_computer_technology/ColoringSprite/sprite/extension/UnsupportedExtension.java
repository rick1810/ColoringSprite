package com.dutch_computer_technology.ColoringSprite.sprite.extension;

public class UnsupportedExtension implements Extension {
	
	private byte[] bytes;
	private int key;
	
	public UnsupportedExtension(int key, byte[] bytes, int version) {
		this.key = key;
		this.bytes = bytes;
	};
	
	@Override
	public int getKey() {
		return this.key;
	};
	
	@Override
	public byte[] toByte(int version) {
		return this.bytes;
	};
	
};