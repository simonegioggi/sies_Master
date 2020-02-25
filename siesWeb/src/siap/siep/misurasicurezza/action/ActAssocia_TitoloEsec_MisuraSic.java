package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.sius.rifasiep.action.ICostantiRifFascicoloSiep;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActAssocia_TitoloEsec_MisuraSic
 * </p>
 * <p>
 * Description: Classe Action per associare un Mis. Sic. ad un titoo esecutivo
 * </p>
 * <p>
 * di altro Procedimento, anche esterno
 * </p>
 * <p>
 * (ES. nel caso in cui la M.S. sia arrivata nel fascicolo da un CUMULO)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Intersistemi Italia spa
 * </p>
 * 
 * @version 8.2
 */
public class ActAssocia_TitoloEsec_MisuraSic extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiSentenza, ICostantiFascicoloSiep {

	public String processRequest() throws Exception {

		// Ricerca Titolo Esecutivo per associarlo alla M.S.
		RiferimentoFascicoloSiepModel lRifMod = new RiferimentoFascicoloSiepModel();
		IRiferimentoFascicoloSiep lCtrlS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();

		lRifMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lRifMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lRifMod.setDataInserimento(DateUtils.getSysDate());

		if (getRequestStringParameter("Modo").equals("CERCA")) {
			lRifMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));
			lRifMod.setFlagMS("N");
			lRifMod.setAnnoFascicoloSiep(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));
			lRifMod.setProgrFascicoloSiep(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));
		} else if (getRequestStringParameter("Modo").equals("INSERISCI")) {
			lRifMod.setFlagMS("M");
			lRifMod.setAnnoFascicoloSiep(getRequestBigDecimalParameter(ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP));
			lRifMod.setProgrFascicoloSiep(getRequestBigDecimalParameter(ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP));
		}

		String CodTipoUf = getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP);
		String lDescLuo = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);

		lRifMod.setCodUffFascicoloSiep(getCodUfficioByCodTipoUfficioDescrComune(CodTipoUf, lDescLuo));

		lRifMod.setCodTipoProvvedimento(getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF));
		lRifMod.setDataProvvedimento(getRequestDateParameter(ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO,
				ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO,
				ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO));
		lRifMod.setAnnoProvvedimento(getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_PROVV_RIF));
		lRifMod.setNumeroProvvedimento(getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF));

		String DescAuto = getRequestStringParameter(ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE);
		lRifMod.setCodLuogoEmittente(getCodComuneByDescr(DescAuto).getCodComune());
		lRifMod.setCodTipoAutoritaEmittente(getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		lRifMod.setDataIrrevocabilita(getRequestDateParameter(
				ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA,
				ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA,
				ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA));
		lRifMod.setNote(getRequestStringParameter(ICostantiSentenza.CAMPO_NOTE));

		BigDecimal lidMis = null;
		lidMis = getRequestBigDecimalParameter(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA);

		lidMis = lCtrlS.ExInserisciRiferimentoSiepUpdateMisuraSic(lRifMod, lidMis);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioMisuraSicurezza";
		lPage += "&" + "IdMisuraSicurezza" + "=" + lidMis;

		return lPage;
	} // Chiude processReq()

} // Chide Classe