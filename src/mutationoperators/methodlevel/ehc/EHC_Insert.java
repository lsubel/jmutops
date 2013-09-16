package mutationoperators.methodlevel.ehc;

import java.util.List;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;

import results.JMutOpsEventListenerMulticaster;
import utils.JDT_Utils;
import utils.Preperator;
import utils.SourceCodeChangeUtils;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class EHC_Insert extends MutationOperator {

	public EHC_Insert() {
		this(null);
	}
	
	public EHC_Insert(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EHC");
		this.mutopproperty.setFullname("Exception Handling Change");
		this.mutopproperty.setDescription("Changes an exception handling statement to an exception propagation statement and vice versa.");
		this.mutopproperty.setLevel(MutationOperatorLevel.BOTH_LEVELS);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setPreCheck();
	}
 
	@Override
	public int preCheck(List<SourceCodeChange> changes,
			Preperator prefixed_preperator, Preperator postfixed_preperator) {
		// run over all all changes
		for(SourceCodeChange change: changes) {
			// if we found an insert
			if(change instanceof Insert) {
				// cast the change
				Insert insert = (Insert) change;
				// check if the insert is related to a catch clause
				if(insert.getChangedEntity().getType().name().equals("CATCH_CLAUSE")) {
					// get the IMethodBinding related to this catch clause
					int[] input = SourceCodeChangeUtils.getNodeFinderInput((Insert) change);
					NodeFinder nodeFinder = new NodeFinder(postfixed_preperator.getAST(), input[0], input[1]);
					ASTNode expr = nodeFinder.getCoveringNode();
					if(!(expr instanceof CatchClause)) {
						continue;
					}
					CatchClause cc = (CatchClause) expr;
					
					ASTNode found_method_declaration = JDT_Utils.searchForSpecificParentNode(cc, ASTNode.METHOD_DECLARATION);
					if(found_method_declaration != null) {
						MethodDeclaration postfix_method_decleration = (MethodDeclaration) found_method_declaration;
						IMethodBinding postfix_method_binding = postfix_method_decleration.resolveBinding();
					}
				}
			}
		}
		
		
		return 0;
	}
	
}
