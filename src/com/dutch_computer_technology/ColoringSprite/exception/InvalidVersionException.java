package com.dutch_computer_technology.ColoringSprite.exception;

public class InvalidVersionException extends SpriteException {
	
	private static final long serialVersionUID = 4187325291314776705L;
	
	public InvalidVersionException(Exception e) {
		super(e);
	};
	
	public InvalidVersionException(String e) {
		super(e);
	};
	
};
