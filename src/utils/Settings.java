package utils;

import java.util.logging.Level;

public class Settings {

	/**
	 * Constant value used when we could not resolve a binding
	 */
	public static final boolean DEFAULT_BINDING_VALUE = false;

	/**
	 * Setting the level of the logger messages
	 */
	public static final Level loggerlevel = Level.INFO;
	
	/**
	 * True if jMutOps should write create a logfile
	 */
	public static final boolean isWritingLog = true;
	
	/**
	 * True if jMutOps should write results in a DB.
	 */
	public static final boolean writeInDB = true;
}
