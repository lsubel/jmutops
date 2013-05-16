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
		
		JMutOps jmutops = new JMutOps();
		
		Logger logger = Logger.getLogger(TestApplication.class.getName());
		
		// for each iBugs id
		for(File folder_id: folders_id){
			
			logger.fine("Starting to check iBugs ID " + folder_id.getName());
			
			File[] prefixedFiles  = new File(folder_id.getAbsolutePath() + "\\pre-fix").listFiles();
			
			// for each file containing in the prefixed folder
			for(File prefixedFile: prefixedFiles){
				File postfixedFile = new File(folder_id.getAbsolutePath() + "\\post-fix\\" + prefixedFile.getName());

				// check both files to be .java
				if( !(FilenameUtils.isExtension(prefixedFile.getName(),"java")) || !(FilenameUtils.isExtension(postfixedFile.getName(),"java"))){
					continue;
				}
				
				logger.fine("Starting to check file " + prefixedFile.getName() + ".");
				
				jmutops.checkFiles(prefixedFile, postfixedFile);
								
				logger.fine("Ending to check iBugs ID " + folder_id.getName() + ", File " + prefixedFile.getName());
		
			}
			
			logger.info("Ending to check iBugs ID " + folder_id.getName() + "\n");
	
		}
		
	}

}
