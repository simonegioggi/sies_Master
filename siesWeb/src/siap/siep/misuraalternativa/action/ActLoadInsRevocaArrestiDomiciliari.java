package siap.siep.misuraalternativa.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInsRevocaArrestiDomiciliari
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Revoca Detenzione Domiciliare
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

public class ActLoadInsRevocaArrestiDomiciliari extends ActRevoca {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getRevoca();
		if (!lRitorno.equals(""))
			return lRitorno;

		// ricerca esistenza almeno una misura alternativa cancessa
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		IMisuraAlternativaIndultino lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		// MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		String[] tipoMisura = { "0005", "0010", "0013", "0011", "0197" };
		String[] natura = { "CO", "DD" };
		String[] decisione = { "03" };
		/* lMisAlModConcessa = */lMisAltCtrl.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
				lFascMod.getIdFascicoloSiep(), decisione, natura, tipoMisura);

		// Posizione giuridica
		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

		// La revoca degli arresti domiciliari può avvenire dopo la sospensione provvisoria, oppure,
		// direttamente.
		// Nel caso (revoca dopo sospensione) Posizione giuridica:
		// a) Sospensione provvisoria prosecuzione arresti domiciliari ex art. 656 comma 10
		// b) Sospensione provvisoria prosecuzione Arresti Domiciliari ex art 89 dpr 309/90
		// c) Sospensione provvisoria prosecuzione permanenza in casa ex art. 656 comma 10
		// d) Sospensione provvisoria prosecuzione collocamento in comunità ex art. 656 comma 10.
		String codPosizioneGiuridica = lPos.getCodPosizioneGiuridica();
		String revocaDopoSospensioneProvvisoria = "";
		if (codPosizioneGiuridica.equalsIgnoreCase("62") || codPosizioneGiuridica.equalsIgnoreCase("63")
				|| codPosizioneGiuridica.equalsIgnoreCase("64")
				|| codPosizioneGiuridica.equalsIgnoreCase("65")) {
			revocaDopoSospensioneProvvisoria = "dopoSospensione";// indiretta
		} else {
			revocaDopoSospensioneProvvisoria = "direttamente";
		}

		setRequestAttribute("revocaDopoSospensioneProvvisoria", revocaDopoSospensioneProvvisoria);

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = null;
		try {
			lAvvocati = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					e.getMessage() + " Impossibile eseguire l'Ordine di Esecuzione.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// setto il campo ufficio emittente
		Option lOptionUfficioEmittenteArrestiDomiciliari = new Option(
				DecodificheManager.getInstance().getUfficioEmittenteArrestiDomiciliari());
		setRequestAttribute("tipoUfficioEmittenteArrestiDomiciliari",
				"" + lOptionUfficioEmittenteArrestiDomiciliari);

		// setto il campo codice motivo
		Option lOption = new Option(
				DecodificheManager.getInstance().getMotivoProvvedimentoRevocaArrestiDom());
		setRequestAttribute("motivoProvv", "" + lOption);

		setRequestAttribute("tipoSospensione", "DETENZIONE");

		Option lOptionAvv = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOptionAvv);

		setRequestAttribute("avvocati", lAvvocati);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_REVOCA_ARRESTI_DOMICILIARI;
	}

}