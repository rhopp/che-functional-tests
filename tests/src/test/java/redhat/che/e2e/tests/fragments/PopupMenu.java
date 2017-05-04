package redhat.che.e2e.tests.fragments;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import redhat.che.e2e.tests.utils.ActionUtils;

/**
 * id = "menu-lock-layer-id"
 */
public class PopupMenu {

    @Drone
    private WebDriver driver;

    @Root
    private WebElement menuLockLayerRoot;

    protected void click(WebElement element) {
        ActionUtils.click(driver, element);
    }
}
