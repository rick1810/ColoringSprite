package com.dutch_computer_technology.ColoringSprite.sprite.extension;

import com.dutch_computer_technology.ColoringSprite.bytes.ByteManager;
import com.dutch_computer_technology.ColoringSprite.bytes.Offset;
import com.dutch_computer_technology.ColoringSprite.exception.ByteException;
import com.dutch_computer_technology.ColoringSprite.exception.ExtensionParseException;

public class ColorExtension implements Extension {
	
	private static final int maxColors = 256;
	
	public static class ColorRGBA {
		
		private int k;
		private int r;
		private int g;
		private int b;
		private int a;
		
		public ColorRGBA(int key, int r, int g, int b, int a) {
			
			this.k = key & 255;
			this.r = r & 255;
			this.g = g & 255;
			this.b = b & 255;
			this.a = a & 255;
			
		};
		
		public ColorRGBA(byte[] bytes) throws ByteException {
			
			Offset offset = new Offset();
			this.k = ByteManager.getInt(bytes, offset, 1);
			this.r = ByteManager.getInt(bytes, offset, 1);
			this.g = ByteManager.getInt(bytes, offset, 1);
			this.b = ByteManager.getInt(bytes, offset, 1);
			this.a = ByteManager.getInt(bytes, offset, 1);
			
		};
		
		public byte[] toByte() {
			
			byte[] bytes = new byte[0];
			try {
				bytes = ByteManager.append(bytes, ByteManager.intToByte(this.k, 1));
			} catch (ByteException ignore) {};
			bytes = ByteManager.append(bytes, ByteManager.intToByte(this.r));
			bytes = ByteManager.append(bytes, ByteManager.intToByte(this.g));
			bytes = ByteManager.append(bytes, ByteManager.intToByte(this.b));
			bytes = ByteManager.append(bytes, ByteManager.intToByte(this.a));
			return bytes;
			
		};
		
		public int getKey() {
			return this.k;
		};
		
		public int getRed() {
			return this.r;
		};
		public double getRedD() {
			return this.r / 255;
		};
		
		public int getGreen() {
			return this.g;
		};
		public double getGreenD() {
			return this.g / 255;
		};
		
		public int getBlue() {
			return this.b;
		};
		public double getBlueD() {
			return this.b / 255;
		};
		
		public int getAlpha() {
			return this.a;
		};
		public double getAlphaD() {
			return this.a / 255;
		};
		
	};
	
	@Override
	public int getKey() {
		return 0;
	};
	
	private ColorRGBA[] colors;
	public ColorExtension() {
		
		this.colors = new ColorRGBA[0];
		
	};
	
	public ColorExtension(ColorRGBA[] colors) {
		
		this.colors = colors;
		
	};
	
	public ColorExtension(byte[] bytes, int version) throws ExtensionParseException {
		
		Offset offset = new Offset();
		this.colors = new ColorRGBA[0];
		
		for (int i = 0; i < bytes.length; i+=5) {
			
			byte[] colBytes;
			try {
				colBytes = ByteManager.getByte(bytes, offset, 5);
			} catch (ByteException ignore) {
				throw new ExtensionParseException("Size of Color exceeds Size of Extension");
			};
			
			ColorRGBA color;
			try {
				color = new ColorRGBA(colBytes);
			} catch (ByteException ignore) {
				throw new ExtensionParseException("Could not parse Color");
			};
			
			addColor(color);
			
		};
		
	};
	
	@Override
	public byte[] toByte(int version) {
		
		byte[] bytes = new byte[0];
		for (ColorRGBA color : this.colors) {
			int i = bytes.length;
			bytes = ByteManager.append(bytes, 5);
			System.arraycopy(color.toByte(), 0, bytes, i, 5);
		};
		return bytes;
		
	};
	
	public int colors() {
		return this.colors.length;
	};
	
	public ColorRGBA[] getColors() {
		return this.colors;
	};
	
	public ColorRGBA getColor(long key) {
		for (ColorRGBA color : this.colors) {
			if (color.getKey() == key) {
				return color;
			};
		};
		return null;
	};
	
	public void addColor(ColorRGBA color) {
		if (color == null) return;
		remColor(color.getKey());
		int len = this.colors.length;
		if (len >= maxColors) return;
		ColorRGBA[] colors = new ColorRGBA[len+1];
		System.arraycopy(this.colors, 0, colors, 0, len);
		colors[len] = color;
		this.colors = colors;
	};
	
	public void remColor(ColorRGBA color) {
		if (color == null) return;
		remColor(color.getKey());
	};
	
	public void remColor(long key) {
		for (int i = 0; i < this.colors.length; i++) {
			ColorRGBA color = this.colors[i];
			if (color.getKey() == key) {
				int len = this.colors.length;
				if (len == 1) {
					this.colors = new ColorRGBA[0];
					break;
				};
				ColorRGBA[] colors = new ColorRGBA[len-1];
				System.arraycopy(this.colors, 0, colors, 0, i);
				System.arraycopy(this.colors, i+1, colors, i, len-i-1);
				this.colors = colors;
				break;
			};
		};
	};
	
};