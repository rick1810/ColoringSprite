package com.dutch_computer_technology.ColoringSprite.sprite.coder.c1;

import java.util.Map;

import com.dutch_computer_technology.ColoringSprite.bytes.Offset;
import com.dutch_computer_technology.ColoringSprite.exception.ColorException;
import com.dutch_computer_technology.ColoringSprite.exception.ExtensionParseException;
import com.dutch_computer_technology.ColoringSprite.exception.InvalidExtensionException;
import com.dutch_computer_technology.ColoringSprite.exception.InvalidHeaderException;
import com.dutch_computer_technology.ColoringSprite.exception.ParseException;
import com.dutch_computer_technology.ColoringSprite.sprite.ColoringSprite;
import com.dutch_computer_technology.ColoringSprite.sprite.ColoringSpriteHeader;
import com.dutch_computer_technology.ColoringSprite.sprite.ColoringSpriteOptions;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.Extension;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.OrderingExtension;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.ExtensionManager.ExtensionName;

public class C1 {
	
	public static void parse(ColoringSprite sprite, byte[] bytes, ColoringSpriteOptions options) throws ParseException, InvalidHeaderException, InvalidExtensionException, ExtensionParseException {
		
		Offset offset = new Offset();
		ColoringSpriteHeader header = new CSH1(bytes, offset, options);
		
		sprite.setOptions(options == null ? new ColoringSpriteOptions() : options);
		sprite.setSize(header.getWidth(), header.getHeight());
		sprite.setExtensions(header.getExtensions());
		
		int dataLength = bytes.length - offset.value;
		byte[] dataBytes = new byte[dataLength];
		System.arraycopy(bytes, offset.value, dataBytes, 0, dataLength);
		
		int[] extensionDone = new int[0];
		if (sprite.hasExtension(ExtensionName.Ordering)) {
			OrderingExtension ordering = (OrderingExtension) sprite.getExtension(ExtensionName.Ordering);
			if (ordering != null) {
				int[] order = ordering.getOrder();
				extensionDone = new int[order.length];
				int d = 0;
				for (int key : order) {
					extensionDone[d] = key;
					d++;
					if (sprite.hasExtension(key)) {
						Extension extension = sprite.getExtension(key);
						if (extension == null) continue;
						dataBytes = extension.decode(dataBytes, header.getVersion());
					};
				};
			};
		};
		
		extensionsLoop: for (Extension extension : sprite.getExtensions()) {
			for (int key : extensionDone) {
				if (extension.getKey() == key) continue extensionsLoop;
			};
			dataBytes = extension.decode(dataBytes, header.getVersion());
		};
		
		C1.decode(sprite, dataBytes, header);
		
	};
	
	public static void decode(ColoringSprite sprite, byte[] bytes, ColoringSpriteHeader header) throws ParseException {
		
		int aspect = (header.getWidth() * header.getHeight());
		int bitsPerColor = header.getBitsPerColor();
		try {
			sprite.getOptions().setBitsPerColor(bitsPerColor);
		} catch (ColorException e) {
			throw new ParseException(e);
		};
		int size = bytes.length;
		
		int[] cols = new int[aspect*header.getSprites()];
		int len = cols.length;
		
		int lastByte = size-1;
		int byteIndex = 0;
		int bitsUsed = 0;
		for (int colorIndex = 0; colorIndex < len; colorIndex++) {
			
			for (int bitIndex = 0; bitIndex < bitsPerColor; bitIndex++) {
				
				if (byteIndex >= size) break;
				int myBitIndex = (7 - bitsUsed);
				if (byteIndex == lastByte) myBitIndex = ((len-colorIndex) * bitsPerColor)-1;
				
				cols[colorIndex] = (int) ((cols[colorIndex] << 1) | ((bytes[byteIndex] >> myBitIndex) & 0x01));
				
				bitsUsed++;
				if (bitsUsed > 7) {
					bitsUsed = 0;
					byteIndex++;
					if (byteIndex >= size) break;
				};
				
			};
			
		};
		
		int sprites = header.getSprites();
		for (int spriteIndex = 0; spriteIndex < sprites; spriteIndex++) {
			
			int[] myCols = new int[aspect];
			System.arraycopy(cols, (aspect*spriteIndex), myCols, 0, aspect);
			try {
				sprite.addSprite(myCols);
			} catch (ColorException e) {
				throw new ParseException(e);
			};
			
		};
		
	};
	
	public static byte[] encode(ColoringSpriteHeader header, ColoringSpriteOptions options, Map<Integer, int[]> spriteMap) throws ColorException {
		
		int aspect = (header.getWidth() * header.getHeight());
		int bitsPerColor = options.getBitsPerColor();
		int size = (int) Math.ceil((aspect * header.getSprites() * bitsPerColor + 7) / 8);
		
		int[] cols = new int[aspect*header.getSprites()];
		int sprites = header.getSprites();
		for (int spriteIndex = 0; spriteIndex < sprites; spriteIndex++) {
			
			int[] ints = spriteMap.get(spriteIndex);
			
			for (int i = 0; i < ints.length; i++) {
				int p = ints[i];
				if (p < 0) throw new ColorException("Colors provided out-of-range, <0");
				if (p > options.getColors()-1) {
					if (options.getUnsupportedColors()) {
						ints[i] = options.getDefaultColor();
					} else {
						throw new ColorException("Colors provided out-of-range, >" + (options.getColors()-1));
					};
				};
			};
			
			System.arraycopy(ints, 0, cols, (aspect*spriteIndex), aspect);
			
		};
		
		byte[] bytes = new byte[size];
		
		int len = cols.length;
		int byteIndex = 0;
		int bitsUsed = 0;
		for (int colorIndex = 0; colorIndex < len; colorIndex++) {
			
			int color = cols[colorIndex];
			for (int bitIndex = bitsPerColor-1; bitIndex > -1; bitIndex--) {
				
				if (byteIndex >= size) break;
				
				bytes[byteIndex] = (byte) ((bytes[byteIndex] << 1) | ((color >> bitIndex) & 0x01));
				
				bitsUsed++;
				if (bitsUsed > 7) {
					bitsUsed = 0;
					byteIndex++;
					if (byteIndex >= size) break;
				};
				
			};
			
		};
		
		return bytes;
		
	};
	
};