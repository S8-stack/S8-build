package com.s8.build;

import com.s8.core.io.json.JSON_Field;
import com.s8.core.io.json.JSON_Type;

@JSON_Type(name="build", sub= {})
public class S8BuildConfigurationFile {

	@JSON_Field(name="module")
	public String moduleName;

	@JSON_Field(name="target")
	public String targetName;

	@JSON_Field(name="dependencies")
	public String[] dependencies;


}
