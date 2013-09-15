package utils;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * @author Lukas Subel
 *
 */
public class JDT_Utils {
	
	/**
	 * Check if the first type is a super class of the second type.<p>
	 * This implementation is required due to the different clusters of bindings.
	 * @param left The first {@link ITypeBinding}.
	 * @param right The second {@link ITypeBinding}.
	 * @return True iff the first type is a super type of the second one.
	 */
	public static boolean isTypeParentOfOtherType(ITypeBinding left, ITypeBinding right){
		ITypeBinding temp = right;
		do{
			if(left.isEqualTo(temp)){
				return true;
			}
			temp = temp.getSuperclass();
		}while(temp != null);
		// when we reached this point, then there is no more possible superclass and we have not detected a match yet
		return false;
	}
	
	
	/**
	 * Gets the {@link MethodDeclaration} node related to the change node.
	 * @param change The {@link ASTNode} related to the change.
	 * @return The related {@link MethodDeclaration} node if method level change, otherwise {@code null}.
	 */
	public static MethodDeclaration getMethodNode(ASTNode change) {
		
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
