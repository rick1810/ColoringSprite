package com.dutch_computer_technology.ColoringSprite.exception;

public class InvalidHeaderException extends SpriteException {
	
	private static final long serialVersionUID = 8308688788839486548L;
	
	public InvalidHeaderException(Exception e) {
		super(e);
	};
	
	public InvalidHeaderException(String e) {
		super(e);
	};
	
};
