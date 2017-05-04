package redhat.che.e2e.tests.utils;

import org.apache.log4j.Logger;
import redhat.che.e2e.tests.provider.CheWorkspaceProvider;
import redhat.che.e2e.tests.resource.CheWorkspace;
import redhat.che.e2e.tests.resource.CheWorkspaceStatus;
import redhat.che.e2e.tests.service.CheWorkspaceService;

import static redhat.che.e2e.tests.utils.Constants.CHE_STARTER_URL;
import static redhat.che.e2e.tests.utils.Constants.CREATE_WORKSPACE_REQUEST_JSON;
import static redhat.che.e2e.tests.utils.Constants.KEYCLOAK_TOKEN;
import static redhat.che.e2e.tests.utils.Constants.OPENSHIFT_MASTER_URL;
import static redhat.che.e2e.tests.utils.Constants.OPENSHIFT_NAMESPACE;
import static redhat.che.e2e.tests.utils.Constants.OPENSHIFT_TOKEN;

public class WorkspaceCreator {

    private static CheWorkspace workspace;

    private static final Logger logger = Logger.getLogger(WorkspaceCreator.class);

    public static CheWorkspace setupWorkspace() {
        if (workspace != null) {
            if (Constants.KEYCLOAK_TOKEN == null) {
                logger.info("Creating Che workspace via Che-starter OpenShift endpoint");
                workspace =
                    CheWorkspaceProvider.createCheWorkspaceOSO(CHE_STARTER_URL, OPENSHIFT_MASTER_URL, OPENSHIFT_TOKEN,
                        CREATE_WORKSPACE_REQUEST_JSON, OPENSHIFT_NAMESPACE);
            } else {
                logger.info("Creating Che workspace via Che-starter Keycloak endpont");
                workspace = CheWorkspaceProvider.createCheWorkspace(CHE_STARTER_URL, OPENSHIFT_MASTER_URL, KEYCLOAK_TOKEN,
                    CREATE_WORKSPACE_REQUEST_JSON, OPENSHIFT_NAMESPACE);
            }
            logger.info("Workspace successfully created.");

            logger.info("Waiting until workspace starts");
            CheWorkspaceService.waitUntilWorkspaceGetsToState(workspace, CheWorkspaceStatus.RUNNING.getStatus());
        }
        return workspace;
    }
}
