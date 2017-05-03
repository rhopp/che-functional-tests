package redhat.che.e2e.tests.fragments;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

/**
 * id = "menu-lock-layer-id"
 */
public class PopupMenu {

    @Root
    private WebElement menuLockLayerRoot;

    protected void click(WebElement element) {
        waitGui().until().element(element).is().visible();
        element.click();
    }
}
