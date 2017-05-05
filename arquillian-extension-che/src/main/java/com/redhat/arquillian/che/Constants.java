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
package com.redhat.arquillian.che;

public class Constants {
    // Properties
    public static final String CHE_STARTER_PROPERTY_NAME = "cheStarterURL";
    public static final String OPENSHIFT_MASTER_URL_PROPERTY_NAME = "openShiftMasterURL";
    public static final String KEYCLOAK_TOKEN_PROPERTY_NAME = "keycloakToken";
    public static final String OPENSHIFT_TOKEN_PROPERTY_NAME = "openShiftToken";
    public static final String PRESERVE_WORKSPACE_PROPERTY_NAME = "preserveWorkspace";
    public static final String OPENSHIFT_NAMESPACE_PROPERTY_NAME = "openShiftNamespace";
    public static final String CHE_WORKSPACE_URL_PROPERTY_NAME = "cheWorkspaceUrl";

    // Current values
    public static final String CHE_STARTER_URL = System.getProperty(CHE_STARTER_PROPERTY_NAME);
    public static final String OPENSHIFT_MASTER_URL = System.getProperty(OPENSHIFT_MASTER_URL_PROPERTY_NAME);
    public static final String KEYCLOAK_TOKEN = System.getProperty(KEYCLOAK_TOKEN_PROPERTY_NAME);
    public static final String OPENSHIFT_TOKEN = System.getProperty(OPENSHIFT_TOKEN_PROPERTY_NAME);
    public static final String OPENSHIFT_NAMESPACE = System.getProperty(OPENSHIFT_NAMESPACE_PROPERTY_NAME, "eclipse-che");
    public static final String CHE_WORKSPACE_URL = System.getProperty(CHE_WORKSPACE_URL_PROPERTY_NAME);

    // Path to resources
    public static final String CREATE_WORKSPACE_REQUEST_JSON = "create-workspace-request.json";
}
