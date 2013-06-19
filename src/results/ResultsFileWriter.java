package results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

public class ResultsFileWriter implements JMutOpsEventListener{

	//////////////////////////////////////////
	///	Fields
	//////////////////////////////////////////
	
	ArrayList<Dictionary<String, String>> results; 
	
	String filename;
	
	File prefix;

	File postfix;
	
	//////////////////////////////////////////
	///	Methods
	//////////////////////////////////////////
	
	public ResultsFileWriter() {
		this.results = new ArrayList<Dictionary<String, String>>();
	}
	
	public void setResultingFileName(String filename){
		this.filename = filename;
	}
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		// generate an entry for the matching
		Dictionary<String, String> entry = new Hashtable<String, String>();
		
		entry.put("operator", operator.getClass().getSimpleName());
		entry.put("prefix-content", prefix.toString());
		entry.put("prefix-node", prefix.getClass().toString());
		entry.put("prefix-range", prefix.getStartPosition() + "-" + (prefix.getStartPosition() + prefix.getLength() - 1));
		entry.put("prefix-file", this.prefix.getAbsolutePath());
		entry.put("postfix-content", postfix.toString());
		entry.put("postfix-node", postfix.getClass().toString());
		entry.put("postfix-range", postfix.getStartPosition() + "-" + (postfix.getStartPosition() + postfix.getLength() - 1));		
		entry.put("postfix-file", this.prefix.getAbsolutePath());
		this.results.add(entry);
	}

	@Override
	public void OnCreatingResult() {
		// initialize variables
		FileWriter fw = null;
		File resultingFile = null;
		BufferedWriter bw = null;
		// generate a new file which stores the result
		try {
			resultingFile = new File(this.filename);
			resultingFile.createNewFile();
			fw = new FileWriter(resultingFile);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			System.out.println("Could not initialize file with results.");
			System.exit(0);
		}
		
		// write results into the file
		try {
			bw.write("///////////////////////////////////////////////////////" + "\n");
			bw.write("						Results							 " + "\n");
			bw.write("///////////////////////////////////////////////////////" + "\n");
			for(Dictionary<String, String> entry: this.results){
				bw.write("\n");
				bw.write("Found application of " + entry.get("operator") + " operator:" + "\n");
				bw.write("\t" + "Prefix version: " + "\n");
				bw.write("\t\t" + "Content: " + entry.get("prefix-content")  + "\n");
				bw.write("\t\t" + "Node type: " + entry.get("prefix-node") + "\n");
				bw.write("\t\t" + "Range: " + entry.get("prefix-range") + "\n");
				bw.write("\t\t" + "File: " + entry.get("prefix-file") + "\n");
				bw.write("\t" + "Postfix version: " + "\n");
				bw.write("\t\t" + "Content: " + entry.get("postfix-content") + "\n");
				bw.write("\t\t" + "Node type: " + entry.get("postfix-node") + "\n");
				bw.write("\t\t" + "Range: " + entry.get("postfix-range") + "\n");
				bw.write("\t\t" + "File: " + entry.get("postfix-file") + "\n");
				bw.write("");
			}
			bw.write("\n");
			bw.write("///////////////////////////////////////////////////////" + "\n");
			bw.write("						End of file						 " + "\n");
			bw.write("///////////////////////////////////////////////////////" + "\n");	
			bw.close();
		} catch (IOException e) {
			System.out.println("Could not write all results into file.");
			System.exit(0);
		}
	}

	@Override
	public void OnProgramChanged(String newProgramName) {
	}

	@Override
	public void OnBugChanged(int officalID) {	
	}

	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {	
		this.prefix 	= prefixedFile;
		this.postfix 	= postfixedFile;
	}

	@Override
	public void OnErrorDetected(String location, String errorMessage) {
	}

	@Override
	public void OnFileCheckFinished() {
	}

	
	
}
