package redhat.che.e2e.tests.fragments.topmenu;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import redhat.che.e2e.tests.fragments.PopupMenu;

public class ProfileTopMenu extends PopupMenu {

    @FindBy(id = "topmenu/Profile/Preferences")
    private WebElement preferencesItem;

    public void openPreferences(){
        click(preferencesItem);
    }
}
