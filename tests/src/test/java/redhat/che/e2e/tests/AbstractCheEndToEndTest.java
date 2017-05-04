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

import com.redhat.arquillian.che.resource.CheWorkspace;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import redhat.che.e2e.tests.fragments.CodeEditorFragment;
import redhat.che.e2e.tests.fragments.ProjectTree;

import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static redhat.che.e2e.tests.utils.Constants.PROJECT_NAME;

@RunWith(Arquillian.class)
public abstract class AbstractCheEndToEndTest {

    @FindByJQuery("#gwt-debug-projectTree > div:contains('" + PROJECT_NAME + "'):first")
    protected ProjectTree projectTree;

    @FindBy(id = "gwt-debug-editorPartStack-contentPanel")
    protected CodeEditorFragment codeEditor;

    @FindByJQuery("#gwt-debug-popup-container:contains('Workspace is running')")
    private WebElement workspaceIsRunningPopup;

    @ArquillianResource
    private static CheWorkspace workspace;

    protected void openBrowser(WebDriver browser) {
        browser.get(workspace.getIdeLink());
        waitModel().until().element(workspaceIsRunningPopup).is().visible();
        waitModel().until().element(workspaceIsRunningPopup).is().not().visible();
    }
}
