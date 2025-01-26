package com.s8.build;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

public class S8BuildUtilities {


	/**
	 * 
	 * @param origin
	 * @param target
	 * @param maxDepth (default value should be 64)
	 * @throws IOException
	 */
	public static void copyAll(Path origin, Path target, int maxDepth) throws IOException {
		if(!Files.exists(origin)) { throw new IOException("Missing origin folder: "+origin.toString()); }


		/* create directory if not yet created */
		Files.createDirectories(target);


		CopyFileVisitor copyVisitor = new CopyFileVisitor(origin, target);
		EnumSet<FileVisitOption> fileVisitOptions = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		Files.walkFileTree(origin, fileVisitOptions, maxDepth, copyVisitor);	
	}
}
