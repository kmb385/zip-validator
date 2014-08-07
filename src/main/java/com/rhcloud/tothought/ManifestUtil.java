package com.rhcloud.tothought;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ManifestUtil {

	public static List<String> createManifest(ZipInputStream zip) {
		List<String> zipContents = new ArrayList<String>();
		
		try {
			ZipEntry zipEntry= zip.getNextEntry();
			
			while(zipEntry != null){
				if(!zipEntry.isDirectory()){
					zipContents.add(normalizeFileName(zipEntry.getName()));					
				}
				zipEntry = zip.getNextEntry();
			}
			
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return zipContents;
	}

	public static List<String> createManifest(File file) {
		List<String> files = new ArrayList<String>();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				files.add(normalizeFileName(line));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return files;
	}

	public static String normalizeFileName(String fileName) {
		fileName = fileName.replaceAll("[\\\\/]", "/").trim();
		return fileName.startsWith("/") ? fileName.substring(1) : fileName;
	}

}
