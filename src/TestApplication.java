import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;


public class TestApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		// Include the files
		File file1 	= new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\Test1.java");
		File file2 	= new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\Test2.java");
		
		// create temporary files
		File left = null;
		File right = null;
		try {
			left = new File("temp_left.java");
			right = new File("temp_right.java");
			if(!left.exists()){
				left.createNewFile();
			}
			if(!right.exists()){
				right.createNewFile();
			}
		} catch (IOException e1) {

			
		}
		
		// extract the content of the first file
		StringBuffer buffer1 = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file1));
			buffer1 = new StringBuffer();
			String line = null;
			while (null != (line = in.readLine())) {
			     buffer1.append(line).append("\n");
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(left));
			String outText = buffer1.toString();
			out.write(outText);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// extract the content of the second file
		StringBuffer buffer2 = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file2));
			buffer2 = new StringBuffer();
			String line = null;
			while (null != (line = in.readLine())) {
			     buffer2.append(line).append("\n");
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(right));
			String outText = buffer2.toString();
			out.write(outText);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// init a new File distiller
		FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		
		// Get the changes between both files
		try {
		    distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch(Exception e) {
		    System.err.println("Warning: error while change distilling: " + e.getMessage());
		}
		
		// extract the differences
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();

		// handle each change
		for(SourceCodeChange change: changes){
			
			if(change instanceof Update){
				// cast the change into the correct type
				Update change_update = (Update) change;	
				
				// extract the change entities
				SourceCodeEntity sce_old = change_update.getChangedEntity();
				SourceCodeEntity sce_new = change_update.getNewEntity();
				
				// extract the ranges in the document
				int sce_old_start 	= sce_old.getStartPosition();
				int sce_old_end 	= sce_old.getEndPosition();
				int sce_new_start 	= sce_new.getStartPosition();
				int sce_new_end 	= sce_new.getEndPosition();

				
				// extract information for first file
				ASTParser parser_old = ASTParser.newParser(AST.JLS4);
				char[] source_old = buffer1.toString().toCharArray();
				parser_old.setSource(source_old);
				parser_old.setKind(ASTParser.K_COMPILATION_UNIT);
				parser_old.setResolveBindings(true); // we need bindings later on
				CompilationUnit result_old = (CompilationUnit) parser_old.createAST(null);
				ASTExtractor extractor_old = new ASTExtractor(sce_old_start, sce_old_end, result_old);
				result_old.accept(extractor_old);
				Expression expr_left = extractor_old.expr;
				
				// extract information for second file
				ASTParser parser_new = ASTParser.newParser(AST.JLS4);
				char[] source_new = buffer2.toString().toCharArray();
				parser_old.setSource(source_new);
				parser_old.setKind(ASTParser.K_COMPILATION_UNIT);
				parser_old.setResolveBindings(true); // we need bindings later on
				CompilationUnit result_new = (CompilationUnit) parser_old.createAST(null);
				ASTExtractor extractor_new = new ASTExtractor(sce_new_start, sce_new_end, result_new);
				result_new.accept(extractor_new);				
				Expression expr_right = extractor_new.expr;
				
				ASTMatcher matcher = new ASTMatcher();
				
				// compare both expressions
				if(!expr_right.subtreeMatch(matcher, expr_left)){
					SimpleName expr_left_casted = (SimpleName) ((MethodInvocation) expr_left ).arguments().get(0);
					FieldAccess expr_right_casted = (FieldAccess) ((MethodInvocation) expr_right ).arguments().get(0);
					
					boolean isThisExpression = (expr_right_casted.getExpression() instanceof ThisExpression);
					boolean hasSameSimpleName = expr_right_casted.getName().subtreeMatch(matcher, expr_left_casted);
					
					if( isThisExpression && hasSameSimpleName){
						System.out.println("Matched!");
						
						
					}
				
				}
				
				// 
				System.out.println("Done");
				
				
			}	
		}
		
	}

}
