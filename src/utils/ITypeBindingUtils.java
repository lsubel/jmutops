package utils;

import org.eclipse.jdt.core.dom.ITypeBinding;

public class ITypeBindingUtils {
	
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
}
