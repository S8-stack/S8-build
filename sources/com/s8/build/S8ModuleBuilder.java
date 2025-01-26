package com.s8.build;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.utilities.JOOS_BufferedFileReader;

public class S8ModuleBuilder {


	public final static String ROOT_BUILD_DIR = "forge";

	public final static String SOURCES_PATH = "forge/java";

	public final static String CLASSES_PATH = "forge/classes";


	public final static String JAR_EXTENSION = ".jar";
	
	
	public final static String STACK_MODULES_PATHNAME = "modules";
	
	public final static String STACK_WEBSOURCES_PATHNAME = "web-sources";
	

	
	public final String JAVA_home;
	
	/**
	 * 
	 */
	public final S8CommandLauncher cmd;

	private String dependencies;

	private String targetName;

	private String moduleName;
	
	

	private final Path repoPath;
	
	private final Path stackPath;

	private final Path stackModulesPath;

	private final Path stackWebSourcesPath;

	/**
	 * 
	 * 
	 * @param JAVA_home
	 * @param cmdPathname
	 */
	public S8ModuleBuilder(String JAVA_home, Path repositoryPath, Path stackPath) {
		super();
		
		this.JAVA_home = JAVA_home;
		this.repoPath = repositoryPath;
		
		this.stackPath = stackPath;
		this.stackModulesPath = stackPath.resolve(STACK_MODULES_PATHNAME);
		this.stackWebSourcesPath = stackPath.resolve(STACK_WEBSOURCES_PATHNAME);
		
		
		cmd = new S8CommandLauncher(repositoryPath);
		
	}



	/**
	 * 
	 * @param moduleName
	 * @param dependencies
	 * @param targetName
	 */
	private void setConfig(S8BuildConfigurationFile config) {
		

		this.moduleName = config.moduleName;
		this.targetName = config.targetName;
		
		StringBuilder dependenciesCmdBuilder = new StringBuilder();
		String[] dependencies = config.dependencies;
		int n = dependencies.length;
		for(int i = 0; i<n; i++) {
			String dependency = stackModulesPath.resolve(dependencies[i] + JAR_EXTENSION).toString();
			dependenciesCmdBuilder.append(dependency);
			if(i < n-1) { dependenciesCmdBuilder.append(":"); }
		}
		this.dependencies = dependenciesCmdBuilder.toString();

	}


	/**
	 * 
	 * @param context
	 * @throws S8BuildException
	 */
	private void loadConfig(JSON_Lexicon context) throws S8BuildException {
		try {
			
			/* prepare access to file */
			RandomAccessFile file = new RandomAccessFile(cmd.getFile("build.js"), "r");
			
			/* build JSON reader */
			JOOS_BufferedFileReader reader = new JOOS_BufferedFileReader(file.getChannel(), StandardCharsets.UTF_8, 64);
			
			/* read config */
			S8BuildConfigurationFile config = (S8BuildConfigurationFile) context.parse(reader, true);
			reader.close();
			
			/* close file */
			file.close();

			/* set config */
			setConfig(config);
		} 
		catch (IOException e) {
			e.printStackTrace();
			throw new S8BuildException("Failed to load config load, due to: " + e.getMessage());
		}


		System.out.println("\nConfig file loaded");

	}



	/**
	 * 
	 * @throws IOException
	 * @throws S8CmdException 
	 * @throws S8BuildException 
	 */
	public void build(JSON_Lexicon context) throws IOException, S8CmdException, S8BuildException {
		

		System.out.println("---- <building module: "+repoPath.toString()+" > ----");
		
		loadConfig(context);

		cmd.cleanUp("forge", false);

		cmd.createDirectories("forge/java", "forge/classes");

		cmd.copyAll("sources", "forge/java/" + moduleName);

		if(cmd.isPresent("demos")) {
			cmd.copyAll("demos", "forge/java/" + moduleName);			
		}

		if(cmd.isPresent("tools")) {
			cmd.copyAll("tools", "forge/java/" + moduleName);			
		}
		
		

		/**
		 * <arg value="--module-path" />
		<arg value="${dependencies}" />
		<arg value="-d" />
		<arg value="forge/classes" />
		<arg value="--module-source-path" />
		<arg value="forge/java" />
		<arg value="--module" />
		<arg value="${module}" />
		 */

		cmd.run(JAVA_home + "/bin/javac",
				//path.toFile().getPath(), /* , dir="${basedir}"> */
				"--module-path", dependencies, //"${dependencies}" />
				"-d", CLASSES_PATH,
				"--module-source-path", SOURCES_PATH,
				"--module", moduleName).print("compile");
		

		/*
		<target name="package-jar" depends="compile">
		<exec executable="${javahome}/bin/jar" dir="${basedir}">
			<arg value="-c" />
			<arg value="--file=${jar-path}" />
			<arg value="-C" />
			<arg value="forge/classes/${module}" />
			<arg value="." />
		</exec>
	</target>
		 */
		
		String jarAbsolutePathname = stackModulesPath.resolve(targetName + JAR_EXTENSION).toString();

		cmd.run(JAVA_home + "/bin/jar",
				//path.toFile().getPath(), /* , dir="${basedir}"> */
				"-c",
				"--file="+jarAbsolutePathname, //"${dependencies}" />
				"-C", 
				"forge/classes/"+moduleName,
				".").print("create jar");
		
		
		/* <web-sources> */
		
		if(cmd.isPresent("web-sources")) {
			if(cmd.isPresent("web-sources/"+targetName)) {
				
				S8CommandLauncher cmd2 = new S8CommandLauncher(stackWebSourcesPath);

				cmd2.createDirectories(targetName);
				
				cmd2.cleanUp(targetName, false);
				
				S8BuildUtilities.copyAll(
						repoPath.resolve(STACK_WEBSOURCES_PATHNAME).resolve(targetName), 
						stackWebSourcesPath.resolve(targetName), 64);
				
			}
			else {
				System.err.println("web-sources are missing!");
			}
			//cmd.copyAll("demos", "forge/java/" + moduleName);			
		}

		/* </web-sources> */

		System.out.println("---- </building module: "+repoPath.toString()+" > ----\n\n");

	}


}
