package mobl.strategies;

import java.io.File;
import java.io.IOException;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.Strategy;

public class deltree_0_0 extends Strategy {

    public static deltree_0_0 instance = new deltree_0_0();

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm current) {
        String path = uglify_0_0.getStringFromTerm(current);
        try {
            delete(context.getIOAgent().openFile(path));
        } catch(IOException e) {
            context.getIOAgent().printError(e.getMessage());
            return null;
        }
        return current;
    }

    void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (!f.delete())
            throw new IOException("Failed to delete file: " + f);
    }

}