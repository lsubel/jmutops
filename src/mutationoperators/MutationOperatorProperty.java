package mutationoperators;

import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class MutationOperatorProperty {
	
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
	 * True iff the mutation operator should detect movement of statements
	 */
	private boolean isMove;
	
	public boolean isMove() {
		return isMove;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

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
}