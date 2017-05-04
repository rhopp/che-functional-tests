/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package redhat.che.e2e.tests.utils;

public class Constants {
	
	// Current values
	public static final String PROJECT_NAME = "vertx-http-booster";
	public static final String TEST_FILE = "HttpApplicationTest.java";
	public static final String[] PATH_TO_TEST_FILE = new String[] { "src", "test", "java",
	        "booster", TEST_FILE };

	// Path to resources
	public static final String CREATE_WORKSPACE_REQUEST_JSON = "src/main/resources/create-workspace-request.json";
	
	private static String getProperty(String propertyName, String defaultValue) {
		String property = System.getProperty(propertyName);
		if (property == null || property.isEmpty()) {
			return defaultValue;
		} else {
			return property;
		}
	}
}
