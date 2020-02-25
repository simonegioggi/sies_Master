package siap.siep.modulocumulo.action;

/**
* <p>Title: ActInserisciUlterioriContinuazioniCumulo</p>
* <p>Description: Classe Action per l'inserimento di Continiazioni</p>
* <p>   in ambito Cumulo (Pena_complessiva_Cumulo/Continiazioni_Cumulo ) </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciUlterioriContinuazioniCumulo extends ActionModuloCumulo
		implements ICostantiPenaComplessivaCumulo, ICostantiContinuazioneCumulo {

	/**
	 * Azione di Inserimento del ContinuazioniCumulo
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		BigDecimal lIdPenaComp = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA_CUM);

		// CONTINUAZIONE CON ALTRI REATI
		List lList = new ArrayList();

		ContinuazioneCumuloModel lCont = new ContinuazioneCumuloModel();

		lCont.setPcIdPenaComplessivaCum(lIdPenaComp);

		lCont.setCodTipoContinuazione(
				getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE));
		lCont.setAnnoSentenza(
				getRequestBigDecimalParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA));
		lCont.setNumSentenza(getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA));

		lCont.setDataSentenza(getRequestDateParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA,
				ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA,
				ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA));

		// Autorità
		lCont.setCodTipoAutorita(
				getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA));
		String lDescLuogo = getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA);
		ComuneModel lComuneLuogo = getCodComuneByDescr(lDescLuogo);

		// Controllo esistenza Ufficio
		/* String lCodLuogo = */getCodUfficioByCodTipoUfficioDescrComune(lCont.getCodTipoAutorita(),
				lDescLuogo);

		lCont.setCodLuogoAutorita(lComuneLuogo.getCodComune());

		if (!isRequestParameterNullObj(ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM)) {
			lCont.setAnnoRegePm(
					getRequestBigDecimalParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM));
			lCont.setNumRegePm(getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM));
		}

		if (!isRequestParameterNullObj(ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN)) {
			lCont.setAnnoRegGen(
					getRequestBigDecimalParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN));
			lCont.setNumeroRegGen(
					getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN));
			lCont.setTipoRegGen(getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN));
		}

		lCont.setMotivoModifica(getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_MOTIVO_INS_MOD));

		lCont.setFlagStato("I");
		lCont.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

		// Se selezionato dalla lista
		if (!isRequestParameterNullObj(ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT))
			lCont.setTitIdTitoloCumulatoCont(getRequestBigDecimalParameter(
					ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT));

		lCont.setCodOperatoreInserimento(getCodUtenteConnesso());
		lCont.setDataInserimento(DateUtils.getSysDate());
		lCont.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lCont.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

		lCont.setCodOperatoreInserimento(getCodUtenteConnesso());
		lCont.setDataInserimento(DateUtils.getSysDate());
		lCont.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lList.add(lCont);

		// ==========================================================================
		//
		// ==========================================================================
		IPenaComplessivaCumulo lCtrl = SIEPLookupRemote.getPenaComplessivaCumuloRemote();

		lCtrl.ExInserisciUlterioriContinuazioniCumulo(lList);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActLoadDettaglioPenaComplessivaCumulo&"
				+ CAMPO_ID_PENA_COMPLESSIVA_CUM + "=" + lIdPenaComp.toString();

		return lPage;
	}

}