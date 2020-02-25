package siap.bdmc.sbviewnotifiche.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewnotifiche.controller.ISbViewNotifiche;
import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActInserisciSbViewNotifiche
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di SbViewNotifiche
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
public class ActInserisciSbViewNotifiche extends ActionSiap implements ICostantiSbViewNotifiche {

	/*****************************************************************************
	 * Azione di Inserimento del SbViewNotifiche
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		SbViewNotificheModel lSbVMod = new SbViewNotificheModel();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		lSbVMod.setProgNoti(getRequestBigDecimalParameter(CAMPO_PROG_NOTI));
		lSbVMod.setCodiNoti(getRequestStringParameter(CAMPO_CODI_NOTI));
		lSbVMod.setDescrizione(getRequestStringParameter(CAMPO_DESCRIZIONE));
		lSbVMod.setDataInviNoti(getRequestDateParameter(CAMPO_ANNO_DATA_INVI_NOTI, CAMPO_MESE_DATA_INVI_NOTI,
				CAMPO_GIORNO_DATA_INVI_NOTI));
		lSbVMod.setDataRegiNoti(getRequestDateParameter(CAMPO_ANNO_DATA_REGI_NOTI, CAMPO_MESE_DATA_REGI_NOTI,
				CAMPO_GIORNO_DATA_REGI_NOTI));
		lSbVMod.setDataValiNoti(getRequestDateParameter(CAMPO_ANNO_DATA_VALI_NOTI, CAMPO_MESE_DATA_VALI_NOTI,
				CAMPO_GIORNO_DATA_VALI_NOTI));
		lSbVMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lSbVMod.setCodiUffiSies(getRequestStringParameter(CAMPO_CODI_UFFI_SIES));
		lSbVMod.setCodiUffi(getRequestStringParameter(CAMPO_CODI_UFFI));
		lSbVMod.setFlagStatNoti(getRequestStringParameter(CAMPO_FLAG_STAT_NOTI));
		lSbVMod.setDataChiuNoti(getRequestDateParameter(CAMPO_ANNO_DATA_CHIU_NOTI, CAMPO_MESE_DATA_CHIU_NOTI,
				CAMPO_GIORNO_DATA_CHIU_NOTI));
		// lSbVMod.setFlagTras ( getRequestBigDecimalParameter ( CAMPO_FLAG_TRAS) );
		lSbVMod.setStopAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_STOP_ANNO_FASC_BDMC));
		lSbVMod.setStopNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_STOP_NUME_FASC_BDMC));
		lSbVMod.setUtenSies(getRequestStringParameter(CAMPO_UTEN_SIES));
		lSbVMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lSbVMod.setProgPeri(getRequestBigDecimalParameter(CAMPO_PROG_PERI));
		lSbVMod.setModiAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_MODI_ANNO_FASC_BDMC));
		lSbVMod.setModiNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_MODI_NUME_FASC_BDMC));
		lSbVMod.setFlagModi(getRequestStringParameter(CAMPO_FLAG_MODI));
		lSbVMod.setDataIniz(getRequestDateParameter(CAMPO_ANNO_DATA_INIZ, CAMPO_MESE_DATA_INIZ,
				CAMPO_GIORNO_DATA_INIZ));
		lSbVMod.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
				CAMPO_GIORNO_DATA_FINE));
		lSbVMod.setDataInizPrec(getRequestDateParameter(CAMPO_ANNO_DATA_INIZ_PREC, CAMPO_MESE_DATA_INIZ_PREC,
				CAMPO_GIORNO_DATA_INIZ_PREC));
		lSbVMod.setDataFinePrec(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_PREC, CAMPO_MESE_DATA_FINE_PREC,
				CAMPO_GIORNO_DATA_FINE_PREC));

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		ISbViewNotifiche lCtrl = BDMCLookupRemote.getSbViewNotificheRemote();
		SbViewNotificheModel lSbVRetMod = new SbViewNotificheModel();
		lSbVRetMod = lCtrl.ExInserisciSbViewNotifiche(lSbVMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.sbviewnotifiche.action.ActLoadDettaglioSbViewNotifiche";
		lPage += "&" + CAMPO_PROG_NOTI + "=" + lSbVRetMod.getProgNoti().toString();

		return lPage;
	}

}