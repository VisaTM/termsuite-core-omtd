
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

package fr.univnantes.termsuite.test.unit.engines;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.io.File;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import fr.univnantes.termsuite.engines.gatherer.TermGatherer;
import fr.univnantes.termsuite.engines.gatherer.YamlRuleSetIO;
import fr.univnantes.termsuite.model.RelationType;
import fr.univnantes.termsuite.model.Term;
import fr.univnantes.termsuite.model.TermProperty;
import fr.univnantes.termsuite.model.termino.MemoryTermIndex;
import fr.univnantes.termsuite.test.unit.Fixtures;
import fr.univnantes.termsuite.test.unit.TermFactory;
import fr.univnantes.termsuite.test.unit.TermSuiteExtractors;

public class TermGathererSpec {
	private MemoryTermIndex termIndex;
	private Term machine_synchrone;
	private Term machine_asynchrone;
	private Term synchrone;
	private Term asynchrone;
	private Term stator;
	private Term phase_statorique;
	private Term statorique;
	private Term phase_du_stator;
	private Term geothermie_hydraulique_solaire;
	private Term geothermie_hydraulique;
	
	private TermGatherer engine;
	
	private static final String VARIANT_RULE_SET = "src/test/resources/fr/univnantes/termsuite/test/resources/variant-rules.yaml";
	@Before
	public void set() throws Exception {
		String text = Files.toString(new File(VARIANT_RULE_SET), Charsets.UTF_8);
		this.termIndex = Fixtures.termIndex();
		populateTermIndex(new TermFactory(termIndex));
		engine = new TermGatherer()
			.setRules(YamlRuleSetIO.fromYaml(text));
		
		engine.gather(this.termIndex);
	}

	private void populateTermIndex(TermFactory termFactory) {
		
		this.machine_synchrone = termFactory.create("N:machine|machin", "A:synchrone|synchro");
		this.machine_asynchrone = termFactory.create("N:machine|machin", "A:asynchrone|asynchro");
		this.synchrone = termFactory.create("A:synchrone|synchron");
		this.asynchrone = termFactory.create("A:asynchrone|asynchron");

		this.stator = termFactory.create("N:stator|stator");
		this.statorique = termFactory.create("A:statorique|statoric");
		this.phase_statorique = termFactory.create("N:phase|phas", "A:statorique|statoric");
		this.phase_du_stator = termFactory.create("N:phase|phas", "P:de|de", "N:stator|stator");

		this.geothermie_hydraulique_solaire = termFactory.create(
				"N:geothermie|géotherm", "A:hydraulique|hydraulic", "A:solaire|solair");
		this.geothermie_hydraulique = termFactory.create(
				"N:geothermie|géotherm", "A:hydraulique|hydraulic");

		
		termFactory.addPrefix(this.asynchrone, this.synchrone);
		termFactory.addDerivesInto("N A", this.stator, this.statorique);
		termFactory.setProperty(TermProperty.FREQUENCY, 1);
	}

	
	@Test
	public void testProcessDefault() throws AnalysisEngineProcessException{
		assertThat(termIndex.getOutboundRelations(this.geothermie_hydraulique))
			.hasSize(1)
			.extracting(TermSuiteExtractors.RELATION_FROM_TYPE_TO)
			.contains(tuple(this.geothermie_hydraulique, RelationType.SYNTACTICAL, this.geothermie_hydraulique_solaire));
		
		assertThat(termIndex.getOutboundRelations(this.geothermie_hydraulique_solaire))
			.hasSize(0);
	}

	
	@Test
	public void testProcessPrefix() throws AnalysisEngineProcessException{
		assertThat(termIndex.getOutboundRelations(this.machine_synchrone))
			.hasSize(1)
			.extracting(TermSuiteExtractors.RELATION_TYPE_RULE_TO)
			.contains(tuple(RelationType.SYNTACTICAL, "NA-NprefA", this.machine_asynchrone));
		
		assertThat(termIndex.getOutboundRelations(this.machine_asynchrone))
			.hasSize(0);
	}

	@Test
	public void testProcessDerivation() throws AnalysisEngineProcessException{
		assertThat(termIndex.getOutboundRelations(this.phase_du_stator))
			.hasSize(1)
			.extracting(TermSuiteExtractors.RELATION_TYPE_RULE_TO)
			.contains(tuple(RelationType.SYNTACTICAL, "S-R2D-NPN", this.phase_statorique));
		assertThat(termIndex.getOutboundRelations(this.phase_statorique))
			.hasSize(0);
		
	}

}
