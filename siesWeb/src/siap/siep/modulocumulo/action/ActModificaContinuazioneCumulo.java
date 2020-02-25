package siap.siep.modulocumulo.action;

/**
* <p>Title: ActModificaContinuazioneCumulo</p>
* <p>Description: Classe Action per la modifica di Continuazione</p>
* <p>		in ambito Cumulo (Pena_complessiva_Cumulo/Continuazione_Cumulo) </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.IContinuazioneCumulo;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActModificaContinuazioneCumulo extends ActionModuloCumulo
		implements ICostantiPenaComplessivaCumulo, ICostantiContinuazioneCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Modifica del Continuazione in ambito Cumulo
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */

	public String processRequest() throws Exception {

		// riempie il model
		ContinuazioneCumuloModel lConMod = new ContinuazioneCumuloModel();
		BigDecimal lIdContinuazione = getRequestBigDecimalParameter(CAMPO_ID_CONTINUAZIONE_CUM);

		lConMod.setIdContinuazioneCum(lIdContinuazione);

		lConMod.setCodTipoContinuazione(getRequestStringParameter(CAMPO_COD_TIPO_CONTINUAZIONE));
		lConMod.setTitIdTitoloCumulatoCont(getRequestBigDecimalParameter(
				ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT));

		if (!isRequestParameterNullObj(CAMPO_ANNO_SENTENZA)) {
			lConMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));
			lConMod.setNumSentenza(getRequestStringParameter(CAMPO_NUM_SENTENZA));

			lConMod.setDataSentenza(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA,
					CAMPO_MESE_DATA_SENTENZA, CAMPO_GIORNO_DATA_SENTENZA));

			lConMod.setCodTipoAutorita(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA));
			lConMod.setCodLuogoAutorita(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_AUTORITA)).getCodComune());

			// Controllo esistenza ufficio
			if (lConMod.getCodTipoAutorita() != null && !lConMod.getCodTipoAutorita().equals("")
					&& !lConMod.getCodTipoAutorita().equals("-") && lConMod.getCodLuogoAutorita() != null
					&& !lConMod.getCodLuogoAutorita().equals("")
					&& !lConMod.getCodLuogoAutorita().equals("-")) {
				getCodUfficioByCodTipoUfficioDescrComune(lConMod.getCodTipoAutorita(),
						getRequestStringParameter(CAMPO_COD_LUOGO_AUTORITA));
			}

			lConMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));
			lConMod.setNumRegePm(getRequestStringParameter(CAMPO_NUM_REGE_PM));

			if (!isRequestParameterNullObj(ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN)) {
				lConMod.setAnnoRegGen(
						getRequestBigDecimalParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN));
				lConMod.setNumeroRegGen(
						getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN));
				lConMod.setTipoRegGen(
						getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN));
			}
		} else {
			// Continuazione già associata a un titolo in isctruttoria. I campi del titolo sono disabilitati.
			// E' stata modificata solo il tipo di continuazione e/o il motivo modifica
		}

		String lFlagStat = getRequestStringParameter(CAMPO_FLAG_STATO_CON_CUM);
		if (lFlagStat.equals("E"))
			lConMod.setFlagStato("M");
		else
			lConMod.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO_CON_CUM));

		lConMod.setMotivoModifica(getRequestStringParameter(CAMPO_MOTIVO_INS_MOD));

		lConMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lConMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lConMod.setDataAggiornamento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lConMod = " + lConMod);
		// chiama il controller
		IContinuazioneCumulo lCtrl = SIEPLookupRemote.getContinuazioneCumuloRemote();
		/* ContinuazioneCumuloModel llConModRet = */lCtrl.ExModificaContinuazioneCum(lConMod);

		BigDecimal lIdPenaCompl = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA_CUM);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActLoadDettaglioPenaComplessivaCumulo&"
				+ CAMPO_ID_PENA_COMPLESSIVA_CUM + "=" + lIdPenaCompl.toString();

		return lPage;
	}

}