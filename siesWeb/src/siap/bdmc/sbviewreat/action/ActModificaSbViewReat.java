package siap.bdmc.sbviewreat.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewreat.controller.ISbViewReat;
import siap.bdmc.sbviewreat.model.SbViewReatModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActModificaSbViewReat
 * </p>
 * <p>
 * Description: Classe Action per la modifica di SbViewReat
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
public class ActModificaSbViewReat extends ActionSiap implements ICostantiSbViewReat {

	/*****************************************************************************
	 * Azione di Modifica del SbViewReat
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
		SbViewReatModel lSbVMod = new SbViewReatModel();

		lSbVMod.setNumeProgCapoImpu(getRequestBigDecimalParameter(CAMPO_NUME_PROG_CAPO_IMPU));
		lSbVMod.setNumeProgReat(getRequestBigDecimalParameter(CAMPO_NUME_PROG_REAT));
		lSbVMod.setCodiFontGiur(getRequestStringParameter(CAMPO_CODI_FONT_GIUR));
		lSbVMod.setAnnoFontGiur(getRequestBigDecimalParameter(CAMPO_ANNO_FONT_GIUR));
		lSbVMod.setNumeFontGiur(getRequestBigDecimalParameter(CAMPO_NUME_FONT_GIUR));
		lSbVMod.setArtiFontGiur(getRequestBigDecimalParameter(CAMPO_ARTI_FONT_GIUR));
		lSbVMod.setCommiArtiFont(getRequestStringParameter(CAMPO_COMMI_ARTI_FONT));
		lSbVMod.setLettArtiFont(getRequestStringParameter(CAMPO_LETT_ARTI_FONT));
		lSbVMod.setNumeArtiFont(getRequestStringParameter(CAMPO_NUME_ARTI_FONT));
		lSbVMod.setArtiQualFont(getRequestStringParameter(CAMPO_ARTI_QUAL_FONT));
		lSbVMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lSbVMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lSbVMod.setNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_NUME_FASC_BDMC));
		lSbVMod.setCodiSedeInst(getRequestStringParameter(CAMPO_CODI_SEDE_INST));

		// ===================================================
		// Recupera il controller ed effettua la modifica
		// ===================================================
		ISbViewReat lCtrl = BDMCLookupRemote.getSbViewReatRemote();
		lCtrl.ExModificaSbViewReat(lSbVMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.sbviewreat.action.ActLoadDettaglioSbViewReat";
		lPage += "&" + CAMPO_ID_PREN + "=" + lSbVMod.getIdPren().toString();
		lPage += "&" + CAMPO_NUME_PROG_CAPO_IMPU + "=" + lSbVMod.getNumeProgCapoImpu().toString();
		lPage += "&" + CAMPO_NUME_PROG_REAT + "=" + lSbVMod.getNumeProgReat().toString();

		return lPage;
	}

}