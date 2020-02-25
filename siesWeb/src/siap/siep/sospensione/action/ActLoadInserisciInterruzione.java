package siap.siep.sospensione.action;

/**
 * <p>Title: ActLoadInserisciInterruzione</p>
 * <p>Description: Classe Action per la load inserimento della Sospensione di Interruzione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActLoadInserisciInterruzione extends ActionSiap implements ICostantiSospensione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Controllo Validazione Fascicolo
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		/******************************* Posizione Giuridica **********************************/
		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		if (lPosizione == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		setRequestAttribute("posizione", lPosizione);

		/******************************* Pena Complessiva *****************************/
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03")) {
				lFlagErgastolo = "S";
			} else if (lPenComMod.getCodTipoPenaDetentiva().equals("04")) {
				lFlagErgastolo = "D";
			}
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);
		/******************************* Fine Pena Complessiva ************************/

		// ==============================================================================
		// Ricerco al pena da interrompere

		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = null;
		PenaResiduaModel lPenaResiduaRic = new PenaResiduaModel();

		lPenaResiduaRic.setFasSieIdFascicoloSiep(lIdFascicolo);
		Vector lpeneResidue = null;

		if (lPosizione.isLibero()) // LIBERO (07)
		{
			// Recupero tutte le pene presenti (validate o meno)
			lpeneResidue = lCtrlPenRes.ExRicercaPenaResidua(lPenaResiduaRic);
			if (lpeneResidue.size() == 1) {
				lPenaResidua = (PenaResiduaModel) lpeneResidue.get(0);
			} else { // se è presente più di una pena recupero l'ultima validata (se esiste)
				lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
			}

			/*
			 * if (lPenaResidua != null && lPenaResidua.getFlagPenaSospesa() != null) { if
			 * (lPenaResidua.getFlagPenaSospesa().equals("S")) throw new
			 * F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Sospesa"); if
			 * (lPenaResidua.getFlagPenaSospesa().equals("I")) throw new
			 * F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Interrotta"); if
			 * (lPenaResidua.getFlagPenaSospesa().equals("D")) throw new
			 * F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Definita"); }
			 */
		} else { // nono libero recupero l'ultima in assoluto (validata o meno)
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
		// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
		String lErrore = null;
		String lAzioneChiamante = null;

		if (lPenaResidua == null) {
			// lErrore = "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?";
			lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
			lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
		} else if ((!lPosizione.isLibero() && !lPosizione.getCodPosizioneGiuridica().equals("16") // Libero in
																									// Differimento
																									// Pena
				&& !lPosizione.getCodPosizioneGiuridica().equals("17") // Libero in Differimento Pena
																		// (Provvisorio)
				&& !lPosizione.getCodPosizioneGiuridica().equals("46") // Libero in Sospensione
				&& !lPosizione.getCodPosizioneGiuridica().equals("47") // Libero in Sospensione DPR 309/90
																		// (simeone)
		)// non è libero
				&& (lPenaResidua.getDataInizio() == null // non ha le date
						|| lPenaResidua.getDataFine() == null)
				&& lFlagErgastolo.equals("N")) { // Se non libero, ma manca le date di decorrenza
			lErrore = "Data decorrenza pena inestistente. Rivedere la Posizione Giuridica";
			lAzioneChiamante = "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica";
		}

		if (lErrore != null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
			lRedirigi.setAction(lAzioneChiamante + "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "="
					+ getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("penaresidua", lPenaResidua);

		// FINE PENA RESIDUA
		// ==============================================================================

		// ** Ricerca su DECRETO_ORDINANZA_SIEP **
		IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();

		String[] aOggetto = { "0266", "0267", "0268", "0269", "0270", "0366" };
		DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd
				.ExRicercaDecretoOrdinanzaSiepByOggettoProcedimento(aOggetto, lFascMod);
		if (lDecOrd != null) {
			setRequestAttribute("decretoordinanza", lDecOrd);
			String lFlagDec = (lDecOrd == null ? "N" : "S");
			setRequestAttribute("flagdecretoordinanza", lFlagDec);
		}

		// TIPO REGISTRO ORDINANZA
		Collection lCollTipoReg = DecodificheManager.getInstance().getTipoRegistroOrdinanza();
		setRequestAttribute("tiporegistroordinanza", lCollTipoReg);

		// AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		Option lOption = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS());
		setRequestAttribute("autoritaemittente", "" + lOption);

		// motivo interruzione
		if (lDecOrd != null)
			lOption = new Option(DecodificheManager.getInstance().getMotivoInterruzione(),
					lDecOrd.getCodOggettoProcedimento());
		else
			lOption = new Option(DecodificheManager.getInstance().getMotivoInterruzione());

		setRequestAttribute("motivointerruzione", "" + lOption);
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
				"siap.siep.sospensione.action.ActLoadInserisciInterruzione");

		return PG_LOAD_INSERISCI_SOSPENSIONE_INTERRUZIONE; // restituisce la jsp di VIEW
	}
}
