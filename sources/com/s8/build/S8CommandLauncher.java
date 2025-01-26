package com.s8.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.EnumSet;

public class S8CommandLauncher {


	public final static int MAX_DEPTHD_DEFAULT = 256;



	public final Path path;

	private int maxDepth = MAX_DEPTHD_DEFAULT;



	/**
	 * 
	 * @param javaHome
	 */
	public S8CommandLauncher(Path path) {
		super();
		this.path = path;
	}




	public void setMaxDepth(int depth) {
		this.maxDepth = depth;
	}


	public File getFile(String pathanme) {
		return path.resolve(pathanme).toFile();
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



	public CmdOutput run(String... commandWords) throws IOException {

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(commandWords);

			/*
		Map<String, String> env = pb.environment();
		env.put("VAR1", "myValue");
		env.remove("OTHERVAR");
		env.put("VAR2", env.get("VAR1") + "suffix");
			 */
			processBuilder.directory(path.toFile());

			Process p = processBuilder.start();

			//wait for the child process to end before proceeding
			p.waitFor();


			return new CmdOutput(
					p.exitValue(), 
					readMessage(p.getInputStream()),
					readMessage(p.getErrorStream()));

		} catch (InterruptedException e) {
			return new CmdOutput(
					-64, 
					"",
					"Interrupted : "+e.getMessage());
		}


	}

	public class CmdOutput {
		public final int exitValue;
		public final String inputMessage;
		public final String errorMessage;

		public CmdOutput(int exitValue, String inputMessage, String errorMessage) {
			super();
			this.exitValue = exitValue;
			this.inputMessage = inputMessage;
			this.errorMessage = errorMessage;
		}

		public void print(String name) {
			if(exitValue == 0) {
				System.out.println(name + " successful : (exit code = "+exitValue+")");
				if(inputMessage != null) { System.out.println(inputMessage); }
				if(errorMessage != null) { System.out.println(errorMessage); }
			}
			else {
				System.out.println(name + " failed : (exit code = "+exitValue+")");
				if(inputMessage != null) { System.out.println(inputMessage); }
				if(errorMessage != null) { System.err.println(errorMessage); }	
			}
		}
	}


	private static String readMessage(InputStream inputStream) throws IOException {
		String line = null;

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder builder = null;
		
		int c = 0;
		while ((line = inputReader.readLine()) != null) {
			if(builder == null) { builder = new StringBuilder(); }
			if(c++ > 0) { builder.append("\n"); }
			builder.append("\t > ");
			builder.append(line);	
		}
		return builder != null ? builder.toString() : null;
	}



}
