/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
*/
package com.redhat.arquillian.che;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

	private static String driverWinURL = "https://chromedriver.storage.googleapis.com/2.27/chromedriver_win32.zip";
	private static String driverLinuxURL = "https://chromedriver.storage.googleapis.com/2.27/chromedriver_linux64.zip";
	private static String driverMacURL = "https://chromedriver.storage.googleapis.com/2.27/chromedriver_mac64.zip";

	/**
	 * Gets text from a file.
	 * @param fileToPath path to a file to get text from
	 * @return text in a file
	 */
	public static String getTextFromFile(String fileToPath) {
		StringBuilder sb = new StringBuilder();
		List<String> json;
		try {
			json = Files.readAllLines(Paths.get(fileToPath), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read file with JSON for a new workspace", e);
		}
		for (String str : json) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * Gets text from a InputStream.
	 * @param fileStream inputstream of a file to get text from
	 * @return text in a file
	 */
	public static String getTextFromFile(InputStream fileStream) {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(fileStream))) {
			return buffer.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			throw new RuntimeException("Cannot read file", e);
		}
	}
}
