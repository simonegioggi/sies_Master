package siap.siep.notifica.action;

import java.math.BigDecimal;
import java.util.Vector;

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
 * Title: ActInserisciRich8Bis
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciRich8Bis extends ActionSiap implements ICostantiNotifica {

	public String processRequest() throws F3BException {

		BigDecimal lIdEve = this.getRequestBigDecimalParameter("evento");

		// RinnovoModel lRinMod = new RinnovoModel();
		ComuneModel lComMod = null;
		Vector Rinnovi = new Vector();
		// RinnovoModel lRinnovo = null;

		// RINNOVO
		if (!this.isRequestParameterNullObj("primoAvvocato")
				&& this.getRequestStringParameter("primoAvvocato").equals("S")) {
			RinnovoModel lRinModel = new RinnovoModel();
			lRinModel.setCodTipoRinnovo("D");

			lRinModel.setCodTipoAutoritaRinnovo(
					getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO));

			lRinModel.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN,
					ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN,
					ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN));
			lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO)));
			lRinModel.setCodLuogoRinnovo(lComMod.getCodComune());
			lRinModel.setNuovoLuogoNotifica("-");
			lRinModel.setNote(getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE));
			lRinModel.setNotIdNotifica(this.getRequestBigDecimalParameter("idPrimaNotifica"));
			lRinModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRinModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lRinModel.setDataInserimento(DateUtils.getSysDate());

			Rinnovi.add(lRinModel);

		}

		// RINNOVO NOTIFICA' secondo Avvocato
		if (!this.isRequestParameterNullObj("secondoAvvocato")
				&& this.getRequestStringParameter("secondoAvvocato").equals("S")) {
			RinnovoModel lRinModelSecondo = new RinnovoModel();
			lRinModelSecondo.setCodTipoRinnovo("D");
			lRinModelSecondo.setCodTipoAutoritaRinnovo(
					getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_A));
			lRinModelSecondo.setDataRinnovo(getRequestDateParameter(
					ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA, ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA,
					ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA));
			lComMod = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_A)));
			lRinModelSecondo.setCodLuogoRinnovo(lComMod.getCodComune());
			lRinModelSecondo.setNuovoLuogoNotifica("-");
			lRinModelSecondo.setNote(getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE_A));
			lRinModelSecondo.setNotIdNotifica(this.getRequestBigDecimalParameter("idSecondaNotifica"));
			lRinModelSecondo.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRinModelSecondo.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lRinModelSecondo.setDataInserimento(DateUtils.getSysDate());

			Rinnovi.add(lRinModelSecondo);
		}

		if (this.getRequestStringParameter("altri").equals("S")) // RINNOVO NOTIFICA'
		{
			RinnovoModel lRinModelAltri = new RinnovoModel();
			lRinModelAltri.setCodTipoRinnovo("I");
			lRinModelAltri.setCodTipoAutoritaRinnovo(
					getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_AL));
			lRinModelAltri.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR,
					ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR,
					ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR));
			lComMod = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_AL)));
			lRinModelAltri.setCodLuogoRinnovo(lComMod.getCodComune());
			lRinModelAltri.setNuovoLuogoNotifica("-");
			lRinModelAltri.setNote(getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE_AL));
			lRinModelAltri.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRinModelAltri.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lRinModelAltri.setNotIdNotifica(this.getRequestBigDecimalParameter("idAltraNotifica"));
			lRinModelAltri.setDataInserimento(DateUtils.getSysDate());

			Rinnovi.add(lRinModelAltri);

		}

		// INSERIMENTO RINNOVO
		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		Vector idRinnovi = lCtrl.ExInserisciRinnovo(Rinnovi);

		this.setRequestAttribute("evento", lIdEve);
		this.setRequestAttribute("idRinnovi", idRinnovi);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.notifica.action.ActLoadDettaglioRich8Bis";
		return lPage;
	}

}