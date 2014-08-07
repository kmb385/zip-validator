package com.rhcloud.tothought;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class ApplicationTest {
	
	@Test
	public void parameterTest() throws Exception {
		String manifestFilePath = "src/test/resources/fileManifest.txt";
		String zipFilePath = "src/test/resources/src.zip";

		Application.main(new String[]{manifestFilePath, zipFilePath});
		
		assertEquals(manifestFilePath, Application.getFileManifestPath());
		assertEquals(zipFilePath, Application.getZipFilePath());
	}

	@Test(expected=Exception.class)
	public void fileTest() throws Exception {
		Application.main(new String[]{"manifestFilePath", "zipFilePath"});
	}
}
