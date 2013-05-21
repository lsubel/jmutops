package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
	
	///////////////////////////////////////////////////
	///	Methods
	///////////////////////////////////////////////////
	
	/**
	 * Default constructor.
	 * 
	 * @param inputFile
	 */
	public Preperator() {
		this.parser = ASTParser.newParser(AST.JLS4);
		this.parser.setKind(ASTParser.K_COMPILATION_UNIT);
		this.parser.setResolveBindings(true); // we need bindings later on
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
		
			// generate an AST for this file
			char[] source_old = this.m_OutputContent.toCharArray();
			this.parser.setSource(source_old);
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
	///	Getter methods
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
	
}
