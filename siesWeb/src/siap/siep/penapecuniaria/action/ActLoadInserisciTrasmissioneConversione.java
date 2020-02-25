package siap.siep.penapecuniaria.action;

/**
* <p>Title: ActLoadInserisciTrasmissioneConversione</p>
* <p>Description: Classe Action per la load inserisci di TrasmissioneConversione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActLoadInserisciTrasmissioneConversione extends ActionSiap implements ICostantiPenaPecuniaria {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// AMBROS 14/11/2013 Se Fascicolo di Ufficio Accorpato, il controllo è su CHIAVE_PROGR_ORIG --->
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();

		UfficioModel UffMod = new UfficioModel();
		UffMod = lUtenteMod.getUfficioUtente();

		if (lUffUtente.equals(lFascMod.getCodUfficioInserimento())) {
			int lFascProg = lFascMod.getChiaveProgr().intValue();
			if (lFascProg < 70000 || lFascProg > 80000) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
								+ " " + "non è di conversione delle pene pecuniarie (classe VII)");
				return IWebConstants.PG_MESSAGE;
			}
		} else {
			if (UffMod.isUfficioDiCompetenza(lFascMod.getCodUfficioInserimento())) {
				int lFascProgOrig = lFascMod.getChiaveProgrOrig().intValue();
				if (lFascProgOrig < 70000 || lFascProgOrig > 80000) {
					setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N."
							+ lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " "
							+ "di Ufficio Accorpato non è di conversione delle pene pecuniarie (classe VII)");
					return IWebConstants.PG_MESSAGE;
				}
			}
		}

		// ---> END AMBROS

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile effettuare la trasmissione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Inserire Eventuali ComboBOX
		// Option lOption = new Option( DecodificheManager.getInstance().get???());
		// setRequestAttribute("???", "" + lOption );
		// ** Seleziona l'ultima eventuale Posizione Giuridica associata al Fascicolo Siep **
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = lCtrlPos
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
						lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPosLuoAltMod);

		// residenza
		IResidenza lResCtrl = SICOLookupRemote.getResidenzaRemote();
		Vector lVecRes = lResCtrl.ExRicercaResidenzeByIdFascicolo(lFascMod.getIdFascicoloSiep());

		ResidenzaAssociataModel lResAssMod = new ResidenzaAssociataModel();
		if (lVecRes != null && !lVecRes.isEmpty()) {
			lResAssMod = (ResidenzaAssociataModel) lVecRes.get(0);
		}
		setRequestAttribute("residenzaassociata", lResAssMod);

		// domicilio
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		ResidenzaAssociataModel lDomAssMod = lCtrl
				.ExRicercaDomicilioFascicoloSiepCorrente(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("domicilioassociato", lDomAssMod);

		// 09/07/2015 Ricerca Magistrato Competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistrato", lMagMod);

		// autorità per la conversione
		// Tolta il 05/02/2009 per 4.0 Ambros

		// Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaSanzioni() , "-");
		// setRequestAttribute("autoritaConv", "" + lOption );

		// carico il record della richiesta
		IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		Vector lRichiestaConversioni = new Vector();
		lRicMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// 22/01/2015 Risolta anomalia 'Trasmissione atti per la conversione'
		lRichiestaConversioni = lCtrlRic.ExRicercaRichiesteConversioniValide(lRicMod);
		if (lRichiestaConversioni.size() > 0) {
			lRicMod = (RichiestaConversioneModel) lRichiestaConversioni.get(0);
			lRicMod = lCtrlRic.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
			setRequestAttribute("richiestaconversione", lRicMod);
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Richiesta di conversione non trovata");
			return IWebConstants.PG_MESSAGE;

		}
		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");

		// 24/07/2015 Tipo UDS per la comunicazione
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		lOption.setFilter(new String[] { "-", "UDS", "UDSM" });
		setRequestAttribute("tipoUDS", "" + lOption);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCITRASMISSIONECONVERSIONE;
	}
}