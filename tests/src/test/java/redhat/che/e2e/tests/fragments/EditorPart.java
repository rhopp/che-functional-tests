package redhat.che.e2e.tests.fragments;

import org.openqa.selenium.support.FindBy;

/**
 * id = "gwt-debug-editorMultiPartStack-contentPanel"
 */
public class EditorPart {

    @FindBy(id = "gwt-debug-editorPartStack-contentPanel")
    protected CodeEditorFragment codeEditor;


    public CodeEditorFragment codeEditor(){
        return codeEditor;
    }
}
