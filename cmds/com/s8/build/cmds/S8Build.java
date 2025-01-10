package com.s8.build.cmds;

import java.io.IOException;

import com.s8.build.S8CmdException;
import com.s8.build.S8ModuleBuilder;
import com.s8.core.io.json.types.JSON_CompilingException;


/**
 * 
 */
public class S8Build {
	

	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws S8CmdException
	 * @throws JSON_CompilingException
	 */
	public static void main(String[] args) throws IOException, S8CmdException, JSON_CompilingException {
		

		//String repo = args[0];
		String JAVA_home = "/Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home";
		
		
		String root = "/Users/pc/qx/git";
		
		String[] repositories = new String[] { 
				root + "/S8-api",
				
				/* I/O */
				root + "/S8-core-io-JSON",
				root + "/S8-core-io-xml",
				root + "/S8-core-io-csv",
				root + "/S8-core-io-bytes",
				
				/* BOHR */
				root + "/S8-core-bohr-atom",
				root + "/S8-core-bohr-beryllium",
				root + "/S8-core-bohr-lithium",
				root + "/S8-core-bohr-neodymium",
				root + "/S8-core-bohr-neon",
				
				/* ARCH */
				root + "/S8-core-arch-silicon",
				root + "/S8-core-arch-titanium",
				
				/* WEB */
				root + "/S8-core-web-helium",
				root + "/S8-core-web-carbon",
				root + "/S8-core-web-xenon",
				
				
				};
		
		String stack = "/Users/pc/qx/git/com.s8.stack/modules";
		
	
		
		for(String repo : repositories) {
			
			S8ModuleBuilder moduleBuilder = new S8ModuleBuilder(JAVA_home, repo, stack);
			moduleBuilder.loadConfig();
			moduleBuilder.build();
		}
		
	}
	
	
}
