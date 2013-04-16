import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Array;
import java.util.logging.Logger;

/**
 * @author sheak
 *
 */
/**
 * @author sheak
 *
 */
/**
 * @author sheak
 * 
 */
public class DatabaseResults {

	/**
	 * Connection values for the database
	 */
	private static final String DB_ADDRESS = "jdbc:postgresql://localhost:5432/BachelorThesis";
	private static final String DB_USERNAME = "java";
	private static final String DB_PASSWORD = "java";

	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(DatabaseResults.class
			.getName());

	Connection connection;

	/**
	 * Initialize the database object
	 */
	public DatabaseResults() {
		// Initialize the connection to and PostgreqSQL database
		log.info("Try to initialize PostgreSQL JDBC Connection");
		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			log.severe("PostgreSQL JDBC Driver was not found!");
			e.printStackTrace();
			return;
		}
		log.info("PostgreSQL JDBC Driver Registered!");
		try {
			connection = DriverManager.getConnection(DB_ADDRESS, DB_USERNAME,
					DB_PASSWORD);
		} catch (SQLException e) {
			log.severe("Connection Failed! Check output console!");
			e.printStackTrace();
			return;
		}
		// check for existing connection
		if (connection != null) {
			log.info("Connection was successfully initialized!");
		} else {
			log.warning("Connection could not be established!");
		}
	}

	// /////////////////////////////////////////////////
	// / add new entries
	// /////////////////////////////////////////////////

	public boolean addProgram(String programName, String programDescription,
			String urlToProjectPage, String urlToBugtracker,
			String pathAtLocalMachine) {
		// assert parameters to be not null
		assert programName != null;
		assert programDescription != null;
		assert urlToProjectPage != null;
		assert urlToBugtracker != null;
		assert pathAtLocalMachine != null;

		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO Program (programName, programDescription, utrlToProjectPage, urlToBugtracker, pathAtLocalMachine) VALUES(?, ?, ?, ?, ?)");
			stmt.setString(0, programName);
			stmt.setString(1, programDescription);
			stmt.setString(2, urlToProjectPage);
			stmt.setString(3, urlToBugtracker);
			stmt.setString(4, pathAtLocalMachine);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}

		// execute the statement
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean addBugReport(int id_program, String bugDescription,
			String priority, int officialID, String urlToBugreport,
			Timestamp reported, Timestamp modified) {
		// assert parameters to be not null
		assert bugDescription != null;
		assert priority != null;
		assert urlToBugreport != null;
		assert reported != null;
		assert modified != null;

		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO Bugreport (ID_program, bugDescription, priority, officialID, urlToBugreport, reported, modified) VALUES (?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(0, id_program);
			stmt.setString(1, bugDescription);
			stmt.setString(2, priority);
			stmt.setInt(3, officialID);
			stmt.setString(4, urlToBugreport);
			stmt.setTimestamp(5, reported);
			stmt.setTimestamp(6, modified);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}
		// execute the statement
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addChangingFiles(int ID_bugreport, String prefixedFilePath,
			String postfixedFilePath) {
		// assert parameters to be not null
		assert prefixedFilePath != null;
		assert postfixedFilePath != null;

		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO ChangingFiles (ID_bugreport, prefixedFilePath, postfixedFilePath) VALUES(?, ?, ?)");
			stmt.setInt(0, ID_bugreport);
			stmt.setString(1, prefixedFilePath);
			stmt.setString(2, postfixedFilePath);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}
		// execute the statement
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addAppliedMutationOperator(int ID_mutationoperator,
			int ID_changingfiles, Integer[] prefixedRange,
			Integer[] postfixedRange) {
		// assert parameters to have size 2
		assert prefixedRange.length == 2;
		assert postfixedRange.length == 2;
		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO AppliedMutationOperator (ID_mutationoperator, ID_changingfiles, prefixedRange, postfixedRange) VALUES(?, ?, ?, ?)");
			Array prefixed = connection.createArrayOf("INTEGER", prefixedRange);
			Array postfixed = connection.createArrayOf("INTEGER",
					postfixedRange);
			stmt.setInt(0, ID_mutationoperator);
			stmt.setInt(1, ID_changingfiles);
			stmt.setArray(2, prefixed);
			stmt.setArray(3, postfixed);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}
		// execute the statement
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addMutationOperator(int ID_mutationoperatorcategory,
			int ID_literature, int ID_effectlocation,
			String mutationOperatorDescription,
			String mutationOperatorAbbreviation) {
		// assert parameters to be not null
		assert mutationOperatorDescription != null;
		assert mutationOperatorAbbreviation != null;
		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO MutationOperator (ID_mutationoperatorcategory, ID_literature, ID_effectlocation, mutationOperatorDescription, mutationOperatorAbbreviation) VALUES(?, ?, ?, ?, ?)");
			stmt.setInt(0, ID_mutationoperatorcategory);
			stmt.setInt(1, ID_literature);
			stmt.setInt(2, ID_effectlocation);
			stmt.setString(3, mutationOperatorDescription);
			stmt.setString(4, mutationOperatorAbbreviation);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}

		// execute the statement
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// /////////////////////////////////////////////////
	// / get existing entries
	// /////////////////////////////////////////////////

	public ResultSet getProgram(int ID_program) {
		// create a parameterized statement to insert the new data
		PreparedStatement stmt = null;
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM Program where ID_program = ?");
			stmt.setInt(0, ID_program);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return null;
		}
		// execute the statement
		ResultSet results;
		try {
			results = stmt.executeQuery();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		// check return resultset
		try {
			results.last();
			int size = results.getRow();
			if (size != 1) {
				log.warning("Found " + size
						+ " entries in table Programs with id " + ID_program
						+ ".");
			}
		} catch (SQLException e) {
			log.warning("Could not check prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ResultSet getAllBugreportsForProgram(int ID_program) {
		// create a parameterized statement to insert the new data
		PreparedStatement stmt = null;
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM Bugreport where ID_program = ?");
			stmt.setInt(0, ID_program);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return null;
		}
		// execute the statement
		ResultSet results;
		try {
			results = stmt.executeQuery();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ResultSet getAllChangingFilesForBugreport(int ID_bugreport) {
		// create a parameterized statement to insert the new data
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM ChangingFiles where ID_bugreport = ?");
			stmt.setInt(0, ID_bugreport);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return null;
		}
		// execute the statement
		ResultSet results;
		try {
			results = stmt.executeQuery();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}
	
	public ResultSet getAllAppliedMutationOperatorForChangingFiles(int ID_changingfiles){
		// create a parameterized statement to insert the new data
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM AppliedMutationOperator where ID_changingfiles = ?");
			stmt.setInt(0, ID_changingfiles);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return null;
		}
		// execute the statement
		ResultSet results;
		try {
			results = stmt.executeQuery();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ResultSet getAllMutationOperators(){
		// create a parameterized statement to insert the new data
		Statement stmt = null;
		String query = "SELECT * FROM AppliedMutationOperator";
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return null;
		}
		// execute the statement
		ResultSet results;
		try {
			results = stmt.executeQuery(query);
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}
	
	public ResultSet getAllAppliedMutationOperatorForMutationOperator(int ID_mutationoperator){
		// create a parameterized statement to insert the new data
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM AppliedMutationOperator where ID_mutationoperator = ?");
			stmt.setInt(0, ID_mutationoperator);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return null;
		}
		// execute the statement
		ResultSet results;
		try {
			results = stmt.executeQuery();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}
	
}
