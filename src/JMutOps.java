import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.Statement;

import results.DatabaseResults;

import utils.Preperator;

import mutationoperators.MutationOperatorChecker;
import mutationoperators.aor.AOR;
import mutationoperators.jti.JTI;
import mutationoperators.mnro.MNRO;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;


public class JMutOps {


	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(JMutOps.class.getName());
	
	FileDistiller distiller;

	MutationOperatorChecker checker;
	
	// preperate the files
	Preperator left_prep = new Preperator();
	Preperator right_prep = new Preperator();
	
	public JMutOps() {
		// init a new File distiller
		this.distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		// init the MutationOperatorChecker, fill it with MutationOperators
		this.checker = new MutationOperatorChecker();
		checker.addMutationOperator(new JTI(checker));
		checker.addMutationOperator(new AOR(checker));
		checker.addMutationOperator(new MNRO(checker));
	}
	
	public void checkFiles(File prefixedFile, File postfixedFile){
		// check for null argument
		if(prefixedFile == null){
			throw new IllegalArgumentException("First argument must not be null.");
		}
		if(postfixedFile == null){
			throw new IllegalArgumentException("Second argument must not be null.");
		}
		
		left_prep.prepare(prefixedFile);
		right_prep.prepare(postfixedFile);
		
		// Get the changes between both files
		try {
		    distiller.extractClassifiedSourceCodeChanges(left_prep.getFile(), right_prep.getFile());
		} catch(Exception e) {
		    System.err.println("Warning: error while change distilling: " + e.getMessage());
		}
		
		// extract the differences
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if(changes == null){
			logger.info("No changes were found.");
			logger.info("Ending to check File " + prefixedFile.getName() + "." + "\n");
			return;
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
				NodeFinder nodeFinder_old = new NodeFinder(left_prep.getAST(), sce_old_start, sce_old_end - sce_old_start + 1);
				ASTNode expr_left = nodeFinder_old.getCoveredNode();
				
				// extract information for second file
				NodeFinder nodeFinder_new = new NodeFinder(right_prep.getAST(), sce_new_start, sce_new_end - sce_new_start + 1);
				ASTNode expr_right = nodeFinder_new.getCoveredNode();

				if((expr_left instanceof Statement) && (expr_right instanceof Statement)){
					this.checker.checkForMutationOperators(expr_left, expr_right, change);
				}
				
				
			}	
		}

	}
}
