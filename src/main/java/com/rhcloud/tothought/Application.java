package com.rhcloud.tothought;

import java.io.File;
import java.util.List;

import com.rhcloud.tothought.Validator.ValidationResult;

public class Application {

	private static String manifestFilePath;
	private static String zipFilePath;
	private static String options;

	private static File manifestFile;
	private static File zipFile;

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			throw new RuntimeException(
					"\nIncorrect arguments specified, please provide manifest file path and zip file path.\n"
							+ "Example Usage: java -zip-validator.jar manifestFile.txt zipFile.zip");
		}
		
		try {
			setup(args);

			Validator validator = Validator.createInstance(manifestFile, zipFile);
			ValidationResult result = validator.validate();

			if (result.isValid()) {
				System.out.printf("%s file validated successfully.\n", zipFile.getName());
			} else {
				System.out.printf("Errors occurred while validating %s.\n", zipFile.getName());

				listFiles(result.getMissingFiles(), "Missing Files:");
				listFiles(result.getExtraFiles(), "Extra Files:");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	private static void listFiles(List<String> fileList, String type) {
		if (!fileList.isEmpty()) {
			System.out.println(type);

			int count = 0;
			for (String file : fileList) {
				System.out.printf("%d.  %s\n", ++count, file);
			}
		}
	}

	private static void setup(String[] args) {
		extractParameterValues(args);
		validateParameterValues();
		manifestFile = new File(manifestFilePath);
		zipFile = new File(zipFilePath);

		validateFile(manifestFile);
		validateFile(zipFile);
	}

	private static void validateParameterValues() {
		if (!manifestFilePath.endsWith(".txt")) {
			throw new RuntimeException("Manifest file must be of type .txt");
		} else if (!zipFilePath.endsWith(".zip") && !zipFilePath.endsWith("zipx")) {
			throw new RuntimeException("Zip file must be of type .zip or .zipx");
		}
	}

	private static void validateFile(File file) {
		if (!file.exists()) {
			throw new RuntimeException(file.getPath() + " not found.");
		}
	}

	private static void extractParameterValues(String[] args) {
		manifestFilePath = args[0];
		zipFilePath = args[1];

		if (args.length == 3) {
			options = args[2];
		}
	}

	public static String getFileManifestPath() {
		return manifestFilePath;
	}

	public static void setFileManifestPath(String fileManifestPath) {
		Application.manifestFilePath = fileManifestPath;
	}

	public static String getZipFilePath() {
		return zipFilePath;
	}

	public static void setZipFilePath(String zipFilePath) {
		Application.zipFilePath = zipFilePath;
	}

	public static String getOptions() {
		return options;
	}

	public static void setOptions(String options) {
		Application.options = options;
	}
}
