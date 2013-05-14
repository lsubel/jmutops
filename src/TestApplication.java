import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import mutationoperators.MutationOperatorChecker;
import mutationoperators.aor.AOR;
import mutationoperators.jti.JTI;
import mutationoperators.mnro.MNRO;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;


import utils.ASTExtractor;
import utils.Preperator;
import utils.iBugsTools;
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
		
		File[] folders_id = new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Repository\\iBugs changed files\\changedistiller-results").listFiles();
//		File[] folders_id = new File[]{new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Repository\\iBugs changed files\\changedistiller-results\\28974")};
		
		
		Logger logger = Logger.getLogger(TestApplication.class.getName());
		
		// for each iBugs id
		for(File folder_id: folders_id){
			
			File[] prefixedFiles  = new File(folder_id.getAbsolutePath() + "\\pre-fix").listFiles();
			
			// for each file containing in the prefixed folder
			for(File prefixedFile: prefixedFiles){
				File postfixedFile = new File(folder_id.getAbsolutePath() + "\\post-fix\\" + prefixedFile.getName());

				// check both files to be .java
				if( !(FilenameUtils.isExtension(prefixedFile.getName(),"java")) || !(FilenameUtils.isExtension(postfixedFile.getName(),"java"))){
					continue;
				}
				
				logger.info("Starting to check iBugs ID " + folder_id.getName() + ", File " + prefixedFile.getName() + ".");
		
				// Include the files
				File file1 	= prefixedFile;
				File file2 	= postfixedFile;
				
				// preperate the files
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
				checker.addMutationOperator(new MNRO(checker));
				
				if(changes == null){
					logger.info("No changes were found.");
					logger.info("Ending to check iBugs ID " + folder_id.getName() + ", File " + prefixedFile.getName() + "." + "\n");
					continue;
				}
				
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
						CompilationUnit result_old = left_prep.getAST();
						ASTExtractor extractor_old = new ASTExtractor();
						ASTNode expr_left = extractor_old.getNode(result_old, sce_old_start, sce_old_end);	
						
						// extract information for second file
						CompilationUnit result_new = right_prep.getAST();
						ASTExtractor extractor_new = new ASTExtractor();
						ASTNode expr_right = extractor_new.getNode(result_new, sce_new_start, sce_new_end);	
		
						if((expr_left instanceof Statement) && (expr_right instanceof Statement)){
							checker.check(expr_left, expr_right, change);
						}
						
						
					}	
				}
				
				logger.info("Ending to check iBugs ID " + folder_id.getName() + ", File " + prefixedFile.getName() + "." + "\n");
		
			}
	
		}
		
	}

}
