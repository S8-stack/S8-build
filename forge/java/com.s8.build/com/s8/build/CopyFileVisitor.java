package com.s8.build;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class CopyFileVisitor implements FileVisitor<Path>  {

	/**
	 * 
	 */
	public final Path origin;

	
	/**
	 * 
	 */
	public final Path target;

	
	public CopyFileVisitor(Path origin, Path target) {
		this.origin = origin;  this.target = target;
	};

	@Override
	public FileVisitResult preVisitDirectory(Path originDirectory, BasicFileAttributes attrs) {
		// before visiting entries in a directory we copy the directory
		// (okay if directory already exists).
		Path targetDirectory = target.resolve(origin.relativize(originDirectory));
		try {
			/* dp not replace existing */
			Files.copy(originDirectory, targetDirectory);
		} catch (FileAlreadyExistsException x) {
			// ignore
		} catch (IOException x) {
			System.err.format("Unable to create: %s: %s%n", targetDirectory, x);
			return FileVisitResult.SKIP_SUBTREE;
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		Path newfile= target.resolve(origin.relativize(file));

		try {
			Files.copy(file, newfile, 
					StandardCopyOption.REPLACE_EXISTING, 
					StandardCopyOption.COPY_ATTRIBUTES);

		} catch (IOException x) {
			System.err.format("Unable to copy: %s: %s%n", origin, x);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exception) {
		if (exception == null) {
			Path newdir = target.resolve(origin.relativize(dir));
			try {
				FileTime time = Files.getLastModifiedTime(dir);
				Files.setLastModifiedTime(newdir, time);
			} catch (IOException x) {
				System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
			}
		}
		return FileVisitResult.CONTINUE;
	}


	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		if (exc instanceof FileSystemLoopException) {
			System.err.println("cycle detected: " + file);
		} else {
			System.err.format("Unable to copy: %s: %s%n", file, exc);
		}
		return FileVisitResult.CONTINUE;
	}

}
