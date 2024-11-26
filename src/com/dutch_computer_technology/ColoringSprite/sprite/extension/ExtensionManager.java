package com.dutch_computer_technology.ColoringSprite.sprite.extension;

import com.dutch_computer_technology.ColoringSprite.exception.ExtensionParseException;
import com.dutch_computer_technology.ColoringSprite.exception.InvalidExtensionException;
import com.dutch_computer_technology.ColoringSprite.sprite.ColoringSpriteOptions;

public class ExtensionManager {
	
	public enum ExtensionName {
		
		Color(0),
		Timing(1),
		Information(2),
		Ordering(3);
		
		private int key;
		private ExtensionName(int key) {
			this.key = key;
		};
		public int getKey() {
			return this.key;
		};
		
	};
	
	public static Extension getExtension(int key, byte[] bytes, ColoringSpriteOptions options) throws InvalidExtensionException, ExtensionParseException {
		
		if (key < 0) throw new InvalidExtensionException("Invalid Extension Key");
		if (bytes == null) throw new ExtensionParseException("No Extension Data to parse");
		switch(key) {
			case 0:
				return new ColorExtension(bytes, options.getVersion());
			case 1:
				return new TimingExtension(bytes, options.getVersion());
			case 2:
				return new InformationExtension(bytes, options.getVersion());
			case 3:
				return new OrderingExtension(bytes, options.getVersion());
			default:
				if (options.getUnsupportedExtensions()) return new UnsupportedExtension(key, bytes, options.getVersion());
				throw new InvalidExtensionException("Invalid Extension Key");
		}
		
	};
	
};