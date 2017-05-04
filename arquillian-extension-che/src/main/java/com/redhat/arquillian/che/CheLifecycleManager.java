package com.redhat.arquillian.che;

import com.redhat.arquillian.che.provider.CheWorkspaceProvider;
import com.redhat.arquillian.che.resource.CheWorkspace;
import com.redhat.arquillian.che.resource.CheWorkspaceStatus;
import com.redhat.arquillian.che.service.CheWorkspaceService;
import org.apache.log4j.Logger;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

import static com.redhat.arquillian.che.Constants.CHE_STARTER_URL;
import static com.redhat.arquillian.che.Constants.KEYCLOAK_TOKEN;
import static com.redhat.arquillian.che.Constants.OPENSHIFT_MASTER_URL;
import static com.redhat.arquillian.che.Constants.OPENSHIFT_NAMESPACE;
import static com.redhat.arquillian.che.Constants.OPENSHIFT_TOKEN;
import static com.redhat.arquillian.che.Constants.PRESERVE_WORKSPACE_PROPERTY_NAME;

public class CheLifecycleManager {

    private static final Logger logger = Logger.getLogger(CheLifecycleManager.class);

    @Inject
    @ApplicationScoped
    private InstanceProducer<CheWorkspace> cheWorkspaceInstanceProducer;

    public void setupWorkspace(@Observes BeforeSuite event) {
        checkRunParams();

        CheWorkspace workspace;
        if (KEYCLOAK_TOKEN == null) {
            logger.info("Creating Che workspace via Che-starter OpenShift endpoint");
            workspace =
                CheWorkspaceProvider.createCheWorkspaceOSO(CHE_STARTER_URL, OPENSHIFT_MASTER_URL, OPENSHIFT_TOKEN,
                    null, OPENSHIFT_NAMESPACE);
        } else {
            logger.info("Creating Che workspace via Che-starter Keycloak endpont");
            workspace = CheWorkspaceProvider.createCheWorkspace(CHE_STARTER_URL, OPENSHIFT_MASTER_URL, KEYCLOAK_TOKEN,
                null, OPENSHIFT_NAMESPACE);
        }
        logger.info("Workspace successfully created.");

        logger.info("Waiting until workspace starts");
        CheWorkspaceService.waitUntilWorkspaceGetsToState(workspace, CheWorkspaceStatus.RUNNING.getStatus());

        cheWorkspaceInstanceProducer.set(workspace);
    }

    public void checkRunParams() {
        StringBuilder sb = new StringBuilder();
        if (CHE_STARTER_URL == null) {
            sb.append("Che starter URL cannot be null. Set property " + Constants.CHE_STARTER_PROPERTY_NAME
                + " and rerun tests\n");
        }
        if (OPENSHIFT_MASTER_URL == null) {
            sb.append("OpenShift master URL cannot be null. Set property "
                + Constants.OPENSHIFT_MASTER_URL_PROPERTY_NAME + "and rerun tests\n");
        }
        if (KEYCLOAK_TOKEN == null && OPENSHIFT_TOKEN == null) {
            sb.append("Keycloak and OpenShift tokens are null. Set either " + Constants.KEYCLOAK_TOKEN_PROPERTY_NAME
                + " or " + Constants.OPENSHIFT_TOKEN_PROPERTY_NAME + " and rerun tests\n");
        }
        if (sb.length() > 0) {
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void cleanUp(@Observes AfterSuite event) {
        CheWorkspace workspace = cheWorkspaceInstanceProducer.get();
        if (workspace != null && !shouldNotDeleteWorkspace()) {
            if (CheWorkspaceService.getWorkspaceStatus(workspace).equals(CheWorkspaceStatus.RUNNING.getStatus())) {
                logger.info("Stopping workspace");
                CheWorkspaceService.stopWorkspace(workspace);
            }
            logger.info("Deleting workspace");
            CheWorkspaceService.deleteWorkspace(workspace);
        } else {
            logger.info("Property to preserve workspace is set to true, skipping workspace deletion");
        }
    }

    private static boolean shouldNotDeleteWorkspace() {
        String preserveWorkspaceProperty = System.getProperty(PRESERVE_WORKSPACE_PROPERTY_NAME);
        if (preserveWorkspaceProperty == null) {
            return false;
        }
        if (preserveWorkspaceProperty.toLowerCase().equals("true")) {
            return true;
        } else {
            return false;
        }
    }
}
