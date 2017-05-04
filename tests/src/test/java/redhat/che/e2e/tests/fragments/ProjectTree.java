package redhat.che.e2e.tests.fragments;

import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * jquery = "#gwt-debug-projectTree > div:contains('" + PROJECT_NAME + "'):first"
 */
public class ProjectTree {

    @Drone
    private WebDriver driver;

    @Root
    private WebElement projectTreeRoot;

    @FindBy(id = "projectFolder")
    private WebElement rootProjectFolder;

    @FindBy(id = "simpleFolder")
    private List<WebElement> subFolders;

    @FindByJQuery("div:contains('pom.xml'):last")
    private WebElement pomXmlItem;

    @FindByJQuery("div:contains('README.md'):last")
    private WebElement readmeMdItem;

    public void expandProjectRoot(){
        waitModel().until().element(projectTreeRoot).is().visible();
        if (subFolders.size() == 0){
            new Actions(driver).doubleClick().doubleClick(rootProjectFolder).build().perform();
        }
    }

    private void open(WebElement item){
        waitModel().until().element(item).is().visible();
        new Actions(driver).doubleClick(item).perform();
    }

    /**
     * Opens pom.xml file
     */
    public void openPomXml() {
        open(pomXmlItem);
    }

    /**
     * Opens README.md file
     */
    public void openReadme() {
        open(readmeMdItem);
    }
}
