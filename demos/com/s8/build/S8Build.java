package com.s8.build;

import java.io.IOException;

import com.s8.core.io.json.types.JSON_CompilingException;

public class S8Build {
	

	public static void main(String[] args) throws IOException, S8CmdException, JSON_CompilingException {
		

		//String repo = args[0];
		String JAVA_home = "/Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home";
		
		String[] repositories = new String[] { 
				"/Users/pc/qx/git/S8-api",
				
				/* I/O */
				"/Users/pc/qx/git/S8-core-io-joos",
				"/Users/pc/qx/git/S8-core-io-xml",
				"/Users/pc/qx/git/S8-core-io-csv",
				"/Users/pc/qx/git/S8-core-io-bytes",
				
				/* BOHR */
				"/Users/pc/qx/git/S8-core-bohr-atom",
				"/Users/pc/qx/git/S8-core-bohr-beryllium",
				"/Users/pc/qx/git/S8-core-bohr-lithium",
				"/Users/pc/qx/git/S8-core-bohr-neodymium",
				"/Users/pc/qx/git/S8-core-bohr-neon",
				
				/* ARCH */
				"/Users/pc/qx/git/S8-core-arch-silicon",
				"/Users/pc/qx/git/S8-core-arch-magnesium",
				
				/* WEB */
				"/Users/pc/qx/git/S8-core-web-helium",
				"/Users/pc/qx/git/S8-core-web-carbon",
				"/Users/pc/qx/git/S8-core-web-xenon",
				
				
				};
		
		String stack = "/Users/pc/qx/git/com.s8.stack/modules";
		
	
		
		for(String repo : repositories) {
			
			S8ModuleBuilder moduleBuilder = new S8ModuleBuilder(JAVA_home, repo, stack);
			moduleBuilder.loadConfig();
			moduleBuilder.build();
		}
		
	}
	
	
}
