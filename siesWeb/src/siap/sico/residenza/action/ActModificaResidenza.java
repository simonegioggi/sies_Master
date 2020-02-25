package siap.sico.residenza.action;

/**
* <p>Title: ActModificaResidenza</p>
* <p>Description: Classe Action per la modifica di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.lock.model.LockModel;
import siap.sico.residenza.controller.ResidenzaController;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaResidenza extends ActionSiap implements ICostantiResidenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Modifica del Residenza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("<------------ ActModificaResidenza ------------>");

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("residenza", getRequestStringParameter(CAMPO_ID_RESIDENZA),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}
		// riempie il model
		ResidenzaModel lResMod = new ResidenzaModel();

		lResMod.setIdResidenza(getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA));
		lResMod.setSogIdSoggetto(this.getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));
		lResMod.setCodTipoResidenza(
				this.getRequestStringParameter(ICostantiResidenza.CAMPO_COD_TIPO_RESIDENZA));
		lResMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));
		lResMod.setCap(getRequestStringParameter(CAMPO_CAP));

		// Recupero dati del Luogo
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(CAMPO_DESCR_COMUNE)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_DESCR_COMUNE)));
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("CAMPO_DESC_COMUNE_ESTERO");
		if (!this.isRequestParameterNullObj(CAMPO_DESC_COMUNE_ESTERO)) {
			lResMod.setDescComuneEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lResMod.getDescComuneEstero() = " + lResMod.getDescComuneEstero());
		}

		lResMod.setCodComune(lComMod.getCodComune());
		lResMod.setCodProvincia(lComMod.getCodProvincia());

		lResMod.setCodStato(getRequestStringParameter(CAMPO_COD_STATO));

		lResMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lResMod.setDataAggiornamento(DateUtils.getSysDate());
		lResMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		// chiama il controller
		ResidenzaController lCtrl = new ResidenzaController();
		lResMod = lCtrl.ExModificaResidenza(lResMod);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sico.residenza.action.ActLoadDettaglioResidenza&" + CAMPO_ID_RESIDENZA + "="
				+ lResMod.getIdResidenza().toString();
	}

}