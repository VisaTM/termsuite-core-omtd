package fr.univnantes.termsuite.test.func.tools.cmd;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Splitter;

import fr.univnantes.termsuite.model.Lang;
import fr.univnantes.termsuite.test.TermSuiteAssertions;
import fr.univnantes.termsuite.test.func.FunctionalTests;
import fr.univnantes.termsuite.tools.Aligner;

public class TermSuiteAlignerCLISpec {
	
	PrintStream out;
	ByteArrayOutputStream baos;
	
	@Before
	public void setup() {
		baos = new ByteArrayOutputStream();
		out = new PrintStream(baos);
	}

	@After
	public void finalize() throws IOException {
		out.close();
		baos.close();
	}
	
	@Test
	public void testAlignerFrEnBasic() throws Exception {
		
		launch(String.format("--source-termino %s --target-termino %s --dictionary %s --term %s",
				FunctionalTests.getTerminoWEShortPath(Lang.FR),
				FunctionalTests.getTerminoWEShortPath(Lang.EN),
				FunctionalTests.getDicoPath(Lang.FR, Lang.EN),
				"énergie"
				));
		
		String string = baos.toString(Charset.defaultCharset().name());
		TermSuiteAssertions.assertThat(string)
			.atLine(1, startsWith("n: power"))
			.atLine(2, startsWith("n: energy"))
			.hasLineCount(10);
	}
	
	@Test
	public void testAlignerDicoWithExplanation() throws Exception {
		launch(String.format("--source-termino %s --target-termino %s --dictionary %s --term %s -n 10 --explain",
				FunctionalTests.getTerminoWEShortPath(Lang.FR),
				FunctionalTests.getTerminoWEShortPath(Lang.EN),
				FunctionalTests.getDicoPath(Lang.FR, Lang.EN),
				"énergie"
				));
		
		String string = baos.toString(Charset.defaultCharset().name());
		TermSuiteAssertions.assertThat(string)
			.atLine(1, startsWith("n: power"))
			.atLine(1, containsString("DICTIONARY"))
			.atLine(1, endsWith("{}"))
			.hasLineCount(10);

	}

	@Test
	public void testAlignerDistribWithExplanation() throws Exception {
		launch(String.format("--source-termino %s --target-termino %s --dictionary %s --term %s -n 10 --explain",
				FunctionalTests.getTerminoWEShortPath(Lang.FR),
				FunctionalTests.getTerminoWEShortPath(Lang.EN),
				FunctionalTests.getDicoPath(Lang.FR, Lang.EN),
				"parc"
				));
		
		String string = baos.toString(Charset.defaultCharset().name());
		TermSuiteAssertions.assertThat(string)
			.atLine(1, startsWith("n: turbine"))
			.atLine(1, containsString("DISTRIBUTIONAL"))
			.atLine(1, endsWith("}"))
			.hasLineCount(10);

	}

	@Test
	public void testAlignerFrEnCount3() throws Exception {
		
		launch(String.format("--source-termino %s --target-termino %s --dictionary %s --term %s -n 3",
				FunctionalTests.getTerminoWEShortPath(Lang.FR),
				FunctionalTests.getTerminoWEShortPath(Lang.EN),
				FunctionalTests.getDicoPath(Lang.FR, Lang.EN),
				"énergie"
				));
		
		String string = baos.toString(Charset.defaultCharset().name());
		TermSuiteAssertions.assertThat(string)
			.atLine(1, startsWith("n: power"))
			.atLine(1, containsString("DICTIONARY"))
			.atLine(1, endsWith("DICTIONARY"))
			.hasLineCount(3);
	}
	

	
	private void launch(String args) throws Exception {
		List<String> argList = Splitter.on(" ").splitToList(args);
		new Aligner().run(argList.toArray(new String[argList.size()]), out);
	}

}