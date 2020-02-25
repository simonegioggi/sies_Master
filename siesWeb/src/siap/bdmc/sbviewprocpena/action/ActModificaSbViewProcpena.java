package siap.bdmc.sbviewprocpena.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewprocpena.controller.ISbViewProcpena;
import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActModificaSbViewProcpena
 * </p>
 * <p>
 * Description: Classe Action per la modifica di SbViewProcpena
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
public class ActModificaSbViewProcpena extends ActionSiap implements ICostantiSbViewProcpena {

	/*****************************************************************************
	 * Azione di Modifica del SbViewProcpena
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
		SbViewProcpenaModel lSbVMod = new SbViewProcpenaModel();

		lSbVMod.setCodiUffiPmpm(getRequestStringParameter(CAMPO_CODI_UFFI_PMPM));
		lSbVMod.setAnnoRegiPmpm(getRequestBigDecimalParameter(CAMPO_ANNO_REGI_PMPM));
		lSbVMod.setNumeRegiPmpm(getRequestBigDecimalParameter(CAMPO_NUME_REGI_PMPM));
		lSbVMod.setCodiUffiGipp(getRequestStringParameter(CAMPO_CODI_UFFI_GIPP));
		lSbVMod.setAnnoRegiGipp(getRequestBigDecimalParameter(CAMPO_ANNO_REGI_GIPP));
		lSbVMod.setNumeRegiGipp(getRequestBigDecimalParameter(CAMPO_NUME_REGI_GIPP));
		lSbVMod.setCodiUffiDibb(getRequestStringParameter(CAMPO_CODI_UFFI_DIBB));
		lSbVMod.setAnnoRegiDibb(getRequestBigDecimalParameter(CAMPO_ANNO_REGI_DIBB));
		lSbVMod.setNumeRegiDibb(getRequestBigDecimalParameter(CAMPO_NUME_REGI_DIBB));
		lSbVMod.setCodiUffiCoap(getRequestStringParameter(CAMPO_CODI_UFFI_COAP));
		lSbVMod.setNumeRegiCoap(getRequestBigDecimalParameter(CAMPO_NUME_REGI_COAP));
		lSbVMod.setAnnoRegiCoap(getRequestBigDecimalParameter(CAMPO_ANNO_REGI_COAP));
		lSbVMod.setDataPassGiud(getRequestDateParameter(CAMPO_ANNO_DATA_PASS_GIUD, CAMPO_MESE_DATA_PASS_GIUD,
				CAMPO_GIORNO_DATA_PASS_GIUD));
		lSbVMod.setFlagReclArreGigu(getRequestStringParameter(CAMPO_FLAG_RECL_ARRE_GIGU));
		lSbVMod.setAnniPenaGigu(getRequestBigDecimalParameter(CAMPO_ANNI_PENA_GIGU));
		lSbVMod.setMesiPenaGigu(getRequestBigDecimalParameter(CAMPO_MESI_PENA_GIGU));
		lSbVMod.setGiorPenaGigu(getRequestBigDecimalParameter(CAMPO_GIOR_PENA_GIGU));
		lSbVMod.setDataSent1gra(getRequestDateParameter(CAMPO_ANNO_DATA_SENT_1GRA, CAMPO_MESE_DATA_SENT_1GRA,
				CAMPO_GIORNO_DATA_SENT_1GRA));
		lSbVMod.setAnnoSent1gra(getRequestBigDecimalParameter(CAMPO_ANNO_SENT_1GRA));
		lSbVMod.setNumeSent1gra(getRequestBigDecimalParameter(CAMPO_NUME_SENT_1GRA));
		lSbVMod.setDataSent2gra(getRequestDateParameter(CAMPO_ANNO_DATA_SENT_2GRA, CAMPO_MESE_DATA_SENT_2GRA,
				CAMPO_GIORNO_DATA_SENT_2GRA));
		lSbVMod.setAnnoSent2gra(getRequestBigDecimalParameter(CAMPO_ANNO_SENT_2GRA));
		lSbVMod.setNumeSent2gra(getRequestBigDecimalParameter(CAMPO_NUME_SENT_2GRA));
		lSbVMod.setDataSentGippGupp(getRequestDateParameter(CAMPO_ANNO_DATA_SENT_GIPP_GUPP,
				CAMPO_MESE_DATA_SENT_GIPP_GUPP, CAMPO_GIORNO_DATA_SENT_GIPP_GUPP));
		lSbVMod.setNumeSentGippGupp(getRequestBigDecimalParameter(CAMPO_NUME_SENT_GIPP_GUPP));
		lSbVMod.setAnnoSentGippGupp(getRequestBigDecimalParameter(CAMPO_ANNO_SENT_GIPP_GUPP));
		lSbVMod.setAnniPenaDiba(getRequestBigDecimalParameter(CAMPO_ANNI_PENA_DIBA));
		lSbVMod.setMesiPenaDiba(getRequestBigDecimalParameter(CAMPO_MESI_PENA_DIBA));
		lSbVMod.setGiorPenaDiba(getRequestBigDecimalParameter(CAMPO_GIOR_PENA_DIBA));
		lSbVMod.setAnniPenaAppe(getRequestBigDecimalParameter(CAMPO_ANNI_PENA_APPE));
		lSbVMod.setMesiPenaAppe(getRequestBigDecimalParameter(CAMPO_MESI_PENA_APPE));
		lSbVMod.setGiorPenaAppe(getRequestBigDecimalParameter(CAMPO_GIOR_PENA_APPE));
		lSbVMod.setFlagReclArreDiba(getRequestStringParameter(CAMPO_FLAG_RECL_ARRE_DIBA));
		lSbVMod.setFlagReclArreAppe(getRequestStringParameter(CAMPO_FLAG_RECL_ARRE_APPE));
		lSbVMod.setFlagArti0089(getRequestStringParameter(CAMPO_FLAG_ARTI_0089));
		lSbVMod.setFlagArti0090(getRequestStringParameter(CAMPO_FLAG_ARTI_0090));
		lSbVMod.setFlagArti0091(getRequestStringParameter(CAMPO_FLAG_ARTI_0091));
		lSbVMod.setFlagArti0092(getRequestStringParameter(CAMPO_FLAG_ARTI_0092));
		lSbVMod.setFlagArti0093(getRequestStringParameter(CAMPO_FLAG_ARTI_0093));
		lSbVMod.setFlagArti0094(getRequestStringParameter(CAMPO_FLAG_ARTI_0094));
		lSbVMod.setFlagArti0095(getRequestStringParameter(CAMPO_FLAG_ARTI_0095));
		lSbVMod.setFlagArti0096(getRequestStringParameter(CAMPO_FLAG_ARTI_0096));
		lSbVMod.setFlagArti0097(getRequestStringParameter(CAMPO_FLAG_ARTI_0097));
		lSbVMod.setFlagArti0098(getRequestStringParameter(CAMPO_FLAG_ARTI_0098));
		lSbVMod.setFlagArti0099(getRequestStringParameter(CAMPO_FLAG_ARTI_0099));
		lSbVMod.setFlagArti62(getRequestStringParameter(CAMPO_FLAG_ARTI_62));
		lSbVMod.setArti0062Comm(getRequestStringParameter(CAMPO_ARTI_0062_COMM));
		lSbVMod.setFlagArt62bi(getRequestStringParameter(CAMPO_FLAG_ART_62BI));
		lSbVMod.setCodiMisuCust(getRequestStringParameter(CAMPO_CODI_MISU_CUST));
		lSbVMod.setCodiIstiPena(getRequestStringParameter(CAMPO_CODI_ISTI_PENA));
		lSbVMod.setDescLuog(getRequestStringParameter(CAMPO_DESC_LUOG));
		lSbVMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lSbVMod.setCodiSedeInst(getRequestStringParameter(CAMPO_CODI_SEDE_INST));
		lSbVMod.setNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_NUME_FASC_BDMC));
		lSbVMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lSbVMod.setFlagInfoSele(getRequestStringParameter(CAMPO_FLAG_INFO_SELE));

		// ===================================================
		// Recupera il controller ed effettua la modifica
		// ===================================================
		ISbViewProcpena lCtrl = BDMCLookupRemote.getSbViewProcpenaRemote();
		lCtrl.ExModificaSbViewProcpena(lSbVMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.sbviewprocpena.action.ActLoadDettaglioSbViewProcpena";
		lPage += "&" + CAMPO_ID_PREN + "=" + lSbVMod.getIdPren().toString();

		return lPage;
	}

}