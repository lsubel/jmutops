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
		// TABLE NoMatchings
		try {
			stmt = connection.prepareStatement("DROP TABLE NoMatchings");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table NoMatchings was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table NoMatchings!");
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
		// TABLE Errors
		try {
			stmt = connection.prepareStatement("DROP TABLE Errors");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Errors was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table Errors!");
			e.printStackTrace();
		}
		// TABLE Changes
		try {
			stmt = connection.prepareStatement("DROP TABLE Changes");
			stmt.executeUpdate();
			stmt.close();
			log.info("Table Changes was dropped!");
		} catch (SQLException e) {
			log.warning("Could not drop table Changes!");
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
				+ "officalID VARCHAR(255) " + ")");

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
				+ "isMethodChange BOOLEAN, "
				+ "changetype VARCHAR(31), "
				+ "changedEntity TEXT, "
				+ "changedStart INTEGER, "
				+ "changedEnd INTEGER,"
				+ "newEntity TEXT, " 
				+ "newStart INTEGER, " 
				+ "newEnd INTEGER"
				+ ")");
		
		// TABLE Matchings
		initializeTable("Matchings", 
				"CREATE TABLE Matchings ("
				+ "ID_matching SERIAL PRIMARY KEY, "
				+ "ID_change INTEGER NOT NULL, "
				+ "ID_mutationoperator INTEGER NOT NULL, "
				+ "prefixStart INTEGER,"
				+ "prefixEnd INTEGER,"
				+ "prefixText TEXT,"
				+ "postfixStart INTEGER,"
				+ "postfixEnd INTEGER," 
				+ "postfixText TEXT" + ")");

		// TABLE NoMatchings
		initializeTable("NoMatchings", 
				"CREATE TABLE NoMatchings ("
				+ "ID_nomatching SERIAL PRIMARY KEY, "
				+ "ID_change INTEGER NOT NULL,"  
				+ "operators TEXT, "
				+ "numberOfOperators INTEGER NOT NULL" +")");
		
		// TABLE MutationOperator
		initializeTable("MutationOperator",
				"CREATE TABLE MutationOperator ("
				+ "ID_mutationoperator SERIAL PRIMARY KEY, "
				+ "mutationOperatorDescription VARCHAR(1023),"
				+ "mutationOperatorFullname VARCHAR(63),"
				+ "mutationOperatorAbbreviation VARCHAR(7) UNIQUE " + ")");
		
		// TABLE Errors
		initializeTable("Errors", 
				"CREATE TABLE Errors ("
				+ "ID_error SERIAL PRIMARY KEY, "
				+ "ID_program INTEGER NOT NULL,"  
				+ "ID_bugreport INTEGER NOT NULL,"  
				+ "ID_changingfiles INTEGER NOT NULL,"  
				+ "ID_change INTEGER NOT NULL,"  
				+ "location VARCHAR(127),"  
				+ "message TEXT" +")");
		
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
			stmt.setString(1, newProgramName);
			stmt.setString(2, programDescription);
			stmt.setString(3, urlToProjectPage);
			stmt.setString(4, urlToBugtracker);
			stmt.setString(5, newProgramName);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}

		// retrieve the ID_program
		try {
			stmt = this.connection.prepareStatement("SELECT * FROM Program WHERE programName = ?");
			stmt.setString(1, newProgramName);
			resultset = stmt.executeQuery();
			resultset.next();
			this.ID_program = resultset.getInt("ID_program");
			stmt.close();
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
					+ 	"SELECT * FROM Bugreport WHERE (ID_program = ?) and (officalID = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(1, this.ID_program);
			stmt.setString(2, urlToBugreport);
			stmt.setString(3, bugID);
			stmt.setInt(4, this.ID_program);
			stmt.setString(5, bugID);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}
		
		// retrieve the ID_bugreport of the newly inserted entry
		try {
			stmt = this.connection.prepareStatement("SELECT * FROM Bugreport WHERE (ID_program = ?) AND (officalID = ?)");
			stmt.setInt(1, this.ID_program);
			stmt.setString(2, bugID);
			resultset = stmt.executeQuery();
			resultset.next();
			this.ID_bugreport = resultset.getInt("ID_bugreport");
			stmt.close();
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
					+ "SELECT ?, ?, ? " 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * FROM ChangingFiles WHERE (ID_bugreport = ?) AND (prefixedFile = ?) AND (postfixedFile = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(1, this.ID_bugreport);
			stmt.setString(2, prefixedFile.getName());
			stmt.setString(3, postfixedFile.getName());
			stmt.setInt(4, this.ID_bugreport);
			stmt.setString(5, prefixedFile.getName());
			stmt.setString(6, postfixedFile.getName());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}
		
		// retrieve the ID_changingfiles
		try {
			stmt = connection.prepareStatement("SELECT * FROM ChangingFiles WHERE (ID_bugreport = ?) AND (prefixedFile = ?) AND (postfixedFile = ?)");
			stmt.setInt(1, this.ID_bugreport);
			stmt.setString(2, prefixedFile.getName());
			stmt.setString(3, postfixedFile.getName());
			resultset = stmt.executeQuery();
			resultset.next();
			this.ID_changingfiles = resultset.getInt("ID_changingfiles");
			stmt.close();
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
		boolean isMethodChange = change.getChangeType().isBodyChange();
		String changedEntity = "";
		String newEntity = "";
		int changedStart 	= -1;
		int changedEnd		= -1;
		int newStart		= -1;
		int newEnd			= -1;
		
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
					+ "(ID_changingfiles, changetype, changedEntity, newEntity, isMethodChange, changedStart, changedEnd, newStart, newEnd) "
					+ "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ? " 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * FROM Changes WHERE (ID_changingfiles = ?) AND (changetype = ?) AND (changedEntity = ?) AND (newEntity = ?) AND (isMethodChange = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(1, this.ID_changingfiles);
			stmt.setString(2, strType);
			stmt.setString(3, changedEntity);
			stmt.setString(4, newEntity);
			stmt.setBoolean(5, isMethodChange);
			stmt.setInt(6, changedStart);
			stmt.setInt(7, changedEnd);
			stmt.setInt(8, newStart);
			stmt.setInt(9, newEnd);
			stmt.setInt(10, this.ID_changingfiles);
			stmt.setString(11, strType);
			stmt.setString(12, changedEntity);
			stmt.setString(13, newEntity);
			stmt.setBoolean(14, isMethodChange);
			
			stmt.executeUpdate();
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
			stmt.setInt(1, this.ID_changingfiles);
			stmt.setString(2, strType);
			stmt.setString(3, changedEntity);
			stmt.setString(4, newEntity);
			
			resultset = stmt.executeQuery();
			resultset.next();
			this.ID_change = resultset.getInt("ID_change");
			
			stmt.close();
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
						+ "(ID_change, ID_mutationoperator, prefixStart, prefixEnd, prefixText) "
						+ "SELECT (?, ?, ?, ?, ?)" 
						+ "WHERE NOT EXISTS (" 
						+ 	"SELECT * " 
						+	"FROM Matchings "
						+   "WHERE (ID_change = ?) AND (ID_mutationoperator = ?) " 
						+ 	"AND (prefixStart = ?) AND (prefixEnd = ?)"
						+ ")";
				stmt = this.connection.prepareStatement(strSQL);
				stmt.setInt(1, this.ID_change);
				stmt.setInt(2, mapMutopsToDB.get(operator));
				stmt.setInt(3, nodeStart);
				stmt.setInt(4, nodeEnd);
				stmt.setString(5, node.toString());
				stmt.setInt(6, this.ID_change);
				stmt.setInt(7, mapMutopsToDB.get(operator));
				stmt.setInt(8, nodeStart);
				stmt.setInt(9, nodeEnd);
				
				stmt.executeUpdate();
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
				stmt.setInt(1, this.ID_change);
				stmt.setInt(2, mapMutopsToDB.get(operator));
				stmt.setInt(3, nodeStart);
				stmt.setInt(4, nodeEnd);
				stmt.setInt(5, this.ID_change);
				stmt.setInt(6, mapMutopsToDB.get(operator));
				stmt.setInt(7, nodeStart);
				stmt.setInt(8, nodeEnd);
				
				stmt.executeUpdate();
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
					+ "(ID_change, ID_mutationoperator, prefixStart, prefixEnd, prefixText, postfixStart, postfixEnd, postfixText) "
					+ "SELECT ?, ?, ?, ?, ?, ?, ?, ? " 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * " 
					+	"FROM Matchings "
					+   "WHERE (ID_change = ?) AND (ID_mutationoperator = ?) " 
					+ 	"AND (prefixStart = ?) AND (prefixEnd = ?)"
					+ 	"AND (postfixStart = ?) AND (postfixEnd = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(1, this.ID_change);
			stmt.setInt(2, mapMutopsToDB.get(operator));
			stmt.setInt(3, prefixStart);
			stmt.setInt(4, prefixEnd);
			stmt.setString(5, prefix.toString());
			stmt.setInt(6, postfixStart);
			stmt.setInt(7, postfixEnd);
			stmt.setString(8, postfix.toString());
			stmt.setInt(9, this.ID_change);
			stmt.setInt(10, mapMutopsToDB.get(operator));
			stmt.setInt(11, prefixStart);
			stmt.setInt(12, prefixEnd);
			stmt.setInt(13, postfixStart);
			stmt.setInt(14, postfixEnd);
		
			stmt.executeUpdate();
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
		
		// first insert a new entry but only if it does not exist
		try {
			String strSQL = 
				"INSERT INTO MutationOperator " 
					+ "(mutationoperatordescription, mutationoperatorfullname, mutationoperatorabbreviation)" 
					+ "SELECT ?, ?, ? " 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * FROM MutationOperator WHERE mutationOperatorAbbreviation = ?"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setString(1, mutop.getDescription());
			stmt.setString(2, mutop.getFullname());
			stmt.setString(3, mutop.getShortname());
			stmt.setString(4, mutop.getShortname());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing mutation operators!");
			e1.printStackTrace();
			return;
		}

		// retrieve the ID_program
		try {
			stmt = this.connection.prepareStatement("SELECT * FROM MutationOperator WHERE mutationOperatorAbbreviation = ?");
			stmt.setString(1, mutop.getShortname());
			resultset = stmt.executeQuery();
			resultset.next();
			int ID_mutationoperator = resultset.getInt("id_mutationoperator");
			this.mapMutopsToDB.put(mutop, ID_mutationoperator);
			stmt.close();
		} catch (SQLException e) {
			log.warning("Could not retrieve id_mutationoperator from MutationOperator!");
			e.printStackTrace();
			return;
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
			String listText = "";
			for(MutationOperator mutop: operatorlist) {
				if(listText != "") {
					listText += ", ";
				}
				listText += mutop.getShortname();
			}
			
			String strSQL = 
				"INSERT INTO NoMatchings " 
					+ "(ID_change, numberOfOperators, operators) "
					+ "SELECT ?, ?, ?" 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * " 
					+	"FROM NoMatchings "
					+   "WHERE (ID_change = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(1, this.ID_change);
			stmt.setInt(2, operatorlist.size());
			stmt.setString(3, listText);
			stmt.setInt(4, this.ID_change);
		
			stmt.executeUpdate();
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
					+ "SELECT ?, ?, ?, ?, ?, ? " 
					+ "WHERE NOT EXISTS (" 
					+ 	"SELECT * " 
					+	"FROM Errors "
					+   "WHERE (ID_program = ?) " 
					+ 	"AND (ID_bugreport = ?) AND (ID_changingfiles = ?) "
					+   "AND (ID_change = ?) AND (location = ?) "
					+ 	"AND (message = ?)"
					+ ")";
			stmt = this.connection.prepareStatement(strSQL);
			stmt.setInt(1, this.ID_program);
			stmt.setInt(2, this.ID_bugreport);
			stmt.setInt(3, this.ID_changingfiles);
			stmt.setInt(4, this.ID_change);
			stmt.setString(5, location);
			stmt.setString(6, errorMessage);
			stmt.setInt(7, this.ID_program);
			stmt.setInt(8, this.ID_bugreport);
			stmt.setInt(9, this.ID_changingfiles);
			stmt.setInt(10, this.ID_change);
			stmt.setString(11, location);
			stmt.setString(12, errorMessage);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e1) {
			log.warning("Could not create and fill prepared statement to detect existing program!");
			e1.printStackTrace();
			return;
		}					
	}

	@Override
	public void OnFileCheckFinished() {
		this.ID_changingfiles = -1;
		this.ID_change		  = -1;
	}

	public static void main(String[] args) {
		// drop all tables
		ResultDatabase rd = new ResultDatabase("jdbc:postgresql://localhost:5432/jmutops_results", "postgres", "asteria");
		rd.dropTables();
		rd.initializeTables();
		System.out.println("Done");
	}
}

