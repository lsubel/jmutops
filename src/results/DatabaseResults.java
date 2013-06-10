package results;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Array;
import java.util.logging.Logger;

import utils.LoggerFactory;

/**
 * Class which builds a connection to the database and which is response to
 * add new data to the database and
 * get existing data from the database.
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
	private static final Logger log = LoggerFactory.getLogger(DatabaseResults.class
			.getName());

	/**
	 * Field which stores the connection object after initialization
	 */
	private Connection connection;

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
	// / initialize database
	// /////////////////////////////////////////////////

	public boolean initializeTables() {
		log.info("Initialization of database tables started!");

		// TABLE Program
		initializeTableProgram();

		// TABLE Bugreport
		initializeTableBugreport();

		// TABLE ChangingFiles
		initializeTableChangingFiles();

		// TABLE AppliedMutationOperator
		initializeTableAppliedMutationOperator();

		// TABLE MutationOperator
		initializeTableMutationOperator();

		// TABLE MutationOperatorCategory
		initializeTableMutationOperatorCategory();

		// TABLE Literature
		initializeTableLiterature();

		// TABLE LocationOfEffect
		initializeTableLocationOfEffect();
		
		// TABLE MutationType
		initializeTableMutationType();
		
		// finished Message
		log.info("Initialization of database tables finished!");
		return true;

	}

	private boolean initializeTableMutationType() {
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("CREATE TABLE MutationType ("
							+ "ID_mutationtype SERIAL PRIMARY KEY,"
							+ "typeName VARCHAR(32) NOT NULL" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table MutationType was created!");
		} catch (SQLException e) {
			log.warning("Could not create table MutationType!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initializeTableLocationOfEffect() {
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("CREATE TABLE LocationOfEffect ("
							+ "ID_effectlocation SERIAL PRIMARY KEY,"
							+ "effectName VARCHAR(127) NOT NULL" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table LocationOfEffect was created!");
		} catch (SQLException e) {
			log.warning("Could not create table LocationOfEffect!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initializeTableLiterature() {
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("CREATE TABLE Literature ("
					+ "ID_literature SERIAL PRIMARY KEY,"
					+ "title VARCHAR(127) NOT NULL, "
					+ "authors VARCHAR(127), " + "url VARCHAR(127)" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Literature was created!");
		} catch (SQLException e) {
			log.warning("Could not create table Literature!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initializeTableMutationOperatorCategory() {
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("CREATE TABLE MutationOperatorCategory ("
							+ "ID_mutationoperatorcategory SERIAL PRIMARY KEY, "
							+ "categoryName VARCHAR(127)" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table MutationOperatorCategory was created!");
		} catch (SQLException e) {
			log.warning("Could not create table MutationOperatorCategory!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initializeTableMutationOperator() {
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("CREATE TABLE MutationOperator ("
							+ "ID_mutationoperator SERIAL PRIMARY KEY, "
							+ "ID_mutationoperatorcategory INTEGER NOT NULL, "
							+ "ID_literature INTEGER NOT NULL, "
							+ "ID_effectlocation INTEGER NOT NULL,"
							+ "mutationOperatorDescription VARCHAR(1023),"
							+ "mutationOperatorAbbreviation VARCHAR(7)" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table MutationOperator was created!");
		} catch (SQLException e) {
			log.warning("Could not create table MutationOperator!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initializeTableAppliedMutationOperator() {
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("CREATE TABLE AppliedMutationOperator ("
							+ "ID_appliedmutationoperator SERIAL PRIMARY KEY, "
							+ "ID_changingfiles INTEGER NOT NULL, "
							+ "ID_mutationoperator INTEGER NOT NULL, "
							+ "prefixedRange INTEGER[2],"
							+ "postfixedRange INTEGER[2]" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table AppliedMutationOperator was created!");
		} catch (SQLException e) {
			log.warning("Could not create table AppliedMutationOperator!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initializeTableChangingFiles() {
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("CREATE TABLE ChangingFiles ("
					+ "ID_changingfiles SERIAL PRIMARY KEY, "
					+ "ID_bugreport INTEGER NOT NULL, "
					+ "prefixedFilePath VARCHAR(511), "
					+ "postfixedFilePath VARCHAR(511)" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table ChangingFiles was created!");
		} catch (SQLException e) {
			log.warning("Could not create table ChangingFiles!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initializeTableBugreport() {
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("CREATE TABLE Bugreport ("
					+ "ID_bugreport SERIAL PRIMARY KEY, "
					+ "ID_program INTEGER NOT NULL, "
					+ "bugDescription VARCHAR(1023), "
					+ "priority VARCHAR(63), " + "officalID INTEGER, "
					+ "reported TIMESTAMP, " + "modified TIMESTAMP" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Bugreport was created!");
		} catch (SQLException e) {
			log.warning("Could not create table Bugreport!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initializeTableProgram() {
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("CREATE TABLE Program ("
					+ "ID_program SERIAL PRIMARY KEY, "
					+ "programName VARCHAR(255) NOT NULL, "
					+ "programDescription VARCHAR(1023), "
					+ "utrlToProjectPage VARCHAR(255), "
					+ "urlToBugtracker VARCHAR(255), "
					+ "pathAtLocalMachine VARCHAR(1023)" + ")");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Program was created!");
		} catch (SQLException e) {
			log.warning("Could not create table Program!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// /////////////////////////////////////////////////
	// / delete tables
	// /////////////////////////////////////////////////

	public boolean dropTable(String tablename){
		
		log.info("Deletion of database table " +  tablename + " started!");

		PreparedStatement stmt;

		try {
			stmt = connection.prepareStatement("DROP TABLE " + tablename);
			stmt.executeUpdate();
			stmt.close();
			log.info("Table " +  tablename + " was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table " +  tablename + "!");
			e.printStackTrace();
			return false;
		}
		
		log.info("Deletion of all database table " +  tablename + " finished!");
		return true;
	}
	
	public void dropTables() {
		log.info("Deletion of all database tables started!");

		PreparedStatement stmt;

		// TABLE Program
		try {
			stmt = connection.prepareStatement("DROP TABLE Program");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Program was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table Program!");
			e.printStackTrace();
		}

		// TABLE Bugreport
		try {
			stmt = connection.prepareStatement("DROP TABLE Bugreport");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Bugreport was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table Bugreport!");
			e.printStackTrace();
		}

		// TABLE ChangingFiles
		try {
			stmt = connection.prepareStatement("DROP TABLE ChangingFiles");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table ChangingFiles was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table ChangingFiles!");
			e.printStackTrace();
		}

		// TABLE AppliedMutationOperator
		try {
			stmt = connection
					.prepareStatement("DROP TABLE AppliedMutationOperator");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table AppliedMutationOperator was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table AppliedMutationOperator!");
			e.printStackTrace();
		}

		// TABLE MutationOperator
		try {
			stmt = connection.prepareStatement("DROP TABLE MutationOperator");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table MutationOperator was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table MutationOperator!");
			e.printStackTrace();
		}
		// TABLE MutationOperatorCategory
		try {
			stmt = connection
					.prepareStatement("DROP TABLE MutationOperatorCategory");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table MutationOperatorCategory was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table MutationOperatorCategory!");
			e.printStackTrace();
		}
		// TABLE Literature
		try {
			stmt = connection.prepareStatement("DROP TABLE Literature");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Literature was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table Literature!");
			e.printStackTrace();
		}
		// TABLE LocationOfEffect
		try {
			stmt = connection.prepareStatement("DROP TABLE LocationOfEffect");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table LocationOfEffect was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table LocationOfEffect!");
			e.printStackTrace();
		}
		// TABLE MutationType
		try {
			stmt = connection.prepareStatement("DROP TABLE MutationType");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table MutationType was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table MutationType!");
			e.printStackTrace();
		}
		// finishing message
		log.info("Deletion of all database tables finished!");

	}
	
	// /////////////////////////////////////////////////
	// / fill some property tables
	// /////////////////////////////////////////////////

	public boolean fillLocationOfEffect() {
		log.info("Filling of table LocationOfEffect started!");
		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO LocationOfEffect (effectName) " +
							"VALUES" +
							"('Method-level') , " +
							"('Class-level'), " +
							"('Class spanning'); ");
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}

		// execute the statement
		try {
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}
		log.info("Filling of table LocationOfEffect finished!");
		return true;

	}
	
	
	public boolean fillMutationOperatorCategory() {
		log.info("Filling of table MutationOperatorCategory started!");
		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO MutationOperatorCategory (categoryName) " +
							"VALUES" +
							"('Method-level'), " +
							"('Inheritance'), " +
							"('Polymorphism'), " +
							"('Java-specific features'), " +
							"('Exception handling');");
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}

		// execute the statement
		try {
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}
		log.info("Filling of table MutationOperatorCategory finished!");
		return true;

	}

	public boolean fillMutationType() {
		log.info("Filling of table MutationType started!");
		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO MutationType (categoryName) " +
							"VALUES" +
							"('Insertion'), " +
							"('Deletion'), " +
							"('Replacement');");
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}

		// execute the statement
		try {
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}
		log.info("Filling of table MutationType finished!");
		return true;

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
			stmt.close();
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
			stmt.close();
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
			stmt.close();
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
			stmt.close();
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
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean addLiterature(String title, String authors, String url) {
		// assert parameters to be not null
		assert title != null;
		assert authors != null;
		assert url != null;
		// create a parameterized statement to insert the new data
		PreparedStatement stmt;
		try {
			stmt = connection
					.prepareStatement("INSERT INTO Literature (title, authors, url) VALUES(?, ?, ?)");
			stmt.setString(0, title);
			stmt.setString(1, authors);
			stmt.setString(2, url);
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return false;
		}

		// execute the statement
		try {
			stmt.executeUpdate();
			stmt.close();
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

	public ResultSet getAllPrograms() {
		// create a parameterized statement to insert the new data
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM Program");
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement!");
			e.printStackTrace();
			return null;
		}
		// execute the statement
		ResultSet results;
		try {
			results = stmt.executeQuery();
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;

	}

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
			stmt.close();
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
			stmt.close();
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
			stmt = connection
					.prepareStatement("SELECT * FROM ChangingFiles where ID_bugreport = ?");
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
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ResultSet getAllAppliedMutationOperatorForChangingFiles(
			int ID_changingfiles) {
		// create a parameterized statement to insert the new data
		PreparedStatement stmt = null;
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM AppliedMutationOperator where ID_changingfiles = ?");
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
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ResultSet getAllMutationOperators() {
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
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ResultSet getAllAppliedMutationOperatorForMutationOperator(
			int ID_mutationoperator) {
		// create a parameterized statement to insert the new data
		PreparedStatement stmt = null;
		try {
			stmt = connection
					.prepareStatement("SELECT * FROM AppliedMutationOperator where ID_mutationoperator = ?");
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
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ResultSet getAllLiterature() {
		// create a parameterized statement to insert the new data
		Statement stmt = null;
		String query = "SELECT * FROM Literature";
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
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not execute prepared statement!");
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public static void main(String[] args) {
		DatabaseResults res = new DatabaseResults();
		res.dropTables();
		res.initializeTables();
		res.fillLocationOfEffect();
		res.fillMutationOperatorCategory();
		res.fillMutationType();

	}

}
