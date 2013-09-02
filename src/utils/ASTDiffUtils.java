package utils;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;

public class ASTDiffUtils {

	public static boolean sameASTNodeList(List firstlist, List secondlist) {
		// check if both list are null
		if((firstlist == null) && (secondlist == null)) {
			return true;
		}
		// check if at least one list is null
		if((firstlist == null) || (secondlist == null)) {
			return false;
		}
		// check for different size
		if(firstlist.size() != secondlist.size()) {
			return false;
		}
		// compare the elements
		ASTMatcher defaultMatcher = new ASTMatcher();
		for (int i = 0; i < firstlist.size(); i++) {
			if (!((ASTNode) firstlist.get(i)).subtreeMatch(defaultMatcher, secondlist.get(i))) {
				return false;
			}
		}
		return true;
	}
	
}
