package mutationoperators.methodlevel.swo;

import java.util.HashSet;
import java.util.List;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;

import results.JMutOpsEventListenerMulticaster;
import utils.JDT_Utils;
import utils.Preperator;
import utils.SourceCodeChangeUtils;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class SWO extends MutationOperator {

	public SWO() {
		this(null);
	}
	
	public SWO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new SWO_Matcher(this);
		this.twoAST_visitor = new SWO_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("SWO");
		this.mutopproperty.setFullname("Statements swap operator");
		this.mutopproperty.setDescription("Changes the order of statements (Case-block-statements, statements in if-then-else, expressions before & after condition operator).");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setPreCheck();
	}
	
	@Override
	public int preCheck(List<SourceCodeChange> changes,
			Preperator prefixed_preperator, Preperator postfixed_preperator) {
		// since this mutation operator may need multiple changes for one application, 
		// we have to implement a special case for him
		
		// reset application counter
		this.application_counter = 0;
		
		// First extract all nodes where such a reorder might occur
		// which are: 
		// * switch-case
		// * if-then-else
		// Therefore, extract all these node with help of an ASTVisitor
		HashSet<MethodDeclaration> prefixMethodsToCheck 	= new HashSet<MethodDeclaration>();
		HashSet<MethodDeclaration> postfixMethodsToCheck 	= new HashSet<MethodDeclaration>();
		for(SourceCodeChange change: changes) {			
			if(change instanceof Insert){
				// extract all positions
				int[] pos_array = SourceCodeChangeUtils.getNodeFinderInput((Insert) change);
				// extract all newly changed methods
				NodeFinder nodefinder = new NodeFinder(postfixed_preperator.getAST(), pos_array[0], pos_array[1]);
				ASTNode expr = nodefinder.getCoveringNode();
				ASTNode found_method_declaration = JDT_Utils.searchForSpecificParentNode(expr, ASTNode.METHOD_DECLARATION);
				if(found_method_declaration != null) {
					postfixMethodsToCheck.add((MethodDeclaration) found_method_declaration);
				}
			} 	
			else if(change instanceof Delete){
				// extract all positions
				int[] pos_array = SourceCodeChangeUtils.getNodeFinderInput((Delete) change);
				// extract all changed methods
				NodeFinder nodefinder = new NodeFinder(prefixed_preperator.getAST(), pos_array[0], pos_array[1]);
				ASTNode expr = nodefinder.getCoveringNode();
				ASTNode found_method_declaration = JDT_Utils.searchForSpecificParentNode(expr, ASTNode.METHOD_DECLARATION);
				if(found_method_declaration != null) {
					prefixMethodsToCheck.add((MethodDeclaration) found_method_declaration);
				}
			}
			else if(change instanceof Move){
				// extract all positions
				int[] pos_array = SourceCodeChangeUtils.getNodeFinderInput((Move) change);
				// extract changed methods
				NodeFinder nodefinder = new NodeFinder(prefixed_preperator.getAST(), pos_array[0], pos_array[1]);
				ASTNode expr = nodefinder.getCoveringNode();
				ASTNode found_method_declaration = JDT_Utils.searchForSpecificParentNode(expr, ASTNode.METHOD_DECLARATION);
				if(found_method_declaration != null) {
					prefixMethodsToCheck.add((MethodDeclaration) found_method_declaration);
				}
				// extract new methods
				NodeFinder nodefinder2 = new NodeFinder(postfixed_preperator.getAST(), pos_array[2], pos_array[3]);
				ASTNode expr2 = nodefinder2.getCoveringNode();
				ASTNode found_method_declaration2 = JDT_Utils.searchForSpecificParentNode(expr, ASTNode.METHOD_DECLARATION);
				if(found_method_declaration2 != null) {
					postfixMethodsToCheck.add((MethodDeclaration) found_method_declaration2);
				}
			}
			else if(change instanceof Update){
				// extract all positions
				int[] pos_array = SourceCodeChangeUtils.getNodeFinderInput((Update) change);
				// extract changed methods
				NodeFinder nodefinder = new NodeFinder(prefixed_preperator.getAST(), pos_array[0], pos_array[1]);
				ASTNode expr = nodefinder.getCoveringNode();
				ASTNode found_method_declaration = JDT_Utils.searchForSpecificParentNode(expr, ASTNode.METHOD_DECLARATION);
				if(found_method_declaration != null) {
					prefixMethodsToCheck.add((MethodDeclaration) found_method_declaration);
				}
				// extract new methods
				NodeFinder nodefinder2 = new NodeFinder(postfixed_preperator.getAST(), pos_array[2], pos_array[3]);
				ASTNode expr2 = nodefinder2.getCoveringNode();
				ASTNode found_method_declaration2 = JDT_Utils.searchForSpecificParentNode(expr, ASTNode.METHOD_DECLARATION);
				if(found_method_declaration2 != null) {
					postfixMethodsToCheck.add((MethodDeclaration) found_method_declaration2);
				}
			}
		}
		
		// so now we have all methods which are changed
		// try to match pairs of methods
		ASTMatcher defaultMatcher = new ASTMatcher();
		for(MethodDeclaration prefix: prefixMethodsToCheck) {
			for(MethodDeclaration postfix: postfixMethodsToCheck) {
				// check for the same header properties
				boolean sameName = prefix.getName().subtreeMatch(defaultMatcher, postfix.getName());
				boolean sameParametersSize = (prefix.parameters().size() == postfix.parameters().size()); 
				boolean sameParameters = defaultMatcher.safeSubtreeListMatch(prefix.parameters(), postfix.parameters());
				boolean sameThrowExceptions = defaultMatcher.safeSubtreeListMatch(prefix.thrownExceptions(), postfix.thrownExceptions());
				boolean sameTypeParameters = defaultMatcher.safeSubtreeListMatch(prefix.typeParameters(), postfix.typeParameters());
				boolean sameModifiers = defaultMatcher.safeSubtreeListMatch(prefix.modifiers(), postfix.modifiers());
				boolean sameReturnType = ((prefix.getReturnType2() == null) && (postfix.getReturnType2() == null)) || prefix.getReturnType2().subtreeMatch(defaultMatcher, postfix.getReturnType2());
				
				// if all of these conditions are valid,
				// * traverse in parallel over both methods
				// * at some special nodes, check for exchanged stuff
				if(sameName && sameParametersSize && sameParameters && sameThrowExceptions && sameTypeParameters && sameModifiers && sameReturnType) {
					this.check(prefix, postfix);
				}
			}
		}
		
		
		return 0;
	}
}
