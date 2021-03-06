package results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * @author Lukas Subel
 *
 */
public class ResultCreator implements JMutOpsEventListener{

	//////////////////////////////////////////
	///	Fields
	//////////////////////////////////////////
	
	private ArrayList<Dictionary<String, String>> results = new ArrayList<Dictionary<String, String>>();; 
	
	private ArrayList<String> error_location = new ArrayList<String>();
	private ArrayList<String> error_message = new ArrayList<String>();
	
	/**
	 * Counts the number of undetected matches.
	 */
	private int undetected_matches = 0;
	
	/**
	 * Temporary field.
	 */
	private File prefix;
	
	/**
	 * Temporary field.
	 */
	private File postfix;
	
	private final boolean shouldOutputFile;
	
	private final boolean shouldOutputConsole;
	
	private final String output_path;
	
	//////////////////////////////////////////
	///	Constructor
	//////////////////////////////////////////

	public ResultCreator() {
		this.shouldOutputConsole = false;
		this.shouldOutputFile = true;
		this.output_path = null;
	}
	
	public ResultCreator(boolean console, boolean file, String path) {
		this.shouldOutputConsole 	= console;
		this.shouldOutputFile 		= file;
		this.output_path 			= path;
	}
	
	//////////////////////////////////////////
	///	Methods
	//////////////////////////////////////////

	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		// generate an entry for the matching
		Dictionary<String, String> entry = new Hashtable<String, String>();
		
		entry.put("operator", operator.getShortname());
		entry.put("prefix-content", prefix.toString());
		entry.put("prefix-node", prefix.getClass().toString());
		entry.put("prefix-range", prefix.getStartPosition() + "-" + (prefix.getStartPosition() + prefix.getLength() - 1));
		entry.put("prefix-file", this.prefix.getAbsolutePath());
		entry.put("postfix-content", postfix.toString());
		entry.put("postfix-node", postfix.getClass().toString());
		entry.put("postfix-range", postfix.getStartPosition() + "-" + (postfix.getStartPosition() + postfix.getLength() - 1));		
		entry.put("postfix-file", this.postfix.getAbsolutePath());
		this.results.add(entry);
	}
	
	@Override
	public void OnCreatingResult() {
		// initialize a object which stores the result
		StringBuffer result = new StringBuffer();
		
		// add the result output
		result.append("///////////////////////////////////////////////////////" + "\n");
		result.append("						Results							 " + "\n");
		result.append("///////////////////////////////////////////////////////" + "\n");
		result.append("\n");
		for(Dictionary<String, String> entry: this.results){
			result.append("Found application of " + entry.get("operator") + " operator:" + "\n");
			result.append("\t" + "Prefix version: " + "\n");
			result.append("\t\t" + "Content: " + entry.get("prefix-content")  + "\n");
			result.append("\t\t" + "Node type: " + entry.get("prefix-node") + "\n");
			result.append("\t\t" + "Range: " + entry.get("prefix-range") + "\n");
			result.append("\t\t" + "File: " + entry.get("prefix-file") + "\n");
			result.append("\t" + "Postfix version: " + "\n");
			result.append("\t\t" + "Content: " + entry.get("postfix-content") + "\n");
			result.append("\t\t" + "Node type: " + entry.get("postfix-node") + "\n");
			result.append("\t\t" + "Range: " + entry.get("postfix-range") + "\n");
			result.append("\t\t" + "File: " + entry.get("postfix-file") + "\n");
			result.append("\n");
		}
		result.append("Number of changes with no matching operators: " + undetected_matches + "\n");
		result.append("\n");
		result.append("///////////////////////////////////////////////////////" + "\n");
		result.append("						End of results					 " + "\n");
		result.append("///////////////////////////////////////////////////////" + "\n");	
		
		
		// add error output
		result.append("///////////////////////////////////////////////////////" + "\n");
		result.append("						Errors					 " + "\n");
		result.append("///////////////////////////////////////////////////////" + "\n");
		result.append("\n");
		for(int i = 0; i < (this.error_location.size() - 1); i++) {
			result.append("\t" + "Location: " + this.error_location.get(i) + "\n");
			result.append("\t" + "Message: " + this.error_message.get(i) + "\n");
			result.append("\n");
		}
		
		result.append("///////////////////////////////////////////////////////" + "\n");
		result.append("						End of errors					 " + "\n");
		result.append("///////////////////////////////////////////////////////" + "\n");
		
		
		
		// check if they should output the result on console
		if(this.shouldOutputConsole) {
			System.out.println(result.toString());
		}
		// check if they should output the result in a file
		if(this.shouldOutputFile) {
			File resultingFile = null;
			BufferedWriter bw = null;
			try {
				String path = this.output_path;
				resultingFile = new File(path);				
				if(!resultingFile.exists()) {
					resultingFile.createNewFile();
				}
				bw = new BufferedWriter(new FileWriter(resultingFile));
			} catch (IOException e) {
				System.out.println("ResultCreator - Could not create output file: " + e.getMessage());
				System.exit(0);
			}
			try {
				bw.write(result.toString());
				bw.close();
			} catch (IOException e) {
				System.out.println("ResultCreator - Could not write all results into file: " + e.getMessage());
			}
		}
	}

	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {	
		// we store it temporary so we can link these information to an 
		// occurring matching.
		this.prefix 	= prefixedFile;
		this.postfix 	= postfixedFile;
	}

	@Override
	public void OnBugChanged(String officalID, String urlToBugreport) {	
	}
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode node) {
	}
	
	@Override
	public void OnErrorDetected(String location, String errorMessage) {
		error_location.add(location);
		error_message.add(errorMessage);
	}

	@Override
	public void OnFileCheckFinished() {
	}

	@Override
	public void OnChangeChecked(SourceCodeChange change) {
	}

	@Override
	public void OnProgramChanged(String newProgramName,
			String programDescription, String urlToProjectPage,
			String urlToBugtracker) {
	}

	@Override
	public void OnMutationOperatorInit(MutationOperator mutop) {
	}

	@Override
	public void OnNoMatchingFound(List<MutationOperator> operatorlist) {
		// increment the counter
		this.undetected_matches += 1;
	}

	@Override
	public void OnAllChangesCheck(List<SourceCodeChange> changes) {		
	}

	
	
}
