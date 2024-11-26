package com.dutch_computer_technology.ColoringSprite.sprite.coder.c1;

import com.dutch_computer_technology.ColoringSprite.bytes.ByteManager;
import com.dutch_computer_technology.ColoringSprite.bytes.Offset;
import com.dutch_computer_technology.ColoringSprite.exception.ByteException;
import com.dutch_computer_technology.ColoringSprite.exception.ColorException;
import com.dutch_computer_technology.ColoringSprite.exception.ExtensionParseException;
import com.dutch_computer_technology.ColoringSprite.exception.InvalidExtensionException;
import com.dutch_computer_technology.ColoringSprite.exception.InvalidHeaderException;
import com.dutch_computer_technology.ColoringSprite.sprite.ColoringSpriteHeader;
import com.dutch_computer_technology.ColoringSprite.sprite.ColoringSpriteOptions;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.Extension;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.ExtensionManager;

public class CSH1 extends ColoringSpriteHeader {
	
	public CSH1(int version, int width, int height, int sprites, ColoringSpriteOptions options) {
		
		this.setOptions(options == null ? new ColoringSpriteOptions() : options);
		this.setVersion(version);
		this.setBitsPerColor(options.getBitsPerColor());
		this.setWidth(width);
		this.setHeight(height);
		this.setSprites(sprites);
		
	};
	
	public CSH1(int version, int width, int height, int sprites, Extension[] extensions, ColoringSpriteOptions options) {
		
		this.setOptions(options == null ? new ColoringSpriteOptions() : options);
		this.setVersion(version);
		this.setBitsPerColor(options.getBitsPerColor());
		this.setWidth(width);
		this.setHeight(height);
		this.setSprites(sprites);
		this.setExtensions(extensions);
		
	};
	
	public CSH1(byte[] bytes, Offset offset, ColoringSpriteOptions options) throws InvalidHeaderException, InvalidExtensionException, ExtensionParseException {
		
		this.setOptions(options == null ? new ColoringSpriteOptions() : options);
		
		try {
			this.setVersion(ByteManager.getInt(bytes, offset, 1));
		} catch (ByteException ignore) {
			throw new InvalidHeaderException("Cannot find Version"); 
		};
		
		this.getOptions().setVersion(this.getVersion());
		
		try {
			this.setBitsPerColor(ByteManager.getInt(bytes, offset, 1));
		} catch (ByteException ignore) {
			throw new InvalidHeaderException("Cannot find BitsPerColor"); 
		};
		
		try {
			this.getOptions().setBitsPerColor(this.getBitsPerColor());
		} catch (ColorException ignore) {};
		
		try {
			int widthLength = ByteManager.getInt(bytes, offset, 1);
			if (widthLength < 1 || widthLength > 4) {
				this.setWidth(0);
			} else {
				this.setWidth(ByteManager.getInt(bytes, offset, widthLength));
			};
		} catch (ByteException ignore) {
			throw new InvalidHeaderException("Cannot find Width"); 
		};
		
		try {
			int heightLength = ByteManager.getInt(bytes, offset, 1);
			if (heightLength < 1 || heightLength > 4) {
				this.setHeight(0);
			} else {
				this.setHeight(ByteManager.getInt(bytes, offset, heightLength));
			};
		} catch (ByteException ignore) {
			throw new InvalidHeaderException("Cannot find Height"); 
		};
		
		try {
			int spriteLength = ByteManager.getInt(bytes, offset, 1);
			if (spriteLength < 1 || spriteLength > 4) {
				this.setSprites(0);
			} else {
				this.setSprites(ByteManager.getInt(bytes, offset, spriteLength));
			};
		} catch (ByteException ignore) {
			throw new InvalidHeaderException("Cannot find Sprites"); 
		};
		
		int extensionsNum = 0;
		try {
			extensionsNum = ByteManager.getInt(bytes, offset, 1);
		} catch (ByteException ignore) {
			throw new InvalidHeaderException("Cannot get number of Extensions"); 
		};
		
		if (extensionsNum == 0) return;
		
		for (int ext = 0; ext < extensionsNum; ext++) {
			
			int key = -1;
			try {
				key = ByteManager.getInt(bytes, offset, 2);
			} catch (ByteException ignore) {
				throw new InvalidExtensionException("Invalid Extension Key"); 
			};
			
			int size = 0;
			try {
				int len = ByteManager.getInt(bytes, offset, 1);
				if (len < 1 || len > 4) {
					size = 0;
				} else {
					size = ByteManager.getInt(bytes, offset, len);
				};
			} catch (ByteException ignore) {
				throw new ExtensionParseException("Could not get size of Extension [" + key + "]"); 
			};
			
			byte[] extBytes = new byte[0];
			if (size > 0) {
				try {
					extBytes = ByteManager.getByte(bytes, offset, size);
				} catch (ByteException ignore) {
					throw new ExtensionParseException("Size of Extension exceeds Size of bytes [" + key + "]"); 
				};
			};
			
			Extension extension = ExtensionManager.getExtension(key, extBytes, this.getOptions());
			this.addExtension(extension);
			
		};
		
	};
	
	@Override
	public byte[] toByte() throws ByteException {
		
		byte[] bytes = new byte[0];
		
		bytes = ByteManager.append(bytes, ByteManager.intToByte(this.getVersion())); //Version, 1byte
		
		bytes = ByteManager.append(bytes, ByteManager.intToByte(this.getBitsPerColor())); //Version, 1byte
		
		byte[] widthBytes = ByteManager.intToByte(this.getWidth(), 4); //Width, 4byte
		widthBytes = ByteManager.trim(widthBytes);
		byte widthLength = ByteManager.intToByte(widthBytes.length);
		bytes = ByteManager.append(bytes, widthLength);
		bytes = ByteManager.append(bytes, widthBytes);
		
		byte[] heightBytes = ByteManager.intToByte(this.getHeight(), 4); //Height, 4byte
		heightBytes = ByteManager.trim(heightBytes);
		byte heightLength = ByteManager.intToByte(heightBytes.length);
		bytes = ByteManager.append(bytes, heightLength);
		bytes = ByteManager.append(bytes, heightBytes);
		
		byte[] spriteBytes = ByteManager.intToByte(this.getSprites(), 4); //Num of Sprites, 4byte
		spriteBytes = ByteManager.trim(spriteBytes);
		byte spriteLength = ByteManager.intToByte(spriteBytes.length);
		bytes = ByteManager.append(bytes, spriteLength);
		bytes = ByteManager.append(bytes, spriteBytes);
		
		bytes = ByteManager.append(bytes, ByteManager.intToByte(extensions())); //Num of Extensions, 1byte
		
		for (Extension extension : this.getExtensions()) { //Extensions
			
			byte[] extBytes = new byte[0];
			extBytes = ByteManager.append(extBytes, ByteManager.intToByte(extension.getKey(), 2)); //Key of Extension, 2byte
			
			byte[] data = extension.toByte(this.getVersion());
			int extensionSize = data.length;
			if (extensionSize > Integer.MAX_VALUE) throw new ByteException("Extension extends max size");
			
			byte[] extLength = ByteManager.intToByte(extensionSize, 4); //Size of Extension, 4byte
			extLength = ByteManager.trim(extLength);
			byte extLengthL = ByteManager.intToByte(extLength.length);
			extBytes = ByteManager.append(extBytes, extLengthL);
			extBytes = ByteManager.append(extBytes, extLength);
			
			if (extensionSize > 0) extBytes = ByteManager.append(extBytes, data); //Extension data
			
			bytes = ByteManager.append(bytes, extBytes); //Add Extension data to header
			
		};
		
		return bytes;
		
	};
	
};