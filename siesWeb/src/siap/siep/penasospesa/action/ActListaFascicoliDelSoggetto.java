package siap.siep.penasospesa.action;

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
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.PenaComplessivaController;
import siap.siep.reato.controller.ReatoController;
import siap.siep.sentenza.controller.ISentenza;
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
public class ActListaFascicoliDelSoggetto extends ActionSiap implements ICostantiFascicoloSiep {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("IdFascCorrente", lFascicoloMod.getIdFascicoloSiep().toString());

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getCodDistretto();

		String lCodTipoUfficio = lUtenteConnesso.getUfficioUtente().getCodTipoUfficio();
		// boolean utenteSIUS = (lCodTipoUfficio.compareToIgnoreCase("TDS") == 0
		// || lCodTipoUfficio.compareToIgnoreCase("UDS") == 0) ? true : false;

		// n.b. di default la ricerca viene impostata sul 'distretto'
		String TipoRicerca = "distretto";

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
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExCountFascicoloOnViewPagedSuperSoggetti(lSogMod, lCodTipoUfficio, 0,
					TipoRicerca, StrCodiceDistrettoUtente);

		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// se ordinanza il passaggio per questa classe è fittizio!!
		// deve ritornare 0 fascicoli perchè dovrei andare sul sige
		if (!this.isRequestParameterNullObj("TipoDoc")) {
			if (this.getRequestStringParameter("TipoDoc").equals("Ordinanza"))
				lVect = lCtrl.ExRicercaFascicoloOnViewPagedSuperSoggetti(new SoggettoModel(), lCodTipoUfficio,
						Integer.parseInt(lPagina), TipoRicerca, StrCodiceDistrettoUtente);
			else
				lVect = lCtrl.ExRicercaFascicoloOnViewPagedSuperSoggetti(lSogMod, lCodTipoUfficio,
						Integer.parseInt(lPagina), TipoRicerca, StrCodiceDistrettoUtente);
		} else {
			lVect = lCtrl.ExRicercaFascicoloOnViewPagedSuperSoggetti(lSogMod, lCodTipoUfficio,
					Integer.parseInt(lPagina), TipoRicerca, StrCodiceDistrettoUtente);

			Vector lVectreati = new Vector();
			Vector lVectpenacompl = new Vector();
			for (int i = 0; i < lVect.size(); i++) {
				FascicoloSiepModel lFascTrovato = (FascicoloSiepModel) lVect.get(i);
				ReatoController lCtrlreati = new ReatoController();
				try {
					lVectreati.add(lCtrlreati
							.ExRicercaReatiNoCircostanzaByFascicolo(lFascTrovato.getIdFascicoloSiep()));
				} catch (F3BException e) {
					if (e.getMessage().equalsIgnoreCase("Nessun Elemento trovato")) {
						// String notFound = "R";
					} else {
						throw e;
					}
				}

				PenaComplessivaController lCtrlpenacompl = new PenaComplessivaController();
				lVectpenacompl.add(lCtrlpenacompl
						.ExRicercaPenaComplessivaByIdFascicolo(lFascTrovato.getIdFascicoloSiep()));

				this.setRequestAttribute("allreati", lVectreati);
				this.setRequestAttribute("allpenacompl", lVectpenacompl);
			}
		}

		String lReturnPage = "";
		// String flagRicercaData = null;

		IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
		FascicoloSiepModel lFasc = new FascicoloSiepModel();
		UfficioModel lUffModDitretto = new UfficioModel();
		UfficioModel lUffModAltroDitretto = new UfficioModel();

		Vector ufficiDistretto = new Vector();
		Vector ufficiAltroDistretto = new Vector();

		ISentenza lCtrlSen = SIEPLookupRemote.getSentenzaRemote();
		for (int i = 0; i < lVect.size(); i++) {
			lFasc = (FascicoloSiepModel) lVect.get(i);

			lFasc.setSentenza(lCtrlSen.ExRicercaSentenzaByKey(lFasc.getSenIdSentenza()));
			if (lFasc.getCodDistretto().equals(StrCodiceDistrettoUtente)) {
				lUffModDitretto = lCtrlUff.getUfficioByKey(lFasc.getChiaveUfficio());
				ufficiDistretto.add(lUffModDitretto);
			} else {
				lUffModAltroDitretto = lCtrlUff.getUfficioByKey(lFasc.getChiaveUfficio());
				ufficiAltroDistretto.add(lUffModAltroDitretto);
			}

		}

		setRequestAttribute("fascicoli", lVect);
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
		String lAzione = "siap.siep.penasospesa.action.ActListaFascicoloPerSoggetto";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

		setRequestAttribute("formname", getRequestStringParameter("formname"));

		lReturnPage = f3b.web.IWebConstants.ROOT_DIR
				+ "files/siap/siep/penasospesa/RicercaFascicoloSiepPerSoggetto.jsp";

		String attivazione = "";
		if (!isRequestParameterNullObj("Attivazione")) {
			attivazione = getRequestStringParameter("Attivazione");
			if (attivazione.equals("RichiestaRevoca"))
				lReturnPage = f3b.web.IWebConstants.ROOT_DIR
						+ "files/siap/siep/penasospesa/FascicoliDelSoggetto.jsp";

		}

		return lReturnPage; // restituisce la jsp di VIEW
	} // Chude processRequest()

} // Chiude Classe