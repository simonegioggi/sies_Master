package siap.siep.fascicolo.action;

/**
* <p>Title: ActInserisciResidenza</p>
* <p>Description: Classe Action per l'inserimento di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
//import siap.sico.decodifiche.controller.ComuneController;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.residenza.action.ICostantiResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciDomicilioFascicolo extends ActionSiap implements ICostantiResidenza {
	/**
	 * Azione di Inserimento della Residenza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();
		BigDecimal lIdSoggetto = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);
		BigDecimal lIdResidenza = getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA);

		ResidenzaModel lResMod = new ResidenzaModel();

		String lCodStato = getRequestStringParameter(CAMPO_COD_STATO);
		lResMod.setCodStato(lCodStato);

		/* 20210531	MEV_Scheda-21 Correzione Comune Domicilio per omonimie dei Comuni con flag validità.
		if (getRequestStringParameter(CAMPO_DESCR_COMUNE).length() > 1) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter(CAMPO_DESCR_COMUNE)));
			lResMod.setCodComune(lComMod.getCodComune());
			lResMod.setCodProvincia(lComMod.getCodProvincia());
		} else {
			lResMod.setCodProvincia("-");
			lResMod.setCodComune("-");
		} */
		// 20210531	MEV_Scheda-21 Correzione Comune Domicilio per omonimie dei Comuni con flag validità.
		// Recupero dati del Comune di residenza
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

		// 20250612 [SG]: risolto problema ricerca soggetto col "-" pari al cod comune nascita
		// Ticket#20250612016 - SIES - ricerche soggetto
		lResMod.setCodComune(lComMod.getCodComune());
		lResMod.setCodProvincia(lComMod.getCodProvincia());

		lResMod.setCap(getRequestStringParameter(CAMPO_CAP));
		lResMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));

		// 'Domicilio' (D)
		lResMod.setCodTipoResidenza("D");

		lResMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lResMod.setDataInserimento(DateUtils.getSysDate());
		lResMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lResMod.setDescComuneEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO));

		// NON é SULLA TABELLA RESIDENZA LA VALIDITA' lResMod.setDataInizioValidita( DateUtils.getSysDate() );
		lResMod.setSogIdSoggetto(lIdSoggetto);
		lResMod.setIdResidenza(lIdResidenza);

		ResidenzaFascicoloSiepModel lResFasc = new ResidenzaFascicoloSiepModel();
		lResFasc.setDataInizioValidita(DateUtils.getSysDate());
		lResFasc.setFasSieIdFascicoloSiep(lIdFascicolo);
		lResFasc.setResIdResidenza(lIdResidenza);

		ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
		lResAss.setResidenza(lResMod);
		lResAss.setResidenzaFascicoloSiep(lResFasc);

		// --- FASCICOLO ---
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lResAss = lCtrl.ExInserisciResidenzaFascicoloSiep(lResAss);

		// String lPage = "";

		return /* lPage = */IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
				+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
	}

}