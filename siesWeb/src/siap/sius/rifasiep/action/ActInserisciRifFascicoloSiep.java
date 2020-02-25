package siap.sius.rifasiep.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciRifFascicoloSiep
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Rif. Fascicolo Siep
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciRifFascicoloSiep extends ActionSiap implements ICostantiRifFascicoloSiep {

	/**
	 * Azione di Inserimento del Rif. Fascicolo Siep
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		RiferimentoFascicoloSiepModel lRFSMod = new RiferimentoFascicoloSiepModel();

		// MOdifica del 30/07/2013
		// Recupero il parametro che mi consente di capire se trattasi di un inserimento
		// di un procedimento SIEP (PROC_SIEP) (Ricercato tra i Procedimenti SIEP)
		// oppure di un inserimento di titoli non presenti su SIEP (NO_SIEP)
		String codTipoInserimento = getRequestStringParameter(CAMPO_COD_TIPO_INSERIMENTO);

		if (!isRequestAttributeNullObj(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP))
			lRFSMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));

		lRFSMod.setFasSiuIdFascicoloSius(getRequestBigDecimalParameter(CAMPO_FAS_SIU_ID_FASCICOLO_SIUS));
		if (codTipoInserimento.equals("PROC_SIEP")) {
			lRFSMod.setAnnoFascicoloSiep(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP));
			lRFSMod.setProgrFascicoloSiep(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP));
			lRFSMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
			lRFSMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
					CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));
			lRFSMod.setAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
			lRFSMod.setNumeroProvvedimento(getRequestStringParameter(CAMPO_NUMERO_PROVVEDIMENTO));
			lRFSMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
			lRFSMod.setCodLuogoEmittente(getCodComuneByDescr(
					getRequestStringParameter(CAMPO_DESCR_LUOGO_EMITTENTE)).getCodComune());
			lRFSMod.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
					CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));
			lRFSMod.setCodUffFascicoloSiep(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP),
					getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP)
							.toUpperCase()));
			lRFSMod.setFlagMS("N");
		} else {
			lRFSMod.setAnnoFascicoloSiep(getRequestBigDecimalParameter(CAMPO_ANNO_MISURA_SICUREZZA));
			BigDecimal numMisSic = getRequestBigDecimalParameter(CAMPO_NUMERO_MISURA_SICUREZZA);
			if (numMisSic == null) {
				lRFSMod.setProgrFascicoloSiep(null);
			} else {
				lRFSMod.setProgrFascicoloSiep(numMisSic);
			}
			lRFSMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_MSIC));
			lRFSMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO_MSIC,
					CAMPO_MESE_DATA_PROVVEDIMENTO_MSIC, CAMPO_GIORNO_DATA_PROVVEDIMENTO_MSIC));
			lRFSMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE_MSIC));
			lRFSMod.setCodLuogoEmittente(getCodComuneByDescr(
					getRequestStringParameter(CAMPO_DESCR_LUOGO_EMITTENTE_MSIC)).getCodComune());
			lRFSMod.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA_MSIC,
					CAMPO_MESE_DATA_IRREVOCABILITA_MSIC, CAMPO_GIORNO_DATA_IRREVOCABILITA_MSIC));
			lRFSMod.setCodUffFascicoloSiep(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_MISURA_SICUREZZA),
					getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_MISURA_SICUREZZA)
							.toUpperCase()));

			// Recupero la Tipologia di Numerazione (Es. Mis. Sic. oppure Es. Pene Pec.)
			String codTipologiaNumerazione = getRequestStringParameter(CAMPO_COD_TIPO_NUMERAZIONE);
			if (codTipologiaNumerazione != null && codTipologiaNumerazione.equals("EMS")) {
				lRFSMod.setFlagMS("M");
			} else {
				lRFSMod.setFlagMS("P");
			}
			// lRFSMod.setFlagMS("S");
		}
		lRFSMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lRFSMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lRFSMod.setDataInserimento(DateUtils.getSysDate());
		lRFSMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		// Rilettura dell'ulteriore titolo esecutivo.
		// Solo se trattasi di inserimento di un procedimento SIEP
		if (codTipoInserimento.equals("PROC_SIEP")) {
			FascicoloSiepModel lFasMod = new FascicoloSiepModel();

			lFasMod.setChiaveUfficio(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP),
					getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP)
							.toUpperCase()));

			if (!isRequestParameterNullObj(CAMPO_ANNO_FASCICOLO_SIEP))
				lFasMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP));

			if (!isRequestParameterNullObj(CAMPO_PROGR_FASCICOLO_SIEP))
				lFasMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP));

			// Lettura del fascicolo SIEP.
			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasMod = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

			// Se il titolo viene re-individuato con la chiave anno/progr/ufficio inputata,
			// si reimpostano i dati del titolo esecutivo al posto di quelli inputati.
			if (lFasMod != null) {
				lRFSMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
				lRFSMod.setAnnoFascicoloSiep(lFasMod.getChiaveAnno());
				lRFSMod.setProgrFascicoloSiep(lFasMod.getChiaveProgr());
				lRFSMod.setCodUffFascicoloSiep(lFasMod.getChiaveUfficio());
				lRFSMod.setCodTipoProvvedimento(lFasMod.getSentenza().getCodTipoProvvedimento());
				lRFSMod.setDataProvvedimento(lFasMod.getSentenza().getDataProvvedimento());
				lRFSMod.setAnnoProvvedimento(lFasMod.getSentenza().getAnnoSentenza());
				lRFSMod.setNumeroProvvedimento(lFasMod.getSentenza().getNumeroSentenza());
				lRFSMod.setCodTipoAutoritaEmittente(lFasMod.getSentenza().getCodTipoAutoritaEmittente());
				lRFSMod.setCodLuogoEmittente(lFasMod.getSentenza().getCodLuogoEmittente());
				// modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
				// lRFSMod.setDataIrrevocabilita( lFasMod.getSentenza().getDataIrrevocabilita() );
				lRFSMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			}
		}

		// Inserimento RiferimentoFascicoloSiep.
		IRiferimentoFascicoloSiep lCtrlRFS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
		RiferimentoFascicoloSiepModel lRFSModRet = lCtrlRFS.ExInserisciRiferimentoFascicoloSiep(lRFSMod); // setta
																											// la
																											// risposta
																											// nella
																											// request

		setRequestAttribute("RiferimentoFascicoloSiep", lRFSModRet);
		// Prepara la pagina di destinazione.
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.rifasiep.action.ActLoadDettaglioRifFascicoloSiep&"
				+ CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP + "="
				+ lRFSModRet.getIdRiferimentoFascicoloSiep().toString();

		return lPage;
	}

}