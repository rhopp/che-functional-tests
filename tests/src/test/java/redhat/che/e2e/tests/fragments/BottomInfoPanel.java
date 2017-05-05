package redhat.che.e2e.tests.fragments;

import com.google.common.base.Function;
import org.assertj.core.api.Assertions;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * id = "gwt-debug-infoPanel"
 */
public class BottomInfoPanel {

    @Root
    private WebElement rootElement;

    @FindByJQuery("div:visible > #gwt-debug-consolePart")
    private WebElement consolePart;

    @FindByJQuery("#gwt-debug-multiSplitPanel-tabsPanel > div[focused]")
    private WebElement focusedTab;

    public void assertThatConsolePartContains(String text){
        waitModel().until().element(consolePart).is().visible();
        Assertions.assertThat(consolePart.getText()).contains(text);
    }

    public void waitUntilConsolePartContains(String text){
        waitModel().until().element(consolePart).is().visible();
        waitModel().until((Function<WebDriver, Boolean>) webDriver -> consolePart.getText().contains(text));
    }

    public void waitUntilFocusedTabHasName(String tabName) {
        waitModel().until().element(consolePart).is().visible();
        waitModel().until((Function<WebDriver, Boolean>) webDriver -> focusedTab.getText().equals(tabName));
    }

    public class TabNames {
        public static final String TAB_GIT_ADD_TO_INDEX = "Git add to index";
        public static final String TAB_GIT_COMMIT = "Git commit";
        public static final String TAB_GIT_PUSH = "Git push";
    }

    public class FixedConsoleText {
        public static final String GIT_ADDED_TO_INDEX_TEXT = "Git index updated";
        public static final String GIT_COMMITED_WITH_REVISION = "Committed with revision";
    }
}
