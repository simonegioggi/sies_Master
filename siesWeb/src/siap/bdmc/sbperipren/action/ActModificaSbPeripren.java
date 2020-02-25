package siap.bdmc.sbperipren.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbperipren.controller.ISbPeripren;
import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActModificaSbPeripren
 * </p>
 * <p>
 * Description: Classe Action per la modifica di SbPeripren
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
public class ActModificaSbPeripren extends ActionSiap implements ICostantiSbPeripren {

	/*****************************************************************************
	 * Azione di Modifica del SbPeripren
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// =====================================================
		// Riempie il model con i campi recuperati dalla form
		// n.b. per i campi del model non valorizzati, i corrispondenti
		// campi della tabella verranno impostati a null
		// Il campo chiave è obbligatorio perchè utilizzato nelle clausola where
		// per individuare il record da aggiornare
		// =====================================================
		SbPeriprenModel lSbPMod = new SbPeriprenModel();

		lSbPMod.setDataInizPeri(getRequestDateParameter(CAMPO_ANNO_DATA_INIZ_PERI, CAMPO_MESE_DATA_INIZ_PERI,
				CAMPO_GIORNO_DATA_INIZ_PERI));
		lSbPMod.setDataFinePeri(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_PERI, CAMPO_MESE_DATA_FINE_PERI,
				CAMPO_GIORNO_DATA_FINE_PERI));
		lSbPMod.setProgPeriPres(getRequestBigDecimalParameter(CAMPO_PROG_PERI_PRES));
		lSbPMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lSbPMod.setCodiUffiSies(getRequestStringParameter(CAMPO_COD_UFFI_SIES));
		lSbPMod.setAnnoFascSiep(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_SIEP));
		lSbPMod.setNumeFascSiep(getRequestBigDecimalParameter(CAMPO_NUME_FASC_SIEP));
		lSbPMod.setCodiSedeInst(getRequestStringParameter(CAMPO_CODI_SEDE_INST));
		lSbPMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lSbPMod.setNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_NUME_FASC_BDMC));
		lSbPMod.setCodStatPrenPeri(getRequestStringParameter(CAMPO_COD_STAT_PREN_PERI));
		// lSbPMod.setCodTipoPeri ( getRequestStringParameter ( CAMPO_COD_TIPO_PERI) );
		lSbPMod.setDataPrenPeri(getRequestDateParameter(CAMPO_ANNO_DATA_PREN_PERI, CAMPO_MESE_DATA_PREN_PERI,
				CAMPO_GIORNO_DATA_PREN_PERI));

		// ===================================================
		// Recupera il controller ed effettua la modifica
		// ===================================================
		ISbPeripren lCtrl = BDMCLookupRemote.getSbPeriprenRemote();
		lCtrl.ExModificaSbPeripren(lSbPMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.sbperipren.action.ActLoadDettaglioSbPeripren";
		lPage += "&" + CAMPO_PROG_PERI_PRES + "=" + lSbPMod.getProgPeriPres().toString();

		return lPage;
	}

}