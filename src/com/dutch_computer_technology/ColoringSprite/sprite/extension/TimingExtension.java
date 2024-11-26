package com.dutch_computer_technology.ColoringSprite.sprite.extension;

import com.dutch_computer_technology.ColoringSprite.bytes.ByteManager;
import com.dutch_computer_technology.ColoringSprite.bytes.Offset;
import com.dutch_computer_technology.ColoringSprite.exception.ByteException;
import com.dutch_computer_technology.ColoringSprite.exception.ExtensionParseException;

public class TimingExtension implements Extension {
	
	@Override
	public int getKey() {
		return 1;
	};
	
	private long ms;
	public TimingExtension(long ms) {
		
		this.ms = ms;
		
	};
	
	public TimingExtension(byte[] bytes, int version) throws ExtensionParseException {
		
		Offset offset = new Offset();
		try {
			int msLength = ByteManager.getInt(bytes, offset, 1);
			if (msLength < 1 || msLength > 8) {
				this.ms = 0;
			} else {
				this.ms = ByteManager.getLong(bytes, offset, msLength);
			};
		} catch (ByteException ignore) {
			throw new ExtensionParseException("Cannot find ms");
		};
		
	};
	
	@Override
	public byte[] toByte(int version) {
		
		byte[] bytes = new byte[0];
		byte[] msBytes = new byte[0];
		try {
			msBytes = ByteManager.longToByte(this.ms, 8);
		} catch (ByteException ignore) {}; //Only called if longToByte size is <1 or >8
		msBytes = ByteManager.trim(msBytes);
		byte msLength = ByteManager.intToByte(msBytes.length);
		bytes = ByteManager.append(bytes, msLength);
		bytes = ByteManager.append(bytes, msBytes);
		return bytes;
		
	};
	
	public void setMiliseconds(long ms) {
		this.ms = ms;
	};
	
	public long getMiliseconds() {
		return this.ms;
	};
	
};