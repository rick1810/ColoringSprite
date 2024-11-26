package com.dutch_computer_technology.ColoringSprite.sprite;

import com.dutch_computer_technology.ColoringSprite.bytes.ByteManager;
import com.dutch_computer_technology.ColoringSprite.exception.ByteException;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.Extension;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.ExtensionManager.ExtensionName;

public class ColoringSpriteHeader {
	
	private ColoringSpriteOptions options;
	private int version;
	private int bitsPerColor;
	private int width;
	private int height;
	private int sprites;
	private Extension[] extensions;
	
	/**
	 * Create a ColoringSpriteHeader.
	 * 
	 */
	public ColoringSpriteHeader() {
		
		this.options = new ColoringSpriteOptions();
		this.version = 1;
		this.bitsPerColor = 2;
		this.width = 0;
		this.height = 0;
		this.sprites = 0;
		this.extensions = new Extension[0];
		
	};
	
	/**
	 * Create a {@code byte[]} from the ColoringSpriteHeader.
	 * 
	 * @throws ByteException
	 */
	public byte[] toByte() throws ByteException {
		
		byte[] bytes = new byte[0];
		
		bytes = ByteManager.append(bytes, ByteManager.intToByte(this.version)); //Version, 1byte
				
		return bytes;
		
	};
	
	/**
	 * Get options.
	 * 
	 * @return {@code ColoringSpriteOptions}
	 */
	public ColoringSpriteOptions getOptions() {
		return this.options;
	};
	
	/**
	 * Set options.
	 * 
	 * @param options {@code ColoringSpriteOptions}
	 */
	public void setOptions(ColoringSpriteOptions options) {
		this.options = options;
	};
	
	/**
	 * Set version.
	 * 
	 * @param version {@code int}
	 */
	public void setVersion(int version) {
		this.version = version;
	};
	
	/**
	 * Get version.
	 * 
	 * @return {@code int}
	 */
	public int getVersion() {
		return this.version;
	};
	
	/**
	 * Set bitsPerColor.
	 * 
	 * @param bitsPerColor {@code int}
	 */
	public void setBitsPerColor(int bitsPerColor) {
		this.bitsPerColor = bitsPerColor;
	};
	
	/**
	 * Get bitsPerColor.
	 * 
	 * @return {@code int}
	 */
	public int getBitsPerColor() {
		return this.bitsPerColor;
	};
	
	/**
	 * Set width.
	 * 
	 * @param width {@code int}
	 */
	public void setWidth(int width) {
		this.width = width;
	};
	
	/**
	 * Get width.
	 * 
	 * @return {@code int}
	 */
	public int getWidth() {
		return this.width;
	};
	
	/**
	 * Set height.
	 * 
	 * @param height {@code int}
	 */
	public void setHeight(int height) {
		this.height = height;
	};
	
	/**
	 * Get height.
	 * 
	 * @return {@code int}
	 */
	public int getHeight() {
		return this.height;
	};
	
	/**
	 * Set number of sprites.
	 * 
	 * @param sprites {@code int}
	 */
	public void setSprites(int sprites) {
		this.sprites = sprites;
	};
	
	/**
	 * Get number of sprites.
	 * 
	 * @return {@code int}
	 */
	public int getSprites() {
		return this.sprites;
	};
	
	/**
	 * Total number of extensions.
	 * 
	 * @return {@code int}
	 */
	public int extensions() {
		return this.extensions.length;
	};
	
	/**
	 * Get extensions.
	 * 
	 * @return {@code Extension[]}
	 */
	public Extension[] getExtensions() {
		return this.extensions;
	};
	
	/**
	 * Set extensions.
	 * 
	 * @param extensions {@code Extension[]}
	 */
	public void setExtensions(Extension[] extensions) {
		this.extensions = extensions;
	};
	
	/**
	 * Get extension using {@code ExtensionName}.
	 * 
	 * @param name {@code ExtensionName}
	 * @return {@code null} when not found, {@code Extension} when found.
	 */
	public Extension getExtension(ExtensionName name) {
		if (name == null) return null;
		return getExtension(name.getKey());
	};
	
	/**
	 * Get extension using {@code int} key.
	 * 
	 * @param key {@code int}
	 * @return {@code null} when not found, {@code Extension} when found.
	 */
	public Extension getExtension(int key) {
		for (Extension extension : this.extensions) {
			if (extension.getKey() == key) {
				return extension;
			};
		};
		return null;
	};
	
	/**
	 * Check if has extension using {@code ExtensionName}.
	 * 
	 * @param name {@code ExtensionName}
	 * @return {@code true} if extension found, {@code false} if not found.
	 */
	public boolean hasExtension(ExtensionName name) {
		if (name == null) return false;
		return hasExtension(name.getKey());
	};
	
	/**
	 * Check if has extension using {@code int} key.
	 * 
	 * @param key {@code int}
	 * @return {@code true} if extension found, {@code false} if not found.
	 */
	public boolean hasExtension(int key) {
		for (Extension extension : this.extensions) {
			if (extension.getKey() == key) {
				return true;
			};
		};
		return false;
	};
	
	/**
	 * Add extension.
	 * 
	 * @param extension {@code Extension}
	 */
	public void addExtension(Extension extension) {
		if (extension == null) return;
		remExtension(extension.getKey());
		int len = this.extensions.length;
		Extension[] extensions = new Extension[len+1];
		System.arraycopy(this.extensions, 0, extensions, 0, len);
		extensions[len] = extension;
		this.extensions = extensions;
	};
	
	/**
	 * Remove extension.
	 * 
	 * @param extension {@code Extension}
	 */
	public void remExtension(Extension extension) {
		if (extension == null) return;
		remExtension(extension.getKey());
	};
	
	/**
	 * Remove extension.
	 * 
	 * @param name {@code ExtensionName}
	 */
	public void remExtension(ExtensionName name) {
		if (name == null) return;
		remExtension(name.getKey());
	};
	
	/**
	 * Remove extension.
	 * 
	 * @param key {@code int}
	 */
	public void remExtension(int key) {
		for (int i = 0; i < this.extensions.length; i++) {
			Extension extension = this.extensions[i];
			if (extension.getKey() == key) {
				int len = this.extensions.length;
				if (len == 1) {
					this.extensions = new Extension[0];
					break;
				};
				Extension[] extensions = new Extension[len-1];
				System.arraycopy(this.extensions, 0, extensions, 0, i);
				System.arraycopy(this.extensions, i+1, extensions, i, len-i-1);
				this.extensions = extensions;
				break;
			};
		};
	};
	
};