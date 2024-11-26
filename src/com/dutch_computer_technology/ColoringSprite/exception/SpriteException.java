package com.dutch_computer_technology.ColoringSprite.exception;

public class SpriteException extends Exception {
	
	private static final long serialVersionUID = 4929598082494793031L;
	
	public SpriteException(Exception e) {
		super(e);
	};
	
	public SpriteException(String e) {
		super(e);
	};
	
};