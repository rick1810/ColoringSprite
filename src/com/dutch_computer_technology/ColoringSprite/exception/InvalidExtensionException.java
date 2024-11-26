package com.dutch_computer_technology.ColoringSprite.exception;

public class InvalidExtensionException extends SpriteException {
	
	private static final long serialVersionUID = 6847254270551189027L;
	
	public InvalidExtensionException(Exception e) {
		super(e);
	};
	
	public InvalidExtensionException(String e) {
		super(e);
	};
	
};