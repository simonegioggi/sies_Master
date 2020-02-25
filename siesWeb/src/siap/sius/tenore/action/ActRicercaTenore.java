package siap.sius.tenore.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sius.tenore.controller.ITenore;
//import f3b.util.F3BException;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaTenore
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Tenore
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
public class ActRicercaTenore extends ActionSiap implements ICostantiTenore {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		TenoreModel lTenMod = new TenoreModel();
		lTenMod.setIdTenore(getRequestBigDecimalParameter(CAMPO_ID_TENORE));
		lTenMod.setCodEsitoTenore(getRequestStringParameter(CAMPO_COD_ESITO_TENORE));
		lTenMod.setData(getRequestDateParameter(CAMPO_ANNO_DATA, CAMPO_MESE_DATA, CAMPO_GIORNO_DATA));
		lTenMod.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		lTenMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lTenMod.setCodOggettoTenore(getRequestStringParameter(CAMPO_COD_OGGETTO_TENORE));
		lTenMod.setProgrTenore(getRequestBigDecimalParameter(CAMPO_PROGR_TENORE));
		// 05/11/2003 REWORKFascicoloGPModel.
		// lTenMod.setFlagEsitoTenore( getRequestStringParameter( CAMPO_FLAG_ESITO_TENORE) );
		lTenMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lTenMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lTenMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lTenMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lTenMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lTenMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));
		lTenMod.setGenPridGeneraleProcedimento(
				getRequestBigDecimalParameter(CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO));
		lTenMod.setDepOpidDepositoOrdinanzaPc(
				getRequestBigDecimalParameter(CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC));
		lTenMod.setImpIdImpugnazione(getRequestBigDecimalParameter(CAMPO_IMP_ID_IMPUGNAZIONE));
		lTenMod.setDepDecIdDepositoDecreto(getRequestBigDecimalParameter(CAMPO_DEP_DEC_ID_DEPOSITO_DECRETO));

		ITenore lCtrl = SIUSLookupRemote.getTenoreRemote();
		Vector lVect = lCtrl.ExRicercaTenore(lTenMod);
		setRequestAttribute("tenore", lVect);

		return PG_RICERCATENORE;
	}

}