package com.s8.build;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.build.lists.S8Modules;
import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.types.JSON_CompilingException;


/**
 * 
 */
public class S8Build {
	

	/**
	 * 
	 * @param JAVA_home
	 * @param root
	 * @param stack
	 * 
	 * @throws IOException
	 * @throws S8CmdException
	 * @throws JSON_CompilingException
	 * @throws S8BuildException
	 */
	public static void launch(String JAVA_home, String root, String stack) 
			throws IOException, S8CmdException, JSON_CompilingException, S8BuildException {
		
		
		String[] repositories = S8Modules.listRepositories(root);
		
		Path stackPath = Paths.get(stack);
		
	
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
