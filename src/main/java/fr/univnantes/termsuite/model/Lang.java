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
package fr.univnantes.termsuite.model;

import java.util.Locale;
import java.util.NoSuchElementException;

import fr.univnantes.termsuite.LanguageException;
import fr.univnantes.termsuite.engines.EngineState;
import fr.univnantes.termsuite.resources.ScorerConfig;
import fr.univnantes.termsuite.utils.OccurrenceBuffer;

public enum Lang {
	FR("french", Locale.FRENCH, OccurrenceBuffer.NO_CLEANING, 0.5d, 0.1d, 0.1d, 0.3d, 0.7d, 3, 3, ScorerConfig.create(0.5, 0.1,0.1, 0.25), 1, EngineState.ENABLED),
	EN("english", Locale.ENGLISH, OccurrenceBuffer.NO_CLEANING, 0.7d, 0.1d, 0.1d, 0.1d, 0.85d, 3, 3, ScorerConfig.create(0.5, 0.1,0.1, 0.25), 1, EngineState.ENABLED),
	ES("spanish", Locale.FRENCH, OccurrenceBuffer.NO_CLEANING, 0.5d, 0.1d, 0.1d, 0.3d, 1d, 3, 3, ScorerConfig.create(0.5, 0.1,0.1, 0.25), 1, EngineState.ENABLED),
	DE("german", Locale.GERMAN, OccurrenceBuffer.NO_CLEANING, 0.5d, 0.3d, 0.1d, 0.1d, 0.75d, 3, 4, ScorerConfig.create(0.5, 0.1,0.1, 0.25), 2, EngineState.ENABLED),
	ZH("chinese", Locale.CHINESE, OccurrenceBuffer.NO_CLEANING, 0.5d, 0.1d, 0.1d, 0.3d, 0.7d, 3, 2, ScorerConfig.create(0.5, 0.1,0.1, 0.25), 1, EngineState.DISABLED),
	LV("latvian", Locale.GERMAN, OccurrenceBuffer.NO_CLEANING,0.5d, 0.1d, 0.1d, 0.3d, 0.8d, 3, 3, ScorerConfig.create(0.5, 0.1,0.1, 0.25), 1, EngineState.ENABLED),
	RU("russian", Locale.JAPAN, OccurrenceBuffer.NO_CLEANING,0.3d, 0.1d, 0.4d, 0.2d, 0.7d, 3, 3,ScorerConfig.create(0.5, 0.1,0.1, 0.25), 2, EngineState.ENABLED),
	DA("danish", Locale.GERMAN, OccurrenceBuffer.NO_CLEANING,0.5f, 0.1f, 0.1f, 0.3f, 0.8f, 3, 3, ScorerConfig.create(0.5, 0.1,0.1, 0.25), 1, EngineState.ENABLED);
	
	private final double compostAlpha;
	private final double compostBeta;
	private final double compostGamma;
	private final double compostDelta;
	private final double compostScoreThreshold;
	private final int compostMinComponentSize;
	private final int compostMaxComponentNumber;
	private final Locale locale;
	private final String longLang;
	private final String regexPostProcessingStrategy;
	private final int gVariantNbPreindexingLetters;
	private final EngineState gVariantGatheringState;

	private ScorerConfig scorerConfig;


    private Lang(String longLang, Locale locale, String regexPostProcessingStrategy, 
    		double compostAlpha,
    		double compostBeta,
    		double compostGamma,
    		double compostDelta,
    		double compostCompostThreshold,
    		int compostMinComponentSize,
    		int compostMaxComponentNumber,
    		ScorerConfig scorefierConfig,
    		int gVariantNbPreindexingLetters,
    		EngineState gVariantGatheringState
    		) {
    	this.locale = locale;
        this.longLang = longLang;
        this.regexPostProcessingStrategy = regexPostProcessingStrategy;
        this.compostAlpha = compostAlpha;
        this.compostBeta = compostBeta;
        this.compostGamma = compostGamma;
        this.compostDelta = compostDelta;
        this.compostScoreThreshold = compostCompostThreshold;
        this.compostMinComponentSize = compostMinComponentSize;
        this.compostMaxComponentNumber = compostMaxComponentNumber;
        this.scorerConfig = scorefierConfig;
        this.gVariantNbPreindexingLetters = gVariantNbPreindexingLetters;
        this.gVariantGatheringState = gVariantGatheringState;
    }
    
    public String getName() {
		return longLang;
	}
    
    public String getNameUC() {
		return getName().substring(0,1).toUpperCase() + getName().substring(1);
	}
    
    @Override
    public String toString() {
    	return getCode();
    }

	public String getCode() {
		return name().toLowerCase();
	}
    
    public String getRegexPostProcessingStrategy() {
		return regexPostProcessingStrategy;
	}
    
	public static void checkLang(String lang) {
		if(!isLanguageSupported(lang))
			throw new LanguageException(lang);
	}

	public static boolean isLanguageSupported(String lang) {
		for(Lang l:Lang.values()) {
			if(l.getCode().equals(lang)) 
				return true;
		}
		return false;
	}
	
	public static Lang forName(String lang) {
		for(Lang l:Lang.values()) {
			if(l.getCode().equals(lang))
				return l;
		}
		throw new LanguageException(lang);
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public double getCompostAlpha() {
		return compostAlpha;
	}
	
	public double getCompostBeta() {
		return compostBeta;
	}
	public double getCompostDelta() {
		return compostDelta;
	}
	public double getCompostGamma() {
		return compostGamma;
	}
	
	public int getCompostMaxComponentNumber() {
		return compostMaxComponentNumber;
	}
	
	public double getCompostScoreThreshold() {
		return compostScoreThreshold;
	}
	
	public static Lang fromCode(String code) {
		for(Lang l:values())
			if(l.getCode().equals(code))
				return l;
		throw new NoSuchElementException(code);
	}

	public ScorerConfig getScorerConfig() {
		return scorerConfig;
	}
	
	public int getCompostMinComponentSize() {
		return compostMinComponentSize;
	}
	
	public int getGraphicalVariantNbPreindexingLetters() {
		return gVariantNbPreindexingLetters;
	}
	
	public EngineState getgVariantGatheringState() {
		return gVariantGatheringState;
	}
}
