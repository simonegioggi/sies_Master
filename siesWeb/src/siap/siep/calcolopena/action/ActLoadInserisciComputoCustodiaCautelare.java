package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
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
 * Title: ActLoadInserisciComputoCustodiaCautelare
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
public class ActLoadInserisciComputoCustodiaCautelare extends ActionSiap {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		CSSAModel lCssa = null;
		IstitutoDetenzioneModel lIst = null;
		AutoritaEsternaModel lAut = null;

		// ==========================================================================
		// Controllo Validazione Fascicolo
		// ==========================================================================
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

		// ==========================================================================
		// Controllo Fascicolo definito
		// ==========================================================================
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
		} else if (flagPage.equals("D")) { // computo Misura Cautelare stesso Reato art. 657 c.p.p.
											// (Presofferto)
			lEveModRic.setCodMotivo("0121");
		}

		EventoModel lEveModDep = lCtrlEve.ExRicercaEventoNonRegistrato(lEveModRic);

		// ==========================================================================
		// Recupero le annotazioni legate all'evento
		// ==========================================================================
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

		/********* MisuraAlternativa e evento provvedimento ***************/
		// ==========================================================================
		// Verifico se presente una Misura Alternativa e recupero l'evento che la
		// ha resa esecutiva e i destinatari per la notifica.
		// Vanno passati alla form perchè sono i destinatari a cui andrà notificato
		// il computo.
		// Attenzione!! non è detto che la misura sia stata ancora eseguita da SIEP
		// potrebbe essere stata trasferita da SIUS ma non ancora eseguita
		//
		// ==========================================================================
		IMisuraAlternativa lCtrlMis = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisMod = lCtrlMis
				.ExRicercaMisuraAlternativaCorrenteByIdFascicolo(lIdFascicolo);
		if (lMisMod != null) {
			// EventoModel lEveOrd = lCtrlEve.ExRicercaEventoByKey(lMisMod.getEveIdEvento());
			try {
				EventoNotificaModel lEveNotMod = lCtrlEve.ExRicercaEventoNotificaByEveIdEvento(lMisMod
						.getEveIdEvento());
				this.setRequestAttribute("misuraalternativa", lMisMod);
				this.setRequestAttribute("eventonotifica", lEveNotMod);
				for (int i = 0; i < lEveNotMod.getNotifiche().length; i++) {
					if (lEveNotMod.getNotifiche()[i].getCSSA() != null) {
						lCssa = lEveNotMod.getNotifiche()[i].getCSSA();
						setRequestAttribute("lCssa", lCssa);
					}
					if (lEveNotMod.getNotifiche()[i].getIstDetIdIstitutoDetenzione() != null) {
						lIst = lEveNotMod.getNotifiche()[i].getIstitutoDetenzione();
						setRequestAttribute("lIstituto", lIst);
					}
					if (lEveNotMod.getNotifiche()[i].getAutoritaEsterna() != null
							&& lEveNotMod.getNotifiche()[i].getCodTipoNotifica().equals("N")) {
						lAut = lEveNotMod.getNotifiche()[i].getAutoritaEsterna();
						setRequestAttribute("lAutorita", lAut);
					}
				}
			} catch (F3BException e) {
				// devo verificare se viene rilanciata l'eccezione 'nessun elemento trovato'
				// in questo caso vuol dire che non è stata ancora eseguita la MA, manca
				// l'evento SIEP.
				if (e.getErrorCode() == F3BException.USER_MESSAGE) {
					//
				} else {
					throw e; // eccezione di altra natura, la rilancio
				}
			}

		}

		/*************************************************************************/
		// magistrato
		// ricerca magistrato
		if (lMisMod != null) {
			IMagistrato lMagComp = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel lMagMod = lMagComp.ExRicercaMagistratoByCod(lMisMod.getCodMagistrato());
			if (lMagMod != null)
				setRequestAttribute("magistrato", lMagMod);
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

		String lPage = new String(f3b.web.IWebConstants.ROOT_DIR
				+ "/files/siap/siep/calcolopena/LoadInserisciComputoCustodiaCautelare.jsp");

		return lPage; // restituisce la jsp di VIEW
	}

}