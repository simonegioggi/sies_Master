package siap.siep.fascicolo.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import siap.sico.decodifiche.controller.ComuneController;
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

/**
 * <p>
 * Title: ActInserisciResidenza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Residenza
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
public class ActInserisciResidenzaFascicolo extends ActionSiap implements ICostantiResidenza {
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

		// String lDescrComune = getRequestStringParameter(CAMPO_DESCR_COMUNE);

		if (getRequestStringParameter(CAMPO_DESCR_COMUNE).length() > 1) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter(CAMPO_DESCR_COMUNE)));
			lResMod.setCodComune(lComMod.getCodComune());
			lResMod.setCodProvincia(lComMod.getCodProvincia());
		} else {
			lResMod.setCodProvincia("-");
			lResMod.setCodComune("-");
		}

		lResMod.setCap(getRequestStringParameter(CAMPO_CAP));
		lResMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));

		// 'Residenza' (R)
		lResMod.setCodTipoResidenza("R");

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