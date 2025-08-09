package dev.ngspace.ngsweb.exceptions;

import java.io.IOException;

/**
 * When da 404 does a 404
 */
public class LegitFuckupException extends Exception {

	public LegitFuckupException(IOException e) {super(e);}

	private static final long serialVersionUID = -4450540232754079118L;
	
}
