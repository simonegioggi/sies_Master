package siap.siep.sentenza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.sentenza.model.SentenzaFascicoliModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.util.F3BException;

/**
 * <p>Title: ISentenza</p>
 * <p>Description: Classe Controller per Sentenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ISentenza {

	public void ExCancellaSentenza(SentenzaModel aSentenza) throws F3BException;

	public SentenzaModel ExInserisciSentenza(SentenzaModel aSentenza) throws F3BException;

	public SentenzaModel ExModificaSentenza(SentenzaModel aSentenza) throws F3BException;

	public Vector ExRicercaSentenzaPaged(SentenzaModel aSentenza, int aPage) throws F3BException;

	public Vector ExRicercaSentenza(SentenzaModel aSentenza) throws F3BException;

	// MEV 16: aggiunti parametri di passaggii per differenziare collegato al cumulo
	public Vector ExRicercaSentenzaWebServices(SentenzaModel aSentenza, boolean isForCumulo,
			String idFascicoloSiep, String idIstruttoriaCumulo) throws F3BException;

	public SentenzaModel ExRicercaSentenzaByKey(BigDecimal aSentenza) throws F3BException;

	public BigDecimal ExGetCountSentenze(SentenzaModel aSentenza) throws F3BException;

	public String ExInserisciSentenzaWithoutSequence(SentenzaModel lSent, Connection lConn)
			throws F3BException;

	public Vector ExRicercaSentenzaDuplicata(SentenzaModel aSentenza) throws F3BException;
	// Ticket#202204010111 - Aggiunto recupero di eventuali fascicoli SIEP
	public Vector <SentenzaFascicoliModel> ExRicercaSentenzaDuplicataFascicoli(SentenzaModel aSentenza) throws F3BException;
	// Ticket#202204010111 - FINE
	public Vector ExRicercaSentenzaFascicolo(SoggettoModel aSoggetto, String TipoBen) throws F3BException;

	public SentenzaModel ExModificaSentenzaSige(SentenzaModel aSentenza, Vector aKeyFascicoli)
			throws F3BException;

	public Vector ExRicercaSentenzaFascicoliPaged(SentenzaModel aSentenza, int aPage) throws F3BException;

	// inizio modifica marzo 2010
	public BigDecimal ExGetCountSentenzeSoggetto(SoggettoModel aSoggetto, SentenzaModel aSentenza)
			throws F3BException;

	public Vector ExRicercaSentenzeSoggettoPaged(SentenzaModel aSentenza, SoggettoModel aSoggetto, int aPage)
			throws F3BException;

	// fine modifica marzo 2010

	public Vector ExRicercaElencoTitoliEsecutiviIscrittiaSIGEPaged(BigDecimal idFascicolo, int aPage)
			throws F3BException;

	public BigDecimal ExGetCountaElencoTitoliEsecutiviIscrittiaSIGE(BigDecimal idFascicolo)
			throws F3BException;

}