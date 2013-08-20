package mutationoperators;

import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class MutationOperatorProperty {
	
	//////////////////////////////////////////////////////////////
	///		Fields
	//////////////////////////////////////////////////////////////
	
	/**
	 * Stores the full name of the mutation operator.
	 */
	private String fullname;
	
	/**
	 * Short cut of the mutation operator.
	 */
	private String shortname;
	
	/**
	 * A description of the mutation operator's effect.
	 */
	private String description;
	
	/**
	 * Stores an enum to distinguish between classlevel operators and methodlevel operators.
	 */
	private MutationOperatorLevel level;
	
	/**
	 * Stores the category of the mutation operator.
	 */
	private MutationOperatorCategory category;
	
	/**
	 * True iff the mutation operator should detect deletion of statements
	 * Default is false.
	 */
	private boolean canHandleDeleteChanges = false;
	
	/**
	 * True iff the mutation operator should detect insertions of statements
	 * Default is false.
	 */
	private boolean canHandleInsertChanges = false;
	
	/**
	 * True iff the mutation operator should detect movement of statements
	 * Default is false.
	 */
	private boolean canHandleMoveChanges = false;
	
	/**
	 * True iff the mutation operator should detect update of statements
	 * Default is false.
	 */
	private boolean canHandleUpdateChanges = false;
	
	/**
	 * True iff the mutation operator should be able to handle one AST cases.
	 * Default is false.
	 */
	private boolean canHandleOneAST = false;
	
	/**
	 * True iff the mutation operator should be able to handle two AST cases.
	 * Default is false.
	 */
	private boolean canHandleTwoASTs = false;
	
	/**
	 * True if the mutation operator should be able to check properties with all changes.
	 * Default is false;
	 */
	private boolean canHandlePreCheck = false;
	
	//////////////////////////////////////////////////////////////
	///		Methods
	//////////////////////////////////////////////////////////////	

	public MutationOperatorProperty(){
	}
	
	public MutationOperatorProperty(String fullname, String shortname,
			String description, MutationOperatorLevel level,
			MutationOperatorCategory category) {
		this.fullname = fullname;
		this.shortname = shortname;
		this.description = description;
		this.level = level;
		this.category = category;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MutationOperatorLevel getLevel() {
		return level;
	}

	public void setLevel(MutationOperatorLevel level) {
		this.level = level;
	}

	public MutationOperatorCategory getCategory() {
		return category;
	}

	public void setCategory(MutationOperatorCategory category) {
		this.category = category;
	}
	
	public boolean canMove() {
		return canHandleMoveChanges;
	}

	public void setMove() {
		this.canHandleMoveChanges = true;
	}

	public boolean canDelete() {
		return canHandleDeleteChanges;
	}

	public void setDelete() {
		this.canHandleDeleteChanges = true;
	}

	public boolean canInsert() {
		return canHandleInsertChanges;
	}

	public void setInsert() {
		this.canHandleInsertChanges = true;
	}

	public boolean canUpdate() {
		return canHandleUpdateChanges;
	}

	public void setUpdate() {
		this.canHandleUpdateChanges = true;
	}

	public boolean canHandleOneAST() {
		return canHandleOneAST;
	}

	public void setOneAST() {
		this.canHandleOneAST = true;
	}

	public boolean canHandleTwoASTs() {
		return canHandleTwoASTs;
	}
	
	public void setTwoAST() {
		this.canHandleTwoASTs = true;
	}
	
	public boolean canHandlePreCheck() {
		return canHandlePreCheck;
	}

	public void setPreCheck() {
		this.canHandlePreCheck = true;
	}

}