package mutationoperators.aco;

import java.util.HashMap;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ACO_Matcher extends BaseASTMatcher {

	public ACO_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	
	@Override
	public boolean match(ClassInstanceCreation node, Object other) {
		
		// if the compared AST is ClassInstanceCreation
		if(other instanceof ClassInstanceCreation){
			// cast the parallel node
			ClassInstanceCreation node2 = (ClassInstanceCreation) other;	
			
			// first check if both constructors have the same name
			boolean sameConstructorName = node.getType().subtreeMatch(defaultMatcher, node2.getType());
			
			if (sameConstructorName) {
				// resolve the bindings
				IMethodBinding constructor1 = node.resolveConstructorBinding();
				IMethodBinding constructor2 = node2.resolveConstructorBinding();
				// check if both constructors are equal
				boolean sameConstructors = (constructor1
						.isEqualTo(constructor2));
				// if we have the same constructor method
				if (sameConstructors) {
					assert (constructor1.getTypeParameters()
							.equals(constructor2.getTypeParameters()));

					// check for changes position of arguments
					boolean argumentPositionChanged = !(node.arguments()
							.equals(node2.arguments()));

					if (argumentPositionChanged) {
						this.mutop.found(node, node2);
						return true;
					}
				}
				// otherwise we have different methods
				else {
					// check if the different methods have the same number of arguments
					boolean sameArgumentNumber = (constructor1.getTypeParameters().length == constructor1.getTypeParameters().length);
					
					// if both have the sane argument length
					if(sameArgumentNumber){
						// we have to check for a different ordering
						// => we check if there is the same number of types declared in both parameters
						ITypeBinding[] parameterTypes1 = constructor1.getTypeParameters();
						ITypeBinding[] parameterTypes2 = constructor2.getTypeParameters();
						
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
						ITypeBinding[] parameterTypes1 = constructor1.getTypeParameters();
						ITypeBinding[] parameterTypes2 = constructor2.getTypeParameters();
						
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
		boolean reducedTypeNumbers = true;
		if(count1.size() > count2.size()){
			for(ITypeBinding parameterType: count1.keySet()){
				if(count1.get(parameterType).intValue() < count2.get(parameterType).intValue()){
					reducedTypeNumbers = false;
				}
			}
		}else{
			for(ITypeBinding parameterType: count2.keySet()){
				if(count1.get(parameterType).intValue() > count2.get(parameterType).intValue()){
					reducedTypeNumbers = false;
				}
			}			
		}
		return reducedTypeNumbers;
	}


	private boolean checkForSameTypes(HashMap<ITypeBinding, Integer> count1,
			HashMap<ITypeBinding, Integer> count2) {
		boolean sameTypeNumbers = true;
		for(ITypeBinding parameterType: count1.keySet()){
			if(count1.get(parameterType).intValue() != count2.get(parameterType).intValue()){
				sameTypeNumbers = false;
			}
		}
		for(ITypeBinding parameterType: count2.keySet()){
			if(count1.get(parameterType).intValue() != count2.get(parameterType).intValue()){
				sameTypeNumbers = false;
			}
		}
		return sameTypeNumbers;
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
