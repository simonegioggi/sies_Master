package siap.bdmc.notifichesies.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActInserisciNotificheSies
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di NotificheSies
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
public class ActInserisciNotificheSies extends ActionSiap implements ICostantiNotificheSies {

	/*****************************************************************************
	 * Azione di Inserimento del NotificheSies
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {
		NotificheSiesModel lNotMod = new NotificheSiesModel();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		lNotMod.setIdNotificheSies(getRequestBigDecimalParameter(CAMPO_ID_NOTIFICHE_SIES));
		lNotMod.setAnnoSiep(getRequestBigDecimalParameter(CAMPO_ANNO_SIEP));
		lNotMod.setProgSiep(getRequestBigDecimalParameter(CAMPO_PROG_SIEP));
		lNotMod.setUfficioSiep(getRequestStringParameter(CAMPO_UFFICIO_SIEP));
		lNotMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lNotMod.setUfficioFascBdmc(getRequestStringParameter(CAMPO_UFFICIO_FASC_BDMC));
		lNotMod.setNumeroFascBdmc(getRequestBigDecimalParameter(CAMPO_NUMERO_FASC_BDMC));
		lNotMod.setTipoNotifica(getRequestStringParameter(CAMPO_TIPO_NOTIFICA));
		lNotMod.setDataNotifica(getRequestDateParameter(CAMPO_ANNO_DATA_NOTIFICA, CAMPO_MESE_DATA_NOTIFICA,
				CAMPO_GIORNO_DATA_NOTIFICA));
		lNotMod.setStatoTrasmissione(getRequestStringParameter(CAMPO_STATO_TRASMISSIONE));
		lNotMod.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		lNotMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lNotMod.setProgPeriPres(getRequestBigDecimalParameter(CAMPO_PROG_PERI_PRES));
		lNotMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lNotMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lNotMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		INotificheSies lCtrl = BDMCLookupRemote.getNotificheSiesRemote();
		NotificheSiesModel lNotRetMod = new NotificheSiesModel();
		lNotRetMod = lCtrl.ExInserisciNotificheSies(lNotMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.notifichesies.action.ActLoadDettaglioNotificheSies";
		lPage += "&" + CAMPO_ID_NOTIFICHE_SIES + "=" + lNotRetMod.getIdNotificheSies().toString();

		return lPage;
	}

}