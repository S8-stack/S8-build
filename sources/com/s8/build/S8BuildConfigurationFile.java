package com.s8.build;

import com.s8.core.io.joos.JOOS_Field;
import com.s8.core.io.joos.JOOS_Type;

@JOOS_Type(name="build", sub= {})
public class S8BuildConfigurationFile {

	@JOOS_Field(name="module")
	public String moduleName;

	@JOOS_Field(name="jar")
	public String jarName;

	@JOOS_Field(name="dependencies")
	public String[] dependencies;


}
