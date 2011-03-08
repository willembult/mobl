package mobl.strategies;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.Strategy;

public class path_to_dataurl_0_0 extends Strategy {

    public static path_to_dataurl_0_0 instance = new path_to_dataurl_0_0();

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm current) {
        String path = uglify_0_0.getStringFromTerm(current);
        ITermFactory factory = context.getFactory();
        try {
            return factory.makeString(pathToDataUrl(context, path));
        } catch (IOException e) {
            context.getIOAgent().printError("Failed to open file and data urlify: " + path);
            context.getIOAgent().printError(e.toString());
            return null;
        }
    }

    private String pathToDataUrl(Context context, String path) throws IOException {
        byte[] buf = read(context.getIOAgent().openFile(path));
        String mime = "";
        if(path.endsWith(".png")) {
            mime = "image/png";
        } else if(path.endsWith(".jpg")) {
            mime = "image/jpeg";
        } else if(path.endsWith(".gif")) {
            mime = "image/gif";
        }
        return "data:" + mime + ";base64," + new String(Base64Coder.encode(buf));
    }

    public byte[] read(File file) throws IOException {

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException(
                        "EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }

        return buffer;
    }

}