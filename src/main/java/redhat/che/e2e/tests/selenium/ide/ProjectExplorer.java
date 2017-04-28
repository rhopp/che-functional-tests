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
package redhat.che.e2e.tests.selenium.ide;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Project explorer located in Che Web IDE.
 * 
 * @author mlabuda
 */
public class ProjectExplorer {

	@Drone
	private WebDriver driver;

	@FindByJQuery("div:contains('pom.xml'):last")
	private WebElement pomXmlItem;

    private static final String PROJECT_EXPLORER_TREE_ID = "gwt-debug-projectTree";
    
	public static final String REFRESH_BUTTON_ID = "gwt-debug-refreshSelectedPath";

	public static final String CONTEXT_MENU_ID = "gwt-debug-contextMenu/newGroup";

	public Project getProject(String name) {
	    String locator = String.format("//div[@id='%s']//div[@name='%s']", PROJECT_EXPLORER_TREE_ID, name);
	    new WebDriverWait(driver, Timeouts.REDRAW)
            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
	    
	    WebElement project = driver.findElement(By.xpath(locator));
	    return new Project(driver, project);
	}

	/**
	 * Opens pom.xml file
	 */
	public void openPomXml() {
		Graphene.waitModel().until().element(pomXmlItem).is().visible();
		new Actions(driver).doubleClick(pomXmlItem).perform();
	}
}
