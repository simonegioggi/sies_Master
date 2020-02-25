package siap.sico.soggetto.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.soggetto.model.SoggettoModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISoggettoFascicolo {

	// Ambros SuperSoggetto 03/2010
	public Vector ExRicercaSoggettoPerDistrettoProgFasc(SoggettoModel aSoggetto, String aDistretto, int aPage)
			throws F3BException;

	// Ambros SuperSoggetto 03/2010
	public Vector ExRicercaSoggettoPerDistrettoProgFasc(SoggettoModel aSoggetto, String aDistretto, int aPage,
			String majorOffice) throws F3BException;

	// Paolo conta SuperSoggetto 04/2010
	public BigDecimal ExCountSoggettoPerDistrettoProgFasc(SoggettoModel aSoggetto, String aDistretto,
			int aPage) throws F3BException;

	// Paolo conta SuperSoggetto 04/2010
	public BigDecimal ExCountSoggettoPerDistrettoProgFasc(SoggettoModel aSoggetto, String aDistretto,
			int aPage, String majorOffice) throws F3BException;

}