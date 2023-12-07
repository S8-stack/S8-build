package com.s8.build;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.json.utilities.JOOS_BufferedFileReader;

public class S8ModuleBuilder extends S8CommandLauncher {


	public final static String ROOT_BUILD_DIR = "forge";

	public final static String SOURCES_PATH = "forge/java";

	public final static String CLASSES_PATH = "forge/classes";


	public final static String JAR_EXTENSION = ".jar";
	

	private String dependencies;

	private String jarName;

	private String moduleName;
	
	private Path stackPath;
	
	/**
	 * 
	 * 
	 * @param JAVA_home
	 * @param cmdPathname
	 */
	public S8ModuleBuilder(String JAVA_home, String repositoryPathname, String stackPathname) {
		super(JAVA_home, repositoryPathname);
		this.stackPath = Paths.get(stackPathname);
	}
	
	
	
	public void setConfig(String moduleName, String[] dependencies, String jarName) {
		StringBuilder dependenciesCmdBuilder = new StringBuilder();
		int n = dependencies.length;
		for(int i = 0; i<n; i++) {
			String dependency = stackPath.resolve(dependencies[i] + JAR_EXTENSION).toString();
			dependenciesCmdBuilder.append(dependency);
			if(i < n-1) { dependenciesCmdBuilder.append(":"); }
		}
		this.dependencies = dependenciesCmdBuilder.toString();
		
		this.moduleName = moduleName;
		this.jarName = stackPath.resolve(jarName + JAR_EXTENSION).toString();
	}


	public void loadConfig() throws IOException, JSON_CompilingException {

		
		JSON_Lexicon context = JSON_Lexicon.from(S8BuildConfigurationFile.class);

		RandomAccessFile file = new RandomAccessFile(getFile("build.js"), "r");

		S8BuildConfigurationFile config = null;

		try {
			JOOS_BufferedFileReader reader = new JOOS_BufferedFileReader(file.getChannel(), StandardCharsets.UTF_8, 64);

			config = (S8BuildConfigurationFile) context.parse(reader, true);
			reader.close();
		}
		catch (JSON_ParsingException e) {
			e.printStackTrace();
		}
		finally {
			file.close();
		}

		System.out.println("\nConfig file loaded");
		
		/* set config */
		setConfig(config.moduleName, config.dependencies, config.targetName);
	}



	/**
	 * 
	 * @throws IOException
	 * @throws S8CmdException 
	 */
	public void build() throws IOException, S8CmdException {

		cleanUp("forge", false);

		createDirectories("forge/java", "forge/classes");

		copyAll("sources", "forge/java/" + moduleName);

		if(isPresent("demos")) {
			copyAll("demos", "forge/java/" + moduleName);			
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

		run(JAVA_home + "/bin/javac",
				//path.toFile().getPath(), /* , dir="${basedir}"> */
				"--module-path", dependencies, //"${dependencies}" />
				"-d", CLASSES_PATH,
				"--module-source-path", SOURCES_PATH,
				"--module", moduleName);


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

		run(JAVA_home + "/bin/jar",
				//path.toFile().getPath(), /* , dir="${basedir}"> */
				"-c",
				"--file="+jarName, //"${dependencies}" />
				"-C", 
				"forge/classes/"+moduleName,
				".");
		
		System.out.println("---- Module: "+moduleName+" is now compiled -----\n\n");


	}


}
