package siap.siep.sentenzariunita.action;

/**
* <p>Title: ActModificaSentenzaRiunita</p>
* <p>Description: Classe Action per la modifica di SentenzaRiunita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActModificaSentenzaRiunita extends ActionSiap implements ICostantiSentenzaRiunita {
	/**
	 * Azione di Modifica del SentenzaRiunita
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {
		String lId = getRequestStringParameter(CAMPO_ID_SENTENZA_RIUNITA);
		// riempie il model
		SentenzaRiunitaModel lSenMod = new SentenzaRiunitaModel();

		lSenMod.setIdSentenzaRiunita(new BigDecimal(lId));
		lSenMod.setDataSentenza(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA, CAMPO_MESE_DATA_SENTENZA,
				CAMPO_GIORNO_DATA_SENTENZA));
		lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));
		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lSenMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		// inizio modifica Marzo 2010 commento le due righe che seguono e sostituisco
		// //lSenMod.setCodAutoritaEmittente( getRequestStringParameter( CAMPO_COD_AUTORITA_EMITTENTE) );
		// lSenMod.setCodAutoritaEmittente( "-" );
		if (!isRequestParameterNullObj(CAMPO_COD_AUTORITA_EMITTENTE)) {
			String lCodAutorita = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_AUTORITA_EMITTENTE))
					.getCodComune();
			lSenMod.setCodAutoritaEmittente(lCodAutorita);
		} else
			lSenMod.setCodAutoritaEmittente("-");

		// fine modifica Marzo 2010

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
		// }

		lSenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lSenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lSenMod.setDataAggiornamento(DateUtils.getSysDate());

		// inizio modifica Marzo 2010
		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO)) {
			String lCodSedeNotizia = getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO))
					.getCodComune();
			lSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else
			lSenMod.setCodSedeNotiziaReato("-");
		// fine modifica Marzo 2010

		// chiama il controller
		ISentenzaRiunita lCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();
		SentenzaRiunitaModel lSenRet = lCtrl.ExModificaSentenzaRiunita(lSenMod);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("sentenzariunita", lSenRet);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sentenzariunita.action.ActLoadDettaglioSentenzaRiunita&"
				+ CAMPO_ID_SENTENZA_RIUNITA + "=" + lSenRet.getIdSentenzaRiunita().toString();
		return lPage;
	}

}