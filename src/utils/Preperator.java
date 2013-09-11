package utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import results.JMutOpsEventListenerMulticaster;

public class Preperator {

	///////////////////////////////////////////////////
	///	Fields
	///////////////////////////////////////////////////
	
	ASTParser parser;
	
	File m_SourceFile;
	
	File m_OutputFile;
	
	String m_OutputContent;
	
	CompilationUnit m_OutputAST;
	
	ArrayList<String> classpathEntries;
	
	ArrayList<String> sourcepathEntries;
	
	ArrayList<String> encodings;
	
	Hashtable<String, String> options;
	
	boolean includeRunningVMBootclasspath;
	
	File defaultPath;
	
	JMutOpsEventListenerMulticaster listener;
	
	///////////////////////////////////////////////////
	///	Methods
	///////////////////////////////////////////////////
	
	/**
	 * Default constructor.
	 * @param listener TODO
	 * @param inputFile
	 */
	public Preperator(JMutOpsEventListenerMulticaster listener, File defaultPath) {
		// store parameter
		this.defaultPath = defaultPath;
		this.listener = listener;
		// initialize parser
		this.parser = ASTParser.newParser(AST.JLS4);
		this.parser.setKind(ASTParser.K_COMPILATION_UNIT);
		this.parser.setResolveBindings(true);
		this.parser.setBindingsRecovery(true);
		this.parser.setStatementsRecovery(true);
		// initialize ArrayList
		this.classpathEntries = new ArrayList<String>();
		this.sourcepathEntries = new ArrayList<String>();
		this.encodings = new ArrayList<String>();
	}
	
	public boolean prepare(File inputFile){
		// store the inputFile
		this.m_SourceFile = inputFile;

		try {
			// generate a new File where we store the content
			String tempFileName = inputFile.getName();
			this.m_OutputFile = File.createTempFile(tempFileName, ".java", this.defaultPath);
		
			// extract the content of the file,
			// write the extracted content into the temp file,
			// store the content
			StringBuffer bufferFileContent = null;
			BufferedReader inBuffer = new BufferedReader(new FileReader(this.m_SourceFile));
			bufferFileContent = new StringBuffer();
			String line = null;
			while (null != (line = inBuffer.readLine())) {
				bufferFileContent.append(line).append("\n");
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(this.m_OutputFile));
			String outText = bufferFileContent.toString();
			out.write(outText);
			out.close();
			this.m_OutputContent = bufferFileContent.toString();
		
			// add additional information to the parser
			String[] classPaths = this.classpathEntries.toArray(new String[this.classpathEntries.size()]);
			String[] sourcePaths = this.sourcepathEntries.toArray(new String[this.sourcepathEntries.size()]);
			String[] encodings = this.encodings.toArray(new String[this.encodings.size()]);
			char[] source_old = this.m_OutputContent.toCharArray();
			this.parser.setEnvironment(classPaths, sourcePaths, encodings, this.includeRunningVMBootclasspath);
			this.parser.setCompilerOptions(this.options);
			this.parser.setUnitName(inputFile.getName());
			this.parser.setSource(source_old);
			
			// generate an AST for this file
			this.m_OutputAST = (CompilationUnit) this.parser.createAST(null);
		
			// write occuring problems into the logger
		    IProblem[] problems = this.m_OutputAST.getProblems();
		    if (problems != null && problems.length > 0) {
		    	// create and send a error message:
		    	StringBuffer errorMessage = new StringBuffer();
		    	errorMessage.append(problems.length + " different errors detected." + "\n");
		        for (IProblem problem : problems) { 
					errorMessage.append("\t" + "In " + new String(problem.getOriginatingFileName()) + ": " + problem.getMessage() + "\n");
		        }
		        this.listener.OnErrorDetected("Preperator - prepare", errorMessage.toString());
		    }
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	assert (this.m_SourceFile != null);
	assert (this.m_OutputContent != null);
	assert (this.m_OutputFile != null);
	assert (this.m_OutputAST != null);
	
	return true;
}
	
	///////////////////////////////////////////////////////
	///	Getter/Setter methods
	///////////////////////////////////////////////////////
	
	public File getFile(){
		return this.m_OutputFile;
	}
	
	public String getFileContent(){
		return this.m_OutputContent;
	}
	
	public CompilationUnit getAST(){
		return this.m_OutputAST;
	}

	public boolean addClasspathEntry(String classPath) {
		if(this.classpathEntries.contains(classPath)){
			return false;
		}
		else {
			this.classpathEntries.add(classPath);
			return true;
		}
	}

	public boolean addSourcepathEntry(String sourcePath, String encoding) {
		if(this.sourcepathEntries.contains(sourcePath)){
			return false;
		}
		else {
			this.sourcepathEntries.add(sourcePath);
			this.encodings.add(encoding);
			return true;
		}
	}

	public boolean setIncludeRunningVMBootclasspath(boolean newValue) {
		this.includeRunningVMBootclasspath = newValue;
		return true;
	}

	public boolean setOptions(Hashtable<String, String> options) {
		try {
			this.options = options;
			return true;
		} catch (Exception e) {
			this.listener.OnErrorDetected("Preperator - setOptions", e.getMessage());
			return false;
		}
	}
	
}
