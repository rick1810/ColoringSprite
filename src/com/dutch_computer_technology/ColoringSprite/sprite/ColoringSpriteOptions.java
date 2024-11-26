package com.dutch_computer_technology.ColoringSprite.sprite;

import com.dutch_computer_technology.ColoringSprite.exception.ColorException;

public class ColoringSpriteOptions {
	
	private int version;
	private boolean unsupportedExtensions;
	private int colors;
	private int bitsPerColor;
	private boolean unsupportedColors;
	private int defaultColor;
	
	/**
	 * Create a ColoringSpriteOptions.
	 * 
	 */
	public ColoringSpriteOptions() {
		
		this.version = 1;
		this.unsupportedExtensions = false;
		this.colors = 4;
		this.bitsPerColor = 2;
		this.unsupportedColors = false;
		this.defaultColor = 0;
		
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
	 * Set version.
	 * 
	 * @param version {@code int}
	 */
	public void setVersion(int version) {
		this.version = version;
	};
	
	/**
	 * Get unsupportedExtensions.
	 * 
	 * @return {@code true} when unsupported extensions are allowed, {@code false} if only standard extensions are allowed.
	 */
	public boolean getUnsupportedExtensions() {
		return this.unsupportedExtensions;
	};
	
	/**
	 * Set unsupportedExtensions.
	 * 
	 * @param unsupportedExtensions {@code boolean} Whenever to allow/disallow unsupported extensions.
	 */
	public void setUnsupportedExtensions(boolean unsupportedExtensions) {
		this.unsupportedExtensions = unsupportedExtensions;
	};
	
	/**
	 * Get total number of colors.
	 * 
	 * @return {@code int}
	 */
	public int getColors() {
		return this.colors;
	};
	
	private static final int maxColors = Integer.MAX_VALUE; // (256L*256L*256L*256L); //Max colors, for rgba
	private static final int maxBitsForColors = (int) (Math.log(maxColors) / Math.log(2)); //Max bits for max colors
	private static final int maxBits = 255; //Max bits to fit in 1byte, maxBits is saved in 1byte inside of the .sc file
	/**
	 * Set total number of colors,
	 * get rounded up to fit together with bitsPerColor.
	 * 
	 * @param colors {@code int}
	 * @throws ColorException when < 2 or > max, or when bits > max
	 */
	public void setColors(int colors) throws ColorException {
		
		if (colors < 2) throw new ColorException("Colors out-of-range <2");
		if (colors > maxColors) throw new ColorException("Colors out-of-range >" + maxColors);
		
		int bits = 1;
		int pos = 2;
		while (pos < colors) {
			bits++;
			pos *= 2L;
		};
		if (bits > maxBits) throw new ColorException("BitsPerColor out-of-range >" + maxBits);
		this.bitsPerColor = bits;
		this.colors = pos;
		
	};
	
	/**
	 * Sets total number of colors to the max supported.
	 * 
	 */
	public void setColorsMax() {
		this.colors = maxColors;
		this.bitsPerColor = maxBitsForColors;
	};
	
	/**
	 * Sets bitsPerColor to the max supported.
	 * 
	 */
	public void setBitsMax() {
		this.colors = maxColors;
		this.bitsPerColor = maxBitsForColors;
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
	 * Set bitsPerColor,
	 * this also changes number of colors.
	 * 
	 * @param bitsPerColor {@code int}
	 * @throws ColorException when < 1 or > max
	 */
	public void setBitsPerColor(int bitsPerColor) throws ColorException {
		
		if (bitsPerColor < 1) throw new ColorException("BitsPerColor out-of-range <1");
		if (bitsPerColor > maxBitsForColors) throw new ColorException("BitsPerColor out-of-range >" + maxBitsForColors);
		if (bitsPerColor > maxBits) throw new ColorException("BitsPerColor out-of-range >" + maxBits);
		
		int bits = 1;
		int pos = 2;
		while (bits < bitsPerColor) {
			bits++;
			pos *= 2L;
		};
		this.bitsPerColor = bitsPerColor;
		this.colors = pos;
		
	};
	
	/**
	 * Get unsupportedColors,
	 * whenever to allow color keys that are out-of-range,
	 * Turns the color to the default color.
	 * 
	 * @return {@code boolean}
	 */
	public boolean getUnsupportedColors() {
		return this.unsupportedColors;
	};
	
	/**
	 * Set unsupportedColors,
	 * whenever to allow color keys that are out-of-range,
	 * Turns the color to the default color.
	 * 
	 * @param unsupportedColors {@code boolean}
	 */
	public void setUnsupportedColors(boolean unsupportedColors) {
		this.unsupportedColors = unsupportedColors;
	};
	
	/**
	 * Get default color key.
	 * 
	 * @return {@code int}
	 */
	public int getDefaultColor() {
		return this.defaultColor;
	};
	
	/**
	 * Set default color key.
	 * 
	 * @param defaultColor {@code int}
	 * @throws ColorException when defaultColor < 0 or when it's exceeds the total number of colors.
	 */
	public void setDefaultColor(int defaultColor) throws ColorException {
		if (defaultColor < 0) throw new ColorException("DefaultColor out-of-range, <0");
		if (defaultColor > this.colors-1L) throw new ColorException("DefaultColor out-of-range, >" + (this.colors-1L));
		this.defaultColor = defaultColor;
	};
	
};