package com.example.ruleflow;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Condition implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	private java.lang.String flag;

	public Condition() {
	}

	@Override
	public String toString() {
		return "Condition [flag=" + flag + "]";
	}

	public java.lang.String getFlag() {
		return this.flag;
	}

	public void setFlag(java.lang.String flag) {
		this.flag = flag;
	}

	public Condition(java.lang.String flag) {
		this.flag = flag;
	}

}