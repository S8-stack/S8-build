package com.s8.build.js;

public class JS_CodeGenerator {
	
	
	private final StringBuilder builder;
	
	

	
	private String indentPrefix = "";
	
	
	

	public JS_CodeGenerator() {
		super();
		this.builder = new StringBuilder();
	}


	public void indent() {
		indentPrefix = indentPrefix + '\t';
	}
	
	public void outdent() {
		int n = indentPrefix.length();
		if(n > 0) {
			indentPrefix = indentPrefix.substring(0, n-1);	
		}
	}


	
	public void appendLine(String code) {
		builder.append(indentPrefix);
		builder.append(code);
		builder.append('\n');	
	}
	
	
	/**
	 * 
	 * @param comment
	 */
	public void appendCommentLine(String comment) {
		builder.append(indentPrefix);
		builder.append("/* ");
		builder.append(comment);
		builder.append(" */\n");
	}
	
	
	public void skipLine() {
		builder.append('\n');
	}
	
	
	/**
	 * 
	 * @param nLines
	 */
	public void skipLines(int nLines) {
		for(int i = 0; i<nLines; i++) { builder.append('\n'); }
	}
	
	/**
	 * 
	 * @param comment
	 */
	public void appendFuncCommentLine(String comment) {
		builder.append(indentPrefix);
		builder.append("/** ");
		builder.append(comment);
		builder.append(" */\n");
	}

	
	public String getCode() {
		return builder.toString();
	}
	
	
	
	
	public void appendEnumByCodeFunc(String comment, String func, JS_Enum[] items) {
		appendFuncCommentLine(comment);
		
		
		appendLine(func + " = function(code) {");
		indent();
		appendLine("switch(code) {");
		indent();
		for(JS_Enum item : items) {
			appendSwitchCase(item);
		}
		outdent();
		appendLine("}");
		outdent();
		appendLine("};");
	}
	
	
	

	/**
	 * 
	 * @param builder
	 * @param key
	 * @param value
	 * @param comment
	 */
	public void appendSwitchCase(JS_Enum item) {
		appendSwitchCase(
				item.isDefault(),
				"0x" + Integer.toHexString(item.getKey()),
				item.getValue(),
				item.getComment());
	}
	
	

	/**
	 * 
	 * @param builder
	 * @param key
	 * @param value
	 * @param comment
	 */
	public void appendSwitchCase(boolean isDefault, String key, String value, String comment) {
		
		if(isDefault) {
			builder.append(indentPrefix);
			builder.append("default : \n");
		}
		
		builder.append(indentPrefix);
		builder.append("case ");
		builder.append(key);
		
		builder.append(" : return ");
		builder.append('\"');
		builder.append(value);
		builder.append('\"');
		
		if(comment != null) {
			builder.append("; /* ");
			builder.append(comment);
			builder.append(" */");
		}
		builder.append("\n");
	}
	
	
	
	/**
	 * 
	 * @param builder
	 * @param key
	 * @param value
	 * @param comment
	 */
	public void appendSwitchDefaultCase(String value, String comment) {
		skipLine();
		builder.append(indentPrefix);
		builder.append("default : return ");
		builder.append(value);
		builder.append("; /* ");
		builder.append(comment);
		builder.append(" */\n");
	}
	
}
