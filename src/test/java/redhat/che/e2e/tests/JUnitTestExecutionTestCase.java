package redhat.che.e2e.tests;

import org.apache.log4j.Logger;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import redhat.che.e2e.tests.selenium.ide.Labels;
import redhat.che.e2e.tests.selenium.ide.Popup;
import redhat.che.e2e.tests.selenium.ide.Project;
import redhat.che.e2e.tests.selenium.ide.ProjectExplorer;
import redhat.che.e2e.tests.selenium.ide.ProjectItem;
import redhat.che.e2e.tests.selenium.ide.TestResultsView;

import static redhat.che.e2e.tests.utils.Constants.PATH_TO_TEST_FILE;
import static redhat.che.e2e.tests.utils.Constants.PROJECT_NAME;

@RunWith(Arquillian.class)
public class JUnitTestExecutionTestCase extends AbstractCheEndToEndTest {

    @Drone
    private WebDriver driver;

    private static final Logger logger = Logger.getLogger(JUnitTestExecutionTestCase.class);

    @Test
    @InSequence(1)
    public void test_run_junit_test_and_verify_popup_window() {
        logger.info("Calling che starter to create a new workspace on OpenShift");
        openBrowser(driver);
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
}
