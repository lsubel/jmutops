package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

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
	
	boolean includeRunningVMBootclasspath;
	
	///////////////////////////////////////////////////
	///	Methods
	///////////////////////////////////////////////////
	
	/**
	 * Default constructor.
	 * 
	 * @param inputFile
	 */
	public Preperator() {
		// initialize parser
		this.parser = ASTParser.newParser(AST.JLS4);
		this.parser.setKind(ASTParser.K_COMPILATION_UNIT);
		this.parser.setResolveBindings(true); // we need bindings later on
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
			String tempFileName = "temp" + "_" + RandomStringUtils.random(16, "abcdefghijklmnopqrstuvwxyz".toCharArray());
			this.m_OutputFile = File.createTempFile(tempFileName, ".java");
		
			// extract the content of the first file
			StringBuffer bufferFileContent = null;
			BufferedReader inBuffer = new BufferedReader(new FileReader(this.m_SourceFile));
			bufferFileContent = new StringBuffer();
			String line = null;
			while (null != (line = inBuffer.readLine())) {
				bufferFileContent.append(line).append("\n");
			}
		
			// write the extracted content into the temp file
			BufferedWriter out = new BufferedWriter(new FileWriter(this.m_OutputFile));
			String outText = bufferFileContent.toString();
			out.write(outText);
			out.close();
		
			// store the content
			this.m_OutputContent = bufferFileContent.toString();
		
			// add additional information
			String[] classPaths = this.classpathEntries.toArray(new String[this.classpathEntries.size()]);
			String[] sourcePaths = this.sourcepathEntries.toArray(new String[this.sourcepathEntries.size()]);
			String[] encodings = this.encodings.toArray(new String[this.encodings.size()]);
			this.parser.setEnvironment(classPaths, sourcePaths, encodings, this.includeRunningVMBootclasspath);
			char[] source_old = this.m_OutputContent.toCharArray();
			this.parser.setSource(source_old);
			
			// generate an AST for this file
			this.m_OutputAST = (CompilationUnit) this.parser.createAST(null);
		
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
	
}
