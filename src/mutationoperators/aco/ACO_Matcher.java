package mutationoperators.aco;

import java.util.HashMap;

import mutationoperators.TwoASTMatcher;
import mutationoperators.MutationOperator;

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
			
			// first check if both methods have the same method name
			boolean sameMethodName = node.getName().subtreeMatch(defaultMatcher, node2.getName());
			
			if(sameMethodName){
				// resolve the bindings
				IMethodBinding method1 = node.resolveMethodBinding();
				IMethodBinding method2 = node2.resolveMethodBinding();
				
				// check if both methods are equal
				boolean sameMethods = method1.isEqualTo(method2);
				
				if(sameMethods){
					assert method1.getParameterTypes().equals(method2.getParameterTypes());
					
					// check for changes position of arguments
					boolean argumentPositionChanged = !(node.arguments().equals(node2.arguments()));

					if (argumentPositionChanged) {
						this.mutop.found(node, node2);
						return true;
					}
				}
				// otherwise we have different methods
				else{
					// check if the different constructors have the same number of arguments
					boolean sameArgumentNumber = (method1.getParameterTypes().length == method2.getParameterTypes().length);
					
					// if both have the sane argument length
					if(sameArgumentNumber){
						// we have to check for a different ordering
						// => we check if there is the same number of types declared in both parameters
						ITypeBinding[] parameterTypes1 = method1.getParameterTypes();
						ITypeBinding[] parameterTypes2 = method2.getParameterTypes();
						
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
						ITypeBinding[] parameterTypes1 = method1.getTypeParameters();
						ITypeBinding[] parameterTypes2 = method2.getTypeParameters();
						
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
					assert (constructor1.getParameterTypes()
							.equals(constructor2.getParameterTypes()));

					// check for changes position of arguments
					boolean argumentPositionChanged = !(node.arguments()
							.equals(node2.arguments()));

					if (argumentPositionChanged) {
						this.mutop.found(node, node2);
						return true;
					}
				}
				// otherwise we have different constructors
				else {
					// check if the different constructors have the same number of arguments
					boolean sameArgumentNumber = (constructor1.getParameterTypes().length == constructor2.getParameterTypes().length);
					
					// if both have the sane argument length
					if(sameArgumentNumber){
						// we have to check for a different ordering
						// => we check if there is the same number of types declared in both parameters
						ITypeBinding[] parameterTypes1 = constructor1.getParameterTypes();
						ITypeBinding[] parameterTypes2 = constructor2.getParameterTypes();
						
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
				Integer i2 = count2.get(parameterType);
				if((i2 != null) || (count1.get(parameterType).intValue() > count2.get(parameterType).intValue())){
					reducedTypeNumbers = false;
				}
			}
		}else{
			for(ITypeBinding parameterType: count2.keySet()){
				Integer i1 = count1.get(parameterType);
				if((i1 != null) || (count1.get(parameterType).intValue() > count2.get(parameterType).intValue())){
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
			Integer i1 = count1.get(parameterType);
			Integer i2 = count2.get(parameterType);
			if((i1 == null) || (i2 == null) || (count1.get(parameterType).intValue() != count2.get(parameterType).intValue())){
				sameTypeNumbers = false;
			}
		}
		for(ITypeBinding parameterType: count2.keySet()){
			Integer i1 = count1.get(parameterType);
			Integer i2 = count2.get(parameterType);
			if((i1 == null) || (i2 == null) || (count1.get(parameterType).intValue() != count2.get(parameterType).intValue())){
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
