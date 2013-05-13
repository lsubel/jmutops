import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;
import mutationoperators.aor.AOR;
import mutationoperators.jti.JTI;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import utils.ASTExtractor;
import utils.Preperator;

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
		
		Preperator left_prep = new Preperator(file1);
		Preperator right_prep = new Preperator(file2);
		
		// init a new File distiller
		FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		
		// Get the changes between both files
		try {
		    distiller.extractClassifiedSourceCodeChanges(left_prep.getFile(), right_prep.getFile());
		} catch(Exception e) {
		    System.err.println("Warning: error while change distilling: " + e.getMessage());
		}
		
		// extract the differences
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();

		// init MutationOperatorChecker
		MutationOperatorChecker checker = new MutationOperatorChecker();
		checker.addMutationOperator(new JTI(checker));
		checker.addMutationOperator(new AOR(checker));
		
		
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
				char[] source_old = left_prep.getFileContent().toCharArray();
				parser_old.setSource(source_old);
				parser_old.setKind(ASTParser.K_COMPILATION_UNIT);
				parser_old.setResolveBindings(true); // we need bindings later on
				CompilationUnit result_old = (CompilationUnit) parser_old.createAST(null);
				ASTExtractor extractor_old = new ASTExtractor();
				ASTNode expr_left = extractor_old.getNode(result_old, sce_old_start, sce_old_end);	
				
				// extract information for second file
				ASTParser parser_new = ASTParser.newParser(AST.JLS4);
				char[] source_new = right_prep.getFileContent().toCharArray();
				parser_old.setSource(source_new);
				parser_old.setKind(ASTParser.K_COMPILATION_UNIT);
				parser_old.setResolveBindings(true); // we need bindings later on
				CompilationUnit result_new = (CompilationUnit) parser_old.createAST(null);
				ASTExtractor extractor_new = new ASTExtractor();
				ASTNode expr_right = extractor_new.getNode(result_new, sce_new_start, sce_new_end);	

				checker.checkMethodLevel((Statement) expr_left, (Statement) expr_right);
				
				// 
				
				
			}	
		}
		
	}

}
