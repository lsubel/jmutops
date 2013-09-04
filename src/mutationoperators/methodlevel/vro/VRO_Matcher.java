package mutationoperators.methodlevel.vro;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

import utils.ITypeBindingUtils;

public class VRO_Matcher extends TwoASTMatcher {

	public VRO_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(SimpleName node, Object other) {

		// if the other object is a NumberLiteral,
		// we have to check that the SimpleName is from correct type
		if(other instanceof NumberLiteral) {
			NumberLiteral node2 = (NumberLiteral) other;
			
			// get the bindings for both items
			ITypeBinding type = node.resolveTypeBinding();
			ITypeBinding type2 = node2.resolveTypeBinding();
			
			// check conditions
			boolean differentNodes = !(node.subtreeMatch(defaultMatcher, node2));
			boolean isCastCompatible = type2.isCastCompatible(type);
			
			if(differentNodes && isCastCompatible) {
				this.mutop.found(node, node2);
				return false;
			}
		}
		// if the other object is a StringLiteral,
		// we have to check that the SimpleName is from correct type
		else if(other instanceof StringLiteral) {
			StringLiteral node2 = (StringLiteral) other;
			
			// get the bindings for both items
			ITypeBinding type = node.resolveTypeBinding();
			ITypeBinding type2 = node2.resolveTypeBinding();
			
			// check conditions
			boolean differentNodes = !(node.subtreeMatch(defaultMatcher, node2));
			boolean isCastCompatible = ITypeBindingUtils.isTypeParentOfOtherType(type, type2) || ITypeBindingUtils.isTypeParentOfOtherType(type2, type);
			
			if(differentNodes && isCastCompatible) {
				this.mutop.found(node, node2);
				return false;
			}
		}
		// if the other object is a SimpleName,
		// we have to check that the SimpleName is from correct type
		else if(other instanceof SimpleName) {
			SimpleName node2 = (SimpleName) other;
			
			// get the bindings for both items
			ITypeBinding type = node.resolveTypeBinding();
			ITypeBinding type2 = node2.resolveTypeBinding();
			
			// check conditions
			boolean differentNodes = !(node.subtreeMatch(defaultMatcher, node2));
			boolean isCastCompatible = (type != null) && (type2 != null) && (ITypeBindingUtils.isTypeParentOfOtherType(type, type2) || ITypeBindingUtils.isTypeParentOfOtherType(type2, type));
			
			if(differentNodes && isCastCompatible) {
				this.mutop.found(node, node2);
				return false;
			}
		}
		// if the other object is a FieldAccess,
		// we have to check that the SimpleName is from correct type
		else if(other instanceof FieldAccess) {
			FieldAccess node2 = (FieldAccess) other;
			
			// get the bindings for both items
			ITypeBinding type = node.resolveTypeBinding();
			ITypeBinding type2 = node2.resolveTypeBinding();
			
			// check conditions
			boolean differentNodes = !(node.subtreeMatch(defaultMatcher, node2));
			boolean isCastCompatible = ITypeBindingUtils.isTypeParentOfOtherType(type, type2) || ITypeBindingUtils.isTypeParentOfOtherType(type2, type);
			
			if(differentNodes && isCastCompatible) {
				this.mutop.found(node, node2);
				return false;
			}
		}
		// if the other object is a QualifiedName,
		// we have to check that the SimpleName is from correct type
		else if(other instanceof QualifiedName) {
			QualifiedName node2 = (QualifiedName) other;
			
			// get the bindings for both items
			ITypeBinding type = node.resolveTypeBinding();
			ITypeBinding type2 = node2.resolveTypeBinding();
			
			// check conditions
			boolean differentNodes = !(node.subtreeMatch(defaultMatcher, node2));
			boolean isCastCompatible = ITypeBindingUtils.isTypeParentOfOtherType(type, type2) || ITypeBindingUtils.isTypeParentOfOtherType(type2, type);
			
			if(differentNodes && isCastCompatible) {
				this.mutop.found(node, node2);
				return false;
			}
		}		
		return false;
		
	}
	
	

}
