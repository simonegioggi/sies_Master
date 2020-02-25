package siap.siep.notifica.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciRinnovazioneNotifica
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Scadenzario
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
public class ActInserisciRinnovazioneNotifica extends ActionSiap implements ICostantiNotifica {

	public String processRequest() throws F3BException {

		// BigDecimal lIdEve = this.getRequestBigDecimalParameter("idevento");
		BigDecimal lIdNot = this.getRequestBigDecimalParameter("idnotifica");

		RinnovoModel lRinMod = new RinnovoModel();
		RinnovoModel lRinModel = new RinnovoModel();
		ComuneModel lComMod = null;

		// RINNOVO
		if (this.getRequestStringParameter("Notifica").equals("FP")) // FORZE DI POLIZIA
		{
			lRinMod.setCodTipoRinnovo("P");
		} else if (this.getRequestStringParameter("Notifica").equals("UG")) // UFFICIALI GIUDIZIARI
		{
			lRinMod.setCodTipoRinnovo("U");
		}
		lRinMod.setCodTipoAutoritaRinnovo(
				this.getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO));
		lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO)));
		lRinMod.setCodLuogoRinnovo(lComMod.getCodComune());
		lRinMod.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO,
				ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO, ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO));
		// indirizzo va dentro note
		lRinMod.setNote(this.getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE));
		lRinMod.setNuovoLuogoNotifica(
				this.getRequestStringParameter(ICostantiRinnovo.CAMPO_LUOGO_NUOVA_NOTIFICA));
		lRinMod.setNotIdNotifica(lIdNot);
		lRinMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lRinMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lRinMod.setDataInserimento(DateUtils.getSysDate());

		// INSERIMENTO RINNOVO
		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		lRinModel = lCtrl.ExInserisciRinnovo(lRinMod);

		this.setRequestAttribute("Notifica", getRequestStringParameter("Notifica"));

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.notifica.action.ActLoadDettaglioRinnovazioneNotifica&"
				+ ICostantiRinnovo.CAMPO_ID_RINNOVO + "=" + lRinModel.getIdRinnovo().toString();
		return lPage;
	}

}