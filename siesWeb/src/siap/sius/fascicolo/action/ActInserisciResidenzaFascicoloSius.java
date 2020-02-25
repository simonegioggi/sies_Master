package siap.sius.fascicolo.action;

/**
* <p>Title: ActInserisciResidenzaFascicoloSius</p>
* <p>Description: Classe Action per l'inserimento della Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.residenza.action.ICostantiResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciResidenzaFascicoloSius extends ActionSiap implements ICostantiResidenza {
	public String processRequest() throws Exception {
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		BigDecimal lIdSoggetto = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);
		BigDecimal lIdResidenza = getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA);

		ResidenzaModel lResMod = new ResidenzaModel();

		String lCodStato = getRequestStringParameter(CAMPO_COD_STATO);
		lResMod.setCodStato(lCodStato);

		// String lDescrComune = getRequestStringParameter( CAMPO_DESCR_COMUNE );

		// 22/08/2008 Recupero dati del Comune di Residenza con Controllo omonimia.
		// ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(
		// CAMPO_DESCR_COMUNE )) );
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 1) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(CAMPO_DESCR_COMUNE)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_DESCR_COMUNE)));
		}

		lResMod.setCodComune(lComMod.getCodComune());
		lResMod.setCodProvincia(lComMod.getCodProvincia());

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

		ResidenzaFascicoloSiusModel lResFasc = new ResidenzaFascicoloSiusModel();
		lResFasc.setDataInizioValidita(DateUtils.getSysDate());
		lResFasc.setFasSiuIdFascicoloSius(lIdFascicolo);
		lResFasc.setResIdResidenza(lIdResidenza);

		ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
		lResAss.setResidenza(lResMod);
		lResAss.setResidenzaFascicoloSius(lResFasc);

		// --- FASCICOLO ---
		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

		if (!isRequestParameterNullObj("modalita")
				&& this.getRequestStringParameter("modalita").compareToIgnoreCase("M") == 0)
			lResAss = lCtrl.ExModificaResidenzaFascicoloSius(lResAss);
		else
			lResAss = lCtrl.ExInserisciResidenzaFascicoloSius(lResAss);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActRicercaResidenzaByProcedimentoSius&"
				+ ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS + "=" + lIdFascicolo;
	}

}