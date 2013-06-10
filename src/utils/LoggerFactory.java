package utils;

import java.util.logging.Logger;

public class LoggerFactory {

	public static Logger getLogger(String name){
		Logger log = Logger.getLogger(name);
		log.setLevel(Settings.loggerlevel);
		return log;
	}
	
	public static Logger getAnonymousLogger(){
		Logger log = Logger.getAnonymousLogger();
		log.setLevel(Settings.loggerlevel);
		return log;
	}
	
}
