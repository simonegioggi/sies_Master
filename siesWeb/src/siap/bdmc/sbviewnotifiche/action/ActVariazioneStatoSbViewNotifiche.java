package siap.bdmc.sbviewnotifiche.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.bdmc.sbviewnotifiche.controller.ISbViewNotifiche;
import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
import siap.sico.web.ActionSiap;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
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
public class ActVariazioneStatoSbViewNotifiche extends ActionSiap implements ICostantiSbViewNotifiche {

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
		lSbVMod.setProgNoti(getRequestBigDecimalParameter(ICostantiSbViewNotifiche.CAMPO_PROG_NOTI));
		lSbVMod.setDataChiuNoti(DateUtils.getDate((DateUtils.getSysDate("dd/MM/yyyy")), "dd/MM/yyyy"));

		// =========================================================
		// Istanzio il controller ed effettuo la ricerca paginata
		// =========================================================
		ISbViewNotifiche lCtrlChiudi = BDMCLookupRemote.getSbViewNotificheRemote();
		lCtrlChiudi.ExChiudiSbViewNotifiche(lSbVMod);

		// Inizion Gestione cessazione/ripristino trasmissione
		if (!isRequestParameterNullObj("annoBdmc")) {

			// =========================================================
			// Ricerco tutte le associazione legate alla notifica in esame
			// presenti su fascicolo_bdmc_siep
			// =========================================================
			FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();
			lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter("annoBdmc"));
			lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter("numeroBdmc"));
			lFasMod.setChiaveUfficioBdmc(getRequestStringParameter("ufficioBdmc"));
			IFascicoloSiepBdmc lCtrlAss = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			Vector lAssociazione = lCtrlAss.ExRicercaFascicoloSiepBdmc(lFasMod);
			// =========================================================
			// Modifico tutte le associazione legate alla notifica in esame
			// presenti su fascicolo_bdmc_siep
			// =========================================================
			if (lAssociazione != null && lAssociazione.size() != 0) {

				for (int ii = 0; ii < lAssociazione.size(); ii++) {
					FascicoloSiepBdmcModel lFasModCercato = (FascicoloSiepBdmcModel) lAssociazione.get(ii);
					if (getRequestStringParameter("flagAssociazione").compareTo("0") == 0)
						lFasModCercato.setFlagTrasmissione("C");
					else
						lFasModCercato.setFlagTrasmissione("S");
					lCtrlAss.ExModificaFascicoloSiepBdmc(lFasModCercato);
				}
			}
			// =============================================================
			// Ricerco tutte le associazione legate alla notifica in esame
			// presenti su misura_cautelare_bdmc che sono da trasmettere
			// =============================================================
			// ======================================================================
			// Aggiorno la riga della tabella misure_cautelare_bdmc
			// legata all'annotazione in esame
			// ======================================================================

			MisuraCautelareBdmcModel lMisCautBdmc = new MisuraCautelareBdmcModel();
			lMisCautBdmc.setAnnoFascBdmc(getRequestBigDecimalParameter("annoBdmc"));
			lMisCautBdmc.setNumeFascBdmc(getRequestBigDecimalParameter("numeroBdmc"));
			lMisCautBdmc.setCodUfficioBdmc(getRequestStringParameter("ufficioBdmc"));

			IMisuraCautelareBdmc lCtrMisCauBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
			Vector misCautBdmc = lCtrMisCauBdmc.ExRicercaMisuraCautelareBdmc(lMisCautBdmc);
			// ======================================================================
			// Tabella riepilogativa per gestione cessazione/ripristino trasmissione
			// Cod. Trasm Cod. No Trasm
			// I Y
			// V W
			// A X
			// C Z
			if (misCautBdmc != null && misCautBdmc.size() != 0) {
				for (int ii = 0; ii < misCautBdmc.size(); ii++) {
					MisuraCautelareBdmcModel lMisBdmc = (MisuraCautelareBdmcModel) misCautBdmc.get(ii);
					if (getRequestStringParameter("flagAssociazione").compareTo("0") == 0) {
						if (lMisBdmc.getFlagStato().compareTo("I") == 0)
							lMisBdmc.setFlagStato("Y");
						if (lMisBdmc.getFlagStato().compareTo("V") == 0)
							lMisBdmc.setFlagStato("W");
						if (lMisBdmc.getFlagStato().compareTo("A") == 0)
							lMisBdmc.setFlagStato("X");
						if (lMisBdmc.getFlagStato().compareTo("C") == 0)
							lMisBdmc.setFlagStato("Z");
					} else {
						if (lMisBdmc.getFlagStato().compareTo("Y") == 0)
							lMisBdmc.setFlagStato("I");
						if (lMisBdmc.getFlagStato().compareTo("W") == 0)
							lMisBdmc.setFlagStato("V");
						if (lMisBdmc.getFlagStato().compareTo("X") == 0)
							lMisBdmc.setFlagStato("A");
						if (lMisBdmc.getFlagStato().compareTo("Z") == 0)
							lMisBdmc.setFlagStato("C");

					}

					IMisuraCautelareBdmc lCtrMisBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
					lCtrMisBdmc.ExModificaMisuraCautelareBdmc(lMisBdmc, null);
				}
			}

		}
		// Fine Gestione Notifica cessazione/ripristino trasmissione

		lSbVMod = new SbViewNotificheModel();
		lSbVMod.setCodiUffiSies(this.getUfficioUtenteConnesso().getCodUfficio());

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
				this.setRequestAttribute(IWebConstants.GOTO_PAGE,
						"/jsp/Main.jsp?Action=siap.sico.security.action.ActLoadOrizontalMenu&IdFunzione=10558");
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
				this.setRequestAttribute(IWebConstants.GOTO_PAGE,
						"/jsp/Main.jsp?Action=siap.sico.security.action.ActLoadOrizontalMenu&IdFunzione=10558");
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