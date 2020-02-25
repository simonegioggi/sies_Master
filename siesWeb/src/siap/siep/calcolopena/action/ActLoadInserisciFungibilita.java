package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciFungibilita
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di AnnotazioneManuale
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
@SuppressWarnings("rawtypes")
public class ActLoadInserisciFungibilita extends ActionSiap {

	/**
	 * Effettua il caricamento dei dati per la form di inserimanto/aggiornamento del ù provvedimento di
	 * computo Pena Detentiva Espiata per Altro Reato (fungibilità) art. 657 c.p.p. Invocata dalla
	 * VediCalcoloPenaValidataAnnotazioniComputo.jsp con flagPage = 'S'
	 * 
	 * @return nome della form di visualizzazione
	 */
	public String processRequest() throws F3BException {

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

		// ==========================================================================
		// Posizione Giuridica
		// ==========================================================================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("posizioneGiuridica", lPos.getPosizioneGiuridica());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Recupero la pena residua corrente (ultima validata o meno)
		// Dovrebbe essere quella ricalcolata in fase di inserimento del computo
		// ==========================================================================
		IPenaResidua lCtrlPosPena = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPosModPen = lCtrlPosPena.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		this.setRequestAttribute("penaresidua", lPosModPen);

		// ==========================================================================
		// Rigiro il flag page sulla form
		// ==========================================================================
		String flagPage = this.getRequestStringParameter("flagPage");
		this.setRequestAttribute("flagPage", flagPage);

		// ==========================================================================
		// Ricerca evento legato all'annotazione manuale
		// ==========================================================================
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();

		EventoModel lEveModRic = new EventoModel();
		lEveModRic.setFasSieIdFascicoloSiep(lIdFascicolo);
		// lEveModRic.setCodTipoProvvedimento("02");
		lEveModRic.setCodTipoProvvedimento("04"); // PROVVEDIMENTO
		lEveModRic.setCodTipoEvento("01");

		// flagPage: A = Altro Titolo (Fungibilità altro reato Misura Cautelare)
		// flagPage: S = Senza Titolo (Fungibilità altro reato Pena Detentiva) ??? in realtà non viene mai
		// chiamata con questo flag (vedi ActLoadInserisciFungibilita)
		// flagPage: D = Stesso Titolo (Presofferto)
		if (flagPage.equals("A")) { // computo Misura Cautelare Altro Reato art. 657 c.p.p.
			lEveModRic.setCodMotivo("0212");
		} else if (flagPage.equals("S")) { // computo Pena Detentiva Espiata per Altro Reato (fungibilità)
											// art. 657 c.p.p.
			lEveModRic.setCodMotivo("0213");
		} else if (flagPage.equals("D")) { // -computo Misura Cautelare stesso Reato art. 657 c.p.p.
											// (Presofferto)
			lEveModRic.setCodMotivo("0121");
		}

		// ==========================================================================
		// Recupero le annotazioni legate all'evento
		// ==========================================================================
		EventoModel lEveModDep = lCtrlEve.ExRicercaEventoNonRegistrato(lEveModRic);

		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();

		if (lEveModDep != null) {
			this.setRequestAttribute("evento", lEveModDep);

			try {
				Vector lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveModDep.getIdEvento());

				this.setRequestAttribute("annotazioneManuale", lAnnMod);
			} catch (Exception e) {
			}
		} else {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non esiste il decreto computo custodia cautelare - art. 657 c.p.p.");
		}

		// ==========================================================================
		// Ricerco l'ultima ordinanza (03) emessa dello stesso tipo del provvedimento
		// e le annotazioni collegate
		// ????????
		// ==========================================================================
		lEveModRic.setCodTipoProvvedimento("03");

		EventoModel lEveOrdMod = lCtrlEve.ExRicercaUltimoTipoEventoByIdFascicolo(lEveModRic);

		AnnotazioneManualeModel lAnnGE = null;
		if (lEveOrdMod != null && lEveOrdMod.getIdEvento() != null)
			lAnnGE = lCtrlAnn.ExRicercaAnnotazioniManualiByIdEvento(lEveOrdMod.getIdEvento());

		setRequestAttribute("annotazioneManualeGE", lAnnGE);

		// ==========================================================================
		// Ricerco l'ultimo Ordine di Esecuzione (06) emesso (e validato)
		// ==========================================================================
		EventoModel lEveModelEve = new EventoModel();

		lEveModelEve.setCodTipoEvento("01");
		lEveModelEve.setCodTipoProvvedimento("06");
		lEveModelEve.setFlagDocumentoRegistrato("S");

		try {
			lEveModelEve.setFasSieIdFascicoloSiep(lIdFascicolo);
			Vector eventi = lCtrlEve.ExRicercaEvento(lEveModelEve);

			EventoModel lEve = (EventoModel) eventi.get(0);
			this.setRequestAttribute("evento06", lEve);
		} catch (Exception ex) {
		}

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		Option lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

		Option lOptionUffici = new Option(DecodificheManager.getInstance()
				.getTipoUfficioCumuloSentenzaDecreto());
		setRequestAttribute("codiceUffici", "" + lOptionUffici);

		String lPage = new String(f3b.web.IWebConstants.ROOT_DIR
				+ "/files/siap/siep/calcolopena/LoadInserisciFungibilita.jsp");

		return lPage; // restituisce la jsp di VIEW
	}

}