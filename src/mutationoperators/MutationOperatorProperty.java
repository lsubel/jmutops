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
	private boolean isDelete = false;
	
	/**
	 * True iff the mutation operator should detect insertions of statements
	 * Default is false.
	 */
	private boolean isInsert = false;
	
	/**
	 * True iff the mutation operator should detect movement of statements
	 * Default is false.
	 */
	private boolean isMove = false;
	
	/**
	 * True iff the mutation operator should detect update of statements
	 * Default is false.
	 */
	private boolean isUpdate = false;
	
	/**
	 * True iff the mutation operator should be able to handle one AST cases.
	 * Default is false.
	 */
	private boolean canOneAST = false;
	
	/**
	 * True iff the mutation operator should be able to handle two AST cases.
	 * Default is false.
	 */
	private boolean canTwoAST = false;
	
	/**
	 * True if the mutation operator should be able to check properties with all changes.
	 * Default is false;
	 */
	private boolean canPreCheck = false;
	
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
	
	public boolean isMove() {
		return isMove;
	}

	public void setMove() {
		this.isMove = true;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete() {
		this.isDelete = true;
	}

	public boolean isInsert() {
		return isInsert;
	}

	public void setInsert() {
		this.isInsert = true;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate() {
		this.isUpdate = true;
	}

	public boolean canHandleOneAST() {
		return canOneAST;
	}

	public void setCanOneAST() {
		this.canOneAST = true;
	}

	public boolean canHandleTwoASTs() {
		return canTwoAST;
	}

	public void setCanTwoAST() {
		this.canTwoAST = true;
	}
	
	public boolean isCanPreCheck() {
		return canPreCheck;
	}

	public void setCanPreCheck(boolean canPreCheck) {
		this.canPreCheck = canPreCheck;
	}

}