import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ASTExtractor extends ASTVisitor {
	// stores the position
	int startPos;
	int endPos;
	
	// object to get line in document
	CompilationUnit cu;
	
	// stores the expression
	Expression expr = null;
	
	public ASTExtractor(int startPos, int endPos, CompilationUnit result) {
		// store the start and end position
		this.startPos 	= startPos;
		this.endPos 	= endPos;
		this.cu = result;
	}
	
	
	@Override
	public boolean visit(MethodInvocation node) {
		// extract document range
		int start 	= node.getStartPosition();
		int end 	= node.getStartPosition() + node.getLength();
		
		
		
		// compare positions
		if( (this.startPos == start) && (this.endPos == end)){
			expr = node;
		}
		
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	
//	@Override
//	public boolean visit(MessageSend messageSend, BlockScope scope) {
//		// extract document range
//		int start 	= messageSend.sourceStart;
//		int end 	= messageSend.sourceEnd;
//		
//		// compare positions
//		if( (this.startPos == start) && (this.endPos == end)){
//			expr = messageSend;
//		}
//		
//		return true;
//	}
	
}
