package redhat.che.e2e.tests.fragments.window;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static redhat.che.e2e.tests.utils.ActionUtils.click;

/**
 * id = "gwt-debug-git-remotes-push-window"
 */
public class GitPushWindow {

    @Drone
    private WebDriver driver;

    @FindBy(id = "git-remotes-push-push")
    private WebElement pushButton;

    public void push(){
        click(driver, pushButton);
    }

}
