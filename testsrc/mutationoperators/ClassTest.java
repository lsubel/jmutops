package mutationoperators;

import java.io.File;
import java.util.HashMap;

import mutationoperators.MutationOperator;

/**
 * General (abstract) class which should be used for class level related tests.
 * @author Lukas Subel
 *
 */
public abstract class ClassTest extends BasicTest {

	/////////////////////////////////////////
	/// Constants
	/////////////////////////////////////////
	
	/**
	 * The class name of the file where the method is contained in.
	 */
	protected static String CLASS_NAME = "Foo";
	
	protected static String CLASS_HEADER = "public class " + CLASS_NAME;
	
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
	
	protected String getImports() {
		return "";
	}
	
	
	/**
	 * Embed method or field into class, creates files for prefix and postfix version 
	 * 	and calls {@link BasicTest#compareMatches(File, File) compareMatches(File, File)} in {@link BasicTest}.
	 * @param prefixMethodBody The prefix method body.
	 * @param postfixMethodBody The postfix method body.
	 * @return The number of applications of the {@link MutationOperator} under test.
	 */
	public HashMap<String, Integer> compareMatches(String prefixItem, String postfixItem) {
		File preFix = this.createPrefixSourceFile(this.createClassSourceCode(prefixItem));
		File postFix = this.createPostfixSourceFile(this.createClassSourceCode(postfixItem));

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
	 * Embed the method's body into a method and the method into a class.<p>
	 * Furthermore adds class fields and other methods from {@link #getOtherClassContent()}.
	 * @param methodBody The method's body.
	 * @return Returns the whole code of a .java class.
	 */
	protected String createClassSourceCode(String class_code){
        return getImports() + "\n" + surroundWithClass(getOtherClassContent() + "\n" + class_code);		
	}
	
	/**
	 * Adds class stuff around the snippet.
	 * @param snippet
	 * @return The complete file content.
	 */
	protected String surroundWithClass(String snippet){
		return CLASS_HEADER + "\n" + " { \n" + snippet + " \n};";
	}
	
	/**
	 * Adds class stuff around the snippet.
	 * @param snippet
	 * @return The complete file content.
	 */
	protected String surroundWithClass(String classname, String snippet){
		return "public class " + classname + " { \n" + snippet + " \n};";
	}
}
