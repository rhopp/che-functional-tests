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
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import redhat.che.e2e.tests.fragments.CodeEditorFragment;
import redhat.che.e2e.tests.fragments.ProjectTree;
import redhat.che.e2e.tests.resource.CheWorkspace;
import redhat.che.e2e.tests.resource.CheWorkspaceStatus;
import redhat.che.e2e.tests.service.CheWorkspaceService;
import redhat.che.e2e.tests.utils.Constants;

import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static redhat.che.e2e.tests.utils.Constants.CHE_STARTER_URL;
import static redhat.che.e2e.tests.utils.Constants.KEYCLOAK_TOKEN;
import static redhat.che.e2e.tests.utils.Constants.OPENSHIFT_MASTER_URL;
import static redhat.che.e2e.tests.utils.Constants.OPENSHIFT_TOKEN;
import static redhat.che.e2e.tests.utils.Constants.PRESERVE_WORKSPACE_PROPERTY_NAME;
import static redhat.che.e2e.tests.utils.Constants.PROJECT_NAME;
import static redhat.che.e2e.tests.utils.WorkspaceCreator.setupWorkspace;

@RunWith(Arquillian.class)
public abstract class AbstractCheEndToEndTest {

    private static final Logger logger = Logger.getLogger(AbstractCheEndToEndTest.class);

    @FindByJQuery("#gwt-debug-projectTree > div:contains('" + PROJECT_NAME + "'):first")
    protected ProjectTree projectTree;

    @FindBy(id = "gwt-debug-editorPartStack-contentPanel")
    protected CodeEditorFragment codeEditor;

    @FindByJQuery("#gwt-debug-popup-container:contains('Workspace is running')")
    private WebElement workspaceIsRunningPopup;

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
        workspace = setupWorkspace();
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

    protected void openBrowser(WebDriver browser) {
        browser.get(workspace.getIdeLink());
        waitModel().until().element(workspaceIsRunningPopup).is().visible();
        waitModel().until().element(workspaceIsRunningPopup).is().not().visible();
    }
}
