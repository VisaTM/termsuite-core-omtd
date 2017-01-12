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
package fr.univnantes.termsuite.test.unit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

import fr.univnantes.termsuite.framework.TerminologyService;
import fr.univnantes.termsuite.model.Term;
import fr.univnantes.termsuite.model.TermWord;
import fr.univnantes.termsuite.model.Terminology;
import fr.univnantes.termsuite.model.Word;
import fr.univnantes.termsuite.test.unit.Fixtures;
import fr.univnantes.termsuite.utils.TermUtils;

public class TermUtilsSpec {
	
	private Terminology termino;
	private TerminologyService terminoService;
	
	private Term energie;
	private Term eolien;
	private Term energie_eolien;
	
	private Term acces;
	private Term radioelectrique;
	private Term recouvrement;
	private Term total;
	private Term recouvrement_total;
	private Term acces_radioelectrique;
	private Term acces_radioelectrique_de_recouvrement;
	private Term acces_radioelectrique_de_recouvrement_total;
	
	private List<Term> allTerms;
	
	@Before
	public void setUp() {
		termino = Fixtures.termino();
		energie = Fixtures.term10(termino);
		eolien = Fixtures.term11(termino);
		energie_eolien = Fixtures.term1(termino);
		
		acces_radioelectrique_de_recouvrement_total = Fixtures.term4(termino);
		acces_radioelectrique_de_recouvrement = Fixtures.term3(termino);
		radioelectrique = Fixtures.term2(termino);
		acces_radioelectrique = Fixtures.term5(termino);
		acces = Fixtures.term8(termino);
		recouvrement = Fixtures.term9(termino);
		total = Fixtures.term7(termino);
		recouvrement_total = Fixtures.term12(termino);
		
		allTerms = new ArrayList<Term>();
		allTerms.add(energie);
		allTerms.add(eolien);
		allTerms.add(energie_eolien);
		allTerms.add(acces_radioelectrique_de_recouvrement_total); 
		allTerms.add(acces_radioelectrique_de_recouvrement);
		allTerms.add(acces_radioelectrique);
		allTerms.add(acces); 
		allTerms.add(radioelectrique);
		allTerms.add(total);
		allTerms.add(recouvrement);
		terminoService = new TerminologyService(termino);
	}

		
	@Test
	public void testCollapseText() {
		assertEquals("la vie, est belle", TermUtils.collapseText("   la\n    vie,\rest   belle "));
		assertEquals("la vie , est belle", TermUtils.collapseText(" \n  la    vie  , est   belle "));
	}
	
	@Test
	public void testIsIncludedIn() {
		assertFalse(TermUtils.isIncludedIn(energie_eolien, energie_eolien));
		assertTrue(TermUtils.isIncludedIn(eolien, energie_eolien));
		assertTrue(TermUtils.isIncludedIn(energie, energie_eolien));
		
		assertTrue(TermUtils.isIncludedIn(acces, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isIncludedIn(acces_radioelectrique, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isIncludedIn(acces_radioelectrique_de_recouvrement, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isIncludedIn(radioelectrique, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isIncludedIn(recouvrement, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isIncludedIn(recouvrement_total, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isIncludedIn(total, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isIncludedIn(acces_radioelectrique_de_recouvrement_total, acces_radioelectrique_de_recouvrement_total));

		assertFalse(TermUtils.isIncludedIn(total, acces_radioelectrique_de_recouvrement));
		assertTrue(TermUtils.isIncludedIn(recouvrement, acces_radioelectrique_de_recouvrement));
		assertTrue(TermUtils.isIncludedIn(radioelectrique, acces_radioelectrique_de_recouvrement));

		assertFalse(TermUtils.isIncludedIn(acces_radioelectrique_de_recouvrement_total, acces_radioelectrique_de_recouvrement));
		assertFalse(TermUtils.isIncludedIn(acces_radioelectrique_de_recouvrement_total, radioelectrique));
		assertFalse(TermUtils.isIncludedIn(acces_radioelectrique_de_recouvrement_total, recouvrement));
		
	}

	@Test
	public void testIsPrefixOf() {
		assertTrue(TermUtils.isPrefixOf(energie_eolien, energie_eolien));
		assertFalse(TermUtils.isPrefixOf(eolien, energie_eolien));
		assertTrue(TermUtils.isPrefixOf(energie, energie_eolien));
		
		assertTrue(TermUtils.isPrefixOf(acces, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isPrefixOf(acces_radioelectrique, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isPrefixOf(acces_radioelectrique_de_recouvrement, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isPrefixOf(radioelectrique, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isPrefixOf(recouvrement, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isPrefixOf(recouvrement_total, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isPrefixOf(total, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isPrefixOf(acces_radioelectrique_de_recouvrement_total, acces_radioelectrique_de_recouvrement_total));

		assertFalse(TermUtils.isPrefixOf(total, acces_radioelectrique_de_recouvrement));
		assertFalse(TermUtils.isPrefixOf(recouvrement, acces_radioelectrique_de_recouvrement));
		assertFalse(TermUtils.isPrefixOf(radioelectrique, acces_radioelectrique_de_recouvrement));

	}
	
	@Test
	public void testIsSuffixOf() {
		assertTrue(TermUtils.isSuffixOf(energie_eolien, energie_eolien));
		assertTrue(TermUtils.isSuffixOf(eolien, energie_eolien));
		assertFalse(TermUtils.isSuffixOf(energie, energie_eolien));
		
		assertFalse(TermUtils.isSuffixOf(acces, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isSuffixOf(acces_radioelectrique, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isSuffixOf(acces_radioelectrique_de_recouvrement, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isSuffixOf(radioelectrique, acces_radioelectrique_de_recouvrement_total));
		assertFalse(TermUtils.isSuffixOf(recouvrement, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isSuffixOf(recouvrement_total, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isSuffixOf(total, acces_radioelectrique_de_recouvrement_total));
		assertTrue(TermUtils.isSuffixOf(acces_radioelectrique_de_recouvrement_total, acces_radioelectrique_de_recouvrement_total));

		assertFalse(TermUtils.isSuffixOf(total, acces_radioelectrique_de_recouvrement));
		assertTrue(TermUtils.isSuffixOf(recouvrement, acces_radioelectrique_de_recouvrement));
		assertFalse(TermUtils.isSuffixOf(radioelectrique, acces_radioelectrique_de_recouvrement));

	}

		
	@Test
	public void testGetExtensionAffix() {
		assertEquals(
				eolien, 
				TermUtils.getExtensionAffix(terminoService, energie, energie_eolien)
				);
		
		assertEquals(
				energie, 
				TermUtils.getExtensionAffix(terminoService, eolien, energie_eolien)
				);

		
		assertEquals(
				acces,
				TermUtils.getExtensionAffix(terminoService, radioelectrique, acces_radioelectrique)
				);

		assertEquals(
				radioelectrique,
				TermUtils.getExtensionAffix(terminoService, acces, acces_radioelectrique)
				);

		assertEquals(
				acces_radioelectrique,
				TermUtils.getExtensionAffix(terminoService, recouvrement, acces_radioelectrique_de_recouvrement)
				);

		assertEquals(
				recouvrement,
				TermUtils.getExtensionAffix(terminoService, acces_radioelectrique, acces_radioelectrique_de_recouvrement)
				);

		assertEquals(
				recouvrement_total,
				TermUtils.getExtensionAffix(terminoService, acces_radioelectrique, acces_radioelectrique_de_recouvrement_total)
				);
		
		for(Term t:allTerms)
			assertEquals(
					null,
					TermUtils.getExtensionAffix(terminoService, t, t)
				);
	}
	
	@Test
	public void testPosition() {
		assertEquals(
				0,
				TermUtils.getPosition(recouvrement, recouvrement)
				);
		assertEquals(
				0,
				TermUtils.getPosition(recouvrement_total, recouvrement_total)
				);
		assertEquals(
				1,
				TermUtils.getPosition(total, recouvrement_total)
				);
		assertEquals(
				0,
				TermUtils.getPosition(recouvrement, recouvrement_total)
				);
		assertEquals(
				3,
				TermUtils.getPosition(recouvrement_total, acces_radioelectrique_de_recouvrement_total)
				);
		assertEquals(
				4,
				TermUtils.getPosition(total, acces_radioelectrique_de_recouvrement_total)
				);
		
		assertEquals(
				3,
				TermUtils.getPosition(recouvrement, acces_radioelectrique_de_recouvrement_total)
				);
		assertEquals(
				-1,
				TermUtils.getPosition(
						acces_radioelectrique_de_recouvrement_total,
						recouvrement
						)
				);
		assertEquals(
				-1,
				TermUtils.getPosition(acces, recouvrement)
			);
	}

	@Test
	public void testFindBiggestPrefix() {
		assertEquals(
				recouvrement_total,
				TermUtils.findBiggestPrefix(terminoService, recouvrement_total.getWords())
				);
		
		assertEquals(
				recouvrement,
				TermUtils.findBiggestPrefix(terminoService, recouvrement.getWords())
				);
		
		assertEquals(
				null,
				TermUtils.findBiggestPrefix(terminoService, termWords("de", "P"))
			);
		
		assertEquals(
				recouvrement_total,
				TermUtils.findBiggestPrefix(terminoService, termWords("recouvrement", "N", "total", "A", "de", "P", "pezojc", "U"))
			);
		assertEquals(
				null,
				TermUtils.findBiggestPrefix(terminoService, termWords("de", "P", "recouvrement", "N", "total", "A", "de", "P", "pezojc", "U"))
			);
	}

	public List<TermWord> termWords(Object... objects) {
		List<TermWord> l = Lists.newArrayList();
		for(int i=0; i<objects.length; i+=2) {
			TermWord tw = Mockito.mock(TermWord.class);
			when(tw.getSyntacticLabel()).thenReturn((String)objects[i+1]);
			Word w = Mockito.mock(Word.class);
			when(w.getLemma()).thenReturn((String)objects[i]);
			when(tw.getWord()).thenReturn(w);
			l.add(tw);
		}
		return l;
	}

	@Test
	public void testFindBiggestSuffix() {
		assertEquals(
				recouvrement_total,
				TermUtils.findBiggestSuffix(terminoService, recouvrement_total.getWords())
				);

		assertEquals(
				recouvrement,
				TermUtils.findBiggestSuffix(terminoService, recouvrement.getWords())
				);

		assertEquals(
				total,
				TermUtils.findBiggestSuffix(terminoService, total.getWords())
				);
		

		assertEquals(
				null,
				TermUtils.findBiggestSuffix(terminoService, termWords("de", "P"))
			);
		
		assertEquals(
				recouvrement_total,
				TermUtils.findBiggestSuffix(terminoService, termWords("de", "P", "pezojc", "U", "recouvrement", "N", "total", "A"))
			);
		assertEquals(
				null,
				TermUtils.findBiggestPrefix(terminoService, termWords("de", "P", "recouvrement", "N", "total", "A", "de", "P"))
			);

	}


}
