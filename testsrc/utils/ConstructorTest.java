package utils;

import java.io.File;
import java.util.HashMap;

import mutationoperators.MutationOperator;

public abstract class ConstructorTest extends InheritTest {

	/////////////////////////////////////////
	/// Constants
	/////////////////////////////////////////
	
	/**
	* The class name of the file where the method is contained in.
	*/
	protected static String CLASS_NAME = "Foo";
	
	/////////////////////////////////////////
	/// Fields
	/////////////////////////////////////////
	
	
	/////////////////////////////////////////
	/// Methods
	/////////////////////////////////////////	
	
	@Override
	protected void initializeContextFiles() {
		// since we do not need external classes for most cases, we do not initialize any context
	}
	
	/**
	* Returns the other fields and methods of the main file under test. Will be used in {@link #createFieldMethodSourceCode(String)}.
	* @return The fields and methods of the file under test.
	*/
	protected abstract String getOtherClassContent();
	
	/**
	* Embed methodbody into method and class, creates files for prefix and postfix version 
	* 	and calls {@link BasicTest#compareMatches(File, File) compareMatches(File, File)} in {@link BasicTest}.
	* @param prefixMethodBody The prefix method body.
	* @param postfixMethodBody The postfix method body.
	* @return The number of applications of the {@link MutationOperator} under test.
	*/
	public HashMap<MutationOperator, Integer> compareMatches(String arguments, String prefixMethodBody, String postfixMethodBody) {
		File preFix = this.createPrefixSourceFile(this.createFieldConstructorSourceCode(arguments, prefixMethodBody));
		File postFix = this.createPostfixSourceFile(this.createFieldConstructorSourceCode(arguments, postfixMethodBody));
		
	return compareMatches(preFix, postFix);
	}
	
	/**
	* Creates and returns a file at the prefix default path.
	* @param sourceCode The file's source code.
	* @return The temporary file for the prefix version.
	*/
	protected File createPrefixSourceFile(String sourceCode) {
		return this.createSourceFile(CLASS_NAME, sourceCode, PATH_FOR_PREFIX_FILES);
	}
	
	/**
	* Creates and returns a file at the postfix default path.
	* @param sourceCode The file's source code.
	* @return The temporary file for the postfix version.
	*/
	protected File createPostfixSourceFile(String sourceCode) {
		return this.createSourceFile(CLASS_NAME, sourceCode, PATH_FOR_POSTFIX_FILES);
	}
	
	/**
	* Adds class stuff around the snippet.
	* @param snippet
	* @return The complete file content.
	*/
	protected String surroundWithClass(String classname, String snippet){
		return "public class " + classname + " { \n" + snippet + " \n};";
	}
	
	/**
	* Adds class stuff with extends and implements around the snippet.
	* @param classname
	* @param extendName
	* @param implementNames 
	* @param snippet
	* @return The complete file content.
	*/
	protected String surroundWithClass(String classname, String extendName, String implementNames, String snippet){
		String strResult = "public class " + classname + " ";
		if(extendName != ""){
			strResult = strResult + "extends " + extendName + " ";
		}
		if(implementNames != ""){
			strResult = strResult + "implements " + implementNames + " ";
		}
		strResult = strResult + "{ \n" + snippet + " \n};";
		return strResult;
	}
	
	/**
	* Embed the method's body into a method and the method into a class.<p>
	* Furthermore adds class fields and other methods from {@link #getOtherClassContent()}.
	* @param methodBody The method's body.
	* @return Returns the whole code of a .java class.
	*/
	protected String createFieldConstructorSourceCode(String arguments, String methodBody){
		StringBuilder methodSource = new StringBuilder();
		methodSource.append("public ");
		methodSource.append(CLASS_NAME);
		methodSource.append("(");
		methodSource.append(arguments);
		methodSource.append(") { ");
		methodSource.append(methodBody);
		methodSource.append(" }");
		return surroundWithClass(CLASS_NAME, getExtends(), getImplements(), getOtherClassContent() + "\n" + methodSource.toString());		
	}

}
