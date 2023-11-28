package com.s8.build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.EnumSet;

public class S8CommandLauncher {


	public final static int MAX_DEPTHD_DEFAULT = 256;



	public final String JAVA_home;

	private Path path;

	private int maxDepth = MAX_DEPTHD_DEFAULT;



	/**
	 * 
	 * @param javaHome
	 */
	public S8CommandLauncher(String JAVA_home, String cmdPathname) {
		super();
		this.JAVA_home = JAVA_home;
		this.path = Paths.get(cmdPathname);
	}




	public void setMaxDepth(int depth) {
		this.maxDepth = depth;
	}


	public void setCmdPathname(String pathname) {
		this.path = Paths.get(pathname);
	}



	/**
	 * Delete all folder and files from root targetPathname
	 * @param targetPathname
	 * @param isVerbose
	 * @throws S8CmdException
	 * @throws IOException
	 */
	public void cleanUp(String targetPathname, boolean isVerbose) throws S8CmdException, IOException {

		/* path to the directory   */
		Path rootDirectoryPath = path.resolve(targetPathname);

		if(Files.exists(rootDirectoryPath)) {
			Files.walk(rootDirectoryPath) /* Traverse the file tree in depth-first order */
			.sorted(Comparator.reverseOrder())
			.forEach(path -> {
				try {
					if(isVerbose) { System.out.println("Deleting: " + path); }

					/* delete each file or directory */
					Files.delete(path);
				} catch (IOException e) {
					if(isVerbose) {
						e.printStackTrace();	
					}
				}
			});	
		}
	}


	/**
	 * 
	 * @param pathname
	 * @return
	 */
	public boolean isPresent(String pathname) {
		return Files.exists(path.resolve(pathname));
	}


	public void copyAll(String originPathname, String targetPathname) throws IOException {

		Path origin = path.resolve(originPathname);
		if(!Files.exists(origin)) { throw new IOException("Missing origin folder: "+originPathname); }

		Path target = path.resolve(targetPathname);

		/* create directory if not yet created */
		Files.createDirectories(target);


		CopyFileVisitor copyVisitor = new CopyFileVisitor(origin, target);
		EnumSet<FileVisitOption> fileVisitOptions = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		Files.walkFileTree(origin, fileVisitOptions, maxDepth, copyVisitor);
	}


	public void createDirectories(String... targetPathnames) throws IOException {
		for(String targetPathname : targetPathnames) {

			/* create path */
			Path target = path.resolve(targetPathname);

			/* create directory if not yet created */
			Files.createDirectories(target);	
		}
	}



	public void run(String... commands) throws IOException {

		ProcessBuilder processBuilder = new ProcessBuilder(commands);

		/*
		Map<String, String> env = pb.environment();
		env.put("VAR1", "myValue");
		env.remove("OTHERVAR");
		env.put("VAR2", env.get("VAR1") + "suffix");
		 */
		processBuilder.directory(path.toFile());

		Process p = processBuilder.start();

		String line = null;

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((line = inputReader.readLine()) != null) {
			System.out.println("\t > " + line);
		}

		BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		while ((line = errorReader.readLine()) != null) {
			System.out.println("\t > " + line);
		}

	}

}
