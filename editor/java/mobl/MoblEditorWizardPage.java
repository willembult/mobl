package mobl;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.imp.preferences.fields.RadioGroupFieldEditor;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (esv).
 */

public class MoblEditorWizardPage extends WizardPage {

    private Text inputProjectName;

    public String getInputProjectName() {
        return inputProjectName.getText().trim();
    }

    private Text inputAppName;

    public String getInputAppName() {
        return inputAppName.getText().trim();
    }

    private boolean isInputProjectNameChanged;

    private boolean ignoreEvents;

    /**
     * Constructor for SampleNewWizardPage.
     */
    public MoblEditorWizardPage() {
        super("wizardPage");
        setTitle("mobl project");
        setDescription("This wizard creates a mobl project.");
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        layout.verticalSpacing = 9;

        new Label(container, SWT.NULL).setText("&Project name:");
        inputProjectName = new Text(container, SWT.BORDER | SWT.SINGLE);
        inputProjectName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        inputProjectName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (!ignoreEvents) {
                    distributeProjectName();
                    onChange();
                }
            }
        });

        new Label(container, SWT.NULL).setText("&Application name:");
        inputAppName = new Text(container, SWT.BORDER | SWT.SINGLE);
        inputAppName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        inputAppName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                onChange();
            }
        });

        setControl(container);
        setPageComplete(false);

        inputProjectName.setFocus();
    }

    private void distributeProjectName() {
        if (!isInputProjectNameChanged || getInputAppName().length() == 0
                || getInputAppName().equals(toLanguageName(getInputProjectName()))) {
            ignoreEvents = true;
            inputAppName.setText(toLanguageName(getInputProjectName()));
            isInputProjectNameChanged = false;
            ignoreEvents = false;
        }
    }

    /**
     * Ensures that both text fields are set.
     */
    private void onChange() {
        if (!ignoreEvents) {
            setErrorMessage(null);
            setErrorStatus(null);

            if (getInputProjectName().length() == 0) {
                setErrorStatus("Project name must be specified");
                return;
            }
            if (getInputAppName().length() == 0) {
                setErrorStatus("Application name must be specified");
                return;
            }

            if (!isValidProjectName(getInputProjectName())) {
                setErrorStatus("Project name must be valid");
                return;
            }
            if (!getInputAppName().matches("^[a-zA-Z0-9]+$")) {
                setErrorStatus("Application name may only contain alphanumeric characters.");
                return;
            }

            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            if (workspace.getRoot().getProject(getInputProjectName()).exists()) {
                setErrorStatus("A project with this name already exists");
                return;
            }
        }
    }

    private static boolean isValidProjectName(String name) {
        for (char c : name.toCharArray()) {
            if (!(Character.isLetterOrDigit(c) || c == '_' || c == ' ' || c == '-' || c == '.' || c == '(' || c == ')'
                    || c == '#' || c == '+' || c == '[' || c == ']' || c == '@'))
                return false;
        }
        return true;
    }

    private static String toLanguageName(String name) {
        return name;
    }

    private void setErrorStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    private void setWarningStatus(String message) {
        if (getErrorMessage() == null)
            setErrorMessage(message);
    }

}