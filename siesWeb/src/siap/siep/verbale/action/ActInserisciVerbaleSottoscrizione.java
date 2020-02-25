package siap.siep.verbale.action;

/**
* <p>Title: ActInserisciVerbale</p>
* <p>Description: Classe Action per l'inserimento di Verbale</p>
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
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

public class ActInserisciVerbaleSottoscrizione extends ActionSiap implements ICostantiVerbale {
	/**
	 * Azione di Inserimento del Verbale
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		VerbaleModel lVerMod = new VerbaleModel();

		lVerMod.setCodTipoVerbale("03");

		lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));

		lVerMod.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));

		if (!this.isRequestParameterNullObj(ICostantiVerbale.CSS_ID_CSSA)) {
			BigDecimal lIdCssa = this.getRequestBigDecimalParameter(ICostantiVerbale.CSS_ID_CSSA);
			lVerMod.setCssIdCssa(lIdCssa);
		}

		if (!this.isRequestParameterNullObj(ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lVerMod.setIstDetIdIstitutoDetenzione(
					this.getRequestStringParameter(ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE));
		} else {
			lVerMod.setIstDetIdIstitutoDetenzione("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)
				&& !this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)) {
			ComuneModel lComMod = this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO));

			lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
			lVerMod.setCodTipoUfficioFirmatario(
					this.getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));
		} else {
			lVerMod.setCodTipoUfficioFirmatario("-");
			lVerMod.setCodLuogoUfficioFirmatario("-");
		}

		lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_NOTE)) {
			lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));
		}

		lVerMod.setDataInserimento(DateUtils.getSysDate());

		// BigDecimal lIdFascicolo = ((FascicoloSiepModel)
		// getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		// all'interno del controller aggiorna la posizione giuridica
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
		VerbaleModel llVerModRet = lCtrl.ExInserisciVerbaleSottoscrizione(lFascMod.getIdFascicoloSiep(),
				lVerMod);

		setRequestAttribute("verbale", llVerModRet);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.verbale.action.ActLoadDettaglioVerbaleSottoscrizione&" + CAMPO_ID_VERBALE + "="
				+ llVerModRet.getIdVerbale().toString();

		return lPage;
	}

}