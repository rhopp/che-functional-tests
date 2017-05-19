package com.redhat.arquillian.che;

import com.redhat.arquillian.che.config.CheExtensionConfiguration;
import com.redhat.arquillian.che.provider.CheWorkspaceProvider;
import com.redhat.arquillian.che.resource.CheWorkspace;
import com.redhat.arquillian.che.resource.CheWorkspaceStatus;
import com.redhat.arquillian.che.service.CheWorkspaceService;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;


public class CheWorkspaceManager {

    private static final Logger logger = Logger.getLogger(CheWorkspaceManager.class);

    @Inject
    @ApplicationScoped
    private InstanceProducer<CheWorkspace> cheWorkspaceInstanceProducer;

    @Inject
    @ApplicationScoped
    private InstanceProducer<CheExtensionConfiguration> configurationInstanceProducer;

    public void setupWorkspace(@Observes BeforeSuite event) {
        CheExtensionConfiguration cheExtensionConfig = new CheExtensionConfiguration();
        configurationInstanceProducer.set(cheExtensionConfig);
        checkRunParams(cheExtensionConfig);

        if (cheExtensionConfig.getCheStarterUrl() == null && cheExtensionConfig.getCheWorkspaceUrl() == null) {
            startCheStarter();
            cheExtensionConfig.setCheStarterUrl("http://localhost:10000");
        }

        if (cheExtensionConfig.getCheWorkspaceUrl() != null) {
            cheWorkspaceInstanceProducer.set(new CheWorkspace(cheExtensionConfig.getCheStarterUrl(), null, null));
        }else {
            cheWorkspaceInstanceProducer.set(createWorkspace(cheExtensionConfig));
        }
    }

    private CheWorkspace createWorkspace(CheExtensionConfiguration config) {
        CheWorkspace workspace;
        if (config.getKeycloakToken() == null) {
            logger.info("Creating Che workspace via Che-starter OpenShift endpoint");
            workspace =
                CheWorkspaceProvider.createCheWorkspaceOSO(config.getCheStarterUrl(), config.getOpenshiftMasterUrl(),
                    config.getOpenshiftToken(), null, config.getOpenshiftNamespace());
        } else {
            logger.info("Creating Che workspace via Che-starter Keycloak endpoint");
            workspace = CheWorkspaceProvider.createCheWorkspace(config.getCheStarterUrl(), config.getOpenshiftMasterUrl(),
                config.getKeycloakToken(), null, config.getOpenshiftNamespace());
        }
        logger.info("Workspace successfully created.");

        logger.info("Waiting until workspace starts");
        String authorizationToken = getAuthorizationToken(config);
        CheWorkspaceService.waitUntilWorkspaceGetsToState(workspace, CheWorkspaceStatus.RUNNING.getStatus(),
            authorizationToken);

        return workspace;
    }

    private void startCheStarter() {
        try {
            File cheStarterDir = new File(System.getProperty("user.dir"), "target" + File.separator + "che-starter");
            logger.info("Cloning che-starter project.");
            Git
                .cloneRepository()
                .setURI("https://github.com/redhat-developer/che-starter")
                .setDirectory(cheStarterDir)
                .call();

            logger.info("Running che starter.");
            EmbeddedMaven
                .forProject(cheStarterDir.getAbsolutePath() + File.separator + "pom.xml")
                .useMaven3Version("3.5.0")
                .setGoals("spring-boot:run")
                .useAsDaemon()
                .withWaitUntilOutputLineMathes(".*Started Application in.*", 50, TimeUnit.SECONDS)
                .build();

        } catch (GitAPIException e) {
            throw new IllegalStateException("There was a problem with getting the git che-starter repository", e);
        } catch (TimeoutException e) {
            throw new IllegalStateException("The che-starter haven't started within 50 seconds.", e);
        }
    }

    private String getAuthorizationToken(CheExtensionConfiguration configuration) {
        return (configuration.getKeycloakToken() != null) ? configuration.getKeycloakToken()
            : configuration.getOpenshiftToken();
    }

    private void checkRunParams(CheExtensionConfiguration configuration) {
        StringBuilder sb = new StringBuilder();
        if (configuration.getCheWorkspaceUrl() != null) {
            return;
        }
        if (configuration.getOpenshiftMasterUrl() == null) {
            sb.append("OpenShift master URL cannot be null. Set property "
                + CheExtensionConfiguration.OPENSHIFT_MASTER_URL_PROPERTY_NAME + "and rerun tests\n");
        }
        if (configuration.getKeycloakToken() == null && configuration.getOpenshiftToken() == null) {
            sb.append("Keycloak and OpenShift tokens are null. Set either "
                + CheExtensionConfiguration.KEYCLOAK_TOKEN_PROPERTY_NAME
                + " or "
                + CheExtensionConfiguration.OPENSHIFT_TOKEN_PROPERTY_NAME
                + " and rerun tests\n");
        }
        if (sb.length() > 0) {
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void cleanUp(@Observes AfterSuite event) {
        CheExtensionConfiguration cheExtensionConfiguration = configurationInstanceProducer.get();
        if (cheExtensionConfiguration.getCheWorkspaceUrl() != null) {
            return;
        }
        CheWorkspace workspace = cheWorkspaceInstanceProducer.get();
        String authorizationToken = getAuthorizationToken(cheExtensionConfiguration);
        if (workspace != null && !cheExtensionConfiguration.getPreserveWorkspace()) {
            String workspaceStatus = CheWorkspaceService.getWorkspaceStatus(workspace, authorizationToken);
            if (workspaceStatus.equals(CheWorkspaceStatus.RUNNING.getStatus())) {
                logger.info("Stopping workspace");
                CheWorkspaceService.stopWorkspace(workspace, authorizationToken);
            }
            logger.info("Deleting workspace");
            CheWorkspaceService.deleteWorkspace(workspace);
        } else {
            logger.info("Property to preserve workspace is set to true, skipping workspace deletion");
        }
    }
}
