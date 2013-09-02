package mutationoperators.methodlevel.swo;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class SWO_MethodExtractor {

	public MethodDeclaration getMethodNode(ASTNode change) {
		
		ASTNode currentNode = change;
		
		// walk upwards until we reached the root node
		while(currentNode != null) {
			
			// check if we reached the MethodDeclaration node
			if(currentNode instanceof MethodDeclaration) {
				return (MethodDeclaration) currentNode;
			}
			
			// otherwise go to the parent node
			currentNode = currentNode.getParent();
		}
		
		return null;
	}
	
}
