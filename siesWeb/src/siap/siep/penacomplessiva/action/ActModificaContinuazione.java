package siap.siep.penacomplessiva.action;

/**
* <p>Title: ActModificaContinuazione</p>
* <p>Description: Classe Action per la modifica di Continuazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.continuazione.action.ICostantiContinuazione;
import siap.siep.continuazione.controller.IContinuazione;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.util.SIEPLookupRemote;

public class ActModificaContinuazione extends ActionSiap
		implements ICostantiPenaComplessiva, ICostantiContinuazione {

	/**
	 * Azione di Modifica del Continuazione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		BigDecimal lIdContinuazione = getRequestBigDecimalParameter(CAMPO_ID_CONTINUAZIONE);
		BigDecimal lIdPenaCompl = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA);

		// riempie il model
		ContinuazioneModel lConMod = new ContinuazioneModel();

		lConMod.setIdContinuazione(lIdContinuazione);

		lConMod.setCodTipoContinuazione(getRequestStringParameter(CAMPO_COD_TIPO_CONTINUAZIONE));
		lConMod.setCodTipoAutorita(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA));

		lConMod.setCodLuogoAutorita(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_AUTORITA)).getCodComune());

		// Controllo esistenza ufficio
		if (lConMod.getCodTipoAutorita() != null && !lConMod.getCodTipoAutorita().equals("")
				&& !lConMod.getCodTipoAutorita().equals("-") && lConMod.getCodLuogoAutorita() != null
				&& !lConMod.getCodLuogoAutorita().equals("") && !lConMod.getCodLuogoAutorita().equals("-")) {
			getCodUfficioByCodTipoUfficioDescrComune(lConMod.getCodTipoAutorita(),
					getRequestStringParameter(CAMPO_COD_LUOGO_AUTORITA));
		}

		lConMod.setDataSentenza(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA, CAMPO_MESE_DATA_SENTENZA,
				CAMPO_GIORNO_DATA_SENTENZA));

		lConMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));
		lConMod.setNumSentenza(getRequestStringParameter(CAMPO_NUM_SENTENZA));

		lConMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));
		lConMod.setNumRegePm(getRequestStringParameter(CAMPO_NUM_REGE_PM));

		// *********************************************************************************************
		// Federica - sostituzione degli enne campi relativi all'anno ed al numero registro generale
		// con una lista di valori
		// *********************************************************************************************

		String TipoRGGenerico = getRequestStringParameter("TipoRG");

		if (TipoRGGenerico.equalsIgnoreCase("gip")) {
			lConMod.setAnnoRegeGip(getRequestBigDecimalParameter("ARG"));
			lConMod.setNumRegeGip(getRequestStringParameter("NRG"));
		}
		if (TipoRGGenerico.equalsIgnoreCase("dib")) {
			lConMod.setAnnoRegeDib(getRequestBigDecimalParameter("ARG"));
			lConMod.setNumRegeDib(getRequestStringParameter("NRG"));
		}
		if (TipoRGGenerico.equalsIgnoreCase("cas")) {
			lConMod.setAnnoRegeCas(getRequestBigDecimalParameter("ARG"));
			lConMod.setNumRegeCas(getRequestStringParameter("NRG"));
		}
		if (TipoRGGenerico.equalsIgnoreCase("cap")) {
			lConMod.setAnnoRegeCap(getRequestBigDecimalParameter("ARG"));
			lConMod.setNumRegeCap(getRequestStringParameter("NRG"));
		}
		if (TipoRGGenerico.equalsIgnoreCase("casap")) {
			lConMod.setAnnoRegeCasap(getRequestBigDecimalParameter("ARG"));
			lConMod.setNumRegeCasap(getRequestStringParameter("NRG"));
		}
		// fine modifica - federica

		lConMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lConMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lConMod.setDataAggiornamento(DateUtils.getSysDate());

		// chiama il controller
		IContinuazione lCtrl = SIEPLookupRemote.getContinuazioneRemote();
		/* ContinuazioneModel llConModRet = */lCtrl.ExModificaContinuazione(lConMod);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&"
				+ CAMPO_ID_PENA_COMPLESSIVA + "=" + lIdPenaCompl.toString();

		return lPage;
	}

}