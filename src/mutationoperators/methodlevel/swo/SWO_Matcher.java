package mutationoperators.methodlevel.swo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

public class SWO_Matcher extends TwoASTMatcher {

	public SWO_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(IfStatement node, Object other) {
		// if the second object is no IfStatement, we cannot compare them
		if(!(other instanceof IfStatement)) {
			return false;
		}
		
		// cast the second ast
		IfStatement node2 = (IfStatement) other;
		
		// extract the then and else block of both trees
		Statement node_then = node.getThenStatement();
		Statement node_else = node.getElseStatement();
		Statement node2_then = node2.getThenStatement();
		Statement node2_else = node2.getElseStatement();
		
		// if one of the else statements is null, we can abort the check
		if((node_else == null) || (node2_else == null)) {
			return false;
		}
		
		// check if the then and else were swapped
		boolean swap_then_to_else = node_then.subtreeMatch(defaultMatcher, node2_else);
		boolean swap_else_to_then = node_else.subtreeMatch(defaultMatcher, node2_then);
		
		// if all conditions are true, we found a matching
		if(swap_else_to_then && swap_then_to_else) {
			this.mutop.found(node, node2);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean match(SwitchStatement node, Object other) {
		// if the second object is no IfStatement, we cannot compare them
		if(!(other instanceof SwitchStatement)) {
			return false;
		}
		
		// cast the second ast
		SwitchStatement node2 = (SwitchStatement) other;
		
		// get the SwitchCase mapping for both switch cases
		Map<SwitchCase, ArrayList<Statement>> node_case_map = getSwitchCaseMapping(node);
		Map<SwitchCase, ArrayList<Statement>> node2_case_map = getSwitchCaseMapping(node2);

		// compare the different nodes
		for(SwitchCase sc: node_case_map.keySet()) {
			for(SwitchCase sc2: node2_case_map.keySet()) {
				// extract values
				ArrayList<Statement> node_case_list = node_case_map.get(sc);
				ArrayList<Statement> node2_case_list = node2_case_map.get(sc2);
				// check the conditions for a matching
				boolean differentSwitch = !(sc.subtreeMatch(defaultMatcher, sc2));
				boolean sameNodeList = this.defaultMatcher.safeSubtreeListMatch(node_case_list, node2_case_list);
				
				// if all conditions are true, notify a matching
				if(differentSwitch && sameNodeList) {
					this.mutop.found(node, node2);
					return true;
				}
			}
		}
		
		return false;
	}

	private Map<SwitchCase, ArrayList<Statement>> getSwitchCaseMapping(SwitchStatement switchStatement){
		// generate a new map object
		Map<SwitchCase, ArrayList<Statement>> map = new HashMap<SwitchCase, ArrayList<Statement>>();
		
		// extract statement list
		List stmt_list = switchStatement.statements();
		
		// run over all included statments
		for (int i = 0; i < stmt_list.size(); i++) {
			Statement stmt = (Statement) stmt_list.get(i);
			if(stmt instanceof SwitchCase) {
				SwitchCase sc = (SwitchCase) stmt;
				ArrayList<Statement> substmt_list = new ArrayList<Statement>();
				
				// run over all following elements until we reach a break or the end
				// and store the corresponding node to the list
				for(int j = i + 1; j < stmt_list.size(); j++) {
					Statement stmt2 = (Statement) stmt_list.get(j);
					// in case of a BreakStatement, stop the adding process
					if(stmt2 instanceof BreakStatement) {
						break;
					}
					
					// in case of a SwitchCase, ignore this statement
					if(stmt2 instanceof SwitchCase) {
						continue;
					}
					
					substmt_list.add(stmt2);
				}
				
				// connect the arraylist with the switchcase
				map.put(sc, substmt_list);
			}
		}
		
		return map;
	}
	
	
}
