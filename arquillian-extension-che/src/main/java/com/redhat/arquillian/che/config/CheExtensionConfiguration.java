package com.redhat.arquillian.che.config;

public class CheExtensionConfiguration {

    // Properties
    public static final String CHE_STARTER_PROPERTY_NAME = "cheStarterURL";
    public static final String OPENSHIFT_MASTER_URL_PROPERTY_NAME = "openShiftMasterURL";
    public static final String KEYCLOAK_TOKEN_PROPERTY_NAME = "keycloakToken";
    public static final String OPENSHIFT_TOKEN_PROPERTY_NAME = "openShiftToken";
    public static final String PRESERVE_WORKSPACE_PROPERTY_NAME = "preserveWorkspace";
    public static final String OPENSHIFT_NAMESPACE_PROPERTY_NAME = "openShiftNamespace";
    public static final String CHE_WORKSPACE_URL_PROPERTY_NAME = "cheWorkspaceUrl";

    // Current values
    private String cheStarterUrl = System.getProperty(CHE_STARTER_PROPERTY_NAME);
    private String openshiftMasterUrl = System.getProperty(OPENSHIFT_MASTER_URL_PROPERTY_NAME);
    private String keycloakToken = System.getProperty(KEYCLOAK_TOKEN_PROPERTY_NAME);
    private String openshiftToken = System.getProperty(OPENSHIFT_TOKEN_PROPERTY_NAME);
    private String openshiftNamespace = System.getProperty(OPENSHIFT_NAMESPACE_PROPERTY_NAME, "eclipse-che");
    private String cheWorkspaceUrl = System.getProperty(CHE_WORKSPACE_URL_PROPERTY_NAME);
    private Boolean preserveWorkspace = Boolean.valueOf(System.getProperty(PRESERVE_WORKSPACE_PROPERTY_NAME));

    public String getCheStarterUrl() {
        return cheStarterUrl;
    }

    public void setCheStarterUrl(String cheStarterUrl) {
        this.cheStarterUrl = cheStarterUrl;
    }

    public String getOpenshiftMasterUrl() {
        return openshiftMasterUrl;
    }

    public void setOpenshiftMasterUrl(String openshiftMasterUrl) {
        this.openshiftMasterUrl = openshiftMasterUrl;
    }

    public String getKeycloakToken() {
        return keycloakToken;
    }

    public void setKeycloakToken(String keycloakToken) {
        this.keycloakToken = keycloakToken;
    }

    public String getOpenshiftToken() {
        return openshiftToken;
    }

    public void setOpenshiftToken(String openshiftToken) {
        this.openshiftToken = openshiftToken;
    }

    public String getOpenshiftNamespace() {
        return openshiftNamespace;
    }

    public void setOpenshiftNamespace(String openshiftNamespace) {
        this.openshiftNamespace = openshiftNamespace;
    }

    public String getCheWorkspaceUrl() {
        return cheWorkspaceUrl;
    }

    public void setCheWorkspaceUrl(String cheWorkspaceUrl) {
        this.cheWorkspaceUrl = cheWorkspaceUrl;
    }

    public Boolean getPreserveWorkspace() {
        return preserveWorkspace;
    }

    public void setPreserveWorkspace(Boolean preserveWorkspace) {
        this.preserveWorkspace = preserveWorkspace;
    }
}
