package siap.siep.fascicolo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;

/**
 * <p>
 * Title: IFascicoloSies
 * </p>
 * <p>
 * Description: Classe Controller per Fascicolo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IFascicoloSiep {

	public void ExCancellaFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public FascicoloSiepModel ExInserisciFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public ResidenzaAssociataModel ExInserisciResidenzaFascicoloSiep(ResidenzaAssociataModel aResidenza)
			throws F3BException;

	public ResidenzaAssociataModel ExRicercaResidenzaFascicoloSiepCorrente(BigDecimal aIdFascicolo)
			throws F3BException;

	public ResidenzaAssociataModel ExRicercaDomicilioFascicoloSiepCorrente(BigDecimal aIdFascicolo)
			throws F3BException;

	public void ExModificaFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public Vector ExRicercaFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public FascicoloSiepModel ExRicercaFascicoloSiepByProgrAnnoCodUfficio(FascicoloSiepModel aFascicoloSiep)
			throws F3BException;

	public Vector ExRicercaFascicoloSiepByProgrAnnoDescrComunePaged(FascicoloSiepModel aFascicoloSiep,
			int aPagenum) throws F3BException;

	// MEV_57: aggiunto parametro di passaggio
	public Vector ExRicercaFascicoloSiepByProgrAnnoDescrComunePaged(FascicoloSiepModel aFascicoloSiep,
			int aPagenum, String majorOffice) throws F3BException;

	// MEV_57: aggiunto parametro di passaggio
	public BigDecimal ExGetNumFascicoloSiepByProgrAnnoDescrComune(FascicoloSiepModel aFascicoloSiep,
			String majorOffice) throws F3BException;

	public FascicoloSiepModel ExRicercaFascicoloByKey(BigDecimal aKey) throws F3BException;

	public DettaglioFascicoloModel ExDettaglioFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException;

	public DettaglioFascicoloModel ExDettaglioPosizionePerFascicoloSiep(BigDecimal aIdFascicolo)
			throws F3BException;

	public Vector ExRicercaFascicoloSiepBySoggettoPaged(SoggettoModel aModel, int aPageNum)
			throws F3BException;

	public Vector ExRicercaFascicoloSiepBySoggettoPaged(SoggettoModel aModel, int aPageNum,
			String majorOffice) throws F3BException;

	public Vector ExRicercaFascicoloSiepByRGNRPaged(SentenzaModel aSentenza, int aPageNum, String majorOffice)
			throws F3BException;

	public BigDecimal ExGetCountFascicoloSiepByRGNRPaged(SentenzaModel aSentenza, String majorOffice)
			throws F3BException;

	public Vector ExRicercaFascicoloSiepBySuperSoggettoPaged(SoggettoModel aModel, int aPageNum)
			throws F3BException;

	// MEV_57: aggiunto parametro di passaggio
	public BigDecimal ExGetNumFascicoloSiepBySoggetto(SoggettoModel aSogModel, String majorOffice)
			throws F3BException;

	public BigDecimal ExGetNumFascicoloSiepBySuperSoggetto(SoggettoModel aSogModel) throws F3BException;

	public FascicoloSiepModel ExValidazione(FascicoloSiepModel aFasModel, StatoProcedimentoModel aStato)
			throws F3BException;

	public Vector ExRicercaFascicoloOnView(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public Vector ExRicercaFascicoloOnViewPaged(FascicoloSiepModel aFascicoloSiep, int aPage,
			String majorOffice, String tipoUfficioUtente) throws F3BException;

	public Vector ExRicercaFascicoloOnViewPaged(FascicoloSiepModel aFascicoloSiep, int aPage)
			throws F3BException;

	// MEV Agosto 2014 - Ricerca Procedimenti X Reato e Circostanze Aggravanti
	// Aggiunto criterio di Ricerca - Cumulati
	// public Vector ExRicercaFascicoloByReatoPaged(ReatoModel aModel,int aPage)
	public Vector ExRicercaFascicoloByReatoPaged(ReatoModel aModel, CircostanzaModel aModCirAggravanti,
			String TipoRic, Boolean solocumulati, int aPage) throws F3BException;

	public FascicoloSiepModel ExRicercaFascicoloByKeyNoError(BigDecimal aKey) throws F3BException;

	public BigDecimal ExgetCountFascicoli(FascicoloSiepModel aModel) throws F3BException;

	public BigDecimal ExGetCountFascicoliSoggetto(FascicoloSiepModel aModel) throws F3BException;

	public BigDecimal ExGetCountFascicoliSoggettoUfficio(FascicoloSiepModel aModel, String aCodUfficio)
			throws F3BException;

	public String ExInserisciFascicoloWithoutSequence(FascicoloSiepModel lFasc, Connection lConn)
			throws F3BException;

	/*
	 * public Vector ExRicercaFascicoliBySoggettoPagina(SoggettoModel aSogModel, String
	 * lCodUfficioUtenteConnesso) throws F3BException;
	 */
	public Vector ExRicercaFascicoloOnViewPagedSoggetti(FascicoloSiepModel aFascicoloSiep, int aPage,
			String TipoRicerca, String StrCodiceDistrettoUtente) throws F3BException;

	public Vector ExRicercaFascicoliBySoggettoPaged(SoggettoModel aSogModel, String lCodUfficioUtenteConnesso,
			int aPage, String lCodDistrettoUtenteConnesso, String TipoRicerca) throws F3BException;

	// MEV Agosto 2014 - Ricerca Procedimenti X Reato e Circostanze Aggravanti
	// Aggiunto criterio di Ricerca - Cumulati
	public BigDecimal ExgetCountReati(ReatoModel aReato, Boolean solocumulati) throws F3BException;

	public BigDecimal ExgetCountCircostanzeAggr(ReatoModel aReato, CircostanzaModel aCirco,
			Boolean solocumulati) throws F3BException;

	public BigDecimal ExgetCountReatiCircostanzeAggr(ReatoModel aReato, CircostanzaModel aCirco,
			Boolean solocumulati) throws F3BException;
	// End MEV

	public Vector ExRicercaFascicoloSiepSoggetto(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	// Metodo per la "Ricerca Procedimento per Soggetto" Alias/Ufficio o
	// Alias/Distretto
	public Vector ExRicercaSoggettoAliasFascicoloPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca) throws F3BException;

	public Vector ExRicercaSoggettoAliasFascicoloPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca, String majorOffice) throws F3BException;

	// Metodo per la Count "Ricerca Procedimento per Soggetto" Alias/Ufficio o
	// Alias/Distretto
	public BigDecimal ExGetCountSoggettoAliasFascicolo(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistrettoUtenteConnesso, String TipoRicerca)
			throws F3BException;

	public BigDecimal ExGetCountSoggettoAliasFascicolo(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistrettoUtenteConnesso, String TipoRicerca,
			String majorOffice) throws F3BException;

	public void ExModificaNoteFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public void ExModificaVisibilitaMinoreFascicoloSiep(FascicoloSiepModel aFascicoloSiep)
			throws F3BException;

	public void ExModificaKeyNscByKey(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public void ExModificaFasSiesIdFascicoloByKey(FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public Vector ExRicercaFascicoloSospesiInterrotiOnViewPaged(FascicoloSiepModel aFascicoloSiep,
			String[] aMotivo, int aPage, String aAggiuntoUnion) throws F3BException;

	public BigDecimal ExGetCountFascicoliSospesiInterrotti(FascicoloSiepModel aFascicolo, String[] aMotivo,
			String aAggiuntoUnion) throws F3BException;

	public Vector ExRicercaFascicoloSospesiInterrotiAll(FascicoloSiepModel aFascicoloSiep, String[] aMotivo,
			String aAggiuntoUnion) throws F3BException;

	public DettaglioFascicoloModel ExAltriDatiFascicoloSiep(DettaglioFascicoloModel aDettaglio,
			BigDecimal aIdFascicolo) throws F3BException;

	/**
	 * Ricerca l'elenco dei fascicoli correntemente assegnati a un magistrato su un particolare ufficio in
	 * base allo stato del fascicolo
	 *
	 * 20251010 [SG]: paginata la ricerca
	 *
	 * @param aCodMagistrato
	 *            - Codice CSM del magistrato
	 * @param aCodUfficio
	 *            - Codice ufficio di appartenenza del Procedimento
	 * @param aStato
	 *            - Array di COD_STATO_FASCICOLO
	 * @param aPage
	 *            - Paginazione
	 *
	 * @return Vector
	 */
	public Vector ExRicercaFascicoliByMagistratoAssegnatario(String aCodMagistrato, String aCodUfficio,
			String[] aStato, int aPage) throws F3BException;

	public BigDecimal ExGetCountProcedimenti(String lCodMagistrato, String lCodUfficio, String[] lStato)
			throws F3BException;

	// paolo cherubini x supersoggetto 22 luglio 2009
	public Vector ExRicercaFascicoloOnViewPagedSuperSoggetti(SoggettoModel aSogModel, String aCodUfficio,
			int aPage, String TipoRicerca, String StrCodiceDistrettoUtente) throws F3BException;

	public Vector ExRicercaFascicoloOnViewPagedSuperSoggetti(SoggettoModel aSogModel, String aCodUfficio,
			int aPage, String TipoRicerca, String StrCodiceDistrettoUtente, String majorOffice,
			String tipoUfficio, String campoDet) throws F3BException;

	// paolo cherubini conta x supersoggetto
	public BigDecimal ExCountFascicoloOnViewPagedSuperSoggetti(SoggettoModel aSogModel, String aCodUfficio,
			int aPage, String TipoRicerca, String StrCodiceDistrettoUtente) throws F3BException;

	public BigDecimal ExCountFascicoloOnViewPagedSuperSoggetti(SoggettoModel aSogModel, String aCodUfficio,
			int aPage, String TipoRicerca, String StrCodiceDistrettoUtente, String majorOffice,
			String tipoUfficio) throws F3BException;
	// fine paolo

	// Ambros SuperSoggetto 08/2009
	public Vector ExRicercaFascicoliBySuperSoggettoPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca) throws F3BException;

	public Vector ExRicercaFascicoliBySuperSoggettoPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca, String majorOffice, boolean fromDetail, String tipoUffcio)
			throws F3BException;

	public BigDecimal ExCountFascicoliBySuperSoggettoPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca) throws F3BException;

	public BigDecimal ExCountFascicoliBySuperSoggettoPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca, String majorOffice, boolean fromDetail, String tipoUffcio)
			throws F3BException;

	// Cherubini 03/2010
	public ResidenzaAssociataModel ExDeassociaResidenzaFascicoloSiep(ResidenzaAssociataModel aResidenza)
			throws F3BException;

	public FascicoloSiepModel ExArchiviazione(FascicoloSiepModel aFasModel, StatoProcedimentoModel aStato)
			throws F3BException;

	public FascicoloSiepModel ExRicercaFascicoloSiepByIdSentenza(BigDecimal aKey) throws F3BException;

	public ByteArrayOutputStream ExGetCertificatoPenale(BigDecimal aKey) throws F3BException;

	public BigDecimal ExGetLengthCertPenaleByIdFascicolo(BigDecimal aIdFascicoloSius) throws F3BException;

	public Vector<FascicoloSiepModel> ExRicercaFascicoliPerSoggetto(SoggettoModel aSoggettoModel)
			throws F3BException;

	public Vector ExRicercaFascSIEPDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto) throws F3BException;

	public Vector<FascicoloSiepModel> ExRicercaFascicoloSiepBySuperSoggettoPerSIGEPaged(SoggettoModel aModel,
			int aPageNum) throws F3BException;

	// MEV_57: aggiunto parametro di passaggio
	public Vector<FascicoloSiepModel> ExRicercaFascicoloSiepBySoggettoPerSIGEPaged(SoggettoModel aModel,
			int aPageNum, String majorOffice) throws F3BException;

	// MERGE v10: aggiunta funzione di ricerca dettaglio
	public DettaglioFascicoloModel ExDettaglioFascicoloSiepNew(BigDecimal aId) throws F3BException;

	public BigDecimal ExGetNumFascicoloSiepBySoggettoSIGE(SoggettoModel aSogModel) throws F3BException;

	// MEV 26 CUMULO -step 2 - Ricerca Procedimento By reato
	public BigDecimal ExRicercaIstruttoriaCumuloByIdFascicoloSiep(BigDecimal aIdFasc) throws F3BException;

	// MEV PLO ANOMALIE SIUS
	public String ExRicercaFasCollegatiFascicoloByKey(BigDecimal aIdFasc, String classe, String ufficio)
			throws F3BException;

	/**
	 *
	 * // [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: creo nuovo metodo passando anche il controllo
	 * su ufficio minorenne o meno
	 *
	 * @param aFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExRicercaFascicoloSiepByProgrAnnoCodUfficio(FascicoloSiepModel aFascicoloSiep,
			String majorOffice) throws F3BException;

}