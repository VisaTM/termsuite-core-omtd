package fr.univnantes.termsuite.export;

import java.io.IOException;
import java.io.Writer;

import com.google.common.collect.Lists;

import fr.univnantes.termsuite.api.TermSuiteException;
import fr.univnantes.termsuite.api.Traverser;
import fr.univnantes.termsuite.api.TsvOptions;
import fr.univnantes.termsuite.model.RelationProperty;
import fr.univnantes.termsuite.model.RelationType;
import fr.univnantes.termsuite.model.Term;
import fr.univnantes.termsuite.model.Terminology;
import fr.univnantes.termsuite.uima.engines.export.IndexerTSVBuilder;

public class TsvExporter {
	
	private Terminology termino;
	private Writer writer;
	private TsvOptions options;
	private Traverser traverser;
	
	private TsvExporter(Terminology termino, Writer writer, Traverser traverser, TsvOptions options) {
		super();
		this.termino = termino;
		this.writer = writer;
		this.options = options;
		this.traverser = traverser;
	}

	public static void export(Terminology termino, Writer writer) {
		export(termino, writer, Traverser.create(), new TsvOptions());
	}

	public static void export(Terminology termino, Writer writer,  TsvOptions options) {
		new TsvExporter(termino, writer, Traverser.create(), options).doExport();
	}
	
	public static void export(Terminology termino, Writer writer, Traverser traverser, TsvOptions options) {
		new TsvExporter(termino, writer, traverser, options).doExport();
	}

	private void doExport() {
		
		final IndexerTSVBuilder tsv = new IndexerTSVBuilder(
				writer,
				Lists.newArrayList(options.properties())
			);
		
		try {
			if(options.showHeaders())
				tsv.writeHeaders();
				
			for(Term t:traverser.toList(termino)) {
				tsv.startTerm(termino, t);
				
				if(options.isShowVariants())
					termino.getOutboundRelations(t, RelationType.VARIATION)
						.stream()
						.sorted(RelationProperty.VARIANT_SCORE.getComparator(true))
						.limit(options.getMaxVariantsPerTerm())
						.forEach(tv -> {
							try {
								boolean hasVariant = !termino.getOutboundRelations(tv.getTo(), RelationType.VARIATION).isEmpty();
								tsv.addVariant(
										termino, 
										tv,
										options.tagsTermsHavingVariants() && hasVariant);
							} catch (IOException e) {
								throw new TermSuiteException(e);
							}
					});
			}
			tsv.close();
		} catch (IOException e) {
			throw new TermSuiteException(e);
		}
	}
}