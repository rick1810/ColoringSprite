package com.dutch_computer_technology.ColoringSprite.exception;

public class ExtensionParseException extends SpriteException {
	
	private static final long serialVersionUID = 4488147858855889284L;
	
	public ExtensionParseException(Exception e) {
		super(e);
	};
	
	public ExtensionParseException(String e) {
		super(e);
	};
	
};