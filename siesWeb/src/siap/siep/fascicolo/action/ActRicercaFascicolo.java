package siap.siep.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiapMinor;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.action.ICostantiSiepJMS;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaFascicolo
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaFascicolo extends ActionSiapMinor implements ICostantiFascicoloSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	boolean checkValidati() throws F3BException {

		boolean ret = false;
		if (getUtenteConnesso().getUfficioUtente() != null
				&& getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
			String tipoUfficioUtente = getUtenteConnesso().getUfficioUtente().getCodTipoUfficio();

			String tipoUfficioRequest = "";
			if (!this.isRequestParameterNullObj(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO)) {
				tipoUfficioRequest = this.getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO);
			}

			// 26/06/2009 Introduzione filtro FLAG_VALIDATO per Ricerca Altre BDI (solo fascicoli validati)
			// ==========================================================================
			// Se l'utente che effettua la ricerca non è SIEP deve poter vedere solo
			// i fascicoli validati altrimenti c'è il rischio che vengano agganciati
			// procedimenti SIUS anche a fascicoli SIEP non ancora Validati
			// ==========================================================================

			if ("PM".equals(tipoUfficioUtente)) {
				if ("PGCAP".equals(tipoUfficioRequest) || "PMM".equals(tipoUfficioRequest)) {
					ret = true;
				}
			}

			if ("PGCAP".equals(tipoUfficioUtente)) {
				if ("PM".equals(tipoUfficioRequest) || "PMM".equals(tipoUfficioRequest)) {
					ret = true;
				}
			}

			if (ufficiMinori.contains(tipoUfficioUtente)) {
				if ("PM".equals(tipoUfficioRequest) || "PGCAP".equals(tipoUfficioRequest)) {
					ret = true;
				}
			}

			if (!"PM".equals(tipoUfficioUtente) && !"PGCAP".equals(tipoUfficioUtente)
					&& !ufficiMinori.contains(tipoUfficioUtente)) {
				ret = true;
			}

		}
		return ret;
	}

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUfficio = null;
		if (!this.isRequestParameterNullObj(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO)) {
			lUfficio = lCtrlUfficio.getUfficioByCodTipoUffDescrComune(this
					.getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO), this
					.getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO).toUpperCase());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ufficio----->" + lUfficio.getCodUfficio());
		}

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		// flag per individuare come costruire il dettaglio in base alla ricerca fatta
		String flagRicercaData = "N";

		// Istanzio il Model
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO))
			lFasMod.setSogIdSoggetto(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));

		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_ID_SENTENZA))
			lFasMod.setSenIdSentenza(getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ID_SENTENZA));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lFasMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			lFasMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lFasMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lFasMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		if (checkValidati()) {
			lFasMod.setFlagValidato("S");
		}

		/************************** modifica 24 marzo 04 ************************/

		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE)) {
			lFasMod.setDataIscrizione(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE,
					ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE,
					ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE));
		}
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_INIZIALE)) {
			lFasMod.setDataIscrizioneIniziale(getRequestDateParameter(
					ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE,
					ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE,
					ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE));

		}
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_FINALE)) {
			lFasMod.setDataIscrizioneFinale(getRequestDateParameter(
					ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE,
					ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE,
					ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE));
		}

		if (lFasMod.getDataIscrizione() != null || lFasMod.getDataIscrizioneIniziale() != null
				|| lFasMod.getDataIscrizioneFinale() != null) {
			flagRicercaData = "S";
		}

		// Aggiunto per mev a8-rr-003
		if (!isRequestParameterNullObj("tipoClasse")) {
			String[] lClassiFascicolo = this.getRequestStringParameters("tipoClasse");
			lFasMod.setClassiFascicolo(lClassiFascicolo);
		}

		/***********************************************************/
		if (!this.isRequestParameterNullObj(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO)) {
			lFasMod.setChiaveUfficio(lUfficio.getCodUfficio());
		} else {
			lFasMod.setChiaveUfficio(this.getCodUfficioUtenteConnesso());

			if (!isRequestParameterNullObj(CAMPO_CHIAVE_ACCORPATO)) {
				// Sono nella form Avanzata - Intervallo Procedimenti
				String ufficioAccorpato = getRequestStringParameter(CAMPO_CHIAVE_ACCORPATO);
				String[] parts = ufficioAccorpato.split("-");
				if (parts.length > 1 && parts[1] != null && !parts[1].equals("")) {
					lFasMod.setCodUfficioInserimento(parts[1]);
				} else {
					lFasMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				}
			}
		}

//		String tipoUfficioUtente = "";
//		if (getUtenteConnesso().getUfficioUtente() != null
//				&& getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
//			tipoUfficioUtente = getUtenteConnesso().getUfficioUtente().getCodTipoUfficio();
//		}

		// ICostantiSiepJMS.CAMPO_SEDE_UFFICIO
		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		// 24/06/2015
		// MEV 10 S3 - In una prima richiesta fatta dal referente: se l'ufficio
		// di appartenenza dell'utente connesso è PGCAP deve poter vedere i fascicoli
		// dei soggetti minorenni iscritti da PMM; in una seconda richiesta viceversa
		// il fascicolo non deve essere visibile.
		// Vector lVect = lCtrl.ExRicercaFascicoloOnViewPaged(lFasMod, Integer.parseInt(lPagina),
		// checkMinori(), tipoUfficioUtente );
		Vector lVect = lCtrl.ExRicercaFascicoloOnViewPaged(lFasMod, Integer.parseInt(lPagina), checkMinori(),
				null);

		String lReturnPage = "";

		if (lVect.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP
					+ "=" + ((FascicoloSiepModel) lVect.get(0)).getIdFascicoloSiep().toString()
					+ "&flagDettaglio=" + flagRicercaData;
		} else {
			setRequestAttribute("fascicoli", lVect);
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExgetCountFascicoli(lFasMod);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute("flagDettaglio", flagRicercaData);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("flagRicercaData---->" + flagRicercaData);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			String lAzione = "siap.siep.fascicolo.action.ActRicercaFascicolo";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

			lReturnPage = PG_RICERCAFASCICOLO_SIEP;
		}

		setLinkRitorno();

		return lReturnPage; // restituisce la jsp di VIEW
	}

}