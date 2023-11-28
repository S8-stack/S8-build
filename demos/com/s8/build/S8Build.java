package com.s8.build;

import java.io.IOException;

import com.s8.core.io.joos.types.JOOS_CompilingException;

public class S8Build {
	
	public final static String JAVA_HOME = "/Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home";
	

	public static void main(String[] args) throws IOException, S8CmdException, JOOS_CompilingException {
		

		//String repo = args[0];
		String repo = "/Users/pc/qx/git/s8-core-io-joos";
		
		S8ModuleBuilder moduleBuilder = new S8ModuleBuilder(JAVA_HOME, repo);
		moduleBuilder.loadConfig();
		moduleBuilder.build();
		
		
		
	}
	
}
