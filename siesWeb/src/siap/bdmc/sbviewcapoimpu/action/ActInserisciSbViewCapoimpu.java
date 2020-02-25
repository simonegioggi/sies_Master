package siap.bdmc.sbviewcapoimpu.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewcapoimpu.controller.ISbViewCapoimpu;
import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActInserisciSbViewCapoimpu
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di SbViewCapoimpu
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
public class ActInserisciSbViewCapoimpu extends ActionSiap implements ICostantiSbViewCapoimpu {

	/*****************************************************************************
	 * Azione di Inserimento del SbViewCapoimpu
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		SbViewCapoimpuModel lSbVMod = new SbViewCapoimpuModel();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		lSbVMod.setFlagArti0056(getRequestStringParameter(CAMPO_FLAG_ARTI_0056));
		lSbVMod.setFlagArti0061(getRequestStringParameter(CAMPO_FLAG_ARTI_0061));
		lSbVMod.setArti0061Comm(getRequestStringParameter(CAMPO_ARTI_0061_COMM));
		lSbVMod.setFlagArti0081(getRequestStringParameter(CAMPO_FLAG_ARTI_0081));
		lSbVMod.setArti0081Comm(getRequestStringParameter(CAMPO_ARTI_0081_COMM));
		lSbVMod.setFlagArt0110(getRequestStringParameter(CAMPO_FLAG_ART_0110));
		lSbVMod.setFlagArti0112(getRequestStringParameter(CAMPO_FLAG_ARTI_0112));
		lSbVMod.setArti0112Commi(getRequestStringParameter(CAMPO_ARTI_0112_COMMI));
		lSbVMod.setFlagArti0113(getRequestStringParameter(CAMPO_FLAG_ARTI_0113));
		lSbVMod.setFlagArti0114(getRequestStringParameter(CAMPO_FLAG_ARTI_0114));
		lSbVMod.setFlagArti0116(getRequestStringParameter(CAMPO_FLAG_ARTI_0116));
		lSbVMod.setFlagArti0117(getRequestStringParameter(CAMPO_FLAG_ARTI_0117));
		lSbVMod.setLuogReat(getRequestStringParameter(CAMPO_LUOG_REAT));
		lSbVMod.setFlagPeriTemp(getRequestStringParameter(CAMPO_FLAG_PERI_TEMP));
		lSbVMod.setDataReat0101(getRequestDateParameter(CAMPO_ANNO_DATA_REAT_0101, CAMPO_MESE_DATA_REAT_0101,
				CAMPO_GIORNO_DATA_REAT_0101));
		lSbVMod.setDataReat0202(getRequestDateParameter(CAMPO_ANNO_DATA_REAT_0202, CAMPO_MESE_DATA_REAT_0202,
				CAMPO_GIORNO_DATA_REAT_0202));
		lSbVMod.setDescPeriTemp(getRequestStringParameter(CAMPO_DESC_PERI_TEMP));
		lSbVMod.setNumeProgCapoImpu(getRequestBigDecimalParameter(CAMPO_NUME_PROG_CAPO_IMPU));
		lSbVMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lSbVMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lSbVMod.setNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_NUME_FASC_BDMC));
		lSbVMod.setCodiSedeInst(getRequestStringParameter(CAMPO_CODI_SEDE_INST));

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		ISbViewCapoimpu lCtrl = BDMCLookupRemote.getSbViewCapoimpuRemote();
		SbViewCapoimpuModel lSbVRetMod = new SbViewCapoimpuModel();
		lSbVRetMod = lCtrl.ExInserisciSbViewCapoimpu(lSbVMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.sbviewcapoimpu.action.ActLoadDettaglioSbViewCapoimpu";
		lPage += "&" + CAMPO_ID_PREN + "=" + lSbVRetMod.getIdPren().toString();
		lPage += "&" + CAMPO_NUME_PROG_CAPO_IMPU + "=" + lSbVRetMod.getNumeProgCapoImpu().toString();

		return lPage;
	}

}