package siap.bdmc.fascicolosiepbdmc.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActRicercaFascicoloSiepBdmc
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di FascicoloSiepBdmc
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
public class ActRicercaFascicoloSiepBdmc extends ActionSiap implements ICostantiFascicoloSiepBdmc {

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
		FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();

		lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_BDMC));
		if (!(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE)).equalsIgnoreCase("-")
				&& !(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)).equalsIgnoreCase("")) {
			lFasMod.setChiaveUfficioBdmc(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
					getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		} else {
			lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_BDMC));
		}
		lFasMod.setChiaveAnnoSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_SIEP));
		lFasMod.setChiaveUfficioSiep(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO_SIEP));
		lFasMod.setChiaveProgrSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_SIEP));
		lFasMod.setFlagTrasmissione(getRequestStringParameter(CAMPO_FLAG_TRASMISSIONE));

		// ==============================================
		// Passaggio della descrizione del ufficio siep
		// ==============================================
		String descr_ufficio_siep = getRequestStringParameter(CAMPO_DESCR_AUTEMI_SIEP);

		// =================================
		// Selezionare il tipo di ricerca
		// =================================
		// String tipo_ricerca = "semplice";
		String tipo_ricerca = "paginata";

		String lReturnPage = null;

		setRequestAttribute("tipo_ricerca", tipo_ricerca);
		setRequestAttribute("descr_ufficio_siep", descr_ufficio_siep);

		String codUfficio = this.getUfficioUtenteConnesso().getCodUfficio(); // per il codice ufficio
																				// dell'operatore connesso
		String descrUfficio = this.getUfficioUtenteConnesso().getDescrTipoUfficio(); // per la descrizione del
																						// tipo ufficio
																						// dell'utente
																						// connesso
		String descrComune = this.getUfficioUtenteConnesso().getDescrComune(); // per il comune dell'ufficio
																				// connesso
		String codProv = this.getUfficioUtenteConnesso().getCodProvincia(); // per la sigla della provincia

		setRequestAttribute("codUfficio", codUfficio);
		setRequestAttribute("descrUfficio", descrUfficio);
		setRequestAttribute("descrComune", descrComune);
		setRequestAttribute("codProv", codProv);
		setRequestAttribute("modalita", "M");

		if (tipo_ricerca.equals("semplice")) {
			// =========================================================
			// Istanzio il controller ed effettuo la ricerca semplice
			// =========================================================
			IFascicoloSiepBdmc lCtrl = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			Vector lVect = lCtrl.ExRicercaFascicoloSiepBdmc(lFasMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			if (lVect.size() == 1) {
				lFasMod = new FascicoloSiepBdmcModel((FascicoloSiepBdmcModel) lVect.firstElement());
				// ===========================================================
				// Restituisce la msg se i dati non sono stati recuperati.
				// ===========================================================
//				if (lFasMod == null) {
//					setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT,
//							"I dati richiesti non sono stati trovati");
//					// Specificare eventualmente la jump page dove verrà ridirezionata la
//					// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
//					// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
//					// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
//					// della root_dir es /siap/frame.htm
//					setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
//					return ISIAPCostantiWeb.PG_MESSAGE;
//				}

				setRequestAttribute("fascicolosiepbdmc", lFasMod);
				setSessionAttribute("fascicolosiepbdmcSession", lFasMod);
				lReturnPage = PG_LOAD_INSERISCIFASCICOLOSIEPBDMC;
			} else {
				setRequestAttribute("fascicolosiepbdmc", lVect);
				lReturnPage = PG_RICERCAFASCICOLOSIEPBDMC;
			}
		} else {
			String lPagina = "1";
			String CountRisultati;

			// ============================================================================
			// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
			// ============================================================================
			if (!isRequestParameterNullObj(ISIAPCostantiWeb.NUM_PAGE))
				lPagina = getRequestStringParameter(ISIAPCostantiWeb.NUM_PAGE);

			// =========================================================
			// Istanzio il controller ed effettuo la ricerca paginata
			// =========================================================

			IFascicoloSiepBdmc lCtrl = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			Vector lVect = lCtrl.ExRicercaFascicoloSiepBdmcPaged(lFasMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}
			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================

			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountFascicoloSiepBdmc(lFasMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (lVect.size() == 1) {
				lFasMod = new FascicoloSiepBdmcModel((FascicoloSiepBdmcModel) lVect.firstElement());

				setRequestAttribute("fascicolosiepbdmc", lFasMod);
				setSessionAttribute("fascicolosiepbdmcSession", lFasMod);

				IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = lUctrl.getUfficioByKey(lFasMod.getChiaveUfficioBdmc());
				Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
						lUfficio.getCodTipoUfficio());

				setRequestAttribute("autoritaEmi", "" + lOption);

				setRequestAttribute("descrLuogoEmittente", lUfficio.getDescrComune());
				setRequestAttribute("codTipoAutoritaEmittente", lUfficio.getCodTipoUfficio());

				setFunctionsAvailableToRequest("siap.bdmc.fascicolosiepbdmc.action.ActLoadModificaFascicoloSiepBdmc");

				lReturnPage = PG_LOAD_INSERISCIFASCICOLOSIEPBDMC;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", new BigDecimal(Integer.parseInt(CountRisultati)));
				setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());

				setRequestAttribute("fascicolosiepbdmc", lVect);
				lReturnPage = PG_RICERCAFASCICOLOSIEPBDMC;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}
