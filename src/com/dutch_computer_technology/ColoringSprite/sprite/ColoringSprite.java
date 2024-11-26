package com.dutch_computer_technology.ColoringSprite.sprite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.dutch_computer_technology.ColoringSprite.bytes.ByteManager;
import com.dutch_computer_technology.ColoringSprite.bytes.Offset;
import com.dutch_computer_technology.ColoringSprite.exception.ByteException;
import com.dutch_computer_technology.ColoringSprite.exception.ColorException;
import com.dutch_computer_technology.ColoringSprite.exception.ExtensionParseException;
import com.dutch_computer_technology.ColoringSprite.exception.InvalidExtensionException;
import com.dutch_computer_technology.ColoringSprite.exception.InvalidHeaderException;
import com.dutch_computer_technology.ColoringSprite.exception.InvalidVersionException;
import com.dutch_computer_technology.ColoringSprite.exception.ParseException;
import com.dutch_computer_technology.ColoringSprite.sprite.coder.c1.C1;
import com.dutch_computer_technology.ColoringSprite.sprite.coder.c1.CSH1;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.Extension;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.ExtensionManager.ExtensionName;
import com.dutch_computer_technology.ColoringSprite.sprite.extension.OrderingExtension;

public class ColoringSprite {
	
	private ColoringSpriteOptions options = new ColoringSpriteOptions();
	
	private int width = 0;
	private int height = 0;
	private int sprites = 0;
	private Extension[] extensions = new Extension[0];
	private Map<Integer, int[]> spriteMap = new HashMap<Integer, int[]>();
	
	private static int bufferSize = 4096;

	/**
	 * Create a ColoringSprite.
	 * 
	 * @param width {@code int} Width of sprite.
	 * @param height {@code int} Height of sprite.
	 */
	public ColoringSprite(int width, int height) {
		
		this.options = null;
		this.width = width;
		this.height = height;
		
	};
	
	/**
	 * Create a ColoringSprite.
	 * 
	 * @param width {@code int} Width of sprite.
	 * @param height {@code int} Height of sprite.
	 * @param options {@code ColoringSpriteOptions} Options of sprite. nullable
	 */
	public ColoringSprite(int width, int height, ColoringSpriteOptions options) {
		
		if (options != null) this.options = options;
		this.width = width;
		this.height = height;
		
	};
	
	/**
	 * Create a ColoringSprite from a File.
	 * 
	 * @param file {@code File} to parse from.
	 * @throws InvalidHeaderException
	 * @throws ParseException
	 * @throws InvalidVersionException
	 * @throws InvalidExtensionException
	 * @throws ExtensionParseException
	 */
	public ColoringSprite(File file) throws InvalidHeaderException, ParseException, InvalidVersionException, InvalidExtensionException, ExtensionParseException {
		
		fileParse(file, bufferSize, null);
		
	};
	
	/**
	 * Create a ColoringSprite from a File.
	 * 
	 * @param file {@code File} to parse from.
	 * @param options {@code ColoringSpriteOptions} to use as options.
	 * @throws InvalidHeaderException
	 * @throws ParseException
	 * @throws InvalidVersionException
	 * @throws InvalidExtensionException
	 * @throws ExtensionParseException
	 */
	public ColoringSprite(File file, ColoringSpriteOptions options) throws InvalidHeaderException, ParseException, InvalidVersionException, InvalidExtensionException, ExtensionParseException {
		
		fileParse(file, bufferSize, options);
		
	};
	
	/**
	 * Create a ColoringSprite from a File.
	 * 
	 * @param file {@code File} to parse from.
	 * @param bufferSize The {@code int} size of the buffer, for parsing.
	 * @throws InvalidHeaderException
	 * @throws ParseException
	 * @throws InvalidVersionException
	 * @throws InvalidExtensionException
	 * @throws ExtensionParseException
	 */
	public ColoringSprite(File file, int bufferSize) throws InvalidHeaderException, ParseException, InvalidVersionException, InvalidExtensionException, ExtensionParseException {
		
		fileParse(file, bufferSize, null);
		
	};
	
	/**
	 * Create a ColoringSprite from a File.
	 * 
	 * @param file {@code File} to parse from.
	 * @param bufferSize The {@code int} size of the buffer, for parsing.
	 * @param options {@code ColoringSpriteOptions} to use as options.
	 * @throws InvalidHeaderException
	 * @throws ParseException
	 * @throws InvalidVersionException
	 * @throws InvalidExtensionException
	 * @throws ExtensionParseException
	 */
	public ColoringSprite(File file, int bufferSize, ColoringSpriteOptions options) throws InvalidHeaderException, ParseException, InvalidVersionException, InvalidExtensionException, ExtensionParseException {
		
		fileParse(file, bufferSize, options);
		
	};
	
	/**
	 * Get bytes of ColoringSprite from a File.
	 * 
	 * @param file {@code File} to parse from.
	 * @param bufferSize The {@code int} size of the buffer, for parsing.
	 * @param options {@code ColoringSpriteOptions} to use as options.
	 * @throws InvalidHeaderException
	 * @throws ParseException
	 * @throws InvalidVersionException
	 * @throws InvalidExtensionException
	 * @throws ExtensionParseException
	 */
	private void fileParse(File file, int bufferSize, ColoringSpriteOptions options) throws InvalidHeaderException, ParseException, InvalidVersionException, InvalidExtensionException, ExtensionParseException {
		
		if (!file.exists()) throw new ParseException("No file");
		if (!file.isFile()) throw new ParseException("Not a file");
		if (!file.canRead()) throw new ParseException("Can't read file");
		
		byte[] buffer = new byte[bufferSize];
		int i = 0;
		
		try (InputStream is = new FileInputStream(file)) {
			
			int b;
			while ((b = is.read()) != -1) {
				if (i >= buffer.length) buffer = ByteManager.append(buffer, bufferSize);
				buffer[i] = (byte) b;
				i++;
			};
			
		} catch(FileNotFoundException ignore) {
			
			throw new ParseException("Could not open InputStream");
			
		} catch(IOException ignore) {
			
			throw new ParseException("Could not read byte");
			
		};
		
		byte[] bytes = new byte[i];
		System.arraycopy(buffer, 0, bytes, 0, i);
		
		parse(bytes, options);
		
	};
	
	/**
	 * Create a ColoringSprite from a byte array.
	 * 
	 * @param bytes The {@code byte[]} to parse.
	 * @param options {@code ColoringSpriteOptions} to use as options.
	 * @throws InvalidHeaderException
	 * @throws ParseException
	 * @throws InvalidVersionException
	 * @throws InvalidExtensionException
	 * @throws ExtensionParseException
	 */
	public ColoringSprite(byte[] bytes, ColoringSpriteOptions options) throws InvalidHeaderException, ParseException, InvalidVersionException, InvalidExtensionException, ExtensionParseException {
		
		parse(bytes, options);
		
	};
	
	/**
	 * Parse a ColoringSprite from a byte array.
	 * 
	 * @param bytes The {@code byte[]} to parse.
	 * @param options {@code ColoringSpriteOptions} to use as options.
	 * @throws InvalidHeaderException
	 * @throws ParseException
	 * @throws InvalidVersionException
	 * @throws InvalidExtensionException
	 * @throws ExtensionParseException
	 */
	private void parse(byte[] bytes, ColoringSpriteOptions options) throws InvalidHeaderException, ParseException, InvalidExtensionException, ExtensionParseException, InvalidVersionException {
		
		int version = 0;
		try {
			version = ByteManager.getInt(bytes, new Offset(), 1); //Get version to parse.
		} catch(ByteException ignore) {
			throw new InvalidVersionException("Could not get version, Check if the file is damaged.");
		};
		
		switch(version) { //Parse using the correct version.
			case 1:
				C1.parse(this, bytes, options);
				break;
			default:
				throw new InvalidVersionException("Unsupported version: " + version + ", Check if you have the latest JAR or dependency version.");
		};
		
	};
	
	/**
	 * Create a {@code byte[]} from the ColoringSprite.
	 * 
	 * @throws InvalidVersionException
	 * @throws ColorException
	 * @throws ByteException
	 */
	public byte[] toByte() throws InvalidVersionException, ColorException, ByteException {
		
		int version = this.options.getVersion();
		
		ColoringSpriteHeader header = null;
		byte[] dataBytes = new byte[0];
		switch(version) {
			case 1:
				header = new CSH1(version, this.width, this.height, this.sprites, this.extensions, this.options);
				dataBytes = C1.encode(header, this.options, this.spriteMap);
				break;
			default:
				throw new InvalidVersionException("Unsupported version: " + version + ", Check if you have the latest JAR or dependency version.");
		};
		
		int[] extensionDone = new int[0];
		if (this.hasExtension(ExtensionName.Ordering)) {
			OrderingExtension ordering = (OrderingExtension) this.getExtension(ExtensionName.Ordering);
			if (ordering != null) {
				int[] order = ordering.getOrder();
				extensionDone = new int[order.length];
				int d = 0;
				for (int key : order) {
					extensionDone[d] = key;
					d++;
					if (this.hasExtension(key)) {
						Extension extension = this.getExtension(key);
						if (extension == null) continue;
						dataBytes = extension.encode(dataBytes, version);
					};
				};
			};
		};
		
		extensionsLoop: for (Extension extension : this.extensions) {
			for (int key : extensionDone) {
				if (extension.getKey() == key) continue extensionsLoop;
			};
			dataBytes = extension.encode(dataBytes, version);
		};
		
		byte[] bytes = header.toByte();
		bytes = ByteManager.append(bytes, dataBytes);
		return bytes;
		
	};
	
	/**
	 * Get a sprite.
	 * 
	 * @return {@code null} when not found, {@code int[]} when found.
	 */
	public int[] getSprite(int sprite) {
		
		if (!this.spriteMap.containsKey(sprite)) return null;
		return this.spriteMap.get(sprite);
		
	};
	
	/**
	 * Add a sprite.
	 * 
	 * @param pixels {@code int[]} The pixels of the new sprite, width*height
	 */
	public void addSprite(int[] pixels) throws ColorException {
		
		int aspect = this.width * this.height;
		
		int[] myPixels = new int[aspect];
		System.arraycopy(pixels, 0, myPixels, 0, pixels.length);
		
		for (int i = 0; i < myPixels.length; i++) {
			int p = myPixels[i];
			if (p < 0) throw new ColorException("Colors provided out-of-range, <0");
			if (p > this.options.getColors()-1) {
				if (this.options.getUnsupportedColors()) {
					myPixels[i] = this.options.getDefaultColor();
				} else {
					throw new ColorException("Colors provided out-of-range, >" + (this.options.getColors()-1));
				};
			};
		};
		
		this.spriteMap.put(this.sprites, myPixels);
		this.sprites++;
		
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
	 * Get width.
	 * 
	 * @return {@code int}
	 */
	public int getWidth() {
		return this.width;
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
	 * Set size.
	 * 
	 * @param width {@code int}
	 * @param height {@code int}
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	};
	
	/**
	 * Total number of sprites.
	 * 
	 * @return {@code int}
	 */
	public int sprites() {
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