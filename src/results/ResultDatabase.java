package results;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;

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
	private int ID_change			= -1;	
	
	boolean isPrefix;
	
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
		
		Properties props = new Properties();
		props.setProperty("user", db_username);
		props.setProperty("password", db_password);
		props.setProperty("ssl", "false");
		
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

	// /////////////////////////////////////////////////
	// / delete tables
	// /////////////////////////////////////////////////

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

		// TABLE Changes
		initializeTable("Changes", 
				"CREATE TABLE Changes ("
				+ "ID_change SERIAL PRIMARY KEY, "
				+ "ID_changingfiles INTEGER NOT NULL, "
				+ "changetype VARCHAR(31), "
				+ "changedEntity VARCHAR(511), "  
				+ "newEntity VARCHAR(511) " + ")");
		
		// TABLE Matchings
		initializeTable("Matchings", 
				"CREATE TABLE Matchings ("
				+ "ID_matching SERIAL PRIMARY KEY, "
				+ "ID_change INTEGER NOT NULL, "
				+ "ID_mutationoperator INTEGER NOT NULL, "
				+ "prefixStart INTEGER,"
				+ "prefixEnd INTEGER,"
				+ "postfixStart INTEGER,"
				+ "postfixEnd INTEGER" + ")");

		// TABLE NoMatchings
		initializeTable("NoMatchings", 
				"CREATE TABLE NoMatchings ("
				+ "ID_nomatching SERIAL PRIMARY KEY, "
				+ "ID_change INTEGER NOT NULL,"  
				+ "numberOfOperators INTEGER NOT NULL" +")");
		
		// TABLE MutationOperator
		initializeTable("MutationOperator",
				"CREATE TABLE MutationOperator ("
				+ "ID_mutationoperator SERIAL PRIMARY KEY, "
				+ "mutationOperatorDescription VARCHAR(1023),"
				+ "mutationOperatorFullname VARCHAR(63),"
				+ "mutationOperatorAbbreviation VARCHAR(7)" + ")");
		
		// TABLE Errors
		initializeTable("Errors", 
				"CREATE TABLE Errors ("
				+ "ID_error SERIAL PRIMARY KEY, "
				+ "ID_program INTEGER NOT NULL,"  
				+ "ID_bugreport INTEGER NOT NULL,"  
				+ "ID_changingfiles INTEGER NOT NULL,"  
				+ "ID_change INTEGER NOT NULL,"  
				+ "location VARCHAR(127),"  
				+ "message VARCHAR(1023)" +")");
		
		// finished Message
		log.info("Initialization of database tables finished!");
		return true;

	}
	
	///////////////////////////////////////////////////
	/// Generate events for corresponding events
	///////////////////////////////////////////////////

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
		
		// first insert a new entry but only if it does not exist
		try {
			String strSQL = 
				"INSERT INTO Program " 
					+ "(programName, programDescription, utrlToProjectPage, urlToBugtracker)" 
					+ "SELECT ?, ?, ?, ? " 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * FROM Program WHERE programName = ?"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setString(0, newProgramName);
			stmt.setString(1, programDescription);
			stmt.setString(2, urlToProjectPage);
			stmt.setString(3, urlToBugtracker);
			stmt.setString(4, newProgramName);
			resultset = stmt.executeQuery();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}

		// retrieve the ID_program
		try {
			stmt = this.connection.prepareStatement("SELECT * FROM Program WHERE programName = ?");
			stmt.setString(0, newProgramName);
			resultset = stmt.executeQuery();
			stmt.close();
			resultset.first();
			this.ID_program = resultset.getInt("ID_program");
		} catch (SQLException e) {
			log.warning("Could not retrieve ID_program from Program!");
			e.printStackTrace();
			return;
		}
		
	}
	
	
	@Override
	public void OnBugChanged(String bugID, String urlToBugreport) {
		// assert parameters to be not null
		assert urlToBugreport != null;

		// initialize variables
		PreparedStatement stmt = null;
		ResultSet resultset = null;
		
		// first insert a new entry but only if it does not exist
		try {
			String strSQL = 
				"INSERT INTO Bugreport " 
					+ "(ID_program, urlToBugreport, officalID)" 
					+ "SELECT ?, ?, ? " 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * FROM Bugreport WHERE (ID_program = ?) and (officialID = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(0, this.ID_program);
			stmt.setString(1, urlToBugreport);
			stmt.setString(2, bugID);
			stmt.setInt(3, this.ID_program);
			stmt.setString(4, bugID);
			resultset = stmt.executeQuery();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
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
			log.warning("Could not retrieve ID_bugreport from Bugreport!");
			e.printStackTrace();
			return;
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
		
		// first insert a new entry but only if it does not exist
		try {
			String strSQL = 
				"INSERT INTO ChangingFiles " 
					+ "(ID_bugreport, prefixedFile, postfixedFile) "
					+ "SELECT (?, ?, ?)" 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * FROM ChangingFiles WHERE (ID_bugreport = ?) AND (prefixedFile = ?) AND (postfixedFile = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(0, this.ID_bugreport);
			stmt.setString(1, prefixedFile.getName());
			stmt.setString(2, postfixedFile.getName());
			stmt.setInt(3, this.ID_bugreport);
			stmt.setString(4, prefixedFile.getName());
			stmt.setString(5, postfixedFile.getName());
			resultset = stmt.executeQuery();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}
		
		// retrieve the ID_changingfiles
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
			log.warning("Could not retrieve ID_changingfiles from ChangingFiles!");
			e.printStackTrace();
			return;
		}
	}
	
	
	@Override
	public void OnChangeChecked(SourceCodeChange change) {
		// assert parameters to be not null
		assert change != null;

		// initialize variables
		PreparedStatement stmt = null;
		ResultSet resultset = null;
		
		// extract information out of the SourceCodeChange
		String strType = change.getChangeType().toString();
		String changedEntity = "";
		String newEntity = "";
		if(change instanceof Insert) {
			isPrefix	  = false;
			Insert insert = (Insert) change;
			changedEntity = insert.getChangedEntity().toString();
		}
		else if(change instanceof Delete) {
			isPrefix 	  = true;
			Delete delete = (Delete) change;
			changedEntity = delete.getChangedEntity().toString();
		}
		else if(change instanceof Update) {
			Update update = (Update) change;
			changedEntity = update.getChangedEntity().toString();
			newEntity     = update.getNewEntity().toString();
		}
		else if(change instanceof Move) {
			Move move = (Move) change;
			changedEntity = move.getChangedEntity().toString();
			newEntity     = move.getNewEntity().toString();
		};
		
		// first insert a new entry but only if it does not exist
		try {
			String strSQL = 
				"INSERT INTO Changes " 
					+ "(ID_changingfiles, changetype, changedEntity, newEntity) "
					+ "SELECT (?, ?, ?, ?)" 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * FROM Changes WHERE (ID_changingfiles = ?) AND (changetype = ?) AND (changedEntity = ?) AND (newEntity = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(0, this.ID_changingfiles);
			stmt.setString(1, strType);
			stmt.setString(2, changedEntity);
			stmt.setString(3, newEntity);
			stmt.setInt(4, this.ID_changingfiles);
			stmt.setString(5, strType);
			stmt.setString(6, changedEntity);
			stmt.setString(7, newEntity);
			
			resultset = stmt.executeQuery();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}

		/// retrieve the ID_change from Changes
		try {
			String strSQL = 
				"SELECT * " +
				"FROM Changes " +
				"WHERE (ID_changingfiles = ?) AND (changetype = ?) AND (changedEntity = ?) AND (newEntity = ?)"; 
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(0, this.ID_changingfiles);
			stmt.setString(1, strType);
			stmt.setString(2, changedEntity);
			stmt.setString(3, newEntity);
			
			resultset = stmt.executeQuery();
			stmt.close();
			resultset.first();
			this.ID_change = resultset.getInt("ID_change");
		} catch (SQLException e1) {
			log.warning("Could not retrieve ID_change from Changes!");
			e1.printStackTrace();
			return;
		}		
	}	

	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode node) {
		// check of non null arguments
		assert operator != null;
		assert node != null;
		
		// initialize variables
		PreparedStatement stmt = null;
		
		int nodeStart = node.getStartPosition();
		int nodeEnd = node.getStartPosition() + node.getLength() - 1;

		if(this.isPrefix) {
			// insert a new entry but only if it does not exist
			try {
				String strSQL = 
					"INSERT INTO Matchings " 
						+ "(ID_change, ID_mutationoperator, prefixStart, prefixEnd) "
						+ "SELECT (?, ?, ?, ?, ?, ?)" 
						+ "WHERE NOT EXISTS (" 
						+ 	"SELECT * " 
						+	"FROM Matchings "
						+   "WHERE (ID_change = ?) AND (ID_mutationoperator = ?) " 
						+ 	"AND (prefixStart = ?) AND (prefixEnd = ?)"
						+ ")";
				stmt = this.connection.prepareStatement(strSQL);
				stmt.setInt(0, this.ID_change);
				stmt.setInt(1, mapMutopsToDB.get(operator));
				stmt.setInt(2, nodeStart);
				stmt.setInt(3, nodeEnd);
				stmt.setInt(4, this.ID_change);
				stmt.setInt(5, mapMutopsToDB.get(operator));
				stmt.setInt(6, nodeStart);
				stmt.setInt(7, nodeEnd);
				
				stmt.executeQuery();
				stmt.close();
			} catch (SQLException e1) {
				log.warning("Could not create and fill prepared statement to detect existing program!");
				e1.printStackTrace();
				return;
			}
		}
		else {
			// insert a new entry but only if it does not exist
			try {
				String strSQL = 
					"INSERT INTO Matchings " 
						+ "(ID_change, ID_mutationoperator, postfixStart, postfixEnd) "
						+ "SELECT (?, ?, ?, ?, ?, ?)" 
						+ "WHERE NOT EXISTS (" 
						+ 	"SELECT * " 
						+	"FROM Matchings "
						+   "WHERE (ID_change = ?) AND (ID_mutationoperator = ?) " 
						+ 	"AND (postfixStart = ?) AND (postfixEnd = ?)"
						+ ")";
				stmt = this.connection.prepareStatement(strSQL);
				stmt.setInt(0, this.ID_change);
				stmt.setInt(1, mapMutopsToDB.get(operator));
				stmt.setInt(2, nodeStart);
				stmt.setInt(3, nodeEnd);
				stmt.setInt(4, this.ID_change);
				stmt.setInt(5, mapMutopsToDB.get(operator));
				stmt.setInt(6, nodeStart);
				stmt.setInt(7, nodeEnd);
				
				stmt.executeQuery();
				stmt.close();
			} catch (SQLException e1) {
				log.warning("Could not create and fill prepared statement to detect existing program!");
				e1.printStackTrace();
				return;
			}
		}
	}

	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		// check of non null arguments
		assert operator != null;
		assert prefix != null;
		assert postfix != null;
		
		// initialize variables
		PreparedStatement stmt = null;

		int prefixStart = prefix.getStartPosition();
		int prefixEnd = prefix.getStartPosition() + prefix.getLength() - 1;
		int postfixStart = postfix.getStartPosition();
		int postfixEnd = postfix.getStartPosition() + postfix.getLength() - 1;
		
		// insert a new entry but only if it does not exist
		try {
			String strSQL = 
				"INSERT INTO Matchings " 
					+ "(ID_change, ID_mutationoperator, prefixStart, prefixEnd, postfixStart, postfixEnd) "
					+ "SELECT (?, ?, ?, ?, ?, ?)" 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * " 
					+	"FROM Matchings "
					+   "WHERE (ID_change = ?) AND (ID_mutationoperator = ?) " 
					+ 	"AND (prefixStart = ?) AND (prefixEnd = ?)"
					+ 	"AND (postfixStart = ?) AND (postfixEnd = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(0, this.ID_change);
			stmt.setInt(1, mapMutopsToDB.get(operator));
			stmt.setInt(2, prefixStart);
			stmt.setInt(3, prefixEnd);
			stmt.setInt(4, postfixStart);
			stmt.setInt(5, postfixEnd);
			stmt.setInt(7, this.ID_change);
			stmt.setInt(8, mapMutopsToDB.get(operator));
			stmt.setInt(9, prefixStart);
			stmt.setInt(10, prefixEnd);
			stmt.setInt(11, postfixStart);
			stmt.setInt(12, postfixEnd);
		
			stmt.executeQuery();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
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
		// check of non null arguments
		assert operatorlist != null;
		
		// initialize variables
		PreparedStatement stmt = null;
		
		// insert a new entry but only if it does not exist
		try {
			String strSQL = 
				"INSERT INTO NoMatchings " 
					+ "(ID_change) "
					+ "SELECT (?, ?, ?, ?, ?, ?)" 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * " 
					+	"FROM NoMatchings "
					+   "WHERE (ID_change = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(0, this.ID_change);
			stmt.setInt(1, this.ID_change);
		
			stmt.executeQuery();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}	
	}

	@Override
	public void OnCreatingResult() {
	}

	@Override
	public void OnErrorDetected(String location, String errorMessage) {
		// check of non null arguments
		assert location != null;
		assert errorMessage != null;
		
		// initialize variables
		PreparedStatement stmt = null;
		
		// insert a new entry but only if it does not exist
		try {
			String strSQL = 
				"INSERT INTO Errors " 
					+ "(ID_program, ID_bugreport, ID_changingfiles, ID_change, location, message) "
					+ "SELECT (?, ?, ?, ?, ?, ?, 9)" 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * " 
					+	"FROM Errors "
					+   "WHERE (ID_program = ?) " 
					+ 	"AND (ID_bugreport = ?) AND (ID_changingfiles = ?) "
					+   "AND (ID_change = ?) AND (location = ?) "
					+ 	"AND (message = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(0, this.ID_program);
			stmt.setInt(1, this.ID_bugreport);
			stmt.setInt(2, this.ID_changingfiles);
			stmt.setInt(3, this.ID_change);
			stmt.setString(4, location);
			stmt.setString(5, errorMessage);
			stmt.setInt(6, this.ID_program);
			stmt.setInt(7, this.ID_bugreport);
			stmt.setInt(8, this.ID_changingfiles);
			stmt.setInt(9, this.ID_change);
			stmt.setString(10, location);
			stmt.setString(11, errorMessage);
			
			stmt.executeQuery();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}					
	}

	@Override
	public void OnFileCheckFinished() {
	}

	
}

