package utils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;


/**
 * ASTVisitor to extract an AST node with a specific range from the source code.
 * 
 * @author sheak
 *
 */
public class ASTExtractor extends ASTVisitor {
	
	
	/**
	 * Stores the start position of the searched AST within the document.
	 */
	private int startPos;
	
	/**
	 * Stores the end position of the searched AST within the document.
	 */
	private int endPos;

	/**
	 * Stores the found AST node until the visit through the AST end.
	 */
	private ASTNode searchedNode;

	
	/**
	 * Get an (sub)node from rootNode with document range by startPos and endPos.
	 * <p>
	 * @param rootNode starting {@link ASTNode} where the traversing should begin
	 * @param startPos starting position in a document, f.e. extracted by {@link getStartPosition()}
	 * @param endPos ending position in a document, f.e. extracted by {@link getEndPosition()}
	 * @return {@link ASTNode} found in rootNode with correct range, otherwise {@link null}
	 */
	public ASTNode getNode(ASTNode rootNode, int startPos, int endPos){
		// initialize variable
		this.searchedNode = null;
		this.startPos = startPos;
		this.endPos = endPos;
		// walk throught the node
		rootNode.accept(this);
		// return the found subnode
		return this.searchedNode;
	}
	
	private void checkRange(ASTNode node){
		// extract document range
		int start 	= node.getStartPosition();
		int end 	= node.getStartPosition() + node.getLength() - 1;
		// compare positions
		if((this.startPos == start) && (this.endPos == end)){
			assert (this.searchedNode == null);
			this.searchedNode = node;
		}		
	}
	
	@Override
	public void preVisit(ASTNode node) {
		checkRange(node);
	}

}
