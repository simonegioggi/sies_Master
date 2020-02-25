package siap.sige.tenore.action;

/**
* <p>Title: ActInserisciEsiti</p>
* <p>Description: Classe Action per l'inserimento degli esiti e di ulteriori dati per TenoreSige</p>
 <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSentenzaReatoModel;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActInserisciEsiti extends ActionSige implements ICostantiTenoreSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	TenoreSigeModel mTenore = null;
	TenoreSentenzaReatoModel[] mTenori = null;
	DatiProvvedimentoSigeModel[] mDatiProv = null;
	// Richieste del PM al GE
	Vector<AnnotazioneManualeModel> mListaRichieste = null;

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "processRequest : inizio");

		letturaDatiTenore();
		letturaDatiProvvedimento();
		// Lettura eventuale Annotazione Manuale (Indulto/Amnistia)
		AnnotazioneManualeModel lAnnotazioneManuale = letturaDatiAnnotazioneManuale();

		if (lAnnotazioneManuale != null)
			lAnnotazioneManuale.setTenIdTenoreSige(mTenore.getIdTenoreSige());

		// lettura tipo di esito
		String lTipoEsito = "U";
		try {
			lTipoEsito = getRequestStringParameter(CHECK_TIPO_ESITO);
		} catch (Exception e) {

		}

		IAnnotazioneManuale lCtrlAnnotazione = SIEPLookupRemote.getAnnotazioneManualeRemote();
		String selBenifici = super.getRequestStringParameter("selBenifici");
		String notSelBenifici = super.getRequestStringParameter("notSelBenifici");

		if (!super.isRequestParameterNullObj("isTitoliEsecutivi")
				&& super.getRequestStringParameter("isTitoliEsecutivi").equals("true")) {
			processTitoliEsecutivi();
			if (selBenifici != null && !selBenifici.equals(""))
				lCtrlAnnotazione.ExAggiornaFlagSelQuantum(selBenifici, "S");
			if (notSelBenifici != null && !notSelBenifici.equals(""))
				lCtrlAnnotazione.ExAggiornaFlagSelQuantum(notSelBenifici, "N");
			return this.buildLinkDettaglio();
		}

		if (lTipoEsito.equalsIgnoreCase("D")) {
			letturaEsitiDistinti();
			mTenore.setCodEsitoSige("0000");
		}

		// @emma 23072018 intervento post COLLAUDO 11.2 recupero Id_provvedimentp
		BigDecimal idProvv = null;
		try {
			idProvv = super.getRequestBigDecimalParameter("idProvvedimento");
			setRequestAttribute("idProvvedimento", idProvv.toString());
		} catch (Exception e) {
			siesLogger.debug("parametro idProvvedimento mancante");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("tenore : " + mTenore);
		ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
		lCtrl.ExInserisciEsitiOggetto(mTenore, mTenori, mDatiProv, lAnnotazioneManuale, mListaRichieste,
				idProvv);

		if (selBenifici != null && !selBenifici.equals(""))
			lCtrlAnnotazione.ExAggiornaFlagSelQuantum(selBenifici, "S");
		if (notSelBenifici != null && !notSelBenifici.equals(""))
			lCtrlAnnotazione.ExAggiornaFlagSelQuantum(notSelBenifici, "N");

		// Ritorno al punto di partenza
		// String lRetPage = ritornoDopoCancellazione("Inseriti gli esiti per l'oggetto", null);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "processRequest : fine");

		return this.buildLinkDettaglio();
	}

	private void letturaDatiTenore() throws Exception {
		mTenore = new TenoreSigeModel();

		// Lettura ID Tenore
		mTenore.setIdTenoreSige(getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE));

		// Cod Esito
		if (isRequestParameterNullObj(CAMPO_COD_ESITO_TENORE_SIGE))
			mTenore.setCodEsitoSige("-");
		else
			mTenore.setCodEsitoSige(getRequestStringParameter(CAMPO_COD_ESITO_TENORE_SIGE));

		// Ulteriore descrizione della decisione
		mTenore.setNote(getRequestStringParameter(CAMPO_NOTE));
		mTenore.setData(DateUtils.getSysDate());
		mTenore.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore
		mTenore.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																			// dell'operatore
		mTenore.setDataAggiornamento(DateUtils.getSysDate()); // data

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod esito sige : " + mTenore.getCodEsitoSige());
	}

	private void letturaEsitiDistinti() throws Exception {
		if (!isRequestParameterNullObj(CAMPO_ID_TEN_SEN_REA)) {
			String[] lListaEsiti = getRequestStringParameters(CAMPO_COD_ESITO_TEN_SEN_REA);
			String[] lListaId = getRequestStringParameters(CAMPO_ID_TEN_SEN_REA);
			mTenori = new TenoreSentenzaReatoModel[lListaId.length];

			// Valorizzazione dei record della tabella di relazione TENORE_SENTENZA_REATO
			for (int i = 0; i < lListaId.length; i++) {
				mTenori[i] = new TenoreSentenzaReatoModel();
				mTenori[i].setIdTenSenRea(new BigDecimal(lListaId[i]));
				mTenori[i].setCodEsito(lListaEsiti[i]);
				mTenori[i].setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore
				mTenori[i].setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																						// dell'operatore
				mTenori[i].setDataAggiornamento(DateUtils.getSysDate()); // data
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("esiti multipli ");
		}
	}

	private void letturaDatiProvvedimento() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "letturaDatiProvvedimento : inizio");
		if (isRequestChecked(CAMPO_COD_TIPO_DATI_PROV)) {
			// Lettura dei codici dei check vistati
			String[] lListaCodDatiProv = getRequestStringParameters(CAMPO_COD_TIPO_DATI_PROV);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("letturaDatiProvvedimento : num dati letti : " + lListaCodDatiProv.length);
			mDatiProv = new DatiProvvedimentoSigeModel[lListaCodDatiProv.length];

			// Valorizzazione dei DatiProvvedimentoSigeModel
			for (int i = 0; i < lListaCodDatiProv.length; i++) {
				mDatiProv[i] = new DatiProvvedimentoSigeModel();
				mDatiProv[i].setTenIdTenoreSige(getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE));
				mDatiProv[i].setCodTipoDatiProv(lListaCodDatiProv[i]);
				mDatiProv[i].setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore
				mDatiProv[i].setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																						// dell'operatore
				mDatiProv[i].setDataInserimento(DateUtils.getSysDate()); // data
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("esiti multipli ");
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "letturaDatiProvvedimento : fine");
	}

	private AnnotazioneManualeModel letturaDatiAnnotazioneManuale() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "letturaDatiAnnotazioneManuale : inizio");
		AnnotazioneManualeModel lAnnMod = null;

		// Il campo TIPO_ANNOTAZIONE indica la presenza dell'Annotazione Manuale
		if (!isRequestParameterNullObj("tipoannotazione")) {
			// Si risale ad eventuale Fascicolo SIEP in sessione
			BigDecimal lIdFascicolo = null;

			FascicoloSiepModel lFascicoloMod = getFascicoloSigeEstesoInSessione().getFascicoloSiep();
			if (lFascicoloMod != null)
				lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

			// FascicoloSiepModel lFascicoloAssociato =
			// (FascicoloSiepModel)FascicoloSigeEsteso.getFascicoloSiep();

			// ==========================================================================
			// Recupero i dati dell'Annotazione Manuale
			// ==========================================================================
			lAnnMod = new AnnotazioneManualeModel();
			lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
			lAnnMod.setCodTipoAnnotazione(getRequestStringParameter("tipoannotazione"));
			lAnnMod.setFlagPiuMeno(getRequestStringParameter("PM"));

			lAnnMod.setFlagValidato("N");
			lAnnMod.setFlagAppProvvisoria("-");
			lAnnMod.setCodFonte("-");
			lAnnMod.setCodSottonumerazione("-");
			lAnnMod.setCodCausaleComputo("-");
			lAnnMod.setSenIdSentenza(super.getRequestBigDecimalParameter("idSenSentenza"));
			lAnnMod.setCodDpr(getRequestStringParameter("dpr"));

			// Conformità alla richiesta
			if (isRequestParameterNullObj("TipoOrd"))
				lAnnMod.setFlagConforme("-");
			else
				lAnnMod.setFlagConforme(getRequestStringParameter("TipoOrd"));

			// ???
			if (!isRequestParameterNullObj("noteRec")) {
				String noteRec = getRequestStringParameter("noteRec");
				lAnnMod.setNoteReclusione(noteRec);
			}

			// TODO: da gestire il reato
			if (!isRequestParameterNullObj("IdReato")) {
				lAnnMod.setReaIdReato(getRequestBigDecimalParameter("IdReato"));
			}

			// =========================================================
			// Recupero la Reclusione e la Multa
			// =========================================================
			String Multa = getRequestStringParameter("Multa");
			String Multa_dec = getRequestStringParameter("Mul_dec");

			lAnnMod.setNumAnniReclusione(getRequestBigDecimalParameter("ARec"));
			lAnnMod.setNumMesiReclusione(getRequestBigDecimalParameter("MRec"));
			lAnnMod.setNumGiorniReclusione(getRequestBigDecimalParameter("GRec"));

			if (!Multa.equals("")) {
				if (!Multa_dec.equals("")) {
					lAnnMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
				} else
					lAnnMod.setImportoMulta(new BigDecimal(Multa));
			} else if (!Multa_dec.equals(""))
				lAnnMod.setImportoMulta(new BigDecimal("0." + Multa_dec));

			// =========================================================
			// Recupero la Arresti e Ammenda
			// =========================================================

			String Ammenda = getRequestStringParameter("Ammenda");
			String Ammenda_dec = getRequestStringParameter("Amm_dec");

			lAnnMod.setNumAnniArresto(getRequestBigDecimalParameter("AArr"));
			lAnnMod.setNumMesiArresto(getRequestBigDecimalParameter("MArr"));
			lAnnMod.setNumGiorniArresto(getRequestBigDecimalParameter("GArr"));

			if (!Ammenda.equals("")) {
				if (!Ammenda_dec.equals(""))
					lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
				else
					lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
			} else if (!Ammenda_dec.equals(""))
				lAnnMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

			lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAnnMod.setDataInserimento(DateUtils.getSysDate());

			// Ricerca del Provvedimento attraverso l'ID TENORE_SIGE
			IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
			ProvvedimentoSigeModel lProvv = lCtrl
					.ExRicercaProvedimentoByIdTenore(getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE));
			// Si associa l'Annotazione all'Ordinanza attrverso l'Evento.
			lAnnMod.setEveIdEvento(lProvv.getIdEventoGenerato());
			lAnnMod.setAnnoGe(lProvv.getChiaveAnno());
			lAnnMod.setDataGE(lProvv.getDataEmissione());

			// ==========================================================================
			// Recupero le richieste da legare alla decisione
			// ==========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero le richieste da legare alla decisione");
			mListaRichieste = new Vector<>();
			int lNumTotRichieste = 0;
			if (!isRequestParameterNullObj("NumTotRichieste"))
				lNumTotRichieste = getRequestIntParameter("NumTotRichieste");
			else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("campo NumTotRichieste non trovato !!");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("numtot richieste = " + lNumTotRichieste);
			for (int i = 0; i < lNumTotRichieste; i++) {
				if (!isRequestParameterNullObj("cb_record_" + (i + 1))) {
					AnnotazioneManualeModel lAnnRichiesta = new AnnotazioneManualeModel();
					lAnnRichiesta
							.setIdAnnotazioneManuale(getRequestBigDecimalParameter("cb_record_" + (i + 1)));
					mListaRichieste.add(lAnnRichiesta);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("cb_record_" + (i + 1) + " = " + lAnnRichiesta.getIdAnnotazioneManuale());
				} else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("cb_record_" + (i + 1) + " is null");
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Annotazione Manuale da inserire : " + lAnnMod);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "letturaDatiAnnotazioneManuale : fine");
		return lAnnMod;
	}

	private void processTitoliEsecutivi() throws Exception {
		ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
		String codOggetto = super.getRequestStringParameter("codOggettoSige");
		BigDecimal idProvvedimento = super.getRequestBigDecimalParameter("idProvvedimento");
		Vector<TenoreSigeEstesoModel> lTenori = lCtrl
				.ExRicercaTenoriByCodOggettoSigeAndIdProvvedimento(codOggetto, idProvvedimento);
		AnnotazioneManualeModel lAnnotazioneManuale = letturaDatiAnnotazioneManuale();

		for (TenoreSigeEstesoModel lTenore : lTenori) {
			TenoreSigeModel tenore = lTenore.getTenoreSige();
			tenore.setCodEsitoSige(getRequestStringParameter(CAMPO_COD_ESITO_TENORE_SIGE));

			// Ulteriore descrizione della decisione
			tenore.setNote(getRequestStringParameter(CAMPO_NOTE));
			tenore.setData(DateUtils.getSysDate());
			tenore.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore
			tenore.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																				// dell'operatore
			tenore.setDataAggiornamento(DateUtils.getSysDate()); // data

			if (lAnnotazioneManuale != null) {
				lAnnotazioneManuale.setSenIdSentenza(lTenore.getSentenza().getIdSentenza());
				lAnnotazioneManuale.setTenIdTenoreSige(tenore.getIdTenoreSige());
			}
			// @emma 23072018 intervento post COLLAUDO 11.2 (passo null come ultimo parametro)
			lCtrl.ExInserisciEsitiOggetto(tenore, null, mDatiProv, lAnnotazioneManuale,
					new Vector<AnnotazioneManualeModel>(), null);
		}
	}

	private String buildLinkDettaglio() throws F3BException {
		// Prepara la "pagina" di destinazione
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.tenore.action.ActLoadDettaglioOggettoProv");
		lRedirigi.setParameter(CAMPO_ID_TENORE_SIGE, getRequestStringParameter(CAMPO_ID_TENORE_SIGE));
		lRedirigi.setParameter("idSenSentenza", super.getRequestStringParameter("idSenSentenza"));
		lRedirigi.setParameter(IWebConstants.FLAG_RITORNO, "20");
		lRedirigi.setParameter("idProvvedimento", super.getRequestStringParameter("idProvvedimento"));
		lRedirigi.setParameter("isTitoliEsecutivi", super.getRequestStringParameter("isTitoliEsecutivi"));

		if (!super.isRequestParameterNullObj("isTitoliEsecutivi")
				&& super.getRequestStringParameter("isTitoliEsecutivi").equals("true")) {
			lRedirigi.setParameter("isTitoliEsecutivi", "true");
		}

		return lRedirigi.toString();
	}

}