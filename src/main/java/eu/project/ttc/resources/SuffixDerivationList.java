package eu.project.ttc.resources;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import eu.project.ttc.models.TermWord;
import eu.project.ttc.utils.TermSuiteConstants;
import fr.univnantes.julestar.uima.resources.TabResource;

public class SuffixDerivationList extends TabResource {
	
	public static class SuffixDerivationEntry {
		private String suffix;
		private String label;
		public SuffixDerivationEntry(String suffix, String label) {
			super();
			this.suffix = suffix;
			this.label = label;
		}
		
		public String getSuffix() {
			return suffix;
		}
		public String getLabel() {
			return label;
		}
		@Override
		public int hashCode() {
			return Objects.hashCode(suffix, label);
		}
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof SuffixDerivationEntry) {
				SuffixDerivationEntry other = (SuffixDerivationEntry)obj;
				return Objects.equal(suffix, other.suffix)
							&& Objects.equal(label, other.label);
			} else
				return false;
		}

	}
	
	public SuffixDerivationList() {
		super(TermSuiteConstants.TAB);
	}

	public static final String SUFFIX_DERIVATIONS = "SuffixDerivations";
	public static final String ERR_SHOULD_START_WITH_HYPHEN = "Suffix should start with hyphen. Got: \"%s\" at line %s ";
	private static final String HYPHEN = "-";
	
	private Map<SuffixDerivationEntry, Set<SuffixDerivation>> derivations = Maps.newHashMap();
	private static final String ERR_REQUIRES_THREE_COLUMNS = "Row must have 3 columns at line %s. Got %s columns (line: \"%s\").";
	private static final String ERR_PATTERN_MUST_BE_SIZE_2 = "Derivation pattern must be of size 2 at line %s. Got size %s (line: \"%s\")";

	@Override
	protected void doRow(int lineNum, String line, String[] values) {
		Preconditions.checkArgument(values.length == 3, ERR_REQUIRES_THREE_COLUMNS,
				lineNum,
				values.length,
				line);
		List<String> pattern = Splitter.on(TermSuiteConstants.WHITESPACE).splitToList(values[0]);
		Preconditions.checkArgument(pattern.size() == 2, ERR_PATTERN_MUST_BE_SIZE_2,
				lineNum,
				pattern.size(),
				line);
		String derivateFormLabel = pattern.get(0).trim();
		String baseFormLabel = pattern.get(1).trim();
		
		
		String derivateFormSuffix = values[1];
		String baseFormSuffix = values[2];
		
		Preconditions.checkArgument(derivateFormSuffix.startsWith(HYPHEN), ERR_SHOULD_START_WITH_HYPHEN, derivateFormSuffix, lineNum);
		Preconditions.checkArgument(baseFormSuffix.startsWith(HYPHEN), ERR_SHOULD_START_WITH_HYPHEN, baseFormSuffix, lineNum);
		String actualDerivateFormSuffix = derivateFormSuffix.substring(1);
		String actualToSuffix = baseFormSuffix.substring(1);
		SuffixDerivation suffixDerivation = new SuffixDerivation(
				derivateFormLabel, 
				baseFormLabel, 
				actualDerivateFormSuffix, 
				actualToSuffix);
		SuffixDerivationEntry suffixDerivationEntry = new SuffixDerivationEntry(
				actualDerivateFormSuffix, 
				derivateFormLabel);
		if(derivations.containsKey(suffixDerivationEntry)) {
			derivations.get(suffixDerivationEntry).add(suffixDerivation);
		} else {
			Set<SuffixDerivation> set = Sets.newHashSet();
			set.add(suffixDerivation);
			derivations.put(suffixDerivationEntry, set);
		}
	}
	
	
	public List<SuffixDerivation> getDerivationsFromDerivateForm(String candidateDerivateLemma, String label) {
		return getDerivationsFromDerivateForm(TermWord.create(candidateDerivateLemma, label));
	}
	
	public List<SuffixDerivation> getDerivationsFromDerivateForm(TermWord candidateDerivateTermWord) {
		List<SuffixDerivation> list = Lists.newArrayListWithExpectedSize(2);
		String suffix;
		Collection<SuffixDerivation> suffixDerivations;
		for(int i=0; i<candidateDerivateTermWord.getWord().getLemma().length(); i++) {
			suffix = candidateDerivateTermWord.getWord().getLemma().substring(i);
			SuffixDerivationEntry entry = new SuffixDerivationEntry(suffix, candidateDerivateTermWord.getSyntacticLabel());
			suffixDerivations = derivations.get(entry);
			if(suffixDerivations != null)
				list.addAll(suffixDerivations);
		}
		return list;
	}
	
	
	public Map<SuffixDerivationEntry, Set<SuffixDerivation>> getDerivations() {
		return derivations;
	}
}