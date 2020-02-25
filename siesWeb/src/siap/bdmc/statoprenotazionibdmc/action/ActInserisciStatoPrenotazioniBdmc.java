package siap.bdmc.statoprenotazionibdmc.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.statoprenotazionibdmc.controller.IStatoPrenotazioniBdmc;
import siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActInserisciStatoPrenotazioniBdmc
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di StatoPrenotazioniBdmc
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
public class ActInserisciStatoPrenotazioniBdmc extends ActionSiap implements ICostantiStatoPrenotazioniBdmc {

	/*****************************************************************************
	 * Azione di Inserimento del StatoPrenotazioniBdmc
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		StatoPrenotazioniBdmcModel lStaMod = new StatoPrenotazioniBdmcModel();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		lStaMod.setStatoPrenotazioniBdmc(getRequestBigDecimalParameter(CAMPO_STATO_PRENOTAZIONI_BDMC));
		lStaMod.setIdMisuraCautelareBdmc(getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE_BDMC));
		lStaMod.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		lStaMod.setEsitoId(getRequestBigDecimalParameter(CAMPO_ESITO_ID));
		lStaMod.setEsitoMsg(getRequestStringParameter(CAMPO_ESITO_MSG));
		lStaMod.setIdPrenotazione(getRequestBigDecimalParameter(CAMPO_ID_PRENOTAZIONE));
		lStaMod.setProgPeriPres(getRequestBigDecimalParameter(CAMPO_PROG_PERI_PRES));
		lStaMod.setTipoTrasmissione(getRequestStringParameter(CAMPO_TIPO_TRASMISSIONE));

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		IStatoPrenotazioniBdmc lCtrl = BDMCLookupRemote.getStatoPrenotazioniBdmcRemote();
		StatoPrenotazioniBdmcModel lStaRetMod = new StatoPrenotazioniBdmcModel();
		lStaRetMod = lCtrl.ExInserisciStatoPrenotazioniBdmc(lStaMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.statoprenotazionibdmc.action.ActLoadDettaglioStatoPrenotazioniBdmc";
		lPage += "&" + CAMPO_STATO_PRENOTAZIONI_BDMC + "=" + lStaRetMod.getStatoPrenotazioniBdmc().toString();

		return lPage;
	}

}