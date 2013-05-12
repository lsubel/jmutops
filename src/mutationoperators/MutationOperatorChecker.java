package mutationoperators;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.Statement;

/**
 * Class storing the implemented MutationOperators, calling the correct operators
 * 
 * @author Lukas Subel
 *
 */
public class MutationOperatorChecker {

	ArrayList<MutationOperator> methodlevel_list;
	ArrayList<MutationOperator> classlevel_list;
	
	
	public MutationOperatorChecker() {
		this.methodlevel_list = new ArrayList<MutationOperator>();
		this.classlevel_list = new ArrayList<MutationOperator>();
	}
	
	public boolean addMutationOperator(MutationOperator mutop){
		switch(mutop.getCategory()){
		case CLASS_LEVEL:
			throw new UnsupportedOperationException("This method has not yet been implemented.");
		case METHOD_LEVEL:
			if(!this.methodlevel_list.contains(mutop)){
				this.methodlevel_list.add(mutop);
				return true;
			}
			else{
				return false;
			}
		default:
			return false;
		}
	}
	
	public void checkMethodLevel(Statement leftStatement, Statement rightStatement){
		for(MutationOperator mutop: this.methodlevel_list){
			mutop.check(leftStatement, rightStatement);
		}
	}
}
