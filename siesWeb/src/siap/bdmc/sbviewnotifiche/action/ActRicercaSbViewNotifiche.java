package siap.bdmc.sbviewnotifiche.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewnotifiche.controller.ISbViewNotifiche;
import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaSbViewNotifiche
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di SbViewNotifiche
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
public class ActRicercaSbViewNotifiche extends ActionSiap implements ICostantiSbViewNotifiche {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// ==============================================================
		// Recupero dalla form i campi di ricerca
		// n.b. cancellare o commentare i campi non presenti nella form
		// ==============================================================
		SbViewNotificheModel lSbVMod = new SbViewNotificheModel();

		// lSbVMod.setUtenSies(this.getUtenteConnesso().getUserId());
		lSbVMod.setCodiUffiSies(this.getUfficioUtenteConnesso().getCodUfficio());

		/*
		 * lSbVMod.setProgNoti ( getRequestBigDecimalParameter ( CAMPO_PROG_NOTI) ); lSbVMod.setCodiNoti (
		 * getRequestStringParameter ( CAMPO_CODI_NOTI) ); lSbVMod.setDescrizione ( getRequestStringParameter
		 * ( CAMPO_DESCRIZIONE) ); lSbVMod.setDataInviNoti ( getRequestDateParameter (
		 * CAMPO_ANNO_DATA_INVI_NOTI,CAMPO_MESE_DATA_INVI_NOTI,CAMPO_GIORNO_DATA_INVI_NOTI) );
		 * lSbVMod.setDataRegiNoti ( getRequestDateParameter (
		 * CAMPO_ANNO_DATA_REGI_NOTI,CAMPO_MESE_DATA_REGI_NOTI,CAMPO_GIORNO_DATA_REGI_NOTI) );
		 * lSbVMod.setDataValiNoti ( getRequestDateParameter (
		 * CAMPO_ANNO_DATA_VALI_NOTI,CAMPO_MESE_DATA_VALI_NOTI,CAMPO_GIORNO_DATA_VALI_NOTI) ); lSbVMod.setNote
		 * ( getRequestStringParameter ( CAMPO_NOTE) ); lSbVMod.setCodiUffiSies ( getRequestStringParameter (
		 * CAMPO_CODI_UFFI_SIES) ); lSbVMod.setCodiUffi ( getRequestStringParameter ( CAMPO_CODI_UFFI) );
		 * lSbVMod.setFlagStatNoti ( getRequestStringParameter ( CAMPO_FLAG_STAT_NOTI) );
		 * lSbVMod.setDataChiuNoti ( getRequestDateParameter (
		 * CAMPO_ANNO_DATA_CHIU_NOTI,CAMPO_MESE_DATA_CHIU_NOTI,CAMPO_GIORNO_DATA_CHIU_NOTI) );
		 * lSbVMod.setFlagTras ( getRequestBigDecimalParameter ( CAMPO_FLAG_TRAS) );
		 * lSbVMod.setStopAnnoFascBdmc ( getRequestBigDecimalParameter ( CAMPO_STOP_ANNO_FASC_BDMC) );
		 * lSbVMod.setStopNumeFascBdmc ( getRequestBigDecimalParameter ( CAMPO_STOP_NUME_FASC_BDMC) );
		 * lSbVMod.setUtenSies ( getRequestStringParameter ( CAMPO_UTEN_SIES) ); lSbVMod.setIdPren (
		 * getRequestBigDecimalParameter ( CAMPO_ID_PREN) ); lSbVMod.setProgPeri (
		 * getRequestBigDecimalParameter ( CAMPO_PROG_PERI) ); lSbVMod.setModiAnnoFascBdmc (
		 * getRequestBigDecimalParameter ( CAMPO_MODI_ANNO_FASC_BDMC) ); lSbVMod.setModiNumeFascBdmc (
		 * getRequestBigDecimalParameter ( CAMPO_MODI_NUME_FASC_BDMC) ); lSbVMod.setFlagModi (
		 * getRequestStringParameter ( CAMPO_FLAG_MODI) ); lSbVMod.setDataIniz ( getRequestDateParameter (
		 * CAMPO_ANNO_DATA_INIZ,CAMPO_MESE_DATA_INIZ,CAMPO_GIORNO_DATA_INIZ) ); lSbVMod.setDataFine (
		 * getRequestDateParameter ( CAMPO_ANNO_DATA_FINE,CAMPO_MESE_DATA_FINE,CAMPO_GIORNO_DATA_FINE) );
		 * lSbVMod.setDataInizPrec ( getRequestDateParameter (
		 * CAMPO_ANNO_DATA_INIZ_PREC,CAMPO_MESE_DATA_INIZ_PREC,CAMPO_GIORNO_DATA_INIZ_PREC) );
		 * lSbVMod.setDataFinePrec ( getRequestDateParameter (
		 * CAMPO_ANNO_DATA_FINE_PREC,CAMPO_MESE_DATA_FINE_PREC,CAMPO_GIORNO_DATA_FINE_PREC) );
		 */

		// =================================
		// Selezionare il tipo di ricerca
		// =================================
		// String tipo_ricerca = "semplice";
		String tipo_ricerca = "paginata";

		String lReturnPage = null;

		if (tipo_ricerca.equals("semplice")) {
			// =========================================================
			// Istanzio il controller ed effettuo la ricerca semplice
			// =========================================================
			ISbViewNotifiche lCtrl = BDMCLookupRemote.getSbViewNotificheRemote();
			Vector lVect = lCtrl.ExRicercaSbViewNotifiche(lSbVMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("sbviewnotifiche", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCASBVIEWNOTIFICHE;
		} else {
			String lPagina = "1";
			String CountRisultati;

			// ============================================================================
			// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
			// ============================================================================
			// if (!isRequestParameterNullObj(ISIAPCostantiWeb.NUM_PAGE))
			// lPagina = getRequestStringParameter(ISIAPCostantiWeb.NUM_PAGE);

			if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
				lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

			// =========================================================
			// Istanzio il controller ed effettuo la ricerca paginata
			// =========================================================
			ISbViewNotifiche lCtrl = BDMCLookupRemote.getSbViewNotificheRemote();
			Vector lVect = lCtrl.ExRicercaSbViewNotifichePaged(lSbVMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountSbViewNotifiche(lSbVMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lSbVMod = new SbViewNotificheModel((SbViewNotificheModel) lVect.firstElement());
				setRequestAttribute("sbviewnotifiche", lSbVMod);
				setFunctionsAvailableToRequest("siap.bdmc.sbviewnotifiche.action.ActLoadDettaglioSbViewNotifiche");
				setRequestAttribute("modalita", "D");

				lReturnPage = PG_LOAD_DETTAGLIOSBVIEWNOTIFICHE;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", new BigDecimal(Integer.parseInt(CountRisultati)));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("CountRisultati = " + CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("sbviewnotifiche", lVect);

				lReturnPage = PG_RICERCASBVIEWNOTIFICHE;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}