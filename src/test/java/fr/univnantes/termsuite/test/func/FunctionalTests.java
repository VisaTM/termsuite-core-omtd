
/*******************************************************************************
 * Copyright 2015-2016 - CNRS (Centre National de Recherche Scientifique)
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 *******************************************************************************/

package fr.univnantes.termsuite.test.func;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import fr.univnantes.termsuite.model.Lang;
import fr.univnantes.termsuite.model.Term;
import fr.univnantes.termsuite.model.TermProperty;
import fr.univnantes.termsuite.model.Terminology;
import fr.univnantes.termsuite.test.func.align.BilingualAlignerDeEnSpec;
import fr.univnantes.termsuite.test.func.align.BilingualAlignerFrEnSpec;
import fr.univnantes.termsuite.test.func.tools.builders.TermSuitePreprocessorSpec;
import fr.univnantes.termsuite.test.func.tools.builders.TerminoExtractorSpec;
import fr.univnantes.termsuite.test.func.tools.builders.TerminoFiltererSpec;
import fr.univnantes.termsuite.test.func.tools.cmd.TermSuiteAlignerCLISpec;
import fr.univnantes.termsuite.test.func.tools.cmd.TermSuiteTerminoCLISpec;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	FrenchWindEnergySpec.class,
	EnglishWindEnergySpec.class,
	GermanWindEnergySpec.class,
	TermSuitePreprocessorSpec.class,
	TerminoFiltererSpec.class,
	TermSuiteTerminoCLISpec.class,
	TermSuiteAlignerCLISpec.class,
	BilingualAlignerFrEnSpec.class,
	BilingualAlignerDeEnSpec.class,
	SemanticGathererSpec.class,
	TerminoExtractorSpec.class
	})
public class FunctionalTests {
	

	public static final Path TERMINOLOGY_1=Paths.get("src", "test", "resources", "fr", "univnantes", "termsuite", "test", "json", "termino1.json");
	public static final Path CORPUS2_PATH=Paths.get("src", "test", "resources", "fr", "univnantes", "termsuite", "test", "corpus", "corpus2");
	public static final Path CORPUS1_PATH=Paths.get("src", "test", "resources", "fr", "univnantes", "termsuite", "test", "corpus", "corpus1");
	public static final Path TERMINO_WESHORT_PATH=Paths.get("src", "test", "resources", "fr", "univnantes", "termsuite", "test", "termino");
	public static final Path DICO_PATH=Paths.get("src", "test", "resources", "fr", "univnantes", "termsuite", "test", "dico");

			
	public static final String CORPUS_WESHORT_PATH="fr/univnantes/termsuite/test/corpus/weshort/";
	public static final String CORPUS_WE_PATH="fr/univnantes/termsuite/test/corpus/we/";
	private static final String FUNCTION_TESTS_CONFIG = "termsuite-test.properties";
	private static final String PROP_TREETAGGER_HOME_PATH = "treetagger.home.path";

	private static Object getConfigProperty( String propName) {
		InputStream is = FunctionalTests.class.getClassLoader().getResourceAsStream(FUNCTION_TESTS_CONFIG);
		Properties properties = new Properties();
		try {
			properties.load(is);
			is.close();
			Preconditions.checkArgument(!properties.contains(propName), "No such property in config file %s: %s", FUNCTION_TESTS_CONFIG, propName);
			return properties.get(propName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getCorpusWEShortPath(Lang lang) {
		return "src/test/resources/" + CORPUS_WESHORT_PATH + lang.getName().toLowerCase() + "/txt/";
	}

	public static String getCorpusWEPath(Lang lang) {
		return "src/test/resources/" + CORPUS_WE_PATH + lang.getName().toLowerCase() + "/txt/";
	}

	public static String getTerminoWEShortPath(Lang lang) {
		return Paths.get(TERMINO_WESHORT_PATH.toString(), "we-short-" +  lang.getCode() + ".json").toString();
		
	}

	public static String getDicoPath(Lang source, Lang target) {
		String dicoFileName = String.format("%s-%s.txt", source.getCode(), target.getCode());
		return Paths.get(DICO_PATH.toString(), dicoFileName).toString();
		
	}

	public static String getTaggerPath() {
		return (String)getConfigProperty(PROP_TREETAGGER_HOME_PATH);
	}
	
	public static List<Term> termsByProperty(Terminology termino, TermProperty termProperty, boolean desc) {
		List<Term> terms = Lists.newArrayList(termino.getTerms());
		Collections.sort(terms, termProperty.getComparator(desc));
		return terms;
	}


	public static Path getTestsOutputFile(String relativePath) {
		return getFunctionalTestsOutputDir().resolve(relativePath);
	}
	
	public static Path getFunctionalTestsOutputDir() {
		return Paths.get(getConfigProperty("tests.output").toString());
	}

	public static Path getFunctionalTestsControlDir() {
		return getFunctionalTestsOutputDir().resolve("control");
	}

	public static Path getTestTmpDir() {
		Path path = Paths.get(System.getProperty("java.io.tmpdir"), "termsuite-tests");
		return createIfNotExist(path);
	}
		
	
	private static Path createIfNotExist(Path path) {
		if(!path.toFile().exists())
			path.toFile().mkdirs();
		return path;
	}

}