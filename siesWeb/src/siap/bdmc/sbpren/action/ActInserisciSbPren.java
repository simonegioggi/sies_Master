package siap.bdmc.sbpren.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbpren.controller.ISbPren;
import siap.bdmc.sbpren.model.SbPrenModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActInserisciSbPren
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di SbPren
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
public class ActInserisciSbPren extends ActionSiap implements ICostantiSbPren {

	/*****************************************************************************
	 * Azione di Inserimento del SbPren
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		SbPrenModel lSbPMod = new SbPrenModel();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		lSbPMod.setDataPren(getRequestDateParameter(CAMPO_ANNO_DATA_PREN, CAMPO_MESE_DATA_PREN,
				CAMPO_GIORNO_DATA_PREN));
		lSbPMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lSbPMod.setUtenSies(getRequestStringParameter(CAMPO_UTEN_SIES));
		lSbPMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lSbPMod.setDataAnnu(getRequestDateParameter(CAMPO_ANNO_DATA_ANNU, CAMPO_MESE_DATA_ANNU,
				CAMPO_GIORNO_DATA_ANNU));
		lSbPMod.setMotiAnnu(getRequestStringParameter(CAMPO_MOTI_ANNU));
		// lSbPMod.setFlagPren ( getRequestStringParameter ( CAMPO_FLAG_PREN) );
		// lSbPMod.setFlagSeleCapoImpu ( getRequestBigDecimalParameter ( CAMPO_FLAG_SELE_CAPO_IMPU) );
		// lSbPMod.setFlagSeleProcPena ( getRequestBigDecimalParameter ( CAMPO_FLAG_SELE_PROC_PENA) );
		// lSbPMod.setFlagSeleSent ( getRequestBigDecimalParameter ( CAMPO_FLAG_SELE_SENT) );
		// lSbPMod.setFlagPrenPres ( getRequestBigDecimalParameter ( CAMPO_FLAG_PREN_PRES) );
		lSbPMod.setCodiUffiSies(getRequestStringParameter(CAMPO_CODI_UFFI_SIES));
		// lSbPMod.setFlagSelePeriComp ( getRequestBigDecimalParameter ( CAMPO_FLAG_SELE_PERI_COMP) );
		lSbPMod.setNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_NUME_FASC_BDMC));
		lSbPMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lSbPMod.setCodiSedeInst(getRequestStringParameter(CAMPO_CODI_SEDE_INST));
		lSbPMod.setCognSogg(getRequestStringParameter(CAMPO_COGN_SOGG));
		lSbPMod.setNomeSogg(getRequestStringParameter(CAMPO_NOME_SOGG));
		lSbPMod.setFlagSess(getRequestStringParameter(CAMPO_FLAG_SESS));
		lSbPMod.setCodiStat(getRequestStringParameter(CAMPO_CODI_STAT));
		lSbPMod.setLuogNasc(getRequestStringParameter(CAMPO_LUOG_NASC));
		lSbPMod.setCodiIdenAfis(getRequestStringParameter(CAMPO_CODI_IDEN_AFIS));
		lSbPMod.setDataNasc(getRequestDateParameter(CAMPO_ANNO_DATA_NASC, CAMPO_MESE_DATA_NASC,
				CAMPO_GIORNO_DATA_NASC));
		// lSbPMod.setFlagSeleCircSogg ( getRequestBigDecimalParameter ( CAMPO_FLAG_SELE_CIRC_SOGG) );

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		ISbPren lCtrl = BDMCLookupRemote.getSbPrenRemote();
		SbPrenModel lSbPRetMod = new SbPrenModel();
		lSbPRetMod = lCtrl.ExInserisciSbPren(lSbPMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.sbpren.action.ActLoadDettaglioSbPren";
		lPage += "&" + CAMPO_ID_PREN + "=" + lSbPRetMod.getIdPren().toString();

		return lPage;
	}

}