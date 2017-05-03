package redhat.che.e2e.tests.fragments;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * id = "gwt-debug-infoPanel"
 */
public class BottomInfoPanel {

    @FindBy(id = "gwt-debug-consolePart")
    private WebElement consolePart;

    public void verifyConsolePartContains(String text){
        waitModel().until().element(consolePart).is().visible();
        Assertions.assertThat(consolePart.getText()).contains(text);
    }
}
