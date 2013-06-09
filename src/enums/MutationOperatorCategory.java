package enums;

public enum MutationOperatorCategory {
	METHOD_LEVEL("Method-level operator"), 
	CLASS_LEVEL("Class-level operator");
	
	private final String label;
	
	private MutationOperatorCategory(String label){
		this.label = label;
	}
	
	public String toString(){
		return this.label;
	}
}