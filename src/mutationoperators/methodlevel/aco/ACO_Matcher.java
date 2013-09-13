package mutationoperators.methodlevel.aco;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ACO_Matcher extends TwoASTMatcher {

	public ACO_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	
	@Override
	public boolean match(MethodInvocation node, Object other) {
		
		// if the compared AST is  MethodInvocation
		if(other instanceof MethodInvocation){
			// cast other node
			MethodInvocation node2 = (MethodInvocation) other;
			
			// if both methods have no parameters, we can abort
			boolean noArguments = (node.resolveMethodBinding().getParameterTypes().length == 0) && (node2.resolveMethodBinding().getParameterTypes().length == 0);
			if(noArguments) {
				return false;
			}
			
			// first check if both methods have the same method name
			boolean sameMethodName = node.getName().subtreeMatch(defaultMatcher, node2.getName());
			
			if(sameMethodName){
				// resolve the bindings
				IMethodBinding node_methodbinding = node.resolveMethodBinding();
				IMethodBinding node2_methodbinding = node2.resolveMethodBinding();
				
				// check if both methods are equal
				boolean sameMethods = node_methodbinding.isEqualTo(node2_methodbinding);
				
				if(sameMethods){
					assert node_methodbinding.getParameterTypes().equals(node2_methodbinding.getParameterTypes());
					
					// check conditions
					boolean argumentPositionChanged = !(safeSubtreeListMatch(node.arguments(), node2.arguments()));
							
					if (argumentPositionChanged) {
						this.mutop.found(node, node2);
						return true;
					}
				}
				// otherwise we have different methods
				else{
					// check if the different constructors have the same number of arguments
					boolean sameArgumentNumber = (node_methodbinding.getParameterTypes().length == node2_methodbinding.getParameterTypes().length);
					
					// if both have the same argument length
					if(sameArgumentNumber){
						// we have to check for a different ordering
						// => we check if there is the same number of types declared in both parameters
						ITypeBinding[] parameterTypes1 = node_methodbinding.getParameterTypes();
						ITypeBinding[] parameterTypes2 = node2_methodbinding.getParameterTypes();
						
						HashMap<ITypeBinding, Integer> count1 = countTypesInParameters(parameterTypes1);
						HashMap<ITypeBinding, Integer> count2 = countTypesInParameters(parameterTypes2);
						
						// check if there are the same values in both HashMaps
						boolean sameTypeNumbers = checkForSameTypes(count1, count2);
						// if we have the same number of detected types, we have a match found
						if(sameTypeNumbers){
							this.mutop.found(node, node2);
							return true;
						}
					}
					// otherwise we also have a different number of arguments
					else{
						ITypeBinding[] parameterTypes1 = node_methodbinding.getParameterTypes();
						ITypeBinding[] parameterTypes2 = node2_methodbinding.getParameterTypes();
						
						HashMap<ITypeBinding, Integer> count1 = countTypesInParameters(parameterTypes1);
						HashMap<ITypeBinding, Integer> count2 = countTypesInParameters(parameterTypes2);
						
						
						// check for reduced parameters
						boolean reducedTypeNumbers = checkForReducedTypes(count1, count2);
						// if we have a reduced number of detected types, we have a match found
						if(reducedTypeNumbers){
							this.mutop.found(node, node2);
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	
	@Override
	public boolean match(ClassInstanceCreation node, Object other) {
		
		// if the compared AST is ClassInstanceCreation
		if(other instanceof ClassInstanceCreation){
			// cast the parallel node
			ClassInstanceCreation node2 = (ClassInstanceCreation) other;	

			// if both methods have no parameters, we can abort
			boolean noArguments = (node.resolveConstructorBinding().getParameterTypes().length == 0) && (node2.resolveConstructorBinding().getParameterTypes().length == 0);
			if(noArguments) {
				return false;
			}
			
			// first check if both constructors have the same name
			boolean sameConstructorName = node.getType().subtreeMatch(defaultMatcher, node2.getType());
			if (sameConstructorName) {
				// resolve the bindings 
				IMethodBinding constructor1 = node.resolveConstructorBinding();
				IMethodBinding constructor2 = node2.resolveConstructorBinding();
				// check if both constructors are equal
				boolean sameConstructors = (constructor1.isEqualTo(constructor2));
				// if we have the same constructor method
				if (sameConstructors) {
					// check for reordering of arguments
					// which is equal to different argument trees
					assert (constructor1.getParameterTypes().equals(constructor2.getParameterTypes()));
					
					boolean argumentPositionChanged = !(safeSubtreeListMatch(node.arguments(), node2.arguments()));
					if (argumentPositionChanged) {
						this.mutop.found(node, node2);
						return true;
					}
				}
				// otherwise we have different constructors
				else {
					// check if the different constructors have the same number of arguments
					boolean sameArgumentNumber = (constructor1.getParameterTypes().length == constructor2.getParameterTypes().length);
					
					// if both have the same argument length
					if(sameArgumentNumber){
						// so we have different constructors with the same number of arguments
						// therefore we have to check for different argument type bindings
						// and for different arguments
						// => we check if there is the same number of types declared in both parameters
						ITypeBinding[] parameterTypes1 = constructor1.getParameterTypes();
						ITypeBinding[] parameterTypes2 = constructor2.getParameterTypes();
						
						HashMap<ITypeBinding, Integer> count1 = countTypesInParameters(parameterTypes1);
						HashMap<ITypeBinding, Integer> count2 = countTypesInParameters(parameterTypes2);

						// check if there are the same values in both HashMaps
						boolean sameTypeNumbers = checkForSameTypes(count1, count2);
						boolean differentArguments = !(safeSubtreeListMatch(node.arguments(), node2.arguments()));
						// if we have the same number of detected types, we have a match found
						if(sameTypeNumbers && differentArguments){
							this.mutop.found(node, node2);
							return true;
						}
					}
					// otherwise we also have a different number of arguments
					else{
						ITypeBinding[] parameterTypes1 = constructor1.getParameterTypes();
						ITypeBinding[] parameterTypes2 = constructor2.getParameterTypes();
						
						HashMap<ITypeBinding, Integer> count1 = countTypesInParameters(parameterTypes1);
						HashMap<ITypeBinding, Integer> count2 = countTypesInParameters(parameterTypes2);
						
						
						// check for reduced parameters
						boolean reducedTypeNumbers = checkForReducedTypes(count1, count2);
						// if we have a reduced number of detected types, we have a match found
						if(reducedTypeNumbers){
							this.mutop.found(node, node2);
							return true;
						}
					}
				}
			}
		}
		// in any other case, return false
		return false;
	}


	private boolean checkForReducedTypes(HashMap<ITypeBinding, Integer> count1,
			HashMap<ITypeBinding, Integer> count2) {
		boolean foundEntryWithReducedNumber = false;
		Set<ITypeBinding> setOfRetrievedBindings = new HashSet<ITypeBinding>();
		
		// since we check for reduced case (=> arguments were removed),
		// we have to assume that count1.keySet().size() >= count1.keySet().size()
		if(count1.keySet().size() < count2.keySet().size()) {
			return false;
		} 
		// now check for each ITypeBinding in count1 that in count2
		// * exists no entry
		// * all entries have the same or less numbers of arguments
		// * at least one entry has the less numbers of arguments
		outer_loop:
		for(ITypeBinding parameterType1: count1.keySet()) {
			// first search for an entry in count2 with the same ITypeBinding
			for(ITypeBinding parameterType2: count2.keySet()) {
				if(parameterType1.isEqualTo(parameterType2)) {
					
					// retrieve the values for both bindings
					int value1 = count1.get(parameterType1).intValue();
					int value2 = count2.get(parameterType2).intValue();
					
					// if value1 < value2, we have found a incrementation of an argument type
					// => we have detected that parameters were not reduced
					if(value1 < value2) {
						return false;
					}
					
					// if value1 > value2, we have found that parameter(s) were removed
					if(value1 > value2) {
						foundEntryWithReducedNumber = true;
					}
					
					// add that the parameterType2 was tested
					setOfRetrievedBindings.add(parameterType2);
					
					continue outer_loop;
				}
			}
			// if we reached this point, 
			// we have not found any corresponding binding in count2
			// therefore we know that no binding was found in the second method
			// and we know that parameters were removed
			foundEntryWithReducedNumber = true;
		}
		
		// at the end, check that all entries in count2 were tested
		// if not, that new parameter types were introduced in the second method
		if(!(count1.keySet().containsAll(setOfRetrievedBindings)) || !(setOfRetrievedBindings.containsAll(count1.keySet()))) {
			return false;
		}
		
		// if we reached this point, everything is fine and we can return true
		return true;
	}


	private boolean checkForSameTypes(HashMap<ITypeBinding, Integer> count1,
			HashMap<ITypeBinding, Integer> count2) {
		// since we check for the same case (=> arguments were not removed),
		// we have to assume that count1.keySet().size() == count1.keySet().size()
		if(count1.keySet().size() != count2.keySet().size()) {
			return false;
		} 
		
		// now we check that the keySets of both counts are the same
		// otherwise the parameter list changed
		if( !(count1.keySet().containsAll(count2.keySet())) || !(count2.keySet().containsAll(count1.keySet()))) {
			return false;
		}
		
		// now check for each ITypeBinding in count1 that in count2 and vice versa
		// * all entries have the same numbers of arguments
		for(ITypeBinding parameterType1: count1.keySet()) {
			// first search for an entry in count2 with the same ITypeBinding
			for(ITypeBinding parameterType2: count2.keySet()) {
				if(parameterType1.isEqualTo(parameterType2)) {
					
					// retrieve the values for both bindings
					int value1 = count1.get(parameterType1).intValue();
					int value2 = count2.get(parameterType2).intValue();
					
					// if value1 < value2, we have found a incrementation of an argument type
					// => we have detected that parameters were not reduced
					if(value1 != value2) {
						return false;
					}
				}
			}
		}
		
		// if we reached this point, everything is fine and we can return true
		return true;
	}


	private HashMap<ITypeBinding, Integer> countTypesInParameters(
			ITypeBinding[] parameterTypes) {
		HashMap<ITypeBinding, Integer> count2 = new HashMap<ITypeBinding, Integer>();
		for(ITypeBinding parameterType: parameterTypes){
			if(count2.containsKey(parameterType)){
				int temp = count2.remove(parameterType);
				count2.put(parameterType, temp + 1);
			}
			else{
				count2.put(parameterType, 1);
			}
		}
		return count2;
	}
}
