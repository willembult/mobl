package mobl;

import static org.eclipse.core.resources.IResource.DEPTH_INFINITE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.strategoxt.imp.runtime.EditorState;
import org.strategoxt.imp.runtime.Environment;

/**
 * A wizard for creating new WebDSL projects.
 */
public class MoblEditorWizard extends Wizard implements INewWizard {

    private final MoblEditorWizardPage input = new MoblEditorWizardPage();

    private IProject lastProject;

    // TODO: Support external directory and working set selection in wizard

    public MoblEditorWizard() {
        setNeedsProgressMonitor(true);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // No further initialization required
    }

    @Override
    public void addPages() {
        addPage(input);
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We
     * will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {
        final String appName = input.getInputAppName();
        final String projectName = input.getInputProjectName();
        System.out.println(appName + projectName);

        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(appName, projectName, monitor);
                } catch (Exception e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };

        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            rollback();
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            Environment.logException("Exception while creating new project", realException);
            MessageDialog.openError(getShell(), "Error: " + realException.getClass().getName(), realException
                    .getMessage());
            rollback();
            return false;
        }
        return true;
    }

    private void rollback() {
        // monitor.setTaskName("Undoing workspace operations");
        try {
            if (lastProject != null)
                lastProject.delete(true, null);
        } catch (CoreException e) {
            Environment.logException("Could not remove new project", e);
        }
    }

    private void doFinish(String appName, String projectName, IProgressMonitor monitor) throws IOException,
            CoreException {
        final int TASK_COUNT = 3;
        lastProject = null;
        monitor.beginTask("Creating " + appName + " application", TASK_COUNT);

        monitor.setTaskName("Creating Eclipse project");
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject project = lastProject = workspace.getRoot().getProject(projectName);
        project.create(null);
        project.open(null);
        monitor.worked(1);

        try {

            monitor.setTaskName("Generating default app");
            StringBuilder sb = new StringBuilder();
            sb.append("application " + appName + "\n");
            sb.append("\n");
            sb.append("import mobl::ui\n");
            sb.append("\n");
            sb.append("screen root() {\n");
            sb.append("\theader(\"" + appName + "\")\n");
            sb.append("}\n");
            monitor.worked(1);
            writeStringToFile(sb.toString(), project.getLocation().toString()+"/"+appName + ".mobl");
        } catch (IOException e) {
            Environment.logException(e);
            throw e;
        }

        monitor.worked(1);
        refreshProject(project);

        monitor.setTaskName("Opening editor tab");

        Display display = getShell().getDisplay();
        EditorState.asyncOpenEditor(display, project.getFile(appName + ".mobl"), true);
        monitor.worked(1);
    }

    public static void writeStringToFile(String s, String file) throws IOException {
        FileOutputStream in = null;
        try {
            File buildxml = new File(file);
            in = new FileOutputStream(buildxml);
            FileChannel fchan = in.getChannel();
            BufferedWriter bf = new BufferedWriter(Channels.newWriter(fchan, "UTF-8"));
            bf.write(s);
            bf.close();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static void createDirs(String dirs) {
        new File(dirs).mkdirs();
    }

    public static void copyFile(String ssource, String sdest) throws IOException {
        System.out.println("Copying " + ssource + " to " + sdest);
        File dest = new File(sdest);
        File source = new File(ssource);
        if (!dest.exists()) {
            dest.createNewFile();
        }
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            out.transferFrom(in, 0, in.size());
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    // If targetLocation does not exist, it will be created.
    public void copyDirectory(File sourceLocation, File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    private void refreshProject(final IProject project) {
        try {
            NullProgressMonitor monitor = new NullProgressMonitor();
            project.refreshLocal(DEPTH_INFINITE, monitor);
            project.close(monitor);
            project.open(monitor);

        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}