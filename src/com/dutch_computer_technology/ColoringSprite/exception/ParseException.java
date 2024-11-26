package com.dutch_computer_technology.ColoringSprite.exception;

public class ParseException extends SpriteException {
	
	private static final long serialVersionUID = 7454493407272224411L;
	
	public ParseException(Exception e) {
		super(e);
	};
	
	public ParseException(String e) {
		super(e);
	};
	
};
