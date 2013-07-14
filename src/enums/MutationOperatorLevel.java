package enums;

public enum MutationOperatorLevel {
	METHOD_LEVEL("Method-level operator"), 
	CLASS_LEVEL("Class-level operator");
	
	private final String label;
	
	private MutationOperatorLevel(String label){
		this.label = label;
	}
	
	public String toString(){
		return this.label;
	}
}