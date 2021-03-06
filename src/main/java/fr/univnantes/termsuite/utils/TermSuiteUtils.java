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
package fr.univnantes.termsuite.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.mutable.MutableInt;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.univnantes.lina.uima.tkregex.LabelledAnnotation;
import fr.univnantes.lina.uima.tkregex.RegexOccurrence;
import fr.univnantes.termsuite.model.TermWord;
import fr.univnantes.termsuite.model.Word;
import fr.univnantes.termsuite.types.TermOccAnnotation;
import fr.univnantes.termsuite.types.WordAnnotation;

public class TermSuiteUtils {
	private static final String GROUPING_KEY_FORMAT = "%s: %s";
	public static final IndexingKey<String, String> KEY_ONE_FIRST_LETTERS = getNFirstLetterIndexingKey(1);
	public static final IndexingKey<String, String> KEY_TWO_FIRST_LETTERS = getNFirstLetterIndexingKey(2);
	public static final IndexingKey<String, String> KEY_THREE_FIRST_LETTERS = getNFirstLetterIndexingKey(3);
	
	public static IndexingKey<String, String> getNFirstLetterIndexingKey(final int n) {
		Preconditions.checkArgument(n>0, "n must be greater than 0");
		return new IndexingKey<String, String>() {
			@Override
			public String getIndexKey(String fullString) {
				if(fullString.length()<=n)
					return fullString;
				else
					return fullString.substring(0, n);
				
			}
		};
	}

	public static String getSingleWordTermId(WordAnnotation word) {
		/*
		 * Single word terms are pre-gathered by their lemma
		 */
		return word.getLemma();
	}

	public static <T> LinkedHashMap<T, Integer> getCounters(Iterable<T> list) {
		Comparator<Entry<T, MutableInt>> comparator = new Comparator<Entry<T, MutableInt>>() {
			public int compare(Entry<T,MutableInt> o1, Entry<T,MutableInt> o2) {
				return ComparisonChain.start()
						.compare(o2.getValue(), o1.getValue())
						.result();
			};
		};
		
		Map<T, MutableInt> map = Maps.newHashMap();
		for(T e:list) {
			MutableInt counter = map.get(e);
			if(counter == null) {
				counter = new MutableInt(0);
				map.put(e, counter);
			}
			counter.increment();
		}
		List<Entry<T, MutableInt>> entries = Lists.newArrayList(map.entrySet());
		Collections.sort(entries, comparator);
		LinkedHashMap<T, Integer> counters = Maps.newLinkedHashMap();
		for(Entry<T, MutableInt> e:entries)
			counters.put(e.getKey(), e.getValue().intValue());
		return counters;
	}

	public static String trimInside(String coveredText) {
		return coveredText.replaceAll(TermSuiteConstants.WHITESPACE_PATTERN_STRING, TermSuiteConstants.WHITESPACE_STRING).trim();
	}
	
	public static String getGroupingKey(String[] pattern, Word[] words) {
		StringBuilder sb = new StringBuilder();
		for(String s:pattern)
			sb.append(s.toLowerCase());
		sb.append(TermSuiteConstants.COLONS);
		sb.append(TermSuiteConstants.WHITESPACE);
		for(int i=0; i<words.length;i++) {
			if(i>0)
				sb.append(TermSuiteConstants.WHITESPACE);
			sb.append(words[i].getLemma().toLowerCase());
		}
		return sb.toString();
	}

	public static String getGroupingKey(TermOccAnnotation annotation) {
		StringBuilder patternSb = new StringBuilder();
		List<String> lemmas = Lists.newArrayListWithExpectedSize(annotation.getWords().size());
		for(int i=0; i< annotation.getWords().size(); i++) {
			patternSb.append(annotation.getPattern(i).toLowerCase());
			lemmas.add(annotation.getWords(i).getLemma());
		}
		return toGroupingKey(patternSb, lemmas);
	}

	public static String toGroupingKey(RegexOccurrence occurrence) {
		StringBuilder builder = new StringBuilder();
		builder
			.append(Joiner.on("").join(occurrence.getLabels()))
			.append(TermSuiteConstants.COLONS)
			.append(TermSuiteConstants.WHITESPACE);
		
		int i = 0;
		for(LabelledAnnotation la:occurrence.getLabelledAnnotations()) {
			if(i>= 1)
				builder.append(TermSuiteConstants.WHITESPACE);
			builder.append(((WordAnnotation) la.getAnnotation()).getLemma());
			i++;
		}
		return builder.toString().toLowerCase();
	}

	private static String toGroupingKey(StringBuilder patternSb, List<String> lemmas) {
		return String.format(GROUPING_KEY_FORMAT, 
				patternSb.toString(), 
				Joiner.on(TermSuiteConstants.WHITESPACE).join(lemmas));
	}
	

	public static String getGroupingKey(TermWord... words) {
		return getGroupingKey(Lists.newArrayList(words));
	}
	public static String getGroupingKey(Collection<TermWord> words) {
		StringBuilder patternSb = new StringBuilder();
		List<String> lemmas = Lists.newArrayListWithExpectedSize(words.size());
		for(TermWord tw:words) {
			patternSb.append(tw.getSyntacticLabel().toLowerCase());
			lemmas.add(tw.getWord().getLemma());
		}
		return toGroupingKey(patternSb, lemmas);
	}

	
	
	/**
	 * 
	 */
	public static void listClasspath() {
	  ClassLoader cl = ClassLoader.getSystemClassLoader();
	  
      URL[] urls = ((URLClassLoader)cl).getURLs();

      for(URL url: urls){
      	System.out.println(url.getFile());
      }
	}
	
}
