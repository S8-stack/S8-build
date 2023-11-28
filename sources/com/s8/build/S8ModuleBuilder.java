package com.s8.build;

import java.io.IOException;

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
	public S8ModuleBuilder(String JAVA_home, 
			String moduleName,
			String repositoryPathname,
			String[] dependencies,
			String jarName) {
		super(JAVA_home, repositoryPathname);
		
		this.dependencies = dependencies;
		this.moduleName = moduleName;
		this.jarName = jarName;
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

		copyAll("demos", "forge/java/" + moduleName);


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
