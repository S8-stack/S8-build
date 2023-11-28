package com.s8.build;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import com.s8.core.io.joos.JOOS_Lexicon;
import com.s8.core.io.joos.parsing.JOOS_ParsingException;
import com.s8.core.io.joos.types.JOOS_CompilingException;
import com.s8.core.io.joos.utilities.JOOS_BufferedFileReader;

public class S8ModuleBuilder extends S8CommandLauncher {


	public final static String ROOT_BUILD_DIR = "forge";

	public final static String SOURCES_PATH = "forge/java";

	public final static String CLASSES_PATH = "forge/classes";



	private String[] dependencies;

	private String jarName;

	private String moduleName;
	/**
	 * 
	 * @param JAVA_home
	 * @param cmdPathname
	 */
	public S8ModuleBuilder(String JAVA_home, String repositoryPathname) {
		super(JAVA_home, repositoryPathname);		
	}
	
	
	
	public void setConfig(String moduleName, String[] dependencies, String jarName) {
		this.dependencies = dependencies;
		this.moduleName = moduleName;
		this.jarName = jarName;
	}


	public void loadConfig() throws IOException, JOOS_CompilingException {

		
		JOOS_Lexicon context = JOOS_Lexicon.from(S8BuildConfigurationFile.class);

		RandomAccessFile file = new RandomAccessFile(getFile("build.js"), "r");

		S8BuildConfigurationFile config = null;

		try {
			JOOS_BufferedFileReader reader = new JOOS_BufferedFileReader(file.getChannel(), StandardCharsets.UTF_8, 64);

			config = (S8BuildConfigurationFile) context.parse(reader, true);
			reader.close();
		}
		catch (JOOS_ParsingException e) {
			e.printStackTrace();
		}
		finally {
			file.close();
		}

		System.out.println("\nConfig file loaded");
		
		/* set config */
		setConfig(config.moduleName, config.dependencies, config.jarName);
	}


	private String buildDependenciesPath() {
		StringBuilder dependenciesCmdBuilder = new StringBuilder();
		int n = dependencies.length;
		for(int i = 0; i<n; i++) {
			dependenciesCmdBuilder.append(dependencies[i]);
			if(i < n-1) { dependenciesCmdBuilder.append(":"); }
		}
		return dependenciesCmdBuilder.toString();
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
				"--module-path",
				buildDependenciesPath(), //"${dependencies}" />
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


	}


}
