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
package redhat.che.e2e.tests;

import org.apache.log4j.Logger;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import redhat.che.e2e.tests.fragments.BottomInfoPanel;
import redhat.che.e2e.tests.fragments.CodeEditorFragment;
import redhat.che.e2e.tests.fragments.PreferencesWindow;
import redhat.che.e2e.tests.fragments.ProjectTree;
import redhat.che.e2e.tests.fragments.UploadPrivateSshForm;
import redhat.che.e2e.tests.fragments.topmenu.GitPopupTopMenu;
import redhat.che.e2e.tests.fragments.topmenu.MainMenuPanel;
import redhat.che.e2e.tests.fragments.topmenu.ProfileTopMenu;
import redhat.che.e2e.tests.provider.CheWorkspaceProvider;
import redhat.che.e2e.tests.resource.CheWorkspace;
import redhat.che.e2e.tests.resource.CheWorkspaceStatus;
import redhat.che.e2e.tests.selenium.ide.Labels;
import redhat.che.e2e.tests.selenium.ide.Popup;
import redhat.che.e2e.tests.selenium.ide.Project;
import redhat.che.e2e.tests.selenium.ide.ProjectExplorer;
import redhat.che.e2e.tests.selenium.ide.ProjectItem;
import redhat.che.e2e.tests.selenium.ide.TestResultsView;
import redhat.che.e2e.tests.service.CheWorkspaceService;

import static redhat.che.e2e.tests.Constants.CHE_STARTER_URL;
import static redhat.che.e2e.tests.Constants.CREATE_WORKSPACE_REQUEST_JSON;
import static redhat.che.e2e.tests.Constants.KEYCLOAK_TOKEN;
import static redhat.che.e2e.tests.Constants.OPENSHIFT_MASTER_URL;
import static redhat.che.e2e.tests.Constants.OPENSHIFT_NAMESPACE;
import static redhat.che.e2e.tests.Constants.OPENSHIFT_TOKEN;
import static redhat.che.e2e.tests.Constants.PATH_TO_TEST_FILE;
import static redhat.che.e2e.tests.Constants.PRESERVE_WORKSPACE_PROPERTY_NAME;
import static redhat.che.e2e.tests.Constants.PROJECT_NAME;

@RunWith(Arquillian.class)
public class CheEndToEndTest {

    private static final Logger logger = Logger.getLogger(CheEndToEndTest.class);

    @Drone
    private WebDriver driver;

    private static final String ADDED_TO_INDEX_TEXT = "Git index updated";

    @FindBy(id = "gwt-debug-editorPartStack-contentPanel")
    private CodeEditorFragment codeEditor;

    @FindByJQuery("div:contains('Preferences'):contains('Java Compiler'):last")
    private PreferencesWindow preferencesWindow;

    @FindByJQuery("div:contains('Host'):contains('Upload'):last")
    private UploadPrivateSshForm uploadPrivateSshForm;

    @FindByJQuery("#gwt-debug-projectTree > div:contains('" + PROJECT_NAME + "'):first")
    private ProjectTree projectTree;

    @FindBy(id = "gwt-debug-mainMenuPanel")
    private MainMenuPanel mainMenuPanel;

    @FindBy(id = "menu-lock-layer-id")
    private ProfileTopMenu profileTopMenu;

    @FindBy(id = "menu-lock-layer-id")
    private GitPopupTopMenu gitPopupTopMenu;

    @FindBy(id = "gwt-debug-infoPanel")
    private BottomInfoPanel bottomInfoPanel;

    private static CheWorkspace workspace;

    @BeforeClass
    public static void checkRunParams() {
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
            Assert.fail(sb.toString());
        }
        setupWorkspace();
    }

    private static void setupWorkspace() {
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

    private void openBrowser() {
        driver.get(workspace.getIdeLink());
    }

    @Test
    @InSequence(1)
    @Ignore
    public void test_run_junit_test_and_verify_popup_window() {
        logger.info("Calling che starter to create a new workspace on OpenShift");
        setupWorkspace();
        openBrowser();
        // Running single JUnit Class
        logger.info("Running JUnit test class on the project");
        ProjectExplorer explorer = new ProjectExplorer(driver);
        Project project = explorer.getProject(PROJECT_NAME);
        project.select();
        ProjectItem testClassItem = project.getProjectItem(PATH_TO_TEST_FILE);
        testClassItem.select();
        testClassItem.openMenu();
        testClassItem.selectMenu(Labels.ContextMenuItem.TEST, Labels.ContextMenuItem.JUNIT_CLASS);

        // Wait until tests finish
        Popup testsPopup = new Popup(driver);
        testsPopup.waitUntilExists(Popup.RUNNING_TESTS_TITLE, 20);
        testsPopup.waitWhileExists(Popup.RUNNING_TESTS_TITLE, 100);
        testsPopup.waitUntilExists(Popup.SUCCESSFULL_TESTS_TITLE, 10);

        // Check the results
        TestResultsView testView = new TestResultsView(driver);
        testView.open();
        testView.assertLatestTestRunPassed();
    }

    @Test
    @InSequence(2)
    public void test_when_pom_has_older_dependency_version_annotation_error_is_shown() {
        openBrowser();
        projectTree.expandProjectRoot();
        projectTree.openPomXml();
        codeEditor.writeDependencyIntoPom();
        codeEditor.verifyAnnotationErrorIsPresent();
    }

    @AfterClass
    public static void cleanUp() {
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

    @Test
    @InSequence(3)
    public void test_set_github_key_and_commit() {
        // set commiter credentials
        openBrowser();
        mainMenuPanel.clickProfile();
        profileTopMenu.openPreferences();
        preferencesWindow.writeCommiterInformation("dev-test-user", "mlabuda@redhat.com");
        //preferencesWindow.openUploadPrivateKeyWindow();
        //File idRsa = new File("src/test/resources/id_rsa");
        //uploadPrivateSshForm.upload("github.com", idRsa);
        preferencesWindow.close();

        //introduce changes
        projectTree.expandProjectRoot();
        projectTree.openReadme();
        codeEditor.writeIntoTextViewContent("hi there");

        logger.info("Commiting and pushing changes");
        mainMenuPanel.clickGit();
        gitPopupTopMenu.addToIndex();
        bottomInfoPanel.verifyConsolePartContains(ADDED_TO_INDEX_TEXT);
        mainMenuPanel.clickGit();
        gitPopupTopMenu.push();
    }
}
