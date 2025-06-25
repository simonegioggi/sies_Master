package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActIscrizioneProcApplicazioneMisuraProvvisoria
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento Procedimento di
 * </p>
 * <p>
 * Applicazione Misura Sicurezza Provvisoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Intersistemi Italia s.p.a.
 * </p>
 *
 * @version 8.2
 */
public class ActIscrizioneProcApplicazioneMisuraProvvisoria extends ActionSiap implements
		ICostantiMisuraSicurezza, ICostantiSentenza, ICostantiSoggetto, ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// preparo il model della sentenza
		SentenzaModel lSenMod = null;
		lSenMod = getSentenza();
		String lMess = "";
		lMess = getControlloSentenzaEsistente(lSenMod);

		// preparo il model del soggetto
		SoggettoModel lSogMod = null;
		if (!isRequestParameterNullObj(CAMPO_COGNOME)) {
			lSogMod = getSoggetto();
		}

		// preparo il model del fascicolo
		FascicoloSiepModel lFascMod = null;
		lFascMod = getFascicolo();

		// preparo il model della Misura Sicurezza
		MisuraSicurezzaModel lMisMod = null;
		lMisMod = getMisura();

		// Misura Provvisoria o Fuori_Sentenza
		String TipoIscrizioneMisura = getRequestStringParameter(CAMPO_TIPO_ISCRIZIONE_MISURA);

		BigDecimal idOrd = null;
		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC)
				&& getRequestStringParameter(
						ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC) != null)
			idOrd = getRequestBigDecimalParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC);

		BigDecimal idFascSius = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)
				&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS) != null)
			idFascSius = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);

		/** Lancia dettaglio **/
		/* Se esiste un provvedimento doppio viene segnalato prima della Insert nel DB */

		String lAzione = "siap.siep.fascicolo.action.ActIscrizioneProcApplicazioneMisuraProvvisoria";
		String lPage = "";

		if (!lMess.equals("")) {
			if (isRequestParameterNullObj(CAMPO_CK_WARNING_MIS)
					|| getRequestStringParameter(CAMPO_CK_WARNING_MIS) == null) {
				// Se nella form di Warning hai selezionato "OK" NON entra più qui, perchè
				// CAMPO_CK_WARNING_MIS = "S"
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction(lAzione);

				setRequestAttribute(IWebConstants.MESSAGE_TEXT, lMess);
				setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, "" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return PG_WARNING_MIS_SIC_FUORI_SENT;
			}
		}

		// Giugno 2015 - Può Essere anche NUMERAZIONE MANUALE oppure
		// Solo il primo procedimento 2015 è manuale

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO)
				&& getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO) != null
				&& !getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO).equals("")) {
			lFascMod.setChiaveAnno(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));
		}

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR)
				&& getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR) != null
				&& !getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR).equals("")) {
			lFascMod.setChiaveProgr(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));
		}
		//
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(" --XX-- ActIscrizione " + TipoIscrizioneMisura + " - Fascicolo Model = " + lFascMod);
		//
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		BigDecimal IdFascicolo = lCtrl.ExInserisciProcedimentoMisuraProvvisoriaeoFuoriSenteneza(lSenMod,
				lSogMod, lMisMod, lFascMod, TipoIscrizioneMisura, idOrd, idFascSius);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
		lPage += "&" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + IdFascicolo;

		return lPage;
	} // Chiude process()

	/**
	 * getSentenza
	 *
	 * @return lSenMod
	 * @throws F3BException
	 */
	protected SentenzaModel getSentenza() throws Exception {

		SentenzaModel lSenMod = new SentenzaModel();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSenMod.setDescrUfficioInserimento(lUtenteMod.getUfficioUtente().getDescrTipoUfficio() + " di "
				+ lUtenteMod.getUfficioUtente().getDescrComune());

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_PM)) {
			lSenMod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));
			lSenMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));
		}

		if (!isRequestParameterNullObj("TipoRG")) {
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gip")) {
				lSenMod.setAnnoRegeGip(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeGip(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("dib")) {
				lSenMod.setAnnoRegeDib(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeDib(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cas")) {
				lSenMod.setAnnoRegeCas(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCas(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cap")) {
				lSenMod.setAnnoRegeCap(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCap(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("casap")) {
				lSenMod.setAnnoRegeCasap(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCasap(getRequestStringParameter("NRG"));
			}
			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gup")) {
				lSenMod.setAnnoRegeGup(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeGup(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("capsm")) {
				lSenMod.setAnnoRegeCapsm(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCapsm(getRequestStringParameter("NRG"));
			}
		}

		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO)) {
			String lCodSedeNotizia = getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO))
					.getCodComune();
			lSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else {
			lSenMod.setCodSedeNotiziaReato("-");
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_PROVVEDIMENTO))
			lSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
					CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));

		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));

		// lSenMod.setCodTipoProvvedimento( "01"); // NON si tratta di Sentenza
		if (getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF).equals("02")) {
			lSenMod.setCodTipoProvvedimento("02"); // Decreto
		} else {
			lSenMod.setCodTipoProvvedimento("03"); // Ordinanza
		}
		// -------------------------------------------------------------------------------------

		String lCodTipo = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
		String lCodSedeEmittente = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE))
				.getCodComune();
		lSenMod.setCodLuogoEmittente(lCodSedeEmittente);
		lSenMod.setCodTipoAutoritaEmittente(lCodTipo);

		if (!isRequestParameterNullObj(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE))
			lSenMod.setNumSezioneAutoritaEmittente(
					getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO))
			lSenMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO));
		else
			lSenMod.setCodTipoRito("-");

		// Per le join
		lSenMod.setCodBilanciamentoCircostanze("-");
		lSenMod.setCodTipoAutoritaProvvRif("-");
		lSenMod.setCodTipoProvvRif("-");
		lSenMod.setCodLuogoProvvRif("-");
		lSenMod.setCodTipoProvvedimentoRif("-");
		lSenMod.setCodTipoProvvedimentoAltro("-");
		lSenMod.setCodTipoDecisioneCassazione("-");

		lSenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lSenMod.setDataInserimento(DateUtils.getSysDate());
		lSenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		return lSenMod;
	} // CHIUDE getSentenza()

	/**
	 * getControlloSentenzaEsistente
	 *
	 * @param lSenMod
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	private String getControlloSentenzaEsistente(SentenzaModel lSenMod) throws Exception {

		SentenzaModel lRis = null;
		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();
		Vector lRisRic = null;
		String lEsiste = "";

		try {
			lRisRic = lSCtrl.ExRicercaSentenzaDuplicata(lSenMod);
		} catch (SIEPException e) {
		}

		if (lRisRic != null && lRisRic.size() > 0) {
			lRis = (SentenzaModel) lRisRic.get(0);
			String lDescrizioneProvv = "";
			if (lRis != null && lRis.getCodTipoProvvedimento() != null) {
				if (lRis.getCodTipoProvvedimento().equals("03"))
					lDescrizioneProvv = "L'ordinanza ";
				else if (lRis.getCodTipoProvvedimento().equals("02"))
					lDescrizioneProvv = "Il decreto ";
				else
					lDescrizioneProvv = "Il provvedimento ";

				lEsiste += lDescrizioneProvv + lRis.getAnnoSentenza() + "/" + lRis.getNumeroSentenza() + " - "
						+ lRis.getDescrTipoAutoritaEmittente() + " <br>di " + lRis.getDescrLuogoEmittente()
						+ " è già presente in archivio.";
			}

		}

		return lEsiste;
	} // CHIUDE getControlloSentenzaEsistente()

	/*
	 **
	 * getSoggetto
	 *
	 * @return lSogMod
	 *
	 * @throws F3BException
	 */
	protected SoggettoModel getSoggetto() throws Exception {

		SoggettoModel lSogMod = new SoggettoModel();

		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lSogMod.setSesso(getRequestStringParameter(CAMPO_SESSO));
		lSogMod.setAnnoNascita(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_NASCITA));
		lSogMod.setMeseNascita(getRequestBigDecimalParameter(CAMPO_MESE_DATA_NASCITA));
		lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA));
		lSogMod.setDataNascitaPresunta(getRequestStringParameter(CAMPO_DATA_NASCITA_PRESUNTA));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
			//lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
			lComMod = new ComuneModel(getDatiComuneByCodDescr(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
				// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
				//getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
				getDatiComuneByDescrOmonimia(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		}
		lSogMod.setCodComuneNascita(lComMod.getCodComune());
		lSogMod.setCodProvinciaNascita(lComMod.getCodProvincia());

		if (!isRequestParameterNullObj(CAMPO_NAZIONALITA)
				&& getRequestStringParameter(CAMPO_NAZIONALITA) != null
				&& getRequestStringParameter(CAMPO_NAZIONALITA).equals("E")) {
			lSogMod.setCodComuneCasellario("342");
		} else {
			lSogMod.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
		}

		lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO));
		lSogMod.setNazionalita(getRequestStringParameter(CAMPO_NAZIONALITA));
		lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));
		lSogMod.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase());
		lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA).toUpperCase());
		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS).toUpperCase());
		lSogMod.setNote(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOTE));
		lSogMod.setFlagPresenzaFascicolo("N");

		lSogMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lSogMod.setDataInserimento(DateUtils.getSysDate());
		lSogMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		return lSogMod;
	} // CHIUDE getSoggetto()

	/**
	 * getFascicolo
	 *
	 * @return lFasMod
	 * @throws F3BException
	 */
	protected FascicoloSiepModel getFascicolo() throws Exception {

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lFasMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
		// Il progressivo del fascicolo (in base all'anno e all'ufficio) viene calcolato applicativamente

		lFasMod.setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio()); // Ufficio dell'operatore che
																					// inserisce

		lFasMod.setCodStatoFascicolo("02"); // Stato fascicolo settato ad aperto
		lFasMod.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
		lFasMod.setCodTipoPosLibero("-"); // Motivo di archiviazione '-' per le join

		// lFasMod.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		lFasMod.setDataIscrizione(getRequestDateParameter(ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE,
				ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE,
				ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE));
		lFasMod.setDataArrivoAtto(getRequestDateParameter(ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO,
				ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO,
				ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO));
		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiSentenza.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_NOTE).equals("")) {
			lFasMod.setNote(getRequestStringParameter(ICostantiSentenza.CAMPO_NOTE));
		}

		lFasMod.setFlagValidato("N"); // Il flag di validazione viene impostato a 'NO'
		lFasMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della posizione
										// giuridica

		lFasMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		lFasMod.setDataInserimento(DateUtils.getSysDate());
		lFasMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// -------> settare SetIdSentenza
		// lFasMod.setSenIdSentenza(aIdSentenza);

		lFasMod.setTipoProgressivo(4);

		return lFasMod;
	} // CHIUDE getFascicolo()

	protected MisuraSicurezzaModel getMisura() throws Exception {

		MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel();

		lMisMod.setCodNatura(getRequestStringParameter(CAMPO_COD_NATURA));
		lMisMod.setCodTipo(getRequestStringParameter(CAMPO_COD_TIPO));
		lMisMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
		lMisMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
		lMisMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));

		if (!isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA)
				&& getRequestStringParameter(
						ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA) != null) {
			lMisMod.setLuogoEsecuzioneMisura(
					getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA));
		}

		lMisMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMisMod.setDataInserimento(DateUtils.getSysDate());
		lMisMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		return lMisMod;
	}

} // CHIUDE CLASSE ActIscrizioneProcApplicazioneMisuraProvvisoria()