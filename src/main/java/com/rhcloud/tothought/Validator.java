package com.rhcloud.tothought;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class Validator {

	/* File containing the required list of files */
	private File manifestFile;

	/* ZipInputStream containg the actual files to validate */
	private ZipInputStream zipFile;

	/*
	 * List containing the files not found in the zipFile but required by the
	 * manifestFile
	 */
	private List<String> missingFiles = new ArrayList<String>();
	
	/*
	 * List containg the files in the zipFile but not listed on the manifest file.
	 */
	private List<String> extraFiles = new ArrayList<String>();

	private Validator(File manifestFile, ZipInputStream zipFile) {
		this.zipFile = zipFile;
		this.manifestFile = manifestFile;
	}

	public static Validator createInstance(File manifestFile, File zipFile) {
		ZipInputStream zis;
		try {
			zis = new ZipInputStream(new FileInputStream(zipFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot locate zipFile: " + zipFile.getAbsolutePath());
		}
		return new Validator(manifestFile, zis);
	}

	public ValidationResult validate() {
		List<String> zipManifest = ManifestUtil.createManifest(zipFile);
		List<String> filesManifest = ManifestUtil.createManifest(manifestFile);

		this.findMissingFiles(zipManifest, filesManifest, this.missingFiles);
		this.findMissingFiles(filesManifest, zipManifest, this.extraFiles);
		
		return new ValidationResult(this.missingFiles.isEmpty() && this.extraFiles.isEmpty(), this.missingFiles, this.extraFiles);
	}

	private void findMissingFiles(List<String> actualFiles, List<String> requiredFiles, List<String> results) {
		for (String entry : requiredFiles) {
			if (!actualFiles.contains(entry)) {
				results.add(entry);
			}
		}
	}

	public class ValidationResult {

		private final boolean isValid;
		private final List<String> missingFiles;
		private final List<String> extraFiles;
		
		public ValidationResult(boolean isValid, List<String> missingFiles, List<String> extraFiles) {
			this.isValid = isValid;
			this.missingFiles = missingFiles;
			this.extraFiles = extraFiles;
		}

		public boolean isValid() {
			return isValid;
		}

		public List<String> getMissingFiles() {
			return missingFiles;
		}

		public List<String> getExtraFiles() {
			return extraFiles;
		}

	}

}
