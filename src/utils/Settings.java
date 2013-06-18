package utils;

import java.util.logging.Level;

public class Settings {

	/**
	 * Constant value used when we could not resolve a binding
	 */
	public static final boolean BINDING_DEFAULTVALUE = false;

	/**
	 * Setting the level of the logger messages
	 */
	public static final Level LOGGER_LEVEL = Level.FINE;
	
	/**
	 * True if jMutOps should write a logfile
	 */
	public static final boolean LOGGER_WRITE_FILE = false;
	
	/**
	 * True if jMutOps should write results in a DB.
	 */
	public static final boolean DB_WRITE = false;
	
	/**
	 * Contains the default folder name for prefix folders.
	 */
	public static final String DEFAULT_PREFIX_FOLDER = "pre-fix";
		
	/**
	 * Contains the default folder name for postfix folders.
	 */
	public static final String DEFAULT_POSTFIX_FOLDER = "post-fix";
}
