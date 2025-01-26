package com.s8.build.cmds;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.build.S8BuildConfigurationFile;
import com.s8.build.S8BuildException;
import com.s8.build.S8CmdException;
import com.s8.build.S8ModuleBuilder;
import com.s8.core.io.json.JSON_Lexicon;
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
	 * @throws S8BuildException 
	 */
	public static void main(String[] args) throws IOException, S8CmdException, JSON_CompilingException, S8BuildException {
		

		//String repo = args[0];
		String JAVA_home = "/Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home";
		
		
		String root = "/Users/pc/qx/git";
		
		String[] repositories = S8CoreList.listRepositories(root);
		
		Path stackPath = Paths.get("/Users/pc/qx/git/S8-build/builds/v4/core");
		
	
		JSON_Lexicon context;
		try {
			context = JSON_Lexicon.from(S8BuildConfigurationFile.class);
		} catch (JSON_CompilingException e) {
			e.printStackTrace();
			throw new S8BuildException("Failed to build S8BuildConfigurationFile context");
		}
		
		for(String repoPathname : repositories) {
			Path repoPath = Paths.get(repoPathname);
			S8ModuleBuilder moduleBuilder = new S8ModuleBuilder(JAVA_home, repoPath, stackPath);
			moduleBuilder.build(context);
		}
	}
	
	
}
