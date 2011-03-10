package mobl.strategies;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.Strategy;

public class confirm_dialog_0_0 extends Strategy {

    public static confirm_dialog_0_0 instance = new confirm_dialog_0_0();

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm current) {
        String message = uglify_0_0.getStringFromTerm(current);

        final MessageDialog messageDialog = new org.eclipse.jface.dialogs.MessageDialog(null, null, null, message, MessageDialog.CONFIRM,new String[] {"Yes", "No"}, 0);
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                messageDialog.open();
            }
        });

        if(messageDialog.getReturnCode() == MessageDialog.OK) {
            return current;
        } else {
            return null;
        }
/*

        final InputDialog d = new InputDialog(null, title, message, value, null);
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                d.open();
            }
        });

        return context.getFactory().makeString(d.getValue());*/
    }
}