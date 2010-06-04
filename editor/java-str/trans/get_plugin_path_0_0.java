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

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("all")
public class get_plugin_path_0_0 extends Strategy {
	public static get_plugin_path_0_0 instance = new get_plugin_path_0_0();

	@Override
	public IStrategoTerm invoke(Context context, IStrategoTerm term) {
		String plugindir = new File(get_plugin_path_0_0.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile().getParent();
		if (System.getProperty("os.name").startsWith("Windows")) {
			plugindir = plugindir.substring(1);
		}
		term = context.getFactory().makeString(plugindir);
		return term;
	}
}