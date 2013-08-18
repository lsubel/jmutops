package results;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

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
public class ResultDatabase implements JMutOpsEventListener {

	//////////////////////////////////////////
	/// Fields
	//////////////////////////////////////////
	
	/**
	 * Connection values for the database
	 */
	private final String db_address; //  = "jdbc:postgresql://avanti.st.cs.uni-saarland.de/jmutops";
	private final String db_username; // = "jmutops";
	private final String db_password; // = "jmutops";

	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(ResultDatabase.class
			.getName());

	/**
	 * Field which stores the connection object after initialization
	 */
	private Connection connection;

	/**
	 * Field which stores the current ID of the checked program.
	 */
	private int ID_program 			= -1;
	private int ID_bugreport 		= -1;
	private int ID_changingfiles 	= -1;
	
	/**
	 * HashMap which maps MutationOperators to corresponding ID_mutationoperator in the DB
	 */
	HashMap<MutationOperator, Integer> mapMutopsToDB = new HashMap<MutationOperator, Integer>();
	
	/**
	 * HashMap which maps MutationType to corresponding ID_mutationtype in the DB
	 */
	HashMap<MutationOperator, Integer> mapMutTypeToDB = new HashMap<MutationOperator, Integer>();	
	
	/**
	 * Initialize the database object
	 */
	public ResultDatabase(String address, String user, String pw) {
		// store the results
		this.db_address  = address;
		this.db_username = user;
		this.db_password = pw;
		
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
			connection = DriverManager.getConnection(db_address, db_username,
					db_password);
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
		initializeTable("Program", 
				"CREATE TABLE Program ("
				+ "ID_program SERIAL PRIMARY KEY, "
				+ "programName VARCHAR(255) NOT NULL, "
				+ "programDescription VARCHAR(1023), "
				+ "utrlToProjectPage VARCHAR(255), "
				+ "urlToBugtracker VARCHAR(255) " + ")");

		// TABLE Bugreport
		initializeTable("Bugreport", 
				"CREATE TABLE Bugreport ("
				+ "ID_bugreport SERIAL PRIMARY KEY, "
				+ "ID_program INTEGER NOT NULL, "
				+ "urlToBugreport VARCHAR(511), "
				+ "officalID INTEGER " + ")");

		// TABLE ChangingFiles
		initializeTable("ChangingFiles", 
				"CREATE TABLE ChangingFiles ("
				+ "ID_changingfiles SERIAL PRIMARY KEY, "
				+ "ID_bugreport INTEGER NOT NULL, "
				+ "prefixedFile VARCHAR(511), "
				+ "postfixedFile VARCHAR(511)" + ")");

		// TABLE Matchings
		initializeTable("Matchings", 
				"CREATE TABLE Matchings ("
				+ "ID_matching SERIAL PRIMARY KEY, "
				+ "ID_changingfiles INTEGER NOT NULL, "
				+ "ID_mutationoperator INTEGER NOT NULL, "
				+ "prefixStart INTEGER,"
				+ "prefixEnd INTEGER,"
				+ "postfixStart INTEGER,"
				+ "postfixEnd INTEGER" + ")");

		// TABLE MutationOperator
		initializeTable("MutationOperator",
				"CREATE TABLE MutationOperator ("
				+ "ID_mutationoperator SERIAL PRIMARY KEY, "
				+ "mutationOperatorDescription VARCHAR(1023),"
				+ "mutationOperatorFullname VARCHAR(63),"
				+ "mutationOperatorAbbreviation VARCHAR(7)" + ")");
		
		// finished Message
		log.info("Initialization of database tables finished!");
		return true;

	}
	
	private boolean initializeTable(String tableName, String creatingStatement){
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(creatingStatement);
			stmt.executeUpdate();
			stmt.close();
			log.info("Table " + tableName + " was created!");
		} catch (SQLException e) {
			log.warning("Could not create table " + tableName + "!");
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

		// TABLE Matchings
		try {
			stmt = connection
					.prepareStatement("DROP TABLE Matchings");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Matchings was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table Matchings!");
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
		// finishing message
		log.info("Deletion of all database tables finished!");

	}
	
	///////////////////////////////////////////////////
	/// add new entries
	///////////////////////////////////////////////////

	@Override
	// TODO: maybe smarter; always insert and only allow one combination
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		// check of non null arguments
		assert operator != null;
		assert prefix != null;
		assert postfix != null;
		
		// initialize variables
		PreparedStatement stmt = null;
		ResultSet resultset = null;
		int rowcount = 0;
		int prefixStart = prefix.getStartPosition();
		int prefixEnd = prefix.getStartPosition() + prefix.getLength() - 1;
		int postfixStart = postfix.getStartPosition();
		int postfixEnd = postfix.getStartPosition() + postfix.getLength() - 1;
		
		// check if there exists an application at this are for the specific operatorat these locations
		try {
			stmt = this.connection.prepareStatement("SELECT * FROM Matchings WHERE (ID_changingfiles = ?) AND (ID_mutationoperator = ?) AND (prefixStart = ?) AND (prefixEnd = ?) AND (postfixStart = ?) AND (postfixEnd = ?)");
			stmt.setInt(0, this.ID_changingfiles);
			stmt.setInt(1, mapMutopsToDB.get(operator));
			stmt.setInt(2, prefixStart);
			stmt.setInt(3, prefixEnd);
			stmt.setInt(4, postfixStart);
			stmt.setInt(5, postfixEnd);
			resultset = stmt.executeQuery();
			stmt.close();
			resultset.last();
			rowcount = resultset.getRow();
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement to detect existing matching!");
			e.printStackTrace();
			return;
		}
		
		// check if there exists not an corresponding entry
		if(rowcount == 0){
			// generate a new entry
			try {
				stmt = this.connection.prepareStatement("INSERT INTO Matchings (ID_changingfiles, ID_mutationoperator, prefixStart, prefixEnd, postfixStart, postfixEnd) VALUES (?, ?, ?, ?, ?, ?)");
				stmt.setInt(0, this.ID_changingfiles);
				stmt.setInt(1, mapMutopsToDB.get(operator));
				stmt.setInt(2, prefixStart);
				stmt.setInt(3, prefixEnd);
				stmt.setInt(4, postfixStart);
				stmt.setInt(5, postfixEnd);
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				log.warning("Could not create, fill and execute prepared statement to add entry for matching!");
				e.printStackTrace();
				return;
			}	
		}
	}
		
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode node) {
		// TODO Auto-generated method stub
	}
	
		
	@Override
	public void OnBugChanged(String bugID, String urlToBugreport) {
		// assert parameters to be not null
		assert urlToBugreport != null;

		// initialize variables
		PreparedStatement stmt = null;
		ResultSet resultset = null;
		int rowcount = 0;		
		
		// first check if there exists an entry in table Bugreport for the 
		
		try {
			stmt = this.connection.prepareStatement("SELECT * FROM Bugreport WHERE (ID_program = ?) AND (officialID = ?)");
			stmt.setInt(0, this.ID_program);
			stmt.setString(1, bugID);
			resultset = stmt.executeQuery();
			stmt.close();
			resultset.last();
			rowcount = resultset.getRow();
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement to detect existing bugreport!");
			e.printStackTrace();
			return;
		}
		
		// check if there is an existing entry
		if((rowcount > 0) && (rowcount == 1)){
			// retrieve the corresponding ID
			try {
				resultset.first();
				this.ID_bugreport = resultset.getInt("ID_bugreport");
			} catch (SQLException e) {
				log.warning("Could not retrieve ID_bugreport of detected bugreport!");
				e.printStackTrace();
				return;
			}
		}
		// otherwise
		else{
			// generate a new entry
			try {
				stmt = connection.prepareStatement("INSERT INTO Bugreport (ID_program, officialID, urlToBugreport) VALUES (?, ?, ?)");
				stmt.setInt(0, this.ID_program);
				stmt.setString(1, bugID);
				stmt.setString(2, urlToBugreport);
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				log.warning("Could not create and fill prepared statement!");
				e.printStackTrace();
				return;
			}
			
			// retrieve the ID_bugreport of the newly inserted entry
			try {
				stmt = this.connection.prepareStatement("SELECT * FROM Bugreport WHERE (ID_program = ?) AND (officialID = ?)");
				stmt.setInt(0, this.ID_program);
				stmt.setString(1, bugID);
				resultset = stmt.executeQuery();
				stmt.close();
				resultset.first();
				this.ID_bugreport = resultset.getInt("ID_bugreport");
			} catch (SQLException e) {
				log.warning("Could not retrieve ID_bugreport of newly inserted program!");
				e.printStackTrace();
				return;
			}
		}
		
		
	}

	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {
		// assert parameters to be not null
		assert prefixedFile != null;
		assert postfixedFile != null;

		// initialize variables
		PreparedStatement stmt = null;
		ResultSet resultset = null;
		int rowcount = 0;
		
		// first check if there exists an entry in table ChangingFiles for the specific checked file combination
		try {
			stmt = connection.prepareStatement("SELECT * FROM ChangingFiles WHERE (ID_bugreport = ?) AND (prefixedFile = ?) AND (postfixedFile = ?)");
			stmt.setInt(0, this.ID_bugreport);
			stmt.setString(1, prefixedFile.getName());
			stmt.setString(2, postfixedFile.getName());
			resultset = stmt.executeQuery();
			stmt.close();
			resultset.last();
			rowcount = resultset.getRow();
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement to detect existing checked files!");
			e.printStackTrace();
			return;
		}
		
		// if there is an existing entry
		if((rowcount > 0) && (rowcount == 1)){
			// retrieve the corresponding ID
			try {
				resultset.first();
				this.ID_changingfiles = resultset.getInt("ID_changingfiles");
			} catch (SQLException e) {
				log.warning("Could not retrieve ID_program of detected program!");
				e.printStackTrace();
				return;
			}
		}
		// otherwise
		else{
			// generate a new entry
			try {
				stmt = connection.prepareStatement("INSERT INTO ChangingFiles (ID_bugreport, prefixedFile, postfixedFile) VALUES(?, ?, ?)");
				stmt.setInt(0, ID_bugreport);
				stmt.setString(1, prefixedFile.getName());
				stmt.setString(2, postfixedFile.getName());
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				log.warning("Could not create and fill prepared statement to insert a new checked file combination!");
				e.printStackTrace();
				return;
			}
			
			// retrieve the ID_changingfiles of the newly inserted entry
			try {
				stmt = connection.prepareStatement("SELECT * FROM ChangingFiles WHERE (ID_bugreport = ?) AND (prefixedFile = ?) AND (postfixedFile = ?)");
				stmt.setInt(0, this.ID_bugreport);
				stmt.setString(1, prefixedFile.getName());
				stmt.setString(2, postfixedFile.getName());
				resultset = stmt.executeQuery();
				stmt.close();
				resultset.first();
				this.ID_changingfiles = resultset.getInt("ID_changingfiles");
			} catch (SQLException e) {
				log.warning("Could not retrieve ID_changingfiles of newly inserted program!");
				e.printStackTrace();
				return;
			}
		}
		
		
	}

	@Override
	public void OnFileCheckFinished() {
	}

	@Override
	public void OnCreatingResult() {
	}

	@Override
	public void OnErrorDetected(String location, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnChangeChecked(SourceCodeChange change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnProgramChanged(String newProgramName,
			String programDescription, String urlToProjectPage,
			String urlToBugtracker) {
		// assert parameters to be not null
		assert newProgramName != null;
		assert programDescription != null;
		assert urlToProjectPage != null;
		assert urlToBugtracker != null;

		// initialize variables
		PreparedStatement stmt = null;
		ResultSet resultset = null;
		int rowcount = 0;
		
		// first check if there exists an entry in table Program for the specific program
		try {
			stmt = this.connection.prepareStatement("SELECT * FROM Program WHERE programName = ?");
			stmt.setString(0, newProgramName);
			resultset = stmt.executeQuery();
			stmt.close();
			resultset.last();
			rowcount = resultset.getRow();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}

		// if there is an existing entry
		if((rowcount > 0) && (rowcount == 1)){
			// retrieve and store the current ID
			try {
				resultset.first();
				this.ID_program = resultset.getInt("ID_program");
			} catch (SQLException e) {
				log.warning("Could not retrieve ID_program of detected program!");
				e.printStackTrace();
				return;
			}
		}
		// otherwise
		else{
			// create a parameterized statement to insert the new data
			try {
				stmt = connection.prepareStatement("INSERT INTO Program (programName, programDescription, utrlToProjectPage, urlToBugtracker) VALUES(?, ?, ?, ?)");
				stmt.setString(0, newProgramName);
				stmt.setString(1, programDescription);
				stmt.setString(2, urlToProjectPage);
				stmt.setString(3, urlToBugtracker);
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				log.warning("Could not create and fill prepared statement to insert a new program!");
				e.printStackTrace();
				return;
			}
			
			// retrieve the ID_program of the newly inserted entry
			try {
				stmt = this.connection.prepareStatement("SELECT * FROM Program WHERE programName = ?");
				stmt.setString(0, newProgramName);
				resultset = stmt.executeQuery();
				stmt.close();
				resultset.first();
				this.ID_program = resultset.getInt("ID_program");
			} catch (SQLException e) {
				log.warning("Could not retrieve ID_program of newly inserted program!");
				e.printStackTrace();
				return;
			}
		}
	}
	
	@Override
	public void OnMutationOperatorInit(MutationOperator mutop) {
		// assert parameters to be not null
		assert mutop != null;

		// initialize variables
		PreparedStatement stmt = null;
		ResultSet resultset = null;
		int rowcount = 0;
		
		// first check if there exists the corresponding mutation operator in table MutationOperator
		try {
			stmt = connection.prepareStatement("SELECT * FROM MutationOperator WHERE (mutationOperatorAbbreviation = ?)");
			stmt.setString(0, mutop.getClass().toString());
			resultset = stmt.executeQuery();
			stmt.close();
			resultset.last();
			rowcount = resultset.getRow();
		} catch (SQLException e) {
			log.warning("Could not create and fill prepared statement to detect existing mutation operator!");
			e.printStackTrace();
			return;
		}
		
		// if there exists an entry
		if((rowcount > 0) && (rowcount == 1)){
			// retrieve and store the corresponding ID
			try {
				resultset.first();
				int ID_mutationoperator = resultset.getInt("ID_mutationoperator");
				this.mapMutopsToDB.put(mutop, ID_mutationoperator);
			} catch (SQLException e) {
				log.warning("Could not retrieve ID_mutationoperator of detected mutation operator!");
				e.printStackTrace();
				return;
			}
		}
		// otherwise
		else{
			// create a parameterized statement to insert the new data
			try {
				stmt = connection.prepareStatement("INSERT INTO MutationOperator (mutationOperatorDescription, mutationOperatorFullname, mutationOperatorAbbreviation) VALUES(?, ?, ?)");
				stmt.setString(0, mutop.getDescription());
				stmt.setString(1, mutop.getFullname());
				stmt.setString(2, mutop.getShortname());
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				log.warning("Could not create and fill prepared statement to insert a new mutation operator!");
				e.printStackTrace();
				return;
			}
			
			// retrieve the ID_mutationoperator of the newly inserted entry
			try {
				stmt = connection.prepareStatement("SELECT * FROM MutationOperator WHERE (mutationOperatorAbbreviation = ?)");
				stmt.setString(0, mutop.getClass().toString());
				resultset = stmt.executeQuery();
				stmt.close();
				resultset.first();
				this.mapMutopsToDB.put(mutop, resultset.getInt("ID_mutationoperator"));
			} catch (SQLException e) {
				log.warning("Could not create and fill prepared statement to detect existing mutation operator!");
				e.printStackTrace();
				return;
			}
		}
	}

	@Override
	public void OnNoMatchingFound(List<MutationOperator> operatorlist) {
		// TODO: create a table which stores the changes with no matching operator
	}
}
