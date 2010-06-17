package trans;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.stratego_lib.*;
import org.strategoxt.stratego_sglr.*;
import org.strategoxt.stratego_gpp.*;
import org.strategoxt.stratego_xtc.*;
import org.strategoxt.stratego_aterm.*;
import org.strategoxt.java_front.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.net.*;
import java.io.*;

@SuppressWarnings("all")
public class upload_files_0_1 extends Strategy {
    public static upload_files_0_1 instance = new upload_files_0_1();

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm h_658) {
        /*
        URL u = new URL("http://mobl-lang.org/upload.php");
        URLConnection uc = u.openConnection();
        IStrategoList list = (IStrategoList) term;
        for (IStrategoTerm t : list.getAllSubterms()) {

        }
        uc.setDoOutput(true);
        uc.setDoInput(true);
        uc.setAllowUserInteraction(false);

        DataOutputStream dstream = new DataOutputStream(uc.getOutputStream());

        // The POST line
        dstream.writeBytes(s);
        dstream.close();

        // Read Response
        InputStream in = uc.getInputStream();
        int x;
        while ((x = in.read()) != -1) {
            System.out.write(x);
        }
        in.close();

        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuffer buf = new StringBuffer();
        String line;
        while ((line = r.readLine()) != null) {
            buf.append(line);
        }
        */
        return term;
    }
}