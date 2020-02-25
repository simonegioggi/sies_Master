package siap.siep.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiapMinor;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title:ActListaFascicoloPerSoggetto
 * </p>
 * <p>
 * Description: MOstra tutti i procedimenti per il soggetto selezionato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 */
public class ActListaFascicoloPerSoggettoCodCui extends ActionSiapMinor implements ICostantiFascicoloSiep {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getCodDistretto();

		String lCodTipoUfficio = lUtenteConnesso.getUfficioUtente().getCodTipoUfficio();
		boolean utenteSIUS = (lCodTipoUfficio.compareToIgnoreCase("TDS") == 0
				|| lCodTipoUfficio.compareToIgnoreCase("UDS") == 0) ? true : false;

		String Ufficio = this.getCodUfficioUtenteConnesso();
		// Controlla il tipo di ricerca se "ufficio" o "distretto"
		//
		// n.b. di default la ricerca viene impostata sul 'distretto' in quanto questa
		// action viene invocata anche dal dettaglio Soggetto (menù a tendina
		// 'Elenco Procedimenti di esecuzione') e in questo caso il parametro
		// non viene passato. Per cui limitando la ricerca all'ufficio si potrebbe
		// erroneamente ritornare il messaggio 'Nessun elemento trovato' sebbene
		// per il soggetto in questione siano presento procedimenti, magari su
		// altri uffici. (n.b. la ricerca per soggetto viene effettuata su tutto
		// il distretto, per cui anche la ricerca dei procedimenti dal dettaglio
		// di quel soggetto deve essere effettuata su tutti il distretto)

		String TipoRicerca = "distretto";
		if (!this.isRequestParameterNullObj("tipoRicerche")) {
			TipoRicerca = this.getRequestStringParameter("tipoRicerche");
		}

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		SoggettoModel lSogMod = new SoggettoModel();

		FascicoloSiepModel lFascMod = new FascicoloSiepModel();
		;
		BigDecimal lIdSoggetto = this.getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);

		lFascMod.setSogIdSoggetto(lIdSoggetto);
		;
		lFascMod.setChiaveUfficio(getCodUfficioUtenteConnesso());

		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		ISoggetto lCtrlSogg = SICOLookupRemote.getSoggettoRemote();
		lSogMod = lCtrlSogg.ExRicercaSoggettoByKey(lFascMod.getSogIdSoggetto());

		// Ritrasformo tipoClasse (che viene da ActRicercaFascicoloPerSoggetto.java) da String in array
		// (solo nel caso in cui sia selezionata la classe in ActRicercaFascicoloPerSoggetto.java)

		String tipoClasse = "";
		if (!this.isRequestParameterNullObj("StrClassiFascicolo")) {
			tipoClasse = this.getRequestStringParameter("StrClassiFascicolo");
			String[] lClassiFascicolo = tipoClasse.split("/");
			lSogMod.setClassiFascicolo(lClassiFascicolo);
		}

		setRequestAttribute("soggetto", lSogMod);
		setSessionAttribute("soggetto", lSogMod);

		// Ambros --> Per il conteggio di inner, nella ricerca passo il parametro pagina=0
		// Per la ricerca lato SIUS forzo il parametro TipoRicerca='sius'
		// Per la ricerca lato SIEP passo il parametro TipoRicerca
		// (che sarà uguale o a 'distretto' o 'ufficio')

		Vector lVect = null;
		String majorOffice = this.checkMinori();
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			if (utenteSIUS) {
				CountRisultati = lCtrl.ExCountFascicoloOnViewPagedSuperSoggetti(lSogMod, lCodTipoUfficio, 0,
						"sius", StrCodiceDistrettoUtente, majorOffice, "");
			} else {

				CountRisultati = lCtrl.ExCountFascicoloOnViewPagedSuperSoggetti(lSogMod, Ufficio, 0,
						TipoRicerca, StrCodiceDistrettoUtente, majorOffice, lCodTipoUfficio);
			}

			// - - - - > TRASFOMAZIONE INTERO in BIGDESCIMAL <- - - - - //
			// int x = lVect.size();
			// CountRisultati = new BigDecimal(""+x);

		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// Ambros --> Per la Query vera di ricercaSSogg passo il parametro lpagina;
		// Per la ricerca lato SIUS forzo il parametro TipoRicerca='sius'
		// Per la ricerca lato SIEP passo il parametro TipoRicerca
		// (che sarà uguale o a 'distretto' o 'ufficio')

		if (utenteSIUS) {
			lVect = lCtrl.ExRicercaFascicoloOnViewPagedSuperSoggetti(lSogMod, lCodTipoUfficio,
					Integer.parseInt(lPagina), "sius", StrCodiceDistrettoUtente, majorOffice, "", "");
		} else {

			lVect = lCtrl.ExRicercaFascicoloOnViewPagedSuperSoggetti(lSogMod, Ufficio,
					Integer.parseInt(lPagina), TipoRicerca, StrCodiceDistrettoUtente, majorOffice,
					lCodTipoUfficio, "");
		}

		String lReturnPage = "";
		String flagRicercaData = null;

		if (lVect.size() == 1 && !utenteSIUS) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) lVect.get(0)).getIdFascicoloSiep().toString() + "&idSogge="
					+ flagRicercaData;
		} else {
			setRequestAttribute("fascicoli", lVect);
			IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
			FascicoloSiepModel lFasc = new FascicoloSiepModel();
			UfficioModel lUffModDitretto = new UfficioModel();
			UfficioModel lUffModAltroDitretto = new UfficioModel();

			Vector ufficiDistretto = new Vector();
			Vector ufficiAltroDistretto = new Vector();

			for (int i = 0; i < lVect.size(); i++) {
				lFasc = (FascicoloSiepModel) lVect.get(i);
				if (lFasc.getCodDistretto().equals(StrCodiceDistrettoUtente)) {
					lUffModDitretto = lCtrlUff.getUfficioByKey(lFasc.getChiaveUfficio());
					ufficiDistretto.add(lUffModDitretto);
				} else {
					lUffModAltroDitretto = lCtrlUff.getUfficioByKey(lFasc.getChiaveUfficio());
					ufficiAltroDistretto.add(lUffModAltroDitretto);
				}

			}

			setRequestAttribute("ufficiDistretto", ufficiDistretto);
			setRequestAttribute("ufficiAltroDistretto", ufficiAltroDistretto);
			setRequestAttribute("CountRisultati", CountRisultati);

			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("StrCodiceDistrettoUtente", StrCodiceDistrettoUtente);

			// Controlla se esiste il parametro dell'Alias per visualizzare il nome dell'Alias

			String lNomeSoggettoAlias = "";
			if (!isRequestParameterNullObj("NomeSoggettoAlias")) {
				lNomeSoggettoAlias = getRequestStringParameter("NomeSoggettoAlias");
			}

			setRequestAttribute("StrNomeSoggettoAlias", lNomeSoggettoAlias);

			// passa alla pagina di view il parametro del tipo di ricerca se ufficio o distretto
			setRequestAttribute("strTipoRicerca", TipoRicerca);
			String lAzione = "siap.siep.fascicolo.action.ActListaFascicoloPerSoggetto";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

			lReturnPage = PG_RICERCAFASCICOLO_SIEP_PER_SOGGETTO;

		} // chiude if (lVect.size()

		return lReturnPage; // restituisce la jsp di VIEW
	} // Chude processRequest()

} // Chiude Classe