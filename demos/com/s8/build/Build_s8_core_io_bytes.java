package com.s8.build;

import java.io.IOException;

public class Build_s8_core_io_bytes {
	
	public final static String JAVA_HOME = "/Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home";
	

	public static void main(String[] args) throws IOException, S8CmdException {
		

		String stack = "/Users/pc/qx/git/com.s8.stack/modules";
		
		String module = "com.s8.core.io.bytes";
		
		String repo = "/Users/pc/qx/git/s8-core-io-bytes";
		
		String[] dependencies = new String[] { stack+"/s8-api.jar" };
			
		String jar = stack + "/s8-core-io-bytes.jar";
		
		S8ModuleBuilder moduleBuilder = new S8ModuleBuilder(JAVA_HOME, repo);
		moduleBuilder.setConfig(module, dependencies, jar);
		moduleBuilder.build();
		
		
		
	}
	
}
