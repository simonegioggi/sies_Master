package siap.siep.sentenzariunita.action;

/**
* <p>Title: ActInserisciSentenzaRiunita</p>
* <p>Description: Classe Action per l'inserimento di SentenzaRiunita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciSentenzaRiunita extends ActionSiap implements ICostantiSentenzaRiunita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del SentenzaRiunita
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));

		SentenzaRiunitaModel lSenMod = new SentenzaRiunitaModel();

		lSenMod.setDataSentenza(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA, CAMPO_MESE_DATA_SENTENZA,
				CAMPO_GIORNO_DATA_SENTENZA));
		lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));
		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lSenMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		lSenMod.setCodAutoritaEmittente("-"); // Per le Join

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lSenMod.setCodLuogoEmittente(lComMod.getCodComune());

		/* String lCodice = */getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));

		lSenMod.setSezioneAutoritaEmittente(getRequestStringParameter(CAMPO_SEZIONE_AUTORITA_EMITTENTE));
		lSenMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));
		lSenMod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));
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

		// inizio modifica Marzo 2010
		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO)) {
			String lCodSedeNotizia = getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO))
					.getCodComune();
			lSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else
			lSenMod.setCodSedeNotiziaReato("-");

		SentenzaRiunitaFascSiepModel lSenFascMod = new SentenzaRiunitaFascSiepModel();

		// fine modifica

		lSenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lSenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lSenMod.setDataInserimento(DateUtils.getSysDate());

		// Id della Sentenza
		lSenMod.setSenIdSentenza(lSen.getIdSentenza());

		ISentenzaRiunita lCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();
		SentenzaRiunitaModel lSenRet;
		if (!isSessionAttributeNullObj("fascicolo")) {
			FascicoloSiepModel lFasc = new FascicoloSiepModel(
					(FascicoloSiepModel) getSessionAttribute("fascicolo"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("lFasc.getSenIdSentenza()= " + lFasc.getSenIdSentenza());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("lSen.getIdSentenza()= " + lSen.getIdSentenza());

			if (lFasc.getSenIdSentenza().compareTo(lSen.getIdSentenza()) == 0) {
				lSenFascMod.setFasSieIdFascicoloSiep(lFasc.getIdFascicoloSiep());
				lSenRet = lCtrl.ExInserisciSentenzaRiunitaFascicoloSiep(lSenMod, lSenFascMod); // setta la
																								// risposta
																								// nella
																								// request
			} else {
				lSenRet = lCtrl.ExInserisciSentenzaRiunita(lSenMod); // setta la risposta nella request
			}
		} else {
			lSenRet = lCtrl.ExInserisciSentenzaRiunita(lSenMod); // setta la risposta nella request
		}

		setRequestAttribute("sentenzariunita", lSenRet);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sentenzariunita.action.ActLoadDettaglioSentenzaRiunita&"
				+ CAMPO_ID_SENTENZA_RIUNITA + "=" + lSenRet.getIdSentenzaRiunita().toString();

		return lPage;
	}

}