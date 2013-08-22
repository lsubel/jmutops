package application;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class InitializeProperty {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create a String which contains comment for the result .xml
		String xml_comment = 
			"\n" +
			"\t" + "eventlogger-active: add a EventLogger to the running jMutOps." + "\n" + 
			"\t" + "eventlogger-output-console: true iff there should be a output file." + "\n" +
			"\t" + "eventlogger-output-file: true iff there should be a output file containing all fired events." + "\n" +	
			"\t" + "eventlogger-output-file-path: the path to the folder where the resulting file should be stored." + "\n"+
			"\n" + 
			"\t" + "resultcreator-active: add a ResultCreator to the running jMutOps." + "\n" + 
			"\t" + "resultcreator-output-file: true iff there should be a output file containing all fired events." + "\n" +
			"\t" + "resultcreator-output-console: true iff there should be a output file." + "\n" +
			"\t" + "resultcreator-output-file-path: the path to the folder where the resulting file should be stored." + "\n" +
			"\n" +
			"\t" + "db-active: add a ResultDatabase to the running jMutOps." + "\n" + 
			"\t" + "db-address: Address to the database which should store the results." + "\n" +
			"\t" + "db-username: Username of the user to sign in." + "\n" +
			"\t" + "db-password: Password of the user to sign in." + "\n" +
			"\n"
			;
		
		// create a new Property File
		Properties prop = new Properties();
		// set up some default values wich should be used
		prop.setProperty("eventlogger-active", "true");
		prop.setProperty("eventlogger-output-console", "true");
		prop.setProperty("eventlogger-output-file", "false");
		prop.setProperty("eventlogger-output-file-path", "PATH-TO-FOLDER-WHICH-STORES-RESULTS");
		prop.setProperty("resultcreator-active", "true");
		prop.setProperty("resultcreator-output-console", "false");
		prop.setProperty("resultcreator-output-file", "true");
		prop.setProperty("resultcreator-output-file-path", "");
		prop.setProperty("db-active", "false");
		prop.setProperty("db-address", "ADDRESS");
		prop.setProperty("db-username", "USERNAME");
		prop.setProperty("db-password", "PASSWORD");
		
		// store the results
		BufferedOutputStream stream;
		try {
			stream = new BufferedOutputStream(new FileOutputStream("results.properties"));
			prop.storeToXML(stream, xml_comment);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
